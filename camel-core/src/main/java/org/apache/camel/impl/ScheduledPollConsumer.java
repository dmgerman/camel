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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * A useful base class for any consumer which is polling based  *   * @version $Revision$  */
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
DECL|field|executor
specifier|private
specifier|final
name|ScheduledExecutorService
name|executor
decl_stmt|;
DECL|field|future
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
comment|// if adding more options then align with ScheduledPollEndpoint#configureScheduledPollConsumerProperties
DECL|field|initialDelay
specifier|private
name|long
name|initialDelay
init|=
literal|1000
decl_stmt|;
DECL|field|delay
specifier|private
name|long
name|delay
init|=
literal|500
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
DECL|field|useFixedDelay
specifier|private
name|boolean
name|useFixedDelay
decl_stmt|;
DECL|field|pollStrategy
specifier|private
name|PollingConsumerPollStrategy
name|pollStrategy
init|=
operator|new
name|DefaultPollingConsumerPollStrategy
argument_list|()
decl_stmt|;
DECL|method|ScheduledPollConsumer (DefaultEndpoint endpoint, Processor processor)
specifier|public
name|ScheduledPollConsumer
parameter_list|(
name|DefaultEndpoint
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
comment|// we only need one thread in the pool to schedule this task
name|this
operator|.
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executor
argument_list|,
literal|"executor"
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduledPollConsumer (Endpoint endpoint, Processor processor, ScheduledExecutorService executor)
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
name|executor
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
name|executor
operator|=
name|executor
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executor
argument_list|,
literal|"executor"
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
if|if
condition|(
name|isSuspended
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot start to poll: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
operator|+
literal|" as its suspended"
argument_list|)
expr_stmt|;
block|}
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
while|while
condition|(
operator|!
name|done
condition|)
block|{
try|try
block|{
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting to poll: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Retrying attempt "
operator|+
name|retryCounter
operator|+
literal|" to poll: "
operator|+
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot begin polling as pollStrategy returned false: "
operator|+
name|pollStrategy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Finished polling: "
operator|+
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
name|done
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// catch throwable to not let the thread die
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
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|". Will try again at next poll"
argument_list|,
name|t
argument_list|)
expr_stmt|;
comment|// we are done due this fatal error
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
comment|// catch throwable to not let the thread die
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
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|". Will try again at next poll"
argument_list|,
name|t
argument_list|)
expr_stmt|;
comment|// we are done due this fatal error
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// avoid this thread to throw exceptions because the thread pool wont re-schedule a new thread
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
name|isUseFixedDelay
argument_list|()
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduling poll (fixed delay) with initialDelay: "
operator|+
name|getInitialDelay
argument_list|()
operator|+
literal|", delay: "
operator|+
name|getDelay
argument_list|()
operator|+
literal|" ("
operator|+
name|getTimeUnit
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|") for: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|future
operator|=
name|executor
operator|.
name|scheduleWithFixedDelay
argument_list|(
name|this
argument_list|,
name|getInitialDelay
argument_list|()
argument_list|,
name|getDelay
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Scheduling poll (fixed rate) with initialDelay: "
operator|+
name|getInitialDelay
argument_list|()
operator|+
literal|", delay: "
operator|+
name|getDelay
argument_list|()
operator|+
literal|" ("
operator|+
name|getTimeUnit
argument_list|()
operator|.
name|name
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|") for: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|future
operator|=
name|executor
operator|.
name|scheduleAtFixedRate
argument_list|(
name|this
argument_list|,
name|getInitialDelay
argument_list|()
argument_list|,
name|getDelay
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
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
block|{
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
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
block|}
end_class

end_unit

