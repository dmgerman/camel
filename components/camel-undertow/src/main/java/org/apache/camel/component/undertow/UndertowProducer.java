begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|Reader
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
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|ClientRequest
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|client
operator|.
name|UndertowClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|protocols
operator|.
name|ssl
operator|.
name|UndertowXnioSsl
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|DefaultByteBufferPool
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|HeaderMap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Headers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|HttpString
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
name|AsyncCallback
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
name|component
operator|.
name|undertow
operator|.
name|handlers
operator|.
name|CamelWebSocketHandler
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
name|http
operator|.
name|common
operator|.
name|cookie
operator|.
name|CookieHandler
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
name|DefaultAsyncProducer
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
name|xnio
operator|.
name|OptionMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|Xnio
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|XnioWorker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xnio
operator|.
name|ssl
operator|.
name|XnioSsl
import|;
end_import

begin_comment
comment|/**  * The Undertow producer.  *  * The implementation of Producer is considered as experimental. The Undertow  * client classes are not thread safe, their purpose is for the reverse proxy  * usage inside Undertow itself. This may change in the future versions and  * general purpose HTTP client wrapper will be added. Therefore this Producer  * may be changed too.  */
end_comment

begin_class
DECL|class|UndertowProducer
specifier|public
class|class
name|UndertowProducer
extends|extends
name|DefaultAsyncProducer
block|{
DECL|field|client
specifier|private
name|UndertowClient
name|client
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|UndertowEndpoint
name|endpoint
decl_stmt|;
DECL|field|options
specifier|private
specifier|final
name|OptionMap
name|options
decl_stmt|;
DECL|field|pool
specifier|private
name|DefaultByteBufferPool
name|pool
decl_stmt|;
DECL|field|ssl
specifier|private
name|XnioSsl
name|ssl
decl_stmt|;
DECL|field|worker
specifier|private
name|XnioWorker
name|worker
decl_stmt|;
DECL|field|webSocketHandler
specifier|private
name|CamelWebSocketHandler
name|webSocketHandler
decl_stmt|;
DECL|method|UndertowProducer (final UndertowEndpoint endpoint, final OptionMap options)
specifier|public
name|UndertowProducer
parameter_list|(
specifier|final
name|UndertowEndpoint
name|endpoint
parameter_list|,
specifier|final
name|OptionMap
name|options
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|options
operator|=
name|options
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|UndertowEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|isSendToAll (Message in)
name|boolean
name|isSendToAll
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
comment|// header may be null; have to be careful here (and fallback to use sendToAll option configured from endpoint)
name|Boolean
name|value
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|UndertowConstants
operator|.
name|SEND_TO_ALL
argument_list|,
name|endpoint
operator|.
name|getSendToAll
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
literal|false
else|:
name|value
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange camelExchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isWebSocket
argument_list|()
condition|)
block|{
return|return
name|processWebSocket
argument_list|(
name|camelExchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/* not a WebSocket */
specifier|final
name|URI
name|uri
decl_stmt|;
specifier|final
name|HttpString
name|method
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|exchangeUri
init|=
name|UndertowHelper
operator|.
name|createURL
argument_list|(
name|camelExchange
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|uri
operator|=
name|UndertowHelper
operator|.
name|createURI
argument_list|(
name|camelExchange
argument_list|,
name|exchangeUri
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|method
operator|=
name|UndertowHelper
operator|.
name|createMethod
argument_list|(
name|camelExchange
argument_list|,
name|endpoint
argument_list|,
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|URISyntaxException
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
specifier|final
name|String
name|pathAndQuery
init|=
name|URISupport
operator|.
name|pathAndQueryOf
argument_list|(
name|uri
argument_list|)
decl_stmt|;
specifier|final
name|UndertowHttpBinding
name|undertowHttpBinding
init|=
name|endpoint
operator|.
name|getUndertowHttpBinding
argument_list|()
decl_stmt|;
specifier|final
name|CookieHandler
name|cookieHandler
init|=
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
decl_stmt|;
specifier|final
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
decl_stmt|;
if|if
condition|(
name|cookieHandler
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|cookieHeaders
operator|=
name|cookieHandler
operator|.
name|loadCookies
argument_list|(
name|camelExchange
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|IOException
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
name|cookieHeaders
operator|=
name|Collections
operator|.
name|emptyMap
argument_list|()
expr_stmt|;
block|}
specifier|final
name|ClientRequest
name|request
init|=
operator|new
name|ClientRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
name|request
operator|.
name|setPath
argument_list|(
name|pathAndQuery
argument_list|)
expr_stmt|;
specifier|final
name|HeaderMap
name|requestHeaders
init|=
name|request
operator|.
name|getRequestHeaders
argument_list|()
decl_stmt|;
comment|// Set the Host header
specifier|final
name|Message
name|message
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|String
name|host
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|Headers
operator|.
name|HOST_STRING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isPreserveHostHeader
argument_list|()
condition|)
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|HOST
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|host
argument_list|)
operator|.
name|orElseGet
argument_list|(
name|uri
operator|::
name|getAuthority
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|HOST
argument_list|,
name|uri
operator|.
name|getAuthority
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|cookieHeaders
operator|.
name|forEach
argument_list|(
parameter_list|(
name|key
parameter_list|,
name|values
parameter_list|)
lambda|->
block|{
name|requestHeaders
operator|.
name|putAll
argument_list|(
name|HttpString
operator|.
name|tryFromString
argument_list|(
name|key
argument_list|)
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
specifier|final
name|Object
name|body
init|=
name|undertowHttpBinding
operator|.
name|toHttpRequest
argument_list|(
name|request
argument_list|,
name|camelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|UndertowClientCallback
name|clientCallback
decl_stmt|;
specifier|final
name|boolean
name|streaming
init|=
name|getEndpoint
argument_list|()
operator|.
name|isUseStreaming
argument_list|()
decl_stmt|;
if|if
condition|(
name|streaming
operator|&&
operator|(
name|body
operator|instanceof
name|InputStream
operator|)
condition|)
block|{
comment|// For streaming, make it chunked encoding instead of specifying content length
name|requestHeaders
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|TRANSFER_ENCODING
argument_list|,
literal|"chunked"
argument_list|)
expr_stmt|;
name|clientCallback
operator|=
operator|new
name|UndertowStreamingClientCallback
argument_list|(
name|camelExchange
argument_list|,
name|callback
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|request
argument_list|,
operator|(
name|InputStream
operator|)
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|TypeConverter
name|tc
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
specifier|final
name|ByteBuffer
name|bodyAsByte
init|=
name|tc
operator|.
name|tryConvertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
comment|// As tryConvertTo is used to convert the body, we should do null check
comment|// or the call bodyAsByte.remaining() may throw an NPE
if|if
condition|(
name|body
operator|!=
literal|null
operator|&&
name|bodyAsByte
operator|!=
literal|null
condition|)
block|{
name|requestHeaders
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|CONTENT_LENGTH
argument_list|,
name|bodyAsByte
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|streaming
condition|)
block|{
comment|// response may receive streaming
name|clientCallback
operator|=
operator|new
name|UndertowStreamingClientCallback
argument_list|(
name|camelExchange
argument_list|,
name|callback
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|request
argument_list|,
name|bodyAsByte
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientCallback
operator|=
operator|new
name|UndertowClientCallback
argument_list|(
name|camelExchange
argument_list|,
name|callback
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|request
argument_list|,
name|bodyAsByte
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Executing http {} method: {}"
argument_list|,
name|method
argument_list|,
name|pathAndQuery
argument_list|)
expr_stmt|;
block|}
comment|// when connect succeeds or fails UndertowClientCallback will
comment|// get notified on a I/O thread run by Xnio worker. The writing
comment|// of request and reading of response is performed also in the
comment|// callback
name|client
operator|.
name|connect
argument_list|(
name|clientCallback
argument_list|,
name|uri
argument_list|,
name|worker
argument_list|,
name|ssl
argument_list|,
name|pool
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// the call above will proceed on Xnio I/O thread we will
comment|// notify the exchange asynchronously when the HTTP exchange
comment|// ends with success or failure from UndertowClientCallback
return|return
literal|false
return|;
block|}
DECL|method|processWebSocket (final Exchange camelExchange, final AsyncCallback camelCallback)
specifier|private
name|boolean
name|processWebSocket
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|AsyncCallback
name|camelCallback
parameter_list|)
block|{
specifier|final
name|Message
name|in
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|message
init|=
name|in
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|message
operator|instanceof
name|String
operator|||
name|message
operator|instanceof
name|byte
index|[]
operator|||
name|message
operator|instanceof
name|Reader
operator|||
name|message
operator|instanceof
name|InputStream
operator|)
condition|)
block|{
name|message
operator|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
specifier|final
name|int
name|timeout
init|=
name|endpoint
operator|.
name|getSendTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|isSendToAll
argument_list|(
name|in
argument_list|)
condition|)
block|{
return|return
name|webSocketHandler
operator|.
name|send
argument_list|(
name|peer
lambda|->
literal|true
argument_list|,
name|message
argument_list|,
name|timeout
argument_list|,
name|camelExchange
argument_list|,
name|camelCallback
argument_list|)
return|;
block|}
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|connectionKeys
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY_LIST
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionKeys
operator|!=
literal|null
condition|)
block|{
return|return
name|webSocketHandler
operator|.
name|send
argument_list|(
name|peer
lambda|->
name|connectionKeys
operator|.
name|contains
argument_list|(
name|peer
operator|.
name|getAttribute
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|)
argument_list|)
argument_list|,
name|message
argument_list|,
name|timeout
argument_list|,
name|camelExchange
argument_list|,
name|camelCallback
argument_list|)
return|;
block|}
specifier|final
name|String
name|connectionKey
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionKey
operator|!=
literal|null
condition|)
block|{
return|return
name|webSocketHandler
operator|.
name|send
argument_list|(
name|peer
lambda|->
name|connectionKey
operator|.
name|equals
argument_list|(
name|peer
operator|.
name|getAttribute
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|)
argument_list|)
argument_list|,
name|message
argument_list|,
name|timeout
argument_list|,
name|camelExchange
argument_list|,
name|camelCallback
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot process message which has none of the headers %s, %s or %s set: %s"
argument_list|,
name|UndertowConstants
operator|.
name|SEND_TO_ALL
argument_list|,
name|UndertowConstants
operator|.
name|CONNECTION_KEY_LIST
argument_list|,
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|in
argument_list|)
argument_list|)
throw|;
block|}
else|else
block|{
comment|/* nothing to do for a null body */
name|camelCallback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|camelCallback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// as in Undertow tests
name|pool
operator|=
operator|new
name|DefaultByteBufferPool
argument_list|(
literal|true
argument_list|,
literal|17
operator|*
literal|1024
argument_list|)
expr_stmt|;
specifier|final
name|Xnio
name|xnio
init|=
name|Xnio
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|worker
operator|=
name|xnio
operator|.
name|createWorker
argument_list|(
name|options
argument_list|)
expr_stmt|;
specifier|final
name|SSLContext
name|sslContext
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSslContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslContext
operator|!=
literal|null
condition|)
block|{
name|ssl
operator|=
operator|new
name|UndertowXnioSsl
argument_list|(
name|xnio
argument_list|,
name|options
argument_list|,
name|sslContext
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
name|UndertowClient
operator|.
name|getInstance
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isWebSocket
argument_list|()
condition|)
block|{
name|this
operator|.
name|webSocketHandler
operator|=
operator|(
name|CamelWebSocketHandler
operator|)
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|registerEndpoint
argument_list|(
name|endpoint
operator|.
name|getHttpHandlerRegistrationInfo
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getSslContext
argument_list|()
argument_list|,
operator|new
name|CamelWebSocketHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Created worker: {} with options: {}"
argument_list|,
name|worker
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isWebSocket
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|unregisterEndpoint
argument_list|(
name|endpoint
operator|.
name|getHttpHandlerRegistrationInfo
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getSslContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|worker
operator|!=
literal|null
operator|&&
operator|!
name|worker
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Shutting down worker: {}"
argument_list|,
name|worker
argument_list|)
expr_stmt|;
name|worker
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

