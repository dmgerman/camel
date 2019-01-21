begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
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
name|List
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
name|Callable
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
name|TimeoutException
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
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
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
name|Suspendable
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_class
DECL|class|RabbitMQConsumer
specifier|public
class|class
name|RabbitMQConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Suspendable
block|{
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|conn
specifier|private
name|Connection
name|conn
decl_stmt|;
DECL|field|closeTimeout
specifier|private
name|int
name|closeTimeout
init|=
literal|30
operator|*
literal|1000
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|RabbitMQEndpoint
name|endpoint
decl_stmt|;
comment|/**      * Task in charge of starting consumer      */
DECL|field|startConsumerCallable
specifier|private
name|StartConsumerCallable
name|startConsumerCallable
decl_stmt|;
comment|/**      * Running consumers      */
DECL|field|consumers
specifier|private
specifier|final
name|List
argument_list|<
name|RabbitConsumer
argument_list|>
name|consumers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|RabbitMQConsumer (RabbitMQEndpoint endpoint, Processor processor)
specifier|public
name|RabbitMQConsumer
parameter_list|(
name|RabbitMQEndpoint
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
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RabbitMQEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RabbitMQEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Open connection      */
DECL|method|openConnection ()
specifier|private
name|void
name|openConnection
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating connection..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|conn
operator|=
name|getEndpoint
argument_list|()
operator|.
name|connect
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created connection: {}"
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the exiting open connection or opens a new one      */
DECL|method|getConnection ()
specifier|protected
specifier|synchronized
name|Connection
name|getConnection
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
if|if
condition|(
name|this
operator|.
name|conn
operator|==
literal|null
condition|)
block|{
name|openConnection
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|conn
return|;
block|}
elseif|else
if|if
condition|(
name|this
operator|.
name|conn
operator|.
name|isOpen
argument_list|()
operator|||
operator|(
operator|!
name|this
operator|.
name|conn
operator|.
name|isOpen
argument_list|()
operator|&&
name|isAutomaticRecoveryEnabled
argument_list|()
operator|)
condition|)
block|{
return|return
name|this
operator|.
name|conn
return|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"The existing connection is closed"
argument_list|)
expr_stmt|;
name|openConnection
argument_list|()
expr_stmt|;
return|return
name|this
operator|.
name|conn
return|;
block|}
block|}
DECL|method|isAutomaticRecoveryEnabled ()
specifier|private
name|boolean
name|isAutomaticRecoveryEnabled
parameter_list|()
block|{
return|return
name|this
operator|.
name|endpoint
operator|.
name|getAutomaticRecoveryEnabled
argument_list|()
operator|!=
literal|null
operator|&&
name|this
operator|.
name|endpoint
operator|.
name|getAutomaticRecoveryEnabled
argument_list|()
return|;
block|}
comment|/**      * Create the consumers but don't start yet      */
DECL|method|createConsumers ()
specifier|private
name|void
name|createConsumers
parameter_list|()
throws|throws
name|IOException
block|{
comment|// Create consumers but don't start yet
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|createConsumer
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Start the consumers (already created)      */
DECL|method|startConsumers ()
specifier|private
name|void
name|startConsumers
parameter_list|()
block|{
comment|// Try starting consumers (which will fail if RabbitMQ can't connect)
name|Throwable
name|fail
init|=
literal|null
decl_stmt|;
comment|// attempt to start all consumers if possible
for|for
control|(
name|RabbitConsumer
name|consumer
range|:
name|this
operator|.
name|consumers
control|)
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|fail
operator|=
name|e
expr_stmt|;
block|}
block|}
if|if
condition|(
name|fail
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connection failed starting consumers, will start background thread to retry!"
argument_list|,
name|fail
argument_list|)
expr_stmt|;
name|reconnect
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Add a consumer thread for given channel      */
DECL|method|createConsumer ()
specifier|private
name|void
name|createConsumer
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitConsumer
name|consumer
init|=
operator|new
name|RabbitConsumer
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|this
operator|.
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|reconnect ()
specifier|private
specifier|synchronized
name|void
name|reconnect
parameter_list|()
block|{
if|if
condition|(
name|startConsumerCallable
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
comment|// Open connection, and start message listener in background
name|Integer
name|networkRecoveryInterval
init|=
name|getEndpoint
argument_list|()
operator|.
name|getNetworkRecoveryInterval
argument_list|()
decl_stmt|;
specifier|final
name|long
name|connectionRetryInterval
init|=
name|networkRecoveryInterval
operator|!=
literal|null
operator|&&
name|networkRecoveryInterval
operator|>
literal|0
condition|?
name|networkRecoveryInterval
else|:
literal|100L
decl_stmt|;
name|startConsumerCallable
operator|=
operator|new
name|StartConsumerCallable
argument_list|(
name|connectionRetryInterval
argument_list|)
expr_stmt|;
name|executor
operator|.
name|submit
argument_list|(
name|startConsumerCallable
argument_list|)
expr_stmt|;
block|}
comment|/**      * If needed, close Connection and Channels      */
DECL|method|closeConnectionAndChannel ()
specifier|private
name|void
name|closeConnectionAndChannel
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
if|if
condition|(
name|startConsumerCallable
operator|!=
literal|null
condition|)
block|{
name|startConsumerCallable
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|RabbitConsumer
name|consumer
range|:
name|this
operator|.
name|consumers
control|)
block|{
try|try
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred while stopping consumer. This exception is ignored"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|consumers
operator|.
name|clear
argument_list|()
expr_stmt|;
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Closing connection: {} with timeout: {} ms."
argument_list|,
name|conn
argument_list|,
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|.
name|close
argument_list|(
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|closeConnectionAndChannel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|createConsumers
argument_list|()
expr_stmt|;
name|startConsumers
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
name|executor
operator|=
name|endpoint
operator|.
name|createExecutor
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Using executor {}"
argument_list|,
name|executor
argument_list|)
expr_stmt|;
name|createConsumers
argument_list|()
expr_stmt|;
name|startConsumers
argument_list|()
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
name|closeConnectionAndChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
operator|&&
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Task in charge of opening connection and adding listener when consumer is      * started and broker is not available.      */
DECL|class|StartConsumerCallable
specifier|private
class|class
name|StartConsumerCallable
implements|implements
name|Callable
argument_list|<
name|Void
argument_list|>
block|{
DECL|field|connectionRetryInterval
specifier|private
specifier|final
name|long
name|connectionRetryInterval
decl_stmt|;
DECL|field|running
specifier|private
specifier|final
name|AtomicBoolean
name|running
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|method|StartConsumerCallable (long connectionRetryInterval)
name|StartConsumerCallable
parameter_list|(
name|long
name|connectionRetryInterval
parameter_list|)
block|{
name|this
operator|.
name|connectionRetryInterval
operator|=
name|connectionRetryInterval
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|RabbitMQConsumer
operator|.
name|this
operator|.
name|startConsumerCallable
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|Void
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|connectionFailed
init|=
literal|true
decl_stmt|;
comment|// Reconnection loop
while|while
condition|(
name|running
operator|.
name|get
argument_list|()
operator|&&
name|connectionFailed
condition|)
block|{
try|try
block|{
for|for
control|(
name|RabbitConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|reconnect
argument_list|()
expr_stmt|;
block|}
name|connectionFailed
operator|=
literal|false
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Connection failed, will retry in "
operator|+
name|connectionRetryInterval
operator|+
literal|"ms"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|connectionRetryInterval
argument_list|)
expr_stmt|;
block|}
block|}
name|stop
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

