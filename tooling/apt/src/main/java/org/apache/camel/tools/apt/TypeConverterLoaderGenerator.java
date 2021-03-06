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
name|StringJoiner
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

begin_class
annotation|@
name|SupportedAnnotationTypes
argument_list|(
block|{
literal|"org.apache.camel.Converter"
block|}
argument_list|)
DECL|class|TypeConverterLoaderGenerator
specifier|public
class|class
name|TypeConverterLoaderGenerator
extends|extends
name|AbstractTypeConverterGenerator
block|{
annotation|@
name|Override
DECL|method|acceptClass (Element element)
name|boolean
name|acceptClass
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
return|return
name|isLoaderEnabled
argument_list|(
name|element
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|convertersKey (String currentClass)
name|String
name|convertersKey
parameter_list|(
name|String
name|currentClass
parameter_list|)
block|{
return|return
name|currentClass
return|;
block|}
annotation|@
name|Override
DECL|method|writeConverters (Map<String, ClassConverters> converters)
name|void
name|writeConverters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ClassConverters
argument_list|>
name|converters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// now write all the converters
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ClassConverters
argument_list|>
name|entry
range|:
name|converters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|ClassConverters
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|writeConverters
argument_list|(
name|key
argument_list|,
literal|"Loader"
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|writeConverterLoaderMetaInfo
argument_list|(
name|converters
argument_list|)
expr_stmt|;
block|}
DECL|method|writeConverterLoaderMetaInfo (Map<String, ClassConverters> converters)
specifier|private
name|void
name|writeConverterLoaderMetaInfo
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ClassConverters
argument_list|>
name|converters
parameter_list|)
throws|throws
name|Exception
block|{
name|StringJoiner
name|sj
init|=
operator|new
name|StringJoiner
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ClassConverters
argument_list|>
name|entry
range|:
name|converters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|ClassConverters
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|sj
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|sj
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|FileObject
name|fo
init|=
name|processingEnv
operator|.
name|getFiler
argument_list|()
operator|.
name|createResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
literal|""
argument_list|,
literal|"META-INF/services/org/apache/camel/TypeConverterLoader"
argument_list|)
decl_stmt|;
try|try
init|(
name|Writer
name|writer
init|=
name|fo
operator|.
name|openWriter
argument_list|()
init|)
block|{
name|writer
operator|.
name|append
argument_list|(
literal|"# Generated by camel annotation processor\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|fqn
range|:
name|sj
operator|.
name|toString
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
control|)
block|{
name|writer
operator|.
name|append
argument_list|(
name|fqn
argument_list|)
operator|.
name|append
argument_list|(
literal|"Loader\n"
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

