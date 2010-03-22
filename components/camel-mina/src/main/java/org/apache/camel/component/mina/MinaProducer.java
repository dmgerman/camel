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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ExchangeTimedOutException
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
name|ServicePoolAware
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
name|ExchangeHelper
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
name|mina
operator|.
name|common
operator|.
name|ConnectFuture
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
name|IoConnector
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
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|SocketConnector
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Producer} implementation for MINA  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaProducer
specifier|public
class|class
name|MinaProducer
extends|extends
name|DefaultProducer
implements|implements
name|ServicePoolAware
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
name|MinaProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|session
specifier|private
name|IoSession
name|session
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MinaEndpoint
name|endpoint
decl_stmt|;
DECL|field|latch
specifier|private
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|lazySessionCreation
specifier|private
name|boolean
name|lazySessionCreation
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|connector
specifier|private
name|IoConnector
name|connector
decl_stmt|;
DECL|field|sync
specifier|private
name|boolean
name|sync
decl_stmt|;
DECL|method|MinaProducer (MinaEndpoint endpoint)
specifier|public
name|MinaProducer
parameter_list|(
name|MinaEndpoint
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
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|lazySessionCreation
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isLazySessionCreation
argument_list|()
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTimeout
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
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// the producer should not be singleton otherwise cannot use concurrent producers and safely
comment|// use request/reply with correct correlation
return|return
literal|false
return|;
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
name|session
operator|==
literal|null
operator|&&
operator|!
name|lazySessionCreation
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Not started yet!"
argument_list|)
throw|;
block|}
if|if
condition|(
name|session
operator|==
literal|null
operator|||
operator|!
name|session
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|openConnection
argument_list|()
expr_stmt|;
block|}
comment|// set the exchange encoding property
if|if
condition|(
name|endpoint
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
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Object
name|body
init|=
name|MinaPayloadHelper
operator|.
name|getIn
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
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
name|warn
argument_list|(
literal|"No payload to send for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
return|return;
comment|// exit early since nothing to write
block|}
comment|// if sync is true then we should also wait for a response (synchronous mode)
if|if
condition|(
name|sync
condition|)
block|{
comment|// only initialize latch if we should get a response
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// reset handler if we expect a response
name|ResponseHandler
name|handler
init|=
operator|(
name|ResponseHandler
operator|)
name|session
operator|.
name|getHandler
argument_list|()
decl_stmt|;
name|handler
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|// log what we are writing
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|Object
name|out
init|=
name|body
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|byte
index|[]
condition|)
block|{
comment|// byte arrays is not readable so convert to string
name|out
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
name|body
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing body : "
operator|+
name|out
argument_list|)
expr_stmt|;
block|}
comment|// write the body
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
if|if
condition|(
name|sync
condition|)
block|{
comment|// wait for response, consider timeout
name|LOG
operator|.
name|debug
argument_list|(
literal|"Waiting for response"
argument_list|)
expr_stmt|;
name|boolean
name|done
init|=
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|done
condition|)
block|{
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|timeout
argument_list|)
throw|;
block|}
comment|// did we get a response
name|ResponseHandler
name|handler
init|=
operator|(
name|ResponseHandler
operator|)
name|session
operator|.
name|getHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|.
name|getCause
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Response Handler had an exception"
argument_list|,
name|exchange
argument_list|,
name|handler
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
operator|!
name|handler
operator|.
name|isMessageReceived
argument_list|()
condition|)
block|{
comment|// no message received
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No response received from remote server: "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
comment|// set the result on either IN or OUT on the original exchange depending on its pattern
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
name|MinaPayloadHelper
operator|.
name|setOut
argument_list|(
name|exchange
argument_list|,
name|handler
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MinaPayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|handler
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|endpoint
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
name|endpoint
operator|.
name|getAddress
argument_list|()
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
operator|!
name|lazySessionCreation
condition|)
block|{
name|openConnection
argument_list|()
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
literal|"Stopping connector: "
operator|+
name|connector
operator|+
literal|" at address: "
operator|+
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|closeConnection
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|closeConnection ()
specifier|private
name|void
name|closeConnection
parameter_list|()
block|{
if|if
condition|(
name|connector
operator|instanceof
name|SocketConnector
condition|)
block|{
comment|// Change the worker timeout to 0 second to make the I/O thread quit soon when there's no connection to manage.
comment|// Default worker timeout is 60 sec and therefore the client using MinaProducer cannot terminate the JVM
comment|// asap but must wait for the timeout to happen, so to speed this up we set the timeout to 0.
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting SocketConnector WorkerTimeout=0 to force MINA stopping its resources faster"
argument_list|)
expr_stmt|;
block|}
operator|(
operator|(
name|SocketConnector
operator|)
name|connector
operator|)
operator|.
name|setWorkerTimeout
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|openConnection ()
specifier|private
name|void
name|openConnection
parameter_list|()
block|{
name|SocketAddress
name|address
init|=
name|endpoint
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|connector
operator|=
name|endpoint
operator|.
name|getConnector
argument_list|()
expr_stmt|;
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
literal|"Creating connector to address: "
operator|+
name|address
operator|+
literal|" using connector: "
operator|+
name|connector
operator|+
literal|" timeout: "
operator|+
name|timeout
operator|+
literal|" millis."
argument_list|)
expr_stmt|;
block|}
name|IoHandler
name|ioHandler
init|=
operator|new
name|ResponseHandler
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
comment|// connect and wait until the connection is established
name|ConnectFuture
name|future
init|=
name|connector
operator|.
name|connect
argument_list|(
name|address
argument_list|,
name|ioHandler
argument_list|,
name|endpoint
operator|.
name|getConnectorConfig
argument_list|()
argument_list|)
decl_stmt|;
name|future
operator|.
name|join
argument_list|()
expr_stmt|;
name|session
operator|=
name|future
operator|.
name|getSession
argument_list|()
expr_stmt|;
block|}
comment|/**      * Handles response from session writes      */
DECL|class|ResponseHandler
specifier|private
specifier|final
class|class
name|ResponseHandler
extends|extends
name|IoHandlerAdapter
block|{
DECL|field|endpoint
specifier|private
name|MinaEndpoint
name|endpoint
decl_stmt|;
DECL|field|message
specifier|private
name|Object
name|message
decl_stmt|;
DECL|field|cause
specifier|private
name|Throwable
name|cause
decl_stmt|;
DECL|field|messageReceived
specifier|private
name|boolean
name|messageReceived
decl_stmt|;
DECL|method|ResponseHandler (MinaEndpoint endpoint)
specifier|private
name|ResponseHandler
parameter_list|(
name|MinaEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|this
operator|.
name|message
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|cause
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|messageReceived
operator|=
literal|false
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession ioSession, Object message)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
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
literal|"Message received: "
operator|+
name|message
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|message
operator|=
name|message
expr_stmt|;
name|messageReceived
operator|=
literal|true
expr_stmt|;
name|cause
operator|=
literal|null
expr_stmt|;
name|countDown
argument_list|()
expr_stmt|;
block|}
DECL|method|countDown ()
specifier|protected
name|void
name|countDown
parameter_list|()
block|{
name|CountDownLatch
name|downLatch
init|=
name|latch
decl_stmt|;
if|if
condition|(
name|downLatch
operator|!=
literal|null
condition|)
block|{
name|downLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|sessionClosed (IoSession session)
specifier|public
name|void
name|sessionClosed
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|sync
operator|&&
name|message
operator|==
literal|null
condition|)
block|{
comment|// sync=true (InOut mode) so we expected a message as reply but did not get one before the session is closed
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
literal|"Session closed but no message received from address: "
operator|+
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// session was closed but no message received. This could be because the remote server had an internal error
comment|// and could not return a response. We should count down to stop waiting for a response
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession ioSession, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|ioSession
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception on receiving message from address: "
operator|+
name|this
operator|.
name|endpoint
operator|.
name|getAddress
argument_list|()
operator|+
literal|" using connector: "
operator|+
name|this
operator|.
name|endpoint
operator|.
name|getConnector
argument_list|()
argument_list|,
name|cause
argument_list|)
expr_stmt|;
name|this
operator|.
name|message
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|messageReceived
operator|=
literal|false
expr_stmt|;
name|this
operator|.
name|cause
operator|=
name|cause
expr_stmt|;
if|if
condition|(
name|ioSession
operator|!=
literal|null
condition|)
block|{
name|ioSession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getCause ()
specifier|public
name|Throwable
name|getCause
parameter_list|()
block|{
return|return
name|this
operator|.
name|cause
return|;
block|}
DECL|method|getMessage ()
specifier|public
name|Object
name|getMessage
parameter_list|()
block|{
return|return
name|this
operator|.
name|message
return|;
block|}
DECL|method|isMessageReceived ()
specifier|public
name|boolean
name|isMessageReceived
parameter_list|()
block|{
return|return
name|messageReceived
return|;
block|}
block|}
block|}
end_class

end_unit

