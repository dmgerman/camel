begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|XMLStreamException
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|not
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
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyObject
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|argThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|isA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|same
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
DECL|class|JaxbDataFormatTest
specifier|public
class|class
name|JaxbDataFormatTest
block|{
DECL|field|jaxbDataFormat
specifier|private
name|JaxbDataFormat
name|jaxbDataFormat
decl_stmt|;
DECL|field|marshallerMock
specifier|private
name|Marshaller
name|marshallerMock
decl_stmt|;
DECL|field|jaxbDataFormatMock
specifier|private
name|JaxbDataFormat
name|jaxbDataFormatMock
decl_stmt|;
DECL|field|unmarshallerMock
specifier|private
name|Unmarshaller
name|unmarshallerMock
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
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
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|jaxbDataFormat
operator|=
operator|new
name|JaxbDataFormat
argument_list|()
expr_stmt|;
name|jaxbDataFormat
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|jaxbDataFormat
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNeedFilteringDisabledFiltering ()
specifier|public
name|void
name|testNeedFilteringDisabledFiltering
parameter_list|()
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
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
name|camelContext
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|jaxbDataFormat
operator|.
name|needFiltering
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNeedFilteringEnabledFiltering ()
specifier|public
name|void
name|testNeedFilteringEnabledFiltering
parameter_list|()
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
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
name|camelContext
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|jaxbDataFormat
operator|.
name|needFiltering
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNeedFilteringTruePropagates ()
specifier|public
name|void
name|testNeedFilteringTruePropagates
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|jaxbDataFormat
operator|.
name|needFiltering
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNeedFilteringFalsePropagates ()
specifier|public
name|void
name|testNeedFilteringFalsePropagates
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|jaxbDataFormat
operator|.
name|needFiltering
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalFilteringDisabled ()
specifier|public
name|void
name|testMarshalFilteringDisabled
parameter_list|()
throws|throws
name|IOException
throws|,
name|XMLStreamException
throws|,
name|JAXBException
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jaxbDataFormatMock
operator|=
name|spy
argument_list|(
name|jaxbDataFormat
argument_list|)
expr_stmt|;
name|marshallerMock
operator|=
name|mock
argument_list|(
name|Marshaller
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|graph
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|OutputStream
name|stream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|jaxbDataFormatMock
operator|.
name|marshal
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
argument_list|,
name|graph
argument_list|,
name|stream
argument_list|,
name|marshallerMock
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|marshallerMock
argument_list|)
operator|.
name|marshal
argument_list|(
name|same
argument_list|(
name|graph
argument_list|)
argument_list|,
name|same
argument_list|(
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalFilteringEnabled ()
specifier|public
name|void
name|testMarshalFilteringEnabled
parameter_list|()
throws|throws
name|XMLStreamException
throws|,
name|JAXBException
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jaxbDataFormatMock
operator|=
name|spy
argument_list|(
name|jaxbDataFormat
argument_list|)
expr_stmt|;
name|marshallerMock
operator|=
name|mock
argument_list|(
name|Marshaller
operator|.
name|class
argument_list|)
expr_stmt|;
name|Object
name|graph
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
name|jaxbDataFormatMock
operator|.
name|marshal
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
argument_list|,
name|graph
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|,
name|marshallerMock
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|marshallerMock
argument_list|)
operator|.
name|marshal
argument_list|(
name|same
argument_list|(
name|graph
argument_list|)
argument_list|,
name|isA
argument_list|(
name|FilteringXmlStreamWriter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalFilteringDisabled ()
specifier|public
name|void
name|testUnmarshalFilteringDisabled
parameter_list|()
throws|throws
name|IOException
throws|,
name|JAXBException
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|jaxbDataFormatMock
operator|=
name|spy
argument_list|(
name|jaxbDataFormat
argument_list|)
expr_stmt|;
name|unmarshallerMock
operator|=
name|mock
argument_list|(
name|Unmarshaller
operator|.
name|class
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|unmarshallerMock
argument_list|)
operator|.
name|when
argument_list|(
name|jaxbDataFormatMock
argument_list|)
operator|.
name|getUnmarshaller
argument_list|()
expr_stmt|;
name|jaxbDataFormatMock
operator|.
name|unmarshal
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|unmarshallerMock
argument_list|)
operator|.
name|unmarshal
argument_list|(
operator|(
name|XMLStreamReader
operator|)
name|argThat
argument_list|(
name|instanceOf
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalFilteringEnabled ()
specifier|public
name|void
name|testUnmarshalFilteringEnabled
parameter_list|()
throws|throws
name|IOException
throws|,
name|JAXBException
block|{
name|jaxbDataFormat
operator|.
name|setFilterNonXmlChars
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|jaxbDataFormatMock
operator|=
name|spy
argument_list|(
name|jaxbDataFormat
argument_list|)
expr_stmt|;
name|unmarshallerMock
operator|=
name|mock
argument_list|(
name|Unmarshaller
operator|.
name|class
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|unmarshallerMock
argument_list|)
operator|.
name|when
argument_list|(
name|jaxbDataFormatMock
argument_list|)
operator|.
name|getUnmarshaller
argument_list|()
expr_stmt|;
name|jaxbDataFormatMock
operator|.
name|unmarshal
argument_list|(
operator|new
name|DefaultExchange
argument_list|(
name|camelContext
argument_list|)
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|unmarshallerMock
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|any
argument_list|(
name|NonXmlFilterReader
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

