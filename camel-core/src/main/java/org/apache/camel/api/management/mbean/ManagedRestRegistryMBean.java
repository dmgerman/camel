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
DECL|interface|ManagedRestRegistryMBean
specifier|public
interface|interface
name|ManagedRestRegistryMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of rest services in the registry"
argument_list|)
DECL|method|getNumberOfRestServices ()
name|int
name|getNumberOfRestServices
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists all the Rest services in the registry (url, path, verb, consumes, produces)"
argument_list|)
DECL|method|listRestServices ()
name|TabularData
name|listRestServices
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

