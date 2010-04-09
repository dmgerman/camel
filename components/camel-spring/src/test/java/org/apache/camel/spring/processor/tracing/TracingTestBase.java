begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.processor.tracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|processor
operator|.
name|tracing
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|TraceHandlerTestHandler
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
name|Tracer
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
name|spring
operator|.
name|SpringTestSupport
import|;
end_import

begin_class
DECL|class|TracingTestBase
specifier|public
specifier|abstract
class|class
name|TracingTestBase
extends|extends
name|SpringTestSupport
block|{
DECL|method|getTracedMessages ()
specifier|protected
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|getTracedMessages
parameter_list|()
block|{
name|Tracer
name|tracer
init|=
operator|(
name|Tracer
operator|)
name|this
operator|.
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"tracer"
argument_list|)
decl_stmt|;
name|TraceHandlerTestHandler
name|handler
init|=
operator|(
name|TraceHandlerTestHandler
operator|)
name|tracer
operator|.
name|getTraceHandler
argument_list|()
decl_stmt|;
return|return
name|handler
operator|.
name|getEventMessages
argument_list|()
return|;
block|}
DECL|method|prepareTestTracerExceptionInOut ()
specifier|protected
name|void
name|prepareTestTracerExceptionInOut
parameter_list|()
block|{     }
DECL|method|validateTestTracerExceptionInOut ()
specifier|protected
name|void
name|validateTestTracerExceptionInOut
parameter_list|()
block|{
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|tracedMessages
init|=
name|getTracedMessages
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|tracedMessages
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|StringBuilder
name|tracedMessage
range|:
name|tracedMessages
control|)
block|{
name|String
name|message
init|=
name|tracedMessage
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|startsWith
argument_list|(
literal|"In"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|contains
argument_list|(
literal|"Out:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|tracedMessages
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Ex:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getMessageCount ()
specifier|protected
name|int
name|getMessageCount
parameter_list|()
block|{
return|return
name|getTracedMessages
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|testTracerExceptionInOut ()
specifier|public
name|void
name|testTracerExceptionInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|Tracer
operator|)
name|context
operator|.
name|getDefaultTracer
argument_list|()
operator|)
operator|.
name|setTraceOutExchanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|prepareTestTracerExceptionInOut
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Kaboom"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// expected
block|}
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|validateTestTracerExceptionInOut
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

