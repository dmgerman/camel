begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
operator|.
name|SedaEndpoint
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
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * Tests mbeans is registered when adding a 2nd route from within an existing route.  *  * @version  */
end_comment

begin_class
DECL|class|ManagedRouteAddFromRouteTest
specifier|public
class|class
name|ManagedRouteAddFromRouteTest
extends|extends
name|ManagementTestSupport
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
comment|// put a message pre-early on the seda queue, to trigger the route, which
comment|// then would add a 2nd route during CamelContext startup. This is a test
comment|// to ensure the foo route is not started too soon, and thus adding the 2nd
comment|// route works as expected
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:start"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|seda
operator|.
name|getQueue
argument_list|()
operator|.
name|put
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|RouteBuilder
name|child
init|=
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
literal|"seda:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
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
DECL|method|testAddRouteFromRoute ()
specifier|public
name|void
name|testAddRouteFromRoute
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|route1
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=routes,name=\"foo\""
argument_list|)
decl_stmt|;
comment|// should be started
name|String
name|state
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|route1
argument_list|,
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be started"
argument_list|,
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// should route the message we put on the seda queue before
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// find the 2nd route
name|ObjectName
name|route2
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=localhost/camel-1,type=routes,name=\"bar\""
argument_list|)
decl_stmt|;
comment|// should be started
name|state
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|route2
argument_list|,
literal|"State"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be started"
argument_list|,
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
argument_list|,
name|state
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

