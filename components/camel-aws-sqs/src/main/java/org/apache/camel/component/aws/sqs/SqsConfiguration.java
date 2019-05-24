begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|AmazonSQS
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
name|RuntimeCamelException
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

begin_class
annotation|@
name|UriParams
DECL|class|SqsConfiguration
specifier|public
class|class
name|SqsConfiguration
implements|implements
name|Cloneable
block|{
comment|// common properties
DECL|field|queueName
specifier|private
name|String
name|queueName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSQSClient
specifier|private
name|AmazonSQS
name|amazonSQSClient
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"amazonaws.com"
argument_list|)
DECL|field|amazonAWSHost
specifier|private
name|String
name|amazonAWSHost
init|=
literal|"amazonaws.com"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|secret
operator|=
literal|true
argument_list|)
DECL|field|queueOwnerAWSAccountId
specifier|private
name|String
name|queueOwnerAWSAccountId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
DECL|field|autoCreateQueue
specifier|private
name|boolean
name|autoCreateQueue
init|=
literal|true
decl_stmt|;
comment|// consumer properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|deleteAfterRead
specifier|private
name|boolean
name|deleteAfterRead
init|=
literal|true
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
literal|"true"
argument_list|)
DECL|field|deleteIfFiltered
specifier|private
name|boolean
name|deleteIfFiltered
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|visibilityTimeout
specifier|private
name|Integer
name|visibilityTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|attributeNames
specifier|private
name|String
name|attributeNames
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|messageAttributeNames
specifier|private
name|String
name|messageAttributeNames
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|waitTimeSeconds
specifier|private
name|Integer
name|waitTimeSeconds
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|defaultVisibilityTimeout
specifier|private
name|Integer
name|defaultVisibilityTimeout
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|extendMessageVisibility
specifier|private
name|boolean
name|extendMessageVisibility
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|kmsMasterKeyId
specifier|private
name|String
name|kmsMasterKeyId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|kmsDataKeyReusePeriodSeconds
specifier|private
name|Integer
name|kmsDataKeyReusePeriodSeconds
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|serverSideEncryptionEnabled
specifier|private
name|boolean
name|serverSideEncryptionEnabled
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
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|queueUrl
specifier|private
name|String
name|queueUrl
decl_stmt|;
comment|// producer properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|delaySeconds
specifier|private
name|Integer
name|delaySeconds
decl_stmt|;
comment|// producer properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|delayQueue
specifier|private
name|boolean
name|delayQueue
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"useConstant,useExchangeId,usePropertyValue"
argument_list|)
DECL|field|messageGroupIdStrategy
specifier|private
name|MessageGroupIdStrategy
name|messageGroupIdStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"useExchangeId"
argument_list|,
name|enums
operator|=
literal|"useExchangeId,useContentBasedDeduplication"
argument_list|)
DECL|field|messageDeduplicationIdStrategy
specifier|private
name|MessageDeduplicationIdStrategy
name|messageDeduplicationIdStrategy
init|=
operator|new
name|ExchangeIdMessageDeduplicationIdStrategy
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|operation
specifier|private
name|SqsOperations
name|operation
decl_stmt|;
comment|// queue properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"queue"
argument_list|)
DECL|field|maximumMessageSize
specifier|private
name|Integer
name|maximumMessageSize
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"queue"
argument_list|)
DECL|field|messageRetentionPeriod
specifier|private
name|Integer
name|messageRetentionPeriod
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"queue"
argument_list|)
DECL|field|receiveMessageWaitTimeSeconds
specifier|private
name|Integer
name|receiveMessageWaitTimeSeconds
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"queue"
argument_list|)
DECL|field|policy
specifier|private
name|String
name|policy
decl_stmt|;
comment|// dead letter queue properties
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"queue"
argument_list|)
DECL|field|redrivePolicy
specifier|private
name|String
name|redrivePolicy
decl_stmt|;
comment|/**      * Whether or not the queue is a FIFO queue      */
DECL|method|isFifoQueue ()
name|boolean
name|isFifoQueue
parameter_list|()
block|{
comment|// AWS docs suggest this is valid derivation.
comment|// FIFO queue names must end with .fifo, and standard queues cannot
if|if
condition|(
name|queueName
operator|.
name|endsWith
argument_list|(
literal|".fifo"
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|getAmazonAWSHost ()
specifier|public
name|String
name|getAmazonAWSHost
parameter_list|()
block|{
return|return
name|amazonAWSHost
return|;
block|}
comment|/**      * The hostname of the Amazon AWS cloud.      */
DECL|method|setAmazonAWSHost (String amazonAWSHost)
specifier|public
name|void
name|setAmazonAWSHost
parameter_list|(
name|String
name|amazonAWSHost
parameter_list|)
block|{
name|this
operator|.
name|amazonAWSHost
operator|=
name|amazonAWSHost
expr_stmt|;
block|}
DECL|method|getQueueName ()
specifier|public
name|String
name|getQueueName
parameter_list|()
block|{
return|return
name|queueName
return|;
block|}
comment|/**      * Name of queue. The queue will be created if they don't already exists.      */
DECL|method|setQueueName (String queueName)
specifier|public
name|void
name|setQueueName
parameter_list|(
name|String
name|queueName
parameter_list|)
block|{
name|this
operator|.
name|queueName
operator|=
name|queueName
expr_stmt|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|isDeleteAfterRead ()
specifier|public
name|boolean
name|isDeleteAfterRead
parameter_list|()
block|{
return|return
name|deleteAfterRead
return|;
block|}
comment|/**      * Delete message from SQS after it has been read      */
DECL|method|setDeleteAfterRead (boolean deleteAfterRead)
specifier|public
name|void
name|setDeleteAfterRead
parameter_list|(
name|boolean
name|deleteAfterRead
parameter_list|)
block|{
name|this
operator|.
name|deleteAfterRead
operator|=
name|deleteAfterRead
expr_stmt|;
block|}
DECL|method|getAmazonSQSClient ()
specifier|public
name|AmazonSQS
name|getAmazonSQSClient
parameter_list|()
block|{
return|return
name|amazonSQSClient
return|;
block|}
comment|/**      * To use the AmazonSQS as client      */
DECL|method|setAmazonSQSClient (AmazonSQS amazonSQSClient)
specifier|public
name|void
name|setAmazonSQSClient
parameter_list|(
name|AmazonSQS
name|amazonSQSClient
parameter_list|)
block|{
name|this
operator|.
name|amazonSQSClient
operator|=
name|amazonSQSClient
expr_stmt|;
block|}
DECL|method|getVisibilityTimeout ()
specifier|public
name|Integer
name|getVisibilityTimeout
parameter_list|()
block|{
return|return
name|visibilityTimeout
return|;
block|}
comment|/**      * The duration (in seconds) that the received messages are hidden from      * subsequent retrieve requests after being retrieved by a ReceiveMessage      * request to set in the      * com.amazonaws.services.sqs.model.SetQueueAttributesRequest. This only      * make sense if its different from defaultVisibilityTimeout. It changes the      * queue visibility timeout attribute permanently.      */
DECL|method|setVisibilityTimeout (Integer visibilityTimeout)
specifier|public
name|void
name|setVisibilityTimeout
parameter_list|(
name|Integer
name|visibilityTimeout
parameter_list|)
block|{
name|this
operator|.
name|visibilityTimeout
operator|=
name|visibilityTimeout
expr_stmt|;
block|}
DECL|method|getAttributeNames ()
specifier|public
name|String
name|getAttributeNames
parameter_list|()
block|{
return|return
name|attributeNames
return|;
block|}
comment|/**      * A list of attribute names to receive when consuming. Multiple names can      * be separated by comma.      */
DECL|method|setAttributeNames (String attributeNames)
specifier|public
name|void
name|setAttributeNames
parameter_list|(
name|String
name|attributeNames
parameter_list|)
block|{
name|this
operator|.
name|attributeNames
operator|=
name|attributeNames
expr_stmt|;
block|}
DECL|method|getMessageAttributeNames ()
specifier|public
name|String
name|getMessageAttributeNames
parameter_list|()
block|{
return|return
name|messageAttributeNames
return|;
block|}
comment|/**      * A list of message attribute names to receive when consuming. Multiple      * names can be separated by comma.      */
DECL|method|setMessageAttributeNames (String messageAttributeNames)
specifier|public
name|void
name|setMessageAttributeNames
parameter_list|(
name|String
name|messageAttributeNames
parameter_list|)
block|{
name|this
operator|.
name|messageAttributeNames
operator|=
name|messageAttributeNames
expr_stmt|;
block|}
DECL|method|getDefaultVisibilityTimeout ()
specifier|public
name|Integer
name|getDefaultVisibilityTimeout
parameter_list|()
block|{
return|return
name|defaultVisibilityTimeout
return|;
block|}
comment|/**      * The default visibility timeout (in seconds)      */
DECL|method|setDefaultVisibilityTimeout (Integer defaultVisibilityTimeout)
specifier|public
name|void
name|setDefaultVisibilityTimeout
parameter_list|(
name|Integer
name|defaultVisibilityTimeout
parameter_list|)
block|{
name|this
operator|.
name|defaultVisibilityTimeout
operator|=
name|defaultVisibilityTimeout
expr_stmt|;
block|}
DECL|method|getDelaySeconds ()
specifier|public
name|Integer
name|getDelaySeconds
parameter_list|()
block|{
return|return
name|delaySeconds
return|;
block|}
comment|/**      * Delay sending messages for a number of seconds.      */
DECL|method|setDelaySeconds (Integer delaySeconds)
specifier|public
name|void
name|setDelaySeconds
parameter_list|(
name|Integer
name|delaySeconds
parameter_list|)
block|{
name|this
operator|.
name|delaySeconds
operator|=
name|delaySeconds
expr_stmt|;
block|}
DECL|method|isDelayQueue ()
specifier|public
name|boolean
name|isDelayQueue
parameter_list|()
block|{
return|return
name|delayQueue
return|;
block|}
comment|/**      * Define if you want to apply delaySeconds option to the queue or on single      * messages      */
DECL|method|setDelayQueue (boolean delayQueue)
specifier|public
name|void
name|setDelayQueue
parameter_list|(
name|boolean
name|delayQueue
parameter_list|)
block|{
name|this
operator|.
name|delayQueue
operator|=
name|delayQueue
expr_stmt|;
block|}
DECL|method|getMaximumMessageSize ()
specifier|public
name|Integer
name|getMaximumMessageSize
parameter_list|()
block|{
return|return
name|maximumMessageSize
return|;
block|}
comment|/**      * The maximumMessageSize (in bytes) an SQS message can contain for this      * queue.      */
DECL|method|setMaximumMessageSize (Integer maximumMessageSize)
specifier|public
name|void
name|setMaximumMessageSize
parameter_list|(
name|Integer
name|maximumMessageSize
parameter_list|)
block|{
name|this
operator|.
name|maximumMessageSize
operator|=
name|maximumMessageSize
expr_stmt|;
block|}
DECL|method|getMessageRetentionPeriod ()
specifier|public
name|Integer
name|getMessageRetentionPeriod
parameter_list|()
block|{
return|return
name|messageRetentionPeriod
return|;
block|}
comment|/**      * The messageRetentionPeriod (in seconds) a message will be retained by SQS      * for this queue.      */
DECL|method|setMessageRetentionPeriod (Integer messageRetentionPeriod)
specifier|public
name|void
name|setMessageRetentionPeriod
parameter_list|(
name|Integer
name|messageRetentionPeriod
parameter_list|)
block|{
name|this
operator|.
name|messageRetentionPeriod
operator|=
name|messageRetentionPeriod
expr_stmt|;
block|}
DECL|method|getPolicy ()
specifier|public
name|String
name|getPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
comment|/**      * The policy for this queue      */
DECL|method|setPolicy (String policy)
specifier|public
name|void
name|setPolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
block|}
DECL|method|getRedrivePolicy ()
specifier|public
name|String
name|getRedrivePolicy
parameter_list|()
block|{
return|return
name|redrivePolicy
return|;
block|}
comment|/**      * Specify the policy that send message to DeadLetter queue. See detail at      * Amazon docs.      */
DECL|method|setRedrivePolicy (String redrivePolicy)
specifier|public
name|void
name|setRedrivePolicy
parameter_list|(
name|String
name|redrivePolicy
parameter_list|)
block|{
name|this
operator|.
name|redrivePolicy
operator|=
name|redrivePolicy
expr_stmt|;
block|}
DECL|method|isExtendMessageVisibility ()
specifier|public
name|boolean
name|isExtendMessageVisibility
parameter_list|()
block|{
return|return
name|this
operator|.
name|extendMessageVisibility
return|;
block|}
comment|/**      * If enabled then a scheduled background task will keep extending the      * message visibility on SQS. This is needed if it takes a long time to      * process the message. If set to true defaultVisibilityTimeout must be set.      * See details at Amazon docs.      */
DECL|method|setExtendMessageVisibility (boolean extendMessageVisibility)
specifier|public
name|void
name|setExtendMessageVisibility
parameter_list|(
name|boolean
name|extendMessageVisibility
parameter_list|)
block|{
name|this
operator|.
name|extendMessageVisibility
operator|=
name|extendMessageVisibility
expr_stmt|;
block|}
DECL|method|getReceiveMessageWaitTimeSeconds ()
specifier|public
name|Integer
name|getReceiveMessageWaitTimeSeconds
parameter_list|()
block|{
return|return
name|receiveMessageWaitTimeSeconds
return|;
block|}
comment|/**      * If you do not specify WaitTimeSeconds in the request, the queue attribute      * ReceiveMessageWaitTimeSeconds is used to determine how long to wait.      */
DECL|method|setReceiveMessageWaitTimeSeconds (Integer receiveMessageWaitTimeSeconds)
specifier|public
name|void
name|setReceiveMessageWaitTimeSeconds
parameter_list|(
name|Integer
name|receiveMessageWaitTimeSeconds
parameter_list|)
block|{
name|this
operator|.
name|receiveMessageWaitTimeSeconds
operator|=
name|receiveMessageWaitTimeSeconds
expr_stmt|;
block|}
DECL|method|getWaitTimeSeconds ()
specifier|public
name|Integer
name|getWaitTimeSeconds
parameter_list|()
block|{
return|return
name|waitTimeSeconds
return|;
block|}
comment|/**      * Duration in seconds (0 to 20) that the ReceiveMessage action call will      * wait until a message is in the queue to include in the response.      */
DECL|method|setWaitTimeSeconds (Integer waitTimeSeconds)
specifier|public
name|void
name|setWaitTimeSeconds
parameter_list|(
name|Integer
name|waitTimeSeconds
parameter_list|)
block|{
name|this
operator|.
name|waitTimeSeconds
operator|=
name|waitTimeSeconds
expr_stmt|;
block|}
DECL|method|getQueueOwnerAWSAccountId ()
specifier|public
name|String
name|getQueueOwnerAWSAccountId
parameter_list|()
block|{
return|return
name|queueOwnerAWSAccountId
return|;
block|}
comment|/**      * Specify the queue owner aws account id when you need to connect the queue      * with different account owner.      */
DECL|method|setQueueOwnerAWSAccountId (String queueOwnerAWSAccountId)
specifier|public
name|void
name|setQueueOwnerAWSAccountId
parameter_list|(
name|String
name|queueOwnerAWSAccountId
parameter_list|)
block|{
name|this
operator|.
name|queueOwnerAWSAccountId
operator|=
name|queueOwnerAWSAccountId
expr_stmt|;
block|}
DECL|method|isDeleteIfFiltered ()
specifier|public
name|boolean
name|isDeleteIfFiltered
parameter_list|()
block|{
return|return
name|deleteIfFiltered
return|;
block|}
comment|/**      * Whether or not to send the DeleteMessage to the SQS queue if an exchange      * fails to get through a filter. If 'false' and exchange does not make it      * through a Camel filter upstream in the route, then don't send      * DeleteMessage.      */
DECL|method|setDeleteIfFiltered (boolean deleteIfFiltered)
specifier|public
name|void
name|setDeleteIfFiltered
parameter_list|(
name|boolean
name|deleteIfFiltered
parameter_list|)
block|{
name|this
operator|.
name|deleteIfFiltered
operator|=
name|deleteIfFiltered
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
comment|/**      * Specify the queue region which could be used with queueOwnerAWSAccountId      * to build the service URL.      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
comment|/**      * Allows you to use multiple threads to poll the sqs queue to increase      * throughput      */
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getQueueUrl ()
specifier|public
name|String
name|getQueueUrl
parameter_list|()
block|{
return|return
name|queueUrl
return|;
block|}
comment|/**      * To define the queueUrl explicitly. All other parameters, which would      * influence the queueUrl, are ignored. This parameter is intended to be      * used, to connect to a mock implementation of SQS, for testing purposes.      */
DECL|method|setQueueUrl (String queueUrl)
specifier|public
name|void
name|setQueueUrl
parameter_list|(
name|String
name|queueUrl
parameter_list|)
block|{
name|this
operator|.
name|queueUrl
operator|=
name|queueUrl
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
comment|/**      * To define a proxy host when instantiating the SQS client      */
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
comment|/**      * To define a proxy port when instantiating the SQS client      */
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
DECL|method|getKmsMasterKeyId ()
specifier|public
name|String
name|getKmsMasterKeyId
parameter_list|()
block|{
return|return
name|kmsMasterKeyId
return|;
block|}
comment|/**      * The ID of an AWS-managed customer master key (CMK) for Amazon SQS or a      * custom CMK.      */
DECL|method|setKmsMasterKeyId (String kmsMasterKeyId)
specifier|public
name|void
name|setKmsMasterKeyId
parameter_list|(
name|String
name|kmsMasterKeyId
parameter_list|)
block|{
name|this
operator|.
name|kmsMasterKeyId
operator|=
name|kmsMasterKeyId
expr_stmt|;
block|}
DECL|method|getKmsDataKeyReusePeriodSeconds ()
specifier|public
name|Integer
name|getKmsDataKeyReusePeriodSeconds
parameter_list|()
block|{
return|return
name|kmsDataKeyReusePeriodSeconds
return|;
block|}
comment|/**      * The length of time, in seconds, for which Amazon SQS can reuse a data key      * to encrypt or decrypt messages before calling AWS KMS again. An integer      * representing seconds, between 60 seconds (1 minute) and 86,400 seconds      * (24 hours). Default: 300 (5 minutes).      */
DECL|method|setKmsDataKeyReusePeriodSeconds (Integer kmsDataKeyReusePeriodSeconds)
specifier|public
name|void
name|setKmsDataKeyReusePeriodSeconds
parameter_list|(
name|Integer
name|kmsDataKeyReusePeriodSeconds
parameter_list|)
block|{
name|this
operator|.
name|kmsDataKeyReusePeriodSeconds
operator|=
name|kmsDataKeyReusePeriodSeconds
expr_stmt|;
block|}
DECL|method|isServerSideEncryptionEnabled ()
specifier|public
name|boolean
name|isServerSideEncryptionEnabled
parameter_list|()
block|{
return|return
name|serverSideEncryptionEnabled
return|;
block|}
comment|/**      * Define if Server Side Encryption is enabled or not on the queue      */
DECL|method|setServerSideEncryptionEnabled (boolean serverSideEncryptionEnabled)
specifier|public
name|void
name|setServerSideEncryptionEnabled
parameter_list|(
name|boolean
name|serverSideEncryptionEnabled
parameter_list|)
block|{
name|this
operator|.
name|serverSideEncryptionEnabled
operator|=
name|serverSideEncryptionEnabled
expr_stmt|;
block|}
comment|/**      * Only for FIFO queues. Strategy for setting the messageGroupId on the      * message. Can be one of the following options: *useConstant*,      * *useExchangeId*, *usePropertyValue*. For the *usePropertyValue* option,      * the value of property "CamelAwsMessageGroupId" will be used.      */
DECL|method|setMessageGroupIdStrategy (String strategy)
specifier|public
name|void
name|setMessageGroupIdStrategy
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
if|if
condition|(
literal|"useConstant"
operator|.
name|equalsIgnoreCase
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|messageGroupIdStrategy
operator|=
operator|new
name|ConstantMessageGroupIdStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"useExchangeId"
operator|.
name|equalsIgnoreCase
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|messageGroupIdStrategy
operator|=
operator|new
name|ExchangeIdMessageGroupIdStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"usePropertyValue"
operator|.
name|equalsIgnoreCase
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|messageGroupIdStrategy
operator|=
operator|new
name|PropertyValueMessageGroupIdStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognised MessageGroupIdStrategy: "
operator|+
name|strategy
argument_list|)
throw|;
block|}
block|}
DECL|method|getMessageGroupIdStrategy ()
specifier|public
name|MessageGroupIdStrategy
name|getMessageGroupIdStrategy
parameter_list|()
block|{
return|return
name|messageGroupIdStrategy
return|;
block|}
DECL|method|getMessageDeduplicationIdStrategy ()
specifier|public
name|MessageDeduplicationIdStrategy
name|getMessageDeduplicationIdStrategy
parameter_list|()
block|{
return|return
name|messageDeduplicationIdStrategy
return|;
block|}
comment|/**      * Only for FIFO queues. Strategy for setting the messageDeduplicationId on      * the message. Can be one of the following options: *useExchangeId*,      * *useContentBasedDeduplication*. For the *useContentBasedDeduplication*      * option, no messageDeduplicationId will be set on the message.      */
DECL|method|setMessageDeduplicationIdStrategy (String strategy)
specifier|public
name|void
name|setMessageDeduplicationIdStrategy
parameter_list|(
name|String
name|strategy
parameter_list|)
block|{
if|if
condition|(
literal|"useExchangeId"
operator|.
name|equalsIgnoreCase
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|messageDeduplicationIdStrategy
operator|=
operator|new
name|ExchangeIdMessageDeduplicationIdStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"useContentBasedDeduplication"
operator|.
name|equalsIgnoreCase
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
name|messageDeduplicationIdStrategy
operator|=
operator|new
name|NullMessageDeduplicationIdStrategy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unrecognised MessageDeduplicationIdStrategy: "
operator|+
name|strategy
argument_list|)
throw|;
block|}
block|}
DECL|method|getOperation ()
specifier|public
name|SqsOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to do in case the user don't want to send only a message      */
DECL|method|setOperation (SqsOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|SqsOperations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|isAutoCreateQueue ()
specifier|public
name|boolean
name|isAutoCreateQueue
parameter_list|()
block|{
return|return
name|autoCreateQueue
return|;
block|}
comment|/**      * Setting the autocreation of the queue      */
DECL|method|setAutoCreateQueue (boolean autoCreateQueue)
specifier|public
name|void
name|setAutoCreateQueue
parameter_list|(
name|boolean
name|autoCreateQueue
parameter_list|)
block|{
name|this
operator|.
name|autoCreateQueue
operator|=
name|autoCreateQueue
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|SqsConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|SqsConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

