begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mina
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mina
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
name|CharacterCodingException
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|CharsetEncoder
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
name|CamelContext
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
name|NoTypeConversionAvailableException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|common
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolCodecFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoderOutput
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolEncoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolEncoderOutput
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|MinaUdpProtocolCodecFactory
specifier|public
class|class
name|MinaUdpProtocolCodecFactory
implements|implements
name|ProtocolCodecFactory
block|{
DECL|field|charset
specifier|private
specifier|final
name|Charset
name|charset
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|MinaUdpProtocolCodecFactory (CamelContext context, Charset charset)
specifier|public
name|MinaUdpProtocolCodecFactory
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Charset
name|charset
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|charset
operator|=
name|charset
expr_stmt|;
block|}
DECL|method|getEncoder ()
specifier|public
name|ProtocolEncoder
name|getEncoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ProtocolEncoder
argument_list|()
block|{
specifier|private
name|CharsetEncoder
name|encoder
decl_stmt|;
specifier|public
name|void
name|encode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|Object
name|message
parameter_list|,
name|ProtocolEncoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|encoder
operator|==
literal|null
condition|)
block|{
name|encoder
operator|=
name|charset
operator|.
name|newEncoder
argument_list|()
expr_stmt|;
block|}
name|ByteBuffer
name|buf
init|=
name|toByteBuffer
argument_list|(
name|message
argument_list|,
name|encoder
argument_list|)
decl_stmt|;
name|buf
operator|.
name|flip
argument_list|()
expr_stmt|;
name|out
operator|.
name|write
argument_list|(
name|buf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
DECL|method|getDecoder ()
specifier|public
name|ProtocolDecoder
name|getDecoder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|ProtocolDecoder
argument_list|()
block|{
specifier|public
name|void
name|decode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|ByteBuffer
name|in
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
comment|// convert to bytes to write, we can not pass in the byte buffer as it could be sent to
comment|// multiple mina sessions so we must convert it to bytes
name|byte
index|[]
name|bytes
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|out
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|finishDecode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
return|;
block|}
DECL|method|toByteBuffer (Object message, CharsetEncoder encoder)
specifier|private
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Object
name|message
parameter_list|,
name|CharsetEncoder
name|encoder
parameter_list|)
throws|throws
name|CharacterCodingException
throws|,
name|NoTypeConversionAvailableException
block|{
name|String
name|value
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|ByteBuffer
name|answer
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
name|value
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|setAutoExpand
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|answer
operator|.
name|putString
argument_list|(
name|value
argument_list|,
name|encoder
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// failback to use a byte buffer converter
return|return
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
name|message
argument_list|)
return|;
block|}
block|}
end_class

end_unit

