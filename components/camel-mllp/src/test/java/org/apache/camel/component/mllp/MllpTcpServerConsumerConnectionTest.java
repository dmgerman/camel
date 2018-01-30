begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
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
name|TimeUnit
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
name|EndpointInject
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
name|LoggingLevel
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpClientResource
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
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpJUnitResourceException
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
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|anyOf
import|;
end_import

begin_class
DECL|class|MllpTcpServerConsumerConnectionTest
specifier|public
class|class
name|MllpTcpServerConsumerConnectionTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|RECEIVE_TIMEOUT
specifier|static
specifier|final
name|int
name|RECEIVE_TIMEOUT
init|=
literal|1000
decl_stmt|;
DECL|field|READ_TIMEOUT
specifier|static
specifier|final
name|int
name|READ_TIMEOUT
init|=
literal|500
decl_stmt|;
annotation|@
name|Rule
DECL|field|mllpClient
specifier|public
name|MllpClientResource
name|mllpClient
init|=
operator|new
name|MllpClientResource
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock://result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|mllpClient
operator|.
name|setMllpHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|setMllpPort
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
block|}
comment|/**      * Simulate a Load Balancer Probe      *<p/>      * Load Balancers check the status of a port by establishing and closing a TCP connection periodically.  The time between these probes can normally be configured, but it is typically set to about      * 15-sec.  Since there could be a large number of port that are being probed, the logging from the connect/disconnect operations can drown-out more useful information.      *<p/>      * Watch the logs when running this test to verify that the log output will be acceptable when a load balancer is probing the port.      *<p/>      * TODO:  Need to add a custom Log4j Appender that can verify the logging is acceptable      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|testConnectThenCloseWithoutData ()
specifier|public
name|void
name|testConnectThenCloseWithoutData
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|connectionCount
init|=
literal|10
decl_stmt|;
name|long
name|connectionMillis
init|=
literal|200
decl_stmt|;
name|result
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|addTestRouteWithIdleTimeout
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|connectionCount
condition|;
operator|++
name|i
control|)
block|{
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|connectionMillis
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// Connect one more time and allow a client thread to start
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|close
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectThenResetWithoutData ()
specifier|public
name|void
name|testConnectThenResetWithoutData
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|connectionCount
init|=
literal|10
decl_stmt|;
name|long
name|connectionMillis
init|=
literal|200
decl_stmt|;
name|result
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|addTestRouteWithIdleTimeout
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|connectionCount
condition|;
operator|++
name|i
control|)
block|{
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|connectionMillis
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
comment|// Connect one more time and allow a client thread to start
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
comment|/**      * Simulate an Idle Client      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|testIdleConnection ()
specifier|public
name|void
name|testIdleConnection
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|idleTimeout
init|=
name|RECEIVE_TIMEOUT
operator|*
literal|3
decl_stmt|;
name|String
name|testMessage
init|=
literal|"MSH|^~\\&|ADT|EPIC|JCAPS|CC|20160902123950|RISTECH|ADT^A08|00001|D|2.3|||||||"
operator|+
literal|'\r'
operator|+
literal|'\n'
decl_stmt|;
name|result
operator|.
name|setExpectedCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|addTestRouteWithIdleTimeout
argument_list|(
name|idleTimeout
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|idleTimeout
operator|+
name|RECEIVE_TIMEOUT
argument_list|)
expr_stmt|;
try|try
block|{
name|mllpClient
operator|.
name|checkConnection
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"The MllpClientResource should have thrown an exception when writing to the reset socket"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpJUnitResourceException
name|expectedEx
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"checkConnection failed - read() returned END_OF_STREAM"
argument_list|,
name|expectedEx
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|expectedEx
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|(
literal|15
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|addTestRouteWithIdleTimeout (final int idleTimeout)
name|void
name|addTestRouteWithIdleTimeout
parameter_list|(
specifier|final
name|int
name|idleTimeout
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-receiver-with-timeout"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp://%s:%d?receiveTimeout=%d&readTimeout=%d&idleTimeout=%d"
argument_list|,
name|mllpClient
operator|.
name|getMllpHost
argument_list|()
argument_list|,
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
argument_list|,
name|RECEIVE_TIMEOUT
argument_list|,
name|READ_TIMEOUT
argument_list|,
name|idleTimeout
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Receiving: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

