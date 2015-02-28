begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"rest"
argument_list|,
name|syntax
operator|=
literal|"rest:method:path:uriTemplate"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"core,http,rest"
argument_list|)
DECL|class|RestEndpoint
specifier|public
class|class
name|RestEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"get,post,put,delete,patch,head,trace,connect,options"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|UriPath
DECL|field|uriTemplate
specifier|private
name|String
name|uriTemplate
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumes
specifier|private
name|String
name|consumes
decl_stmt|;
annotation|@
name|UriParam
DECL|field|produces
specifier|private
name|String
name|produces
decl_stmt|;
annotation|@
name|UriParam
DECL|field|componentName
specifier|private
name|String
name|componentName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|inType
specifier|private
name|String
name|inType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outType
specifier|private
name|String
name|outType
decl_stmt|;
annotation|@
name|UriParam
DECL|field|routeId
specifier|private
name|String
name|routeId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|description
specifier|private
name|String
name|description
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
DECL|method|RestEndpoint (String endpointUri, RestComponent component)
specifier|public
name|RestEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RestComponent
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
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|RestComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|RestComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
comment|/**      * HTTP method to use.      */
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
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
DECL|method|getUriTemplate ()
specifier|public
name|String
name|getUriTemplate
parameter_list|()
block|{
return|return
name|uriTemplate
return|;
block|}
comment|/**      * The uri template      */
DECL|method|setUriTemplate (String uriTemplate)
specifier|public
name|void
name|setUriTemplate
parameter_list|(
name|String
name|uriTemplate
parameter_list|)
block|{
name|this
operator|.
name|uriTemplate
operator|=
name|uriTemplate
expr_stmt|;
block|}
DECL|method|getConsumes ()
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumes
return|;
block|}
comment|/**      * Media type such as: 'text/xml', or 'application/json' this REST service accepts.      * By default we accept all kinds of types.      */
DECL|method|setConsumes (String consumes)
specifier|public
name|void
name|setConsumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
block|}
DECL|method|getProduces ()
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|produces
return|;
block|}
comment|/**      * Media type such as: 'text/xml', or 'application/json' this REST service returns.      */
DECL|method|setProduces (String produces)
specifier|public
name|void
name|setProduces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
block|}
DECL|method|getComponentName ()
specifier|public
name|String
name|getComponentName
parameter_list|()
block|{
return|return
name|componentName
return|;
block|}
comment|/**      * The Camel Rest component to use for the REST transport, such as restlet, spark-rest.      * If no component has been explicit configured, then Camel will lookup if there is a Camel component      * that integrates with the Rest DSL, or if a org.apache.camel.spi.RestConsumerFactory is registered in the registry.      * If either one is found, then that is being used.      */
DECL|method|setComponentName (String componentName)
specifier|public
name|void
name|setComponentName
parameter_list|(
name|String
name|componentName
parameter_list|)
block|{
name|this
operator|.
name|componentName
operator|=
name|componentName
expr_stmt|;
block|}
DECL|method|getInType ()
specifier|public
name|String
name|getInType
parameter_list|()
block|{
return|return
name|inType
return|;
block|}
comment|/**      * To declare the incoming POJO binding type as a FQN class name      */
DECL|method|setInType (String inType)
specifier|public
name|void
name|setInType
parameter_list|(
name|String
name|inType
parameter_list|)
block|{
name|this
operator|.
name|inType
operator|=
name|inType
expr_stmt|;
block|}
DECL|method|getOutType ()
specifier|public
name|String
name|getOutType
parameter_list|()
block|{
return|return
name|outType
return|;
block|}
comment|/**      * To declare the outgoing POJO binding type as a FQN class name      */
DECL|method|setOutType (String outType)
specifier|public
name|void
name|setOutType
parameter_list|(
name|String
name|outType
parameter_list|)
block|{
name|this
operator|.
name|outType
operator|=
name|outType
expr_stmt|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
comment|/**      * Name of the route this REST services creates      */
DECL|method|setRouteId (String routeId)
specifier|public
name|void
name|setRouteId
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Human description to document this REST service      */
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
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
name|RestConsumerFactory
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getComponentName
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
name|getComponentName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
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
name|getComponentName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|comp
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
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
name|getComponentName
argument_list|()
operator|+
literal|" is not a RestConsumerFactory"
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|NoSuchBeanException
argument_list|(
name|getComponentName
argument_list|()
argument_list|,
name|RestConsumerFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
block|}
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
operator|!=
literal|null
operator|&&
name|comp
operator|instanceof
name|RestConsumerFactory
condition|)
block|{
name|factory
operator|=
operator|(
name|RestConsumerFactory
operator|)
name|comp
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
name|RestConsumerFactory
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
name|RestConsumerFactory
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
name|Consumer
name|consumer
init|=
name|factory
operator|.
name|createConsumer
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|processor
argument_list|,
name|getMethod
argument_list|()
argument_list|,
name|getPath
argument_list|()
argument_list|,
name|getUriTemplate
argument_list|()
argument_list|,
name|getConsumes
argument_list|()
argument_list|,
name|getProduces
argument_list|()
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
comment|// if no explicit port/host configured, then use port from rest configuration
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
literal|80
decl_stmt|;
name|RestConfiguration
name|config
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRestConfiguration
argument_list|()
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
comment|// calculate the url to the rest service
name|String
name|path
init|=
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
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
comment|// there may be an optional context path configured to help Camel calculate the correct urls for the REST services
comment|// this may be needed when using camel-serlvet where we cannot get the actual context-path or port number of the servlet engine
comment|// during init of the servlet
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
name|contextPath
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|contextPath
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
name|contextPath
operator|+
name|path
expr_stmt|;
block|}
else|else
block|{
name|path
operator|=
name|contextPath
operator|+
name|path
expr_stmt|;
block|}
block|}
name|String
name|baseUrl
init|=
name|scheme
operator|+
literal|"://"
operator|+
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
operator|+
name|path
decl_stmt|;
name|String
name|url
init|=
name|baseUrl
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
name|url
operator|=
name|url
operator|+
name|uriTemplate
expr_stmt|;
block|}
else|else
block|{
name|url
operator|=
name|url
operator|+
literal|"/"
operator|+
name|uriTemplate
expr_stmt|;
block|}
block|}
comment|// add to rest registry so we can keep track of them, we will remove from the registry when the consumer is removed
comment|// the rest registry will automatic keep track when the consumer is removed,
comment|// and un-register the REST service from the registry
name|getCamelContext
argument_list|()
operator|.
name|getRestRegistry
argument_list|()
operator|.
name|addRestService
argument_list|(
name|consumer
argument_list|,
name|url
argument_list|,
name|baseUrl
argument_list|,
name|getPath
argument_list|()
argument_list|,
name|getUriTemplate
argument_list|()
argument_list|,
name|getMethod
argument_list|()
argument_list|,
name|getConsumes
argument_list|()
argument_list|,
name|getProduces
argument_list|()
argument_list|,
name|getInType
argument_list|()
argument_list|,
name|getOutType
argument_list|()
argument_list|,
name|getRouteId
argument_list|()
argument_list|,
name|getDescription
argument_list|()
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
literal|"Cannot find RestConsumerFactory in Registry or as a Component to use"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
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

