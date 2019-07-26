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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CamelContextAware
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
name|Exchange
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
name|Route
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
name|Service
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
name|ServiceStatus
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
name|StatefulService
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
name|StaticService
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
name|RestRegistry
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
name|LifecycleStrategySupport
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
name|service
operator|.
name|ServiceSupport
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
DECL|class|DefaultRestRegistry
specifier|public
class|class
name|DefaultRestRegistry
extends|extends
name|ServiceSupport
implements|implements
name|StaticService
implements|,
name|RestRegistry
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|registry
specifier|private
specifier|final
name|Map
argument_list|<
name|Consumer
argument_list|,
name|RestService
argument_list|>
name|registry
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|apiProducer
specifier|private
specifier|transient
name|Producer
name|apiProducer
decl_stmt|;
DECL|method|addRestService (Consumer consumer, String url, String baseUrl, String basePath, String uriTemplate, String method, String consumes, String produces, String inType, String outType, String routeId, String description)
specifier|public
name|void
name|addRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|baseUrl
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|String
name|inType
parameter_list|,
name|String
name|outType
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|RestServiceEntry
name|entry
init|=
operator|new
name|RestServiceEntry
argument_list|(
name|consumer
argument_list|,
name|url
argument_list|,
name|baseUrl
argument_list|,
name|basePath
argument_list|,
name|uriTemplate
argument_list|,
name|method
argument_list|,
name|consumes
argument_list|,
name|produces
argument_list|,
name|inType
argument_list|,
name|outType
argument_list|,
name|routeId
argument_list|,
name|description
argument_list|)
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
name|consumer
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
DECL|method|removeRestService (Consumer consumer)
specifier|public
name|void
name|removeRestService
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
name|registry
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|listAllRestServices ()
specifier|public
name|List
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
name|listAllRestServices
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|registry
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|registry
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|apiDocAsJson ()
specifier|public
name|String
name|apiDocAsJson
parameter_list|()
block|{
comment|// see if there is a rest-api endpoint which would be the case if rest api-doc has been explicit enabled
if|if
condition|(
name|apiProducer
operator|==
literal|null
condition|)
block|{
name|Endpoint
name|restApiEndpoint
init|=
literal|null
decl_stmt|;
name|Endpoint
name|restEndpoint
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|entry
range|:
name|camelContext
operator|.
name|getEndpointMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|uri
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|startsWith
argument_list|(
literal|"rest-api:"
argument_list|)
condition|)
block|{
name|restApiEndpoint
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|restEndpoint
operator|==
literal|null
operator|&&
name|uri
operator|.
name|startsWith
argument_list|(
literal|"rest:"
argument_list|)
condition|)
block|{
name|restEndpoint
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|restApiEndpoint
operator|==
literal|null
operator|&&
name|restEndpoint
operator|!=
literal|null
condition|)
block|{
comment|// no rest-api has been explicit enabled, then we need to create it first
name|RestEndpoint
name|rest
init|=
operator|(
name|RestEndpoint
operator|)
name|restEndpoint
decl_stmt|;
name|String
name|componentName
init|=
name|rest
operator|.
name|getProducerComponentName
argument_list|()
decl_stmt|;
if|if
condition|(
name|componentName
operator|!=
literal|null
condition|)
block|{
name|RestConfiguration
name|config
init|=
name|camelContext
operator|.
name|getRestConfiguration
argument_list|(
name|componentName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|String
name|apiComponent
init|=
name|config
operator|.
name|getApiComponent
argument_list|()
operator|!=
literal|null
condition|?
name|config
operator|.
name|getApiComponent
argument_list|()
else|:
name|RestApiEndpoint
operator|.
name|DEFAULT_API_COMPONENT_NAME
decl_stmt|;
name|String
name|path
init|=
name|config
operator|.
name|getApiContextPath
argument_list|()
operator|!=
literal|null
condition|?
name|config
operator|.
name|getApiContextPath
argument_list|()
else|:
literal|"api-doc"
decl_stmt|;
name|restApiEndpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"rest-api:%s/%s?componentName=%s&apiComponentName=%s&contextIdPattern=#name#"
argument_list|,
name|path
argument_list|,
name|camelContext
operator|.
name|getName
argument_list|()
argument_list|,
name|componentName
argument_list|,
name|apiComponent
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|restApiEndpoint
operator|!=
literal|null
condition|)
block|{
comment|// reuse the producer to avoid creating it
try|try
block|{
name|apiProducer
operator|=
name|restApiEndpoint
operator|.
name|createProducer
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|addService
argument_list|(
name|apiProducer
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
block|}
if|if
condition|(
name|apiProducer
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Exchange
name|dummy
init|=
name|apiProducer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|apiProducer
operator|.
name|process
argument_list|(
name|dummy
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|dummy
operator|.
name|getMessage
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|json
return|;
block|}
catch|catch
parameter_list|(
name|Exception
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
block|}
return|return
literal|null
return|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// add a lifecycle so we can keep track when consumers is being removed, so we can unregister them from our registry
name|camelContext
operator|.
name|addLifecycleStrategy
argument_list|(
operator|new
name|RemoveRestServiceLifecycleStrategy
argument_list|()
argument_list|)
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
name|registry
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Represents a rest service      */
DECL|class|RestServiceEntry
specifier|private
specifier|final
class|class
name|RestServiceEntry
implements|implements
name|RestService
block|{
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
name|consumer
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|field|baseUrl
specifier|private
specifier|final
name|String
name|baseUrl
decl_stmt|;
DECL|field|basePath
specifier|private
specifier|final
name|String
name|basePath
decl_stmt|;
DECL|field|uriTemplate
specifier|private
specifier|final
name|String
name|uriTemplate
decl_stmt|;
DECL|field|method
specifier|private
specifier|final
name|String
name|method
decl_stmt|;
DECL|field|consumes
specifier|private
specifier|final
name|String
name|consumes
decl_stmt|;
DECL|field|produces
specifier|private
specifier|final
name|String
name|produces
decl_stmt|;
DECL|field|inType
specifier|private
specifier|final
name|String
name|inType
decl_stmt|;
DECL|field|outType
specifier|private
specifier|final
name|String
name|outType
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|method|RestServiceEntry (Consumer consumer, String url, String baseUrl, String basePath, String uriTemplate, String method, String consumes, String produces, String inType, String outType, String routeId, String description)
specifier|private
name|RestServiceEntry
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|baseUrl
parameter_list|,
name|String
name|basePath
parameter_list|,
name|String
name|uriTemplate
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|consumes
parameter_list|,
name|String
name|produces
parameter_list|,
name|String
name|inType
parameter_list|,
name|String
name|outType
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|baseUrl
operator|=
name|baseUrl
expr_stmt|;
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
name|this
operator|.
name|uriTemplate
operator|=
name|uriTemplate
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
name|this
operator|.
name|inType
operator|=
name|inType
expr_stmt|;
name|this
operator|.
name|outType
operator|=
name|outType
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
DECL|method|getConsumer ()
specifier|public
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|getBaseUrl ()
specifier|public
name|String
name|getBaseUrl
parameter_list|()
block|{
return|return
name|baseUrl
return|;
block|}
DECL|method|getBasePath ()
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
return|;
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
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
name|ServiceStatus
name|status
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|consumer
operator|instanceof
name|StatefulService
condition|)
block|{
name|status
operator|=
operator|(
operator|(
name|StatefulService
operator|)
name|consumer
operator|)
operator|.
name|getStatus
argument_list|()
expr_stmt|;
block|}
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
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
block|}
comment|/**      * A {@link org.apache.camel.spi.LifecycleStrategy} that keeps track when a {@link Consumer} is removed      * and automatic un-register it from this REST registry.      */
DECL|class|RemoveRestServiceLifecycleStrategy
specifier|private
specifier|final
class|class
name|RemoveRestServiceLifecycleStrategy
extends|extends
name|LifecycleStrategySupport
block|{
annotation|@
name|Override
DECL|method|onServiceRemove (CamelContext context, Service service, Route route)
specifier|public
name|void
name|onServiceRemove
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|super
operator|.
name|onServiceRemove
argument_list|(
name|context
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
expr_stmt|;
comment|// if its a consumer then de-register it from the rest registry
if|if
condition|(
name|service
operator|instanceof
name|Consumer
condition|)
block|{
name|removeRestService
argument_list|(
operator|(
name|Consumer
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

