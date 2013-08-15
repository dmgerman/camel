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
comment|/**      * Resolves the mandatory resource.      *<p/>      * If possible recommended to use {@link #resolveMandatoryResourceAsUrl(org.apache.camel.spi.ClassResolver, String)}      *      * @param classResolver the class resolver to load the resource from the classpath      * @param uri URI of the resource      * @return the resource as an {@link InputStream}.  Remember to close this stream after usage.      * @throws java.io.IOException is thrown if the resource file could not be found or loaded as {@link InputStream}      */
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
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"file:"
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
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"classpath:"
argument_list|)
expr_stmt|;
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
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"file:"
argument_list|)
decl_stmt|;
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
name|ObjectHelper
operator|.
name|after
argument_list|(
name|uri
argument_list|,
literal|"classpath:"
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

