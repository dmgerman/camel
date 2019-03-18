begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
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
name|LinkedBlockingQueue
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
name|Endpoint
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
name|IsSingleton
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
name|PollingConsumerPollingStrategy
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
name|spi
operator|.
name|ExceptionHandler
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

begin_comment
comment|/**  * A default implementation of the {@link org.apache.camel.PollingConsumer} which uses the normal  * asynchronous consumer mechanism along with a {@link BlockingQueue} to allow  * the caller to pull messages on demand.  */
end_comment

begin_class
DECL|class|EventDrivenPollingConsumer
specifier|public
class|class
name|EventDrivenPollingConsumer
extends|extends
name|PollingConsumerSupport
implements|implements
name|Processor
implements|,
name|IsSingleton
block|{
DECL|field|queue
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|field|interruptedExceptionHandler
specifier|private
name|ExceptionHandler
name|interruptedExceptionHandler
decl_stmt|;
DECL|field|consumer
specifier|private
name|Consumer
name|consumer
decl_stmt|;
DECL|field|blockWhenFull
specifier|private
name|boolean
name|blockWhenFull
init|=
literal|true
decl_stmt|;
DECL|field|blockTimeout
specifier|private
name|long
name|blockTimeout
decl_stmt|;
DECL|field|queueCapacity
specifier|private
specifier|final
name|int
name|queueCapacity
decl_stmt|;
DECL|method|EventDrivenPollingConsumer (Endpoint endpoint)
specifier|public
name|EventDrivenPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
block|}
DECL|method|EventDrivenPollingConsumer (Endpoint endpoint, int queueSize)
specifier|public
name|EventDrivenPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|queueSize
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|queueCapacity
operator|=
name|queueSize
expr_stmt|;
if|if
condition|(
name|queueSize
operator|<=
literal|0
condition|)
block|{
name|this
operator|.
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|queue
operator|=
operator|new
name|ArrayBlockingQueue
argument_list|<>
argument_list|(
name|queueSize
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|interruptedExceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|EventDrivenPollingConsumer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|EventDrivenPollingConsumer (Endpoint endpoint, BlockingQueue<Exchange> queue)
specifier|public
name|EventDrivenPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
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
name|queue
expr_stmt|;
name|this
operator|.
name|queueCapacity
operator|=
name|queue
operator|.
name|remainingCapacity
argument_list|()
expr_stmt|;
name|this
operator|.
name|interruptedExceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|EventDrivenPollingConsumer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|this
return|;
block|}
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
DECL|method|getBlockTimeout ()
specifier|public
name|long
name|getBlockTimeout
parameter_list|()
block|{
return|return
name|blockTimeout
return|;
block|}
DECL|method|setBlockTimeout (long blockTimeout)
specifier|public
name|void
name|setBlockTimeout
parameter_list|(
name|long
name|blockTimeout
parameter_list|)
block|{
name|this
operator|.
name|blockTimeout
operator|=
name|blockTimeout
expr_stmt|;
block|}
comment|/**      * Gets the queue capacity.      */
DECL|method|getQueueCapacity ()
specifier|public
name|int
name|getQueueCapacity
parameter_list|()
block|{
return|return
name|queueCapacity
return|;
block|}
comment|/**      * Gets the current queue size (no of elements in the queue).      */
DECL|method|getQueueSize ()
specifier|public
name|int
name|getQueueSize
parameter_list|()
block|{
return|return
name|queue
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|receiveNoWait ()
specifier|public
name|Exchange
name|receiveNoWait
parameter_list|()
block|{
return|return
name|receive
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|receive ()
specifier|public
name|Exchange
name|receive
parameter_list|()
block|{
comment|// must be started
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
operator|||
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
name|this
operator|+
literal|" is not started, but in state: "
operator|+
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
comment|// synchronizing the ordering of beforePoll, poll and afterPoll as an atomic activity
synchronized|synchronized
init|(
name|this
init|)
block|{
try|try
block|{
name|beforePoll
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// take will block waiting for message
return|return
name|queue
operator|.
name|take
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|handleInterruptedException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|afterPoll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Consumer is not running, so returning null"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|receive (long timeout)
specifier|public
name|Exchange
name|receive
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
comment|// must be started
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
operator|||
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
name|this
operator|+
literal|" is not started, but in state: "
operator|+
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
throw|;
block|}
comment|// synchronizing the ordering of beforePoll, poll and afterPoll as an atomic activity
synchronized|synchronized
init|(
name|this
init|)
block|{
try|try
block|{
comment|// use the timeout value returned from beforePoll
name|timeout
operator|=
name|beforePoll
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|queue
operator|.
name|poll
argument_list|(
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|handleInterruptedException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
finally|finally
block|{
name|afterPoll
argument_list|()
expr_stmt|;
block|}
block|}
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
name|isBlockWhenFull
argument_list|()
condition|)
block|{
try|try
block|{
if|if
condition|(
name|getBlockTimeout
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|queue
operator|.
name|put
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|added
init|=
name|queue
operator|.
name|offer
argument_list|(
name|exchange
argument_list|,
name|getBlockTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|added
condition|)
block|{
throw|throw
operator|new
name|ExchangeTimedOutException
argument_list|(
name|exchange
argument_list|,
name|getBlockTimeout
argument_list|()
argument_list|)
throw|;
block|}
block|}
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
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getInterruptedExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getInterruptedExceptionHandler
parameter_list|()
block|{
return|return
name|interruptedExceptionHandler
return|;
block|}
DECL|method|setInterruptedExceptionHandler (ExceptionHandler interruptedExceptionHandler)
specifier|public
name|void
name|setInterruptedExceptionHandler
parameter_list|(
name|ExceptionHandler
name|interruptedExceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|interruptedExceptionHandler
operator|=
name|interruptedExceptionHandler
expr_stmt|;
block|}
DECL|method|getDelegateConsumer ()
specifier|public
name|Consumer
name|getDelegateConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|handleInterruptedException (InterruptedException e)
specifier|protected
name|void
name|handleInterruptedException
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|getInterruptedExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
DECL|method|beforePoll (long timeout)
specifier|protected
name|long
name|beforePoll
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
if|if
condition|(
name|consumer
operator|instanceof
name|PollingConsumerPollingStrategy
condition|)
block|{
name|PollingConsumerPollingStrategy
name|strategy
init|=
operator|(
name|PollingConsumerPollingStrategy
operator|)
name|consumer
decl_stmt|;
try|try
block|{
name|timeout
operator|=
name|strategy
operator|.
name|beforePoll
argument_list|(
name|timeout
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
name|debug
argument_list|(
literal|"Error occurred before polling "
operator|+
name|consumer
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|timeout
return|;
block|}
DECL|method|afterPoll ()
specifier|protected
name|void
name|afterPoll
parameter_list|()
block|{
if|if
condition|(
name|consumer
operator|instanceof
name|PollingConsumerPollingStrategy
condition|)
block|{
name|PollingConsumerPollingStrategy
name|strategy
init|=
operator|(
name|PollingConsumerPollingStrategy
operator|)
name|consumer
decl_stmt|;
try|try
block|{
name|strategy
operator|.
name|afterPoll
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
name|debug
argument_list|(
literal|"Error occurred after polling "
operator|+
name|consumer
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getConsumer ()
specifier|protected
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|createConsumer ()
specifier|protected
name|Consumer
name|createConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|createConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// lets add ourselves as a consumer
name|consumer
operator|=
name|createConsumer
argument_list|()
expr_stmt|;
comment|// if the consumer has a polling strategy then invoke that
if|if
condition|(
name|consumer
operator|instanceof
name|PollingConsumerPollingStrategy
condition|)
block|{
name|PollingConsumerPollingStrategy
name|strategy
init|=
operator|(
name|PollingConsumerPollingStrategy
operator|)
name|consumer
decl_stmt|;
name|strategy
operator|.
name|onInit
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
comment|// As the consumer could take the messages at once, so we cannot release the consumer
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
block|}
end_class

end_unit

