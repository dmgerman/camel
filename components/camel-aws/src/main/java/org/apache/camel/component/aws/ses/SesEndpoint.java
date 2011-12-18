begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses
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
name|ses
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
name|simpleemail
operator|.
name|AmazonSimpleEmailService
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
name|simpleemail
operator|.
name|AmazonSimpleEmailServiceClient
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

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/aws.html">AWS SES Endpoint</a>.    *  */
end_comment

begin_class
DECL|class|SesEndpoint
specifier|public
class|class
name|SesEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|configuration
specifier|private
name|SesConfiguration
name|configuration
decl_stmt|;
DECL|method|SesEndpoint (String uri, CamelContext context, SesConfiguration configuration)
specifier|public
name|SesEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|SesConfiguration
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
name|SesProducer
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
DECL|method|getConfiguration ()
specifier|public
name|SesConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getSESClient ()
specifier|public
name|AmazonSimpleEmailService
name|getSESClient
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAmazonSESClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonSESClient
argument_list|()
else|:
name|createSESClient
argument_list|()
return|;
block|}
DECL|method|createSESClient ()
specifier|private
name|AmazonSimpleEmailService
name|createSESClient
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
name|AmazonSimpleEmailService
name|client
init|=
operator|new
name|AmazonSimpleEmailServiceClient
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|setAmazonSESClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

