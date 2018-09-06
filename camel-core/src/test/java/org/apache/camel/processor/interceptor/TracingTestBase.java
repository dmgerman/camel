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
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|TracingTestBase
specifier|public
specifier|abstract
class|class
name|TracingTestBase
extends|extends
name|ContextTestSupport
block|{
DECL|field|tracedMessages
specifier|protected
name|List
argument_list|<
name|StringBuilder
argument_list|>
name|tracedMessages
decl_stmt|;
DECL|field|processor
specifier|private
name|TraceTestProcessor
name|processor
init|=
operator|new
name|TraceTestProcessor
argument_list|()
decl_stmt|;
DECL|method|prepareTestTracerInOnly ()
specifier|protected
name|void
name|prepareTestTracerInOnly
parameter_list|()
block|{     }
DECL|method|prepareTestTracerInOut ()
specifier|protected
name|void
name|prepareTestTracerInOut
parameter_list|()
block|{     }
DECL|method|prepareTestTracerExceptionInOut ()
specifier|protected
name|void
name|prepareTestTracerExceptionInOut
parameter_list|()
block|{     }
DECL|method|validateTestTracerInOnly ()
specifier|protected
name|void
name|validateTestTracerInOnly
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|3
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
literal|"Complete:"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|validateTestTracerInOut ()
specifier|protected
name|void
name|validateTestTracerInOut
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|3
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
literal|"In:"
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
block|}
DECL|method|validateTestTracerExceptionInOut ()
specifier|protected
name|void
name|validateTestTracerExceptionInOut
parameter_list|()
block|{
name|assertEquals
argument_list|(
literal|5
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
literal|"In:"
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
literal|2
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
name|tracedMessages
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testTracerInOnly ()
specifier|public
name|void
name|testTracerInOnly
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
literal|false
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|prepareTestTracerInOnly
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
name|validateTestTracerInOnly
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTracerInOut ()
specifier|public
name|void
name|testTracerInOut
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
name|prepareTestTracerInOut
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
name|validateTestTracerInOut
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
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
literal|2
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
comment|// ignore
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
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|tracing
argument_list|()
operator|.
name|process
argument_list|(
name|processor
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

