begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AdviceWithRouteBuilder
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
name|reifier
operator|.
name|RouteReifier
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
DECL|class|AdviceWithMockEndpointsHavingParameterTest
specifier|public
class|class
name|AdviceWithMockEndpointsHavingParameterTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testNoAdvised ()
specifier|public
name|void
name|testNoAdvised
parameter_list|()
throws|throws
name|Exception
block|{
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
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
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
block|}
annotation|@
name|Test
DECL|method|testAdvisedMockEndpoints ()
specifier|public
name|void
name|testAdvisedMockEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
comment|// advice the first route using the inlined AdviceWith route builder
comment|// which has extended capabilities than the regular route builder
name|RouteReifier
operator|.
name|adviceWith
argument_list|(
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|context
argument_list|,
operator|new
name|AdviceWithRouteBuilder
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
comment|// mock all endpoints (will mock in all routes)
name|mockEndpoints
argument_list|()
expr_stmt|;
block|}
block|}
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
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:direct:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:log:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:seda:foo"
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
comment|// additional test to ensure correct endpoints in registry
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"direct:start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"seda:foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log:foo?showHeaders=false"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"log:start?showAll=true"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
argument_list|)
expr_stmt|;
comment|// all the endpoints was mocked
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:direct:start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:seda:foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:log:start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|hasEndpoint
argument_list|(
literal|"mock:log:foo"
argument_list|)
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
name|from
argument_list|(
literal|"seda:foo?size=20"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"We transformed ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo?showHeaders=false"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:start?showAll=true"
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

