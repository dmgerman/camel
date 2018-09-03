begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLConnection
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
name|Map
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
name|CamelContext
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
name|Exchange
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
name|RuntimeCamelException
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
name|impl
operator|.
name|DefaultExchange
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|ClassResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Helper class for loading resources on the classpath or file system.  */
end_comment

begin_class
DECL|class|ResourceHelper
specifier|public
specifier|final
class|class
name|ResourceHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ResourceHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|ResourceHelper ()
specifier|private
name|ResourceHelper
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Resolves the expression/predicate whether it refers to an external script on the file/classpath etc.      * This requires to use the prefix<tt>resource:</tt> such as<tt>resource:classpath:com/foo/myscript.groovy</tt>,      *<tt>resource:file:/var/myscript.groovy</tt>.      *<p/>      * If not then the returned value is returned as-is.      */
DECL|method|resolveOptionalExternalScript (CamelContext camelContext, String expression)
specifier|public
specifier|static
name|String
name|resolveOptionalExternalScript
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
return|return
name|resolveOptionalExternalScript
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
name|expression
argument_list|)
return|;
block|}
comment|/**      * Resolves the expression/predicate whether it refers to an external script on the file/classpath etc.      * This requires to use the prefix<tt>resource:</tt> such as<tt>resource:classpath:com/foo/myscript.groovy</tt>,      *<tt>resource:file:/var/myscript.groovy</tt>.      *<p/>      * If not then the returned value is returned as-is.      *<p/>      * If the exchange is provided (not null), then the external script can be referred via simple language for dynamic values, etc.      *<tt>resource:classpath:${header.myFileName}</tt>      */
DECL|method|resolveOptionalExternalScript (CamelContext camelContext, Exchange exchange, String expression)
specifier|public
specifier|static
name|String
name|resolveOptionalExternalScript
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|external
init|=
name|expression
decl_stmt|;
comment|// must be one line only
name|int
name|newLines
init|=
name|StringHelper
operator|.
name|countChar
argument_list|(
name|expression
argument_list|,
literal|'\n'
argument_list|)
decl_stmt|;
if|if
condition|(
name|newLines
operator|>
literal|1
condition|)
block|{
comment|// okay then just use as-is
return|return
name|expression
return|;
block|}
comment|// must start with resource: to denote an external resource
if|if
condition|(
name|external
operator|.
name|startsWith
argument_list|(
literal|"resource:"
argument_list|)
condition|)
block|{
name|external
operator|=
name|external
operator|.
name|substring
argument_list|(
literal|9
argument_list|)
expr_stmt|;
if|if
condition|(
name|hasScheme
argument_list|(
name|external
argument_list|)
condition|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|SimpleLanguage
operator|.
name|hasSimpleFunction
argument_list|(
name|external
argument_list|)
condition|)
block|{
name|SimpleLanguage
name|simple
init|=
operator|(
name|SimpleLanguage
operator|)
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
decl_stmt|;
name|external
operator|=
name|simple
operator|.
name|createExpression
argument_list|(
name|external
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
try|try
block|{
name|is
operator|=
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
argument_list|,
name|external
argument_list|)
expr_stmt|;
name|expression
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Cannot load resource "
operator|+
name|external
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|expression
return|;
block|}
comment|/**      * Determines whether the URI has a scheme (e.g. file:, classpath: or http:)      *      * @param uri the URI      * @return<tt>true</tt> if the URI starts with a scheme      */
DECL|method|hasScheme (String uri)
specifier|public
specifier|static
name|boolean
name|hasScheme
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|uri
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
return|;
block|}
comment|/**      * Gets the scheme from the URI (e.g. file:, classpath: or http:)      *      * @param uri  the uri      * @return the scheme, or<tt>null</tt> if no scheme      */
DECL|method|getScheme (String uri)
specifier|public
specifier|static
name|String
name|getScheme
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|hasScheme
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
operator|+
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Resolves the mandatory resource.      *<p/>      * The resource uri can refer to the following systems to be loaded from      *<ul>      *<il>file:nameOfFile - to refer to the file system</il>      *<il>classpath:nameOfFile - to refer to the classpath (default)</il>      *<il>http:uri - to load the resource using HTTP</il>      *<il>ref:nameOfBean - to lookup the resource in the {@link org.apache.camel.spi.Registry}</il>      *<il>bean:nameOfBean.methodName or bean:nameOfBean::methodName - to lookup a bean in the {@link org.apache.camel.spi.Registry} and call the method</il>      *<il><customProtocol>:uri - to lookup the resource using a custom {@link java.net.URLStreamHandler} registered for the<customProtocol>,      *     on how to register it @see java.net.URL#URL(java.lang.String, java.lang.String, int, java.lang.String)</il>      *</ul>      * If no prefix has been given, then the resource is loaded from the classpath      *<p/>      * If possible recommended to use {@link #resolveMandatoryResourceAsUrl(org.apache.camel.spi.ClassResolver, String)}      *      * @param camelContext the Camel Context      * @param uri URI of the resource      * @return the resource as an {@link InputStream}.  Remember to close this stream after usage.      * @throws java.io.IOException is thrown if the resource file could not be found or loaded as {@link InputStream}      */
DECL|method|resolveMandatoryResourceAsInputStream (CamelContext camelContext, String uri)
specifier|public
specifier|static
name|InputStream
name|resolveMandatoryResourceAsInputStream
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"ref:"
argument_list|)
condition|)
block|{
name|String
name|ref
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"bean:"
argument_list|)
condition|)
block|{
name|String
name|bean
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|Exchange
name|dummy
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|Object
name|out
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
literal|"bean"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|bean
argument_list|)
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dummy
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|IOException
name|io
init|=
operator|new
name|IOException
argument_list|(
literal|"Cannot find resource: "
operator|+
name|uri
operator|+
literal|" from calling the bean"
argument_list|)
decl_stmt|;
name|io
operator|.
name|initCause
argument_list|(
name|dummy
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|io
throw|;
block|}
if|if
condition|(
name|out
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|dummy
argument_list|,
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|String
name|text
init|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|dummy
argument_list|,
name|out
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|text
operator|.
name|getBytes
argument_list|()
argument_list|)
return|;
block|}
block|}
else|else
block|{
return|return
name|is
return|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot find resource: "
operator|+
name|uri
operator|+
literal|" from calling the bean"
argument_list|)
throw|;
block|}
block|}
name|InputStream
name|is
init|=
name|resolveResourceAsInputStream
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Cannot find resource: "
operator|+
name|resolvedName
operator|+
literal|" in classpath for URI: "
operator|+
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|is
return|;
block|}
block|}
comment|/**      * Resolves the mandatory resource.      *<p/>      * If possible recommended to use {@link #resolveMandatoryResourceAsUrl(org.apache.camel.spi.ClassResolver, String)}      *      * @param classResolver the class resolver to load the resource from the classpath      * @param uri URI of the resource      * @return the resource as an {@link InputStream}.  Remember to close this stream after usage.      * @throws java.io.IOException is thrown if the resource file could not be found or loaded as {@link InputStream}      * @deprecated use {@link #resolveMandatoryResourceAsInputStream(CamelContext, String)}      */
annotation|@
name|Deprecated
DECL|method|resolveMandatoryResourceAsInputStream (ClassResolver classResolver, String uri)
specifier|public
specifier|static
name|InputStream
name|resolveMandatoryResourceAsInputStream
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|resolveResourceAsInputStream
argument_list|(
name|classResolver
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Cannot find resource: "
operator|+
name|resolvedName
operator|+
literal|" in classpath for URI: "
operator|+
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|is
return|;
block|}
block|}
comment|/**      * Resolves the resource.      *<p/>      * If possible recommended to use {@link #resolveMandatoryResourceAsUrl(org.apache.camel.spi.ClassResolver, String)}      *      * @param classResolver the class resolver to load the resource from the classpath      * @param uri URI of the resource      * @return the resource as an {@link InputStream}. Remember to close this stream after usage. Or<tt>null</tt> if not found.      * @throws java.io.IOException is thrown if error loading the resource      */
DECL|method|resolveResourceAsInputStream (ClassResolver classResolver, String uri)
specifier|public
specifier|static
name|InputStream
name|resolveResourceAsInputStream
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"file:"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|tryDecodeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from file system"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
operator|new
name|FileInputStream
argument_list|(
name|uri
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from HTTP"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|URLConnection
name|con
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|con
operator|.
name|setUseCaches
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|con
operator|.
name|getInputStream
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// close the http connection to avoid
comment|// leaking gaps in case of an exception
if|if
condition|(
name|con
operator|instanceof
name|HttpURLConnection
condition|)
block|{
operator|(
operator|(
name|HttpURLConnection
operator|)
name|con
operator|)
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"classpath:"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|tryDecodeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} with UrlHandler for protocol {}"
argument_list|,
name|uri
argument_list|,
name|uri
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|URLConnection
name|con
init|=
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
return|return
name|con
operator|.
name|getInputStream
argument_list|()
return|;
block|}
comment|// load from classpath by default
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from classpath"
argument_list|,
name|resolvedName
argument_list|)
expr_stmt|;
return|return
name|classResolver
operator|.
name|loadResourceAsStream
argument_list|(
name|resolvedName
argument_list|)
return|;
block|}
comment|/**      * Resolves the mandatory resource.      *      * @param classResolver the class resolver to load the resource from the classpath      * @param uri uri of the resource      * @return the resource as an {@link java.net.URL}.      * @throws java.io.FileNotFoundException is thrown if the resource file could not be found      * @throws java.net.MalformedURLException if the URI is malformed      */
DECL|method|resolveMandatoryResourceAsUrl (ClassResolver classResolver, String uri)
specifier|public
specifier|static
name|URL
name|resolveMandatoryResourceAsUrl
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
name|URL
name|url
init|=
name|resolveResourceAsUrl
argument_list|(
name|classResolver
argument_list|,
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|url
operator|==
literal|null
condition|)
block|{
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|FileNotFoundException
argument_list|(
literal|"Cannot find resource: "
operator|+
name|resolvedName
operator|+
literal|" in classpath for URI: "
operator|+
name|uri
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|url
return|;
block|}
block|}
comment|/**      * Resolves the resource.      *      * @param classResolver the class resolver to load the resource from the classpath      * @param uri uri of the resource      * @return the resource as an {@link java.net.URL}. Or<tt>null</tt> if not found.      * @throws java.net.MalformedURLException if the URI is malformed      */
DECL|method|resolveResourceAsUrl (ClassResolver classResolver, String uri)
specifier|public
specifier|static
name|URL
name|resolveResourceAsUrl
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|MalformedURLException
block|{
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
condition|)
block|{
comment|// check if file exists first
name|String
name|name
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"file:"
argument_list|)
decl_stmt|;
name|uri
operator|=
name|tryDecodeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from file system"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from HTTP"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"classpath:"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|StringHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"classpath:"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|tryDecodeUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|uri
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} with UrlHandler for protocol {}"
argument_list|,
name|uri
argument_list|,
name|uri
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
return|return
operator|new
name|URL
argument_list|(
name|uri
argument_list|)
return|;
block|}
comment|// load from classpath by default
name|String
name|resolvedName
init|=
name|resolveUriPath
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Loading resource: {} from classpath"
argument_list|,
name|resolvedName
argument_list|)
expr_stmt|;
return|return
name|classResolver
operator|.
name|loadResourceAsURL
argument_list|(
name|resolvedName
argument_list|)
return|;
block|}
comment|/**      * Is the given uri a http uri?      *      * @param uri the uri      * @return<tt>true</tt> if the uri starts with<tt>http:</tt> or<tt>https:</tt>      */
DECL|method|isHttpUri (String uri)
specifier|public
specifier|static
name|boolean
name|isHttpUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|uri
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|uri
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
return|;
block|}
comment|/**      * Appends the parameters to the given uri      *      * @param uri the uri      * @param parameters the additional parameters (will clear the map)      * @return a new uri with the additional parameters appended      * @throws URISyntaxException is thrown if the uri is invalid      */
DECL|method|appendParameters (String uri, Map<String, Object> parameters)
specifier|public
specifier|static
name|String
name|appendParameters
parameter_list|(
name|String
name|uri
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|URISyntaxException
block|{
comment|// add additional parameters to the resource uri
if|if
condition|(
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|u
operator|=
name|URISupport
operator|.
name|createURIWithQuery
argument_list|(
name|u
argument_list|,
name|query
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|u
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
comment|/**      * Helper operation used to remove relative path notation from      * resources.  Most critical for resources on the Classpath      * as resource loaders will not resolve the relative paths correctly.      *      * @param name the name of the resource to load      * @return the modified or unmodified string if there were no changes      */
DECL|method|resolveUriPath (String name)
specifier|private
specifier|static
name|String
name|resolveUriPath
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// compact the path and use / as separator as that's used for loading resources on the classpath
return|return
name|FileUtil
operator|.
name|compactPath
argument_list|(
name|name
argument_list|,
literal|'/'
argument_list|)
return|;
block|}
comment|/**      * Tries decoding the uri.      *      * @param uri the uri      * @return the decoded uri, or the original uri      */
DECL|method|tryDecodeUri (String uri)
specifier|private
specifier|static
name|String
name|tryDecodeUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
try|try
block|{
comment|// try to decode as the uri may contain %20 for spaces etc
name|uri
operator|=
name|URLDecoder
operator|.
name|decode
argument_list|(
name|uri
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Error URL decoding uri using UTF-8 encoding: {}. This exception is ignored."
argument_list|,
name|uri
argument_list|)
expr_stmt|;
comment|// ignore
block|}
return|return
name|uri
return|;
block|}
block|}
end_class

end_unit

