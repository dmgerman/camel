begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
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
name|converter
operator|.
name|IOConverter
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
name|impl
operator|.
name|DefaultCamelContext
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
name|DefaultMessage
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
name|GZIPHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|GZIPHelperTest
specifier|public
class|class
name|GZIPHelperTest
block|{
DECL|field|sampleBytes
specifier|private
specifier|static
name|byte
index|[]
name|sampleBytes
init|=
operator|new
name|byte
index|[]
block|{
literal|1
block|,
literal|2
block|,
literal|3
block|,
literal|1
block|,
literal|2
block|,
literal|3
block|}
decl_stmt|;
DECL|field|sampleString
specifier|private
specifier|static
name|String
name|sampleString
init|=
literal|"<Hello>World</Hello>"
decl_stmt|;
annotation|@
name|Test
DECL|method|toGZIPInputStreamShouldReturnTheSameInputStream ()
specifier|public
name|void
name|toGZIPInputStreamShouldReturnTheSameInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|inputStream
init|=
name|GZIPHelper
operator|.
name|uncompressGzip
argument_list|(
literal|"text"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|sampleBytes
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|6
index|]
decl_stmt|;
name|inputStream
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|-
literal|1
argument_list|,
name|inputStream
operator|.
name|read
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|sampleBytes
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toGZIPInputStreamShouldReturnAByteArrayInputStream ()
specifier|public
name|void
name|toGZIPInputStreamShouldReturnAByteArrayInputStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|inputStream
init|=
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
literal|"text"
argument_list|,
name|sampleBytes
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|sampleBytes
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCompressAndUnCompressData ()
specifier|public
name|void
name|testCompressAndUnCompressData
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|inputStream
init|=
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
literal|"gzip"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|sampleString
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The inputStream should not be null."
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|inputStream
operator|=
name|GZIPHelper
operator|.
name|uncompressGzip
argument_list|(
literal|"gzip"
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|inputStream
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The result is wrong."
argument_list|,
name|sampleString
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIsGzipMessage ()
specifier|public
name|void
name|testIsGzipMessage
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|createMessageWithContentEncodingHeader
argument_list|(
literal|"gzip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|createMessageWithContentEncodingHeader
argument_list|(
literal|"GZip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|createMessageWithContentEncodingHeader
argument_list|(
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|createMessageWithContentEncodingHeader
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|isGzipString ()
specifier|public
name|void
name|isGzipString
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
literal|"gzip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
literal|"GZip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
operator|(
name|String
operator|)
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
literal|"zip"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createMessageWithContentEncodingHeader (String contentEncoding)
specifier|private
name|Message
name|createMessageWithContentEncodingHeader
parameter_list|(
name|String
name|contentEncoding
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|DefaultMessage
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
literal|"Content-Encoding"
argument_list|,
name|contentEncoding
argument_list|)
expr_stmt|;
return|return
name|msg
return|;
block|}
block|}
end_class

end_unit

