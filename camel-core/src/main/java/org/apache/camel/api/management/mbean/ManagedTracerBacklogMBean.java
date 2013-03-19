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
DECL|interface|ManagedTracerBacklogMBean
specifier|public
interface|interface
name|ManagedTracerBacklogMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is tracing enabled"
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
literal|"Is tracing enabled"
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
literal|"Number of maximum traced messages in total to keep in the backlog (FIFO queue)"
argument_list|)
DECL|method|getBacklogSize ()
name|int
name|getBacklogSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of maximum traced messages in total to keep in the backlog (FIFO queue)"
argument_list|)
DECL|method|setBacklogSize (int backlogSize)
name|void
name|setBacklogSize
parameter_list|(
name|int
name|backlogSize
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to remove traced message from backlog when dumping trace messages"
argument_list|)
DECL|method|isRemoveOnDump ()
name|boolean
name|isRemoveOnDump
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to remove traced message from backlog when dumping trace messages"
argument_list|)
DECL|method|setRemoveOnDump (boolean removeOnDump)
name|void
name|setRemoveOnDump
parameter_list|(
name|boolean
name|removeOnDump
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"To filter tracing by nodes (pattern)"
argument_list|)
DECL|method|setTracePattern (String pattern)
name|void
name|setTracePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"To filter tracing by nodes (pattern)"
argument_list|)
DECL|method|getTracePattern ()
name|String
name|getTracePattern
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of total traced messages"
argument_list|)
DECL|method|getTraceCounter ()
name|long
name|getTraceCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resets the trace counter"
argument_list|)
DECL|method|resetTraceCounter ()
name|void
name|resetTraceCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of maximum chars in the message body in the trace message. Use zero or negative value to have unlimited size."
argument_list|)
DECL|method|getBodyMaxChars ()
name|int
name|getBodyMaxChars
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of maximum chars in the message body in the trace message. Use zero or negative value to have unlimited size."
argument_list|)
DECL|method|setBodyMaxChars (int bodyMaxChars)
name|void
name|setBodyMaxChars
parameter_list|(
name|int
name|bodyMaxChars
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to include stream based message body in the trace message."
argument_list|)
DECL|method|isBodyIncludeStreams ()
name|boolean
name|isBodyIncludeStreams
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to include stream based message body in the trace message."
argument_list|)
DECL|method|setBodyIncludeStreams (boolean bodyIncludeStreams)
name|void
name|setBodyIncludeStreams
parameter_list|(
name|boolean
name|bodyIncludeStreams
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to include file based message body in the trace message."
argument_list|)
DECL|method|isBodyIncludeFiles ()
name|boolean
name|isBodyIncludeFiles
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to include file based message body in the trace message."
argument_list|)
DECL|method|setBodyIncludeFiles (boolean bodyIncludeFiles)
name|void
name|setBodyIncludeFiles
parameter_list|(
name|boolean
name|bodyIncludeFiles
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the traced messages for the given node or route"
argument_list|)
DECL|method|dumpTracedMessages (String nodeOrRouteId)
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpTracedMessages
parameter_list|(
name|String
name|nodeOrRouteId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the traced messages for the given node or route in xml format"
argument_list|)
DECL|method|dumpTracedMessagesAsXml (String nodeOrRouteId)
name|String
name|dumpTracedMessagesAsXml
parameter_list|(
name|String
name|nodeOrRouteId
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps all the traced messages"
argument_list|)
DECL|method|dumpAllTracedMessages ()
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpAllTracedMessages
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps all the traced messages in xml format"
argument_list|)
DECL|method|dumpAllTracedMessagesAsXml ()
name|String
name|dumpAllTracedMessagesAsXml
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

