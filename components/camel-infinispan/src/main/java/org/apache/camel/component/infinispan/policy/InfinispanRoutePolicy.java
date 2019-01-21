begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
operator|.
name|policy
package|;
end_package

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
name|ScheduledFuture
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
name|component
operator|.
name|infinispan
operator|.
name|InfinispanConfiguration
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
name|infinispan
operator|.
name|InfinispanManager
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
name|infinispan
operator|.
name|InfinispanUtil
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
name|StringHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|RemoteCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|annotation
operator|.
name|ClientCacheEntryExpired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|annotation
operator|.
name|ClientCacheEntryRemoved
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|annotation
operator|.
name|ClientListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|event
operator|.
name|ClientCacheEntryExpiredEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|client
operator|.
name|hotrod
operator|.
name|event
operator|.
name|ClientCacheEntryRemovedEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|Listener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryExpired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|annotation
operator|.
name|CacheEntryRemoved
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|notifications
operator|.
name|cachelistener
operator|.
name|event
operator|.
name|CacheEntryEvent
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
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Route policy using Infinispan as clustered lock"
argument_list|)
DECL|class|InfinispanRoutePolicy
specifier|public
class|class
name|InfinispanRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|CamelContextAware
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
name|InfinispanRoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|stoppeddRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|stoppeddRoutes
decl_stmt|;
DECL|field|manager
specifier|private
specifier|final
name|InfinispanManager
name|manager
decl_stmt|;
DECL|field|refCount
specifier|private
specifier|final
name|ReferenceCount
name|refCount
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ScheduledExecutorService
name|executorService
decl_stmt|;
DECL|field|shouldStopRoute
specifier|private
name|boolean
name|shouldStopRoute
decl_stmt|;
DECL|field|lockMapName
specifier|private
name|String
name|lockMapName
decl_stmt|;
DECL|field|lockKey
specifier|private
name|String
name|lockKey
decl_stmt|;
DECL|field|lockValue
specifier|private
name|String
name|lockValue
decl_stmt|;
DECL|field|lifespan
specifier|private
name|long
name|lifespan
decl_stmt|;
DECL|field|lifespanTimeUnit
specifier|private
name|TimeUnit
name|lifespanTimeUnit
decl_stmt|;
DECL|field|future
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|service
specifier|private
name|Service
name|service
decl_stmt|;
DECL|method|InfinispanRoutePolicy (InfinispanConfiguration configuration)
specifier|public
name|InfinispanRoutePolicy
parameter_list|(
name|InfinispanConfiguration
name|configuration
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|InfinispanManager
argument_list|(
name|configuration
argument_list|)
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|InfinispanRoutePolicy (InfinispanManager manager)
specifier|public
name|InfinispanRoutePolicy
parameter_list|(
name|InfinispanManager
name|manager
parameter_list|)
block|{
name|this
argument_list|(
name|manager
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|InfinispanRoutePolicy (InfinispanManager manager, String lockKey, String lockValue)
specifier|public
name|InfinispanRoutePolicy
parameter_list|(
name|InfinispanManager
name|manager
parameter_list|,
name|String
name|lockKey
parameter_list|,
name|String
name|lockValue
parameter_list|)
block|{
name|this
operator|.
name|manager
operator|=
name|manager
expr_stmt|;
name|this
operator|.
name|stoppeddRoutes
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
name|shouldStopRoute
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|lockKey
operator|=
name|lockKey
expr_stmt|;
name|this
operator|.
name|lockValue
operator|=
name|lockValue
expr_stmt|;
name|this
operator|.
name|lifespan
operator|=
literal|30
expr_stmt|;
name|this
operator|.
name|lifespanTimeUnit
operator|=
name|TimeUnit
operator|.
name|SECONDS
expr_stmt|;
name|this
operator|.
name|service
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|refCount
operator|=
name|ReferenceCount
operator|.
name|on
argument_list|(
name|this
operator|::
name|startService
argument_list|,
name|this
operator|::
name|stopService
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
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
specifier|synchronized
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
name|LOGGER
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
name|RouteDefinition
name|definition
init|=
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
decl_stmt|;
name|definition
operator|.
name|setAutoStartup
argument_list|(
literal|"false"
argument_list|)
expr_stmt|;
name|stoppeddRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|this
operator|.
name|refCount
operator|.
name|retain
argument_list|()
expr_stmt|;
name|startManagedRoutes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|public
specifier|synchronized
name|void
name|doShutdown
parameter_list|()
block|{
name|this
operator|.
name|refCount
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
comment|// ****************************************
comment|// Helpers
comment|// ****************************************
DECL|method|startService ()
specifier|private
name|void
name|startService
parameter_list|()
block|{
comment|// validate
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|lockMapName
argument_list|,
literal|"lockMapName"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|lockKey
argument_list|,
literal|"lockKey"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|lockValue
argument_list|,
literal|"lockValue"
argument_list|,
name|this
argument_list|)
expr_stmt|;
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
try|try
block|{
name|this
operator|.
name|manager
operator|.
name|start
argument_list|()
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"InfinispanRoutePolicy"
argument_list|)
expr_stmt|;
if|if
condition|(
name|lifespanTimeUnit
operator|.
name|convert
argument_list|(
name|lifespan
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|<
literal|2
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Lock lifespan can not be less that 2 seconds"
argument_list|)
throw|;
block|}
if|if
condition|(
name|manager
operator|.
name|isCacheContainerEmbedded
argument_list|()
condition|)
block|{
name|BasicCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
name|manager
operator|.
name|getCache
argument_list|(
name|lockMapName
argument_list|)
decl_stmt|;
name|this
operator|.
name|service
operator|=
operator|new
name|EmbeddedCacheService
argument_list|(
name|InfinispanUtil
operator|.
name|asEmbedded
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// By default, previously existing values for java.util.Map operations
comment|// are not returned for remote caches but policy needs it so force it.
name|BasicCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
name|manager
operator|.
name|getCache
argument_list|(
name|lockMapName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|this
operator|.
name|service
operator|=
operator|new
name|RemoteCacheService
argument_list|(
name|InfinispanUtil
operator|.
name|asRemote
argument_list|(
name|cache
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|service
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|stopService ()
specifier|private
name|void
name|stopService
parameter_list|()
block|{
name|leader
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|future
operator|=
literal|null
expr_stmt|;
block|}
name|manager
operator|.
name|stop
argument_list|()
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|service
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|service
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|executorService
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setLeader (boolean isLeader)
specifier|private
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
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Leadership taken (map={}, key={}, val={})"
argument_list|,
name|lockMapName
argument_list|,
name|lockKey
argument_list|,
name|lockValue
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
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Leadership lost (map={}, key={} val={})"
argument_list|,
name|lockMapName
argument_list|,
name|lockKey
argument_list|,
name|lockValue
argument_list|)
expr_stmt|;
name|stopManagedRoutes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startManagedRoutes ()
specifier|private
specifier|synchronized
name|void
name|startManagedRoutes
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isLeader
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
name|stoppeddRoutes
control|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Starting route {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|startRoute
argument_list|(
name|route
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
name|stoppeddRoutes
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
specifier|synchronized
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
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Stopping route {}"
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
name|stoppeddRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
name|startedRoutes
operator|.
name|removeAll
argument_list|(
name|stoppeddRoutes
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
comment|// *************************************************************************
comment|// Getter/Setters
comment|// *************************************************************************
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to stop route when starting up and failed to become master"
argument_list|)
DECL|method|isShouldStopRoute ()
specifier|public
name|boolean
name|isShouldStopRoute
parameter_list|()
block|{
return|return
name|shouldStopRoute
return|;
block|}
DECL|method|setShouldStopRoute (boolean shouldStopRoute)
specifier|public
name|void
name|setShouldStopRoute
parameter_list|(
name|boolean
name|shouldStopRoute
parameter_list|)
block|{
name|this
operator|.
name|shouldStopRoute
operator|=
name|shouldStopRoute
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The lock map name"
argument_list|)
DECL|method|getLockMapName ()
specifier|public
name|String
name|getLockMapName
parameter_list|()
block|{
return|return
name|lockMapName
return|;
block|}
DECL|method|setLockMapName (String lockMapName)
specifier|public
name|void
name|setLockMapName
parameter_list|(
name|String
name|lockMapName
parameter_list|)
block|{
name|this
operator|.
name|lockMapName
operator|=
name|lockMapName
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The lock key"
argument_list|)
DECL|method|getLockKey ()
specifier|public
name|String
name|getLockKey
parameter_list|()
block|{
return|return
name|lockKey
return|;
block|}
DECL|method|setLockKey (String lockKey)
specifier|public
name|void
name|setLockKey
parameter_list|(
name|String
name|lockKey
parameter_list|)
block|{
name|this
operator|.
name|lockKey
operator|=
name|lockKey
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The lock value"
argument_list|)
DECL|method|getLockValue ()
specifier|public
name|String
name|getLockValue
parameter_list|()
block|{
return|return
name|lockValue
return|;
block|}
DECL|method|setLockValue (String lockValue)
specifier|public
name|void
name|setLockValue
parameter_list|(
name|String
name|lockValue
parameter_list|)
block|{
name|this
operator|.
name|lockValue
operator|=
name|lockValue
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The key lifespan for the lock"
argument_list|)
DECL|method|getLifespan ()
specifier|public
name|long
name|getLifespan
parameter_list|()
block|{
return|return
name|lifespan
return|;
block|}
DECL|method|setLifespan (long lifespan)
specifier|public
name|void
name|setLifespan
parameter_list|(
name|long
name|lifespan
parameter_list|)
block|{
name|this
operator|.
name|lifespan
operator|=
name|lifespan
expr_stmt|;
block|}
DECL|method|setLifespan (long lifespan, TimeUnit lifespanTimeUnit)
specifier|public
name|void
name|setLifespan
parameter_list|(
name|long
name|lifespan
parameter_list|,
name|TimeUnit
name|lifespanTimeUnit
parameter_list|)
block|{
name|this
operator|.
name|lifespan
operator|=
name|lifespan
expr_stmt|;
name|this
operator|.
name|lifespanTimeUnit
operator|=
name|lifespanTimeUnit
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The key lifespan time unit for the lock"
argument_list|)
DECL|method|getLifespanTimeUnit ()
specifier|public
name|TimeUnit
name|getLifespanTimeUnit
parameter_list|()
block|{
return|return
name|lifespanTimeUnit
return|;
block|}
DECL|method|setLifespanTimeUnit (TimeUnit lifespanTimeUnit)
specifier|public
name|void
name|setLifespanTimeUnit
parameter_list|(
name|TimeUnit
name|lifespanTimeUnit
parameter_list|)
block|{
name|this
operator|.
name|lifespanTimeUnit
operator|=
name|lifespanTimeUnit
expr_stmt|;
block|}
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
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
annotation|@
name|Listener
argument_list|(
name|clustered
operator|=
literal|true
argument_list|,
name|sync
operator|=
literal|false
argument_list|)
DECL|class|EmbeddedCacheService
specifier|private
specifier|final
class|class
name|EmbeddedCacheService
extends|extends
name|ServiceSupport
implements|implements
name|Runnable
block|{
DECL|field|cache
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
decl_stmt|;
DECL|field|future
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|method|EmbeddedCacheService (Cache<String, String> cache)
name|EmbeddedCacheService
parameter_list|(
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|future
operator|=
literal|null
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
name|this
operator|.
name|future
operator|=
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|this
operator|::
name|run
argument_list|,
literal|0
argument_list|,
name|lifespan
operator|/
literal|2
argument_list|,
name|lifespanTimeUnit
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|addListener
argument_list|(
name|this
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
name|this
operator|.
name|cache
operator|.
name|removeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|remove
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|)
expr_stmt|;
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|future
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
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
if|if
condition|(
name|isLeader
argument_list|()
condition|)
block|{
comment|// I'm still the leader, so refresh the key so it does not expire.
if|if
condition|(
operator|!
name|cache
operator|.
name|replace
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|,
name|lockValue
argument_list|,
name|lifespan
argument_list|,
name|lifespanTimeUnit
argument_list|)
condition|)
block|{
comment|// Looks like I've lost the leadership.
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|isLeader
argument_list|()
condition|)
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|,
name|lifespan
argument_list|,
name|lifespanTimeUnit
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// Acquired the key so I'm the leader.
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockValue
argument_list|,
name|result
argument_list|)
operator|&&
operator|!
name|isLeader
argument_list|()
condition|)
block|{
comment|// Hey, I may have recovered from failure (or reboot was really
comment|// fast) and my key was still there so yeah, I'm the leader again!
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|CacheEntryRemoved
DECL|method|onCacheEntryRemoved (CacheEntryEvent<Object, Object> event)
specifier|public
name|void
name|onCacheEntryRemoved
parameter_list|(
name|CacheEntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockKey
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|CacheEntryExpired
DECL|method|onCacheEntryExpired (CacheEntryEvent<Object, Object> event)
specifier|public
name|void
name|onCacheEntryExpired
parameter_list|(
name|CacheEntryEvent
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockKey
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|ClientListener
DECL|class|RemoteCacheService
specifier|private
specifier|final
class|class
name|RemoteCacheService
extends|extends
name|ServiceSupport
implements|implements
name|Runnable
block|{
DECL|field|cache
specifier|private
name|RemoteCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
decl_stmt|;
DECL|field|future
specifier|private
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|future
decl_stmt|;
DECL|field|version
specifier|private
name|Long
name|version
decl_stmt|;
DECL|method|RemoteCacheService (RemoteCache<String, String> cache)
name|RemoteCacheService
parameter_list|(
name|RemoteCache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
name|this
operator|.
name|future
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|version
operator|=
literal|null
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
name|this
operator|.
name|future
operator|=
name|executorService
operator|.
name|scheduleAtFixedRate
argument_list|(
name|this
operator|::
name|run
argument_list|,
literal|0
argument_list|,
name|lifespan
operator|/
literal|2
argument_list|,
name|lifespanTimeUnit
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|.
name|addClientListener
argument_list|(
name|this
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
name|this
operator|.
name|cache
operator|.
name|removeClientListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|version
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|cache
operator|.
name|removeWithVersion
argument_list|(
name|lockKey
argument_list|,
name|this
operator|.
name|version
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|future
operator|!=
literal|null
condition|)
block|{
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|future
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
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
if|if
condition|(
name|isLeader
argument_list|()
operator|&&
name|version
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Lock refresh key={} with version={}"
argument_list|,
name|lockKey
argument_list|,
name|version
argument_list|)
expr_stmt|;
comment|// I'm still the leader, so refresh the key so it does not expire.
if|if
condition|(
operator|!
name|cache
operator|.
name|replaceWithVersion
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|,
name|version
argument_list|,
operator|(
name|int
operator|)
name|lifespanTimeUnit
operator|.
name|toSeconds
argument_list|(
name|lifespan
argument_list|)
argument_list|)
condition|)
block|{
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|version
operator|=
name|cache
operator|.
name|getWithMetadata
argument_list|(
name|lockKey
argument_list|)
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Lock refreshed key={} with new version={}"
argument_list|,
name|lockKey
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|isLeader
argument_list|()
condition|)
block|{
name|Object
name|result
init|=
name|cache
operator|.
name|putIfAbsent
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|,
name|lifespan
argument_list|,
name|lifespanTimeUnit
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// Acquired the key so I'm the leader.
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Get the version
name|version
operator|=
name|cache
operator|.
name|getWithMetadata
argument_list|(
name|lockKey
argument_list|)
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Lock acquired key={} with version={}"
argument_list|,
name|lockKey
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockValue
argument_list|,
name|result
argument_list|)
operator|&&
operator|!
name|isLeader
argument_list|()
condition|)
block|{
comment|// Hey, I may have recovered from failure (or reboot was really
comment|// fast) and my key was still there so yeah, I'm the leader again!
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Get the version
name|version
operator|=
name|cache
operator|.
name|getWithMetadata
argument_list|(
name|lockKey
argument_list|)
operator|.
name|getVersion
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Lock resumed key={} with version={}"
argument_list|,
name|lockKey
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|ClientCacheEntryRemoved
DECL|method|onCacheEntryRemoved (ClientCacheEntryRemovedEvent<String> event)
specifier|public
name|void
name|onCacheEntryRemoved
parameter_list|(
name|ClientCacheEntryRemovedEvent
argument_list|<
name|String
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockKey
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|ClientCacheEntryExpired
DECL|method|onCacheEntryExpired (ClientCacheEntryExpiredEvent<String> event)
specifier|public
name|void
name|onCacheEntryExpired
parameter_list|(
name|ClientCacheEntryExpiredEvent
argument_list|<
name|String
argument_list|>
name|event
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|lockKey
argument_list|,
name|event
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|run
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|// *************************************************************************
comment|// Helpers
comment|// *************************************************************************
DECL|method|withManager (BasicCacheContainer cacheContainer)
specifier|public
specifier|static
name|InfinispanRoutePolicy
name|withManager
parameter_list|(
name|BasicCacheContainer
name|cacheContainer
parameter_list|)
block|{
name|InfinispanConfiguration
name|conf
init|=
operator|new
name|InfinispanConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setCacheContainer
argument_list|(
name|cacheContainer
argument_list|)
expr_stmt|;
return|return
operator|new
name|InfinispanRoutePolicy
argument_list|(
name|conf
argument_list|)
return|;
block|}
block|}
end_class

end_unit

