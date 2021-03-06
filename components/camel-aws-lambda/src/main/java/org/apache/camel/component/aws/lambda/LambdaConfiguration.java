begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.lambda
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
name|lambda
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|Protocol
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
name|lambda
operator|.
name|AWSLambda
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
name|RuntimeCamelException
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
DECL|class|LambdaConfiguration
specifier|public
class|class
name|LambdaConfiguration
implements|implements
name|Cloneable
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|function
specifier|private
name|String
name|function
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|LambdaOperations
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|accessKey
specifier|private
name|String
name|accessKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|secretKey
specifier|private
name|String
name|secretKey
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|region
specifier|private
name|String
name|region
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"HTTP,HTTPS"
argument_list|,
name|defaultValue
operator|=
literal|"HTTPS"
argument_list|)
DECL|field|proxyProtocol
specifier|private
name|Protocol
name|proxyProtocol
init|=
name|Protocol
operator|.
name|HTTPS
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyHost
specifier|private
name|String
name|proxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|proxyPort
specifier|private
name|Integer
name|proxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|awsLambdaClient
specifier|private
name|AWSLambda
name|awsLambdaClient
decl_stmt|;
DECL|method|getFunction ()
specifier|public
name|String
name|getFunction
parameter_list|()
block|{
return|return
name|function
return|;
block|}
comment|/**      * Name of the Lambda function.      */
DECL|method|setFunction (String function)
specifier|public
name|void
name|setFunction
parameter_list|(
name|String
name|function
parameter_list|)
block|{
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
block|}
DECL|method|getAwsLambdaClient ()
specifier|public
name|AWSLambda
name|getAwsLambdaClient
parameter_list|()
block|{
return|return
name|awsLambdaClient
return|;
block|}
comment|/**      * To use a existing configured AwsLambdaClient as client      */
DECL|method|setAwsLambdaClient (AWSLambda awsLambdaClient)
specifier|public
name|void
name|setAwsLambdaClient
parameter_list|(
name|AWSLambda
name|awsLambdaClient
parameter_list|)
block|{
name|this
operator|.
name|awsLambdaClient
operator|=
name|awsLambdaClient
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
DECL|method|getRegion ()
specifier|public
name|String
name|getRegion
parameter_list|()
block|{
return|return
name|region
return|;
block|}
comment|/**      * Amazon AWS Region. When using this parameter, the configuration will expect the capitalized name of the region (for example AP_EAST_1)      * You'll need to use the name Regions.EU_WEST_1.name()      */
DECL|method|setRegion (String region)
specifier|public
name|void
name|setRegion
parameter_list|(
name|String
name|region
parameter_list|)
block|{
name|this
operator|.
name|region
operator|=
name|region
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|LambdaOperations
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The operation to perform. It can be listFunctions, getFunction, createFunction, deleteFunction or invokeFunction      */
DECL|method|setOperation (LambdaOperations operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|LambdaOperations
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
DECL|method|getProxyProtocol ()
specifier|public
name|Protocol
name|getProxyProtocol
parameter_list|()
block|{
return|return
name|proxyProtocol
return|;
block|}
comment|/**      * To define a proxy protocol when instantiating the Lambda client      */
DECL|method|setProxyProtocol (Protocol proxyProtocol)
specifier|public
name|void
name|setProxyProtocol
parameter_list|(
name|Protocol
name|proxyProtocol
parameter_list|)
block|{
name|this
operator|.
name|proxyProtocol
operator|=
name|proxyProtocol
expr_stmt|;
block|}
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
comment|/**      * To define a proxy host when instantiating the Lambda client      */
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
comment|/**      * To define a proxy port when instantiating the Lambda client      */
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
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|LambdaConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|LambdaConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

