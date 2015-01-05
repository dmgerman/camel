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
name|net
operator|.
name|URISyntaxException
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
name|Message
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
name|Producer
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
name|DefaultEndpoint
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
name|DefaultExchange
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
name|DefaultMessage
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
name|UriEndpoint
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"kafka"
argument_list|,
name|consumerClass
operator|=
name|KafkaConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|KafkaEndpoint
specifier|public
class|class
name|KafkaEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
DECL|field|brokers
specifier|private
name|String
name|brokers
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|KafkaConfiguration
name|configuration
init|=
operator|new
name|KafkaConfiguration
argument_list|()
decl_stmt|;
DECL|method|KafkaEndpoint ()
specifier|public
name|KafkaEndpoint
parameter_list|()
block|{     }
DECL|method|KafkaEndpoint (String endpointUri, String remaining, KafkaComponent component)
specifier|public
name|KafkaEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|KafkaComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|brokers
operator|=
name|remaining
operator|.
name|split
argument_list|(
literal|"\\?"
argument_list|)
index|[
literal|0
index|]
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|KafkaConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
name|createConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (KafkaConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|KafkaConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createConfiguration ()
specifier|protected
name|KafkaConfiguration
name|createConfiguration
parameter_list|()
block|{
return|return
operator|new
name|KafkaConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|KafkaConsumer
name|consumer
init|=
operator|new
name|KafkaConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|KafkaProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
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
DECL|method|createExecutor ()
specifier|public
name|ExecutorService
name|createExecutor
parameter_list|()
block|{
return|return
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
literal|"KafkaTopic["
operator|+
name|configuration
operator|.
name|getTopic
argument_list|()
operator|+
literal|"]"
argument_list|,
name|configuration
operator|.
name|getConsumerStreams
argument_list|()
argument_list|)
return|;
block|}
DECL|method|createKafkaExchange (MessageAndMetadata<byte[], byte[]> mm)
specifier|public
name|Exchange
name|createKafkaExchange
parameter_list|(
name|MessageAndMetadata
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|mm
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION
argument_list|,
name|mm
operator|.
name|partition
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|mm
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mm
operator|.
name|key
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
operator|new
name|String
argument_list|(
name|mm
operator|.
name|key
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setBody
argument_list|(
name|mm
operator|.
name|message
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Delegated properties from the configuration
comment|//-------------------------------------------------------------------------
DECL|method|getZookeeperConnect ()
specifier|public
name|String
name|getZookeeperConnect
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperConnect
argument_list|()
return|;
block|}
DECL|method|setZookeeperConnect (String zookeeperConnect)
specifier|public
name|void
name|setZookeeperConnect
parameter_list|(
name|String
name|zookeeperConnect
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperConnect
argument_list|(
name|zookeeperConnect
argument_list|)
expr_stmt|;
block|}
DECL|method|getZookeeperHost ()
specifier|public
name|String
name|getZookeeperHost
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperHost
argument_list|()
return|;
block|}
DECL|method|setZookeeperHost (String zookeeperHost)
specifier|public
name|void
name|setZookeeperHost
parameter_list|(
name|String
name|zookeeperHost
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperHost
argument_list|(
name|zookeeperHost
argument_list|)
expr_stmt|;
block|}
DECL|method|getZookeeperPort ()
specifier|public
name|int
name|getZookeeperPort
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperPort
argument_list|()
return|;
block|}
DECL|method|setZookeeperPort (int zookeeperPort)
specifier|public
name|void
name|setZookeeperPort
parameter_list|(
name|int
name|zookeeperPort
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperPort
argument_list|(
name|zookeeperPort
argument_list|)
expr_stmt|;
block|}
DECL|method|getGroupId ()
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getGroupId
argument_list|()
return|;
block|}
DECL|method|setGroupId (String groupId)
specifier|public
name|void
name|setGroupId
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|configuration
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
block|}
DECL|method|getPartitioner ()
specifier|public
name|String
name|getPartitioner
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getPartitioner
argument_list|()
return|;
block|}
DECL|method|setPartitioner (String partitioner)
specifier|public
name|void
name|setPartitioner
parameter_list|(
name|String
name|partitioner
parameter_list|)
block|{
name|configuration
operator|.
name|setPartitioner
argument_list|(
name|partitioner
argument_list|)
expr_stmt|;
block|}
DECL|method|getTopic ()
specifier|public
name|String
name|getTopic
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getTopic
argument_list|()
return|;
block|}
DECL|method|setTopic (String topic)
specifier|public
name|void
name|setTopic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|configuration
operator|.
name|setTopic
argument_list|(
name|topic
argument_list|)
expr_stmt|;
block|}
DECL|method|getBrokers ()
specifier|public
name|String
name|getBrokers
parameter_list|()
block|{
return|return
name|brokers
return|;
block|}
DECL|method|getConsumerStreams ()
specifier|public
name|int
name|getConsumerStreams
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConsumerStreams
argument_list|()
return|;
block|}
DECL|method|setConsumerStreams (int consumerStreams)
specifier|public
name|void
name|setConsumerStreams
parameter_list|(
name|int
name|consumerStreams
parameter_list|)
block|{
name|configuration
operator|.
name|setConsumerStreams
argument_list|(
name|consumerStreams
argument_list|)
expr_stmt|;
block|}
DECL|method|getBatchSize ()
specifier|public
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBatchSize
argument_list|()
return|;
block|}
DECL|method|setBatchSize (int batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
block|}
DECL|method|getBarrierAwaitTimeoutMs ()
specifier|public
name|int
name|getBarrierAwaitTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBarrierAwaitTimeoutMs
argument_list|()
return|;
block|}
DECL|method|setBarrierAwaitTimeoutMs (int barrierAwaitTimeoutMs)
specifier|public
name|void
name|setBarrierAwaitTimeoutMs
parameter_list|(
name|int
name|barrierAwaitTimeoutMs
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setBarrierAwaitTimeoutMs
argument_list|(
name|barrierAwaitTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getConsumersCount ()
specifier|public
name|int
name|getConsumersCount
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
operator|.
name|getConsumersCount
argument_list|()
return|;
block|}
DECL|method|setConsumersCount (int consumersCount)
specifier|public
name|void
name|setConsumersCount
parameter_list|(
name|int
name|consumersCount
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|.
name|setConsumersCount
argument_list|(
name|consumersCount
argument_list|)
expr_stmt|;
block|}
DECL|method|setConsumerTimeoutMs (int consumerTimeoutMs)
specifier|public
name|void
name|setConsumerTimeoutMs
parameter_list|(
name|int
name|consumerTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setConsumerTimeoutMs
argument_list|(
name|consumerTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setSerializerClass (String serializerClass)
specifier|public
name|void
name|setSerializerClass
parameter_list|(
name|String
name|serializerClass
parameter_list|)
block|{
name|configuration
operator|.
name|setSerializerClass
argument_list|(
name|serializerClass
argument_list|)
expr_stmt|;
block|}
DECL|method|setQueueBufferingMaxMessages (int queueBufferingMaxMessages)
specifier|public
name|void
name|setQueueBufferingMaxMessages
parameter_list|(
name|int
name|queueBufferingMaxMessages
parameter_list|)
block|{
name|configuration
operator|.
name|setQueueBufferingMaxMessages
argument_list|(
name|queueBufferingMaxMessages
argument_list|)
expr_stmt|;
block|}
DECL|method|getFetchWaitMaxMs ()
specifier|public
name|int
name|getFetchWaitMaxMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getFetchWaitMaxMs
argument_list|()
return|;
block|}
DECL|method|getZookeeperConnectionTimeoutMs ()
specifier|public
name|Integer
name|getZookeeperConnectionTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperConnectionTimeoutMs
argument_list|()
return|;
block|}
DECL|method|setZookeeperConnectionTimeoutMs (Integer zookeeperConnectionTimeoutMs)
specifier|public
name|void
name|setZookeeperConnectionTimeoutMs
parameter_list|(
name|Integer
name|zookeeperConnectionTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperConnectionTimeoutMs
argument_list|(
name|zookeeperConnectionTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setMessageSendMaxRetries (int messageSendMaxRetries)
specifier|public
name|void
name|setMessageSendMaxRetries
parameter_list|(
name|int
name|messageSendMaxRetries
parameter_list|)
block|{
name|configuration
operator|.
name|setMessageSendMaxRetries
argument_list|(
name|messageSendMaxRetries
argument_list|)
expr_stmt|;
block|}
DECL|method|getQueueBufferingMaxMs ()
specifier|public
name|int
name|getQueueBufferingMaxMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getQueueBufferingMaxMs
argument_list|()
return|;
block|}
DECL|method|setRequestRequiredAcks (short requestRequiredAcks)
specifier|public
name|void
name|setRequestRequiredAcks
parameter_list|(
name|short
name|requestRequiredAcks
parameter_list|)
block|{
name|configuration
operator|.
name|setRequestRequiredAcks
argument_list|(
name|requestRequiredAcks
argument_list|)
expr_stmt|;
block|}
DECL|method|getRebalanceBackoffMs ()
specifier|public
name|Integer
name|getRebalanceBackoffMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRebalanceBackoffMs
argument_list|()
return|;
block|}
DECL|method|setQueueEnqueueTimeoutMs (int queueEnqueueTimeoutMs)
specifier|public
name|void
name|setQueueEnqueueTimeoutMs
parameter_list|(
name|int
name|queueEnqueueTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setQueueEnqueueTimeoutMs
argument_list|(
name|queueEnqueueTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getFetchMessageMaxBytes ()
specifier|public
name|int
name|getFetchMessageMaxBytes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getFetchMessageMaxBytes
argument_list|()
return|;
block|}
DECL|method|getQueuedMaxMessages ()
specifier|public
name|int
name|getQueuedMaxMessages
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getQueuedMaxMessages
argument_list|()
return|;
block|}
DECL|method|getAutoCommitIntervalMs ()
specifier|public
name|int
name|getAutoCommitIntervalMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAutoCommitIntervalMs
argument_list|()
return|;
block|}
DECL|method|setSocketTimeoutMs (int socketTimeoutMs)
specifier|public
name|void
name|setSocketTimeoutMs
parameter_list|(
name|int
name|socketTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setSocketTimeoutMs
argument_list|(
name|socketTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setAutoCommitIntervalMs (int autoCommitIntervalMs)
specifier|public
name|void
name|setAutoCommitIntervalMs
parameter_list|(
name|int
name|autoCommitIntervalMs
parameter_list|)
block|{
name|configuration
operator|.
name|setAutoCommitIntervalMs
argument_list|(
name|autoCommitIntervalMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setRequestTimeoutMs (int requestTimeoutMs)
specifier|public
name|void
name|setRequestTimeoutMs
parameter_list|(
name|int
name|requestTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setRequestTimeoutMs
argument_list|(
name|requestTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setCompressedTopics (String compressedTopics)
specifier|public
name|void
name|setCompressedTopics
parameter_list|(
name|String
name|compressedTopics
parameter_list|)
block|{
name|configuration
operator|.
name|setCompressedTopics
argument_list|(
name|compressedTopics
argument_list|)
expr_stmt|;
block|}
DECL|method|getSocketReceiveBufferBytes ()
specifier|public
name|int
name|getSocketReceiveBufferBytes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSocketReceiveBufferBytes
argument_list|()
return|;
block|}
DECL|method|setSendBufferBytes (int sendBufferBytes)
specifier|public
name|void
name|setSendBufferBytes
parameter_list|(
name|int
name|sendBufferBytes
parameter_list|)
block|{
name|configuration
operator|.
name|setSendBufferBytes
argument_list|(
name|sendBufferBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|setFetchMessageMaxBytes (int fetchMessageMaxBytes)
specifier|public
name|void
name|setFetchMessageMaxBytes
parameter_list|(
name|int
name|fetchMessageMaxBytes
parameter_list|)
block|{
name|configuration
operator|.
name|setFetchMessageMaxBytes
argument_list|(
name|fetchMessageMaxBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|getRefreshLeaderBackoffMs ()
specifier|public
name|int
name|getRefreshLeaderBackoffMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRefreshLeaderBackoffMs
argument_list|()
return|;
block|}
DECL|method|setFetchWaitMaxMs (int fetchWaitMaxMs)
specifier|public
name|void
name|setFetchWaitMaxMs
parameter_list|(
name|int
name|fetchWaitMaxMs
parameter_list|)
block|{
name|configuration
operator|.
name|setFetchWaitMaxMs
argument_list|(
name|fetchWaitMaxMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getTopicMetadataRefreshIntervalMs ()
specifier|public
name|int
name|getTopicMetadataRefreshIntervalMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getTopicMetadataRefreshIntervalMs
argument_list|()
return|;
block|}
DECL|method|setZookeeperSessionTimeoutMs (int zookeeperSessionTimeoutMs)
specifier|public
name|void
name|setZookeeperSessionTimeoutMs
parameter_list|(
name|int
name|zookeeperSessionTimeoutMs
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperSessionTimeoutMs
argument_list|(
name|zookeeperSessionTimeoutMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getConsumerTimeoutMs ()
specifier|public
name|Integer
name|getConsumerTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConsumerTimeoutMs
argument_list|()
return|;
block|}
DECL|method|setAutoCommitEnable (boolean autoCommitEnable)
specifier|public
name|void
name|setAutoCommitEnable
parameter_list|(
name|boolean
name|autoCommitEnable
parameter_list|)
block|{
name|configuration
operator|.
name|setAutoCommitEnable
argument_list|(
name|autoCommitEnable
argument_list|)
expr_stmt|;
block|}
DECL|method|getCompressionCodec ()
specifier|public
name|String
name|getCompressionCodec
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCompressionCodec
argument_list|()
return|;
block|}
DECL|method|setProducerType (String producerType)
specifier|public
name|void
name|setProducerType
parameter_list|(
name|String
name|producerType
parameter_list|)
block|{
name|configuration
operator|.
name|setProducerType
argument_list|(
name|producerType
argument_list|)
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getClientId
argument_list|()
return|;
block|}
DECL|method|getFetchMinBytes ()
specifier|public
name|int
name|getFetchMinBytes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getFetchMinBytes
argument_list|()
return|;
block|}
DECL|method|getAutoOffsetReset ()
specifier|public
name|String
name|getAutoOffsetReset
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAutoOffsetReset
argument_list|()
return|;
block|}
DECL|method|setRefreshLeaderBackoffMs (int refreshLeaderBackoffMs)
specifier|public
name|void
name|setRefreshLeaderBackoffMs
parameter_list|(
name|int
name|refreshLeaderBackoffMs
parameter_list|)
block|{
name|configuration
operator|.
name|setRefreshLeaderBackoffMs
argument_list|(
name|refreshLeaderBackoffMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setAutoOffsetReset (String autoOffsetReset)
specifier|public
name|void
name|setAutoOffsetReset
parameter_list|(
name|String
name|autoOffsetReset
parameter_list|)
block|{
name|configuration
operator|.
name|setAutoOffsetReset
argument_list|(
name|autoOffsetReset
argument_list|)
expr_stmt|;
block|}
DECL|method|setConsumerId (String consumerId)
specifier|public
name|void
name|setConsumerId
parameter_list|(
name|String
name|consumerId
parameter_list|)
block|{
name|configuration
operator|.
name|setConsumerId
argument_list|(
name|consumerId
argument_list|)
expr_stmt|;
block|}
DECL|method|getRetryBackoffMs ()
specifier|public
name|int
name|getRetryBackoffMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRetryBackoffMs
argument_list|()
return|;
block|}
DECL|method|getRebalanceMaxRetries ()
specifier|public
name|int
name|getRebalanceMaxRetries
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRebalanceMaxRetries
argument_list|()
return|;
block|}
DECL|method|isAutoCommitEnable ()
specifier|public
name|Boolean
name|isAutoCommitEnable
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isAutoCommitEnable
argument_list|()
return|;
block|}
DECL|method|setQueueBufferingMaxMs (int queueBufferingMaxMs)
specifier|public
name|void
name|setQueueBufferingMaxMs
parameter_list|(
name|int
name|queueBufferingMaxMs
parameter_list|)
block|{
name|configuration
operator|.
name|setQueueBufferingMaxMs
argument_list|(
name|queueBufferingMaxMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setRebalanceMaxRetries (int rebalanceMaxRetries)
specifier|public
name|void
name|setRebalanceMaxRetries
parameter_list|(
name|int
name|rebalanceMaxRetries
parameter_list|)
block|{
name|configuration
operator|.
name|setRebalanceMaxRetries
argument_list|(
name|rebalanceMaxRetries
argument_list|)
expr_stmt|;
block|}
DECL|method|getZookeeperSessionTimeoutMs ()
specifier|public
name|int
name|getZookeeperSessionTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperSessionTimeoutMs
argument_list|()
return|;
block|}
DECL|method|setKeySerializerClass (String keySerializerClass)
specifier|public
name|void
name|setKeySerializerClass
parameter_list|(
name|String
name|keySerializerClass
parameter_list|)
block|{
name|configuration
operator|.
name|setKeySerializerClass
argument_list|(
name|keySerializerClass
argument_list|)
expr_stmt|;
block|}
DECL|method|setCompressionCodec (String compressionCodec)
specifier|public
name|void
name|setCompressionCodec
parameter_list|(
name|String
name|compressionCodec
parameter_list|)
block|{
name|configuration
operator|.
name|setCompressionCodec
argument_list|(
name|compressionCodec
argument_list|)
expr_stmt|;
block|}
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|configuration
operator|.
name|setClientId
argument_list|(
name|clientId
argument_list|)
expr_stmt|;
block|}
DECL|method|getSocketTimeoutMs ()
specifier|public
name|int
name|getSocketTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSocketTimeoutMs
argument_list|()
return|;
block|}
DECL|method|getCompressedTopics ()
specifier|public
name|String
name|getCompressedTopics
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCompressedTopics
argument_list|()
return|;
block|}
DECL|method|getZookeeperSyncTimeMs ()
specifier|public
name|int
name|getZookeeperSyncTimeMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getZookeeperSyncTimeMs
argument_list|()
return|;
block|}
DECL|method|setSocketReceiveBufferBytes (int socketReceiveBufferBytes)
specifier|public
name|void
name|setSocketReceiveBufferBytes
parameter_list|(
name|int
name|socketReceiveBufferBytes
parameter_list|)
block|{
name|configuration
operator|.
name|setSocketReceiveBufferBytes
argument_list|(
name|socketReceiveBufferBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|getQueueEnqueueTimeoutMs ()
specifier|public
name|int
name|getQueueEnqueueTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getQueueEnqueueTimeoutMs
argument_list|()
return|;
block|}
DECL|method|getQueueBufferingMaxMessages ()
specifier|public
name|int
name|getQueueBufferingMaxMessages
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getQueueBufferingMaxMessages
argument_list|()
return|;
block|}
DECL|method|setZookeeperSyncTimeMs (int zookeeperSyncTimeMs)
specifier|public
name|void
name|setZookeeperSyncTimeMs
parameter_list|(
name|int
name|zookeeperSyncTimeMs
parameter_list|)
block|{
name|configuration
operator|.
name|setZookeeperSyncTimeMs
argument_list|(
name|zookeeperSyncTimeMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeySerializerClass ()
specifier|public
name|String
name|getKeySerializerClass
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getKeySerializerClass
argument_list|()
return|;
block|}
DECL|method|setTopicMetadataRefreshIntervalMs (int topicMetadataRefreshIntervalMs)
specifier|public
name|void
name|setTopicMetadataRefreshIntervalMs
parameter_list|(
name|int
name|topicMetadataRefreshIntervalMs
parameter_list|)
block|{
name|configuration
operator|.
name|setTopicMetadataRefreshIntervalMs
argument_list|(
name|topicMetadataRefreshIntervalMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setBatchNumMessages (int batchNumMessages)
specifier|public
name|void
name|setBatchNumMessages
parameter_list|(
name|int
name|batchNumMessages
parameter_list|)
block|{
name|configuration
operator|.
name|setBatchNumMessages
argument_list|(
name|batchNumMessages
argument_list|)
expr_stmt|;
block|}
DECL|method|getSendBufferBytes ()
specifier|public
name|int
name|getSendBufferBytes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSendBufferBytes
argument_list|()
return|;
block|}
DECL|method|setRebalanceBackoffMs (Integer rebalanceBackoffMs)
specifier|public
name|void
name|setRebalanceBackoffMs
parameter_list|(
name|Integer
name|rebalanceBackoffMs
parameter_list|)
block|{
name|configuration
operator|.
name|setRebalanceBackoffMs
argument_list|(
name|rebalanceBackoffMs
argument_list|)
expr_stmt|;
block|}
DECL|method|setQueuedMaxMessages (int queuedMaxMessages)
specifier|public
name|void
name|setQueuedMaxMessages
parameter_list|(
name|int
name|queuedMaxMessages
parameter_list|)
block|{
name|configuration
operator|.
name|setQueuedMaxMessages
argument_list|(
name|queuedMaxMessages
argument_list|)
expr_stmt|;
block|}
DECL|method|setRetryBackoffMs (int retryBackoffMs)
specifier|public
name|void
name|setRetryBackoffMs
parameter_list|(
name|int
name|retryBackoffMs
parameter_list|)
block|{
name|configuration
operator|.
name|setRetryBackoffMs
argument_list|(
name|retryBackoffMs
argument_list|)
expr_stmt|;
block|}
DECL|method|getBatchNumMessages ()
specifier|public
name|int
name|getBatchNumMessages
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBatchNumMessages
argument_list|()
return|;
block|}
DECL|method|getRequestRequiredAcks ()
specifier|public
name|short
name|getRequestRequiredAcks
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRequestRequiredAcks
argument_list|()
return|;
block|}
DECL|method|getProducerType ()
specifier|public
name|String
name|getProducerType
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProducerType
argument_list|()
return|;
block|}
DECL|method|getConsumerId ()
specifier|public
name|String
name|getConsumerId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConsumerId
argument_list|()
return|;
block|}
DECL|method|getMessageSendMaxRetries ()
specifier|public
name|int
name|getMessageSendMaxRetries
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getMessageSendMaxRetries
argument_list|()
return|;
block|}
DECL|method|setFetchMinBytes (int fetchMinBytes)
specifier|public
name|void
name|setFetchMinBytes
parameter_list|(
name|int
name|fetchMinBytes
parameter_list|)
block|{
name|configuration
operator|.
name|setFetchMinBytes
argument_list|(
name|fetchMinBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|getSerializerClass ()
specifier|public
name|String
name|getSerializerClass
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSerializerClass
argument_list|()
return|;
block|}
DECL|method|getRequestTimeoutMs ()
specifier|public
name|int
name|getRequestTimeoutMs
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRequestTimeoutMs
argument_list|()
return|;
block|}
block|}
end_class

end_unit

