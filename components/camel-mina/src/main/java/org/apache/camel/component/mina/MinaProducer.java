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
name|common
operator|.
name|WriteFuture
import|;
end_import

begin_comment
comment|/**  * A {@link Producer} implementation for MINA  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaProducer
specifier|public
class|class
name|MinaProducer
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
name|MinaProducer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO: The max wait response should be configurable
comment|// The URI parameter could be a option
DECL|field|MAX_WAIT_RESPONSE
specifier|private
specifier|static
specifier|final
name|long
name|MAX_WAIT_RESPONSE
init|=
literal|30000
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
name|this
operator|.
name|endpoint
operator|.
name|getLazySessionCreation
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
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No payload for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
comment|// write the body
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|WriteFuture
name|future
init|=
name|session
operator|.
name|write
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|future
operator|.
name|join
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isWritten
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|MAX_WAIT_RESPONSE
argument_list|)
throw|;
block|}
comment|// wait for response, consider timeout
name|latch
operator|.
name|await
argument_list|(
name|MAX_WAIT_RESPONSE
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|latch
operator|.
name|getCount
argument_list|()
operator|==
literal|1
condition|)
block|{
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|MAX_WAIT_RESPONSE
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
name|CamelException
argument_list|(
literal|"Response Handler had an exception"
argument_list|,
name|handler
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
else|else
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
literal|"Handler message: "
operator|+
name|handler
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|handler
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|session
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
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
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|()
operator|.
name|join
argument_list|(
literal|2000
argument_list|)
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
name|IoConnector
name|connector
init|=
name|endpoint
operator|.
name|getConnector
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
literal|"Creating connector to address: "
operator|+
name|address
operator|+
literal|" using connector: "
operator|+
name|connector
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
comment|/**      * Handles response from session writes      *      * @author<a href="mailto:karajdaar@gmail.com">nsandhu</a>      */
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
name|cause
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|message
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
literal|"Session closed"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
comment|// session was closed but no message received. This is because the remote server had an internal error
comment|// and could not return a proper response. We should count down to stop waiting for a response
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
name|countDown
argument_list|()
expr_stmt|;
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
block|}
block|}
end_class

end_unit

