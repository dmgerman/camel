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
name|concurrent
operator|.
name|RejectedExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Attribute
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
name|builder
operator|.
name|NotifyBuilder
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
comment|/**  * @version  */
end_comment

begin_class
DECL|class|ManagedThrottlerTest
specifier|public
class|class
name|ManagedThrottlerTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testManageThrottler ()
specifier|public
name|void
name|testManageThrottler
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
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
comment|// Send in a first batch of 10 messages and check that the endpoint
comment|// gets them.  We'll check the total time of the second and third
comment|// batches as it seems that there is some time required to prime
comment|// things, which can vary significantly... particularly on slower
comment|// machines.
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
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
name|throttlerName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"mythrottler\""
argument_list|)
decl_stmt|;
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route1\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// send in 10 messages
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
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
name|routeName
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|timePeriod
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|throttlerName
argument_list|,
literal|"TimePeriodMillis"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|250
argument_list|,
name|timePeriod
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
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
name|routeName
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should take at most 1.0 sec: was "
operator|+
name|total
argument_list|,
name|total
operator|<
literal|1000
argument_list|)
expr_stmt|;
comment|// change the throttler using JMX
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|throttlerName
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"MaximumRequestsPerPeriod"
argument_list|,
operator|(
name|long
operator|)
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// send in another 10 messages
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|Long
name|period
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|throttlerName
argument_list|,
literal|"MaximumRequestsPerPeriod"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|period
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|period
operator|.
name|longValue
argument_list|()
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
name|routeName
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
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
name|routeName
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be around 1 sec now: was "
operator|+
name|total
argument_list|,
name|total
operator|>
literal|1000
argument_list|)
expr_stmt|;
block|}
DECL|method|testThrottleVisableViaJmx ()
specifier|public
name|void
name|testThrottleVisableViaJmx
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
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
comment|// windows needs more sleep to read updated jmx values so we skip as we dont want further delays in core tests
return|return;
block|}
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route2\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|NotifyBuilder
name|notifier
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|from
argument_list|(
literal|"seda:throttleCount"
argument_list|)
operator|.
name|whenReceived
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:throttleCount"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|notifier
operator|.
name|matches
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|routeName
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testThrottleAsyncVisableViaJmx ()
specifier|public
name|void
name|testThrottleAsyncVisableViaJmx
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
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
comment|// windows needs more sleep to read updated jmx values so we skip as we dont want further delays in core tests
return|return;
block|}
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route3\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:endAsync"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
comment|// we pick '5' because we are right in the middle of the number of messages
comment|// that have been and reduces any race conditions to minimal...
name|NotifyBuilder
name|notifier
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|from
argument_list|(
literal|"seda:throttleCountAsync"
argument_list|)
operator|.
name|whenReceived
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:throttleCountAsync"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|notifier
operator|.
name|matches
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
name|routeName
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testThrottleAsyncExceptionVisableViaJmx ()
specifier|public
name|void
name|testThrottleAsyncExceptionVisableViaJmx
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
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
comment|// windows needs more sleep to read updated jmx values so we skip as we dont want further delays in core tests
return|return;
block|}
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route4\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:endAsyncException"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|NotifyBuilder
name|notifier
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|from
argument_list|(
literal|"seda:throttleCountAsyncException"
argument_list|)
operator|.
name|whenReceived
argument_list|(
literal|5
argument_list|)
operator|.
name|create
argument_list|()
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:throttleCountAsyncException"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|notifier
operator|.
name|matches
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// give a sec for exception handling to finish..
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
comment|// since all exchanges ended w/ exception, they are not completed
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
name|routeName
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRejectedExecution ()
specifier|public
name|void
name|testRejectedExecution
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when delaying async, we can possibly fill up the execution queue
comment|//. which would through a RejectedExecutionException.. we need to make
comment|// sure that the delayedCount/throttledCount doesn't leak
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
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route2\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endAsyncReject"
argument_list|)
decl_stmt|;
comment|// only one message (the first one) should get through because the rest should get delayed
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|MockEndpoint
name|exceptionMock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:rejectedExceptionEndpoint1"
argument_list|)
decl_stmt|;
name|exceptionMock
operator|.
name|expectedMessageCount
argument_list|(
literal|9
argument_list|)
expr_stmt|;
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:throttleCountRejectExecution"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testRejectedExecutionCallerRuns ()
specifier|public
name|void
name|testRejectedExecutionCallerRuns
parameter_list|()
throws|throws
name|Exception
block|{
comment|// when delaying async, we can possibly fill up the execution queue
comment|//. which would through a RejectedExecutionException.. we need to make
comment|// sure that the delayedCount/throttledCount doesn't leak
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
comment|// use route to get the total time
name|ObjectName
name|routeName
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route2\""
argument_list|)
decl_stmt|;
comment|// reset the counters
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|routeName
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:endAsyncRejectCallerRuns"
argument_list|)
decl_stmt|;
comment|// only one message (the first one) should get through because the rest should get delayed
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|MockEndpoint
name|exceptionMock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:rejectedExceptionEndpoint"
argument_list|)
decl_stmt|;
name|exceptionMock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
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
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:throttleCountRejectExecutionCallerRuns"
argument_list|,
literal|"Message "
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
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
specifier|final
name|ScheduledExecutorService
name|badService
init|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|1
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|schedule
parameter_list|(
name|Runnable
name|command
parameter_list|,
name|long
name|delay
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
throw|throw
operator|new
name|RejectedExecutionException
argument_list|()
throw|;
block|}
block|}
decl_stmt|;
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
name|throttle
argument_list|(
literal|10
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:throttleCount"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:throttleCountAsync"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|asyncDelayed
argument_list|()
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endAsync"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:throttleCountAsyncException"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|asyncDelayed
argument_list|()
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler4"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endAsyncException"
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
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Fail me"
argument_list|)
throw|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:throttleCountRejectExecutionCallerRuns"
argument_list|)
operator|.
name|onException
argument_list|(
name|RejectedExecutionException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:rejectedExceptionEndpoint1"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|asyncDelayed
argument_list|()
operator|.
name|executorService
argument_list|(
name|badService
argument_list|)
operator|.
name|callerRunsWhenRejected
argument_list|(
literal|true
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endAsyncRejectCallerRuns"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:throttleCountRejectExecution"
argument_list|)
operator|.
name|onException
argument_list|(
name|RejectedExecutionException
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:rejectedExceptionEndpoint1"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|250
argument_list|)
operator|.
name|asyncDelayed
argument_list|()
operator|.
name|executorService
argument_list|(
name|badService
argument_list|)
operator|.
name|callerRunsWhenRejected
argument_list|(
literal|false
argument_list|)
operator|.
name|id
argument_list|(
literal|"mythrottler6"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:endAsyncReject"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

