begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ScheduledExecutorService
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|CamelContext
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
name|ShutdownRunningTask
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
name|ShutdownAware
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A useful base class for any processor which provides some kind of throttling  * or delayed processing.  *<p/>  * This implementation will block while waiting.  */
end_comment

begin_class
DECL|class|DelayProcessorSupport
specifier|public
specifier|abstract
class|class
name|DelayProcessorSupport
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|ShutdownAware
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|final
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
specifier|final
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|field|asyncDelayed
specifier|private
name|boolean
name|asyncDelayed
init|=
literal|true
decl_stmt|;
DECL|field|callerRunsWhenRejected
specifier|private
name|boolean
name|callerRunsWhenRejected
init|=
literal|true
decl_stmt|;
DECL|field|delayedCount
specifier|private
specifier|final
name|AtomicInteger
name|delayedCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// TODO: Add option to cancel tasks on shutdown so we can stop fast
DECL|class|ProcessCall
specifier|private
specifier|final
class|class
name|ProcessCall
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|method|ProcessCall (Exchange exchange, AsyncCallback callback)
name|ProcessCall
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// we are running now so decrement the counter
name|delayedCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Delayed task woke up and continues routing for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Run is not allowed"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange now that we woke up
name|DelayProcessorSupport
operator|.
name|this
operator|.
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Delayed task done for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// we must done the callback from this async callback as well, to ensure callback is done correctly
comment|// must invoke done on callback with false, as that is what the original caller would
comment|// expect as we returned false in the process method
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|DelayProcessorSupport (CamelContext camelContext, Processor processor)
specifier|public
name|DelayProcessorSupport
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|DelayProcessorSupport (CamelContext camelContext, Processor processor, ScheduledExecutorService executorService, boolean shutdownExecutorService)
specifier|public
name|DelayProcessorSupport
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ScheduledExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|shutdownExecutorService
operator|=
name|shutdownExecutorService
expr_stmt|;
block|}
DECL|method|processDelay (Exchange exchange, AsyncCallback callback, long delay)
specifier|protected
name|boolean
name|processDelay
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|long
name|delay
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isAsyncDelayed
argument_list|()
operator|||
name|exchange
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
comment|// use synchronous delay (also required if using transactions)
try|try
block|{
name|delay
argument_list|(
name|delay
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// then continue routing
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// exception occurred so we are done
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
else|else
block|{
comment|// asynchronous delay so schedule a process call task
comment|// and increment the counter (we decrement the counter when we run the ProcessCall)
name|delayedCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|ProcessCall
name|call
init|=
operator|new
name|ProcessCall
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Scheduling delayed task to run in {} millis for exchangeId: {}"
argument_list|,
name|delay
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|schedule
argument_list|(
name|call
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
comment|// tell Camel routing engine we continue routing asynchronous
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
comment|// we were not allowed to run the ProcessCall, so need to decrement the counter here
name|delayedCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
name|isCallerRunsWhenRejected
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Scheduling rejected task, so letting caller run, delaying at first for {} millis for exchangeId: {}"
argument_list|,
name|delay
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// let caller run by processing
try|try
block|{
name|delay
argument_list|(
name|delay
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ie
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ie
argument_list|)
expr_stmt|;
block|}
comment|// then continue routing
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// caller don't run the task so we are done
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
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Run is not allowed"
argument_list|)
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
comment|// calculate delay and wait
name|long
name|delay
decl_stmt|;
try|try
block|{
name|delay
operator|=
name|calculateDelay
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|delay
operator|<=
literal|0
condition|)
block|{
comment|// no delay then continue routing
name|log
operator|.
name|trace
argument_list|(
literal|"No delay for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
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
return|return
name|processDelay
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|delay
argument_list|)
return|;
block|}
DECL|method|isAsyncDelayed ()
specifier|public
name|boolean
name|isAsyncDelayed
parameter_list|()
block|{
return|return
name|asyncDelayed
return|;
block|}
DECL|method|setAsyncDelayed (boolean asyncDelayed)
specifier|public
name|void
name|setAsyncDelayed
parameter_list|(
name|boolean
name|asyncDelayed
parameter_list|)
block|{
name|this
operator|.
name|asyncDelayed
operator|=
name|asyncDelayed
expr_stmt|;
block|}
DECL|method|isCallerRunsWhenRejected ()
specifier|public
name|boolean
name|isCallerRunsWhenRejected
parameter_list|()
block|{
return|return
name|callerRunsWhenRejected
return|;
block|}
DECL|method|setCallerRunsWhenRejected (boolean callerRunsWhenRejected)
specifier|public
name|void
name|setCallerRunsWhenRejected
parameter_list|(
name|boolean
name|callerRunsWhenRejected
parameter_list|)
block|{
name|this
operator|.
name|callerRunsWhenRejected
operator|=
name|callerRunsWhenRejected
expr_stmt|;
block|}
DECL|method|calculateDelay (Exchange exchange)
specifier|protected
specifier|abstract
name|long
name|calculateDelay
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Gets the current number of {@link Exchange}s being delayed (hold back due throttle limit hit)      */
DECL|method|getDelayedCount ()
specifier|public
name|int
name|getDelayedCount
parameter_list|()
block|{
return|return
name|delayedCount
operator|.
name|get
argument_list|()
return|;
block|}
comment|/**      * Delays the given time before continuing.      *<p/>      * This implementation will block while waiting      *       * @param delay the delay time in millis      * @param exchange the exchange being processed      */
DECL|method|delay (long delay, Exchange exchange)
specifier|protected
name|void
name|delay
parameter_list|(
name|long
name|delay
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// only run is we are started
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|delay
operator|<
literal|0
condition|)
block|{
return|return;
block|}
else|else
block|{
try|try
block|{
comment|// keep track on delayer counter while we sleep
name|delayedCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|handleSleepInterruptedException
argument_list|(
name|e
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|delayedCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Called when a sleep is interrupted; allows derived classes to handle this case differently      */
DECL|method|handleSleepInterruptedException (InterruptedException e, Exchange exchange)
specifier|protected
name|void
name|handleSleepInterruptedException
parameter_list|(
name|InterruptedException
name|e
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sleep interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
DECL|method|currentSystemTime ()
specifier|protected
name|long
name|currentSystemTime
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
return|;
block|}
DECL|method|sleep (long delay)
specifier|private
name|void
name|sleep
parameter_list|(
name|long
name|delay
parameter_list|)
throws|throws
name|InterruptedException
block|{
if|if
condition|(
name|delay
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Sleeping for: {} millis"
argument_list|,
name|delay
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
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
if|if
condition|(
name|isAsyncDelayed
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|asyncDelayed
operator|=
literal|true
expr_stmt|;
block|}
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
if|if
condition|(
name|shutdownExecutorService
operator|&&
name|executorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
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
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
return|return
name|getDelayedCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean suspendOnly, boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|suspendOnly
parameter_list|,
name|boolean
name|forced
parameter_list|)
block|{      }
block|}
end_class

end_unit

