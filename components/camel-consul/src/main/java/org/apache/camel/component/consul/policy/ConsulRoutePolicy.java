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
name|consul
operator|.
name|ConsulConfiguration
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
name|consul
operator|.
name|ConsulConstants
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
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Route policy using Consul as clustered lock"
argument_list|)
DECL|class|ConsulRoutePolicy
specifier|public
specifier|final
class|class
name|ConsulRoutePolicy
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
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
DECL|field|leader
specifier|private
specifier|final
name|AtomicBoolean
name|leader
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
DECL|field|suspendedRoutes
specifier|private
specifier|final
name|Set
argument_list|<
name|Route
argument_list|>
name|suspendedRoutes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|AtomicReference
argument_list|<
name|BigInteger
argument_list|>
name|index
init|=
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
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|ttl
specifier|private
name|int
name|ttl
init|=
literal|60
decl_stmt|;
DECL|field|lockDelay
specifier|private
name|int
name|lockDelay
init|=
literal|10
decl_stmt|;
DECL|field|shouldStopConsumer
specifier|private
name|boolean
name|shouldStopConsumer
init|=
literal|true
decl_stmt|;
DECL|field|consulUrl
specifier|private
name|String
name|consulUrl
init|=
name|ConsulConstants
operator|.
name|CONSUL_DEFAULT_URL
decl_stmt|;
DECL|field|consul
specifier|private
name|Consul
name|consul
decl_stmt|;
DECL|field|sessionClient
specifier|private
name|SessionClient
name|sessionClient
decl_stmt|;
DECL|field|keyValueClient
specifier|private
name|KeyValueClient
name|keyValueClient
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
block|{     }
DECL|method|ConsulRoutePolicy (String consulUrl)
specifier|public
name|ConsulRoutePolicy
parameter_list|(
name|String
name|consulUrl
parameter_list|)
block|{
name|this
operator|.
name|consulUrl
operator|=
name|consulUrl
expr_stmt|;
block|}
DECL|method|ConsulRoutePolicy (ConsulConfiguration configuration)
specifier|public
name|ConsulRoutePolicy
parameter_list|(
name|ConsulConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|consulUrl
operator|=
name|configuration
operator|.
name|getUrl
argument_list|()
expr_stmt|;
name|this
operator|.
name|consul
operator|=
name|configuration
operator|.
name|createConsulClient
argument_list|()
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
DECL|method|getConsulUrl ()
specifier|public
name|String
name|getConsulUrl
parameter_list|()
block|{
return|return
name|consulUrl
return|;
block|}
DECL|method|setConsulUrl (String consulUrl)
specifier|public
name|void
name|setConsulUrl
parameter_list|(
name|String
name|consulUrl
parameter_list|)
block|{
name|this
operator|.
name|consulUrl
operator|=
name|consulUrl
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceName
argument_list|,
literal|"serviceName"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|servicePath
argument_list|,
literal|"servicePath"
argument_list|)
expr_stmt|;
if|if
condition|(
name|consul
operator|==
literal|null
condition|)
block|{
name|Consul
operator|.
name|Builder
name|builder
init|=
name|Consul
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|consulUrl
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|withUrl
argument_list|(
name|consulUrl
argument_list|)
expr_stmt|;
block|}
name|consul
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sessionClient
operator|==
literal|null
condition|)
block|{
name|sessionClient
operator|=
name|consul
operator|.
name|sessionClient
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|keyValueClient
operator|==
literal|null
condition|)
block|{
name|keyValueClient
operator|=
name|consul
operator|.
name|keyValueClient
argument_list|()
expr_stmt|;
block|}
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
literal|"ConsulRoutePolicy"
argument_list|)
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
block|}
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The consul service name"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The time to live (seconds)"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The lock delay (seconds)"
argument_list|)
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
name|Optional
argument_list|<
name|Value
argument_list|>
name|value
init|=
name|consulResponse
operator|.
name|getResponse
argument_list|()
decl_stmt|;
if|if
condition|(
name|value
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|Optional
argument_list|<
name|String
argument_list|>
name|sid
init|=
name|value
operator|.
name|get
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|sid
operator|.
name|isPresent
argument_list|()
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|sid
operator|.
name|get
argument_list|()
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

