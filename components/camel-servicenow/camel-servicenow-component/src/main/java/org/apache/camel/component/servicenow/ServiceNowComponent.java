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
name|Endpoint
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
name|SSLContextParametersAware
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
name|ComponentVerifierExtension
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
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
name|support
operator|.
name|EndpointHelper
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
name|support
operator|.
name|IntrospectionSupport
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
comment|/**  * Represents the component that manages {@link ServiceNowEndpoint}.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"verifiers"
argument_list|,
name|enums
operator|=
literal|"parameters,connectivity"
argument_list|)
annotation|@
name|Component
argument_list|(
literal|"servicenow"
argument_list|)
DECL|class|ServiceNowComponent
specifier|public
class|class
name|ServiceNowComponent
extends|extends
name|DefaultComponent
implements|implements
name|SSLContextParametersAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|instanceName
specifier|private
name|String
name|instanceName
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|configuration
specifier|private
name|ServiceNowConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|useGlobalSslContextParameters
specifier|private
name|boolean
name|useGlobalSslContextParameters
decl_stmt|;
DECL|method|ServiceNowComponent ()
specifier|public
name|ServiceNowComponent
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ServiceNowComponent (CamelContext camelContext)
specifier|public
name|ServiceNowComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
operator|new
name|ServiceNowConfiguration
argument_list|()
expr_stmt|;
name|registerExtension
argument_list|(
name|ServiceNowComponentVerifierExtension
operator|::
operator|new
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
name|ServiceNowMetaDataExtension
operator|::
operator|new
argument_list|)
expr_stmt|;
block|}
comment|// ****************************************
comment|// Properties
comment|// ****************************************
DECL|method|getInstanceName ()
specifier|public
name|String
name|getInstanceName
parameter_list|()
block|{
return|return
name|instanceName
return|;
block|}
comment|/**      * The ServiceNow instance name      */
DECL|method|setInstanceName (String instanceName)
specifier|public
name|void
name|setInstanceName
parameter_list|(
name|String
name|instanceName
parameter_list|)
block|{
name|this
operator|.
name|instanceName
operator|=
name|instanceName
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|ServiceNowConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * The ServiceNow default configuration      */
DECL|method|setConfiguration (ServiceNowConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ServiceNowConfiguration
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
DECL|method|getApiUrl ()
specifier|public
name|String
name|getApiUrl
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getApiUrl
argument_list|()
return|;
block|}
comment|/**      * The ServiceNow REST API url      */
DECL|method|setApiUrl (String apiUrl)
specifier|public
name|void
name|setApiUrl
parameter_list|(
name|String
name|apiUrl
parameter_list|)
block|{
name|configuration
operator|.
name|setApiUrl
argument_list|(
name|apiUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getUserName
argument_list|()
return|;
block|}
comment|/**      * ServiceNow user account name      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|configuration
operator|.
name|setUserName
argument_list|(
name|userName
argument_list|)
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getPassword
argument_list|()
return|;
block|}
comment|/**      * ServiceNow account password      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|configuration
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
DECL|method|getOauthClientId ()
specifier|public
name|String
name|getOauthClientId
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOauthClientId
argument_list|()
return|;
block|}
comment|/**      * OAuth2 ClientID      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setOauthClientId (String oauthClientId)
specifier|public
name|void
name|setOauthClientId
parameter_list|(
name|String
name|oauthClientId
parameter_list|)
block|{
name|configuration
operator|.
name|setOauthClientId
argument_list|(
name|oauthClientId
argument_list|)
expr_stmt|;
block|}
DECL|method|getOauthClientSecret ()
specifier|public
name|String
name|getOauthClientSecret
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOauthClientSecret
argument_list|()
return|;
block|}
comment|/**      * OAuth2 ClientSecret      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setOauthClientSecret (String oauthClientSecret)
specifier|public
name|void
name|setOauthClientSecret
parameter_list|(
name|String
name|oauthClientSecret
parameter_list|)
block|{
name|configuration
operator|.
name|setOauthClientSecret
argument_list|(
name|oauthClientSecret
argument_list|)
expr_stmt|;
block|}
DECL|method|getOauthTokenUrl ()
specifier|public
name|String
name|getOauthTokenUrl
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getOauthTokenUrl
argument_list|()
return|;
block|}
comment|/**      * OAuth token Url      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setOauthTokenUrl (String oauthTokenUrl)
specifier|public
name|void
name|setOauthTokenUrl
parameter_list|(
name|String
name|oauthTokenUrl
parameter_list|)
block|{
name|configuration
operator|.
name|setOauthTokenUrl
argument_list|(
name|oauthTokenUrl
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxyHost ()
specifier|public
name|String
name|getProxyHost
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProxyHost
argument_list|()
return|;
block|}
comment|/**      * The proxy host name      * @param proxyHost      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|method|setProxyHost (String proxyHost)
specifier|public
name|void
name|setProxyHost
parameter_list|(
name|String
name|proxyHost
parameter_list|)
block|{
name|configuration
operator|.
name|setProxyHost
argument_list|(
name|proxyHost
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxyPort ()
specifier|public
name|Integer
name|getProxyPort
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProxyPort
argument_list|()
return|;
block|}
comment|/**      * The proxy port number      * @param proxyPort      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|method|setProxyPort (Integer proxyPort)
specifier|public
name|void
name|setProxyPort
parameter_list|(
name|Integer
name|proxyPort
parameter_list|)
block|{
name|configuration
operator|.
name|setProxyPort
argument_list|(
name|proxyPort
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxyUserName ()
specifier|public
name|String
name|getProxyUserName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProxyUserName
argument_list|()
return|;
block|}
comment|/**      * Username for proxy authentication      * @param proxyUserName      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced,security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setProxyUserName (String proxyUserName)
specifier|public
name|void
name|setProxyUserName
parameter_list|(
name|String
name|proxyUserName
parameter_list|)
block|{
name|configuration
operator|.
name|setProxyUserName
argument_list|(
name|proxyUserName
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxyPassword ()
specifier|public
name|String
name|getProxyPassword
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProxyPassword
argument_list|()
return|;
block|}
comment|/**      * Password for proxy authentication      * @param proxyPassword      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced,security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|method|setProxyPassword (String proxyPassword)
specifier|public
name|void
name|setProxyPassword
parameter_list|(
name|String
name|proxyPassword
parameter_list|)
block|{
name|configuration
operator|.
name|setProxyPassword
argument_list|(
name|proxyPassword
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseGlobalSslContextParameters ()
specifier|public
name|boolean
name|isUseGlobalSslContextParameters
parameter_list|()
block|{
return|return
name|this
operator|.
name|useGlobalSslContextParameters
return|;
block|}
comment|/**      * Enable usage of global SSL context parameters.      */
annotation|@
name|Override
DECL|method|setUseGlobalSslContextParameters (boolean useGlobalSslContextParameters)
specifier|public
name|void
name|setUseGlobalSslContextParameters
parameter_list|(
name|boolean
name|useGlobalSslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|useGlobalSslContextParameters
operator|=
name|useGlobalSslContextParameters
expr_stmt|;
block|}
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifierExtension
name|getVerifier
parameter_list|()
block|{
return|return
parameter_list|(
name|scope
parameter_list|,
name|parameters
parameter_list|)
lambda|->
name|getExtension
argument_list|(
name|ComponentVerifierExtension
operator|.
name|class
argument_list|)
operator|.
name|orElseThrow
argument_list|(
name|UnsupportedOperationException
operator|::
operator|new
argument_list|)
operator|.
name|verify
argument_list|(
name|scope
argument_list|,
name|parameters
argument_list|)
return|;
block|}
comment|// ****************************************
comment|// Component impl
comment|// ****************************************
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
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
specifier|final
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|ServiceNowConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|models
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"model."
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|models
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|configuration
operator|.
name|addModel
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|context
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|requestModels
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"requestModel."
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|requestModels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|configuration
operator|.
name|addRequestModel
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|context
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseModels
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"responseModel."
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|responseModels
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|configuration
operator|.
name|addResponseModel
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|EndpointHelper
operator|.
name|resolveParameter
argument_list|(
name|context
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|Class
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
comment|// If an instance is not set on the endpoint uri, use the one set on
comment|// component.
name|remaining
operator|=
name|instanceName
expr_stmt|;
block|}
name|String
name|instanceName
init|=
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|==
literal|null
condition|)
block|{
name|configuration
operator|.
name|setSslContextParameters
argument_list|(
name|retrieveGlobalSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ServiceNowEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|,
name|instanceName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

