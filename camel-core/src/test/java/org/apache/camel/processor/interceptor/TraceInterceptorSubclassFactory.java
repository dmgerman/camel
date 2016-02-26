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
name|DelegateProcessor
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

begin_class
DECL|class|TraceInterceptorSubclassFactory
specifier|public
class|class
name|TraceInterceptorSubclassFactory
implements|implements
name|TraceInterceptorFactory
block|{
DECL|field|eventMessages
specifier|private
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|eventMessages
decl_stmt|;
DECL|field|traceAllNodes
specifier|private
name|boolean
name|traceAllNodes
decl_stmt|;
DECL|method|TraceInterceptorSubclassFactory (List<StringBuilder> eventMessages)
specifier|public
name|TraceInterceptorSubclassFactory
parameter_list|(
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|eventMessages
parameter_list|)
block|{
name|this
operator|.
name|eventMessages
operator|=
name|eventMessages
expr_stmt|;
block|}
DECL|method|createTraceInterceptor (ProcessorDefinition<?> node, Processor target, TraceFormatter formatter, Tracer tracer)
specifier|public
name|TraceInterceptor
name|createTraceInterceptor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|TraceFormatter
name|formatter
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
return|return
operator|new
name|TracerInterceptorSubclass
argument_list|(
name|node
argument_list|,
name|target
argument_list|,
name|formatter
argument_list|,
name|tracer
argument_list|,
name|eventMessages
argument_list|,
name|this
argument_list|)
return|;
block|}
DECL|method|getTraceAllNodes ()
specifier|public
name|boolean
name|getTraceAllNodes
parameter_list|()
block|{
return|return
name|traceAllNodes
return|;
block|}
DECL|method|setTraceAllNodes (boolean traceAllNodes)
specifier|public
name|void
name|setTraceAllNodes
parameter_list|(
name|boolean
name|traceAllNodes
parameter_list|)
block|{
name|this
operator|.
name|traceAllNodes
operator|=
name|traceAllNodes
expr_stmt|;
block|}
DECL|class|TracerInterceptorSubclass
specifier|private
specifier|static
class|class
name|TracerInterceptorSubclass
extends|extends
name|TraceInterceptor
block|{
DECL|field|eventMessages
specifier|private
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|eventMessages
decl_stmt|;
DECL|field|traceThisNode
specifier|private
name|boolean
name|traceThisNode
init|=
literal|true
decl_stmt|;
DECL|field|factory
specifier|private
name|TraceInterceptorSubclassFactory
name|factory
decl_stmt|;
DECL|method|TracerInterceptorSubclass (ProcessorDefinition<?> node, Processor target, TraceFormatter formatter, Tracer tracer, List<StringBuilder> eventMessages, TraceInterceptorSubclassFactory factory)
name|TracerInterceptorSubclass
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|TraceFormatter
name|formatter
parameter_list|,
name|Tracer
name|tracer
parameter_list|,
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|eventMessages
parameter_list|,
name|TraceInterceptorSubclassFactory
name|factory
parameter_list|)
block|{
name|super
argument_list|(
name|node
argument_list|,
name|target
argument_list|,
name|formatter
argument_list|,
name|tracer
argument_list|)
expr_stmt|;
name|this
operator|.
name|eventMessages
operator|=
name|eventMessages
expr_stmt|;
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
while|while
condition|(
name|target
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|target
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|target
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|TraceTestProcessor
operator|.
name|class
argument_list|)
condition|)
block|{
name|traceThisNode
operator|=
literal|false
expr_stmt|;
block|}
block|}
DECL|method|storeMessage (StringBuilder message)
specifier|private
specifier|synchronized
name|void
name|storeMessage
parameter_list|(
name|StringBuilder
name|message
parameter_list|)
block|{
name|eventMessages
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|traceExchange (Exchange exchange)
specifier|protected
name|void
name|traceExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traceThisNode
operator|||
name|factory
operator|.
name|getTraceAllNodes
argument_list|()
condition|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|TraceHandlerTestHandler
operator|.
name|recordComplete
argument_list|(
name|message
argument_list|,
name|getNode
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|storeMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|traceExchangeIn (Exchange exchange)
specifier|protected
name|Object
name|traceExchangeIn
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traceThisNode
operator|||
name|factory
operator|.
name|getTraceAllNodes
argument_list|()
condition|)
block|{
name|StringBuilder
name|message
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|TraceHandlerTestHandler
operator|.
name|recordIn
argument_list|(
name|message
argument_list|,
name|getNode
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|traceExchangeOut (Exchange exchange, Object traceState)
specifier|protected
name|void
name|traceExchangeOut
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|traceState
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traceThisNode
operator|||
name|factory
operator|.
name|getTraceAllNodes
argument_list|()
condition|)
block|{
if|if
condition|(
name|StringBuilder
operator|.
name|class
operator|.
name|equals
argument_list|(
name|traceState
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|StringBuilder
name|message
init|=
operator|(
name|StringBuilder
operator|)
name|traceState
decl_stmt|;
name|TraceHandlerTestHandler
operator|.
name|recordOut
argument_list|(
name|message
argument_list|,
name|getNode
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|storeMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

