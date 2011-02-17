begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxp
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
name|OutputKeys
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Node
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
name|CamelContext
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
name|impl
operator|.
name|DefaultExchange
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XmlConverterTest
specifier|public
class|class
name|XmlConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testToResultNoSource ()
specifier|public
name|void
name|testToResultNoSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|conv
operator|.
name|toResult
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|testToBytesSource ()
specifier|public
name|void
name|testToBytesSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|BytesSource
name|bs
init|=
name|conv
operator|.
name|toBytesSource
argument_list|(
literal|"<foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|bs
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
operator|new
name|String
argument_list|(
name|bs
operator|.
name|getData
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringFromSourceNoSource ()
specifier|public
name|void
name|testToStringFromSourceNoSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
literal|null
decl_stmt|;
name|String
name|out
init|=
name|conv
operator|.
name|toString
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStringWithBytesSource ()
specifier|public
name|void
name|testToStringWithBytesSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|conv
operator|.
name|toBytesSource
argument_list|(
literal|"<foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|conv
operator|.
name|toString
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSource ()
specifier|public
name|void
name|testToSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|conv
operator|.
name|toSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
name|conv
operator|.
name|toString
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSourceUsingTypeConverter ()
specifier|public
name|void
name|testToSourceUsingTypeConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|Source
name|source
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|String
name|out
init|=
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
name|source
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
comment|// try again to ensure it works the 2nd time
name|source
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
literal|"<foo>baz</foo>"
argument_list|)
expr_stmt|;
name|out
operator|=
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
name|source
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>baz</foo>"
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteArrayWithExchange ()
specifier|public
name|void
name|testToByteArrayWithExchange
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
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|conv
operator|.
name|toBytesSource
argument_list|(
literal|"<foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|conv
operator|.
name|toByteArray
argument_list|(
name|source
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
operator|new
name|String
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToByteArrayWithNoExchange ()
specifier|public
name|void
name|testToByteArrayWithNoExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
name|conv
operator|.
name|toBytesSource
argument_list|(
literal|"<foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|out
init|=
name|conv
operator|.
name|toByteArray
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
operator|new
name|String
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomSourceByDomSource ()
specifier|public
name|void
name|testToDomSourceByDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|DOMSource
name|source
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|DOMSource
name|out
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomSourceByStaxSource ()
specifier|public
name|void
name|testToDomSourceByStaxSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|DOMSource
name|out
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomSourceByCustomSource ()
specifier|public
name|void
name|testToDomSourceByCustomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|dummy
init|=
operator|new
name|Source
argument_list|()
block|{
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|s
parameter_list|)
block|{             }
block|}
decl_stmt|;
name|DOMSource
name|out
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
name|dummy
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSaxSourceByInputStream ()
specifier|public
name|void
name|testToSaxSourceByInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|SAXSource
name|out
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSaxSourceByDomSource ()
specifier|public
name|void
name|testToSaxSourceByDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|DOMSource
name|source
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|SAXSource
name|out
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSaxSourceByStaxSource ()
specifier|public
name|void
name|testToSaxSourceByStaxSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SAXSource
name|out
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToSaxSourceByCustomSource ()
specifier|public
name|void
name|testToSaxSourceByCustomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|dummy
init|=
operator|new
name|Source
argument_list|()
block|{
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|s
parameter_list|)
block|{             }
block|}
decl_stmt|;
name|SAXSource
name|out
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
name|dummy
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByFile ()
specifier|public
name|void
name|testToStreamSourceByFile
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"org/apache/camel/converter/stream/test.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|StreamSource
name|source
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByStreamSource ()
specifier|public
name|void
name|testToStreamSourceByStreamSource
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
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|StreamSource
name|source
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
literal|"<foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByDomSource ()
specifier|public
name|void
name|testToStreamSourceByDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|DOMSource
name|source
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByStaxSource ()
specifier|public
name|void
name|testToStreamSourceByStaxSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByCustomSource ()
specifier|public
name|void
name|testToStreamSourceByCustomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Source
name|dummy
init|=
operator|new
name|Source
argument_list|()
block|{
specifier|public
name|String
name|getSystemId
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|s
parameter_list|)
block|{             }
block|}
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|dummy
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByInputStream ()
specifier|public
name|void
name|testToStreamSourceByInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByReader ()
specifier|public
name|void
name|testToStreamSourceByReader
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Reader
name|reader
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByByteArray ()
specifier|public
name|void
name|testToStreamSourceByByteArray
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
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|context
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
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|bytes
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToStreamSourceByByteBuffer ()
specifier|public
name|void
name|testToStreamSourceByByteBuffer
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
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|ByteBuffer
name|bytes
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|StreamSource
name|out
init|=
name|conv
operator|.
name|toStreamSource
argument_list|(
name|bytes
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToVariousUsingNull ()
specifier|public
name|void
name|testToVariousUsingNull
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|conv
operator|.
name|toStreamSource
argument_list|(
name|is
argument_list|)
argument_list|)
expr_stmt|;
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|conv
operator|.
name|toStreamSource
argument_list|(
name|reader
argument_list|)
argument_list|)
expr_stmt|;
name|File
name|file
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|conv
operator|.
name|toStreamSource
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|null
decl_stmt|;
name|assertNull
argument_list|(
name|conv
operator|.
name|toStreamSource
argument_list|(
name|bytes
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|Node
name|node
init|=
literal|null
decl_stmt|;
name|conv
operator|.
name|toDOMElement
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
DECL|method|testToReaderFromSource ()
specifier|public
name|void
name|testToReaderFromSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Reader
name|out
init|=
name|conv
operator|.
name|toReaderFromSource
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomSourceFromInputStream ()
specifier|public
name|void
name|testToDomSourceFromInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|DOMSource
name|out
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomElement ()
specifier|public
name|void
name|testToDomElement
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Element
name|out
init|=
name|conv
operator|.
name|toDOMElement
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomElementFromDocumentNode ()
specifier|public
name|void
name|testToDomElementFromDocumentNode
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|context
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|Element
name|out
init|=
name|conv
operator|.
name|toDOMElement
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDomElementFromElementNode ()
specifier|public
name|void
name|testToDomElementFromElementNode
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|context
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|Element
name|out
init|=
name|conv
operator|.
name|toDOMElement
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDocumentFromBytes ()
specifier|public
name|void
name|testToDocumentFromBytes
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|Document
name|out
init|=
name|conv
operator|.
name|toDOMDocument
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDocumentFromInputStream ()
specifier|public
name|void
name|testToDocumentFromInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|Document
name|out
init|=
name|conv
operator|.
name|toDOMDocument
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|out
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToInputStreamFromDocument ()
specifier|public
name|void
name|testToInputStreamFromDocument
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|context
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
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
name|conv
operator|.
name|toInputStream
argument_list|(
name|doc
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|is
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
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
name|is
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToDocumentFromFile ()
specifier|public
name|void
name|testToDocumentFromFile
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"./src/test/resources/org/apache/camel/converter/stream/test.xml"
argument_list|)
operator|.
name|getAbsoluteFile
argument_list|()
decl_stmt|;
name|Document
name|out
init|=
name|conv
operator|.
name|toDOMDocument
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|String
name|s
init|=
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
name|out
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|s
operator|.
name|contains
argument_list|(
literal|"<firstName>James</firstName>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testToInputStreamByDomSource ()
specifier|public
name|void
name|testToInputStreamByDomSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|DOMSource
name|source
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputStream
name|out
init|=
name|conv
operator|.
name|toInputStream
argument_list|(
name|source
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|String
name|s
init|=
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
name|out
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
DECL|method|testToInputSource ()
specifier|public
name|void
name|testToInputSource
parameter_list|()
throws|throws
name|Exception
block|{
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
literal|"<foo>bar</foo>"
argument_list|)
decl_stmt|;
name|InputSource
name|out
init|=
name|conv
operator|.
name|toInputSource
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
operator|.
name|getByteStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testOutOptionsFromCamelContext ()
specifier|public
name|void
name|testOutOptionsFromCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
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
comment|// shows how to set the OutputOptions from camelContext
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|XmlConverter
operator|.
name|OUTPUT_PROPERTIES_PREFIX
operator|+
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|put
argument_list|(
name|XmlConverter
operator|.
name|OUTPUT_PROPERTIES_PREFIX
operator|+
name|OutputKeys
operator|.
name|STANDALONE
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|XmlConverter
name|conv
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
name|SAXSource
name|source
init|=
name|conv
operator|.
name|toSAXSource
argument_list|(
literal|"<foo>bar</foo>"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|DOMSource
name|out
init|=
name|conv
operator|.
name|toDOMSource
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|source
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><foo>bar</foo>"
argument_list|,
name|conv
operator|.
name|toString
argument_list|(
name|out
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

