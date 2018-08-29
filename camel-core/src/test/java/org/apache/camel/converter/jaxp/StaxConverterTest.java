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
name|org
operator|.
name|junit
operator|.
name|Test
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
name|StringReader
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLEventReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLEventWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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

begin_class
DECL|class|StaxConverterTest
specifier|public
class|class
name|StaxConverterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ISO_8859_1
specifier|private
specifier|static
specifier|final
name|Charset
name|ISO_8859_1
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
DECL|field|UTF_8
specifier|private
specifier|static
specifier|final
name|Charset
name|UTF_8
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
DECL|field|TEST_XML
specifier|private
specifier|static
specifier|final
name|String
name|TEST_XML
init|=
literal|"<test>Test Message with umlaut \u00E4\u00F6\u00FC</test>"
decl_stmt|;
comment|// umlauts have different encoding in UTF-8 and ISO-8859-1 (Latin1)
DECL|field|TEST_XML_WITH_XML_HEADER_ISO_8859_1
specifier|private
specifier|static
specifier|final
name|String
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1
init|=
literal|"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"
operator|+
name|TEST_XML
decl_stmt|;
DECL|field|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
specifier|private
specifier|static
specifier|final
name|ByteArrayInputStream
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1
operator|.
name|getBytes
argument_list|(
name|ISO_8859_1
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|TEST_XML_WITH_XML_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_XML_WITH_XML_HEADER
init|=
literal|"<?xml version=\"1.0\"?>"
operator|+
name|TEST_XML
decl_stmt|;
DECL|field|TEST_XML_7000
specifier|private
specifier|static
specifier|final
name|String
name|TEST_XML_7000
decl_stmt|;
static|static
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|7000
argument_list|)
decl_stmt|;
comment|// using quote character to make the plain characters comparison work with the generated xml
name|sb
operator|.
name|append
argument_list|(
literal|"<?xml version='1.0' encoding='utf-8'?>"
argument_list|)
operator|.
name|append
argument_list|(
literal|"<list>"
argument_list|)
expr_stmt|;
name|int
name|n
init|=
literal|6963
operator|-
name|TEST_XML
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|n
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|TEST_XML
argument_list|)
expr_stmt|;
name|n
operator|-=
name|TEST_XML
operator|.
name|length
argument_list|()
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</list>"
argument_list|)
expr_stmt|;
name|TEST_XML_7000
operator|=
name|sb
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEncodingXmlEventReader ()
specifier|public
name|void
name|testEncodingXmlEventReader
parameter_list|()
throws|throws
name|Exception
block|{
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
operator|.
name|reset
argument_list|()
expr_stmt|;
name|XMLEventReader
name|reader
init|=
literal|null
decl_stmt|;
name|XMLEventWriter
name|writer
init|=
literal|null
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// enter text encoded with Latin1
name|reader
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLEventReader
operator|.
name|class
argument_list|,
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
argument_list|)
expr_stmt|;
name|output
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
comment|// ensure UTF-8 encoding
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
name|UTF_8
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLEventWriter
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|output
argument_list|)
expr_stmt|;
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|writer
operator|.
name|add
argument_list|(
name|reader
operator|.
name|nextEvent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
comment|// normalize the auotation mark
if|if
condition|(
name|result
operator|.
name|indexOf
argument_list|(
literal|'\''
argument_list|)
operator|>
literal|0
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|replace
argument_list|(
literal|'\''
argument_list|,
literal|'"'
argument_list|)
expr_stmt|;
block|}
name|boolean
name|equals
init|=
name|TEST_XML_WITH_XML_HEADER
operator|.
name|equals
argument_list|(
name|result
argument_list|)
operator|||
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1
operator|.
name|equals
argument_list|(
name|result
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should match header"
argument_list|,
name|equals
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEncodingXmlStreamReader ()
specifier|public
name|void
name|testEncodingXmlStreamReader
parameter_list|()
throws|throws
name|Exception
block|{
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
operator|.
name|reset
argument_list|()
expr_stmt|;
name|XMLStreamReader
name|reader
init|=
literal|null
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
literal|null
decl_stmt|;
name|ByteArrayOutputStream
name|output
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// enter text encoded with Latin1
name|reader
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|TEST_XML_WITH_XML_HEADER_ISO_8859_1_AS_BYTE_ARRAY_STREAM
argument_list|)
expr_stmt|;
name|output
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|()
expr_stmt|;
comment|// ensure UTF-8 encoding
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
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|writer
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|output
argument_list|)
expr_stmt|;
comment|// copy to writer
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|reader
operator|.
name|next
argument_list|()
expr_stmt|;
switch|switch
condition|(
name|reader
operator|.
name|getEventType
argument_list|()
condition|)
block|{
case|case
name|XMLStreamConstants
operator|.
name|START_DOCUMENT
case|:
name|writer
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
case|:
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
name|writer
operator|.
name|writeStartElement
argument_list|(
name|reader
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|CHARACTERS
case|:
name|writer
operator|.
name|writeCharacters
argument_list|(
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
name|writer
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
break|break;
default|default:
break|break;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
name|assertNotNull
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|String
name|result
init|=
operator|new
name|String
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|,
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|TEST_XML
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testToReaderByXmlStreamReader ()
specifier|public
name|void
name|testToReaderByXmlStreamReader
parameter_list|()
throws|throws
name|Exception
block|{
name|StringReader
name|src
init|=
operator|new
name|StringReader
argument_list|(
name|TEST_XML_7000
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xreader
init|=
literal|null
decl_stmt|;
name|Reader
name|reader
init|=
literal|null
decl_stmt|;
try|try
block|{
name|xreader
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|src
argument_list|)
expr_stmt|;
name|reader
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|xreader
argument_list|)
expr_stmt|;
comment|// verify
name|StringReader
name|expected
init|=
operator|new
name|StringReader
argument_list|(
name|TEST_XML_7000
argument_list|)
decl_stmt|;
name|char
index|[]
name|tmp1
init|=
operator|new
name|char
index|[
literal|512
index|]
decl_stmt|;
name|char
index|[]
name|tmp2
init|=
operator|new
name|char
index|[
literal|512
index|]
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|n1
init|=
literal|0
decl_stmt|;
name|int
name|n2
init|=
literal|0
decl_stmt|;
try|try
block|{
name|n1
operator|=
name|expected
operator|.
name|read
argument_list|(
name|tmp1
argument_list|,
literal|0
argument_list|,
name|tmp1
operator|.
name|length
argument_list|)
expr_stmt|;
name|n2
operator|=
name|reader
operator|.
name|read
argument_list|(
name|tmp2
argument_list|,
literal|0
argument_list|,
name|tmp2
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"unable to read data"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
expr_stmt|;
if|if
condition|(
name|n2
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|tmp1
argument_list|,
name|tmp2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|xreader
operator|!=
literal|null
condition|)
block|{
name|xreader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testToInputSreamByXmlStreamReader ()
specifier|public
name|void
name|testToInputSreamByXmlStreamReader
parameter_list|()
throws|throws
name|Exception
block|{
name|StringReader
name|src
init|=
operator|new
name|StringReader
argument_list|(
name|TEST_XML_7000
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xreader
init|=
literal|null
decl_stmt|;
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
name|xreader
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|src
argument_list|)
expr_stmt|;
name|in
operator|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|xreader
argument_list|)
expr_stmt|;
comment|// verify
name|InputStream
name|expected
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|TEST_XML_7000
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|tmp1
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
name|byte
index|[]
name|tmp2
init|=
operator|new
name|byte
index|[
literal|512
index|]
decl_stmt|;
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|n1
init|=
literal|0
decl_stmt|;
name|int
name|n2
init|=
literal|0
decl_stmt|;
try|try
block|{
name|n1
operator|=
name|expected
operator|.
name|read
argument_list|(
name|tmp1
argument_list|,
literal|0
argument_list|,
name|tmp1
operator|.
name|length
argument_list|)
expr_stmt|;
name|n2
operator|=
name|in
operator|.
name|read
argument_list|(
name|tmp2
argument_list|,
literal|0
argument_list|,
name|tmp2
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"unable to read data"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|n1
argument_list|,
name|n2
argument_list|)
expr_stmt|;
if|if
condition|(
name|n2
operator|<
literal|0
condition|)
block|{
break|break;
block|}
name|assertTrue
argument_list|(
name|Arrays
operator|.
name|equals
argument_list|(
name|tmp1
argument_list|,
name|tmp2
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|xreader
operator|!=
literal|null
condition|)
block|{
name|xreader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
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
block|}
block|}
end_class

end_unit

