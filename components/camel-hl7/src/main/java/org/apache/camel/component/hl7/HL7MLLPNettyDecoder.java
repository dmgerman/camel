begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
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
name|buffer
operator|.
name|Unpooled
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
name|DecoderException
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
comment|/**  * HL7 MLLP Decoder for Netty  */
end_comment

begin_class
DECL|class|HL7MLLPNettyDecoder
class|class
name|HL7MLLPNettyDecoder
extends|extends
name|DelimiterBasedFrameDecoder
block|{
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
name|HL7MLLPNettyDecoder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|MAX_FRAME_LENGTH
specifier|private
specifier|static
specifier|final
name|int
name|MAX_FRAME_LENGTH
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|HL7MLLPConfig
name|config
decl_stmt|;
comment|/**      * Creates a decoder instance using a default HL7MLLPConfig      */
DECL|method|HL7MLLPNettyDecoder ()
name|HL7MLLPNettyDecoder
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|HL7MLLPConfig
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a decoder instance      *      * @param config HL7MLLPConfig to be used for decoding      * @throws java.lang.NullPointerException is config is null      */
DECL|method|HL7MLLPNettyDecoder (HL7MLLPConfig config)
name|HL7MLLPNettyDecoder
parameter_list|(
name|HL7MLLPConfig
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|MAX_FRAME_LENGTH
argument_list|,
literal|true
argument_list|,
name|Unpooled
operator|.
name|copiedBuffer
argument_list|(
operator|new
name|char
index|[]
block|{
name|config
operator|.
name|getEndByte1
argument_list|()
block|,
name|config
operator|.
name|getEndByte2
argument_list|()
block|}
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|decode (ChannelHandlerContext ctx, ByteBuf buffer)
specifier|protected
name|Object
name|decode
parameter_list|(
name|ChannelHandlerContext
name|ctx
parameter_list|,
name|ByteBuf
name|buffer
parameter_list|)
throws|throws
name|Exception
block|{
name|ByteBuf
name|buf
init|=
operator|(
name|ByteBuf
operator|)
name|super
operator|.
name|decode
argument_list|(
name|ctx
argument_list|,
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|buf
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|int
name|pos
init|=
name|buf
operator|.
name|bytesBefore
argument_list|(
operator|(
name|byte
operator|)
name|config
operator|.
name|getStartByte
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|>=
literal|0
condition|)
block|{
name|ByteBuf
name|msg
init|=
name|buf
operator|.
name|readerIndex
argument_list|(
name|pos
operator|+
literal|1
argument_list|)
operator|.
name|slice
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message ends with length {}"
argument_list|,
name|msg
operator|.
name|readableBytes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|config
operator|.
name|isProduceString
argument_list|()
condition|?
name|asString
argument_list|(
name|msg
argument_list|)
else|:
name|asByteArray
argument_list|(
name|msg
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|DecoderException
argument_list|(
literal|"Did not find start byte "
operator|+
operator|(
name|int
operator|)
name|config
operator|.
name|getStartByte
argument_list|()
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
comment|// We need to release the buf here to avoid the memory leak
name|buf
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Message not complete yet - return null to be called again
name|LOG
operator|.
name|debug
argument_list|(
literal|"No complete messages yet at position {}"
argument_list|,
name|buffer
operator|.
name|readableBytes
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|asByteArray (ByteBuf msg)
specifier|private
name|byte
index|[]
name|asByteArray
parameter_list|(
name|ByteBuf
name|msg
parameter_list|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|msg
operator|.
name|readableBytes
argument_list|()
index|]
decl_stmt|;
name|msg
operator|.
name|getBytes
argument_list|(
literal|0
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|isConvertLFtoCR
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|bytes
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|bytes
index|[
name|i
index|]
operator|==
operator|(
name|byte
operator|)
literal|'\n'
condition|)
block|{
name|bytes
index|[
name|i
index|]
operator|=
operator|(
name|byte
operator|)
literal|'\r'
expr_stmt|;
block|}
block|}
block|}
return|return
name|bytes
return|;
block|}
DECL|method|asString (ByteBuf msg)
specifier|private
name|String
name|asString
parameter_list|(
name|ByteBuf
name|msg
parameter_list|)
block|{
name|String
name|s
init|=
name|msg
operator|.
name|toString
argument_list|(
name|config
operator|.
name|getCharset
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|config
operator|.
name|isConvertLFtoCR
argument_list|()
condition|)
block|{
return|return
name|s
operator|.
name|replace
argument_list|(
literal|'\n'
argument_list|,
literal|'\r'
argument_list|)
return|;
block|}
return|return
name|s
return|;
block|}
block|}
end_class

end_unit

