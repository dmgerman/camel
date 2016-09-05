begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AmazonWebServiceRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|ResponseMetadata
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
name|sns
operator|.
name|AmazonSNSClient
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
name|sns
operator|.
name|model
operator|.
name|AddPermissionRequest
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
name|sns
operator|.
name|model
operator|.
name|AddPermissionResult
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
name|sns
operator|.
name|model
operator|.
name|ConfirmSubscriptionRequest
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
name|sns
operator|.
name|model
operator|.
name|ConfirmSubscriptionResult
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
name|sns
operator|.
name|model
operator|.
name|CreateTopicRequest
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
name|sns
operator|.
name|model
operator|.
name|CreateTopicResult
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
name|sns
operator|.
name|model
operator|.
name|DeleteTopicRequest
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
name|sns
operator|.
name|model
operator|.
name|DeleteTopicResult
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
name|sns
operator|.
name|model
operator|.
name|GetTopicAttributesRequest
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
name|sns
operator|.
name|model
operator|.
name|GetTopicAttributesResult
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
name|sns
operator|.
name|model
operator|.
name|ListSubscriptionsByTopicRequest
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
name|sns
operator|.
name|model
operator|.
name|ListSubscriptionsByTopicResult
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
name|sns
operator|.
name|model
operator|.
name|ListSubscriptionsRequest
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
name|sns
operator|.
name|model
operator|.
name|ListSubscriptionsResult
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
name|sns
operator|.
name|model
operator|.
name|ListTopicsRequest
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
name|sns
operator|.
name|model
operator|.
name|ListTopicsResult
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
name|sns
operator|.
name|model
operator|.
name|PublishRequest
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
name|sns
operator|.
name|model
operator|.
name|PublishResult
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
name|sns
operator|.
name|model
operator|.
name|RemovePermissionRequest
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
name|sns
operator|.
name|model
operator|.
name|RemovePermissionResult
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
name|sns
operator|.
name|model
operator|.
name|SetTopicAttributesRequest
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
name|sns
operator|.
name|model
operator|.
name|SetTopicAttributesResult
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
name|sns
operator|.
name|model
operator|.
name|SubscribeRequest
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
name|sns
operator|.
name|model
operator|.
name|SubscribeResult
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
name|sns
operator|.
name|model
operator|.
name|Topic
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
name|sns
operator|.
name|model
operator|.
name|UnsubscribeRequest
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
name|sns
operator|.
name|model
operator|.
name|UnsubscribeResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_class
DECL|class|AmazonSNSClientMock
specifier|public
class|class
name|AmazonSNSClientMock
extends|extends
name|AmazonSNSClient
block|{
DECL|field|DEFAULT_TOPIC_ARN
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_TOPIC_ARN
init|=
literal|"arn:aws:sns:us-east-1:541925086079:MyTopic"
decl_stmt|;
DECL|field|endpoint
specifier|private
name|String
name|endpoint
decl_stmt|;
DECL|method|AmazonSNSClientMock ()
specifier|public
name|AmazonSNSClientMock
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
DECL|method|setEndpoint (String endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|String
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|String
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|confirmSubscription (ConfirmSubscriptionRequest confirmSubscriptionRequest)
specifier|public
name|ConfirmSubscriptionResult
name|confirmSubscription
parameter_list|(
name|ConfirmSubscriptionRequest
name|confirmSubscriptionRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|getTopicAttributes (GetTopicAttributesRequest getTopicAttributesRequest)
specifier|public
name|GetTopicAttributesResult
name|getTopicAttributes
parameter_list|(
name|GetTopicAttributesRequest
name|getTopicAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|subscribe (SubscribeRequest subscribeRequest)
specifier|public
name|SubscribeResult
name|subscribe
parameter_list|(
name|SubscribeRequest
name|subscribeRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|setTopicAttributes (SetTopicAttributesRequest setTopicAttributesRequest)
specifier|public
name|SetTopicAttributesResult
name|setTopicAttributes
parameter_list|(
name|SetTopicAttributesRequest
name|setTopicAttributesRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|DEFAULT_TOPIC_ARN
argument_list|,
name|setTopicAttributesRequest
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Policy"
argument_list|,
name|setTopicAttributesRequest
operator|.
name|getAttributeName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"XXX"
argument_list|,
name|setTopicAttributesRequest
operator|.
name|getAttributeValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|SetTopicAttributesResult
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|deleteTopic (DeleteTopicRequest deleteTopicRequest)
specifier|public
name|DeleteTopicResult
name|deleteTopic
parameter_list|(
name|DeleteTopicRequest
name|deleteTopicRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|removePermission (RemovePermissionRequest removePermissionRequest)
specifier|public
name|RemovePermissionResult
name|removePermission
parameter_list|(
name|RemovePermissionRequest
name|removePermissionRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listSubscriptions (ListSubscriptionsRequest listSubscriptionsRequest)
specifier|public
name|ListSubscriptionsResult
name|listSubscriptions
parameter_list|(
name|ListSubscriptionsRequest
name|listSubscriptionsRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|addPermission (AddPermissionRequest addPermissionRequest)
specifier|public
name|AddPermissionResult
name|addPermission
parameter_list|(
name|AddPermissionRequest
name|addPermissionRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|createTopic (CreateTopicRequest createTopicRequest)
specifier|public
name|CreateTopicResult
name|createTopic
parameter_list|(
name|CreateTopicRequest
name|createTopicRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|CreateTopicResult
name|createTopicResult
init|=
operator|new
name|CreateTopicResult
argument_list|()
decl_stmt|;
name|createTopicResult
operator|.
name|setTopicArn
argument_list|(
name|DEFAULT_TOPIC_ARN
argument_list|)
expr_stmt|;
return|return
name|createTopicResult
return|;
block|}
annotation|@
name|Override
DECL|method|listTopics (ListTopicsRequest listTopicsRequest)
specifier|public
name|ListTopicsResult
name|listTopics
parameter_list|(
name|ListTopicsRequest
name|listTopicsRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|unsubscribe (UnsubscribeRequest unsubscribeRequest)
specifier|public
name|UnsubscribeResult
name|unsubscribe
parameter_list|(
name|UnsubscribeRequest
name|unsubscribeRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listSubscriptionsByTopic (ListSubscriptionsByTopicRequest listSubscriptionsByTopicRequest)
specifier|public
name|ListSubscriptionsByTopicResult
name|listSubscriptionsByTopic
parameter_list|(
name|ListSubscriptionsByTopicRequest
name|listSubscriptionsByTopicRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|publish (PublishRequest publishRequest)
specifier|public
name|PublishResult
name|publish
parameter_list|(
name|PublishRequest
name|publishRequest
parameter_list|)
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
name|PublishResult
name|publishResult
init|=
operator|new
name|PublishResult
argument_list|()
decl_stmt|;
name|publishResult
operator|.
name|setMessageId
argument_list|(
literal|"dcc8ce7a-7f18-4385-bedd-b97984b4363c"
argument_list|)
expr_stmt|;
return|return
name|publishResult
return|;
block|}
annotation|@
name|Override
DECL|method|listSubscriptions ()
specifier|public
name|ListSubscriptionsResult
name|listSubscriptions
parameter_list|()
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listTopics ()
specifier|public
name|ListTopicsResult
name|listTopics
parameter_list|()
throws|throws
name|AmazonServiceException
throws|,
name|AmazonClientException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
DECL|method|listTopics (String nextToken)
specifier|public
name|ListTopicsResult
name|listTopics
parameter_list|(
name|String
name|nextToken
parameter_list|)
block|{
name|ListTopicsResult
name|res
init|=
operator|new
name|ListTopicsResult
argument_list|()
decl_stmt|;
name|Topic
name|topic
init|=
operator|new
name|Topic
argument_list|()
decl_stmt|;
name|topic
operator|.
name|setTopicArn
argument_list|(
name|DEFAULT_TOPIC_ARN
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Topic
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Topic
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|topic
argument_list|)
expr_stmt|;
name|res
operator|.
name|setTopics
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
annotation|@
name|Override
DECL|method|getCachedResponseMetadata (AmazonWebServiceRequest request)
specifier|public
name|ResponseMetadata
name|getCachedResponseMetadata
parameter_list|(
name|AmazonWebServiceRequest
name|request
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

