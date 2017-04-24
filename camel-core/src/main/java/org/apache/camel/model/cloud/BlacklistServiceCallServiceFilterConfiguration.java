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
name|List
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
literal|"routing,cloud,service-filter"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"blacklistServiceFilter"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BlacklistServiceCallServiceFilterConfiguration
specifier|public
class|class
name|BlacklistServiceCallServiceFilterConfiguration
extends|extends
name|ServiceCallServiceFilterConfiguration
block|{
annotation|@
name|XmlElement
DECL|field|servers
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|servers
decl_stmt|;
DECL|method|BlacklistServiceCallServiceFilterConfiguration ()
specifier|public
name|BlacklistServiceCallServiceFilterConfiguration
parameter_list|()
block|{
name|this
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|BlacklistServiceCallServiceFilterConfiguration (ServiceCallDefinition parent)
specifier|public
name|BlacklistServiceCallServiceFilterConfiguration
parameter_list|(
name|ServiceCallDefinition
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|"blacklist-service-filter"
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Properties
comment|// *************************************************************************
DECL|method|getServers ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getServers
parameter_list|()
block|{
return|return
name|servers
return|;
block|}
comment|/**      * Sets the server list;      */
DECL|method|setServers (List<String> servers)
specifier|public
name|void
name|setServers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|servers
parameter_list|)
block|{
name|this
operator|.
name|servers
operator|=
name|servers
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Fluent API
comment|// *************************************************************************
comment|/**      * Sets the server list;      */
DECL|method|servers (List<String> servers)
specifier|public
name|BlacklistServiceCallServiceFilterConfiguration
name|servers
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|servers
parameter_list|)
block|{
name|setServers
argument_list|(
name|servers
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

