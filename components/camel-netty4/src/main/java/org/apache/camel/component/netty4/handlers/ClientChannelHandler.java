begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.handlers
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
name|handlers
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
name|CamelExchangeException
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
name|component
operator|.
name|netty4
operator|.
name|NettyCamelState
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
name|netty4
operator|.
name|NettyHelper
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
name|NettyPayloadHelper
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
name|NettyProducer
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
name|channel
operator|.
name|ChannelHandler
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
name|ChannelStateEvent
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
name|ExceptionEvent
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
name|channel
operator|.
name|SimpleChannelUpstreamHandler
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
comment|/**  * Client handler which cannot be shared  */
end_comment

begin_class
DECL|class|ClientChannelHandler
specifier|public
class|class
name|ClientChannelHandler
extends|extends
name|SimpleChannelUpstreamHandler
block|{
comment|// use NettyProducer as logger to make it easier to read the logs as this is part of the producer
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
name|NettyProducer
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
DECL|field|messageReceived
specifier|private
specifier|volatile
name|boolean
name|messageReceived
decl_stmt|;
DECL|field|exceptionHandled
specifier|private
specifier|volatile
name|boolean
name|exceptionHandled
decl_stmt|;
DECL|method|ClientChannelHandler (NettyProducer producer)
specifier|public
name|ClientChannelHandler
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
block|}
annotation|@
name|Override
DECL|method|channelOpen (ChannelHandlerContext ctx, ChannelStateEvent channelStateEvent)
specifier|public
name|void
name|channelOpen
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ChannelStateEvent
name|channelStateEvent
parameter_list|)
throws|throws
name|Exception
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
literal|"Channel open: {}"
argument_list|,
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// to keep track of open sockets
name|producer
operator|.
name|getAllChannels
argument_list|()
operator|.
name|add
argument_list|(
name|channelStateEvent
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, ExceptionEvent exceptionEvent)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ExceptionEvent
name|exceptionEvent
parameter_list|)
throws|throws
name|Exception
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
literal|"Exception caught at Channel: "
operator|+
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|,
name|exceptionEvent
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exceptionHandled
condition|)
block|{
comment|// ignore subsequent exceptions being thrown
return|return;
block|}
name|exceptionHandled
operator|=
literal|true
expr_stmt|;
name|Throwable
name|cause
init|=
name|exceptionEvent
operator|.
name|getCause
argument_list|()
decl_stmt|;
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
literal|"Closing channel as an exception was thrown from Netty"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getExchange
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|AsyncCallback
name|callback
init|=
name|getAsyncCallback
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
comment|// the state may not be set
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|callback
operator|!=
literal|null
condition|)
block|{
comment|// set the cause on the exchange
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
comment|// close channel in case an exception was thrown
name|NettyHelper
operator|.
name|close
argument_list|(
name|exceptionEvent
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
comment|// signal callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|channelClosed (ChannelHandlerContext ctx, ChannelStateEvent e)
specifier|public
name|void
name|channelClosed
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ChannelStateEvent
name|e
parameter_list|)
throws|throws
name|Exception
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
literal|"Channel closed: {}"
argument_list|,
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|getExchange
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|AsyncCallback
name|callback
init|=
name|getAsyncCallback
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
comment|// remove state
name|producer
operator|.
name|removeState
argument_list|(
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
comment|// to keep track of open sockets
name|producer
operator|.
name|getAllChannels
argument_list|()
operator|.
name|remove
argument_list|(
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
operator|&&
operator|!
name|messageReceived
operator|&&
operator|!
name|exceptionHandled
condition|)
block|{
comment|// To avoid call the callback.done twice
name|exceptionHandled
operator|=
literal|true
expr_stmt|;
comment|// session was closed but no message received. This could be because the remote server had an internal error
comment|// and could not return a response. We should count down to stop waiting for a response
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
literal|"Channel closed but no message received from address: {}"
argument_list|,
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"No response received from remote server: "
operator|+
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// signal callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|messageReceived (ChannelHandlerContext ctx, MessageEvent messageEvent)
specifier|public
name|void
name|messageReceived
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
name|messageReceived
operator|=
literal|true
expr_stmt|;
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
literal|"Message received: {}"
argument_list|,
name|messageEvent
argument_list|)
expr_stmt|;
block|}
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
name|ChannelHandler
name|handler
init|=
name|ctx
operator|.
name|getPipeline
argument_list|()
operator|.
name|get
argument_list|(
literal|"timeout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removing timeout channel as we received message"
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getPipeline
argument_list|()
operator|.
name|remove
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
name|Exchange
name|exchange
init|=
name|getExchange
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
if|if
condition|(
name|exchange
operator|==
literal|null
condition|)
block|{
comment|// we just ignore the received message as the channel is closed
return|return;
block|}
name|AsyncCallback
name|callback
init|=
name|getAsyncCallback
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|Message
name|message
decl_stmt|;
try|try
block|{
name|message
operator|=
name|getResponseMessage
argument_list|(
name|exchange
argument_list|,
name|messageEvent
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
literal|false
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// set the result on either IN or OUT on the original exchange depending on its pattern
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
name|exchange
operator|.
name|setOut
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
comment|// check the setting on the exchange property
if|if
condition|(
name|close
operator|==
literal|null
condition|)
block|{
name|close
operator|=
name|exchange
operator|.
name|getProperty
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
name|producer
operator|.
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
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closing channel when complete at address: {}"
argument_list|,
name|producer
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
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// signal callback
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Gets the Camel {@link Message} to use as the message to be set on the current {@link Exchange} when      * we have received a reply message.      *<p/>      *      * @param exchange      the current exchange      * @param messageEvent  the incoming event which has the response message from Netty.      * @return the Camel {@link Message} to set on the current {@link Exchange} as the response message.      * @throws Exception is thrown if error getting the response message      */
DECL|method|getResponseMessage (Exchange exchange, MessageEvent messageEvent)
specifier|protected
name|Message
name|getResponseMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|MessageEvent
name|messageEvent
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|messageEvent
operator|.
name|getMessage
argument_list|()
decl_stmt|;
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
literal|"Channel: {} received body: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|messageEvent
operator|.
name|getChannel
argument_list|()
block|,
name|body
block|}
argument_list|)
expr_stmt|;
block|}
comment|// if textline enabled then covert to a String which must be used for textline
if|if
condition|(
name|producer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTextline
argument_list|()
condition|)
block|{
name|body
operator|=
name|producer
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|// set the result on either IN or OUT on the original exchange depending on its pattern
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
name|NettyPayloadHelper
operator|.
name|setOut
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
else|else
block|{
name|NettyPayloadHelper
operator|.
name|setIn
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|)
expr_stmt|;
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
DECL|method|getExchange (ChannelHandlerContext ctx)
specifier|private
name|Exchange
name|getExchange
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
block|{
name|NettyCamelState
name|state
init|=
name|producer
operator|.
name|getState
argument_list|(
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|state
operator|!=
literal|null
condition|?
name|state
operator|.
name|getExchange
argument_list|()
else|:
literal|null
return|;
block|}
DECL|method|getAsyncCallback (ChannelHandlerContext ctx)
specifier|private
name|AsyncCallback
name|getAsyncCallback
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|)
block|{
name|NettyCamelState
name|state
init|=
name|producer
operator|.
name|getState
argument_list|(
name|ctx
operator|.
name|getChannel
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|state
operator|!=
literal|null
condition|?
name|state
operator|.
name|getCallback
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

