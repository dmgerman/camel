begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
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
name|ExchangePattern
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
name|ExtendedCamelContext
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
name|NoSuchBeanException
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
name|spi
operator|.
name|FactoryFinder
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
name|RestApiProcessorFactory
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|DefaultEndpoint
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The rest-api component is used for providing Swagger API of the REST services which has been defined using the rest-dsl in Camel.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"rest-api"
argument_list|,
name|title
operator|=
literal|"REST API"
argument_list|,
name|syntax
operator|=
literal|"rest-api:path/contextIdPattern"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,rest"
argument_list|,
name|lenientProperties
operator|=
literal|true
argument_list|)
DECL|class|RestApiEndpoint
specifier|public
class|class
name|RestApiEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|DEFAULT_API_COMPONENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_API_COMPONENT_NAME
init|=
literal|"swagger"
decl_stmt|;
DECL|field|RESOURCE_PATH
specifier|public
specifier|static
specifier|final
name|String
name|RESOURCE_PATH
init|=
literal|"META-INF/services/org/apache/camel/restapi/"
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriPath
DECL|field|contextIdPattern
specifier|private
name|String
name|contextIdPattern
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumerComponentName
specifier|private
name|String
name|consumerComponentName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|apiComponentName
specifier|private
name|String
name|apiComponentName
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|method|RestApiEndpoint (String endpointUri, RestApiComponent component)
specifier|public
name|RestApiEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RestApiComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|RestApiComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|RestApiComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
comment|/**      * The base path      */
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getContextIdPattern ()
specifier|public
name|String
name|getContextIdPattern
parameter_list|()
block|{
return|return
name|contextIdPattern
return|;
block|}
comment|/**      * Optional CamelContext id pattern to only allow Rest APIs from rest services within CamelContext's which name matches the pattern.      */
DECL|method|setContextIdPattern (String contextIdPattern)
specifier|public
name|void
name|setContextIdPattern
parameter_list|(
name|String
name|contextIdPattern
parameter_list|)
block|{
name|this
operator|.
name|contextIdPattern
operator|=
name|contextIdPattern
expr_stmt|;
block|}
DECL|method|getConsumerComponentName ()
specifier|public
name|String
name|getConsumerComponentName
parameter_list|()
block|{
return|return
name|consumerComponentName
return|;
block|}
comment|/**      * The Camel Rest component to use for (consumer) the REST transport, such as jetty, servlet, undertow.      * If no component has been explicit configured, then Camel will lookup if there is a Camel component      * that integrates with the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory is registered in the registry.      * If either one is found, then that is being used.      */
DECL|method|setConsumerComponentName (String consumerComponentName)
specifier|public
name|void
name|setConsumerComponentName
parameter_list|(
name|String
name|consumerComponentName
parameter_list|)
block|{
name|this
operator|.
name|consumerComponentName
operator|=
name|consumerComponentName
expr_stmt|;
block|}
DECL|method|getApiComponentName ()
specifier|public
name|String
name|getApiComponentName
parameter_list|()
block|{
return|return
name|apiComponentName
return|;
block|}
comment|/**      * The Camel Rest API component to use for generating the API of the REST services, such as swagger.      */
DECL|method|setApiComponentName (String apiComponentName)
specifier|public
name|void
name|setApiComponentName
parameter_list|(
name|String
name|apiComponentName
parameter_list|)
block|{
name|this
operator|.
name|apiComponentName
operator|=
name|apiComponentName
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Additional parameters to configure the consumer of the REST transport for this REST service      */
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
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
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|RestApiProcessorFactory
name|factory
init|=
literal|null
decl_stmt|;
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
name|consumerComponentName
argument_list|,
literal|false
argument_list|)
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
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
name|consumerComponentName
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// lookup in registry
name|Set
argument_list|<
name|RestApiProcessorFactory
argument_list|>
name|factories
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|RestApiProcessorFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factories
operator|!=
literal|null
operator|&&
name|factories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|factory
operator|=
name|factories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
comment|// lookup on classpath using factory finder to automatic find it (just add camel-swagger-java to classpath etc)
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|String
name|name
init|=
name|apiComponentName
operator|!=
literal|null
condition|?
name|apiComponentName
else|:
name|config
operator|.
name|getApiComponent
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|name
operator|=
name|DEFAULT_API_COMPONENT_NAME
expr_stmt|;
block|}
name|FactoryFinder
name|finder
init|=
name|getCamelContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getFactoryFinder
argument_list|(
name|RESOURCE_PATH
argument_list|)
decl_stmt|;
name|factory
operator|=
name|finder
operator|.
name|newInstance
argument_list|(
name|name
argument_list|,
name|RestApiProcessorFactory
operator|.
name|class
argument_list|)
operator|.
name|orElse
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
comment|// if no explicit port/host configured, then use port from rest configuration
name|String
name|host
init|=
literal|""
decl_stmt|;
name|int
name|port
init|=
literal|80
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|getApiHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|host
operator|=
name|config
operator|.
name|getApiHost
argument_list|()
expr_stmt|;
block|}
elseif|else
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
name|getHostNameResolver
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
name|getHostNameResolver
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
name|getHostNameResolver
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
comment|// no host was configured so calculate a host to use
comment|// there should be no schema in the host (but only port)
name|String
name|targetHost
init|=
name|host
operator|+
operator|(
name|port
operator|!=
literal|80
condition|?
literal|":"
operator|+
name|port
else|:
literal|""
operator|)
decl_stmt|;
name|getParameters
argument_list|()
operator|.
name|put
argument_list|(
literal|"host"
argument_list|,
name|targetHost
argument_list|)
expr_stmt|;
block|}
comment|// the base path should start with a leading slash
name|String
name|path
init|=
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
comment|// whether listing of the context id's is enabled or not
name|boolean
name|contextIdListing
init|=
name|config
operator|.
name|isApiContextListing
argument_list|()
decl_stmt|;
name|Processor
name|processor
init|=
name|factory
operator|.
name|createApiProcessor
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|path
argument_list|,
name|getContextIdPattern
argument_list|()
argument_list|,
name|contextIdListing
argument_list|,
name|config
argument_list|,
name|getParameters
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|RestApiProducer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find RestApiProcessorFactory in Registry or classpath (such as the camel-swagger-java component)"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|RestApiConsumerFactory
name|factory
init|=
literal|null
decl_stmt|;
name|String
name|cname
init|=
literal|null
decl_stmt|;
comment|// we use the rest component as the HTTP consumer to service the API
comment|// the API then uses the api component (eg usually camel-swagger-java) to build the API
if|if
condition|(
name|getConsumerComponentName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|comp
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByName
argument_list|(
name|getConsumerComponentName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestApiConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestApiConsumerFactory
operator|)
name|comp
expr_stmt|;
block|}
else|else
block|{
name|comp
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|getConsumerComponentName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestApiConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestApiConsumerFactory
operator|)
name|comp
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|comp
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Component "
operator|+
name|getConsumerComponentName
argument_list|()
operator|+
literal|" is not a RestApiConsumerFactory"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|getConsumerComponentName
argument_list|()
argument_list|,
name|RestApiConsumerFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|cname
operator|=
name|getConsumerComponentName
argument_list|()
expr_stmt|;
block|}
comment|// try all components
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
for|for
control|(
name|String
name|name
range|:
name|getCamelContext
argument_list|()
operator|.
name|getComponentNames
argument_list|()
control|)
block|{
name|Component
name|comp
init|=
name|getCamelContext
argument_list|()
operator|.
name|getComponent
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|instanceof
name|RestApiConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestApiConsumerFactory
operator|)
name|comp
expr_stmt|;
name|cname
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
block|}
comment|// lookup in registry
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|RestApiConsumerFactory
argument_list|>
name|factories
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByType
argument_list|(
name|RestApiConsumerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|factories
operator|!=
literal|null
operator|&&
name|factories
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|factory
operator|=
name|factories
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
comment|// calculate the url to the rest API service
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|(
name|cname
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// calculate the url to the rest API service
name|String
name|path
init|=
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
operator|!
name|path
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|"/"
operator|+
name|path
expr_stmt|;
block|}
name|Consumer
name|consumer
init|=
name|factory
operator|.
name|createApiConsumer
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|processor
argument_list|,
name|path
argument_list|,
name|config
argument_list|,
name|getParameters
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Cannot find RestApiConsumerFactory in Registry or as a Component to use"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

