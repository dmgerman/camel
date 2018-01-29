begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|BindException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ExecutorService
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
name|Executors
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
name|RejectedExecutionException
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
name|SynchronousQueue
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
name|ThreadPoolExecutor
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|mllp
operator|.
name|internal
operator|.
name|MllpSocketBuffer
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
name|mllp
operator|.
name|internal
operator|.
name|TcpServerConsumerValidationRunnable
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
name|mllp
operator|.
name|internal
operator|.
name|TcpServerAcceptThread
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
name|mllp
operator|.
name|internal
operator|.
name|TcpSocketConsumerRunnable
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

begin_comment
comment|/**  * The MLLP consumer.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"MLLP Producer"
argument_list|)
DECL|class|MllpTcpServerConsumer
specifier|public
class|class
name|MllpTcpServerConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|validationExecutor
specifier|final
name|ExecutorService
name|validationExecutor
decl_stmt|;
DECL|field|consumerExecutor
specifier|final
name|ExecutorService
name|consumerExecutor
decl_stmt|;
DECL|field|acceptThread
name|TcpServerAcceptThread
name|acceptThread
decl_stmt|;
DECL|field|consumerRunnables
name|Map
argument_list|<
name|TcpSocketConsumerRunnable
argument_list|,
name|Long
argument_list|>
name|consumerRunnables
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|MllpTcpServerConsumer (MllpEndpoint endpoint, Processor processor)
specifier|public
name|MllpTcpServerConsumer
parameter_list|(
name|MllpEndpoint
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
name|log
operator|.
name|trace
argument_list|(
literal|"MllpTcpServerConsumer(endpoint, processor)"
argument_list|)
expr_stmt|;
name|validationExecutor
operator|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
expr_stmt|;
name|consumerExecutor
operator|=
operator|new
name|ThreadPoolExecutor
argument_list|(
literal|1
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMaxConcurrentConsumers
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getAcceptTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|SynchronousQueue
argument_list|<>
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last activity time"
argument_list|)
DECL|method|getLastActivityTimes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Date
argument_list|>
name|getLastActivityTimes
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Date
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|TcpSocketConsumerRunnable
argument_list|,
name|Long
argument_list|>
name|entry
range|:
name|consumerRunnables
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|TcpSocketConsumerRunnable
name|consumerRunnable
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumerRunnable
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|put
argument_list|(
name|consumerRunnable
operator|.
name|getCombinedAddress
argument_list|()
argument_list|,
operator|new
name|Date
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Close Connections"
argument_list|)
DECL|method|closeConnections ()
specifier|public
name|void
name|closeConnections
parameter_list|()
block|{
for|for
control|(
name|TcpSocketConsumerRunnable
name|consumerRunnable
range|:
name|consumerRunnables
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|consumerRunnable
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Close Connection called via JMX for address {}"
argument_list|,
name|consumerRunnable
operator|.
name|getCombinedAddress
argument_list|()
argument_list|)
expr_stmt|;
name|consumerRunnable
operator|.
name|closeSocket
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset Connections"
argument_list|)
DECL|method|resetConnections ()
specifier|public
name|void
name|resetConnections
parameter_list|()
block|{
for|for
control|(
name|TcpSocketConsumerRunnable
name|consumerRunnable
range|:
name|consumerRunnables
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|consumerRunnable
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Reset Connection called via JMX for address {}"
argument_list|,
name|consumerRunnable
operator|.
name|getCombinedAddress
argument_list|()
argument_list|)
expr_stmt|;
name|consumerRunnable
operator|.
name|resetSocket
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MllpEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MllpEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|log
operator|.
name|debug
argument_list|(
literal|"doStop()"
argument_list|)
expr_stmt|;
comment|// Close any client sockets that are currently open
for|for
control|(
name|TcpSocketConsumerRunnable
name|consumerClientSocketThread
range|:
name|consumerRunnables
operator|.
name|keySet
argument_list|()
control|)
block|{
name|consumerClientSocketThread
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|acceptThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
name|acceptThread
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
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
name|log
operator|.
name|debug
argument_list|(
literal|"doStart() - starting acceptor"
argument_list|)
expr_stmt|;
name|ServerSocket
name|serverSocket
init|=
operator|new
name|ServerSocket
argument_list|()
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|hasReceiveBufferSize
argument_list|()
condition|)
block|{
name|serverSocket
operator|.
name|setReceiveBufferSize
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|hasReuseAddress
argument_list|()
condition|)
block|{
name|serverSocket
operator|.
name|setReuseAddress
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReuseAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Accept Timeout
name|serverSocket
operator|.
name|setSoTimeout
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getAcceptTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|InetSocketAddress
name|socketAddress
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|getEndpoint
argument_list|()
operator|.
name|getHostname
argument_list|()
condition|)
block|{
name|socketAddress
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|socketAddress
operator|=
operator|new
name|InetSocketAddress
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getHostname
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|long
name|startTicks
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// Log usage of deprecated URI options
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|hasMaxReceiveTimeouts
argument_list|()
condition|)
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|hasIdleTimeout
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Both maxReceivedTimeouts {} and idleTimeout {} URI options are specified - idleTimeout will be used"
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMaxReceiveTimeouts
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getConfiguration
argument_list|()
operator|.
name|setIdleTimeout
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxReceiveTimeouts
argument_list|()
operator|*
name|getConfiguration
argument_list|()
operator|.
name|getReceiveTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Deprecated URI option maxReceivedTimeouts {} specified - idleTimeout {} will be used"
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getMaxReceiveTimeouts
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getIdleTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
do|do
block|{
try|try
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|hasBacklog
argument_list|()
condition|)
block|{
name|serverSocket
operator|.
name|bind
argument_list|(
name|socketAddress
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getBacklog
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|serverSocket
operator|.
name|bind
argument_list|(
name|socketAddress
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|BindException
name|bindException
parameter_list|)
block|{
if|if
condition|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|>
name|startTicks
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getBindTimeout
argument_list|()
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Failed to bind to address {} within timeout {}"
argument_list|,
name|socketAddress
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getBindTimeout
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|bindException
throw|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to bind to address {} - retrying in {} milliseconds"
argument_list|,
name|socketAddress
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getBindRetryInterval
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getBindRetryInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
do|while
condition|(
operator|!
name|serverSocket
operator|.
name|isBound
argument_list|()
condition|)
do|;
comment|// acceptRunnable = new TcpServerConsumerValidationRunnable(this, serverSocket);
comment|// validationExecutor.submit(acceptRunnable);
name|acceptThread
operator|=
operator|new
name|TcpServerAcceptThread
argument_list|(
name|this
argument_list|,
name|serverSocket
argument_list|)
expr_stmt|;
name|acceptThread
operator|.
name|start
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
name|consumerExecutor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
if|if
condition|(
name|acceptThread
operator|!=
literal|null
condition|)
block|{
name|acceptThread
operator|.
name|interrupt
argument_list|()
expr_stmt|;
block|}
name|validationExecutor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|MllpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getConsumerRunnables ()
specifier|public
name|Map
argument_list|<
name|TcpSocketConsumerRunnable
argument_list|,
name|Long
argument_list|>
name|getConsumerRunnables
parameter_list|()
block|{
return|return
name|consumerRunnables
return|;
block|}
DECL|method|validateConsumer (Socket clientSocket)
specifier|public
name|void
name|validateConsumer
parameter_list|(
name|Socket
name|clientSocket
parameter_list|)
block|{
name|MllpSocketBuffer
name|mllpBuffer
init|=
operator|new
name|MllpSocketBuffer
argument_list|(
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|TcpServerConsumerValidationRunnable
name|client
init|=
operator|new
name|TcpServerConsumerValidationRunnable
argument_list|(
name|this
argument_list|,
name|clientSocket
argument_list|,
name|mllpBuffer
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Validating consumer for Socket {}"
argument_list|,
name|clientSocket
argument_list|)
expr_stmt|;
name|validationExecutor
operator|.
name|submit
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|rejectedExecutionEx
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot validate consumer - max validations already active"
argument_list|)
expr_stmt|;
name|mllpBuffer
operator|.
name|resetSocket
argument_list|(
name|clientSocket
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|startConsumer (Socket clientSocket, MllpSocketBuffer mllpBuffer)
specifier|public
name|void
name|startConsumer
parameter_list|(
name|Socket
name|clientSocket
parameter_list|,
name|MllpSocketBuffer
name|mllpBuffer
parameter_list|)
block|{
name|TcpSocketConsumerRunnable
name|client
init|=
operator|new
name|TcpSocketConsumerRunnable
argument_list|(
name|this
argument_list|,
name|clientSocket
argument_list|,
name|mllpBuffer
argument_list|)
decl_stmt|;
name|consumerRunnables
operator|.
name|put
argument_list|(
name|client
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting consumer for Socket {}"
argument_list|,
name|clientSocket
argument_list|)
expr_stmt|;
name|consumerExecutor
operator|.
name|submit
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|rejectedExecutionEx
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot start consumer - max consumers already active"
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|doConnectionClose
argument_list|(
name|clientSocket
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|handleException (Throwable t)
specifier|public
name|void
name|handleException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|super
operator|.
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleException (String message, Throwable t)
specifier|public
name|void
name|handleException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
name|super
operator|.
name|handleException
argument_list|(
name|message
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

