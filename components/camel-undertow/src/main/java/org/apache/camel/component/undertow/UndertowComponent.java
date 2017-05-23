begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|Locale
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|Consumer
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
name|Processor
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
name|impl
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
name|RestApiConsumerFactory
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
name|RestConfiguration
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
name|RestConsumerFactory
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
name|HostUtils
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
comment|/**  * Represents the component that manages {@link UndertowEndpoint}.  */
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
DECL|class|UndertowComponent
specifier|public
class|class
name|UndertowComponent
extends|extends
name|DefaultComponent
implements|implements
name|RestConsumerFactory
implements|,
name|RestApiConsumerFactory
implements|,
name|RestProducerFactory
implements|,
name|VerifiableComponent
implements|,
name|SSLContextParametersAware
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
name|UndertowEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|undertowRegistry
specifier|private
name|Map
argument_list|<
name|UndertowHostKey
argument_list|,
name|UndertowHost
argument_list|>
name|undertowRegistry
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|UndertowHostKey
argument_list|,
name|UndertowHost
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|undertowHttpBinding
specifier|private
name|UndertowHttpBinding
name|undertowHttpBinding
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
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
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|hostOptions
specifier|private
name|UndertowHostOptions
name|hostOptions
decl_stmt|;
DECL|method|UndertowComponent ()
specifier|public
name|UndertowComponent
parameter_list|()
block|{     }
DECL|method|UndertowComponent (CamelContext context)
specifier|public
name|UndertowComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
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
name|URI
name|uriHttpUriAddress
init|=
operator|new
name|URI
argument_list|(
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|remaining
argument_list|)
argument_list|)
decl_stmt|;
name|URI
name|endpointUri
init|=
name|URISupport
operator|.
name|createRemainingURI
argument_list|(
name|uriHttpUriAddress
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
comment|// any additional channel options
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
name|IntrospectionSupport
operator|.
name|extractProperties
argument_list|(
name|parameters
argument_list|,
literal|"option."
argument_list|)
decl_stmt|;
comment|// determine sslContextParameters
name|SSLContextParameters
name|sslParams
init|=
name|this
operator|.
name|sslContextParameters
decl_stmt|;
if|if
condition|(
name|sslParams
operator|==
literal|null
condition|)
block|{
name|sslParams
operator|=
name|retrieveGlobalSslContextParameters
argument_list|()
expr_stmt|;
block|}
comment|// create the endpoint first
name|UndertowEndpoint
name|endpoint
init|=
name|createEndpointInstance
argument_list|(
name|endpointUri
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// set options from component
name|endpoint
operator|.
name|setSslContextParameters
argument_list|(
name|sslParams
argument_list|)
expr_stmt|;
comment|// Prefer endpoint configured over component configured
if|if
condition|(
name|undertowHttpBinding
operator|==
literal|null
condition|)
block|{
comment|// fallback to component configured
name|undertowHttpBinding
operator|=
name|getUndertowHttpBinding
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|undertowHttpBinding
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setUndertowHttpBinding
argument_list|(
name|undertowHttpBinding
argument_list|)
expr_stmt|;
block|}
comment|// set options from parameters
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setOptions
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
comment|// then re-create the http uri with the remaining parameters which the endpoint did not use
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
name|uriHttpUriAddress
operator|.
name|getScheme
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getUserInfo
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getHost
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getPort
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getPath
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getQuery
argument_list|()
argument_list|,
name|uriHttpUriAddress
operator|.
name|getFragment
argument_list|()
argument_list|)
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setHttpURI
argument_list|(
name|httpUri
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|createEndpointInstance (URI endpointUri, UndertowComponent component)
specifier|protected
name|UndertowEndpoint
name|createEndpointInstance
parameter_list|(
name|URI
name|endpointUri
parameter_list|,
name|UndertowComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
return|return
operator|new
name|UndertowEndpoint
argument_list|(
name|endpointUri
operator|.
name|toString
argument_list|()
argument_list|,
name|component
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
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
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
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
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
name|verb
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createApiConsumer (CamelContext camelContext, Processor processor, String contextPath, RestConfiguration configuration, Map<String, Object> parameters)
specifier|public
name|Consumer
name|createApiConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|contextPath
parameter_list|,
name|RestConfiguration
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
comment|// reuse the createConsumer method we already have. The api need to use GET and match on uri prefix
return|return
name|doCreateConsumer
argument_list|(
name|camelContext
argument_list|,
name|processor
argument_list|,
literal|"GET"
argument_list|,
name|contextPath
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|configuration
argument_list|,
name|parameters
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doCreateConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, RestConfiguration configuration, Map<String, Object> parameters, boolean api)
name|Consumer
name|doCreateConsumer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
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
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|RestConfiguration
name|configuration
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|,
name|boolean
name|api
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|path
init|=
name|basePath
decl_stmt|;
if|if
condition|(
name|uriTemplate
operator|!=
literal|null
condition|)
block|{
comment|// make sure to avoid double slashes
if|if
condition|(
name|uriTemplate
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|+
name|uriTemplate
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|path
operator|+
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
block|}
name|path
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|String
name|scheme
init|=
literal|"http"
decl_stmt|;
name|String
name|host
init|=
literal|""
decl_stmt|;
name|int
name|port
init|=
literal|0
decl_stmt|;
name|RestConfiguration
name|config
init|=
name|configuration
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|camelContext
operator|.
name|getRestConfiguration
argument_list|(
literal|"undertow"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|scheme
operator|=
name|config
operator|.
name|getScheme
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|.
name|getHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|host
operator|=
name|config
operator|.
name|getHost
argument_list|()
expr_stmt|;
block|}
name|int
name|num
init|=
name|config
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|num
operator|>
literal|0
condition|)
block|{
name|port
operator|=
name|num
expr_stmt|;
block|}
comment|// prefix path with context-path if configured in rest-dsl configuration
name|String
name|contextPath
init|=
name|config
operator|.
name|getContextPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripTrailingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
name|contextPath
operator|=
name|FileUtil
operator|.
name|stripLeadingSeparator
argument_list|(
name|contextPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|contextPath
argument_list|)
condition|)
block|{
name|path
operator|=
name|contextPath
operator|+
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
block|}
comment|// if no explicit hostname set then resolve the hostname
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|host
argument_list|)
condition|)
block|{
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|allLocalIp
condition|)
block|{
name|host
operator|=
literal|"0.0.0.0"
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localHostName
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalHostName
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|.
name|getRestHostNameResolver
argument_list|()
operator|==
name|RestConfiguration
operator|.
name|RestHostNameResolver
operator|.
name|localIp
condition|)
block|{
name|host
operator|=
name|HostUtils
operator|.
name|getLocalIp
argument_list|()
expr_stmt|;
block|}
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// build query string, and append any endpoint configuration properties
if|if
condition|(
name|config
operator|.
name|getComponent
argument_list|()
operator|==
literal|null
operator|||
name|config
operator|.
name|getComponent
argument_list|()
operator|.
name|equals
argument_list|(
literal|"undertow"
argument_list|)
condition|)
block|{
comment|// setup endpoint options
if|if
condition|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getEndpointProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|map
operator|.
name|putAll
argument_list|(
name|config
operator|.
name|getEndpointProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|boolean
name|cors
init|=
name|config
operator|.
name|isEnableCORS
argument_list|()
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
comment|// allow HTTP Options as we want to handle CORS in rest-dsl
name|map
operator|.
name|put
argument_list|(
literal|"optionsEnabled"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
name|String
name|query
init|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|map
argument_list|)
decl_stmt|;
name|String
name|url
decl_stmt|;
if|if
condition|(
name|api
condition|)
block|{
name|url
operator|=
literal|"undertow:%s://%s:%s/%s?httpMethodRestrict=%s"
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
literal|"undertow:%s://%s:%s/%s?matchOnUriPrefix=false&httpMethodRestrict=%s"
expr_stmt|;
block|}
comment|// must use upper case for restrict
name|String
name|restrict
init|=
name|verb
operator|.
name|toUpperCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
decl_stmt|;
if|if
condition|(
name|cors
condition|)
block|{
name|restrict
operator|+=
literal|",OPTIONS"
expr_stmt|;
block|}
comment|// get the endpoint
name|url
operator|=
name|String
operator|.
name|format
argument_list|(
name|url
argument_list|,
name|scheme
argument_list|,
name|host
argument_list|,
name|port
argument_list|,
name|path
argument_list|,
name|restrict
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|query
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|url
operator|=
name|url
operator|+
literal|"&"
operator|+
name|query
expr_stmt|;
block|}
name|UndertowEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|UndertowEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|map
operator|.
name|containsKey
argument_list|(
literal|"undertowHttpBinding"
argument_list|)
condition|)
block|{
comment|// use the rest binding, if not using a custom http binding
name|endpoint
operator|.
name|setUndertowHttpBinding
argument_list|(
operator|new
name|RestUndertowHttpBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// configure consumer properties
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getConsumerProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|camelContext
argument_list|,
name|consumer
argument_list|,
name|config
operator|.
name|getConsumerProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|consumer
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
literal|"undertow:"
operator|+
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
name|UndertowEndpoint
name|endpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|url
argument_list|,
name|UndertowEndpoint
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
name|UndertowRestHeaderFilterStrategy
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
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
literal|"undertow"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// configure additional options on undertow configuration
if|if
condition|(
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|config
operator|.
name|getComponentProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperties
argument_list|(
name|this
argument_list|,
name|config
operator|.
name|getComponentProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|registerConsumer (UndertowConsumer consumer)
specifier|public
name|void
name|registerConsumer
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|)
block|{
name|URI
name|uri
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
decl_stmt|;
name|UndertowHostKey
name|key
init|=
operator|new
name|UndertowHostKey
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSslContext
argument_list|()
argument_list|)
decl_stmt|;
name|UndertowHost
name|host
init|=
name|undertowRegistry
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|host
operator|==
literal|null
condition|)
block|{
name|host
operator|=
name|createUndertowHost
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|undertowRegistry
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|host
argument_list|)
expr_stmt|;
block|}
name|host
operator|.
name|validateEndpointURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|host
operator|.
name|registerHandler
argument_list|(
name|consumer
operator|.
name|getHttpHandlerRegistrationInfo
argument_list|()
argument_list|,
name|consumer
operator|.
name|getHttpHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|unregisterConsumer (UndertowConsumer consumer)
specifier|public
name|void
name|unregisterConsumer
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|)
block|{
name|URI
name|uri
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
decl_stmt|;
name|UndertowHostKey
name|key
init|=
operator|new
name|UndertowHostKey
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|,
name|uri
operator|.
name|getPort
argument_list|()
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getSslContext
argument_list|()
argument_list|)
decl_stmt|;
name|UndertowHost
name|host
init|=
name|undertowRegistry
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|host
operator|.
name|unregisterHandler
argument_list|(
name|consumer
operator|.
name|getHttpHandlerRegistrationInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createUndertowHost (UndertowHostKey key)
specifier|protected
name|UndertowHost
name|createUndertowHost
parameter_list|(
name|UndertowHostKey
name|key
parameter_list|)
block|{
return|return
operator|new
name|DefaultUndertowHost
argument_list|(
name|key
argument_list|,
name|hostOptions
argument_list|)
return|;
block|}
DECL|method|getUndertowHttpBinding ()
specifier|public
name|UndertowHttpBinding
name|getUndertowHttpBinding
parameter_list|()
block|{
return|return
name|undertowHttpBinding
return|;
block|}
comment|/**      * To use a custom HttpBinding to control the mapping between Camel message and HttpClient.      */
DECL|method|setUndertowHttpBinding (UndertowHttpBinding undertowHttpBinding)
specifier|public
name|void
name|setUndertowHttpBinding
parameter_list|(
name|UndertowHttpBinding
name|undertowHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|undertowHttpBinding
operator|=
name|undertowHttpBinding
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
comment|/**      * To configure security using SSLContextParameters      */
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
DECL|method|getHostOptions ()
specifier|public
name|UndertowHostOptions
name|getHostOptions
parameter_list|()
block|{
return|return
name|hostOptions
return|;
block|}
comment|/**      * To configure common options, such as thread pools      */
DECL|method|setHostOptions (UndertowHostOptions hostOptions)
specifier|public
name|void
name|setHostOptions
parameter_list|(
name|UndertowHostOptions
name|hostOptions
parameter_list|)
block|{
name|this
operator|.
name|hostOptions
operator|=
name|hostOptions
expr_stmt|;
block|}
comment|/**      *      */
DECL|method|getVerifier ()
specifier|public
name|ComponentVerifier
name|getVerifier
parameter_list|()
block|{
return|return
operator|new
name|UndertowComponentVerifier
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
end_class

end_unit

