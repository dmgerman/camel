begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.etcd.policy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|etcd
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
name|TimeoutException
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
name|AtomicLong
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|client
operator|.
name|promises
operator|.
name|ResponsePromise
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|EtcdClient
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdErrorCode
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdException
import|;
end_import

begin_import
import|import
name|mousio
operator|.
name|etcd4j
operator|.
name|responses
operator|.
name|EtcdKeysResponse
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
name|etcd
operator|.
name|EtcdConfiguration
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
name|etcd
operator|.
name|EtcdConstants
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
name|etcd
operator|.
name|EtcdHelper
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
literal|"Route policy using Etcd as clustered lock"
argument_list|)
DECL|class|EtcdRoutePolicy
specifier|public
class|class
name|EtcdRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|ResponsePromise
operator|.
name|IsSimplePromiseResponseHandler
argument_list|<
name|EtcdKeysResponse
argument_list|>
implements|,
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
name|EtcdRoutePolicy
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
name|AtomicLong
name|index
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|ttl
specifier|private
name|int
name|ttl
init|=
literal|60
decl_stmt|;
DECL|field|watchTimeout
specifier|private
name|int
name|watchTimeout
init|=
literal|60
operator|/
literal|3
decl_stmt|;
DECL|field|shouldStopConsumer
specifier|private
name|boolean
name|shouldStopConsumer
init|=
literal|true
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
DECL|field|client
specifier|private
name|EtcdClient
name|client
decl_stmt|;
DECL|field|managedClient
specifier|private
name|boolean
name|managedClient
decl_stmt|;
DECL|field|clientUris
specifier|private
name|String
name|clientUris
init|=
name|EtcdConstants
operator|.
name|ETCD_DEFAULT_URIS
decl_stmt|;
DECL|method|EtcdRoutePolicy ()
specifier|public
name|EtcdRoutePolicy
parameter_list|()
block|{
name|this
operator|.
name|client
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|managedClient
operator|=
literal|false
expr_stmt|;
block|}
DECL|method|EtcdRoutePolicy (EtcdConfiguration configuration)
specifier|public
name|EtcdRoutePolicy
parameter_list|(
name|EtcdConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|client
operator|=
name|configuration
operator|.
name|createClient
argument_list|()
expr_stmt|;
name|this
operator|.
name|managedClient
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|EtcdRoutePolicy (EtcdClient client)
specifier|public
name|EtcdRoutePolicy
parameter_list|(
name|EtcdClient
name|client
parameter_list|)
block|{
name|this
argument_list|(
name|client
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|EtcdRoutePolicy (EtcdClient client, boolean managedClient)
specifier|public
name|EtcdRoutePolicy
parameter_list|(
name|EtcdClient
name|client
parameter_list|,
name|boolean
name|managedClient
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
name|this
operator|.
name|managedClient
operator|=
name|managedClient
expr_stmt|;
block|}
DECL|method|EtcdRoutePolicy (String clientUris)
specifier|public
name|EtcdRoutePolicy
parameter_list|(
name|String
name|clientUris
parameter_list|)
block|{
name|this
operator|.
name|clientUris
operator|=
name|clientUris
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
name|clientUris
argument_list|,
literal|"clientUris"
argument_list|)
expr_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
operator|new
name|EtcdClient
argument_list|(
name|EtcdHelper
operator|.
name|resolveURIs
argument_list|(
name|camelContext
argument_list|,
name|clientUris
argument_list|)
argument_list|)
expr_stmt|;
name|managedClient
operator|=
literal|true
expr_stmt|;
block|}
name|setLeader
argument_list|(
name|tryTakeLeadership
argument_list|()
argument_list|)
expr_stmt|;
name|watch
argument_list|()
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
name|managedClient
condition|)
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
literal|"Leadership taken (path={}, name={})"
argument_list|,
name|servicePath
argument_list|,
name|serviceName
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
literal|"Leadership lost (path={}, name={})"
argument_list|,
name|servicePath
argument_list|,
name|serviceName
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
DECL|method|getClient ()
specifier|public
name|EtcdClient
name|getClient
parameter_list|()
block|{
return|return
name|client
return|;
block|}
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The etcd service name"
argument_list|)
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
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The etcd service path"
argument_list|)
DECL|method|getServicePath ()
specifier|public
name|String
name|getServicePath
parameter_list|()
block|{
return|return
name|servicePath
return|;
block|}
DECL|method|setServicePath (String servicePath)
specifier|public
name|void
name|setServicePath
parameter_list|(
name|String
name|servicePath
parameter_list|)
block|{
name|this
operator|.
name|servicePath
operator|=
name|servicePath
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
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The watch timeout (seconds)"
argument_list|)
DECL|method|getWatchTimeout ()
specifier|public
name|int
name|getWatchTimeout
parameter_list|()
block|{
return|return
name|watchTimeout
return|;
block|}
DECL|method|setWatchTimeout (int watchTimeout)
specifier|public
name|void
name|setWatchTimeout
parameter_list|(
name|int
name|watchTimeout
parameter_list|)
block|{
name|this
operator|.
name|watchTimeout
operator|=
name|watchTimeout
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Etcd endpoints"
argument_list|)
DECL|method|getClientUris ()
specifier|public
name|String
name|getClientUris
parameter_list|()
block|{
return|return
name|clientUris
return|;
block|}
DECL|method|setClientUris (String clientUris)
specifier|public
name|void
name|setClientUris
parameter_list|(
name|String
name|clientUris
parameter_list|)
block|{
name|this
operator|.
name|clientUris
operator|=
name|clientUris
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Watch
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|onResponse (ResponsePromise<EtcdKeysResponse> promise)
specifier|public
name|void
name|onResponse
parameter_list|(
name|ResponsePromise
argument_list|<
name|EtcdKeysResponse
argument_list|>
name|promise
parameter_list|)
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
name|Throwable
name|throwable
init|=
name|promise
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|throwable
operator|instanceof
name|EtcdException
condition|)
block|{
name|EtcdException
name|exception
init|=
operator|(
name|EtcdException
operator|)
name|throwable
decl_stmt|;
if|if
condition|(
name|EtcdHelper
operator|.
name|isOutdatedIndexException
argument_list|(
name|exception
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Outdated index, key={}, cause={}"
argument_list|,
name|servicePath
argument_list|,
name|exception
operator|.
name|etcdCause
argument_list|)
expr_stmt|;
name|index
operator|.
name|set
argument_list|(
name|exception
operator|.
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
name|throwable
operator|=
literal|null
expr_stmt|;
block|}
block|}
else|else
block|{
try|try
block|{
name|EtcdKeysResponse
name|response
init|=
name|promise
operator|.
name|get
argument_list|()
decl_stmt|;
name|EtcdHelper
operator|.
name|setIndex
argument_list|(
name|index
argument_list|,
name|response
argument_list|)
expr_stmt|;
if|if
condition|(
name|response
operator|.
name|node
operator|.
name|value
operator|==
literal|null
condition|)
block|{
name|setLeader
argument_list|(
name|tryTakeLeadership
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|serviceName
argument_list|,
name|response
operator|.
name|node
operator|.
name|value
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
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Timeout watching for {}"
argument_list|,
name|servicePath
argument_list|)
expr_stmt|;
name|throwable
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|throwable
operator|=
name|e1
expr_stmt|;
block|}
block|}
if|if
condition|(
name|throwable
operator|==
literal|null
condition|)
block|{
name|watch
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|throwable
argument_list|)
throw|;
block|}
block|}
DECL|method|watch ()
specifier|private
name|void
name|watch
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
if|if
condition|(
name|leader
operator|.
name|get
argument_list|()
condition|)
block|{
name|EtcdHelper
operator|.
name|setIndex
argument_list|(
name|index
argument_list|,
name|client
operator|.
name|refresh
argument_list|(
name|servicePath
argument_list|,
name|ttl
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Watch (path={}, isLeader={}, index={})"
argument_list|,
name|servicePath
argument_list|,
name|leader
operator|.
name|get
argument_list|()
argument_list|,
name|index
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|get
argument_list|(
name|servicePath
argument_list|)
operator|.
name|waitForChange
argument_list|(
name|index
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|timeout
argument_list|(
name|watchTimeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|addListener
argument_list|(
name|this
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
DECL|method|tryTakeLeadership ()
specifier|private
name|boolean
name|tryTakeLeadership
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|result
init|=
literal|false
decl_stmt|;
try|try
block|{
name|EtcdKeysResponse
name|response
init|=
name|getClient
argument_list|()
operator|.
name|put
argument_list|(
name|servicePath
argument_list|,
name|serviceName
argument_list|)
operator|.
name|prevExist
argument_list|(
literal|false
argument_list|)
operator|.
name|ttl
argument_list|(
name|ttl
argument_list|)
operator|.
name|send
argument_list|()
operator|.
name|get
argument_list|()
decl_stmt|;
name|result
operator|=
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|serviceName
argument_list|,
name|response
operator|.
name|node
operator|.
name|value
argument_list|)
expr_stmt|;
name|EtcdHelper
operator|.
name|setIndex
argument_list|(
name|index
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|EtcdException
name|e
parameter_list|)
block|{
if|if
condition|(
operator|!
name|e
operator|.
name|isErrorCode
argument_list|(
name|EtcdErrorCode
operator|.
name|NodeExist
argument_list|)
condition|)
block|{
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

