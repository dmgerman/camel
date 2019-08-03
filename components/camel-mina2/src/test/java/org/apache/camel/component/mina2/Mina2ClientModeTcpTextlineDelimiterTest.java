begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InetSocketAddress
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
name|mina
operator|.
name|core
operator|.
name|service
operator|.
name|IoAcceptor
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
name|transport
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioSocketAcceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"fix me"
argument_list|)
DECL|class|Mina2ClientModeTcpTextlineDelimiterTest
specifier|public
class|class
name|Mina2ClientModeTcpTextlineDelimiterTest
extends|extends
name|BaseMina2Test
block|{
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
name|Object
name|body
init|=
literal|"Hello there!"
decl_stmt|;
name|endpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// need to start the server first
name|Server
name|server
init|=
operator|new
name|Server
argument_list|(
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|server
operator|.
name|startup
argument_list|()
expr_stmt|;
comment|// start the camel route to connect to the server
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"minaRoute"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|server
operator|.
name|shutdown
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
name|String
operator|.
name|format
argument_list|(
literal|"mina2:tcp://127.0.0.1:%1$s?sync=false&textline=true&textlineDelimiter=UNIX&clientMode=true"
argument_list|,
name|getPort
argument_list|()
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"minaRoute"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:before?showAll=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after?showAll=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|Server
specifier|private
class|class
name|Server
block|{
DECL|field|port
specifier|private
specifier|final
name|int
name|port
decl_stmt|;
DECL|field|acceptor
specifier|private
name|IoAcceptor
name|acceptor
decl_stmt|;
DECL|method|Server (int port)
name|Server
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|startup ()
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|Exception
block|{
name|acceptor
operator|=
operator|new
name|NioSocketAcceptor
argument_list|()
expr_stmt|;
name|Mina2TextLineCodecFactory
name|codecFactory
init|=
operator|new
name|Mina2TextLineCodecFactory
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|,
name|LineDelimiter
operator|.
name|UNIX
argument_list|)
decl_stmt|;
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
name|setHandler
argument_list|(
operator|new
name|ServerHandler
argument_list|()
argument_list|)
expr_stmt|;
name|acceptor
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|"127.0.0.1"
argument_list|,
name|port
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|acceptor
operator|.
name|unbind
argument_list|()
expr_stmt|;
name|acceptor
operator|.
name|dispose
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|ServerHandler
specifier|private
class|class
name|ServerHandler
extends|extends
name|IoHandlerAdapter
block|{
annotation|@
name|Override
DECL|method|sessionOpened (IoSession session)
specifier|public
name|void
name|sessionOpened
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|session
operator|.
name|write
argument_list|(
literal|"Hello there!\n"
argument_list|)
expr_stmt|;
name|session
operator|.
name|closeNow
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

