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
literal|"Logging Level"
argument_list|)
DECL|method|getLoggingLevel ()
name|String
name|getLoggingLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|setLoggingLevel (String level)
name|void
name|setLoggingLevel
parameter_list|(
name|String
name|level
parameter_list|)
function_decl|;
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
literal|"Add a conditional breakpoint at the given node id"
argument_list|)
DECL|method|addConditionalBreakpoint (String nodeId, String language, String predicate)
name|void
name|addConditionalBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|,
name|String
name|language
parameter_list|,
name|String
name|predicate
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
literal|"Resume running from the suspended breakpoint at the given node id"
argument_list|)
DECL|method|resumeBreakpoint (String nodeId)
name|void
name|resumeBreakpoint
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
literal|"Resume running any suspended breakpoints, and exits step mode"
argument_list|)
DECL|method|resumeAll ()
name|void
name|resumeAll
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Starts single step debugging from the suspended breakpoint at the given node id"
argument_list|)
DECL|method|stepBreakpoint (String nodeId)
name|void
name|stepBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether currently in step mode"
argument_list|)
DECL|method|isSingleStepMode ()
name|boolean
name|isSingleStepMode
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Steps to next node in step mode"
argument_list|)
DECL|method|step ()
name|void
name|step
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Return the node ids which has breakpoints"
argument_list|)
DECL|method|getBreakpoints ()
name|Set
argument_list|<
name|String
argument_list|>
name|getBreakpoints
parameter_list|()
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
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Suspend a breakpoint"
argument_list|)
DECL|method|suspendBreakpoint (String nodeId)
specifier|public
name|void
name|suspendBreakpoint
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
literal|"Activate a breakpoint"
argument_list|)
DECL|method|activateBreakpoint (String nodeId)
specifier|public
name|void
name|activateBreakpoint
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
literal|"Dumps the messages in xml format from the suspended breakpoint at the given node"
argument_list|)
DECL|method|dumpTracedMessagesAsXml (String nodeId)
name|String
name|dumpTracedMessagesAsXml
parameter_list|(
name|String
name|nodeId
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of total debugged messages"
argument_list|)
DECL|method|getDebugCounter ()
name|long
name|getDebugCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resets the debug counter"
argument_list|)
DECL|method|resetDebugCounter ()
name|void
name|resetDebugCounter
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

