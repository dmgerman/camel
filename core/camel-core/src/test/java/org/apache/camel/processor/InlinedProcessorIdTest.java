begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|model
operator|.
name|RouteDefinition
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
DECL|class|InlinedProcessorIdTest
specifier|public
class|class
name|InlinedProcessorIdTest
extends|extends
name|ContextTestSupport
block|{
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"log"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"foo"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|id
argument_list|(
literal|"inlined"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testInlinedProcessorId ()
specifier|public
name|void
name|testInlinedProcessorId
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"foo"
argument_list|,
literal|123
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
comment|// check ids
name|RouteDefinition
name|route
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log"
argument_list|,
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"inlined"
argument_list|,
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"result"
argument_list|,
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

