begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
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
name|ObjectInputStream
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
name|Message
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
name|RuntimeExchangeException
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
name|converter
operator|.
name|IOConverter
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
name|IOHelper
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
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|StringHelper
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_comment
comment|/**  * Helpers.  */
end_comment

begin_class
DECL|class|NettyHttpHelper
specifier|public
specifier|final
class|class
name|NettyHttpHelper
block|{
DECL|method|NettyHttpHelper ()
specifier|private
name|NettyHttpHelper
parameter_list|()
block|{     }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|setCharsetFromContentType (String contentType, Exchange exchange)
specifier|public
specifier|static
name|void
name|setCharsetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|charset
init|=
name|getCharsetFromContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|IOConverter
operator|.
name|normalizeCharset
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getCharsetFromContentType (String contentType)
specifier|public
specifier|static
name|String
name|getCharsetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
comment|// find the charset and set it to the Exchange
name|int
name|index
init|=
name|contentType
operator|.
name|indexOf
argument_list|(
literal|"charset="
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|String
name|charset
init|=
name|contentType
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|8
argument_list|)
decl_stmt|;
comment|// there may be another parameter after a semi colon, so skip that
if|if
condition|(
name|charset
operator|.
name|contains
argument_list|(
literal|";"
argument_list|)
condition|)
block|{
name|charset
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|charset
argument_list|,
literal|";"
argument_list|)
expr_stmt|;
block|}
return|return
name|IOHelper
operator|.
name|normalizeCharset
argument_list|(
name|charset
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Appends the key/value to the headers.      *<p/>      * This implementation supports keys with multiple values. In such situations the value      * will be a {@link java.util.List} that contains the multiple values.      *      * @param headers  headers      * @param key      the key      * @param value    the value      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|appendHeader (Map<String, Object> headers, String key, Object value)
specifier|public
specifier|static
name|void
name|appendHeader
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|headers
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|Object
name|existing
init|=
name|headers
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|list
decl_stmt|;
if|if
condition|(
name|existing
operator|instanceof
name|List
condition|)
block|{
name|list
operator|=
operator|(
name|List
argument_list|<
name|Object
argument_list|>
operator|)
name|existing
expr_stmt|;
block|}
else|else
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|existing
argument_list|)
expr_stmt|;
block|}
name|list
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|value
operator|=
name|list
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link HttpMethod} to use to call the remote server, often either its GET or POST.      *      * @param message  the Camel message      * @return the created method      */
DECL|method|createMethod (Message message, boolean hasPayload)
specifier|public
specifier|static
name|HttpMethod
name|createMethod
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|hasPayload
parameter_list|)
block|{
comment|// use header first
name|HttpMethod
name|m
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
return|return
name|m
return|;
block|}
name|String
name|name
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
return|return
name|HttpMethod
operator|.
name|valueOf
argument_list|(
name|name
argument_list|)
return|;
block|}
if|if
condition|(
name|hasPayload
condition|)
block|{
comment|// use POST if we have payload
return|return
name|HttpMethod
operator|.
name|POST
return|;
block|}
else|else
block|{
comment|// fallback to GET
return|return
name|HttpMethod
operator|.
name|GET
return|;
block|}
block|}
DECL|method|populateNettyHttpOperationFailedException (Exchange exchange, String url, HttpResponse response, int responseCode, boolean transferException)
specifier|public
specifier|static
name|Exception
name|populateNettyHttpOperationFailedException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|int
name|responseCode
parameter_list|,
name|boolean
name|transferException
parameter_list|)
block|{
name|String
name|uri
init|=
name|url
decl_stmt|;
name|String
name|statusText
init|=
name|response
operator|.
name|getStatus
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
decl_stmt|;
if|if
condition|(
name|responseCode
operator|>=
literal|300
operator|&&
name|responseCode
operator|<
literal|400
condition|)
block|{
name|String
name|redirectLocation
init|=
name|response
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
literal|"location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|redirectLocation
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|NettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
name|redirectLocation
argument_list|,
name|response
argument_list|)
return|;
block|}
else|else
block|{
comment|// no redirect location
return|return
operator|new
name|NettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|response
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|transferException
condition|)
block|{
name|String
name|contentType
init|=
name|response
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|NettyHttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
operator|.
name|equals
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
comment|// if the response was a serialized exception then use that
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|response
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Object
name|body
init|=
name|deserializeJavaObjectFromStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Exception
condition|)
block|{
return|return
operator|(
name|Exception
operator|)
name|body
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|e
return|;
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
block|}
comment|// internal server error (error code 500)
return|return
operator|new
name|NettyHttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|response
argument_list|)
return|;
block|}
DECL|method|deserializeJavaObjectFromStream (InputStream is)
specifier|public
specifier|static
name|Object
name|deserializeJavaObjectFromStream
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|ClassNotFoundException
throws|,
name|IOException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Object
name|answer
init|=
literal|null
decl_stmt|;
name|ObjectInputStream
name|ois
init|=
operator|new
name|ObjectInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|ois
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|ois
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Creates the URL to invoke.      *      * @param exchange the exchange      * @param endpoint the endpoint      * @return the URL to invoke      */
DECL|method|createURL (Exchange exchange, NettyHttpEndpoint endpoint)
specifier|public
specifier|static
name|String
name|createURL
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|NettyHttpEndpoint
name|endpoint
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|String
name|uri
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
comment|// resolve placeholders in uri
try|try
block|{
name|uri
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Cannot resolve property placeholders with uri: "
operator|+
name|uri
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// append HTTP_PATH to HTTP_URI if it is provided in the header
name|String
name|path
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// NOW the HTTP_PATH is just related path, we don't need to trim it
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// inject the dynamic path before the query params, if there are any
name|int
name|idx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|"?"
argument_list|)
decl_stmt|;
comment|// if there are no query params
if|if
condition|(
name|idx
operator|==
operator|-
literal|1
condition|)
block|{
comment|// make sure that there is exactly one "/" between HTTP_URI and HTTP_PATH
name|uri
operator|=
name|uri
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|uri
else|:
name|uri
operator|+
literal|"/"
expr_stmt|;
name|uri
operator|=
name|uri
operator|.
name|concat
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// there are query params, so inject the relative path in the right place
name|String
name|base
init|=
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|base
operator|=
name|base
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|?
name|base
else|:
name|base
operator|+
literal|"/"
expr_stmt|;
name|base
operator|=
name|base
operator|.
name|concat
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|uri
operator|=
name|base
operator|.
name|concat
argument_list|(
name|uri
operator|.
name|substring
argument_list|(
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// ensure uri is encoded to be valid
name|uri
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|uri
return|;
block|}
comment|/**      * Creates the URI to invoke.      *      * @param exchange the exchange      * @param url      the url to invoke      * @param endpoint the endpoint      * @return the URI to invoke      */
DECL|method|createURI (Exchange exchange, String url, NettyHttpEndpoint endpoint)
specifier|public
specifier|static
name|URI
name|createURI
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|NettyHttpEndpoint
name|endpoint
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
decl_stmt|;
comment|// is a query string provided in the endpoint URI or in a header
comment|// (header overrules endpoint, raw query header overrules query header)
name|String
name|queryString
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RAW_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
name|queryString
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
comment|// use raw as we encode just below
name|queryString
operator|=
name|uri
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|queryString
operator|!=
literal|null
condition|)
block|{
comment|// need to encode query string
name|queryString
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
name|uri
operator|=
name|URISupport
operator|.
name|createURIWithQuery
argument_list|(
name|uri
argument_list|,
name|queryString
argument_list|)
expr_stmt|;
block|}
return|return
name|uri
return|;
block|}
comment|/**      * Checks whether the given http status code is within the ok range      *      * @param statusCode the status code      * @param okStatusCodeRange the ok range (inclusive)      * @return<tt>true</tt> if ok,<tt>false</tt> otherwise      */
DECL|method|isStatusCodeOk (int statusCode, String okStatusCodeRange)
specifier|public
specifier|static
name|boolean
name|isStatusCodeOk
parameter_list|(
name|int
name|statusCode
parameter_list|,
name|String
name|okStatusCodeRange
parameter_list|)
block|{
name|String
index|[]
name|ranges
init|=
name|okStatusCodeRange
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|range
range|:
name|ranges
control|)
block|{
name|boolean
name|ok
decl_stmt|;
if|if
condition|(
name|range
operator|.
name|contains
argument_list|(
literal|"-"
argument_list|)
condition|)
block|{
name|int
name|from
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|StringHelper
operator|.
name|before
argument_list|(
name|range
argument_list|,
literal|"-"
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|to
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|StringHelper
operator|.
name|after
argument_list|(
name|range
argument_list|,
literal|"-"
argument_list|)
argument_list|)
decl_stmt|;
name|ok
operator|=
name|statusCode
operator|>=
name|from
operator|&&
name|statusCode
operator|<=
name|to
expr_stmt|;
block|}
else|else
block|{
name|int
name|exact
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|range
argument_list|)
decl_stmt|;
name|ok
operator|=
name|exact
operator|==
name|statusCode
expr_stmt|;
block|}
if|if
condition|(
name|ok
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

