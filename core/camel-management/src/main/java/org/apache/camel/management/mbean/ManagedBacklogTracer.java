begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|mbean
operator|.
name|BacklogTracerEventMessage
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
name|mbean
operator|.
name|ManagedBacklogTracerMBean
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
name|processor
operator|.
name|interceptor
operator|.
name|BacklogTracer
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
name|ManagementStrategy
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed BacklogTracer"
argument_list|)
DECL|class|ManagedBacklogTracer
specifier|public
class|class
name|ManagedBacklogTracer
implements|implements
name|ManagedBacklogTracerMBean
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|backlogTracer
specifier|private
specifier|final
name|BacklogTracer
name|backlogTracer
decl_stmt|;
DECL|method|ManagedBacklogTracer (CamelContext camelContext, BacklogTracer backlogTracer)
specifier|public
name|ManagedBacklogTracer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|BacklogTracer
name|backlogTracer
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|backlogTracer
operator|=
name|backlogTracer
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getBacklogTracer ()
specifier|public
name|BacklogTracer
name|getBacklogTracer
parameter_list|()
block|{
return|return
name|backlogTracer
return|;
block|}
DECL|method|getEnabled ()
specifier|public
name|boolean
name|getEnabled
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|isEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|isEnabled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getBacklogSize ()
specifier|public
name|int
name|getBacklogSize
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|getBacklogSize
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setBacklogSize (int backlogSize)
specifier|public
name|void
name|setBacklogSize
parameter_list|(
name|int
name|backlogSize
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setBacklogSize
argument_list|(
name|backlogSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isRemoveOnDump ()
specifier|public
name|boolean
name|isRemoveOnDump
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|isRemoveOnDump
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setRemoveOnDump (boolean removeOnDump)
specifier|public
name|void
name|setRemoveOnDump
parameter_list|(
name|boolean
name|removeOnDump
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setRemoveOnDump
argument_list|(
name|removeOnDump
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setTracePattern (String pattern)
specifier|public
name|void
name|setTracePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setTracePattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTracePattern ()
specifier|public
name|String
name|getTracePattern
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|getTracePattern
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setTraceFilter (String predicate)
specifier|public
name|void
name|setTraceFilter
parameter_list|(
name|String
name|predicate
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setTraceFilter
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTraceFilter ()
specifier|public
name|String
name|getTraceFilter
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|getTraceFilter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTraceCounter ()
specifier|public
name|long
name|getTraceCounter
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|getTraceCounter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resetTraceCounter ()
specifier|public
name|void
name|resetTraceCounter
parameter_list|()
block|{
name|backlogTracer
operator|.
name|resetTraceCounter
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBodyMaxChars ()
specifier|public
name|int
name|getBodyMaxChars
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|getBodyMaxChars
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setBodyMaxChars (int bodyMaxChars)
specifier|public
name|void
name|setBodyMaxChars
parameter_list|(
name|int
name|bodyMaxChars
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setBodyMaxChars
argument_list|(
name|bodyMaxChars
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isBodyIncludeStreams ()
specifier|public
name|boolean
name|isBodyIncludeStreams
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|isBodyIncludeStreams
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setBodyIncludeStreams (boolean bodyIncludeStreams)
specifier|public
name|void
name|setBodyIncludeStreams
parameter_list|(
name|boolean
name|bodyIncludeStreams
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setBodyIncludeStreams
argument_list|(
name|bodyIncludeStreams
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isBodyIncludeFiles ()
specifier|public
name|boolean
name|isBodyIncludeFiles
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|isBodyIncludeFiles
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setBodyIncludeFiles (boolean bodyIncludeFiles)
specifier|public
name|void
name|setBodyIncludeFiles
parameter_list|(
name|boolean
name|bodyIncludeFiles
parameter_list|)
block|{
name|backlogTracer
operator|.
name|setBodyIncludeFiles
argument_list|(
name|bodyIncludeFiles
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|dumpTracedMessages (String nodeOrRouteId)
specifier|public
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpTracedMessages
parameter_list|(
name|String
name|nodeOrRouteId
parameter_list|)
block|{
return|return
name|backlogTracer
operator|.
name|dumpTracedMessages
argument_list|(
name|nodeOrRouteId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|dumpAllTracedMessages ()
specifier|public
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpAllTracedMessages
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|dumpAllTracedMessages
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|dumpTracedMessagesAsXml (String nodeOrRouteId)
specifier|public
name|String
name|dumpTracedMessagesAsXml
parameter_list|(
name|String
name|nodeOrRouteId
parameter_list|)
block|{
return|return
name|backlogTracer
operator|.
name|dumpTracedMessagesAsXml
argument_list|(
name|nodeOrRouteId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|dumpAllTracedMessagesAsXml ()
specifier|public
name|String
name|dumpAllTracedMessagesAsXml
parameter_list|()
block|{
return|return
name|backlogTracer
operator|.
name|dumpAllTracedMessagesAsXml
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|backlogTracer
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

