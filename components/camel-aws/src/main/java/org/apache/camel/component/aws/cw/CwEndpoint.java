begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.cw
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
name|cw
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
name|cloudwatch
operator|.
name|AmazonCloudWatch
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
name|cloudwatch
operator|.
name|AmazonCloudWatchClient
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
name|impl
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://aws.amazon.com/cloudwatch/">AWS CloudWatch Endpoint</a>  */
end_comment

begin_class
DECL|class|CwEndpoint
specifier|public
class|class
name|CwEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|configuration
specifier|private
name|CwConfiguration
name|configuration
decl_stmt|;
DECL|field|cloudWatchClient
specifier|private
name|AmazonCloudWatch
name|cloudWatchClient
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|CwEndpoint (String uri, CamelContext context, CwConfiguration configuration)
specifier|public
name|CwEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|CwConfiguration
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
DECL|method|CwEndpoint (String uri, Component component, CwConfiguration configuration)
specifier|public
name|CwEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|CwConfiguration
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
name|CwProducer
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
name|cloudWatchClient
operator|=
name|configuration
operator|.
name|getAmazonCwClient
argument_list|()
operator|!=
literal|null
condition|?
name|configuration
operator|.
name|getAmazonCwClient
argument_list|()
else|:
name|createCloudWatchClient
argument_list|()
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configuration
operator|.
name|getAmazonCwEndpoint
argument_list|()
argument_list|)
condition|)
block|{
name|cloudWatchClient
operator|.
name|setEndpoint
argument_list|(
name|configuration
operator|.
name|getAmazonCwEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|CwConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (CwConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|CwConfiguration
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
DECL|method|setCloudWatchClient (AmazonCloudWatch cloudWatchClient)
specifier|public
name|void
name|setCloudWatchClient
parameter_list|(
name|AmazonCloudWatch
name|cloudWatchClient
parameter_list|)
block|{
name|this
operator|.
name|cloudWatchClient
operator|=
name|cloudWatchClient
expr_stmt|;
block|}
DECL|method|getCloudWatchClient ()
specifier|public
name|AmazonCloudWatch
name|getCloudWatchClient
parameter_list|()
block|{
return|return
name|cloudWatchClient
return|;
block|}
DECL|method|createCloudWatchClient ()
name|AmazonCloudWatch
name|createCloudWatchClient
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
name|AmazonCloudWatch
name|client
init|=
operator|new
name|AmazonCloudWatchClient
argument_list|(
name|credentials
argument_list|)
decl_stmt|;
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

