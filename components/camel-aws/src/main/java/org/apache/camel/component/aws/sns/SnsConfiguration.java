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

begin_comment
comment|/**  * The AWS SNS component configuration properties  *   */
end_comment

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

begin_class
DECL|class|SnsConfiguration
specifier|public
class|class
name|SnsConfiguration
implements|implements
name|Cloneable
block|{
comment|// Common properties
DECL|field|topicName
specifier|private
name|String
name|topicName
decl_stmt|;
DECL|field|amazonSNSClient
specifier|private
name|AmazonSNSClient
name|amazonSNSClient
decl_stmt|;
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
comment|// Producer only properties
DECL|field|subject
specifier|private
name|String
name|subject
decl_stmt|;
DECL|field|topicArn
specifier|private
name|String
name|topicArn
decl_stmt|;
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
name|AmazonSNSClient
name|getAmazonSNSClient
parameter_list|()
block|{
return|return
name|amazonSNSClient
return|;
block|}
DECL|method|setAmazonSNSClient (AmazonSNSClient amazonSNSClient)
specifier|public
name|void
name|setAmazonSNSClient
parameter_list|(
name|AmazonSNSClient
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
literal|", amazonSQSClient="
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
literal|"]"
return|;
block|}
block|}
end_class

end_unit

