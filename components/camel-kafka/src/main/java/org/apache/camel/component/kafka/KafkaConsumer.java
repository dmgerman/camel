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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|spi
operator|.
name|StateRepository
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
name|IOHelper
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
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerRecord
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerRecords
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|OffsetAndMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|TopicPartition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|errors
operator|.
name|InterruptException
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
DECL|field|pollTimeoutMs
specifier|private
specifier|final
name|Long
name|pollTimeoutMs
decl_stmt|;
comment|// This list helps working around the infinite loop of KAFKA-1894
DECL|field|tasks
specifier|private
specifier|final
name|List
argument_list|<
name|KafkaFetchRecords
argument_list|>
name|tasks
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
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
name|pollTimeoutMs
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPollTimeoutMs
argument_list|()
expr_stmt|;
comment|// brokers can be configured on endpoint or component level
name|String
name|brokers
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokers
argument_list|()
decl_stmt|;
if|if
condition|(
name|brokers
operator|==
literal|null
condition|)
block|{
name|brokers
operator|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getBrokers
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|brokers
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Brokers must be configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
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
name|endpoint
operator|.
name|updateClassProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|// brokers can be configured on endpoint or component level
name|String
name|brokers
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getBrokers
argument_list|()
decl_stmt|;
if|if
condition|(
name|brokers
operator|==
literal|null
condition|)
block|{
name|brokers
operator|=
name|endpoint
operator|.
name|getComponent
argument_list|()
operator|.
name|getBrokers
argument_list|()
expr_stmt|;
block|}
name|props
operator|.
name|put
argument_list|(
name|ConsumerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|,
name|brokers
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|ConsumerConfig
operator|.
name|GROUP_ID_CONFIG
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting Kafka consumer"
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStart
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|getConsumersCount
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|KafkaFetchRecords
name|task
init|=
operator|new
name|KafkaFetchRecords
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopic
argument_list|()
argument_list|,
name|i
operator|+
literal|""
argument_list|,
name|getProps
argument_list|()
argument_list|)
decl_stmt|;
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|tasks
operator|.
name|add
argument_list|(
name|task
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Stopping Kafka consumer"
argument_list|)
expr_stmt|;
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
name|shutdownGraceful
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
if|if
condition|(
operator|!
name|executor
operator|.
name|isTerminated
argument_list|()
condition|)
block|{
name|tasks
operator|.
name|forEach
argument_list|(
name|KafkaFetchRecords
operator|::
name|shutdown
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
name|tasks
operator|.
name|clear
argument_list|()
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|class|KafkaFetchRecords
class|class
name|KafkaFetchRecords
implements|implements
name|Runnable
block|{
DECL|field|consumer
specifier|private
specifier|final
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|KafkaConsumer
name|consumer
decl_stmt|;
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
DECL|field|threadId
specifier|private
specifier|final
name|String
name|threadId
decl_stmt|;
DECL|field|kafkaProps
specifier|private
specifier|final
name|Properties
name|kafkaProps
decl_stmt|;
DECL|method|KafkaFetchRecords (String topicName, String id, Properties kafkaProps)
name|KafkaFetchRecords
parameter_list|(
name|String
name|topicName
parameter_list|,
name|String
name|id
parameter_list|,
name|Properties
name|kafkaProps
parameter_list|)
block|{
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
name|this
operator|.
name|threadId
operator|=
name|topicName
operator|+
literal|"-"
operator|+
literal|"Thread "
operator|+
name|id
expr_stmt|;
name|this
operator|.
name|kafkaProps
operator|=
name|kafkaProps
expr_stmt|;
name|ClassLoader
name|threadClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
comment|// Kafka uses reflection for loading authentication settings, use its classloader
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|KafkaConsumer
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|KafkaConsumer
argument_list|(
name|kafkaProps
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|threadClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Subscribing {} to topic {}"
argument_list|,
name|threadId
argument_list|,
name|topicName
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|subscribe
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|topicName
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|StateRepository
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|offsetRepository
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOffsetRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|offsetRepository
operator|!=
literal|null
condition|)
block|{
comment|// This poll to ensures we have an assigned partition otherwise seek won't work
name|ConsumerRecords
name|poll
init|=
name|consumer
operator|.
name|poll
argument_list|(
literal|100
argument_list|)
decl_stmt|;
for|for
control|(
name|TopicPartition
name|topicPartition
range|:
operator|(
name|Set
argument_list|<
name|TopicPartition
argument_list|>
operator|)
name|consumer
operator|.
name|assignment
argument_list|()
control|)
block|{
name|String
name|offsetState
init|=
name|offsetRepository
operator|.
name|getState
argument_list|(
name|serializeOffsetKey
argument_list|(
name|topicPartition
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|offsetState
operator|!=
literal|null
operator|&&
operator|!
name|offsetState
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// The state contains the last read offset so you need to seek from the next one
name|long
name|offset
init|=
name|deserializeOffsetValue
argument_list|(
name|offsetState
argument_list|)
operator|+
literal|1
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resuming partition {} from offset {} from state"
argument_list|,
name|topicPartition
operator|.
name|partition
argument_list|()
argument_list|,
name|offset
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|seek
argument_list|(
name|topicPartition
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// If the init poll has returned some data of a currently unknown topic/partition in the state
comment|// then resume from their offset in order to avoid losing data
name|List
argument_list|<
name|ConsumerRecord
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|partitionRecords
init|=
name|poll
operator|.
name|records
argument_list|(
name|topicPartition
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|partitionRecords
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|long
name|offset
init|=
name|partitionRecords
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|offset
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Resuming partition {} from offset {}"
argument_list|,
name|topicPartition
operator|.
name|partition
argument_list|()
argument_list|,
name|offset
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|seek
argument_list|(
name|topicPartition
argument_list|,
name|offset
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSeekToBeginning
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} is seeking to the beginning on topic {}"
argument_list|,
name|threadId
argument_list|,
name|topicName
argument_list|)
expr_stmt|;
comment|// This poll to ensures we have an assigned partition otherwise seek won't work
name|consumer
operator|.
name|poll
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|seekToBeginning
argument_list|(
name|consumer
operator|.
name|assignment
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|isStoppingOrStopped
argument_list|()
operator|&&
operator|!
name|isSuspendingOrSuspended
argument_list|()
condition|)
block|{
name|ConsumerRecords
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|allRecords
init|=
name|consumer
operator|.
name|poll
argument_list|(
name|pollTimeoutMs
argument_list|)
decl_stmt|;
for|for
control|(
name|TopicPartition
name|partition
range|:
name|allRecords
operator|.
name|partitions
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ConsumerRecord
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|>
name|partitionRecords
init|=
name|allRecords
operator|.
name|records
argument_list|(
name|partition
argument_list|)
decl_stmt|;
for|for
control|(
name|ConsumerRecord
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|record
range|:
name|partitionRecords
control|)
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
literal|"partition = {}, offset = {}, key = {}, value = {}"
argument_list|,
name|record
operator|.
name|partition
argument_list|()
argument_list|,
name|record
operator|.
name|offset
argument_list|()
argument_list|,
name|record
operator|.
name|key
argument_list|()
argument_list|,
name|record
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createKafkaExchange
argument_list|(
name|record
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|offsetRepository
operator|!=
literal|null
condition|)
block|{
name|long
name|partitionLastOffset
init|=
name|partitionRecords
operator|.
name|get
argument_list|(
name|partitionRecords
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|offset
argument_list|()
decl_stmt|;
name|offsetRepository
operator|.
name|setState
argument_list|(
name|serializeOffsetKey
argument_list|(
name|partition
argument_list|)
argument_list|,
name|serializeOffsetValue
argument_list|(
name|partitionLastOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoCommitEnable
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoCommitEnable
argument_list|()
condition|)
block|{
comment|// if autocommit is false
name|long
name|partitionLastoffset
init|=
name|partitionRecords
operator|.
name|get
argument_list|(
name|partitionRecords
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|offset
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|commitSync
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|partition
argument_list|,
operator|new
name|OffsetAndMetadata
argument_list|(
name|partitionLastoffset
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsubscribing {} from topic {}"
argument_list|,
name|threadId
argument_list|,
name|topicName
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Interrupted while consuming "
operator|+
name|threadId
operator|+
literal|" from kafka topic"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Unsubscribing {} from topic {}"
argument_list|,
name|threadId
argument_list|,
name|topicName
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|unsubscribe
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
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
literal|"Error consuming "
operator|+
name|threadId
operator|+
literal|" from kafka topic"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing {} "
argument_list|,
name|threadId
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shutdown ()
specifier|private
name|void
name|shutdown
parameter_list|()
block|{
comment|// As advised in the KAFKA-1894 ticket, calling this wakeup method breaks the infinite loop
name|consumer
operator|.
name|wakeup
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|serializeOffsetKey (TopicPartition topicPartition)
specifier|protected
name|String
name|serializeOffsetKey
parameter_list|(
name|TopicPartition
name|topicPartition
parameter_list|)
block|{
return|return
name|topicPartition
operator|.
name|topic
argument_list|()
operator|+
literal|'/'
operator|+
name|topicPartition
operator|.
name|partition
argument_list|()
return|;
block|}
DECL|method|serializeOffsetValue (long offset)
specifier|protected
name|String
name|serializeOffsetValue
parameter_list|(
name|long
name|offset
parameter_list|)
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|offset
argument_list|)
return|;
block|}
DECL|method|deserializeOffsetValue (String offset)
specifier|protected
name|long
name|deserializeOffsetValue
parameter_list|(
name|String
name|offset
parameter_list|)
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|offset
argument_list|)
return|;
block|}
block|}
end_class

end_unit

