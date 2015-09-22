begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|TwoRouteSuspendResumeTest
specifier|public
class|class
name|TwoRouteSuspendResumeTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testSuspendResume ()
specifier|public
name|void
name|testSuspendResume
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Suspending"
argument_list|)
expr_stmt|;
comment|// now suspend and dont expect a message to be routed
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockBar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|mockBar
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|suspendRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|// need to give seda consumer thread time to idle
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
comment|// we can still send a message to bar when foo route is suspended
name|mockBar
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Suspended"
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Resuming"
argument_list|)
expr_stmt|;
comment|// now resume and expect the previous message to be routed
name|resetMocks
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|context
operator|.
name|resumeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|name
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
name|from
argument_list|(
literal|"seda:foo"
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
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

