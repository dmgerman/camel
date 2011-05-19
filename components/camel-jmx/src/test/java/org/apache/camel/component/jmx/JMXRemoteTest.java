begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jmx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jmx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|LocateRegistry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|rmi
operator|.
name|registry
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|remote
operator|.
name|JMXConnectorServer
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
name|JMXConnectorServerFactory
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

begin_comment
comment|/**  * Tests against a "remote" JMX server. Creates an RMI Registry at port 61000  * and registers the simple mbean  *<p/>  * Only test here is the notification test since everything should work the  * same as the platform server. May want to refactor the exisiting tests to  * run the full suite on the local platform and this "remote" setup.  */
end_comment

begin_class
DECL|class|JMXRemoteTest
specifier|public
class|class
name|JMXRemoteTest
extends|extends
name|SimpleBeanFixture
block|{
DECL|field|url
name|JMXServiceURL
name|url
decl_stmt|;
DECL|field|connector
name|JMXConnectorServer
name|connector
decl_stmt|;
DECL|field|registry
name|Registry
name|registry
decl_stmt|;
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
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|initServer ()
specifier|protected
name|void
name|initServer
parameter_list|()
throws|throws
name|Exception
block|{
name|registry
operator|=
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
literal|61000
argument_list|)
expr_stmt|;
name|url
operator|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:61000/"
operator|+
name|DOMAIN
argument_list|)
expr_stmt|;
comment|// create MBean server
name|server
operator|=
name|MBeanServerFactory
operator|.
name|createMBeanServer
argument_list|(
name|DOMAIN
argument_list|)
expr_stmt|;
comment|// create JMXConnectorServer MBean
name|connector
operator|=
name|JMXConnectorServerFactory
operator|.
name|newJMXConnectorServer
argument_list|(
name|url
argument_list|,
name|Collections
operator|.
name|EMPTY_MAP
argument_list|,
name|server
argument_list|)
expr_stmt|;
name|connector
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|buildFromURI ()
specifier|protected
name|JMXUriBuilder
name|buildFromURI
parameter_list|()
block|{
name|String
name|uri
init|=
name|url
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|super
operator|.
name|buildFromURI
argument_list|()
operator|.
name|withServerName
argument_list|(
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|notification ()
specifier|public
name|void
name|notification
parameter_list|()
throws|throws
name|Exception
block|{
name|getSimpleMXBean
argument_list|()
operator|.
name|touch
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|waitForMessages
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|assertMessageReceived
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/consumer-test/touched.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

