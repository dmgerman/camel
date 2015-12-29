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
name|nio
operator|.
name|channels
operator|.
name|ClosedChannelException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

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
name|component
operator|.
name|netty
operator|.
name|http
operator|.
name|RestContextPathMatcher
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
name|rawPath
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
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
comment|// use rest path matcher in case Rest DSL is in use
name|ContextPathMatcher
name|matcher
init|=
operator|new
name|RestContextPathMatcher
argument_list|(
name|rawPath
argument_list|,
name|path
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
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
name|rawPath
init|=
name|consumer
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
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
comment|// use rest path matcher in case Rest DSL is in use
name|ContextPathMatcher
name|matcher
init|=
operator|new
name|RestContextPathMatcher
argument_list|(
name|rawPath
argument_list|,
name|path
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
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
name|headers
argument_list|()
operator|.
name|set
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
name|headers
argument_list|()
operator|.
name|set
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
operator|.
name|syncUninterruptibly
argument_list|()
expr_stmt|;
comment|// close the channel after send error message
name|messageEvent
operator|.
name|getChannel
argument_list|()
operator|.
name|close
argument_list|()
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
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ClosedChannelException
condition|)
block|{
comment|// The channel is closed so we do nothing here
name|LOG
operator|.
name|debug
argument_list|(
literal|"Channel already closed. Ignoring this exception."
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
if|if
condition|(
literal|"Broken pipe"
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
condition|)
block|{
comment|// Can't recover channel at this point. Only valid thing to do is close. A TCP RST is a possible cause for this.
comment|// Note that trying to write to channel in this state will cause infinite recursion in netty 3.x
name|LOG
operator|.
name|debug
argument_list|(
literal|"Channel pipe is broken. Closing channel now."
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|getChannel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// we cannot throw the exception here
name|LOG
operator|.
name|warn
argument_list|(
literal|"HttpServerChannelHandler is not found as attachment to handle exception, send 404 back to the client."
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now we just send 404 back to the client
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
name|headers
argument_list|()
operator|.
name|set
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
name|headers
argument_list|()
operator|.
name|set
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
literal|0
argument_list|)
expr_stmt|;
comment|// Here we don't want to expose the exception detail to the client
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
name|ctx
operator|.
name|getChannel
argument_list|()
operator|.
name|write
argument_list|(
name|response
argument_list|)
operator|.
name|syncUninterruptibly
argument_list|()
expr_stmt|;
comment|// close the channel after send error message
name|ctx
operator|.
name|getChannel
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
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
name|HttpServerChannelHandler
name|answer
init|=
literal|null
decl_stmt|;
comment|// need to strip out host and port etc, as we only need the context-path for matching
name|String
name|method
init|=
name|request
operator|.
name|getMethod
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|method
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
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
name|List
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
argument_list|>
name|candidates
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
comment|// first match by http method
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
name|NettyHttpConsumer
name|consumer
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
name|String
name|restrict
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|matchMethod
argument_list|(
name|method
argument_list|,
name|restrict
argument_list|)
condition|)
block|{
name|candidates
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
comment|// then see if we got a direct match
name|List
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|directMatches
init|=
operator|new
name|LinkedList
argument_list|<
name|HttpServerChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
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
name|candidates
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|matchesRest
argument_list|(
name|path
argument_list|,
literal|false
argument_list|)
condition|)
block|{
name|directMatches
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|directMatches
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
comment|// Single match found, just return it without any further analysis.
name|answer
operator|=
name|directMatches
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|directMatches
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
comment|// possible if the prefix match occurred
name|List
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|directMatchesWithOptions
init|=
name|handlersWithExplicitOptionsMethod
argument_list|(
name|directMatches
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|directMatchesWithOptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// prefer options matches
name|answer
operator|=
name|handlerWithTheLongestMatchingPrefix
argument_list|(
name|directMatchesWithOptions
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|handlerWithTheLongestMatchingPrefix
argument_list|(
name|directMatches
argument_list|)
expr_stmt|;
block|}
block|}
comment|// then match by wildcard path
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
argument_list|>
name|it
init|=
name|candidates
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// filter non matching paths
if|if
condition|(
operator|!
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|matchesRest
argument_list|(
name|path
argument_list|,
literal|true
argument_list|)
condition|)
block|{
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
comment|// if there is multiple candidates with wildcards then pick anyone with the least number of wildcards
name|int
name|bestWildcard
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
name|best
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|it
operator|=
name|candidates
operator|.
name|iterator
argument_list|()
expr_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Map
operator|.
name|Entry
argument_list|<
name|ContextPathMatcher
argument_list|,
name|HttpServerChannelHandler
argument_list|>
name|entry
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|consumerPath
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|getConsumer
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|int
name|wildcards
init|=
name|countWildcards
argument_list|(
name|consumerPath
argument_list|)
decl_stmt|;
if|if
condition|(
name|wildcards
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|best
operator|==
literal|null
operator|||
name|wildcards
operator|<
name|bestWildcard
condition|)
block|{
name|best
operator|=
name|entry
expr_stmt|;
name|bestWildcard
operator|=
name|wildcards
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|best
operator|!=
literal|null
condition|)
block|{
comment|// pick the best among the wildcards
name|answer
operator|=
name|best
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
comment|// if there is one left then its our answer
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|candidates
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|answer
operator|=
name|candidates
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
comment|// fallback to regular matching
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
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
name|answer
operator|=
name|entry
operator|.
name|getValue
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Counts the number of wildcards in the path      *      * @param consumerPath  the consumer path which may use { } tokens      * @return number of wildcards, or<tt>0</tt> if no wildcards      */
DECL|method|countWildcards (String consumerPath)
specifier|private
specifier|static
name|int
name|countWildcards
parameter_list|(
name|String
name|consumerPath
parameter_list|)
block|{
name|int
name|wildcards
init|=
literal|0
decl_stmt|;
comment|// remove starting/ending slashes
if|if
condition|(
name|consumerPath
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|consumerPath
operator|.
name|endsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
name|consumerPath
operator|=
name|consumerPath
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|consumerPath
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|consumerPaths
init|=
name|consumerPath
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|p2
range|:
name|consumerPaths
control|)
block|{
if|if
condition|(
name|p2
operator|.
name|startsWith
argument_list|(
literal|"{"
argument_list|)
operator|&&
name|p2
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
name|wildcards
operator|++
expr_stmt|;
block|}
block|}
return|return
name|wildcards
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
DECL|method|handlersWithExplicitOptionsMethod (Iterable<HttpServerChannelHandler> handlers)
specifier|private
specifier|static
name|List
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|handlersWithExplicitOptionsMethod
parameter_list|(
name|Iterable
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|List
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|handlersWithOptions
init|=
operator|new
name|LinkedList
argument_list|<
name|HttpServerChannelHandler
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|HttpServerChannelHandler
name|handler
range|:
name|handlers
control|)
block|{
name|String
name|consumerMethod
init|=
name|handler
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumerMethod
operator|!=
literal|null
operator|&&
name|consumerMethod
operator|.
name|contains
argument_list|(
literal|"OPTIONS"
argument_list|)
condition|)
block|{
name|handlersWithOptions
operator|.
name|add
argument_list|(
name|handler
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|handlersWithOptions
return|;
block|}
DECL|method|handlerWithTheLongestMatchingPrefix (Iterable<HttpServerChannelHandler> handlers)
specifier|private
specifier|static
name|HttpServerChannelHandler
name|handlerWithTheLongestMatchingPrefix
parameter_list|(
name|Iterable
argument_list|<
name|HttpServerChannelHandler
argument_list|>
name|handlers
parameter_list|)
block|{
name|HttpServerChannelHandler
name|handlerWithTheLongestPrefix
init|=
name|handlers
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|HttpServerChannelHandler
name|handler
range|:
name|handlers
control|)
block|{
name|String
name|consumerPath
init|=
name|handler
operator|.
name|getConsumer
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|String
name|longestPath
init|=
name|handlerWithTheLongestPrefix
operator|.
name|getConsumer
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|consumerPath
operator|.
name|length
argument_list|()
operator|>
name|longestPath
operator|.
name|length
argument_list|()
condition|)
block|{
name|handlerWithTheLongestPrefix
operator|=
name|handler
expr_stmt|;
block|}
block|}
return|return
name|handlerWithTheLongestPrefix
return|;
block|}
block|}
end_class

end_unit

