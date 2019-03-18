begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.classtype
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
name|classtype
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
name|TimeZone
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|simple
operator|.
name|oneclass
operator|.
name|Order
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
name|BindyDataFormat
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
name|Test
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|BindyCsvClassTypeTest
specifier|public
class|class
name|BindyCsvClassTypeTest
extends|extends
name|CamelTestSupport
block|{
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
name|String
name|expected
init|=
literal|"1,B2,Keira,Knightley,ISIN,XX23456789,BUY,Share,400.25,EUR,14-01-2009,17-02-2010 23:21:59\r\n"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:in"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:in"
argument_list|,
name|generateOrder
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|Order
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|data
init|=
literal|"1,B2,Keira,Knightley,ISIN,XX23456789,BUY,Share,400.25,EUR,14-01-2009,16-02-2010 23:21:59\r\n"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:out"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Order
name|order
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|order
operator|.
name|getOrderNr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"BUY"
argument_list|,
name|order
operator|.
name|getOrderType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B2"
argument_list|,
name|order
operator|.
name|getClientNr
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Keira"
argument_list|,
name|order
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Knightley"
argument_list|,
name|order
operator|.
name|getLastName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"400.25"
argument_list|)
argument_list|,
name|order
operator|.
name|getAmount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ISIN"
argument_list|,
name|order
operator|.
name|getInstrumentCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"XX23456789"
argument_list|,
name|order
operator|.
name|getInstrumentNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Share"
argument_list|,
name|order
operator|.
name|getInstrumentType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"EUR"
argument_list|,
name|order
operator|.
name|getCurrency
argument_list|()
argument_list|)
expr_stmt|;
name|Calendar
name|calendar
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
decl_stmt|;
comment|// 4 hour shift
comment|// 16-02-2010 23:21:59 by GMT+4
name|calendar
operator|.
name|set
argument_list|(
literal|2010
argument_list|,
literal|1
argument_list|,
literal|16
argument_list|,
literal|19
argument_list|,
literal|21
argument_list|,
literal|59
argument_list|)
expr_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|,
name|order
operator|.
name|getOrderDateTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|generateOrder ()
specifier|public
name|Order
name|generateOrder
parameter_list|()
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
literal|1
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
literal|"B2"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setFirstName
argument_list|(
literal|"Keira"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setLastName
argument_list|(
literal|"Knightley"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"400.25"
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
literal|"XX23456789"
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
literal|"EUR"
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
literal|0
argument_list|,
literal|14
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
name|calendar
operator|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
expr_stmt|;
comment|// 4 hour shift
comment|// 17-02-2010 23:21:59 by GMT+4
name|calendar
operator|.
name|set
argument_list|(
literal|2010
argument_list|,
literal|1
argument_list|,
literal|17
argument_list|,
literal|19
argument_list|,
literal|21
argument_list|,
literal|59
argument_list|)
expr_stmt|;
name|order
operator|.
name|setOrderDateTime
argument_list|(
name|calendar
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|order
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
name|BindyDataFormat
name|bindy
init|=
operator|new
name|BindyDataFormat
argument_list|()
decl_stmt|;
name|bindy
operator|.
name|setClassType
argument_list|(
name|Order
operator|.
name|class
argument_list|)
expr_stmt|;
name|bindy
operator|.
name|setLocale
argument_list|(
literal|"en"
argument_list|)
expr_stmt|;
name|bindy
operator|.
name|setType
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:in"
argument_list|)
operator|.
name|marshal
argument_list|(
name|bindy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:in"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:out"
argument_list|)
operator|.
name|unmarshal
argument_list|()
operator|.
name|bindy
argument_list|(
name|BindyType
operator|.
name|Csv
argument_list|,
name|Order
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

