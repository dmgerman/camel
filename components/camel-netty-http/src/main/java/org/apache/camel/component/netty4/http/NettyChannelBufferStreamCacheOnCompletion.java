begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|support
operator|.
name|SynchronizationAdapter
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.Synchronization} to handle the lifecycle of the {@link NettyChannelBufferStreamCache}  * so the cache is released when the unit of work of the Exchange is done.  */
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
comment|// release the cache when we are done routing the Exchange
name|cache
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

