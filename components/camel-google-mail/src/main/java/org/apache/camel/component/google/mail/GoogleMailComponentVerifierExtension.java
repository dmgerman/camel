begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
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
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|Gmail
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
DECL|class|GoogleMailComponentVerifierExtension
specifier|public
class|class
name|GoogleMailComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|method|GoogleMailComponentVerifierExtension ()
specifier|public
name|GoogleMailComponentVerifierExtension
parameter_list|()
block|{
name|this
argument_list|(
literal|"google-mail"
argument_list|)
expr_stmt|;
block|}
DECL|method|GoogleMailComponentVerifierExtension (String scheme)
specifier|public
name|GoogleMailComponentVerifierExtension
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
literal|"applicationName"
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
literal|"clientId"
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
literal|"clientSecret"
argument_list|,
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"PMD.AvoidCatchingGenericException"
argument_list|)
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
name|GoogleMailConfiguration
name|configuration
init|=
name|setProperties
argument_list|(
operator|new
name|GoogleMailConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|GoogleMailClientFactory
name|clientFactory
init|=
operator|new
name|BatchGoogleMailClientFactory
argument_list|()
decl_stmt|;
name|Gmail
name|client
init|=
name|clientFactory
operator|.
name|makeClient
argument_list|(
name|configuration
operator|.
name|getClientId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getClientSecret
argument_list|()
argument_list|,
name|configuration
operator|.
name|getApplicationName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getRefreshToken
argument_list|()
argument_list|,
name|configuration
operator|.
name|getAccessToken
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|.
name|users
argument_list|()
operator|.
name|getProfile
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"userId"
argument_list|)
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
literal|"gmail_exception_message"
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

