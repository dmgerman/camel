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
literal|"customServiceFilter"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CustomServiceCallServiceFilterConfiguration
specifier|public
class|class
name|CustomServiceCallServiceFilterConfiguration
extends|extends
name|ServiceCallServiceFilterConfiguration
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|name
operator|=
literal|"ref"
argument_list|)
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
DECL|method|CustomServiceCallServiceFilterConfiguration ()
specifier|public
name|CustomServiceCallServiceFilterConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|CustomServiceCallServiceFilterConfiguration (ServiceCallDefinition parent)
specifier|public
name|CustomServiceCallServiceFilterConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"custom-service-filter"
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
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
comment|/**      * Reference of a ServiceFilter      */
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
comment|/**      * Set the ServiceFilter      */
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
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * Reference of a ServiceFilter      */
DECL|method|serviceFilter (String serviceFilter)
specifier|public
name|CustomServiceCallServiceFilterConfiguration
name|serviceFilter
parameter_list|(
name|String
name|serviceFilter
parameter_list|)
block|{
name|setServiceFilterRef
argument_list|(
name|serviceFilter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Set the ServiceFilter      */
DECL|method|serviceFilter (ServiceFilter serviceFilter)
specifier|public
name|CustomServiceCallServiceFilterConfiguration
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
comment|// *************************************************************************
comment|// Factory
comment|// *************************************************************************
annotation|@
name|Override
DECL|method|newInstance (CamelContext camelContext)
specifier|public
name|ServiceFilter
name|newInstance
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceFilter
name|answer
init|=
name|serviceFilter
decl_stmt|;
if|if
condition|(
name|serviceFilterRef
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|serviceFilterRef
argument_list|,
name|ServiceFilter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

