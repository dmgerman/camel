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
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|Test
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
DECL|field|mllpPort
name|int
name|mllpPort
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
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|mllpPort
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-receiver"
decl_stmt|;
name|String
name|host
init|=
literal|"0.0.0.0"
decl_stmt|;
name|int
name|port
init|=
name|mllpPort
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp:%d?autoAck=false"
argument_list|,
name|port
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
return|;
block|}
comment|/**      * Simulate a Load Balancer Probe      *<p/>      * Load Balancers check the status of a port by establishing and closing a TCP connection periodically.  The time      * between these probes can normally be configured, but it is typically set to about 15-sec.  Since there could be      * a large number of port that are being probed, the logging from the connect/disconnect operations can drown-out      * more useful information.      *<p/>      * Watch the logs when running this test to verify that the log output will be acceptable when a load balancer      * is probing the port.      *<p/>      * TODO:  Need to add a custom Log4j Appender that can verify the logging is acceptable      *      * @throws Exception      */
annotation|@
name|Test
DECL|method|testConnectWithoutData ()
specifier|public
name|void
name|testConnectWithoutData
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|setExpectedCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|int
name|connectionCount
init|=
literal|10
decl_stmt|;
name|Socket
name|dummyLoadBalancerSocket
init|=
literal|null
decl_stmt|;
name|SocketAddress
name|address
init|=
operator|new
name|InetSocketAddress
argument_list|(
literal|"localhost"
argument_list|,
name|mllpPort
argument_list|)
decl_stmt|;
name|int
name|connectTimeout
init|=
literal|5000
decl_stmt|;
try|try
block|{
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
name|log
operator|.
name|debug
argument_list|(
literal|"Creating connection #{}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|dummyLoadBalancerSocket
operator|=
operator|new
name|Socket
argument_list|()
expr_stmt|;
name|dummyLoadBalancerSocket
operator|.
name|connect
argument_list|(
name|address
argument_list|,
name|connectTimeout
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Closing connection #{}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|dummyLoadBalancerSocket
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
literal|null
operator|!=
name|dummyLoadBalancerSocket
condition|)
block|{
try|try
block|{
name|dummyLoadBalancerSocket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Exception encountered closing dummy load balancer socket"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
end_class

end_unit

