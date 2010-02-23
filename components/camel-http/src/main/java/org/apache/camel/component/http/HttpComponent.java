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
name|impl
operator|.
name|HeaderFilterStrategyComponent
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
name|CastUtils
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
name|URISupport
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
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
name|auth
operator|.
name|params
operator|.
name|AuthParamBean
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
name|params
operator|.
name|ClientParamBean
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
name|conn
operator|.
name|ClientConnectionManager
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
name|conn
operator|.
name|params
operator|.
name|ConnConnectionParamBean
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
name|conn
operator|.
name|params
operator|.
name|ConnManagerParamBean
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
name|conn
operator|.
name|params
operator|.
name|ConnPerRouteBean
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
name|conn
operator|.
name|params
operator|.
name|ConnRouteParamBean
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
name|conn
operator|.
name|scheme
operator|.
name|PlainSocketFactory
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
name|conn
operator|.
name|scheme
operator|.
name|Scheme
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
name|conn
operator|.
name|scheme
operator|.
name|SchemeRegistry
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
name|conn
operator|.
name|ssl
operator|.
name|SSLSocketFactory
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
name|cookie
operator|.
name|params
operator|.
name|CookieSpecParamBean
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
name|conn
operator|.
name|tsccm
operator|.
name|ThreadSafeClientConnManager
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
name|params
operator|.
name|BasicHttpParams
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
name|params
operator|.
name|HttpConnectionParamBean
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
name|params
operator|.
name|HttpParams
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
name|params
operator|.
name|HttpProtocolParamBean
import|;
end_import

begin_comment
comment|/**  * Defines the<a href="http://camel.apache.org/http.html">HTTP  * Component</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpComponent
specifier|public
class|class
name|HttpComponent
extends|extends
name|HeaderFilterStrategyComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|httpClientConfigurer
specifier|protected
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
DECL|field|httpConnectionManager
specifier|protected
name|ClientConnectionManager
name|httpConnectionManager
decl_stmt|;
DECL|field|httpBinding
specifier|protected
name|HttpBinding
name|httpBinding
decl_stmt|;
comment|// options to the default created http connection manager
DECL|field|maxTotalConnections
specifier|protected
name|int
name|maxTotalConnections
init|=
literal|200
decl_stmt|;
DECL|field|connectionsPerRoute
specifier|protected
name|int
name|connectionsPerRoute
init|=
literal|20
decl_stmt|;
comment|/**      * Connects the URL specified on the endpoint to the specified processor.      *      * @param  consumer the consumer      * @throws Exception can be thrown      */
DECL|method|connect (HttpConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{     }
comment|/**      * Disconnects the URL specified on the endpoint from the specified processor.      *      * @param  consumer the consumer      * @throws Exception can be thrown      */
DECL|method|disconnect (HttpConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|HttpConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{     }
comment|/**      * Setting http binding and http client configurer according to the parameters      * Also setting the BasicAuthenticationHttpClientConfigurer if the username      * and password option are not null.      *      * @param parameters the map of parameters      */
DECL|method|configureParameters (Map<String, Object> parameters)
specifier|protected
name|void
name|configureParameters
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
comment|// lookup http binding in registry if provided
name|httpBinding
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpBindingRef"
argument_list|,
name|HttpBinding
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// lookup http client front configurer in the registry if provided
name|httpClientConfigurer
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"httpClientConfigurerRef"
argument_list|,
name|HttpClientConfigurer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// check the user name and password for basic authentication
name|String
name|username
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"username"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|password
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"password"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|domain
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"domain"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|host
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"host"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|password
operator|!=
literal|null
condition|)
block|{
name|httpClientConfigurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|httpClientConfigurer
argument_list|,
operator|new
name|BasicAuthenticationHttpClientConfigurer
argument_list|(
name|username
argument_list|,
name|password
argument_list|,
name|domain
argument_list|,
name|host
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// check the proxy details for proxy configuration
name|String
name|proxyHost
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Integer
name|proxyPort
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyPort"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyHost
operator|!=
literal|null
operator|&&
name|proxyPort
operator|!=
literal|null
condition|)
block|{
name|String
name|proxyUsername
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyUsername"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|proxyPassword
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyPassword"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|proxyDomain
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyDomain"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|proxyNtHost
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"proxyNtHost"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyUsername
operator|!=
literal|null
operator|&&
name|proxyPassword
operator|!=
literal|null
condition|)
block|{
name|httpClientConfigurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|httpClientConfigurer
argument_list|,
operator|new
name|ProxyHttpClientConfigurer
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|,
name|proxyUsername
argument_list|,
name|proxyPassword
argument_list|,
name|proxyDomain
argument_list|,
name|proxyNtHost
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|httpClientConfigurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|httpClientConfigurer
argument_list|,
operator|new
name|ProxyHttpClientConfigurer
argument_list|(
name|proxyHost
argument_list|,
name|proxyPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// http client can be configured from URI options
name|HttpParams
name|clientParams
init|=
name|configureHttpParams
argument_list|(
name|parameters
argument_list|)
decl_stmt|;
comment|// validate that we could resolve all httpClient. parameters as this component is lenient
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|configureParameters
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
comment|// should we use an exception for failed error codes?
name|Boolean
name|throwExceptionOnFailure
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"throwExceptionOnFailure"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|bridgeEndpoint
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"bridgeEndpoint"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|matchOnUriPrefix
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"matchOnUriPrefix"
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
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
name|uri
argument_list|)
argument_list|,
name|CastUtils
operator|.
name|cast
argument_list|(
name|parameters
argument_list|)
argument_list|)
decl_stmt|;
name|uri
operator|=
name|httpUri
operator|.
name|toString
argument_list|()
expr_stmt|;
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
comment|// create default connection manager if none provided
if|if
condition|(
name|httpConnectionManager
operator|==
literal|null
condition|)
block|{
name|httpConnectionManager
operator|=
name|createConnectionManager
argument_list|(
name|clientParams
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Using ClientConnectionManager: "
operator|+
name|httpConnectionManager
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoint
init|=
operator|new
name|HttpEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|httpUri
argument_list|,
name|clientParams
argument_list|,
name|httpConnectionManager
argument_list|,
name|httpClientConfigurer
argument_list|)
decl_stmt|;
if|if
condition|(
name|httpBinding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBinding
argument_list|(
name|httpBinding
argument_list|)
expr_stmt|;
block|}
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|throwExceptionOnFailure
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setThrowExceptionOnFailure
argument_list|(
name|throwExceptionOnFailure
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bridgeEndpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
name|bridgeEndpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|matchOnUriPrefix
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMatchOnUriPrefix
argument_list|(
name|matchOnUriPrefix
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
return|return
name|endpoint
return|;
block|}
DECL|method|createConnectionManager (HttpParams clientParams, String uri)
specifier|protected
name|ClientConnectionManager
name|createConnectionManager
parameter_list|(
name|HttpParams
name|clientParams
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Created ClientConnectionManager configured with"
argument_list|)
decl_stmt|;
name|ThreadSafeClientConnManager
name|answer
decl_stmt|;
name|SchemeRegistry
name|schemeRegistry
init|=
operator|new
name|SchemeRegistry
argument_list|()
decl_stmt|;
if|if
condition|(
name|isSecureConnection
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|schemeRegistry
operator|.
name|register
argument_list|(
operator|new
name|Scheme
argument_list|(
literal|"https"
argument_list|,
name|SSLSocketFactory
operator|.
name|getSocketFactory
argument_list|()
argument_list|,
literal|443
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|schemeRegistry
operator|.
name|register
argument_list|(
operator|new
name|Scheme
argument_list|(
literal|"http"
argument_list|,
name|PlainSocketFactory
operator|.
name|getSocketFactory
argument_list|()
argument_list|,
literal|80
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
operator|new
name|ThreadSafeClientConnManager
argument_list|(
name|clientParams
argument_list|,
name|schemeRegistry
argument_list|)
expr_stmt|;
comment|// configure additional configurations
name|ConnManagerParamBean
name|param
init|=
operator|new
name|ConnManagerParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
if|if
condition|(
name|getMaxTotalConnections
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" maxTotalConnections="
operator|+
name|getMaxTotalConnections
argument_list|()
argument_list|)
expr_stmt|;
name|param
operator|.
name|setMaxTotalConnections
argument_list|(
name|getMaxTotalConnections
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConnectionsPerRoute
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" connectionsPerRoute="
operator|+
name|getConnectionsPerRoute
argument_list|()
argument_list|)
expr_stmt|;
name|param
operator|.
name|setConnectionsPerRoute
argument_list|(
operator|new
name|ConnPerRouteBean
argument_list|(
name|getConnectionsPerRoute
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// log information about the created connection manager
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
name|sb
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|endsWith
argument_list|(
literal|"with"
argument_list|)
condition|)
block|{
name|msg
operator|+=
literal|" default values"
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|msg
operator|+
literal|": "
operator|+
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|configureHttpParams (Map<String, Object> parameters)
specifier|protected
name|HttpParams
name|configureHttpParams
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
name|HttpParams
name|clientParams
init|=
operator|new
name|BasicHttpParams
argument_list|()
decl_stmt|;
name|AuthParamBean
name|authParamBean
init|=
operator|new
name|AuthParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|authParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|ClientParamBean
name|clientParamBean
init|=
operator|new
name|ClientParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|clientParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|ConnConnectionParamBean
name|connConnectionParamBean
init|=
operator|new
name|ConnConnectionParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|connConnectionParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|ConnManagerParamBean
name|connManagerParamBean
init|=
operator|new
name|ConnManagerParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|connManagerParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|ConnRouteParamBean
name|connRouteParamBean
init|=
operator|new
name|ConnRouteParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|connRouteParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|CookieSpecParamBean
name|cookieSpecParamBean
init|=
operator|new
name|CookieSpecParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|cookieSpecParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|HttpConnectionParamBean
name|httpConnectionParamBean
init|=
operator|new
name|HttpConnectionParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|httpConnectionParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
name|HttpProtocolParamBean
name|httpProtocolParamBean
init|=
operator|new
name|HttpProtocolParamBean
argument_list|(
name|clientParams
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|httpProtocolParamBean
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
return|return
name|clientParams
return|;
block|}
DECL|method|isSecureConnection (String uri)
specifier|private
name|boolean
name|isSecureConnection
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|uri
operator|.
name|startsWith
argument_list|(
literal|"https"
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|useIntrospectionOnEndpoint ()
specifier|protected
name|boolean
name|useIntrospectionOnEndpoint
parameter_list|()
block|{
return|return
literal|false
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
name|ClientConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
DECL|method|setHttpConnectionManager (ClientConnectionManager httpConnectionManager)
specifier|public
name|void
name|setHttpConnectionManager
parameter_list|(
name|ClientConnectionManager
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
DECL|method|getHttpBinding ()
specifier|public
name|HttpBinding
name|getHttpBinding
parameter_list|()
block|{
return|return
name|httpBinding
return|;
block|}
DECL|method|setHttpBinding (HttpBinding httpBinding)
specifier|public
name|void
name|setHttpBinding
parameter_list|(
name|HttpBinding
name|httpBinding
parameter_list|)
block|{
name|this
operator|.
name|httpBinding
operator|=
name|httpBinding
expr_stmt|;
block|}
DECL|method|getMaxTotalConnections ()
specifier|public
name|int
name|getMaxTotalConnections
parameter_list|()
block|{
return|return
name|maxTotalConnections
return|;
block|}
DECL|method|setMaxTotalConnections (int maxTotalConnections)
specifier|public
name|void
name|setMaxTotalConnections
parameter_list|(
name|int
name|maxTotalConnections
parameter_list|)
block|{
name|this
operator|.
name|maxTotalConnections
operator|=
name|maxTotalConnections
expr_stmt|;
block|}
DECL|method|getConnectionsPerRoute ()
specifier|public
name|int
name|getConnectionsPerRoute
parameter_list|()
block|{
return|return
name|connectionsPerRoute
return|;
block|}
DECL|method|setConnectionsPerRoute (int connectionsPerRoute)
specifier|public
name|void
name|setConnectionsPerRoute
parameter_list|(
name|int
name|connectionsPerRoute
parameter_list|)
block|{
name|this
operator|.
name|connectionsPerRoute
operator|=
name|connectionsPerRoute
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// shutdown connection manager
if|if
condition|(
name|httpConnectionManager
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Shutting down ClientConnectionManager: "
operator|+
name|httpConnectionManager
argument_list|)
expr_stmt|;
name|httpConnectionManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|httpConnectionManager
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

