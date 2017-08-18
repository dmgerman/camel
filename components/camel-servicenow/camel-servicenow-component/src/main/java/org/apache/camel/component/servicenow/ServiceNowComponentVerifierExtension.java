begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|NoSuchOptionException
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ServiceNowComponentVerifierExtension
specifier|final
class|class
name|ServiceNowComponentVerifierExtension
extends|extends
name|DefaultComponentVerifierExtension
block|{
DECL|method|ServiceNowComponentVerifierExtension ()
name|ServiceNowComponentVerifierExtension
parameter_list|()
block|{
name|super
argument_list|(
literal|"servicenow"
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
comment|// Default is success
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
specifier|final
name|ServiceNowConfiguration
name|configuration
init|=
name|getServiceNowConfiguration
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
specifier|final
name|ServiceNowClient
name|client
init|=
name|getServiceNowClient
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
specifier|final
name|String
name|tableName
init|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|configuration
operator|.
name|getTable
argument_list|()
argument_list|,
parameter_list|()
lambda|->
literal|"incident"
argument_list|)
decl_stmt|;
name|client
operator|.
name|reset
argument_list|()
operator|.
name|types
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
argument_list|)
operator|.
name|path
argument_list|(
literal|"now"
argument_list|)
operator|.
name|path
argument_list|(
name|configuration
operator|.
name|getApiVersion
argument_list|()
argument_list|)
operator|.
name|path
argument_list|(
literal|"table"
argument_list|)
operator|.
name|path
argument_list|(
name|tableName
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_LIMIT
operator|.
name|getId
argument_list|()
argument_list|,
literal|1L
argument_list|)
operator|.
name|query
argument_list|(
name|ServiceNowParams
operator|.
name|SYSPARM_FIELDS
operator|.
name|getId
argument_list|()
argument_list|,
literal|"sys_id"
argument_list|)
operator|.
name|invoke
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchOptionException
name|e
parameter_list|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
name|e
operator|.
name|getOptionName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ServiceNowException
name|e
parameter_list|)
block|{
name|ResultErrorBuilder
name|errorBuilder
init|=
name|ResultErrorBuilder
operator|.
name|withException
argument_list|(
name|e
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_CODE
argument_list|,
name|e
operator|.
name|getCode
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
literal|"servicenow_error_message"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
literal|"servicenow_error_status"
argument_list|,
name|e
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|detail
argument_list|(
literal|"servicenow_error_detail"
argument_list|,
name|e
operator|.
name|getDetail
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|.
name|getCode
argument_list|()
operator|==
literal|401
condition|)
block|{
name|errorBuilder
operator|.
name|code
argument_list|(
name|VerificationError
operator|.
name|StandardCode
operator|.
name|AUTHENTICATION
argument_list|)
expr_stmt|;
name|errorBuilder
operator|.
name|parameterKey
argument_list|(
literal|"userName"
argument_list|)
expr_stmt|;
name|errorBuilder
operator|.
name|parameterKey
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|errorBuilder
operator|.
name|parameterKey
argument_list|(
literal|"oauthClientId"
argument_list|)
expr_stmt|;
name|errorBuilder
operator|.
name|parameterKey
argument_list|(
literal|"oauthClientSecret"
argument_list|)
expr_stmt|;
block|}
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
comment|// *********************************
comment|// Helpers
comment|// *********************************
DECL|method|getInstanceName (Map<String, Object> parameters)
specifier|private
name|String
name|getInstanceName
parameter_list|(
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
name|String
name|instanceName
init|=
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
literal|"instanceName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|instanceName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getComponent
argument_list|()
argument_list|)
condition|)
block|{
name|instanceName
operator|=
name|getComponent
argument_list|(
name|ServiceNowComponent
operator|.
name|class
argument_list|)
operator|.
name|getInstanceName
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|instanceName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NoSuchOptionException
argument_list|(
literal|"instanceName"
argument_list|)
throw|;
block|}
return|return
name|instanceName
return|;
block|}
DECL|method|getServiceNowClient (ServiceNowConfiguration configuration, Map<String, Object> parameters)
specifier|private
name|ServiceNowClient
name|getServiceNowClient
parameter_list|(
name|ServiceNowConfiguration
name|configuration
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
name|ServiceNowClient
name|client
init|=
literal|null
decl_stmt|;
comment|// check if a client has been supplied to the parameters map
for|for
control|(
name|Object
name|value
range|:
name|parameters
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|ServiceNowClient
condition|)
block|{
name|client
operator|=
name|ServiceNowClient
operator|.
name|class
operator|.
name|cast
argument_list|(
name|value
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// if no client is provided
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|client
argument_list|)
condition|)
block|{
specifier|final
name|String
name|instanceName
init|=
name|getInstanceName
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
comment|// Configure Api and OAuthToken ULRs using instanceName
if|if
condition|(
operator|!
name|configuration
operator|.
name|hasApiUrl
argument_list|()
condition|)
block|{
name|configuration
operator|.
name|setApiUrl
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://%s.service-now.com/api"
argument_list|,
name|instanceName
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|configuration
operator|.
name|hasOauthTokenUrl
argument_list|()
condition|)
block|{
name|configuration
operator|.
name|setOauthTokenUrl
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"https://%s.service-now.com/oauth_token.do"
argument_list|,
name|instanceName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|client
operator|=
operator|new
name|ServiceNowClient
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|getServiceNowConfiguration (Map<String, Object> parameters)
specifier|private
name|ServiceNowConfiguration
name|getServiceNowConfiguration
parameter_list|(
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
name|ServiceNowConfiguration
name|configuration
init|=
literal|null
decl_stmt|;
comment|// check if a configuration has been supplied to the parameters map
for|for
control|(
name|Object
name|value
range|:
name|parameters
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|ServiceNowConfiguration
condition|)
block|{
name|configuration
operator|=
name|ServiceNowConfiguration
operator|.
name|class
operator|.
name|cast
argument_list|(
name|value
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// if no configuration is provided
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|configuration
argument_list|)
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getComponent
argument_list|()
argument_list|)
condition|)
block|{
name|configuration
operator|=
name|getComponent
argument_list|(
name|ServiceNowComponent
operator|.
name|class
argument_list|)
operator|.
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|configuration
operator|=
operator|new
name|ServiceNowConfiguration
argument_list|()
expr_stmt|;
block|}
comment|// bind parameters to ServiceNow Configuration only if configuration
comment|// does not come from the parameters map as in that case we expect
comment|// it to be pre-configured.
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

