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
name|org
operator|.
name|junit
operator|.
name|Before
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
name|EventObject
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
name|impl
operator|.
name|BreakpointSupport
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
name|impl
operator|.
name|ConditionSupport
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
name|impl
operator|.
name|DefaultDebugger
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
name|management
operator|.
name|event
operator|.
name|ExchangeCompletedEvent
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
name|ToDefinition
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
name|Breakpoint
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
name|Condition
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DebugTest
specifier|public
class|class
name|DebugTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|logs
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|logs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|camelCondition
specifier|private
name|Condition
name|camelCondition
decl_stmt|;
DECL|field|mockCondition
specifier|private
name|Condition
name|mockCondition
decl_stmt|;
DECL|field|doneCondition
specifier|private
name|Condition
name|doneCondition
decl_stmt|;
DECL|field|breakpoint
specifier|private
name|Breakpoint
name|breakpoint
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|breakpoint
operator|=
operator|new
name|BreakpointSupport
argument_list|()
block|{
specifier|public
name|void
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|logs
operator|.
name|add
argument_list|(
literal|"Breakpoint at "
operator|+
name|definition
operator|+
literal|" with body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|logs
operator|.
name|add
argument_list|(
literal|"Breakpoint event "
operator|+
name|event
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|" with body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
name|camelCondition
operator|=
operator|new
name|ConditionSupport
argument_list|()
block|{
specifier|public
name|boolean
name|matchProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
return|return
name|body
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Camel"
argument_list|)
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
expr_stmt|;
name|mockCondition
operator|=
operator|new
name|ConditionSupport
argument_list|()
block|{
specifier|public
name|boolean
name|matchProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// match when sending to mocks
if|if
condition|(
name|definition
operator|instanceof
name|ToDefinition
condition|)
block|{
name|ToDefinition
name|to
init|=
operator|(
name|ToDefinition
operator|)
name|definition
decl_stmt|;
return|return
name|to
operator|.
name|getUriOrRef
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"mock"
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
expr_stmt|;
name|doneCondition
operator|=
operator|new
name|ConditionSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matchEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeCompletedEvent
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDebug ()
specifier|public
name|void
name|testDebug
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|,
name|camelCondition
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello Camel"
argument_list|)
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
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Breakpoint at To[log:foo] with body: Hello Camel"
argument_list|,
name|logs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Breakpoint at To[mock:result] with body: Hello Camel"
argument_list|,
name|logs
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDebugEvent ()
specifier|public
name|void
name|testDebugEvent
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|,
name|doneCondition
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello Camel"
argument_list|)
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
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Breakpoint event ExchangeCompletedEvent with body: Hello World"
argument_list|,
name|logs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Breakpoint event ExchangeCompletedEvent with body: Hello Camel"
argument_list|,
name|logs
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDebugSuspended ()
specifier|public
name|void
name|testDebugSuspended
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|,
name|mockCondition
argument_list|,
name|camelCondition
argument_list|)
expr_stmt|;
comment|// suspend the breakpoint
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|suspendAllBreakpoints
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello Camel"
argument_list|)
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
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// resume the breakpoint
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|activateAllBreakpoints
argument_list|()
expr_stmt|;
comment|// reset and test again now the breakpoint is active
name|resetMocks
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
literal|"Hello Camel"
argument_list|)
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
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Breakpoint at To[mock:result] with body: Hello Camel"
argument_list|,
name|logs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDebugRemoveBreakpoint ()
specifier|public
name|void
name|testDebugRemoveBreakpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove the breakpoint
name|context
operator|.
name|getDebugger
argument_list|()
operator|.
name|removeBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
comment|// reset and test again now the breakpoint is removed
name|resetMocks
argument_list|()
expr_stmt|;
name|logs
operator|.
name|clear
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Camel"
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
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|logs
operator|.
name|size
argument_list|()
argument_list|)
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
comment|// use debugger
name|context
operator|.
name|setDebugger
argument_list|(
operator|new
name|DefaultDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
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

