begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
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
name|ErrorHandlerFactory
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
name|NamedNode
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
name|NoSuchEndpointException
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
name|ShutdownRoute
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
name|ShutdownRunningTask
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
name|processor
operator|.
name|CamelInternalProcessor
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
name|processor
operator|.
name|Pipeline
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
name|CamelInternalProcessorAdvice
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
name|InterceptStrategy
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
name|ManagementInterceptStrategy
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
name|RouteContext
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
name|RouteController
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
name|RouteError
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
name|RoutePolicy
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * The context used to activate new routing rules  */
end_comment

begin_class
DECL|class|DefaultRouteContext
specifier|public
class|class
name|DefaultRouteContext
implements|implements
name|RouteContext
block|{
DECL|field|route
specifier|private
specifier|final
name|NamedNode
name|route
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|runtimeRoute
specifier|private
name|Route
name|runtimeRoute
decl_stmt|;
DECL|field|endpoint
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|eventDrivenProcessors
specifier|private
specifier|final
name|List
argument_list|<
name|Processor
argument_list|>
name|eventDrivenProcessors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|interceptStrategies
specifier|private
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|managementInterceptStrategy
specifier|private
name|ManagementInterceptStrategy
name|managementInterceptStrategy
decl_stmt|;
DECL|field|routeAdded
specifier|private
name|boolean
name|routeAdded
decl_stmt|;
DECL|field|trace
specifier|private
name|Boolean
name|trace
decl_stmt|;
DECL|field|messageHistory
specifier|private
name|Boolean
name|messageHistory
decl_stmt|;
DECL|field|logMask
specifier|private
name|Boolean
name|logMask
decl_stmt|;
DECL|field|logExhaustedMessageBody
specifier|private
name|Boolean
name|logExhaustedMessageBody
decl_stmt|;
DECL|field|streamCache
specifier|private
name|Boolean
name|streamCache
decl_stmt|;
DECL|field|handleFault
specifier|private
name|Boolean
name|handleFault
decl_stmt|;
DECL|field|delay
specifier|private
name|Long
name|delay
decl_stmt|;
DECL|field|autoStartup
specifier|private
name|Boolean
name|autoStartup
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
DECL|field|routePolicyList
specifier|private
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicyList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|shutdownRoute
specifier|private
name|ShutdownRoute
name|shutdownRoute
decl_stmt|;
DECL|field|shutdownRunningTask
specifier|private
name|ShutdownRunningTask
name|shutdownRunningTask
decl_stmt|;
DECL|field|routeError
specifier|private
name|RouteError
name|routeError
decl_stmt|;
DECL|field|routeController
specifier|private
name|RouteController
name|routeController
decl_stmt|;
DECL|field|onCompletions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Processor
argument_list|>
name|onCompletions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|onExceptions
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Processor
argument_list|>
name|onExceptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|advices
specifier|private
specifier|final
name|List
argument_list|<
name|CamelInternalProcessorAdvice
argument_list|<
name|?
argument_list|>
argument_list|>
name|advices
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|properties
specifier|private
specifier|final
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
DECL|field|errorHandlerFactory
specifier|private
name|ErrorHandlerFactory
name|errorHandlerFactory
decl_stmt|;
DECL|field|startupOrder
specifier|private
name|Integer
name|startupOrder
decl_stmt|;
DECL|method|DefaultRouteContext (CamelContext camelContext, NamedNode route, String routeId)
specifier|public
name|DefaultRouteContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|NamedNode
name|route
parameter_list|,
name|String
name|routeId
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|setEndpoint (Endpoint endpoint)
specifier|public
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getRoute ()
specifier|public
name|NamedNode
name|getRoute
parameter_list|()
block|{
return|return
name|route
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
DECL|method|getRuntimeRoute ()
specifier|public
name|Route
name|getRuntimeRoute
parameter_list|()
block|{
return|return
name|runtimeRoute
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
DECL|method|resolveEndpoint (String uri)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|getMandatoryEndpoint
argument_list|(
name|camelContext
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (String uri, String ref)
specifier|public
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|uri
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|uri
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|ref
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|=
name|lookup
argument_list|(
name|ref
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
literal|"ref:"
operator|+
name|ref
argument_list|,
literal|"check your camel registry with id "
operator|+
name|ref
argument_list|)
throw|;
block|}
comment|// Check the endpoint has the right CamelContext
if|if
condition|(
operator|!
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
literal|"ref:"
operator|+
name|ref
argument_list|,
literal|"make sure the endpoint has the same camel context as the route does."
argument_list|)
throw|;
block|}
try|try
block|{
comment|// need add the endpoint into service
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either 'uri' or 'ref' must be specified on: "
operator|+
name|this
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|endpoint
return|;
block|}
block|}
DECL|method|lookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|lookupByType (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|mandatoryLookup (String name, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryLookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
return|;
block|}
DECL|method|commit ()
specifier|public
name|Route
name|commit
parameter_list|()
block|{
comment|// now lets turn all of the event driven consumer processors into a single route
if|if
condition|(
operator|!
name|eventDrivenProcessors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Processor
name|target
init|=
name|Pipeline
operator|.
name|newInstance
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|eventDrivenProcessors
argument_list|)
decl_stmt|;
comment|// and wrap it in a unit of work so the UoW is on the top, so the entire route will be in the same UoW
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
comment|// and then optionally add route policy processor if a custom policy is set
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicyList
init|=
name|getRoutePolicyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|routePolicyList
operator|!=
literal|null
operator|&&
operator|!
name|routePolicyList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|policy
range|:
name|routePolicyList
control|)
block|{
comment|// add policy as service if we have not already done that (eg possible if two routes have the same service)
comment|// this ensures Camel can control the lifecycle of the policy
if|if
condition|(
operator|!
name|camelContext
operator|.
name|hasService
argument_list|(
name|policy
argument_list|)
condition|)
block|{
try|try
block|{
name|camelContext
operator|.
name|addService
argument_list|(
name|policy
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
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|RoutePolicyAdvice
argument_list|(
name|routePolicyList
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// wrap in route inflight processor to track number of inflight exchanges for the route
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|RouteInflightRepositoryAdvice
argument_list|(
name|camelContext
operator|.
name|getInflightRepository
argument_list|()
argument_list|,
name|routeId
argument_list|)
argument_list|)
expr_stmt|;
comment|// wrap in JMX instrumentation processor that is used for performance stats
if|if
condition|(
name|managementInterceptStrategy
operator|!=
literal|null
condition|)
block|{
name|internal
operator|.
name|addAdvice
argument_list|(
name|CamelInternalProcessor
operator|.
name|wrap
argument_list|(
name|managementInterceptStrategy
operator|.
name|createProcessor
argument_list|(
literal|"route"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// wrap in route lifecycle
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|RouteLifecycleAdvice
argument_list|()
argument_list|)
expr_stmt|;
comment|// wrap in REST binding
name|advices
operator|.
name|forEach
argument_list|(
name|internal
operator|::
name|addAdvice
argument_list|)
expr_stmt|;
comment|// and create the route that wraps the UoW
name|Route
name|edcr
init|=
operator|new
name|EventDrivenConsumerRoute
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|,
name|internal
argument_list|)
decl_stmt|;
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|properties
argument_list|)
expr_stmt|;
comment|// after the route is created then set the route on the policy processor so we get hold of it
name|CamelInternalProcessor
operator|.
name|RoutePolicyAdvice
name|task
init|=
name|internal
operator|.
name|getAdvice
argument_list|(
name|CamelInternalProcessor
operator|.
name|RoutePolicyAdvice
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|task
operator|!=
literal|null
condition|)
block|{
name|task
operator|.
name|setRoute
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
block|}
name|CamelInternalProcessor
operator|.
name|RouteLifecycleAdvice
name|task2
init|=
name|internal
operator|.
name|getAdvice
argument_list|(
name|CamelInternalProcessor
operator|.
name|RouteLifecycleAdvice
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|task2
operator|!=
literal|null
condition|)
block|{
name|task2
operator|.
name|setRoute
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
block|}
comment|// invoke init on route policy
if|if
condition|(
name|routePolicyList
operator|!=
literal|null
operator|&&
operator|!
name|routePolicyList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|policy
range|:
name|routePolicyList
control|)
block|{
name|policy
operator|.
name|onInit
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
block|}
block|}
name|runtimeRoute
operator|=
name|edcr
expr_stmt|;
block|}
return|return
name|runtimeRoute
return|;
block|}
DECL|method|addEventDrivenProcessor (Processor processor)
specifier|public
name|void
name|addEventDrivenProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|eventDrivenProcessors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getInterceptStrategies ()
specifier|public
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
block|{
return|return
name|interceptStrategies
return|;
block|}
DECL|method|setInterceptStrategies (List<InterceptStrategy> interceptStrategies)
specifier|public
name|void
name|setInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
parameter_list|)
block|{
name|this
operator|.
name|interceptStrategies
operator|=
name|interceptStrategies
expr_stmt|;
block|}
DECL|method|addInterceptStrategy (InterceptStrategy interceptStrategy)
specifier|public
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
block|{
name|getInterceptStrategies
argument_list|()
operator|.
name|add
argument_list|(
name|interceptStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|setManagementInterceptStrategy (ManagementInterceptStrategy interceptStrategy)
specifier|public
name|void
name|setManagementInterceptStrategy
parameter_list|(
name|ManagementInterceptStrategy
name|interceptStrategy
parameter_list|)
block|{
name|this
operator|.
name|managementInterceptStrategy
operator|=
name|interceptStrategy
expr_stmt|;
block|}
DECL|method|getManagementInterceptStrategy ()
specifier|public
name|ManagementInterceptStrategy
name|getManagementInterceptStrategy
parameter_list|()
block|{
return|return
name|managementInterceptStrategy
return|;
block|}
DECL|method|isRouteAdded ()
specifier|public
name|boolean
name|isRouteAdded
parameter_list|()
block|{
return|return
name|routeAdded
return|;
block|}
DECL|method|setIsRouteAdded (boolean routeAdded)
specifier|public
name|void
name|setIsRouteAdded
parameter_list|(
name|boolean
name|routeAdded
parameter_list|)
block|{
name|this
operator|.
name|routeAdded
operator|=
name|routeAdded
expr_stmt|;
block|}
DECL|method|setTracing (Boolean tracing)
specifier|public
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
block|{
name|this
operator|.
name|trace
operator|=
name|tracing
expr_stmt|;
block|}
DECL|method|isTracing ()
specifier|public
name|Boolean
name|isTracing
parameter_list|()
block|{
if|if
condition|(
name|trace
operator|!=
literal|null
condition|)
block|{
return|return
name|trace
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isTracing
argument_list|()
return|;
block|}
block|}
DECL|method|setMessageHistory (Boolean messageHistory)
specifier|public
name|void
name|setMessageHistory
parameter_list|(
name|Boolean
name|messageHistory
parameter_list|)
block|{
name|this
operator|.
name|messageHistory
operator|=
name|messageHistory
expr_stmt|;
block|}
DECL|method|isMessageHistory ()
specifier|public
name|Boolean
name|isMessageHistory
parameter_list|()
block|{
if|if
condition|(
name|messageHistory
operator|!=
literal|null
condition|)
block|{
return|return
name|messageHistory
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isMessageHistory
argument_list|()
return|;
block|}
block|}
DECL|method|setLogMask (Boolean logMask)
specifier|public
name|void
name|setLogMask
parameter_list|(
name|Boolean
name|logMask
parameter_list|)
block|{
name|this
operator|.
name|logMask
operator|=
name|logMask
expr_stmt|;
block|}
DECL|method|isLogMask ()
specifier|public
name|Boolean
name|isLogMask
parameter_list|()
block|{
if|if
condition|(
name|logMask
operator|!=
literal|null
condition|)
block|{
return|return
name|logMask
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isLogMask
argument_list|()
return|;
block|}
block|}
DECL|method|setLogExhaustedMessageBody (Boolean logExhaustedMessageBody)
specifier|public
name|void
name|setLogExhaustedMessageBody
parameter_list|(
name|Boolean
name|logExhaustedMessageBody
parameter_list|)
block|{
name|this
operator|.
name|logExhaustedMessageBody
operator|=
name|logExhaustedMessageBody
expr_stmt|;
block|}
DECL|method|isLogExhaustedMessageBody ()
specifier|public
name|Boolean
name|isLogExhaustedMessageBody
parameter_list|()
block|{
if|if
condition|(
name|logExhaustedMessageBody
operator|!=
literal|null
condition|)
block|{
return|return
name|logExhaustedMessageBody
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isLogExhaustedMessageBody
argument_list|()
return|;
block|}
block|}
DECL|method|setStreamCaching (Boolean cache)
specifier|public
name|void
name|setStreamCaching
parameter_list|(
name|Boolean
name|cache
parameter_list|)
block|{
name|this
operator|.
name|streamCache
operator|=
name|cache
expr_stmt|;
block|}
DECL|method|isStreamCaching ()
specifier|public
name|Boolean
name|isStreamCaching
parameter_list|()
block|{
if|if
condition|(
name|streamCache
operator|!=
literal|null
condition|)
block|{
return|return
name|streamCache
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isStreamCaching
argument_list|()
return|;
block|}
block|}
DECL|method|setHandleFault (Boolean handleFault)
specifier|public
name|void
name|setHandleFault
parameter_list|(
name|Boolean
name|handleFault
parameter_list|)
block|{
name|this
operator|.
name|handleFault
operator|=
name|handleFault
expr_stmt|;
block|}
DECL|method|isHandleFault ()
specifier|public
name|Boolean
name|isHandleFault
parameter_list|()
block|{
if|if
condition|(
name|handleFault
operator|!=
literal|null
condition|)
block|{
return|return
name|handleFault
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|isHandleFault
argument_list|()
return|;
block|}
block|}
DECL|method|setDelayer (Long delay)
specifier|public
name|void
name|setDelayer
parameter_list|(
name|Long
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
expr_stmt|;
block|}
DECL|method|getDelayer ()
specifier|public
name|Long
name|getDelayer
parameter_list|()
block|{
if|if
condition|(
name|delay
operator|!=
literal|null
condition|)
block|{
return|return
name|delay
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|getDelayer
argument_list|()
return|;
block|}
block|}
DECL|method|setAutoStartup (Boolean autoStartup)
specifier|public
name|void
name|setAutoStartup
parameter_list|(
name|Boolean
name|autoStartup
parameter_list|)
block|{
name|this
operator|.
name|autoStartup
operator|=
name|autoStartup
expr_stmt|;
block|}
DECL|method|isAutoStartup ()
specifier|public
name|Boolean
name|isAutoStartup
parameter_list|()
block|{
if|if
condition|(
name|autoStartup
operator|!=
literal|null
condition|)
block|{
return|return
name|autoStartup
return|;
block|}
comment|// default to true
return|return
literal|true
return|;
block|}
DECL|method|setStartupOrder (Integer startupOrder)
specifier|public
name|void
name|setStartupOrder
parameter_list|(
name|Integer
name|startupOrder
parameter_list|)
block|{
name|this
operator|.
name|startupOrder
operator|=
name|startupOrder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getStartupOrder ()
specifier|public
name|Integer
name|getStartupOrder
parameter_list|()
block|{
return|return
name|startupOrder
return|;
block|}
DECL|method|setErrorHandlerFactory (ErrorHandlerFactory errorHandlerFactory)
specifier|public
name|void
name|setErrorHandlerFactory
parameter_list|(
name|ErrorHandlerFactory
name|errorHandlerFactory
parameter_list|)
block|{
name|this
operator|.
name|errorHandlerFactory
operator|=
name|errorHandlerFactory
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getErrorHandlerFactory ()
specifier|public
name|ErrorHandlerFactory
name|getErrorHandlerFactory
parameter_list|()
block|{
return|return
name|errorHandlerFactory
return|;
block|}
DECL|method|setShutdownRoute (ShutdownRoute shutdownRoute)
specifier|public
name|void
name|setShutdownRoute
parameter_list|(
name|ShutdownRoute
name|shutdownRoute
parameter_list|)
block|{
name|this
operator|.
name|shutdownRoute
operator|=
name|shutdownRoute
expr_stmt|;
block|}
DECL|method|setAllowUseOriginalMessage (Boolean allowUseOriginalMessage)
specifier|public
name|void
name|setAllowUseOriginalMessage
parameter_list|(
name|Boolean
name|allowUseOriginalMessage
parameter_list|)
block|{
comment|// can only be configured on CamelContext
name|getCamelContext
argument_list|()
operator|.
name|setAllowUseOriginalMessage
argument_list|(
name|allowUseOriginalMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|isAllowUseOriginalMessage ()
specifier|public
name|Boolean
name|isAllowUseOriginalMessage
parameter_list|()
block|{
comment|// can only be configured on CamelContext
return|return
name|getCamelContext
argument_list|()
operator|.
name|isAllowUseOriginalMessage
argument_list|()
return|;
block|}
DECL|method|getShutdownRoute ()
specifier|public
name|ShutdownRoute
name|getShutdownRoute
parameter_list|()
block|{
if|if
condition|(
name|shutdownRoute
operator|!=
literal|null
condition|)
block|{
return|return
name|shutdownRoute
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|getShutdownRoute
argument_list|()
return|;
block|}
block|}
DECL|method|setShutdownRunningTask (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|void
name|setShutdownRunningTask
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
name|this
operator|.
name|shutdownRunningTask
operator|=
name|shutdownRunningTask
expr_stmt|;
block|}
DECL|method|getShutdownRunningTask ()
specifier|public
name|ShutdownRunningTask
name|getShutdownRunningTask
parameter_list|()
block|{
if|if
condition|(
name|shutdownRunningTask
operator|!=
literal|null
condition|)
block|{
return|return
name|shutdownRunningTask
return|;
block|}
else|else
block|{
comment|// fallback to the option from camel context
return|return
name|getCamelContext
argument_list|()
operator|.
name|getShutdownRunningTask
argument_list|()
return|;
block|}
block|}
DECL|method|setRoutePolicyList (List<RoutePolicy> routePolicyList)
specifier|public
name|void
name|setRoutePolicyList
parameter_list|(
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicyList
parameter_list|)
block|{
name|this
operator|.
name|routePolicyList
operator|=
name|routePolicyList
expr_stmt|;
block|}
DECL|method|getRoutePolicyList ()
specifier|public
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|getRoutePolicyList
parameter_list|()
block|{
return|return
name|routePolicyList
return|;
block|}
annotation|@
name|Override
DECL|method|getLastError ()
specifier|public
name|RouteError
name|getLastError
parameter_list|()
block|{
return|return
name|routeError
return|;
block|}
annotation|@
name|Override
DECL|method|setLastError (RouteError routeError)
specifier|public
name|void
name|setLastError
parameter_list|(
name|RouteError
name|routeError
parameter_list|)
block|{
name|this
operator|.
name|routeError
operator|=
name|routeError
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRouteController ()
specifier|public
name|RouteController
name|getRouteController
parameter_list|()
block|{
return|return
name|routeController
return|;
block|}
annotation|@
name|Override
DECL|method|setRouteController (RouteController routeController)
specifier|public
name|void
name|setRouteController
parameter_list|(
name|RouteController
name|routeController
parameter_list|)
block|{
name|this
operator|.
name|routeController
operator|=
name|routeController
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOnCompletion (String onCompletionId)
specifier|public
name|Processor
name|getOnCompletion
parameter_list|(
name|String
name|onCompletionId
parameter_list|)
block|{
return|return
name|onCompletions
operator|.
name|get
argument_list|(
name|onCompletionId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setOnCompletion (String onCompletionId, Processor processor)
specifier|public
name|void
name|setOnCompletion
parameter_list|(
name|String
name|onCompletionId
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|onCompletions
operator|.
name|put
argument_list|(
name|onCompletionId
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOnException (String onExceptionId)
specifier|public
name|Processor
name|getOnException
parameter_list|(
name|String
name|onExceptionId
parameter_list|)
block|{
return|return
name|onExceptions
operator|.
name|get
argument_list|(
name|onExceptionId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setOnException (String onExceptionId, Processor processor)
specifier|public
name|void
name|setOnException
parameter_list|(
name|String
name|onExceptionId
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|onExceptions
operator|.
name|put
argument_list|(
name|onExceptionId
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|addAdvice (CamelInternalProcessorAdvice<?> advice)
specifier|public
name|void
name|addAdvice
parameter_list|(
name|CamelInternalProcessorAdvice
argument_list|<
name|?
argument_list|>
name|advice
parameter_list|)
block|{
name|advices
operator|.
name|add
argument_list|(
name|advice
argument_list|)
expr_stmt|;
block|}
DECL|method|addProperty (String key, Object value)
specifier|public
name|void
name|addProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|properties
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
DECL|field|errorHandlers
specifier|private
name|Map
argument_list|<
name|ErrorHandlerFactory
argument_list|,
name|Set
argument_list|<
name|NamedNode
argument_list|>
argument_list|>
name|errorHandlers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|addErrorHandler (ErrorHandlerFactory factory, NamedNode onException)
specifier|public
name|void
name|addErrorHandler
parameter_list|(
name|ErrorHandlerFactory
name|factory
parameter_list|,
name|NamedNode
name|onException
parameter_list|)
block|{
name|getErrorHandlers
argument_list|(
name|factory
argument_list|)
operator|.
name|add
argument_list|(
name|onException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getErrorHandlers (ErrorHandlerFactory factory)
specifier|public
name|Set
argument_list|<
name|NamedNode
argument_list|>
name|getErrorHandlers
parameter_list|(
name|ErrorHandlerFactory
name|factory
parameter_list|)
block|{
return|return
name|errorHandlers
operator|.
name|computeIfAbsent
argument_list|(
name|factory
argument_list|,
name|f
lambda|->
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addErrorHandlerFactoryReference (ErrorHandlerFactory source, ErrorHandlerFactory target)
specifier|public
name|void
name|addErrorHandlerFactoryReference
parameter_list|(
name|ErrorHandlerFactory
name|source
parameter_list|,
name|ErrorHandlerFactory
name|target
parameter_list|)
block|{
name|Set
argument_list|<
name|NamedNode
argument_list|>
name|list
init|=
name|getErrorHandlers
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|NamedNode
argument_list|>
name|previous
init|=
name|errorHandlers
operator|.
name|put
argument_list|(
name|target
argument_list|,
name|list
argument_list|)
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
operator|&&
name|previous
operator|!=
name|list
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"multiple references with different handlers"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

