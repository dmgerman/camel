begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.handlers
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
name|ExchangePattern
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
name|netty
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
comment|/**  * Server handler which is shared  */
end_comment

begin_class
annotation|@
name|ChannelHandler
operator|.
name|Sharable
DECL|class|ServerChannelHandler
specifier|public
class|class
name|ServerChannelHandler
extends|extends
name|SimpleChannelUpstreamHandler
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
name|ServerChannelHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
name|NettyConsumer
name|consumer
decl_stmt|;
DECL|field|noReplyLogger
specifier|private
name|CamelLogger
name|noReplyLogger
decl_stmt|;
DECL|method|ServerChannelHandler (NettyConsumer consumer)
specifier|public
name|ServerChannelHandler
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
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
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getNoReplyLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|channelOpen (ChannelHandlerContext ctx, ChannelStateEvent e)
specifier|public
name|void
name|channelOpen
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
literal|"Channel open: "
operator|+
name|e
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// to keep track of open sockets
name|consumer
operator|.
name|getAllChannels
argument_list|()
operator|.
name|add
argument_list|(
name|e
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Channel closed: "
operator|+
name|e
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// only close if we are still allowed to run
if|if
condition|(
name|consumer
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Closing channel as an exception was thrown from Netty"
argument_list|,
name|exceptionEvent
operator|.
name|getCause
argument_list|()
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
name|Object
name|in
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
literal|"Incoming message: "
operator|+
name|in
argument_list|)
expr_stmt|;
block|}
comment|// create Exchange and let the consumer process it
name|Exchange
name|exchange
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|ctx
argument_list|,
name|messageEvent
argument_list|)
decl_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
comment|// set the exchange charset property for converting
if|if
condition|(
name|consumer
operator|.
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
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getCharsetName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// send back response if the communication is synchronous
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isSync
argument_list|()
condition|)
block|{
name|sendResponse
argument_list|(
name|messageEvent
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sendResponse (MessageEvent messageEvent, Exchange exchange)
specifier|private
name|void
name|sendResponse
parameter_list|(
name|MessageEvent
name|messageEvent
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
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
name|body
operator|=
name|NettyPayloadHelper
operator|.
name|getOut
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|body
operator|=
name|NettyPayloadHelper
operator|.
name|getIn
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
if|if
condition|(
name|failed
operator|&&
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTransferExchange
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|body
operator|=
name|exchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// failed and no exception, must be a fault
name|body
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
block|}
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
literal|"No payload to send as reply for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnectOnNoReply
argument_list|()
condition|)
block|{
comment|// must close session if no data to write otherwise client will never receive a response
comment|// and wait forever (if not timing out)
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
literal|"Closing channel as no payload to send as reply at address: "
operator|+
name|messageEvent
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NettyHelper
operator|.
name|close
argument_list|(
name|messageEvent
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// if textline enabled then covert to a String which must be used for textline
if|if
condition|(
name|consumer
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
name|NettyHelper
operator|.
name|getTextlineBody
argument_list|(
name|body
argument_list|,
name|exchange
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDelimiter
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoAppendDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// we got a body to write
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
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isTcp
argument_list|()
condition|)
block|{
name|NettyHelper
operator|.
name|writeBodySync
argument_list|(
name|messageEvent
operator|.
name|getChannel
argument_list|()
argument_list|,
literal|null
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NettyHelper
operator|.
name|writeBodySync
argument_list|(
name|messageEvent
operator|.
name|getChannel
argument_list|()
argument_list|,
name|messageEvent
operator|.
name|getRemoteAddress
argument_list|()
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
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
name|consumer
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
name|messageEvent
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|NettyHelper
operator|.
name|close
argument_list|(
name|messageEvent
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

