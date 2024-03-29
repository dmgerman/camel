begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|LinkedHashMap
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
name|StringJoiner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpHeaders
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
name|ahc
operator|.
name|helper
operator|.
name|AhcHelper
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
name|asynchttpclient
operator|.
name|HttpResponseStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|RequestBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|request
operator|.
name|body
operator|.
name|generator
operator|.
name|BodyGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|request
operator|.
name|body
operator|.
name|generator
operator|.
name|FileBodyGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|request
operator|.
name|body
operator|.
name|generator
operator|.
name|InputStreamBodyGenerator
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
DECL|class|DefaultAhcBinding
specifier|public
class|class
name|DefaultAhcBinding
implements|implements
name|AhcBinding
block|{
DECL|field|log
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|httpProtocolHeaderFilterStrategy
specifier|protected
name|HeaderFilterStrategy
name|httpProtocolHeaderFilterStrategy
init|=
operator|new
name|HttpProtocolHeaderFilterStrategy
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|prepareRequest (AhcEndpoint endpoint, Exchange exchange)
specifier|public
name|Request
name|prepareRequest
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
if|if
condition|(
name|endpoint
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
name|RequestBuilder
name|builder
init|=
operator|new
name|RequestBuilder
argument_list|()
decl_stmt|;
name|URI
name|uri
decl_stmt|;
try|try
block|{
comment|// creating the url to use takes 2-steps
name|String
name|url
init|=
name|AhcHelper
operator|.
name|createURL
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|uri
operator|=
name|AhcHelper
operator|.
name|createURI
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// get the url from the uri
name|url
operator|=
name|uri
operator|.
name|toASCIIString
argument_list|()
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Setting url {}"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setUrl
argument_list|(
name|url
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
name|CamelExchangeException
argument_list|(
literal|"Error creating URL"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|String
name|method
init|=
name|extractMethod
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Setting method {}"
argument_list|,
name|method
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|populateHeaders
argument_list|(
name|builder
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|populateBody
argument_list|(
name|builder
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|extractMethod (Exchange exchange)
specifier|protected
name|String
name|extractMethod
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// prefer method from header
name|String
name|method
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
return|return
name|method
return|;
block|}
comment|// if there is a body then do a POST otherwise a GET
name|boolean
name|hasBody
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
decl_stmt|;
return|return
name|hasBody
condition|?
literal|"POST"
else|:
literal|"GET"
return|;
block|}
DECL|method|populateHeaders (RequestBuilder builder, AhcEndpoint endpoint, Exchange exchange, URI uri)
specifier|protected
name|void
name|populateHeaders
parameter_list|(
name|RequestBuilder
name|builder
parameter_list|,
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|URI
name|uri
parameter_list|)
throws|throws
name|CamelExchangeException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|HeaderFilterStrategy
name|strategy
init|=
name|endpoint
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Adding header {} = {}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|put
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
if|if
condition|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|cookieHeaders
init|=
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|loadCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|cookieHeaders
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
name|StringJoiner
name|joiner
init|=
operator|new
name|StringJoiner
argument_list|(
literal|"; "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
name|joiner
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Adding header {} = {}"
argument_list|,
name|key
argument_list|,
name|joiner
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|joiner
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
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
name|CamelExchangeException
argument_list|(
literal|"Error loading cookies"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|.
name|isConnectionClose
argument_list|()
condition|)
block|{
name|builder
operator|.
name|addHeader
argument_list|(
literal|"Connection"
argument_list|,
literal|"close"
argument_list|)
expr_stmt|;
block|}
name|headers
operator|.
name|forEach
argument_list|(
name|builder
operator|::
name|addHeader
argument_list|)
expr_stmt|;
block|}
DECL|method|populateBody (RequestBuilder builder, AhcEndpoint endpoint, Exchange exchange)
specifier|protected
name|void
name|populateBody
parameter_list|(
name|RequestBuilder
name|builder
parameter_list|,
name|AhcEndpoint
name|endpoint
parameter_list|,
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
return|return;
block|}
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
name|BodyGenerator
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|BodyGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|charset
init|=
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
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
if|if
condition|(
name|contentType
operator|!=
literal|null
operator|&&
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
operator|.
name|equals
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|isAllowJavaSerializedObject
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Content-type "
operator|+
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
operator|+
literal|" is not allowed"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
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
argument_list|(
name|endpoint
operator|.
name|getBufferSize
argument_list|()
argument_list|)
decl_stmt|;
name|AhcHelper
operator|.
name|writeObjectToStream
argument_list|(
name|bos
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|bos
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|body
operator|=
operator|new
name|InputStreamBodyGenerator
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|bytes
argument_list|)
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
name|body
operator|=
operator|new
name|FileBodyGenerator
argument_list|(
name|file
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
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|body
operator|=
operator|new
name|InputStreamBodyGenerator
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|String
operator|)
name|data
operator|)
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
operator|new
name|InputStreamBodyGenerator
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|String
operator|)
name|data
operator|)
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fallback as input stream
if|if
condition|(
name|body
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
name|body
operator|=
operator|new
name|InputStreamBodyGenerator
argument_list|(
name|is
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
literal|"Error creating BodyGenerator from message body"
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
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Setting body {}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Setting body charset {}"
argument_list|,
name|charset
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setCharset
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charset
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// must set content type, even if its null, otherwise it may default to
comment|// application/x-www-form-urlencoded which may not be your intention
name|log
operator|.
name|trace
argument_list|(
literal|"Setting Content-Type {}"
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contentType
argument_list|)
condition|)
block|{
name|builder
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|contentType
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onThrowable (AhcEndpoint endpoint, Exchange exchange, Throwable t)
specifier|public
name|void
name|onThrowable
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|t
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStatusReceived (AhcEndpoint endpoint, Exchange exchange, HttpResponseStatus responseStatus)
specifier|public
name|void
name|onStatusReceived
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpResponseStatus
name|responseStatus
parameter_list|)
throws|throws
name|Exception
block|{
comment|// preserve headers from in by copying any non existing headers
comment|// to avoid overriding existing headers with old values
comment|// Just filter the http protocol headers
name|MessageHelper
operator|.
name|copyHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|httpProtocolHeaderFilterStrategy
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|responseStatus
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_TEXT
argument_list|,
name|responseStatus
operator|.
name|getStatusText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onHeadersReceived (AhcEndpoint endpoint, Exchange exchange, HttpHeaders headers)
specifier|public
name|void
name|onHeadersReceived
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|HttpHeaders
name|headers
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|m
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|headers
operator|.
name|names
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|getAll
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|values
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
name|m
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
comment|// handle cookies
if|if
condition|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// creating the url to use takes 2-steps
name|String
name|url
init|=
name|AhcHelper
operator|.
name|createURL
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|URI
name|uri
init|=
name|AhcHelper
operator|.
name|createURI
argument_list|(
name|exchange
argument_list|,
name|url
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|storeCookies
argument_list|(
name|exchange
argument_list|,
name|uri
argument_list|,
name|m
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
name|CamelExchangeException
argument_list|(
literal|"Error storing cookies"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onComplete (AhcEndpoint endpoint, Exchange exchange, String url, ByteArrayOutputStream os, int contentLength, int statusCode, String statusText)
specifier|public
name|void
name|onComplete
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|ByteArrayOutputStream
name|os
parameter_list|,
name|int
name|contentLength
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|statusText
parameter_list|)
throws|throws
name|Exception
block|{
comment|// copy from output stream to input stream
name|os
operator|.
name|flush
argument_list|()
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|contentEncoding
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
comment|// find the charset and set it to the Exchange
name|AhcHelper
operator|.
name|setCharsetFromContentType
argument_list|(
name|contentType
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|is
decl_stmt|;
comment|// if content type is a serialized java object then de-serialize it back to a Java object but only if its allowed
comment|// an exception can also be transferred as java object
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
name|AhcConstants
operator|.
name|CONTENT_TYPE_JAVA_SERIALIZED_OBJECT
argument_list|)
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|isAllowJavaSerializedObject
argument_list|()
operator|||
name|endpoint
operator|.
name|isTransferException
argument_list|()
condition|)
block|{
name|body
operator|=
name|AhcHelper
operator|.
name|deserializeJavaObjectFromStream
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isThrowExceptionOnFailure
argument_list|()
condition|)
block|{
comment|// if we do not use failed exception then populate response for all response codes
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|contentLength
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|statusCode
operator|>=
literal|100
operator|&&
name|statusCode
operator|<
literal|300
condition|)
block|{
comment|// only populate response for OK response
name|populateResponse
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|,
name|contentLength
argument_list|,
name|statusCode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// operation failed so populate exception to throw
throw|throw
name|populateHttpOperationFailedException
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|url
argument_list|,
name|body
argument_list|,
name|contentLength
argument_list|,
name|statusCode
argument_list|,
name|statusText
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|populateHttpOperationFailedException (AhcEndpoint endpoint, Exchange exchange, String url, Object body, int contentLength, int statusCode, String statusText)
specifier|private
name|Exception
name|populateHttpOperationFailedException
parameter_list|(
name|AhcEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|url
parameter_list|,
name|Object
name|body
parameter_list|,
name|int
name|contentLength
parameter_list|,
name|int
name|statusCode
parameter_list|,
name|String
name|statusText
parameter_list|)
block|{
name|Exception
name|answer
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isTransferException
argument_list|()
operator|&&
name|body
operator|!=
literal|null
operator|&&
name|body
operator|instanceof
name|Exception
condition|)
block|{
comment|// if the response was a serialized exception then use that
return|return
operator|(
name|Exception
operator|)
name|body
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
name|body
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
name|body
argument_list|)
expr_stmt|;
block|}
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
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|statusCode
operator|>=
literal|300
operator|&&
name|statusCode
operator|<
literal|400
condition|)
block|{
name|String
name|redirectLocation
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"Location"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|redirectLocation
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|AhcOperationFailedException
argument_list|(
name|url
argument_list|,
name|statusCode
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
name|AhcOperationFailedException
argument_list|(
name|url
argument_list|,
name|statusCode
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
name|AhcOperationFailedException
argument_list|(
name|url
argument_list|,
name|statusCode
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
DECL|method|extractResponseHeaders (Exchange exchange)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extractResponseHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
name|exchange
operator|.
name|getOut
argument_list|()
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
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|populateResponse (Exchange exchange, Object body, int contentLength, int responseCode)
specifier|private
name|void
name|populateResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|int
name|contentLength
parameter_list|,
name|int
name|responseCode
parameter_list|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
name|contentLength
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

