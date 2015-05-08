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
name|services
operator|.
name|sns
operator|.
name|AmazonSNS
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
name|UriParams
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

begin_class
annotation|@
name|UriParams
DECL|class|SnsConfiguration
specifier|public
class|class
name|SnsConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|topicArn
specifier|private
name|String
name|topicArn
decl_stmt|;
comment|// Common properties
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|topicName
specifier|private
name|String
name|topicName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSNSClient
specifier|private
name|AmazonSNS
name|amazonSNSClient
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonSNSEndpoint
specifier|private
name|String
name|amazonSNSEndpoint
decl_stmt|;
comment|// Producer only properties
annotation|@
name|UriParam
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
annotation|@
name|UriParam
DECL|field|policy
specifier|private
name|String
name|policy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|messageStructure
specifier|private
name|String
name|messageStructure
decl_stmt|;
comment|/**      * The region with which the AWS-SNS client wants to work with.      */
DECL|method|setAmazonSNSEndpoint (String awsSNSEndpoint)
specifier|public
name|void
name|setAmazonSNSEndpoint
parameter_list|(
name|String
name|awsSNSEndpoint
parameter_list|)
block|{
name|this
operator|.
name|amazonSNSEndpoint
operator|=
name|awsSNSEndpoint
expr_stmt|;
block|}
DECL|method|getAmazonSNSEndpoint ()
specifier|public
name|String
name|getAmazonSNSEndpoint
parameter_list|()
block|{
return|return
name|amazonSNSEndpoint
return|;
block|}
DECL|method|getSubject ()
specifier|public
name|String
name|getSubject
parameter_list|()
block|{
return|return
name|subject
return|;
block|}
comment|/**      * The subject which is used if the message header 'CamelAwsSnsSubject' is not present.      */
DECL|method|setSubject (String subject)
specifier|public
name|void
name|setSubject
parameter_list|(
name|String
name|subject
parameter_list|)
block|{
name|this
operator|.
name|subject
operator|=
name|subject
expr_stmt|;
block|}
DECL|method|getTopicArn ()
specifier|public
name|String
name|getTopicArn
parameter_list|()
block|{
return|return
name|topicArn
return|;
block|}
comment|/**      * The Amazon Resource Name (ARN) assigned to the created topic.      */
DECL|method|setTopicArn (String topicArn)
specifier|public
name|void
name|setTopicArn
parameter_list|(
name|String
name|topicArn
parameter_list|)
block|{
name|this
operator|.
name|topicArn
operator|=
name|topicArn
expr_stmt|;
block|}
DECL|method|getAccessKey ()
specifier|public
name|String
name|getAccessKey
parameter_list|()
block|{
return|return
name|accessKey
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
name|this
operator|.
name|accessKey
operator|=
name|accessKey
expr_stmt|;
block|}
DECL|method|getSecretKey ()
specifier|public
name|String
name|getSecretKey
parameter_list|()
block|{
return|return
name|secretKey
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
name|this
operator|.
name|secretKey
operator|=
name|secretKey
expr_stmt|;
block|}
DECL|method|getAmazonSNSClient ()
specifier|public
name|AmazonSNS
name|getAmazonSNSClient
parameter_list|()
block|{
return|return
name|amazonSNSClient
return|;
block|}
comment|/**      * To use the AmazonSNS as the client      */
DECL|method|setAmazonSNSClient (AmazonSNS amazonSNSClient)
specifier|public
name|void
name|setAmazonSNSClient
parameter_list|(
name|AmazonSNS
name|amazonSNSClient
parameter_list|)
block|{
name|this
operator|.
name|amazonSNSClient
operator|=
name|amazonSNSClient
expr_stmt|;
block|}
DECL|method|getTopicName ()
specifier|public
name|String
name|getTopicName
parameter_list|()
block|{
return|return
name|topicName
return|;
block|}
comment|/**      * The name of the topic      */
DECL|method|setTopicName (String topicName)
specifier|public
name|void
name|setTopicName
parameter_list|(
name|String
name|topicName
parameter_list|)
block|{
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
block|}
DECL|method|getPolicy ()
specifier|public
name|String
name|getPolicy
parameter_list|()
block|{
return|return
name|policy
return|;
block|}
comment|/**      * The policy for this queue      */
DECL|method|setPolicy (String policy)
specifier|public
name|void
name|setPolicy
parameter_list|(
name|String
name|policy
parameter_list|)
block|{
name|this
operator|.
name|policy
operator|=
name|policy
expr_stmt|;
block|}
DECL|method|getMessageStructure ()
specifier|public
name|String
name|getMessageStructure
parameter_list|()
block|{
return|return
name|messageStructure
return|;
block|}
comment|/**      * The message structure to use such as json      */
DECL|method|setMessageStructure (String messageStructure)
specifier|public
name|void
name|setMessageStructure
parameter_list|(
name|String
name|messageStructure
parameter_list|)
block|{
name|this
operator|.
name|messageStructure
operator|=
name|messageStructure
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SnsConfiguration[topicName="
operator|+
name|topicName
operator|+
literal|", amazonSNSClient="
operator|+
name|amazonSNSClient
operator|+
literal|", accessKey="
operator|+
name|accessKey
operator|+
literal|", secretKey=xxxxxxxxxxxxxxx"
operator|+
literal|", subject="
operator|+
name|subject
operator|+
literal|", topicArn="
operator|+
name|topicArn
operator|+
literal|", policy="
operator|+
name|policy
operator|+
literal|", messageStructure="
operator|+
name|messageStructure
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

