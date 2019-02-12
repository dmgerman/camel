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
name|Collection
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
DECL|interface|ManagedClusterServiceMBean
specifier|public
interface|interface
name|ManagedClusterServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The namespaces handled by the service"
argument_list|)
DECL|method|getNamespaces ()
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Service State"
argument_list|)
DECL|method|getState ()
name|String
name|getState
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ID"
argument_list|)
DECL|method|getCamelId ()
name|String
name|getCamelId
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Start Service"
argument_list|)
DECL|method|start ()
name|void
name|start
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop Service"
argument_list|)
DECL|method|stop ()
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Start the View"
argument_list|)
DECL|method|startView (String namespace)
name|void
name|startView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop the View"
argument_list|)
DECL|method|stopView (String namespace)
name|void
name|stopView
parameter_list|(
name|String
name|namespace
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"If the local view is leader"
argument_list|)
DECL|method|isLeader (String namespace)
name|boolean
name|isLeader
parameter_list|(
name|String
name|namespace
parameter_list|)
function_decl|;
block|}
end_interface

end_unit
