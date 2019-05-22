begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|InputStream
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|ContextTestSupport
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|DefaultExchange
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
name|xml
operator|.
name|StreamSourceConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * Test cases for {@link StreamCacheConverter}  */
end_comment

begin_class
DECL|class|StreamCacheConverterTest
specifier|public
class|class
name|StreamCacheConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TEST_FILE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FILE
init|=
literal|"org/apache/camel/converter/stream/test.xml"
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"<test>This is a test</test>"
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStreamCache ()
specifier|public
name|void
name|testConvertToStreamCache
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|ByteArrayInputStream
name|inputStream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|MESSAGE
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|StreamCache
name|streamCache
init|=
name|StreamSourceConverter
operator|.
name|convertToStreamCache
argument_list|(
operator|new
name|SAXSource
argument_list|(
operator|new
name|InputSource
argument_list|(
name|inputStream
argument_list|)
argument_list|)
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|String
name|message
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
name|streamCache
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The converted message is wrong"
argument_list|,
name|MESSAGE
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStreamCacheStreamSource ()
specifier|public
name|void
name|testConvertToStreamCacheStreamSource
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|StreamSource
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|getTestFileStream
argument_list|()
argument_list|)
decl_stmt|;
name|StreamCache
name|cache
init|=
name|StreamSourceConverter
operator|.
name|convertToStreamCache
argument_list|(
name|source
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|//assert re-readability of the cached StreamSource
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|converter
operator|.
name|toString
argument_list|(
operator|(
name|Source
operator|)
name|cache
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|converter
operator|.
name|toString
argument_list|(
operator|(
name|Source
operator|)
name|cache
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStreamCacheInputStream ()
specifier|public
name|void
name|testConvertToStreamCacheInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getTestFileStream
argument_list|()
decl_stmt|;
name|InputStream
name|cache
init|=
operator|(
name|InputStream
operator|)
name|StreamCacheConverter
operator|.
name|convertToStreamCache
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
comment|//assert re-readability of the cached InputStream
name|String
name|data
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|cache
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
name|String
name|data2
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|cache
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|data
argument_list|,
name|data2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToStreamCacheInputStreamWithFileCache ()
specifier|public
name|void
name|testConvertToStreamCacheInputStreamWithFileCache
parameter_list|()
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getStreamCachingStrategy
argument_list|()
operator|.
name|setSpoolThreshold
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getTestFileStream
argument_list|()
decl_stmt|;
name|InputStream
name|cache
init|=
operator|(
name|InputStream
operator|)
name|StreamCacheConverter
operator|.
name|convertToStreamCache
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|cache
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
comment|// since the stream is closed you delete the temp file
comment|// reset will not work any more
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|done
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"except the exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
comment|// do nothing
block|}
block|}
annotation|@
name|Test
DECL|method|testConvertToSerializable ()
specifier|public
name|void
name|testConvertToSerializable
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getTestFileStream
argument_list|()
decl_stmt|;
name|StreamCache
name|cache
init|=
name|StreamCacheConverter
operator|.
name|convertToStreamCache
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Serializable
name|ser
init|=
name|StreamSourceConverter
operator|.
name|convertToSerializable
argument_list|(
name|cache
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ser
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConvertToByteArray ()
specifier|public
name|void
name|testConvertToByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|InputStream
name|is
init|=
name|getTestFileStream
argument_list|()
decl_stmt|;
name|StreamCache
name|cache
init|=
name|StreamCacheConverter
operator|.
name|convertToStreamCache
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|StreamCacheConverter
operator|.
name|convertToByteArray
argument_list|(
name|cache
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
DECL|method|getTestFileStream ()
specifier|protected
name|InputStream
name|getTestFileStream
parameter_list|()
block|{
name|InputStream
name|answer
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|TEST_FILE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have found the file: "
operator|+
name|TEST_FILE
operator|+
literal|" on the classpath"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

