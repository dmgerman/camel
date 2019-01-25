begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sparkrest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sparkrest
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
name|ObjectOutputStream
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
name|UnsupportedEncodingException
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
name|TypeConverter
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
name|support
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
name|support
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
name|support
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
name|IOHelper
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

begin_import
import|import
name|spark
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|spark
operator|.
name|Response
import|;
end_import

begin_class
DECL|class|DefaultSparkBinding
specifier|public
class|class
name|DefaultSparkBinding
implements|implements
name|SparkBinding
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
name|DefaultSparkBinding
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
operator|new
name|SparkHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|toCamelMessage (Request request, Exchange exchange, SparkConfiguration configuration)
specifier|public
name|Message
name|toCamelMessage
parameter_list|(
name|Request
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"toCamelMessage: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|SparkMessage
name|answer
init|=
operator|new
name|SparkMessage
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|request
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isMapHeaders
argument_list|()
condition|)
block|{
name|populateCamelHeaders
argument_list|(
name|request
argument_list|,
name|answer
operator|.
name|getHeaders
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|isDisableStreamCache
argument_list|()
condition|)
block|{
comment|// keep the body as a input stream
name|answer
operator|.
name|setBody
argument_list|(
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|setBody
argument_list|(
name|request
operator|.
name|body
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|populateCamelHeaders (Request request, Map<String, Object> headers, Exchange exchange, SparkConfiguration configuration)
specifier|public
name|void
name|populateCamelHeaders
parameter_list|(
name|Request
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getPathInfo
argument_list|()
decl_stmt|;
name|SparkEndpoint
name|endpoint
init|=
operator|(
name|SparkEndpoint
operator|)
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// need to match by lower case as we want to ignore case on context-path
name|String
name|endpointPath
init|=
name|endpoint
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|matchPath
init|=
name|path
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
name|String
name|match
init|=
name|endpointPath
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|.
name|endsWith
argument_list|(
literal|"/*"
argument_list|)
condition|)
block|{
name|match
operator|=
name|match
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|match
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|match
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|match
operator|=
literal|"/"
operator|+
name|match
expr_stmt|;
block|}
if|if
condition|(
name|matchPath
operator|.
name|startsWith
argument_list|(
name|match
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|match
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|headers
operator|.
name|put
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|path
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|key
range|:
name|request
operator|.
name|attributes
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|request
operator|.
name|attribute
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|decoded
init|=
name|shouldUrlDecodeHeader
argument_list|(
name|configuration
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|decoded
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|SparkHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|key
range|:
name|request
operator|.
name|headers
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|request
operator|.
name|headers
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|decoded
init|=
name|shouldUrlDecodeHeader
argument_list|(
name|configuration
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|decoded
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|SparkHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|request
operator|.
name|params
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|mapKey
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Object
name|decoded
init|=
name|shouldUrlDecodeHeader
argument_list|(
name|configuration
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|decoded
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|SparkHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|String
name|key
range|:
name|request
operator|.
name|queryParams
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|request
operator|.
name|queryParams
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|Object
name|decoded
init|=
name|shouldUrlDecodeHeader
argument_list|(
name|configuration
argument_list|,
name|key
argument_list|,
name|value
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|decoded
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|SparkHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|decoded
argument_list|)
expr_stmt|;
block|}
block|}
name|String
index|[]
name|splat
init|=
name|request
operator|.
name|splat
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|SparkConstants
operator|.
name|SPLAT
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|key
argument_list|,
name|splat
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|SparkHelper
operator|.
name|appendHeader
argument_list|(
name|headers
argument_list|,
name|key
argument_list|,
name|splat
argument_list|)
expr_stmt|;
block|}
comment|// store the method and query and other info in headers as String types
name|headers
operator|.
name|putIfAbsent
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putIfAbsent
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getQueryString
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putIfAbsent
argument_list|(
name|Exchange
operator|.
name|HTTP_URL
argument_list|,
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getRequestURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putIfAbsent
argument_list|(
name|Exchange
operator|.
name|HTTP_URI
argument_list|,
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getRequestURI
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|putIfAbsent
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|request
operator|.
name|raw
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toSparkResponse (Message message, Response response, SparkConfiguration configuration)
specifier|public
name|void
name|toSparkResponse
parameter_list|(
name|Message
name|message
parameter_list|,
name|Response
name|response
parameter_list|,
name|SparkConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"toSparkResponse: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
comment|// the response code is 200 for OK and 500 for failed
name|boolean
name|failed
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|isFailed
argument_list|()
decl_stmt|;
name|int
name|defaultCode
init|=
name|failed
condition|?
literal|500
else|:
literal|200
decl_stmt|;
name|int
name|code
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|defaultCode
argument_list|,
name|int
operator|.
name|class
argument_list|)
decl_stmt|;
name|response
operator|.
name|status
argument_list|(
name|code
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"HTTP Status Code: {}"
argument_list|,
name|code
argument_list|)
expr_stmt|;
name|TypeConverter
name|tc
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
comment|// append headers
comment|// must use entrySet to ensure case of keys is preserved
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
name|message
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
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|Exchange
operator|.
name|CONTENT_TYPE
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// we set content-type later
continue|continue;
block|}
comment|// use an iterator as there can be multiple values. (must not use a delimiter)
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
name|value
argument_list|,
literal|null
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|headerValue
init|=
name|tc
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
if|if
condition|(
name|headerValue
operator|!=
literal|null
operator|&&
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|key
argument_list|,
name|headerValue
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"HTTP-Header: {}={}"
argument_list|,
name|key
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
name|response
operator|.
name|header
argument_list|(
name|key
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// set the content type in the response.
name|String
name|contentType
init|=
name|MessageHelper
operator|.
name|getContentType
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
comment|// set content-type
name|response
operator|.
name|header
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Content-Type: {}"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|message
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Exception
name|cause
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getException
argument_list|()
decl_stmt|;
comment|// if there was an exception then use that as body
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|isTransferException
argument_list|()
condition|)
block|{
comment|// we failed due an exception, and transfer it as java serialized object
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ObjectOutputStream
name|oos
init|=
operator|new
name|ObjectOutputStream
argument_list|(
name|bos
argument_list|)
decl_stmt|;
name|oos
operator|.
name|writeObject
argument_list|(
name|cause
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
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|body
operator|=
name|bos
operator|.
name|toByteArray
argument_list|()
expr_stmt|;
comment|// force content type to be serialized java object
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|SparkConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we failed due an exception so print it as plain text
name|StringWriter
name|sw
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|sw
argument_list|)
decl_stmt|;
name|cause
operator|.
name|printStackTrace
argument_list|(
name|pw
argument_list|)
expr_stmt|;
comment|// the body should then be the stacktrace
name|body
operator|=
name|sw
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
expr_stmt|;
comment|// force content type to be text/plain as that is what the stacktrace is
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
block|}
comment|// and mark the exception as failure handled, as we handled it by returning it as the response
name|ExchangeHelper
operator|.
name|setFailureHandled
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|String
name|str
init|=
name|tc
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|message
operator|.
name|getExchange
argument_list|()
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|response
operator|.
name|body
argument_list|(
name|str
argument_list|)
expr_stmt|;
comment|// and must set body to the response body as Spark otherwise may output something else
name|message
operator|.
name|setBody
argument_list|(
name|str
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Decodes the header if needed to, or returns the header value as is.      *      * @param configuration the configuration      * @param headerName    the header name      * @param value         the current header value      * @param charset       the charset to use for decoding      * @return the decoded value (if decoded was needed) or a<tt>toString</tt> representation of the value.      * @throws java.io.UnsupportedEncodingException is thrown if error decoding.      */
DECL|method|shouldUrlDecodeHeader (SparkConfiguration configuration, String headerName, Object value, String charset)
specifier|protected
name|String
name|shouldUrlDecodeHeader
parameter_list|(
name|SparkConfiguration
name|configuration
parameter_list|,
name|String
name|headerName
parameter_list|,
name|Object
name|value
parameter_list|,
name|String
name|charset
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
comment|// do not decode Content-Type
if|if
condition|(
name|Exchange
operator|.
name|CONTENT_TYPE
operator|.
name|equals
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|isUrlDecodeHeaders
argument_list|()
condition|)
block|{
return|return
name|URLDecoder
operator|.
name|decode
argument_list|(
name|value
operator|.
name|toString
argument_list|()
argument_list|,
name|charset
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|value
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
DECL|method|mapKey (String key)
specifier|protected
name|String
name|mapKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|key
operator|.
name|startsWith
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
return|return
name|key
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|key
return|;
block|}
block|}
block|}
end_class

end_unit

