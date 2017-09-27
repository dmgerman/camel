begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ha
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

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
name|Collections
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
name|Objects
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|CopyOnWriteArraySet
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|ha
operator|.
name|CamelClusterService
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
name|DefaultRouteController
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
name|spi
operator|.
name|RoutePolicyFactory
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

begin_class
DECL|class|ClusteredRouteController
specifier|public
class|class
name|ClusteredRouteController
extends|extends
name|DefaultRouteController
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ClusteredRouteController
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|routes
decl_stmt|;
DECL|field|configurations
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|ClusteredRouteConfiguration
argument_list|>
name|configurations
decl_stmt|;
DECL|field|filters
specifier|private
specifier|final
name|List
argument_list|<
name|ClusteredRouteFilter
argument_list|>
name|filters
decl_stmt|;
DECL|field|policyFactory
specifier|private
specifier|final
name|PolicyFactory
name|policyFactory
decl_stmt|;
DECL|field|defaultConfiguration
specifier|private
specifier|final
name|ClusteredRouteConfiguration
name|defaultConfiguration
decl_stmt|;
DECL|field|clusterService
specifier|private
name|CamelClusterService
name|clusterService
decl_stmt|;
DECL|field|clusterServiceSelector
specifier|private
name|CamelClusterService
operator|.
name|Selector
name|clusterServiceSelector
decl_stmt|;
DECL|method|ClusteredRouteController ()
specifier|public
name|ClusteredRouteController
parameter_list|()
block|{
name|this
operator|.
name|routes
operator|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|configurations
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|filters
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
expr_stmt|;
name|this
operator|.
name|policyFactory
operator|=
operator|new
name|PolicyFactory
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultConfiguration
operator|=
operator|new
name|ClusteredRouteConfiguration
argument_list|()
expr_stmt|;
name|this
operator|.
name|defaultConfiguration
operator|.
name|setInitialDelay
argument_list|(
name|Duration
operator|.
name|ofMillis
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *******************************
comment|// Properties.
comment|// *******************************
comment|/**      * Add a filter used to to filter cluster aware routes.      */
DECL|method|addFilter (ClusteredRouteFilter filter)
specifier|public
name|void
name|addFilter
parameter_list|(
name|ClusteredRouteFilter
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the filters used to filter cluster aware routes.      */
DECL|method|setFilters (Collection<ClusteredRouteFilter> filters)
specifier|public
name|void
name|setFilters
parameter_list|(
name|Collection
argument_list|<
name|ClusteredRouteFilter
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|filters
operator|.
name|addAll
argument_list|(
name|filters
argument_list|)
expr_stmt|;
block|}
DECL|method|getFilters ()
specifier|public
name|Collection
argument_list|<
name|ClusteredRouteFilter
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|filters
argument_list|)
return|;
block|}
comment|/**      * Add a configuration for the given route.      */
DECL|method|addRouteConfiguration (String routeId, ClusteredRouteConfiguration configuration)
specifier|public
name|void
name|addRouteConfiguration
parameter_list|(
name|String
name|routeId
parameter_list|,
name|ClusteredRouteConfiguration
name|configuration
parameter_list|)
block|{
name|configurations
operator|.
name|put
argument_list|(
name|routeId
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the configurations for the routes.      */
DECL|method|setRoutesConfiguration (Map<String, ClusteredRouteConfiguration> configurations)
specifier|public
name|void
name|setRoutesConfiguration
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|ClusteredRouteConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
name|this
operator|.
name|configurations
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|configurations
operator|.
name|putAll
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
DECL|method|getRoutesConfiguration ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ClusteredRouteConfiguration
argument_list|>
name|getRoutesConfiguration
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|this
operator|.
name|configurations
argument_list|)
return|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|Duration
name|getInitialDelay
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultConfiguration
operator|.
name|getInitialDelay
argument_list|()
return|;
block|}
comment|/**      * Set the amount of time the route controller should wait before to start      * the routes after the camel context is started.      *      * @param initialDelay the initial delay.      */
DECL|method|setInitialDelay (Duration initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|Duration
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setInitialDelay
argument_list|(
name|initialDelay
argument_list|)
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultConfiguration
operator|.
name|getNamespace
argument_list|()
return|;
block|}
comment|/**      * Set the default namespace.      */
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
DECL|method|getClusterService ()
specifier|public
name|CamelClusterService
name|getClusterService
parameter_list|()
block|{
return|return
name|clusterService
return|;
block|}
comment|/**      * Set the cluster service to use.      */
DECL|method|setClusterService (CamelClusterService clusterService)
specifier|public
name|void
name|setClusterService
parameter_list|(
name|CamelClusterService
name|clusterService
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clusterService
argument_list|,
literal|"CamelClusterService"
argument_list|)
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
name|clusterService
expr_stmt|;
block|}
DECL|method|getClusterServiceSelector ()
specifier|public
name|CamelClusterService
operator|.
name|Selector
name|getClusterServiceSelector
parameter_list|()
block|{
return|return
name|clusterServiceSelector
return|;
block|}
comment|/**      * Set the selector strategy to look-up a {@link CamelClusterService}      */
DECL|method|setClusterServiceSelector (CamelClusterService.Selector clusterServiceSelector)
specifier|public
name|void
name|setClusterServiceSelector
parameter_list|(
name|CamelClusterService
operator|.
name|Selector
name|clusterServiceSelector
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clusterService
argument_list|,
literal|"CamelClusterService.Selector"
argument_list|)
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
name|clusterServiceSelector
expr_stmt|;
block|}
comment|// *******************************
comment|//
comment|// *******************************
annotation|@
name|Override
DECL|method|getControlledRoutes ()
specifier|public
name|Collection
argument_list|<
name|Route
argument_list|>
name|getControlledRoutes
parameter_list|()
block|{
return|return
name|this
operator|.
name|routes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|getCamelContext
argument_list|()
operator|::
name|getRoute
argument_list|)
operator|.
name|filter
argument_list|(
name|Objects
operator|::
name|nonNull
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|getCamelContext
argument_list|()
decl_stmt|;
comment|// Parameters validation
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|defaultConfiguration
operator|.
name|getNamespace
argument_list|()
argument_list|,
literal|"Namespace"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|defaultConfiguration
operator|.
name|getInitialDelay
argument_list|()
argument_list|,
literal|"initialDelay"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|context
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|clusterService
operator|==
literal|null
condition|)
block|{
comment|// Finally try to grab it from the camel context.
name|clusterService
operator|=
name|ClusterServiceHelper
operator|.
name|mandatoryLookupService
argument_list|(
name|context
argument_list|,
name|clusterServiceSelector
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Using ClusterService instance {} (id={}, type={})"
argument_list|,
name|clusterService
argument_list|,
name|clusterService
operator|.
name|getId
argument_list|()
argument_list|,
name|clusterService
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|clusterService
argument_list|)
condition|)
block|{
comment|// Start the cluster service if not yet started.
name|clusterService
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ServiceHelper
operator|.
name|isStarted
argument_list|(
name|clusterService
argument_list|)
condition|)
block|{
comment|// Stop the cluster service.
name|clusterService
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
operator|!
name|camelContext
operator|.
name|getRoutePolicyFactories
argument_list|()
operator|.
name|contains
argument_list|(
name|this
operator|.
name|policyFactory
argument_list|)
condition|)
block|{
name|camelContext
operator|.
name|addRoutePolicyFactory
argument_list|(
name|this
operator|.
name|policyFactory
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// *******************************
comment|// Route operations are disabled
comment|// *******************************
annotation|@
name|Override
DECL|method|startRoute (String routeId)
specifier|public
name|void
name|startRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|startRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
specifier|public
name|boolean
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|boolean
name|abortAfterTimeout
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
return|return
name|super
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|,
name|abortAfterTimeout
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|suspendRoute (String routeId)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|suspendRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|suspendRoute (String routeId, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|suspendRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|resumeRoute (String routeId)
specifier|public
name|void
name|resumeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
block|{
name|failIfClustered
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
comment|// Delegate to default impl.
name|super
operator|.
name|resumeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
comment|// *******************************
comment|// Helpers
comment|// *******************************
DECL|method|failIfClustered (String routeId)
specifier|private
name|void
name|failIfClustered
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{
comment|// Can't perform action on routes managed by this controller as they
comment|// are clustered and they may be part of the same view.
if|if
condition|(
name|routes
operator|.
name|contains
argument_list|(
name|routeId
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Operation not supported as route "
operator|+
name|routeId
operator|+
literal|" is clustered"
argument_list|)
throw|;
block|}
block|}
comment|// *******************************
comment|// Factories
comment|// *******************************
DECL|class|PolicyFactory
specifier|private
specifier|final
class|class
name|PolicyFactory
implements|implements
name|RoutePolicyFactory
block|{
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, RouteDefinition route)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
comment|// All the filter have to be match to include the route in the
comment|// clustering set-up
if|if
condition|(
name|filters
operator|.
name|stream
argument_list|()
operator|.
name|allMatch
argument_list|(
name|filter
lambda|->
name|filter
operator|.
name|test
argument_list|(
name|camelContext
argument_list|,
name|routeId
argument_list|,
name|route
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|route
operator|.
name|getRoutePolicies
argument_list|()
argument_list|)
condition|)
block|{
comment|// Check if the route is already configured with a clustered
comment|// route policy, in that case exclude it.
if|if
condition|(
name|route
operator|.
name|getRoutePolicies
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|ClusteredRoutePolicy
operator|.
name|class
operator|::
name|isInstance
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Route '{}' has a ClusteredRoutePolicy already set-up"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
try|try
block|{
specifier|final
name|ClusteredRouteConfiguration
name|configuration
init|=
name|configurations
operator|.
name|getOrDefault
argument_list|(
name|routeId
argument_list|,
name|defaultConfiguration
argument_list|)
decl_stmt|;
specifier|final
name|String
name|namespace
init|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|configuration
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|defaultConfiguration
operator|::
name|getNamespace
argument_list|)
decl_stmt|;
specifier|final
name|Duration
name|initialDelay
init|=
name|ObjectHelper
operator|.
name|supplyIfEmpty
argument_list|(
name|configuration
operator|.
name|getInitialDelay
argument_list|()
argument_list|,
name|defaultConfiguration
operator|::
name|getInitialDelay
argument_list|)
decl_stmt|;
name|ClusteredRoutePolicy
name|policy
init|=
name|ClusteredRoutePolicy
operator|.
name|forView
argument_list|(
name|clusterService
operator|.
name|getView
argument_list|(
name|namespace
argument_list|)
argument_list|)
decl_stmt|;
name|policy
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
name|policy
operator|.
name|setInitialDelay
argument_list|(
name|initialDelay
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Attaching route '{}' to namespace '{}'"
argument_list|,
name|routeId
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
name|routes
operator|.
name|add
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
return|return
name|policy
return|;
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
return|return
literal|null
return|;
block|}
block|}
end_class

unit|}
end_unit

