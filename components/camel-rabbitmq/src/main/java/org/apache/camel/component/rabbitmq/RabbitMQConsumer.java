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
name|AMQP
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
name|Channel
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
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Envelope
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
name|ExchangePattern
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
name|impl
operator|.
name|DefaultConsumer
import|;
end_import

begin_class
DECL|class|RabbitMQConsumer
specifier|public
class|class
name|RabbitMQConsumer
extends|extends
name|DefaultConsumer
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
argument_list|<
name|RabbitConsumer
argument_list|>
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
comment|/**      * Open channel      */
DECL|method|openChannel ()
specifier|private
name|Channel
name|openChannel
parameter_list|()
throws|throws
name|IOException
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating channel..."
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|conn
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created channel: {}"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
comment|// setup the basicQos
if|if
condition|(
name|endpoint
operator|.
name|isPrefetchEnabled
argument_list|()
condition|)
block|{
name|channel
operator|.
name|basicQos
argument_list|(
name|endpoint
operator|.
name|getPrefetchSize
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPrefetchCount
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isPrefetchGlobal
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|channel
return|;
block|}
comment|/**      * Add a consumer thread for given channel      */
DECL|method|startConsumers ()
specifier|private
name|void
name|startConsumers
parameter_list|()
throws|throws
name|IOException
block|{
comment|// First channel used to declare Exchange and Queue
name|Channel
name|channel
init|=
name|openChannel
argument_list|()
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isDeclare
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|declareExchangeAndQueue
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
name|startConsumer
argument_list|(
name|channel
argument_list|)
expr_stmt|;
comment|// Other channels
for|for
control|(
name|int
name|i
init|=
literal|1
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
name|channel
operator|=
name|openChannel
argument_list|()
expr_stmt|;
name|startConsumer
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add a consumer thread for given channel      */
DECL|method|startConsumer (Channel channel)
specifier|private
name|void
name|startConsumer
parameter_list|(
name|Channel
name|channel
parameter_list|)
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
argument_list|,
name|channel
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
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
try|try
block|{
name|openConnection
argument_list|()
expr_stmt|;
name|startConsumers
argument_list|()
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
literal|"Connection failed, will start background thread to retry!"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|reconnect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|reconnect ()
specifier|private
name|void
name|reconnect
parameter_list|()
block|{
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
name|reconnect
argument_list|()
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
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Timeout occured"
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
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
DECL|class|RabbitConsumer
class|class
name|RabbitConsumer
extends|extends
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|DefaultConsumer
block|{
DECL|field|consumer
specifier|private
specifier|final
name|RabbitMQConsumer
name|consumer
decl_stmt|;
DECL|field|channel
specifier|private
specifier|final
name|Channel
name|channel
decl_stmt|;
DECL|field|tag
specifier|private
name|String
name|tag
decl_stmt|;
comment|/**          * Constructs a new instance and records its association to the          * passed-in channel.          *          * @param channel the channel to which this consumer is attached          */
DECL|method|RabbitConsumer (RabbitMQConsumer consumer, Channel channel)
specifier|public
name|RabbitConsumer
parameter_list|(
name|RabbitMQConsumer
name|consumer
parameter_list|,
name|Channel
name|channel
parameter_list|)
block|{
name|super
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleDelivery (String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
specifier|public
name|void
name|handleDelivery
parameter_list|(
name|String
name|consumerTag
parameter_list|,
name|Envelope
name|envelope
parameter_list|,
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|,
name|byte
index|[]
name|body
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|endpoint
operator|.
name|createRabbitExchange
argument_list|(
name|envelope
argument_list|,
name|properties
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getMessageConverter
argument_list|()
operator|.
name|mergeAmqpProperties
argument_list|(
name|exchange
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|boolean
name|sendReply
init|=
name|properties
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|sendReply
operator|&&
operator|!
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"In an inOut capable route"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Created exchange [exchange={}]"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|long
name|deliveryTag
init|=
name|envelope
operator|.
name|getDeliveryTag
argument_list|()
decl_stmt|;
try|try
block|{
name|consumer
operator|.
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
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// obtain the message after processing
name|Message
name|msg
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// processing success
if|if
condition|(
name|sendReply
operator|&&
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
try|try
block|{
name|endpoint
operator|.
name|publishExchangeToChannel
argument_list|(
name|exchange
argument_list|,
name|channel
argument_list|,
name|properties
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
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
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|consumer
operator|.
name|endpoint
operator|.
name|isAutoAck
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Acknowledging receipt [delivery_tag={}]"
argument_list|,
name|deliveryTag
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicAck
argument_list|(
name|deliveryTag
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|isTransferException
argument_list|()
operator|&&
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
comment|// the inOut exchange failed so put the exception in the body
comment|// and send back
name|msg
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOut
argument_list|(
name|msg
argument_list|)
expr_stmt|;
try|try
block|{
name|endpoint
operator|.
name|publishExchangeToChannel
argument_list|(
name|exchange
argument_list|,
name|channel
argument_list|,
name|properties
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|e
parameter_list|)
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
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|consumer
operator|.
name|endpoint
operator|.
name|isAutoAck
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Acknowledging receipt when transferring exception [delivery_tag={}]"
argument_list|,
name|deliveryTag
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicAck
argument_list|(
name|deliveryTag
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
name|boolean
name|isRequeueHeaderSet
init|=
name|msg
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REQUEUE
argument_list|,
literal|false
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// processing failed, then reject and handle the exception
if|if
condition|(
name|deliveryTag
operator|!=
literal|0
operator|&&
operator|!
name|consumer
operator|.
name|endpoint
operator|.
name|isAutoAck
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Rejecting receipt [delivery_tag={}] with requeue={}"
argument_list|,
name|deliveryTag
argument_list|,
name|isRequeueHeaderSet
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRequeueHeaderSet
condition|)
block|{
name|channel
operator|.
name|basicReject
argument_list|(
name|deliveryTag
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|channel
operator|.
name|basicReject
argument_list|(
name|deliveryTag
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**          * Bind consumer to channel          */
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|IOException
block|{
name|tag
operator|=
name|channel
operator|.
name|basicConsume
argument_list|(
name|endpoint
operator|.
name|getQueue
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isAutoAck
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**          * Unbind consumer from channel          */
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
if|if
condition|(
name|tag
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|basicCancel
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|channel
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Timeout occured"
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
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
specifier|public
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
name|openConnection
argument_list|()
expr_stmt|;
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
literal|"Connection failed, will retry in {}"
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
if|if
condition|(
operator|!
name|connectionFailed
condition|)
block|{
name|startConsumers
argument_list|()
expr_stmt|;
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

