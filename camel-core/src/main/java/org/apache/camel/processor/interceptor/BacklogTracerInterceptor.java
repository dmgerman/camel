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
name|Date
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
name|Exchange
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|MessageHelper
import|;
end_import

begin_comment
comment|/**  * An interceptor for tracing messages by using the {@link BacklogTracer}.  */
end_comment

begin_class
DECL|class|BacklogTracerInterceptor
specifier|public
class|class
name|BacklogTracerInterceptor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|queue
specifier|private
specifier|final
name|Queue
argument_list|<
name|DefaultBacklogTracerEventMessage
argument_list|>
name|queue
decl_stmt|;
DECL|field|backlogTracer
specifier|private
specifier|final
name|BacklogTracer
name|backlogTracer
decl_stmt|;
DECL|field|processorDefinition
specifier|private
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
decl_stmt|;
DECL|field|routeDefinition
specifier|private
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|routeDefinition
decl_stmt|;
DECL|field|first
specifier|private
specifier|final
name|boolean
name|first
decl_stmt|;
DECL|method|BacklogTracerInterceptor (Queue<DefaultBacklogTracerEventMessage> queue, Processor processor, ProcessorDefinition<?> processorDefinition, ProcessorDefinition<?> routeDefinition, boolean first, BacklogTracer tracer)
specifier|public
name|BacklogTracerInterceptor
parameter_list|(
name|Queue
argument_list|<
name|DefaultBacklogTracerEventMessage
argument_list|>
name|queue
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processorDefinition
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|routeDefinition
parameter_list|,
name|boolean
name|first
parameter_list|,
name|BacklogTracer
name|tracer
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
name|this
operator|.
name|processorDefinition
operator|=
name|processorDefinition
expr_stmt|;
name|this
operator|.
name|routeDefinition
operator|=
name|routeDefinition
expr_stmt|;
name|this
operator|.
name|first
operator|=
name|first
expr_stmt|;
name|this
operator|.
name|backlogTracer
operator|=
name|tracer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|backlogTracer
operator|.
name|shouldTrace
argument_list|(
name|processorDefinition
argument_list|)
condition|)
block|{
comment|// ensure there is space on the queue
name|int
name|drain
init|=
name|queue
operator|.
name|size
argument_list|()
operator|-
name|backlogTracer
operator|.
name|getBacklogSize
argument_list|()
decl_stmt|;
if|if
condition|(
name|drain
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|drain
condition|;
name|i
operator|++
control|)
block|{
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
block|}
name|Date
name|timestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|String
name|toNode
init|=
name|processorDefinition
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|exchangeId
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|messageAsXml
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
comment|// if first we should add a pseudo trace message as well, so we have a starting message (eg from the route)
if|if
condition|(
name|first
condition|)
block|{
name|Date
name|created
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CREATED_TIMESTAMP
argument_list|,
name|timestamp
argument_list|,
name|Date
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|routeId
init|=
name|routeDefinition
operator|.
name|getId
argument_list|()
decl_stmt|;
name|DefaultBacklogTracerEventMessage
name|pseudo
init|=
operator|new
name|DefaultBacklogTracerEventMessage
argument_list|(
name|backlogTracer
operator|.
name|incrementTraceCounter
argument_list|()
argument_list|,
name|created
argument_list|,
name|routeId
argument_list|,
name|exchangeId
argument_list|,
name|messageAsXml
argument_list|)
decl_stmt|;
name|queue
operator|.
name|add
argument_list|(
name|pseudo
argument_list|)
expr_stmt|;
block|}
name|DefaultBacklogTracerEventMessage
name|event
init|=
operator|new
name|DefaultBacklogTracerEventMessage
argument_list|(
name|backlogTracer
operator|.
name|incrementTraceCounter
argument_list|()
argument_list|,
name|timestamp
argument_list|,
name|toNode
argument_list|,
name|exchangeId
argument_list|,
name|messageAsXml
argument_list|)
decl_stmt|;
name|queue
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
comment|// invoke processor
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|queue
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// notify backlogTracer we are stopping to not leak resources
name|backlogTracer
operator|.
name|stopProcessor
argument_list|(
name|processorDefinition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|processor
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

