begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|List
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
name|LoadBalancer
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
name|LoadBalancerFunction
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
name|ServiceChooser
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
name|ServiceChooserAware
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
name|support
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
DECL|class|DefaultLoadBalancer
specifier|public
class|class
name|DefaultLoadBalancer
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|ServiceDiscoveryAware
implements|,
name|ServiceChooserAware
implements|,
name|ServiceFilterAware
implements|,
name|LoadBalancer
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
name|DefaultLoadBalancer
operator|.
name|class
argument_list|)
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
DECL|field|serviceChooser
specifier|private
name|ServiceChooser
name|serviceChooser
decl_stmt|;
DECL|field|serviceFilter
specifier|private
name|ServiceFilter
name|serviceFilter
decl_stmt|;
DECL|method|DefaultLoadBalancer ()
specifier|public
name|DefaultLoadBalancer
parameter_list|()
block|{     }
comment|// *************************************
comment|// Bean
comment|// *************************************
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
DECL|method|setServiceDiscovery (ServiceDiscovery serverDiscovery)
specifier|public
name|void
name|setServiceDiscovery
parameter_list|(
name|ServiceDiscovery
name|serverDiscovery
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscovery
operator|=
name|serverDiscovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getServiceChooser ()
specifier|public
name|ServiceChooser
name|getServiceChooser
parameter_list|()
block|{
return|return
name|serviceChooser
return|;
block|}
annotation|@
name|Override
DECL|method|setServiceChooser (ServiceChooser serverChooser)
specifier|public
name|void
name|setServiceChooser
parameter_list|(
name|ServiceChooser
name|serverChooser
parameter_list|)
block|{
name|this
operator|.
name|serviceChooser
operator|=
name|serverChooser
expr_stmt|;
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
comment|// *************************************
comment|// Lifecycle
comment|// *************************************
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
literal|"camel context"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceDiscovery
argument_list|,
literal|"service discovery"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceChooser
argument_list|,
literal|"service chooser"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|serviceFilter
argument_list|,
literal|"service serviceFilter"
argument_list|)
expr_stmt|;
name|LOGGER
operator|.
name|info
argument_list|(
literal|"ServiceCall is using default load balancer with service discovery type: {}, service filter type: {} and service chooser type: {}"
argument_list|,
name|serviceDiscovery
operator|.
name|getClass
argument_list|()
argument_list|,
name|serviceFilter
operator|.
name|getClass
argument_list|()
argument_list|,
name|serviceChooser
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|serviceChooser
argument_list|)
expr_stmt|;
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
comment|// Stop services if needed
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|serviceDiscovery
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|serviceChooser
argument_list|)
expr_stmt|;
block|}
comment|// *************************************
comment|// Load Balancer
comment|// *************************************
annotation|@
name|Override
DECL|method|process (String serviceName, LoadBalancerFunction<T> function)
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
name|LoadBalancerFunction
argument_list|<
name|T
argument_list|>
name|function
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceDefinition
name|service
decl_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|serviceDiscovery
operator|.
name|getUpdatedListOfServices
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
if|if
condition|(
name|services
operator|==
literal|null
operator|||
name|services
operator|.
name|isEmpty
argument_list|()
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
else|else
block|{
comment|// filter services
name|services
operator|=
name|serviceFilter
operator|.
name|apply
argument_list|(
name|services
argument_list|)
expr_stmt|;
comment|// let the client service chooser find which server to use
name|service
operator|=
name|services
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|?
name|serviceChooser
operator|.
name|choose
argument_list|(
name|services
argument_list|)
else|:
name|services
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
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
block|}
return|return
name|function
operator|.
name|apply
argument_list|(
name|service
argument_list|)
return|;
block|}
block|}
end_class

end_unit

