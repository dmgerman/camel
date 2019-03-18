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
name|Before
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
DECL|class|ManagedRouteStopAndStartTest
specifier|public
class|class
name|ManagedRouteStopAndStartTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/data/managed"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStopAndStartRoute ()
specifier|public
name|void
name|testStopAndStartRoute
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
name|getRouteObjectName
argument_list|(
name|mbeanServer
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/managed"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
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
name|String
name|id
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
literal|"foo"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|String
name|description
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
literal|"Description"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"This is the foo route"
argument_list|,
name|description
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
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// just wait a little bit
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/data/managed"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
comment|// route is stopped so we do not get the file
name|mock
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
comment|// prepare mock for starting route
name|mock
operator|.
name|reset
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
comment|// start
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
comment|// this time the file is consumed
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
name|from
argument_list|(
literal|"file://target/data/managed?initialDelay=0&delay=10"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|routeDescription
argument_list|(
literal|"This is the foo route"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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

