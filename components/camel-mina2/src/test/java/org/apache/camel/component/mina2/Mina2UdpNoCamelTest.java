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
name|SocketException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|UnknownHostException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|service
operator|.
name|IoHandlerAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|textline
operator|.
name|LineDelimiter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|logging
operator|.
name|LoggingFilter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|DatagramSessionConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioDatagramAcceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioDatagramConnector
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|Mina2UdpNoCamelTest
specifier|public
class|class
name|Mina2UdpNoCamelTest
block|{
DECL|field|logger
specifier|private
specifier|static
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Mina2UdpNoCamelTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|charset
name|Charset
name|charset
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
DECL|field|delimiter
name|LineDelimiter
name|delimiter
init|=
name|LineDelimiter
operator|.
name|DEFAULT
decl_stmt|;
DECL|field|codecFactory
name|Mina2TextLineCodecFactory
name|codecFactory
init|=
operator|new
name|Mina2TextLineCodecFactory
argument_list|(
name|charset
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
DECL|field|server
name|UDPServer
name|server
decl_stmt|;
comment|// Create the UDPServer before the test is run
annotation|@
name|Before
DECL|method|setupUDPAcceptor ()
specifier|public
name|void
name|setupUDPAcceptor
parameter_list|()
throws|throws
name|IOException
block|{
name|server
operator|=
operator|new
name|UDPServer
argument_list|(
literal|"127.0.0.1"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
name|server
operator|.
name|listen
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|closeUDPAcceptor ()
specifier|public
name|void
name|closeUDPAcceptor
parameter_list|()
throws|throws
name|IOException
block|{
name|server
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMinaUDPWithNoCamel ()
specifier|public
name|void
name|testMinaUDPWithNoCamel
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|UDPClient
name|client
init|=
operator|new
name|UDPClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|connect
argument_list|(
literal|"127.0.0.1"
argument_list|,
literal|1234
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|222
condition|;
name|i
operator|++
control|)
block|{
name|client
operator|.
name|sendNoMina
argument_list|(
literal|"Hello Mina "
operator|+
name|i
operator|+
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
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
name|assertEquals
argument_list|(
literal|222
argument_list|,
name|server
operator|.
name|numMessagesReceived
argument_list|)
expr_stmt|;
block|}
comment|/*      * Mina UDP Server      */
DECL|class|UDPServer
specifier|private
specifier|final
class|class
name|UDPServer
extends|extends
name|IoHandlerAdapter
block|{
DECL|field|host
specifier|private
specifier|final
name|String
name|host
decl_stmt|;
DECL|field|port
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
DECL|field|acceptor
specifier|private
specifier|final
name|NioDatagramAcceptor
name|acceptor
decl_stmt|;
DECL|field|numMessagesReceived
specifier|private
name|int
name|numMessagesReceived
decl_stmt|;
DECL|method|UDPServer (String host, int port)
specifier|private
name|UDPServer
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|host
operator|=
name|host
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|acceptor
operator|=
operator|new
name|NioDatagramAcceptor
argument_list|()
expr_stmt|;
name|DatagramSessionConfig
name|sessionConfig
init|=
name|acceptor
operator|.
name|getSessionConfig
argument_list|()
decl_stmt|;
name|sessionConfig
operator|.
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"codec"
argument_list|,
operator|new
name|ProtocolCodecFilter
argument_list|(
name|codecFactory
argument_list|)
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"logger"
argument_list|,
operator|new
name|LoggingFilter
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|setHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|listen ()
specifier|public
name|void
name|listen
parameter_list|()
throws|throws
name|IOException
block|{
name|acceptor
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
name|host
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
block|{
name|acceptor
operator|.
name|unbind
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession session, Object message)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"UDPServer Received body: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|numMessagesReceived
operator|++
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession session, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Ooops! Something went wrong :|"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|UDPClient
specifier|private
specifier|final
class|class
name|UDPClient
extends|extends
name|IoHandlerAdapter
block|{
comment|/**          * Three optional arguments can be provided (defaults in brackets):          * path, host (localhost) and port (1234).          *          * @param args The command line args.          */
DECL|field|connector
specifier|private
specifier|final
name|NioDatagramConnector
name|connector
decl_stmt|;
DECL|field|socket
specifier|private
name|DatagramSocket
name|socket
decl_stmt|;
DECL|field|address
specifier|private
name|InetAddress
name|address
decl_stmt|;
DECL|field|localPort
specifier|private
name|int
name|localPort
init|=
literal|1234
decl_stmt|;
DECL|field|localHost
specifier|private
name|String
name|localHost
init|=
literal|"127.0.0.1"
decl_stmt|;
DECL|method|UDPClient ()
specifier|private
name|UDPClient
parameter_list|()
block|{
name|connector
operator|=
operator|new
name|NioDatagramConnector
argument_list|()
expr_stmt|;
name|connector
operator|.
name|getFilterChain
argument_list|()
operator|.
name|addLast
argument_list|(
literal|"codec"
argument_list|,
operator|new
name|ProtocolCodecFilter
argument_list|(
name|codecFactory
argument_list|)
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|connect (String host, int port)
specifier|public
name|void
name|connect
parameter_list|(
name|String
name|host
parameter_list|,
name|int
name|port
parameter_list|)
block|{
name|localPort
operator|=
name|port
expr_stmt|;
name|localHost
operator|=
name|host
expr_stmt|;
try|try
block|{
name|socket
operator|=
operator|new
name|DatagramSocket
argument_list|()
expr_stmt|;
name|address
operator|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|localHost
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownHostException
name|ex
parameter_list|)
block|{
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
operator|.
name|getLogger
argument_list|(
name|Mina2UdpNoCamelTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SocketException
name|ex
parameter_list|)
block|{
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
operator|.
name|getLogger
argument_list|(
name|Mina2UdpNoCamelTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendNoMina (String msg)
specifier|public
name|void
name|sendNoMina
parameter_list|(
name|String
name|msg
parameter_list|)
block|{
try|try
block|{
name|DatagramPacket
name|packet
init|=
operator|new
name|DatagramPacket
argument_list|(
name|msg
operator|.
name|getBytes
argument_list|()
argument_list|,
name|msg
operator|.
name|getBytes
argument_list|()
operator|.
name|length
argument_list|,
name|address
argument_list|,
name|localPort
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
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
operator|.
name|getLogger
argument_list|(
name|Mina2UdpNoCamelTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|null
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|messageReceived (IoSession session, Object message)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|message
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"Client Received body: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (IoSession session, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|error
argument_list|(
literal|"Ooops! Something went wrong :|"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

