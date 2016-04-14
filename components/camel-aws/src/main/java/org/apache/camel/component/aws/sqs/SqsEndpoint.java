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
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|ClientConfiguration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|AWSCredentials
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|auth
operator|.
name|BasicAWSCredentials
import|;
end_import

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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|AmazonSQSClient
import|;
end_import

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
name|model
operator|.
name|CreateQueueRequest
import|;
end_import

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
name|model
operator|.
name|CreateQueueResult
import|;
end_import

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
name|model
operator|.
name|GetQueueUrlRequest
import|;
end_import

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
name|model
operator|.
name|GetQueueUrlResult
import|;
end_import

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
name|model
operator|.
name|ListQueuesResult
import|;
end_import

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
name|model
operator|.
name|MessageAttributeValue
import|;
end_import

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
name|model
operator|.
name|QueueAttributeName
import|;
end_import

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
name|model
operator|.
name|SetQueueAttributesRequest
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
name|ExchangePattern
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
name|DefaultScheduledPollConsumerScheduler
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
name|ScheduledPollEndpoint
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
name|HeaderFilterStrategy
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
name|HeaderFilterStrategyAware
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
comment|/**  * The aws-sqs component is used for sending and receiving messages to Amazon's SQS service.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"aws-sqs"
argument_list|,
name|title
operator|=
literal|"AWS Simple Queue Service"
argument_list|,
name|syntax
operator|=
literal|"aws-sqs:queueNameOrArn"
argument_list|,
name|consumerClass
operator|=
name|SqsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"cloud,messaging"
argument_list|)
DECL|class|SqsEndpoint
specifier|public
class|class
name|SqsEndpoint
extends|extends
name|ScheduledPollEndpoint
implements|implements
name|HeaderFilterStrategyAware
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
name|SqsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|AmazonSQS
name|client
decl_stmt|;
DECL|field|queueUrl
specifier|private
name|String
name|queueUrl
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|SqsConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|SqsEndpoint (String uri, SqsComponent component, SqsConfiguration configuration)
specifier|public
name|SqsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SqsComponent
name|component
parameter_list|,
name|SqsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to map headers to/from Camel.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
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
name|SqsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
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
name|SqsConsumer
name|sqsConsumer
init|=
operator|new
name|SqsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|sqsConsumer
argument_list|)
expr_stmt|;
name|sqsConsumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|maxMessagesPerPoll
argument_list|)
expr_stmt|;
name|DefaultScheduledPollConsumerScheduler
name|scheduler
init|=
operator|new
name|DefaultScheduledPollConsumerScheduler
argument_list|()
decl_stmt|;
name|scheduler
operator|.
name|setConcurrentTasks
argument_list|(
name|configuration
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|sqsConsumer
operator|.
name|setScheduler
argument_list|(
name|scheduler
argument_list|)
expr_stmt|;
return|return
name|sqsConsumer
return|;
block|}
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
name|client
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSClient
argument_list|()
operator|!=
literal|null
condition|?
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSClient
argument_list|()
else|:
name|getClient
argument_list|()
expr_stmt|;
comment|// Override the endpoint location
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSEndpoint
argument_list|()
argument_list|)
condition|)
block|{
name|client
operator|.
name|setEndpoint
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// check the setting the headerFilterStrategy
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|SqsHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
comment|// If both region and Account ID is provided the queue URL can be built manually.
comment|// This allows accessing queues where you don't have permission to list queues or query queues
if|if
condition|(
name|configuration
operator|.
name|getRegion
argument_list|()
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|queueUrl
operator|=
literal|"https://sqs."
operator|+
name|configuration
operator|.
name|getRegion
argument_list|()
operator|+
literal|".amazonaws.com/"
operator|+
name|configuration
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
operator|+
literal|"/"
operator|+
name|configuration
operator|.
name|getQueueName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|configuration
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|GetQueueUrlRequest
name|getQueueUrlRequest
init|=
operator|new
name|GetQueueUrlRequest
argument_list|()
decl_stmt|;
name|getQueueUrlRequest
operator|.
name|setQueueName
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|getQueueUrlRequest
operator|.
name|setQueueOwnerAWSAccountId
argument_list|(
name|configuration
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|GetQueueUrlResult
name|getQueueUrlResult
init|=
name|client
operator|.
name|getQueueUrl
argument_list|(
name|getQueueUrlRequest
argument_list|)
decl_stmt|;
name|queueUrl
operator|=
name|getQueueUrlResult
operator|.
name|getQueueUrl
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// check whether the queue already exists
name|ListQueuesResult
name|listQueuesResult
init|=
name|client
operator|.
name|listQueues
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|url
range|:
name|listQueuesResult
operator|.
name|getQueueUrls
argument_list|()
control|)
block|{
if|if
condition|(
name|url
operator|.
name|endsWith
argument_list|(
literal|"/"
operator|+
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
condition|)
block|{
name|queueUrl
operator|=
name|url
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Queue available at '{}'."
argument_list|,
name|queueUrl
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|queueUrl
operator|==
literal|null
condition|)
block|{
name|createQueue
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|updateQueueAttributes
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createQueue (AmazonSQS client)
specifier|protected
name|void
name|createQueue
parameter_list|(
name|AmazonSQS
name|client
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Queue '{}' doesn't exist. Will create it..."
argument_list|,
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
comment|// creates a new queue, or returns the URL of an existing one
name|CreateQueueRequest
name|request
init|=
operator|new
name|CreateQueueRequest
argument_list|(
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultVisibilityTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|VisibilityTimeout
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultVisibilityTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getMaximumMessageSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|MaximumMessageSize
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaximumMessageSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getMessageRetentionPeriod
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|MessageRetentionPeriod
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMessageRetentionPeriod
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|Policy
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveMessageWaitTimeSeconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|ReceiveMessageWaitTimeSeconds
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveMessageWaitTimeSeconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|RedrivePolicy
operator|.
name|name
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating queue [{}] with request [{}]..."
argument_list|,
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|CreateQueueResult
name|queueResult
init|=
name|client
operator|.
name|createQueue
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|queueUrl
operator|=
name|queueResult
operator|.
name|getQueueUrl
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Queue created and available at: {}"
argument_list|,
name|queueUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|updateQueueAttributes (AmazonSQS client)
specifier|private
name|void
name|updateQueueAttributes
parameter_list|(
name|AmazonSQS
name|client
parameter_list|)
block|{
name|SetQueueAttributesRequest
name|request
init|=
operator|new
name|SetQueueAttributesRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setQueueUrl
argument_list|(
name|queueUrl
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultVisibilityTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|VisibilityTimeout
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getDefaultVisibilityTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getMaximumMessageSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|MaximumMessageSize
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaximumMessageSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getMessageRetentionPeriod
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|MessageRetentionPeriod
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMessageRetentionPeriod
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|Policy
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveMessageWaitTimeSeconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|ReceiveMessageWaitTimeSeconds
operator|.
name|name
argument_list|()
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveMessageWaitTimeSeconds
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|put
argument_list|(
name|QueueAttributeName
operator|.
name|RedrivePolicy
operator|.
name|name
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|request
operator|.
name|getAttributes
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating queue '{}' with the provided queue attributes..."
argument_list|,
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setQueueAttributes
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Queue '{}' updated and available at {}'"
argument_list|,
name|configuration
operator|.
name|getQueueName
argument_list|()
argument_list|,
name|queueUrl
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
name|client
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|createExchange (com.amazonaws.services.sqs.model.Message msg)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|Message
name|msg
parameter_list|)
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|,
name|msg
argument_list|)
return|;
block|}
DECL|method|createExchange (ExchangePattern pattern, com.amazonaws.services.sqs.model.Message msg)
specifier|private
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|,
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|Message
name|msg
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|super
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|msg
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeaders
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|msg
operator|.
name|getAttributes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|msg
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|,
name|msg
operator|.
name|getMD5OfBody
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|,
name|msg
operator|.
name|getReceiptHandle
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|ATTRIBUTES
argument_list|,
name|msg
operator|.
name|getAttributes
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ATTRIBUTES
argument_list|,
name|msg
operator|.
name|getMessageAttributes
argument_list|()
argument_list|)
expr_stmt|;
comment|//Need to apply the SqsHeaderFilterStrategy this time
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|getHeaderFilterStrategy
argument_list|()
decl_stmt|;
comment|//add all sqs message attributes as camel message headers so that knowledge of
comment|//the Sqs class MessageAttributeValue will not leak to the client
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|MessageAttributeValue
argument_list|>
name|entry
range|:
name|msg
operator|.
name|getMessageAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|header
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|translateValue
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|header
argument_list|,
name|value
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|exchange
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SqsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (SqsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SqsConfiguration
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
DECL|method|getClient ()
specifier|public
name|AmazonSQS
name|getClient
parameter_list|()
block|{
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
name|createClient
argument_list|()
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|setClient (AmazonSQS client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AmazonSQS
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
comment|/**      * Provide the possibility to override this method for an mock implementation      * @return AmazonSQSClient      */
DECL|method|createClient ()
name|AmazonSQS
name|createClient
parameter_list|()
block|{
name|AmazonSQS
name|client
init|=
literal|null
decl_stmt|;
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
condition|)
block|{
name|ClientConfiguration
name|clientConfiguration
init|=
operator|new
name|ClientConfiguration
argument_list|()
decl_stmt|;
name|clientConfiguration
operator|.
name|setProxyHost
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyPort
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|AmazonSQSClient
argument_list|(
name|credentials
argument_list|,
name|clientConfiguration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|=
operator|new
name|AmazonSQSClient
argument_list|(
name|credentials
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getQueueUrl ()
specifier|protected
name|String
name|getQueueUrl
parameter_list|()
block|{
return|return
name|queueUrl
return|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * Gets the maximum number of messages as a limit to poll at each polling.      *<p/>      * Is default unlimited, but use 0 or negative number to disable it as unlimited.      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|translateValue (MessageAttributeValue mav)
specifier|private
name|Object
name|translateValue
parameter_list|(
name|MessageAttributeValue
name|mav
parameter_list|)
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mav
operator|.
name|getStringValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|mav
operator|.
name|getStringValue
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mav
operator|.
name|getBinaryValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|mav
operator|.
name|getBinaryValue
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

