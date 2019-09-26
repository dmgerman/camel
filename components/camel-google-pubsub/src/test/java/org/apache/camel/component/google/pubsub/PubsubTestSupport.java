begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.pubsub
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|pubsub
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|client
operator|.
name|googleapis
operator|.
name|json
operator|.
name|GoogleJsonResponseException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|pubsub
operator|.
name|Pubsub
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|pubsub
operator|.
name|model
operator|.
name|Subscription
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|pubsub
operator|.
name|model
operator|.
name|Topic
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
name|CamelContext
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

begin_class
DECL|class|PubsubTestSupport
specifier|public
class|class
name|PubsubTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|SERVICE_KEY
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_KEY
decl_stmt|;
DECL|field|SERVICE_ACCOUNT
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_ACCOUNT
decl_stmt|;
DECL|field|PROJECT_ID
specifier|public
specifier|static
specifier|final
name|String
name|PROJECT_ID
decl_stmt|;
DECL|field|SERVICE_URL
specifier|public
specifier|static
specifier|final
name|String
name|SERVICE_URL
decl_stmt|;
static|static
block|{
name|Properties
name|testProperties
init|=
name|loadProperties
argument_list|()
decl_stmt|;
name|SERVICE_KEY
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"service.key"
argument_list|)
expr_stmt|;
name|SERVICE_ACCOUNT
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"service.account"
argument_list|)
expr_stmt|;
name|PROJECT_ID
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"project.id"
argument_list|)
expr_stmt|;
name|SERVICE_URL
operator|=
name|testProperties
operator|.
name|getProperty
argument_list|(
literal|"test.serviceURL"
argument_list|)
expr_stmt|;
block|}
DECL|method|loadProperties ()
specifier|private
specifier|static
name|Properties
name|loadProperties
parameter_list|()
block|{
name|Properties
name|testProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|InputStream
name|fileIn
init|=
name|PubsubTestSupport
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"simple.properties"
argument_list|)
decl_stmt|;
try|try
block|{
name|testProperties
operator|.
name|load
argument_list|(
name|fileIn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|testProperties
return|;
block|}
DECL|method|addPubsubComponent (CamelContext context)
specifier|protected
name|void
name|addPubsubComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|GooglePubsubConnectionFactory
name|cf
init|=
operator|new
name|GooglePubsubConnectionFactory
argument_list|()
operator|.
name|setServiceAccount
argument_list|(
name|SERVICE_ACCOUNT
argument_list|)
operator|.
name|setServiceAccountKey
argument_list|(
name|SERVICE_KEY
argument_list|)
operator|.
name|setServiceURL
argument_list|(
name|SERVICE_URL
argument_list|)
decl_stmt|;
name|GooglePubsubComponent
name|component
init|=
operator|new
name|GooglePubsubComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"google-pubsub"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|context
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"ref:prop"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"prop"
argument_list|)
DECL|method|loadRegProperties ()
specifier|public
name|Properties
name|loadRegProperties
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loadProperties
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|addPubsubComponent
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|createTopicSubscriptionPair (String topicName, String subscriptionName)
specifier|public
specifier|static
name|void
name|createTopicSubscriptionPair
parameter_list|(
name|String
name|topicName
parameter_list|,
name|String
name|subscriptionName
parameter_list|)
throws|throws
name|Exception
block|{
name|createTopicSubscriptionPair
argument_list|(
name|topicName
argument_list|,
name|subscriptionName
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
DECL|method|createTopicSubscriptionPair (String topicName, String subscriptionName, int ackDealineSeconds)
specifier|public
specifier|static
name|void
name|createTopicSubscriptionPair
parameter_list|(
name|String
name|topicName
parameter_list|,
name|String
name|subscriptionName
parameter_list|,
name|int
name|ackDealineSeconds
parameter_list|)
throws|throws
name|Exception
block|{
name|Pubsub
name|pubsub
init|=
operator|new
name|GooglePubsubConnectionFactory
argument_list|()
operator|.
name|setServiceAccount
argument_list|(
name|SERVICE_ACCOUNT
argument_list|)
operator|.
name|setServiceAccountKey
argument_list|(
name|SERVICE_KEY
argument_list|)
operator|.
name|setServiceURL
argument_list|(
name|SERVICE_URL
argument_list|)
operator|.
name|getDefaultClient
argument_list|()
decl_stmt|;
name|String
name|topicFullName
init|=
name|String
operator|.
name|format
argument_list|(
literal|"projects/%s/topics/%s"
argument_list|,
name|PubsubTestSupport
operator|.
name|PROJECT_ID
argument_list|,
name|topicName
argument_list|)
decl_stmt|;
name|String
name|subscriptionFullName
init|=
name|String
operator|.
name|format
argument_list|(
literal|"projects/%s/subscriptions/%s"
argument_list|,
name|PubsubTestSupport
operator|.
name|PROJECT_ID
argument_list|,
name|subscriptionName
argument_list|)
decl_stmt|;
try|try
block|{
name|pubsub
operator|.
name|projects
argument_list|()
operator|.
name|topics
argument_list|()
operator|.
name|create
argument_list|(
name|topicFullName
argument_list|,
operator|new
name|Topic
argument_list|()
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleAlreadyExistsException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Subscription
name|subscription
init|=
operator|new
name|Subscription
argument_list|()
operator|.
name|setTopic
argument_list|(
name|topicFullName
argument_list|)
operator|.
name|setAckDeadlineSeconds
argument_list|(
name|ackDealineSeconds
argument_list|)
decl_stmt|;
name|pubsub
operator|.
name|projects
argument_list|()
operator|.
name|subscriptions
argument_list|()
operator|.
name|create
argument_list|(
name|subscriptionFullName
argument_list|,
name|subscription
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleAlreadyExistsException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleAlreadyExistsException (Exception e)
specifier|private
specifier|static
name|void
name|handleAlreadyExistsException
parameter_list|(
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|e
operator|instanceof
name|GoogleJsonResponseException
condition|)
block|{
name|GoogleJsonResponseException
name|exc
init|=
operator|(
name|GoogleJsonResponseException
operator|)
name|e
decl_stmt|;
comment|// 409 indicates that the resource is available already
if|if
condition|(
literal|409
operator|==
name|exc
operator|.
name|getStatusCode
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
throw|throw
name|e
throw|;
block|}
block|}
end_class

end_unit

