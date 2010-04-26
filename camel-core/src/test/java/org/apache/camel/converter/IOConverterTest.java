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
name|BufferedReader
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
name|FileInputStream
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
name|impl
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
operator|new
name|FileInputStream
argument_list|(
name|file
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
name|char
name|testChar
init|=
operator|(
name|char
operator|)
name|data
index|[
name|data
operator|.
name|length
operator|-
literal|2
index|]
decl_stmt|;
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"Windows"
argument_list|)
condition|)
block|{
comment|// to skip the "0D" character
name|testChar
operator|=
operator|(
name|char
operator|)
name|data
index|[
name|data
operator|.
name|length
operator|-
literal|3
index|]
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|'!'
argument_list|,
name|testChar
argument_list|)
expr_stmt|;
comment|// should end with a new line
name|assertEquals
argument_list|(
literal|'\n'
argument_list|,
operator|(
name|char
operator|)
name|data
index|[
name|data
operator|.
name|length
operator|-
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteArray ()
specifier|public
name|void
name|testToByteArray
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|val
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|IOConverter
operator|.
name|toByteArray
argument_list|(
name|val
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|assertNotNull
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
name|assertNotNull
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|testToReader ()
specifier|public
name|void
name|testToReader
parameter_list|()
throws|throws
name|Exception
block|{
name|Reader
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
block|}
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
operator|new
name|BufferedReader
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
operator|new
name|BufferedReader
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
operator|new
name|BufferedReader
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
operator|new
name|BufferedReader
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
literal|"log4j.properties"
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
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
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
literal|"log4j.properties"
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
DECL|method|testStringByBufferedReader ()
specifier|public
name|void
name|testStringByBufferedReader
parameter_list|()
throws|throws
name|Exception
block|{
name|BufferedReader
name|reader
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|IOConverter
operator|.
name|toString
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|BufferedReader
name|br
init|=
operator|new
name|BufferedReader
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
block|}
end_class

end_unit

