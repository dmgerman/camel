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
name|LinkedList
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
DECL|class|TraceInterceptorFactoryCreatesHandlerTest
specifier|public
class|class
name|TraceInterceptorFactoryCreatesHandlerTest
extends|extends
name|TracingTestBase
block|{
DECL|class|TraceInterceptorFactoryCreatesHandlerTestFactory
specifier|private
specifier|static
class|class
name|TraceInterceptorFactoryCreatesHandlerTestFactory
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
DECL|method|TraceInterceptorFactoryCreatesHandlerTestFactory (List<StringBuilder> eventMessages, boolean traceAllNodes)
name|TraceInterceptorFactoryCreatesHandlerTestFactory
parameter_list|(
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|eventMessages
parameter_list|,
name|boolean
name|traceAllNodes
parameter_list|)
block|{
name|this
operator|.
name|eventMessages
operator|=
name|eventMessages
expr_stmt|;
name|this
operator|.
name|traceAllNodes
operator|=
name|traceAllNodes
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
DECL|method|createTraceInterceptor (ProcessorDefinition node, Processor target, TraceFormatter formatter, Tracer tracer)
specifier|public
name|Processor
name|createTraceInterceptor
parameter_list|(
name|ProcessorDefinition
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
name|TraceInterceptor
name|interceptor
init|=
operator|new
name|TraceInterceptor
argument_list|(
name|node
argument_list|,
name|target
argument_list|,
name|formatter
argument_list|,
name|tracer
argument_list|)
decl_stmt|;
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
name|traceAllNodes
operator|||
operator|!
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
name|TraceHandlerTestHandler
name|traceHandler
init|=
operator|new
name|TraceHandlerTestHandler
argument_list|(
name|eventMessages
argument_list|)
decl_stmt|;
name|traceHandler
operator|.
name|setTraceAllNodes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|interceptor
operator|.
name|setTraceHandler
argument_list|(
name|traceHandler
argument_list|)
expr_stmt|;
block|}
return|return
name|interceptor
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|contextLocal
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|tracedMessages
operator|=
operator|new
name|LinkedList
argument_list|<
name|StringBuilder
argument_list|>
argument_list|()
expr_stmt|;
name|Tracer
name|tracer
init|=
operator|(
name|Tracer
operator|)
name|contextLocal
operator|.
name|getDefaultTracer
argument_list|()
decl_stmt|;
name|tracer
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setTraceExceptions
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"testTracerExceptionInOut"
operator|.
name|equals
argument_list|(
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|tracer
operator|.
name|setTraceInterceptorFactory
argument_list|(
operator|new
name|TraceInterceptorFactoryCreatesHandlerTestFactory
argument_list|(
name|tracedMessages
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tracer
operator|.
name|setTraceInterceptorFactory
argument_list|(
operator|new
name|TraceInterceptorFactoryCreatesHandlerTestFactory
argument_list|(
name|tracedMessages
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|contextLocal
return|;
block|}
block|}
end_class

end_unit

