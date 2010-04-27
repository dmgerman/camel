begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Collection
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|management
operator|.
name|InstrumentationProcessor
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
name|model
operator|.
name|FromDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|RouteDefinition
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
name|processor
operator|.
name|RoutePolicyProcessor
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
name|UnitOfWorkProcessor
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The context used to activate new routing rules  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultRouteContext
specifier|public
class|class
name|DefaultRouteContext
implements|implements
name|RouteContext
block|{
DECL|field|nodeIndex
specifier|private
specifier|final
name|Map
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicInteger
argument_list|>
name|nodeIndex
init|=
operator|new
name|HashMap
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|,
name|AtomicInteger
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|route
specifier|private
specifier|final
name|RouteDefinition
name|route
decl_stmt|;
DECL|field|from
specifier|private
name|FromDefinition
name|from
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
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
argument_list|<
name|Processor
argument_list|>
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
argument_list|<
name|InterceptStrategy
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|managedInterceptStrategy
specifier|private
name|InterceptStrategy
name|managedInterceptStrategy
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
DECL|field|stramCache
specifier|private
name|Boolean
name|stramCache
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
DECL|field|routePolicy
specifier|private
name|RoutePolicy
name|routePolicy
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
DECL|method|DefaultRouteContext (CamelContext camelContext, RouteDefinition route, FromDefinition from, Collection<Route> routes)
specifier|public
name|DefaultRouteContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RouteDefinition
name|route
parameter_list|,
name|FromDefinition
name|from
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
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
name|from
operator|=
name|from
expr_stmt|;
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
comment|/**      * Only used for lazy construction from inside ExpressionType      */
DECL|method|DefaultRouteContext (CamelContext camelContext)
specifier|public
name|DefaultRouteContext
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
name|this
operator|.
name|routes
operator|=
operator|new
name|ArrayList
argument_list|<
name|Route
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|route
operator|=
operator|new
name|RouteDefinition
argument_list|(
literal|"temporary"
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
name|endpoint
operator|=
name|from
operator|.
name|resolveEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getFrom ()
specifier|public
name|FromDefinition
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|getRoute ()
specifier|public
name|RouteDefinition
name|getRoute
parameter_list|()
block|{
return|return
name|route
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
DECL|method|createProcessor (ProcessorDefinition<?> node)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|node
operator|.
name|createOutputsProcessor
argument_list|(
name|this
argument_list|)
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
name|route
operator|.
name|resolveEndpoint
argument_list|(
name|getCamelContext
argument_list|()
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
name|lookup
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
name|lookupByType
argument_list|(
name|type
argument_list|)
return|;
block|}
DECL|method|commit ()
specifier|public
name|void
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
name|processor
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
name|Processor
name|unitOfWorkProcessor
init|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|Processor
name|target
init|=
name|unitOfWorkProcessor
decl_stmt|;
comment|// and then optionally add route policy processor if a custom policy is set
name|RoutePolicyProcessor
name|routePolicyProcessor
init|=
literal|null
decl_stmt|;
name|RoutePolicy
name|policy
init|=
name|getRoutePolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|routePolicyProcessor
operator|=
operator|new
name|RoutePolicyProcessor
argument_list|(
name|unitOfWorkProcessor
argument_list|,
name|policy
argument_list|)
expr_stmt|;
comment|// add it as service if we have not already done that (eg possible if two routes have the same service)
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|target
operator|=
name|routePolicyProcessor
expr_stmt|;
block|}
comment|// and wrap it by a instrumentation processor that is to be used for performance stats
comment|// for this particular route
name|InstrumentationProcessor
name|wrapper
init|=
operator|new
name|InstrumentationProcessor
argument_list|()
decl_stmt|;
name|wrapper
operator|.
name|setType
argument_list|(
literal|"route"
argument_list|)
expr_stmt|;
name|wrapper
operator|.
name|setProcessor
argument_list|(
name|target
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
name|wrapper
argument_list|)
decl_stmt|;
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|ID_PROPERTY
argument_list|,
name|route
operator|.
name|idOrCreate
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|PARENT_PROPERTY
argument_list|,
name|Integer
operator|.
name|toHexString
argument_list|(
name|route
operator|.
name|hashCode
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|getGroup
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|edcr
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|Route
operator|.
name|GROUP_PROPERTY
argument_list|,
name|route
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// after the route is created then set the route on the policy processor so we get hold of it
if|if
condition|(
name|routePolicyProcessor
operator|!=
literal|null
condition|)
block|{
name|routePolicyProcessor
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
name|policy
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|onInit
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
block|}
name|routes
operator|.
name|add
argument_list|(
name|edcr
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setManagedInterceptStrategy (InterceptStrategy interceptStrategy)
specifier|public
name|void
name|setManagedInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
block|{
name|this
operator|.
name|managedInterceptStrategy
operator|=
name|interceptStrategy
expr_stmt|;
block|}
DECL|method|getManagedInterceptStrategy ()
specifier|public
name|InterceptStrategy
name|getManagedInterceptStrategy
parameter_list|()
block|{
return|return
name|managedInterceptStrategy
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
name|stramCache
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
name|stramCache
operator|!=
literal|null
condition|)
block|{
return|return
name|stramCache
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
DECL|method|getRoutePolicy ()
specifier|public
name|RoutePolicy
name|getRoutePolicy
parameter_list|()
block|{
return|return
name|routePolicy
return|;
block|}
DECL|method|setRoutePolicy (RoutePolicy routePolicy)
specifier|public
name|void
name|setRoutePolicy
parameter_list|(
name|RoutePolicy
name|routePolicy
parameter_list|)
block|{
name|this
operator|.
name|routePolicy
operator|=
name|routePolicy
expr_stmt|;
block|}
DECL|method|getAndIncrement (ProcessorDefinition<?> node)
specifier|public
name|int
name|getAndIncrement
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
name|AtomicInteger
name|count
init|=
name|nodeIndex
operator|.
name|get
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|count
operator|=
operator|new
name|AtomicInteger
argument_list|()
expr_stmt|;
name|nodeIndex
operator|.
name|put
argument_list|(
name|node
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
return|return
name|count
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
block|}
end_class

end_unit

