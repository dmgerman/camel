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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|Channel
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
name|RouteAware
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
name|model
operator|.
name|OnCompletionDefinition
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
name|OnExceptionDefinition
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
name|EndpointAware
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
name|ErrorHandler
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
name|LifecycleStrategy
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
name|support
operator|.
name|ChildServiceSupport
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
name|EventHelper
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
comment|/**  * Represents the runtime objects for a given {@link RouteDefinition} so that it can be stopped independently  * of other routes  *  * @version   */
end_comment

begin_class
DECL|class|RouteService
specifier|public
class|class
name|RouteService
extends|extends
name|ChildServiceSupport
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
name|RouteService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|DefaultCamelContext
name|camelContext
decl_stmt|;
DECL|field|routeDefinition
specifier|private
specifier|final
name|RouteDefinition
name|routeDefinition
decl_stmt|;
DECL|field|routeContexts
specifier|private
specifier|final
name|List
argument_list|<
name|RouteContext
argument_list|>
name|routeContexts
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
name|List
argument_list|<
name|Route
argument_list|>
name|routes
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|removingRoutes
specifier|private
name|boolean
name|removingRoutes
decl_stmt|;
DECL|field|inputs
specifier|private
specifier|final
name|Map
argument_list|<
name|Route
argument_list|,
name|Consumer
argument_list|>
name|inputs
init|=
operator|new
name|HashMap
argument_list|<
name|Route
argument_list|,
name|Consumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|warmUpDone
specifier|private
specifier|final
name|AtomicBoolean
name|warmUpDone
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|endpointDone
specifier|private
specifier|final
name|AtomicBoolean
name|endpointDone
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|method|RouteService (DefaultCamelContext camelContext, RouteDefinition routeDefinition, List<RouteContext> routeContexts, List<Route> routes)
specifier|public
name|RouteService
parameter_list|(
name|DefaultCamelContext
name|camelContext
parameter_list|,
name|RouteDefinition
name|routeDefinition
parameter_list|,
name|List
argument_list|<
name|RouteContext
argument_list|>
name|routeContexts
parameter_list|,
name|List
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
name|routeDefinition
operator|=
name|routeDefinition
expr_stmt|;
name|this
operator|.
name|routeContexts
operator|=
name|routeContexts
expr_stmt|;
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|routeDefinition
operator|.
name|idOrCreate
argument_list|(
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
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
DECL|method|getRouteContexts ()
specifier|public
name|List
argument_list|<
name|RouteContext
argument_list|>
name|getRouteContexts
parameter_list|()
block|{
return|return
name|routeContexts
return|;
block|}
DECL|method|getRouteDefinition ()
specifier|public
name|RouteDefinition
name|getRouteDefinition
parameter_list|()
block|{
return|return
name|routeDefinition
return|;
block|}
DECL|method|getRoutes ()
specifier|public
name|Collection
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
comment|/**      * Gather all the endpoints this route service uses      *<p/>      * This implementation finds the endpoints by searching all the child services      * for {@link org.apache.camel.EndpointAware} processors which uses an endpoint.      */
DECL|method|gatherEndpoints ()
specifier|public
name|Set
argument_list|<
name|Endpoint
argument_list|>
name|gatherEndpoints
parameter_list|()
block|{
name|Set
argument_list|<
name|Endpoint
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Endpoint
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|Set
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|gatherChildServices
argument_list|(
name|route
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|EndpointAware
condition|)
block|{
name|Endpoint
name|endpoint
init|=
operator|(
operator|(
name|EndpointAware
operator|)
name|service
operator|)
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Gets the inputs to the routes.      *      * @return list of {@link Consumer} as inputs for the routes      */
DECL|method|getInputs ()
specifier|public
name|Map
argument_list|<
name|Route
argument_list|,
name|Consumer
argument_list|>
name|getInputs
parameter_list|()
block|{
return|return
name|inputs
return|;
block|}
DECL|method|isRemovingRoutes ()
specifier|public
name|boolean
name|isRemovingRoutes
parameter_list|()
block|{
return|return
name|removingRoutes
return|;
block|}
DECL|method|setRemovingRoutes (boolean removingRoutes)
specifier|public
name|void
name|setRemovingRoutes
parameter_list|(
name|boolean
name|removingRoutes
parameter_list|)
block|{
name|this
operator|.
name|removingRoutes
operator|=
name|removingRoutes
expr_stmt|;
block|}
DECL|method|warmUp ()
specifier|public
specifier|synchronized
name|void
name|warmUp
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|endpointDone
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// endpoints should only be started once as they can be reused on other routes
comment|// and whatnot, thus their lifecycle is to start once, and only to stop when Camel shutdown
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
comment|// ensure endpoint is started first (before the route services, such as the consumer)
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|warmUpDone
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
comment|// warm up the route first
name|route
operator|.
name|warmUp
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting services on route: {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|route
operator|.
name|getServices
argument_list|()
decl_stmt|;
comment|// callback that we are staring these services
name|route
operator|.
name|onStartingServices
argument_list|(
name|services
argument_list|)
expr_stmt|;
comment|// gather list of services to start as we need to start child services as well
name|Set
argument_list|<
name|Service
argument_list|>
name|list
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|ServiceHelper
operator|.
name|getChildServices
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// split into consumers and child services as we need to start the consumers
comment|// afterwards to avoid them being active while the others start
name|List
argument_list|<
name|Service
argument_list|>
name|childServices
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|list
control|)
block|{
comment|// inject the route
if|if
condition|(
name|service
operator|instanceof
name|RouteAware
condition|)
block|{
operator|(
operator|(
name|RouteAware
operator|)
name|service
operator|)
operator|.
name|setRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|service
operator|instanceof
name|Consumer
condition|)
block|{
name|inputs
operator|.
name|put
argument_list|(
name|route
argument_list|,
operator|(
name|Consumer
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|childServices
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
name|startChildService
argument_list|(
name|route
argument_list|,
name|childServices
argument_list|)
expr_stmt|;
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteAdded
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
comment|// ensure lifecycle strategy is invoked which among others enlist the route in JMX
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRoutesAdd
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
comment|// add routes to camel context
name|camelContext
operator|.
name|addRouteCollection
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure we are warmed up before starting the route
name|warmUp
argument_list|()
expr_stmt|;
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
comment|// start the route itself
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// invoke callbacks on route policy
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|routePolicy
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
control|)
block|{
name|routePolicy
operator|.
name|onStart
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteStarted
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// if we are stopping CamelContext then we are shutting down
name|boolean
name|isShutdownCamelContext
init|=
name|camelContext
operator|.
name|isStopping
argument_list|()
decl_stmt|;
if|if
condition|(
name|isShutdownCamelContext
operator|||
name|isRemovingRoutes
argument_list|()
condition|)
block|{
comment|// need to call onRoutesRemove when the CamelContext is shutting down or Route is shutdown
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRoutesRemove
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping services on route: {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// gather list of services to stop as we need to start child services as well
name|Set
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|gatherChildServices
argument_list|(
name|route
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// stop services
name|stopChildService
argument_list|(
name|route
argument_list|,
name|services
argument_list|,
name|isShutdownCamelContext
argument_list|)
expr_stmt|;
comment|// stop the route itself
if|if
condition|(
name|isShutdownCamelContext
condition|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
comment|// invoke callbacks on route policy
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|routePolicy
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
control|)
block|{
name|routePolicy
operator|.
name|onStop
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteStopped
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isRemovingRoutes
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|removeRouteCollection
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
comment|// need to warm up again
name|warmUpDone
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down services on route: {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// gather list of services to stop as we need to start child services as well
name|Set
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|gatherChildServices
argument_list|(
name|route
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// shutdown services
name|stopChildService
argument_list|(
name|route
argument_list|,
name|services
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// shutdown the route itself
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// endpoints should only be stopped when Camel is shutting down
comment|// see more details in the warmUp method
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke callbacks on route policy
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|routePolicy
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
control|)
block|{
name|routePolicy
operator|.
name|onRemove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteRemoved
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
comment|// need to call onRoutesRemove when the CamelContext is shutting down or Route is shutdown
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRoutesRemove
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
comment|// remove the routes from the inflight registry
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
name|camelContext
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|removeRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// remove the routes from the collections
name|camelContext
operator|.
name|removeRouteCollection
argument_list|(
name|routes
argument_list|)
expr_stmt|;
comment|// clear inputs on shutdown
name|inputs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|warmUpDone
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|endpointDone
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
comment|// suspend and resume logic is provided by DefaultCamelContext which leverages ShutdownStrategy
comment|// to safely suspend and resume
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|routePolicy
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
control|)
block|{
name|routePolicy
operator|.
name|onSuspend
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
comment|// suspend and resume logic is provided by DefaultCamelContext which leverages ShutdownStrategy
comment|// to safely suspend and resume
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RoutePolicy
name|routePolicy
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
control|)
block|{
name|routePolicy
operator|.
name|onResume
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|startChildService (Route route, List<Service> services)
specifier|protected
name|void
name|startChildService
parameter_list|(
name|Route
name|route
parameter_list|,
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting child service on route: {} -> {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|service
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onServiceAdd
argument_list|(
name|camelContext
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|addChildService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopChildService (Route route, Set<Service> services, boolean shutdown)
specifier|protected
name|void
name|stopChildService
parameter_list|(
name|Route
name|route
parameter_list|,
name|Set
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|,
name|boolean
name|shutdown
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"{} child service on route: {} -> {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|shutdown
condition|?
literal|"Shutting down"
else|:
literal|"Stopping"
block|,
name|route
operator|.
name|getId
argument_list|()
block|,
name|service
block|}
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|ErrorHandler
condition|)
block|{
comment|// special for error handlers
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onErrorHandlerRemove
argument_list|(
name|route
operator|.
name|getRouteContext
argument_list|()
argument_list|,
operator|(
name|Processor
operator|)
name|service
argument_list|,
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onServiceRemove
argument_list|(
name|camelContext
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|shutdown
condition|)
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
name|removeChildService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gather all child services      */
DECL|method|gatherChildServices (Route route, boolean includeErrorHandler)
specifier|private
name|Set
argument_list|<
name|Service
argument_list|>
name|gatherChildServices
parameter_list|(
name|Route
name|route
parameter_list|,
name|boolean
name|includeErrorHandler
parameter_list|)
block|{
comment|// gather list of services to stop as we need to start child services as well
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
name|services
operator|.
name|addAll
argument_list|(
name|route
operator|.
name|getServices
argument_list|()
argument_list|)
expr_stmt|;
comment|// also get route scoped services
name|doGetRouteScopedServices
argument_list|(
name|services
argument_list|,
name|route
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Service
argument_list|>
name|list
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|list
operator|.
name|addAll
argument_list|(
name|ServiceHelper
operator|.
name|getChildServices
argument_list|(
name|service
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|includeErrorHandler
condition|)
block|{
comment|// also get route scoped error handler (which must be done last)
name|doGetRouteScopedErrorHandler
argument_list|(
name|list
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|Service
argument_list|>
name|answer
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Gather the route scoped error handler from the given route      */
DECL|method|doGetRouteScopedErrorHandler (Set<Service> services, Route route)
specifier|private
name|void
name|doGetRouteScopedErrorHandler
parameter_list|(
name|Set
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
comment|// only include error handlers if they are route scoped
name|boolean
name|includeErrorHandler
init|=
operator|!
name|routeDefinition
operator|.
name|isContextScopedErrorHandler
argument_list|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Service
argument_list|>
name|extra
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeErrorHandler
condition|)
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|Channel
condition|)
block|{
name|Processor
name|eh
init|=
operator|(
operator|(
name|Channel
operator|)
name|service
operator|)
operator|.
name|getErrorHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|eh
operator|!=
literal|null
operator|&&
name|eh
operator|instanceof
name|Service
condition|)
block|{
name|extra
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|eh
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
operator|!
name|extra
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|services
operator|.
name|addAll
argument_list|(
name|extra
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gather all other kind of route scoped services from the given route, except error handler      */
DECL|method|doGetRouteScopedServices (List<Service> services, Route route)
specifier|private
name|void
name|doGetRouteScopedServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoute
argument_list|()
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
name|OnExceptionDefinition
name|onExceptionDefinition
init|=
operator|(
name|OnExceptionDefinition
operator|)
name|output
decl_stmt|;
if|if
condition|(
name|onExceptionDefinition
operator|.
name|isRouteScoped
argument_list|()
condition|)
block|{
name|Processor
name|errorHandler
init|=
name|onExceptionDefinition
operator|.
name|getErrorHandler
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|errorHandler
operator|!=
literal|null
operator|&&
name|errorHandler
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|errorHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|output
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|OnCompletionDefinition
name|onCompletionDefinition
init|=
operator|(
name|OnCompletionDefinition
operator|)
name|output
decl_stmt|;
if|if
condition|(
name|onCompletionDefinition
operator|.
name|isRouteScoped
argument_list|()
condition|)
block|{
name|Processor
name|onCompletionProcessor
init|=
name|onCompletionDefinition
operator|.
name|getOnCompletion
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|onCompletionProcessor
operator|!=
literal|null
operator|&&
name|onCompletionProcessor
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|onCompletionProcessor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

