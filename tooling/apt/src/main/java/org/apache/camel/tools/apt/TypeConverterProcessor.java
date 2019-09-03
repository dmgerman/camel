begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.tools.apt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|Filer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|RoundEnvironment
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|SupportedAnnotationTypes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|AnnotationMirror
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|AnnotationValue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|ExecutableElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|TypeElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|FileObject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|StandardLocation
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|tools
operator|.
name|apt
operator|.
name|helper
operator|.
name|Strings
operator|.
name|canonicalClassName
import|;
end_import

begin_class
annotation|@
name|SupportedAnnotationTypes
argument_list|(
block|{
literal|"org.apache.camel.Converter"
block|}
argument_list|)
DECL|class|TypeConverterProcessor
specifier|public
class|class
name|TypeConverterProcessor
extends|extends
name|AbstractCamelAnnotationProcessor
block|{
DECL|method|acceptClass (Element element)
name|boolean
name|acceptClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
comment|// we accept any class that is not using @Converter(loader = true)
return|return
operator|!
name|isLoaderEnabled
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doProcess (Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Set
argument_list|<
name|?
extends|extends
name|TypeElement
argument_list|>
name|annotations
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|)
throws|throws
name|Exception
block|{
name|TypeElement
name|converterAnnotationType
init|=
name|this
operator|.
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
operator|.
name|getTypeElement
argument_list|(
literal|"org.apache.camel.Converter"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|elements
init|=
name|roundEnv
operator|.
name|getElementsAnnotatedWith
argument_list|(
name|converterAnnotationType
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Element
argument_list|>
name|converterClasses
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|element
range|:
name|elements
control|)
block|{
if|if
condition|(
name|element
operator|instanceof
name|TypeElement
condition|)
block|{
name|TypeElement
name|te
init|=
operator|(
name|TypeElement
operator|)
name|element
decl_stmt|;
comment|// we only support top-level classes (not inner classes)
if|if
condition|(
operator|!
name|te
operator|.
name|getNestingKind
argument_list|()
operator|.
name|isNested
argument_list|()
operator|&&
name|acceptClass
argument_list|(
name|te
argument_list|)
condition|)
block|{
specifier|final
name|String
name|javaTypeName
init|=
name|canonicalClassName
argument_list|(
name|te
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|converterClasses
operator|.
name|put
argument_list|(
name|javaTypeName
argument_list|,
name|element
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// skip all converter classes from core as we just want to use the optimized TypeConverterLoader files
if|if
condition|(
operator|!
name|converterClasses
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|converterClasses
operator|.
name|containsKey
argument_list|(
literal|"org.apache.camel.converter.IOConverter"
argument_list|)
operator|&&
operator|!
name|converterClasses
operator|.
name|containsKey
argument_list|(
literal|"org.apache.camel.converter.jaxp.DomConverter"
argument_list|)
operator|&&
operator|!
name|converterClasses
operator|.
name|containsKey
argument_list|(
literal|"org.apache.camel.converter.jaxp.XmlConverter"
argument_list|)
operator|&&
operator|!
name|converterClasses
operator|.
name|containsKey
argument_list|(
literal|"org.apache.camel.util.xml.StreamSourceConverter"
argument_list|)
operator|&&
operator|!
name|converterClasses
operator|.
name|containsKey
argument_list|(
literal|"org.apache.camel.converter.stream.StreamCacheConverter"
argument_list|)
condition|)
block|{
name|Filer
name|filer
init|=
name|processingEnv
operator|.
name|getFiler
argument_list|()
decl_stmt|;
name|FileObject
name|resource
init|=
name|filer
operator|.
name|createResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
literal|""
argument_list|,
literal|"META-INF/services/org/apache/camel/TypeConverter"
argument_list|,
name|converterClasses
operator|.
name|values
argument_list|()
operator|.
name|toArray
argument_list|(
operator|new
name|Element
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
try|try
init|(
name|Writer
name|w
init|=
name|resource
operator|.
name|openWriter
argument_list|()
init|)
block|{
name|w
operator|.
name|append
argument_list|(
literal|"# Generated by camel annotation processor\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|s
range|:
name|converterClasses
operator|.
name|keySet
argument_list|()
control|)
block|{
name|w
operator|.
name|append
argument_list|(
name|s
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|isLoaderEnabled (Element element)
specifier|private
specifier|static
name|boolean
name|isLoaderEnabled
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
for|for
control|(
name|AnnotationMirror
name|ann
range|:
name|element
operator|.
name|getAnnotationMirrors
argument_list|()
control|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
extends|extends
name|ExecutableElement
argument_list|,
name|?
extends|extends
name|AnnotationValue
argument_list|>
name|entry
range|:
name|ann
operator|.
name|getElementValues
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
literal|"generateLoader"
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|Boolean
operator|)
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

