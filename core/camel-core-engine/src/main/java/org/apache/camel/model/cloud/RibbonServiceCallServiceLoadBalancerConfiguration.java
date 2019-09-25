begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
literal|"routing,cloud,load-balancing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"ribbonLoadBalancer"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RibbonServiceCallServiceLoadBalancerConfiguration
specifier|public
class|class
name|RibbonServiceCallServiceLoadBalancerConfiguration
extends|extends
name|ServiceCallServiceLoadBalancerConfiguration
block|{
annotation|@
name|XmlAttribute
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|clientName
specifier|private
name|String
name|clientName
decl_stmt|;
DECL|method|RibbonServiceCallServiceLoadBalancerConfiguration ()
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|RibbonServiceCallServiceLoadBalancerConfiguration (ServiceCallDefinition parent)
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"ribbon-service-load-balancer"
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
comment|/**      * The namespace      */
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
comment|/**      * The username      */
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * The password      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getClientName ()
specifier|public
name|String
name|getClientName
parameter_list|()
block|{
return|return
name|clientName
return|;
block|}
comment|/**      * Sets the Ribbon client name      */
DECL|method|setClientName (String clientName)
specifier|public
name|void
name|setClientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|this
operator|.
name|clientName
operator|=
name|clientName
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * Sets the namespace      */
DECL|method|namespace (String namespace)
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
name|namespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the username      */
DECL|method|username (String username)
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the password      */
DECL|method|password (String password)
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the Ribbon client name      */
DECL|method|clientName (String clientName)
specifier|public
name|RibbonServiceCallServiceLoadBalancerConfiguration
name|clientName
parameter_list|(
name|String
name|clientName
parameter_list|)
block|{
name|setClientName
argument_list|(
name|clientName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

