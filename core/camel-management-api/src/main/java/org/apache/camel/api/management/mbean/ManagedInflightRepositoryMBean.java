begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
DECL|interface|ManagedInflightRepositoryMBean
specifier|public
interface|interface
name|ManagedInflightRepositoryMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current size of inflight exchanges."
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
literal|"Current size of inflight exchanges which are from the given route."
argument_list|)
DECL|method|size (String routeId)
name|int
name|size
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists all the exchanges which are currently inflight"
argument_list|)
DECL|method|browse ()
name|TabularData
name|browse
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists all the exchanges which are currently inflight, limited and sorted"
argument_list|)
DECL|method|browse (int limit, boolean sortByLongestDuration)
name|TabularData
name|browse
parameter_list|(
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"List all the exchanges that origins from the given route, which are currently inflight, limited and sorted"
argument_list|)
DECL|method|browse (String fromRouteId, int limit, boolean sortByLongestDuration)
name|TabularData
name|browse
parameter_list|(
name|String
name|fromRouteId
parameter_list|,
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

