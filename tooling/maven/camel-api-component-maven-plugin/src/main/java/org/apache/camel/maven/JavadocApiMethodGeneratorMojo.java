begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|UnsupportedEncodingException
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
name|net
operator|.
name|URLDecoder
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
name|ChangedCharSetException
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
name|SimpleAttributeSet
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
name|HTML
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
name|Parser
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
name|TagElement
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

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|IOUtil
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
literal|"excludeMethods"
argument_list|)
DECL|field|excludeMethods
specifier|protected
name|String
name|excludeMethods
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|Pattern
name|classPatterns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|excludeClasses
operator|!=
literal|null
condition|)
block|{
name|classPatterns
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeClasses
argument_list|)
expr_stmt|;
block|}
name|Pattern
name|methodPatterns
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|excludeMethods
operator|!=
literal|null
condition|)
block|{
name|methodPatterns
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
name|excludeMethods
argument_list|)
expr_stmt|;
block|}
comment|// for proxy class and super classes not matching excluded packages or classes
for|for
control|(
name|Class
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
operator|+
literal|".html"
decl_stmt|;
comment|// read javadoc html text for class
name|InputStream
name|inputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|inputStream
operator|=
name|getProjectClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|javaDocPath
argument_list|)
expr_stmt|;
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
name|methodPatterns
operator|==
literal|null
operator|||
operator|!
name|methodPatterns
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
name|types
operator|=
name|args
operator|.
name|split
argument_list|(
literal|","
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
specifier|final
name|StringBuilder
name|signature
init|=
operator|new
name|StringBuilder
argument_list|(
name|resultType
argument_list|)
decl_stmt|;
name|signature
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
name|methodMap
operator|.
name|get
argument_list|(
name|method
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|put
argument_list|(
name|method
argument_list|,
name|signature
operator|.
name|toString
argument_list|()
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
finally|finally
block|{
name|IOUtil
operator|.
name|close
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
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
argument_list|<
name|String
argument_list|>
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
operator|.
name|trim
argument_list|()
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
comment|// return null for non-public and non-static methods
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
comment|// only include non-static public methods
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
condition|)
block|{
name|result
operator|=
name|method
operator|.
name|getReturnType
argument_list|()
operator|.
name|getCanonicalName
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
DECL|class|JavadocParser
specifier|private
class|class
name|JavadocParser
extends|extends
name|Parser
block|{
DECL|field|hrefPattern
specifier|private
name|String
name|hrefPattern
decl_stmt|;
DECL|field|parserState
specifier|private
name|ParserState
name|parserState
decl_stmt|;
DECL|field|methodWithTypes
specifier|private
name|String
name|methodWithTypes
decl_stmt|;
DECL|field|methodTextBuilder
specifier|private
name|StringBuilder
name|methodTextBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|field|methods
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|methods
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|methodText
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|methodText
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|JavadocParser (DTD dtd, String docPath)
specifier|public
name|JavadocParser
parameter_list|(
name|DTD
name|dtd
parameter_list|,
name|String
name|docPath
parameter_list|)
block|{
name|super
argument_list|(
name|dtd
argument_list|)
expr_stmt|;
name|this
operator|.
name|hrefPattern
operator|=
name|docPath
operator|+
literal|"#"
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startTag (TagElement tag)
specifier|protected
name|void
name|startTag
parameter_list|(
name|TagElement
name|tag
parameter_list|)
throws|throws
name|ChangedCharSetException
block|{
name|super
operator|.
name|startTag
argument_list|(
name|tag
argument_list|)
expr_stmt|;
specifier|final
name|HTML
operator|.
name|Tag
name|htmlTag
init|=
name|tag
operator|.
name|getHTMLTag
argument_list|()
decl_stmt|;
if|if
condition|(
name|htmlTag
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|HTML
operator|.
name|Tag
operator|.
name|A
operator|.
name|equals
argument_list|(
name|htmlTag
argument_list|)
condition|)
block|{
specifier|final
name|SimpleAttributeSet
name|attributes
init|=
name|getAttributes
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|name
init|=
name|attributes
operator|.
name|getAttribute
argument_list|(
name|HTML
operator|.
name|Attribute
operator|.
name|NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
specifier|final
name|String
name|nameAttr
init|=
operator|(
name|String
operator|)
name|name
decl_stmt|;
if|if
condition|(
name|parserState
operator|==
literal|null
operator|&&
literal|"method_summary"
operator|.
name|equals
argument_list|(
name|nameAttr
argument_list|)
condition|)
block|{
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD_SUMMARY
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD_SUMMARY
operator|&&
name|nameAttr
operator|.
name|startsWith
argument_list|(
literal|"methods_inherited_from_class_"
argument_list|)
condition|)
block|{
name|parserState
operator|=
literal|null
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
operator|&&
name|methodWithTypes
operator|==
literal|null
condition|)
block|{
specifier|final
name|Object
name|href
init|=
name|attributes
operator|.
name|getAttribute
argument_list|(
name|HTML
operator|.
name|Attribute
operator|.
name|HREF
argument_list|)
decl_stmt|;
if|if
condition|(
name|href
operator|!=
literal|null
condition|)
block|{
name|String
name|hrefAttr
init|=
operator|(
name|String
operator|)
name|href
decl_stmt|;
if|if
condition|(
name|hrefAttr
operator|.
name|contains
argument_list|(
name|hrefPattern
argument_list|)
condition|)
block|{
try|try
block|{
name|methodWithTypes
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|hrefAttr
operator|.
name|substring
argument_list|(
name|hrefAttr
operator|.
name|indexOf
argument_list|(
literal|'#'
argument_list|)
operator|+
literal|1
argument_list|)
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD_SUMMARY
operator|&&
name|HTML
operator|.
name|Tag
operator|.
name|CODE
operator|.
name|equals
argument_list|(
name|htmlTag
argument_list|)
condition|)
block|{
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|handleEmptyTag (TagElement tag)
specifier|protected
name|void
name|handleEmptyTag
parameter_list|(
name|TagElement
name|tag
parameter_list|)
block|{
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
operator|&&
name|HTML
operator|.
name|Tag
operator|.
name|CODE
operator|.
name|equals
argument_list|(
name|tag
operator|.
name|getHTMLTag
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|methodWithTypes
operator|!=
literal|null
condition|)
block|{
comment|// process collected method data
name|methods
operator|.
name|add
argument_list|(
name|methodWithTypes
argument_list|)
expr_stmt|;
name|this
operator|.
name|methodText
operator|.
name|put
argument_list|(
name|methodWithTypes
argument_list|,
name|getArgSignature
argument_list|()
argument_list|)
expr_stmt|;
comment|// clear the text builder for next method
name|methodTextBuilder
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|methodTextBuilder
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|methodWithTypes
operator|=
literal|null
expr_stmt|;
block|}
name|parserState
operator|=
name|ParserState
operator|.
name|METHOD_SUMMARY
expr_stmt|;
block|}
block|}
DECL|method|getArgSignature ()
specifier|private
name|String
name|getArgSignature
parameter_list|()
block|{
specifier|final
name|String
name|typeString
init|=
name|methodWithTypes
operator|.
name|substring
argument_list|(
name|methodWithTypes
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
operator|+
literal|1
argument_list|,
name|methodWithTypes
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|typeString
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|"()"
return|;
block|}
specifier|final
name|String
index|[]
name|types
init|=
name|typeString
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|String
name|argText
init|=
name|methodTextBuilder
operator|.
name|toString
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"&nbsp;"
argument_list|,
literal|" "
argument_list|)
operator|.
name|replaceAll
argument_list|(
literal|"&nbsp"
argument_list|,
literal|" "
argument_list|)
decl_stmt|;
specifier|final
name|String
index|[]
name|args
init|=
name|argText
operator|.
name|substring
argument_list|(
name|argText
operator|.
name|indexOf
argument_list|(
literal|'('
argument_list|)
operator|+
literal|1
argument_list|,
name|argText
operator|.
name|indexOf
argument_list|(
literal|')'
argument_list|)
argument_list|)
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"("
argument_list|)
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
specifier|final
name|String
index|[]
name|arg
init|=
name|args
index|[
name|i
index|]
operator|.
name|trim
argument_list|()
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|types
index|[
name|i
index|]
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|arg
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|deleteCharAt
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|handleText (char[] text)
specifier|protected
name|void
name|handleText
parameter_list|(
name|char
index|[]
name|text
parameter_list|)
block|{
if|if
condition|(
name|parserState
operator|==
name|ParserState
operator|.
name|METHOD
operator|&&
name|methodWithTypes
operator|!=
literal|null
condition|)
block|{
name|methodTextBuilder
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMethods ()
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getMethods
parameter_list|()
block|{
return|return
name|methods
return|;
block|}
DECL|method|getMethodText ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getMethodText
parameter_list|()
block|{
return|return
name|methodText
return|;
block|}
block|}
DECL|enum|ParserState
specifier|private
specifier|static
enum|enum
name|ParserState
block|{
DECL|enumConstant|METHOD_SUMMARY
DECL|enumConstant|METHOD
name|METHOD_SUMMARY
block|,
name|METHOD
block|;     }
block|}
end_class

end_unit

