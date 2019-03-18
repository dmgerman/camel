begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|SocketAddress
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
name|ChannelFutureListener
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
name|ChannelHandlerContext
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
name|SimpleChannelInboundHandler
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
name|spi
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
name|IOHelper
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
comment|/**  * Server handler which cannot be shared  */
end_comment

begin_class
DECL|class|ServerChannelHandler
specifier|public
class|class
name|ServerChannelHandler
extends|extends
name|SimpleChannelInboundHandler
argument_list|<
name|Object
argument_list|>
block|{
comment|// use NettyConsumer as logger to make it easier to read the logs as this is part of the consumer
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
name|NettyConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|NettyConsumer
name|consumer
decl_stmt|;
DECL|field|noReplyLogger
specifier|private
specifier|final
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
DECL|method|channelActive (ChannelHandlerContext ctx)
specifier|public
name|void
name|channelActive
parameter_list|(
name|ChannelHandlerContext
name|ctx
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
name|channel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// to keep track of open sockets
name|consumer
operator|.
name|getNettyServerBootstrapFactory
argument_list|()
operator|.
name|addChannel
argument_list|(
name|ctx
operator|.
name|channel
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|channelActive
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|channelInactive (ChannelHandlerContext ctx)
specifier|public
name|void
name|channelInactive
parameter_list|(
name|ChannelHandlerContext
name|ctx
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
name|channel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// to keep track of open sockets
name|consumer
operator|.
name|getNettyServerBootstrapFactory
argument_list|()
operator|.
name|removeChannel
argument_list|(
name|ctx
operator|.
name|channel
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|channelInactive
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, Throwable cause)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Throwable
name|cause
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
comment|// let the exception handler deal with it
name|consumer
operator|.
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Closing channel as an exception was thrown from Netty"
argument_list|,
name|cause
argument_list|)
expr_stmt|;
comment|// close channel in case an exception was thrown
name|NettyHelper
operator|.
name|close
argument_list|(
name|ctx
operator|.
name|channel
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|channelRead0 (ChannelHandlerContext ctx, Object msg)
specifier|protected
name|void
name|channelRead0
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|msg
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|in
init|=
name|msg
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
name|ctx
operator|.
name|channel
argument_list|()
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
comment|// create Exchange and let the consumer process it
specifier|final
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
name|msg
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
name|IOHelper
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
if|if
condition|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isReuseChannel
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|NettyConstants
operator|.
name|NETTY_CHANNEL
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// we want to handle the UoW
name|consumer
operator|.
name|createUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|beforeProcess
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// process accordingly to endpoint configuration
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isSynchronous
argument_list|()
condition|)
block|{
name|processSynchronously
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processAsynchronously
argument_list|(
name|exchange
argument_list|,
name|ctx
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Allows any custom logic before the {@link Exchange} is processed by the routing engine.      *      * @param exchange       the exchange      * @param ctx            the channel handler context      * @param message        the message which needs to be sent      */
DECL|method|beforeProcess (final Exchange exchange, final ChannelHandlerContext ctx, final Object message)
specifier|protected
name|void
name|beforeProcess
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|ChannelHandlerContext
name|ctx
parameter_list|,
specifier|final
name|Object
name|message
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|processSynchronously (final Exchange exchange, final ChannelHandlerContext ctx, final Object message)
specifier|private
name|void
name|processSynchronously
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|ChannelHandlerContext
name|ctx
parameter_list|,
specifier|final
name|Object
name|message
parameter_list|)
block|{
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
name|message
argument_list|,
name|ctx
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
finally|finally
block|{
name|consumer
operator|.
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processAsynchronously (final Exchange exchange, final ChannelHandlerContext ctx, final Object message)
specifier|private
name|void
name|processAsynchronously
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|ChannelHandlerContext
name|ctx
parameter_list|,
specifier|final
name|Object
name|message
parameter_list|)
block|{
name|consumer
operator|.
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// send back response if the communication is synchronous
try|try
block|{
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
name|message
argument_list|,
name|ctx
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
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
finally|finally
block|{
name|consumer
operator|.
name|doneUoW
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|sendResponse (Object message, ChannelHandlerContext ctx, Exchange exchange)
specifier|private
name|void
name|sendResponse
parameter_list|(
name|Object
name|message
parameter_list|,
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|getResponseBody
argument_list|(
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
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closing channel as no payload to send as reply at address: {}"
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
operator|.
name|remoteAddress
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
name|channel
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
name|ChannelFutureListener
name|listener
init|=
name|createResponseFutureListener
argument_list|(
name|consumer
argument_list|,
name|exchange
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
operator|.
name|remoteAddress
argument_list|()
argument_list|)
decl_stmt|;
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
name|writeBodyAsync
argument_list|(
name|LOG
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
argument_list|,
literal|null
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NettyHelper
operator|.
name|writeBodyAsync
argument_list|(
name|LOG
argument_list|,
name|ctx
operator|.
name|channel
argument_list|()
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|NettyConstants
operator|.
name|NETTY_REMOTE_ADDRESS
argument_list|,
name|SocketAddress
operator|.
name|class
argument_list|)
argument_list|,
name|body
argument_list|,
name|exchange
argument_list|,
name|listener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Gets the object we want to use as the response object for sending to netty.      *      * @param exchange the exchange      * @return the object to use as response      * @throws Exception is thrown if error getting the response body      */
DECL|method|getResponseBody (Exchange exchange)
specifier|protected
name|Object
name|getResponseBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if there was an exception then use that as response body
name|boolean
name|exception
init|=
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
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
decl_stmt|;
if|if
condition|(
name|exception
condition|)
block|{
return|return
name|exchange
operator|.
name|getException
argument_list|()
return|;
block|}
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
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
return|;
block|}
else|else
block|{
return|return
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
return|;
block|}
block|}
comment|/**      * Creates the {@link ChannelFutureListener} to execute when writing the response is complete.      *      * @param consumer          the netty consumer      * @param exchange          the exchange      * @param remoteAddress     the remote address of the message      * @return the listener.      */
DECL|method|createResponseFutureListener (NettyConsumer consumer, Exchange exchange, SocketAddress remoteAddress)
specifier|protected
name|ChannelFutureListener
name|createResponseFutureListener
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|SocketAddress
name|remoteAddress
parameter_list|)
block|{
return|return
operator|new
name|ServerResponseFutureListener
argument_list|(
name|consumer
argument_list|,
name|exchange
argument_list|,
name|remoteAddress
argument_list|)
return|;
block|}
block|}
end_class

end_unit

