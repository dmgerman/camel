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
name|handlers
operator|.
name|ServerChannelHandler
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|ChannelDownstreamHandler
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
name|ChannelUpstreamHandler
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
name|ssl
operator|.
name|SslHandler
import|;
end_import

begin_class
DECL|class|ServerPipelineFactory
specifier|public
class|class
name|ServerPipelineFactory
implements|implements
name|ChannelPipelineFactory
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ServerPipelineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
name|NettyConsumer
name|consumer
decl_stmt|;
DECL|method|ServerPipelineFactory (NettyConsumer consumer)
specifier|public
name|ServerPipelineFactory
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|getPipeline ()
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
literal|"Server SSL handler configured and added as an interceptor against the ChannelPipeline"
argument_list|)
expr_stmt|;
block|}
name|channelPipeline
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
name|ChannelUpstreamHandler
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
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"decoder-"
operator|+
name|x
argument_list|,
name|decoders
operator|.
name|get
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ChannelDownstreamHandler
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
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"encoder-"
operator|+
name|x
argument_list|,
name|encoders
operator|.
name|get
argument_list|(
name|x
argument_list|)
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
name|getHandler
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|channelPipeline
operator|.
name|addLast
argument_list|(
literal|"handler"
argument_list|,
operator|new
name|ServerChannelHandler
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|channelPipeline
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
else|else
block|{
name|SSLEngineFactory
name|sslEngineFactory
init|=
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
decl_stmt|;
name|SSLEngine
name|sslEngine
init|=
name|sslEngineFactory
operator|.
name|createServerSSLEngine
argument_list|()
decl_stmt|;
return|return
operator|new
name|SslHandler
argument_list|(
name|sslEngine
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

