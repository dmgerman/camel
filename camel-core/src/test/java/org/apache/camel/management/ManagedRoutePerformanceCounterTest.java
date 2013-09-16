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
name|Date
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedRoutePerformanceCounterTest
specifier|public
class|class
name|ManagedRoutePerformanceCounterTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testPerformanceCounterStats ()
specifier|public
name|void
name|testPerformanceCounterStats
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
comment|// get the stats for the route
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
literal|"org.apache.camel:context=localhost/camel-1,type=routes,name=\"route1\""
argument_list|)
decl_stmt|;
name|Long
name|delta
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DeltaProcessingTime"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|delta
operator|.
name|intValue
argument_list|()
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
name|asyncSendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// cater for slow boxes
name|Integer
name|inFlight
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|inFlight
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"InflightExchanges"
argument_list|)
expr_stmt|;
if|if
condition|(
name|inFlight
operator|.
name|longValue
argument_list|()
operator|==
literal|1
condition|)
block|{
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"too slow server"
argument_list|,
name|inFlight
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|inFlight
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|Long
name|completed
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|delta
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DeltaProcessingTime"
argument_list|)
expr_stmt|;
name|Long
name|last
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LastProcessingTime"
argument_list|)
decl_stmt|;
name|Long
name|total
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|delta
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take around 3 sec: was "
operator|+
name|last
argument_list|,
name|last
operator|>
literal|2900
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take around 3 sec: was "
operator|+
name|total
argument_list|,
name|total
operator|>
literal|2900
argument_list|)
expr_stmt|;
comment|// send in another message
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|delta
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"DeltaProcessingTime"
argument_list|)
expr_stmt|;
name|last
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LastProcessingTime"
argument_list|)
expr_stmt|;
name|total
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|delta
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should take around 3 sec: was "
operator|+
name|last
argument_list|,
name|last
operator|>
literal|2900
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be around 5 sec now: was "
operator|+
name|total
argument_list|,
name|total
operator|>
literal|4900
argument_list|)
expr_stmt|;
name|Date
name|reset
init|=
operator|(
name|Date
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"ResetTimestamp"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|reset
argument_list|)
expr_stmt|;
name|Date
name|lastFailed
init|=
operator|(
name|Date
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"LastExchangeFailureTimestamp"
argument_list|)
decl_stmt|;
name|Date
name|firstFailed
init|=
operator|(
name|Date
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"FirstExchangeFailureTimestamp"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|lastFailed
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|firstFailed
argument_list|)
expr_stmt|;
name|inFlight
operator|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"InflightExchanges"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|inFlight
operator|.
name|longValue
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
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|3000
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

