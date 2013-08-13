begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|LoggingLevel
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
name|SuspendableService
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
name|PollingConsumerPollStrategy
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
name|ScheduledPollConsumerScheduler
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
name|util
operator|.
name|IntrospectionSupport
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
comment|/**  * A useful base class for any consumer which is polling based  *   * @version   */
end_comment

begin_class
DECL|class|ScheduledPollConsumer
specifier|public
specifier|abstract
class|class
name|ScheduledPollConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|Runnable
implements|,
name|SuspendableService
implements|,
name|PollingConsumerPollingStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScheduledPollConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|scheduler
specifier|private
name|ScheduledPollConsumerScheduler
name|scheduler
decl_stmt|;
DECL|field|scheduledExecutorService
specifier|private
name|ScheduledExecutorService
name|scheduledExecutorService
decl_stmt|;
comment|// if adding more options then align with ScheduledPollEndpoint#configureScheduledPollConsumerProperties
annotation|@
name|UriParam
DECL|field|startScheduler
specifier|private
name|boolean
name|startScheduler
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|initialDelay
specifier|private
name|long
name|initialDelay
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|500
decl_stmt|;
annotation|@
name|UriParam
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useFixedDelay
specifier|private
name|boolean
name|useFixedDelay
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|pollStrategy
specifier|private
name|PollingConsumerPollStrategy
name|pollStrategy
init|=
operator|new
name|DefaultPollingConsumerPollStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|runLoggingLevel
specifier|private
name|LoggingLevel
name|runLoggingLevel
init|=
name|LoggingLevel
operator|.
name|TRACE
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sendEmptyMessageWhenIdle
specifier|private
name|boolean
name|sendEmptyMessageWhenIdle
decl_stmt|;
annotation|@
name|UriParam
DECL|field|greedy
specifier|private
name|boolean
name|greedy
decl_stmt|;
DECL|field|polling
specifier|private
specifier|volatile
name|boolean
name|polling
decl_stmt|;
DECL|method|ScheduledPollConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|ScheduledPollConsumer
parameter_list|(
name|Endpoint
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
block|}
DECL|method|ScheduledPollConsumer (Endpoint endpoint, Processor processor, ScheduledExecutorService scheduledExecutorService)
specifier|public
name|ScheduledPollConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ScheduledExecutorService
name|scheduledExecutorService
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
comment|// we have been given an existing thread pool, so we should not manage its lifecycle
comment|// so we should keep shutdownExecutor as false
name|this
operator|.
name|scheduledExecutorService
operator|=
name|scheduledExecutorService
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|scheduledExecutorService
argument_list|,
literal|"scheduledExecutorService"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Invoked whenever we should be polled      */
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// avoid this thread to throw exceptions because the thread pool wont re-schedule a new thread
try|try
block|{
comment|// log starting
if|if
condition|(
name|LoggingLevel
operator|.
name|ERROR
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Scheduled task started on:   {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|WARN
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Scheduled task started on:   {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|INFO
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Scheduled task started on:   {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|DEBUG
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled task started on:   {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scheduled task started on:   {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// execute scheduled task
name|doRun
argument_list|()
expr_stmt|;
comment|// log completed
if|if
condition|(
name|LoggingLevel
operator|.
name|ERROR
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Scheduled task completed on: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|WARN
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Scheduled task completed on: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|INFO
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Scheduled task completed on: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LoggingLevel
operator|.
name|DEBUG
operator|==
name|runLoggingLevel
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduled task completed on: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Scheduled task completed on: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
comment|// must catch Error, to ensure the task is re-scheduled
name|LOG
operator|.
name|error
argument_list|(
literal|"Error occurred during running scheduled task on: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
operator|+
literal|", due: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doRun ()
specifier|private
name|void
name|doRun
parameter_list|()
block|{
if|if
condition|(
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot start to poll: {} as its suspended"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|int
name|retryCounter
init|=
operator|-
literal|1
decl_stmt|;
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
try|try
block|{
name|cause
operator|=
literal|null
expr_stmt|;
comment|// eager assume we are done
name|done
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|isPollAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|retryCounter
operator|==
operator|-
literal|1
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting to poll: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Retrying attempt {} to poll: {}"
argument_list|,
name|retryCounter
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// mark we are polling which should also include the begin/poll/commit
name|polling
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|boolean
name|begin
init|=
name|pollStrategy
operator|.
name|begin
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|begin
condition|)
block|{
name|retryCounter
operator|++
expr_stmt|;
name|int
name|polledMessages
init|=
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|polledMessages
operator|==
literal|0
operator|&&
name|isSendEmptyMessageWhenIdle
argument_list|()
condition|)
block|{
comment|// send an "empty" exchange
name|processEmptyMessage
argument_list|()
expr_stmt|;
block|}
name|pollStrategy
operator|.
name|commit
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|polledMessages
argument_list|)
expr_stmt|;
if|if
condition|(
name|polledMessages
operator|>
literal|0
operator|&&
name|isGreedy
argument_list|()
condition|)
block|{
name|done
operator|=
literal|false
expr_stmt|;
name|retryCounter
operator|=
operator|-
literal|1
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Greedy polling after processing {} messages"
argument_list|,
name|polledMessages
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot begin polling as pollStrategy returned false: {}"
argument_list|,
name|pollStrategy
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|polling
operator|=
literal|false
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Finished polling: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
name|boolean
name|retry
init|=
name|pollStrategy
operator|.
name|rollback
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|retryCounter
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|retry
condition|)
block|{
comment|// do not set cause as we retry
name|done
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|cause
operator|=
name|e
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|cause
operator|=
name|t
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|cause
operator|=
name|t
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|cause
operator|!=
literal|null
operator|&&
name|isRunAllowed
argument_list|()
condition|)
block|{
comment|// let exception handler deal with the caused exception
comment|// but suppress this during shutdown as the logs may get flooded with exceptions during shutdown/forced shutdown
try|try
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Consumer "
operator|+
name|this
operator|+
literal|" failed polling endpoint: "
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|". Will try again at next poll"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error handling exception. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|cause
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// avoid this thread to throw exceptions because the thread pool wont re-schedule a new thread
block|}
comment|/**      * No messages to poll so send an empty message instead.      *      * @throws Exception is thrown if error processing the empty message.      */
DECL|method|processEmptyMessage ()
specifier|protected
name|void
name|processEmptyMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sending empty message as there were no messages from polling: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|isPollAllowed ()
specifier|protected
name|boolean
name|isPollAllowed
parameter_list|()
block|{
return|return
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|isSuspended
argument_list|()
return|;
block|}
comment|/**      * Whether polling is currently in progress      */
DECL|method|isPolling ()
specifier|protected
name|boolean
name|isPolling
parameter_list|()
block|{
return|return
name|polling
return|;
block|}
DECL|method|getScheduler ()
specifier|public
name|ScheduledPollConsumerScheduler
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
comment|/**      * Sets a cutom scheduler to use for scheduling running this task (poll).      * @param scheduler      */
DECL|method|setScheduler (ScheduledPollConsumerScheduler scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
name|ScheduledPollConsumerScheduler
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|long
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (long initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (long delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
comment|/**      * Sets the time unit to use.      *<p/>      * Notice that both {@link #getDelay()} and {@link #getInitialDelay()} are using      * the same time unit. So if you change this value, then take into account that the      * default value of {@link #getInitialDelay()} is 1000. So you may to adjust this value accordingly.      *      * @param timeUnit the time unit.      */
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|isUseFixedDelay ()
specifier|public
name|boolean
name|isUseFixedDelay
parameter_list|()
block|{
return|return
name|useFixedDelay
return|;
block|}
DECL|method|setUseFixedDelay (boolean useFixedDelay)
specifier|public
name|void
name|setUseFixedDelay
parameter_list|(
name|boolean
name|useFixedDelay
parameter_list|)
block|{
name|this
operator|.
name|useFixedDelay
operator|=
name|useFixedDelay
expr_stmt|;
block|}
DECL|method|getRunLoggingLevel ()
specifier|public
name|LoggingLevel
name|getRunLoggingLevel
parameter_list|()
block|{
return|return
name|runLoggingLevel
return|;
block|}
DECL|method|setRunLoggingLevel (LoggingLevel runLoggingLevel)
specifier|public
name|void
name|setRunLoggingLevel
parameter_list|(
name|LoggingLevel
name|runLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|runLoggingLevel
operator|=
name|runLoggingLevel
expr_stmt|;
block|}
DECL|method|getPollStrategy ()
specifier|public
name|PollingConsumerPollStrategy
name|getPollStrategy
parameter_list|()
block|{
return|return
name|pollStrategy
return|;
block|}
DECL|method|setPollStrategy (PollingConsumerPollStrategy pollStrategy)
specifier|public
name|void
name|setPollStrategy
parameter_list|(
name|PollingConsumerPollStrategy
name|pollStrategy
parameter_list|)
block|{
name|this
operator|.
name|pollStrategy
operator|=
name|pollStrategy
expr_stmt|;
block|}
DECL|method|isStartScheduler ()
specifier|public
name|boolean
name|isStartScheduler
parameter_list|()
block|{
return|return
name|startScheduler
return|;
block|}
comment|/**      * Sets whether the scheduler should be started when this consumer starts.      *<p/>      * This option is default true.      *      * @param startScheduler whether to start scheduler      */
DECL|method|setStartScheduler (boolean startScheduler)
specifier|public
name|void
name|setStartScheduler
parameter_list|(
name|boolean
name|startScheduler
parameter_list|)
block|{
name|this
operator|.
name|startScheduler
operator|=
name|startScheduler
expr_stmt|;
block|}
DECL|method|setSendEmptyMessageWhenIdle (boolean sendEmptyMessageWhenIdle)
specifier|public
name|void
name|setSendEmptyMessageWhenIdle
parameter_list|(
name|boolean
name|sendEmptyMessageWhenIdle
parameter_list|)
block|{
name|this
operator|.
name|sendEmptyMessageWhenIdle
operator|=
name|sendEmptyMessageWhenIdle
expr_stmt|;
block|}
DECL|method|isSendEmptyMessageWhenIdle ()
specifier|public
name|boolean
name|isSendEmptyMessageWhenIdle
parameter_list|()
block|{
return|return
name|sendEmptyMessageWhenIdle
return|;
block|}
DECL|method|isGreedy ()
specifier|public
name|boolean
name|isGreedy
parameter_list|()
block|{
return|return
name|greedy
return|;
block|}
comment|/**      * If greedy then a poll is executed immediate after a previous poll that polled 1 or more messages.      */
DECL|method|setGreedy (boolean greedy)
specifier|public
name|void
name|setGreedy
parameter_list|(
name|boolean
name|greedy
parameter_list|)
block|{
name|this
operator|.
name|greedy
operator|=
name|greedy
expr_stmt|;
block|}
DECL|method|getScheduledExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|()
block|{
return|return
name|scheduledExecutorService
return|;
block|}
comment|/**      * Whether the scheduler has been started.      *<p/>      * The scheduler can be started with the {@link #startScheduler()} method.      *      * @return<tt>true</tt> if started,<tt>false</tt> if not.      */
DECL|method|isSchedulerStarted ()
specifier|public
name|boolean
name|isSchedulerStarted
parameter_list|()
block|{
return|return
name|scheduler
operator|.
name|isSchedulerStarted
argument_list|()
return|;
block|}
comment|/**      * Sets a custom shared {@link ScheduledExecutorService} to use as thread pool      *<p/>      *<b>Notice:</b> When using a custom thread pool, then the lifecycle of this thread      * pool is not controlled by this consumer (eg this consumer will not start/stop the thread pool      * when the consumer is started/stopped etc.)      *      * @param scheduledExecutorService the custom thread pool to use      */
DECL|method|setScheduledExecutorService (ScheduledExecutorService scheduledExecutorService)
specifier|public
name|void
name|setScheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|scheduledExecutorService
parameter_list|)
block|{
name|this
operator|.
name|scheduledExecutorService
operator|=
name|scheduledExecutorService
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * The polling method which is invoked periodically to poll this consumer      *      * @return number of messages polled, will be<tt>0</tt> if no message was polled at all.      * @throws Exception can be thrown if an exception occurred during polling      */
DECL|method|poll ()
specifier|protected
specifier|abstract
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
function_decl|;
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
if|if
condition|(
name|scheduler
operator|==
literal|null
condition|)
block|{
name|scheduler
operator|=
operator|new
name|DefaultScheduledPollConsumerScheduler
argument_list|()
expr_stmt|;
block|}
name|scheduler
operator|.
name|setCamelContext
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|scheduler
operator|.
name|scheduleTask
argument_list|(
name|this
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// configure scheduler with options from this consumer
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|this
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
argument_list|,
name|scheduler
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|scheduler
argument_list|,
literal|"scheduler"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|pollStrategy
argument_list|,
literal|"pollStrategy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
if|if
condition|(
name|isStartScheduler
argument_list|()
condition|)
block|{
name|startScheduler
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Starts the scheduler.      *<p/>      * If the scheduler is already started, then this is a noop method call.      */
DECL|method|startScheduler ()
specifier|public
name|void
name|startScheduler
parameter_list|()
block|{
name|scheduler
operator|.
name|startScheduler
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|scheduler
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
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
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
comment|// dont stop/cancel the future task since we just check in the run method
block|}
annotation|@
name|Override
DECL|method|onInit ()
specifier|public
name|void
name|onInit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|beforePoll (long timeout)
specifier|public
name|long
name|beforePoll
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Before poll {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
comment|// resume or start our self
if|if
condition|(
operator|!
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|this
argument_list|)
condition|)
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|// ensure at least timeout is as long as one poll delay
return|return
name|Math
operator|.
name|max
argument_list|(
name|timeout
argument_list|,
name|getDelay
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|afterPoll ()
specifier|public
name|void
name|afterPoll
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"After poll {}"
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
comment|// suspend or stop our self
if|if
condition|(
operator|!
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|this
argument_list|)
condition|)
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

