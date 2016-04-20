begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hystrix.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hystrix
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|hystrix
operator|.
name|metrics
operator|.
name|HystrixEventStreamService
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|HystrixManagementTest
specifier|public
class|class
name|HystrixManagementTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|stream
specifier|private
name|HystrixEventStreamService
name|stream
init|=
operator|new
name|HystrixEventStreamService
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|getMBeanServer ()
specifier|protected
name|MBeanServer
name|getMBeanServer
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testHystrix ()
specifier|public
name|void
name|testHystrix
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
comment|// look inside jmx
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// context name
name|String
name|name
init|=
name|context
operator|.
name|getManagementName
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
literal|"org.apache.camel:context="
operator|+
name|name
operator|+
literal|",type=processors,name=\"myHystrix\""
argument_list|)
decl_stmt|;
comment|// should be on start
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
literal|"start"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
comment|// should be id of the node
name|String
name|commandKey
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
literal|"HystrixCommandKey"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myHystrix"
argument_list|,
name|commandKey
argument_list|)
expr_stmt|;
name|String
name|groupKey
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
literal|"HystrixGroupKey"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CamelHystrix"
argument_list|,
name|groupKey
argument_list|)
expr_stmt|;
comment|// these metrics need a little time before updating
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|Long
name|totalRequests
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
literal|"HystrixTotalRequests"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|totalRequests
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|errorCount
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
literal|"HystrixErrorCount"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|errorCount
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// let it gather for a while
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|String
name|latest
init|=
name|stream
operator|.
name|oldestMetricsAsJSon
argument_list|()
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Oldest json stream: {}"
argument_list|,
name|latest
argument_list|)
expr_stmt|;
name|Stream
argument_list|<
name|String
argument_list|>
name|jsons
init|=
name|stream
operator|.
name|streamMetrics
argument_list|()
decl_stmt|;
name|jsons
operator|.
name|forEach
argument_list|(
name|s
lambda|->
block|{
name|log
operator|.
name|info
argument_list|(
literal|"JSon: {}"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
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
comment|// add the stream
name|stream
operator|.
name|setQueueSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|stream
operator|.
name|setDelay
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|stream
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"start"
argument_list|)
operator|.
name|hystrix
argument_list|()
operator|.
name|id
argument_list|(
literal|"myHystrix"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|onFallback
argument_list|()
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Fallback message"
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
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

