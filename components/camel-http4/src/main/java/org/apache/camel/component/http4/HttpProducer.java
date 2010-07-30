begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|HashMap
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
name|InvalidPayloadException
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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
name|http4
operator|.
name|helper
operator|.
name|GZIPHelper
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
name|http4
operator|.
name|helper
operator|.
name|HttpProducerHelper
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
name|impl
operator|.
name|DefaultProducer
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
name|HeaderFilterStrategy
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
name|ExchangeHelper
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|HttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpEntityEnclosingRequestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpRequestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|FileEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|InputStreamEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|params
operator|.
name|CoreProtocolPNames
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpProducer
specifier|public
class|class
name|HttpProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|HttpProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|httpClient
specifier|private
name|HttpClient
name|httpClient
decl_stmt|;
DECL|field|throwException
specifier|private
name|boolean
name|throwException
decl_stmt|;
DECL|method|HttpProducer (HttpEndpoint endpoint)
specifier|public
name|HttpProducer
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|httpClient
operator|=
name|endpoint
operator|.
name|getHttpClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|throwException
operator|=
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|(
operator|(
name|HttpEndpoint
operator|)
name|getEndpoint
argument_list|()
operator|)
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|HttpRequestBase
name|httpRequest
init|=
name|createMethod
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|httpProtocolVersion
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PROTOCOL_VERSION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpProtocolVersion
operator|!=
literal|null
condition|)
block|{
comment|// set the HTTP protocol version
name|httpRequest
operator|.
name|getParams
argument_list|()
operator|.
name|setParameter
argument_list|(
name|CoreProtocolPNames
operator|.
name|PROTOCOL_VERSION
argument_list|,
name|HttpProducerHelper
operator|.
name|parserHttpVersion
argument_list|(
name|httpProtocolVersion
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HeaderFilterStrategy
name|strategy
init|=
name|getEndpoint
argument_list|()
operator|.
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
comment|// propagate headers as HTTP headers
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|headerValue
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|httpRequest
operator|.
name|addHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|// lets store the result in the output message.
name|HttpResponse
name|httpResponse
init|=
literal|null
decl_stmt|;
try|try
block|{
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
literal|"Executing http "
operator|+
name|httpRequest
operator|.
name|getMethod
argument_list|()
operator|+
literal|" method: "
operator|+
name|httpRequest
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|httpResponse
operator|=
name|executeMethod
argument_list|(
name|httpRequest
argument_list|)
expr_stmt|;
name|int
name|responseCode
init|=
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
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
literal|"Http responseCode: "
operator|+
name|responseCode
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|throwException
operator|&&
operator|(
name|responseCode
operator|<
literal|100
operator|||
name|responseCode
operator|>=
literal|300
operator|)
condition|)
block|{
throw|throw
name|populateHttpOperationFailedException
argument_list|(
name|exchange
argument_list|,
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
name|responseCode
argument_list|)
throw|;
block|}
else|else
block|{
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
name|in
argument_list|,
name|strategy
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|httpResponse
operator|!=
literal|null
operator|&&
name|httpResponse
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|httpResponse
operator|.
name|getEntity
argument_list|()
operator|.
name|consumeContent
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// nothing we could do
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|HttpEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|HttpEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|populateResponse (Exchange exchange, HttpRequestBase httpRequest, HttpResponse httpResponse, Message in, HeaderFilterStrategy strategy, int responseCode)
specifier|protected
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpRequestBase
name|httpRequest
parameter_list|,
name|HttpResponse
name|httpResponse
parameter_list|,
name|Message
name|in
parameter_list|,
name|HeaderFilterStrategy
name|strategy
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setHeaders
argument_list|(
name|in
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|extractResponseBody
argument_list|(
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// propagate HTTP response headers
name|Header
index|[]
name|headers
init|=
name|httpResponse
operator|.
name|getAllHeaders
argument_list|()
decl_stmt|;
for|for
control|(
name|Header
name|header
range|:
name|headers
control|)
block|{
name|String
name|name
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|value
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|toLowerCase
argument_list|()
operator|.
name|equals
argument_list|(
literal|"content-type"
argument_list|)
condition|)
block|{
name|name
operator|=
name|Exchange
operator|.
name|CONTENT_TYPE
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|answer
operator|.
name|setHeader
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|populateHttpOperationFailedException (Exchange exchange, HttpRequestBase httpRequest, HttpResponse httpResponse, int responseCode)
specifier|protected
name|HttpOperationFailedException
name|populateHttpOperationFailedException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpRequestBase
name|httpRequest
parameter_list|,
name|HttpResponse
name|httpResponse
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpOperationFailedException
name|exception
decl_stmt|;
name|String
name|uri
init|=
name|httpRequest
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|statusText
init|=
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|!=
literal|null
condition|?
name|httpResponse
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
else|:
literal|null
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
init|=
name|extractResponseHeaders
argument_list|(
name|httpResponse
operator|.
name|getAllHeaders
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|extractResponseBody
argument_list|(
name|httpRequest
argument_list|,
name|httpResponse
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// make a defensive copy of the response body in the exception so its detached from the cache
name|String
name|copy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|copy
operator|=
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
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
name|Header
name|locationHeader
init|=
name|httpResponse
operator|.
name|getFirstHeader
argument_list|(
literal|"location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|locationHeader
operator|!=
literal|null
operator|&&
operator|(
name|responseCode
operator|>=
literal|300
operator|&&
name|responseCode
operator|<
literal|400
operator|)
condition|)
block|{
name|exception
operator|=
operator|new
name|HttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
name|locationHeader
operator|.
name|getValue
argument_list|()
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exception
operator|=
operator|new
name|HttpOperationFailedException
argument_list|(
name|uri
argument_list|,
name|responseCode
argument_list|,
name|statusText
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
return|return
name|exception
return|;
block|}
comment|/**      * Strategy when executing the method (calling the remote server).      *      * @param httpRequest the http Request to execute      * @return the response      * @throws IOException can be thrown      */
DECL|method|executeMethod (HttpUriRequest httpRequest)
specifier|protected
name|HttpResponse
name|executeMethod
parameter_list|(
name|HttpUriRequest
name|httpRequest
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|httpClient
operator|.
name|execute
argument_list|(
name|httpRequest
argument_list|)
return|;
block|}
comment|/**      * Extracts the response headers      *      * @param responseHeaders the headers      * @return the extracted headers or<tt>null</tt> if no headers existed      */
DECL|method|extractResponseHeaders (Header[] responseHeaders)
specifier|protected
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extractResponseHeaders
parameter_list|(
name|Header
index|[]
name|responseHeaders
parameter_list|)
block|{
if|if
condition|(
name|responseHeaders
operator|==
literal|null
operator|||
name|responseHeaders
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
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
for|for
control|(
name|Header
name|header
range|:
name|responseHeaders
control|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Extracts the response from the method as a InputStream.      *      * @param httpRequest the method that was executed      * @return the response as a stream      * @throws IOException can be thrown      */
DECL|method|extractResponseBody (HttpRequestBase httpRequest, HttpResponse httpResponse, Exchange exchange)
specifier|protected
specifier|static
name|InputStream
name|extractResponseBody
parameter_list|(
name|HttpRequestBase
name|httpRequest
parameter_list|,
name|HttpResponse
name|httpResponse
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpEntity
name|entity
init|=
name|httpResponse
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InputStream
name|is
init|=
name|entity
operator|.
name|getContent
argument_list|()
decl_stmt|;
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
name|Header
name|header
init|=
name|httpResponse
operator|.
name|getFirstHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
name|String
name|contentEncoding
init|=
name|header
operator|!=
literal|null
condition|?
name|header
operator|.
name|getValue
argument_list|()
else|:
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
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
name|is
operator|=
name|GZIPHelper
operator|.
name|uncompressGzip
argument_list|(
name|contentEncoding
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
comment|// Honor the character encoding
name|header
operator|=
name|httpRequest
operator|.
name|getFirstHeader
argument_list|(
literal|"content-type"
argument_list|)
expr_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|String
name|contentType
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
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
return|return
name|doExtractResponseBody
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
return|;
block|}
DECL|method|doExtractResponseBody (InputStream is, Exchange exchange)
specifier|private
specifier|static
name|InputStream
name|doExtractResponseBody
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
comment|// As httpclient is using a AutoCloseInputStream, it will be closed when the connection is closed
comment|// we need to cache the stream for it.
try|try
block|{
comment|// This CachedOutputStream will not be closed when the exchange is onCompletion
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|cos
argument_list|)
expr_stmt|;
comment|// When the InputStream is closed, the CachedOutputStream will be closed
return|return
name|cos
operator|.
name|getWrappedInputStream
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
literal|"Extracting response body"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates the HttpMethod to use to call the remote server, either its GET or POST.      *      * @param exchange the exchange      * @return the created method as either GET or POST      * @throws URISyntaxException is thrown if the URI is invalid      * @throws org.apache.camel.InvalidPayloadException is thrown if message body cannot      * be converted to a type supported by HttpClient      */
DECL|method|createMethod (Exchange exchange)
specifier|protected
name|HttpRequestBase
name|createMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|URISyntaxException
throws|,
name|InvalidPayloadException
block|{
name|String
name|url
init|=
name|HttpProducerHelper
operator|.
name|createURL
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|HttpEntity
name|requestEntity
init|=
name|createRequestEntity
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|HttpMethods
name|methodToUse
init|=
name|HttpProducerHelper
operator|.
name|createMethod
argument_list|(
name|exchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|requestEntity
operator|!=
literal|null
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
name|getEndpoint
argument_list|()
operator|.
name|getHttpUri
argument_list|()
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"://"
argument_list|)
operator|.
name|append
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|getPort
argument_list|()
operator|!=
operator|-
literal|1
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
operator|.
name|append
argument_list|(
name|uri
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|uri
operator|.
name|getRawPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|queryString
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
literal|'?'
argument_list|)
expr_stmt|;
name|builder
operator|.
name|append
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
block|}
name|HttpRequestBase
name|httpRequest
init|=
name|methodToUse
operator|.
name|createMethod
argument_list|(
name|builder
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|methodToUse
operator|.
name|isEntityEnclosing
argument_list|()
condition|)
block|{
operator|(
operator|(
name|HttpEntityEnclosingRequestBase
operator|)
name|httpRequest
operator|)
operator|.
name|setEntity
argument_list|(
name|requestEntity
argument_list|)
expr_stmt|;
if|if
condition|(
name|requestEntity
operator|!=
literal|null
operator|&&
name|requestEntity
operator|.
name|getContentType
argument_list|()
operator|==
literal|null
condition|)
block|{
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
literal|"No Content-Type provided for URL: "
operator|+
name|url
operator|+
literal|" with exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|httpRequest
return|;
block|}
comment|/**      * Creates a holder object for the data to send to the remote server.      *      * @param exchange the exchange with the IN message with data to send      * @return the data holder      * @throws org.apache.camel.InvalidPayloadException is thrown if message body cannot      * be converted to a type supported by HttpClient      */
DECL|method|createRequestEntity (Exchange exchange)
specifier|protected
name|HttpEntity
name|createRequestEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InvalidPayloadException
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HttpEntity
name|answer
init|=
name|in
operator|.
name|getBody
argument_list|(
name|HttpEntity
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|Object
name|data
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// file based (could potentially also be a FTP file etc)
if|if
condition|(
name|data
operator|instanceof
name|File
operator|||
name|data
operator|instanceof
name|GenericFile
condition|)
block|{
name|File
name|file
init|=
name|in
operator|.
name|getBody
argument_list|(
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|FileEntity
argument_list|(
name|file
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|data
operator|instanceof
name|String
condition|)
block|{
comment|// be a bit careful with String as any type can most likely be converted to String
comment|// so we only do an instanceof check and accept String if the body is really a String
comment|// do not fallback to use the default charset as it can influence the request
comment|// (for example application/x-www-form-urlencoded forms being sent)
name|String
name|charset
init|=
name|IOConverter
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|StringEntity
argument_list|(
operator|(
name|String
operator|)
name|data
argument_list|,
name|charset
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|StringEntity
operator|)
name|answer
operator|)
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fallback as input stream
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// force the body as an input stream since this is the fallback
name|InputStream
name|is
init|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|InputStreamEntity
argument_list|(
name|in
operator|.
name|getBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
operator|(
operator|(
name|InputStreamEntity
operator|)
name|answer
operator|)
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getHttpClient ()
specifier|public
name|HttpClient
name|getHttpClient
parameter_list|()
block|{
return|return
name|httpClient
return|;
block|}
DECL|method|setHttpClient (HttpClient httpClient)
specifier|public
name|void
name|setHttpClient
parameter_list|(
name|HttpClient
name|httpClient
parameter_list|)
block|{
name|this
operator|.
name|httpClient
operator|=
name|httpClient
expr_stmt|;
block|}
block|}
end_class

end_unit

