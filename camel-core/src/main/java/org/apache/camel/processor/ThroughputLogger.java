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
name|text
operator|.
name|NumberFormat
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
name|support
operator|.
name|ServiceSupport
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
name|CamelLogger
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
comment|/**  * A logger for logging message throughput.  *  * @version   */
end_comment

begin_class
DECL|class|ThroughputLogger
specifier|public
class|class
name|ThroughputLogger
extends|extends
name|ServiceSupport
implements|implements
name|Processor
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
name|ThroughputLogger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|receivedCounter
specifier|private
specifier|final
name|AtomicInteger
name|receivedCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|numberFormat
specifier|private
name|NumberFormat
name|numberFormat
init|=
name|NumberFormat
operator|.
name|getNumberInstance
argument_list|()
decl_stmt|;
DECL|field|groupReceivedCount
specifier|private
name|long
name|groupReceivedCount
decl_stmt|;
DECL|field|groupActiveOnly
specifier|private
name|boolean
name|groupActiveOnly
decl_stmt|;
DECL|field|groupSize
specifier|private
name|Integer
name|groupSize
decl_stmt|;
DECL|field|groupDelay
specifier|private
name|long
name|groupDelay
init|=
literal|1000
decl_stmt|;
DECL|field|groupInterval
specifier|private
name|Long
name|groupInterval
decl_stmt|;
DECL|field|startTime
specifier|private
name|long
name|startTime
decl_stmt|;
DECL|field|groupStartTime
specifier|private
name|long
name|groupStartTime
decl_stmt|;
DECL|field|action
specifier|private
name|String
name|action
init|=
literal|"Received"
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|logSchedulerService
specifier|private
name|ScheduledExecutorService
name|logSchedulerService
decl_stmt|;
DECL|field|log
specifier|private
name|CamelLogger
name|log
decl_stmt|;
DECL|method|ThroughputLogger (CamelLogger log)
specifier|public
name|ThroughputLogger
parameter_list|(
name|CamelLogger
name|log
parameter_list|)
block|{
name|this
operator|.
name|log
operator|=
name|log
expr_stmt|;
block|}
DECL|method|ThroughputLogger (CamelLogger log, Integer groupSize)
specifier|public
name|ThroughputLogger
parameter_list|(
name|CamelLogger
name|log
parameter_list|,
name|Integer
name|groupSize
parameter_list|)
block|{
name|this
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|setGroupSize
argument_list|(
name|groupSize
argument_list|)
expr_stmt|;
block|}
DECL|method|ThroughputLogger (CamelLogger log, CamelContext camelContext, Long groupInterval, Long groupDelay, Boolean groupActiveOnly)
specifier|public
name|ThroughputLogger
parameter_list|(
name|CamelLogger
name|log
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|,
name|Long
name|groupInterval
parameter_list|,
name|Long
name|groupDelay
parameter_list|,
name|Boolean
name|groupActiveOnly
parameter_list|)
block|{
name|this
argument_list|(
name|log
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|setGroupInterval
argument_list|(
name|groupInterval
argument_list|)
expr_stmt|;
name|setGroupActiveOnly
argument_list|(
name|groupActiveOnly
argument_list|)
expr_stmt|;
if|if
condition|(
name|groupDelay
operator|!=
literal|null
condition|)
block|{
name|setGroupDelay
argument_list|(
name|groupDelay
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|startTime
operator|==
literal|0
condition|)
block|{
name|startTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
name|int
name|receivedCount
init|=
name|receivedCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
comment|//only process if groupSize is set...otherwise we're in groupInterval mode
if|if
condition|(
name|groupSize
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|receivedCount
operator|%
name|groupSize
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|log
argument_list|(
name|createLogMessage
argument_list|(
name|exchange
argument_list|,
name|receivedCount
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getGroupSize ()
specifier|public
name|Integer
name|getGroupSize
parameter_list|()
block|{
return|return
name|groupSize
return|;
block|}
DECL|method|setGroupSize (Integer groupSize)
specifier|public
name|void
name|setGroupSize
parameter_list|(
name|Integer
name|groupSize
parameter_list|)
block|{
if|if
condition|(
name|groupSize
operator|==
literal|null
operator|||
name|groupSize
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"groupSize must be positive, was: "
operator|+
name|groupSize
argument_list|)
throw|;
block|}
name|this
operator|.
name|groupSize
operator|=
name|groupSize
expr_stmt|;
block|}
DECL|method|getGroupInterval ()
specifier|public
name|Long
name|getGroupInterval
parameter_list|()
block|{
return|return
name|groupInterval
return|;
block|}
DECL|method|setGroupInterval (Long groupInterval)
specifier|public
name|void
name|setGroupInterval
parameter_list|(
name|Long
name|groupInterval
parameter_list|)
block|{
if|if
condition|(
name|groupInterval
operator|==
literal|null
operator|||
name|groupInterval
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"groupInterval must be positive, was: "
operator|+
name|groupInterval
argument_list|)
throw|;
block|}
name|this
operator|.
name|groupInterval
operator|=
name|groupInterval
expr_stmt|;
block|}
DECL|method|getGroupDelay ()
specifier|public
name|long
name|getGroupDelay
parameter_list|()
block|{
return|return
name|groupDelay
return|;
block|}
DECL|method|setGroupDelay (long groupDelay)
specifier|public
name|void
name|setGroupDelay
parameter_list|(
name|long
name|groupDelay
parameter_list|)
block|{
name|this
operator|.
name|groupDelay
operator|=
name|groupDelay
expr_stmt|;
block|}
DECL|method|getGroupActiveOnly ()
specifier|public
name|boolean
name|getGroupActiveOnly
parameter_list|()
block|{
return|return
name|groupActiveOnly
return|;
block|}
DECL|method|setGroupActiveOnly (boolean groupActiveOnly)
specifier|private
name|void
name|setGroupActiveOnly
parameter_list|(
name|boolean
name|groupActiveOnly
parameter_list|)
block|{
name|this
operator|.
name|groupActiveOnly
operator|=
name|groupActiveOnly
expr_stmt|;
block|}
DECL|method|getNumberFormat ()
specifier|public
name|NumberFormat
name|getNumberFormat
parameter_list|()
block|{
return|return
name|numberFormat
return|;
block|}
DECL|method|setNumberFormat (NumberFormat numberFormat)
specifier|public
name|void
name|setNumberFormat
parameter_list|(
name|NumberFormat
name|numberFormat
parameter_list|)
block|{
name|this
operator|.
name|numberFormat
operator|=
name|numberFormat
expr_stmt|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// if an interval was specified, create a background thread
if|if
condition|(
name|groupInterval
operator|!=
literal|null
condition|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|logSchedulerService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
literal|"ThroughputLogger"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|Runnable
name|scheduledLogTask
init|=
operator|new
name|ScheduledLogTask
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Scheduling throughput log to run every "
operator|+
name|groupInterval
operator|+
literal|" millis."
argument_list|)
expr_stmt|;
comment|// must use fixed rate to have it trigger at every X interval
name|logSchedulerService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|scheduledLogTask
argument_list|,
name|groupDelay
argument_list|,
name|groupInterval
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|logSchedulerService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|logSchedulerService
argument_list|)
expr_stmt|;
name|logSchedulerService
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|createLogMessage (Exchange exchange, int receivedCount)
specifier|protected
name|String
name|createLogMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|receivedCount
parameter_list|)
block|{
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupStartTime
operator|==
literal|0
condition|)
block|{
name|groupStartTime
operator|=
name|startTime
expr_stmt|;
block|}
name|double
name|rate
init|=
name|messagesPerSecond
argument_list|(
name|groupSize
argument_list|,
name|groupStartTime
argument_list|,
name|time
argument_list|)
decl_stmt|;
name|double
name|average
init|=
name|messagesPerSecond
argument_list|(
name|receivedCount
argument_list|,
name|startTime
argument_list|,
name|time
argument_list|)
decl_stmt|;
name|long
name|duration
init|=
name|time
operator|-
name|groupStartTime
decl_stmt|;
name|groupStartTime
operator|=
name|time
expr_stmt|;
return|return
name|getAction
argument_list|()
operator|+
literal|": "
operator|+
name|receivedCount
operator|+
literal|" messages so far. Last group took: "
operator|+
name|duration
operator|+
literal|" millis which is: "
operator|+
name|numberFormat
operator|.
name|format
argument_list|(
name|rate
argument_list|)
operator|+
literal|" messages per second. average: "
operator|+
name|numberFormat
operator|.
name|format
argument_list|(
name|average
argument_list|)
return|;
block|}
comment|/**      * Background task that logs throughput stats.      */
DECL|class|ScheduledLogTask
specifier|private
specifier|final
class|class
name|ScheduledLogTask
implements|implements
name|Runnable
block|{
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// only run if CamelContext has been fully started
if|if
condition|(
operator|!
name|camelContext
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"ThroughputLogger cannot start because CamelContext({}) has not been started yet"
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
name|createGroupIntervalLogMessage
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createGroupIntervalLogMessage ()
specifier|protected
name|void
name|createGroupIntervalLogMessage
parameter_list|()
block|{
comment|// this indicates that no messages have been received yet...don't log yet
if|if
condition|(
name|startTime
operator|==
literal|0
condition|)
block|{
return|return;
block|}
name|int
name|receivedCount
init|=
name|receivedCounter
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// if configured, hide log messages when no new messages have been received
if|if
condition|(
name|groupActiveOnly
operator|&&
name|receivedCount
operator|==
name|groupReceivedCount
condition|)
block|{
return|return;
block|}
name|long
name|time
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupStartTime
operator|==
literal|0
condition|)
block|{
name|groupStartTime
operator|=
name|startTime
expr_stmt|;
block|}
name|long
name|duration
init|=
name|time
operator|-
name|groupStartTime
decl_stmt|;
name|long
name|currentCount
init|=
name|receivedCount
operator|-
name|groupReceivedCount
decl_stmt|;
name|double
name|rate
init|=
name|messagesPerSecond
argument_list|(
name|currentCount
argument_list|,
name|groupStartTime
argument_list|,
name|time
argument_list|)
decl_stmt|;
name|double
name|average
init|=
name|messagesPerSecond
argument_list|(
name|receivedCount
argument_list|,
name|startTime
argument_list|,
name|time
argument_list|)
decl_stmt|;
name|groupStartTime
operator|=
name|time
expr_stmt|;
name|groupReceivedCount
operator|=
name|receivedCount
expr_stmt|;
name|String
name|message
init|=
name|getAction
argument_list|()
operator|+
literal|": "
operator|+
name|currentCount
operator|+
literal|" new messages, with total "
operator|+
name|receivedCount
operator|+
literal|" so far. Last group took: "
operator|+
name|duration
operator|+
literal|" millis which is: "
operator|+
name|numberFormat
operator|.
name|format
argument_list|(
name|rate
argument_list|)
operator|+
literal|" messages per second. average: "
operator|+
name|numberFormat
operator|.
name|format
argument_list|(
name|average
argument_list|)
decl_stmt|;
name|log
operator|.
name|log
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
DECL|method|messagesPerSecond (long messageCount, long startTime, long endTime)
specifier|protected
name|double
name|messagesPerSecond
parameter_list|(
name|long
name|messageCount
parameter_list|,
name|long
name|startTime
parameter_list|,
name|long
name|endTime
parameter_list|)
block|{
comment|// timeOneMessage = elapsed / messageCount
comment|// messagePerSend = 1000 / timeOneMessage
name|double
name|rate
init|=
name|messageCount
operator|*
literal|1000.0
decl_stmt|;
name|rate
operator|/=
name|endTime
operator|-
name|startTime
expr_stmt|;
return|return
name|rate
return|;
block|}
block|}
end_class

end_unit

