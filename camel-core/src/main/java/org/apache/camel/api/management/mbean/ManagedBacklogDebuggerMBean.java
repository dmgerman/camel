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
name|Set
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
DECL|interface|ManagedBacklogDebuggerMBean
specifier|public
interface|interface
name|ManagedBacklogDebuggerMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is debugger enabled"
argument_list|)
DECL|method|isEnabled ()
name|boolean
name|isEnabled
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Enable the debugger"
argument_list|)
DECL|method|enableDebugger ()
name|void
name|enableDebugger
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Disable the debugger"
argument_list|)
DECL|method|disableDebugger ()
name|void
name|disableDebugger
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Add a breakpoint at the given node id"
argument_list|)
DECL|method|addBreakpoint (String nodeId)
name|void
name|addBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remote the breakpoint from the given node id"
argument_list|)
DECL|method|removeBreakpoint (String nodeId)
name|void
name|removeBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Continue debugging the suspended breakpoints at the given node id"
argument_list|)
DECL|method|continueBreakpoint (String nodeId)
name|void
name|continueBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Return the node ids which is currently suspended"
argument_list|)
DECL|method|getSuspendedBreakpointNodeIds ()
name|Set
argument_list|<
name|String
argument_list|>
name|getSuspendedBreakpointNodeIds
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

