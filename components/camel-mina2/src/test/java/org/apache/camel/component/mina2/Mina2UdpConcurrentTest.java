begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina2
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|DatagramSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|Mina2UdpConcurrentTest
specifier|public
class|class
name|Mina2UdpConcurrentTest
extends|extends
name|BaseMina2Test
block|{
DECL|field|messageCount
specifier|protected
name|int
name|messageCount
init|=
literal|3
decl_stmt|;
DECL|method|Mina2UdpConcurrentTest ()
specifier|public
name|Mina2UdpConcurrentTest
parameter_list|()
block|{     }
annotation|@
name|Test
DECL|method|testMinaRoute ()
specifier|public
name|void
name|testMinaRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello Message: 0"
argument_list|,
literal|"Hello Message: 1"
argument_list|,
literal|"Hello Message: 2"
argument_list|)
expr_stmt|;
name|sendUdpMessages
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendUdpMessages ()
specifier|protected
name|void
name|sendUdpMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|DatagramSocket
name|socket
init|=
operator|new
name|DatagramSocket
argument_list|()
decl_stmt|;
try|try
block|{
name|InetAddress
name|address
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
literal|"127.0.0.1"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageCount
condition|;
name|i
operator|++
control|)
block|{
name|String
name|text
init|=
literal|"Hello Message: "
operator|+
name|Integer
operator|.
name|toString
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|text
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|DatagramPacket
name|packet
init|=
operator|new
name|DatagramPacket
argument_list|(
name|data
argument_list|,
name|data
operator|.
name|length
argument_list|,
name|address
argument_list|,
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|socket
operator|.
name|send
argument_list|(
name|packet
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
comment|// we use un-ordered to allow processing the UDP messages in any order from same client
name|from
argument_list|(
literal|"mina2:udp://127.0.0.1:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"?sync=false&minaLogger=true&orderedThreadPoolExecutor=false"
argument_list|)
operator|.
name|delay
argument_list|(
literal|1000
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

