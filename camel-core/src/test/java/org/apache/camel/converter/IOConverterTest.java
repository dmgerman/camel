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
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
import|;
end_import

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
name|File
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
name|OutputStream
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
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|IOHelper
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
name|ObjectHelper
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
comment|/**  * Test case for {@link IOConverter}  */
end_comment

begin_class
DECL|class|IOConverterTest
specifier|public
class|class
name|IOConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|TESTDATA
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|TESTDATA
init|=
literal|"My test data"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testToBytes ()
specifier|public
name|void
name|testToBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/dummy.txt"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|IOConverter
operator|.
name|toBytes
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"get the wrong byte size"
argument_list|,
name|file
operator|.
name|length
argument_list|()
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'#'
argument_list|,
operator|(
name|char
operator|)
name|data
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
comment|// should contain Hello World!
name|String
name|s
init|=
operator|new
name|String
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should contain Hello World!"
argument_list|,
name|s
operator|.
name|contains
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCopy ()
specifier|public
name|void
name|testCopy
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TESTDATA
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|bis
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TESTDATA
argument_list|,
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEquals (byte[] data1, byte[] data2)
specifier|private
name|void
name|assertEquals
parameter_list|(
name|byte
index|[]
name|data1
parameter_list|,
name|byte
index|[]
name|data2
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|data1
operator|.
name|length
argument_list|,
name|data2
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|data1
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
name|data1
index|[
name|i
index|]
argument_list|,
name|data2
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToOutputStreamFile ()
specifier|public
name|void
name|testToOutputStreamFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/test/hello.txt"
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
name|IOConverter
operator|.
name|toOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|BufferedOutputStream
operator|.
name|class
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToWriterFile ()
specifier|public
name|void
name|testToWriterFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/test/hello.txt"
argument_list|)
decl_stmt|;
name|Writer
name|writer
init|=
name|IOConverter
operator|.
name|toWriter
argument_list|(
name|file
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|BufferedWriter
operator|.
name|class
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToReader ()
specifier|public
name|void
name|testToReader
parameter_list|()
throws|throws
name|Exception
block|{
name|StringReader
name|reader
init|=
name|IOConverter
operator|.
name|toReader
argument_list|(
literal|"Hello"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBytesToReader ()
specifier|public
name|void
name|testBytesToReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|defaultCharacterSet
init|=
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|defaultCharacterSet
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|"Hello World"
operator|.
name|getBytes
argument_list|(
name|defaultCharacterSet
argument_list|)
decl_stmt|;
name|Reader
name|reader
init|=
name|IOConverter
operator|.
name|toReader
argument_list|(
name|bytes
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStreamExchange ()
specifier|public
name|void
name|testToInputStreamExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
literal|"Hello World"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStreamStringBufferAndBuilderExchange ()
specifier|public
name|void
name|testToInputStreamStringBufferAndBuilderExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
name|StringBuffer
name|buffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|buffer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|append
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|is
operator|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|builder
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStreamBufferReader ()
specifier|public
name|void
name|testToInputStreamBufferReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
expr_stmt|;
name|BufferedReader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|br
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToByteArrayFile ()
specifier|public
name|void
name|testToByteArrayFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://target/test"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/test/hello.txt"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|data
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToStringBufferReader ()
specifier|public
name|void
name|testToStringBufferReader
parameter_list|()
throws|throws
name|Exception
block|{
name|BufferedReader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|br
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToByteArrayBufferReader ()
specifier|public
name|void
name|testToByteArrayBufferReader
parameter_list|()
throws|throws
name|Exception
block|{
name|BufferedReader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|br
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToByteArrayReader ()
specifier|public
name|void
name|testToByteArrayReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|br
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToByteArrayOutputStream ()
specifier|public
name|void
name|testToByteArrayOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|11
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToStringOutputStream ()
specifier|public
name|void
name|testToStringOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|os
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStreamOutputStream ()
specifier|public
name|void
name|testToInputStreamOutputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
literal|"Hello World"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToInputStreamUrl ()
specifier|public
name|void
name|testToInputStreamUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"log4j2.properties"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|BufferedInputStream
operator|.
name|class
argument_list|,
name|is
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringUrl ()
specifier|public
name|void
name|testStringUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|ObjectHelper
operator|.
name|loadResourceAsURL
argument_list|(
literal|"log4j2.properties"
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|url
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testStringByBufferedReader ()
specifier|public
name|void
name|testStringByBufferedReader
parameter_list|()
throws|throws
name|Exception
block|{
name|BufferedReader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|IOConverter
operator|.
name|toString
argument_list|(
name|br
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testByteArrayByBufferedReader ()
specifier|public
name|void
name|testByteArrayByBufferedReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|data
init|=
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|reader
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
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
name|data
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInputStreamToString ()
specifier|public
name|void
name|testInputStreamToString
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|data
init|=
literal|"46\u00B037'00\"N\""
decl_stmt|;
name|ByteArrayInputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
name|is
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong result"
argument_list|,
name|data
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToPropertiesFromReader ()
specifier|public
name|void
name|testToPropertiesFromReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
name|br
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"foo=123\nbar=456"
argument_list|)
argument_list|)
decl_stmt|;
name|Properties
name|p
init|=
name|IOConverter
operator|.
name|toProperties
argument_list|(
name|br
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|p
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123"
argument_list|,
name|p
operator|.
name|get
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"456"
argument_list|,
name|p
operator|.
name|get
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToPropertiesFromFile ()
specifier|public
name|void
name|testToPropertiesFromFile
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|p
init|=
name|IOConverter
operator|.
name|toProperties
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/log4j2.properties"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be 8 or more properties, was "
operator|+
name|p
operator|.
name|size
argument_list|()
argument_list|,
name|p
operator|.
name|size
argument_list|()
operator|>=
literal|8
argument_list|)
expr_stmt|;
name|String
name|root
init|=
operator|(
name|String
operator|)
name|p
operator|.
name|get
argument_list|(
literal|"rootLogger.level"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|root
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|root
operator|.
name|contains
argument_list|(
literal|"INFO"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

