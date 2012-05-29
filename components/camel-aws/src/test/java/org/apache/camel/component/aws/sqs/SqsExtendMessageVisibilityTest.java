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
name|impl
operator|.
name|JndiRegistry
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
DECL|class|SqsExtendMessageVisibilityTest
specifier|public
class|class
name|SqsExtendMessageVisibilityTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|TIMEOUT
init|=
literal|4
decl_stmt|;
comment|// 4 seconds.
DECL|field|RECEIPT_HANDLE
specifier|private
specifier|static
specifier|final
name|String
name|RECEIPT_HANDLE
init|=
literal|"0NNAq8PwvXsyZkR6yu4nQ07FGxNmOBWi5"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
DECL|field|clientMock
specifier|private
name|AmazonSQSClientMock
name|clientMock
decl_stmt|;
annotation|@
name|Test
DECL|method|longReceiveExtendsMessageVisibility ()
specifier|public
name|void
name|longReceiveExtendsMessageVisibility
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
name|this
operator|.
name|mock
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Simulate message that takes a while to receive.
name|Thread
operator|.
name|sleep
argument_list|(
name|TIMEOUT
operator|*
literal|1500L
argument_list|)
expr_stmt|;
comment|// 150% of TIMEOUT.
block|}
block|}
argument_list|)
expr_stmt|;
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
literal|"Message 1"
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
name|RECEIPT_HANDLE
argument_list|)
expr_stmt|;
name|this
operator|.
name|clientMock
operator|.
name|messages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// Wait for message to arrive.
name|assertTrue
argument_list|(
literal|"Expected at least one changeMessageVisibility request."
argument_list|,
name|this
operator|.
name|clientMock
operator|.
name|changeMessageVisibilityRequests
operator|.
name|size
argument_list|()
operator|>=
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|ChangeMessageVisibilityRequest
name|req
range|:
name|this
operator|.
name|clientMock
operator|.
name|changeMessageVisibilityRequests
control|)
block|{
name|assertEquals
argument_list|(
literal|"https://queue.amazonaws.com/541925086079/MyQueue"
argument_list|,
name|req
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RECEIPT_HANDLE
argument_list|,
name|req
operator|.
name|getReceiptHandle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|4
argument_list|)
argument_list|,
name|req
operator|.
name|getVisibilityTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|this
operator|.
name|clientMock
operator|=
operator|new
name|AmazonSQSClientMock
argument_list|()
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|this
operator|.
name|clientMock
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&extendMessageVisibility=true&visibilityTimeout="
operator|+
name|TIMEOUT
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
block|}
end_class

end_unit

