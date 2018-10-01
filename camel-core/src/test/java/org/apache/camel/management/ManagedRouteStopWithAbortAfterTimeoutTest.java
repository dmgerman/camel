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

begin_class
DECL|class|ManagedRouteStopWithAbortAfterTimeoutTest
specifier|public
class|class
name|ManagedRouteStopWithAbortAfterTimeoutTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testStopRouteWithAbortAfterTimeoutTrue ()
specifier|public
name|void
name|testStopRouteWithAbortAfterTimeoutTrue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX or windows CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
operator|||
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|mockEP
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mockEP
operator|.
name|setExpectedMessageCount
argument_list|(
literal|10
argument_list|)
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
name|getRouteObjectName
argument_list|(
name|mbeanServer
argument_list|)
decl_stmt|;
comment|// confirm that route has started
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
literal|"route should be started"
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
comment|//send some message through the route
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// stop route with a 1s timeout and abortAfterTimeout=true (should abort after 1s)
name|Long
name|timeout
init|=
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Boolean
name|abortAfterTimeout
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
name|Object
index|[]
name|params
init|=
block|{
name|timeout
block|,
name|abortAfterTimeout
block|}
decl_stmt|;
name|String
index|[]
name|sig
init|=
block|{
literal|"java.lang.Long"
block|,
literal|"java.lang.Boolean"
block|}
decl_stmt|;
name|Boolean
name|stopRouteResponse
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"stop"
argument_list|,
name|params
argument_list|,
name|sig
argument_list|)
decl_stmt|;
comment|// confirm that route is still running
name|state
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
literal|"State"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"stopRoute response should be False"
argument_list|,
name|stopRouteResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route should still be started"
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
comment|//send some more messages through the route
for|for
control|(
name|int
name|i
init|=
literal|5
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
literal|"seda:start"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|mockEP
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopRouteWithAbortAfterTimeoutFalse ()
specifier|public
name|void
name|testStopRouteWithAbortAfterTimeoutFalse
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX or windows CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
operator|||
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|MockEndpoint
name|mockEP
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|getRouteObjectName
argument_list|(
name|mbeanServer
argument_list|)
decl_stmt|;
comment|// confirm that route has started
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
literal|"route should be started"
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
comment|//send some message through the route
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|5
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
comment|// stop route with a 1s timeout and abortAfterTimeout=false (normal timeout behavior)
name|Long
name|timeout
init|=
operator|new
name|Long
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Boolean
name|abortAfterTimeout
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
name|Object
index|[]
name|params
init|=
block|{
name|timeout
block|,
name|abortAfterTimeout
block|}
decl_stmt|;
name|String
index|[]
name|sig
init|=
block|{
literal|"java.lang.Long"
block|,
literal|"java.lang.Boolean"
block|}
decl_stmt|;
name|Boolean
name|stopRouteResponse
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"stop"
argument_list|,
name|params
argument_list|,
name|sig
argument_list|)
decl_stmt|;
comment|// confirm that route is stopped
name|state
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
literal|"State"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"stopRoute response should be True"
argument_list|,
name|stopRouteResponse
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"route should be stopped"
argument_list|,
name|ServiceStatus
operator|.
name|Stopped
operator|.
name|name
argument_list|()
argument_list|,
name|state
argument_list|)
expr_stmt|;
comment|// send some more messages through the route
for|for
control|(
name|int
name|i
init|=
literal|5
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
literal|"seda:start"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not have received more than 5 messages"
argument_list|,
name|mockEP
operator|.
name|getExchanges
argument_list|()
operator|.
name|size
argument_list|()
operator|<=
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|getRouteObjectName (MBeanServer mbeanServer)
specifier|static
name|ObjectName
name|getRouteObjectName
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|"*:type=routes,*"
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
return|return
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
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
comment|// shutdown this test faster
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
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

