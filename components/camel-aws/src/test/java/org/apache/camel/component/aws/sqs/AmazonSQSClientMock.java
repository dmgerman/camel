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
name|Iterator
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

begin_class
DECL|class|AmazonSQSClientMock
specifier|public
class|class
name|AmazonSQSClientMock
extends|extends
name|AmazonSQSClient
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
argument_list|<
name|Message
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|AmazonSQSClientMock ()
specifier|public
name|AmazonSQSClientMock
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
literal|"https://queue.amazonaws.com/541925086079/MyQueue"
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
argument_list|<
name|Message
argument_list|>
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
name|resultMessages
operator|.
name|add
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|iterator
operator|.
name|remove
argument_list|()
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
annotation|@
name|Override
DECL|method|deleteMessage (DeleteMessageRequest deleteMessageRequest)
specifier|public
name|void
name|deleteMessage
parameter_list|(
name|DeleteMessageRequest
name|deleteMessageRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
comment|// noop
block|}
block|}
end_class

end_unit

