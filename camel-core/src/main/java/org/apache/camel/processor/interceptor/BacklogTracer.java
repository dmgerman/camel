begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ArrayBlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicLong
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
name|Processor
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
name|model
operator|.
name|ProcessorDefinition
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|model
operator|.
name|RouteDefinition
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
name|model
operator|.
name|RouteDefinitionHelper
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
name|InterceptStrategy
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
name|NodeIdFactory
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
name|support
operator|.
name|ServiceSupport
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
name|util
operator|.
name|EndpointHelper
import|;
end_import

begin_comment
comment|/**  * A tracer used for message tracing, storing a copy of the message details in a backlog.  *<p/>  * This tracer allows to store message tracers per node in the Camel routes. The tracers  * is stored in a backlog queue (FIFO based) which allows to pull the traced messages on demand.  */
end_comment

begin_class
DECL|class|BacklogTracer
specifier|public
class|class
name|BacklogTracer
extends|extends
name|ServiceSupport
implements|implements
name|InterceptStrategy
block|{
comment|// lets limit the tracer to 100 thousand messages in total
DECL|field|MAX_BACKLOG_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|MAX_BACKLOG_SIZE
init|=
literal|100
operator|*
literal|1000
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
DECL|field|traceCounter
specifier|private
specifier|final
name|AtomicLong
name|traceCounter
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// use a queue with a upper limit to avoid storing too many messages
DECL|field|queue
specifier|private
specifier|final
name|Queue
argument_list|<
name|DefaultBacklogTracerEventMessage
argument_list|>
name|queue
init|=
operator|new
name|ArrayBlockingQueue
argument_list|<
name|DefaultBacklogTracerEventMessage
argument_list|>
argument_list|(
name|MAX_BACKLOG_SIZE
argument_list|)
decl_stmt|;
comment|// how many of the last messages to keep in the backlog at total
DECL|field|backlogSize
specifier|private
name|int
name|backlogSize
init|=
literal|1000
decl_stmt|;
DECL|field|removeOnDump
specifier|private
name|boolean
name|removeOnDump
init|=
literal|true
decl_stmt|;
DECL|field|bodyMaxChars
specifier|private
name|int
name|bodyMaxChars
init|=
literal|128
operator|*
literal|1024
decl_stmt|;
DECL|field|bodyIncludeStreams
specifier|private
name|boolean
name|bodyIncludeStreams
decl_stmt|;
DECL|field|bodyIncludeFiles
specifier|private
name|boolean
name|bodyIncludeFiles
init|=
literal|true
decl_stmt|;
comment|// a pattern to filter tracing nodes
DECL|field|tracePattern
specifier|private
name|String
name|tracePattern
decl_stmt|;
DECL|field|patterns
specifier|private
name|String
index|[]
name|patterns
decl_stmt|;
comment|// remember the processors we are tracing, which we need later
DECL|field|processors
specifier|private
specifier|final
name|Set
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|processors
init|=
operator|new
name|HashSet
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|BacklogTracer (CamelContext camelContext)
specifier|public
name|BacklogTracer
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|wrapProcessorInInterceptors (CamelContext context, ProcessorDefinition<?> definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
comment|// is this the first output from a route, as we want to know this so we can do special logic in first
name|boolean
name|first
init|=
literal|false
decl_stmt|;
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
name|definition
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
operator|!
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|first
operator|=
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
name|definition
expr_stmt|;
block|}
name|processors
operator|.
name|add
argument_list|(
name|definition
argument_list|)
expr_stmt|;
return|return
operator|new
name|BacklogTracerInterceptor
argument_list|(
name|queue
argument_list|,
name|target
argument_list|,
name|definition
argument_list|,
name|route
argument_list|,
name|first
argument_list|,
name|this
argument_list|)
return|;
block|}
comment|/**      * Creates a new backlog tracer.      *      * @param context Camel context      * @return a new backlog tracer      */
DECL|method|createTracer (CamelContext context)
specifier|public
specifier|static
name|BacklogTracer
name|createTracer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|BacklogTracer
name|tracer
init|=
operator|new
name|BacklogTracer
argument_list|(
name|context
argument_list|)
decl_stmt|;
return|return
name|tracer
return|;
block|}
comment|/**      * A helper method to return the BacklogTracer instance if one is enabled      *      * @return the backlog tracer or null if none can be found      */
DECL|method|getBacklogTracer (CamelContext context)
specifier|public
specifier|static
name|BacklogTracer
name|getBacklogTracer
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|list
init|=
name|context
operator|.
name|getInterceptStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|interceptStrategy
range|:
name|list
control|)
block|{
if|if
condition|(
name|interceptStrategy
operator|instanceof
name|BacklogTracer
condition|)
block|{
return|return
operator|(
name|BacklogTracer
operator|)
name|interceptStrategy
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Whether or not to trace the given processor definition.      *      * @param definition the processor definition      * @return<tt>true</tt> to trace,<tt>false</tt> to skip tracing      */
DECL|method|shouldTrace (ProcessorDefinition<?> definition)
specifier|public
name|boolean
name|shouldTrace
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
if|if
condition|(
operator|!
name|enabled
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|patterns
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|pattern
range|:
name|patterns
control|)
block|{
comment|// match either route id, or node id
name|String
name|id
init|=
name|definition
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// use matchPattern method from endpoint helper that has a good matcher we use in Camel
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|id
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
name|definition
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
name|id
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
if|if
condition|(
name|EndpointHelper
operator|.
name|matchPattern
argument_list|(
name|id
argument_list|,
name|pattern
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
comment|// not matched the pattern
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
comment|// okay tracer is enabled then force auto assigning ids
if|if
condition|(
name|enabled
condition|)
block|{
name|forceAutoAssigningIds
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getBacklogSize ()
specifier|public
name|int
name|getBacklogSize
parameter_list|()
block|{
return|return
name|backlogSize
return|;
block|}
DECL|method|setBacklogSize (int backlogSize)
specifier|public
name|void
name|setBacklogSize
parameter_list|(
name|int
name|backlogSize
parameter_list|)
block|{
if|if
condition|(
name|backlogSize
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The backlog size must be a positive number, was: "
operator|+
name|backlogSize
argument_list|)
throw|;
block|}
if|if
condition|(
name|backlogSize
operator|>
name|MAX_BACKLOG_SIZE
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The backlog size cannot be greater than the max size of "
operator|+
name|MAX_BACKLOG_SIZE
operator|+
literal|", was: "
operator|+
name|backlogSize
argument_list|)
throw|;
block|}
name|this
operator|.
name|backlogSize
operator|=
name|backlogSize
expr_stmt|;
block|}
DECL|method|isRemoveOnDump ()
specifier|public
name|boolean
name|isRemoveOnDump
parameter_list|()
block|{
return|return
name|removeOnDump
return|;
block|}
DECL|method|setRemoveOnDump (boolean removeOnDump)
specifier|public
name|void
name|setRemoveOnDump
parameter_list|(
name|boolean
name|removeOnDump
parameter_list|)
block|{
name|this
operator|.
name|removeOnDump
operator|=
name|removeOnDump
expr_stmt|;
block|}
DECL|method|getBodyMaxChars ()
specifier|public
name|int
name|getBodyMaxChars
parameter_list|()
block|{
return|return
name|bodyMaxChars
return|;
block|}
DECL|method|setBodyMaxChars (int bodyMaxChars)
specifier|public
name|void
name|setBodyMaxChars
parameter_list|(
name|int
name|bodyMaxChars
parameter_list|)
block|{
name|this
operator|.
name|bodyMaxChars
operator|=
name|bodyMaxChars
expr_stmt|;
block|}
DECL|method|isBodyIncludeStreams ()
specifier|public
name|boolean
name|isBodyIncludeStreams
parameter_list|()
block|{
return|return
name|bodyIncludeStreams
return|;
block|}
DECL|method|setBodyIncludeStreams (boolean bodyIncludeStreams)
specifier|public
name|void
name|setBodyIncludeStreams
parameter_list|(
name|boolean
name|bodyIncludeStreams
parameter_list|)
block|{
name|this
operator|.
name|bodyIncludeStreams
operator|=
name|bodyIncludeStreams
expr_stmt|;
block|}
DECL|method|isBodyIncludeFiles ()
specifier|public
name|boolean
name|isBodyIncludeFiles
parameter_list|()
block|{
return|return
name|bodyIncludeFiles
return|;
block|}
DECL|method|setBodyIncludeFiles (boolean bodyIncludeFiles)
specifier|public
name|void
name|setBodyIncludeFiles
parameter_list|(
name|boolean
name|bodyIncludeFiles
parameter_list|)
block|{
name|this
operator|.
name|bodyIncludeFiles
operator|=
name|bodyIncludeFiles
expr_stmt|;
block|}
DECL|method|getTracePattern ()
specifier|public
name|String
name|getTracePattern
parameter_list|()
block|{
return|return
name|tracePattern
return|;
block|}
DECL|method|setTracePattern (String tracePattern)
specifier|public
name|void
name|setTracePattern
parameter_list|(
name|String
name|tracePattern
parameter_list|)
block|{
name|this
operator|.
name|tracePattern
operator|=
name|tracePattern
expr_stmt|;
if|if
condition|(
name|tracePattern
operator|!=
literal|null
condition|)
block|{
comment|// the pattern can have multiple nodes separated by comma
name|this
operator|.
name|patterns
operator|=
name|tracePattern
operator|.
name|split
argument_list|(
literal|","
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|patterns
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getTraceCounter ()
specifier|public
name|long
name|getTraceCounter
parameter_list|()
block|{
return|return
name|traceCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|resetTraceCounter ()
specifier|public
name|void
name|resetTraceCounter
parameter_list|()
block|{
name|traceCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|dumpTracedMessages (String nodeId)
specifier|public
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpTracedMessages
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|nodeId
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DefaultBacklogTracerEventMessage
name|message
range|:
name|queue
control|)
block|{
if|if
condition|(
name|nodeId
operator|.
name|equals
argument_list|(
name|message
operator|.
name|getToNode
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|removeOnDump
condition|)
block|{
name|queue
operator|.
name|removeAll
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|dumpTracedMessagesAsXml (String nodeId)
specifier|public
name|String
name|dumpTracedMessagesAsXml
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|events
init|=
name|dumpTracedMessages
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|DefaultBacklogTracerEventMessage
operator|.
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|"s>"
argument_list|)
expr_stmt|;
for|for
control|(
name|BacklogTracerEventMessage
name|event
range|:
name|events
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
name|event
operator|.
name|toXml
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n</"
argument_list|)
operator|.
name|append
argument_list|(
name|DefaultBacklogTracerEventMessage
operator|.
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|"s>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|dumpAllTracedMessages ()
specifier|public
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|dumpAllTracedMessages
parameter_list|()
block|{
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
argument_list|()
decl_stmt|;
name|answer
operator|.
name|addAll
argument_list|(
name|queue
argument_list|)
expr_stmt|;
if|if
condition|(
name|isRemoveOnDump
argument_list|()
condition|)
block|{
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|dumpAllTracedMessagesAsXml ()
specifier|public
name|String
name|dumpAllTracedMessagesAsXml
parameter_list|()
block|{
name|List
argument_list|<
name|BacklogTracerEventMessage
argument_list|>
name|events
init|=
name|dumpAllTracedMessages
argument_list|()
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<"
argument_list|)
operator|.
name|append
argument_list|(
name|BacklogTracerEventMessage
operator|.
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|"s>"
argument_list|)
expr_stmt|;
for|for
control|(
name|BacklogTracerEventMessage
name|event
range|:
name|events
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
operator|.
name|append
argument_list|(
name|event
operator|.
name|toXml
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"\n</"
argument_list|)
operator|.
name|append
argument_list|(
name|BacklogTracerEventMessage
operator|.
name|ROOT_TAG
argument_list|)
operator|.
name|append
argument_list|(
literal|"s>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|incrementTraceCounter ()
name|long
name|incrementTraceCounter
parameter_list|()
block|{
return|return
name|traceCounter
operator|.
name|incrementAndGet
argument_list|()
return|;
block|}
DECL|method|stopProcessor (ProcessorDefinition<?> processorDefinition)
name|void
name|stopProcessor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
parameter_list|)
block|{
name|this
operator|.
name|processors
operator|.
name|remove
argument_list|(
name|processorDefinition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
name|processors
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|forceAutoAssigningIds ()
specifier|private
name|void
name|forceAutoAssigningIds
parameter_list|()
block|{
name|NodeIdFactory
name|factory
init|=
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|child
range|:
name|processors
control|)
block|{
comment|// ensure also the children get ids assigned
name|RouteDefinitionHelper
operator|.
name|forceAssignIds
argument_list|(
name|camelContext
argument_list|,
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

