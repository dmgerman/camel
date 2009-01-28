begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

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
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
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
name|file
operator|.
name|GenericFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Some core java.nio based  *<a href="http://activemq.apache.org/camel/type-converter.html">Type Converters</a>  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|NIOConverter
specifier|public
specifier|final
class|class
name|NIOConverter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|NIOConverter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|NIOConverter ()
specifier|private
name|NIOConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toByteArray (ByteBuffer buffer)
specifier|public
specifier|static
name|byte
index|[]
name|toByteArray
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|)
block|{
return|return
name|buffer
operator|.
name|array
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (ByteBuffer buffer, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|ByteBuffer
name|buffer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|IOConverter
operator|.
name|toString
argument_list|(
name|buffer
operator|.
name|array
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (byte[] data)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (GenericFile<File> file)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|toByteBuffer
argument_list|(
name|file
operator|.
name|getFile
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (File file)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
operator|(
name|int
operator|)
name|file
operator|.
name|length
argument_list|()
index|]
decl_stmt|;
name|in
operator|=
operator|new
name|BufferedInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|sizeLeft
init|=
operator|(
name|int
operator|)
name|file
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|sizeLeft
operator|>
literal|0
condition|)
block|{
name|int
name|readSize
init|=
name|in
operator|.
name|read
argument_list|(
name|buf
argument_list|,
name|offset
argument_list|,
name|sizeLeft
argument_list|)
decl_stmt|;
name|sizeLeft
operator|-=
name|readSize
expr_stmt|;
name|offset
operator|+=
name|readSize
expr_stmt|;
block|}
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|buf
argument_list|)
return|;
block|}
finally|finally
block|{
try|try
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close file stream: "
operator|+
name|file
operator|.
name|getPath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (String value, Exchange exchange)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|String
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|ByteBuffer
name|buf
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
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|charsetName
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|charsetName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|bytes
operator|=
name|value
operator|.
name|getBytes
argument_list|(
name|charsetName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Can't convert the byte to String with the charset "
operator|+
name|charsetName
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|bytes
operator|==
literal|null
condition|)
block|{
name|bytes
operator|=
name|value
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
name|buf
operator|.
name|put
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
DECL|method|toByteBuffer (Short value)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Short
name|value
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putShort
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (Integer value)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Integer
name|value
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putInt
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (Long value)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putLong
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (Float value)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Float
name|value
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|4
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putFloat
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toByteBuffer (Double value)
specifier|public
specifier|static
name|ByteBuffer
name|toByteBuffer
parameter_list|(
name|Double
name|value
parameter_list|)
block|{
name|ByteBuffer
name|buf
init|=
name|ByteBuffer
operator|.
name|allocate
argument_list|(
literal|8
argument_list|)
decl_stmt|;
name|buf
operator|.
name|putDouble
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
name|buf
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (ByteBuffer bufferbuffer)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|ByteBuffer
name|bufferbuffer
parameter_list|)
block|{
return|return
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|toByteArray
argument_list|(
name|bufferbuffer
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

