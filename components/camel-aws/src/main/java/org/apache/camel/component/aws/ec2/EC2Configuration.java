begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ec2
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
name|ec2
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
name|ec2
operator|.
name|AmazonEC2Client
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
DECL|class|EC2Configuration
specifier|public
class|class
name|EC2Configuration
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Logical name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|label
specifier|private
name|String
name|label
decl_stmt|;
annotation|@
name|UriParam
DECL|field|amazonEc2Client
specifier|private
name|AmazonEC2Client
name|amazonEc2Client
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
DECL|field|amazonEc2Endpoint
specifier|private
name|String
name|amazonEc2Endpoint
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"createAndRunInstances, startInstances, stopInstances, terminateInstances, describeInstances, "
operator|+
literal|"describeInstancesStatus, rebootInstances, monitorInstances, unmonitorInstances, createTags, deleteTags"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|operation
specifier|private
name|EC2Operations
name|operation
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
DECL|method|getAmazonEc2Client ()
specifier|public
name|AmazonEC2Client
name|getAmazonEc2Client
parameter_list|()
block|{
return|return
name|amazonEc2Client
return|;
block|}
comment|/**      * To use a existing configured AmazonEC2Client as client      */
DECL|method|setAmazonEc2Client (AmazonEC2Client amazonEc2Client)
specifier|public
name|void
name|setAmazonEc2Client
parameter_list|(
name|AmazonEC2Client
name|amazonEc2Client
parameter_list|)
block|{
name|this
operator|.
name|amazonEc2Client
operator|=
name|amazonEc2Client
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
DECL|method|getAmazonEc2Endpoint ()
specifier|public
name|String
name|getAmazonEc2Endpoint
parameter_list|()
block|{
return|return
name|amazonEc2Endpoint
return|;
block|}
comment|/**      * The region with which the AWS-EC2 client wants to work with.      */
DECL|method|setAmazonEc2Endpoint (String amazonEc2Endpoint)
specifier|public
name|void
name|setAmazonEc2Endpoint
parameter_list|(
name|String
name|amazonEc2Endpoint
parameter_list|)
block|{
name|this
operator|.
name|amazonEc2Endpoint
operator|=
name|amazonEc2Endpoint
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|EC2Operations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to perform. It can be createAndRunInstances, startInstances, stopInstances, terminateInstances,       * describeInstances, describeInstancesStatus, rebootInstances, monitorInstances, unmonitorInstances,        * createTags or deleteTags      */
DECL|method|setOperation (EC2Operations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|EC2Operations
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * To define a proxy host when instantiating the SQS client      */
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|proxyHost
return|;
block|}
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|this
operator|.
name|proxyHost
operator|=
name|proxyHost
expr_stmt|;
block|}
comment|/**      * To define a proxy port when instantiating the SQS client      */
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|proxyPort
return|;
block|}
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|this
operator|.
name|proxyPort
operator|=
name|proxyPort
expr_stmt|;
block|}
block|}
end_class

end_unit

