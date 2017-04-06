begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Optional
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
name|http
operator|.
name|common
operator|.
name|HttpHelper
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
name|verifier
operator|.
name|DefaultComponentVerifier
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
name|impl
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
name|FileUtil
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|config
operator|.
name|RequestConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|CloseableHttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpUriRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_class
DECL|class|HttpComponentVerifier
specifier|final
class|class
name|HttpComponentVerifier
extends|extends
name|DefaultComponentVerifier
block|{
DECL|field|component
specifier|private
specifier|final
name|HttpComponent
name|component
decl_stmt|;
DECL|method|HttpComponentVerifier (HttpComponent component)
name|HttpComponentVerifier
parameter_list|(
name|HttpComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
literal|"http4"
argument_list|,
name|component
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
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
comment|// Default is success
specifier|final
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
comment|// Make a copy to avoid clashing with parent validation
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|verifyParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
comment|// Check if validation is rest-related
specifier|final
name|boolean
name|isRest
init|=
name|verifyParams
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"rest."
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRest
condition|)
block|{
comment|// Build the httpUri from rest configuration
name|verifyParams
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|buildHttpUriFromRestParameters
argument_list|(
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
comment|// Cleanup parameters map from rest related stuffs
name|verifyParams
operator|.
name|entrySet
argument_list|()
operator|.
name|removeIf
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"rest."
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Validate using the catalog
name|super
operator|.
name|verifyParametersAgainstCatalog
argument_list|(
name|builder
argument_list|,
name|verifyParams
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
specifier|final
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
comment|// Make a copy to avoid clashing with parent validation
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|verifyParams
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
comment|// Check if validation is rest-related
specifier|final
name|boolean
name|isRest
init|=
name|verifyParams
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"rest."
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|isRest
condition|)
block|{
comment|// Build the httpUri from rest configuration
name|verifyParams
operator|.
name|put
argument_list|(
literal|"httpUri"
argument_list|,
name|buildHttpUriFromRestParameters
argument_list|(
name|parameters
argument_list|)
argument_list|)
expr_stmt|;
comment|// Cleanup parameters from rest related stuffs
name|verifyParams
operator|.
name|entrySet
argument_list|()
operator|.
name|removeIf
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"rest."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|httpUri
init|=
name|getOption
argument_list|(
name|verifyParams
argument_list|,
literal|"httpUri"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|httpUri
argument_list|)
condition|)
block|{
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withMissingOption
argument_list|(
literal|"httpUri"
argument_list|)
operator|.
name|detail
argument_list|(
literal|"rest"
argument_list|,
name|isRest
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|CloseableHttpClient
name|httpclient
init|=
name|createHttpClient
argument_list|(
name|verifyParams
argument_list|)
decl_stmt|;
name|HttpUriRequest
name|request
init|=
operator|new
name|HttpGet
argument_list|(
name|httpUri
argument_list|)
decl_stmt|;
try|try
init|(
name|CloseableHttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|request
argument_list|)
init|)
block|{
name|int
name|code
init|=
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
decl_stmt|;
name|String
name|okCodes
init|=
name|getOption
argument_list|(
name|verifyParams
argument_list|,
literal|"okStatusCodeRange"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|"200-299"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|HttpHelper
operator|.
name|isStatusCodeOk
argument_list|(
name|code
argument_list|,
name|okCodes
argument_list|)
condition|)
block|{
if|if
condition|(
name|code
operator|==
literal|401
condition|)
block|{
comment|// Unauthorized, add authUsername and authPassword to the list
comment|// of parameters in error
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withHttpCode
argument_list|(
name|code
argument_list|)
operator|.
name|description
argument_list|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
operator|.
name|parameterKey
argument_list|(
literal|"authUsername"
argument_list|)
operator|.
name|parameterKey
argument_list|(
literal|"authPassword"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|code
operator|>=
literal|300
operator|&&
name|code
operator|<
literal|400
condition|)
block|{
comment|// redirect
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withHttpCode
argument_list|(
name|code
argument_list|)
operator|.
name|description
argument_list|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
operator|.
name|parameterKey
argument_list|(
literal|"httpUri"
argument_list|)
operator|.
name|detail
argument_list|(
name|VerificationError
operator|.
name|HttpAttribute
operator|.
name|HTTP_REDIRECT
argument_list|,
parameter_list|()
lambda|->
name|HttpUtil
operator|.
name|responseHeaderValue
argument_list|(
name|response
argument_list|,
literal|"location"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|code
operator|>=
literal|400
condition|)
block|{
comment|// generic http error
name|builder
operator|.
name|error
argument_list|(
name|ResultErrorBuilder
operator|.
name|withHttpCode
argument_list|(
name|code
argument_list|)
operator|.
name|description
argument_list|(
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getReasonPhrase
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|UnknownHostException
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
name|parameterKey
argument_list|(
literal|"httpUri"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
DECL|method|buildHttpUriFromRestParameters (Map<String, Object> parameters)
specifier|private
name|String
name|buildHttpUriFromRestParameters
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
comment|// We are doing rest endpoint validation but as today the endpoint
comment|// can't do any param substitution so the validation is performed
comment|// against the http uri
name|String
name|httpUri
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"rest.host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"rest.path"
argument_list|,
name|String
operator|.
name|class
argument_list|)
operator|.
name|map
argument_list|(
name|FileUtil
operator|::
name|stripLeadingSeparator
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|httpUri
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|httpUri
operator|=
name|httpUri
operator|+
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
return|return
name|httpUri
return|;
block|}
DECL|method|configureAuthentication (Map<String, Object> parameters)
specifier|private
name|Optional
argument_list|<
name|HttpClientConfigurer
argument_list|>
name|configureAuthentication
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
name|Optional
argument_list|<
name|String
argument_list|>
name|authUsername
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"authUsername"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|authPassword
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"authPassword"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|authUsername
operator|.
name|isPresent
argument_list|()
operator|&&
name|authPassword
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|authDomain
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"authDomain"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|authHost
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"authHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|BasicAuthenticationHttpClientConfigurer
argument_list|(
name|authUsername
operator|.
name|get
argument_list|()
argument_list|,
name|authPassword
operator|.
name|get
argument_list|()
argument_list|,
name|authDomain
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
name|authHost
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|configureProxy (Map<String, Object> parameters)
specifier|private
name|Optional
argument_list|<
name|HttpClientConfigurer
argument_list|>
name|configureProxy
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
name|Optional
argument_list|<
name|String
argument_list|>
name|uri
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"httpUri"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthHost
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|Integer
argument_list|>
name|proxyAuthPort
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthPort"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyAuthHost
operator|.
name|isPresent
argument_list|()
operator|&&
name|proxyAuthPort
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthScheme
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthScheme"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthUsername
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthUsername"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthPassword
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthPassword"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthDomain
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthDomain"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Optional
argument_list|<
name|String
argument_list|>
name|proxyAuthNtHost
init|=
name|getOption
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthNtHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|proxyAuthScheme
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|proxyAuthScheme
operator|=
name|Optional
operator|.
name|of
argument_list|(
name|HttpHelper
operator|.
name|isSecureConnection
argument_list|(
name|uri
operator|.
name|get
argument_list|()
argument_list|)
condition|?
literal|"https"
else|:
literal|"http"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|proxyAuthUsername
operator|!=
literal|null
operator|&&
name|proxyAuthPassword
operator|!=
literal|null
condition|)
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|ProxyHttpClientConfigurer
argument_list|(
name|proxyAuthHost
operator|.
name|get
argument_list|()
argument_list|,
name|proxyAuthPort
operator|.
name|get
argument_list|()
argument_list|,
name|proxyAuthScheme
operator|.
name|get
argument_list|()
argument_list|,
name|proxyAuthUsername
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
name|proxyAuthPassword
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
name|proxyAuthDomain
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|,
name|proxyAuthNtHost
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Optional
operator|.
name|of
argument_list|(
operator|new
name|ProxyHttpClientConfigurer
argument_list|(
name|proxyAuthHost
operator|.
name|get
argument_list|()
argument_list|,
name|proxyAuthPort
operator|.
name|get
argument_list|()
argument_list|,
name|proxyAuthScheme
operator|.
name|get
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
name|Optional
operator|.
name|empty
argument_list|()
return|;
block|}
DECL|method|createHttpClient (Map<String, Object> parameters)
specifier|private
name|CloseableHttpClient
name|createHttpClient
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
name|CompositeHttpConfigurer
name|configurer
init|=
operator|new
name|CompositeHttpConfigurer
argument_list|()
decl_stmt|;
name|configureAuthentication
argument_list|(
name|parameters
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|configurer
operator|::
name|addConfigurer
argument_list|)
expr_stmt|;
name|configureProxy
argument_list|(
name|parameters
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|configurer
operator|::
name|addConfigurer
argument_list|)
expr_stmt|;
name|HttpClientBuilder
name|builder
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
decl_stmt|;
name|configurer
operator|.
name|configureHttpClient
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|RequestConfig
operator|.
name|Builder
name|requestConfigBuilder
init|=
name|RequestConfig
operator|.
name|custom
argument_list|()
decl_stmt|;
comment|// Apply custom http client properties like httpClient.redirectsEnabled
name|setProperties
argument_list|(
name|builder
argument_list|,
literal|"httpClient."
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|requestConfigBuilder
argument_list|,
literal|"httpClient."
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|builder
operator|.
name|setDefaultRequestConfig
argument_list|(
name|requestConfigBuilder
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

