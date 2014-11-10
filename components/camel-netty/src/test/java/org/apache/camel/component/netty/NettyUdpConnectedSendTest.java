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
name|InetSocketAddress
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
name|jboss
operator|.
name|netty
operator|.
name|bootstrap
operator|.
name|ConnectionlessBootstrap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
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
name|org
operator|.
name|jboss
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
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelPipelineFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|MessageEvent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|SimpleChannelUpstreamHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|nio
operator|.
name|NioDatagramChannelFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
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
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|util
operator|.
name|CharsetUtil
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
DECL|class|NettyUdpConnectedSendTest
specifier|public
class|class
name|NettyUdpConnectedSendTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|SEND_STRING
specifier|private
specifier|static
specifier|final
name|String
name|SEND_STRING
init|=
literal|"***<We all love camel>***"
decl_stmt|;
DECL|field|SEND_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|SEND_COUNT
init|=
literal|20
decl_stmt|;
DECL|field|receivedCount
specifier|private
name|int
name|receivedCount
decl_stmt|;
DECL|field|bootstrap
specifier|private
name|ConnectionlessBootstrap
name|bootstrap
decl_stmt|;
DECL|method|createNettyUdpReceiver ()
specifier|public
name|void
name|createNettyUdpReceiver
parameter_list|()
block|{
name|bootstrap
operator|=
operator|new
name|ConnectionlessBootstrap
argument_list|(
operator|new
name|NioDatagramChannelFactory
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|setPipelineFactory
argument_list|(
operator|new
name|ChannelPipelineFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelPipeline
name|getPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|ChannelPipeline
name|channelPipeline
init|=
name|Channels
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"StringDecoder"
argument_list|,
operator|new
name|StringDecoder
argument_list|(
name|CharsetUtil
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"ContentHandler"
argument_list|,
operator|new
name|ContentHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|channelPipeline
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|bind ()
specifier|public
name|void
name|bind
parameter_list|()
block|{
name|bootstrap
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|8601
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|bootstrap
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendConnectedUdp ()
specifier|public
name|void
name|sendConnectedUdp
parameter_list|()
throws|throws
name|Exception
block|{
name|createNettyUdpReceiver
argument_list|()
expr_stmt|;
name|Thread
name|t
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|bind
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
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
name|SEND_COUNT
condition|;
operator|++
name|i
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
name|SEND_STRING
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|stop
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should have received some datagrams"
argument_list|,
name|receivedCount
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendConnectedWithoutReceiver ()
specifier|public
name|void
name|sendConnectedWithoutReceiver
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|exceptionCount
init|=
literal|0
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
name|SEND_COUNT
condition|;
operator|++
name|i
control|)
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
name|SEND_STRING
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
operator|++
name|exceptionCount
expr_stmt|;
block|}
block|}
name|assertTrue
argument_list|(
literal|"There should at least one exception because port is unreachable"
argument_list|,
name|exceptionCount
operator|>
literal|0
argument_list|)
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
literal|"direct:in"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty:udp://localhost:8601?sync=false&textline=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|ContentHandler
specifier|public
class|class
name|ContentHandler
extends|extends
name|SimpleChannelUpstreamHandler
block|{
annotation|@
name|Override
DECL|method|messageReceived (ChannelHandlerContext ctx, MessageEvent messageEvent)
specifier|public
name|void
name|messageReceived
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|MessageEvent
name|messageEvent
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|s
init|=
operator|(
name|String
operator|)
name|messageEvent
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|receivedCount
operator|++
expr_stmt|;
name|assertEquals
argument_list|(
name|SEND_STRING
argument_list|,
name|s
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

