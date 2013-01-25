begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.syslog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|syslog
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|spring
operator|.
name|CamelSpringTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SyslogSpringNettyTest
specifier|public
class|class
name|SyslogSpringNettyTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|serverPort
specifier|private
specifier|static
name|int
name|serverPort
decl_stmt|;
DECL|field|messageCount
specifier|private
specifier|final
name|int
name|messageCount
init|=
literal|1
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|String
name|message
init|=
literal|"<165>Aug  4 05:34:00 mymachine myproc[10]: %% It's\n         time to make the do-nuts.  %%  Ingredients: Mix=OK, Jelly=OK #\n"
operator|+
literal|"         Devices: Mixer=OK, Jelly_Injector=OK, Frier=OK # Transport:\n"
operator|+
literal|"         Conveyer1=OK, Conveyer2=OK # %%"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
block|{
name|serverPort
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"server-port"
argument_list|,
operator|new
name|Integer
argument_list|(
name|serverPort
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/syslog/applicationContext-Netty.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testSendingRawUDP ()
specifier|public
name|void
name|testSendingRawUDP
parameter_list|()
throws|throws
name|IOException
throws|,
name|InterruptedException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:stop1"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:stop2"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock2
operator|.
name|expectedBodiesReceived
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|byte
index|[]
name|data
init|=
name|message
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
name|serverPort
argument_list|)
decl_stmt|;
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
literal|100
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

