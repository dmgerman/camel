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
name|component
operator|.
name|sjms
operator|.
name|jms
operator|.
name|JmsMessageHelper
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

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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

begin_comment
comment|/**  * @author jkorab  */
end_comment

begin_class
DECL|class|SjmsBatchConsumer
specifier|public
class|class
name|SjmsBatchConsumer
extends|extends
name|DefaultConsumer
block|{
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
DECL|field|batchCount
specifier|private
specifier|static
name|AtomicInteger
name|batchCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|messagesReceived
specifier|private
specifier|static
name|AtomicLong
name|messagesReceived
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|messagesProcessed
specifier|private
specifier|static
name|AtomicLong
name|messagesProcessed
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|jmsConsumerExecutors
specifier|private
name|ExecutorService
name|jmsConsumerExecutors
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
name|completionTimeout
operator|=
name|sjmsBatchEndpoint
operator|.
name|getCompletionTimeout
argument_list|()
expr_stmt|;
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
try|try
block|{
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
block|}
catch|catch
parameter_list|(
name|JMSException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception caught closing connection: {}"
argument_list|,
name|getStackTrace
argument_list|(
name|ex
argument_list|)
argument_list|)
expr_stmt|;
return|return;
block|}
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
name|jmsConsumerExecutors
operator|.
name|execute
argument_list|(
operator|new
name|BatchConsumptionLoop
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
name|JMSException
name|jex
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"Exception caught closing connection: {}"
argument_list|,
name|getStackTrace
argument_list|(
name|jex
argument_list|)
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
block|}
DECL|method|getStackTrace (Exception ex)
specifier|private
name|String
name|getStackTrace
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|ex
operator|.
name|printStackTrace
argument_list|(
operator|new
name|PrintWriter
argument_list|(
name|writer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|class|BatchConsumptionLoop
specifier|private
class|class
name|BatchConsumptionLoop
implements|implements
name|Runnable
block|{
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
name|log
operator|.
name|error
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
name|log
operator|.
name|error
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
name|error
argument_list|(
literal|"Exception caught consuming from {}: {}"
argument_list|,
name|destinationName
argument_list|,
name|getStackTrace
argument_list|(
name|ex
argument_list|)
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
DECL|method|consumeBatchesOnLoop (Session session, MessageConsumer consumer)
specifier|private
name|void
name|consumeBatchesOnLoop
parameter_list|(
name|Session
name|session
parameter_list|,
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
name|batchConsumption
label|:
while|while
condition|(
name|running
operator|.
name|get
argument_list|()
condition|)
block|{
name|int
name|messageCount
init|=
literal|0
decl_stmt|;
comment|// reset the clock counters
name|long
name|timeElapsed
init|=
literal|0
decl_stmt|;
name|long
name|startTime
init|=
literal|0
decl_stmt|;
name|Exchange
name|aggregatedExchange
init|=
literal|null
decl_stmt|;
name|batch
label|:
while|while
condition|(
operator|(
name|completionSize
operator|<=
literal|0
operator|)
operator|||
operator|(
name|messageCount
operator|<
name|completionSize
operator|)
condition|)
block|{
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
if|if
condition|(
operator|(
name|usingTimeout
operator|)
operator|&&
operator|(
name|messageCount
operator|==
literal|0
operator|)
condition|)
block|{
comment|// this is the first message
name|startTime
operator|=
operator|new
name|Date
argument_list|()
operator|.
name|getTime
argument_list|()
expr_stmt|;
comment|// start counting down the period for this batch
block|}
name|messageCount
operator|++
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message received: {}"
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|message
operator|instanceof
name|ObjectMessage
operator|)
operator|||
operator|(
name|message
operator|instanceof
name|TextMessage
operator|)
condition|)
block|{
name|Exchange
name|exchange
init|=
name|JmsMessageHelper
operator|.
name|createExchange
argument_list|(
name|message
argument_list|,
name|getEndpoint
argument_list|()
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
name|SjmsBatchEndpoint
operator|.
name|PROPERTY_BATCH_SIZE
argument_list|,
name|messageCount
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unexpected message type: "
operator|+
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|(
name|usingTimeout
operator|)
operator|&&
operator|(
name|startTime
operator|>
literal|0
operator|)
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
break|break
name|batch
break|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutdown signal received - rolling batch back"
argument_list|)
expr_stmt|;
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
break|break
name|batchConsumption
break|;
block|}
block|}
comment|// batch
assert|assert
operator|(
name|aggregatedExchange
operator|!=
literal|null
operator|)
assert|;
name|process
argument_list|(
name|aggregatedExchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**          * Determine the time that a call to {@link MessageConsumer#receive()} should wait given the time that has elapsed for this batch.          * @param timeElapsed The time that has elapsed.          * @return The shorter of the time remaining or poll duration.          */
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
operator|(
name|timeRemaining
operator|>
name|pollDuration
operator|)
condition|?
name|pollDuration
else|:
name|timeRemaining
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"waiting for {}"
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
operator|(
name|timeElapsed
operator|>
literal|0
operator|)
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
DECL|method|process (Exchange exchange, Session session)
specifier|private
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
assert|assert
operator|(
name|exchange
operator|!=
literal|null
operator|)
assert|;
name|int
name|id
init|=
name|batchCount
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
name|SjmsBatchEndpoint
operator|.
name|PROPERTY_BATCH_SIZE
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
name|messagesReceived
operator|.
name|addAndGet
argument_list|(
name|batchSize
argument_list|)
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Completed processing[{}]:total={}"
argument_list|,
name|id
argument_list|,
name|messagesProcessed
operator|.
name|addAndGet
argument_list|(
name|batchSize
argument_list|)
argument_list|)
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
name|error
argument_list|(
literal|"Error processing exchange: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

