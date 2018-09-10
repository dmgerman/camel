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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|CamelContext
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
DECL|class|ManagedProducerRouteAddRemoveRegisterAlwaysTest
specifier|public
class|class
name|ManagedProducerRouteAddRemoveRegisterAlwaysTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|services
specifier|private
name|int
name|services
init|=
literal|10
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setRegisterAlways
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|testRouteAddRemoteRouteWithRecipientList ()
specifier|public
name|void
name|testRouteAddRemoteRouteWithRecipientList
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
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
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
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=services,*"
argument_list|)
decl_stmt|;
comment|// number of services
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|on
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|services
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// number of producers
name|ObjectName
name|onP
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=producers,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|namesP
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|onP
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|namesP
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Adding 2nd route"
argument_list|)
expr_stmt|;
comment|// add a 2nd route
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"direct:bar"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|recipientList
argument_list|(
name|header
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// and send a message to it
name|MockEndpoint
name|bar
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|bar
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:bar"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"bar"
argument_list|,
literal|"mock:bar"
argument_list|)
expr_stmt|;
name|bar
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// there should still be the same number of services
name|names
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|on
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|services
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// but as its recipient list which is dynamic-to we add new producers because we have register always
name|namesP
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|onP
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|namesP
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Removing 2nd route"
argument_list|)
expr_stmt|;
comment|// now remove the 2nd route
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|boolean
name|removed
init|=
name|context
operator|.
name|removeRoute
argument_list|(
literal|"bar"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|removed
argument_list|)
expr_stmt|;
comment|// there should still be the same number of services
name|names
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|on
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|services
argument_list|,
name|names
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// and we still have the other producers, but not the one from the 2nd route that was removed
name|namesP
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|onP
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|namesP
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Shutting down..."
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

