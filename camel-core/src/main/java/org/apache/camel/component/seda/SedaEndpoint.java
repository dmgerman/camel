begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

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
name|HashSet
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
name|BlockingQueue
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
name|PollingConsumer
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
name|processor
operator|.
name|MulticastProcessor
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
name|BrowsableEndpoint
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
comment|/**  * The seda component provides asynchronous call to another endpoint from any CamelContext in the same JVM.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed SedaEndpoint"
argument_list|)
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"seda"
argument_list|,
name|title
operator|=
literal|"SEDA"
argument_list|,
name|syntax
operator|=
literal|"seda:name"
argument_list|,
name|consumerClass
operator|=
name|SedaConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,endpoint"
argument_list|)
DECL|class|SedaEndpoint
specifier|public
class|class
name|SedaEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|BrowsableEndpoint
implements|,
name|MultipleConsumersSupport
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
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producers
specifier|private
specifier|final
name|Set
argument_list|<
name|SedaProducer
argument_list|>
name|producers
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|SedaProducer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|Set
argument_list|<
name|SedaConsumer
argument_list|>
name|consumers
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<
name|SedaConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|consumerMulticastProcessor
specifier|private
specifier|volatile
name|MulticastProcessor
name|consumerMulticastProcessor
decl_stmt|;
DECL|field|multicastStarted
specifier|private
specifier|volatile
name|boolean
name|multicastStarted
decl_stmt|;
DECL|field|multicastExecutor
specifier|private
specifier|volatile
name|ExecutorService
name|multicastExecutor
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
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Define the queue instance which will be used by the endpoint"
argument_list|)
DECL|field|queue
specifier|private
name|BlockingQueue
name|queue
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|size
specifier|private
name|int
name|size
init|=
name|Integer
operator|.
name|MAX_VALUE
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
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|limitConcurrentConsumers
specifier|private
name|boolean
name|limitConcurrentConsumers
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|multipleConsumers
specifier|private
name|boolean
name|multipleConsumers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|purgeWhenStopping
specifier|private
name|boolean
name|purgeWhenStopping
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|pollTimeout
specifier|private
name|int
name|pollTimeout
init|=
literal|1000
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
literal|"producer"
argument_list|)
DECL|field|failIfNoConsumers
specifier|private
name|boolean
name|failIfNoConsumers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|discardIfNoConsumers
specifier|private
name|boolean
name|discardIfNoConsumers
decl_stmt|;
DECL|field|queueFactory
specifier|private
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|queueFactory
decl_stmt|;
DECL|method|SedaEndpoint ()
specifier|public
name|SedaEndpoint
parameter_list|()
block|{
name|queueFactory
operator|=
operator|new
name|LinkedBlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|queue
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, Component component, BlockingQueue<Exchange> queue, int concurrentConsumers)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
if|if
condition|(
name|queue
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|size
operator|=
name|queue
operator|.
name|remainingCapacity
argument_list|()
expr_stmt|;
block|}
name|queueFactory
operator|=
operator|new
name|LinkedBlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
argument_list|()
expr_stmt|;
name|getComponent
argument_list|()
operator|.
name|registerQueue
argument_list|(
name|this
argument_list|,
name|queue
argument_list|)
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, Component component, BlockingQueueFactory<Exchange> queueFactory, int concurrentConsumers)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|BlockingQueueFactory
argument_list|<
name|Exchange
argument_list|>
name|queueFactory
parameter_list|,
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|concurrentConsumers
argument_list|)
expr_stmt|;
name|this
operator|.
name|queueFactory
operator|=
name|queueFactory
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, Component component, int concurrentConsumers)
specifier|private
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|int
name|concurrentConsumers
parameter_list|)
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
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SedaComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SedaComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SedaProducer
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
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// all consumers must match having the same multipleConsumers options
name|String
name|key
init|=
name|getComponent
argument_list|()
operator|.
name|getQueueKey
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|QueueReference
name|ref
init|=
name|getComponent
argument_list|()
operator|.
name|getQueueReference
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getMultipleConsumers
argument_list|()
operator|!=
name|isMultipleConsumers
argument_list|()
condition|)
block|{
comment|// there is already a multiple consumers, so make sure they matches
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot use existing queue "
operator|+
name|key
operator|+
literal|" as the existing queue multiple consumers "
operator|+
name|ref
operator|.
name|getMultipleConsumers
argument_list|()
operator|+
literal|" does not match given multiple consumers "
operator|+
name|multipleConsumers
argument_list|)
throw|;
block|}
block|}
name|Consumer
name|answer
init|=
name|createNewConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createNewConsumer (Processor processor)
specifier|protected
name|SedaConsumer
name|createNewConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
return|return
operator|new
name|SedaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|SedaPollingConsumer
name|answer
init|=
operator|new
name|SedaPollingConsumer
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getQueue ()
specifier|public
specifier|synchronized
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|getQueue
parameter_list|()
block|{
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
comment|// prefer to lookup queue from component, so if this endpoint is re-created or re-started
comment|// then the existing queue from the component can be used, so new producers and consumers
comment|// can use the already existing queue referenced from the component
if|if
condition|(
name|getComponent
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// use null to indicate default size (= use what the existing queue has been configured with)
name|Integer
name|size
init|=
name|getSize
argument_list|()
operator|==
name|Integer
operator|.
name|MAX_VALUE
condition|?
literal|null
else|:
name|getSize
argument_list|()
decl_stmt|;
name|QueueReference
name|ref
init|=
name|getComponent
argument_list|()
operator|.
name|getOrCreateQueue
argument_list|(
name|this
argument_list|,
name|size
argument_list|,
name|isMultipleConsumers
argument_list|()
argument_list|,
name|queueFactory
argument_list|)
decl_stmt|;
name|queue
operator|=
name|ref
operator|.
name|getQueue
argument_list|()
expr_stmt|;
name|String
name|key
init|=
name|getComponent
argument_list|()
operator|.
name|getQueueKey
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint {} is using shared queue: {} with size: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|this
block|,
name|key
block|,
name|ref
operator|.
name|getSize
argument_list|()
operator|!=
literal|null
condition|?
name|ref
operator|.
name|getSize
argument_list|()
else|:
name|Integer
operator|.
name|MAX_VALUE
block|}
argument_list|)
expr_stmt|;
comment|// and set the size we are using
if|if
condition|(
name|ref
operator|.
name|getSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|setSize
argument_list|(
name|ref
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// fallback and create queue (as this endpoint has no component)
name|queue
operator|=
name|createQueue
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint {} is using queue: {} with size: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|this
block|,
name|getEndpointUri
argument_list|()
block|,
name|getSize
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|queue
return|;
block|}
DECL|method|createQueue ()
specifier|protected
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|createQueue
parameter_list|()
block|{
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
return|return
name|queueFactory
operator|.
name|create
argument_list|(
name|size
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|queueFactory
operator|.
name|create
argument_list|()
return|;
block|}
block|}
comment|/**      * Get's the {@link QueueReference} for the this endpoint.      * @return the reference, or<tt>null</tt> if no queue reference exists.      */
DECL|method|getQueueReference ()
specifier|public
specifier|synchronized
name|QueueReference
name|getQueueReference
parameter_list|()
block|{
name|String
name|key
init|=
name|getComponent
argument_list|()
operator|.
name|getQueueKey
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|QueueReference
name|ref
init|=
name|getComponent
argument_list|()
operator|.
name|getQueueReference
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|ref
return|;
block|}
DECL|method|getConsumerMulticastProcessor ()
specifier|protected
specifier|synchronized
name|MulticastProcessor
name|getConsumerMulticastProcessor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|multicastStarted
operator|&&
name|consumerMulticastProcessor
operator|!=
literal|null
condition|)
block|{
comment|// only start it on-demand to avoid starting it during stopping
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumerMulticastProcessor
argument_list|)
expr_stmt|;
name|multicastStarted
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|consumerMulticastProcessor
return|;
block|}
DECL|method|updateMulticastProcessor ()
specifier|protected
specifier|synchronized
name|void
name|updateMulticastProcessor
parameter_list|()
throws|throws
name|Exception
block|{
comment|// only needed if we support multiple consumers
if|if
condition|(
operator|!
name|isMultipleConsumersSupported
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// stop old before we create a new
if|if
condition|(
name|consumerMulticastProcessor
operator|!=
literal|null
condition|)
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumerMulticastProcessor
argument_list|)
expr_stmt|;
name|consumerMulticastProcessor
operator|=
literal|null
expr_stmt|;
block|}
name|int
name|size
init|=
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|size
operator|>=
literal|1
condition|)
block|{
if|if
condition|(
name|multicastExecutor
operator|==
literal|null
condition|)
block|{
comment|// create multicast executor as we need it when we have more than 1 processor
name|multicastExecutor
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"(multicast)"
argument_list|)
expr_stmt|;
block|}
comment|// create list of consumers to multicast to
name|List
argument_list|<
name|Processor
argument_list|>
name|processors
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
name|size
argument_list|)
decl_stmt|;
for|for
control|(
name|SedaConsumer
name|consumer
range|:
name|getConsumers
argument_list|()
control|)
block|{
name|processors
operator|.
name|add
argument_list|(
name|consumer
operator|.
name|getProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// create multicast processor
name|multicastStarted
operator|=
literal|false
expr_stmt|;
name|consumerMulticastProcessor
operator|=
operator|new
name|MulticastProcessor
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|processors
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|multicastExecutor
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Define the queue instance which will be used by the endpoint.      *<p/>      * This option is only for rare use-cases where you want to use a custom queue instance.      */
DECL|method|setQueue (BlockingQueue<Exchange> queue)
specifier|public
name|void
name|setQueue
parameter_list|(
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|queue
operator|.
name|remainingCapacity
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Queue max capacity"
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
comment|/**      * The maximum capacity of the SEDA queue (i.e., the number of messages it can hold).      */
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
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current queue size"
argument_list|)
DECL|method|getCurrentQueueSize ()
specifier|public
name|int
name|getCurrentQueueSize
parameter_list|()
block|{
return|return
name|queue
operator|.
name|size
argument_list|()
return|;
block|}
comment|/**      * Whether a thread that sends messages to a full SEDA queue will block until the queue's capacity is no longer exhausted.      * By default, an exception will be thrown stating that the queue is full.      * By enabling this option, the calling thread will instead block and wait until the message can be accepted.      */
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
literal|"Whether the caller will block sending to a full queue"
argument_list|)
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
comment|/**      * Number of concurrent threads processing exchanges.      */
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
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
DECL|method|isLimitConcurrentConsumers ()
specifier|public
name|boolean
name|isLimitConcurrentConsumers
parameter_list|()
block|{
return|return
name|limitConcurrentConsumers
return|;
block|}
comment|/**      * Whether to limit the number of concurrentConsumers to the maximum of 500.      * By default, an exception will be thrown if an endpoint is configured with a greater number. You can disable that check by turning this option off.      */
DECL|method|setLimitConcurrentConsumers (boolean limitConcurrentConsumers)
specifier|public
name|void
name|setLimitConcurrentConsumers
parameter_list|(
name|boolean
name|limitConcurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|limitConcurrentConsumers
operator|=
name|limitConcurrentConsumers
expr_stmt|;
block|}
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
comment|/**      * Option to specify whether the caller should wait for the async task to complete or not before continuing.      * The following three options are supported: Always, Never or IfReplyExpected.      * The first two values are self-explanatory.      * The last value, IfReplyExpected, will only wait if the message is Request Reply based.      * The default option is IfReplyExpected.      */
DECL|method|setWaitForTaskToComplete (WaitForTaskToComplete waitForTaskToComplete)
specifier|public
name|void
name|setWaitForTaskToComplete
parameter_list|(
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
comment|/**      * Timeout (in milliseconds) before a SEDA producer will stop waiting for an asynchronous task to complete.      * You can disable timeout by using 0 or a negative value.      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
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
DECL|method|isFailIfNoConsumers ()
specifier|public
name|boolean
name|isFailIfNoConsumers
parameter_list|()
block|{
return|return
name|failIfNoConsumers
return|;
block|}
comment|/**      * Whether the producer should fail by throwing an exception, when sending to a queue with no active consumers.      *<p/>      * Only one of the options<tt>discardIfNoConsumers</tt> and<tt>failIfNoConsumers</tt> can be enabled at the same time.      */
DECL|method|setFailIfNoConsumers (boolean failIfNoConsumers)
specifier|public
name|void
name|setFailIfNoConsumers
parameter_list|(
name|boolean
name|failIfNoConsumers
parameter_list|)
block|{
name|this
operator|.
name|failIfNoConsumers
operator|=
name|failIfNoConsumers
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isDiscardIfNoConsumers ()
specifier|public
name|boolean
name|isDiscardIfNoConsumers
parameter_list|()
block|{
return|return
name|discardIfNoConsumers
return|;
block|}
comment|/**      * Whether the producer should discard the message (do not add the message to the queue), when sending to a queue with no active consumers.      *<p/>      * Only one of the options<tt>discardIfNoConsumers</tt> and<tt>failIfNoConsumers</tt> can be enabled at the same time.      */
DECL|method|setDiscardIfNoConsumers (boolean discardIfNoConsumers)
specifier|public
name|void
name|setDiscardIfNoConsumers
parameter_list|(
name|boolean
name|discardIfNoConsumers
parameter_list|)
block|{
name|this
operator|.
name|discardIfNoConsumers
operator|=
name|discardIfNoConsumers
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
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
comment|/**      * Specifies whether multiple consumers are allowed. If enabled, you can use SEDA for Publish-Subscribe messaging.      * That is, you can send a message to the SEDA queue and have each consumer receive a copy of the message.      * When enabled, this option should be specified on every consumer endpoint.      */
DECL|method|setMultipleConsumers (boolean multipleConsumers)
specifier|public
name|void
name|setMultipleConsumers
parameter_list|(
name|boolean
name|multipleConsumers
parameter_list|)
block|{
name|this
operator|.
name|multipleConsumers
operator|=
name|multipleConsumers
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|getPollTimeout ()
specifier|public
name|int
name|getPollTimeout
parameter_list|()
block|{
return|return
name|pollTimeout
return|;
block|}
comment|/**      * The timeout used when polling. When a timeout occurs, the consumer can check whether it is allowed to continue running.      * Setting a lower value allows the consumer to react more quickly upon shutdown.      */
DECL|method|setPollTimeout (int pollTimeout)
specifier|public
name|void
name|setPollTimeout
parameter_list|(
name|int
name|pollTimeout
parameter_list|)
block|{
name|this
operator|.
name|pollTimeout
operator|=
name|pollTimeout
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
DECL|method|isPurgeWhenStopping ()
specifier|public
name|boolean
name|isPurgeWhenStopping
parameter_list|()
block|{
return|return
name|purgeWhenStopping
return|;
block|}
comment|/**      * Whether to purge the task queue when stopping the consumer/route.      * This allows to stop faster, as any pending messages on the queue is discarded.      */
DECL|method|setPurgeWhenStopping (boolean purgeWhenStopping)
specifier|public
name|void
name|setPurgeWhenStopping
parameter_list|(
name|boolean
name|purgeWhenStopping
parameter_list|)
block|{
name|this
operator|.
name|purgeWhenStopping
operator|=
name|purgeWhenStopping
expr_stmt|;
block|}
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
comment|/**      * Returns the current pending exchanges      */
DECL|method|getExchanges ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchanges
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|getQueue
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|ManagedAttribute
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
comment|/**      * Purges the queue      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Purges the seda queue"
argument_list|)
DECL|method|purgeQueue ()
specifier|public
name|void
name|purgeQueue
parameter_list|()
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Purging queue with {} exchanges"
argument_list|,
name|queue
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Returns the current active consumers on this endpoint      */
DECL|method|getConsumers ()
specifier|public
name|Set
argument_list|<
name|SedaConsumer
argument_list|>
name|getConsumers
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|SedaConsumer
argument_list|>
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
name|SedaProducer
argument_list|>
name|getProducers
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<
name|SedaProducer
argument_list|>
argument_list|(
name|producers
argument_list|)
return|;
block|}
DECL|method|onStarted (SedaProducer producer)
name|void
name|onStarted
parameter_list|(
name|SedaProducer
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
DECL|method|onStopped (SedaProducer producer)
name|void
name|onStopped
parameter_list|(
name|SedaProducer
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
DECL|method|onStarted (SedaConsumer consumer)
name|void
name|onStarted
parameter_list|(
name|SedaConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
if|if
condition|(
name|isMultipleConsumers
argument_list|()
condition|)
block|{
name|updateMulticastProcessor
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onStopped (SedaConsumer consumer)
name|void
name|onStopped
parameter_list|(
name|SedaConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
if|if
condition|(
name|isMultipleConsumers
argument_list|()
condition|)
block|{
name|updateMulticastProcessor
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|hasConsumers ()
specifier|public
name|boolean
name|hasConsumers
parameter_list|()
block|{
return|return
name|this
operator|.
name|consumers
operator|.
name|size
argument_list|()
operator|>
literal|0
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
comment|// force creating queue when starting
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
name|queue
operator|=
name|getQueue
argument_list|()
expr_stmt|;
block|}
comment|// special for unit testing where we can set a system property to make seda poll faster
comment|// and therefore also react faster upon shutdown, which makes overall testing faster of the Camel project
name|String
name|override
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"CamelSedaPollTimeout"
argument_list|,
literal|""
operator|+
name|getPollTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|setPollTimeout
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
name|override
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|getConsumers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"There is still active consumers."
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Service already shut down"
argument_list|)
expr_stmt|;
return|return;
block|}
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
if|if
condition|(
name|getConsumers
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|super
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"There is still active consumers."
argument_list|)
expr_stmt|;
block|}
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
comment|// shutdown thread pool if it was in use
if|if
condition|(
name|multicastExecutor
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|multicastExecutor
argument_list|)
expr_stmt|;
name|multicastExecutor
operator|=
literal|null
expr_stmt|;
block|}
comment|// clear queue, as we are shutdown, so if re-created then the queue must be updated
name|queue
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

