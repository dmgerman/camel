begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
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
name|ExecutorService
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
name|Future
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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
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
name|hazelcast
operator|.
name|HazelcastUtil
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
name|util
operator|.
name|StringHelper
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
literal|"Route policy using Hazelcast as clustered lock"
argument_list|)
DECL|class|HazelcastRoutePolicy
specifier|public
class|class
name|HazelcastRoutePolicy
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
name|HazelcastRoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|managedInstance
specifier|private
specifier|final
name|boolean
name|managedInstance
decl_stmt|;
DECL|field|leader
specifier|private
specifier|final
name|AtomicBoolean
name|leader
decl_stmt|;
DECL|field|suspendedRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|suspendedRoutes
decl_stmt|;
DECL|field|route
specifier|private
name|Route
name|route
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|instance
specifier|private
name|HazelcastInstance
name|instance
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
DECL|field|tryLockTimeout
specifier|private
name|long
name|tryLockTimeout
decl_stmt|;
DECL|field|tryLockTimeoutUnit
specifier|private
name|TimeUnit
name|tryLockTimeoutUnit
decl_stmt|;
DECL|field|locks
specifier|private
name|IMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|locks
decl_stmt|;
DECL|field|future
specifier|private
specifier|volatile
name|Future
argument_list|<
name|Void
argument_list|>
name|future
decl_stmt|;
DECL|field|shouldStopConsumer
specifier|private
name|boolean
name|shouldStopConsumer
decl_stmt|;
DECL|method|HazelcastRoutePolicy ()
specifier|public
name|HazelcastRoutePolicy
parameter_list|()
block|{
name|this
argument_list|(
name|HazelcastUtil
operator|.
name|newInstance
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|HazelcastRoutePolicy (HazelcastInstance instance)
specifier|public
name|HazelcastRoutePolicy
parameter_list|(
name|HazelcastInstance
name|instance
parameter_list|)
block|{
name|this
argument_list|(
name|instance
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|HazelcastRoutePolicy (HazelcastInstance instance, boolean managedInstance)
specifier|public
name|HazelcastRoutePolicy
parameter_list|(
name|HazelcastInstance
name|instance
parameter_list|,
name|boolean
name|managedInstance
parameter_list|)
block|{
name|this
operator|.
name|instance
operator|=
name|instance
expr_stmt|;
name|this
operator|.
name|managedInstance
operator|=
name|managedInstance
expr_stmt|;
name|this
operator|.
name|suspendedRoutes
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
name|lockMapName
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|lockKey
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|lockValue
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|tryLockTimeout
operator|=
literal|10
operator|*
literal|1000
expr_stmt|;
name|this
operator|.
name|tryLockTimeoutUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
name|this
operator|.
name|locks
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|future
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|shouldStopConsumer
operator|=
literal|true
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
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStart (Route route)
specifier|public
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
operator|!
name|leader
operator|.
name|get
argument_list|()
operator|&&
name|shouldStopConsumer
condition|)
block|{
name|stopConsumer
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onStop (Route route)
specifier|public
specifier|synchronized
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onSuspend (Route route)
specifier|public
specifier|synchronized
name|void
name|onSuspend
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
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
name|executorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"HazelcastRoutePolicy"
argument_list|)
expr_stmt|;
name|locks
operator|=
name|instance
operator|.
name|getMap
argument_list|(
name|lockMapName
argument_list|)
expr_stmt|;
name|future
operator|=
name|executorService
operator|.
name|submit
argument_list|(
name|this
operator|::
name|acquireLeadership
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|managedInstance
condition|)
block|{
name|instance
operator|.
name|shutdown
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// *************************************************************************
comment|//
comment|// *************************************************************************
DECL|method|setLeader (boolean isLeader)
specifier|protected
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
name|startAllStoppedConsumers
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
operator|!
name|leader
operator|.
name|getAndSet
argument_list|(
name|isLeader
argument_list|)
operator|&&
name|isLeader
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
block|}
block|}
block|}
DECL|method|startConsumer (Route route)
specifier|private
specifier|synchronized
name|void
name|startConsumer
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|suspendedRoutes
operator|.
name|contains
argument_list|(
name|route
argument_list|)
condition|)
block|{
name|startConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
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
DECL|method|stopConsumer (Route route)
specifier|private
specifier|synchronized
name|void
name|stopConsumer
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|suspendedRoutes
operator|.
name|contains
argument_list|(
name|route
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Stopping consumer for {} ({})"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|stopConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|suspendedRoutes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
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
DECL|method|startAllStoppedConsumers ()
specifier|private
specifier|synchronized
name|void
name|startAllStoppedConsumers
parameter_list|()
block|{
try|try
block|{
for|for
control|(
name|Route
name|route
range|:
name|suspendedRoutes
control|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Starting consumer for {} ({})"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|startConsumer
argument_list|(
name|route
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|suspendedRoutes
operator|.
name|clear
argument_list|()
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
literal|"The route id"
argument_list|)
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
return|return
name|route
operator|.
name|getId
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The consumer endpoint"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getEndpointUrl ()
specifier|public
name|String
name|getEndpointUrl
parameter_list|()
block|{
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
name|route
operator|.
name|getConsumer
argument_list|()
operator|!=
literal|null
operator|&&
name|route
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|route
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
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
literal|"Whether to stop consumer when starting up and failed to become master"
argument_list|)
DECL|method|isShouldStopConsumer ()
specifier|public
name|boolean
name|isShouldStopConsumer
parameter_list|()
block|{
return|return
name|shouldStopConsumer
return|;
block|}
DECL|method|setShouldStopConsumer (boolean shouldStopConsumer)
specifier|public
name|void
name|setShouldStopConsumer
parameter_list|(
name|boolean
name|shouldStopConsumer
parameter_list|)
block|{
name|this
operator|.
name|shouldStopConsumer
operator|=
name|shouldStopConsumer
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
literal|"Timeout used by slaves to try to obtain the lock to become new master"
argument_list|)
DECL|method|getTryLockTimeout ()
specifier|public
name|long
name|getTryLockTimeout
parameter_list|()
block|{
return|return
name|tryLockTimeout
return|;
block|}
DECL|method|setTryLockTimeout (long tryLockTimeout)
specifier|public
name|void
name|setTryLockTimeout
parameter_list|(
name|long
name|tryLockTimeout
parameter_list|)
block|{
name|this
operator|.
name|tryLockTimeout
operator|=
name|tryLockTimeout
expr_stmt|;
block|}
DECL|method|setTryLockTimeout (long tryLockTimeout, TimeUnit tryLockTimeoutUnit)
specifier|public
name|void
name|setTryLockTimeout
parameter_list|(
name|long
name|tryLockTimeout
parameter_list|,
name|TimeUnit
name|tryLockTimeoutUnit
parameter_list|)
block|{
name|this
operator|.
name|tryLockTimeout
operator|=
name|tryLockTimeout
expr_stmt|;
name|this
operator|.
name|tryLockTimeoutUnit
operator|=
name|tryLockTimeoutUnit
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Timeout unit"
argument_list|)
DECL|method|getTryLockTimeoutUnit ()
specifier|public
name|TimeUnit
name|getTryLockTimeoutUnit
parameter_list|()
block|{
return|return
name|tryLockTimeoutUnit
return|;
block|}
DECL|method|setTryLockTimeoutUnit (TimeUnit tryLockTimeoutUnit)
specifier|public
name|void
name|setTryLockTimeoutUnit
parameter_list|(
name|TimeUnit
name|tryLockTimeoutUnit
parameter_list|)
block|{
name|this
operator|.
name|tryLockTimeoutUnit
operator|=
name|tryLockTimeoutUnit
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
DECL|method|acquireLeadership ()
specifier|private
name|Void
name|acquireLeadership
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|locked
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
try|try
block|{
name|locked
operator|=
name|locks
operator|.
name|tryLock
argument_list|(
name|lockKey
argument_list|,
name|tryLockTimeout
argument_list|,
name|tryLockTimeoutUnit
argument_list|)
expr_stmt|;
if|if
condition|(
name|locked
condition|)
block|{
name|locks
operator|.
name|put
argument_list|(
name|lockKey
argument_list|,
name|lockValue
argument_list|)
expr_stmt|;
name|setLeader
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// Wait almost forever
name|Thread
operator|.
name|sleep
argument_list|(
name|Long
operator|.
name|MAX_VALUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Failed to acquire lock (map={}, key={}, val={}) after {} {}"
argument_list|,
name|lockMapName
argument_list|,
name|lockKey
argument_list|,
name|lockValue
argument_list|,
name|tryLockTimeout
argument_list|,
name|tryLockTimeoutUnit
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|locked
condition|)
block|{
name|locks
operator|.
name|remove
argument_list|(
name|lockKey
argument_list|)
expr_stmt|;
name|locks
operator|.
name|unlock
argument_list|(
name|lockKey
argument_list|)
expr_stmt|;
name|locked
operator|=
literal|false
expr_stmt|;
block|}
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

