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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

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
name|ExecutorService
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
name|Executors
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedRouteRemoveWireTapExplicitThreadPoolTest
specifier|public
class|class
name|ManagedRouteRemoveWireTapExplicitThreadPoolTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|myThreadPool
specifier|private
name|ExecutorService
name|myThreadPool
decl_stmt|;
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
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
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=routes,name=\"foo\""
argument_list|)
decl_stmt|;
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
name|getMockEndpoint
argument_list|(
literal|"mock:tap"
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
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
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
comment|// and no wire tap thread pool as we use an existing external pool
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
literal|"*:type=threadpools,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|boolean
name|wireTap
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|set
control|)
block|{
if|if
condition|(
name|name
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"wireTap"
argument_list|)
condition|)
block|{
name|wireTap
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertFalse
argument_list|(
literal|"Should not have a wire tap thread pool"
argument_list|,
name|wireTap
argument_list|)
expr_stmt|;
comment|// stop
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
literal|"Should be stopped"
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
comment|// remove
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"remove"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// should not be registered anymore
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
name|assertFalse
argument_list|(
literal|"Route mbean should have been unregistered"
argument_list|,
name|registered
argument_list|)
expr_stmt|;
comment|// and no wire tap thread pool as we use an existing external pool
name|set
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=threadpools,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|wireTap
operator|=
literal|false
expr_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|set
control|)
block|{
if|if
condition|(
name|name
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"wireTap"
argument_list|)
condition|)
block|{
name|wireTap
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
name|assertFalse
argument_list|(
literal|"Should not have a wire tap thread pool"
argument_list|,
name|wireTap
argument_list|)
expr_stmt|;
comment|// should not be shutdown
name|assertFalse
argument_list|(
literal|"Thread pool should not be shutdown"
argument_list|,
name|myThreadPool
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|myThreadPool
operator|.
name|shutdownNow
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
comment|// create a new thread pool to use for wire tap
name|myThreadPool
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|wireTap
argument_list|(
literal|"direct:tap"
argument_list|,
name|myThreadPool
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:tap"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"tap"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:tap"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

