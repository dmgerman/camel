begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.handlers
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
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|ClosedChannelException
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
name|http
operator|.
name|NettyHttpConsumer
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
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|DefaultHttpResponse
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
name|HttpResponse
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

begin_import
import|import static
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
name|HttpHeaders
operator|.
name|is100ContinueExpected
import|;
end_import

begin_import
import|import static
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
name|HttpHeaders
operator|.
name|isKeepAlive
import|;
end_import

begin_import
import|import static
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
name|HttpResponseStatus
operator|.
name|CONTINUE
import|;
end_import

begin_import
import|import static
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
name|HttpResponseStatus
operator|.
name|METHOD_NOT_ALLOWED
import|;
end_import

begin_import
import|import static
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
name|HttpResponseStatus
operator|.
name|SERVICE_UNAVAILABLE
import|;
end_import

begin_import
import|import static
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
name|HttpVersion
operator|.
name|HTTP_1_1
import|;
end_import

begin_comment
comment|/**  * Netty HTTP {@link ServerChannelHandler} that handles the incoming HTTP requests and routes  * the received message in Camel.  */
end_comment

begin_class
DECL|class|HttpServerChannelHandler
specifier|public
class|class
name|HttpServerChannelHandler
extends|extends
name|ServerChannelHandler
block|{
comment|// use NettyHttpConsumer as logger to make it easier to read the logs as this is part of the consumer
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
name|NettyHttpConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|NettyHttpConsumer
name|consumer
decl_stmt|;
DECL|field|request
specifier|private
name|HttpRequest
name|request
decl_stmt|;
DECL|method|HttpServerChannelHandler (NettyHttpConsumer consumer)
specifier|public
name|HttpServerChannelHandler
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
block|{
name|super
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
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
comment|// store request, as this channel handler is created per pipeline
name|request
operator|=
operator|(
name|HttpRequest
operator|)
name|messageEvent
operator|.
name|getMessage
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message received: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
if|if
condition|(
name|is100ContinueExpected
argument_list|(
name|request
argument_list|)
condition|)
block|{
comment|// send back http 100 response to continue
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|CONTINUE
argument_list|)
decl_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|consumer
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
comment|// are we suspended?
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer suspended, cannot service request {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|SERVICE_UNAVAILABLE
argument_list|)
decl_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
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
name|getHttpMethodRestrict
argument_list|()
operator|.
name|contains
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|METHOD_NOT_ALLOWED
argument_list|)
decl_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"TRACE"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|&&
operator|!
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|METHOD_NOT_ALLOWED
argument_list|)
decl_stmt|;
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// let Camel process this message
name|super
operator|.
name|messageReceived
argument_list|(
name|ctx
argument_list|,
name|messageEvent
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
comment|// only close if we are still allowed to run
if|if
condition|(
name|consumer
operator|.
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|exceptionEvent
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ClosedChannelException
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Channel already closed. Ignoring this exception."
argument_list|)
expr_stmt|;
block|}
else|else
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
block|}
annotation|@
name|Override
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
comment|// make sure to close channel if not keep-alive
if|if
condition|(
name|request
operator|!=
literal|null
operator|&&
name|isKeepAlive
argument_list|(
name|request
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Request has Connection: keep-alive so Channel is not being closed"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Request is not Connection: close so Channel is being closed"
argument_list|)
expr_stmt|;
return|return
name|ChannelFutureListener
operator|.
name|CLOSE
return|;
block|}
block|}
annotation|@
name|Override
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
comment|// use the binding
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyResponse
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyResponse
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

