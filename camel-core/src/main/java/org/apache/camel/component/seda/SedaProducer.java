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
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Predicate
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
name|DefaultAsyncProducer
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
name|Synchronization
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
name|SynchronizationVetoable
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
name|SynchronizationAdapter
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SedaProducer
specifier|public
class|class
name|SedaProducer
extends|extends
name|DefaultAsyncProducer
block|{
comment|/**      * @deprecated Better make use of the {@link SedaEndpoint#getQueue()} API which delivers the accurate reference to the queue currently being used.      */
annotation|@
name|Deprecated
DECL|field|queue
specifier|protected
specifier|final
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|SedaEndpoint
name|endpoint
decl_stmt|;
DECL|field|waitForTaskToComplete
specifier|private
specifier|final
name|WaitForTaskToComplete
name|waitForTaskToComplete
decl_stmt|;
DECL|field|timeout
specifier|private
specifier|final
name|long
name|timeout
decl_stmt|;
DECL|field|blockWhenFull
specifier|private
specifier|final
name|boolean
name|blockWhenFull
decl_stmt|;
comment|/**      * @deprecated Use {@link #SedaProducer(SedaEndpoint, WaitForTaskToComplete, long, boolean) the other constructor}.      */
annotation|@
name|Deprecated
DECL|method|SedaProducer (SedaEndpoint endpoint, BlockingQueue<Exchange> queue, WaitForTaskToComplete waitForTaskToComplete, long timeout)
specifier|public
name|SedaProducer
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|waitForTaskToComplete
argument_list|,
name|timeout
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated Use {@link #SedaProducer(SedaEndpoint, WaitForTaskToComplete, long, boolean) the other constructor}.      */
annotation|@
name|Deprecated
DECL|method|SedaProducer (SedaEndpoint endpoint, BlockingQueue<Exchange> queue, WaitForTaskToComplete waitForTaskToComplete, long timeout, boolean blockWhenFull)
specifier|public
name|SedaProducer
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
parameter_list|,
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|,
name|long
name|timeout
parameter_list|,
name|boolean
name|blockWhenFull
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|waitForTaskToComplete
argument_list|,
name|timeout
argument_list|,
name|blockWhenFull
argument_list|)
expr_stmt|;
block|}
DECL|method|SedaProducer (SedaEndpoint endpoint, WaitForTaskToComplete waitForTaskToComplete, long timeout, boolean blockWhenFull)
specifier|public
name|SedaProducer
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|,
name|long
name|timeout
parameter_list|,
name|boolean
name|blockWhenFull
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|endpoint
operator|.
name|getQueue
argument_list|()
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|waitForTaskToComplete
operator|=
name|waitForTaskToComplete
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
name|this
operator|.
name|blockWhenFull
operator|=
name|blockWhenFull
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|WaitForTaskToComplete
name|wait
init|=
name|waitForTaskToComplete
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ASYNC_WAIT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|wait
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ASYNC_WAIT
argument_list|,
name|WaitForTaskToComplete
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wait
operator|==
name|WaitForTaskToComplete
operator|.
name|Always
operator|||
operator|(
name|wait
operator|==
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
operator|&&
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
operator|)
condition|)
block|{
comment|// do not handover the completion as we wait for the copy to complete, and copy its result back when it done
name|Exchange
name|copy
init|=
name|prepareCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// latch that waits until we are complete
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// we should wait for the reply so install a on completion so we know when its complete
name|copy
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|SynchronizationAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|response
parameter_list|)
block|{
comment|// check for timeout, which then already would have invoked the latch
if|if
condition|(
name|latch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{}. Timeout occurred so response will be ignored: {}"
argument_list|,
name|this
argument_list|,
name|response
operator|.
name|hasOut
argument_list|()
condition|?
name|response
operator|.
name|getOut
argument_list|()
else|:
name|response
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} with response: {}"
argument_list|,
name|this
argument_list|,
name|response
operator|.
name|hasOut
argument_list|()
condition|?
name|response
operator|.
name|getOut
argument_list|()
else|:
name|response
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// always ensure latch is triggered
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|allowHandover
parameter_list|()
block|{
comment|// do not allow handover as we want to seda producer to have its completion triggered
comment|// at this point in the routing (at this leg), instead of at the very last (this ensure timeout is honored)
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"onDone at endpoint: "
operator|+
name|endpoint
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Adding Exchange to queue: {}"
argument_list|,
name|copy
argument_list|)
expr_stmt|;
try|try
block|{
comment|// do not copy as we already did the copy
name|addToQueue
argument_list|(
name|copy
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SedaConsumerNotAvailableException
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
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for task to complete using timeout (ms): {} at [{}]"
argument_list|,
name|timeout
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// lets see if we can get the task done before the timeout
name|boolean
name|done
init|=
literal|false
decl_stmt|;
try|try
block|{
name|done
operator|=
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
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
if|if
condition|(
operator|!
name|done
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|timeout
argument_list|)
argument_list|)
expr_stmt|;
comment|// remove timed out Exchange from queue
name|endpoint
operator|.
name|getQueue
argument_list|()
operator|.
name|remove
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// count down to indicate timeout
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Waiting for task to complete (blocking) at [{}]"
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// no timeout then wait until its done
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
else|else
block|{
comment|// no wait, eg its a InOnly then just add to queue and return
try|try
block|{
name|addToQueue
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SedaConsumerNotAvailableException
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
block|}
comment|// we use OnCompletion on the Exchange to callback and wait for the Exchange to be done
comment|// so we should just signal the callback we are done synchronously
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
DECL|method|prepareCopy (Exchange exchange, boolean handover)
specifier|protected
name|Exchange
name|prepareCopy
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|handover
parameter_list|)
block|{
comment|// use a new copy of the exchange to route async (and use same message id)
comment|// if handover we need to do special handover to avoid handing over
comment|// RestBindingMarshalOnCompletion as it should not be handed over with SEDA
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
name|handover
argument_list|,
literal|true
argument_list|,
name|synchronization
lambda|->
operator|!
name|synchronization
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"RestBindingMarshalOnCompletion"
argument_list|)
argument_list|)
decl_stmt|;
comment|// set a new from endpoint to be the seda queue
name|copy
operator|.
name|setFromEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|copy
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
name|endpoint
operator|.
name|onStarted
argument_list|(
name|this
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
name|endpoint
operator|.
name|onStopped
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
comment|/**      * Strategy method for adding the exchange to the queue.      *<p>      * Will perform a blocking "put" if blockWhenFull is true, otherwise it will      * simply add which will throw exception if the queue is full      *       * @param exchange the exchange to add to the queue      * @param copy     whether to create a copy of the exchange to use for adding to the queue      */
DECL|method|addToQueue (Exchange exchange, boolean copy)
specifier|protected
name|void
name|addToQueue
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|copy
parameter_list|)
throws|throws
name|SedaConsumerNotAvailableException
block|{
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
literal|null
decl_stmt|;
name|QueueReference
name|queueReference
init|=
name|endpoint
operator|.
name|getQueueReference
argument_list|()
decl_stmt|;
if|if
condition|(
name|queueReference
operator|!=
literal|null
condition|)
block|{
name|queue
operator|=
name|queueReference
operator|.
name|getQueue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|queue
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|SedaConsumerNotAvailableException
argument_list|(
literal|"No queue available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|boolean
name|empty
init|=
operator|!
name|queueReference
operator|.
name|hasConsumers
argument_list|()
decl_stmt|;
if|if
condition|(
name|empty
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|isFailIfNoConsumers
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SedaConsumerNotAvailableException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|endpoint
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|isDiscardIfNoConsumers
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Discard message as no active consumers on endpoint: "
operator|+
name|endpoint
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|Exchange
name|target
init|=
name|exchange
decl_stmt|;
comment|// handover the completion so its the copy which performs that, as we do not wait
if|if
condition|(
name|copy
condition|)
block|{
name|target
operator|=
name|prepareCopy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Adding Exchange to queue: {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|blockWhenFull
condition|)
block|{
try|try
block|{
name|queue
operator|.
name|put
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
name|log
operator|.
name|debug
argument_list|(
literal|"Put interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|queue
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

