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
name|Route
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
name|support
operator|.
name|RoutePolicySupport
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
DECL|class|ManagedRouteRestartTest
specifier|public
class|class
name|ManagedRouteRestartTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|myRoutePolicy
specifier|private
name|MyRoutePolicy
name|myRoutePolicy
init|=
operator|new
name|MyRoutePolicy
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRestartRoute ()
specifier|public
name|void
name|testRestartRoute
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
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|myRoutePolicy
operator|.
name|getStart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|myRoutePolicy
operator|.
name|getStop
argument_list|()
argument_list|)
expr_stmt|;
comment|// fire a message to get it running
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
name|boolean
name|registered
init|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should be registered"
argument_list|,
literal|true
argument_list|,
name|registered
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
comment|// the route has this starting endpoint uri
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
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
name|on
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
name|String
name|uptime
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
literal|"Uptime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uptime
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Uptime: {}"
argument_list|,
name|uptime
argument_list|)
expr_stmt|;
name|long
name|uptimeMillis
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
literal|"UptimeMillis"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|uptimeMillis
operator|>
literal|0
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"restart"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|registered
operator|=
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should be registered"
argument_list|,
literal|true
argument_list|,
name|registered
argument_list|)
expr_stmt|;
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
name|on
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|myRoutePolicy
operator|.
name|getStart
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|myRoutePolicy
operator|.
name|getStop
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
name|routePolicy
argument_list|(
name|myRoutePolicy
argument_list|)
operator|.
name|delayer
argument_list|(
literal|10
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
DECL|class|MyRoutePolicy
specifier|private
specifier|final
class|class
name|MyRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|start
specifier|private
name|int
name|start
decl_stmt|;
DECL|field|stop
specifier|private
name|int
name|stop
decl_stmt|;
annotation|@
name|Override
DECL|method|onStart (Route route)
specifier|public
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|start
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onStop (Route route)
specifier|public
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|stop
operator|++
expr_stmt|;
block|}
DECL|method|getStart ()
specifier|public
name|int
name|getStart
parameter_list|()
block|{
return|return
name|start
return|;
block|}
DECL|method|getStop ()
specifier|public
name|int
name|getStop
parameter_list|()
block|{
return|return
name|stop
return|;
block|}
block|}
block|}
end_class

end_unit

