begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Serializable
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
name|Iterator
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
name|CamelExchangeException
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
name|http
operator|.
name|helper
operator|.
name|HttpHelper
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
name|MessageHelper
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
name|apache
operator|.
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|HttpMethod
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
name|httpclient
operator|.
name|HttpVersion
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
name|httpclient
operator|.
name|methods
operator|.
name|ByteArrayRequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|EntityEnclosingMethod
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
name|httpclient
operator|.
name|methods
operator|.
name|FileRequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|InputStreamRequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
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
name|httpclient
operator|.
name|methods
operator|.
name|StringRequestEntity
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
name|httpclient
operator|.
name|params
operator|.
name|HttpMethodParams
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
comment|/**  * @version   */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
DECL|field|transferException
specifier|private
name|boolean
name|transferException
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
name|createHttpClient
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
name|this
operator|.
name|transferException
operator|=
name|endpoint
operator|.
name|isTransferException
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
comment|// if we bridge endpoint then we need to skip matching headers with the HTTP_QUERY to avoid sending
comment|// duplicated headers to the receiver, so use this skipRequestHeaders as the list of headers to skip
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|skipRequestHeaders
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
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
operator|!=
literal|null
condition|)
block|{
name|skipRequestHeaders
operator|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
block|}
comment|// Need to remove the Host key as it should be not used
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|remove
argument_list|(
literal|"host"
argument_list|)
expr_stmt|;
block|}
name|HttpMethod
name|method
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
name|HttpMethodParams
name|params
init|=
name|method
operator|.
name|getParams
argument_list|()
decl_stmt|;
name|params
operator|.
name|setVersion
argument_list|(
name|HttpVersion
operator|.
name|parse
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
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerValue
operator|!=
literal|null
condition|)
block|{
comment|// use an iterator as there can be multiple values. (must not use a delimiter, and allow empty values)
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|headerValue
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// the value to add as request header
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// if its a multi value then check each value if we can add it and for multi values they
comment|// should be combined into a single value
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|value
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
name|String
operator|.
name|class
argument_list|,
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
comment|// we should not add headers for the parameters in the uri if we bridge the endpoint
comment|// as then we would duplicate headers on both the endpoint uri, and in HTTP headers as well
if|if
condition|(
name|skipRequestHeaders
operator|!=
literal|null
operator|&&
name|skipRequestHeaders
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|strategy
operator|!=
literal|null
operator|&&
operator|!
name|strategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|key
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|// add the value(s) as a http request header
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// use the default toString of a ArrayList to create in the form [xxx, yyy]
comment|// if multi valued, for a single value, then just output the value as is
name|String
name|s
init|=
name|values
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|?
name|values
operator|.
name|toString
argument_list|()
else|:
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|method
operator|.
name|addRequestHeader
argument_list|(
name|key
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// lets store the result in the output message.
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
literal|"Executing http {} method: {}"
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|,
name|method
operator|.
name|getURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|responseCode
init|=
name|executeMethod
argument_list|(
name|method
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Http responseCode: {}"
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|throwException
condition|)
block|{
comment|// if we do not use failed exception then populate response for all response codes
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|method
argument_list|,
name|in
argument_list|,
name|strategy
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|responseCode
operator|>=
literal|100
operator|&&
name|responseCode
operator|<
literal|300
condition|)
block|{
comment|// only populate response for OK response
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|method
argument_list|,
name|in
argument_list|,
name|strategy
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// operation failed so populate exception to throw
throw|throw
name|populateHttpOperationFailedException
argument_list|(
name|exchange
argument_list|,
name|method
argument_list|,
name|responseCode
argument_list|)
throw|;
block|}
block|}
block|}
finally|finally
block|{
name|method
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
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
DECL|method|populateResponse (Exchange exchange, HttpMethod method, Message in, HeaderFilterStrategy strategy, int responseCode)
specifier|protected
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpMethod
name|method
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
throws|,
name|ClassNotFoundException
block|{
comment|//We just make the out message is not create when extractResponseBody throws exception,
name|Object
name|response
init|=
name|extractResponseBody
argument_list|(
name|method
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
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
name|response
argument_list|)
expr_stmt|;
comment|// propagate HTTP response headers
name|Header
index|[]
name|headers
init|=
name|method
operator|.
name|getResponseHeaders
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
comment|// use http helper to extract parameter value as it may contain multiple values
name|Object
name|extracted
init|=
name|HttpHelper
operator|.
name|extractHttpParameterValue
argument_list|(
name|value
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
name|applyFilterToExternalHeaders
argument_list|(
name|name
argument_list|,
name|extracted
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|HttpHelper
operator|.
name|appendHeader
argument_list|(
name|answer
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|name
argument_list|,
name|extracted
argument_list|)
expr_stmt|;
block|}
block|}
comment|// preserve headers from in by copying any non existing headers
comment|// to avoid overriding existing headers with old values
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|answer
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|populateHttpOperationFailedException (Exchange exchange, HttpMethod method, int responseCode)
specifier|protected
name|Exception
name|populateHttpOperationFailedException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HttpMethod
name|method
parameter_list|,
name|int
name|responseCode
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|Exception
name|answer
decl_stmt|;
name|String
name|uri
init|=
name|method
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
name|method
operator|.
name|getStatusLine
argument_list|()
operator|!=
literal|null
condition|?
name|method
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
name|method
operator|.
name|getResponseHeaders
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|responseBody
init|=
name|extractResponseBody
argument_list|(
name|method
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|transferException
operator|&&
name|responseBody
operator|!=
literal|null
operator|&&
name|responseBody
operator|instanceof
name|Exception
condition|)
block|{
comment|// if the response was a serialized exception then use that
return|return
operator|(
name|Exception
operator|)
name|responseBody
return|;
block|}
comment|// make a defensive copy of the response body in the exception so its detached from the cache
name|String
name|copy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|responseBody
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
name|responseBody
argument_list|)
expr_stmt|;
block|}
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
decl_stmt|;
name|Header
name|locationHeader
init|=
name|method
operator|.
name|getResponseHeader
argument_list|(
literal|"location"
argument_list|)
decl_stmt|;
if|if
condition|(
name|locationHeader
operator|!=
literal|null
condition|)
block|{
name|redirectLocation
operator|=
name|locationHeader
operator|.
name|getValue
argument_list|()
expr_stmt|;
name|answer
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
name|redirectLocation
argument_list|,
name|headers
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no redirect location
name|answer
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
block|}
else|else
block|{
comment|// internal server error (error code 500)
name|answer
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
name|answer
return|;
block|}
comment|/**      * Strategy when executing the method (calling the remote server).      *      * @param method the method to execute      * @return the response code      * @throws IOException can be thrown      */
DECL|method|executeMethod (HttpMethod method)
specifier|protected
name|int
name|executeMethod
parameter_list|(
name|HttpMethod
name|method
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|httpClient
operator|.
name|executeMethod
argument_list|(
name|method
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
comment|/**      * Extracts the response from the method as a InputStream.      *      * @param method the method that was executed      * @return the response either as a stream, or as a deserialized java object      * @throws IOException can be thrown      */
DECL|method|extractResponseBody (HttpMethod method, Exchange exchange)
specifier|protected
specifier|static
name|Object
name|extractResponseBody
parameter_list|(
name|HttpMethod
name|method
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|ClassNotFoundException
block|{
name|InputStream
name|is
init|=
name|method
operator|.
name|getResponseBodyAsStream
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
name|method
operator|.
name|getResponseHeader
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
name|String
name|contentType
init|=
literal|null
decl_stmt|;
name|header
operator|=
name|method
operator|.
name|getResponseHeader
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
name|contentType
operator|=
name|header
operator|.
name|getValue
argument_list|()
expr_stmt|;
comment|// find the charset and set it to the Exchange
name|HttpHelper
operator|.
name|setCharsetFromContentType
argument_list|(
name|contentType
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|response
init|=
name|doExtractResponseBodyAsStream
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// if content type is a serialized java object then de-serialize it back to a Java object
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|contentType
operator|.
name|equals
argument_list|(
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
condition|)
block|{
return|return
name|HttpHelper
operator|.
name|deserializeJavaObjectFromStream
argument_list|(
name|response
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|response
return|;
block|}
block|}
DECL|method|doExtractResponseBodyAsStream (InputStream is, Exchange exchange)
specifier|private
specifier|static
name|InputStream
name|doExtractResponseBodyAsStream
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
name|CachedOutputStream
name|cos
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// This CachedOutputStream will not be closed when the exchange is onCompletion
name|cos
operator|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|// try to close the CachedOutputStream when we get the IOException
try|try
block|{
name|cos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignore
parameter_list|)
block|{
comment|//do nothing here
block|}
throw|throw
name|ex
throw|;
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
comment|/**      * Creates the HttpMethod to use to call the remote server, either its GET or POST.      *      * @param exchange the exchange      * @return the created method as either GET or POST      * @throws CamelExchangeException is thrown if error creating RequestEntity      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|createMethod (Exchange exchange)
specifier|protected
name|HttpMethod
name|createMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// creating the url to use takes 2-steps
name|String
name|url
init|=
name|HttpHelper
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
name|HttpHelper
operator|.
name|createURI
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
comment|// get the url and query string from the uri
name|url
operator|=
name|uri
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
name|String
name|queryString
init|=
name|uri
operator|.
name|getRawQuery
argument_list|()
decl_stmt|;
comment|// execute any custom url rewrite
name|String
name|rewriteUrl
init|=
name|HttpHelper
operator|.
name|urlRewrite
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|rewriteUrl
operator|!=
literal|null
condition|)
block|{
comment|// update url and query string from the rewritten url
name|url
operator|=
name|rewriteUrl
expr_stmt|;
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// use raw query to have uri decimal encoded which http client requires
name|queryString
operator|=
name|uri
operator|.
name|getRawQuery
argument_list|()
expr_stmt|;
block|}
comment|// remove query string as http client does not accept that
if|if
condition|(
name|url
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|url
operator|=
name|url
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|url
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// create http holder objects for the request
name|RequestEntity
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
name|HttpHelper
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
name|HttpMethod
name|method
init|=
name|methodToUse
operator|.
name|createMethod
argument_list|(
name|url
argument_list|)
decl_stmt|;
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
name|encode
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
name|method
operator|.
name|setQueryString
argument_list|(
name|queryString
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using URL: {} with method: {}"
argument_list|,
name|url
argument_list|,
name|method
argument_list|)
expr_stmt|;
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
name|EntityEnclosingMethod
operator|)
name|method
operator|)
operator|.
name|setRequestEntity
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"No Content-Type provided for URL: {} with exchange: {}"
argument_list|,
name|url
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// there must be a host on the method
if|if
condition|(
name|method
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid uri: "
operator|+
name|url
operator|+
literal|". If you are forwarding/bridging http endpoints, then enable the bridgeEndpoint option on the endpoint: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|method
return|;
block|}
comment|/**      * Creates a holder object for the data to send to the remote server.      *      * @param exchange the exchange with the IN message with data to send      * @return the data holder      * @throws CamelExchangeException is thrown if error creating RequestEntity      */
DECL|method|createRequestEntity (Exchange exchange)
specifier|protected
name|RequestEntity
name|createRequestEntity
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelExchangeException
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
name|RequestEntity
name|answer
init|=
name|in
operator|.
name|getBody
argument_list|(
name|RequestEntity
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
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
operator|.
name|equals
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
comment|// serialized java object
name|Serializable
name|obj
init|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|Serializable
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// write object to output stream
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|HttpHelper
operator|.
name|writeObjectToStream
argument_list|(
name|bos
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|ByteArrayRequestEntity
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|HttpConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|bos
argument_list|)
expr_stmt|;
block|}
elseif|else
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
comment|// file based (could potentially also be a FTP file etc)
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
name|FileRequestEntity
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
name|IOHelper
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
name|StringRequestEntity
argument_list|(
operator|(
name|String
operator|)
name|data
argument_list|,
name|contentType
argument_list|,
name|charset
argument_list|)
expr_stmt|;
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
name|InputStreamRequestEntity
argument_list|(
name|is
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
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
name|CamelExchangeException
argument_list|(
literal|"Error creating RequestEntity from message body"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error serializing message body"
argument_list|,
name|exchange
argument_list|,
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

