begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
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
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedRuntimeEndpointRegistryMBean
specifier|public
interface|interface
name|ManagedRuntimeEndpointRegistryMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clears the registry"
argument_list|)
DECL|method|clear ()
name|void
name|clear
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset the statistic counters"
argument_list|)
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether gathering runtime usage is enabled or not"
argument_list|)
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether gathering runtime usage is enabled or not"
argument_list|)
DECL|method|setEnabled (boolean enabled)
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum number of endpoints to keep in the cache per route"
argument_list|)
DECL|method|getLimit ()
name|int
name|getLimit
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of endpoints currently in the registry"
argument_list|)
DECL|method|getSize ()
name|int
name|getSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|" Gets all the endpoint urls captured during runtime that are in-use"
argument_list|)
DECL|method|getAllEndpoints (boolean includeInputs)
name|List
argument_list|<
name|String
argument_list|>
name|getAllEndpoints
parameter_list|(
name|boolean
name|includeInputs
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|" Gets all the endpoint urls captured during runtime that are in-use for the given route"
argument_list|)
DECL|method|getEndpointsPerRoute (String routeId, boolean includeInputs)
name|List
argument_list|<
name|String
argument_list|>
name|getEndpointsPerRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|boolean
name|includeInputs
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists statistics about all the endpoints in the registry"
argument_list|)
DECL|method|endpointStatistics ()
name|TabularData
name|endpointStatistics
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

