begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.codec
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
name|codec
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|buffer
operator|.
name|ByteBuf
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

begin_comment
comment|/**  * Just expose the encode method for DatagramPacketObjectEncoder to use  */
end_comment

begin_class
DECL|class|ObjectEncoder
specifier|public
class|class
name|ObjectEncoder
extends|extends
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|serialization
operator|.
name|ObjectEncoder
block|{
annotation|@
name|Override
DECL|method|encode (ChannelHandlerContext ctx, Serializable msg, ByteBuf out)
specifier|public
name|void
name|encode
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|Serializable
name|msg
parameter_list|,
name|ByteBuf
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|super
operator|.
name|encode
argument_list|(
name|ctx
argument_list|,
name|msg
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

