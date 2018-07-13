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
name|impl
operator|.
name|PropertyPlaceholderDelegateRegistry
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
DECL|class|SqsComponentConfigurationTest
specifier|public
class|class
name|SqsComponentConfigurationTest
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|getAttributeNames
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
name|getMessageAttributeNames
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
name|getDefaultVisibilityTimeout
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
name|getVisibilityTimeout
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
name|getMaximumMessageSize
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
name|getMessageRetentionPeriod
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
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
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
name|getRegion
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
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|getAttributeNames
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
name|getMessageAttributeNames
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
name|getDefaultVisibilityTimeout
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
name|getVisibilityTimeout
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
name|getMaximumMessageSize
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
name|getMessageRetentionPeriod
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
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
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
name|getRegion
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://arn:aws:sqs:us-east-1:account:MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx&secretKey=yyy"
argument_list|)
decl_stmt|;
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
literal|"account"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
block|}
annotation|@
name|Test
DECL|method|createEndpointAttributeNames ()
specifier|public
name|void
name|createEndpointAttributeNames
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx&secretKey=yyy&attributeNames=foo,bar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|getAmazonSQSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo,bar"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|assertSame
argument_list|(
name|mock
argument_list|,
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
name|getAttributeNames
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
name|getMessageAttributeNames
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
name|getDefaultVisibilityTimeout
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
name|getVisibilityTimeout
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
name|getMaximumMessageSize
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
name|getMessageRetentionPeriod
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
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
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
name|getRegion
argument_list|()
argument_list|)
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx"
operator|+
literal|"&secretKey=yyy&attributeNames=color,size"
operator|+
literal|"&messageAttributeNames=msgColor,msgSize&DefaultVisibilityTimeout=1000&visibilityTimeout=2000&maximumMessageSize=65536&messageRetentionPeriod=1209600&policy="
operator|+
literal|"%7B%22Version%22%3A%222008-10-17%22%2C%22Id%22%3A%22%2F195004372649%2FMyQueue%2FSQSDefaultPolicy%22%2C%22Statement%22%3A%5B%7B%22Sid%22%3A%22Queue1ReceiveMessage%22%2C%22"
operator|+
literal|"Effect%22%3A%22Allow%22%2C%22Principal%22%3A%7B%22AWS%22%3A%22*%22%7D%2C%22Action%22%3A%22SQS%3AReceiveMessage%22%2C%22Resource%22%3A%22%2F195004372649%2FMyQueue%22%7D%5D%7D"
operator|+
literal|"&delaySeconds=123&receiveMessageWaitTimeSeconds=10&waitTimeSeconds=20"
operator|+
literal|"&queueOwnerAWSAccountId=111222333&region=us-east-1"
operator|+
literal|"&redrivePolicy={\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:195004372649:MyDeadLetterQueue\"}"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|getAmazonSQSClient
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"color,size"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"msgColor,msgSize"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageAttributeNames
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1000
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDefaultVisibilityTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|2000
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getVisibilityTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|65536
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaximumMessageSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|1209600
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMessageRetentionPeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Version\":\"2008-10-17\",\"Id\":\"/195004372649/MyQueue/SQSDefaultPolicy\",\"Statement\":[{\"Sid\":\"Queue1ReceiveMessage\",\"Effect\":\"Allow\",\"Principal\":"
operator|+
literal|"{\"AWS\":\"*\"},\"Action\":\"SQS:ReceiveMessage\",\"Resource\":\"/195004372649/MyQueue\"}]}"
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
name|assertEquals
argument_list|(
literal|"{\"maxReceiveCount\":\"5\", \"deadLetterTargetArn\":\"arn:aws:sqs:us-east-1:195004372649:MyDeadLetterQueue\"}"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRedrivePolicy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
literal|123
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDelaySeconds
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|10
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getReceiveMessageWaitTimeSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|20
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWaitTimeSeconds
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"111222333"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueOwnerAWSAccountId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"us-east-1"
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
DECL|method|createEndpointWithPollConsumerConfiguration ()
specifier|public
name|void
name|createEndpointWithPollConsumerConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient"
operator|+
literal|"&accessKey=xxx&secretKey=yyy&initialDelay=300&delay=400&maxMessagesPerPoll=50"
argument_list|)
decl_stmt|;
name|SqsConsumer
name|consumer
init|=
operator|(
name|SqsConsumer
operator|)
name|endpoint
operator|.
name|createConsumer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|300
argument_list|,
name|consumer
operator|.
name|getInitialDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|400
argument_list|,
name|consumer
operator|.
name|getDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|50
argument_list|,
name|consumer
operator|.
name|getMaxMessagesPerPoll
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
DECL|method|createEndpointWithoutAccessKeyConfiguration ()
specifier|public
name|void
name|createEndpointWithoutAccessKeyConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?secretKey=yyy"
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
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=xxx"
argument_list|)
expr_stmt|;
block|}
comment|// Setting extendMessageVisibility on an SQS consumer should make visibilityTimeout compulsory
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|createEndpointWithExtendMessageVisibilityAndNoVisibilityTimeoutThrowsException ()
specifier|public
name|void
name|createEndpointWithExtendMessageVisibilityAndNoVisibilityTimeoutThrowsException
parameter_list|()
throws|throws
name|Exception
block|{
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=xxx&secretKey=yyy&extendMessageVisibility=true"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithExtendMessageVisibilityTrueAndVisibilityTimeoutSet ()
specifier|public
name|void
name|createEndpointWithExtendMessageVisibilityTrueAndVisibilityTimeoutSet
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx&secretKey=yyy&visibilityTimeout=30&extendMessageVisibility=true"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createEndpointWithExtendMessageVisibilityFalseAndVisibilityTimeoutSet ()
specifier|public
name|void
name|createEndpointWithExtendMessageVisibilityFalseAndVisibilityTimeoutSet
parameter_list|()
throws|throws
name|Exception
block|{
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient&accessKey=xxx&secretKey=yyy&visibilityTimeout=30&extendMessageVisibility=false"
argument_list|)
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient"
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
name|AmazonSQSClientMock
name|mock
init|=
operator|new
name|AmazonSQSClientMock
argument_list|()
decl_stmt|;
operator|(
call|(
name|JndiRegistry
call|)
argument_list|(
operator|(
name|PropertyPlaceholderDelegateRegistry
operator|)
name|context
operator|.
name|getRegistry
argument_list|()
argument_list|)
operator|.
name|getRegistry
argument_list|()
operator|)
operator|.
name|bind
argument_list|(
literal|"amazonSQSClient"
argument_list|,
name|mock
argument_list|)
expr_stmt|;
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
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
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?amazonSQSClient=#amazonSQSClient"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
name|SqsComponent
name|component
init|=
operator|new
name|SqsComponent
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
name|SqsEndpoint
name|endpoint
init|=
operator|(
name|SqsEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"aws-sqs://MyQueue?accessKey=xxxxxx&secretKey=yyyyy&region=US_EAST_1"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyQueue"
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getQueueName
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
block|}
end_class

end_unit

