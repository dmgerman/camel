begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|SedaRemoveRouteThenAddAgainTest
specifier|public
class|class
name|SedaRemoveRouteThenAddAgainTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testRemoveRouteAndThenAddAgain ()
specifier|public
name|void
name|testRemoveRouteAndThenAddAgain
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|out
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"before removing the route"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:in"
argument_list|,
literal|"before removing the route"
argument_list|)
expr_stmt|;
name|out
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// now stop& remove the route
name|context
operator|.
name|stopRoute
argument_list|(
literal|"sedaToMock"
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
literal|"sedaToMock"
argument_list|)
expr_stmt|;
comment|// and then add it back again
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
comment|// the mock endpoint was removed, so need to grab it again
name|out
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|out
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|out
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"after removing the route"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:in"
argument_list|,
literal|"after removing the route"
argument_list|)
expr_stmt|;
name|out
operator|.
name|assertIsSatisfied
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
name|from
argument_list|(
literal|"seda:in"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"sedaToMock"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

