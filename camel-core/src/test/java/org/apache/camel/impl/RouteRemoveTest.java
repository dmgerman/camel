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
name|ServiceStatus
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RouteRemoveTest
specifier|public
class|class
name|RouteRemoveTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testStopRouteOnContext ()
specifier|public
name|void
name|testStopRouteOnContext
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
operator|(
operator|(
name|DefaultRoute
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|)
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
operator|(
operator|(
name|DefaultRoute
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|)
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Stopped
argument_list|,
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
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
literal|1
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
comment|// must be stopped so we cant remove
name|boolean
name|removed
init|=
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|removed
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
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
comment|// remove route then
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|removed
operator|=
name|context
operator|.
name|removeRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|removed
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
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
name|assertNull
argument_list|(
name|context
operator|.
name|getRouteStatus
argument_list|(
literal|"foo"
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

