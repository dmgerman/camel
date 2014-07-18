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
name|support
operator|.
name|ServiceSupport
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
name|ServerBootstrap
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
name|Channel
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
name|ChannelFactory
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
name|ChannelFuture
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
name|group
operator|.
name|ChannelGroup
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
name|group
operator|.
name|ChannelGroupFuture
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
name|group
operator|.
name|DefaultChannelGroup
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
name|BossPool
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
name|NioServerSocketChannelFactory
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
name|WorkerPool
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
DECL|class|SingleTCPNettyServerBootstrapFactory
specifier|public
class|class
name|SingleTCPNettyServerBootstrapFactory
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
name|SingleTCPNettyServerBootstrapFactory
operator|.
name|class
argument_list|)
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
name|ChannelPipelineFactory
name|pipelineFactory
decl_stmt|;
DECL|field|channelFactory
specifier|private
name|ChannelFactory
name|channelFactory
decl_stmt|;
DECL|field|serverBootstrap
specifier|private
name|ServerBootstrap
name|serverBootstrap
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
DECL|field|bossPool
specifier|private
name|BossPool
name|bossPool
decl_stmt|;
DECL|field|workerPool
specifier|private
name|WorkerPool
name|workerPool
decl_stmt|;
DECL|method|SingleTCPNettyServerBootstrapFactory ()
specifier|public
name|SingleTCPNettyServerBootstrapFactory
parameter_list|()
block|{
name|this
operator|.
name|allChannels
operator|=
operator|new
name|DefaultChannelGroup
argument_list|(
name|SingleTCPNettyServerBootstrapFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|init (CamelContext camelContext, NettyServerBootstrapConfiguration configuration, ChannelPipelineFactory pipelineFactory)
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
name|ChannelPipelineFactory
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
DECL|method|init (ThreadFactory threadFactory, NettyServerBootstrapConfiguration configuration, ChannelPipelineFactory pipelineFactory)
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
name|ChannelPipelineFactory
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
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"ServerBootstrap binding to {}:{}"
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
name|future
init|=
name|channel
operator|.
name|bind
argument_list|(
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
argument_list|)
decl_stmt|;
name|future
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|future
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
comment|// if we cannot bind, the re-create channel
name|allChannels
operator|.
name|remove
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|channel
operator|=
name|serverBootstrap
operator|.
name|bind
argument_list|(
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
argument_list|)
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
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"ServerBootstrap unbinding from {}:{}"
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
name|future
init|=
name|channel
operator|.
name|unbind
argument_list|()
decl_stmt|;
name|future
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|startServerBootstrap ()
specifier|protected
name|void
name|startServerBootstrap
parameter_list|()
block|{
comment|// prefer using explicit configured thread pools
name|BossPool
name|bp
init|=
name|configuration
operator|.
name|getBossPool
argument_list|()
decl_stmt|;
name|WorkerPool
name|wp
init|=
name|configuration
operator|.
name|getWorkerPool
argument_list|()
decl_stmt|;
if|if
condition|(
name|bp
operator|==
literal|null
condition|)
block|{
comment|// create new pool which we should shutdown when stopping as its not shared
name|bossPool
operator|=
operator|new
name|NettyServerBossPoolBuilder
argument_list|()
operator|.
name|withBossCount
argument_list|(
name|configuration
operator|.
name|getBossCount
argument_list|()
argument_list|)
operator|.
name|withName
argument_list|(
literal|"NettyServerTCPBoss"
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|bp
operator|=
name|bossPool
expr_stmt|;
block|}
if|if
condition|(
name|wp
operator|==
literal|null
condition|)
block|{
comment|// create new pool which we should shutdown when stopping as its not shared
name|workerPool
operator|=
operator|new
name|NettyWorkerPoolBuilder
argument_list|()
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
name|wp
operator|=
name|workerPool
expr_stmt|;
block|}
name|channelFactory
operator|=
operator|new
name|NioServerSocketChannelFactory
argument_list|(
name|bp
argument_list|,
name|wp
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|=
operator|new
name|ServerBootstrap
argument_list|(
name|channelFactory
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"child.keepAlive"
argument_list|,
name|configuration
operator|.
name|isKeepAlive
argument_list|()
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"child.tcpNoDelay"
argument_list|,
name|configuration
operator|.
name|isTcpNoDelay
argument_list|()
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"reuseAddress"
argument_list|,
name|configuration
operator|.
name|isReuseAddress
argument_list|()
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"child.reuseAddress"
argument_list|,
name|configuration
operator|.
name|isReuseAddress
argument_list|()
argument_list|)
expr_stmt|;
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"child.connectTimeoutMillis"
argument_list|,
name|configuration
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
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
name|serverBootstrap
operator|.
name|setOption
argument_list|(
literal|"backlog"
argument_list|,
name|configuration
operator|.
name|getBacklog
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set any additional netty options
if|if
condition|(
name|configuration
operator|.
name|getOptions
argument_list|()
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
name|configuration
operator|.
name|getOptions
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|serverBootstrap
operator|.
name|setOption
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Created ServerBootstrap {} with options: {}"
argument_list|,
name|serverBootstrap
argument_list|,
name|serverBootstrap
operator|.
name|getOptions
argument_list|()
argument_list|)
expr_stmt|;
comment|// set the pipeline factory, which creates the pipeline for each newly created channels
name|serverBootstrap
operator|.
name|setPipelineFactory
argument_list|(
name|pipelineFactory
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"ServerBootstrap binding to {}:{}"
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
name|channel
operator|=
name|serverBootstrap
operator|.
name|bind
argument_list|(
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
argument_list|)
expr_stmt|;
comment|// to keep track of all channels in use
name|allChannels
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
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
literal|"ServerBootstrap unbinding from {}:{}"
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
name|ChannelGroupFuture
name|future
init|=
name|allChannels
operator|.
name|close
argument_list|()
decl_stmt|;
name|future
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
comment|// close server external resources
if|if
condition|(
name|channelFactory
operator|!=
literal|null
condition|)
block|{
name|channelFactory
operator|.
name|releaseExternalResources
argument_list|()
expr_stmt|;
name|channelFactory
operator|=
literal|null
expr_stmt|;
block|}
comment|// and then shutdown the thread pools
if|if
condition|(
name|bossPool
operator|!=
literal|null
condition|)
block|{
name|bossPool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|bossPool
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|workerPool
operator|!=
literal|null
condition|)
block|{
name|workerPool
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|workerPool
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

