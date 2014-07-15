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
name|nio
operator|.
name|channels
operator|.
name|Channels
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLEngine
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
name|ChannelHandler
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
name|handler
operator|.
name|ssl
operator|.
name|SslHandler
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
name|timeout
operator|.
name|ReadTimeoutHandler
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
name|handlers
operator|.
name|ClientChannelHandler
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
name|ssl
operator|.
name|SSLEngineFactory
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

begin_class
DECL|class|DefaultClientPipelineFactory
specifier|public
class|class
name|DefaultClientPipelineFactory
extends|extends
name|ClientPipelineFactory
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
name|DefaultClientPipelineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|NettyProducer
name|producer
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|method|DefaultClientPipelineFactory (NettyProducer producer)
specifier|public
name|DefaultClientPipelineFactory
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
try|try
block|{
name|this
operator|.
name|sslContext
operator|=
name|createSSLContext
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|initChannel (Channel ch)
specifier|protected
name|void
name|initChannel
parameter_list|(
name|Channel
name|ch
parameter_list|)
throws|throws
name|Exception
block|{
comment|// create a new pipeline
name|ChannelPipeline
name|channelPipeline
init|=
name|ch
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|SslHandler
name|sslHandler
init|=
name|configureClientSSLOnDemand
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslHandler
operator|!=
literal|null
condition|)
block|{
comment|//TODO  must close on SSL exception
comment|//sslHandler.setCloseOnSSLException(true);
name|LOG
operator|.
name|debug
argument_list|(
literal|"Client SSL handler configured and added to the ChannelPipeline: {}"
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
name|addToPipeline
argument_list|(
literal|"ssl"
argument_list|,
name|channelPipeline
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decoders
init|=
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDecoders
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|decoders
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|ChannelHandler
name|decoder
init|=
name|decoders
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|decoder
operator|instanceof
name|ChannelHandlerFactory
condition|)
block|{
comment|// use the factory to create a new instance of the channel as it may not be shareable
name|decoder
operator|=
operator|(
operator|(
name|ChannelHandlerFactory
operator|)
name|decoder
operator|)
operator|.
name|newChannelHandler
argument_list|()
expr_stmt|;
block|}
name|addToPipeline
argument_list|(
literal|"decoder-"
operator|+
name|x
argument_list|,
name|channelPipeline
argument_list|,
name|decoder
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encoders
init|=
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getEncoders
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|encoders
operator|.
name|size
argument_list|()
condition|;
name|x
operator|++
control|)
block|{
name|ChannelHandler
name|encoder
init|=
name|encoders
operator|.
name|get
argument_list|(
name|x
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoder
operator|instanceof
name|ChannelHandlerFactory
condition|)
block|{
comment|// use the factory to create a new instance of the channel as it may not be shareable
name|encoder
operator|=
operator|(
operator|(
name|ChannelHandlerFactory
operator|)
name|encoder
operator|)
operator|.
name|newChannelHandler
argument_list|()
expr_stmt|;
block|}
name|addToPipeline
argument_list|(
literal|"encoder-"
operator|+
name|x
argument_list|,
name|channelPipeline
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
block|}
comment|// do we use request timeout?
if|if
condition|(
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using request timeout {} millis"
argument_list|,
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ChannelHandler
name|timeout
init|=
operator|new
name|ReadTimeoutHandler
argument_list|(
name|NettyComponent
operator|.
name|getTimer
argument_list|()
argument_list|,
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRequestTimeout
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|addToPipeline
argument_list|(
literal|"timeout"
argument_list|,
name|channelPipeline
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|// our handler must be added last
name|addToPipeline
argument_list|(
literal|"handler"
argument_list|,
name|channelPipeline
argument_list|,
operator|new
name|ClientChannelHandler
argument_list|(
name|producer
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Created ChannelPipeline: {}"
argument_list|,
name|channelPipeline
argument_list|)
expr_stmt|;
block|}
DECL|method|addToPipeline (String name, ChannelPipeline pipeline, ChannelHandler handler)
specifier|private
name|void
name|addToPipeline
parameter_list|(
name|String
name|name
parameter_list|,
name|ChannelPipeline
name|pipeline
parameter_list|,
name|ChannelHandler
name|handler
parameter_list|)
block|{
name|pipeline
operator|.
name|addLast
argument_list|(
name|name
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
DECL|method|createSSLContext (NettyProducer producer)
specifier|private
name|SSLContext
name|createSSLContext
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
name|NettyConfiguration
name|configuration
init|=
name|producer
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|configuration
operator|.
name|isSsl
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|SSLContext
name|answer
decl_stmt|;
comment|// create ssl context once
if|if
condition|(
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|configuration
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|configuration
operator|.
name|getKeyStoreFile
argument_list|()
operator|==
literal|null
operator|&&
name|configuration
operator|.
name|getKeyStoreResource
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"keystorefile is null"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getTrustStoreFile
argument_list|()
operator|==
literal|null
operator|&&
name|configuration
operator|.
name|getTrustStoreResource
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"truststorefile is null"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configuration
operator|.
name|getPassphrase
argument_list|()
operator|.
name|toCharArray
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"passphrase is null"
argument_list|)
expr_stmt|;
block|}
name|SSLEngineFactory
name|sslEngineFactory
decl_stmt|;
if|if
condition|(
name|configuration
operator|.
name|getKeyStoreFile
argument_list|()
operator|!=
literal|null
operator|||
name|configuration
operator|.
name|getTrustStoreFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sslEngineFactory
operator|=
operator|new
name|SSLEngineFactory
argument_list|()
expr_stmt|;
name|answer
operator|=
name|sslEngineFactory
operator|.
name|createSSLContext
argument_list|(
name|producer
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|configuration
operator|.
name|getKeyStoreFormat
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecurityProvider
argument_list|()
argument_list|,
literal|"file:"
operator|+
name|configuration
operator|.
name|getKeyStoreFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
literal|"file:"
operator|+
name|configuration
operator|.
name|getTrustStoreFile
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassphrase
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sslEngineFactory
operator|=
operator|new
name|SSLEngineFactory
argument_list|()
expr_stmt|;
name|answer
operator|=
name|sslEngineFactory
operator|.
name|createSSLContext
argument_list|(
name|producer
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|configuration
operator|.
name|getKeyStoreFormat
argument_list|()
argument_list|,
name|configuration
operator|.
name|getSecurityProvider
argument_list|()
argument_list|,
name|configuration
operator|.
name|getKeyStoreResource
argument_list|()
argument_list|,
name|configuration
operator|.
name|getTrustStoreResource
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPassphrase
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|configureClientSSLOnDemand ()
specifier|private
name|SslHandler
name|configureClientSSLOnDemand
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSsl
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslHandler
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|sslContext
operator|!=
literal|null
condition|)
block|{
name|SSLEngine
name|engine
init|=
name|sslContext
operator|.
name|createSSLEngine
argument_list|()
decl_stmt|;
name|engine
operator|.
name|setUseClientMode
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
operator|new
name|SslHandler
argument_list|(
name|engine
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPipelineFactory (NettyProducer producer)
specifier|public
name|ClientPipelineFactory
name|createPipelineFactory
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
block|{
return|return
operator|new
name|DefaultClientPipelineFactory
argument_list|(
name|producer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

