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
name|InputStreamReader
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
name|StandardCharsets
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
name|Arrays
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
name|util
operator|.
name|IOHelper
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

begin_class
DECL|class|IOConverterCharsetTest
specifier|public
class|class
name|IOConverterCharsetTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|CONTENT
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT
init|=
literal|"G\u00f6tzend\u00e4mmerung,Joseph und seine Br\u00fcder"
decl_stmt|;
annotation|@
name|Test
DECL|method|testToInputStreamFileWithCharsetUTF8 ()
specifier|public
name|void
name|testToInputStreamFileWithCharsetUTF8
parameter_list|()
throws|throws
name|Exception
block|{
name|switchToDefaultCharset
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/german.utf-8.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|)
init|;
name|BufferedReader
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|;
name|BufferedReader
name|naiveReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
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
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|naiveLine
init|=
name|naiveReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|naiveLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToInputStreamFileWithCharsetUTF8withOtherDefaultEncoding ()
specifier|public
name|void
name|testToInputStreamFileWithCharsetUTF8withOtherDefaultEncoding
parameter_list|()
throws|throws
name|Exception
block|{
name|switchToDefaultCharset
argument_list|(
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/german.utf-8.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|)
init|;
name|BufferedReader
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|StandardCharsets
operator|.
name|ISO_8859_1
argument_list|)
argument_list|)
init|;
name|BufferedReader
name|naiveReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
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
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|naiveLine
init|=
name|naiveReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|naiveLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToInputStreamFileWithCharsetLatin1 ()
specifier|public
name|void
name|testToInputStreamFileWithCharsetLatin1
parameter_list|()
throws|throws
name|Exception
block|{
name|switchToDefaultCharset
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/german.iso-8859-1.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|,
literal|"ISO-8859-1"
argument_list|)
init|;
name|BufferedReader
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|;
name|BufferedReader
name|naiveReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
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
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|naiveLine
init|=
name|naiveReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|naiveLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToInputStreamFileDirectByteDumpWithCharsetLatin1 ()
specifier|public
name|void
name|testToInputStreamFileDirectByteDumpWithCharsetLatin1
parameter_list|()
throws|throws
name|Exception
block|{
name|switchToDefaultCharset
argument_list|(
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/converter/german.iso-8859-1.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|InputStream
name|in
init|=
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|,
literal|"ISO-8859-1"
argument_list|)
init|;
name|InputStream
name|naiveIn
operator|=
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
init|)
block|{
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|in
operator|.
name|read
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|byte
index|[]
name|naiveBytes
init|=
operator|new
name|byte
index|[
literal|8192
index|]
decl_stmt|;
name|naiveIn
operator|.
name|read
argument_list|(
name|naiveBytes
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"both input streams deliver the same byte sequence"
argument_list|,
name|Arrays
operator|.
name|equals
argument_list|(
name|naiveBytes
argument_list|,
name|bytes
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToReaderFileWithCharsetUTF8 ()
specifier|public
name|void
name|testToReaderFileWithCharsetUTF8
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
literal|"src/test/resources/org/apache/camel/converter/german.utf-8.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|toReader
argument_list|(
name|file
argument_list|,
literal|"UTF-8"
argument_list|)
init|;
name|BufferedReader
name|naiveReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
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
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|naiveLine
init|=
name|naiveReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|naiveLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testToReaderFileWithCharsetLatin1 ()
specifier|public
name|void
name|testToReaderFileWithCharsetLatin1
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
literal|"src/test/resources/org/apache/camel/converter/german.iso-8859-1.txt"
argument_list|)
decl_stmt|;
try|try
init|(
name|BufferedReader
name|reader
init|=
name|IOHelper
operator|.
name|toReader
argument_list|(
name|file
argument_list|,
literal|"ISO-8859-1"
argument_list|)
init|;
name|BufferedReader
name|naiveReader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
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
argument_list|,
literal|"ISO-8859-1"
argument_list|)
argument_list|)
init|)
block|{
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|String
name|naiveLine
init|=
name|naiveReader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|naiveLine
argument_list|,
name|line
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTENT
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|switchToDefaultCharset (final Charset charset)
specifier|private
specifier|static
name|void
name|switchToDefaultCharset
parameter_list|(
specifier|final
name|Charset
name|charset
parameter_list|)
block|{
name|IOHelper
operator|.
name|defaultCharset
operator|=
parameter_list|()
lambda|->
name|charset
expr_stmt|;
block|}
block|}
end_class

end_unit

