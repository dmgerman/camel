begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|CamelException
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
name|processor
operator|.
name|CamelLogger
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
name|mina
operator|.
name|common
operator|.
name|IoAcceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoHandlerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
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
comment|/**  * A {@link org.apache.camel.Consumer Consumer} implementation for Apache MINA.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaConsumer
specifier|public
class|class
name|MinaConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MinaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|address
specifier|private
specifier|final
name|SocketAddress
name|address
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
decl_stmt|;
DECL|field|noReplyLogger
specifier|private
name|CamelLogger
name|noReplyLogger
decl_stmt|;
DECL|method|MinaConsumer (final MinaEndpoint endpoint, Processor processor)
specifier|public
name|MinaConsumer
parameter_list|(
specifier|final
name|MinaEndpoint
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
name|this
operator|.
name|address
operator|=
name|endpoint
operator|.
name|getAddress
argument_list|()
expr_stmt|;
name|this
operator|.
name|acceptor
operator|=
name|endpoint
operator|.
name|getAcceptor
argument_list|()
expr_stmt|;
name|this
operator|.
name|sync
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
expr_stmt|;
name|this
operator|.
name|noReplyLogger
operator|=
operator|new
name|CamelLogger
argument_list|(
name|LOG
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNoReplyLogLevel
argument_list|()
argument_list|)
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Binding to server address: "
operator|+
name|address
operator|+
literal|" using acceptor: "
operator|+
name|acceptor
argument_list|)
expr_stmt|;
block|}
name|IoHandler
name|handler
init|=
operator|new
name|ReceiveHandler
argument_list|()
decl_stmt|;
name|acceptor
operator|.
name|bind
argument_list|(
name|address
argument_list|,
name|handler
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getAcceptorConfig
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Unbinding from server address: "
operator|+
name|address
operator|+
literal|" using acceptor: "
operator|+
name|acceptor
argument_list|)
expr_stmt|;
block|}
name|acceptor
operator|.
name|unbind
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MinaEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MinaEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Handles consuming messages and replying if the exchange is out capable.      */
DECL|class|ReceiveHandler
specifier|private
specifier|final
class|class
name|ReceiveHandler
extends|extends
name|IoHandlerAdapter
block|{
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession session, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
comment|// close invalid session
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing session as an exception was thrown from MINA"
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// must wrap and rethrow since cause can be of Throwable and we must only throw Exception
throw|throw
operator|new
name|CamelException
argument_list|(
name|cause
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession session, Object object)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
block|{
comment|// log what we received
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|Object
name|in
init|=
name|object
decl_stmt|;
if|if
condition|(
name|in
operator|instanceof
name|byte
index|[]
condition|)
block|{
comment|// byte arrays is not readable so convert to string
name|in
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
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
name|in
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received body: "
operator|+
name|in
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|session
argument_list|,
name|object
argument_list|)
decl_stmt|;
comment|//Set the exchange charset property for converting
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
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
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
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
comment|// if sync then we should return a response
if|if
condition|(
name|sync
condition|)
block|{
name|Object
name|body
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|body
operator|=
name|MinaPayloadHelper
operator|.
name|getOut
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|MinaPayloadHelper
operator|.
name|getIn
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
if|if
condition|(
name|failed
operator|&&
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
condition|)
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
name|body
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// failed and no exception, must be a fault
name|body
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|noReplyLogger
operator|.
name|log
argument_list|(
literal|"No payload to send as reply for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnectOnNoReply
argument_list|()
condition|)
block|{
comment|// must close session if no data to write otherwise client will never receive a response
comment|// and wait forever (if not timing out)
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
literal|"Closing session as no payload to send as reply at address: "
operator|+
name|address
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// we got a response to write
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
literal|"Writing body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
name|MinaHelper
operator|.
name|writeBody
argument_list|(
name|session
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// should session be closed after complete?
name|Boolean
name|close
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|close
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MinaConstants
operator|.
name|MINA_CLOSE_SESSION_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|close
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MinaConstants
operator|.
name|MINA_CLOSE_SESSION_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// should we disconnect, the header can override the configuration
name|boolean
name|disconnect
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnect
argument_list|()
decl_stmt|;
if|if
condition|(
name|close
operator|!=
literal|null
condition|)
block|{
name|disconnect
operator|=
name|close
expr_stmt|;
block|}
if|if
condition|(
name|disconnect
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
literal|"Closing session when complete at address: "
operator|+
name|address
argument_list|)
expr_stmt|;
block|}
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

