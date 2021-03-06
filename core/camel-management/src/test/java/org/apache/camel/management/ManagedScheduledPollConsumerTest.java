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
name|java
operator|.
name|util
operator|.
name|Set
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
DECL|class|ManagedScheduledPollConsumerTest
specifier|public
class|class
name|ManagedScheduledPollConsumerTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testScheduledPollConsumer ()
specifier|public
name|void
name|testScheduledPollConsumer
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
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=consumers,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ObjectName
name|on
init|=
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
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
literal|"EndpointUri"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"file://target/data/foo?backoffErrorThreshold=3&backoffIdleThreshold=2&backoffMultiplier=4&delay=4000"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|Long
name|delay
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
literal|"Delay"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4000
argument_list|,
name|delay
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|initialDelay
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
literal|"InitialDelay"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|initialDelay
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Boolean
name|fixedDelay
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"UseFixedDelay"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|fixedDelay
argument_list|)
expr_stmt|;
name|Boolean
name|schedulerStarted
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"SchedulerStarted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|schedulerStarted
argument_list|)
expr_stmt|;
name|String
name|timeUnit
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
literal|"TimeUnit"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|toString
argument_list|()
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
name|Integer
name|backoffMultiplier
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"BackoffMultiplier"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|backoffMultiplier
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|backoffCounter
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"BackoffCounter"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|backoffCounter
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|backoffIdleThreshold
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"BackoffIdleThreshold"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|backoffIdleThreshold
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|backoffErrorThreshold
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"BackoffErrorThreshold"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|backoffErrorThreshold
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// stop it
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"stop"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|schedulerStarted
operator|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"SchedulerStarted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|schedulerStarted
argument_list|)
expr_stmt|;
comment|// change delay
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"Delay"
argument_list|,
literal|2000
argument_list|)
argument_list|)
expr_stmt|;
comment|// start it
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"start"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|delay
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
literal|"Delay"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|delay
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// change some options
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"UseFixedDelay"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
name|fixedDelay
operator|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"UseFixedDelay"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|fixedDelay
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"TimeUnit"
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|timeUnit
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"TimeUnit"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|toString
argument_list|()
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|setAttribute
argument_list|(
name|on
argument_list|,
operator|new
name|Attribute
argument_list|(
literal|"InitialDelay"
argument_list|,
name|Long
operator|.
name|valueOf
argument_list|(
literal|"2000"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|initialDelay
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
literal|"InitialDelay"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2000
argument_list|,
name|initialDelay
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should no longer be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
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
literal|"file://target/data/foo?delay=4000&backoffMultiplier=4&backoffIdleThreshold=2&backoffErrorThreshold=3"
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

