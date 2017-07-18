begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|LinkedHashSet
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
name|Set
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
name|ComponentVerifier
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
name|ResolveEndpointFailedException
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
name|VerifiableComponent
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
name|http
operator|.
name|common
operator|.
name|HttpBinding
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
name|HttpCommonComponent
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
name|HttpConfiguration
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
name|HttpRestHeaderFilterStrategy
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
name|UrlRewrite
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
name|HeaderFilterStrategy
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
name|RestProducerFactory
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
name|ServiceHelper
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
name|URISupport
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
name|UnsafeUriCharactersEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|MultiThreadedHttpConnectionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|params
operator|.
name|HttpClientParams
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|params
operator|.
name|HttpConnectionManagerParams
import|;
end_import

begin_comment
comment|/**  * The<a href="http://camel.apache.org/http.html">HTTP Component</a>  *  */
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
DECL|class|HttpComponent
specifier|public
class|class
name|HttpComponent
extends|extends
name|HttpCommonComponent
implements|implements
name|RestProducerFactory
implements|,
name|VerifiableComponent
implements|,
name|SSLContextParametersAware
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|httpClientConfigurer
specifier|protected
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|httpConnectionManager
specifier|protected
name|HttpConnectionManager
name|httpConnectionManager
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
DECL|method|HttpComponent ()
specifier|public
name|HttpComponent
parameter_list|()
block|{
name|this
argument_list|(
name|HttpEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|HttpComponent (Class<? extends HttpEndpoint> endpointClass)
specifier|public
name|HttpComponent
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|HttpEndpoint
argument_list|>
name|endpointClass
parameter_list|)
block|{
name|super
argument_list|(
name|endpointClass
argument_list|)
expr_stmt|;
name|registerExtension
argument_list|(
name|HttpComponentVerifierExtension
operator|::
operator|new
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the HttpClientConfigurer based on the given parameters      *       * @param parameters the map of parameters       * @return the configurer      */
DECL|method|createHttpClientConfigurer (Map<String, Object> parameters, Set<AuthMethod> authMethods)
specifier|protected
name|HttpClientConfigurer
name|createHttpClientConfigurer
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|Set
argument_list|<
name|AuthMethod
argument_list|>
name|authMethods
parameter_list|)
block|{
comment|// prefer to use endpoint configured over component configured
name|HttpClientConfigurer
name|configurer
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpClientConfigurer"
argument_list|,
name|HttpClientConfigurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configurer
operator|==
literal|null
condition|)
block|{
comment|// fallback to component configured
name|configurer
operator|=
name|getHttpClientConfigurer
argument_list|()
expr_stmt|;
block|}
comment|// authentication can be endpoint configured
name|String
name|authUsername
init|=
name|getParameter
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
name|String
name|authMethod
init|=
name|getParameter
argument_list|(
name|parameters
argument_list|,
literal|"authMethod"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// validate that if auth username is given then the auth method is also provided
if|if
condition|(
name|authUsername
operator|!=
literal|null
operator|&&
name|authMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option authMethod must be provided to use authentication"
argument_list|)
throw|;
block|}
if|if
condition|(
name|authMethod
operator|!=
literal|null
condition|)
block|{
name|String
name|authPassword
init|=
name|getParameter
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
name|String
name|authDomain
init|=
name|getParameter
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
name|String
name|authHost
init|=
name|getParameter
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
name|configurer
operator|=
name|configureAuth
argument_list|(
name|configurer
argument_list|,
name|authMethod
argument_list|,
name|authUsername
argument_list|,
name|authPassword
argument_list|,
name|authDomain
argument_list|,
name|authHost
argument_list|,
name|authMethods
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|httpConfiguration
operator|!=
literal|null
condition|)
block|{
comment|// or fallback to use component configuration
name|configurer
operator|=
name|configureAuth
argument_list|(
name|configurer
argument_list|,
name|httpConfiguration
operator|.
name|getAuthMethod
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getAuthUsername
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getAuthPassword
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getAuthDomain
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getAuthHost
argument_list|()
argument_list|,
name|authMethods
argument_list|)
expr_stmt|;
block|}
comment|// proxy authentication can be endpoint configured
name|String
name|proxyAuthUsername
init|=
name|getParameter
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
name|String
name|proxyAuthMethod
init|=
name|getParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyAuthMethod"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// validate that if proxy auth username is given then the proxy auth method is also provided
if|if
condition|(
name|proxyAuthUsername
operator|!=
literal|null
operator|&&
name|proxyAuthMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option proxyAuthMethod must be provided to use proxy authentication"
argument_list|)
throw|;
block|}
if|if
condition|(
name|proxyAuthMethod
operator|!=
literal|null
condition|)
block|{
name|String
name|proxyAuthPassword
init|=
name|getParameter
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
name|String
name|proxyAuthDomain
init|=
name|getParameter
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
name|String
name|proxyAuthHost
init|=
name|getParameter
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
name|configurer
operator|=
name|configureProxyAuth
argument_list|(
name|configurer
argument_list|,
name|proxyAuthMethod
argument_list|,
name|proxyAuthUsername
argument_list|,
name|proxyAuthPassword
argument_list|,
name|proxyAuthDomain
argument_list|,
name|proxyAuthHost
argument_list|,
name|authMethods
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|httpConfiguration
operator|!=
literal|null
condition|)
block|{
comment|// or fallback to use component configuration
name|configurer
operator|=
name|configureProxyAuth
argument_list|(
name|configurer
argument_list|,
name|httpConfiguration
operator|.
name|getProxyAuthMethod
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getProxyAuthUsername
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getProxyAuthPassword
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getProxyAuthDomain
argument_list|()
argument_list|,
name|httpConfiguration
operator|.
name|getProxyAuthHost
argument_list|()
argument_list|,
name|authMethods
argument_list|)
expr_stmt|;
block|}
return|return
name|configurer
return|;
block|}
comment|/**      * Configures the authentication method to be used      *      * @return configurer to used      */
DECL|method|configureAuth (HttpClientConfigurer configurer, String authMethod, String username, String password, String domain, String host, Set<AuthMethod> authMethods)
specifier|protected
name|HttpClientConfigurer
name|configureAuth
parameter_list|(
name|HttpClientConfigurer
name|configurer
parameter_list|,
name|String
name|authMethod
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|domain
parameter_list|,
name|String
name|host
parameter_list|,
name|Set
argument_list|<
name|AuthMethod
argument_list|>
name|authMethods
parameter_list|)
block|{
comment|// no auth is in use
if|if
condition|(
name|username
operator|==
literal|null
operator|&&
name|authMethod
operator|==
literal|null
condition|)
block|{
return|return
name|configurer
return|;
block|}
comment|// validate mandatory options given
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|authMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option authMethod must be provided to use authentication"
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|authMethod
argument_list|,
literal|"authMethod"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|username
argument_list|,
literal|"authUsername"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|password
argument_list|,
literal|"authPassword"
argument_list|)
expr_stmt|;
name|AuthMethod
name|auth
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|AuthMethod
operator|.
name|class
argument_list|,
name|authMethod
argument_list|)
decl_stmt|;
comment|// add it as a auth method used
name|authMethods
operator|.
name|add
argument_list|(
name|auth
argument_list|)
expr_stmt|;
if|if
condition|(
name|auth
operator|==
name|AuthMethod
operator|.
name|Basic
operator|||
name|auth
operator|==
name|AuthMethod
operator|.
name|Digest
condition|)
block|{
return|return
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
argument_list|,
operator|new
name|BasicAuthenticationHttpClientConfigurer
argument_list|(
literal|false
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|auth
operator|==
name|AuthMethod
operator|.
name|NTLM
condition|)
block|{
comment|// domain is mandatory for NTLM
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|domain
argument_list|,
literal|"authDomain"
argument_list|)
expr_stmt|;
return|return
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
argument_list|,
operator|new
name|NTLMAuthenticationHttpClientConfigurer
argument_list|(
literal|false
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|domain
argument_list|,
name|host
argument_list|)
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown authMethod "
operator|+
name|authMethod
argument_list|)
throw|;
block|}
comment|/**      * Configures the proxy authentication method to be used      *      * @return configurer to used      */
DECL|method|configureProxyAuth (HttpClientConfigurer configurer, String authMethod, String username, String password, String domain, String host, Set<AuthMethod> authMethods)
specifier|protected
name|HttpClientConfigurer
name|configureProxyAuth
parameter_list|(
name|HttpClientConfigurer
name|configurer
parameter_list|,
name|String
name|authMethod
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|domain
parameter_list|,
name|String
name|host
parameter_list|,
name|Set
argument_list|<
name|AuthMethod
argument_list|>
name|authMethods
parameter_list|)
block|{
comment|// no proxy auth is in use
if|if
condition|(
name|username
operator|==
literal|null
operator|&&
name|authMethod
operator|==
literal|null
condition|)
block|{
return|return
name|configurer
return|;
block|}
comment|// validate mandatory options given
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|authMethod
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option proxyAuthMethod must be provided to use proxy authentication"
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|authMethod
argument_list|,
literal|"proxyAuthMethod"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|username
argument_list|,
literal|"proxyAuthUsername"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|password
argument_list|,
literal|"proxyAuthPassword"
argument_list|)
expr_stmt|;
name|AuthMethod
name|auth
init|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|AuthMethod
operator|.
name|class
argument_list|,
name|authMethod
argument_list|)
decl_stmt|;
comment|// add it as a auth method used
name|authMethods
operator|.
name|add
argument_list|(
name|auth
argument_list|)
expr_stmt|;
if|if
condition|(
name|auth
operator|==
name|AuthMethod
operator|.
name|Basic
operator|||
name|auth
operator|==
name|AuthMethod
operator|.
name|Digest
condition|)
block|{
return|return
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
argument_list|,
operator|new
name|BasicAuthenticationHttpClientConfigurer
argument_list|(
literal|true
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|auth
operator|==
name|AuthMethod
operator|.
name|NTLM
condition|)
block|{
comment|// domain is mandatory for NTML
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|domain
argument_list|,
literal|"proxyAuthDomain"
argument_list|)
expr_stmt|;
return|return
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
argument_list|,
operator|new
name|NTLMAuthenticationHttpClientConfigurer
argument_list|(
literal|true
argument_list|,
name|username
argument_list|,
name|password
argument_list|,
name|domain
argument_list|,
name|host
argument_list|)
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown proxyAuthMethod "
operator|+
name|authMethod
argument_list|)
throw|;
block|}
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
name|String
name|addressUri
init|=
literal|"http://"
operator|+
name|remaining
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"https:"
argument_list|)
condition|)
block|{
name|addressUri
operator|=
literal|"https://"
operator|+
name|remaining
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientParameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
comment|// must extract well known parameters before we create the endpoint
name|HttpBinding
name|binding
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpBinding"
argument_list|,
name|HttpBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|HeaderFilterStrategy
name|headerFilterStrategy
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|HeaderFilterStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
name|UrlRewrite
name|urlRewrite
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"urlRewrite"
argument_list|,
name|UrlRewrite
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// http client can be configured from URI options
name|HttpClientParams
name|clientParams
init|=
operator|new
name|HttpClientParams
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpClientOptions
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|clientParams
argument_list|,
name|httpClientOptions
argument_list|)
expr_stmt|;
comment|// validate that we could resolve all httpClient. parameters as this component is lenient
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|httpClientOptions
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// http client can be configured from URI options
name|HttpConnectionManagerParams
name|connectionManagerParams
init|=
operator|new
name|HttpConnectionManagerParams
argument_list|()
decl_stmt|;
comment|// setup the httpConnectionManagerParams
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpConnectionManagerOptions
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"httpConnectionManager."
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|connectionManagerParams
argument_list|,
name|httpConnectionManagerOptions
argument_list|)
expr_stmt|;
comment|// validate that we could resolve all httpConnectionManager. parameters as this component is lenient
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|httpConnectionManagerOptions
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// make sure the component httpConnectionManager is take effect
name|HttpConnectionManager
name|thisHttpConnectionManager
init|=
name|httpConnectionManager
decl_stmt|;
if|if
condition|(
name|thisHttpConnectionManager
operator|==
literal|null
condition|)
block|{
comment|// only set the params on the new created http connection manager
name|thisHttpConnectionManager
operator|=
operator|new
name|MultiThreadedHttpConnectionManager
argument_list|()
expr_stmt|;
name|thisHttpConnectionManager
operator|.
name|setParams
argument_list|(
name|connectionManagerParams
argument_list|)
expr_stmt|;
block|}
comment|// create the configurer to use for this endpoint (authMethods contains the used methods created by the configurer)
specifier|final
name|Set
argument_list|<
name|AuthMethod
argument_list|>
name|authMethods
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|AuthMethod
argument_list|>
argument_list|()
decl_stmt|;
name|HttpClientConfigurer
name|configurer
init|=
name|createHttpClientConfigurer
argument_list|(
name|parameters
argument_list|,
name|authMethods
argument_list|)
decl_stmt|;
name|addressUri
operator|=
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|addressUri
argument_list|)
expr_stmt|;
name|URI
name|endpointUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|addressUri
argument_list|)
argument_list|,
name|httpClientParameters
argument_list|)
decl_stmt|;
comment|// create the endpoint and connectionManagerParams already be set
name|HttpEndpoint
name|endpoint
init|=
name|createHttpEndpoint
argument_list|(
name|endpointUri
operator|.
name|toString
argument_list|()
argument_list|,
name|this
argument_list|,
name|clientParams
argument_list|,
name|thisHttpConnectionManager
argument_list|,
name|configurer
argument_list|)
decl_stmt|;
comment|// configure the endpoint with the common configuration from the component
if|if
condition|(
name|getHttpConfiguration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|getHttpConfiguration
argument_list|()
argument_list|,
name|properties
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|headerFilterStrategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|urlRewrite
operator|!=
literal|null
condition|)
block|{
comment|// let CamelContext deal with the lifecycle of the url rewrite
comment|// this ensures its being shutdown when Camel shutdown etc.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|urlRewrite
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setUrlRewrite
argument_list|(
name|urlRewrite
argument_list|)
expr_stmt|;
block|}
comment|// prefer to use endpoint configured over component configured
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// fallback to component configured
name|binding
operator|=
name|getHttpBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|binding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBinding
argument_list|(
name|binding
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
comment|// restructure uri to be based on the parameters left as we dont want to include the Camel internal options
name|URI
name|httpUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
operator|new
name|URI
argument_list|(
name|addressUri
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
comment|// validate http uri that end-user did not duplicate the http part that can be a common error
name|String
name|part
init|=
name|httpUri
operator|.
name|getSchemeSpecificPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
name|part
operator|=
name|part
operator|.
name|toLowerCase
argument_list|()
expr_stmt|;
if|if
condition|(
name|part
operator|.
name|startsWith
argument_list|(
literal|"//http//"
argument_list|)
operator|||
name|part
operator|.
name|startsWith
argument_list|(
literal|"//https//"
argument_list|)
operator|||
name|part
operator|.
name|startsWith
argument_list|(
literal|"//http://"
argument_list|)
operator|||
name|part
operator|.
name|startsWith
argument_list|(
literal|"//https://"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResolveEndpointFailedException
argument_list|(
name|uri
argument_list|,
literal|"The uri part is not configured correctly. You have duplicated the http(s) protocol."
argument_list|)
throw|;
block|}
block|}
name|endpoint
operator|.
name|setHttpUri
argument_list|(
name|httpUri
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setHttpClientOptions
argument_list|(
name|httpClientOptions
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|createHttpEndpoint (String uri, HttpComponent component, HttpClientParams clientParams, HttpConnectionManager connectionManager, HttpClientConfigurer configurer)
specifier|protected
name|HttpEndpoint
name|createHttpEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|HttpComponent
name|component
parameter_list|,
name|HttpClientParams
name|clientParams
parameter_list|,
name|HttpConnectionManager
name|connectionManager
parameter_list|,
name|HttpClientConfigurer
name|configurer
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|HttpEndpoint
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|clientParams
argument_list|,
name|connectionManager
argument_list|,
name|configurer
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer (CamelContext camelContext, String host, String verb, String basePath, String uriTemplate, String queryParameters, String consumes, String produces, Map<String, Object> parameters)
specifier|public
name|Producer
name|createProducer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|host
parameter_list|,
name|String
name|verb
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|queryParameters
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
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
comment|// avoid leading slash
name|basePath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
name|uriTemplate
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|uriTemplate
argument_list|)
expr_stmt|;
comment|// get the endpoint
name|String
name|url
init|=
name|host
decl_stmt|;
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|basePath
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
operator|+
name|basePath
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|uriTemplate
argument_list|)
condition|)
block|{
name|url
operator|+=
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
name|HttpEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
name|String
name|path
init|=
name|uriTemplate
operator|!=
literal|null
condition|?
name|uriTemplate
else|:
name|basePath
decl_stmt|;
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|HttpRestHeaderFilterStrategy
argument_list|(
name|path
argument_list|,
name|queryParameters
argument_list|)
argument_list|)
expr_stmt|;
comment|// the endpoint must be started before creating the producer
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|endpoint
operator|.
name|createProducer
argument_list|()
return|;
block|}
DECL|method|getHttpClientConfigurer ()
specifier|public
name|HttpClientConfigurer
name|getHttpClientConfigurer
parameter_list|()
block|{
return|return
name|httpClientConfigurer
return|;
block|}
comment|/**      * To use the custom HttpClientConfigurer to perform configuration of the HttpClient that will be used.      */
DECL|method|setHttpClientConfigurer (HttpClientConfigurer httpClientConfigurer)
specifier|public
name|void
name|setHttpClientConfigurer
parameter_list|(
name|HttpClientConfigurer
name|httpClientConfigurer
parameter_list|)
block|{
name|this
operator|.
name|httpClientConfigurer
operator|=
name|httpClientConfigurer
expr_stmt|;
block|}
DECL|method|getHttpConnectionManager ()
specifier|public
name|HttpConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
comment|/**      * To use a custom HttpConnectionManager to manage connections      */
DECL|method|setHttpConnectionManager (HttpConnectionManager httpConnectionManager)
specifier|public
name|void
name|setHttpConnectionManager
parameter_list|(
name|HttpConnectionManager
name|httpConnectionManager
parameter_list|)
block|{
name|this
operator|.
name|httpConnectionManager
operator|=
name|httpConnectionManager
expr_stmt|;
block|}
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message and HttpClient.      */
annotation|@
name|Override
DECL|method|setHttpBinding (HttpBinding httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
name|httpBinding
parameter_list|)
block|{
comment|// need to override and call super for component docs
name|super
operator|.
name|setHttpBinding
argument_list|(
name|httpBinding
argument_list|)
expr_stmt|;
block|}
comment|/**      * To use the shared HttpConfiguration as base configuration.      */
annotation|@
name|Override
DECL|method|setHttpConfiguration (HttpConfiguration httpConfiguration)
specifier|public
name|void
name|setHttpConfiguration
parameter_list|(
name|HttpConfiguration
name|httpConfiguration
parameter_list|)
block|{
comment|// need to override and call super for component docs
name|super
operator|.
name|setHttpConfiguration
argument_list|(
name|httpConfiguration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
annotation|@
name|Override
DECL|method|setAllowJavaSerializedObject (boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
comment|// need to override and call super for component docs
name|super
operator|.
name|setAllowJavaSerializedObject
argument_list|(
name|allowJavaSerializedObject
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
annotation|@
name|Override
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifier
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
block|}
end_class

end_unit

