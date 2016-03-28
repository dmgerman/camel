begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|javax
operator|.
name|annotation
operator|.
name|processing
operator|.
name|AbstractProcessor
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
name|PackageElement
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
name|lang
operator|.
name|model
operator|.
name|element
operator|.
name|VariableElement
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
name|type
operator|.
name|TypeKind
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
name|util
operator|.
name|ElementFilter
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
name|util
operator|.
name|Elements
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|tools
operator|.
name|Diagnostic
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
name|IOHelper
operator|.
name|loadText
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
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|/**  * Abstract class for Camel apt plugins.  */
end_comment

begin_class
DECL|class|AbstractAnnotationProcessor
specifier|public
specifier|abstract
class|class
name|AbstractAnnotationProcessor
extends|extends
name|AbstractProcessor
block|{
DECL|method|findJavaDoc (Elements elementUtils, Element element, String fieldName, String name, TypeElement classElement, boolean builderPattern)
specifier|protected
name|String
name|findJavaDoc
parameter_list|(
name|Elements
name|elementUtils
parameter_list|,
name|Element
name|element
parameter_list|,
name|String
name|fieldName
parameter_list|,
name|String
name|name
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|boolean
name|builderPattern
parameter_list|)
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|element
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|answer
argument_list|)
condition|)
block|{
name|ExecutableElement
name|setter
init|=
name|findSetter
argument_list|(
name|fieldName
argument_list|,
name|classElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|setter
operator|!=
literal|null
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|setter
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
block|}
block|}
comment|// lets find the getter
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|ExecutableElement
name|getter
init|=
name|findGetter
argument_list|(
name|fieldName
argument_list|,
name|classElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|getter
operator|!=
literal|null
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|getter
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
block|}
block|}
block|}
comment|// lets try builder pattern
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|builderPattern
condition|)
block|{
name|List
argument_list|<
name|ExecutableElement
argument_list|>
name|methods
init|=
name|ElementFilter
operator|.
name|methodsIn
argument_list|(
name|classElement
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
comment|// lets try the builder pattern using annotation name (optional) as the method name
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// there may be builder pattern with no-parameter methods, such as more common for boolean types
comment|// so lets try those as well
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
comment|// lets try builder pattern using fieldName as the method name
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// there may be builder pattern with no-parameter methods, such as more common for boolean types
comment|// so lets try those as well
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|String
name|doc
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|method
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|answer
operator|=
name|doc
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|findSetter (String fieldName, TypeElement classElement)
specifier|protected
name|ExecutableElement
name|findSetter
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|TypeElement
name|classElement
parameter_list|)
block|{
name|String
name|setter
init|=
literal|"set"
operator|+
name|fieldName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|setter
operator|+=
name|fieldName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|//  lets find the setter
name|List
argument_list|<
name|ExecutableElement
argument_list|>
name|methods
init|=
name|ElementFilter
operator|.
name|methodsIn
argument_list|(
name|classElement
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|setter
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|getKind
argument_list|()
operator|.
name|equals
argument_list|(
name|TypeKind
operator|.
name|VOID
argument_list|)
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findGetter (String fieldName, TypeElement classElement)
specifier|protected
name|ExecutableElement
name|findGetter
parameter_list|(
name|String
name|fieldName
parameter_list|,
name|TypeElement
name|classElement
parameter_list|)
block|{
name|String
name|getter1
init|=
literal|"get"
operator|+
name|fieldName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|getter1
operator|+=
name|fieldName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
name|getter2
init|=
literal|"is"
operator|+
name|fieldName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
decl_stmt|;
if|if
condition|(
name|fieldName
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
name|getter2
operator|+=
name|fieldName
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|//  lets find the getter
name|List
argument_list|<
name|ExecutableElement
argument_list|>
name|methods
init|=
name|ElementFilter
operator|.
name|methodsIn
argument_list|(
name|classElement
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ExecutableElement
name|method
range|:
name|methods
control|)
block|{
name|String
name|methodName
init|=
name|method
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|getter1
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|||
name|getter2
operator|.
name|equals
argument_list|(
name|methodName
argument_list|)
operator|)
operator|&&
name|method
operator|.
name|getParameters
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
return|return
name|method
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findFieldElement (TypeElement classElement, String fieldName)
specifier|protected
name|VariableElement
name|findFieldElement
parameter_list|(
name|TypeElement
name|classElement
parameter_list|,
name|String
name|fieldName
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|fieldName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|VariableElement
argument_list|>
name|fields
init|=
name|ElementFilter
operator|.
name|fieldsIn
argument_list|(
name|classElement
operator|.
name|getEnclosedElements
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|VariableElement
name|field
range|:
name|fields
control|)
block|{
if|if
condition|(
name|fieldName
operator|.
name|equals
argument_list|(
name|field
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
name|field
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|findTypeElement (RoundEnvironment roundEnv, String className)
specifier|protected
name|TypeElement
name|findTypeElement
parameter_list|(
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|className
argument_list|)
operator|||
literal|"java.lang.Object"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Set
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|rootElements
init|=
name|roundEnv
operator|.
name|getRootElements
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|rootElement
range|:
name|rootElements
control|)
block|{
if|if
condition|(
name|rootElement
operator|instanceof
name|TypeElement
condition|)
block|{
name|TypeElement
name|typeElement
init|=
operator|(
name|TypeElement
operator|)
name|rootElement
decl_stmt|;
name|String
name|aRootName
init|=
name|canonicalClassName
argument_list|(
name|typeElement
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|aRootName
argument_list|)
condition|)
block|{
return|return
name|typeElement
return|;
block|}
block|}
block|}
comment|// fallback using package name
name|Elements
name|elementUtils
init|=
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|className
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|String
name|packageName
init|=
name|className
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|PackageElement
name|pe
init|=
name|elementUtils
operator|.
name|getPackageElement
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|enclosedElements
init|=
name|getEnclosedElements
argument_list|(
name|pe
argument_list|)
decl_stmt|;
for|for
control|(
name|Element
name|rootElement
range|:
name|enclosedElements
control|)
block|{
if|if
condition|(
name|rootElement
operator|instanceof
name|TypeElement
condition|)
block|{
name|TypeElement
name|typeElement
init|=
operator|(
name|TypeElement
operator|)
name|rootElement
decl_stmt|;
name|String
name|aRootName
init|=
name|canonicalClassName
argument_list|(
name|typeElement
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|.
name|equals
argument_list|(
name|aRootName
argument_list|)
condition|)
block|{
return|return
name|typeElement
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getEnclosedElements (PackageElement pe)
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|getEnclosedElements
parameter_list|(
name|PackageElement
name|pe
parameter_list|)
block|{
comment|// some components like hadoop/spark has bad classes that causes javac scanning issues
try|try
block|{
return|return
name|pe
operator|.
name|getEnclosedElements
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
return|return
name|Collections
operator|.
name|EMPTY_LIST
return|;
block|}
DECL|method|findTypeElementChildren (RoundEnvironment roundEnv, Set<TypeElement> found, String superClassName)
specifier|protected
name|void
name|findTypeElementChildren
parameter_list|(
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|Set
argument_list|<
name|TypeElement
argument_list|>
name|found
parameter_list|,
name|String
name|superClassName
parameter_list|)
block|{
name|Elements
name|elementUtils
init|=
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|superClassName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
name|String
name|packageName
init|=
name|superClassName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|PackageElement
name|pe
init|=
name|elementUtils
operator|.
name|getPackageElement
argument_list|(
name|packageName
argument_list|)
decl_stmt|;
if|if
condition|(
name|pe
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|?
extends|extends
name|Element
argument_list|>
name|enclosedElements
init|=
name|pe
operator|.
name|getEnclosedElements
argument_list|()
decl_stmt|;
for|for
control|(
name|Element
name|rootElement
range|:
name|enclosedElements
control|)
block|{
if|if
condition|(
name|rootElement
operator|instanceof
name|TypeElement
condition|)
block|{
name|TypeElement
name|typeElement
init|=
operator|(
name|TypeElement
operator|)
name|rootElement
decl_stmt|;
name|String
name|aSuperClassName
init|=
name|canonicalClassName
argument_list|(
name|typeElement
operator|.
name|getSuperclass
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|superClassName
operator|.
name|equals
argument_list|(
name|aSuperClassName
argument_list|)
condition|)
block|{
name|found
operator|.
name|add
argument_list|(
name|typeElement
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
DECL|method|hasSuperClass (RoundEnvironment roundEnv, TypeElement classElement, String superClassName)
specifier|protected
name|boolean
name|hasSuperClass
parameter_list|(
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|String
name|superClassName
parameter_list|)
block|{
name|String
name|aRootName
init|=
name|canonicalClassName
argument_list|(
name|classElement
operator|.
name|getQualifiedName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
comment|// do not check the classes from JDK itself
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|aRootName
argument_list|)
operator|||
name|aRootName
operator|.
name|startsWith
argument_list|(
literal|"java."
argument_list|)
operator|||
name|aRootName
operator|.
name|startsWith
argument_list|(
literal|"javax."
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|String
name|aSuperClassName
init|=
name|canonicalClassName
argument_list|(
name|classElement
operator|.
name|getSuperclass
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|superClassName
operator|.
name|equals
argument_list|(
name|aSuperClassName
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|TypeElement
name|aSuperClass
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|aSuperClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|aSuperClass
operator|!=
literal|null
condition|)
block|{
return|return
name|hasSuperClass
argument_list|(
name|roundEnv
argument_list|,
name|aSuperClass
argument_list|,
name|superClassName
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Helper method to produce class output text file using the given handler      */
DECL|method|processFile (String packageName, String fileName, Func1<PrintWriter, Void> handler)
specifier|protected
name|void
name|processFile
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|fileName
parameter_list|,
name|Func1
argument_list|<
name|PrintWriter
argument_list|,
name|Void
argument_list|>
name|handler
parameter_list|)
block|{
name|PrintWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Writer
name|out
decl_stmt|;
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
decl_stmt|;
try|try
block|{
name|resource
operator|=
name|filer
operator|.
name|getResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
name|packageName
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|resource
operator|=
name|filer
operator|.
name|createResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
name|packageName
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
name|URI
name|uri
init|=
name|resource
operator|.
name|toUri
argument_list|()
decl_stmt|;
name|File
name|file
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|file
operator|=
operator|new
name|File
argument_list|(
name|uri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|warning
argument_list|(
literal|"Could not convert output directory resource URI to a file "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|file
operator|==
literal|null
condition|)
block|{
name|warning
argument_list|(
literal|"No class output directory could be found!"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|out
operator|=
operator|new
name|FileWriter
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|writer
operator|=
operator|new
name|PrintWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|handler
operator|.
name|call
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|log (String message)
specifier|protected
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|NOTE
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|warning (String message)
specifier|protected
name|void
name|warning
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|WARNING
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|error (String message)
specifier|protected
name|void
name|error
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|ERROR
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|log (Throwable e)
specifier|protected
name|void
name|log
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|ERROR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|writer
init|=
operator|new
name|PrintWriter
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|processingEnv
operator|.
name|getMessager
argument_list|()
operator|.
name|printMessage
argument_list|(
name|Diagnostic
operator|.
name|Kind
operator|.
name|ERROR
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|loadResource (String packageName, String fileName)
specifier|protected
name|String
name|loadResource
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|fileName
parameter_list|)
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
decl_stmt|;
try|try
block|{
name|resource
operator|=
name|filer
operator|.
name|getResource
argument_list|(
name|StandardLocation
operator|.
name|CLASS_OUTPUT
argument_list|,
literal|""
argument_list|,
name|packageName
operator|+
literal|"/"
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
return|return
literal|"Crap"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
return|;
block|}
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|InputStream
name|is
init|=
name|resource
operator|.
name|openInputStream
argument_list|()
decl_stmt|;
return|return
name|loadText
argument_list|(
name|is
argument_list|,
literal|true
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|warning
argument_list|(
literal|"Could not load file"
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|dumpExceptionToErrorFile (String fileName, String message, Throwable e)
specifier|protected
name|void
name|dumpExceptionToErrorFile
parameter_list|(
name|String
name|fileName
parameter_list|,
name|String
name|message
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|fileName
argument_list|)
decl_stmt|;
try|try
block|{
name|FileOutputStream
name|fos
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|fos
operator|.
name|write
argument_list|(
name|sw
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
name|sw
operator|.
name|close
argument_list|()
expr_stmt|;
name|fos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
end_class

end_unit

