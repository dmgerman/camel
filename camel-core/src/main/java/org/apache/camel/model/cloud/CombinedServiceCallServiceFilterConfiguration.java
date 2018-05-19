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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"routing,cloud,service-filter"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"combinedServiceFilter"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CombinedServiceCallServiceFilterConfiguration
specifier|public
class|class
name|CombinedServiceCallServiceFilterConfiguration
extends|extends
name|ServiceCallServiceFilterConfiguration
block|{
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
DECL|field|serviceFilterConfigurations
specifier|private
name|List
argument_list|<
name|ServiceCallServiceFilterConfiguration
argument_list|>
name|serviceFilterConfigurations
decl_stmt|;
DECL|method|CombinedServiceCallServiceFilterConfiguration ()
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|CombinedServiceCallServiceFilterConfiguration (ServiceCallDefinition parent)
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"combined-service-filter"
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getServiceFilterConfigurations ()
specifier|public
name|List
argument_list|<
name|ServiceCallServiceFilterConfiguration
argument_list|>
name|getServiceFilterConfigurations
parameter_list|()
block|{
return|return
name|serviceFilterConfigurations
return|;
block|}
comment|/**      * List of ServiceFilter configuration to use      * @param serviceFilterConfigurations      */
DECL|method|setServiceFilterConfigurations (List<ServiceCallServiceFilterConfiguration> serviceFilterConfigurations)
specifier|public
name|void
name|setServiceFilterConfigurations
parameter_list|(
name|List
argument_list|<
name|ServiceCallServiceFilterConfiguration
argument_list|>
name|serviceFilterConfigurations
parameter_list|)
block|{
name|this
operator|.
name|serviceFilterConfigurations
operator|=
name|serviceFilterConfigurations
expr_stmt|;
block|}
comment|/**      *  Add a ServiceFilter configuration      */
DECL|method|addServiceFilterConfiguration (ServiceCallServiceFilterConfiguration serviceFilterConfiguration)
specifier|public
name|void
name|addServiceFilterConfiguration
parameter_list|(
name|ServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
parameter_list|)
block|{
if|if
condition|(
name|serviceFilterConfigurations
operator|==
literal|null
condition|)
block|{
name|serviceFilterConfigurations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|serviceFilterConfigurations
operator|.
name|add
argument_list|(
name|serviceFilterConfiguration
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      *  List of ServiceFilter configuration to use      */
DECL|method|serviceFilterConfigurations (List<ServiceCallServiceFilterConfiguration> serviceFilterConfigurations)
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|serviceFilterConfigurations
parameter_list|(
name|List
argument_list|<
name|ServiceCallServiceFilterConfiguration
argument_list|>
name|serviceFilterConfigurations
parameter_list|)
block|{
name|setServiceFilterConfigurations
argument_list|(
name|serviceFilterConfigurations
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      *  Add a ServiceFilter configuration      */
DECL|method|serviceFilterConfiguration (ServiceCallServiceFilterConfiguration serviceFilterConfiguration)
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
parameter_list|(
name|ServiceCallServiceFilterConfiguration
name|serviceFilterConfiguration
parameter_list|)
block|{
name|addServiceFilterConfiguration
argument_list|(
name|serviceFilterConfiguration
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// *****************************
comment|// Shortcuts - ServiceFilter
comment|// *****************************
DECL|method|healthy ()
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|healthy
parameter_list|()
block|{
name|addServiceFilterConfiguration
argument_list|(
operator|new
name|HealthyServiceCallServiceFilterConfiguration
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|passThrough ()
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|passThrough
parameter_list|()
block|{
name|addServiceFilterConfiguration
argument_list|(
operator|new
name|PassThroughServiceCallServiceFilterConfiguration
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|custom (String serviceFilter)
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|custom
parameter_list|(
name|String
name|serviceFilter
parameter_list|)
block|{
name|addServiceFilterConfiguration
argument_list|(
operator|new
name|CustomServiceCallServiceFilterConfiguration
argument_list|()
operator|.
name|serviceFilter
argument_list|(
name|serviceFilter
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|custom (ServiceFilter serviceFilter)
specifier|public
name|CombinedServiceCallServiceFilterConfiguration
name|custom
parameter_list|(
name|ServiceFilter
name|serviceFilter
parameter_list|)
block|{
name|addServiceFilterConfiguration
argument_list|(
operator|new
name|CustomServiceCallServiceFilterConfiguration
argument_list|()
operator|.
name|serviceFilter
argument_list|(
name|serviceFilter
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// *************************************************************************
comment|// Utilities
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|postProcessFactoryParameters (final CamelContext camelContext, final Map<String, Object> parameters)
specifier|protected
name|void
name|postProcessFactoryParameters
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|serviceFilterConfigurations
operator|!=
literal|null
operator|&&
operator|!
name|serviceFilterConfigurations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|ServiceFilter
argument_list|>
name|discoveries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|serviceFilterConfigurations
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ServiceCallServiceFilterConfiguration
name|conf
range|:
name|serviceFilterConfigurations
control|)
block|{
name|discoveries
operator|.
name|add
argument_list|(
name|conf
operator|.
name|newInstance
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|parameters
operator|.
name|put
argument_list|(
literal|"serviceFilterList"
argument_list|,
name|discoveries
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

