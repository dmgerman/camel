begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
package|;
end_package

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
name|After
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerFactory
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
DECL|class|JettyEnableJmxTest
specifier|public
class|class
name|JettyEnableJmxTest
extends|extends
name|BaseJettyTest
block|{
DECL|field|serverUri0
specifier|private
name|String
name|serverUri0
decl_stmt|;
DECL|field|serverUri1
specifier|private
name|String
name|serverUri1
decl_stmt|;
DECL|field|serverUri2
specifier|private
name|String
name|serverUri2
decl_stmt|;
DECL|field|serverUri3
specifier|private
name|String
name|serverUri3
decl_stmt|;
DECL|field|mbsc
specifier|private
name|MBeanServerConnection
name|mbsc
decl_stmt|;
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|releaseMBeanServers
argument_list|()
expr_stmt|;
name|mbsc
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|disableJMX
argument_list|()
expr_stmt|;
block|}
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
name|enableJMX
argument_list|()
expr_stmt|;
name|releaseMBeanServers
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mbsc
operator|=
name|getMBeanConnection
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEnableJmxProperty ()
specifier|public
name|void
name|testEnableJmxProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|String
name|expectedBody
init|=
literal|"<html><body>foo</body></html>"
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|,
name|expectedBody
argument_list|,
name|expectedBody
argument_list|,
name|expectedBody
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"x"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri0
operator|+
literal|"&x=foo"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri1
operator|+
literal|"&x=foo"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri2
operator|+
literal|"&x=foo"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
name|serverUri3
operator|+
literal|"&x=foo"
argument_list|,
literal|null
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 Jetty Server: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testShutdown ()
specifier|public
name|void
name|testShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 Jetty Server: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 0 Jetty Server: "
operator|+
name|s
argument_list|,
literal|0
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEndpointDisconnect ()
specifier|public
name|void
name|testEndpointDisconnect
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 Jetty Server: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"route0"
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 Jetty Server: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"route2"
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"route3"
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 Jetty Server: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"route1"
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"org.eclipse.jetty.server:type=server,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 0 Jetty Server: "
operator|+
name|s
argument_list|,
literal|0
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|serverUri0
operator|=
literal|"http://localhost:"
operator|+
name|getNextPort
argument_list|()
operator|+
literal|"/myservice?enableJmx=true"
expr_stmt|;
name|serverUri1
operator|=
literal|"http://localhost:"
operator|+
name|getNextPort
argument_list|()
operator|+
literal|"/myservice?enableJmx=true"
expr_stmt|;
name|serverUri2
operator|=
literal|"http://localhost:"
operator|+
name|getNextPort
argument_list|()
operator|+
literal|"/myservice?enableJmx=false"
expr_stmt|;
name|serverUri3
operator|=
literal|"http://localhost:"
operator|+
name|getNextPort
argument_list|()
operator|+
literal|"/myservice?enableJmx=false"
expr_stmt|;
name|from
argument_list|(
literal|"jetty:"
operator|+
name|serverUri0
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route0"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<html><body>${in.header.x}</body></html>"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:"
operator|+
name|serverUri1
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route1"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<html><body>${in.header.x}</body></html>"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:"
operator|+
name|serverUri2
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route2"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<html><body>${in.header.x}</body></html>"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"jetty:"
operator|+
name|serverUri3
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route3"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|simple
argument_list|(
literal|"<html><body>${in.header.x}</body></html>"
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
DECL|method|releaseMBeanServers ()
specifier|protected
name|void
name|releaseMBeanServers
parameter_list|()
block|{
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|MBeanServer
name|server
range|:
name|servers
control|)
block|{
name|MBeanServerFactory
operator|.
name|releaseMBeanServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMBeanConnection ()
specifier|protected
name|MBeanServerConnection
name|getMBeanConnection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|mbsc
operator|==
literal|null
condition|)
block|{
name|mbsc
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
block|}
return|return
name|mbsc
return|;
block|}
block|}
end_class

end_unit

