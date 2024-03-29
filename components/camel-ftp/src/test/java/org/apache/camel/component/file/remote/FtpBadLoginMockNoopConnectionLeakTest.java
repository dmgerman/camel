begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|remote
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
name|InetAddress
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|SocketFactory
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
name|BindToRegistry
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
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPClient
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
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
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

begin_comment
comment|/**  * Test which checks leaking connections when FTP server returns correct status for NOOP operation.  */
end_comment

begin_class
DECL|class|FtpBadLoginMockNoopConnectionLeakTest
specifier|public
class|class
name|FtpBadLoginMockNoopConnectionLeakTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FtpBadLoginMockNoopConnectionLeakTest
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Mapping of socket hashcode to two element tab ([connect() called, close() called])      */
DECL|field|socketAudits
specifier|private
name|Map
argument_list|<
name|Integer
argument_list|,
name|boolean
index|[]
argument_list|>
name|socketAudits
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"sf"
argument_list|)
DECL|field|sf
specifier|private
name|SocketFactory
name|sf
init|=
operator|new
name|AuditingSocketFactory
argument_list|()
decl_stmt|;
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://dummy@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/badlogin?password=cantremeber&maximumReconnectAttempts=3"
operator|+
literal|"&throwExceptionOnConnectFailed=false&ftpClient.socketFactory=#sf"
return|;
block|}
annotation|@
name|Override
annotation|@
name|BeforeEach
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|FtpEndpoint
argument_list|<
name|?
argument_list|>
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
name|FtpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setFtpClient
argument_list|(
operator|new
name|FTPClient
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|sendNoOp
parameter_list|()
throws|throws
name|IOException
block|{
comment|// return true as long as connection is established
return|return
name|this
operator|.
name|isConnected
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConnectionLeak ()
specifier|public
name|void
name|testConnectionLeak
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
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// let's have several login attempts
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000L
argument_list|)
expr_stmt|;
name|stopCamelContext
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Integer
argument_list|,
name|boolean
index|[]
argument_list|>
name|socketStats
range|:
name|socketAudits
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|assertTrue
argument_list|(
name|socketStats
operator|.
name|getValue
argument_list|()
index|[
literal|0
index|]
argument_list|,
literal|"Socket should be connected"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|socketStats
operator|.
name|getValue
argument_list|()
index|[
literal|0
index|]
argument_list|,
name|socketStats
operator|.
name|getValue
argument_list|()
index|[
literal|1
index|]
argument_list|,
literal|"Socket should be closed"
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|getFtpUrl
argument_list|()
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
comment|/**      * {@link SocketFactory} which creates {@link Socket}s that expose statistics about {@link Socket#connect(SocketAddress)}/{@link Socket#close()}      * invocations      */
DECL|class|AuditingSocketFactory
specifier|private
class|class
name|AuditingSocketFactory
extends|extends
name|SocketFactory
block|{
annotation|@
name|Override
DECL|method|createSocket (String s, int i)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket (String s, int i, InetAddress inetAddress, int i1)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|String
name|s
parameter_list|,
name|int
name|i
parameter_list|,
name|InetAddress
name|inetAddress
parameter_list|,
name|int
name|i1
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket (InetAddress inetAddress, int i)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|inetAddress
parameter_list|,
name|int
name|i
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket ()
specifier|public
name|Socket
name|createSocket
parameter_list|()
throws|throws
name|IOException
block|{
name|AuditingSocket
name|socket
init|=
operator|new
name|AuditingSocket
argument_list|()
decl_stmt|;
name|socketAudits
operator|.
name|put
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|socket
argument_list|)
argument_list|,
operator|new
name|boolean
index|[]
block|{
literal|false
block|,
literal|false
block|}
argument_list|)
expr_stmt|;
return|return
name|socket
return|;
block|}
annotation|@
name|Override
DECL|method|createSocket (InetAddress inetAddress, int i, InetAddress inetAddress1, int i1)
specifier|public
name|Socket
name|createSocket
parameter_list|(
name|InetAddress
name|inetAddress
parameter_list|,
name|int
name|i
parameter_list|,
name|InetAddress
name|inetAddress1
parameter_list|,
name|int
name|i1
parameter_list|)
throws|throws
name|IOException
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * {@link Socket} which counts connect()/close() invocations      */
DECL|class|AuditingSocket
specifier|private
class|class
name|AuditingSocket
extends|extends
name|Socket
block|{
annotation|@
name|Override
DECL|method|connect (SocketAddress endpoint, int timeout)
specifier|public
name|void
name|connect
parameter_list|(
name|SocketAddress
name|endpoint
parameter_list|,
name|int
name|timeout
parameter_list|)
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Connecting socket {}"
argument_list|,
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|connect
argument_list|(
name|endpoint
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|boolean
index|[]
name|value
init|=
name|socketAudits
operator|.
name|get
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
argument_list|)
decl_stmt|;
name|value
index|[
literal|0
index|]
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
specifier|synchronized
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Disconnecting socket {}"
argument_list|,
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|close
argument_list|()
expr_stmt|;
name|boolean
index|[]
name|value
init|=
name|socketAudits
operator|.
name|get
argument_list|(
name|System
operator|.
name|identityHashCode
argument_list|(
name|this
argument_list|)
argument_list|)
decl_stmt|;
name|value
index|[
literal|1
index|]
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

