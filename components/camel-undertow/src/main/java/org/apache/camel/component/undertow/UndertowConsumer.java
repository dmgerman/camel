begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Handlers
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
name|HttpHandler
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
name|HttpServerExchange
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
name|handlers
operator|.
name|form
operator|.
name|EagerFormParsingHandler
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
name|io
operator|.
name|undertow
operator|.
name|util
operator|.
name|Methods
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
name|MimeMappings
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
name|StatusCodes
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|websockets
operator|.
name|core
operator|.
name|WebSocketChannel
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
name|Processor
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
name|UndertowConstants
operator|.
name|EventType
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
name|impl
operator|.
name|DefaultConsumer
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
name|CollectionStringBuffer
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
comment|/**  * The Undertow consumer which is also an Undertow HttpHandler implementation to handle incoming request.  */
end_comment

begin_class
DECL|class|UndertowConsumer
specifier|public
class|class
name|UndertowConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|HttpHandler
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
name|UndertowConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|webSocketHandler
specifier|private
name|CamelWebSocketHandler
name|webSocketHandler
decl_stmt|;
DECL|method|UndertowConsumer (UndertowEndpoint endpoint, Processor processor)
specifier|public
name|UndertowConsumer
parameter_list|(
name|UndertowEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
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
operator|(
name|UndertowEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
specifier|final
name|UndertowEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isWebSocket
argument_list|()
condition|)
block|{
comment|/*              * note that the new CamelWebSocketHandler() we pass to registerEndpoint() does not necessarily have to be              * the same instance that is returned from there              */
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
name|this
operator|.
name|webSocketHandler
operator|.
name|setConsumer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// allow for HTTP 1.1 continue
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
name|Handlers
operator|.
name|httpContinueRead
argument_list|(
comment|// wrap with EagerFormParsingHandler to enable undertow form parsers
operator|new
name|EagerFormParsingHandler
argument_list|()
operator|.
name|setNext
argument_list|(
name|UndertowConsumer
operator|.
name|this
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|this
operator|.
name|webSocketHandler
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|webSocketHandler
operator|.
name|setConsumer
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|UndertowEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
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
annotation|@
name|Override
DECL|method|handleRequest (HttpServerExchange httpExchange)
specifier|public
name|void
name|handleRequest
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpString
name|requestMethod
init|=
name|httpExchange
operator|.
name|getRequestMethod
argument_list|()
decl_stmt|;
if|if
condition|(
name|Methods
operator|.
name|OPTIONS
operator|.
name|equals
argument_list|(
name|requestMethod
argument_list|)
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isOptionsEnabled
argument_list|()
condition|)
block|{
name|CollectionStringBuffer
name|csb
init|=
operator|new
name|CollectionStringBuffer
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|HttpHandlerRegistrationInfo
argument_list|>
name|handlers
init|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getHandlers
argument_list|()
decl_stmt|;
for|for
control|(
name|HttpHandlerRegistrationInfo
name|reg
range|:
name|handlers
control|)
block|{
name|URI
name|uri
init|=
name|reg
operator|.
name|getUri
argument_list|()
decl_stmt|;
comment|// what other HTTP methods may exists for the same path
if|if
condition|(
name|reg
operator|.
name|getMethodRestrict
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|String
name|restrict
init|=
name|reg
operator|.
name|getMethodRestrict
argument_list|()
decl_stmt|;
if|if
condition|(
name|restrict
operator|.
name|endsWith
argument_list|(
literal|",OPTIONS"
argument_list|)
condition|)
block|{
name|restrict
operator|=
name|restrict
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|restrict
operator|.
name|length
argument_list|()
operator|-
literal|8
argument_list|)
expr_stmt|;
block|}
name|csb
operator|.
name|append
argument_list|(
name|restrict
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|allowedMethods
init|=
name|csb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|allowedMethods
argument_list|)
condition|)
block|{
name|allowedMethods
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|allowedMethods
argument_list|)
condition|)
block|{
name|allowedMethods
operator|=
literal|"GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,CONNECT,PATCH"
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|allowedMethods
operator|.
name|contains
argument_list|(
literal|"OPTIONS"
argument_list|)
condition|)
block|{
name|allowedMethods
operator|=
name|allowedMethods
operator|+
literal|",OPTIONS"
expr_stmt|;
block|}
comment|//return list of allowed methods in response headers
name|httpExchange
operator|.
name|setStatusCode
argument_list|(
name|StatusCodes
operator|.
name|OK
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|ExchangeHeaders
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// do not include content-type as that would indicate to the caller that we can only do text/plain
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|Headers
operator|.
name|ALLOW
argument_list|,
name|allowedMethods
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
return|return;
block|}
comment|//perform blocking operation on exchange
if|if
condition|(
name|httpExchange
operator|.
name|isInIoThread
argument_list|()
condition|)
block|{
name|httpExchange
operator|.
name|dispatch
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return;
block|}
comment|//create new Exchange
comment|//binding is used to extract header and payload(if available)
name|Exchange
name|camelExchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|httpExchange
argument_list|)
decl_stmt|;
comment|//Unit of Work to process the Exchange
name|createUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUoW
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|getResponseBody
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
argument_list|)
decl_stmt|;
name|TypeConverter
name|tc
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"No payload to send as reply for exchange: {}"
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|ExchangeHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|MimeMappings
operator|.
name|DEFAULT_MIME_MAPPINGS
operator|.
name|get
argument_list|(
literal|"txt"
argument_list|)
argument_list|)
expr_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|send
argument_list|(
literal|"No response available"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ByteBuffer
name|bodyAsByteBuffer
init|=
name|tc
operator|.
name|convertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|send
argument_list|(
name|bodyAsByteBuffer
argument_list|)
expr_stmt|;
block|}
name|httpExchange
operator|.
name|getResponseSender
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create an {@link Exchange} from the associated {@link UndertowEndpoint} and set the {@code in} {@link Message}'s      * body to the given {@code message} and {@link UndertowConstants#CONNECTION_KEY} header to the given      * {@code connectionKey}.      *      * @param connectionKey an identifier of {@link WebSocketChannel} through which the {@code message} was received      * @param message the message received via the {@link WebSocketChannel}      */
DECL|method|sendMessage (final String connectionKey, final Object message)
specifier|public
name|void
name|sendMessage
parameter_list|(
specifier|final
name|String
name|connectionKey
parameter_list|,
specifier|final
name|Object
name|message
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// set header and body
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|connectionKey
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// send exchange using the async routing engine
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Send a notification related a WebSocket peer.      *      * @param connectionKey of WebSocket peer      * @param eventType the type of the event      */
DECL|method|sendEventNotification (String connectionKey, EventType eventType)
specifier|public
name|void
name|sendEventNotification
parameter_list|(
name|String
name|connectionKey
parameter_list|,
name|EventType
name|eventType
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
specifier|final
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|UndertowConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|connectionKey
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|UndertowConstants
operator|.
name|EVENT_TYPE
argument_list|,
name|eventType
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|UndertowConstants
operator|.
name|EVENT_TYPE_ENUM
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
comment|// send exchange using the async routing engine
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getResponseBody (HttpServerExchange httpExchange, Exchange camelExchange)
specifier|private
name|Object
name|getResponseBody
parameter_list|(
name|HttpServerExchange
name|httpExchange
parameter_list|,
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|result
decl_stmt|;
if|if
condition|(
name|camelExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|result
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toHttpResponse
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getUndertowHttpBinding
argument_list|()
operator|.
name|toHttpResponse
argument_list|(
name|httpExchange
argument_list|,
name|camelExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

