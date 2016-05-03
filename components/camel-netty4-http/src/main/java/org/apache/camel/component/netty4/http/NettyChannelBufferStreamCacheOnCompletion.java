begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
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
name|netty4
operator|.
name|http
operator|.
name|handlers
operator|.
name|HttpServerChannelHandler
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.Synchronization} to keep track of the unit of work on the current {@link Exchange}  * that has the {@link NettyChannelBufferStreamCache} as message body. This cache is wrapping the raw original  * Netty {@link io.netty.buffer.ByteBuf}. Because the Netty HTTP server ({@link HttpServerChannelHandler}) will  * close the {@link io.netty.buffer.ByteBuf} when Netty is complete processing the HttpMessage, then any further  * access to the cache will cause in a buffer unreadable. In the case of Camel async routing engine will  * handover the processing of the {@link Exchange} to another thread, then we need to keep track of this event  * so we can do a defensive copy of the netty {@link io.netty.buffer.ByteBuf} so Camel is able to read  * the content from other threads, while Netty has closed the original {@link io.netty.buffer.ByteBuf}.  */
end_comment

begin_class
DECL|class|NettyChannelBufferStreamCacheOnCompletion
specifier|public
class|class
name|NettyChannelBufferStreamCacheOnCompletion
extends|extends
name|SynchronizationAdapter
block|{
DECL|field|cache
specifier|private
specifier|final
name|NettyChannelBufferStreamCache
name|cache
decl_stmt|;
DECL|method|NettyChannelBufferStreamCacheOnCompletion (NettyChannelBufferStreamCache cache)
specifier|public
name|NettyChannelBufferStreamCacheOnCompletion
parameter_list|(
name|NettyChannelBufferStreamCache
name|cache
parameter_list|)
block|{
name|this
operator|.
name|cache
operator|=
name|cache
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onDone (Exchange exchange)
specifier|public
name|void
name|onDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// okay netty is no longer being active, so we need to signal to the cache that its to preserve the buffer if still in need.
name|cache
operator|.
name|defensiveCopyBuffer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|allowHandover ()
specifier|public
name|boolean
name|allowHandover
parameter_list|()
block|{
comment|// do not allow handover, so we can do the defensive copy in the onDone method
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

