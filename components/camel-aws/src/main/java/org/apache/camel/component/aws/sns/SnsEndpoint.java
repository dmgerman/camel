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
name|impl
operator|.
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/aws.html">AWS SNS Endpoint</a>.    *  */
end_comment

begin_class
DECL|class|SnsEndpoint
specifier|public
class|class
name|SnsEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SnsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|SnsConfiguration
name|configuration
decl_stmt|;
DECL|field|snsClient
specifier|private
name|AmazonSNSClient
name|snsClient
decl_stmt|;
DECL|method|SnsEndpoint (String uri, CamelContext context, SnsConfiguration configuration)
specifier|public
name|SnsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|SnsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
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
name|LOG
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
name|getSNSClient
argument_list|()
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
name|LOG
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
DECL|method|setSNSClient (AmazonSNSClient snsClient)
specifier|public
name|void
name|setSNSClient
parameter_list|(
name|AmazonSNSClient
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
name|AmazonSNSClient
name|getSNSClient
parameter_list|()
block|{
if|if
condition|(
name|snsClient
operator|==
literal|null
condition|)
block|{
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
block|}
return|return
name|snsClient
return|;
block|}
comment|/**      * Provide the possibility to override this method for an mock implementation      *      * @return AmazonSNSClient      */
DECL|method|createSNSClient ()
name|AmazonSNSClient
name|createSNSClient
parameter_list|()
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
return|return
operator|new
name|AmazonSNSClient
argument_list|(
name|credentials
argument_list|)
return|;
block|}
block|}
end_class

end_unit

