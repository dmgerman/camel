begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.map
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
operator|.
name|map
package|;
end_package

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
name|Endpoint
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
name|atomix
operator|.
name|client
operator|.
name|AbstractAtomixClientComponent
import|;
end_import

begin_class
DECL|class|AtomixClientMapComponent
specifier|public
class|class
name|AtomixClientMapComponent
extends|extends
name|AbstractAtomixClientComponent
argument_list|<
name|AtomixClientMapConfiguration
argument_list|>
block|{
DECL|field|configuration
specifier|private
name|AtomixClientMapConfiguration
name|configuration
init|=
operator|new
name|AtomixClientMapConfiguration
argument_list|()
decl_stmt|;
DECL|method|AtomixClientMapComponent ()
specifier|public
name|AtomixClientMapComponent
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
DECL|method|AtomixClientMapComponent (CamelContext camelContext)
specifier|public
name|AtomixClientMapComponent
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
comment|// **********************************************
comment|// Endpoints
comment|// **********************************************
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
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
name|AtomixClientMapConfiguration
name|configuration
init|=
name|this
operator|.
name|configuration
operator|.
name|copy
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|AtomixClientMapEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|configuration
argument_list|,
name|remaining
argument_list|)
return|;
block|}
comment|// **********************************************
comment|// Properties
comment|// **********************************************
DECL|method|getConfiguration ()
specifier|public
name|AtomixClientMapConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|configuration
return|;
block|}
comment|/**      * The shared component configuration      */
DECL|method|setConfiguration (AtomixClientMapConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|AtomixClientMapConfiguration
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
DECL|method|getComponentConfiguration ()
specifier|protected
name|AtomixClientMapConfiguration
name|getComponentConfiguration
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
return|;
block|}
block|}
end_class

end_unit

