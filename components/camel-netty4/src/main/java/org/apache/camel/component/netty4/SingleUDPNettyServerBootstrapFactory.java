begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|NetworkInterface
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|bootstrap
operator|.
name|Bootstrap
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
name|ChannelOption
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
name|FixedRecvByteBufAllocator
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
name|epoll
operator|.
name|EpollDatagramChannel
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
name|group
operator|.
name|ChannelGroup
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
name|group
operator|.
name|DefaultChannelGroup
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
name|DatagramChannel
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
name|NioDatagramChannel
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ImmediateEventExecutor
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
name|CamelContext
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
name|netty4
operator|.
name|util
operator|.
name|SubnetUtils
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
name|support
operator|.
name|ServiceSupport
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
name|util
operator|.
name|CamelContextHelper
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
name|util
operator|.
name|EndpointHelper
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
name|util
operator|.
name|ObjectHelper
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
comment|/**  * A {@link NettyServerBootstrapFactory} which is used by a single consumer (not shared).  */
end_comment

begin_class
DECL|class|SingleUDPNettyServerBootstrapFactory
specifier|public
class|class
name|SingleUDPNettyServerBootstrapFactory
extends|extends
name|ServiceSupport
implements|implements
name|NettyServerBootstrapFactory
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SingleUDPNettyServerBootstrapFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|LOOPBACK_INTERFACE
specifier|private
specifier|static
specifier|final
name|String
name|LOOPBACK_INTERFACE
init|=
literal|"lo"
decl_stmt|;
DECL|field|MULTICAST_SUBNET
specifier|private
specifier|static
specifier|final
name|String
name|MULTICAST_SUBNET
init|=
literal|"224.0.0.0/4"
decl_stmt|;
DECL|field|allChannels
specifier|private
specifier|final
name|ChannelGroup
name|allChannels
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|threadFactory
specifier|private
name|ThreadFactory
name|threadFactory
decl_stmt|;
DECL|field|configuration
specifier|private
name|NettyServerBootstrapConfiguration
name|configuration
decl_stmt|;
DECL|field|pipelineFactory
specifier|private
name|ChannelInitializer
argument_list|<
name|Channel
argument_list|>
name|pipelineFactory
decl_stmt|;
DECL|field|multicastNetworkInterface
specifier|private
name|NetworkInterface
name|multicastNetworkInterface
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
DECL|field|workerGroup
specifier|private
name|EventLoopGroup
name|workerGroup
decl_stmt|;
DECL|method|SingleUDPNettyServerBootstrapFactory ()
specifier|public
name|SingleUDPNettyServerBootstrapFactory
parameter_list|()
block|{
name|this
operator|.
name|allChannels
operator|=
operator|new
name|DefaultChannelGroup
argument_list|(
name|SingleUDPNettyServerBootstrapFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|ImmediateEventExecutor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
DECL|method|init (CamelContext camelContext, NettyServerBootstrapConfiguration configuration, ChannelInitializer<Channel> pipelineFactory)
specifier|public
name|void
name|init
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|NettyServerBootstrapConfiguration
name|configuration
parameter_list|,
name|ChannelInitializer
argument_list|<
name|Channel
argument_list|>
name|pipelineFactory
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|pipelineFactory
operator|=
name|pipelineFactory
expr_stmt|;
block|}
DECL|method|init (ThreadFactory threadFactory, NettyServerBootstrapConfiguration configuration, ChannelInitializer<Channel> pipelineFactory)
specifier|public
name|void
name|init
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|,
name|NettyServerBootstrapConfiguration
name|configuration
parameter_list|,
name|ChannelInitializer
argument_list|<
name|Channel
argument_list|>
name|pipelineFactory
parameter_list|)
block|{
name|this
operator|.
name|threadFactory
operator|=
name|threadFactory
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|pipelineFactory
operator|=
name|pipelineFactory
expr_stmt|;
block|}
DECL|method|addChannel (Channel channel)
specifier|public
name|void
name|addChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
block|{
name|allChannels
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
DECL|method|removeChannel (Channel channel)
specifier|public
name|void
name|removeChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
block|{
name|allChannels
operator|.
name|remove
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
DECL|method|addConsumer (NettyConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
block|{
comment|// does not allow sharing
block|}
DECL|method|removeConsumer (NettyConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
block|{
comment|// does not allow sharing
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|camelContext
operator|==
literal|null
operator|&&
name|threadFactory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Either CamelContext or ThreadFactory must be set on "
operator|+
name|this
argument_list|)
throw|;
block|}
name|startServerBootstrap
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|stopServerBootstrap
argument_list|()
expr_stmt|;
block|}
DECL|method|startServerBootstrap ()
specifier|protected
name|void
name|startServerBootstrap
parameter_list|()
throws|throws
name|Exception
block|{
comment|// create non-shared worker pool
name|EventLoopGroup
name|wg
init|=
name|configuration
operator|.
name|getWorkerGroup
argument_list|()
decl_stmt|;
if|if
condition|(
name|wg
operator|==
literal|null
condition|)
block|{
comment|// create new pool which we should shutdown when stopping as its not shared
name|workerGroup
operator|=
operator|new
name|NettyWorkerPoolBuilder
argument_list|()
operator|.
name|withNativeTransport
argument_list|(
name|configuration
operator|.
name|isNativeTransport
argument_list|()
argument_list|)
operator|.
name|withWorkerCount
argument_list|(
name|configuration
operator|.
name|getWorkerCount
argument_list|()
argument_list|)
operator|.
name|withName
argument_list|(
literal|"NettyServerTCPWorker"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|wg
operator|=
name|workerGroup
expr_stmt|;
block|}
name|Bootstrap
name|bootstrap
init|=
operator|new
name|Bootstrap
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isNativeTransport
argument_list|()
condition|)
block|{
name|bootstrap
operator|.
name|group
argument_list|(
name|wg
argument_list|)
operator|.
name|channel
argument_list|(
name|EpollDatagramChannel
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bootstrap
operator|.
name|group
argument_list|(
name|wg
argument_list|)
operator|.
name|channel
argument_list|(
name|NioDatagramChannel
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// We cannot set the child option here
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|SO_REUSEADDR
argument_list|,
name|configuration
operator|.
name|isReuseAddress
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|SO_SNDBUF
argument_list|,
name|configuration
operator|.
name|getSendBufferSize
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|SO_RCVBUF
argument_list|,
name|configuration
operator|.
name|getReceiveBufferSize
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|SO_BROADCAST
argument_list|,
name|configuration
operator|.
name|isBroadcast
argument_list|()
argument_list|)
expr_stmt|;
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|CONNECT_TIMEOUT_MILLIS
argument_list|,
name|configuration
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
comment|// only set this if user has specified
if|if
condition|(
name|configuration
operator|.
name|getReceiveBufferSizePredictor
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|RCVBUF_ALLOCATOR
argument_list|,
operator|new
name|FixedRecvByteBufAllocator
argument_list|(
name|configuration
operator|.
name|getReceiveBufferSizePredictor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getBacklog
argument_list|()
operator|>
literal|0
condition|)
block|{
name|bootstrap
operator|.
name|option
argument_list|(
name|ChannelOption
operator|.
name|SO_BACKLOG
argument_list|,
name|configuration
operator|.
name|getBacklog
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
name|configuration
operator|.
name|getOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|options
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|ChannelOption
argument_list|<
name|Object
argument_list|>
name|option
init|=
name|ChannelOption
operator|.
name|valueOf
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
comment|//For all netty options that aren't of type String
comment|//TODO: find a way to add primitive Netty options without having to add them to the Camel registry.
if|if
condition|(
name|EndpointHelper
operator|.
name|isReferenceParameter
argument_list|(
name|value
argument_list|)
condition|)
block|{
name|String
name|name
init|=
name|value
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|Object
name|o
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|bootstrap
operator|.
name|option
argument_list|(
name|option
argument_list|,
name|o
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bootstrap
operator|.
name|option
argument_list|(
name|option
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created Bootstrap {}"
argument_list|,
name|bootstrap
argument_list|)
expr_stmt|;
comment|// set the pipeline factory, which creates the pipeline for each newly created channels
name|bootstrap
operator|.
name|handler
argument_list|(
name|pipelineFactory
argument_list|)
expr_stmt|;
name|InetSocketAddress
name|hostAddress
init|=
operator|new
name|InetSocketAddress
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|SubnetUtils
name|multicastSubnet
init|=
operator|new
name|SubnetUtils
argument_list|(
name|MULTICAST_SUBNET
argument_list|)
decl_stmt|;
if|if
condition|(
name|multicastSubnet
operator|.
name|getInfo
argument_list|()
operator|.
name|isInRange
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|)
condition|)
block|{
name|ChannelFuture
name|channelFuture
init|=
name|bootstrap
operator|.
name|bind
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|sync
argument_list|()
decl_stmt|;
name|channel
operator|=
name|channelFuture
operator|.
name|channel
argument_list|()
expr_stmt|;
name|DatagramChannel
name|datagramChannel
init|=
operator|(
name|DatagramChannel
operator|)
name|channel
decl_stmt|;
name|String
name|networkInterface
init|=
name|configuration
operator|.
name|getNetworkInterface
argument_list|()
operator|==
literal|null
condition|?
name|LOOPBACK_INTERFACE
else|:
name|configuration
operator|.
name|getNetworkInterface
argument_list|()
decl_stmt|;
name|multicastNetworkInterface
operator|=
name|NetworkInterface
operator|.
name|getByName
argument_list|(
name|networkInterface
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|multicastNetworkInterface
argument_list|,
literal|"No network interface found for '"
operator|+
name|networkInterface
operator|+
literal|"'."
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"ConnectionlessBootstrap joining {}:{} using network interface: {}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
name|multicastNetworkInterface
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|datagramChannel
operator|.
name|joinGroup
argument_list|(
name|hostAddress
argument_list|,
name|multicastNetworkInterface
argument_list|)
operator|.
name|syncUninterruptibly
argument_list|()
expr_stmt|;
name|allChannels
operator|.
name|add
argument_list|(
name|datagramChannel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"ConnectionlessBootstrap binding to {}:{}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|ChannelFuture
name|channelFuture
init|=
name|bootstrap
operator|.
name|bind
argument_list|(
name|hostAddress
argument_list|)
operator|.
name|sync
argument_list|()
decl_stmt|;
name|channel
operator|=
name|channelFuture
operator|.
name|channel
argument_list|()
expr_stmt|;
name|allChannels
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopServerBootstrap ()
specifier|protected
name|void
name|stopServerBootstrap
parameter_list|()
block|{
comment|// close all channels
name|LOG
operator|.
name|info
argument_list|(
literal|"ConnectionlessBootstrap disconnecting from {}:{}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closing {} channels"
argument_list|,
name|allChannels
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|allChannels
operator|.
name|close
argument_list|()
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
comment|// and then shutdown the thread pools
if|if
condition|(
name|workerGroup
operator|!=
literal|null
condition|)
block|{
name|workerGroup
operator|.
name|shutdownGracefully
argument_list|()
expr_stmt|;
name|workerGroup
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

