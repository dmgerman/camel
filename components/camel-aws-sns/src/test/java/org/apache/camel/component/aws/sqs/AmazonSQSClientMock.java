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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|concurrent
operator|.
name|CopyOnWriteArrayList
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
name|ScheduledFuture
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
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonServiceException
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
name|AbstractAmazonSQS
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
name|ChangeMessageVisibilityRequest
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
name|ChangeMessageVisibilityResult
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
name|DeleteMessageRequest
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
name|DeleteMessageResult
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
name|Message
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
name|ReceiveMessageRequest
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
name|ReceiveMessageResult
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
name|SendMessageRequest
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
name|SendMessageResult
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
name|SetQueueAttributesResult
import|;
end_import

begin_class
DECL|class|AmazonSQSClientMock
specifier|public
class|class
name|AmazonSQSClientMock
extends|extends
name|AbstractAmazonSQS
block|{
DECL|field|messages
name|List
argument_list|<
name|Message
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|queueAttributes
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|queueAttributes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|changeMessageVisibilityRequests
name|List
argument_list|<
name|ChangeMessageVisibilityRequest
argument_list|>
name|changeMessageVisibilityRequests
init|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|queues
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|CreateQueueRequest
argument_list|>
name|queues
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|inFlight
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
argument_list|>
name|inFlight
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|scheduler
specifier|private
name|ScheduledExecutorService
name|scheduler
decl_stmt|;
DECL|method|AmazonSQSClientMock ()
specifier|public
name|AmazonSQSClientMock
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listQueues ()
specifier|public
name|ListQueuesResult
name|listQueues
parameter_list|()
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|ListQueuesResult
name|result
init|=
operator|new
name|ListQueuesResult
argument_list|()
decl_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|createQueue (CreateQueueRequest createQueueRequest)
specifier|public
name|CreateQueueResult
name|createQueue
parameter_list|(
name|CreateQueueRequest
name|createQueueRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|String
name|queueName
init|=
literal|"https://queue.amazonaws.com/541925086079/"
operator|+
name|createQueueRequest
operator|.
name|getQueueName
argument_list|()
decl_stmt|;
name|queues
operator|.
name|put
argument_list|(
name|queueName
argument_list|,
name|createQueueRequest
argument_list|)
expr_stmt|;
name|CreateQueueResult
name|result
init|=
operator|new
name|CreateQueueResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setQueueUrl
argument_list|(
name|queueName
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|sendMessage (SendMessageRequest sendMessageRequest)
specifier|public
name|SendMessageResult
name|sendMessage
parameter_list|(
name|SendMessageRequest
name|sendMessageRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|Message
name|message
init|=
operator|new
name|Message
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|sendMessageRequest
operator|.
name|getMessageBody
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setMD5OfBody
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setMessageId
argument_list|(
literal|"f6fb6f99-5eb2-4be4-9b15-144774141458"
argument_list|)
expr_stmt|;
name|message
operator|.
name|setReceiptHandle
argument_list|(
literal|"0NNAq8PwvXsyZkR6yu4nQ07FGxNmOBWi5zC9+4QMqJZ0DJ3gVOmjI2Gh/oFnb0IeJqy5Zc8kH4JX7GVpfjcEDjaAPSeOkXQZRcaBqt"
operator|+
literal|"4lOtyfj0kcclVV/zS7aenhfhX5Ixfgz/rHhsJwtCPPvTAdgQFGYrqaHly+etJiawiNPVc="
argument_list|)
expr_stmt|;
synchronized|synchronized
init|(
name|messages
init|)
block|{
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
name|SendMessageResult
name|result
init|=
operator|new
name|SendMessageResult
argument_list|()
decl_stmt|;
name|result
operator|.
name|setMessageId
argument_list|(
literal|"f6fb6f99-5eb2-4be4-9b15-144774141458"
argument_list|)
expr_stmt|;
name|result
operator|.
name|setMD5OfMessageBody
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|receiveMessage (ReceiveMessageRequest receiveMessageRequest)
specifier|public
name|ReceiveMessageResult
name|receiveMessage
parameter_list|(
name|ReceiveMessageRequest
name|receiveMessageRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|Integer
name|maxNumberOfMessages
init|=
name|receiveMessageRequest
operator|.
name|getMaxNumberOfMessages
argument_list|()
operator|!=
literal|null
condition|?
name|receiveMessageRequest
operator|.
name|getMaxNumberOfMessages
argument_list|()
else|:
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|ReceiveMessageResult
name|result
init|=
operator|new
name|ReceiveMessageResult
argument_list|()
decl_stmt|;
name|Collection
argument_list|<
name|Message
argument_list|>
name|resultMessages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|messages
init|)
block|{
name|int
name|fetchSize
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Message
argument_list|>
name|iterator
init|=
name|messages
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
operator|&&
name|fetchSize
operator|<
name|maxNumberOfMessages
condition|;
name|fetchSize
operator|++
control|)
block|{
name|Message
name|rc
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|resultMessages
operator|.
name|add
argument_list|(
name|rc
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
name|scheduleCancelInflight
argument_list|(
name|receiveMessageRequest
operator|.
name|getQueueUrl
argument_list|()
argument_list|,
name|rc
argument_list|)
expr_stmt|;
block|}
block|}
name|result
operator|.
name|setMessages
argument_list|(
name|resultMessages
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
comment|/*      * Cancel (put back onto queue) in flight messages if the visibility time has expired      * and has not been manually deleted (ack'd)      */
DECL|method|scheduleCancelInflight (final String queueUrl, final Message message)
specifier|private
name|void
name|scheduleCancelInflight
parameter_list|(
specifier|final
name|String
name|queueUrl
parameter_list|,
specifier|final
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|scheduler
operator|!=
literal|null
condition|)
block|{
name|int
name|visibility
init|=
name|getVisibilityForQueue
argument_list|(
name|queueUrl
argument_list|)
decl_stmt|;
if|if
condition|(
name|visibility
operator|>
literal|0
condition|)
block|{
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|task
init|=
name|scheduler
operator|.
name|schedule
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
synchronized|synchronized
init|(
name|messages
init|)
block|{
comment|// put it back!
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|,
name|visibility
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|inFlight
operator|.
name|put
argument_list|(
name|message
operator|.
name|getReceiptHandle
argument_list|()
argument_list|,
name|task
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getVisibilityForQueue (String queueUrl)
specifier|private
name|int
name|getVisibilityForQueue
parameter_list|(
name|String
name|queueUrl
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queueAttr
init|=
name|queues
operator|.
name|get
argument_list|(
name|queueUrl
argument_list|)
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|queueAttr
operator|.
name|containsKey
argument_list|(
literal|"VisibilityTimeout"
argument_list|)
condition|)
block|{
return|return
name|Integer
operator|.
name|parseInt
argument_list|(
name|queueAttr
operator|.
name|get
argument_list|(
literal|"VisibilityTimeout"
argument_list|)
argument_list|)
return|;
block|}
return|return
literal|0
return|;
block|}
DECL|method|getScheduler ()
specifier|public
name|ScheduledExecutorService
name|getScheduler
parameter_list|()
block|{
return|return
name|scheduler
return|;
block|}
DECL|method|setScheduler (ScheduledExecutorService scheduler)
specifier|public
name|void
name|setScheduler
parameter_list|(
name|ScheduledExecutorService
name|scheduler
parameter_list|)
block|{
name|this
operator|.
name|scheduler
operator|=
name|scheduler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|deleteMessage (DeleteMessageRequest deleteMessageRequest)
specifier|public
name|DeleteMessageResult
name|deleteMessage
parameter_list|(
name|DeleteMessageRequest
name|deleteMessageRequest
parameter_list|)
throws|throws
name|AmazonClientException
block|{
name|String
name|receiptHandle
init|=
name|deleteMessageRequest
operator|.
name|getReceiptHandle
argument_list|()
decl_stmt|;
if|if
condition|(
name|inFlight
operator|.
name|containsKey
argument_list|(
name|receiptHandle
argument_list|)
condition|)
block|{
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|inFlightTask
init|=
name|inFlight
operator|.
name|get
argument_list|(
name|receiptHandle
argument_list|)
decl_stmt|;
name|inFlightTask
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|DeleteMessageResult
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setQueueAttributes (SetQueueAttributesRequest setQueueAttributesRequest)
specifier|public
name|SetQueueAttributesResult
name|setQueueAttributes
parameter_list|(
name|SetQueueAttributesRequest
name|setQueueAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
synchronized|synchronized
init|(
name|queueAttributes
init|)
block|{
if|if
condition|(
operator|!
name|queueAttributes
operator|.
name|containsKey
argument_list|(
name|setQueueAttributesRequest
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
condition|)
block|{
name|queueAttributes
operator|.
name|put
argument_list|(
name|setQueueAttributesRequest
operator|.
name|getQueueUrl
argument_list|()
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
specifier|final
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|setQueueAttributesRequest
operator|.
name|getAttributes
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|queueAttributes
operator|.
name|get
argument_list|(
name|setQueueAttributesRequest
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SetQueueAttributesResult
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|changeMessageVisibility (ChangeMessageVisibilityRequest changeMessageVisibilityRequest)
specifier|public
name|ChangeMessageVisibilityResult
name|changeMessageVisibility
parameter_list|(
name|ChangeMessageVisibilityRequest
name|changeMessageVisibilityRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|this
operator|.
name|changeMessageVisibilityRequests
operator|.
name|add
argument_list|(
name|changeMessageVisibilityRequest
argument_list|)
expr_stmt|;
return|return
operator|new
name|ChangeMessageVisibilityResult
argument_list|()
return|;
block|}
block|}
end_class

end_unit
