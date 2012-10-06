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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|impl
operator|.
name|DefaultConsumer
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
name|FixedReceiveBufferSizePredictorFactory
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
name|DatagramChannelFactory
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
name|handler
operator|.
name|execution
operator|.
name|OrderedDownstreamThreadPoolExecutor
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
name|execution
operator|.
name|OrderedMemoryAwareThreadPoolExecutor
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

begin_class
DECL|class|NettyConsumer
specifier|public
class|class
name|NettyConsumer
extends|extends
name|DefaultConsumer
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
name|NettyConsumer
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
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|configuration
specifier|private
name|NettyConfiguration
name|configuration
decl_stmt|;
DECL|field|channelFactory
specifier|private
name|ChannelFactory
name|channelFactory
decl_stmt|;
DECL|field|datagramChannelFactory
specifier|private
name|DatagramChannelFactory
name|datagramChannelFactory
decl_stmt|;
DECL|field|serverBootstrap
specifier|private
name|ServerBootstrap
name|serverBootstrap
decl_stmt|;
DECL|field|connectionlessServerBootstrap
specifier|private
name|ConnectionlessBootstrap
name|connectionlessServerBootstrap
decl_stmt|;
DECL|field|pipelineFactory
specifier|private
name|ServerPipelineFactory
name|pipelineFactory
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
DECL|field|bossExecutor
specifier|private
name|ExecutorService
name|bossExecutor
decl_stmt|;
DECL|field|workerExecutor
specifier|private
name|ExecutorService
name|workerExecutor
decl_stmt|;
DECL|method|NettyConsumer (NettyEndpoint nettyEndpoint, Processor processor, NettyConfiguration configuration)
specifier|public
name|NettyConsumer
parameter_list|(
name|NettyEndpoint
name|nettyEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|nettyEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|this
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|allChannels
operator|=
operator|new
name|DefaultChannelGroup
argument_list|(
literal|"NettyConsumer-"
operator|+
name|nettyEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NettyEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NettyEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Netty consumer binding to: {}"
argument_list|,
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// setup pipeline factory
name|ServerPipelineFactory
name|factory
init|=
name|configuration
operator|.
name|getServerPipelineFactory
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|pipelineFactory
operator|=
name|factory
operator|.
name|createPipelineFactory
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pipelineFactory
operator|=
operator|new
name|DefaultServerPipelineFactory
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isTcp
argument_list|()
condition|)
block|{
name|initializeTCPServerSocketCommunicationLayer
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|initializeUDPServerSocketCommunicationLayer
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Netty consumer bound to: "
operator|+
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Netty consumer unbinding from: {}"
argument_list|,
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// close all channels
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
block|}
comment|// and then shutdown the thread pools
if|if
condition|(
name|bossExecutor
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|bossExecutor
argument_list|)
expr_stmt|;
name|bossExecutor
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|workerExecutor
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|workerExecutor
argument_list|)
expr_stmt|;
name|workerExecutor
operator|=
literal|null
expr_stmt|;
block|}
name|LOG
operator|.
name|info
argument_list|(
literal|"Netty consumer unbound from: "
operator|+
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getAllChannels ()
specifier|public
name|ChannelGroup
name|getAllChannels
parameter_list|()
block|{
return|return
name|allChannels
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|NettyConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (NettyConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getChannelFactory ()
specifier|public
name|ChannelFactory
name|getChannelFactory
parameter_list|()
block|{
return|return
name|channelFactory
return|;
block|}
DECL|method|setChannelFactory (ChannelFactory channelFactory)
specifier|public
name|void
name|setChannelFactory
parameter_list|(
name|ChannelFactory
name|channelFactory
parameter_list|)
block|{
name|this
operator|.
name|channelFactory
operator|=
name|channelFactory
expr_stmt|;
block|}
DECL|method|getDatagramChannelFactory ()
specifier|public
name|DatagramChannelFactory
name|getDatagramChannelFactory
parameter_list|()
block|{
return|return
name|datagramChannelFactory
return|;
block|}
DECL|method|setDatagramChannelFactory (DatagramChannelFactory datagramChannelFactory)
specifier|public
name|void
name|setDatagramChannelFactory
parameter_list|(
name|DatagramChannelFactory
name|datagramChannelFactory
parameter_list|)
block|{
name|this
operator|.
name|datagramChannelFactory
operator|=
name|datagramChannelFactory
expr_stmt|;
block|}
DECL|method|getServerBootstrap ()
specifier|public
name|ServerBootstrap
name|getServerBootstrap
parameter_list|()
block|{
return|return
name|serverBootstrap
return|;
block|}
DECL|method|setServerBootstrap (ServerBootstrap serverBootstrap)
specifier|public
name|void
name|setServerBootstrap
parameter_list|(
name|ServerBootstrap
name|serverBootstrap
parameter_list|)
block|{
name|this
operator|.
name|serverBootstrap
operator|=
name|serverBootstrap
expr_stmt|;
block|}
DECL|method|getConnectionlessServerBootstrap ()
specifier|public
name|ConnectionlessBootstrap
name|getConnectionlessServerBootstrap
parameter_list|()
block|{
return|return
name|connectionlessServerBootstrap
return|;
block|}
DECL|method|setConnectionlessServerBootstrap (ConnectionlessBootstrap connectionlessServerBootstrap)
specifier|public
name|void
name|setConnectionlessServerBootstrap
parameter_list|(
name|ConnectionlessBootstrap
name|connectionlessServerBootstrap
parameter_list|)
block|{
name|this
operator|.
name|connectionlessServerBootstrap
operator|=
name|connectionlessServerBootstrap
expr_stmt|;
block|}
DECL|method|isTcp ()
specifier|protected
name|boolean
name|isTcp
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProtocol
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"tcp"
argument_list|)
return|;
block|}
DECL|method|initializeTCPServerSocketCommunicationLayer ()
specifier|private
name|void
name|initializeTCPServerSocketCommunicationLayer
parameter_list|()
throws|throws
name|Exception
block|{
name|bossExecutor
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyTCPBoss"
argument_list|)
expr_stmt|;
name|workerExecutor
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyTCPWorker"
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getWorkerCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|channelFactory
operator|=
operator|new
name|NioServerSocketChannelFactory
argument_list|(
name|bossExecutor
argument_list|,
name|workerExecutor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|channelFactory
operator|=
operator|new
name|NioServerSocketChannelFactory
argument_list|(
name|bossExecutor
argument_list|,
name|workerExecutor
argument_list|,
name|configuration
operator|.
name|getWorkerCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// set the pipeline factory, which creates the pipeline for each newly created channels
name|serverBootstrap
operator|.
name|setPipelineFactory
argument_list|(
name|pipelineFactory
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
DECL|method|initializeUDPServerSocketCommunicationLayer ()
specifier|private
name|void
name|initializeUDPServerSocketCommunicationLayer
parameter_list|()
throws|throws
name|Exception
block|{
name|workerExecutor
operator|=
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyUDPWorker"
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getWorkerCount
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|datagramChannelFactory
operator|=
operator|new
name|NioDatagramChannelFactory
argument_list|(
name|workerExecutor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|datagramChannelFactory
operator|=
operator|new
name|NioDatagramChannelFactory
argument_list|(
name|workerExecutor
argument_list|,
name|configuration
operator|.
name|getWorkerCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connectionlessServerBootstrap
operator|=
operator|new
name|ConnectionlessBootstrap
argument_list|(
name|datagramChannelFactory
argument_list|)
expr_stmt|;
name|connectionlessServerBootstrap
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
name|connectionlessServerBootstrap
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
name|connectionlessServerBootstrap
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
name|connectionlessServerBootstrap
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
name|connectionlessServerBootstrap
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
name|connectionlessServerBootstrap
operator|.
name|setOption
argument_list|(
literal|"child.broadcast"
argument_list|,
name|configuration
operator|.
name|isBroadcast
argument_list|()
argument_list|)
expr_stmt|;
name|connectionlessServerBootstrap
operator|.
name|setOption
argument_list|(
literal|"sendBufferSize"
argument_list|,
name|configuration
operator|.
name|getSendBufferSize
argument_list|()
argument_list|)
expr_stmt|;
name|connectionlessServerBootstrap
operator|.
name|setOption
argument_list|(
literal|"receiveBufferSize"
argument_list|,
name|configuration
operator|.
name|getReceiveBufferSize
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
name|connectionlessServerBootstrap
operator|.
name|setOption
argument_list|(
literal|"receiveBufferSizePredictorFactory"
argument_list|,
operator|new
name|FixedReceiveBufferSizePredictorFactory
argument_list|(
name|configuration
operator|.
name|getReceiveBufferSizePredictor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// set the pipeline factory, which creates the pipeline for each newly created channels
name|connectionlessServerBootstrap
operator|.
name|setPipelineFactory
argument_list|(
name|pipelineFactory
argument_list|)
expr_stmt|;
name|channel
operator|=
name|connectionlessServerBootstrap
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
block|}
end_class

end_unit

