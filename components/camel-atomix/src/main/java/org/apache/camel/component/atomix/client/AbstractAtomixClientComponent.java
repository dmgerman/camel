begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
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
name|io
operator|.
name|atomix
operator|.
name|AtomixClient
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|transport
operator|.
name|Address
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
name|impl
operator|.
name|DefaultComponent
import|;
end_import

begin_class
DECL|class|AbstractAtomixClientComponent
specifier|public
specifier|abstract
class|class
name|AbstractAtomixClientComponent
parameter_list|<
name|C
extends|extends
name|AtomixClientConfiguration
parameter_list|>
extends|extends
name|DefaultComponent
block|{
DECL|method|AbstractAtomixClientComponent ()
specifier|protected
name|AbstractAtomixClientComponent
parameter_list|()
block|{     }
DECL|method|AbstractAtomixClientComponent (CamelContext camelContext)
specifier|protected
name|AbstractAtomixClientComponent
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// *****************************************
comment|// Properties
comment|// *****************************************
DECL|method|getAtomix ()
specifier|public
name|AtomixClient
name|getAtomix
parameter_list|()
block|{
return|return
name|getComponentConfiguration
argument_list|()
operator|.
name|getAtomix
argument_list|()
return|;
block|}
comment|/**      * The shared AtomixClient instance      */
DECL|method|setAtomix (AtomixClient client)
specifier|public
name|void
name|setAtomix
parameter_list|(
name|AtomixClient
name|client
parameter_list|)
block|{
name|getComponentConfiguration
argument_list|()
operator|.
name|setAtomix
argument_list|(
name|client
argument_list|)
expr_stmt|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|Address
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|getComponentConfiguration
argument_list|()
operator|.
name|getNodes
argument_list|()
return|;
block|}
comment|/**      * The nodes the AtomixClient should connect to      */
DECL|method|setNodes (List<Address> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|List
argument_list|<
name|Address
argument_list|>
name|nodes
parameter_list|)
block|{
name|getComponentConfiguration
argument_list|()
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|setNodes (String nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|String
name|nodes
parameter_list|)
block|{
name|getComponentConfiguration
argument_list|()
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|getConfigurationUri ()
specifier|public
name|String
name|getConfigurationUri
parameter_list|()
block|{
return|return
name|getComponentConfiguration
argument_list|()
operator|.
name|getConfigurationUri
argument_list|()
return|;
block|}
comment|/**      * The path to the AtomixClient configuration      */
DECL|method|setConfigurationUri (String configurationUri)
specifier|public
name|void
name|setConfigurationUri
parameter_list|(
name|String
name|configurationUri
parameter_list|)
block|{
name|getComponentConfiguration
argument_list|()
operator|.
name|setConfigurationUri
argument_list|(
name|configurationUri
argument_list|)
expr_stmt|;
block|}
comment|// *****************************************
comment|// Properties
comment|// *****************************************
DECL|method|getComponentConfiguration ()
specifier|protected
specifier|abstract
name|C
name|getComponentConfiguration
parameter_list|()
function_decl|;
block|}
end_class

end_unit

