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
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXConnectorFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|remote
operator|.
name|JMXServiceURL
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
name|CamelContext
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
name|After
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
DECL|class|ManagedServiceUrlPathTest
specifier|public
class|class
name|ManagedServiceUrlPathTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|JMXSERVICEURL
specifier|private
specifier|static
specifier|final
name|String
name|JMXSERVICEURL
init|=
literal|"service:jmx:rmi:///jndi/rmi://localhost:2113/foo/bar"
decl_stmt|;
DECL|field|clientConnector
specifier|private
name|JMXConnector
name|clientConnector
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
comment|// START SNIPPET: e1
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setServiceUrlPath
argument_list|(
literal|"/foo/bar"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setRegistryPort
argument_list|(
literal|2113
argument_list|)
expr_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setCreateConnector
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|canRunOnThisPlatform ()
specifier|protected
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
comment|// does not work well when maven surefire plugin is set to forkmode=once
return|return
literal|false
return|;
block|}
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
try|try
block|{
if|if
condition|(
name|clientConnector
operator|!=
literal|null
condition|)
block|{
name|clientConnector
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
annotation|@
name|Test
DECL|method|testConnectToJmx ()
specifier|public
name|void
name|testConnectToJmx
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
name|clientConnector
operator|=
name|JMXConnectorFactory
operator|.
name|connect
argument_list|(
operator|new
name|JMXServiceURL
argument_list|(
name|JMXSERVICEURL
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|MBeanServerConnection
name|mbeanServer
init|=
name|clientConnector
operator|.
name|getMBeanServerConnection
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"direct://start\""
argument_list|)
decl_stmt|;
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
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"direct://start"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"log://foo\""
argument_list|)
expr_stmt|;
name|uri
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"log://foo"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"mock://result\""
argument_list|)
expr_stmt|;
name|uri
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"EndpointUri"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"mock://result"
argument_list|,
name|uri
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
name|name
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|Boolean
name|singleton
init|=
operator|(
name|Boolean
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"Singleton"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|singleton
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

