begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

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
name|List
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
name|Templates
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
name|ExpectedBodyTypeException
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
name|xslt
operator|.
name|StreamResultHandlerFactory
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
name|xslt
operator|.
name|XsltBuilder
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
name|spi
operator|.
name|Synchronization
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
name|support
operator|.
name|UnitOfWorkHelper
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

begin_class
DECL|class|XsltBuilderTest
specifier|public
class|class
name|XsltBuilderTest
extends|extends
name|ContextTestSupport
block|{
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
literal|"target/data/xslt"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/data/xslt"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltUrl ()
specifier|public
name|void
name|testXsltUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltTransformerUrl ()
specifier|public
name|void
name|testXsltTransformerUrl
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
operator|new
name|XsltBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setTransformerURL
argument_list|(
name|styleSheet
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltFile ()
specifier|public
name|void
name|testXsltFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|styleSheet
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltTransformerFile ()
specifier|public
name|void
name|testXsltTransformerFile
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|styleSheet
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
operator|new
name|XsltBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setTransformerFile
argument_list|(
name|styleSheet
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltInputStream ()
specifier|public
name|void
name|testXsltInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|styleSheet
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|styleSheet
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltTransformerInputStream ()
specifier|public
name|void
name|testXsltTransformerInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|styleSheet
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
operator|new
name|XsltBuilder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setTransformerInputStream
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|styleSheet
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltSource ()
specifier|public
name|void
name|testXsltSource
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
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|Source
name|styleSheet
init|=
operator|new
name|SAXSource
argument_list|(
operator|new
name|InputSource
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
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltTemplates ()
specifier|public
name|void
name|testXsltTemplates
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
literal|"src/test/resources/org/apache/camel/builder/xml/example.xsl"
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
operator|new
name|SAXSource
argument_list|(
operator|new
name|InputSource
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
argument_list|)
decl_stmt|;
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Templates
name|styleSheet
init|=
name|converter
operator|.
name|getTransformerFactory
argument_list|()
operator|.
name|newTemplates
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltOutputString ()
specifier|public
name|void
name|testXsltOutputString
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
operator|.
name|outputString
argument_list|()
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltOutputBytes ()
specifier|public
name|void
name|testXsltOutputBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
operator|.
name|outputBytes
argument_list|()
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltOutputDOM ()
specifier|public
name|void
name|testXsltOutputDOM
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
operator|.
name|outputDOM
argument_list|()
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltOutputFile ()
specifier|public
name|void
name|testXsltOutputFile
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
operator|.
name|outputFile
argument_list|()
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|XSLT_FILE_NAME
argument_list|,
literal|"target/data/xslt/xsltout.xml"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|File
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/xslt/xsltout.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Output file should exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|endsWith
argument_list|(
literal|"<goodbye>world!</goodbye>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltOutputFileDelete ()
specifier|public
name|void
name|testXsltOutputFileDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
operator|.
name|outputFile
argument_list|()
operator|.
name|deleteOutputFile
argument_list|()
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|XSLT_FILE_NAME
argument_list|,
literal|"target/data/xslt/xsltout.xml"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|File
operator|.
name|class
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/data/xslt/xsltout.xml"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Output file should exist"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|endsWith
argument_list|(
literal|"<goodbye>world!</goodbye>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// now done the exchange
name|List
argument_list|<
name|Synchronization
argument_list|>
name|onCompletions
init|=
name|exchange
operator|.
name|handoverCompletions
argument_list|()
decl_stmt|;
name|UnitOfWorkHelper
operator|.
name|doneSynchronizations
argument_list|(
name|exchange
argument_list|,
name|onCompletions
argument_list|,
name|log
argument_list|)
expr_stmt|;
comment|// the file should be deleted
name|assertFalse
argument_list|(
literal|"Output file should be deleted"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXsltResultHandler ()
specifier|public
name|void
name|testXsltResultHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|builder
operator|.
name|getResultHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|outputBytes
argument_list|()
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|StreamResultHandlerFactory
operator|.
name|class
argument_list|,
name|builder
operator|.
name|getResultHandlerFactory
argument_list|()
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye>world!</goodbye>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNullBodyDefault ()
specifier|public
name|void
name|testNullBodyDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpectedBodyTypeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testFailNullBody ()
specifier|public
name|void
name|testFailNullBody
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setFailOnNullBody
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExpectedBodyTypeException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testNotFailNullBody ()
specifier|public
name|void
name|testNotFailNullBody
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|styleSheet
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"example.xsl"
argument_list|)
decl_stmt|;
name|XsltBuilder
name|builder
init|=
name|XsltBuilder
operator|.
name|xslt
argument_list|(
name|styleSheet
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setFailOnNullBody
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|builder
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><goodbye/>"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

