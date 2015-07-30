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
name|Map
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Handlers
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|Undertow
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|handlers
operator|.
name|PathHandler
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
name|component
operator|.
name|undertow
operator|.
name|handlers
operator|.
name|HttpCamelHandler
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
name|undertow
operator|.
name|handlers
operator|.
name|NotFoundHandler
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
name|UriEndpointComponent
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
DECL|class|UndertowComponent
specifier|public
class|class
name|UndertowComponent
extends|extends
name|UriEndpointComponent
implements|implements
name|RestConsumerFactory
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
DECL|field|undertowHttpBinding
specifier|private
name|UndertowHttpBinding
name|undertowHttpBinding
init|=
operator|new
name|DefaultUndertowHttpBinding
argument_list|()
decl_stmt|;
DECL|field|serversRegistry
specifier|private
specifier|final
name|Map
argument_list|<
name|Integer
argument_list|,
name|UndertowRegistry
argument_list|>
name|serversRegistry
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|UndertowRegistry
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|UndertowComponent ()
specifier|public
name|UndertowComponent
parameter_list|()
block|{
name|super
argument_list|(
name|UndertowEndpoint
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
name|endpoint
operator|.
name|setUndertowHttpBinding
argument_list|(
name|undertowHttpBinding
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
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
DECL|method|createConsumer (CamelContext camelContext, Processor processor, String verb, String basePath, String uriTemplate, String consumes, String produces, Map<String, Object> parameters)
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
operator|!=
literal|null
operator|&&
operator|(
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
operator|)
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
init|=
literal|"undertow:%s://%s:%s/%s"
decl_stmt|;
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
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
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
return|return
name|consumer
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|serversRegistry
operator|.
name|clear
argument_list|()
expr_stmt|;
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
name|int
name|port
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|serversRegistry
operator|.
name|containsKey
argument_list|(
name|port
argument_list|)
condition|)
block|{
comment|//server listens on port, we need add configuration for path
name|UndertowRegistry
name|undertowRegistry
init|=
name|serversRegistry
operator|.
name|get
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|undertowRegistry
operator|.
name|registerConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//create new server to listen on specified port
name|serversRegistry
operator|.
name|put
argument_list|(
name|port
argument_list|,
operator|new
name|UndertowRegistry
argument_list|(
name|consumer
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|int
name|port
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|serversRegistry
operator|.
name|containsKey
argument_list|(
name|port
argument_list|)
condition|)
block|{
name|serversRegistry
operator|.
name|get
argument_list|(
name|port
argument_list|)
operator|.
name|unregisterConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|serversRegistry
operator|.
name|get
argument_list|(
name|port
argument_list|)
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|//if there no Consumer left, we can shut down server
name|Undertow
name|server
init|=
name|serversRegistry
operator|.
name|get
argument_list|(
name|port
argument_list|)
operator|.
name|getServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|serversRegistry
operator|.
name|remove
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//call startServer to rebuild otherwise
name|startServer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|startServer (UndertowConsumer consumer)
specifier|public
name|void
name|startServer
parameter_list|(
name|UndertowConsumer
name|consumer
parameter_list|)
block|{
name|int
name|port
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting server on port: {}"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|UndertowRegistry
name|undertowRegistry
init|=
name|serversRegistry
operator|.
name|get
argument_list|(
name|port
argument_list|)
decl_stmt|;
if|if
condition|(
name|undertowRegistry
operator|.
name|getServer
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|//server is running, we need to stop it first and then rebuild
name|undertowRegistry
operator|.
name|getServer
argument_list|()
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|Undertow
name|newServer
init|=
name|rebuildServer
argument_list|(
name|undertowRegistry
argument_list|)
decl_stmt|;
name|newServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|undertowRegistry
operator|.
name|setServer
argument_list|(
name|newServer
argument_list|)
expr_stmt|;
block|}
DECL|method|rebuildServer (UndertowRegistry registy)
specifier|protected
name|Undertow
name|rebuildServer
parameter_list|(
name|UndertowRegistry
name|registy
parameter_list|)
block|{
name|Undertow
operator|.
name|Builder
name|result
init|=
name|Undertow
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|registy
operator|.
name|getSslContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|addHttpsListener
argument_list|(
name|registy
operator|.
name|getPort
argument_list|()
argument_list|,
name|registy
operator|.
name|getHost
argument_list|()
argument_list|,
name|registy
operator|.
name|getSslContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|result
operator|.
name|addHttpListener
argument_list|(
name|registy
operator|.
name|getPort
argument_list|()
argument_list|,
name|registy
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|PathHandler
name|path
init|=
name|Handlers
operator|.
name|path
argument_list|(
operator|new
name|NotFoundHandler
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|URI
name|key
range|:
name|registy
operator|.
name|getConsumersRegistry
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|UndertowConsumer
name|consumer
init|=
name|registy
operator|.
name|getConsumersRegistry
argument_list|()
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|URI
name|httpUri
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpURI
argument_list|()
decl_stmt|;
name|HttpCamelHandler
name|handler
init|=
operator|new
name|HttpCamelHandler
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getMatchOnUriPrefix
argument_list|()
condition|)
block|{
name|path
operator|.
name|addPrefixPath
argument_list|(
name|httpUri
operator|.
name|getPath
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|path
operator|.
name|addExactPath
argument_list|(
name|httpUri
operator|.
name|getPath
argument_list|()
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Rebuild for path: {}"
argument_list|,
name|httpUri
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|result
operator|.
name|setHandler
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|build
argument_list|()
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
block|}
end_class

end_unit

