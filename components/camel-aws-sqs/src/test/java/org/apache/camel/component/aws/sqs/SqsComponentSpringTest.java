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
name|SendMessageBatchResult
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
name|ProducerTemplate
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
name|spring
operator|.
name|CamelSpringTestSupport
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SqsComponentSpringTest
specifier|public
class|class
name|SqsComponentSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|sendInOnly ()
specifier|public
name|void
name|sendInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|resultExchange
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"This is my message text."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|ATTRIBUTES
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ATTRIBUTES
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOut ()
specifier|public
name|void
name|sendInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my message text."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|resultExchange
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"This is my message text."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|ATTRIBUTES
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ATTRIBUTES
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MESSAGE_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"6a1559560f67c5e7a7d5d838bf0272ee"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|MD5_OF_BODY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendBatchMessage ()
specifier|public
name|void
name|sendBatchMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start-batch"
argument_list|,
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
name|Collection
name|c
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|c
operator|.
name|add
argument_list|(
literal|"team1"
argument_list|)
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
literal|"team2"
argument_list|)
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
literal|"team3"
argument_list|)
expr_stmt|;
name|c
operator|.
name|add
argument_list|(
literal|"team4"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|SendMessageBatchResult
name|res
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|SendMessageBatchResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|res
operator|.
name|getFailed
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|res
operator|.
name|getSuccessful
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteMessage ()
specifier|public
name|void
name|deleteMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:start-delete"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|,
literal|"123456"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DeleteMessageResult
name|res
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|DeleteMessageResult
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|res
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/aws/sqs/SqsComponentSpringTest-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

