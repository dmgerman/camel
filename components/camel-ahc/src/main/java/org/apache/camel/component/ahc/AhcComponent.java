begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
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
name|camel
operator|.
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|DefaultAsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|Realm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|Realm
operator|.
name|Builder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *  To call external HTTP services using<a href="http://github.com/sonatype/async-http-client">Async Http Client</a>  */
end_comment

begin_class
DECL|class|AhcComponent
specifier|public
class|class
name|AhcComponent
extends|extends
name|HeaderFilterStrategyComponent
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AhcComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CLIENT_CONFIG_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_CONFIG_PREFIX
init|=
literal|"clientConfig."
decl_stmt|;
DECL|field|CLIENT_REALM_CONFIG_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_REALM_CONFIG_PREFIX
init|=
literal|"clientConfig.realm."
decl_stmt|;
DECL|field|client
specifier|private
name|AsyncHttpClient
name|client
decl_stmt|;
DECL|field|clientConfig
specifier|private
name|AsyncHttpClientConfig
name|clientConfig
decl_stmt|;
DECL|field|binding
specifier|private
name|AhcBinding
name|binding
decl_stmt|;
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
DECL|field|allowJavaSerializedObject
specifier|private
name|boolean
name|allowJavaSerializedObject
decl_stmt|;
DECL|method|AhcComponent ()
specifier|public
name|AhcComponent
parameter_list|()
block|{
name|super
argument_list|(
name|AhcEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
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
name|createAddressUri
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
comment|// Do not set the HTTP URI because we still have all of the Camel internal
comment|// parameters in the URI at this point.
name|AhcEndpoint
name|endpoint
init|=
name|createAhcEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|setEndpointHeaderFilterStrategy
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setClient
argument_list|(
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setClientConfig
argument_list|(
name|getClientConfig
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBinding
argument_list|(
name|getBinding
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setSslContextParameters
argument_list|(
name|getSslContextParameters
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|parameters
argument_list|,
name|CLIENT_CONFIG_PREFIX
argument_list|)
condition|)
block|{
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
name|endpoint
operator|.
name|getClientConfig
argument_list|()
operator|==
literal|null
condition|?
operator|new
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
else|:
name|AhcComponent
operator|.
name|cloneConfig
argument_list|(
name|endpoint
operator|.
name|getClientConfig
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getClient
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The user explicitly set an AsyncHttpClient instance on the component or "
operator|+
literal|"endpoint, but this endpoint URI contains client configuration parameters.  "
operator|+
literal|"Are you sure that this is what was intended?  The AsyncHttpClient will be used"
operator|+
literal|" and the URI parameters will be ignored."
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getClientConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The user explicitly set an AsyncHttpClientConfig instance on the component or "
operator|+
literal|"endpoint, but this endpoint URI contains client configuration parameters.  "
operator|+
literal|"Are you sure that this is what was intended?  The URI parameters will be applied"
operator|+
literal|" to a clone of the supplied AsyncHttpClientConfig in order to prevent unintended modification"
operator|+
literal|" of the explicitly configured AsyncHttpClientConfig.  That is, the URI parameters override the"
operator|+
literal|" settings on the explicitly configured AsyncHttpClientConfig for this endpoint."
argument_list|)
expr_stmt|;
block|}
comment|// special for realm builder
name|Builder
name|realmBuilder
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|IntrospectionSupport
operator|.
name|hasProperties
argument_list|(
name|parameters
argument_list|,
name|CLIENT_REALM_CONFIG_PREFIX
argument_list|)
condition|)
block|{
comment|// set and validate additional parameters on client config
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|realmParams
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
name|CLIENT_REALM_CONFIG_PREFIX
argument_list|)
decl_stmt|;
name|Object
name|principal
init|=
name|realmParams
operator|.
name|remove
argument_list|(
literal|"principal"
argument_list|)
decl_stmt|;
name|Object
name|password
init|=
name|realmParams
operator|.
name|remove
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|principal
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|CLIENT_REALM_CONFIG_PREFIX
operator|+
literal|".principal must be configured"
argument_list|)
throw|;
block|}
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
name|password
operator|=
literal|""
expr_stmt|;
block|}
name|realmBuilder
operator|=
operator|new
name|Realm
operator|.
name|Builder
argument_list|(
name|principal
operator|.
name|toString
argument_list|()
argument_list|,
name|password
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|realmBuilder
argument_list|,
name|realmParams
argument_list|)
expr_stmt|;
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|realmParams
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// set and validate additional parameters on client config
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|clientParams
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
name|CLIENT_CONFIG_PREFIX
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|builder
argument_list|,
name|clientParams
argument_list|)
expr_stmt|;
name|validateParameters
argument_list|(
name|uri
argument_list|,
name|clientParams
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|realmBuilder
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|setRealm
argument_list|(
name|realmBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setClientConfig
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// restructure uri to be based on the parameters left as we dont want to include the Camel internal options
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
name|endpoint
operator|.
name|setHttpUri
argument_list|(
name|httpUri
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getClient ()
specifier|public
name|AsyncHttpClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
comment|/**      * To use a custom {@link AsyncHttpClient}      */
DECL|method|setClient (AsyncHttpClient client)
specifier|public
name|void
name|setClient
parameter_list|(
name|AsyncHttpClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|getBinding ()
specifier|public
name|AhcBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
operator|new
name|DefaultAhcBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * To use a custom {@link AhcBinding} which allows to control how to bind between AHC and Camel.      */
DECL|method|setBinding (AhcBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|AhcBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getClientConfig ()
specifier|public
name|AsyncHttpClientConfig
name|getClientConfig
parameter_list|()
block|{
return|return
name|clientConfig
return|;
block|}
comment|/**      * To configure the AsyncHttpClient to use a custom com.ning.http.client.AsyncHttpClientConfig instance.      */
DECL|method|setClientConfig (AsyncHttpClientConfig clientConfig)
specifier|public
name|void
name|setClientConfig
parameter_list|(
name|AsyncHttpClientConfig
name|clientConfig
parameter_list|)
block|{
name|this
operator|.
name|clientConfig
operator|=
name|clientConfig
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * Reference to a org.apache.camel.util.jsse.SSLContextParameters in the Registry.      * Note that configuring this option will override any SSL/TLS configuration options provided through the      * clientConfig option at the endpoint or component level.      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|isAllowJavaSerializedObject ()
specifier|public
name|boolean
name|isAllowJavaSerializedObject
parameter_list|()
block|{
return|return
name|allowJavaSerializedObject
return|;
block|}
comment|/**      * Whether to allow java serialization when a request uses context-type=application/x-java-serialized-object      *<p/>      * This is by default turned off. If you enable this then be aware that Java will deserialize the incoming      * data from the request to Java and that can be a potential security risk.      */
DECL|method|setAllowJavaSerializedObject (boolean allowJavaSerializedObject)
specifier|public
name|void
name|setAllowJavaSerializedObject
parameter_list|(
name|boolean
name|allowJavaSerializedObject
parameter_list|)
block|{
name|this
operator|.
name|allowJavaSerializedObject
operator|=
name|allowJavaSerializedObject
expr_stmt|;
block|}
DECL|method|createAddressUri (String uri, String remaining)
specifier|protected
name|String
name|createAddressUri
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
return|return
name|remaining
return|;
block|}
DECL|method|createAhcEndpoint (String endpointUri, AhcComponent component, URI httpUri)
specifier|protected
name|AhcEndpoint
name|createAhcEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AhcComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|)
block|{
return|return
operator|new
name|AhcEndpoint
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|)
return|;
block|}
comment|/**      * Creates a new client configuration builder using {@code DefaultAsyncHttpClientConfig} as a template for      * the builder.      *      * @param clientConfig the instance to serve as a template for the builder      * @return a builder configured with the same options as the supplied config      */
DECL|method|cloneConfig (AsyncHttpClientConfig clientConfig)
specifier|static
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
name|cloneConfig
parameter_list|(
name|AsyncHttpClientConfig
name|clientConfig
parameter_list|)
block|{
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
argument_list|(
name|clientConfig
argument_list|)
decl_stmt|;
return|return
name|builder
return|;
block|}
block|}
end_class

end_unit

