begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
package|;
end_package

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
name|Collections
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
name|Set
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
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|InsufficientCapacityException
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
name|AsyncEndpoint
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
name|Component
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
name|Consumer
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
name|MultipleConsumersSupport
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
name|WaitForTaskToComplete
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
name|impl
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
comment|/**  * The disruptor component provides asynchronous SEDA behavior using LMAX Disruptor.  *  * This component works much as the standard SEDA Component, but utilizes a Disruptor  * instead of a BlockingQueue utilized by the standard SEDA.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Disruptor Endpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.12.0"
argument_list|,
name|scheme
operator|=
literal|"disruptor,disruptor-vm"
argument_list|,
name|title
operator|=
literal|"Disruptor,Disruptor VM"
argument_list|,
name|syntax
operator|=
literal|"disruptor:name"
argument_list|,
name|consumerClass
operator|=
name|DisruptorConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"endpoint"
argument_list|)
DECL|class|DisruptorEndpoint
specifier|public
class|class
name|DisruptorEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|AsyncEndpoint
implements|,
name|MultipleConsumersSupport
block|{
DECL|field|DISRUPTOR_IGNORE_EXCHANGE
specifier|public
specifier|static
specifier|final
name|String
name|DISRUPTOR_IGNORE_EXCHANGE
init|=
literal|"disruptor.ignoreExchange"
decl_stmt|;
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DisruptorEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producers
specifier|private
specifier|final
name|Set
argument_list|<
name|DisruptorProducer
argument_list|>
name|producers
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|Set
argument_list|<
name|DisruptorConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|disruptorReference
specifier|private
specifier|final
name|DisruptorReference
name|disruptorReference
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of queue"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|concurrentConsumers
specifier|private
specifier|final
name|int
name|concurrentConsumers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|multipleConsumers
specifier|private
specifier|final
name|boolean
name|multipleConsumers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"IfReplyExpected"
argument_list|)
DECL|field|waitForTaskToComplete
specifier|private
name|WaitForTaskToComplete
name|waitForTaskToComplete
init|=
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DisruptorComponent
operator|.
name|DEFAULT_BUFFER_SIZE
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|blockWhenFull
specifier|private
name|boolean
name|blockWhenFull
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"Blocking"
argument_list|)
DECL|field|waitStrategy
specifier|private
name|DisruptorWaitStrategy
name|waitStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"Multi"
argument_list|)
DECL|field|producerType
specifier|private
name|DisruptorProducerType
name|producerType
decl_stmt|;
DECL|method|DisruptorEndpoint (final String endpointUri, final Component component, final DisruptorReference disruptorReference, final int concurrentConsumers, final boolean multipleConsumers, boolean blockWhenFull)
specifier|public
name|DisruptorEndpoint
parameter_list|(
specifier|final
name|String
name|endpointUri
parameter_list|,
specifier|final
name|Component
name|component
parameter_list|,
specifier|final
name|DisruptorReference
name|disruptorReference
parameter_list|,
specifier|final
name|int
name|concurrentConsumers
parameter_list|,
specifier|final
name|boolean
name|multipleConsumers
parameter_list|,
name|boolean
name|blockWhenFull
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|disruptorReference
operator|=
name|disruptorReference
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|disruptorReference
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
name|this
operator|.
name|multipleConsumers
operator|=
name|multipleConsumers
expr_stmt|;
name|this
operator|.
name|blockWhenFull
operator|=
name|blockWhenFull
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Queue name"
argument_list|)
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Buffer max capacity"
argument_list|)
DECL|method|getBufferSize ()
specifier|public
name|int
name|getBufferSize
parameter_list|()
block|{
return|return
name|disruptorReference
operator|.
name|getBufferSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Remaining capacity in ring buffer"
argument_list|)
DECL|method|getRemainingCapacity ()
specifier|public
name|long
name|getRemainingCapacity
parameter_list|()
throws|throws
name|DisruptorNotStartedException
block|{
return|return
name|getDisruptor
argument_list|()
operator|.
name|getRemainingCapacity
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Amount of pending exchanges waiting for consumption in ring buffer"
argument_list|)
DECL|method|getPendingExchangeCount ()
specifier|public
name|long
name|getPendingExchangeCount
parameter_list|()
throws|throws
name|DisruptorNotStartedException
block|{
return|return
name|getDisruptor
argument_list|()
operator|.
name|getPendingExchangeCount
argument_list|()
return|;
block|}
comment|/**      * Number of concurrent threads processing exchanges.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of concurrent consumers"
argument_list|)
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Option to specify whether the caller should wait for the async task to complete or not before continuing"
argument_list|)
DECL|method|getWaitForTaskToComplete ()
specifier|public
name|WaitForTaskToComplete
name|getWaitForTaskToComplete
parameter_list|()
block|{
return|return
name|waitForTaskToComplete
return|;
block|}
comment|/**      * Option to specify whether the caller should wait for the async task to complete or not before continuing.      * The following three options are supported: Always, Never or IfReplyExpected. The first two values are self-explanatory.      * The last value, IfReplyExpected, will only wait if the message is Request Reply based.      */
DECL|method|setWaitForTaskToComplete (final WaitForTaskToComplete waitForTaskToComplete)
specifier|public
name|void
name|setWaitForTaskToComplete
parameter_list|(
specifier|final
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|)
block|{
name|this
operator|.
name|waitForTaskToComplete
operator|=
name|waitForTaskToComplete
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timeout (in milliseconds) before a producer will stop waiting for an asynchronous task to complete"
argument_list|)
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Timeout (in milliseconds) before a producer will stop waiting for an asynchronous task to complete.      * You can disable timeout by using 0 or a negative value.      */
DECL|method|setTimeout (final long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
specifier|final
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The maximum capacity of the Disruptors ringbuffer"
argument_list|)
DECL|method|getSize ()
specifier|public
name|int
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
comment|/**      * The maximum capacity of the Disruptors ringbuffer      * Will be effectively increased to the nearest power of two.      * Notice: Mind if you use this option, then its the first endpoint being created with the queue name,      * that determines the size. To make sure all endpoints use same size, then configure the size option      * on all of them, or the first endpoint being created.      */
DECL|method|setSize (int size)
specifier|public
name|void
name|setSize
parameter_list|(
name|int
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Specifies whether multiple consumers are allowed"
argument_list|)
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
name|isMultipleConsumers
argument_list|()
return|;
block|}
comment|/**      * Specifies whether multiple consumers are allowed.      * If enabled, you can use Disruptor for Publish-Subscribe messaging.      * That is, you can send a message to the queue and have each consumer receive a copy of the message.      * When enabled, this option should be specified on every consumer endpoint.      */
DECL|method|isMultipleConsumers ()
specifier|public
name|boolean
name|isMultipleConsumers
parameter_list|()
block|{
return|return
name|multipleConsumers
return|;
block|}
comment|/**      * Returns the current active consumers on this endpoint      */
DECL|method|getConsumers ()
specifier|public
name|Set
argument_list|<
name|DisruptorConsumer
argument_list|>
name|getConsumers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|consumers
argument_list|)
return|;
block|}
comment|/**      * Returns the current active producers on this endpoint      */
DECL|method|getProducers ()
specifier|public
name|Set
argument_list|<
name|DisruptorProducer
argument_list|>
name|getProducers
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|producers
argument_list|)
return|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isBlockWhenFull ()
specifier|public
name|boolean
name|isBlockWhenFull
parameter_list|()
block|{
return|return
name|blockWhenFull
return|;
block|}
comment|/**      * Whether a thread that sends messages to a full Disruptor will block until the ringbuffer's capacity is no longer exhausted.      * By default, the calling thread will block and wait until the message can be accepted.      * By disabling this option, an exception will be thrown stating that the queue is full.      */
DECL|method|setBlockWhenFull (boolean blockWhenFull)
specifier|public
name|void
name|setBlockWhenFull
parameter_list|(
name|boolean
name|blockWhenFull
parameter_list|)
block|{
name|this
operator|.
name|blockWhenFull
operator|=
name|blockWhenFull
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Defines the strategy used by consumer threads to wait on new exchanges to be published"
argument_list|)
DECL|method|getWaitStrategy ()
specifier|public
name|DisruptorWaitStrategy
name|getWaitStrategy
parameter_list|()
block|{
return|return
name|waitStrategy
return|;
block|}
comment|/**      * Defines the strategy used by consumer threads to wait on new exchanges to be published.      * The options allowed are:Blocking, Sleeping, BusySpin and Yielding.      */
DECL|method|setWaitStrategy (DisruptorWaitStrategy waitStrategy)
specifier|public
name|void
name|setWaitStrategy
parameter_list|(
name|DisruptorWaitStrategy
name|waitStrategy
parameter_list|)
block|{
name|this
operator|.
name|waitStrategy
operator|=
name|waitStrategy
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|" Defines the producers allowed on the Disruptor"
argument_list|)
DECL|method|getProducerType ()
specifier|public
name|DisruptorProducerType
name|getProducerType
parameter_list|()
block|{
return|return
name|producerType
return|;
block|}
comment|/**      * Defines the producers allowed on the Disruptor.      * The options allowed are: Multi to allow multiple producers and Single to enable certain optimizations only      * allowed when one concurrent producer (on one thread or otherwise synchronized) is active.      */
DECL|method|setProducerType (DisruptorProducerType producerType)
specifier|public
name|void
name|setProducerType
parameter_list|(
name|DisruptorProducerType
name|producerType
parameter_list|)
block|{
name|this
operator|.
name|producerType
operator|=
name|producerType
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
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getProducers
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
operator|&&
name|getDisruptor
argument_list|()
operator|.
name|getProducerType
argument_list|()
operator|==
name|DisruptorProducerType
operator|.
name|Single
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Endpoint can't support multiple producers when ProducerType SINGLE is configured"
argument_list|)
throw|;
block|}
return|return
operator|new
name|DisruptorProducer
argument_list|(
name|this
argument_list|,
name|getWaitForTaskToComplete
argument_list|()
argument_list|,
name|getTimeout
argument_list|()
argument_list|,
name|isBlockWhenFull
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (final Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DisruptorConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
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
comment|// notify reference we are shutting down this endpoint
name|disruptorReference
operator|.
name|addEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
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
comment|// notify reference we are shutting down this endpoint
name|disruptorReference
operator|.
name|removeEndpoint
argument_list|(
name|this
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
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
comment|// notify component we are shutting down this endpoint
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getComponent
argument_list|()
operator|.
name|onShutdownEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|DisruptorComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|DisruptorComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|onStarted (final DisruptorConsumer consumer)
name|void
name|onStarted
parameter_list|(
specifier|final
name|DisruptorConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|// validate multiple consumers has been enabled is necessary
if|if
condition|(
operator|!
name|consumers
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|isMultipleConsumersSupported
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Multiple consumers for the same endpoint is not allowed: "
operator|+
name|this
argument_list|)
throw|;
block|}
if|if
condition|(
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Starting consumer {} on endpoint {}"
argument_list|,
name|consumer
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|getDisruptor
argument_list|()
operator|.
name|reconfigure
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Tried to start Consumer {} on endpoint {} but it was already started"
argument_list|,
name|consumer
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onStopped (final DisruptorConsumer consumer)
name|void
name|onStopped
parameter_list|(
specifier|final
name|DisruptorConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
if|if
condition|(
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Stopping consumer {} on endpoint {}"
argument_list|,
name|consumer
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|getDisruptor
argument_list|()
operator|.
name|reconfigure
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Tried to stop Consumer {} on endpoint {} but it was already stopped"
argument_list|,
name|consumer
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|onStarted (final DisruptorProducer producer)
name|void
name|onStarted
parameter_list|(
specifier|final
name|DisruptorProducer
name|producer
parameter_list|)
block|{
name|producers
operator|.
name|add
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|onStopped (final DisruptorProducer producer)
name|void
name|onStopped
parameter_list|(
specifier|final
name|DisruptorProducer
name|producer
parameter_list|)
block|{
name|producers
operator|.
name|remove
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumerEventHandlers ()
name|Map
argument_list|<
name|DisruptorConsumer
argument_list|,
name|Collection
argument_list|<
name|LifecycleAwareExchangeEventHandler
argument_list|>
argument_list|>
name|createConsumerEventHandlers
parameter_list|()
block|{
name|Map
argument_list|<
name|DisruptorConsumer
argument_list|,
name|Collection
argument_list|<
name|LifecycleAwareExchangeEventHandler
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|DisruptorConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|result
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|consumer
operator|.
name|createEventHandlers
argument_list|(
name|concurrentConsumers
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * Called by DisruptorProducers to publish new exchanges on the RingBuffer, blocking when full      */
DECL|method|publish (final Exchange exchange)
name|void
name|publish
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|DisruptorNotStartedException
block|{
name|disruptorReference
operator|.
name|publish
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Called by DisruptorProducers to publish new exchanges on the RingBuffer, throwing InsufficientCapacityException      * when full      *      * @throws InsufficientCapacityException when the Ringbuffer is full.      */
DECL|method|tryPublish (final Exchange exchange)
name|void
name|tryPublish
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|DisruptorNotStartedException
throws|,
name|InsufficientCapacityException
block|{
name|disruptorReference
operator|.
name|tryPublish
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|getDisruptor ()
name|DisruptorReference
name|getDisruptor
parameter_list|()
block|{
return|return
name|disruptorReference
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|boolean
name|result
init|=
name|super
operator|.
name|equals
argument_list|(
name|object
argument_list|)
decl_stmt|;
return|return
name|result
operator|&&
name|getCamelContext
argument_list|()
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|DisruptorEndpoint
operator|)
name|object
operator|)
operator|.
name|getCamelContext
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getEndpointUri
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
operator|+
name|getCamelContext
argument_list|()
operator|.
name|hashCode
argument_list|()
return|;
block|}
block|}
end_class

end_unit

