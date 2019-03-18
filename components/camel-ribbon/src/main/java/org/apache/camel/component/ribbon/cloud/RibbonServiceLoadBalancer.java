begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ribbon.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ribbon
operator|.
name|cloud
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
name|RejectedExecutionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|client
operator|.
name|config
operator|.
name|CommonClientConfigKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|client
operator|.
name|config
operator|.
name|IClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|client
operator|.
name|config
operator|.
name|IClientConfigKey
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|DummyPing
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|ILoadBalancer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|PollingServerListUpdater
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|RoundRobinRule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|ServerList
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|loadbalancer
operator|.
name|ZoneAwareLoadBalancer
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
name|cloud
operator|.
name|ServiceDefinition
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
name|cloud
operator|.
name|ServiceDiscovery
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
name|cloud
operator|.
name|ServiceDiscoveryAware
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
name|cloud
operator|.
name|ServiceFilter
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
name|cloud
operator|.
name|ServiceFilterAware
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
name|cloud
operator|.
name|ServiceLoadBalancer
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
name|cloud
operator|.
name|ServiceLoadBalancerFunction
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
name|ribbon
operator|.
name|RibbonConfiguration
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
name|ServiceHelper
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
DECL|class|RibbonServiceLoadBalancer
specifier|public
class|class
name|RibbonServiceLoadBalancer
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|ServiceDiscoveryAware
implements|,
name|ServiceFilterAware
implements|,
name|ServiceLoadBalancer
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
name|RibbonServiceLoadBalancer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|RibbonConfiguration
name|configuration
decl_stmt|;
DECL|field|loadBalancers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|ZoneAwareLoadBalancer
argument_list|<
name|RibbonServiceDefinition
argument_list|>
argument_list|>
name|loadBalancers
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
name|ServiceDiscovery
name|serviceDiscovery
decl_stmt|;
DECL|field|serviceFilter
specifier|private
name|ServiceFilter
name|serviceFilter
decl_stmt|;
DECL|method|RibbonServiceLoadBalancer (RibbonConfiguration configuration)
specifier|public
name|RibbonServiceLoadBalancer
parameter_list|(
name|RibbonConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|loadBalancers
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
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
annotation|@
name|Override
DECL|method|getServiceDiscovery ()
specifier|public
name|ServiceDiscovery
name|getServiceDiscovery
parameter_list|()
block|{
return|return
name|serviceDiscovery
return|;
block|}
annotation|@
name|Override
DECL|method|setServiceDiscovery (ServiceDiscovery serviceDiscovery)
specifier|public
name|void
name|setServiceDiscovery
parameter_list|(
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscovery
operator|=
name|serviceDiscovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServiceFilter ()
specifier|public
name|ServiceFilter
name|getServiceFilter
parameter_list|()
block|{
return|return
name|serviceFilter
return|;
block|}
annotation|@
name|Override
DECL|method|setServiceFilter (ServiceFilter serviceFilter)
specifier|public
name|void
name|setServiceFilter
parameter_list|(
name|ServiceFilter
name|serviceFilter
parameter_list|)
block|{
name|this
operator|.
name|serviceFilter
operator|=
name|serviceFilter
expr_stmt|;
block|}
comment|// ************************
comment|// lifecycle
comment|// ************************
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
name|configuration
argument_list|,
literal|"configuration"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camel context"
argument_list|)
expr_stmt|;
if|if
condition|(
name|serviceDiscovery
operator|!=
literal|null
condition|)
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"ServiceCall is using ribbon load balancer with service discovery type: {} and service filter: {}"
argument_list|,
name|serviceDiscovery
operator|.
name|getClass
argument_list|()
argument_list|,
name|serviceDiscovery
operator|!=
literal|null
condition|?
name|serviceFilter
operator|.
name|getClass
argument_list|()
else|:
literal|"none"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"ServiceCall is using ribbon load balancer"
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|serviceDiscovery
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
name|loadBalancers
operator|.
name|values
argument_list|()
operator|.
name|forEach
argument_list|(
name|ZoneAwareLoadBalancer
operator|::
name|stopServerListRefreshing
argument_list|)
expr_stmt|;
name|loadBalancers
operator|.
name|clear
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|serviceDiscovery
argument_list|)
expr_stmt|;
block|}
comment|// ************************
comment|// Processor
comment|// ************************
annotation|@
name|Override
DECL|method|process (String serviceName, ServiceLoadBalancerFunction<T> request)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|process
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|ServiceLoadBalancerFunction
argument_list|<
name|T
argument_list|>
name|request
parameter_list|)
throws|throws
name|Exception
block|{
name|ILoadBalancer
name|loadBalancer
init|=
name|loadBalancers
operator|.
name|computeIfAbsent
argument_list|(
name|serviceName
argument_list|,
name|key
lambda|->
name|createLoadBalancer
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|Server
name|server
init|=
name|loadBalancer
operator|.
name|chooseServer
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|server
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|(
literal|"No active services with name "
operator|+
name|serviceName
argument_list|)
throw|;
block|}
name|ServiceDefinition
name|definition
decl_stmt|;
if|if
condition|(
name|server
operator|instanceof
name|ServiceDefinition
condition|)
block|{
comment|// If the service discovery is one of camel provides, the definition
comment|// is already of the expected type.
name|definition
operator|=
operator|(
name|ServiceDefinition
operator|)
name|server
expr_stmt|;
block|}
else|else
block|{
comment|// If ribbon server list is configured through client config properties
comment|// i.e. with listOfServers property the instance provided by the load
comment|// balancer is of type Server so a conversion is needed
name|definition
operator|=
operator|new
name|RibbonServiceDefinition
argument_list|(
name|serviceName
argument_list|,
name|server
operator|.
name|getHost
argument_list|()
argument_list|,
name|server
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|zone
init|=
name|server
operator|.
name|getZone
argument_list|()
decl_stmt|;
if|if
condition|(
name|zone
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|setZone
argument_list|(
name|zone
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|request
operator|.
name|apply
argument_list|(
name|definition
argument_list|)
return|;
block|}
comment|// ************************
comment|// Helpers
comment|// ************************
DECL|method|createLoadBalancer (String serviceName)
specifier|private
name|ZoneAwareLoadBalancer
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|createLoadBalancer
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
comment|// setup client config
name|IClientConfig
name|config
init|=
name|configuration
operator|.
name|getClientName
argument_list|()
operator|!=
literal|null
condition|?
name|IClientConfig
operator|.
name|Builder
operator|.
name|newBuilder
argument_list|(
name|configuration
operator|.
name|getClientName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
else|:
name|IClientConfig
operator|.
name|Builder
operator|.
name|newBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|configuration
operator|.
name|getProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|IClientConfigKey
name|key
init|=
name|CommonClientConfigKey
operator|.
name|valueOf
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"RibbonClientConfig: {}={}"
argument_list|,
name|key
operator|.
name|key
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|config
operator|.
name|set
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|ZoneAwareLoadBalancer
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|loadBalancer
decl_stmt|;
if|if
condition|(
name|serviceDiscovery
operator|!=
literal|null
condition|)
block|{
name|loadBalancer
operator|=
operator|new
name|ZoneAwareLoadBalancer
argument_list|<>
argument_list|(
name|config
argument_list|,
name|configuration
operator|.
name|getRuleOrDefault
argument_list|(
name|RoundRobinRule
operator|::
operator|new
argument_list|)
argument_list|,
name|configuration
operator|.
name|getPingOrDefault
argument_list|(
name|DummyPing
operator|::
operator|new
argument_list|)
argument_list|,
operator|new
name|RibbonServerList
argument_list|(
name|serviceName
argument_list|,
name|serviceDiscovery
argument_list|,
name|serviceFilter
argument_list|)
argument_list|,
literal|null
argument_list|,
operator|new
name|PollingServerListUpdater
argument_list|(
name|config
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|loadBalancer
operator|=
operator|new
name|ZoneAwareLoadBalancer
argument_list|<>
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
return|return
name|loadBalancer
return|;
block|}
DECL|class|RibbonServerList
specifier|static
specifier|final
class|class
name|RibbonServerList
implements|implements
name|ServerList
argument_list|<
name|RibbonServiceDefinition
argument_list|>
block|{
DECL|field|serviceName
specifier|private
specifier|final
name|String
name|serviceName
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
specifier|final
name|ServiceDiscovery
name|serviceDiscovery
decl_stmt|;
DECL|field|serviceFilter
specifier|private
specifier|final
name|ServiceFilter
name|serviceFilter
decl_stmt|;
DECL|method|RibbonServerList (String serviceName, ServiceDiscovery serviceDiscovery, ServiceFilter serviceFilter)
name|RibbonServerList
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|,
name|ServiceFilter
name|serviceFilter
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
name|serviceDiscovery
operator|=
name|serviceDiscovery
expr_stmt|;
name|this
operator|.
name|serviceFilter
operator|=
name|serviceFilter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getInitialListOfServers ()
specifier|public
name|List
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|getInitialListOfServers
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|serviceDiscovery
operator|.
name|getServices
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceFilter
operator|!=
literal|null
condition|)
block|{
name|services
operator|=
name|serviceFilter
operator|.
name|apply
argument_list|(
name|services
argument_list|)
expr_stmt|;
block|}
return|return
name|asRibbonServerList
argument_list|(
name|services
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getUpdatedListOfServers ()
specifier|public
name|List
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|getUpdatedListOfServers
parameter_list|()
block|{
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|serviceDiscovery
operator|.
name|getServices
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceFilter
operator|!=
literal|null
condition|)
block|{
name|services
operator|=
name|serviceFilter
operator|.
name|apply
argument_list|(
name|services
argument_list|)
expr_stmt|;
block|}
return|return
name|asRibbonServerList
argument_list|(
name|services
argument_list|)
return|;
block|}
DECL|method|asRibbonServerList (List<ServiceDefinition> services)
specifier|private
name|List
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|asRibbonServerList
parameter_list|(
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
parameter_list|)
block|{
name|List
argument_list|<
name|RibbonServiceDefinition
argument_list|>
name|ribbonServers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|ServiceDefinition
name|service
range|:
name|services
control|)
block|{
if|if
condition|(
name|service
operator|instanceof
name|RibbonServiceDefinition
condition|)
block|{
name|ribbonServers
operator|.
name|add
argument_list|(
operator|(
name|RibbonServiceDefinition
operator|)
name|service
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|RibbonServiceDefinition
name|serviceDef
init|=
operator|new
name|RibbonServiceDefinition
argument_list|(
name|serviceName
argument_list|,
name|service
operator|.
name|getHost
argument_list|()
argument_list|,
name|service
operator|.
name|getPort
argument_list|()
argument_list|,
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|,
name|service
operator|.
name|getHealth
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|zone
init|=
name|serviceDef
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|"zone"
argument_list|)
decl_stmt|;
if|if
condition|(
name|zone
operator|!=
literal|null
condition|)
block|{
name|serviceDef
operator|.
name|setZone
argument_list|(
name|zone
argument_list|)
expr_stmt|;
block|}
name|ribbonServers
operator|.
name|add
argument_list|(
name|serviceDef
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|ribbonServers
return|;
block|}
block|}
block|}
end_class

end_unit

