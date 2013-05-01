begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
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
operator|.
name|http
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|netty
operator|.
name|ServerPipelineFactory
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
name|netty
operator|.
name|http
operator|.
name|handlers
operator|.
name|HttpServerChannelHandler
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
name|netty
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
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpChunkAggregator
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
name|http
operator|.
name|HttpContentCompressor
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
name|http
operator|.
name|HttpRequestDecoder
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
name|http
operator|.
name|HttpResponseEncoder
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
name|ssl
operator|.
name|SslHandler
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
comment|/**  * {@link ServerPipelineFactory} for the Netty HTTP server.  */
end_comment

begin_class
DECL|class|HttpServerPipelineFactory
specifier|public
class|class
name|HttpServerPipelineFactory
extends|extends
name|ServerPipelineFactory
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
name|HttpServerPipelineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
name|NettyHttpConsumer
name|consumer
decl_stmt|;
DECL|field|sslContext
specifier|private
name|SSLContext
name|sslContext
decl_stmt|;
DECL|method|HttpServerPipelineFactory ()
specifier|public
name|HttpServerPipelineFactory
parameter_list|()
block|{
comment|// default constructor needed
block|}
DECL|method|HttpServerPipelineFactory (NettyHttpConsumer nettyConsumer)
specifier|public
name|HttpServerPipelineFactory
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
try|try
block|{
name|this
operator|.
name|sslContext
operator|=
name|createSSLContext
argument_list|(
name|consumer
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
annotation|@
name|Override
DECL|method|createPipelineFactory (NettyConsumer nettyConsumer)
specifier|public
name|ServerPipelineFactory
name|createPipelineFactory
parameter_list|(
name|NettyConsumer
name|nettyConsumer
parameter_list|)
block|{
return|return
operator|new
name|HttpServerPipelineFactory
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
DECL|method|getPipeline ()
specifier|public
name|ChannelPipeline
name|getPipeline
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a default pipeline implementation.
name|ChannelPipeline
name|pipeline
init|=
name|Channels
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
comment|// Uncomment the following line if you don't want to handle HttpChunks.
if|if
condition|(
name|supportChunked
argument_list|()
condition|)
block|{
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"aggregator"
argument_list|,
operator|new
name|HttpChunkAggregator
argument_list|(
literal|1048576
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
comment|// handler to route Camel messages
name|pipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
operator|new
name|HttpServerChannelHandler
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|pipeline
return|;
block|}
DECL|method|createSSLContext (NettyConsumer consumer)
specifier|private
name|SSLContext
name|createSSLContext
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
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
comment|// create ssl context once
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SSLContext
name|context
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSslContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|()
decl_stmt|;
return|return
name|context
return|;
block|}
return|return
literal|null
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
else|else
block|{
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreFile
argument_list|()
operator|==
literal|null
operator|&&
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTrustStoreFile
argument_list|()
operator|==
literal|null
operator|&&
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreFile
argument_list|()
operator|!=
literal|null
operator|||
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreFormat
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecurityProvider
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreFile
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTrustStoreFile
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
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
argument_list|(
name|consumer
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreFormat
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSecurityProvider
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getKeyStoreResource
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTrustStoreResource
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPassphrase
argument_list|()
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SSLEngine
name|sslEngine
init|=
name|sslEngineFactory
operator|.
name|createServerSSLEngine
argument_list|()
decl_stmt|;
name|sslEngine
operator|.
name|setUseClientMode
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sslEngine
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
name|sslEngine
argument_list|)
return|;
block|}
block|}
DECL|method|supportChunked ()
specifier|private
name|boolean
name|supportChunked
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
name|isChunked
argument_list|()
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

