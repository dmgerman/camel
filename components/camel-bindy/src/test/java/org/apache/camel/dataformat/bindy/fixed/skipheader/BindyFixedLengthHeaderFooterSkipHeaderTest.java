begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fixed.skipheader
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|fixed
operator|.
name|skipheader
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|GregorianCalendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|EndpointInject
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
name|dataformat
operator|.
name|bindy
operator|.
name|fixed
operator|.
name|BindyFixedLengthDataFormat
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
name|model
operator|.
name|dataformat
operator|.
name|BindyType
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
name|Assert
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

begin_comment
comment|/**  * This test validates that the header for a fixed length record will be skipped during  * marshalling or unmarshalling if 'skipHeader=true' is set in the FixedLengthRecord annotation  */
end_comment

begin_class
DECL|class|BindyFixedLengthHeaderFooterSkipHeaderTest
specifier|public
class|class
name|BindyFixedLengthHeaderFooterSkipHeaderTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|URI_DIRECT_MARSHALL
specifier|public
specifier|static
specifier|final
name|String
name|URI_DIRECT_MARSHALL
init|=
literal|"direct:marshall"
decl_stmt|;
DECL|field|URI_DIRECT_UNMARSHALL
specifier|public
specifier|static
specifier|final
name|String
name|URI_DIRECT_UNMARSHALL
init|=
literal|"direct:unmarshall"
decl_stmt|;
DECL|field|URI_MOCK_MARSHALL_RESULT
specifier|public
specifier|static
specifier|final
name|String
name|URI_MOCK_MARSHALL_RESULT
init|=
literal|"mock:marshall-result"
decl_stmt|;
DECL|field|URI_MOCK_UNMARSHALL_RESULT
specifier|public
specifier|static
specifier|final
name|String
name|URI_MOCK_UNMARSHALL_RESULT
init|=
literal|"mock:unmarshall-result"
decl_stmt|;
DECL|field|TEST_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_HEADER
init|=
literal|"101-08-2009\r\n"
decl_stmt|;
DECL|field|TEST_RECORD
specifier|private
specifier|static
specifier|final
name|String
name|TEST_RECORD
init|=
literal|"10A9  PaulineM    ISINXD12345678BUYShare000002500.45USD01-08-2009\r\n"
decl_stmt|;
DECL|field|TEST_FOOTER
specifier|private
specifier|static
specifier|final
name|String
name|TEST_FOOTER
init|=
literal|"9000000001\r\n"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_MARSHALL_RESULT
argument_list|)
DECL|field|marshallResult
specifier|private
name|MockEndpoint
name|marshallResult
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
name|URI_MOCK_UNMARSHALL_RESULT
argument_list|)
DECL|field|unmarshallResult
specifier|private
name|MockEndpoint
name|unmarshallResult
decl_stmt|;
comment|// *************************************************************************
comment|// TESTS
comment|// *************************************************************************
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testUnmarshallMessage ()
specifier|public
name|void
name|testUnmarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|StringBuffer
name|buff
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buff
operator|.
name|append
argument_list|(
name|TEST_HEADER
argument_list|)
operator|.
name|append
argument_list|(
name|TEST_RECORD
argument_list|)
operator|.
name|append
argument_list|(
name|TEST_FOOTER
argument_list|)
expr_stmt|;
name|unmarshallResult
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
name|URI_DIRECT_UNMARSHALL
argument_list|,
name|buff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|unmarshallResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// check the model
name|Exchange
name|exchange
init|=
name|unmarshallResult
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Order
name|order
init|=
operator|(
name|Order
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|order
operator|.
name|getOrderNr
argument_list|()
argument_list|)
expr_stmt|;
comment|// the field is not trimmed
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"  Pauline"
argument_list|,
name|order
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"M    "
argument_list|,
name|order
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaderMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BindyFixedLengthDataFormat
operator|.
name|CAMEL_BINDY_FIXED_LENGTH_HEADER
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedFooterMap
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|BindyFixedLengthDataFormat
operator|.
name|CAMEL_BINDY_FIXED_LENGTH_FOOTER
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|receivedHeaderMap
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|receivedFooterMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshallMessage ()
specifier|public
name|void
name|testMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Order
name|order
init|=
operator|new
name|Order
argument_list|()
decl_stmt|;
name|order
operator|.
name|setOrderNr
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|order
operator|.
name|setOrderType
argument_list|(
literal|"BUY"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setClientNr
argument_list|(
literal|"A9"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setFirstName
argument_list|(
literal|"Pauline"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setLastName
argument_list|(
literal|"M"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"2500.45"
argument_list|)
argument_list|)
expr_stmt|;
name|order
operator|.
name|setInstrumentCode
argument_list|(
literal|"ISIN"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setInstrumentNumber
argument_list|(
literal|"XD12345678"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setInstrumentType
argument_list|(
literal|"Share"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setCurrency
argument_list|(
literal|"USD"
argument_list|)
expr_stmt|;
name|Calendar
name|calendar
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|set
argument_list|(
literal|2009
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|order
operator|.
name|setOrderDate
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|input
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bodyRow
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|bodyRow
operator|.
name|put
argument_list|(
name|Order
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|input
operator|.
name|add
argument_list|(
name|createHeaderRow
argument_list|()
argument_list|)
expr_stmt|;
name|input
operator|.
name|add
argument_list|(
name|bodyRow
argument_list|)
expr_stmt|;
name|input
operator|.
name|add
argument_list|(
name|createFooterRow
argument_list|()
argument_list|)
expr_stmt|;
name|marshallResult
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|StringBuffer
name|buff
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|buff
operator|.
name|append
argument_list|(
name|TEST_RECORD
argument_list|)
operator|.
name|append
argument_list|(
name|TEST_FOOTER
argument_list|)
expr_stmt|;
name|marshallResult
operator|.
name|expectedBodiesReceived
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|buff
operator|.
name|toString
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|URI_DIRECT_MARSHALL
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|marshallResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|createHeaderRow ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createHeaderRow
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|OrderHeader
name|header
init|=
operator|new
name|OrderHeader
argument_list|()
decl_stmt|;
name|Calendar
name|calendar
init|=
operator|new
name|GregorianCalendar
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|set
argument_list|(
literal|2009
argument_list|,
literal|7
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|header
operator|.
name|setRecordDate
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
name|headerMap
operator|.
name|put
argument_list|(
name|OrderHeader
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|header
argument_list|)
expr_stmt|;
return|return
name|headerMap
return|;
block|}
DECL|method|createFooterRow ()
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createFooterRow
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|footerMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|OrderFooter
name|footer
init|=
operator|new
name|OrderFooter
argument_list|()
decl_stmt|;
name|footer
operator|.
name|setNumberOfRecordsInTheFile
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|footerMap
operator|.
name|put
argument_list|(
name|OrderFooter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|footer
argument_list|)
expr_stmt|;
return|return
name|footerMap
return|;
block|}
comment|// *************************************************************************
comment|// ROUTES
comment|// *************************************************************************
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
name|RouteBuilder
name|routeBuilder
init|=
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
name|from
argument_list|(
name|URI_DIRECT_MARSHALL
argument_list|)
operator|.
name|marshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Fixed
argument_list|,
name|Order
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_MARSHALL_RESULT
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|URI_DIRECT_UNMARSHALL
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Fixed
argument_list|,
name|Order
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_UNMARSHALL_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|routeBuilder
return|;
block|}
block|}
end_class

end_unit

