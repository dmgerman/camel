begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|TypeConversionException
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
name|TypeConverter
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
name|jaxb
operator|.
name|FallbackTypeConverter
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
name|jaxb
operator|.
name|message
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
name|example
operator|.
name|Bar
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
name|example
operator|.
name|Foo
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
name|foo
operator|.
name|bar
operator|.
name|PersonType
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CamelJaxbFallbackConverterTest
specifier|public
class|class
name|CamelJaxbFallbackConverterTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testFallbackConverterWithoutObjectFactory ()
specifier|public
name|void
name|testFallbackConverterWithoutObjectFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|Foo
name|foo
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
literal|"<foo><zot name=\"bar1\" value=\"value\" otherValue=\"otherValue\"/></foo>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"foo should not be null"
argument_list|,
name|foo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|foo
operator|.
name|getBarRefs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|foo
operator|.
name|getBarRefs
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Bar
name|bar
init|=
operator|new
name|Bar
argument_list|()
decl_stmt|;
name|bar
operator|.
name|setName
argument_list|(
literal|"myName"
argument_list|)
expr_stmt|;
name|bar
operator|.
name|setValue
argument_list|(
literal|"myValue"
argument_list|)
expr_stmt|;
name|foo
operator|.
name|getBarRefs
argument_list|()
operator|.
name|add
argument_list|(
name|bar
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
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|foo
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should get a right marshalled string"
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|"<bar name=\"myName\" value=\"myValue\"/>"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFallbackConverterUnmarshalWithNonJAXBComplaintValue ()
specifier|public
name|void
name|testFallbackConverterUnmarshalWithNonJAXBComplaintValue
parameter_list|()
throws|throws
name|Exception
block|{
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
try|try
block|{
name|converter
operator|.
name|convertTo
argument_list|(
name|Foo
operator|.
name|class
argument_list|,
literal|"Not every String is XML"
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
name|TypeConversionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
try|try
block|{
name|converter
operator|.
name|convertTo
argument_list|(
name|Bar
operator|.
name|class
argument_list|,
literal|"<bar></bar"
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
name|TypeConversionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testConverter ()
specifier|public
name|void
name|testConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|PersonType
name|person
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|PersonType
operator|.
name|class
argument_list|,
literal|"<Person><firstName>FOO</firstName><lastName>BAR</lastName></Person>"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Person should not be null "
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong first name "
argument_list|,
literal|"FOO"
argument_list|,
name|person
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong second name "
argument_list|,
literal|"BAR"
argument_list|,
name|person
operator|.
name|getLastName
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
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|person
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should get a right marshalled string"
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|"<lastName>BAR</lastName>"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffers
init|=
literal|"<Person><firstName>FOO</firstName><lastName>BAR\u0008</lastName></Person>"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffers
argument_list|)
decl_stmt|;
try|try
block|{
name|converter
operator|.
name|convertTo
argument_list|(
name|PersonType
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
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
name|TypeConversionException
name|e
parameter_list|)
block|{
comment|// expected
block|}
block|}
annotation|@
name|Test
DECL|method|testFilteringConverter ()
specifier|public
name|void
name|testFilteringConverter
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|buffers
init|=
literal|"<Person><firstName>FOO</firstName><lastName>BAR\u0008</lastName></Person>"
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|InputStream
name|is
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buffers
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|PersonType
name|person
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|PersonType
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Person should not be null "
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong first name "
argument_list|,
literal|"FOO"
argument_list|,
name|person
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong second name "
argument_list|,
literal|"BAR "
argument_list|,
name|person
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|person
operator|.
name|setLastName
argument_list|(
literal|"BAR\u0008\uD8FF"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|person
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Didn't filter the non-xml chars"
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|"<lastName>BAR</lastName>"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|value
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|person
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should not filter the non-xml chars"
argument_list|,
name|value
operator|.
name|indexOf
argument_list|(
literal|"<lastName>BAR
literal|\uD8FF</lastName>"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoXmlRootElementAnnotation ()
specifier|public
name|void
name|testNoXmlRootElementAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
operator|new
name|Message
argument_list|(
literal|"Hello World"
argument_list|)
decl_stmt|;
name|TypeConverter
name|converter
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
name|String
name|marshalled
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|Message
name|out
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Message
operator|.
name|class
argument_list|,
name|marshalled
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

