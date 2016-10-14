begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
operator|.
name|Scope
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
name|AsyncProcessor
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
name|component
operator|.
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|model
operator|.
name|SoapHeaderInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|CastUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|context
operator|.
name|WrappedMessageContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|ExchangeImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingMessageInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
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
comment|/**  * CxfProducer binds a Camel exchange to a CXF exchange, acts as a CXF   * client, and sends the request to a CXF to a server.  Any response will   * be bound to Camel exchange.   *  * @version   */
end_comment

begin_class
DECL|class|CxfProducer
specifier|public
class|class
name|CxfProducer
extends|extends
name|DefaultProducer
implements|implements
name|AsyncProcessor
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
name|CxfProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|Client
name|client
decl_stmt|;
DECL|field|endpoint
specifier|private
name|CxfEndpoint
name|endpoint
decl_stmt|;
comment|/**      * Constructor to create a CxfProducer.  It will create a CXF client      * object.      *       * @param endpoint a CxfEndpoint that creates this producer      * @throws Exception any exception thrown during the creation of a       * CXF client      */
DECL|method|CxfProducer (CxfEndpoint endpoint)
specifier|public
name|CxfProducer
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
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
comment|// failsafe as cxf may not ensure the endpoint is started (CAMEL-8956)
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|endpoint
operator|.
name|createClient
argument_list|()
expr_stmt|;
block|}
name|Conduit
name|conduit
init|=
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
if|if
condition|(
name|conduit
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"JMSConduit"
argument_list|)
condition|)
block|{
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
name|getJmsConfig
init|=
name|conduit
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getJmsConfig"
argument_list|)
decl_stmt|;
name|Object
name|jmsConfig
init|=
name|getJmsConfig
operator|.
name|invoke
argument_list|(
name|conduit
argument_list|)
decl_stmt|;
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
name|getMessageType
init|=
name|jmsConfig
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getMessageType"
argument_list|)
decl_stmt|;
name|boolean
name|isTextPayload
init|=
literal|"text"
operator|.
name|equals
argument_list|(
name|getMessageType
operator|.
name|invoke
argument_list|(
name|jmsConfig
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isTextPayload
operator|&&
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|.
name|equals
argument_list|(
name|DataFormat
operator|.
name|MESSAGE
argument_list|)
condition|)
block|{
comment|//throw Exception as the Text JMS mesasge won't send as stream
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Text JMS message coundn't be a stream"
argument_list|)
throw|;
block|}
block|}
name|endpoint
operator|.
name|getChainedCxfEndpointConfigurer
argument_list|()
operator|.
name|configureClient
argument_list|(
name|client
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
name|client
operator|!=
literal|null
condition|)
block|{
comment|// It will help to release the request context map
name|client
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// As the cxf client async and sync api is implement different,
comment|// so we don't delegate the sync process call to the async process
DECL|method|process (Exchange camelExchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Process exchange: {} in an async way."
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
try|try
block|{
comment|// create CXF exchange
name|ExchangeImpl
name|cxfExchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
comment|// set the Bus on the exchange in case the CXF interceptor need to access it from exchange
name|cxfExchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
comment|// prepare binding operation info
name|BindingOperationInfo
name|boi
init|=
name|prepareBindingOperation
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|invocationContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|prepareRequest
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
argument_list|)
expr_stmt|;
name|CxfClientCallback
name|cxfClientCallback
init|=
operator|new
name|CxfClientCallback
argument_list|(
name|callback
argument_list|,
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|,
name|boi
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
comment|// send the CXF async request
name|client
operator|.
name|invoke
argument_list|(
name|cxfClientCallback
argument_list|,
name|boi
argument_list|,
name|getParams
argument_list|(
name|endpoint
argument_list|,
name|camelExchange
argument_list|)
argument_list|,
name|invocationContext
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
comment|// error occurred before we had a chance to go async
comment|// so set exception and invoke callback true
name|camelExchange
operator|.
name|setException
argument_list|(
name|ex
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
return|return
literal|false
return|;
block|}
comment|/**      * This processor binds Camel exchange to a CXF exchange and      * invokes the CXF client.      */
DECL|method|process (Exchange camelExchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Process exchange: {} in sync way."
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
comment|// create CXF exchange
name|ExchangeImpl
name|cxfExchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
comment|// set the Bus on the exchange in case the CXF interceptor need to access it from exchange
name|cxfExchange
operator|.
name|put
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
comment|// prepare binding operation info
name|BindingOperationInfo
name|boi
init|=
name|prepareBindingOperation
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|invocationContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
name|invocationContext
operator|.
name|put
argument_list|(
name|Client
operator|.
name|REQUEST_CONTEXT
argument_list|,
name|prepareRequest
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// send the CXF request
name|client
operator|.
name|invoke
argument_list|(
name|boi
argument_list|,
name|getParams
argument_list|(
name|endpoint
argument_list|,
name|camelExchange
argument_list|)
argument_list|,
name|invocationContext
argument_list|,
name|cxfExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
name|camelExchange
operator|.
name|setException
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// add cookies to the cookie store
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
name|cxfHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|cxfExchange
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|storeCookies
argument_list|(
name|camelExchange
argument_list|,
name|endpoint
operator|.
name|getRequestUri
argument_list|(
name|camelExchange
argument_list|)
argument_list|,
name|cxfHeaders
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Cannot store cookies"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// bind the CXF response to Camel exchange
if|if
condition|(
operator|!
name|boi
operator|.
name|getOperationInfo
argument_list|()
operator|.
name|isOneWay
argument_list|()
condition|)
block|{
name|endpoint
operator|.
name|getCxfBinding
argument_list|()
operator|.
name|populateExchangeFromCxfResponse
argument_list|(
name|camelExchange
argument_list|,
name|cxfExchange
argument_list|,
name|responseContext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|prepareRequest (Exchange camelExchange, org.apache.cxf.message.Exchange cxfExchange)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|prepareRequest
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create invocation context
name|WrappedMessageContext
name|requestContext
init|=
operator|new
name|WrappedMessageContext
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|,
literal|null
argument_list|,
name|Scope
operator|.
name|APPLICATION
argument_list|)
decl_stmt|;
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|Message
operator|.
name|MTOM_ENABLED
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|endpoint
operator|.
name|isMtomEnabled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// set data format mode in exchange
name|DataFormat
name|dataFormat
init|=
name|endpoint
operator|.
name|getDataFormat
argument_list|()
decl_stmt|;
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Set Camel Exchange property: {}={}"
argument_list|,
name|DataFormat
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getMergeProtocolHeaders
argument_list|()
condition|)
block|{
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_PROTOCOL_HEADERS_MERGED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|// set data format mode in the request context
name|requestContext
operator|.
name|put
argument_list|(
name|DataFormat
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|dataFormat
argument_list|)
expr_stmt|;
comment|// don't let CXF ClientImpl close the input stream
if|if
condition|(
name|dataFormat
operator|.
name|dealias
argument_list|()
operator|==
name|DataFormat
operator|.
name|RAW
condition|)
block|{
name|cxfExchange
operator|.
name|put
argument_list|(
name|Client
operator|.
name|KEEP_CONDUIT_ALIVE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Set CXF Exchange property: {}={}"
argument_list|,
name|Client
operator|.
name|KEEP_CONDUIT_ALIVE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// bind the request CXF exchange
name|endpoint
operator|.
name|getCxfBinding
argument_list|()
operator|.
name|populateCxfRequestFromExchange
argument_list|(
name|cxfExchange
argument_list|,
name|camelExchange
argument_list|,
name|requestContext
argument_list|)
expr_stmt|;
comment|// add appropriate cookies from the cookie store to the protocol headers
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
name|transportHeaders
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|requestContext
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|added
decl_stmt|;
if|if
condition|(
name|transportHeaders
operator|==
literal|null
condition|)
block|{
name|transportHeaders
operator|=
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
expr_stmt|;
name|added
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|added
operator|=
literal|false
expr_stmt|;
block|}
name|transportHeaders
operator|.
name|putAll
argument_list|(
name|endpoint
operator|.
name|getCookieHandler
argument_list|()
operator|.
name|loadCookies
argument_list|(
name|camelExchange
argument_list|,
name|endpoint
operator|.
name|getRequestUri
argument_list|(
name|camelExchange
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|added
operator|&&
name|transportHeaders
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|requestContext
operator|.
name|put
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|,
name|transportHeaders
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot load cookies"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Remove protocol headers from scopes.  Otherwise, response headers can be
comment|// overwritten by request headers when SOAPHandlerInterceptor tries to create
comment|// a wrapped message context by the copyScoped() method.
name|requestContext
operator|.
name|getScopes
argument_list|()
operator|.
name|remove
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
return|return
name|requestContext
operator|.
name|getWrappedMap
argument_list|()
return|;
block|}
DECL|method|prepareBindingOperation (Exchange camelExchange, org.apache.cxf.message.Exchange cxfExchange)
specifier|private
name|BindingOperationInfo
name|prepareBindingOperation
parameter_list|(
name|Exchange
name|camelExchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Exchange
name|cxfExchange
parameter_list|)
block|{
comment|// get binding operation info
name|BindingOperationInfo
name|boi
init|=
name|getBindingOperationInfo
argument_list|(
name|camelExchange
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|boi
argument_list|,
literal|"BindingOperationInfo"
argument_list|)
expr_stmt|;
comment|// keep the message wrapper in PAYLOAD mode
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|PAYLOAD
operator|&&
name|boi
operator|.
name|isUnwrapped
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getWrappedOperation
argument_list|()
expr_stmt|;
name|cxfExchange
operator|.
name|put
argument_list|(
name|BindingOperationInfo
operator|.
name|class
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
comment|// store the original boi in the exchange
name|camelExchange
operator|.
name|setProperty
argument_list|(
name|BindingOperationInfo
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|boi
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Set exchange property: BindingOperationInfo: {}"
argument_list|,
name|boi
argument_list|)
expr_stmt|;
comment|// Unwrap boi before passing it to make a client call
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|!=
name|DataFormat
operator|.
name|PAYLOAD
operator|&&
operator|!
name|endpoint
operator|.
name|isWrapped
argument_list|()
operator|&&
name|boi
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|boi
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Unwrapped BOI {}"
argument_list|,
name|boi
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|boi
return|;
block|}
DECL|method|checkParameterSize (CxfEndpoint endpoint, Exchange exchange, Object[] parameters)
specifier|private
name|void
name|checkParameterSize
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
index|[]
name|parameters
parameter_list|)
block|{
name|BindingOperationInfo
name|boi
init|=
name|getBindingOperationInfo
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|boi
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't find the binding operation information from camel exchange"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isWrapped
argument_list|()
condition|)
block|{
if|if
condition|(
name|boi
operator|.
name|isUnwrappedCapable
argument_list|()
condition|)
block|{
name|boi
operator|=
name|boi
operator|.
name|getUnwrappedOperation
argument_list|()
expr_stmt|;
block|}
block|}
name|int
name|experctMessagePartsSize
init|=
name|boi
operator|.
name|getInput
argument_list|()
operator|.
name|getMessageParts
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|length
operator|<
name|experctMessagePartsSize
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get the wrong parameter size to invoke the out service, Expect size "
operator|+
name|experctMessagePartsSize
operator|+
literal|", Parameter size "
operator|+
name|parameters
operator|.
name|length
operator|+
literal|". Please check if the message body matches the CXFEndpoint POJO Dataformat request."
argument_list|)
throw|;
block|}
if|if
condition|(
name|parameters
operator|.
name|length
operator|>
name|experctMessagePartsSize
condition|)
block|{
comment|// need to check the holder parameters
name|int
name|holdersSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|parameter
range|:
name|parameters
control|)
block|{
if|if
condition|(
name|parameter
operator|instanceof
name|Holder
condition|)
block|{
name|holdersSize
operator|++
expr_stmt|;
block|}
block|}
comment|// need to check the soap header information
name|int
name|soapHeadersSize
init|=
literal|0
decl_stmt|;
name|BindingMessageInfo
name|bmi
init|=
name|boi
operator|.
name|getInput
argument_list|()
decl_stmt|;
if|if
condition|(
name|bmi
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|SoapHeaderInfo
argument_list|>
name|headers
init|=
name|bmi
operator|.
name|getExtensors
argument_list|(
name|SoapHeaderInfo
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|soapHeadersSize
operator|=
name|headers
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|holdersSize
operator|+
name|experctMessagePartsSize
operator|+
name|soapHeadersSize
operator|<
name|parameters
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Get the wrong parameter size to invoke the out service, Expect size "
operator|+
operator|(
name|experctMessagePartsSize
operator|+
name|holdersSize
operator|+
name|soapHeadersSize
operator|)
operator|+
literal|", Parameter size "
operator|+
name|parameters
operator|.
name|length
operator|+
literal|". Please check if the message body matches the CXFEndpoint POJO Dataformat request."
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Get the parameters for the web service operation      */
DECL|method|getParams (CxfEndpoint endpoint, Exchange exchange)
specifier|private
name|Object
index|[]
name|getParams
parameter_list|(
name|CxfEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|InvalidPayloadException
block|{
name|Object
index|[]
name|params
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|POJO
condition|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|Object
index|[]
condition|)
block|{
name|params
operator|=
operator|(
name|Object
index|[]
operator|)
name|body
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|List
condition|)
block|{
comment|// Now we just check if the request is List
name|params
operator|=
operator|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|body
operator|)
operator|.
name|toArray
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// maybe we can iterate the body and that way create a list for the parameters
comment|// then end users do not need to trouble with List
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|it
operator|!=
literal|null
operator|&&
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
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
name|List
operator|.
name|class
argument_list|,
name|it
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|params
operator|=
name|list
operator|.
name|toArray
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|params
operator|==
literal|null
condition|)
block|{
comment|// now we just use the body as single parameter
name|params
operator|=
operator|new
name|Object
index|[
literal|1
index|]
expr_stmt|;
name|params
index|[
literal|0
index|]
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
comment|// make sure we have the right number of parameters
name|checkParameterSize
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|params
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|==
name|DataFormat
operator|.
name|PAYLOAD
condition|)
block|{
name|params
operator|=
operator|new
name|Object
index|[
literal|1
index|]
expr_stmt|;
name|params
index|[
literal|0
index|]
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|.
name|dealias
argument_list|()
operator|==
name|DataFormat
operator|.
name|RAW
condition|)
block|{
name|params
operator|=
operator|new
name|Object
index|[
literal|1
index|]
expr_stmt|;
name|params
index|[
literal|0
index|]
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getDataFormat
argument_list|()
operator|.
name|dealias
argument_list|()
operator|==
name|DataFormat
operator|.
name|CXF_MESSAGE
condition|)
block|{
name|params
operator|=
operator|new
name|Object
index|[
literal|1
index|]
expr_stmt|;
name|params
index|[
literal|0
index|]
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|params
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|params
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"params[{}] = {}"
argument_list|,
name|i
argument_list|,
name|params
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|params
return|;
block|}
comment|/**      *<p>Get operation name from header and use it to lookup and return a       * {@link BindingOperationInfo}.</p>      *<p>CxfProducer lookups the operation name lookup with below order, and it uses the first found one which is not null:</p>      *<ul>      *<li> Using the in message header "operationName".</li>      *<li> Using the defaultOperationName option value from the CxfEndpoint.</li>      *<li> Using the first operation which is find from the CxfEndpoint Operations list.</li>      *<ul>      */
DECL|method|getBindingOperationInfo (Exchange ex)
specifier|private
name|BindingOperationInfo
name|getBindingOperationInfo
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|this
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|BindingOperationInfo
name|answer
init|=
literal|null
decl_stmt|;
name|String
name|lp
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|lp
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CxfProducer cannot find the {} from message header, trying with defaultOperationName"
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
expr_stmt|;
name|lp
operator|=
name|endpoint
operator|.
name|getDefaultOperationName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|lp
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CxfProducer cannot find the {} from message header and there is no DefaultOperationName setting, CxfProducer will pick up the first available operation."
argument_list|,
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|BindingOperationInfo
argument_list|>
name|bois
init|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|BindingOperationInfo
argument_list|>
name|iter
init|=
name|bois
operator|.
name|iterator
argument_list|()
decl_stmt|;
if|if
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|answer
operator|=
name|iter
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|ns
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAMESPACE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|ns
operator|=
name|endpoint
operator|.
name|getDefaultOperationNamespace
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ns
operator|==
literal|null
condition|)
block|{
name|ns
operator|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Operation namespace not in header. Set it to: {}"
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
name|QName
name|qname
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|lp
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Operation qname = {}"
argument_list|,
name|qname
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|=
name|client
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperation
argument_list|(
name|qname
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't find the BindingOperationInfo with operation name "
operator|+
name|qname
operator|+
literal|". Please check the message headers of operationName and operationNamespace."
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|getClient ()
specifier|public
name|Client
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

