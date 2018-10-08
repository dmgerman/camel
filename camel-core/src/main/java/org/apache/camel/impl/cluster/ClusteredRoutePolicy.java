begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cluster
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|cluster
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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|ScheduledExecutorService
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|StartupListener
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|cluster
operator|.
name|CamelClusterEventListener
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
name|cluster
operator|.
name|CamelClusterMember
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
name|cluster
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
name|cluster
operator|.
name|CamelClusterView
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
name|CamelEvent
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
name|CamelEvent
operator|.
name|CamelContextStartedEvent
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
name|EventNotifierSupport
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
name|RoutePolicySupport
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
name|cluster
operator|.
name|ClusterServiceHelper
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
name|cluster
operator|.
name|ClusterServiceSelectors
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
name|ReferenceCount
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Clustered Route policy using"
argument_list|)
DECL|class|ClusteredRoutePolicy
specifier|public
specifier|final
class|class
name|ClusteredRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|CamelContextAware
block|{
DECL|field|leader
specifier|private
specifier|final
name|AtomicBoolean
name|leader
decl_stmt|;
DECL|field|startedRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|startedRoutes
decl_stmt|;
DECL|field|stoppedRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|stoppedRoutes
decl_stmt|;
DECL|field|refCount
specifier|private
specifier|final
name|ReferenceCount
name|refCount
decl_stmt|;
DECL|field|leadershipEventListener
specifier|private
specifier|final
name|CamelClusterEventListener
operator|.
name|Leadership
name|leadershipEventListener
decl_stmt|;
DECL|field|listener
specifier|private
specifier|final
name|CamelContextStartupListener
name|listener
decl_stmt|;
DECL|field|contextStarted
specifier|private
specifier|final
name|AtomicBoolean
name|contextStarted
decl_stmt|;
DECL|field|namespace
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
DECL|field|clusterServiceSelector
specifier|private
specifier|final
name|CamelClusterService
operator|.
name|Selector
name|clusterServiceSelector
decl_stmt|;
DECL|field|clusterService
specifier|private
name|CamelClusterService
name|clusterService
decl_stmt|;
DECL|field|clusterView
specifier|private
name|CamelClusterView
name|clusterView
decl_stmt|;
DECL|field|initialDelay
specifier|private
name|Duration
name|initialDelay
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|ClusteredRoutePolicy (CamelClusterService clusterService, CamelClusterService.Selector clusterServiceSelector, String namespace)
specifier|private
name|ClusteredRoutePolicy
parameter_list|(
name|CamelClusterService
name|clusterService
parameter_list|,
name|CamelClusterService
operator|.
name|Selector
name|clusterServiceSelector
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
name|clusterService
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
name|clusterServiceSelector
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|namespace
argument_list|,
literal|"Namespace"
argument_list|)
expr_stmt|;
name|this
operator|.
name|leadershipEventListener
operator|=
operator|new
name|CamelClusterLeadershipListener
argument_list|()
expr_stmt|;
name|this
operator|.
name|stoppedRoutes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|startedRoutes
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|leader
operator|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|contextStarted
operator|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|initialDelay
operator|=
name|Duration
operator|.
name|ofMillis
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|listener
operator|=
operator|new
name|CamelContextStartupListener
argument_list|()
expr_stmt|;
name|this
operator|.
name|listener
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// Cleanup the policy when all the routes it manages have been shut down
comment|// so a single policy instance can be shared among routes.
name|this
operator|.
name|refCount
operator|=
name|ReferenceCount
operator|.
name|onRelease
argument_list|(
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|camelContext
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|removeEventNotifier
argument_list|(
name|listener
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
comment|// Remove event listener
name|clusterView
operator|.
name|removeEventListener
argument_list|(
name|leadershipEventListener
argument_list|)
expr_stmt|;
comment|// If all the routes have been shut down then the view and its
comment|// resources can eventually be released.
name|clusterView
operator|.
name|getClusterService
argument_list|()
operator|.
name|releaseView
argument_list|(
name|clusterView
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
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|this
operator|.
name|camelContext
operator|==
name|camelContext
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|this
operator|.
name|camelContext
operator|!=
literal|null
operator|&&
name|this
operator|.
name|camelContext
operator|!=
name|camelContext
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext should not be changed: current="
operator|+
name|this
operator|.
name|camelContext
operator|+
literal|", new="
operator|+
name|camelContext
argument_list|)
throw|;
block|}
try|try
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|camelContext
operator|.
name|addStartupListener
argument_list|(
name|this
operator|.
name|listener
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|this
operator|.
name|listener
argument_list|)
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"ClusteredRoutePolicy"
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
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getInitialDelay ()
specifier|public
name|Duration
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
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
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
comment|// ****************************************************
comment|// life-cycle
comment|// ****************************************************
DECL|method|definition (Route route)
specifier|private
name|RouteDefinition
name|definition
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
operator|(
name|RouteDefinition
operator|)
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoute
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|super
operator|.
name|onInit
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Route managed by {}. Setting route {} AutoStartup flag to false."
argument_list|,
name|getClass
argument_list|()
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|definition
argument_list|(
name|route
argument_list|)
operator|.
name|setAutoStartup
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|this
operator|.
name|refCount
operator|.
name|retain
argument_list|()
expr_stmt|;
name|this
operator|.
name|stoppedRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|startManagedRoutes
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|clusterService
operator|==
literal|null
condition|)
block|{
name|clusterService
operator|=
name|ClusterServiceHelper
operator|.
name|lookupService
argument_list|(
name|camelContext
argument_list|,
name|clusterServiceSelector
argument_list|)
operator|.
name|orElseThrow
argument_list|(
parameter_list|()
lambda|->
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelCluster service not found"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"ClusteredRoutePolicy {} is using ClusterService instance {} (id={}, type={})"
argument_list|,
name|this
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
name|clusterView
operator|=
name|clusterService
operator|.
name|getView
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|public
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|refCount
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
comment|// ****************************************************
comment|// Management
comment|// ****************************************************
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is this route the master or a slave"
argument_list|)
DECL|method|isLeader ()
specifier|public
name|boolean
name|isLeader
parameter_list|()
block|{
return|return
name|leader
operator|.
name|get
argument_list|()
return|;
block|}
comment|// ****************************************************
comment|// Route managements
comment|// ****************************************************
DECL|method|setLeader (boolean isLeader)
specifier|private
specifier|synchronized
name|void
name|setLeader
parameter_list|(
name|boolean
name|isLeader
parameter_list|)
block|{
if|if
condition|(
name|isLeader
operator|&&
name|leader
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
name|isLeader
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Leadership taken"
argument_list|)
expr_stmt|;
name|startManagedRoutes
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isLeader
operator|&&
name|leader
operator|.
name|getAndSet
argument_list|(
name|isLeader
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Leadership lost"
argument_list|)
expr_stmt|;
name|stopManagedRoutes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startManagedRoutes ()
specifier|private
name|void
name|startManagedRoutes
parameter_list|()
block|{
if|if
condition|(
name|isLeader
argument_list|()
condition|)
block|{
name|doStartManagedRoutes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// If the leadership has been lost in the meanwhile, stop any
comment|// eventually started route
name|doStopManagedRoutes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doStartManagedRoutes ()
specifier|private
name|void
name|doStartManagedRoutes
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
for|for
control|(
name|Route
name|route
range|:
name|stoppedRoutes
control|)
block|{
name|ServiceStatus
name|status
init|=
name|definition
argument_list|(
name|route
argument_list|)
operator|.
name|getStatus
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|.
name|isStartable
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Starting route '{}'"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|startedRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
name|stoppedRoutes
operator|.
name|removeAll
argument_list|(
name|startedRoutes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopManagedRoutes ()
specifier|private
name|void
name|stopManagedRoutes
parameter_list|()
block|{
if|if
condition|(
name|isLeader
argument_list|()
condition|)
block|{
comment|// If became a leader in the meanwhile, start any eventually stopped
comment|// route
name|doStartManagedRoutes
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|doStopManagedRoutes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doStopManagedRoutes ()
specifier|private
name|void
name|doStopManagedRoutes
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
return|return;
block|}
try|try
block|{
for|for
control|(
name|Route
name|route
range|:
name|startedRoutes
control|)
block|{
name|ServiceStatus
name|status
init|=
name|definition
argument_list|(
name|route
argument_list|)
operator|.
name|getStatus
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|status
operator|.
name|isStoppable
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Stopping route '{}'"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|stopRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|stoppedRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
name|startedRoutes
operator|.
name|removeAll
argument_list|(
name|stoppedRoutes
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onCamelContextStarted ()
specifier|private
name|void
name|onCamelContextStarted
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Apply cluster policy (stopped-routes='{}', started-routes='{}')"
argument_list|,
name|stoppedRoutes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Route
operator|::
name|getId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|,
name|startedRoutes
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|Route
operator|::
name|getId
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|clusterView
operator|.
name|addEventListener
argument_list|(
name|leadershipEventListener
argument_list|)
expr_stmt|;
block|}
comment|// ****************************************************
comment|// Event handling
comment|// ****************************************************
DECL|class|CamelClusterLeadershipListener
specifier|private
class|class
name|CamelClusterLeadershipListener
implements|implements
name|CamelClusterEventListener
operator|.
name|Leadership
block|{
annotation|@
name|Override
DECL|method|leadershipChanged (CamelClusterView view, Optional<CamelClusterMember> leader)
specifier|public
name|void
name|leadershipChanged
parameter_list|(
name|CamelClusterView
name|view
parameter_list|,
name|Optional
argument_list|<
name|CamelClusterMember
argument_list|>
name|leader
parameter_list|)
block|{
name|setLeader
argument_list|(
name|clusterView
operator|.
name|getLocalMember
argument_list|()
operator|.
name|isLeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|CamelContextStartupListener
specifier|private
class|class
name|CamelContextStartupListener
extends|extends
name|EventNotifierSupport
implements|implements
name|StartupListener
block|{
annotation|@
name|Override
DECL|method|notify (CamelEvent event)
specifier|public
name|void
name|notify
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|onCamelContextStarted
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled (CamelEvent event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|CamelEvent
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|CamelContextStartedEvent
return|;
block|}
annotation|@
name|Override
DECL|method|onCamelContextStarted (CamelContext context, boolean alreadyStarted)
specifier|public
name|void
name|onCamelContextStarted
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|boolean
name|alreadyStarted
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|alreadyStarted
condition|)
block|{
comment|// Invoke it only if the context was already started as this
comment|// method is not invoked at last event as documented but after
comment|// routes warm-up so this is useful for routes deployed after
comment|// the camel context has been started-up. For standard routes
comment|// configuration the notification of the camel context started
comment|// is provided by EventNotifier.
comment|//
comment|// We should check why this callback is not invoked at latest
comment|// stage, or maybe rename it as it is misleading and provide a
comment|// better alternative for intercept camel events.
name|onCamelContextStarted
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|onCamelContextStarted ()
specifier|private
name|void
name|onCamelContextStarted
parameter_list|()
block|{
comment|// Start managing the routes only when the camel context is started
comment|// so start/stop of managed routes do not clash with CamelContext
comment|// startup
if|if
condition|(
name|contextStarted
operator|.
name|compareAndSet
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|)
condition|)
block|{
comment|// Eventually delay the startup of the routes a later time
if|if
condition|(
name|initialDelay
operator|.
name|toMillis
argument_list|()
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Policy will be effective in {}"
argument_list|,
name|initialDelay
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|schedule
argument_list|(
name|ClusteredRoutePolicy
operator|.
name|this
operator|::
name|onCamelContextStarted
argument_list|,
name|initialDelay
operator|.
name|toMillis
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ClusteredRoutePolicy
operator|.
name|this
operator|.
name|onCamelContextStarted
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// ****************************************************
comment|// Static helpers
comment|// ****************************************************
DECL|method|forNamespace (CamelContext camelContext, CamelClusterService.Selector selector, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicy
name|forNamespace
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|CamelClusterService
operator|.
name|Selector
name|selector
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
name|ClusteredRoutePolicy
name|policy
init|=
operator|new
name|ClusteredRoutePolicy
argument_list|(
literal|null
argument_list|,
name|selector
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
name|policy
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
name|policy
return|;
block|}
DECL|method|forNamespace (CamelContext camelContext, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicy
name|forNamespace
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|forNamespace
argument_list|(
name|camelContext
argument_list|,
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
argument_list|,
name|namespace
argument_list|)
return|;
block|}
DECL|method|forNamespace (CamelClusterService service, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicy
name|forNamespace
parameter_list|(
name|CamelClusterService
name|service
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ClusteredRoutePolicy
argument_list|(
name|service
argument_list|,
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
argument_list|,
name|namespace
argument_list|)
return|;
block|}
DECL|method|forNamespace (CamelClusterService.Selector selector, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicy
name|forNamespace
parameter_list|(
name|CamelClusterService
operator|.
name|Selector
name|selector
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ClusteredRoutePolicy
argument_list|(
literal|null
argument_list|,
name|selector
argument_list|,
name|namespace
argument_list|)
return|;
block|}
DECL|method|forNamespace (String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicy
name|forNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|forNamespace
argument_list|(
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

