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
name|netty
operator|.
name|handlers
operator|.
name|ClientChannelHandler
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
name|NettyHttpProducer
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
name|ChannelBuffer
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
name|HttpChunk
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
name|HttpChunkTrailer
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

begin_comment
comment|/**  * Netty HTTP {@link org.apache.camel.component.netty.handlers.ClientChannelHandler} that handles the response combing  * back from thhe HTTP server, called by this client.  *  */
end_comment

begin_class
DECL|class|HttpClientChannelHandler
specifier|public
class|class
name|HttpClientChannelHandler
extends|extends
name|ClientChannelHandler
block|{
comment|// use NettyHttpProducer as logger to make it easier to read the logs as this is part of the producer
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
name|NettyHttpProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|NettyHttpProducer
name|producer
decl_stmt|;
DECL|field|response
specifier|private
name|HttpResponse
name|response
decl_stmt|;
DECL|field|buffer
specifier|private
name|ChannelBuffer
name|buffer
decl_stmt|;
DECL|method|HttpClientChannelHandler (NettyHttpProducer producer)
specifier|public
name|HttpClientChannelHandler
parameter_list|(
name|NettyHttpProducer
name|producer
parameter_list|)
block|{
name|super
argument_list|(
name|producer
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
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
comment|// store response, as this channel handler is created per pipeline
name|Object
name|msg
init|=
name|messageEvent
operator|.
name|getMessage
argument_list|()
decl_stmt|;
comment|// it may be a chunked message
if|if
condition|(
name|msg
operator|instanceof
name|HttpChunk
condition|)
block|{
name|HttpChunk
name|chunk
init|=
operator|(
name|HttpChunk
operator|)
name|msg
decl_stmt|;
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
literal|"HttpChunk received: {} isLast: {}"
argument_list|,
name|chunk
argument_list|,
name|chunk
operator|.
name|isLast
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|msg
operator|instanceof
name|HttpChunkTrailer
condition|)
block|{
comment|// chunk trailer only has headers
name|HttpChunkTrailer
name|trailer
init|=
operator|(
name|HttpChunkTrailer
operator|)
name|msg
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|trailer
operator|.
name|getHeaders
argument_list|()
control|)
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
literal|"Adding trailing header {}={}"
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|response
operator|.
name|addHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// append chunked content
name|buffer
operator|.
name|writeBytes
argument_list|(
name|chunk
operator|.
name|getContent
argument_list|()
argument_list|)
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
literal|"Wrote {} bytes to chunk buffer"
argument_list|,
name|buffer
operator|.
name|writerIndex
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|chunk
operator|.
name|isLast
argument_list|()
condition|)
block|{
comment|// the content is a copy of the buffer with the actual data we wrote to it
name|int
name|end
init|=
name|buffer
operator|.
name|writerIndex
argument_list|()
decl_stmt|;
name|ChannelBuffer
name|copy
init|=
name|buffer
operator|.
name|copy
argument_list|(
literal|0
argument_list|,
name|end
argument_list|)
decl_stmt|;
comment|// the copy must not be readable when the content was chunked, so set the index to the end
name|copy
operator|.
name|setIndex
argument_list|(
name|end
argument_list|,
name|end
argument_list|)
expr_stmt|;
name|response
operator|.
name|setContent
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// we the all the content now, so call super to process the received message
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
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|HttpResponse
condition|)
block|{
name|response
operator|=
operator|(
name|HttpResponse
operator|)
name|msg
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
literal|"HttpResponse received: {} chunked:"
argument_list|,
name|response
argument_list|,
name|response
operator|.
name|isChunked
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|response
operator|.
name|isChunked
argument_list|()
condition|)
block|{
comment|// the response is not chunked so we have all the content
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
else|else
block|{
comment|// the response is chunkced so use a dynamic buffer to receive the content in chunks
name|buffer
operator|=
name|ChannelBuffers
operator|.
name|dynamicBuffer
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// ignore not supported message
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
operator|&&
name|msg
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Ignoring non supported response message of type {} -> {}"
argument_list|,
name|msg
operator|.
name|getClass
argument_list|()
argument_list|,
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
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
comment|// use the binding
return|return
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toCamelMessage
argument_list|(
name|response
argument_list|,
name|exchange
argument_list|,
name|producer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

