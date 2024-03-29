begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar.configuration
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|configuration
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
name|component
operator|.
name|pulsar
operator|.
name|PulsarMessageReceipt
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
name|pulsar
operator|.
name|utils
operator|.
name|consumers
operator|.
name|SubscriptionType
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
name|UriParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|CompressionType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|MessageRouter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|MessageRoutingMode
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|consumers
operator|.
name|SubscriptionType
operator|.
name|EXCLUSIVE
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|PulsarConfiguration
specifier|public
class|class
name|PulsarConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"subs"
argument_list|)
DECL|field|subscriptionName
specifier|private
name|String
name|subscriptionName
init|=
literal|"subs"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"EXCLUSIVE"
argument_list|)
DECL|field|subscriptionType
specifier|private
name|SubscriptionType
name|subscriptionType
init|=
name|EXCLUSIVE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|numberOfConsumers
specifier|private
name|int
name|numberOfConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|consumerQueueSize
specifier|private
name|int
name|consumerQueueSize
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"sole-consumer"
argument_list|)
DECL|field|consumerName
specifier|private
name|String
name|consumerName
init|=
literal|"sole-consumer"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|producerName
specifier|private
name|String
name|producerName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"cons"
argument_list|)
DECL|field|consumerNamePrefix
specifier|private
name|String
name|consumerNamePrefix
init|=
literal|"cons"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|allowManualAcknowledgement
specifier|private
name|boolean
name|allowManualAcknowledgement
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"10000"
argument_list|)
DECL|field|ackTimeoutMillis
specifier|private
name|long
name|ackTimeoutMillis
init|=
literal|10000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|ackGroupTimeMillis
specifier|private
name|long
name|ackGroupTimeMillis
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Send timeout in milliseconds"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|sendTimeoutMs
specifier|private
name|int
name|sendTimeoutMs
init|=
literal|30000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Whether to block the producing thread if pending messages queue is full or to throw a ProducerQueueIsFullError"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|blockIfQueueFull
specifier|private
name|boolean
name|blockIfQueueFull
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Size of the pending massages queue. When the queue is full, by default, any further sends will fail unless blockIfQueueFull=true"
argument_list|,
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|maxPendingMessages
specifier|private
name|int
name|maxPendingMessages
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The maximum number of pending messages for partitioned topics. The maxPendingMessages value will be reduced if "
operator|+
literal|"(number of partitions * maxPendingMessages) exceeds this value. Partitioned topics have a pending message queue for each partition."
argument_list|,
name|defaultValue
operator|=
literal|"50000"
argument_list|)
DECL|field|maxPendingMessagesAcrossPartitions
specifier|private
name|int
name|maxPendingMessagesAcrossPartitions
init|=
literal|50000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The maximum time period within which the messages sent will be batched if batchingEnabled is true."
argument_list|,
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|batchingMaxPublishDelayMicros
specifier|private
name|long
name|batchingMaxPublishDelayMicros
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toMicros
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The maximum size to batch messages."
argument_list|,
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|batchingMaxMessages
specifier|private
name|int
name|batchingMaxMessages
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Control whether automatic batching of messages is enabled for the producer."
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|batchingEnabled
specifier|private
name|boolean
name|batchingEnabled
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The first message published will have a sequence Id of initialSequenceId  1."
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|initialSequenceId
specifier|private
name|long
name|initialSequenceId
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Compression type to use"
argument_list|,
name|defaultValue
operator|=
literal|"NONE"
argument_list|)
DECL|field|compressionType
specifier|private
name|CompressionType
name|compressionType
init|=
name|CompressionType
operator|.
name|NONE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Message Routing Mode to use"
argument_list|,
name|defaultValue
operator|=
literal|"RoundRobinPartition"
argument_list|)
DECL|field|messageRoutingMode
specifier|private
name|MessageRoutingMode
name|messageRoutingMode
init|=
name|MessageRoutingMode
operator|.
name|RoundRobinPartition
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Custom Message Router to use"
argument_list|)
DECL|field|messageRouter
specifier|private
name|MessageRouter
name|messageRouter
decl_stmt|;
DECL|method|getSubscriptionName ()
specifier|public
name|String
name|getSubscriptionName
parameter_list|()
block|{
return|return
name|subscriptionName
return|;
block|}
comment|/**      * Name of the subscription to use      */
DECL|method|setSubscriptionName (String subscriptionName)
specifier|public
name|void
name|setSubscriptionName
parameter_list|(
name|String
name|subscriptionName
parameter_list|)
block|{
name|this
operator|.
name|subscriptionName
operator|=
name|subscriptionName
expr_stmt|;
block|}
DECL|method|getSubscriptionType ()
specifier|public
name|SubscriptionType
name|getSubscriptionType
parameter_list|()
block|{
return|return
name|subscriptionType
return|;
block|}
comment|/**      * Type of the subscription [EXCLUSIVE|SHARED|FAILOVER], defaults to      * EXCLUSIVE      */
DECL|method|setSubscriptionType (SubscriptionType subscriptionType)
specifier|public
name|void
name|setSubscriptionType
parameter_list|(
name|SubscriptionType
name|subscriptionType
parameter_list|)
block|{
name|this
operator|.
name|subscriptionType
operator|=
name|subscriptionType
expr_stmt|;
block|}
DECL|method|getNumberOfConsumers ()
specifier|public
name|int
name|getNumberOfConsumers
parameter_list|()
block|{
return|return
name|numberOfConsumers
return|;
block|}
comment|/**      * Number of consumers - defaults to 1      */
DECL|method|setNumberOfConsumers (int numberOfConsumers)
specifier|public
name|void
name|setNumberOfConsumers
parameter_list|(
name|int
name|numberOfConsumers
parameter_list|)
block|{
name|this
operator|.
name|numberOfConsumers
operator|=
name|numberOfConsumers
expr_stmt|;
block|}
DECL|method|getConsumerQueueSize ()
specifier|public
name|int
name|getConsumerQueueSize
parameter_list|()
block|{
return|return
name|consumerQueueSize
return|;
block|}
comment|/**      * Size of the consumer queue - defaults to 10      */
DECL|method|setConsumerQueueSize (int consumerQueueSize)
specifier|public
name|void
name|setConsumerQueueSize
parameter_list|(
name|int
name|consumerQueueSize
parameter_list|)
block|{
name|this
operator|.
name|consumerQueueSize
operator|=
name|consumerQueueSize
expr_stmt|;
block|}
DECL|method|getConsumerName ()
specifier|public
name|String
name|getConsumerName
parameter_list|()
block|{
return|return
name|consumerName
return|;
block|}
comment|/**      * Name of the consumer when subscription is EXCLUSIVE      */
DECL|method|setConsumerName (String consumerName)
specifier|public
name|void
name|setConsumerName
parameter_list|(
name|String
name|consumerName
parameter_list|)
block|{
name|this
operator|.
name|consumerName
operator|=
name|consumerName
expr_stmt|;
block|}
DECL|method|getProducerName ()
specifier|public
name|String
name|getProducerName
parameter_list|()
block|{
return|return
name|producerName
return|;
block|}
comment|/**      * Name of the producer. If unset, lets Pulsar select a unique identifier.      */
DECL|method|setProducerName (String producerName)
specifier|public
name|void
name|setProducerName
parameter_list|(
name|String
name|producerName
parameter_list|)
block|{
name|this
operator|.
name|producerName
operator|=
name|producerName
expr_stmt|;
block|}
DECL|method|getConsumerNamePrefix ()
specifier|public
name|String
name|getConsumerNamePrefix
parameter_list|()
block|{
return|return
name|consumerNamePrefix
return|;
block|}
comment|/**      * Prefix to add to consumer names when a SHARED or FAILOVER subscription is      * used      */
DECL|method|setConsumerNamePrefix (String consumerNamePrefix)
specifier|public
name|void
name|setConsumerNamePrefix
parameter_list|(
name|String
name|consumerNamePrefix
parameter_list|)
block|{
name|this
operator|.
name|consumerNamePrefix
operator|=
name|consumerNamePrefix
expr_stmt|;
block|}
DECL|method|isAllowManualAcknowledgement ()
specifier|public
name|boolean
name|isAllowManualAcknowledgement
parameter_list|()
block|{
return|return
name|allowManualAcknowledgement
return|;
block|}
comment|/**      * Whether to allow manual message acknowledgements.      *<p/>      * If this option is enabled, then messages are not immediately acknowledged      * after being consumed. Instead, an instance of      * {@link PulsarMessageReceipt} is stored as a header on the      * {@link org.apache.camel.Exchange}. Messages can then be acknowledged      * using {@link PulsarMessageReceipt} at any time before the ackTimeout      * occurs.      */
DECL|method|setAllowManualAcknowledgement (boolean allowManualAcknowledgement)
specifier|public
name|void
name|setAllowManualAcknowledgement
parameter_list|(
name|boolean
name|allowManualAcknowledgement
parameter_list|)
block|{
name|this
operator|.
name|allowManualAcknowledgement
operator|=
name|allowManualAcknowledgement
expr_stmt|;
block|}
DECL|method|getAckTimeoutMillis ()
specifier|public
name|long
name|getAckTimeoutMillis
parameter_list|()
block|{
return|return
name|ackTimeoutMillis
return|;
block|}
comment|/**      * Timeout for unacknowledged messages in milliseconds - defaults to 10000      */
DECL|method|setAckTimeoutMillis (long ackTimeoutMillis)
specifier|public
name|void
name|setAckTimeoutMillis
parameter_list|(
name|long
name|ackTimeoutMillis
parameter_list|)
block|{
name|this
operator|.
name|ackTimeoutMillis
operator|=
name|ackTimeoutMillis
expr_stmt|;
block|}
DECL|method|getAckGroupTimeMillis ()
specifier|public
name|long
name|getAckGroupTimeMillis
parameter_list|()
block|{
return|return
name|ackGroupTimeMillis
return|;
block|}
comment|/**      * Group the consumer acknowledgments for the specified time in milliseconds      * - defaults to 100      */
DECL|method|setAckGroupTimeMillis (long ackGroupTimeMillis)
specifier|public
name|void
name|setAckGroupTimeMillis
parameter_list|(
name|long
name|ackGroupTimeMillis
parameter_list|)
block|{
name|this
operator|.
name|ackGroupTimeMillis
operator|=
name|ackGroupTimeMillis
expr_stmt|;
block|}
comment|/**      * Send timeout in milliseconds. Defaults to 30,000ms (30 seconds)      */
DECL|method|setSendTimeoutMs (int sendTimeoutMs)
specifier|public
name|void
name|setSendTimeoutMs
parameter_list|(
name|int
name|sendTimeoutMs
parameter_list|)
block|{
name|this
operator|.
name|sendTimeoutMs
operator|=
name|sendTimeoutMs
expr_stmt|;
block|}
DECL|method|getSendTimeoutMs ()
specifier|public
name|int
name|getSendTimeoutMs
parameter_list|()
block|{
return|return
name|sendTimeoutMs
return|;
block|}
comment|/**      * Set whether the send and asyncSend operations should block when the      * outgoing message queue is full. If set to false, send operations will      * immediately fail with ProducerQueueIsFullError when there is no space      * left in the pending queue. Default is false.      */
DECL|method|setBlockIfQueueFull (boolean blockIfQueueFull)
specifier|public
name|void
name|setBlockIfQueueFull
parameter_list|(
name|boolean
name|blockIfQueueFull
parameter_list|)
block|{
name|this
operator|.
name|blockIfQueueFull
operator|=
name|blockIfQueueFull
expr_stmt|;
block|}
DECL|method|isBlockIfQueueFull ()
specifier|public
name|boolean
name|isBlockIfQueueFull
parameter_list|()
block|{
return|return
name|blockIfQueueFull
return|;
block|}
comment|/**      * Set the max size of the queue holding the messages pending to receive an      * acknowledgment from the broker. Default is 1000.      */
DECL|method|setMaxPendingMessages (int maxPendingMessages)
specifier|public
name|void
name|setMaxPendingMessages
parameter_list|(
name|int
name|maxPendingMessages
parameter_list|)
block|{
name|this
operator|.
name|maxPendingMessages
operator|=
name|maxPendingMessages
expr_stmt|;
block|}
DECL|method|getMaxPendingMessages ()
specifier|public
name|int
name|getMaxPendingMessages
parameter_list|()
block|{
return|return
name|maxPendingMessages
return|;
block|}
comment|/**      * Set the number of max pending messages across all the partitions. Default      * is 50000.      */
DECL|method|setMaxPendingMessagesAcrossPartitions (int maxPendingMessagesAcrossPartitions)
specifier|public
name|void
name|setMaxPendingMessagesAcrossPartitions
parameter_list|(
name|int
name|maxPendingMessagesAcrossPartitions
parameter_list|)
block|{
name|this
operator|.
name|maxPendingMessagesAcrossPartitions
operator|=
name|maxPendingMessagesAcrossPartitions
expr_stmt|;
block|}
DECL|method|getMaxPendingMessagesAcrossPartitions ()
specifier|public
name|int
name|getMaxPendingMessagesAcrossPartitions
parameter_list|()
block|{
return|return
name|maxPendingMessagesAcrossPartitions
return|;
block|}
comment|/**      * Set the time period within which the messages sent will be batched if      * batch messages are enabled. If set to a non zero value, messages will be      * queued until either:      *<ul>      *<li>this time interval expires</li>      *<li>the max number of messages in a batch is reached      *</ul>      * Default is 1ms.      */
DECL|method|setBatchingMaxPublishDelayMicros (long batchingMaxPublishDelayMicros)
specifier|public
name|void
name|setBatchingMaxPublishDelayMicros
parameter_list|(
name|long
name|batchingMaxPublishDelayMicros
parameter_list|)
block|{
name|this
operator|.
name|batchingMaxPublishDelayMicros
operator|=
name|batchingMaxPublishDelayMicros
expr_stmt|;
block|}
DECL|method|getBatchingMaxPublishDelayMicros ()
specifier|public
name|long
name|getBatchingMaxPublishDelayMicros
parameter_list|()
block|{
return|return
name|batchingMaxPublishDelayMicros
return|;
block|}
comment|/**      * Set the maximum number of messages permitted in a batch. Default 1,000.      */
DECL|method|setBatchingMaxMessages (int batchingMaxMessages)
specifier|public
name|void
name|setBatchingMaxMessages
parameter_list|(
name|int
name|batchingMaxMessages
parameter_list|)
block|{
name|this
operator|.
name|batchingMaxMessages
operator|=
name|batchingMaxMessages
expr_stmt|;
block|}
DECL|method|getBatchingMaxMessages ()
specifier|public
name|int
name|getBatchingMaxMessages
parameter_list|()
block|{
return|return
name|batchingMaxMessages
return|;
block|}
comment|/**      * Control whether automatic batching of messages is enabled for the      * producer. Default is true.      */
DECL|method|setBatchingEnabled (boolean batchingEnabled)
specifier|public
name|void
name|setBatchingEnabled
parameter_list|(
name|boolean
name|batchingEnabled
parameter_list|)
block|{
name|this
operator|.
name|batchingEnabled
operator|=
name|batchingEnabled
expr_stmt|;
block|}
DECL|method|isBatchingEnabled ()
specifier|public
name|boolean
name|isBatchingEnabled
parameter_list|()
block|{
return|return
name|batchingEnabled
return|;
block|}
comment|/**      * Set the baseline for the sequence ids for messages published by the      * producer. First message will be using (initialSequenceId 1) as its      * sequence id and subsequent messages will be assigned incremental sequence      * ids, if not otherwise specified.      */
DECL|method|setInitialSequenceId (long initialSequenceId)
specifier|public
name|void
name|setInitialSequenceId
parameter_list|(
name|long
name|initialSequenceId
parameter_list|)
block|{
name|this
operator|.
name|initialSequenceId
operator|=
name|initialSequenceId
expr_stmt|;
block|}
DECL|method|getInitialSequenceId ()
specifier|public
name|long
name|getInitialSequenceId
parameter_list|()
block|{
return|return
name|initialSequenceId
return|;
block|}
comment|/**      * Set the compression type for the producer.      */
DECL|method|setCompressionType (String compressionType)
specifier|public
name|void
name|setCompressionType
parameter_list|(
name|String
name|compressionType
parameter_list|)
block|{
name|this
operator|.
name|compressionType
operator|=
name|CompressionType
operator|.
name|valueOf
argument_list|(
name|compressionType
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set the compression type for the producer.      */
DECL|method|setCompressionType (CompressionType compressionType)
specifier|public
name|void
name|setCompressionType
parameter_list|(
name|CompressionType
name|compressionType
parameter_list|)
block|{
name|this
operator|.
name|compressionType
operator|=
name|compressionType
expr_stmt|;
block|}
DECL|method|getCompressionType ()
specifier|public
name|CompressionType
name|getCompressionType
parameter_list|()
block|{
return|return
name|compressionType
return|;
block|}
comment|/**      * Set the message routing mode for the producer.      */
DECL|method|getMessageRoutingMode ()
specifier|public
name|MessageRoutingMode
name|getMessageRoutingMode
parameter_list|()
block|{
return|return
name|messageRoutingMode
return|;
block|}
DECL|method|setMessageRoutingMode (MessageRoutingMode messageRoutingMode)
specifier|public
name|void
name|setMessageRoutingMode
parameter_list|(
name|MessageRoutingMode
name|messageRoutingMode
parameter_list|)
block|{
name|this
operator|.
name|messageRoutingMode
operator|=
name|messageRoutingMode
expr_stmt|;
block|}
comment|/**      * Set a custom Message Router.      */
DECL|method|getMessageRouter ()
specifier|public
name|MessageRouter
name|getMessageRouter
parameter_list|()
block|{
return|return
name|messageRouter
return|;
block|}
DECL|method|setMessageRouter (MessageRouter messageRouter)
specifier|public
name|void
name|setMessageRouter
parameter_list|(
name|MessageRouter
name|messageRouter
parameter_list|)
block|{
name|this
operator|.
name|messageRouter
operator|=
name|messageRouter
expr_stmt|;
block|}
block|}
end_class

end_unit

