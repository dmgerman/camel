begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.zipfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|zipfile
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
name|FileOutputStream
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
name|OutputStream
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipOutputStream
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
name|Processor
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
name|builder
operator|.
name|NotifyBuilder
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|stream
operator|.
name|InputStreamCache
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|FILE_NAME
import|;
end_import

begin_comment
comment|/**  * Unit tests for {@link ZipFileDataFormat}.  */
end_comment

begin_class
DECL|class|ZipFileDataFormatTest
specifier|public
class|class
name|ZipFileDataFormatTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEXT
specifier|private
specifier|static
specifier|final
name|String
name|TEXT
init|=
literal|"The Masque of Queen Bersabe (excerpt) \n"
operator|+
literal|"by: Algernon Charles Swinburne \n\n"
operator|+
literal|"My lips kissed dumb the word of Ah \n"
operator|+
literal|"Sighed on strange lips grown sick thereby. \n"
operator|+
literal|"God wrought to me my royal bed; \n"
operator|+
literal|"The inner work thereof was red, \n"
operator|+
literal|"The outer work was ivory. \n"
operator|+
literal|"My mouth's heat was the heat of flame \n"
operator|+
literal|"For lust towards the kings that came \n"
operator|+
literal|"With horsemen riding royally."
decl_stmt|;
DECL|field|TEST_DIR
specifier|private
specifier|static
specifier|final
name|File
name|TEST_DIR
init|=
operator|new
name|File
argument_list|(
literal|"target/zip"
argument_list|)
decl_stmt|;
DECL|field|zip
specifier|private
name|ZipFileDataFormat
name|zip
decl_stmt|;
annotation|@
name|Test
DECL|method|testZipAndStreamCaching ()
specifier|public
name|void
name|testZipAndStreamCaching
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:zipStreamCache"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:zipStreamCache"
argument_list|,
name|TEXT
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
operator|+
literal|".zip"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|InputStreamCache
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|getZippedText
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipWithoutFileName ()
specifier|public
name|void
name|testZipWithoutFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:zip"
argument_list|,
name|TEXT
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
operator|+
literal|".zip"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|getZippedText
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipWithFileName ()
specifier|public
name|void
name|testZipWithFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|getZippedText
argument_list|(
literal|"poem.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"poem.txt.zip"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:zip"
argument_list|,
name|TEXT
argument_list|,
name|FILE_NAME
argument_list|,
literal|"poem.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipWithPathElements ()
specifier|public
name|void
name|testZipWithPathElements
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|getZippedText
argument_list|(
literal|"poem.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"poem.txt.zip"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:zip"
argument_list|,
name|TEXT
argument_list|,
name|FILE_NAME
argument_list|,
literal|"poems/poem.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipWithPreservedPathElements ()
specifier|public
name|void
name|testZipWithPreservedPathElements
parameter_list|()
throws|throws
name|Exception
block|{
name|zip
operator|.
name|setPreservePathElements
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|getZippedTextInFolder
argument_list|(
literal|"poems/"
argument_list|,
literal|"poems/poem.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:zip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"poem.txt.zip"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:zip"
argument_list|,
name|TEXT
argument_list|,
name|FILE_NAME
argument_list|,
literal|"poems/poem.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnzip ()
specifier|public
name|void
name|testUnzip
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:unzip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEXT
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:unzip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"file"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unzip"
argument_list|,
name|getZippedText
argument_list|(
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnzipWithEmptyDirectorySupported ()
specifier|public
name|void
name|testUnzipWithEmptyDirectorySupported
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"hello_out"
argument_list|)
argument_list|)
expr_stmt|;
name|zip
operator|.
name|setUsingIterator
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|zip
operator|.
name|setAllowEmptyDirectory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unzipWithEmptyDirectory"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/hello.odt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"hello_out/Configurations2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"hello_out"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnzipWithEmptyDirectoryUnsupported ()
specifier|public
name|void
name|testUnzipWithEmptyDirectoryUnsupported
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"hello_out"
argument_list|)
argument_list|)
expr_stmt|;
name|zip
operator|.
name|setUsingIterator
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|zip
operator|.
name|setAllowEmptyDirectory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unzipWithEmptyDirectory"
argument_list|,
operator|new
name|File
argument_list|(
literal|"src/test/resources/hello.odt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"hello_out/Configurations2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
operator|new
name|File
argument_list|(
literal|"hello_out"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipAndUnzip ()
specifier|public
name|void
name|testZipAndUnzip
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:zipAndUnzip"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:zipAndUnzip"
argument_list|,
name|TEXT
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|TEXT
argument_list|,
operator|new
name|String
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipToFileWithoutFileName ()
specifier|public
name|void
name|testZipToFileWithoutFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
index|[]
name|files
init|=
name|TEST_DIR
operator|.
name|list
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|files
operator|==
literal|null
operator|||
name|files
operator|.
name|length
operator|==
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:intercepted"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:zipToFile"
argument_list|,
name|TEXT
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// use builder to ensure the exchange is fully done before we check for file exists
name|assertTrue
argument_list|(
literal|"The exchange is not done in time."
argument_list|,
name|notify
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|TEST_DIR
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
operator|+
literal|".zip"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"The file should exist."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Get a wrong message content."
argument_list|,
name|getZippedText
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
argument_list|,
name|getBytes
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testZipToFileWithFileName ()
specifier|public
name|void
name|testZipToFileWithFileName
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|notify
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:zipToFile"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|TEST_DIR
argument_list|,
literal|"poem.txt.zip"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"The zip should not exit."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:zipToFile"
argument_list|,
name|TEXT
argument_list|,
name|FILE_NAME
argument_list|,
literal|"poem.txt"
argument_list|)
expr_stmt|;
comment|// just make sure the file is created
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// use builder to ensure the exchange is fully done before we check for file exists
name|assertTrue
argument_list|(
literal|"The exchange is not done in time."
argument_list|,
name|notify
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"The file should exist."
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Get a wrong message content."
argument_list|,
name|getZippedText
argument_list|(
literal|"poem.txt"
argument_list|)
argument_list|,
name|getBytes
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDslZip ()
specifier|public
name|void
name|testDslZip
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:dslZip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|getZippedText
argument_list|(
literal|"poem.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dslZip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"poem.txt.zip"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:dslZip"
argument_list|,
name|TEXT
argument_list|,
name|FILE_NAME
argument_list|,
literal|"poem.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDslUnzip ()
specifier|public
name|void
name|testDslUnzip
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:dslUnzip"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEXT
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:dslUnzip"
argument_list|)
operator|.
name|expectedHeaderReceived
argument_list|(
name|FILE_NAME
argument_list|,
literal|"test.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:dslUnzip"
argument_list|,
name|getZippedText
argument_list|(
literal|"test.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|deleteDirectory
argument_list|(
name|TEST_DIR
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|copy (InputStream in, OutputStream out)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
while|while
condition|(
literal|true
condition|)
block|{
name|int
name|readCount
init|=
name|in
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
if|if
condition|(
name|readCount
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|out
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|readCount
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy (File file, OutputStream out)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
name|File
name|file
parameter_list|,
name|OutputStream
name|out
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|InputStream
name|in
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy (InputStream in, File file)
specifier|private
specifier|static
name|void
name|copy
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|OutputStream
name|out
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
init|)
block|{
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|interceptSendToEndpoint
argument_list|(
literal|"file:*"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:intercepted"
argument_list|)
expr_stmt|;
name|zip
operator|=
operator|new
name|ZipFileDataFormat
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:zip"
argument_list|)
operator|.
name|marshal
argument_list|(
name|zip
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:zip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unzip"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|zip
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unzip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unzipWithEmptyDirectory"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|zip
argument_list|)
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|streaming
argument_list|()
comment|//.to("file:hello_out?autoCreate=true")
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|ZipFile
name|zfile
init|=
operator|new
name|ZipFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/hello.odt"
argument_list|)
argument_list|)
decl_stmt|;
name|ZipEntry
name|entry
init|=
operator|new
name|ZipEntry
argument_list|(
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"hello_out"
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|InputStream
name|in
init|=
name|zfile
operator|.
name|getInputStream
argument_list|(
name|entry
argument_list|)
decl_stmt|;
try|try
block|{
name|copy
argument_list|(
name|in
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"direct:zipAndUnzip"
argument_list|)
operator|.
name|marshal
argument_list|(
name|zip
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|zip
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:zipAndUnzip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:zipToFile"
argument_list|)
operator|.
name|marshal
argument_list|(
name|zip
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:"
operator|+
name|TEST_DIR
operator|.
name|getPath
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:zipToFile"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:dslZip"
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|zipFile
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:dslZip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:dslUnzip"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|zipFile
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:dslUnzip"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:zipStreamCache"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|marshal
argument_list|()
operator|.
name|zipFile
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:zipStreamCache"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getZippedText (String entryName)
specifier|private
specifier|static
name|byte
index|[]
name|getZippedText
parameter_list|(
name|String
name|entryName
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEXT
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
try|try
block|{
name|zos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|entryName
argument_list|)
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|bais
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|bais
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|getZippedTextInFolder (String folder, String file)
specifier|private
specifier|static
name|byte
index|[]
name|getZippedTextInFolder
parameter_list|(
name|String
name|folder
parameter_list|,
name|String
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayInputStream
name|bais
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEXT
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|baos
argument_list|)
decl_stmt|;
try|try
block|{
name|zos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|folder
argument_list|)
argument_list|)
expr_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|bais
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|bais
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|getBytes (File file)
specifier|private
specifier|static
name|byte
index|[]
name|getBytes
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|fis
argument_list|,
name|baos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|,
name|baos
argument_list|)
expr_stmt|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

