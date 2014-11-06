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
name|LinkedHashSet
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
name|annotation
operator|.
name|processing
operator|.
name|SupportedSourceVersion
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
name|SourceVersion
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
name|MirroredTypeException
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
name|TypeMirror
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParams
import|;
end_import

begin_import
import|import
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
name|util
operator|.
name|Func1
import|;
end_import

begin_import
import|import
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
name|util
operator|.
name|Strings
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
name|util
operator|.
name|Strings
operator|.
name|canonicalClassName
import|;
end_import

begin_comment
comment|/**  * Processes all Camel {@link UriEndpoint}s and generate json schema and html documentation for the endpoint/component.  */
end_comment

begin_class
annotation|@
name|SupportedAnnotationTypes
argument_list|(
block|{
literal|"org.apache.camel.spi.*"
block|}
argument_list|)
annotation|@
name|SupportedSourceVersion
argument_list|(
name|SourceVersion
operator|.
name|RELEASE_7
argument_list|)
DECL|class|EndpointAnnotationProcessor
specifier|public
class|class
name|EndpointAnnotationProcessor
extends|extends
name|AbstractProcessor
block|{
DECL|method|process (Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv)
specifier|public
name|boolean
name|process
parameter_list|(
name|Set
argument_list|<
name|?
extends|extends
name|TypeElement
argument_list|>
name|annotations
parameter_list|,
specifier|final
name|RoundEnvironment
name|roundEnv
parameter_list|)
block|{
if|if
condition|(
name|roundEnv
operator|.
name|processingOver
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
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
name|UriEndpoint
operator|.
name|class
argument_list|)
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
name|processEndpointClass
argument_list|(
name|roundEnv
argument_list|,
operator|(
name|TypeElement
operator|)
name|element
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|processEndpointClass (final RoundEnvironment roundEnv, final TypeElement classElement)
specifier|protected
name|void
name|processEndpointClass
parameter_list|(
specifier|final
name|RoundEnvironment
name|roundEnv
parameter_list|,
specifier|final
name|TypeElement
name|classElement
parameter_list|)
block|{
specifier|final
name|UriEndpoint
name|uriEndpoint
init|=
name|classElement
operator|.
name|getAnnotation
argument_list|(
name|UriEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|uriEndpoint
operator|!=
literal|null
condition|)
block|{
name|String
name|scheme
init|=
name|uriEndpoint
operator|.
name|scheme
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|String
name|name
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
name|String
name|packageName
init|=
name|name
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|name
operator|.
name|lastIndexOf
argument_list|(
literal|"."
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|scheme
operator|+
literal|".html"
decl_stmt|;
name|Func1
argument_list|<
name|PrintWriter
argument_list|,
name|Void
argument_list|>
name|handler
init|=
operator|new
name|Func1
argument_list|<
name|PrintWriter
argument_list|,
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|call
parameter_list|(
name|PrintWriter
name|writer
parameter_list|)
block|{
name|writeHtmlDocumentation
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|classElement
argument_list|,
name|uriEndpoint
argument_list|)
expr_stmt|;
name|writeJSonSchemeDocumentation
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|classElement
argument_list|,
name|uriEndpoint
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
name|processFile
argument_list|(
name|packageName
argument_list|,
name|scheme
argument_list|,
name|fileName
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|writeJSonSchemeDocumentation (PrintWriter writer, RoundEnvironment roundEnv, TypeElement classElement, UriEndpoint uriEndpoint)
specifier|protected
name|void
name|writeJSonSchemeDocumentation
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|UriEndpoint
name|uriEndpoint
parameter_list|)
block|{
comment|// todo
block|}
DECL|method|writeHtmlDocumentation (PrintWriter writer, RoundEnvironment roundEnv, TypeElement classElement, UriEndpoint uriEndpoint)
specifier|protected
name|void
name|writeHtmlDocumentation
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|UriEndpoint
name|uriEndpoint
parameter_list|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<html>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<header>"
argument_list|)
expr_stmt|;
name|String
name|scheme
init|=
name|uriEndpoint
operator|.
name|scheme
argument_list|()
decl_stmt|;
name|String
name|title
init|=
name|scheme
operator|+
literal|" endpoint"
decl_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<title>"
operator|+
name|title
operator|+
literal|"</title>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</header>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<body>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<h1>"
operator|+
name|title
operator|+
literal|"</h1>"
argument_list|)
expr_stmt|;
name|showDocumentationAndFieldInjections
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|classElement
argument_list|,
literal|""
argument_list|)
expr_stmt|;
comment|// This code is not my fault, it seems to honestly be the hacky way to find a class name in APT :)
name|TypeMirror
name|consumerType
init|=
literal|null
decl_stmt|;
try|try
block|{
name|uriEndpoint
operator|.
name|consumerClass
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MirroredTypeException
name|mte
parameter_list|)
block|{
name|consumerType
operator|=
name|mte
operator|.
name|getTypeMirror
argument_list|()
expr_stmt|;
block|}
name|boolean
name|found
init|=
literal|false
decl_stmt|;
name|String
name|consumerClassName
init|=
literal|null
decl_stmt|;
name|String
name|consumerPrefix
init|=
name|Strings
operator|.
name|getOrElse
argument_list|(
name|uriEndpoint
operator|.
name|consumerPrefix
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumerType
operator|!=
literal|null
condition|)
block|{
name|consumerClassName
operator|=
name|consumerType
operator|.
name|toString
argument_list|()
expr_stmt|;
name|TypeElement
name|consumerElement
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|consumerClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumerElement
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<h2>"
operator|+
name|scheme
operator|+
literal|" consumer"
operator|+
literal|"</h2>"
argument_list|)
expr_stmt|;
name|showDocumentationAndFieldInjections
argument_list|(
name|writer
argument_list|,
name|roundEnv
argument_list|,
name|consumerElement
argument_list|,
name|consumerPrefix
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
operator|&&
name|consumerClassName
operator|!=
literal|null
condition|)
block|{
name|warning
argument_list|(
literal|"APT could not find consumer class "
operator|+
name|consumerClassName
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"</body>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</html>"
argument_list|)
expr_stmt|;
block|}
DECL|method|showDocumentationAndFieldInjections (PrintWriter writer, RoundEnvironment roundEnv, TypeElement classElement, String prefix)
specifier|protected
name|void
name|showDocumentationAndFieldInjections
parameter_list|(
name|PrintWriter
name|writer
parameter_list|,
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
name|String
name|classDoc
init|=
name|processingEnv
operator|.
name|getElementUtils
argument_list|()
operator|.
name|getDocComment
argument_list|(
name|classElement
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|classDoc
argument_list|)
condition|)
block|{
comment|// remove dodgy @version that we may have in class javadoc
name|classDoc
operator|=
name|classDoc
operator|.
name|replaceFirst
argument_list|(
literal|"\\@version"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|classDoc
operator|=
name|classDoc
operator|.
name|trim
argument_list|()
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<p>"
operator|+
name|classDoc
operator|+
literal|"</p>"
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|EndpointOption
argument_list|>
name|endpointOptions
init|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|findClassProperties
argument_list|(
name|roundEnv
argument_list|,
name|endpointOptions
argument_list|,
name|classElement
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|endpointOptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<table class='table'>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<th>Name</th>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<th>Type</th>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<th>Description</th>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
for|for
control|(
name|EndpointOption
name|option
range|:
name|endpointOptions
control|)
block|{
name|writer
operator|.
name|println
argument_list|(
literal|"<tr>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|option
operator|.
name|getName
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|option
operator|.
name|getType
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"<td>"
operator|+
name|option
operator|.
name|getDocumentation
argument_list|()
operator|+
literal|"</td>"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|println
argument_list|(
literal|"</tr>"
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|println
argument_list|(
literal|"</table>"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|findClassProperties (RoundEnvironment roundEnv, Set<EndpointOption> endpointOptions, TypeElement classElement, String prefix)
specifier|protected
name|void
name|findClassProperties
parameter_list|(
name|RoundEnvironment
name|roundEnv
parameter_list|,
name|Set
argument_list|<
name|EndpointOption
argument_list|>
name|endpointOptions
parameter_list|,
name|TypeElement
name|classElement
parameter_list|,
name|String
name|prefix
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
while|while
condition|(
literal|true
condition|)
block|{
name|List
argument_list|<
name|VariableElement
argument_list|>
name|fieldElements
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
if|if
condition|(
name|fieldElements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
break|break;
block|}
for|for
control|(
name|VariableElement
name|fieldElement
range|:
name|fieldElements
control|)
block|{
name|UriParam
name|param
init|=
name|fieldElement
operator|.
name|getAnnotation
argument_list|(
name|UriParam
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fieldName
init|=
name|fieldElement
operator|.
name|getSimpleName
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|param
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|param
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|fieldName
expr_stmt|;
block|}
name|name
operator|=
name|prefix
operator|+
name|name
expr_stmt|;
comment|// if the field type is a nested parameter then iterate through its fields
name|TypeMirror
name|fieldType
init|=
name|fieldElement
operator|.
name|asType
argument_list|()
decl_stmt|;
name|String
name|fieldTypeName
init|=
name|fieldType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|TypeElement
name|fieldTypeElement
init|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|fieldTypeName
argument_list|)
decl_stmt|;
name|UriParams
name|fieldParams
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|fieldTypeElement
operator|!=
literal|null
condition|)
block|{
name|fieldParams
operator|=
name|fieldTypeElement
operator|.
name|getAnnotation
argument_list|(
name|UriParams
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fieldParams
operator|!=
literal|null
condition|)
block|{
name|String
name|nestedPrefix
init|=
name|prefix
decl_stmt|;
name|String
name|extraPrefix
init|=
name|fieldParams
operator|.
name|prefix
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|extraPrefix
argument_list|)
condition|)
block|{
name|nestedPrefix
operator|+=
name|extraPrefix
expr_stmt|;
block|}
name|findClassProperties
argument_list|(
name|roundEnv
argument_list|,
name|endpointOptions
argument_list|,
name|fieldTypeElement
argument_list|,
name|nestedPrefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|docComment
init|=
name|elementUtils
operator|.
name|getDocComment
argument_list|(
name|fieldElement
argument_list|)
decl_stmt|;
if|if
condition|(
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|docComment
argument_list|)
condition|)
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
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|doc
argument_list|)
condition|)
block|{
name|docComment
operator|=
name|doc
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
if|if
condition|(
name|docComment
operator|==
literal|null
condition|)
block|{
name|docComment
operator|=
literal|""
expr_stmt|;
block|}
name|EndpointOption
name|option
init|=
operator|new
name|EndpointOption
argument_list|(
name|name
argument_list|,
name|fieldTypeName
argument_list|,
name|docComment
operator|.
name|trim
argument_list|()
argument_list|)
decl_stmt|;
name|endpointOptions
operator|.
name|add
argument_list|(
name|option
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|TypeElement
name|baseTypeElement
init|=
literal|null
decl_stmt|;
name|TypeMirror
name|superclass
init|=
name|classElement
operator|.
name|getSuperclass
argument_list|()
decl_stmt|;
if|if
condition|(
name|superclass
operator|!=
literal|null
condition|)
block|{
name|String
name|superClassName
init|=
name|canonicalClassName
argument_list|(
name|superclass
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|baseTypeElement
operator|=
name|findTypeElement
argument_list|(
name|roundEnv
argument_list|,
name|superClassName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|baseTypeElement
operator|!=
literal|null
condition|)
block|{
name|classElement
operator|=
name|baseTypeElement
expr_stmt|;
block|}
else|else
block|{
break|break;
block|}
block|}
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
operator|!
name|Strings
operator|.
name|isNullOrEmpty
argument_list|(
name|className
argument_list|)
operator|&&
operator|!
literal|"java.lang.Object"
operator|.
name|equals
argument_list|(
name|className
argument_list|)
condition|)
block|{
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
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Helper method to produce class output text file using the given handler      */
DECL|method|processFile (String packageName, String scheme, String fileName, Func1<PrintWriter, Void> handler)
specifier|protected
name|void
name|processFile
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|scheme
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
DECL|class|EndpointOption
specifier|private
specifier|static
specifier|final
class|class
name|EndpointOption
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|documentation
specifier|private
name|String
name|documentation
decl_stmt|;
DECL|method|EndpointOption (String name, String type, String documentation)
specifier|private
name|EndpointOption
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|documentation
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|documentation
operator|=
name|documentation
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|getDocumentation ()
specifier|public
name|String
name|getDocumentation
parameter_list|()
block|{
return|return
name|documentation
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|()
operator|!=
name|o
operator|.
name|getClass
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|EndpointOption
name|that
init|=
operator|(
name|EndpointOption
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|name
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

