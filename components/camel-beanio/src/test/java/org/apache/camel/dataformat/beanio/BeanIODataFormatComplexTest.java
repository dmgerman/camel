begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|java
operator|.
name|util
operator|.
name|Locale
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
name|beanio
operator|.
name|InvalidRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|UnexpectedRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|UnidentifiedRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|BeanIODataFormatComplexTest
specifier|public
class|class
name|BeanIODataFormatComplexTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|defaultLocale
specifier|private
specifier|static
name|Locale
name|defaultLocale
decl_stmt|;
DECL|field|recordData
specifier|private
specifier|final
name|String
name|recordData
init|=
literal|"0001917A112345.678900           "
operator|+
name|LS
operator|+
literal|"0002374A159303290.020           "
operator|+
name|LS
operator|+
literal|"0015219B1SECURITY ONE           "
operator|+
name|LS
operator|+
literal|"END OF SECTION 1                "
operator|+
name|LS
operator|+
literal|"0076647A10.0000000001           "
operator|+
name|LS
operator|+
literal|"0135515A1999999999999           "
operator|+
name|LS
operator|+
literal|"2000815B1SECURITY TWO           "
operator|+
name|LS
operator|+
literal|"2207122B1SECURITY THR           "
operator|+
name|LS
operator|+
literal|"END OF FILE 000007              "
operator|+
name|LS
decl_stmt|;
DECL|field|data
specifier|private
specifier|final
name|String
name|data
init|=
literal|"0000000A1030808PRICE            "
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"HEADER END                      "
operator|+
name|LS
operator|+
name|recordData
decl_stmt|;
DECL|field|unExpectedData
specifier|private
specifier|final
name|String
name|unExpectedData
init|=
literal|"0000000A1030808PRICE            "
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"HEADER END                      "
operator|+
name|LS
operator|+
name|recordData
decl_stmt|;
DECL|field|invalidData
specifier|private
specifier|final
name|String
name|invalidData
init|=
literal|"0000000A1030808PRICE            "
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         EXTRA DATA"
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"HEADER END                      "
operator|+
name|LS
operator|+
name|recordData
decl_stmt|;
DECL|field|unidentifiedData
specifier|private
specifier|final
name|String
name|unidentifiedData
init|=
literal|"0000000A1030808PRICE            "
operator|+
name|LS
operator|+
literal|"0000000C1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"0000000B1030808SECURITY         "
operator|+
name|LS
operator|+
literal|"HEADER END                      "
operator|+
name|LS
operator|+
name|recordData
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setLocale ()
specifier|public
specifier|static
name|void
name|setLocale
parameter_list|()
block|{
if|if
condition|(
operator|!
name|Locale
operator|.
name|getDefault
argument_list|()
operator|.
name|equals
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
condition|)
block|{
comment|// the Locale used for the number formatting of the above data is
comment|// english which could be other than the default locale
name|defaultLocale
operator|=
name|Locale
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|Locale
operator|.
name|setDefault
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AfterClass
DECL|method|resetLocale ()
specifier|public
specifier|static
name|void
name|resetLocale
parameter_list|()
block|{
if|if
condition|(
name|defaultLocale
operator|!=
literal|null
condition|)
block|{
name|Locale
operator|.
name|setDefault
argument_list|(
name|defaultLocale
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beanio-marshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|createTestData
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|createTestData
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalUnexpected ()
specifier|public
name|void
name|testUnmarshalUnexpected
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|unExpectedData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
name|assertIsInstanceOf
argument_list|(
name|UnexpectedRecordException
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalInvalid ()
specifier|public
name|void
name|testUnmarshalInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|invalidData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
name|assertIsInstanceOf
argument_list|(
name|InvalidRecordException
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalUnidentifiedIgnore ()
specifier|public
name|void
name|testUnmarshalUnidentifiedIgnore
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|createTestData
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal-forgiving"
argument_list|,
name|unidentifiedData
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalUnexpectedIgnore ()
specifier|public
name|void
name|testUnmarshalUnexpectedIgnore
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|createTestData
argument_list|(
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal-forgiving"
argument_list|,
name|unExpectedData
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalInvalidIgnore ()
specifier|public
name|void
name|testUnmarshalInvalidIgnore
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|createTestData
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal-forgiving"
argument_list|,
name|invalidData
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalUnidentified ()
specifier|public
name|void
name|testUnmarshalUnidentified
parameter_list|()
throws|throws
name|Exception
block|{
name|Throwable
name|ex
init|=
literal|null
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|unidentifiedData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
operator|.
name|getCause
argument_list|()
expr_stmt|;
block|}
name|assertIsInstanceOf
argument_list|(
name|UnidentifiedRecordException
operator|.
name|class
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestData (boolean skipB1header)
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|createTestData
parameter_list|(
name|boolean
name|skipB1header
parameter_list|)
throws|throws
name|ParseException
block|{
name|String
name|source
init|=
literal|"camel-beanio"
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Date
name|date
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"ddMMyy"
argument_list|)
operator|.
name|parse
argument_list|(
literal|"030808"
argument_list|)
decl_stmt|;
name|Header
name|hFirst
init|=
operator|new
name|Header
argument_list|(
literal|"A1"
argument_list|,
name|date
argument_list|,
literal|"PRICE"
argument_list|)
decl_stmt|;
name|Header
name|hSecond
init|=
operator|new
name|Header
argument_list|(
literal|"B1"
argument_list|,
name|date
argument_list|,
literal|"SECURITY"
argument_list|)
decl_stmt|;
name|Separator
name|headerEnd
init|=
operator|new
name|Separator
argument_list|(
literal|"HEADER END"
argument_list|)
decl_stmt|;
name|A1Record
name|first
init|=
operator|new
name|A1Record
argument_list|(
literal|"0001917"
argument_list|,
name|source
argument_list|,
literal|12345.678900
argument_list|)
decl_stmt|;
name|A1Record
name|second
init|=
operator|new
name|A1Record
argument_list|(
literal|"0002374"
argument_list|,
name|source
argument_list|,
literal|59303290.020
argument_list|)
decl_stmt|;
name|B1Record
name|third
init|=
operator|new
name|B1Record
argument_list|(
literal|"0015219"
argument_list|,
name|source
argument_list|,
literal|"SECURITY ONE"
argument_list|)
decl_stmt|;
name|Separator
name|sectionEnd
init|=
operator|new
name|Separator
argument_list|(
literal|"END OF SECTION 1"
argument_list|)
decl_stmt|;
name|A1Record
name|fourth
init|=
operator|new
name|A1Record
argument_list|(
literal|"0076647"
argument_list|,
name|source
argument_list|,
literal|0.0000000001
argument_list|)
decl_stmt|;
name|A1Record
name|fifth
init|=
operator|new
name|A1Record
argument_list|(
literal|"0135515"
argument_list|,
name|source
argument_list|,
literal|999999999999d
argument_list|)
decl_stmt|;
name|B1Record
name|sixth
init|=
operator|new
name|B1Record
argument_list|(
literal|"2000815"
argument_list|,
name|source
argument_list|,
literal|"SECURITY TWO"
argument_list|)
decl_stmt|;
name|B1Record
name|seventh
init|=
operator|new
name|B1Record
argument_list|(
literal|"2207122"
argument_list|,
name|source
argument_list|,
literal|"SECURITY THR"
argument_list|)
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|hFirst
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|skipB1header
condition|)
block|{
name|body
operator|.
name|add
argument_list|(
name|hSecond
argument_list|)
expr_stmt|;
block|}
name|body
operator|.
name|add
argument_list|(
name|headerEnd
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|first
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|second
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|third
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|sectionEnd
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|fourth
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|fifth
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|sixth
argument_list|)
expr_stmt|;
name|body
operator|.
name|add
argument_list|(
name|seventh
argument_list|)
expr_stmt|;
name|Trailer
name|trailer
init|=
operator|new
name|Trailer
argument_list|(
literal|7
argument_list|)
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|trailer
argument_list|)
expr_stmt|;
return|return
name|body
return|;
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
name|BeanIODataFormat
name|format
init|=
operator|new
name|BeanIODataFormat
argument_list|(
literal|"org/apache/camel/dataformat/beanio/mappings.xml"
argument_list|,
literal|"securityData"
argument_list|)
decl_stmt|;
name|BeanIODataFormat
name|forgivingFormat
init|=
operator|new
name|BeanIODataFormat
argument_list|(
literal|"org/apache/camel/dataformat/beanio/mappings.xml"
argument_list|,
literal|"securityData"
argument_list|)
decl_stmt|;
name|forgivingFormat
operator|.
name|setIgnoreInvalidRecords
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|forgivingFormat
operator|.
name|setIgnoreUnexpectedRecords
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|forgivingFormat
operator|.
name|setIgnoreUnidentifiedRecords
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|format
argument_list|)
operator|.
name|split
argument_list|(
name|simple
argument_list|(
literal|"${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal-forgiving"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|forgivingFormat
argument_list|)
operator|.
name|split
argument_list|(
name|simple
argument_list|(
literal|"${body}"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beanio-unmarshal"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|format
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:beanio-marshal"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

