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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|ManagementStatisticsLevel
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedRoutingSlipTest
specifier|public
class|class
name|ManagedRoutingSlipTest
extends|extends
name|ManagementTestSupport
block|{
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
name|setStatisticsLevel
argument_list|(
name|ManagementStatisticsLevel
operator|.
name|Extended
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testManageRoutingSlip ()
specifier|public
name|void
name|testManageRoutingSlip
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
name|MockEndpoint
name|foo
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|foo
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
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
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"whereTo"
argument_list|,
literal|"direct:foo,direct:foo,direct:bar"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// get the object name for the delayer
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"mysend\""
argument_list|)
decl_stmt|;
comment|// should be on route1
name|String
name|routeId
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"RouteId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"route1"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
name|String
name|camelId
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|camelId
argument_list|)
expr_stmt|;
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
name|on
argument_list|,
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
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
name|String
name|lan
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ExpressionLanguage"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"header"
argument_list|,
name|lan
argument_list|)
expr_stmt|;
name|String
name|uri
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"Expression"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"whereTo"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|TabularData
name|data
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"endpointStatistics"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"explain"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|false
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|data
operator|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"explain"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|json
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"informationJson"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"description\": \"Routes a message through a series of steps that are pre-determined (the slip)"
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
literal|"direct:start"
argument_list|)
operator|.
name|routingSlip
argument_list|(
name|header
argument_list|(
literal|"whereTo"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"mysend"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
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

