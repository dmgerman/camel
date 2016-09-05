begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|policy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|Executors
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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicReference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|KeyValueClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|SessionClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|async
operator|.
name|ConsulResponseCallback
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|ConsulResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|kv
operator|.
name|Value
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|session
operator|.
name|ImmutableSession
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|option
operator|.
name|QueryOptions
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
name|NonManagedService
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
name|ObjectHelper
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
DECL|class|ConsulRoutePolicy
specifier|public
class|class
name|ConsulRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|NonManagedService
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
name|ConsulRoutePolicy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|Object
name|lock
decl_stmt|;
DECL|field|consul
specifier|private
specifier|final
name|Consul
name|consul
decl_stmt|;
DECL|field|sessionClient
specifier|private
specifier|final
name|SessionClient
name|sessionClient
decl_stmt|;
DECL|field|keyValueClient
specifier|private
specifier|final
name|KeyValueClient
name|keyValueClient
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
DECL|field|index
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|BigInteger
argument_list|>
name|index
decl_stmt|;
DECL|field|serviceName
specifier|private
name|String
name|serviceName
decl_stmt|;
DECL|field|servicePath
specifier|private
name|String
name|servicePath
decl_stmt|;
DECL|field|ttl
specifier|private
name|int
name|ttl
decl_stmt|;
DECL|field|lockDelay
specifier|private
name|int
name|lockDelay
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|shouldStopConsumer
specifier|private
name|boolean
name|shouldStopConsumer
decl_stmt|;
DECL|field|sessionId
specifier|private
name|String
name|sessionId
decl_stmt|;
DECL|method|ConsulRoutePolicy ()
specifier|public
name|ConsulRoutePolicy
parameter_list|()
block|{
name|this
argument_list|(
name|Consul
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsulRoutePolicy (Consul consul)
specifier|public
name|ConsulRoutePolicy
parameter_list|(
name|Consul
name|consul
parameter_list|)
block|{
name|this
operator|.
name|consul
operator|=
name|consul
expr_stmt|;
name|this
operator|.
name|sessionClient
operator|=
name|consul
operator|.
name|sessionClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|keyValueClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
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
name|lock
operator|=
operator|new
name|Object
argument_list|()
expr_stmt|;
name|this
operator|.
name|index
operator|=
operator|new
name|AtomicReference
argument_list|<>
argument_list|(
name|BigInteger
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|servicePath
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|ttl
operator|=
literal|60
expr_stmt|;
name|this
operator|.
name|lockDelay
operator|=
literal|10
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|shouldStopConsumer
operator|=
literal|true
expr_stmt|;
name|this
operator|.
name|sessionId
operator|=
literal|null
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
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
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
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|suspendedRoutes
operator|.
name|remove
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|sessionId
operator|==
literal|null
condition|)
block|{
name|sessionId
operator|=
name|sessionClient
operator|.
name|createSession
argument_list|(
name|ImmutableSession
operator|.
name|builder
argument_list|()
operator|.
name|name
argument_list|(
name|serviceName
argument_list|)
operator|.
name|ttl
argument_list|(
name|ttl
operator|+
literal|"s"
argument_list|)
operator|.
name|lockDelay
argument_list|(
name|lockDelay
operator|+
literal|"s"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|getId
argument_list|()
expr_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"SessionID = {}"
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
expr_stmt|;
block|}
name|setLeader
argument_list|(
name|keyValueClient
operator|.
name|acquireLock
argument_list|(
name|servicePath
argument_list|,
name|sessionId
argument_list|)
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Watcher
argument_list|()
argument_list|)
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
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|sessionId
operator|!=
literal|null
condition|)
block|{
name|sessionClient
operator|.
name|destroySession
argument_list|(
name|sessionId
argument_list|)
expr_stmt|;
name|sessionId
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|executorService
operator|.
name|awaitTermination
argument_list|(
name|ttl
operator|/
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
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
name|debug
argument_list|(
literal|"Leadership taken ({}, {})"
argument_list|,
name|serviceName
argument_list|,
name|sessionId
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
name|debug
argument_list|(
literal|"Leadership lost ({}, {})"
argument_list|,
name|serviceName
argument_list|,
name|sessionId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|startConsumer (Route route)
specifier|private
name|void
name|startConsumer
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
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
block|}
DECL|method|stopConsumer (Route route)
specifier|private
name|void
name|stopConsumer
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
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
block|}
DECL|method|startAllStoppedConsumers ()
specifier|private
name|void
name|startAllStoppedConsumers
parameter_list|()
block|{
synchronized|synchronized
init|(
name|lock
init|)
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
block|}
comment|// *************************************************************************
comment|// Getter/Setters
comment|// *************************************************************************
DECL|method|getConsul ()
specifier|public
name|Consul
name|getConsul
parameter_list|()
block|{
return|return
name|consul
return|;
block|}
DECL|method|getServiceName ()
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
DECL|method|setServiceName (String serviceName)
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
name|this
operator|.
name|servicePath
operator|=
name|String
operator|.
name|format
argument_list|(
literal|"/service/%s/leader"
argument_list|,
name|serviceName
argument_list|)
expr_stmt|;
block|}
DECL|method|getTtl ()
specifier|public
name|int
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
DECL|method|setTtl (int ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|int
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
operator|>
literal|10
condition|?
name|ttl
else|:
literal|10
expr_stmt|;
block|}
DECL|method|getLockDelay ()
specifier|public
name|int
name|getLockDelay
parameter_list|()
block|{
return|return
name|lockDelay
return|;
block|}
DECL|method|setLockDelay (int lockDelay)
specifier|public
name|void
name|setLockDelay
parameter_list|(
name|int
name|lockDelay
parameter_list|)
block|{
name|this
operator|.
name|lockDelay
operator|=
name|lockDelay
operator|>
literal|10
condition|?
name|lockDelay
else|:
literal|10
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
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
comment|// *************************************************************************
comment|// Watch
comment|// *************************************************************************
DECL|class|Watcher
specifier|private
class|class
name|Watcher
implements|implements
name|Runnable
implements|,
name|ConsulResponseCallback
argument_list|<
name|Optional
argument_list|<
name|Value
argument_list|>
argument_list|>
block|{
annotation|@
name|Override
DECL|method|onComplete (ConsulResponse<Optional<Value>> consulResponse)
specifier|public
name|void
name|onComplete
parameter_list|(
name|ConsulResponse
argument_list|<
name|Optional
argument_list|<
name|Value
argument_list|>
argument_list|>
name|consulResponse
parameter_list|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|Value
name|response
init|=
name|consulResponse
operator|.
name|getResponse
argument_list|()
operator|.
name|orNull
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
name|String
name|sid
init|=
name|response
operator|.
name|getSession
argument_list|()
operator|.
name|orNull
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|sid
argument_list|)
condition|)
block|{
comment|// If the key is not held by any session, try acquire a
comment|// lock (become leader)
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Try to take leadership ..."
argument_list|)
expr_stmt|;
name|setLeader
argument_list|(
name|keyValueClient
operator|.
name|acquireLock
argument_list|(
name|servicePath
argument_list|,
name|sessionId
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|sessionId
operator|.
name|equals
argument_list|(
name|sid
argument_list|)
operator|&&
name|leader
operator|.
name|get
argument_list|()
condition|)
block|{
comment|// Looks like I've lost leadership
name|setLeader
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
name|index
operator|.
name|set
argument_list|(
name|consulResponse
operator|.
name|getIndex
argument_list|()
argument_list|)
expr_stmt|;
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onFailure (Throwable throwable)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|handleException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
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
name|isRunAllowed
argument_list|()
condition|)
block|{
comment|// Refresh session
name|sessionClient
operator|.
name|renewSession
argument_list|(
name|sessionId
argument_list|)
expr_stmt|;
name|keyValueClient
operator|.
name|getValue
argument_list|(
name|servicePath
argument_list|,
name|QueryOptions
operator|.
name|blockSeconds
argument_list|(
name|ttl
operator|/
literal|3
argument_list|,
name|index
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

