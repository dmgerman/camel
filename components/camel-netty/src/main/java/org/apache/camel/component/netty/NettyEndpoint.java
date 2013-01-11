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
name|Producer
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
name|DefaultEndpoint
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
name|SynchronousDelegateProducer
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
name|util
operator|.
name|Timer
import|;
end_import

begin_class
DECL|class|NettyEndpoint
specifier|public
class|class
name|NettyEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|configuration
specifier|private
name|NettyConfiguration
name|configuration
decl_stmt|;
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
DECL|method|NettyEndpoint (String endpointUri, NettyComponent component, NettyConfiguration configuration)
specifier|public
name|NettyEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|NettyComponent
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
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
return|return
operator|new
name|NettyConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Producer
name|answer
init|=
operator|new
name|NettyProducer
argument_list|(
name|this
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSynchronous
argument_list|()
condition|)
block|{
return|return
operator|new
name|SynchronousDelegateProducer
argument_list|(
name|answer
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|answer
return|;
block|}
block|}
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
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|NettyPayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|messageEvent
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|NettyComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|NettyComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
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
DECL|method|setTimer (Timer timer)
specifier|public
name|void
name|setTimer
parameter_list|(
name|Timer
name|timer
parameter_list|)
block|{
name|this
operator|.
name|timer
operator|=
name|timer
expr_stmt|;
block|}
DECL|method|getTimer ()
specifier|public
name|Timer
name|getTimer
parameter_list|()
block|{
return|return
name|timer
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|timer
argument_list|,
literal|"timer"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

