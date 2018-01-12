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
name|java
operator|.
name|util
operator|.
name|Map
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
name|Endpoint
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
name|DefaultComponent
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|SqsComponent
specifier|public
class|class
name|SqsComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|Metadata
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|Metadata
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|SqsConfiguration
name|configuration
decl_stmt|;
DECL|method|SqsComponent ()
specifier|public
name|SqsComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SqsComponent (CamelContext context)
specifier|public
name|SqsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|SqsConfiguration
argument_list|()
expr_stmt|;
name|registerExtension
argument_list|(
operator|new
name|SqsComponentVerifierExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|SqsConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|remaining
operator|==
literal|null
operator|||
name|remaining
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Queue name must be specified."
argument_list|)
throw|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
literal|"arn:"
argument_list|)
condition|)
block|{
name|String
index|[]
name|parts
init|=
name|remaining
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|!=
literal|6
operator|||
operator|!
name|parts
index|[
literal|2
index|]
operator|.
name|equals
argument_list|(
literal|"sqs"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Queue arn must be in format arn:aws:sqs:region:account:name."
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|setRegion
argument_list|(
name|parts
index|[
literal|3
index|]
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setQueueOwnerAWSAccountId
argument_list|(
name|parts
index|[
literal|4
index|]
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setQueueName
argument_list|(
name|parts
index|[
literal|5
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|.
name|setQueueName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
argument_list|)
condition|)
block|{
name|setAccessKey
argument_list|(
name|accessKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getSecretKey
argument_list|()
argument_list|)
condition|)
block|{
name|setSecretKey
argument_list|(
name|secretKey
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
condition|)
block|{
name|setRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getAmazonSQSClient
argument_list|()
operator|==
literal|null
operator|&&
operator|(
name|configuration
operator|.
name|getAccessKey
argument_list|()
operator|==
literal|null
operator|||
name|configuration
operator|.
name|getSecretKey
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AmazonSQSClient or accessKey and secretKey must be specified."
argument_list|)
throw|;
block|}
comment|// Verify that visibilityTimeout is set if extendMessageVisibility is set to true.
if|if
condition|(
name|configuration
operator|.
name|isExtendMessageVisibility
argument_list|()
operator|&&
operator|(
name|configuration
operator|.
name|getVisibilityTimeout
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Extending message visibility (extendMessageVisibility) requires visibilityTimeout to be set on the Endpoint."
argument_list|)
throw|;
block|}
if|if
condition|(
name|configuration
operator|.
name|isFifoQueue
argument_list|()
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
operator|.
name|getMessageGroupIdStrategy
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"messageGroupIdStrategy must be set for FIFO queues."
argument_list|)
throw|;
block|}
name|SqsEndpoint
name|sqsEndpoint
init|=
operator|new
name|SqsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|sqsEndpoint
operator|.
name|setConsumerProperties
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
return|return
name|sqsEndpoint
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SqsConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The AWS SQS default configuration      */
DECL|method|setConfiguration (SqsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SqsConfiguration
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
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAccessKey
argument_list|()
return|;
block|}
comment|/**      * Amazon AWS Access Key      */
DECL|method|setAccessKey (String accessKey)
specifier|public
name|void
name|setAccessKey
parameter_list|(
name|String
name|accessKey
parameter_list|)
block|{
name|configuration
operator|.
name|setAccessKey
argument_list|(
name|accessKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSecretKey
argument_list|()
return|;
block|}
comment|/**      * Amazon AWS Secret Key      */
DECL|method|setSecretKey (String secretKey)
specifier|public
name|void
name|setSecretKey
parameter_list|(
name|String
name|secretKey
parameter_list|)
block|{
name|configuration
operator|.
name|setSecretKey
argument_list|(
name|secretKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRegion
argument_list|()
return|;
block|}
comment|/**      * Specify the queue region which could be used with queueOwnerAWSAccountId to build the service URL.      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|configuration
operator|.
name|setRegion
argument_list|(
name|region
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

