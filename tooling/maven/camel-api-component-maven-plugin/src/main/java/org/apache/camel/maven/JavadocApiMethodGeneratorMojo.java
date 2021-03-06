begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
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
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Modifier
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|swing
operator|.
name|text
operator|.
name|html
operator|.
name|parser
operator|.
name|DTD
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
name|support
operator|.
name|component
operator|.
name|ApiMethodParser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
import|;
end_import

begin_comment
comment|/**  * Parses ApiMethod signatures from Javadoc.  */
end_comment

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"fromJavadoc"
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|TEST
argument_list|,
name|requiresProject
operator|=
literal|true
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|GENERATE_SOURCES
argument_list|,
name|threadSafe
operator|=
literal|true
argument_list|)
DECL|class|JavadocApiMethodGeneratorMojo
specifier|public
class|class
name|JavadocApiMethodGeneratorMojo
extends|extends
name|AbstractApiMethodGeneratorMojo
block|{
static|static
block|{
comment|// set Java AWT to headless before using Swing HTML parser
name|System
operator|.
name|setProperty
argument_list|(
literal|"java.awt.headless"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
DECL|field|DEFAULT_EXCLUDE_PACKAGES
specifier|protected
specifier|static
specifier|final
name|String
name|DEFAULT_EXCLUDE_PACKAGES
init|=
literal|"javax?\\.lang.*"
decl_stmt|;
DECL|field|RAW_ARGTYPES_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|RAW_ARGTYPES_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\s*([^<\\s,]+)\\s*(<[^>]+>)?\\s*,?"
argument_list|)
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"excludePackages"
argument_list|,
name|defaultValue
operator|=
name|DEFAULT_EXCLUDE_PACKAGES
argument_list|)
DECL|field|excludePackages
specifier|protected
name|String
name|excludePackages
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"excludeClasses"
argument_list|)
DECL|field|excludeClasses
specifier|protected
name|String
name|excludeClasses
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"includeMethods"
argument_list|)
DECL|field|includeMethods
specifier|protected
name|String
name|includeMethods
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"excludeMethods"
argument_list|)
DECL|field|excludeMethods
specifier|protected
name|String
name|excludeMethods
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|property
operator|=
name|PREFIX
operator|+
literal|"includeStaticMethods"
argument_list|)
DECL|field|includeStaticMethods
specifier|protected
name|Boolean
name|includeStaticMethods
decl_stmt|;
annotation|@
name|Override
DECL|method|getSignatureList ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSignatureList
parameter_list|()
throws|throws
name|MojoExecutionException
block|{
comment|// signatures as map from signature with no arg names to arg names from JavadocParser
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Pattern
name|packagePatterns
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludePackages
argument_list|)
decl_stmt|;
specifier|final
name|Pattern
name|classPatterns
init|=
operator|(
name|excludeClasses
operator|!=
literal|null
operator|)
condition|?
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeClasses
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|Pattern
name|includeMethodPatterns
init|=
operator|(
name|includeMethods
operator|!=
literal|null
operator|)
condition|?
name|Pattern
operator|.
name|compile
argument_list|(
name|includeMethods
argument_list|)
else|:
literal|null
decl_stmt|;
specifier|final
name|Pattern
name|excludeMethodPatterns
init|=
operator|(
name|excludeMethods
operator|!=
literal|null
operator|)
condition|?
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeMethods
argument_list|)
else|:
literal|null
decl_stmt|;
comment|// for proxy class and super classes not matching excluded packages or classes
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|getProxyType
argument_list|()
init|;
name|aClass
operator|!=
literal|null
operator|&&
operator|!
name|packagePatterns
operator|.
name|matcher
argument_list|(
name|aClass
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|&&
operator|(
name|classPatterns
operator|==
literal|null
operator|||
operator|!
name|classPatterns
operator|.
name|matcher
argument_list|(
name|aClass
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|matches
argument_list|()
operator|)
condition|;
name|aClass
operator|=
name|aClass
operator|.
name|getSuperclass
argument_list|()
control|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Processing "
operator|+
name|aClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|javaDocPath
init|=
name|aClass
operator|.
name|getName
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"\\."
argument_list|,
literal|"/"
argument_list|)
operator|.
name|replace
argument_list|(
literal|'$'
argument_list|,
literal|'.'
argument_list|)
operator|+
literal|".html"
decl_stmt|;
comment|// read javadoc html text for class
try|try
init|(
name|InputStream
name|inputStream
init|=
name|getProjectClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|javaDocPath
argument_list|)
init|)
block|{
if|if
condition|(
name|inputStream
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"JavaDoc not found on classpath for "
operator|+
name|aClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// transform the HTML to get method summary as text
comment|// dummy DTD
specifier|final
name|DTD
name|dtd
init|=
name|DTD
operator|.
name|getDTD
argument_list|(
literal|"html.dtd"
argument_list|)
decl_stmt|;
specifier|final
name|JavadocParser
name|htmlParser
init|=
operator|new
name|JavadocParser
argument_list|(
name|dtd
argument_list|,
name|javaDocPath
argument_list|)
decl_stmt|;
name|htmlParser
operator|.
name|parse
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
comment|// look for parse errors
specifier|final
name|String
name|parseError
init|=
name|htmlParser
operator|.
name|getErrorMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|parseError
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|parseError
argument_list|)
throw|;
block|}
comment|// get public method signature
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|methodMap
init|=
name|htmlParser
operator|.
name|getMethodText
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|method
range|:
name|htmlParser
operator|.
name|getMethods
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|result
operator|.
name|containsKey
argument_list|(
name|method
argument_list|)
operator|&&
operator|(
name|includeMethodPatterns
operator|==
literal|null
operator|||
name|includeMethodPatterns
operator|.
name|matcher
argument_list|(
name|method
argument_list|)
operator|.
name|find
argument_list|()
operator|)
operator|&&
operator|(
name|excludeMethodPatterns
operator|==
literal|null
operator|||
operator|!
name|excludeMethodPatterns
operator|.
name|matcher
argument_list|(
name|method
argument_list|)
operator|.
name|find
argument_list|()
operator|)
condition|)
block|{
specifier|final
name|int
name|leftBracket
init|=
name|method
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
decl_stmt|;
specifier|final
name|String
name|name
init|=
name|method
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|leftBracket
argument_list|)
decl_stmt|;
specifier|final
name|String
name|args
init|=
name|method
operator|.
name|substring
argument_list|(
name|leftBracket
operator|+
literal|1
argument_list|,
name|method
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|String
index|[]
name|types
decl_stmt|;
if|if
condition|(
name|args
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|types
operator|=
operator|new
name|String
index|[
literal|0
index|]
expr_stmt|;
block|}
else|else
block|{
comment|// get raw types from args
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|rawTypes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Matcher
name|argTypesMatcher
init|=
name|RAW_ARGTYPES_PATTERN
operator|.
name|matcher
argument_list|(
name|args
argument_list|)
decl_stmt|;
while|while
condition|(
name|argTypesMatcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|rawTypes
operator|.
name|add
argument_list|(
name|argTypesMatcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|types
operator|=
name|rawTypes
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|rawTypes
operator|.
name|size
argument_list|()
index|]
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|resultType
init|=
name|getResultType
argument_list|(
name|aClass
argument_list|,
name|name
argument_list|,
name|types
argument_list|)
decl_stmt|;
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|resultType
operator|+
literal|" "
operator|+
name|name
operator|+
name|methodMap
operator|.
name|get
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
literal|"No public non-static methods found, "
operator|+
literal|"make sure Javadoc is available as project test dependency"
argument_list|)
throw|;
block|}
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|result
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getResultType (Class<?> aClass, String name, String[] types)
specifier|private
name|String
name|getResultType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|,
name|String
name|name
parameter_list|,
name|String
index|[]
name|types
parameter_list|)
throws|throws
name|MojoExecutionException
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|argTypes
init|=
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[
name|types
operator|.
name|length
index|]
decl_stmt|;
specifier|final
name|ClassLoader
name|classLoader
init|=
name|getProjectClassLoader
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|types
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
try|try
block|{
name|argTypes
index|[
name|i
index|]
operator|=
name|ApiMethodParser
operator|.
name|forName
argument_list|(
name|types
index|[
name|i
index|]
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// return null for non-public methods, and for non-static methods if includeStaticMethods is null or false
name|String
name|result
init|=
literal|null
decl_stmt|;
try|try
block|{
specifier|final
name|Method
name|method
init|=
name|aClass
operator|.
name|getMethod
argument_list|(
name|name
argument_list|,
name|argTypes
argument_list|)
decl_stmt|;
name|int
name|modifiers
init|=
name|method
operator|.
name|getModifiers
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|Modifier
operator|.
name|isStatic
argument_list|(
name|modifiers
argument_list|)
operator|||
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|includeStaticMethods
argument_list|)
condition|)
block|{
name|result
operator|=
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e
parameter_list|)
block|{
comment|// could be a non-public method
try|try
block|{
name|aClass
operator|.
name|getDeclaredMethod
argument_list|(
name|name
argument_list|,
name|argTypes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchMethodException
name|e1
parameter_list|)
block|{
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|e1
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e1
argument_list|)
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

