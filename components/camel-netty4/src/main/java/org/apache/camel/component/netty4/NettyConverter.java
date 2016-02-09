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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInput
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stax
operator|.
name|StAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|ByteBufAllocator
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
name|ByteBufInputStream
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
name|Converter
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

begin_comment
comment|/**  * A set of converter methods for working with Netty types  *  * @version   */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|NettyConverter
specifier|public
specifier|final
class|class
name|NettyConverter
block|{
DECL|method|NettyConverter ()
specifier|private
name|NettyConverter
parameter_list|()
block|{
comment|//Utility Class
block|}
annotation|@
name|Converter
DECL|method|toByteArray (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|buffer
operator|.
name|hasArray
argument_list|()
condition|)
block|{
return|return
name|buffer
operator|.
name|array
argument_list|()
return|;
block|}
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
name|buffer
operator|.
name|readableBytes
argument_list|()
index|]
decl_stmt|;
name|int
name|readerIndex
init|=
name|buffer
operator|.
name|readerIndex
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|getBytes
argument_list|(
name|readerIndex
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
return|return
name|bytes
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
name|byte
index|[]
name|bytes
init|=
name|toByteArray
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|// use type converter as it can handle encoding set on the Exchange
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
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
name|exchange
argument_list|,
name|bytes
argument_list|)
return|;
block|}
return|return
operator|new
name|String
argument_list|(
name|bytes
argument_list|,
literal|"UTF-8"
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|ByteBufInputStream
argument_list|(
name|buffer
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toObjectInput (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|ObjectInput
name|toObjectInput
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|ObjectInputStream
argument_list|(
name|is
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (byte[] bytes)
specifier|public
specifier|static
name|ByteBuf
name|toByteBuffer
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|ByteBuf
name|buf
init|=
name|ByteBufAllocator
operator|.
name|DEFAULT
operator|.
name|buffer
argument_list|(
name|bytes
operator|.
name|length
argument_list|)
decl_stmt|;
name|buf
operator|.
name|writeBytes
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (String s, Exchange exchange)
specifier|public
specifier|static
name|ByteBuf
name|toByteBuffer
parameter_list|(
name|String
name|s
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|byte
index|[]
name|bytes
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
comment|// use type converter as it can handle encoding set on the Exchange
name|bytes
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
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
name|exchange
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|bytes
operator|=
name|s
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
return|return
name|toByteBuffer
argument_list|(
name|bytes
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toDocument (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|Document
name|toDocument
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toDOMSource (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|DOMSource
name|toDOMSource
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|DOMSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toSAXSource (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|SAXSource
name|toSAXSource
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|SAXSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toStreamSource (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|StreamSource
name|toStreamSource
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|StreamSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toStAXSource (ByteBuf buffer, Exchange exchange)
specifier|public
specifier|static
name|StAXSource
name|toStAXSource
parameter_list|(
name|ByteBuf
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|StAXSource
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
return|;
block|}
block|}
end_class

end_unit

