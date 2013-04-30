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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Message
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
name|component
operator|.
name|netty
operator|.
name|NettyConfiguration
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
name|NettyConstants
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
name|NettyEndpoint
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|spi
operator|.
name|HeaderFilterStrategyAware
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
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_comment
comment|/**  * HTTP based {@link NettyEndpoint}  */
end_comment

begin_class
DECL|class|NettyHttpEndpoint
specifier|public
class|class
name|NettyHttpEndpoint
extends|extends
name|NettyEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|nettyHttpBinding
specifier|private
name|NettyHttpBinding
name|nettyHttpBinding
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|field|traceEnabled
specifier|private
name|boolean
name|traceEnabled
decl_stmt|;
DECL|field|httpMethodRestrict
specifier|private
name|String
name|httpMethodRestrict
decl_stmt|;
DECL|method|NettyHttpEndpoint (String endpointUri, NettyHttpComponent component, NettyConfiguration configuration)
specifier|public
name|NettyHttpEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|NettyHttpComponent
name|component
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
operator|new
name|NettyHttpConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|getConfiguration
argument_list|()
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ChannelHandlerContext ctx, MessageEvent messageEvent)
specifier|public
name|Exchange
name|createExchange
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
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
comment|// use the http binding
name|HttpRequest
name|request
init|=
operator|(
name|HttpRequest
operator|)
name|messageEvent
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|Message
name|in
init|=
name|getNettyHttpBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|request
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
comment|// set additional headers
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_CHANNEL_HANDLER_CONTEXT
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_MESSAGE_EVENT
argument_list|,
name|messageEvent
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_REMOTE_ADDRESS
argument_list|,
name|messageEvent
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|NettyConstants
operator|.
name|NETTY_LOCAL_ADDRESS
argument_list|,
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|getLocalAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// honor the character encoding
name|String
name|contentType
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|NettyHttpHelper
operator|.
name|setCharsetFromContentType
argument_list|(
name|contentType
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|NettyHttpConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
operator|(
name|NettyHttpConfiguration
operator|)
name|super
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getNettyHttpBinding ()
specifier|public
name|NettyHttpBinding
name|getNettyHttpBinding
parameter_list|()
block|{
return|return
name|nettyHttpBinding
return|;
block|}
DECL|method|setNettyHttpBinding (NettyHttpBinding nettyHttpBinding)
specifier|public
name|void
name|setNettyHttpBinding
parameter_list|(
name|NettyHttpBinding
name|nettyHttpBinding
parameter_list|)
block|{
name|this
operator|.
name|nettyHttpBinding
operator|=
name|nettyHttpBinding
expr_stmt|;
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|isTraceEnabled ()
specifier|public
name|boolean
name|isTraceEnabled
parameter_list|()
block|{
return|return
name|traceEnabled
return|;
block|}
DECL|method|setTraceEnabled (boolean traceEnabled)
specifier|public
name|void
name|setTraceEnabled
parameter_list|(
name|boolean
name|traceEnabled
parameter_list|)
block|{
name|this
operator|.
name|traceEnabled
operator|=
name|traceEnabled
expr_stmt|;
block|}
DECL|method|getHttpMethodRestrict ()
specifier|public
name|String
name|getHttpMethodRestrict
parameter_list|()
block|{
return|return
name|httpMethodRestrict
return|;
block|}
DECL|method|setHttpMethodRestrict (String httpMethodRestrict)
specifier|public
name|void
name|setHttpMethodRestrict
parameter_list|(
name|String
name|httpMethodRestrict
parameter_list|)
block|{
name|this
operator|.
name|httpMethodRestrict
operator|=
name|httpMethodRestrict
expr_stmt|;
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|nettyHttpBinding
argument_list|,
literal|"nettyHttpBinding"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|headerFilterStrategy
argument_list|,
literal|"headerFilterStrategy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

