begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
operator|.
name|helper
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
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|javax
operator|.
name|servlet
operator|.
name|ServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|Producer
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
name|component
operator|.
name|http
operator|.
name|HttpConstants
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
name|component
operator|.
name|http
operator|.
name|HttpConverter
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
name|component
operator|.
name|http
operator|.
name|HttpEndpoint
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
name|component
operator|.
name|http
operator|.
name|HttpMessage
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
name|component
operator|.
name|http
operator|.
name|HttpMethods
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
name|component
operator|.
name|http
operator|.
name|HttpServletUrlRewrite
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
name|converter
operator|.
name|stream
operator|.
name|CachedOutputStream
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

begin_class
DECL|class|HttpHelper
specifier|public
specifier|final
class|class
name|HttpHelper
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
name|HttpHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HttpHelper ()
specifier|private
name|HttpHelper
parameter_list|()
block|{
comment|// Helper class
block|}
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
comment|/**      * Writes the given object as response body to the servlet response      *<p/>      * The content type will be set to {@link HttpConstants#CONTENT_TYPE_JAVA_SERIALIZED_OBJECT}      *      * @param response servlet response      * @param target   object to write      * @throws IOException is thrown if error writing      */
DECL|method|writeObjectToServletResponse (ServletResponse response, Object target)
specifier|public
specifier|static
name|void
name|writeObjectToServletResponse
parameter_list|(
name|ServletResponse
name|response
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|response
operator|.
name|setContentType
argument_list|(
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
name|writeObjectToStream
argument_list|(
name|response
operator|.
name|getOutputStream
argument_list|()
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
comment|/**      * Writes the given object as response body to the output stream      *      * @param stream output stream      * @param target   object to write      * @throws IOException is thrown if error writing      */
DECL|method|writeObjectToStream (OutputStream stream, Object target)
specifier|public
specifier|static
name|void
name|writeObjectToStream
parameter_list|(
name|OutputStream
name|stream
parameter_list|,
name|Object
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|oos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|oos
argument_list|)
expr_stmt|;
block|}
comment|/**      * Deserializes the input stream to a Java object      *      * @param is input stream for the Java object      * @return the java object, or<tt>null</tt> if input stream was<tt>null</tt>      * @throws ClassNotFoundException is thrown if class not found      * @throws IOException can be thrown      */
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
comment|/**      * Reads the response body from the given http servlet request.      *      * @param request  http servlet request      * @param exchange the exchange      * @return the response body, can be<tt>null</tt> if no body      * @throws IOException is thrown if error reading response body      */
DECL|method|readResponseBodyFromServletRequest (HttpServletRequest request, Exchange exchange)
specifier|public
specifier|static
name|Object
name|readResponseBodyFromServletRequest
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|HttpConverter
operator|.
name|toInputStream
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|readResponseBodyFromInputStream
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Reads the response body from the given input stream.      *      * @param is       the input stream      * @param exchange the exchange      * @return the response body, can be<tt>null</tt> if no body      * @throws IOException is thrown if error reading response body      */
DECL|method|readResponseBodyFromInputStream (InputStream is, Exchange exchange)
specifier|public
specifier|static
name|Object
name|readResponseBodyFromInputStream
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
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
comment|// convert the input stream to StreamCache if the stream cache is not disabled
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|DISABLE_HTTP_STREAM_CACHE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|is
return|;
block|}
else|else
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|cos
argument_list|)
expr_stmt|;
return|return
name|cos
operator|.
name|newStreamCache
argument_list|()
return|;
block|}
block|}
comment|/**      * Creates the URL to invoke.      *      * @param exchange the exchange      * @param endpoint the endpoint      * @return the URL to invoke      */
DECL|method|createURL (Exchange exchange, HttpEndpoint endpoint)
specifier|public
specifier|static
name|String
name|createURL
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|String
name|uri
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|endpoint
operator|.
name|isBridgeEndpoint
argument_list|()
operator|)
condition|)
block|{
name|uri
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
name|HTTP_URI
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|==
literal|null
condition|)
block|{
name|uri
operator|=
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
block|}
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
comment|// make sure that there is exactly one "/" between HTTP_URI and
comment|// HTTP_PATH
if|if
condition|(
operator|!
name|uri
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|uri
operator|=
name|uri
operator|+
literal|"/"
expr_stmt|;
block|}
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
DECL|method|createURI (Exchange exchange, String url, HttpEndpoint endpoint)
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
name|HttpEndpoint
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
comment|// is a query string provided in the endpoint URI or in a header (header overrules endpoint)
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
name|HTTP_QUERY
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
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
comment|// We should user the query string from the HTTP_URI header
if|if
condition|(
name|queryString
operator|==
literal|null
condition|)
block|{
name|queryString
operator|=
name|uri
operator|.
name|getQuery
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
comment|/**      * Creates the HttpMethod to use to call the remote server, often either its GET or POST.      *      * @param exchange  the exchange      * @return the created method      * @throws URISyntaxException      */
DECL|method|createMethod (Exchange exchange, HttpEndpoint endpoint, boolean hasPayload)
specifier|public
specifier|static
name|HttpMethods
name|createMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpEndpoint
name|endpoint
parameter_list|,
name|boolean
name|hasPayload
parameter_list|)
throws|throws
name|URISyntaxException
block|{
comment|// compute what method to use either GET or POST
name|HttpMethods
name|answer
decl_stmt|;
name|HttpMethods
name|m
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
name|HTTP_METHOD
argument_list|,
name|HttpMethods
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
comment|// always use what end-user provides in a header
name|answer
operator|=
name|m
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hasPayload
condition|)
block|{
comment|// use POST if we have payload
name|answer
operator|=
name|HttpMethods
operator|.
name|POST
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to GET
name|answer
operator|=
name|HttpMethods
operator|.
name|GET
expr_stmt|;
block|}
return|return
name|answer
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
comment|/**      * Extracts the parameter value.      *<p/>      * This implementation supports HTTP multi value parameters which      * is based on the syntax of<tt>[value1, value2, value3]</tt> by returning      * a {@link List} containing the values.      *<p/>      * If the value is not a HTTP mulit value the value is returned as is.      *      * @param value the parameter value      * @return the extracted parameter value, see more details in javadoc.      */
DECL|method|extractHttpParameterValue (String value)
specifier|public
specifier|static
name|Object
name|extractHttpParameterValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|value
argument_list|)
condition|)
block|{
return|return
name|value
return|;
block|}
comment|// trim value before checking for multiple parameters
name|String
name|trimmed
init|=
name|value
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|trimmed
operator|.
name|startsWith
argument_list|(
literal|"["
argument_list|)
operator|&&
name|trimmed
operator|.
name|endsWith
argument_list|(
literal|"]"
argument_list|)
condition|)
block|{
comment|// remove the [ ] markers
name|trimmed
operator|=
name|trimmed
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|trimmed
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|String
index|[]
name|values
init|=
name|trimmed
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|values
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|s
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
return|return
name|value
return|;
block|}
comment|/**      * Processes any custom {@link org.apache.camel.component.http.UrlRewrite}.      *      * @param exchange    the exchange      * @param url         the url      * @param endpoint    the http endpoint      * @param producer    the producer      * @return            the rewritten url, or<tt>null</tt> to use original url      * @throws Exception is thrown if any error during rewriting url      */
DECL|method|urlRewrite (Exchange exchange, String url, HttpEndpoint endpoint, Producer producer)
specifier|public
specifier|static
name|String
name|urlRewrite
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|HttpEndpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|answer
init|=
literal|null
decl_stmt|;
name|String
name|relativeUrl
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getUrlRewrite
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// we should use the relative path if possible
name|String
name|baseUrl
decl_stmt|;
name|relativeUrl
operator|=
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
if|if
condition|(
name|url
operator|.
name|startsWith
argument_list|(
name|relativeUrl
argument_list|)
condition|)
block|{
name|baseUrl
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|relativeUrl
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|relativeUrl
operator|=
name|url
operator|.
name|substring
argument_list|(
name|relativeUrl
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|baseUrl
operator|=
literal|null
expr_stmt|;
name|relativeUrl
operator|=
name|url
expr_stmt|;
block|}
comment|// mark it as null if its empty
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|relativeUrl
argument_list|)
condition|)
block|{
name|relativeUrl
operator|=
literal|null
expr_stmt|;
block|}
name|String
name|newUrl
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getUrlRewrite
argument_list|()
operator|instanceof
name|HttpServletUrlRewrite
condition|)
block|{
comment|// its servlet based, so we need the servlet request
name|HttpServletRequest
name|request
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|HttpServletRequest
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
name|HttpMessage
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|HttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
name|request
operator|=
name|msg
operator|.
name|getRequest
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"UrlRewrite "
operator|+
name|endpoint
operator|.
name|getUrlRewrite
argument_list|()
operator|+
literal|" requires the message body to be a"
operator|+
literal|"HttpServletRequest instance, but was: "
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
comment|// we need to adapt the context-path to be the path from the endpoint, if it came from a http based endpoint
comment|// as eg camel-jetty have hardcoded context-path as / for all its servlets/endpoints
comment|// we have the actual context-path stored as a header with the key CamelServletContextPath
name|String
name|contextPath
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelServletContextPath"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|request
operator|=
operator|new
name|UrlRewriteHttpServletRequestAdapter
argument_list|(
name|request
argument_list|,
name|contextPath
argument_list|)
expr_stmt|;
name|newUrl
operator|=
operator|(
operator|(
name|HttpServletUrlRewrite
operator|)
name|endpoint
operator|.
name|getUrlRewrite
argument_list|()
operator|)
operator|.
name|rewrite
argument_list|(
name|url
argument_list|,
name|relativeUrl
argument_list|,
name|producer
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newUrl
operator|=
name|endpoint
operator|.
name|getUrlRewrite
argument_list|()
operator|.
name|rewrite
argument_list|(
name|url
argument_list|,
name|relativeUrl
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|newUrl
argument_list|)
operator|&&
name|newUrl
operator|!=
name|url
condition|)
block|{
comment|// we got a new url back, that can either be a new absolute url
comment|// or a new relative url
if|if
condition|(
name|newUrl
operator|.
name|startsWith
argument_list|(
literal|"http:"
argument_list|)
operator|||
name|newUrl
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|newUrl
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|baseUrl
operator|!=
literal|null
condition|)
block|{
comment|// avoid double // when adding the urls
if|if
condition|(
name|baseUrl
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
operator|&&
name|newUrl
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|answer
operator|=
name|baseUrl
operator|+
name|newUrl
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|baseUrl
operator|+
name|newUrl
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// use the new url as is
name|answer
operator|=
name|newUrl
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using url rewrite to rewrite from url {} to {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|relativeUrl
operator|!=
literal|null
condition|?
name|relativeUrl
else|:
name|url
block|,
name|newUrl
block|,
name|answer
block|}
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

