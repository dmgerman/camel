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
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|http
operator|.
name|ContextPathMatcher
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
name|DefaultContextPathMatcher
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
name|HttpServerConsumerChannelFactory
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|UnsafeUriCharactersEncoder
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
name|buffer
operator|.
name|ChannelBuffers
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
name|HttpResponseStatus
operator|.
name|NOT_FOUND
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
comment|/**  * A multiplex {@link org.apache.camel.component.netty.http.HttpServerPipelineFactory} which keeps a list of handlers, and delegates to the  * target handler based on the http context path in the incoming request. This is used to allow to reuse  * the same Netty consumer, allowing to have multiple routes on the same netty {@link org.jboss.netty.bootstrap.ServerBootstrap}  */
end_comment

begin_class
DECL|class|HttpServerMultiplexChannelHandler
specifier|public
class|class
name|HttpServerMultiplexChannelHandler
extends|extends
name|SimpleChannelUpstreamHandler
implements|implements
name|HttpServerConsumerChannelFactory
block|{
comment|// use NettyHttpConsumer as logger to make it easier to read the logs as this is part of the consumer
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
name|NettyHttpConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
name|consumers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|token
specifier|private
name|String
name|token
decl_stmt|;
DECL|field|len
specifier|private
name|int
name|len
decl_stmt|;
DECL|method|HttpServerMultiplexChannelHandler ()
specifier|public
name|HttpServerMultiplexChannelHandler
parameter_list|()
block|{
comment|// must have default no-arg constructor to allow IoC containers to manage it
block|}
DECL|method|init (int port)
specifier|public
name|void
name|init
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|this
operator|.
name|token
operator|=
literal|":"
operator|+
name|port
expr_stmt|;
name|this
operator|.
name|len
operator|=
name|token
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
DECL|method|addConsumer (NettyHttpConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
block|{
name|String
name|path
init|=
name|pathAsKey
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|ContextPathMatcher
name|matcher
init|=
operator|new
name|DefaultContextPathMatcher
argument_list|(
name|path
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
argument_list|)
decl_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|matcher
argument_list|,
operator|new
name|HttpServerChannelHandler
argument_list|(
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (NettyHttpConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
block|{
name|String
name|path
init|=
name|pathAsKey
argument_list|(
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
decl_stmt|;
name|ContextPathMatcher
name|matcher
init|=
operator|new
name|DefaultContextPathMatcher
argument_list|(
name|path
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
argument_list|)
decl_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|matcher
argument_list|)
expr_stmt|;
block|}
DECL|method|consumers ()
specifier|public
name|int
name|consumers
parameter_list|()
block|{
return|return
name|consumers
operator|.
name|size
argument_list|()
return|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getChannelHandler ()
specifier|public
name|ChannelHandler
name|getChannelHandler
parameter_list|()
block|{
return|return
name|this
return|;
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message received: {}"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|HttpServerChannelHandler
name|handler
init|=
name|getHandler
argument_list|(
name|request
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
comment|// store handler as attachment
name|ctx
operator|.
name|setAttachment
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|handler
operator|.
name|messageReceived
argument_list|(
name|ctx
argument_list|,
name|messageEvent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// this resource is not found, so send empty response back
name|HttpResponse
name|response
init|=
operator|new
name|DefaultHttpResponse
argument_list|(
name|HTTP_1_1
argument_list|,
name|NOT_FOUND
argument_list|)
decl_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|ChannelBuffers
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
block|}
annotation|@
name|Override
DECL|method|exceptionCaught (ChannelHandlerContext ctx, ExceptionEvent e)
specifier|public
name|void
name|exceptionCaught
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ExceptionEvent
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpServerChannelHandler
name|handler
init|=
operator|(
name|HttpServerChannelHandler
operator|)
name|ctx
operator|.
name|getAttachment
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|exceptionCaught
argument_list|(
name|ctx
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"HttpServerChannelHandler not found as attachment. Cannot handle caught exception."
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|getHandler (HttpRequest request)
specifier|private
name|HttpServerChannelHandler
name|getHandler
parameter_list|(
name|HttpRequest
name|request
parameter_list|)
block|{
comment|// need to strip out host and port etc, as we only need the context-path for matching
name|String
name|path
init|=
name|request
operator|.
name|getUri
argument_list|()
decl_stmt|;
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
operator|-
literal|1
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
name|idx
operator|+
name|len
argument_list|)
expr_stmt|;
block|}
comment|// use the path as key to find the consumer handler to use
name|path
operator|=
name|pathAsKey
argument_list|(
name|path
argument_list|)
expr_stmt|;
comment|// find the one that matches
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
name|entry
range|:
name|consumers
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|matches
argument_list|(
name|path
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|pathAsKey (String path)
specifier|private
specifier|static
name|String
name|pathAsKey
parameter_list|(
name|String
name|path
parameter_list|)
block|{
comment|// cater for default path
if|if
condition|(
name|path
operator|==
literal|null
operator|||
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
literal|""
expr_stmt|;
block|}
comment|// strip out query parameters
name|int
name|idx
init|=
name|path
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
operator|-
literal|1
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
expr_stmt|;
block|}
comment|// strip of ending /
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|path
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
return|return
name|UnsafeUriCharactersEncoder
operator|.
name|encodeHttpURI
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

