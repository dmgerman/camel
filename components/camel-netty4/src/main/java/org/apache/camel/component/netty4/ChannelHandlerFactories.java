begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|ChannelHandler
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
name|DelimiterBasedFrameDecoder
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
name|LengthFieldBasedFrameDecoder
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
name|serialization
operator|.
name|ClassResolvers
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
name|serialization
operator|.
name|ObjectDecoder
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
name|serialization
operator|.
name|ObjectEncoder
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
name|string
operator|.
name|StringDecoder
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
name|string
operator|.
name|StringEncoder
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
name|codec
operator|.
name|DatagramPacketDecoder
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
name|codec
operator|.
name|DatagramPacketDelimiterDecoder
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
name|codec
operator|.
name|DatagramPacketEncoder
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
name|codec
operator|.
name|DatagramPacketObjectDecoder
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
name|codec
operator|.
name|DatagramPacketObjectEncoder
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
name|codec
operator|.
name|DatagramPacketStringDecoder
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
name|codec
operator|.
name|DatagramPacketStringEncoder
import|;
end_import

begin_comment
comment|/**  * Helper to create commonly used {@link ChannelHandlerFactory} instances.  */
end_comment

begin_class
DECL|class|ChannelHandlerFactories
specifier|public
specifier|final
class|class
name|ChannelHandlerFactories
block|{
DECL|method|ChannelHandlerFactories ()
specifier|private
name|ChannelHandlerFactories
parameter_list|()
block|{     }
DECL|method|newStringEncoder (Charset charset, String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newStringEncoder
parameter_list|(
name|Charset
name|charset
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
literal|"udp"
operator|.
name|equalsIgnoreCase
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|DatagramPacketStringEncoder
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|StringEncoder
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|newStringDecoder (Charset charset, String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newStringDecoder
parameter_list|(
name|Charset
name|charset
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
literal|"udp"
operator|.
name|equalsIgnoreCase
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|DatagramPacketStringDecoder
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|StringDecoder
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|newObjectDecoder (String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newObjectDecoder
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
literal|"udp"
operator|.
name|equalsIgnoreCase
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
operator|new
name|DefaultChannelHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|DatagramPacketObjectDecoder
argument_list|(
name|ClassResolvers
operator|.
name|weakCachingResolver
argument_list|(
literal|null
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultChannelHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|ObjectDecoder
argument_list|(
name|ClassResolvers
operator|.
name|weakCachingResolver
argument_list|(
literal|null
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
DECL|method|newObjectEncoder (String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newObjectEncoder
parameter_list|(
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
literal|"udp"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|DatagramPacketObjectEncoder
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|ObjectEncoder
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|newDelimiterBasedFrameDecoder (final int maxFrameLength, final ByteBuf[] delimiters, String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newDelimiterBasedFrameDecoder
parameter_list|(
specifier|final
name|int
name|maxFrameLength
parameter_list|,
specifier|final
name|ByteBuf
index|[]
name|delimiters
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
return|return
name|newDelimiterBasedFrameDecoder
argument_list|(
name|maxFrameLength
argument_list|,
name|delimiters
argument_list|,
literal|true
argument_list|,
name|protocol
argument_list|)
return|;
block|}
DECL|method|newDelimiterBasedFrameDecoder (final int maxFrameLength, final ByteBuf[] delimiters, final boolean stripDelimiter, String protocol)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newDelimiterBasedFrameDecoder
parameter_list|(
specifier|final
name|int
name|maxFrameLength
parameter_list|,
specifier|final
name|ByteBuf
index|[]
name|delimiters
parameter_list|,
specifier|final
name|boolean
name|stripDelimiter
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
if|if
condition|(
literal|"udp"
operator|.
name|equals
argument_list|(
name|protocol
argument_list|)
condition|)
block|{
return|return
operator|new
name|DefaultChannelHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|DatagramPacketDelimiterDecoder
argument_list|(
name|maxFrameLength
argument_list|,
name|stripDelimiter
argument_list|,
name|delimiters
argument_list|)
return|;
block|}
block|}
return|;
block|}
else|else
block|{
return|return
operator|new
name|DefaultChannelHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|DelimiterBasedFrameDecoder
argument_list|(
name|maxFrameLength
argument_list|,
name|stripDelimiter
argument_list|,
name|delimiters
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
DECL|method|newDatagramPacketDecoder ()
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newDatagramPacketDecoder
parameter_list|()
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|DatagramPacketDecoder
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newDatagramPacketEncoder ()
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newDatagramPacketEncoder
parameter_list|()
block|{
return|return
operator|new
name|ShareableChannelHandlerFactory
argument_list|(
operator|new
name|DatagramPacketEncoder
argument_list|()
argument_list|)
return|;
block|}
DECL|method|newLengthFieldBasedFrameDecoder (final int maxFrameLength, final int lengthFieldOffset, final int lengthFieldLength, final int lengthAdjustment, final int initialBytesToStrip)
specifier|public
specifier|static
name|ChannelHandlerFactory
name|newLengthFieldBasedFrameDecoder
parameter_list|(
specifier|final
name|int
name|maxFrameLength
parameter_list|,
specifier|final
name|int
name|lengthFieldOffset
parameter_list|,
specifier|final
name|int
name|lengthFieldLength
parameter_list|,
specifier|final
name|int
name|lengthAdjustment
parameter_list|,
specifier|final
name|int
name|initialBytesToStrip
parameter_list|)
block|{
return|return
operator|new
name|DefaultChannelHandlerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ChannelHandler
name|newChannelHandler
parameter_list|()
block|{
return|return
operator|new
name|LengthFieldBasedFrameDecoder
argument_list|(
name|maxFrameLength
argument_list|,
name|lengthFieldOffset
argument_list|,
name|lengthFieldLength
argument_list|,
name|lengthAdjustment
argument_list|,
name|initialBytesToStrip
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

