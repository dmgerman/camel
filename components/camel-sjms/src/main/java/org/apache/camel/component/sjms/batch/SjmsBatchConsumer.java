begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|batch
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
name|Date
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
name|ExecutorService
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
name|AtomicBoolean
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Queue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
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
name|impl
operator|.
name|DefaultConsumer
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
name|aggregate
operator|.
name|AggregationStrategy
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

begin_class
DECL|class|SjmsBatchConsumer
specifier|public
class|class
name|SjmsBatchConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|SJMS_BATCH_TIMEOUT_CHECKER
specifier|public
specifier|static
specifier|final
name|String
name|SJMS_BATCH_TIMEOUT_CHECKER
init|=
literal|"SJmsBatchTimeoutChecker"
decl_stmt|;
DECL|field|TRANSACTED
specifier|private
specifier|static
specifier|final
name|boolean
name|TRANSACTED
init|=
literal|true
decl_stmt|;
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
name|SjmsBatchConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// global counters, maybe they should be on component instead?
DECL|field|BATCH_COUNT
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|BATCH_COUNT
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|MESSAGE_RECEIVED
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|MESSAGE_RECEIVED
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|MESSAGE_PROCESSED
specifier|private
specifier|static
specifier|final
name|AtomicLong
name|MESSAGE_PROCESSED
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|timeoutCheckerExecutorService
specifier|private
name|ScheduledExecutorService
name|timeoutCheckerExecutorService
decl_stmt|;
DECL|field|shutdownTimeoutCheckerExecutorService
specifier|private
name|boolean
name|shutdownTimeoutCheckerExecutorService
decl_stmt|;
DECL|field|sjmsBatchEndpoint
specifier|private
specifier|final
name|SjmsBatchEndpoint
name|sjmsBatchEndpoint
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
specifier|final
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|completionSize
specifier|private
specifier|final
name|int
name|completionSize
decl_stmt|;
DECL|field|completionInterval
specifier|private
specifier|final
name|int
name|completionInterval
decl_stmt|;
DECL|field|completionTimeout
specifier|private
specifier|final
name|int
name|completionTimeout
decl_stmt|;
DECL|field|consumerCount
specifier|private
specifier|final
name|int
name|consumerCount
decl_stmt|;
DECL|field|pollDuration
specifier|private
specifier|final
name|int
name|pollDuration
decl_stmt|;
DECL|field|connectionFactory
specifier|private
specifier|final
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|field|destinationName
specifier|private
specifier|final
name|String
name|destinationName
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|jmsConsumerExecutors
specifier|private
name|ExecutorService
name|jmsConsumerExecutors
decl_stmt|;
DECL|field|running
specifier|private
specifier|final
name|AtomicBoolean
name|running
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|field|consumersShutdownLatchRef
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|CountDownLatch
argument_list|>
name|consumersShutdownLatchRef
init|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|method|SjmsBatchConsumer (SjmsBatchEndpoint sjmsBatchEndpoint, Processor processor)
specifier|public
name|SjmsBatchConsumer
parameter_list|(
name|SjmsBatchEndpoint
name|sjmsBatchEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|sjmsBatchEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|sjmsBatchEndpoint
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sjmsBatchEndpoint
argument_list|,
literal|"batchJmsEndpoint"
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
name|destinationName
operator|=
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|sjmsBatchEndpoint
operator|.
name|getDestinationName
argument_list|()
argument_list|,
literal|"destinationName"
argument_list|)
expr_stmt|;
name|completionSize
operator|=
name|sjmsBatchEndpoint
operator|.
name|getCompletionSize
argument_list|()
expr_stmt|;
name|completionInterval
operator|=
name|sjmsBatchEndpoint
operator|.
name|getCompletionInterval
argument_list|()
expr_stmt|;
name|completionTimeout
operator|=
name|sjmsBatchEndpoint
operator|.
name|getCompletionTimeout
argument_list|()
expr_stmt|;
if|if
condition|(
name|completionInterval
operator|>
literal|0
operator|&&
name|completionTimeout
operator|!=
name|SjmsBatchEndpoint
operator|.
name|DEFAULT_COMPLETION_TIMEOUT
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only one of completionInterval or completionTimeout can be used, not both."
argument_list|)
throw|;
block|}
if|if
condition|(
name|sjmsBatchEndpoint
operator|.
name|isSendEmptyMessageWhenIdle
argument_list|()
operator|&&
name|completionTimeout
operator|<=
literal|0
operator|&&
name|completionInterval
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"SendEmptyMessageWhenIdle can only be enabled if either completionInterval or completionTimeout is also set"
argument_list|)
throw|;
block|}
name|pollDuration
operator|=
name|sjmsBatchEndpoint
operator|.
name|getPollDuration
argument_list|()
expr_stmt|;
if|if
condition|(
name|pollDuration
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"pollDuration must be 0 or greater"
argument_list|)
throw|;
block|}
name|this
operator|.
name|aggregationStrategy
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sjmsBatchEndpoint
operator|.
name|getAggregationStrategy
argument_list|()
argument_list|,
literal|"aggregationStrategy"
argument_list|)
expr_stmt|;
name|consumerCount
operator|=
name|sjmsBatchEndpoint
operator|.
name|getConsumerCount
argument_list|()
expr_stmt|;
if|if
condition|(
name|consumerCount
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"consumerCount must be greater than 0"
argument_list|)
throw|;
block|}
name|SjmsBatchComponent
name|sjmsBatchComponent
init|=
operator|(
name|SjmsBatchComponent
operator|)
name|sjmsBatchEndpoint
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|connectionFactory
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sjmsBatchComponent
operator|.
name|getConnectionFactory
argument_list|()
argument_list|,
literal|"jmsBatchComponent.connectionFactory"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SjmsBatchEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|sjmsBatchEndpoint
return|;
block|}
DECL|method|getTimeoutCheckerExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getTimeoutCheckerExecutorService
parameter_list|()
block|{
return|return
name|timeoutCheckerExecutorService
return|;
block|}
DECL|method|setTimeoutCheckerExecutorService (ScheduledExecutorService timeoutCheckerExecutorService)
specifier|public
name|void
name|setTimeoutCheckerExecutorService
parameter_list|(
name|ScheduledExecutorService
name|timeoutCheckerExecutorService
parameter_list|)
block|{
name|this
operator|.
name|timeoutCheckerExecutorService
operator|=
name|timeoutCheckerExecutorService
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// start up a shared connection
name|connection
operator|=
name|connectionFactory
operator|.
name|createConnection
argument_list|()
expr_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isInfoEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting "
operator|+
name|consumerCount
operator|+
literal|" consumer(s) for "
operator|+
name|destinationName
operator|+
literal|":"
operator|+
name|completionSize
argument_list|)
expr_stmt|;
block|}
name|consumersShutdownLatchRef
operator|.
name|set
argument_list|(
operator|new
name|CountDownLatch
argument_list|(
name|consumerCount
argument_list|)
argument_list|)
expr_stmt|;
name|jmsConsumerExecutors
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"SjmsBatchConsumer"
argument_list|,
name|consumerCount
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|AtomicBoolean
argument_list|>
name|triggers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|consumerCount
condition|;
name|i
operator|++
control|)
block|{
name|BatchConsumptionLoop
name|loop
init|=
operator|new
name|BatchConsumptionLoop
argument_list|()
decl_stmt|;
name|triggers
operator|.
name|add
argument_list|(
name|loop
operator|.
name|getCompletionTimeoutTrigger
argument_list|()
argument_list|)
expr_stmt|;
name|jmsConsumerExecutors
operator|.
name|execute
argument_list|(
name|loop
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|completionInterval
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using CompletionInterval to run every "
operator|+
name|completionInterval
operator|+
literal|" millis."
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeoutCheckerExecutorService
operator|==
literal|null
condition|)
block|{
name|setTimeoutCheckerExecutorService
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
name|SJMS_BATCH_TIMEOUT_CHECKER
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|shutdownTimeoutCheckerExecutorService
operator|=
literal|true
expr_stmt|;
block|}
comment|// trigger completion based on interval
name|timeoutCheckerExecutorService
operator|.
name|scheduleAtFixedRate
argument_list|(
operator|new
name|CompletionIntervalTask
argument_list|(
name|triggers
argument_list|)
argument_list|,
name|completionInterval
argument_list|,
name|completionInterval
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
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|CountDownLatch
name|consumersShutdownLatch
init|=
name|consumersShutdownLatchRef
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumersShutdownLatch
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stop signalled, waiting on consumers to shut down"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumersShutdownLatch
operator|.
name|await
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Timeout waiting on consumer threads to signal completion - shutting down"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"All consumers have shut down"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Stop signalled while there are no consumers yet, so no need to wait for consumers"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down JMS connection"
argument_list|)
expr_stmt|;
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception caught closing JMS connection"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|jmsConsumerExecutors
argument_list|)
expr_stmt|;
name|jmsConsumerExecutors
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|shutdownTimeoutCheckerExecutorService
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|timeoutCheckerExecutorService
argument_list|)
expr_stmt|;
name|timeoutCheckerExecutorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Background task that triggers completion based on interval.      */
DECL|class|CompletionIntervalTask
specifier|private
specifier|final
class|class
name|CompletionIntervalTask
implements|implements
name|Runnable
block|{
DECL|field|triggers
specifier|private
specifier|final
name|List
argument_list|<
name|AtomicBoolean
argument_list|>
name|triggers
decl_stmt|;
DECL|method|CompletionIntervalTask (List<AtomicBoolean> triggers)
name|CompletionIntervalTask
parameter_list|(
name|List
argument_list|<
name|AtomicBoolean
argument_list|>
name|triggers
parameter_list|)
block|{
name|this
operator|.
name|triggers
operator|=
name|triggers
expr_stmt|;
block|}
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
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
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
literal|"Completion interval task cannot start due CamelContext({}) has not been started yet"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// signal
for|for
control|(
name|AtomicBoolean
name|trigger
range|:
name|triggers
control|)
block|{
name|trigger
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|BatchConsumptionLoop
specifier|private
class|class
name|BatchConsumptionLoop
implements|implements
name|Runnable
block|{
DECL|field|completionTimeoutTrigger
specifier|private
specifier|final
name|AtomicBoolean
name|completionTimeoutTrigger
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|task
specifier|private
specifier|final
name|BatchConsumptionTask
name|task
init|=
operator|new
name|BatchConsumptionTask
argument_list|(
name|completionTimeoutTrigger
argument_list|)
decl_stmt|;
DECL|method|getCompletionTimeoutTrigger ()
specifier|public
name|AtomicBoolean
name|getCompletionTimeoutTrigger
parameter_list|()
block|{
return|return
name|completionTimeoutTrigger
return|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
comment|// a batch corresponds to a single session that will be committed or rolled back by a background thread
specifier|final
name|Session
name|session
init|=
name|connection
operator|.
name|createSession
argument_list|(
name|TRANSACTED
argument_list|,
name|Session
operator|.
name|CLIENT_ACKNOWLEDGE
argument_list|)
decl_stmt|;
try|try
block|{
comment|// only batch consumption from queues is supported - it makes no sense to transactionally consume
comment|// from a topic as you don't car about message loss, users can just use a regular aggregator instead
name|Queue
name|queue
init|=
name|session
operator|.
name|createQueue
argument_list|(
name|destinationName
argument_list|)
decl_stmt|;
name|MessageConsumer
name|consumer
init|=
name|session
operator|.
name|createConsumer
argument_list|(
name|queue
argument_list|)
decl_stmt|;
try|try
block|{
name|task
operator|.
name|consumeBatchesOnLoop
argument_list|(
name|session
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
try|try
block|{
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex2
parameter_list|)
block|{
comment|// only include stacktrace in debug logging
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
literal|"Exception caught closing consumer"
argument_list|,
name|ex2
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|warn
argument_list|(
literal|"Exception caught closing consumer: {}"
argument_list|,
name|ex2
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
try|try
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex1
parameter_list|)
block|{
comment|// only include stacktrace in debug logging
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
literal|"Exception caught closing session: {}"
argument_list|,
name|ex1
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|warn
argument_list|(
literal|"Exception caught closing session: {}"
argument_list|,
name|ex1
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
comment|// from loop
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception caught consuming from "
operator|+
name|destinationName
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// indicate that we have shut down
name|CountDownLatch
name|consumersShutdownLatch
init|=
name|consumersShutdownLatchRef
operator|.
name|get
argument_list|()
decl_stmt|;
name|consumersShutdownLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|BatchConsumptionTask
specifier|private
specifier|final
class|class
name|BatchConsumptionTask
block|{
comment|// state
DECL|field|timeoutInterval
specifier|private
specifier|final
name|AtomicBoolean
name|timeoutInterval
decl_stmt|;
DECL|field|timeout
specifier|private
specifier|final
name|AtomicBoolean
name|timeout
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|messageCount
specifier|private
name|int
name|messageCount
decl_stmt|;
DECL|field|timeElapsed
specifier|private
name|long
name|timeElapsed
decl_stmt|;
DECL|field|startTime
specifier|private
name|long
name|startTime
decl_stmt|;
DECL|field|aggregatedExchange
specifier|private
name|Exchange
name|aggregatedExchange
decl_stmt|;
DECL|method|BatchConsumptionTask (AtomicBoolean timeoutInterval)
name|BatchConsumptionTask
parameter_list|(
name|AtomicBoolean
name|timeoutInterval
parameter_list|)
block|{
name|this
operator|.
name|timeoutInterval
operator|=
name|timeoutInterval
expr_stmt|;
block|}
DECL|method|consumeBatchesOnLoop (final Session session, final MessageConsumer consumer)
specifier|private
name|void
name|consumeBatchesOnLoop
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|,
specifier|final
name|MessageConsumer
name|consumer
parameter_list|)
throws|throws
name|JMSException
block|{
specifier|final
name|boolean
name|usingTimeout
init|=
name|completionTimeout
operator|>
literal|0
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"BatchConsumptionTask +++ start +++"
argument_list|)
expr_stmt|;
while|while
condition|(
name|running
operator|.
name|get
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"BatchConsumptionTask running"
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeout
operator|.
name|compareAndSet
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
operator|||
name|timeoutInterval
operator|.
name|compareAndSet
argument_list|(
literal|true
argument_list|,
literal|false
argument_list|)
condition|)
block|{
comment|// trigger timeout
name|LOG
operator|.
name|trace
argument_list|(
literal|"Completion batch due timeout"
argument_list|)
expr_stmt|;
name|completionBatch
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
continue|continue;
block|}
if|if
condition|(
name|completionSize
operator|>
literal|0
operator|&&
name|messageCount
operator|>=
name|completionSize
condition|)
block|{
comment|// trigger completion size
name|LOG
operator|.
name|trace
argument_list|(
literal|"Completion batch due size"
argument_list|)
expr_stmt|;
name|completionBatch
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
continue|continue;
block|}
comment|// check periodically to see whether we should be shutting down
name|long
name|waitTime
init|=
operator|(
name|usingTimeout
operator|&&
operator|(
name|timeElapsed
operator|>
literal|0
operator|)
operator|)
condition|?
name|getReceiveWaitTime
argument_list|(
name|timeElapsed
argument_list|)
else|:
name|pollDuration
decl_stmt|;
name|Message
name|message
init|=
name|consumer
operator|.
name|receive
argument_list|(
name|waitTime
argument_list|)
decl_stmt|;
if|if
condition|(
name|running
operator|.
name|get
argument_list|()
condition|)
block|{
comment|// no interruptions received
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
comment|// timed out, no message received
name|LOG
operator|.
name|trace
argument_list|(
literal|"No message received"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageCount
operator|++
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"#{} messages received"
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
if|if
condition|(
name|usingTimeout
operator|&&
name|startTime
operator|==
literal|0
condition|)
block|{
comment|// this is the first message start counting down the period for this batch
name|startTime
operator|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|message
argument_list|,
name|session
argument_list|)
decl_stmt|;
name|aggregatedExchange
operator|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|aggregatedExchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|aggregatedExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|usingTimeout
operator|&&
name|startTime
operator|>
literal|0
condition|)
block|{
comment|// a batch has been started, check whether it should be timed out
name|long
name|currentTime
init|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|timeElapsed
operator|=
name|currentTime
operator|-
name|startTime
expr_stmt|;
if|if
condition|(
name|timeElapsed
operator|>
name|completionTimeout
condition|)
block|{
comment|// batch finished by timeout
name|timeout
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"This batch has more time until the timeout, elapsed: {} timeout: {}"
argument_list|,
name|timeElapsed
argument_list|,
name|completionTimeout
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutdown signal received - rolling back batch"
argument_list|)
expr_stmt|;
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"BatchConsumptionTask +++ end +++"
argument_list|)
expr_stmt|;
block|}
DECL|method|reset ()
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|messageCount
operator|=
literal|0
expr_stmt|;
name|timeElapsed
operator|=
literal|0
expr_stmt|;
name|startTime
operator|=
literal|0
expr_stmt|;
name|aggregatedExchange
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|completionBatch (final Session session)
specifier|private
name|void
name|completionBatch
parameter_list|(
specifier|final
name|Session
name|session
parameter_list|)
block|{
comment|// batch
if|if
condition|(
name|aggregatedExchange
operator|==
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|isSendEmptyMessageWhenIdle
argument_list|()
condition|)
block|{
name|processEmptyMessage
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aggregatedExchange
operator|!=
literal|null
condition|)
block|{
name|processBatch
argument_list|(
name|aggregatedExchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**          * Determine the time that a call to {@link MessageConsumer#receive()} should wait given the time that has elapsed for this batch.          *          * @param timeElapsed The time that has elapsed.          * @return The shorter of the time remaining or poll duration.          */
DECL|method|getReceiveWaitTime (long timeElapsed)
specifier|private
name|long
name|getReceiveWaitTime
parameter_list|(
name|long
name|timeElapsed
parameter_list|)
block|{
name|long
name|timeRemaining
init|=
name|getTimeRemaining
argument_list|(
name|timeElapsed
argument_list|)
decl_stmt|;
comment|// wait for the shorter of the time remaining or the poll duration
if|if
condition|(
name|timeRemaining
operator|<=
literal|0
condition|)
block|{
comment|// ensure that the thread doesn't wait indefinitely
name|timeRemaining
operator|=
literal|1
expr_stmt|;
block|}
specifier|final
name|long
name|waitTime
init|=
name|Math
operator|.
name|min
argument_list|(
name|timeRemaining
argument_list|,
name|pollDuration
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for {}"
argument_list|,
name|waitTime
argument_list|)
expr_stmt|;
return|return
name|waitTime
return|;
block|}
DECL|method|getTimeRemaining (long timeElapsed)
specifier|private
name|long
name|getTimeRemaining
parameter_list|(
name|long
name|timeElapsed
parameter_list|)
block|{
name|long
name|timeRemaining
init|=
name|completionTimeout
operator|-
name|timeElapsed
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
operator|&&
name|timeElapsed
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Time remaining this batch: {}"
argument_list|,
name|timeRemaining
argument_list|)
expr_stmt|;
block|}
return|return
name|timeRemaining
return|;
block|}
comment|/**          * No messages in batch so send an empty message instead.          */
DECL|method|processEmptyMessage ()
specifier|private
name|void
name|processEmptyMessage
parameter_list|()
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
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**          * Send an message with the batches messages.          */
DECL|method|processBatch (Exchange exchange, Session session)
specifier|private
name|void
name|processBatch
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
name|int
name|id
init|=
name|BATCH_COUNT
operator|.
name|getAndIncrement
argument_list|()
decl_stmt|;
name|int
name|batchSize
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|long
name|total
init|=
name|MESSAGE_RECEIVED
operator|.
name|get
argument_list|()
operator|+
name|batchSize
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing batch["
operator|+
name|id
operator|+
literal|"]:size="
operator|+
name|batchSize
operator|+
literal|":total="
operator|+
name|total
argument_list|)
expr_stmt|;
block|}
name|SessionCompletion
name|sessionCompletion
init|=
operator|new
name|SessionCompletion
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
name|sessionCompletion
argument_list|)
expr_stmt|;
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|long
name|total
init|=
name|MESSAGE_PROCESSED
operator|.
name|addAndGet
argument_list|(
name|batchSize
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed processing[{}]:total={}"
argument_list|,
name|id
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

