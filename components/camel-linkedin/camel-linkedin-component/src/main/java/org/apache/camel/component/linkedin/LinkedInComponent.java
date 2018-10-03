begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
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
name|component
operator|.
name|linkedin
operator|.
name|api
operator|.
name|LinkedInOAuthRequestFilter
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
name|linkedin
operator|.
name|api
operator|.
name|OAuthParams
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
name|linkedin
operator|.
name|internal
operator|.
name|CachingOAuthSecureStorage
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiCollection
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiName
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
name|component
operator|.
name|AbstractApiComponent
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
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link LinkedInEndpoint}.  */
end_comment

begin_class
DECL|class|LinkedInComponent
specifier|public
class|class
name|LinkedInComponent
extends|extends
name|AbstractApiComponent
argument_list|<
name|LinkedInApiName
argument_list|,
name|LinkedInConfiguration
argument_list|,
name|LinkedInApiCollection
argument_list|>
block|{
DECL|field|requestFilter
specifier|private
name|LinkedInOAuthRequestFilter
name|requestFilter
decl_stmt|;
DECL|method|LinkedInComponent ()
specifier|public
name|LinkedInComponent
parameter_list|()
block|{
name|super
argument_list|(
name|LinkedInEndpoint
operator|.
name|class
argument_list|,
name|LinkedInApiName
operator|.
name|class
argument_list|,
name|LinkedInApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|LinkedInComponent (CamelContext context)
specifier|public
name|LinkedInComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|LinkedInEndpoint
operator|.
name|class
argument_list|,
name|LinkedInApiName
operator|.
name|class
argument_list|,
name|LinkedInApiCollection
operator|.
name|getCollection
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getApiName (String apiNameStr)
specifier|protected
name|LinkedInApiName
name|getApiName
parameter_list|(
name|String
name|apiNameStr
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
return|return
name|LinkedInApiName
operator|.
name|fromValue
argument_list|(
name|apiNameStr
argument_list|)
return|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|setConfiguration (LinkedInConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|LinkedInConfiguration
name|configuration
parameter_list|)
block|{
name|super
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * To use the shared configuration      */
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|LinkedInConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String methodName, LinkedInApiName apiName, LinkedInConfiguration endpointConfiguration)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|methodName
parameter_list|,
name|LinkedInApiName
name|apiName
parameter_list|,
name|LinkedInConfiguration
name|endpointConfiguration
parameter_list|)
block|{
name|endpointConfiguration
operator|.
name|setApiName
argument_list|(
name|apiName
argument_list|)
expr_stmt|;
name|endpointConfiguration
operator|.
name|setMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
operator|new
name|LinkedInEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|apiName
argument_list|,
name|methodName
argument_list|,
name|endpointConfiguration
argument_list|)
return|;
block|}
DECL|method|getRequestFilter (LinkedInConfiguration endpointConfiguration)
specifier|public
specifier|synchronized
name|LinkedInOAuthRequestFilter
name|getRequestFilter
parameter_list|(
name|LinkedInConfiguration
name|endpointConfiguration
parameter_list|)
block|{
if|if
condition|(
name|endpointConfiguration
operator|.
name|equals
argument_list|(
name|configuration
argument_list|)
condition|)
block|{
if|if
condition|(
name|requestFilter
operator|==
literal|null
condition|)
block|{
name|requestFilter
operator|=
name|createRequestFilter
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
block|}
return|return
name|requestFilter
return|;
block|}
else|else
block|{
return|return
name|createRequestFilter
argument_list|(
name|endpointConfiguration
argument_list|)
return|;
block|}
block|}
DECL|method|createRequestFilter (LinkedInConfiguration configuration)
specifier|private
name|LinkedInOAuthRequestFilter
name|createRequestFilter
parameter_list|(
name|LinkedInConfiguration
name|configuration
parameter_list|)
block|{
comment|// validate configuration
name|configuration
operator|.
name|validate
argument_list|()
expr_stmt|;
specifier|final
name|String
index|[]
name|enabledProtocols
decl_stmt|;
try|try
block|{
comment|// use default SSP to create supported non-SSL protocols list
specifier|final
name|SSLContext
name|sslContext
init|=
operator|new
name|SSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|enabledProtocols
operator|=
name|sslContext
operator|.
name|createSSLEngine
argument_list|()
operator|.
name|getEnabledProtocols
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|LinkedInOAuthRequestFilter
argument_list|(
name|getOAuthParams
argument_list|(
name|configuration
argument_list|)
argument_list|,
name|configuration
operator|.
name|getHttpParams
argument_list|()
argument_list|,
name|configuration
operator|.
name|isLazyAuth
argument_list|()
argument_list|,
name|enabledProtocols
argument_list|)
return|;
block|}
DECL|method|getOAuthParams (LinkedInConfiguration configuration)
specifier|private
specifier|static
name|OAuthParams
name|getOAuthParams
parameter_list|(
name|LinkedInConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|OAuthParams
argument_list|(
name|configuration
operator|.
name|getUserName
argument_list|()
argument_list|,
name|configuration
operator|.
name|getUserPassword
argument_list|()
argument_list|,
operator|new
name|CachingOAuthSecureStorage
argument_list|(
name|configuration
operator|.
name|getSecureStorage
argument_list|()
argument_list|)
argument_list|,
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
name|getRedirectUri
argument_list|()
argument_list|,
name|configuration
operator|.
name|getScopes
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|requestFilter
operator|!=
literal|null
condition|)
block|{
name|closeLogException
argument_list|(
name|requestFilter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|closeRequestFilter (LinkedInOAuthRequestFilter requestFilter)
specifier|protected
name|void
name|closeRequestFilter
parameter_list|(
name|LinkedInOAuthRequestFilter
name|requestFilter
parameter_list|)
block|{
comment|// only close if not a shared filter
if|if
condition|(
name|this
operator|.
name|requestFilter
operator|!=
name|requestFilter
condition|)
block|{
name|closeLogException
argument_list|(
name|requestFilter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|closeLogException (LinkedInOAuthRequestFilter requestFilter)
specifier|private
name|void
name|closeLogException
parameter_list|(
name|LinkedInOAuthRequestFilter
name|requestFilter
parameter_list|)
block|{
try|try
block|{
name|requestFilter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error closing OAuth2 request filter: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

