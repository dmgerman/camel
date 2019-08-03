begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|bootstrap
operator|.
name|ServerBootstrap
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelFuture
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandlerContext
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelInitializer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelPipeline
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|EventLoopGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|SimpleChannelInboundHandler
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|nio
operator|.
name|NioEventLoopGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|SocketChannel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioServerSocketChannel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|DelimiterBasedFrameDecoder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|Delimiters
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringDecoder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|string
operator|.
name|StringEncoder
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

begin_class
DECL|class|NettyConsumerClientModeReconnectTest
specifier|public
class|class
name|NettyConsumerClientModeReconnectTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|server
specifier|private
name|MyServer
name|server
decl_stmt|;
DECL|method|startNettyServer ()
specifier|public
name|void
name|startNettyServer
parameter_list|()
throws|throws
name|Exception
block|{
name|server
operator|=
operator|new
name|MyServer
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdownServer ()
specifier|public
name|void
name|shutdownServer
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNettyRouteServerNotStarted ()
specifier|public
name|void
name|testNettyRouteServerNotStarted
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|MockEndpoint
name|receive
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:receive"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|receive
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye Willem"
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|">>> starting Camel route while Netty server is not ready"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"client"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|">>> starting Netty server"
argument_list|)
expr_stmt|;
name|startNettyServer
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|">>> routing done"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|log
operator|.
name|info
argument_list|(
literal|">>> shutting down Netty server"
argument_list|)
expr_stmt|;
name|shutdownServer
argument_list|()
expr_stmt|;
block|}
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?textline=true&clientMode=true&reconnect=true&reconnectInterval=200"
argument_list|)
operator|.
name|id
argument_list|(
literal|"client"
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
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Processing exchange in Netty server {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|String
name|body
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:receive"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:receive"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyServer
specifier|private
specifier|static
class|class
name|MyServer
block|{
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|bootstrap
specifier|private
name|ServerBootstrap
name|bootstrap
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
DECL|field|bossGroup
specifier|private
name|EventLoopGroup
name|bossGroup
decl_stmt|;
DECL|field|workerGroup
specifier|private
name|EventLoopGroup
name|workerGroup
decl_stmt|;
DECL|method|MyServer (int port)
name|MyServer
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
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|bossGroup
operator|=
operator|new
name|NioEventLoopGroup
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|workerGroup
operator|=
operator|new
name|NioEventLoopGroup
argument_list|()
expr_stmt|;
name|bootstrap
operator|=
operator|new
name|ServerBootstrap
argument_list|()
expr_stmt|;
name|bootstrap
operator|.
name|group
argument_list|(
name|bossGroup
argument_list|,
name|workerGroup
argument_list|)
operator|.
name|channel
argument_list|(
name|NioServerSocketChannel
operator|.
name|class
argument_list|)
operator|.
name|childHandler
argument_list|(
operator|new
name|ServerInitializer
argument_list|()
argument_list|)
expr_stmt|;
name|ChannelFuture
name|cf
init|=
name|bootstrap
operator|.
name|bind
argument_list|(
name|port
argument_list|)
operator|.
name|sync
argument_list|()
decl_stmt|;
name|channel
operator|=
name|cf
operator|.
name|channel
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|channel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|bossGroup
operator|.
name|shutdownGracefully
argument_list|()
expr_stmt|;
name|workerGroup
operator|.
name|shutdownGracefully
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|ServerHandler
specifier|private
specifier|static
class|class
name|ServerHandler
extends|extends
name|SimpleChannelInboundHandler
argument_list|<
name|String
argument_list|>
block|{
annotation|@
name|Override
DECL|method|channelActive (ChannelHandlerContext ctx)
specifier|public
name|void
name|channelActive
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|write
argument_list|(
literal|"Willem\r\n"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Throwable
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
name|cause
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|ctx
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|channelRead0 (ChannelHandlerContext ctx, String msg)
specifier|protected
name|void
name|channelRead0
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Do nothing here
block|}
annotation|@
name|Override
DECL|method|channelReadComplete (ChannelHandlerContext ctx)
specifier|public
name|void
name|channelReadComplete
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
throws|throws
name|Exception
block|{
name|ctx
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|ServerInitializer
specifier|private
specifier|static
class|class
name|ServerInitializer
extends|extends
name|ChannelInitializer
argument_list|<
name|SocketChannel
argument_list|>
block|{
DECL|field|DECODER
specifier|private
specifier|static
specifier|final
name|StringDecoder
name|DECODER
init|=
operator|new
name|StringDecoder
argument_list|()
decl_stmt|;
DECL|field|ENCODER
specifier|private
specifier|static
specifier|final
name|StringEncoder
name|ENCODER
init|=
operator|new
name|StringEncoder
argument_list|()
decl_stmt|;
DECL|field|SERVERHANDLER
specifier|private
specifier|static
specifier|final
name|ServerHandler
name|SERVERHANDLER
init|=
operator|new
name|ServerHandler
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|initChannel (SocketChannel ch)
specifier|public
name|void
name|initChannel
parameter_list|(
name|SocketChannel
name|ch
parameter_list|)
throws|throws
name|Exception
block|{
name|ChannelPipeline
name|pipeline
init|=
name|ch
operator|.
name|pipeline
argument_list|()
decl_stmt|;
comment|// Add the text line codec combination first,
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"framer"
argument_list|,
operator|new
name|DelimiterBasedFrameDecoder
argument_list|(
literal|8192
argument_list|,
name|Delimiters
operator|.
name|lineDelimiter
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// the encoder and decoder are static as these are sharable
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"decoder"
argument_list|,
name|DECODER
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"encoder"
argument_list|,
name|ENCODER
argument_list|)
expr_stmt|;
comment|// and then business logic.
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
name|SERVERHANDLER
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

