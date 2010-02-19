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
name|LinkedBlockingQueue
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
name|BrowsableEndpoint
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a  * href="http://camel.apache.org/queue.html">Queue components</a> for  * asynchronous SEDA exchanges on a {@link BlockingQueue} within a CamelContext  *  * @version $Revision$  */
end_comment

begin_class
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
DECL|field|queue
specifier|private
specifier|volatile
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|field|size
specifier|private
name|int
name|size
decl_stmt|;
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
DECL|field|multipleConsumers
specifier|private
name|boolean
name|multipleConsumers
decl_stmt|;
DECL|field|waitForTaskToComplete
specifier|private
name|WaitForTaskToComplete
name|waitForTaskToComplete
init|=
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000
decl_stmt|;
DECL|field|producers
specifier|private
specifier|volatile
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
specifier|volatile
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
DECL|method|SedaEndpoint ()
specifier|public
name|SedaEndpoint
parameter_list|()
block|{     }
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
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, BlockingQueue<Exchange> queue)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
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
name|queue
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|SedaEndpoint (String endpointUri, BlockingQueue<Exchange> queue, int concurrentConsumers)
specifier|public
name|SedaEndpoint
parameter_list|(
name|String
name|endpointUri
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
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
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
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
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
name|getQueue
argument_list|()
argument_list|,
name|getWaitForTaskToComplete
argument_list|()
argument_list|,
name|getTimeout
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
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|size
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|queue
return|;
block|}
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
block|{
name|consumers
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|onStopped (SedaConsumer consumer)
name|void
name|onStopped
parameter_list|(
name|SedaConsumer
name|consumer
parameter_list|)
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

