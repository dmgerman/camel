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
DECL|method|testConvertor ()
specifier|public
name|void
name|testConvertor
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
name|person
operator|.
name|getFirstName
argument_list|()
argument_list|,
literal|"FOO"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong second name "
argument_list|,
name|person
operator|.
name|getLastName
argument_list|()
argument_list|,
literal|"BAR"
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
block|}
annotation|@
name|Test
DECL|method|testFilteringConvertor ()
specifier|public
name|void
name|testFilteringConvertor
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
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
name|person
operator|.
name|getFirstName
argument_list|()
argument_list|,
literal|"FOO"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get the wrong second name "
argument_list|,
name|person
operator|.
name|getLastName
argument_list|()
argument_list|,
literal|"BAR "
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
literal|"Didn't filter the non-xml chars"
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
block|}
end_class

end_unit

