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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|RejectedExecutionException
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
name|AsyncCallback
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
name|CamelException
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
name|NoTypeConversionAvailableException
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
name|ServicePoolAware
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
name|converter
operator|.
name|IOConverter
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
name|DefaultAsyncProducer
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
name|DefaultExchange
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
name|processor
operator|.
name|CamelLogger
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
name|ExchangeHelper
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
name|ClientBootstrap
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
name|ChannelFutureListener
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
name|NioClientSocketChannelFactory
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
DECL|class|NettyProducer
specifier|public
class|class
name|NettyProducer
extends|extends
name|DefaultAsyncProducer
implements|implements
name|ServicePoolAware
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
name|NettyProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|ALL_CHANNELS
specifier|private
specifier|static
specifier|final
name|ChannelGroup
name|ALL_CHANNELS
init|=
operator|new
name|DefaultChannelGroup
argument_list|(
literal|"NettyProducer"
argument_list|)
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
DECL|field|noReplyLogger
specifier|private
name|CamelLogger
name|noReplyLogger
decl_stmt|;
DECL|method|NettyProducer (NettyEndpoint nettyEndpoint, NettyConfiguration configuration)
specifier|public
name|NettyProducer
parameter_list|(
name|NettyEndpoint
name|nettyEndpoint
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|nettyEndpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
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
name|noReplyLogger
operator|=
operator|new
name|CamelLogger
argument_list|(
name|LOG
argument_list|,
name|configuration
operator|.
name|getNoReplyLogLevel
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
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// the producer should not be singleton otherwise cannot use concurrent producers and safely
comment|// use request/reply with correct correlation
return|return
literal|false
return|;
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
if|if
condition|(
name|isTcp
argument_list|()
condition|)
block|{
name|setupTCPCommunication
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|setupUDPCommunication
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|configuration
operator|.
name|isLazyChannelCreation
argument_list|()
condition|)
block|{
comment|// ensure the connection can be established when we start up
name|openAndCloseConnection
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Stopping producer at address: "
operator|+
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// close all channels
name|ChannelGroupFuture
name|future
init|=
name|ALL_CHANNELS
operator|.
name|close
argument_list|()
decl_stmt|;
name|future
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
comment|// and then release other resources
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|Object
name|body
init|=
name|NettyPayloadHelper
operator|.
name|getIn
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
name|noReplyLogger
operator|.
name|log
argument_list|(
literal|"No payload to send for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// if textline enabled then covert to a String which must be used for textline
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isTextline
argument_list|()
condition|)
block|{
try|try
block|{
name|body
operator|=
name|NettyHelper
operator|.
name|getTextlineBody
argument_list|(
name|body
argument_list|,
name|exchange
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getDelimiter
argument_list|()
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|isAutoAppendDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// set the exchange encoding property
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|IOConverter
operator|.
name|normalizeCharset
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|ChannelFuture
name|channelFuture
decl_stmt|;
specifier|final
name|Channel
name|channel
decl_stmt|;
try|try
block|{
name|channelFuture
operator|=
name|openConnection
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|channel
operator|=
name|openChannel
argument_list|(
name|channelFuture
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// log what we are writing
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Writing body: "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
comment|// write the body asynchronously
name|ChannelFuture
name|future
init|=
name|channel
operator|.
name|write
argument_list|(
name|body
argument_list|)
decl_stmt|;
comment|// add listener which handles the operation
name|future
operator|.
name|addListener
argument_list|(
operator|new
name|ChannelFutureListener
argument_list|()
block|{
specifier|public
name|void
name|operationComplete
parameter_list|(
name|ChannelFuture
name|channelFuture
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Operation complete "
operator|+
name|channelFuture
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|channelFuture
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
comment|// no success the set the caused exception and signal callback and break
name|exchange
operator|.
name|setException
argument_list|(
name|channelFuture
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// if we do not expect any reply then signal callback to continue routing
if|if
condition|(
operator|!
name|configuration
operator|.
name|isSync
argument_list|()
condition|)
block|{
try|try
block|{
comment|// should channel be closed after complete?
name|Boolean
name|close
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|close
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_CLOSE_CHANNEL_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|close
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_CLOSE_CHANNEL_WHEN_COMPLETE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// should we disconnect, the header can override the configuration
name|boolean
name|disconnect
init|=
name|getConfiguration
argument_list|()
operator|.
name|isDisconnect
argument_list|()
decl_stmt|;
if|if
condition|(
name|close
operator|!=
literal|null
condition|)
block|{
name|disconnect
operator|=
name|close
expr_stmt|;
block|}
if|if
condition|(
name|disconnect
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Closing channel when complete at address: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NettyHelper
operator|.
name|close
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// signal callback to continue routing
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// continue routing asynchronously
return|return
literal|false
return|;
block|}
DECL|method|setupTCPCommunication ()
specifier|protected
name|void
name|setupTCPCommunication
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|channelFactory
operator|==
literal|null
condition|)
block|{
name|ExecutorService
name|bossExecutor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyTCPBoss"
argument_list|,
name|configuration
operator|.
name|getCorePoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
decl_stmt|;
name|ExecutorService
name|workerExecutor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyTCPWorker"
argument_list|,
name|configuration
operator|.
name|getCorePoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
decl_stmt|;
name|channelFactory
operator|=
operator|new
name|NioClientSocketChannelFactory
argument_list|(
name|bossExecutor
argument_list|,
name|workerExecutor
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setupUDPCommunication ()
specifier|protected
name|void
name|setupUDPCommunication
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|datagramChannelFactory
operator|==
literal|null
condition|)
block|{
name|ExecutorService
name|workerExecutor
init|=
name|context
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|this
argument_list|,
literal|"NettyUDPWorker"
argument_list|,
name|configuration
operator|.
name|getCorePoolSize
argument_list|()
argument_list|,
name|configuration
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|)
decl_stmt|;
name|datagramChannelFactory
operator|=
operator|new
name|NioDatagramChannelFactory
argument_list|(
name|workerExecutor
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|openConnection (Exchange exchange, AsyncCallback callback)
specifier|private
name|ChannelFuture
name|openConnection
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|ChannelFuture
name|answer
decl_stmt|;
name|ChannelPipeline
name|clientPipeline
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getClientPipelineFactory
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// initialize user defined client pipeline factory
name|configuration
operator|.
name|getClientPipelineFactory
argument_list|()
operator|.
name|setProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getClientPipelineFactory
argument_list|()
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getClientPipelineFactory
argument_list|()
operator|.
name|setCallback
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|clientPipeline
operator|=
name|configuration
operator|.
name|getClientPipelineFactory
argument_list|()
operator|.
name|getPipeline
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// initialize client pipeline factory
name|ClientPipelineFactory
name|clientPipelineFactory
init|=
operator|new
name|DefaultClientPipelineFactory
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
comment|// must get the pipeline from the factory when opening a new connection
name|clientPipeline
operator|=
name|clientPipelineFactory
operator|.
name|getPipeline
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isTcp
argument_list|()
condition|)
block|{
name|ClientBootstrap
name|clientBootstrap
init|=
operator|new
name|ClientBootstrap
argument_list|(
name|channelFactory
argument_list|)
decl_stmt|;
name|clientBootstrap
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
name|clientBootstrap
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
name|clientBootstrap
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
name|clientBootstrap
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
comment|// set the pipeline on the bootstrap
name|clientBootstrap
operator|.
name|setPipeline
argument_list|(
name|clientPipeline
argument_list|)
expr_stmt|;
name|answer
operator|=
name|clientBootstrap
operator|.
name|connect
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
return|return
name|answer
return|;
block|}
else|else
block|{
name|ConnectionlessBootstrap
name|connectionlessClientBootstrap
init|=
operator|new
name|ConnectionlessBootstrap
argument_list|(
name|datagramChannelFactory
argument_list|)
decl_stmt|;
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
name|connectionlessClientBootstrap
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
comment|// set the pipeline on the bootstrap
name|connectionlessClientBootstrap
operator|.
name|setPipeline
argument_list|(
name|clientPipeline
argument_list|)
expr_stmt|;
name|connectionlessClientBootstrap
operator|.
name|bind
argument_list|(
operator|new
name|InetSocketAddress
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|=
name|connectionlessClientBootstrap
operator|.
name|connect
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
return|return
name|answer
return|;
block|}
block|}
DECL|method|openChannel (ChannelFuture channelFuture)
specifier|private
name|Channel
name|openChannel
parameter_list|(
name|ChannelFuture
name|channelFuture
parameter_list|)
throws|throws
name|Exception
block|{
comment|// wait until we got connection
name|channelFuture
operator|.
name|awaitUninterruptibly
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|channelFuture
operator|.
name|isSuccess
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Cannot connect to "
operator|+
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|,
name|channelFuture
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
name|Channel
name|channel
init|=
name|channelFuture
operator|.
name|getChannel
argument_list|()
decl_stmt|;
comment|// to keep track of all channels in use
name|ALL_CHANNELS
operator|.
name|add
argument_list|(
name|channel
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating connector to address: "
operator|+
name|configuration
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|channel
return|;
block|}
DECL|method|openAndCloseConnection ()
specifier|private
name|void
name|openAndCloseConnection
parameter_list|()
throws|throws
name|Exception
block|{
name|ChannelFuture
name|future
init|=
name|openConnection
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
block|}
block|}
argument_list|)
decl_stmt|;
name|Channel
name|channel
init|=
name|openChannel
argument_list|(
name|future
argument_list|)
decl_stmt|;
name|NettyHelper
operator|.
name|close
argument_list|(
name|channel
argument_list|)
expr_stmt|;
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
DECL|method|getAllChannels ()
specifier|public
name|ChannelGroup
name|getAllChannels
parameter_list|()
block|{
return|return
name|ALL_CHANNELS
return|;
block|}
block|}
end_class

end_unit

