begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|api
operator|.
name|management
operator|.
name|ManagedCamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedSendProcessorMBean
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedStepMBean
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ManagedStepTest
specifier|public
class|class
name|ManagedStepTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testManageStep ()
specifier|public
name|void
name|testManageStep
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
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
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
literal|"foo"
argument_list|,
literal|"123"
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
literal|"org.apache.camel:context=camel-1,type=steps,name=\"foo\""
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
comment|// should be step foo
name|String
name|stepId
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
literal|"StepId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|stepId
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
name|ManagedCamelContext
name|mcc
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|ManagedStepMBean
name|step
init|=
name|mcc
operator|.
name|getManagedStep
argument_list|(
literal|"foo"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|step
operator|.
name|getProcessorId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|step
operator|.
name|getExchangesCompleted
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|.
name|dumpStepStatsAsXml
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<stepStat id=\"foo\""
argument_list|)
argument_list|)
expr_stmt|;
name|xml
operator|=
name|mcc
operator|.
name|getManagedCamelContext
argument_list|()
operator|.
name|dumpStepStatsAsXml
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"<stepStat id=\"foo\""
argument_list|)
argument_list|)
expr_stmt|;
comment|// the processors within the step should point-back to the parent step
name|ManagedSendProcessorMBean
name|mp
init|=
name|mcc
operator|.
name|getManagedProcessor
argument_list|(
literal|"abc"
argument_list|,
name|ManagedSendProcessorMBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|mp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route1"
argument_list|,
name|mp
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|mp
operator|.
name|getStepId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|mp
operator|.
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
name|mp
operator|=
name|mcc
operator|.
name|getManagedProcessor
argument_list|(
literal|"def"
argument_list|,
name|ManagedSendProcessorMBean
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|mp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route1"
argument_list|,
name|mp
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|mp
operator|.
name|getStepId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://foo"
argument_list|,
name|mp
operator|.
name|getDestination
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|step
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
literal|"abc"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"def"
argument_list|)
operator|.
name|end
argument_list|()
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

