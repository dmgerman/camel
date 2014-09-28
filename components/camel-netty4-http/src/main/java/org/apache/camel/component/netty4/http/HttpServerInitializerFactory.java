begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
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
operator|.
name|http
package|;
end_package

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
name|codec
operator|.
name|http
operator|.
name|HttpContentCompressor
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
name|http
operator|.
name|HttpObjectAggregator
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
name|http
operator|.
name|HttpRequestDecoder
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
name|http
operator|.
name|HttpResponseEncoder
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
name|util
operator|.
name|concurrent
operator|.
name|EventExecutorGroup
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
name|ChannelHandlerFactory
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
name|NettyConsumer
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
name|NettyServerBootstrapConfiguration
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
name|ServerInitializerFactory
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

begin_comment
comment|/**  * {@link ServerInitializerFactory} for the Netty HTTP server.  */
end_comment

begin_class
DECL|class|HttpServerInitializerFactory
specifier|public
class|class
name|HttpServerInitializerFactory
extends|extends
name|ServerInitializerFactory
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
name|HttpServerInitializerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|protected
name|NettyHttpConsumer
name|consumer
decl_stmt|;
DECL|field|sslContext
specifier|protected
name|SSLContext
name|sslContext
decl_stmt|;
DECL|field|configuration
specifier|protected
name|NettyHttpConfiguration
name|configuration
decl_stmt|;
DECL|method|HttpServerInitializerFactory ()
specifier|public
name|HttpServerInitializerFactory
parameter_list|()
block|{
comment|// default constructor needed
block|}
DECL|method|HttpServerInitializerFactory (NettyHttpConsumer nettyConsumer)
specifier|public
name|HttpServerInitializerFactory
parameter_list|(
name|NettyHttpConsumer
name|nettyConsumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|nettyConsumer
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|nettyConsumer
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
try|try
block|{
name|this
operator|.
name|sslContext
operator|=
name|createSSLContext
argument_list|(
name|consumer
operator|.
name|getContext
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
if|if
condition|(
name|sslContext
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Created SslContext {}"
argument_list|,
name|sslContext
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createPipelineFactory (NettyConsumer nettyConsumer)
specifier|public
name|ServerInitializerFactory
name|createPipelineFactory
parameter_list|(
name|NettyConsumer
name|nettyConsumer
parameter_list|)
block|{
return|return
operator|new
name|HttpServerInitializerFactory
argument_list|(
operator|(
name|NettyHttpConsumer
operator|)
name|nettyConsumer
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|pipeline
init|=
name|ch
operator|.
name|pipeline
argument_list|()
decl_stmt|;
name|SslHandler
name|sslHandler
init|=
name|configureServerSSLOnDemand
argument_list|()
decl_stmt|;
if|if
condition|(
name|sslHandler
operator|!=
literal|null
condition|)
block|{
comment|//TODO must close on SSL exception
comment|// sslHandler.setCloseOnSSLException(true);
name|LOG
operator|.
name|debug
argument_list|(
literal|"Server SSL handler configured and added as an interceptor against the ChannelPipeline: {}"
argument_list|,
name|sslHandler
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"ssl"
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
name|consumer
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
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-"
operator|+
name|x
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
name|consumer
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
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"encoder-"
operator|+
name|x
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
block|}
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"decoder"
argument_list|,
operator|new
name|HttpRequestDecoder
argument_list|()
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"aggregator"
argument_list|,
operator|new
name|HttpObjectAggregator
argument_list|(
name|configuration
operator|.
name|getChunkedMaxContentLength
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"encoder"
argument_list|,
operator|new
name|HttpResponseEncoder
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportCompressed
argument_list|()
condition|)
block|{
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"deflater"
argument_list|,
operator|new
name|HttpContentCompressor
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|port
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
decl_stmt|;
name|ChannelHandler
name|handler
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getMultiplexChannelHandler
argument_list|(
name|port
argument_list|)
operator|.
name|getChannelHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUsingExecutorService
argument_list|()
condition|)
block|{
name|EventExecutorGroup
name|applicationExecutor
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getExecutorService
argument_list|()
decl_stmt|;
name|pipeline
operator|.
name|addLast
argument_list|(
name|applicationExecutor
argument_list|,
literal|"handler"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSSLContext (CamelContext camelContext, NettyServerBootstrapConfiguration configuration)
specifier|private
name|SSLContext
name|createSSLContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|NettyServerBootstrapConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
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
name|camelContext
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
name|camelContext
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
DECL|method|configureServerSSLOnDemand ()
specifier|private
name|SslHandler
name|configureServerSSLOnDemand
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|consumer
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
name|consumer
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
name|consumer
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
literal|false
argument_list|)
expr_stmt|;
name|engine
operator|.
name|setNeedClientAuth
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isNeedClientAuth
argument_list|()
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
DECL|method|supportCompressed ()
specifier|private
name|boolean
name|supportCompressed
parameter_list|()
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isCompression
argument_list|()
return|;
block|}
block|}
end_class

end_unit

