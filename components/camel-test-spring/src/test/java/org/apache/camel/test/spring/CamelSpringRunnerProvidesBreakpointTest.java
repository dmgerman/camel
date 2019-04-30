begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|spring
package|;
end_package

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
name|NamedNode
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
name|processor
operator|.
name|interceptor
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
name|spi
operator|.
name|Breakpoint
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|CamelSpringRunnerProvidesBreakpointTest
specifier|public
class|class
name|CamelSpringRunnerProvidesBreakpointTest
extends|extends
name|CamelSpringRunnerPlainTest
block|{
annotation|@
name|ProvidesBreakpoint
DECL|method|createBreakpoint ()
specifier|public
specifier|static
name|Breakpoint
name|createBreakpoint
parameter_list|()
block|{
return|return
operator|new
name|TestBreakpoint
argument_list|()
return|;
block|}
annotation|@
name|Test
annotation|@
name|Override
DECL|method|testProvidesBreakpoint ()
specifier|public
name|void
name|testProvidesBreakpoint
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|camelContext
operator|.
name|getDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelContext2
operator|.
name|getDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|start
operator|.
name|sendBody
argument_list|(
literal|"David"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
operator|.
name|getDebugger
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|camelContext
operator|.
name|getDebugger
argument_list|()
operator|.
name|getBreakpoints
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|camelContext
operator|.
name|getDebugger
argument_list|()
operator|.
name|getBreakpoints
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|camelContext
operator|.
name|getDebugger
argument_list|()
operator|.
name|getBreakpoints
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|TestBreakpoint
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|TestBreakpoint
operator|)
name|camelContext
operator|.
name|getDebugger
argument_list|()
operator|.
name|getBreakpoints
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|)
operator|.
name|isBreakpointHit
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|TestBreakpoint
specifier|private
specifier|static
specifier|final
class|class
name|TestBreakpoint
extends|extends
name|BreakpointSupport
block|{
DECL|field|breakpointHit
specifier|private
name|boolean
name|breakpointHit
decl_stmt|;
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, Processor processor, NamedNode definition)
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
name|NamedNode
name|definition
parameter_list|)
block|{
name|breakpointHit
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isBreakpointHit ()
specifier|public
name|boolean
name|isBreakpointHit
parameter_list|()
block|{
return|return
name|breakpointHit
return|;
block|}
block|}
block|}
end_class

end_unit

