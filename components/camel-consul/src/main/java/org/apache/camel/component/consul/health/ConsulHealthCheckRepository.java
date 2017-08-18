begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.health
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
name|health
package|;
end_package

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
name|HashSet
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
name|function
operator|.
name|Supplier
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
name|Stream
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckConfiguration
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
name|health
operator|.
name|HealthCheckRepository
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
name|health
operator|.
name|HealthCheckResultBuilder
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
name|health
operator|.
name|AbstractHealthCheck
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
name|function
operator|.
name|Suppliers
import|;
end_import

begin_class
DECL|class|ConsulHealthCheckRepository
specifier|public
class|class
name|ConsulHealthCheckRepository
implements|implements
name|HealthCheckRepository
implements|,
name|CamelContextAware
block|{
DECL|field|client
specifier|private
specifier|final
name|Supplier
argument_list|<
name|Consul
argument_list|>
name|client
decl_stmt|;
DECL|field|checks
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|HealthCheck
argument_list|>
name|checks
decl_stmt|;
DECL|field|configuration
specifier|private
name|ConsulHealthCheckRepositoryConfiguration
name|configuration
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|ConsulHealthCheckRepository ()
specifier|public
name|ConsulHealthCheckRepository
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsulHealthCheckRepository (ConsulHealthCheckRepositoryConfiguration configuration)
specifier|private
name|ConsulHealthCheckRepository
parameter_list|(
name|ConsulHealthCheckRepositoryConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|checks
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|Suppliers
operator|.
name|memorize
argument_list|(
name|this
operator|::
name|createConsul
argument_list|,
name|ObjectHelper
operator|::
name|wrapRuntimeCamelException
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|//
comment|// *************************************
DECL|method|getConfiguration ()
specifier|public
name|ConsulHealthCheckRepositoryConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (ConsulHealthCheckRepositoryConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ConsulHealthCheckRepositoryConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
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
DECL|method|stream ()
specifier|public
name|Stream
argument_list|<
name|HealthCheck
argument_list|>
name|stream
parameter_list|()
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|configuration
operator|.
name|getChecks
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ids
argument_list|)
condition|)
block|{
return|return
name|ids
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|checkId
lambda|->
name|checks
operator|.
name|computeIfAbsent
argument_list|(
name|checkId
argument_list|,
name|ConsulHealthCheck
operator|::
operator|new
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|check
lambda|->
name|check
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
return|;
block|}
return|return
name|Stream
operator|.
name|empty
argument_list|()
return|;
block|}
comment|// *************************************
comment|//
comment|// *************************************
DECL|method|createConsul ()
specifier|private
name|Consul
name|createConsul
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsulHealthCheckRepositoryConfiguration
name|conf
init|=
name|configuration
decl_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
name|conf
operator|=
operator|new
name|ConsulHealthCheckRepositoryConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|conf
operator|.
name|createConsulClient
argument_list|(
name|camelContext
argument_list|)
return|;
block|}
DECL|class|ConsulHealthCheck
specifier|private
class|class
name|ConsulHealthCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|field|checkId
specifier|private
specifier|final
name|String
name|checkId
decl_stmt|;
DECL|method|ConsulHealthCheck (String checkId)
name|ConsulHealthCheck
parameter_list|(
name|String
name|checkId
parameter_list|)
block|{
name|super
argument_list|(
literal|"consul-"
operator|+
name|checkId
operator|.
name|replaceAll
argument_list|(
literal|":"
argument_list|,
literal|"-"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkId
operator|=
name|checkId
expr_stmt|;
name|HealthCheckConfiguration
name|conf
init|=
name|configuration
operator|.
name|getConfigurations
argument_list|()
operator|.
name|get
argument_list|(
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|conf
operator|==
literal|null
condition|)
block|{
name|conf
operator|=
name|HealthCheckConfiguration
operator|.
name|builder
argument_list|()
operator|.
name|complete
argument_list|(
name|configuration
operator|.
name|getDefaultConfiguration
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|setConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCall (HealthCheckResultBuilder builder, Map<String, Object> options)
specifier|protected
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|builder
operator|.
name|unknown
argument_list|()
expr_stmt|;
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|model
operator|.
name|health
operator|.
name|HealthCheck
name|check
init|=
name|client
operator|.
name|get
argument_list|()
operator|.
name|agentClient
argument_list|()
operator|.
name|getChecks
argument_list|()
operator|.
name|get
argument_list|(
name|checkId
argument_list|)
decl_stmt|;
if|if
condition|(
name|check
operator|!=
literal|null
condition|)
block|{
comment|// From Consul sources:
comment|// https://github.com/hashicorp/consul/blob/master/api/health.go#L8-L16
comment|//
comment|// States are:
comment|//
comment|// const (
comment|//     HealthAny is special, and is used as a wild card,
comment|//     not as a specific state.
comment|//     HealthAny      = "any"
comment|//     HealthPassing  = "passing"
comment|//     HealthWarning  = "warning"
comment|//     HealthCritical = "critical"
comment|//     HealthMaint    = "maintenance"
comment|// )
if|if
condition|(
name|ObjectHelper
operator|.
name|equalIgnoreCase
argument_list|(
name|check
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|"passing"
argument_list|)
condition|)
block|{
name|builder
operator|.
name|up
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|equalIgnoreCase
argument_list|(
name|check
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|"warning"
argument_list|)
condition|)
block|{
name|builder
operator|.
name|down
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|equalIgnoreCase
argument_list|(
name|check
operator|.
name|getStatus
argument_list|()
argument_list|,
literal|"critical"
argument_list|)
condition|)
block|{
name|builder
operator|.
name|down
argument_list|()
expr_stmt|;
block|}
name|builder
operator|.
name|detail
argument_list|(
literal|"consul.service.name"
argument_list|,
name|check
operator|.
name|getServiceName
argument_list|()
operator|.
name|orNull
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"consul.service.id"
argument_list|,
name|check
operator|.
name|getServiceId
argument_list|()
operator|.
name|orNull
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"consul.check.status"
argument_list|,
name|check
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"consul.check.id"
argument_list|,
name|check
operator|.
name|getCheckId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// ****************************************
comment|// Builder
comment|// ****************************************
DECL|class|Builder
specifier|public
specifier|static
specifier|final
class|class
name|Builder
implements|implements
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Builder
argument_list|<
name|ConsulHealthCheckRepository
argument_list|>
block|{
DECL|field|defaultConfiguration
specifier|private
name|HealthCheckConfiguration
name|defaultConfiguration
decl_stmt|;
DECL|field|checks
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|checks
decl_stmt|;
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|HealthCheckConfiguration
argument_list|>
name|configurations
decl_stmt|;
DECL|method|configuration (HealthCheckConfiguration defaultConfiguration)
specifier|public
name|Builder
name|configuration
parameter_list|(
name|HealthCheckConfiguration
name|defaultConfiguration
parameter_list|)
block|{
name|this
operator|.
name|defaultConfiguration
operator|=
name|defaultConfiguration
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|configuration (String id, HealthCheckConfiguration configuration)
specifier|public
name|Builder
name|configuration
parameter_list|(
name|String
name|id
parameter_list|,
name|HealthCheckConfiguration
name|configuration
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|configurations
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|configurations
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|configurations
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|configurations (Map<String, HealthCheckConfiguration> configurations)
specifier|public
name|Builder
name|configurations
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|HealthCheckConfiguration
argument_list|>
name|configurations
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|configurations
argument_list|)
condition|)
block|{
name|configurations
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|configuration
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|check (String id)
specifier|public
name|Builder
name|check
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|id
argument_list|)
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|checks
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|checks
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|checks
operator|.
name|add
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|checks (Collection<String> ids)
specifier|public
name|Builder
name|checks
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ids
parameter_list|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|ids
argument_list|)
condition|)
block|{
name|ids
operator|.
name|forEach
argument_list|(
name|this
operator|::
name|check
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|build ()
specifier|public
name|ConsulHealthCheckRepository
name|build
parameter_list|()
block|{
name|ConsulHealthCheckRepositoryConfiguration
name|configuration
init|=
operator|new
name|ConsulHealthCheckRepositoryConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setDefaultConfiguration
argument_list|(
name|defaultConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|checks
operator|!=
literal|null
condition|)
block|{
name|checks
operator|.
name|forEach
argument_list|(
name|configuration
operator|::
name|addCheck
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configurations
operator|!=
literal|null
condition|)
block|{
name|configurations
operator|.
name|forEach
argument_list|(
name|configuration
operator|::
name|addConfiguration
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ConsulHealthCheckRepository
argument_list|(
name|configuration
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

