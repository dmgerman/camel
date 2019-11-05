begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sns
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
name|sns
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|regions
operator|.
name|Regions
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
name|AmazonSQSClientMock
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
DECL|class|SnsComponentConfigurationTest
specifier|public
class|class
name|SnsComponentConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|createEndpointWithMinimalConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithOnlyAccessKeyAndSecretKey ()
specifier|public
name|void
name|createEndpointWithOnlyAccessKeyAndSecretKey
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMinimalArnConfiguration ()
specifier|public
name|void
name|createEndpointWithMinimalArnConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://arn:aws:sns:us-east-1:account:MyTopic?amazonSNSClient=#amazonSNSClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arn:aws:sns:us-east-1:account:MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMinimalConfigurationAndProvidedClient ()
specifier|public
name|void
name|createEndpointWithMinimalConfigurationAndProvidedClient
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arn:aws:sns:us-east-1:541925086079:MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithMaximalConfiguration ()
specifier|public
name|void
name|createEndpointWithMaximalConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient&accessKey=xxx&secretKey=yyy"
operator|+
literal|"&policy=%7B%22Version%22%3A%222008-10-17%22,%22Statement%22%3A%5B%7B%22Sid%22%3A%221%22,%22Effect%22%3A%22Allow%22,%22Principal%22%3A%7B%22AWS%22%3A%5B%22*%22%5D%7D,"
operator|+
literal|"%22Action%22%3A%5B%22sns%3ASubscribe%22%5D%7D%5D%7D&subject=The+subject+message"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The subject message"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Version\":\"2008-10-17\",\"Statement\":[{\"Sid\":\"1\",\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"sns:Subscribe\"]}]}"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithSQSSubscription ()
specifier|public
name|void
name|createEndpointWithSQSSubscription
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|AmazonSQSClientMock
name|mockSQS
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mockSQS
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient&"
operator|+
literal|"accessKey=xxx&secretKey=yyy&amazonSQSClient=#amazonSQSClient&queueUrl=arn:aws:sqs:us-east-1:541925086079:MyQueue&subscribeSNStoSQS=true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arn:aws:sqs:us-east-1:541925086079:MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithSQSSubscriptionIllegalArgument ()
specifier|public
name|void
name|createEndpointWithSQSSubscriptionIllegalArgument
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|AmazonSQSClientMock
name|mockSQS
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mockSQS
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient&accessKey=xxx"
operator|+
literal|"&secretKey=yyy&amazonSQSClient=#amazonSQSClient&subscribeSNStoSQS=true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAmazonSQSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutAccessKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutAccessKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?secretKey=yyy"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithoutSecretKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutSecretKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?accessKey=xxx"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithComponentElements ()
specifier|public
name|void
name|createEndpointWithComponentElements
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"YYY"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithComponentAndEndpointElements ()
specifier|public
name|void
name|createEndpointWithComponentAndEndpointElements
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setRegion
argument_list|(
name|Regions
operator|.
name|US_WEST_1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?accessKey=xxxxxx&secretKey=yyyyy&region=US_EAST_1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxxxxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyyyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"US_EAST_1"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithoutAutocreation ()
specifier|public
name|void
name|createEndpointWithoutAutocreation
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setRegion
argument_list|(
name|Regions
operator|.
name|US_WEST_1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?accessKey=xxxxxx&secretKey=yyyyy&region=US_EAST_1&autoCreateTopic=false"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyTopic"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTopicName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xxxxxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyyyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"US_EAST_1"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoCreateTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithComponentEndpointElementsAndProxy ()
specifier|public
name|void
name|createEndpointWithComponentEndpointElementsAndProxy
parameter_list|()
throws|throws
name|Exception
block|{
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|setAccessKey
argument_list|(
literal|"XXX"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setSecretKey
argument_list|(
literal|"YYY"
argument_list|)
expr_stmt|;
name|component
operator|.
name|setRegion
argument_list|(
name|Regions
operator|.
name|US_WEST_1
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|SnsEndpoint
name|endpoint
init|=
operator|(
name|SnsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://label?accessKey=xxxxxx&secretKey=yyyyy&region=US_EAST_1&proxyHost=localhost&proxyPort=9000&proxyProtocol=HTTP"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"xxxxxx"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAccessKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"yyyyy"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecretKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"US_EAST_1"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRegion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"localhost"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|9000
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithoutSecretKeyAndAccessKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutSecretKeyAndAccessKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSNSClientMock
name|mock
init|=
operator|new
name|AmazonSNSClientMock
argument_list|()
decl_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"amazonSNSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SnsComponent
name|component
init|=
operator|new
name|SnsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sns://MyTopic?amazonSNSClient=#amazonSNSClient"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

