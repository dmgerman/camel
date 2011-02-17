begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|Processor
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|NettyUdpWithInOutUsingPlainSocketTest
specifier|public
class|class
name|NettyUdpWithInOutUsingPlainSocketTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|NettyUdpWithInOutUsingPlainSocketTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|PORT
specifier|private
specifier|static
specifier|final
name|int
name|PORT
init|=
literal|4445
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendAndReceiveOnce ()
specifier|public
name|void
name|testSendAndReceiveOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|sendAndReceiveUdpMessages
argument_list|(
literal|"World"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"should receive data"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World\n"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|sendAndReceiveUdpMessages (String input)
specifier|private
name|String
name|sendAndReceiveUdpMessages
parameter_list|(
name|String
name|input
parameter_list|)
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
comment|// must append delimiter
name|byte
index|[]
name|data
init|=
operator|(
name|input
operator|+
literal|"\n"
operator|)
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
name|PORT
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"+++ Sending data +++"
argument_list|)
expr_stmt|;
name|socket
operator|.
name|send
argument_list|(
name|packet
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|128
index|]
decl_stmt|;
name|DatagramPacket
name|receive
init|=
operator|new
name|DatagramPacket
argument_list|(
name|buf
argument_list|,
name|buf
operator|.
name|length
argument_list|,
name|address
argument_list|,
name|PORT
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"+++ Receiving data +++"
argument_list|)
expr_stmt|;
name|socket
operator|.
name|receive
argument_list|(
name|receive
argument_list|)
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
operator|new
name|String
argument_list|(
name|receive
operator|.
name|getData
argument_list|()
argument_list|,
literal|0
argument_list|,
name|receive
operator|.
name|getLength
argument_list|()
argument_list|)
return|;
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
name|from
argument_list|(
literal|"netty:udp://127.0.0.1:"
operator|+
name|PORT
operator|+
literal|"?textline=true&sync=true"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|s
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Server got: "
operator|+
name|s
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

