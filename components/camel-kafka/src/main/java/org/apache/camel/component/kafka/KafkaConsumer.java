begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
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
name|List
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
name|Properties
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
name|BrokenBarrierException
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
name|CyclicBarrier
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|consumer
operator|.
name|ConsumerConfig
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|consumer
operator|.
name|KafkaStream
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|javaapi
operator|.
name|consumer
operator|.
name|ConsumerConnector
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|message
operator|.
name|MessageAndMetadata
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
comment|/**  *  */
end_comment

begin_class
DECL|class|KafkaConsumer
specifier|public
class|class
name|KafkaConsumer
extends|extends
name|DefaultConsumer
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
name|KafkaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|executor
specifier|protected
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|KafkaEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|consumerBarriers
specifier|private
name|Map
argument_list|<
name|ConsumerConnector
argument_list|,
name|CyclicBarrier
argument_list|>
name|consumerBarriers
decl_stmt|;
DECL|method|KafkaConsumer (KafkaEndpoint endpoint, Processor processor)
specifier|public
name|KafkaConsumer
parameter_list|(
name|KafkaEndpoint
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|consumerBarriers
operator|=
operator|new
name|HashMap
argument_list|<
name|ConsumerConnector
argument_list|,
name|CyclicBarrier
argument_list|>
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getZookeeperConnect
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"zookeeper host or zookeeper connect must be specified"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getGroupId
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"groupId must not be null"
argument_list|)
throw|;
block|}
block|}
DECL|method|getProps ()
name|Properties
name|getProps
parameter_list|()
block|{
name|Properties
name|props
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|createConsumerProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"zookeeper.connect"
argument_list|,
name|endpoint
operator|.
name|getZookeeperConnect
argument_list|()
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"group.id"
argument_list|,
name|endpoint
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|props
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
name|log
operator|.
name|info
argument_list|(
literal|"Starting Kafka consumer"
argument_list|)
expr_stmt|;
name|executor
operator|=
name|endpoint
operator|.
name|createExecutor
argument_list|()
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
name|endpoint
operator|.
name|getConsumersCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|ConsumerConnector
name|consumer
init|=
name|kafka
operator|.
name|consumer
operator|.
name|Consumer
operator|.
name|createJavaConsumerConnector
argument_list|(
operator|new
name|ConsumerConfig
argument_list|(
name|getProps
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|topicCountMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|topicCountMap
operator|.
name|put
argument_list|(
name|endpoint
operator|.
name|getTopic
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConsumerStreams
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|>
argument_list|>
name|consumerMap
init|=
name|consumer
operator|.
name|createMessageStreams
argument_list|(
name|topicCountMap
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
argument_list|>
name|streams
init|=
name|consumerMap
operator|.
name|get
argument_list|(
name|endpoint
operator|.
name|getTopic
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isAutoCommitEnable
argument_list|()
operator|!=
literal|null
operator|&&
name|Boolean
operator|.
name|FALSE
operator|==
name|endpoint
operator|.
name|isAutoCommitEnable
argument_list|()
operator|.
name|booleanValue
argument_list|()
condition|)
block|{
name|CyclicBarrier
name|barrier
init|=
operator|new
name|CyclicBarrier
argument_list|(
name|endpoint
operator|.
name|getConsumerStreams
argument_list|()
argument_list|,
operator|new
name|CommitOffsetTask
argument_list|(
name|consumer
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
range|:
name|streams
control|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|BatchingConsumerTask
argument_list|(
name|stream
argument_list|,
name|barrier
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|consumerBarriers
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|barrier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
specifier|final
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
range|:
name|streams
control|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|AutoCommitConsumerTask
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|consumerBarriers
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
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
name|log
operator|.
name|info
argument_list|(
literal|"Stopping Kafka consumer"
argument_list|)
expr_stmt|;
for|for
control|(
name|ConsumerConnector
name|consumer
range|:
name|consumerBarriers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|consumer
operator|!=
literal|null
condition|)
block|{
name|consumer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
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
name|executor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
name|executor
operator|=
literal|null
expr_stmt|;
block|}
DECL|class|BatchingConsumerTask
class|class
name|BatchingConsumerTask
implements|implements
name|Runnable
block|{
DECL|field|stream
specifier|private
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
decl_stmt|;
DECL|field|berrier
specifier|private
name|CyclicBarrier
name|berrier
decl_stmt|;
DECL|method|BatchingConsumerTask (KafkaStream<byte[], byte[]> stream, CyclicBarrier berrier)
specifier|public
name|BatchingConsumerTask
parameter_list|(
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
parameter_list|,
name|CyclicBarrier
name|berrier
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
name|this
operator|.
name|berrier
operator|=
name|berrier
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|processed
init|=
literal|0
decl_stmt|;
for|for
control|(
name|MessageAndMetadata
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|mm
range|:
name|stream
control|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createKafkaExchange
argument_list|(
name|mm
argument_list|)
decl_stmt|;
try|try
block|{
name|processor
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
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|processed
operator|++
expr_stmt|;
if|if
condition|(
name|processed
operator|>=
name|endpoint
operator|.
name|getBatchSize
argument_list|()
condition|)
block|{
try|try
block|{
name|berrier
operator|.
name|await
argument_list|(
name|endpoint
operator|.
name|getBarrierAwaitTimeoutMs
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|processed
operator|=
literal|0
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|BrokenBarrierException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
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
block|}
block|}
block|}
DECL|class|CommitOffsetTask
class|class
name|CommitOffsetTask
implements|implements
name|Runnable
block|{
DECL|field|consumer
specifier|private
name|ConsumerConnector
name|consumer
decl_stmt|;
DECL|method|CommitOffsetTask (ConsumerConnector consumer)
specifier|public
name|CommitOffsetTask
parameter_list|(
name|ConsumerConnector
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|consumer
operator|.
name|commitOffsets
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|AutoCommitConsumerTask
class|class
name|AutoCommitConsumerTask
implements|implements
name|Runnable
block|{
DECL|field|stream
specifier|private
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
decl_stmt|;
DECL|method|AutoCommitConsumerTask (KafkaStream<byte[], byte[]> stream)
specifier|public
name|AutoCommitConsumerTask
parameter_list|(
name|KafkaStream
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|stream
parameter_list|)
block|{
name|this
operator|.
name|stream
operator|=
name|stream
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|MessageAndMetadata
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|mm
range|:
name|stream
control|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createKafkaExchange
argument_list|(
name|mm
argument_list|)
decl_stmt|;
try|try
block|{
name|processor
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
name|LOG
operator|.
name|error
argument_list|(
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
block|}
block|}
block|}
end_class

end_unit

