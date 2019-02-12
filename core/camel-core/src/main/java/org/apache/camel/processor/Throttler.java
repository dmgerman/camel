begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DelayQueue
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
name|Delayed
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
name|ScheduledFuture
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
name|AtomicReference
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
name|Expression
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
name|RuntimeExchangeException
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
name|Traceable
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
name|IdAware
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
name|AsyncProcessorSupport
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
comment|/**  * A<a href="http://camel.apache.org/throttler.html">Throttler</a>  * will set a limit on the maximum number of message exchanges which can be sent  * to a processor within a specific time period.<p/> This pattern can be  * extremely useful if you have some external system which meters access; such  * as only allowing 100 requests per second; or if huge load can cause a  * particular system to malfunction or to reduce its throughput you might want  * to introduce some throttling.  *  * This throttle implementation is thread-safe and is therefore safe to be used  * by multiple concurrent threads in a single route.  *  * The throttling mechanism is a DelayQueue with maxRequestsPerPeriod permits on  * it. Each permit is set to be delayed by timePeriodMillis (except when the  * throttler is initialized or the throttle rate increased, then there is no delay  * for those permits). Callers trying to acquire a permit from the DelayQueue will  * block if necessary. The end result is a rolling window of time. Where from the  * callers point of view in the last timePeriodMillis no more than  * maxRequestsPerPeriod have been allowed to be acquired.  */
end_comment

begin_class
DECL|class|Throttler
specifier|public
class|class
name|Throttler
extends|extends
name|AsyncProcessorSupport
implements|implements
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|DEFAULT_KEY
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_KEY
init|=
literal|"CamelThrottlerDefaultKey"
decl_stmt|;
DECL|field|PROPERTY_EXCHANGE_QUEUED_TIMESTAMP
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_EXCHANGE_QUEUED_TIMESTAMP
init|=
literal|"CamelThrottlerExchangeQueuedTimestamp"
decl_stmt|;
DECL|field|PROPERTY_EXCHANGE_STATE
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_EXCHANGE_STATE
init|=
literal|"CamelThrottlerExchangeState"
decl_stmt|;
DECL|enum|State
DECL|enumConstant|SYNC
DECL|enumConstant|ASYNC
DECL|enumConstant|ASYNC_REJECTED
specifier|private
enum|enum
name|State
block|{
name|SYNC
block|,
name|ASYNC
block|,
name|ASYNC_REJECTED
block|}
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|asyncExecutor
specifier|private
specifier|final
name|ScheduledExecutorService
name|asyncExecutor
decl_stmt|;
DECL|field|shutdownAsyncExecutor
specifier|private
specifier|final
name|boolean
name|shutdownAsyncExecutor
decl_stmt|;
DECL|field|timePeriodMillis
specifier|private
specifier|volatile
name|long
name|timePeriodMillis
decl_stmt|;
DECL|field|cleanPeriodMillis
specifier|private
specifier|volatile
name|long
name|cleanPeriodMillis
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|maxRequestsPerPeriodExpression
specifier|private
name|Expression
name|maxRequestsPerPeriodExpression
decl_stmt|;
DECL|field|rejectExecution
specifier|private
name|boolean
name|rejectExecution
decl_stmt|;
DECL|field|asyncDelayed
specifier|private
name|boolean
name|asyncDelayed
decl_stmt|;
DECL|field|callerRunsWhenRejected
specifier|private
name|boolean
name|callerRunsWhenRejected
init|=
literal|true
decl_stmt|;
DECL|field|correlationExpression
specifier|private
name|Expression
name|correlationExpression
decl_stmt|;
DECL|field|states
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ThrottlingState
argument_list|>
name|states
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|Throttler (final CamelContext camelContext, final Expression maxRequestsPerPeriodExpression, final long timePeriodMillis, final ScheduledExecutorService asyncExecutor, final boolean shutdownAsyncExecutor, final boolean rejectExecution, Expression correlation)
specifier|public
name|Throttler
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Expression
name|maxRequestsPerPeriodExpression
parameter_list|,
specifier|final
name|long
name|timePeriodMillis
parameter_list|,
specifier|final
name|ScheduledExecutorService
name|asyncExecutor
parameter_list|,
specifier|final
name|boolean
name|shutdownAsyncExecutor
parameter_list|,
specifier|final
name|boolean
name|rejectExecution
parameter_list|,
name|Expression
name|correlation
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|rejectExecution
operator|=
name|rejectExecution
expr_stmt|;
name|this
operator|.
name|shutdownAsyncExecutor
operator|=
name|shutdownAsyncExecutor
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|maxRequestsPerPeriodExpression
argument_list|,
literal|"maxRequestsPerPeriodExpression"
argument_list|)
expr_stmt|;
name|this
operator|.
name|maxRequestsPerPeriodExpression
operator|=
name|maxRequestsPerPeriodExpression
expr_stmt|;
if|if
condition|(
name|timePeriodMillis
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"TimePeriodMillis should be a positive number, was: "
operator|+
name|timePeriodMillis
argument_list|)
throw|;
block|}
name|this
operator|.
name|timePeriodMillis
operator|=
name|timePeriodMillis
expr_stmt|;
name|this
operator|.
name|cleanPeriodMillis
operator|=
name|timePeriodMillis
operator|*
literal|10
expr_stmt|;
name|this
operator|.
name|asyncExecutor
operator|=
name|asyncExecutor
expr_stmt|;
name|this
operator|.
name|correlationExpression
operator|=
name|correlation
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
name|long
name|queuedStart
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|queuedStart
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_EXCHANGE_QUEUED_TIMESTAMP
argument_list|,
literal|0L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|PROPERTY_EXCHANGE_QUEUED_TIMESTAMP
argument_list|)
expr_stmt|;
block|}
name|State
name|state
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|PROPERTY_EXCHANGE_STATE
argument_list|,
name|State
operator|.
name|SYNC
argument_list|,
name|State
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|removeProperty
argument_list|(
name|PROPERTY_EXCHANGE_STATE
argument_list|)
expr_stmt|;
name|boolean
name|doneSync
init|=
name|state
operator|==
name|State
operator|.
name|SYNC
operator|||
name|state
operator|==
name|State
operator|.
name|ASYNC_REJECTED
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Run is not allowed"
argument_list|)
throw|;
block|}
name|String
name|key
init|=
name|DEFAULT_KEY
decl_stmt|;
if|if
condition|(
name|correlationExpression
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|correlationExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|ThrottlingState
name|throttlingState
init|=
name|states
operator|.
name|computeIfAbsent
argument_list|(
name|key
argument_list|,
name|ThrottlingState
operator|::
operator|new
argument_list|)
decl_stmt|;
name|throttlingState
operator|.
name|calculateAndSetMaxRequestsPerPeriod
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ThrottlePermit
name|permit
init|=
name|throttlingState
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|permit
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isRejectExecution
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ThrottlerRejectedExecutionException
argument_list|(
literal|"Exceeded the max throttle rate of "
operator|+
name|throttlingState
operator|.
name|getThrottleRate
argument_list|()
operator|+
literal|" within "
operator|+
name|timePeriodMillis
operator|+
literal|"ms"
argument_list|)
throw|;
block|}
else|else
block|{
comment|// delegate to async pool
if|if
condition|(
name|isAsyncDelayed
argument_list|()
operator|&&
operator|!
name|exchange
operator|.
name|isTransacted
argument_list|()
operator|&&
name|state
operator|==
name|State
operator|.
name|SYNC
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Throttle rate exceeded but AsyncDelayed enabled, so queueing for async processing, exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|processAsynchronously
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|throttlingState
argument_list|)
return|;
block|}
comment|// block waiting for a permit
name|long
name|start
init|=
literal|0
decl_stmt|;
name|long
name|elapsed
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
name|permit
operator|=
name|throttlingState
operator|.
name|take
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|elapsed
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
expr_stmt|;
block|}
name|throttlingState
operator|.
name|enqueue
argument_list|(
name|permit
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|ASYNC
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
name|long
name|queuedTime
init|=
name|start
operator|-
name|queuedStart
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Queued for {}ms, Throttled for {}ms, exchangeId: {}"
argument_list|,
name|queuedTime
argument_list|,
name|elapsed
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Throttled for {}ms, exchangeId: {}"
argument_list|,
name|elapsed
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|throttlingState
operator|.
name|enqueue
argument_list|(
name|permit
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|state
operator|==
name|State
operator|.
name|ASYNC
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
name|long
name|queuedTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|queuedStart
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Queued for {}ms, No throttling applied (throttle cleared while queued), for exchangeId: {}"
argument_list|,
name|queuedTime
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"No throttling applied to exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
return|return
name|doneSync
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// determine if we can still run, or the camel context is forcing a shutdown
name|boolean
name|forceShutdown
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|forceShutdown
argument_list|(
name|this
argument_list|)
decl_stmt|;
if|if
condition|(
name|forceShutdown
condition|)
block|{
name|String
name|msg
init|=
literal|"Run not allowed as ShutdownStrategy is forcing shutting down, will reject executing exchange: "
operator|+
name|exchange
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
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
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
return|return
name|doneSync
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|t
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
return|return
name|doneSync
return|;
block|}
block|}
comment|/**      * Delegate blocking on the DelayQueue to an asyncExecutor. Except if the executor rejects the submission      * and isCallerRunsWhenRejected() is enabled, then this method will delegate back to process(), but not      * before changing the exchange state to stop any recursion.      */
DECL|method|processAsynchronously (final Exchange exchange, final AsyncCallback callback, ThrottlingState throttlingState)
specifier|protected
name|boolean
name|processAsynchronously
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
name|ThrottlingState
name|throttlingState
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|PROPERTY_EXCHANGE_QUEUED_TIMESTAMP
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|PROPERTY_EXCHANGE_STATE
argument_list|,
name|State
operator|.
name|ASYNC
argument_list|)
expr_stmt|;
name|long
name|delay
init|=
name|throttlingState
operator|.
name|peek
argument_list|()
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
decl_stmt|;
name|asyncExecutor
operator|.
name|schedule
argument_list|(
parameter_list|()
lambda|->
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
argument_list|,
name|delay
argument_list|,
name|TimeUnit
operator|.
name|NANOSECONDS
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
specifier|final
name|RejectedExecutionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|isCallerRunsWhenRejected
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"AsyncExecutor is full, rejected exchange will run in the current thread, exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|PROPERTY_EXCHANGE_STATE
argument_list|,
name|State
operator|.
name|ASYNC_REJECTED
argument_list|)
expr_stmt|;
return|return
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
throw|throw
name|e
throw|;
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
name|isAsyncDelayed
argument_list|()
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|asyncExecutor
argument_list|,
literal|"executorService"
argument_list|,
name|this
argument_list|)
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
block|{     }
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
name|shutdownAsyncExecutor
operator|&&
name|asyncExecutor
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
name|asyncExecutor
argument_list|)
expr_stmt|;
block|}
name|states
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
DECL|class|ThrottlingState
specifier|private
class|class
name|ThrottlingState
block|{
DECL|field|key
specifier|private
specifier|final
name|String
name|key
decl_stmt|;
DECL|field|delayQueue
specifier|private
specifier|final
name|DelayQueue
argument_list|<
name|ThrottlePermit
argument_list|>
name|delayQueue
init|=
operator|new
name|DelayQueue
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|cleanFuture
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|cleanFuture
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|throttleRate
specifier|private
specifier|volatile
name|int
name|throttleRate
decl_stmt|;
DECL|method|ThrottlingState (String key)
name|ThrottlingState
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|String
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
DECL|method|getThrottleRate ()
specifier|public
name|int
name|getThrottleRate
parameter_list|()
block|{
return|return
name|throttleRate
return|;
block|}
DECL|method|poll ()
specifier|public
name|ThrottlePermit
name|poll
parameter_list|()
block|{
return|return
name|delayQueue
operator|.
name|poll
argument_list|()
return|;
block|}
DECL|method|peek ()
specifier|public
name|ThrottlePermit
name|peek
parameter_list|()
block|{
return|return
name|delayQueue
operator|.
name|peek
argument_list|()
return|;
block|}
DECL|method|take ()
specifier|public
name|ThrottlePermit
name|take
parameter_list|()
throws|throws
name|InterruptedException
block|{
return|return
name|delayQueue
operator|.
name|take
argument_list|()
return|;
block|}
DECL|method|clean ()
specifier|public
name|void
name|clean
parameter_list|()
block|{
name|states
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|/**          * Returns a permit to the DelayQueue, first resetting it's delay to be relative to now.          */
DECL|method|enqueue (final ThrottlePermit permit, final Exchange exchange)
specifier|public
name|void
name|enqueue
parameter_list|(
specifier|final
name|ThrottlePermit
name|permit
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|permit
operator|.
name|setDelayMs
argument_list|(
name|getTimePeriodMillis
argument_list|()
argument_list|)
expr_stmt|;
name|delayQueue
operator|.
name|put
argument_list|(
name|permit
argument_list|)
expr_stmt|;
try|try
block|{
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|next
init|=
name|asyncExecutor
operator|.
name|schedule
argument_list|(
name|this
operator|::
name|clean
argument_list|,
name|cleanPeriodMillis
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|prev
init|=
name|cleanFuture
operator|.
name|getAndSet
argument_list|(
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
name|prev
operator|!=
literal|null
condition|)
block|{
name|prev
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|// try and incur the least amount of overhead while releasing permits back to the queue
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
literal|"Permit released, for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Throttling queue cleaning rejected"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**          * Evaluates the maxRequestsPerPeriodExpression and adjusts the throttle rate up or down.          */
DECL|method|calculateAndSetMaxRequestsPerPeriod (final Exchange exchange)
specifier|public
specifier|synchronized
name|void
name|calculateAndSetMaxRequestsPerPeriod
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Integer
name|newThrottle
init|=
name|maxRequestsPerPeriodExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|newThrottle
operator|!=
literal|null
operator|&&
name|newThrottle
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The maximumRequestsPerPeriod must be a positive number, was: "
operator|+
name|newThrottle
argument_list|)
throw|;
block|}
if|if
condition|(
name|newThrottle
operator|==
literal|null
operator|&&
name|throttleRate
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"The maxRequestsPerPeriodExpression was evaluated as null: "
operator|+
name|maxRequestsPerPeriodExpression
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
if|if
condition|(
name|newThrottle
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|newThrottle
operator|!=
name|throttleRate
condition|)
block|{
comment|// decrease
if|if
condition|(
name|throttleRate
operator|>
name|newThrottle
condition|)
block|{
name|int
name|delta
init|=
name|throttleRate
operator|-
name|newThrottle
decl_stmt|;
comment|// discard any permits that are needed to decrease throttling
while|while
condition|(
name|delta
operator|>
literal|0
condition|)
block|{
name|delayQueue
operator|.
name|take
argument_list|()
expr_stmt|;
name|delta
operator|--
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Permit discarded due to throttling rate decrease, triggered by ExchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Throttle rate decreased from {} to {}, triggered by ExchangeId: {}"
argument_list|,
name|throttleRate
argument_list|,
name|newThrottle
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// increase
block|}
elseif|else
if|if
condition|(
name|newThrottle
operator|>
name|throttleRate
condition|)
block|{
name|int
name|delta
init|=
name|newThrottle
operator|-
name|throttleRate
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|delta
condition|;
name|i
operator|++
control|)
block|{
name|delayQueue
operator|.
name|put
argument_list|(
operator|new
name|ThrottlePermit
argument_list|(
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|throttleRate
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Initial throttle rate set to {}, triggered by ExchangeId: {}"
argument_list|,
name|newThrottle
argument_list|,
name|exchange
operator|.
name|getExchangeId
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
literal|"Throttle rate increase from {} to {}, triggered by ExchangeId: {}"
argument_list|,
name|throttleRate
argument_list|,
name|newThrottle
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|throttleRate
operator|=
name|newThrottle
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Permit that implements the Delayed interface needed by DelayQueue.      */
DECL|class|ThrottlePermit
specifier|private
class|class
name|ThrottlePermit
implements|implements
name|Delayed
block|{
DECL|field|scheduledTime
specifier|private
specifier|volatile
name|long
name|scheduledTime
decl_stmt|;
DECL|method|ThrottlePermit (final long delayMs)
name|ThrottlePermit
parameter_list|(
specifier|final
name|long
name|delayMs
parameter_list|)
block|{
name|setDelayMs
argument_list|(
name|delayMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setDelayMs (final long delayMs)
specifier|public
name|void
name|setDelayMs
parameter_list|(
specifier|final
name|long
name|delayMs
parameter_list|)
block|{
name|this
operator|.
name|scheduledTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|delayMs
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDelay (final TimeUnit unit)
specifier|public
name|long
name|getDelay
parameter_list|(
specifier|final
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|unit
operator|.
name|convert
argument_list|(
name|scheduledTime
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|compareTo (final Delayed o)
specifier|public
name|int
name|compareTo
parameter_list|(
specifier|final
name|Delayed
name|o
parameter_list|)
block|{
return|return
call|(
name|int
call|)
argument_list|(
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|-
name|o
operator|.
name|getDelay
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|isRejectExecution ()
specifier|public
name|boolean
name|isRejectExecution
parameter_list|()
block|{
return|return
name|rejectExecution
return|;
block|}
DECL|method|setRejectExecution (boolean rejectExecution)
specifier|public
name|void
name|setRejectExecution
parameter_list|(
name|boolean
name|rejectExecution
parameter_list|)
block|{
name|this
operator|.
name|rejectExecution
operator|=
name|rejectExecution
expr_stmt|;
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
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (final String id)
specifier|public
name|void
name|setId
parameter_list|(
specifier|final
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Sets the maximum number of requests per time period expression      */
DECL|method|setMaximumRequestsPerPeriodExpression (Expression maxRequestsPerPeriodExpression)
specifier|public
name|void
name|setMaximumRequestsPerPeriodExpression
parameter_list|(
name|Expression
name|maxRequestsPerPeriodExpression
parameter_list|)
block|{
name|this
operator|.
name|maxRequestsPerPeriodExpression
operator|=
name|maxRequestsPerPeriodExpression
expr_stmt|;
block|}
DECL|method|getMaximumRequestsPerPeriodExpression ()
specifier|public
name|Expression
name|getMaximumRequestsPerPeriodExpression
parameter_list|()
block|{
return|return
name|maxRequestsPerPeriodExpression
return|;
block|}
comment|/**      * Gets the current maximum request per period value.      * If it is grouped throttling applied with correlationExpression      * than the max per period within the group will return      */
DECL|method|getCurrentMaximumRequestsPerPeriod ()
specifier|public
name|int
name|getCurrentMaximumRequestsPerPeriod
parameter_list|()
block|{
return|return
name|states
operator|.
name|values
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|mapToInt
argument_list|(
name|ThrottlingState
operator|::
name|getThrottleRate
argument_list|)
operator|.
name|max
argument_list|()
operator|.
name|orElse
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Sets the time period during which the maximum number of requests apply      */
DECL|method|setTimePeriodMillis (final long timePeriodMillis)
specifier|public
name|void
name|setTimePeriodMillis
parameter_list|(
specifier|final
name|long
name|timePeriodMillis
parameter_list|)
block|{
name|this
operator|.
name|timePeriodMillis
operator|=
name|timePeriodMillis
expr_stmt|;
block|}
DECL|method|getTimePeriodMillis ()
specifier|public
name|long
name|getTimePeriodMillis
parameter_list|()
block|{
return|return
name|timePeriodMillis
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"throttle["
operator|+
name|maxRequestsPerPeriodExpression
operator|+
literal|" per: "
operator|+
name|timePeriodMillis
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Throttler[requests: "
operator|+
name|maxRequestsPerPeriodExpression
operator|+
literal|" per: "
operator|+
name|timePeriodMillis
operator|+
literal|" (ms)]"
return|;
block|}
block|}
end_class

end_unit
