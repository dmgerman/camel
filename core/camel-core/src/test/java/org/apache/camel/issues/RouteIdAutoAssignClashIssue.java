begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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

begin_class
DECL|class|RouteIdAutoAssignClashIssue
specifier|public
class|class
name|RouteIdAutoAssignClashIssue
extends|extends
name|ContextTestSupport
block|{
DECL|method|testRouteIdAutoAssignClash ()
specifier|public
name|void
name|testRouteIdAutoAssignClash
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:start1"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:start2"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:start"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hi Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start1"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start2"
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
literal|"Hi Camel"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|context
operator|.
name|getRoutes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"route1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"route2"
argument_list|)
argument_list|)
expr_stmt|;
comment|// we will auto assign the route as route3
name|assertNotNull
argument_list|(
name|context
operator|.
name|getRoute
argument_list|(
literal|"route3"
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
comment|// use route1, route2 to clash with Camel's auto assigning
name|from
argument_list|(
literal|"direct:start1"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start1"
argument_list|)
expr_stmt|;
comment|// use route1, route2 to clash with Camel's auto assigning
name|from
argument_list|(
literal|"direct:start2"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start2"
argument_list|)
expr_stmt|;
comment|// no assigned route id which should be auto assigned
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:start"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
