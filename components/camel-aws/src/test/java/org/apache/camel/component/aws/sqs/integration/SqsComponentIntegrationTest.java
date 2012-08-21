begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs.integration
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
operator|.
name|integration
package|;
end_package

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
name|aws
operator|.
name|sqs
operator|.
name|SqsConstants
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Must be manually tested. Provide your own accessKey and secretKey!"
argument_list|)
DECL|class|SqsComponentIntegrationTest
specifier|public
class|class
name|SqsComponentIntegrationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|accessKey
specifier|private
name|String
name|accessKey
init|=
literal|"xxx"
decl_stmt|;
DECL|field|secretKey
specifier|private
name|String
name|secretKey
init|=
literal|"yyy"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
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
name|uri
operator|=
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
name|exchange
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
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|sqsEndpointUri
init|=
name|String
operator|.
name|format
argument_list|(
literal|"aws-sqs://MyNewCamelQueue?accessKey=%s&secretKey=%s&messageRetentionPeriod=%s&maximumMessageSize=%s&visibilityTimeout=%s&policy=%s"
argument_list|,
name|accessKey
argument_list|,
name|secretKey
argument_list|,
literal|"1209600"
argument_list|,
literal|"65536"
argument_list|,
literal|"60"
argument_list|,
literal|"%7B%22Version%22%3A%222008-10-17%22%2C%22Id%22%3A%22%2F195004372649%2FMyNewCamelQueue%2FSQSDefaultPolicy%22%2C%22"
operator|+
literal|"Statement%22%3A%5B%7B%22Sid%22%3A%22Queue1ReceiveMessage%22%2C%22Effect%22%3A%22Allow%22%2C%22Principal%22%3A%7B%22AWS%22%3A%22*%22%7D%2C%22"
operator|+
literal|"Action%22%3A%22SQS%3AReceiveMessage%22%2C%22Resource%22%3A%22%2F195004372649%2FMyNewCamelQueue%22%7D%5D%7D"
argument_list|)
decl_stmt|;
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|sqsEndpointUri
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|sqsEndpointUri
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

