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
name|ClientConfiguration
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
name|AWSCredentials
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
name|AWSCredentialsProvider
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
name|AWSStaticCredentialsProvider
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
name|regions
operator|.
name|Regions
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
name|AmazonSNS
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
name|AmazonSNSClientBuilder
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
name|Component
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
name|Consumer
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
name|Producer
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
name|support
operator|.
name|DefaultEndpoint
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|spi
operator|.
name|HeaderFilterStrategyAware
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The aws-sns component is used for sending messages to an Amazon Simple Notification Topic.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.8.0"
argument_list|,
name|scheme
operator|=
literal|"aws-sns"
argument_list|,
name|title
operator|=
literal|"AWS Simple Notification System"
argument_list|,
name|syntax
operator|=
literal|"aws-sns:topicNameOrArn"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"cloud,mobile,messaging"
argument_list|)
DECL|class|SnsEndpoint
specifier|public
class|class
name|SnsEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|snsClient
specifier|private
name|AmazonSNS
name|snsClient
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Topic name or ARN"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|topicNameOrArn
specifier|private
name|String
name|topicNameOrArn
decl_stmt|;
comment|// to support component docs
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|SnsConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|SnsEndpoint (String uri, Component component, SnsConfiguration configuration)
specifier|public
name|SnsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|SnsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to map headers to/from Camel.      */
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"You cannot receive messages from this endpoint"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SnsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|snsClient
operator|=
name|configuration
operator|.
name|getAmazonSNSClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonSNSClient
argument_list|()
else|:
name|createSNSClient
argument_list|()
expr_stmt|;
comment|// check the setting the headerFilterStrategy
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|SnsHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getTopicArn
argument_list|()
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|String
name|nextToken
init|=
literal|null
decl_stmt|;
specifier|final
name|String
name|arnSuffix
init|=
literal|":"
operator|+
name|configuration
operator|.
name|getTopicName
argument_list|()
decl_stmt|;
do|do
block|{
specifier|final
name|ListTopicsResult
name|response
init|=
name|snsClient
operator|.
name|listTopics
argument_list|(
name|nextToken
argument_list|)
decl_stmt|;
name|nextToken
operator|=
name|response
operator|.
name|getNextToken
argument_list|()
expr_stmt|;
for|for
control|(
specifier|final
name|Topic
name|topic
range|:
name|response
operator|.
name|getTopics
argument_list|()
control|)
block|{
if|if
condition|(
name|topic
operator|.
name|getTopicArn
argument_list|()
operator|.
name|endsWith
argument_list|(
name|arnSuffix
argument_list|)
condition|)
block|{
name|configuration
operator|.
name|setTopicArn
argument_list|(
name|topic
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
do|while
condition|(
name|nextToken
operator|!=
literal|null
condition|)
do|;
block|}
catch|catch
parameter_list|(
specifier|final
name|AmazonServiceException
name|ase
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"The list topics operation return the following error code {}"
argument_list|,
name|ase
operator|.
name|getErrorCode
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ase
throw|;
block|}
block|}
if|if
condition|(
name|configuration
operator|.
name|getTopicArn
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// creates a new topic, or returns the URL of an existing one
name|CreateTopicRequest
name|request
init|=
operator|new
name|CreateTopicRequest
argument_list|(
name|configuration
operator|.
name|getTopicName
argument_list|()
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Creating topic [{}] with request [{}]..."
argument_list|,
name|configuration
operator|.
name|getTopicName
argument_list|()
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|CreateTopicResult
name|result
init|=
name|snsClient
operator|.
name|createTopic
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setTopicArn
argument_list|(
name|result
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Topic created with Amazon resource name: {}"
argument_list|,
name|configuration
operator|.
name|getTopicArn
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getPolicy
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Updating topic [{}] with policy [{}]"
argument_list|,
name|configuration
operator|.
name|getTopicArn
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
name|snsClient
operator|.
name|setTopicAttributes
argument_list|(
operator|new
name|SetTopicAttributesRequest
argument_list|(
name|configuration
operator|.
name|getTopicArn
argument_list|()
argument_list|,
literal|"Policy"
argument_list|,
name|configuration
operator|.
name|getPolicy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Topic policy updated"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getAmazonSNSClient
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|snsClient
operator|!=
literal|null
condition|)
block|{
name|snsClient
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SnsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (SnsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SnsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|setSNSClient (AmazonSNS snsClient)
specifier|public
name|void
name|setSNSClient
parameter_list|(
name|AmazonSNS
name|snsClient
parameter_list|)
block|{
name|this
operator|.
name|snsClient
operator|=
name|snsClient
expr_stmt|;
block|}
DECL|method|getSNSClient ()
specifier|public
name|AmazonSNS
name|getSNSClient
parameter_list|()
block|{
return|return
name|snsClient
return|;
block|}
comment|/**      * Provide the possibility to override this method for an mock implementation      *      * @return AmazonSNSClient      */
DECL|method|createSNSClient ()
name|AmazonSNS
name|createSNSClient
parameter_list|()
block|{
name|AmazonSNS
name|client
init|=
literal|null
decl_stmt|;
name|AmazonSNSClientBuilder
name|clientBuilder
init|=
literal|null
decl_stmt|;
name|ClientConfiguration
name|clientConfiguration
init|=
literal|null
decl_stmt|;
name|boolean
name|isClientConfigFound
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
condition|)
block|{
name|clientConfiguration
operator|=
operator|new
name|ClientConfiguration
argument_list|()
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyHost
argument_list|(
name|configuration
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|clientConfiguration
operator|.
name|setProxyPort
argument_list|(
name|configuration
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
name|isClientConfigFound
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
operator|!=
literal|null
operator|&&
name|configuration
operator|.
name|getSecretKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|AWSCredentials
name|credentials
init|=
operator|new
name|BasicAWSCredentials
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
decl_stmt|;
name|AWSCredentialsProvider
name|credentialsProvider
init|=
operator|new
name|AWSStaticCredentialsProvider
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
if|if
condition|(
name|isClientConfigFound
condition|)
block|{
name|clientBuilder
operator|=
name|AmazonSNSClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonSNSClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isClientConfigFound
condition|)
block|{
name|clientBuilder
operator|=
name|AmazonSNSClientBuilder
operator|.
name|standard
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|clientBuilder
operator|=
name|AmazonSNSClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withClientConfiguration
argument_list|(
name|clientConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|clientBuilder
operator|=
name|clientBuilder
operator|.
name|withRegion
argument_list|(
name|Regions
operator|.
name|valueOf
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
name|clientBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

