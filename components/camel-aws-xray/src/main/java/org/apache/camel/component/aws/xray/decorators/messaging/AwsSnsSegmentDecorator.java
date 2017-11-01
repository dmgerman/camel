begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.decorators.messaging
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
name|xray
operator|.
name|decorators
operator|.
name|messaging
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_class
DECL|class|AwsSnsSegmentDecorator
specifier|public
class|class
name|AwsSnsSegmentDecorator
extends|extends
name|AbstractMessagingSegmentDecorator
block|{
DECL|field|CAMEL_AWS_SNS_MESSAGE_ID
specifier|protected
specifier|static
specifier|final
name|String
name|CAMEL_AWS_SNS_MESSAGE_ID
init|=
literal|"CamelAwsSnsMessageId"
decl_stmt|;
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"aws-sns"
return|;
block|}
annotation|@
name|Override
DECL|method|getMessageId (Exchange exchange)
specifier|protected
name|String
name|getMessageId
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CAMEL_AWS_SNS_MESSAGE_ID
argument_list|)
return|;
block|}
block|}
end_class

end_unit

