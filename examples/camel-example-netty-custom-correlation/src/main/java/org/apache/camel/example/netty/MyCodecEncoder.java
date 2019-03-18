begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|netty
package|;
end_package

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

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|MessageToByteEncoder
import|;
end_import

begin_comment
comment|/**  * Netty encoder that writes the Camel message to bytes with start and end byte markers.  */
end_comment

begin_class
DECL|class|MyCodecEncoder
specifier|public
class|class
name|MyCodecEncoder
extends|extends
name|MessageToByteEncoder
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|startByte
specifier|private
specifier|static
name|char
name|startByte
init|=
literal|0x0b
decl_stmt|;
comment|// 11 decimal
DECL|field|endByte1
specifier|private
specifier|static
name|char
name|endByte1
init|=
literal|0x1c
decl_stmt|;
comment|// 28 decimal
DECL|field|endByte2
specifier|private
specifier|static
name|char
name|endByte2
init|=
literal|0x0d
decl_stmt|;
comment|// 13 decimal
annotation|@
name|Override
DECL|method|encode (ChannelHandlerContext channelHandlerContext, Object message, ByteBuf byteBuf)
specifier|protected
name|void
name|encode
parameter_list|(
name|ChannelHandlerContext
name|channelHandlerContext
parameter_list|,
name|Object
name|message
parameter_list|,
name|ByteBuf
name|byteBuf
parameter_list|)
throws|throws
name|Exception
block|{
name|byte
index|[]
name|body
decl_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|String
condition|)
block|{
name|body
operator|=
operator|(
operator|(
name|String
operator|)
name|message
operator|)
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|byte
index|[]
condition|)
block|{
name|body
operator|=
operator|(
name|byte
index|[]
operator|)
name|message
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The message to encode is not a supported type: "
operator|+
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
throw|;
block|}
name|byteBuf
operator|.
name|writeByte
argument_list|(
name|startByte
argument_list|)
expr_stmt|;
name|byteBuf
operator|.
name|writeBytes
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|byteBuf
operator|.
name|writeByte
argument_list|(
name|endByte1
argument_list|)
expr_stmt|;
name|byteBuf
operator|.
name|writeByte
argument_list|(
name|endByte2
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

