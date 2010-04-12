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
DECL|field|httpClientConfigurer
specifier|protected
name|HttpClientConfigurer
name|httpClientConfigurer
decl_stmt|;
DECL|field|httpConnectionManager
specifier|protected
name|HttpConnectionManager
name|httpConnectionManager
init|=
operator|new
name|MultiThreadedHttpConnectionManager
argument_list|()
decl_stmt|;
DECL|field|httpBinding
specifier|protected
name|HttpBinding
name|httpBinding
decl_stmt|;
comment|/**      * Connects the URL specified on the endpoint to the specified processor.      *      * @param consumer the consumer      * @throws Exception can be thrown      */
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
comment|/**      * Disconnects the URL specified on the endpoint from the specified processor.      *      * @param consumer the consumer      * @throws Exception can be thrown      */
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
comment|/**       * Creates the HttpClientConfigurer based on the given parameters      *       * @param parameters the map of parameters       * @return the configurer      */
DECL|method|createHttpClientConfigurer (Map<String, Object> parameters)
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
literal|"httpClientConfigurerRef"
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
comment|// try without ref
name|configurer
operator|=
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
expr_stmt|;
block|}
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
name|configurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
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
name|configurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
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
name|configurer
operator|=
name|CompositeHttpConfigurer
operator|.
name|combineConfigurers
argument_list|(
name|configurer
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
return|return
name|configurer
return|;
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
comment|// must extract well known parameters before we create the endpoint
name|HttpBinding
name|binding
init|=
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
decl_stmt|;
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
comment|// try without ref
name|binding
operator|=
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
expr_stmt|;
block|}
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
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"matchOnUriPrefix"
argument_list|,
name|Boolean
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
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|clientParams
argument_list|,
name|parameters
argument_list|,
literal|"httpClient."
argument_list|)
expr_stmt|;
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
comment|// create the configurer to use for this endpoint
name|HttpClientConfigurer
name|configurer
init|=
name|createHttpClientConfigurer
argument_list|(
name|parameters
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
comment|// create the endpoint
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
name|configurer
argument_list|)
decl_stmt|;
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
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
comment|// should we use an exception for failed error codes?
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
name|HttpConnectionManager
name|getHttpConnectionManager
parameter_list|()
block|{
return|return
name|httpConnectionManager
return|;
block|}
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
block|}
end_class

end_unit

