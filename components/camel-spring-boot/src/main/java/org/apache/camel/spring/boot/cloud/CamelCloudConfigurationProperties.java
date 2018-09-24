begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
operator|.
name|ServiceCallDefinitionConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.cloud"
argument_list|)
DECL|class|CamelCloudConfigurationProperties
specifier|public
class|class
name|CamelCloudConfigurationProperties
block|{
comment|/**      * Global option to enable/disable Camel cloud support, default is true.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|field|serviceCall
specifier|private
name|ServiceCall
name|serviceCall
init|=
operator|new
name|ServiceCall
argument_list|()
decl_stmt|;
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
init|=
operator|new
name|LoadBalancer
argument_list|()
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
name|ServiceDiscovery
name|serviceDiscovery
init|=
operator|new
name|ServiceDiscovery
argument_list|()
decl_stmt|;
DECL|field|serviceFilter
specifier|private
name|ServiceFilter
name|serviceFilter
init|=
operator|new
name|ServiceFilter
argument_list|()
decl_stmt|;
DECL|field|serviceChooser
specifier|private
name|ServiceChooser
name|serviceChooser
init|=
operator|new
name|ServiceChooser
argument_list|()
decl_stmt|;
DECL|field|serviceRegistry
specifier|private
name|ServiceRegistry
name|serviceRegistry
init|=
operator|new
name|ServiceRegistry
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getServiceCall ()
specifier|public
name|ServiceCall
name|getServiceCall
parameter_list|()
block|{
return|return
name|serviceCall
return|;
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|LoadBalancer
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
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
DECL|method|getServiceRegistry ()
specifier|public
name|ServiceRegistry
name|getServiceRegistry
parameter_list|()
block|{
return|return
name|serviceRegistry
return|;
block|}
comment|// *****************************************
comment|// Service Call
comment|// *****************************************
DECL|class|ServiceCall
specifier|public
class|class
name|ServiceCall
block|{
comment|/**          * The uri of the endpoint to send to.          * The uri can be dynamic computed using the simple language expression.          */
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
comment|/**          * The Camel component to use for calling the service. The default is http4 component.          */
DECL|field|component
specifier|private
name|String
name|component
init|=
name|ServiceCallDefinitionConstants
operator|.
name|DEFAULT_COMPONENT
decl_stmt|;
comment|/**          * A reference to the org.apache.camel.cloud.ServiceDiscovery to use.          */
DECL|field|serviceDiscovery
specifier|private
name|String
name|serviceDiscovery
decl_stmt|;
comment|/**          * A reference to the org.apache.camel.cloud.ServiceFilter to use.          */
DECL|field|serviceFilter
specifier|private
name|String
name|serviceFilter
decl_stmt|;
comment|/**          * A reference to the org.apache.camel.cloud.ServiceChooser to use.          */
DECL|field|serviceChooser
specifier|private
name|String
name|serviceChooser
decl_stmt|;
comment|/**          * A reference to the org.apache.camel.cloud.ServiceLoadBalancer to use.          */
DECL|field|loadBalancer
specifier|private
name|String
name|loadBalancer
decl_stmt|;
comment|/**          * Determine if the default load balancer should be used instead of any auto discovered one.          */
DECL|field|defaultLoadBalancer
specifier|private
name|boolean
name|defaultLoadBalancer
decl_stmt|;
comment|/**          * The expression to use.          */
DECL|field|expression
specifier|private
name|String
name|expression
decl_stmt|;
comment|/**          * The expression language to use, default is ref.          */
DECL|field|expressionLanguage
specifier|private
name|String
name|expressionLanguage
init|=
literal|"ref"
decl_stmt|;
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|setUri (String uri)
specifier|public
name|void
name|setUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
block|}
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
DECL|method|setComponent (String component)
specifier|public
name|void
name|setComponent
parameter_list|(
name|String
name|component
parameter_list|)
block|{
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
block|}
DECL|method|getServiceDiscovery ()
specifier|public
name|String
name|getServiceDiscovery
parameter_list|()
block|{
return|return
name|serviceDiscovery
return|;
block|}
DECL|method|setServiceDiscovery (String serviceDiscovery)
specifier|public
name|void
name|setServiceDiscovery
parameter_list|(
name|String
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
DECL|method|getServiceFilter ()
specifier|public
name|String
name|getServiceFilter
parameter_list|()
block|{
return|return
name|serviceFilter
return|;
block|}
DECL|method|setServiceFilter (String serviceFilter)
specifier|public
name|void
name|setServiceFilter
parameter_list|(
name|String
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
DECL|method|getServiceChooser ()
specifier|public
name|String
name|getServiceChooser
parameter_list|()
block|{
return|return
name|serviceChooser
return|;
block|}
DECL|method|setServiceChooser (String serviceChooser)
specifier|public
name|void
name|setServiceChooser
parameter_list|(
name|String
name|serviceChooser
parameter_list|)
block|{
name|this
operator|.
name|serviceChooser
operator|=
name|serviceChooser
expr_stmt|;
block|}
DECL|method|getLoadBalancer ()
specifier|public
name|String
name|getLoadBalancer
parameter_list|()
block|{
return|return
name|loadBalancer
return|;
block|}
DECL|method|setLoadBalancer (String loadBalancer)
specifier|public
name|void
name|setLoadBalancer
parameter_list|(
name|String
name|loadBalancer
parameter_list|)
block|{
name|this
operator|.
name|loadBalancer
operator|=
name|loadBalancer
expr_stmt|;
block|}
DECL|method|isDefaultLoadBalancer ()
specifier|public
name|boolean
name|isDefaultLoadBalancer
parameter_list|()
block|{
return|return
name|defaultLoadBalancer
return|;
block|}
DECL|method|setDefaultLoadBalancer (boolean defaultLoadBalancer)
specifier|public
name|void
name|setDefaultLoadBalancer
parameter_list|(
name|boolean
name|defaultLoadBalancer
parameter_list|)
block|{
name|this
operator|.
name|defaultLoadBalancer
operator|=
name|defaultLoadBalancer
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|String
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|setExpression (String expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getExpressionLanguage ()
specifier|public
name|String
name|getExpressionLanguage
parameter_list|()
block|{
return|return
name|expressionLanguage
return|;
block|}
DECL|method|setExpressionLanguage (String expressionLanguage)
specifier|public
name|void
name|setExpressionLanguage
parameter_list|(
name|String
name|expressionLanguage
parameter_list|)
block|{
name|this
operator|.
name|expressionLanguage
operator|=
name|expressionLanguage
expr_stmt|;
block|}
block|}
comment|// *****************************************
comment|// Load Balancer
comment|// *****************************************
DECL|class|LoadBalancer
specifier|public
specifier|static
class|class
name|LoadBalancer
block|{
comment|/**          * Global option to enable/disable Camel cloud load balancer, default is true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
block|}
comment|// *****************************************
comment|// Service Discovery
comment|// *****************************************
DECL|class|ServiceDiscoveryConfiguration
specifier|public
specifier|static
class|class
name|ServiceDiscoveryConfiguration
block|{
comment|/**          * Configure service discoveries.          */
DECL|field|services
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|services
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**          * Configure cache timeout (in millis).          */
DECL|field|cacheTimeout
specifier|private
name|String
name|cacheTimeout
decl_stmt|;
DECL|method|getServices ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getServices
parameter_list|()
block|{
return|return
name|services
return|;
block|}
DECL|method|getCacheTimeout ()
specifier|public
name|String
name|getCacheTimeout
parameter_list|()
block|{
return|return
name|cacheTimeout
return|;
block|}
DECL|method|setCacheTimeout (String cacheTimeout)
specifier|public
name|void
name|setCacheTimeout
parameter_list|(
name|String
name|cacheTimeout
parameter_list|)
block|{
name|this
operator|.
name|cacheTimeout
operator|=
name|cacheTimeout
expr_stmt|;
block|}
block|}
DECL|class|ServiceDiscovery
specifier|public
specifier|static
class|class
name|ServiceDiscovery
extends|extends
name|ServiceDiscoveryConfiguration
block|{
comment|/**          * Global option to enable/disable Camel cloud service discovery, default is true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
comment|/**          * Configure the service discovery rules.          */
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ServiceDiscoveryConfiguration
argument_list|>
name|configurations
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ServiceDiscoveryConfiguration
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
block|}
comment|// *****************************************
comment|// Service Filter
comment|// *****************************************
DECL|class|ServiceFilterConfiguration
specifier|public
specifier|static
class|class
name|ServiceFilterConfiguration
block|{
comment|/**          * Configure service filter blacklists.          */
DECL|field|blacklist
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|blacklist
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|getBlacklist ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|getBlacklist
parameter_list|()
block|{
return|return
name|blacklist
return|;
block|}
block|}
DECL|class|ServiceFilter
specifier|public
specifier|static
class|class
name|ServiceFilter
extends|extends
name|ServiceFilterConfiguration
block|{
comment|/**          * Global option to enable/disable Camel cloud service filter, default is true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
comment|/**          * Configure the service filtering rules.          */
DECL|field|configurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ServiceFilterConfiguration
argument_list|>
name|configurations
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getConfigurations ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ServiceFilterConfiguration
argument_list|>
name|getConfigurations
parameter_list|()
block|{
return|return
name|configurations
return|;
block|}
block|}
comment|// *****************************************
comment|// Service Chooser
comment|// *****************************************
DECL|class|ServiceChooser
specifier|public
specifier|static
class|class
name|ServiceChooser
block|{
comment|/**          * Global option to enable/disable Camel cloud service chooser, default is true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
block|}
comment|// *****************************************
comment|// Service Registry
comment|// *****************************************
DECL|class|ServiceRegistry
specifier|public
specifier|static
class|class
name|ServiceRegistry
block|{
comment|/**          * Configure if service registry should be enabled or not, default true.          */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
comment|/**          * Configure the service listening address.          */
DECL|field|serviceHost
specifier|private
name|String
name|serviceHost
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getServiceHost ()
specifier|public
name|String
name|getServiceHost
parameter_list|()
block|{
return|return
name|serviceHost
return|;
block|}
DECL|method|setServiceHost (String serviceHost)
specifier|public
name|void
name|setServiceHost
parameter_list|(
name|String
name|serviceHost
parameter_list|)
block|{
name|this
operator|.
name|serviceHost
operator|=
name|serviceHost
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

