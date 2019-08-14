begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|AfterEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeEach
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|fail
import|;
end_import

begin_comment
comment|/**  * Test to verify:  *   * 1.  The JMX consumer can actively connect (via polling) to a JMX server that is not listening   *     for connections when the route is started  *       * 2.  The JMX consumer can detect a lost JMX connection, and will reconnect to the JMX server  *     when the server is listening for connections again on the configured port  */
end_comment

begin_class
DECL|class|JMXRobustRemoteConnectionTest
specifier|public
class|class
name|JMXRobustRemoteConnectionTest
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
DECL|field|port
name|int
name|port
decl_stmt|;
annotation|@
name|BeforeEach
annotation|@
name|Override
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|url
operator|=
operator|new
name|JMXServiceURL
argument_list|(
literal|"service:jmx:rmi:///jndi/rmi://localhost:"
operator|+
name|port
operator|+
literal|"/"
operator|+
name|DOMAIN
argument_list|)
expr_stmt|;
name|initContext
argument_list|()
expr_stmt|;
name|startContext
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|AfterEach
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
name|Override
DECL|method|initServer ()
specifier|protected
name|void
name|initServer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
name|LocateRegistry
operator|.
name|createRegistry
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
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
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
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
operator|.
name|withTestConnectionOnStartup
argument_list|(
literal|false
argument_list|)
operator|.
name|withReconnectDelay
argument_list|(
literal|1
argument_list|)
operator|.
name|withReconnectOnConnectionFailure
argument_list|(
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRobustConnection ()
specifier|public
name|void
name|testRobustConnection
parameter_list|()
throws|throws
name|Exception
block|{
comment|// the JMX service should not be started
try|try
block|{
name|getSimpleMXBean
argument_list|()
operator|.
name|touch
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"The mxbean should not be available."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|e
operator|instanceof
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Null connection"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// start the server;  the JMX consumer should connect and start;  the mock should receive a notification
name|initServer
argument_list|()
expr_stmt|;
name|initBean
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
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
comment|// stop the server; the JMX consumer should lose connectivity and the mock will not receive notifications
name|connector
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|resetMockEndpoint
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|getMockEndpoint
argument_list|()
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getSimpleMXBean
argument_list|()
operator|.
name|touch
argument_list|()
expr_stmt|;
name|getMockFixture
argument_list|()
operator|.
name|getMockEndpoint
argument_list|()
operator|.
name|assertIsNotSatisfied
argument_list|()
expr_stmt|;
comment|// restart the server;  the JMX consumer should re-connect and the mock should receive a notification
name|initServer
argument_list|()
expr_stmt|;
name|initBean
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
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

