begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.generator.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|generator
operator|.
name|swagger
package|;
end_package

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
name|time
operator|.
name|Instant
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|Modifier
import|;
end_import

begin_import
import|import
name|com
operator|.
name|squareup
operator|.
name|javapoet
operator|.
name|AnnotationSpec
import|;
end_import

begin_import
import|import
name|com
operator|.
name|squareup
operator|.
name|javapoet
operator|.
name|ClassName
import|;
end_import

begin_import
import|import
name|com
operator|.
name|squareup
operator|.
name|javapoet
operator|.
name|JavaFile
import|;
end_import

begin_import
import|import
name|com
operator|.
name|squareup
operator|.
name|javapoet
operator|.
name|MethodSpec
import|;
end_import

begin_import
import|import
name|com
operator|.
name|squareup
operator|.
name|javapoet
operator|.
name|TypeSpec
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Info
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
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
name|builder
operator|.
name|RouteBuilder
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
name|util
operator|.
name|ObjectHelper
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
name|util
operator|.
name|StringHelper
operator|.
name|notEmpty
import|;
end_import

begin_comment
comment|/**  * Generates Java source code  */
end_comment

begin_class
DECL|class|RestDslSourceCodeGenerator
specifier|public
specifier|abstract
class|class
name|RestDslSourceCodeGenerator
parameter_list|<
name|T
parameter_list|>
extends|extends
name|RestDslGenerator
argument_list|<
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
DECL|field|DEFAULT_CLASS_NAME
specifier|static
specifier|final
name|String
name|DEFAULT_CLASS_NAME
init|=
literal|"RestDslRoute"
decl_stmt|;
DECL|field|DEFAULT_PACKAGE_NAME
specifier|static
specifier|final
name|String
name|DEFAULT_PACKAGE_NAME
init|=
literal|"rest.dsl.generated"
decl_stmt|;
DECL|field|DEFAULT_INDENT
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_INDENT
init|=
literal|"    "
decl_stmt|;
DECL|field|classNameGenerator
specifier|private
name|Function
argument_list|<
name|Swagger
argument_list|,
name|String
argument_list|>
name|classNameGenerator
init|=
name|RestDslSourceCodeGenerator
operator|::
name|generateClassName
decl_stmt|;
DECL|field|generated
specifier|private
name|Instant
name|generated
init|=
name|Instant
operator|.
name|now
argument_list|()
decl_stmt|;
DECL|field|indent
specifier|private
name|String
name|indent
init|=
name|DEFAULT_INDENT
decl_stmt|;
DECL|field|packageNameGenerator
specifier|private
name|Function
argument_list|<
name|Swagger
argument_list|,
name|String
argument_list|>
name|packageNameGenerator
init|=
name|RestDslSourceCodeGenerator
operator|::
name|generatePackageName
decl_stmt|;
DECL|field|sourceCodeTimestamps
specifier|private
name|boolean
name|sourceCodeTimestamps
decl_stmt|;
DECL|method|RestDslSourceCodeGenerator (final Swagger swagger)
name|RestDslSourceCodeGenerator
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
name|super
argument_list|(
name|swagger
argument_list|)
expr_stmt|;
block|}
DECL|method|generate (T destination)
specifier|public
specifier|abstract
name|void
name|generate
parameter_list|(
name|T
name|destination
parameter_list|)
throws|throws
name|IOException
function_decl|;
DECL|method|withClassName (final String className)
specifier|public
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withClassName
parameter_list|(
specifier|final
name|String
name|className
parameter_list|)
block|{
name|notEmpty
argument_list|(
name|className
argument_list|,
literal|"className"
argument_list|)
expr_stmt|;
name|this
operator|.
name|classNameGenerator
operator|=
parameter_list|(
name|s
parameter_list|)
lambda|->
name|className
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withIndent (final String indent)
specifier|public
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withIndent
parameter_list|(
specifier|final
name|String
name|indent
parameter_list|)
block|{
name|this
operator|.
name|indent
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|indent
argument_list|,
literal|"indent"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withoutSourceCodeTimestamps ()
specifier|public
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withoutSourceCodeTimestamps
parameter_list|()
block|{
name|sourceCodeTimestamps
operator|=
literal|false
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withPackageName (final String packageName)
specifier|public
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withPackageName
parameter_list|(
specifier|final
name|String
name|packageName
parameter_list|)
block|{
name|notEmpty
argument_list|(
name|packageName
argument_list|,
literal|"packageName"
argument_list|)
expr_stmt|;
name|this
operator|.
name|packageNameGenerator
operator|=
parameter_list|(
name|s
parameter_list|)
lambda|->
name|packageName
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|withSourceCodeTimestamps ()
specifier|public
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withSourceCodeTimestamps
parameter_list|()
block|{
name|sourceCodeTimestamps
operator|=
literal|true
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|generateConfigureMethod (final Swagger swagger)
name|MethodSpec
name|generateConfigureMethod
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
specifier|final
name|MethodSpec
operator|.
name|Builder
name|configure
init|=
name|MethodSpec
operator|.
name|methodBuilder
argument_list|(
literal|"configure"
argument_list|)
operator|.
name|addModifiers
argument_list|(
name|Modifier
operator|.
name|PUBLIC
argument_list|)
operator|.
name|returns
argument_list|(
name|void
operator|.
name|class
argument_list|)
operator|.
name|addJavadoc
argument_list|(
literal|"Defines Apache Camel routes using REST DSL fluent API.\n"
argument_list|)
decl_stmt|;
specifier|final
name|MethodBodySourceCodeEmitter
name|emitter
init|=
operator|new
name|MethodBodySourceCodeEmitter
argument_list|(
name|configure
argument_list|)
decl_stmt|;
if|if
condition|(
name|restComponent
operator|!=
literal|null
condition|)
block|{
name|configure
operator|.
name|addCode
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|configure
operator|.
name|addCode
argument_list|(
literal|"restConfiguration().component(\""
operator|+
name|restComponent
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|restContextPath
operator|!=
literal|null
condition|)
block|{
name|configure
operator|.
name|addCode
argument_list|(
literal|".contextPath(\""
operator|+
name|restContextPath
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|apiContextPath
argument_list|)
condition|)
block|{
name|configure
operator|.
name|addCode
argument_list|(
literal|".apiContextPath(\""
operator|+
name|apiContextPath
operator|+
literal|"\")"
argument_list|)
expr_stmt|;
block|}
name|configure
operator|.
name|addCode
argument_list|(
literal|";\n\n"
argument_list|)
expr_stmt|;
block|}
specifier|final
name|PathVisitor
argument_list|<
name|MethodSpec
argument_list|>
name|restDslStatement
init|=
operator|new
name|PathVisitor
argument_list|<>
argument_list|(
name|swagger
operator|.
name|getBasePath
argument_list|()
argument_list|,
name|emitter
argument_list|,
name|filter
argument_list|,
name|destinationGenerator
argument_list|()
argument_list|)
decl_stmt|;
name|swagger
operator|.
name|getPaths
argument_list|()
operator|.
name|forEach
argument_list|(
name|restDslStatement
operator|::
name|visit
argument_list|)
expr_stmt|;
return|return
name|emitter
operator|.
name|result
argument_list|()
return|;
block|}
DECL|method|generated ()
name|Instant
name|generated
parameter_list|()
block|{
return|return
name|generated
return|;
block|}
DECL|method|generateSourceCode ()
name|JavaFile
name|generateSourceCode
parameter_list|()
block|{
specifier|final
name|MethodSpec
name|methodSpec
init|=
name|generateConfigureMethod
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
specifier|final
name|String
name|classNameToUse
init|=
name|classNameGenerator
operator|.
name|apply
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
specifier|final
name|AnnotationSpec
operator|.
name|Builder
name|generatedAnnotation
init|=
name|AnnotationSpec
operator|.
name|builder
argument_list|(
name|Generated
operator|.
name|class
argument_list|)
operator|.
name|addMember
argument_list|(
literal|"value"
argument_list|,
literal|"$S"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sourceCodeTimestamps
condition|)
block|{
name|generatedAnnotation
operator|.
name|addMember
argument_list|(
literal|"date"
argument_list|,
literal|"$S"
argument_list|,
name|generated
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TypeSpec
operator|.
name|Builder
name|builder
init|=
name|TypeSpec
operator|.
name|classBuilder
argument_list|(
name|classNameToUse
argument_list|)
operator|.
name|superclass
argument_list|(
name|RouteBuilder
operator|.
name|class
argument_list|)
operator|.
name|addModifiers
argument_list|(
name|Modifier
operator|.
name|PUBLIC
argument_list|,
name|Modifier
operator|.
name|FINAL
argument_list|)
operator|.
name|addMethod
argument_list|(
name|methodSpec
argument_list|)
operator|.
name|addAnnotation
argument_list|(
name|generatedAnnotation
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|addJavadoc
argument_list|(
literal|"Generated from Swagger specification by Camel REST DSL generator.\n"
argument_list|)
decl_stmt|;
if|if
condition|(
name|springComponent
condition|)
block|{
specifier|final
name|AnnotationSpec
operator|.
name|Builder
name|springAnnotation
init|=
name|AnnotationSpec
operator|.
name|builder
argument_list|(
name|ClassName
operator|.
name|bestGuess
argument_list|(
literal|"org.springframework.stereotype.Component"
argument_list|)
argument_list|)
decl_stmt|;
name|builder
operator|.
name|addAnnotation
argument_list|(
name|springAnnotation
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TypeSpec
name|generatedRouteBuilder
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|String
name|packageNameToUse
init|=
name|packageNameGenerator
operator|.
name|apply
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
return|return
name|JavaFile
operator|.
name|builder
argument_list|(
name|packageNameToUse
argument_list|,
name|generatedRouteBuilder
argument_list|)
operator|.
name|indent
argument_list|(
name|indent
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|withGeneratedTime (final Instant generated)
name|RestDslSourceCodeGenerator
argument_list|<
name|T
argument_list|>
name|withGeneratedTime
parameter_list|(
specifier|final
name|Instant
name|generated
parameter_list|)
block|{
name|this
operator|.
name|generated
operator|=
name|generated
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|generateClassName (final Swagger swagger)
specifier|static
name|String
name|generateClassName
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
specifier|final
name|Info
name|info
init|=
name|swagger
operator|.
name|getInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|info
operator|==
literal|null
condition|)
block|{
return|return
name|DEFAULT_CLASS_NAME
return|;
block|}
specifier|final
name|String
name|title
init|=
name|info
operator|.
name|getTitle
argument_list|()
decl_stmt|;
if|if
condition|(
name|title
operator|==
literal|null
condition|)
block|{
return|return
name|DEFAULT_CLASS_NAME
return|;
block|}
specifier|final
name|String
name|className
init|=
name|title
operator|.
name|chars
argument_list|()
operator|.
name|filter
argument_list|(
name|Character
operator|::
name|isJavaIdentifierPart
argument_list|)
operator|.
name|filter
argument_list|(
name|c
lambda|->
name|c
operator|<
literal|'z'
argument_list|)
operator|.
name|boxed
argument_list|()
operator|.
name|collect
argument_list|(
name|Collector
operator|.
name|of
argument_list|(
name|StringBuilder
operator|::
operator|new
argument_list|,
name|StringBuilder
operator|::
name|appendCodePoint
argument_list|,
name|StringBuilder
operator|::
name|append
argument_list|,
name|StringBuilder
operator|::
name|toString
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|className
operator|.
name|isEmpty
argument_list|()
operator|||
operator|!
name|Character
operator|.
name|isJavaIdentifierStart
argument_list|(
name|className
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
condition|)
block|{
return|return
name|DEFAULT_CLASS_NAME
return|;
block|}
return|return
name|className
return|;
block|}
DECL|method|generatePackageName (final Swagger swagger)
specifier|static
name|String
name|generatePackageName
parameter_list|(
specifier|final
name|Swagger
name|swagger
parameter_list|)
block|{
specifier|final
name|String
name|host
init|=
name|swagger
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
specifier|final
name|StringBuilder
name|packageName
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|String
name|hostWithoutPort
init|=
name|host
operator|.
name|replaceFirst
argument_list|(
literal|":.*"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"localhost"
operator|.
name|equalsIgnoreCase
argument_list|(
name|hostWithoutPort
argument_list|)
condition|)
block|{
return|return
name|DEFAULT_PACKAGE_NAME
return|;
block|}
specifier|final
name|String
index|[]
name|parts
init|=
name|hostWithoutPort
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
name|parts
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
name|packageName
operator|.
name|append
argument_list|(
name|parts
index|[
name|i
index|]
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|!=
literal|0
condition|)
block|{
name|packageName
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|packageName
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
name|DEFAULT_PACKAGE_NAME
return|;
block|}
block|}
end_class

end_unit

