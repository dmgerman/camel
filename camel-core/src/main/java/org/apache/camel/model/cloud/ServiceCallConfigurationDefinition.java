begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElements
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|ExchangePattern
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
name|Expression
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
name|model
operator|.
name|IdentifiedType
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Remote service call configuration  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"routing,cloud"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"serviceCallConfiguration"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ServiceCallConfigurationDefinition
specifier|public
class|class
name|ServiceCallConfigurationDefinition
extends|extends
name|IdentifiedType
block|{
annotation|@
name|XmlAttribute
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"http"
argument_list|)
DECL|field|component
specifier|private
name|String
name|component
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|pattern
specifier|private
name|ExchangePattern
name|pattern
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceDiscoveryRef
specifier|private
name|String
name|serviceDiscoveryRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|serviceDiscovery
specifier|private
name|ServiceDiscovery
name|serviceDiscovery
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceFilterRef
specifier|private
name|String
name|serviceFilterRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|serviceFilter
specifier|private
name|ServiceFilter
name|serviceFilter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|serviceChooserRef
specifier|private
name|String
name|serviceChooserRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|serviceChooser
specifier|private
name|ServiceChooser
name|serviceChooser
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|loadBalancerRef
specifier|private
name|String
name|loadBalancerRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|loadBalancer
specifier|private
name|LoadBalancer
name|loadBalancer
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|expressionRef
specifier|private
name|String
name|expressionRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"cachingServiceDiscovery"
argument_list|,
name|type
operator|=
name|CachingServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"chainedServiceDiscovery"
argument_list|,
name|type
operator|=
name|ChainedServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"consulServiceDiscovery"
argument_list|,
name|type
operator|=
name|ConsulServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"dnsServiceDiscovery"
argument_list|,
name|type
operator|=
name|DnsServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"etcdServiceDiscovery"
argument_list|,
name|type
operator|=
name|EtcdServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"kubernetesServiceDiscovery"
argument_list|,
name|type
operator|=
name|KubernetesServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"staticServiceDiscovery"
argument_list|,
name|type
operator|=
name|StaticServiceCallServiceDiscoveryConfiguration
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|serviceDiscoveryConfiguration
specifier|private
name|ServiceCallServiceDiscoveryConfiguration
name|serviceDiscoveryConfiguration
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"blacklistServiceFilter"
argument_list|,
name|type
operator|=
name|BlacklistServiceCallServiceFilterConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"chainedServiceFilter"
argument_list|,
name|type
operator|=
name|ChainedServiceCallServiceFilterConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"customServiceFilter"
argument_list|,
name|type
operator|=
name|CustomServiceCallServiceFilterConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"healthyServiceFilter"
argument_list|,
name|type
operator|=
name|HealthyServiceCallServiceFilterConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"passThroughServiceFilter"
argument_list|,
name|type
operator|=
name|PassThroughServiceCallServiceFilterConfiguration
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|serviceFilterConfiguration
specifier|private
name|ServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"ribbonLoadBalancer"
argument_list|,
name|type
operator|=
name|RibbonServiceCallLoadBalancerConfiguration
operator|.
name|class
argument_list|)
block|,
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"defaultLoadBalancer"
argument_list|,
name|type
operator|=
name|DefaultServiceCallLoadBalancerConfiguration
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|loadBalancerConfiguration
specifier|private
name|ServiceCallLoadBalancerConfiguration
name|loadBalancerConfiguration
decl_stmt|;
annotation|@
name|XmlElements
argument_list|(
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"expression"
argument_list|,
name|type
operator|=
name|ServiceCallExpressionConfiguration
operator|.
name|class
argument_list|)
block|}
argument_list|)
DECL|field|expressionConfiguration
specifier|private
name|ServiceCallExpressionConfiguration
name|expressionConfiguration
decl_stmt|;
DECL|method|ServiceCallConfigurationDefinition ()
specifier|public
name|ServiceCallConfigurationDefinition
parameter_list|()
block|{     }
comment|// *****************************
comment|// Properties
comment|// *****************************
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
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
comment|/**      * The uri of the endpoint to send to.      * The uri can be dynamic computed using the {@link org.apache.camel.language.simple.SimpleLanguage} expression.      */
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
comment|/**      * The component to use.      */
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
DECL|method|getServiceDiscoveryRef ()
specifier|public
name|String
name|getServiceDiscoveryRef
parameter_list|()
block|{
return|return
name|serviceDiscoveryRef
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceDiscovery} to use.      */
DECL|method|setServiceDiscoveryRef (String serviceDiscoveryRef)
specifier|public
name|void
name|setServiceDiscoveryRef
parameter_list|(
name|String
name|serviceDiscoveryRef
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscoveryRef
operator|=
name|serviceDiscoveryRef
expr_stmt|;
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
comment|/**      * Sets a custom {@link ServiceDiscovery} to use.      */
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
DECL|method|getServiceFilterRef ()
specifier|public
name|String
name|getServiceFilterRef
parameter_list|()
block|{
return|return
name|serviceFilterRef
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceFilter} to use.      */
DECL|method|setServiceFilterRef (String serviceFilterRef)
specifier|public
name|void
name|setServiceFilterRef
parameter_list|(
name|String
name|serviceFilterRef
parameter_list|)
block|{
name|this
operator|.
name|serviceFilterRef
operator|=
name|serviceFilterRef
expr_stmt|;
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
comment|/**      * Sets a custom {@link ServiceFilter} to use.      */
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
DECL|method|getServiceChooserRef ()
specifier|public
name|String
name|getServiceChooserRef
parameter_list|()
block|{
return|return
name|serviceChooserRef
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceChooser} to use.      */
DECL|method|setServiceChooserRef (String serviceChooserRef)
specifier|public
name|void
name|setServiceChooserRef
parameter_list|(
name|String
name|serviceChooserRef
parameter_list|)
block|{
name|this
operator|.
name|serviceChooserRef
operator|=
name|serviceChooserRef
expr_stmt|;
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
comment|/**      * Sets a custom {@link ServiceChooser} to use.      */
DECL|method|setServiceChooser (ServiceChooser serviceChooser)
specifier|public
name|void
name|setServiceChooser
parameter_list|(
name|ServiceChooser
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
DECL|method|getLoadBalancerRef ()
specifier|public
name|String
name|getLoadBalancerRef
parameter_list|()
block|{
return|return
name|loadBalancerRef
return|;
block|}
comment|/**      * Sets a reference to a custom {@link LoadBalancer} to use.      */
DECL|method|setLoadBalancerRef (String loadBalancerRef)
specifier|public
name|void
name|setLoadBalancerRef
parameter_list|(
name|String
name|loadBalancerRef
parameter_list|)
block|{
name|this
operator|.
name|loadBalancerRef
operator|=
name|loadBalancerRef
expr_stmt|;
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
comment|/**      * Sets a custom {@link LoadBalancer} to use.      */
DECL|method|setLoadBalancer (LoadBalancer loadBalancer)
specifier|public
name|void
name|setLoadBalancer
parameter_list|(
name|LoadBalancer
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
DECL|method|getExpressionRef ()
specifier|public
name|String
name|getExpressionRef
parameter_list|()
block|{
return|return
name|expressionRef
return|;
block|}
comment|/**      * Set a reference to a custom {@link Expression} to use.      */
DECL|method|setExpressionRef (String expressionRef)
specifier|public
name|void
name|setExpressionRef
parameter_list|(
name|String
name|expressionRef
parameter_list|)
block|{
name|this
operator|.
name|expressionRef
operator|=
name|expressionRef
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
comment|/**      * Set a custom {@link Expression} to use.      */
DECL|method|setExpression (Expression expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|Expression
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
DECL|method|getServiceDiscoveryConfiguration ()
specifier|public
name|ServiceCallServiceDiscoveryConfiguration
name|getServiceDiscoveryConfiguration
parameter_list|()
block|{
return|return
name|serviceDiscoveryConfiguration
return|;
block|}
comment|/**      * Configures the ServiceDiscovery using the given configuration.      */
DECL|method|setServiceDiscoveryConfiguration (ServiceCallServiceDiscoveryConfiguration serviceDiscoveryConfiguration)
specifier|public
name|void
name|setServiceDiscoveryConfiguration
parameter_list|(
name|ServiceCallServiceDiscoveryConfiguration
name|serviceDiscoveryConfiguration
parameter_list|)
block|{
name|this
operator|.
name|serviceDiscoveryConfiguration
operator|=
name|serviceDiscoveryConfiguration
expr_stmt|;
block|}
DECL|method|getServiceFilterConfiguration ()
specifier|public
name|ServiceCallServiceFilterConfiguration
name|getServiceFilterConfiguration
parameter_list|()
block|{
return|return
name|serviceFilterConfiguration
return|;
block|}
comment|/**      * Configures the ServiceFilter using the given configuration.      */
DECL|method|setServiceFilterConfiguration (ServiceCallServiceFilterConfiguration serviceFilterConfiguration)
specifier|public
name|void
name|setServiceFilterConfiguration
parameter_list|(
name|ServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
parameter_list|)
block|{
name|this
operator|.
name|serviceFilterConfiguration
operator|=
name|serviceFilterConfiguration
expr_stmt|;
block|}
DECL|method|getLoadBalancerConfiguration ()
specifier|public
name|ServiceCallLoadBalancerConfiguration
name|getLoadBalancerConfiguration
parameter_list|()
block|{
return|return
name|loadBalancerConfiguration
return|;
block|}
comment|/**      * Configures theL oadBalancer using the given configuration.      */
DECL|method|setLoadBalancerConfiguration (ServiceCallLoadBalancerConfiguration loadBalancerConfiguration)
specifier|public
name|void
name|setLoadBalancerConfiguration
parameter_list|(
name|ServiceCallLoadBalancerConfiguration
name|loadBalancerConfiguration
parameter_list|)
block|{
name|this
operator|.
name|loadBalancerConfiguration
operator|=
name|loadBalancerConfiguration
expr_stmt|;
block|}
DECL|method|getExpressionConfiguration ()
specifier|public
name|ServiceCallExpressionConfiguration
name|getExpressionConfiguration
parameter_list|()
block|{
return|return
name|expressionConfiguration
return|;
block|}
comment|/**      * Configures the Expression using the given configuration.      */
DECL|method|setExpressionConfiguration (ServiceCallExpressionConfiguration expressionConfiguration)
specifier|public
name|void
name|setExpressionConfiguration
parameter_list|(
name|ServiceCallExpressionConfiguration
name|expressionConfiguration
parameter_list|)
block|{
name|this
operator|.
name|expressionConfiguration
operator|=
name|expressionConfiguration
expr_stmt|;
block|}
comment|// *****************************
comment|// Fluent API
comment|// *****************************
comment|/**      * Sets the default Camel component to use for calling the remote service.      *<p/>      * By default the http component is used. You can configure this to use<tt>netty4-http</tt>,<tt>jetty</tt>,      *<tt>restlet</tt> or some other components of choice. If the service is not HTTP protocol you can use other      * components such as<tt>mqtt</tt>,<tt>jms</tt>,<tt>amqp</tt> etc.      *<p/>      * If the service call has been configured using an uri, then the component from the uri is used instead      * of this default component.      */
DECL|method|component (String component)
specifier|public
name|ServiceCallConfigurationDefinition
name|component
parameter_list|(
name|String
name|component
parameter_list|)
block|{
name|setComponent
argument_list|(
name|component
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the uri of the service to use      */
DECL|method|uri (String uri)
specifier|public
name|ServiceCallConfigurationDefinition
name|uri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the optional {@link ExchangePattern} used to invoke this endpoint      */
DECL|method|pattern (ExchangePattern pattern)
specifier|public
name|ServiceCallConfigurationDefinition
name|pattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceDiscovery} to use.      */
DECL|method|serviceDiscovery (String serviceDiscoveryRef)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceDiscovery
parameter_list|(
name|String
name|serviceDiscoveryRef
parameter_list|)
block|{
name|setServiceDiscoveryRef
argument_list|(
name|serviceDiscoveryRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a custom {@link ServiceDiscovery} to use.      */
DECL|method|serviceDiscovery (ServiceDiscovery serviceDiscovery)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceDiscovery
parameter_list|(
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|)
block|{
name|setServiceDiscovery
argument_list|(
name|serviceDiscovery
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceFilter} to use.      */
DECL|method|serviceFilter (String serviceFilterRef)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceFilter
parameter_list|(
name|String
name|serviceFilterRef
parameter_list|)
block|{
name|setServiceDiscoveryRef
argument_list|(
name|serviceDiscoveryRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a custom {@link ServiceFilter} to use.      */
DECL|method|serviceFilter (ServiceFilter serviceFilter)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceFilter
parameter_list|(
name|ServiceFilter
name|serviceFilter
parameter_list|)
block|{
name|setServiceFilter
argument_list|(
name|serviceFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a custom {@link ServiceChooser} to use.      */
DECL|method|serviceChooser (String serviceChooserRef)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceChooser
parameter_list|(
name|String
name|serviceChooserRef
parameter_list|)
block|{
name|setServiceChooserRef
argument_list|(
name|serviceChooserRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a custom {@link ServiceChooser} to use.      */
DECL|method|serviceChooser (ServiceChooser serviceChooser)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceChooser
parameter_list|(
name|ServiceChooser
name|serviceChooser
parameter_list|)
block|{
name|setServiceChooser
argument_list|(
name|serviceChooser
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a custom {@link LoadBalancer} to use.      */
DECL|method|loadBalancer (String loadBalancerRef)
specifier|public
name|ServiceCallConfigurationDefinition
name|loadBalancer
parameter_list|(
name|String
name|loadBalancerRef
parameter_list|)
block|{
name|setLoadBalancerRef
argument_list|(
name|loadBalancerRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a custom {@link LoadBalancer} to use.      */
DECL|method|loadBalancer (LoadBalancer loadBalancer)
specifier|public
name|ServiceCallConfigurationDefinition
name|loadBalancer
parameter_list|(
name|LoadBalancer
name|loadBalancer
parameter_list|)
block|{
name|setLoadBalancer
argument_list|(
name|loadBalancer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a custom {@link Expression} to use.      */
DECL|method|expression (String expressionRef)
specifier|public
name|ServiceCallConfigurationDefinition
name|expression
parameter_list|(
name|String
name|expressionRef
parameter_list|)
block|{
name|setExpressionRef
argument_list|(
name|loadBalancerRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a custom {@link Expression} to use.      */
DECL|method|expression (Expression expression)
specifier|public
name|ServiceCallConfigurationDefinition
name|expression
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|setExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the ServiceDiscovery using the given configuration.      */
DECL|method|serviceDiscoveryConfiguration (ServiceCallServiceDiscoveryConfiguration serviceDiscoveryConfiguration)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceDiscoveryConfiguration
parameter_list|(
name|ServiceCallServiceDiscoveryConfiguration
name|serviceDiscoveryConfiguration
parameter_list|)
block|{
name|setServiceDiscoveryConfiguration
argument_list|(
name|serviceDiscoveryConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the ServiceFilter using the given configuration.      */
DECL|method|serviceFilterConfiguration (ServiceCallServiceFilterConfiguration serviceFilterConfiguration)
specifier|public
name|ServiceCallConfigurationDefinition
name|serviceFilterConfiguration
parameter_list|(
name|ServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
parameter_list|)
block|{
name|setServiceFilterConfiguration
argument_list|(
name|serviceFilterConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the LoadBalancer using the given configuration.      */
DECL|method|loadBalancerConfiguration (ServiceCallLoadBalancerConfiguration loadBalancerConfiguration)
specifier|public
name|ServiceCallConfigurationDefinition
name|loadBalancerConfiguration
parameter_list|(
name|ServiceCallLoadBalancerConfiguration
name|loadBalancerConfiguration
parameter_list|)
block|{
name|setLoadBalancerConfiguration
argument_list|(
name|loadBalancerConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Configures the Expression using the given configuration.      */
DECL|method|expressionConfiguration (ServiceCallExpressionConfiguration expressionConfiguration)
specifier|public
name|ServiceCallConfigurationDefinition
name|expressionConfiguration
parameter_list|(
name|ServiceCallExpressionConfiguration
name|expressionConfiguration
parameter_list|)
block|{
name|setExpressionConfiguration
argument_list|(
name|expressionConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// *****************************
comment|// Shortcuts - ServiceDiscovery
comment|// *****************************
DECL|method|cachingServiceDiscovery ()
specifier|public
name|CachingServiceCallServiceDiscoveryConfiguration
name|cachingServiceDiscovery
parameter_list|()
block|{
name|CachingServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|CachingServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|consulServiceDiscovery ()
specifier|public
name|ConsulServiceCallServiceDiscoveryConfiguration
name|consulServiceDiscovery
parameter_list|()
block|{
name|ConsulServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|ConsulServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|dnsServiceDiscovery ()
specifier|public
name|DnsServiceCallServiceDiscoveryConfiguration
name|dnsServiceDiscovery
parameter_list|()
block|{
name|DnsServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|DnsServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|dnsServiceDiscovery (String domain)
specifier|public
name|ServiceCallConfigurationDefinition
name|dnsServiceDiscovery
parameter_list|(
name|String
name|domain
parameter_list|)
block|{
name|DnsServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|DnsServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|dnsServiceDiscovery (String domain, String protocol)
specifier|public
name|ServiceCallConfigurationDefinition
name|dnsServiceDiscovery
parameter_list|(
name|String
name|domain
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
name|DnsServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|DnsServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setProto
argument_list|(
name|protocol
argument_list|)
expr_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|etcdServiceDiscovery ()
specifier|public
name|EtcdServiceCallServiceDiscoveryConfiguration
name|etcdServiceDiscovery
parameter_list|()
block|{
name|EtcdServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|EtcdServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|kubernetesServiceDiscovery ()
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|kubernetesServiceDiscovery
parameter_list|()
block|{
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|KubernetesServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|kubernetesClientServiceDiscovery ()
specifier|public
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|kubernetesClientServiceDiscovery
parameter_list|()
block|{
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|KubernetesServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setLookup
argument_list|(
literal|"client"
argument_list|)
expr_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|kubernetesEnvServiceDiscovery ()
specifier|public
name|ServiceCallConfigurationDefinition
name|kubernetesEnvServiceDiscovery
parameter_list|()
block|{
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|KubernetesServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setLookup
argument_list|(
literal|"environment"
argument_list|)
expr_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|kubernetesDnsServiceDiscovery (String namespace, String domain)
specifier|public
name|ServiceCallConfigurationDefinition
name|kubernetesDnsServiceDiscovery
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|domain
parameter_list|)
block|{
name|KubernetesServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|KubernetesServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setDnsDomain
argument_list|(
name|domain
argument_list|)
expr_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|multiServiceDiscovery ()
specifier|public
name|ChainedServiceCallServiceDiscoveryConfiguration
name|multiServiceDiscovery
parameter_list|()
block|{
name|ChainedServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|ChainedServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|staticServiceDiscovery ()
specifier|public
name|StaticServiceCallServiceDiscoveryConfiguration
name|staticServiceDiscovery
parameter_list|()
block|{
name|StaticServiceCallServiceDiscoveryConfiguration
name|conf
init|=
operator|new
name|StaticServiceCallServiceDiscoveryConfiguration
argument_list|()
decl_stmt|;
name|setServiceDiscoveryConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
comment|// *****************************
comment|// Shortcuts - ServiceFilter
comment|// *****************************
DECL|method|healthyFilter ()
specifier|public
name|ServiceCallConfigurationDefinition
name|healthyFilter
parameter_list|()
block|{
name|HealthyServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|HealthyServiceCallServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|setServiceFilterConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|passThroughFilter ()
specifier|public
name|ServiceCallConfigurationDefinition
name|passThroughFilter
parameter_list|()
block|{
name|PassThroughServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|PassThroughServiceCallServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|setServiceFilterConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|multiFilter ()
specifier|public
name|ChainedServiceCallServiceFilterConfiguration
name|multiFilter
parameter_list|()
block|{
name|ChainedServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|ChainedServiceCallServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|setServiceFilterConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|conf
return|;
block|}
DECL|method|customFilter (String serviceFilter)
specifier|public
name|ServiceCallConfigurationDefinition
name|customFilter
parameter_list|(
name|String
name|serviceFilter
parameter_list|)
block|{
name|CustomServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|CustomServiceCallServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceFilterRef
argument_list|(
name|serviceFilter
argument_list|)
expr_stmt|;
name|setServiceFilterConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|customFilter (ServiceFilter serviceFilter)
specifier|public
name|ServiceCallConfigurationDefinition
name|customFilter
parameter_list|(
name|ServiceFilter
name|serviceFilter
parameter_list|)
block|{
name|CustomServiceCallServiceFilterConfiguration
name|conf
init|=
operator|new
name|CustomServiceCallServiceFilterConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setServiceFilter
argument_list|(
name|serviceFilter
argument_list|)
expr_stmt|;
name|setServiceFilterConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// *****************************
comment|// Shortcuts - LoadBalancer
comment|// *****************************
DECL|method|defaultLoadBalancer ()
specifier|public
name|ServiceCallConfigurationDefinition
name|defaultLoadBalancer
parameter_list|()
block|{
name|DefaultServiceCallLoadBalancerConfiguration
name|conf
init|=
operator|new
name|DefaultServiceCallLoadBalancerConfiguration
argument_list|()
decl_stmt|;
name|setLoadBalancerConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|ribbonLoadBalancer ()
specifier|public
name|ServiceCallConfigurationDefinition
name|ribbonLoadBalancer
parameter_list|()
block|{
name|RibbonServiceCallLoadBalancerConfiguration
name|conf
init|=
operator|new
name|RibbonServiceCallLoadBalancerConfiguration
argument_list|()
decl_stmt|;
name|setLoadBalancerConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|ribbonLoadBalancer (String clientName)
specifier|public
name|ServiceCallConfigurationDefinition
name|ribbonLoadBalancer
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|RibbonServiceCallLoadBalancerConfiguration
name|conf
init|=
operator|new
name|RibbonServiceCallLoadBalancerConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setClientName
argument_list|(
name|clientName
argument_list|)
expr_stmt|;
name|setLoadBalancerConfiguration
argument_list|(
name|conf
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

