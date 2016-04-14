begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSQSEndpoint
specifier|private
name|String
name|amazonSQSEndpoint
decl_stmt|;
annotation|@
name|UriParam
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
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
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
comment|// queue properties
annotation|@
name|UriParam
DECL|field|maximumMessageSize
specifier|private
name|Integer
name|maximumMessageSize
decl_stmt|;
annotation|@
name|UriParam
DECL|field|messageRetentionPeriod
specifier|private
name|Integer
name|messageRetentionPeriod
decl_stmt|;
annotation|@
name|UriParam
DECL|field|receiveMessageWaitTimeSeconds
specifier|private
name|Integer
name|receiveMessageWaitTimeSeconds
decl_stmt|;
annotation|@
name|UriParam
DECL|field|policy
specifier|private
name|String
name|policy
decl_stmt|;
comment|// dead letter queue properties
annotation|@
name|UriParam
DECL|field|redrivePolicy
specifier|private
name|String
name|redrivePolicy
decl_stmt|;
comment|/**      * The region with which the AWS-SQS client wants to work with.      * Only works if Camel creates the AWS-SQS client, i.e., if you explicitly set amazonSQSClient,      * then this setting will have no effect. You would have to set it on the client you create directly      */
DECL|method|setAmazonSQSEndpoint (String amazonSQSEndpoint)
specifier|public
name|void
name|setAmazonSQSEndpoint
parameter_list|(
name|String
name|amazonSQSEndpoint
parameter_list|)
block|{
name|this
operator|.
name|amazonSQSEndpoint
operator|=
name|amazonSQSEndpoint
expr_stmt|;
block|}
DECL|method|getAmazonSQSEndpoint ()
specifier|public
name|String
name|getAmazonSQSEndpoint
parameter_list|()
block|{
return|return
name|amazonSQSEndpoint
return|;
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
comment|/**      * The duration (in seconds) that the received messages are hidden from subsequent retrieve requests after being retrieved      * by a ReceiveMessage request to set in the com.amazonaws.services.sqs.model.SetQueueAttributesRequest.      * This only make sense if its different from defaultVisibilityTimeout.      * It changes the queue visibility timeout attribute permanently.      */
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
comment|/**      * A list of attribute names to receive when consuming.  Multiple names can be separated by comma.      */
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
comment|/**      * A list of message attribute names to receive when consuming. Multiple names can be separated by comma.      */
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
comment|/**      * The maximumMessageSize (in bytes) an SQS message can contain for this queue.      */
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
comment|/**      * The messageRetentionPeriod (in seconds) a message will be retained by SQS for this queue.      */
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
comment|/**      * Specify the policy that send message to DeadLetter queue. See detail at Amazon docs.      */
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
comment|/**      * If enabled then a scheduled background task will keep extending the message visibility on SQS.      * This is needed if it takes a long time to process the message. If set to true defaultVisibilityTimeout must be set.      * See details at Amazon docs.      */
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
comment|/**      * If you do not specify WaitTimeSeconds in the request, the queue attribute ReceiveMessageWaitTimeSeconds is used to determine how long to wait.      */
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
comment|/**      * Duration in seconds (0 to 20) that the ReceiveMessage action call will wait until a message is in the queue to include in the response.      */
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
comment|/**      * Specify the queue owner aws account id when you need to connect the queue with different account owner.      */
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
comment|/**      * Whether or not to send the DeleteMessage to the SQS queue if an exchange fails to get through a filter.      * If 'false' and exchange does not make it through a Camel filter upstream in the route, then don't send DeleteMessage.      */
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
comment|/**      * Specify the queue region which could be used with queueOwnerAWSAccountId to build the service URL.      */
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
comment|/**      * Allows you to use multiple threads to poll the sqs queue to increase throughput      */
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
comment|/**      * To define a proxy host when instantiating the SQS client      */
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
comment|/**      * To define a proxy port when instantiating the SQS client      */
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SqsConfiguration[queueName="
operator|+
name|queueName
operator|+
literal|", amazonSQSClient="
operator|+
name|amazonSQSClient
operator|+
literal|", accessKey="
operator|+
name|accessKey
operator|+
literal|", secretKey=xxxxxxxxxxxxxxx"
operator|+
literal|", deleteAfterRead="
operator|+
name|deleteAfterRead
operator|+
literal|", deleteIfFiltered="
operator|+
name|deleteIfFiltered
operator|+
literal|", visibilityTimeout="
operator|+
name|visibilityTimeout
operator|+
literal|", attributeNames="
operator|+
name|attributeNames
operator|+
literal|", messageAttributeNames="
operator|+
name|messageAttributeNames
operator|+
literal|", waitTimeSeconds="
operator|+
name|waitTimeSeconds
operator|+
literal|", defaultVisibilityTimeout="
operator|+
name|defaultVisibilityTimeout
operator|+
literal|", maximumMessageSize="
operator|+
name|maximumMessageSize
operator|+
literal|", messageRetentionPeriod="
operator|+
name|messageRetentionPeriod
operator|+
literal|", receiveMessageWaitTimeSeconds="
operator|+
name|receiveMessageWaitTimeSeconds
operator|+
literal|", delaySeconds="
operator|+
name|delaySeconds
operator|+
literal|", policy="
operator|+
name|policy
operator|+
literal|", redrivePolicy="
operator|+
name|redrivePolicy
operator|+
literal|", extendMessageVisibility="
operator|+
name|extendMessageVisibility
operator|+
literal|", queueOwnerAWSAccountId="
operator|+
name|queueOwnerAWSAccountId
operator|+
literal|", concurrentConsumers="
operator|+
name|concurrentConsumers
operator|+
literal|", region="
operator|+
name|region
operator|+
literal|", proxyHost="
operator|+
name|proxyHost
operator|+
literal|", proxyPort="
operator|+
name|proxyPort
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

