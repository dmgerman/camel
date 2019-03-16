begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|stream
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|Reader
import|;
end_import

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
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
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
name|TransformerException
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
name|stream
operator|.
name|StreamSource
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
name|BytesSource
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
name|StreamCache
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
name|StringSource
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * A set of {@link Converter} methods for wrapping stream-based messages in a {@link StreamCache}  * implementation to ensure message re-readability (eg multicasting, retrying)  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
DECL|class|StreamCacheConverter
specifier|public
specifier|final
class|class
name|StreamCacheConverter
block|{
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|StreamCacheConverter ()
specifier|private
name|StreamCacheConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|convertToStreamCache (StreamSource source, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|StreamSource
name|source
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|StreamSourceCache
argument_list|(
name|source
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (StringSource source)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|StringSource
name|source
parameter_list|)
block|{
comment|//no need to do stream caching for a StringSource
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (BytesSource source)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|BytesSource
name|source
parameter_list|)
block|{
comment|//no need to do stream caching for a BytesSource
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (SAXSource source, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|SAXSource
name|source
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|TransformerException
block|{
name|String
name|data
init|=
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
name|source
argument_list|)
decl_stmt|;
return|return
operator|new
name|SourceCache
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (ByteArrayInputStream stream, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|ByteArrayInputStream
name|stream
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|ByteArrayInputStreamCache
argument_list|(
name|stream
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (InputStream stream, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|InputStream
name|stream
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
comment|// transfer the input stream to a cached output stream, and then creates a new stream cache view
comment|// of the data, which ensures the input stream is cached and re-readable.
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|stream
argument_list|,
name|cos
argument_list|)
expr_stmt|;
return|return
name|cos
operator|.
name|newStreamCache
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (CachedOutputStream cos, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|CachedOutputStream
name|cos
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|cos
operator|.
name|newStreamCache
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToStreamCache (Reader reader, Exchange exchange)
specifier|public
specifier|static
name|StreamCache
name|convertToStreamCache
parameter_list|(
name|Reader
name|reader
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|data
init|=
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
name|reader
argument_list|)
decl_stmt|;
return|return
operator|new
name|ReaderCache
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToSerializable (StreamCache cache, Exchange exchange)
specifier|public
specifier|static
name|Serializable
name|convertToSerializable
parameter_list|(
name|StreamCache
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|data
init|=
name|convertToByteArray
argument_list|(
name|cache
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|BytesSource
argument_list|(
name|data
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToByteArray (StreamCache cache, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|convertToByteArray
parameter_list|(
name|StreamCache
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
comment|// lets serialize it as a byte array
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|cache
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
return|return
name|os
operator|.
name|toByteArray
argument_list|()
return|;
block|}
annotation|@
name|Converter
DECL|method|convertToByteBuffer (StreamCache cache, Exchange exchange)
specifier|public
specifier|static
name|ByteBuffer
name|convertToByteBuffer
parameter_list|(
name|StreamCache
name|cache
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|array
init|=
name|convertToByteArray
argument_list|(
name|cache
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
return|return
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
end_class

end_unit

