begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ChannelPromise
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
name|DefaultChannelPromise
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|stream
operator|.
name|ChunkedWriteHandler
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
name|OutboundStreamHttpRequest
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
name|OutboundStreamHttpResponse
import|;
end_import

begin_class
DECL|class|HttpOutboundStreamHandler
specifier|public
class|class
name|HttpOutboundStreamHandler
extends|extends
name|ChunkedWriteHandler
block|{
annotation|@
name|Override
DECL|method|write (ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
specifier|public
name|void
name|write
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Object
name|msg
parameter_list|,
name|ChannelPromise
name|promise
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|needNewPromise
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|msg
operator|instanceof
name|OutboundStreamHttpRequest
condition|)
block|{
name|super
operator|.
name|write
argument_list|(
name|ctx
argument_list|,
operator|(
operator|(
name|OutboundStreamHttpRequest
operator|)
name|msg
operator|)
operator|.
name|getRequest
argument_list|()
argument_list|,
name|promise
argument_list|)
expr_stmt|;
name|needNewPromise
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|msg
operator|instanceof
name|OutboundStreamHttpResponse
condition|)
block|{
name|super
operator|.
name|write
argument_list|(
name|ctx
argument_list|,
operator|(
operator|(
name|OutboundStreamHttpResponse
operator|)
name|msg
operator|)
operator|.
name|getResponse
argument_list|()
argument_list|,
name|promise
argument_list|)
expr_stmt|;
name|needNewPromise
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|needNewPromise
condition|)
block|{
name|promise
operator|=
operator|new
name|DefaultChannelPromise
argument_list|(
name|ctx
operator|.
name|channel
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|write
argument_list|(
name|ctx
argument_list|,
name|msg
argument_list|,
name|promise
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

