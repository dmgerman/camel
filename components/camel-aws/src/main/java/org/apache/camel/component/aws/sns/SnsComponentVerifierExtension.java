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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|SdkClientException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|DefaultComponentVerifierExtension
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultBuilder
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorBuilder
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
name|component
operator|.
name|extension
operator|.
name|verifier
operator|.
name|ResultErrorHelper
import|;
end_import

begin_class
DECL|class|SnsComponentVerifierExtension
specifier|public
class|class
name|SnsComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|method|SnsComponentVerifierExtension ()
specifier|public
name|SnsComponentVerifierExtension
parameter_list|()
block|{
name|this
argument_list|(
literal|"aws-sns"
argument_list|)
expr_stmt|;
block|}
DECL|method|SnsComponentVerifierExtension (String scheme)
specifier|public
name|SnsComponentVerifierExtension
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|super
argument_list|(
name|scheme
argument_list|)
expr_stmt|;
block|}
comment|// *********************************
comment|// Parameters validation
comment|// *********************************
annotation|@
name|Override
DECL|method|verifyParameters (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|ResultBuilder
name|builder
init|=
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|PARAMETERS
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"accessKey"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"secretKey"
argument_list|,
name|parameters
argument_list|)
argument_list|)
operator|.
name|error
argument_list|(
name|ResultErrorHelper
operator|.
name|requiresOption
argument_list|(
literal|"region"
argument_list|,
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
comment|// Validate using the catalog
name|super
operator|.
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
comment|// *********************************
comment|// Connectivity validation
comment|// *********************************
annotation|@
name|Override
DECL|method|verifyConnectivity (Map<String, Object> parameters)
specifier|protected
name|Result
name|verifyConnectivity
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|ResultBuilder
name|builder
init|=
name|ResultBuilder
operator|.
name|withStatusAndScope
argument_list|(
name|Result
operator|.
name|Status
operator|.
name|OK
argument_list|,
name|Scope
operator|.
name|CONNECTIVITY
argument_list|)
decl_stmt|;
try|try
block|{
name|SnsConfiguration
name|configuration
init|=
name|setProperties
argument_list|(
operator|new
name|SnsConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
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
name|AmazonSNS
name|client
init|=
name|AmazonSNSClientBuilder
operator|.
name|standard
argument_list|()
operator|.
name|withCredentials
argument_list|(
name|credentialsProvider
argument_list|)
operator|.
name|withRegion
argument_list|(
name|configuration
operator|.
name|getRegion
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|listSubscriptions
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SdkClientException
name|e
parameter_list|)
block|{
name|ResultErrorBuilder
name|errorBuilder
init|=
name|ResultErrorBuilder
operator|.
name|withCodeAndDescription
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
literal|"aws_sns_exception_message"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_CLASS
argument_list|,
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|ExceptionAttribute
operator|.
name|EXCEPTION_INSTANCE
argument_list|,
name|e
argument_list|)
decl_stmt|;
name|builder
operator|.
name|error
argument_list|(
name|errorBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withException
argument_list|(
name|e
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

