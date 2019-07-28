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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|EndpointInject
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SqsEndpointUseExistingQueueTest
specifier|public
class|class
name|SqsEndpointUseExistingQueueTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonSQSClient"
argument_list|)
DECL|field|client
specifier|private
name|AmazonSQSClientMock
name|client
init|=
operator|new
name|SqsEndpointUseExistingQueueTest
operator|.
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|defaultsToDisabled ()
specifier|public
name|void
name|defaultsToDisabled
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// Wait for message to arrive.
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|AmazonSQSClientMock
specifier|static
class|class
name|AmazonSQSClientMock
extends|extends
name|AmazonSQSClient
block|{
DECL|method|AmazonSQSClientMock ()
name|AmazonSQSClientMock
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|BasicAWSCredentials
argument_list|(
literal|"myAccessKey"
argument_list|,
literal|"mySecretKey"
argument_list|)
argument_list|)
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
name|result
operator|.
name|getQueueUrls
argument_list|()
operator|.
name|add
argument_list|(
literal|"http://queue.amazonaws.com/0815/Foo"
argument_list|)
expr_stmt|;
name|result
operator|.
name|getQueueUrls
argument_list|()
operator|.
name|add
argument_list|(
literal|"http://queue.amazonaws.com/0815/MyQueue"
argument_list|)
expr_stmt|;
name|result
operator|.
name|getQueueUrls
argument_list|()
operator|.
name|add
argument_list|(
literal|"http://queue.amazonaws.com/0815/Bar"
argument_list|)
expr_stmt|;
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
throw|throw
operator|new
name|AmazonServiceException
argument_list|(
literal|"forced exception for test if this method is called"
argument_list|)
throw|;
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
return|return
operator|new
name|SetQueueAttributesResult
argument_list|()
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
name|ReceiveMessageResult
name|result
init|=
operator|new
name|ReceiveMessageResult
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Message
argument_list|>
name|resultMessages
init|=
name|result
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|Message
argument_list|()
decl_stmt|;
name|resultMessages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
block|}
end_class

end_unit

