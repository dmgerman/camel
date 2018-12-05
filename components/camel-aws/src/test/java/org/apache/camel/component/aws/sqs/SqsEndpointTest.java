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
name|QueueAttributeName
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|mockito
operator|.
name|ArgumentMatchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|SqsEndpointTest
specifier|public
class|class
name|SqsEndpointTest
block|{
DECL|field|endpoint
specifier|private
name|SqsEndpoint
name|endpoint
decl_stmt|;
DECL|field|amazonSQSClient
specifier|private
name|AmazonSQSClient
name|amazonSQSClient
decl_stmt|;
DECL|field|config
specifier|private
name|SqsConfiguration
name|config
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|amazonSQSClient
operator|=
name|Mockito
operator|.
name|mock
argument_list|(
name|AmazonSQSClient
operator|.
name|class
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|SqsConfiguration
argument_list|()
expr_stmt|;
name|config
operator|.
name|setQueueName
argument_list|(
literal|"test-queue"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setAmazonSQSClient
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|SqsEndpoint
argument_list|(
literal|"aws-sqs://test-queue"
argument_list|,
operator|new
name|SqsComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStartShouldNotCallUpdateQueueAttributesIfQueueExistAndNoOptionIsSpecified ()
specifier|public
name|void
name|doStartShouldNotCallUpdateQueueAttributesIfQueueExistAndNoOptionIsSpecified
parameter_list|()
throws|throws
name|Exception
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|listQueues
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|ListQueuesResult
argument_list|()
operator|.
name|withQueueUrls
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/ID/dummy-queue"
argument_list|,
literal|"https://sqs.us-east-1.amazonaws.com/ID/test-queue"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|listQueues
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStartWithDifferentQueueOwner ()
specifier|public
name|void
name|doStartWithDifferentQueueOwner
parameter_list|()
throws|throws
name|Exception
block|{
name|GetQueueUrlRequest
name|expectedGetQueueUrlRequest
init|=
operator|new
name|GetQueueUrlRequest
argument_list|(
literal|"test-queue"
argument_list|)
operator|.
name|withQueueOwnerAWSAccountId
argument_list|(
literal|"111222333"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|getQueueUrl
argument_list|(
name|expectedGetQueueUrlRequest
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|GetQueueUrlResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setQueueOwnerAWSAccountId
argument_list|(
literal|"111222333"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|getQueueUrl
argument_list|(
name|expectedGetQueueUrlRequest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createQueueShouldCreateFifoQueueWithContentBasedDeduplication ()
specifier|public
name|void
name|createQueueShouldCreateFifoQueueWithContentBasedDeduplication
parameter_list|()
block|{
name|config
operator|.
name|setQueueName
argument_list|(
literal|"test-queue.fifo"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMessageDeduplicationIdStrategy
argument_list|(
literal|"useContentBasedDeduplication"
argument_list|)
expr_stmt|;
name|CreateQueueRequest
name|expectedCreateQueueRequest
init|=
operator|new
name|CreateQueueRequest
argument_list|(
literal|"test-queue.fifo"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|FifoQueue
operator|.
name|name
argument_list|()
argument_list|,
literal|"true"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|ContentBasedDeduplication
operator|.
name|name
argument_list|()
argument_list|,
literal|"true"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|createQueue
argument_list|(
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|CreateQueueRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|CreateQueueResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue.fifo"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|createQueue
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|createQueue
argument_list|(
name|expectedCreateQueueRequest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue.fifo"
argument_list|,
name|endpoint
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createQueueShouldCreateFifoQueueWithoutContentBasedDeduplication ()
specifier|public
name|void
name|createQueueShouldCreateFifoQueueWithoutContentBasedDeduplication
parameter_list|()
block|{
name|config
operator|.
name|setQueueName
argument_list|(
literal|"test-queue.fifo"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMessageDeduplicationIdStrategy
argument_list|(
literal|"useExchangeId"
argument_list|)
expr_stmt|;
name|CreateQueueRequest
name|expectedCreateQueueRequest
init|=
operator|new
name|CreateQueueRequest
argument_list|(
literal|"test-queue.fifo"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|FifoQueue
operator|.
name|name
argument_list|()
argument_list|,
literal|"true"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|ContentBasedDeduplication
operator|.
name|name
argument_list|()
argument_list|,
literal|"false"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|createQueue
argument_list|(
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|CreateQueueRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|CreateQueueResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue.fifo"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|createQueue
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|createQueue
argument_list|(
name|expectedCreateQueueRequest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue.fifo"
argument_list|,
name|endpoint
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createQueueShouldCreateStandardQueueWithCorrectAttributes ()
specifier|public
name|void
name|createQueueShouldCreateStandardQueueWithCorrectAttributes
parameter_list|()
block|{
name|config
operator|.
name|setDefaultVisibilityTimeout
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMaximumMessageSize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMessageRetentionPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPolicy
argument_list|(
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReceiveMessageWaitTimeSeconds
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRedrivePolicy
argument_list|(
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
expr_stmt|;
name|CreateQueueRequest
name|expectedCreateQueueRequest
init|=
operator|new
name|CreateQueueRequest
argument_list|(
literal|"test-queue"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|VisibilityTimeout
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MaximumMessageSize
operator|.
name|name
argument_list|()
argument_list|,
literal|"128"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MessageRetentionPeriod
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|Policy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|ReceiveMessageWaitTimeSeconds
operator|.
name|name
argument_list|()
argument_list|,
literal|"5"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|RedrivePolicy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|createQueue
argument_list|(
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|CreateQueueRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|CreateQueueResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|createQueue
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|createQueue
argument_list|(
name|expectedCreateQueueRequest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|,
name|endpoint
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createQueueWithSSEShouldCreateStandardQueueWithSSESet ()
specifier|public
name|void
name|createQueueWithSSEShouldCreateStandardQueueWithSSESet
parameter_list|()
block|{
name|config
operator|.
name|setDefaultVisibilityTimeout
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMaximumMessageSize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMessageRetentionPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPolicy
argument_list|(
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReceiveMessageWaitTimeSeconds
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRedrivePolicy
argument_list|(
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setKmsMasterKeyId
argument_list|(
literal|"keyMaster1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setKmsDataKeyReusePeriodSeconds
argument_list|(
literal|300
argument_list|)
expr_stmt|;
name|config
operator|.
name|setServerSideEncryptionEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CreateQueueRequest
name|expectedCreateQueueRequest
init|=
operator|new
name|CreateQueueRequest
argument_list|(
literal|"test-queue"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|VisibilityTimeout
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MaximumMessageSize
operator|.
name|name
argument_list|()
argument_list|,
literal|"128"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MessageRetentionPeriod
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|Policy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|ReceiveMessageWaitTimeSeconds
operator|.
name|name
argument_list|()
argument_list|,
literal|"5"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|KmsMasterKeyId
operator|.
name|name
argument_list|()
argument_list|,
literal|"keyMaster1"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|KmsDataKeyReusePeriodSeconds
operator|.
name|name
argument_list|()
argument_list|,
literal|"300"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|RedrivePolicy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|createQueue
argument_list|(
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|CreateQueueRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|CreateQueueResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|createQueue
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|createQueue
argument_list|(
name|expectedCreateQueueRequest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|,
name|endpoint
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createQueueWithoutSSEShouldNotCreateStandardQueueWithSSESet ()
specifier|public
name|void
name|createQueueWithoutSSEShouldNotCreateStandardQueueWithSSESet
parameter_list|()
block|{
name|config
operator|.
name|setDefaultVisibilityTimeout
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMaximumMessageSize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|config
operator|.
name|setMessageRetentionPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|config
operator|.
name|setPolicy
argument_list|(
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReceiveMessageWaitTimeSeconds
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRedrivePolicy
argument_list|(
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setKmsMasterKeyId
argument_list|(
literal|"keyMaster1"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setKmsDataKeyReusePeriodSeconds
argument_list|(
literal|300
argument_list|)
expr_stmt|;
name|CreateQueueRequest
name|expectedCreateQueueRequest
init|=
operator|new
name|CreateQueueRequest
argument_list|(
literal|"test-queue"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|VisibilityTimeout
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MaximumMessageSize
operator|.
name|name
argument_list|()
argument_list|,
literal|"128"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|MessageRetentionPeriod
operator|.
name|name
argument_list|()
argument_list|,
literal|"1000"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|Policy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{\"Version\": \"2012-10-17\"}"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|ReceiveMessageWaitTimeSeconds
operator|.
name|name
argument_list|()
argument_list|,
literal|"5"
argument_list|)
operator|.
name|addAttributesEntry
argument_list|(
name|QueueAttributeName
operator|.
name|RedrivePolicy
operator|.
name|name
argument_list|()
argument_list|,
literal|"{ \"deadLetterTargetArn\" : String, \"maxReceiveCount\" : Integer }"
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|amazonSQSClient
operator|.
name|createQueue
argument_list|(
name|ArgumentMatchers
operator|.
name|any
argument_list|(
name|CreateQueueRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|CreateQueueResult
argument_list|()
operator|.
name|withQueueUrl
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|)
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|createQueue
argument_list|(
name|amazonSQSClient
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|amazonSQSClient
argument_list|)
operator|.
name|createQueue
argument_list|(
name|expectedCreateQueueRequest
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"https://sqs.us-east-1.amazonaws.com/111222333/test-queue"
argument_list|,
name|endpoint
operator|.
name|getQueueUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

