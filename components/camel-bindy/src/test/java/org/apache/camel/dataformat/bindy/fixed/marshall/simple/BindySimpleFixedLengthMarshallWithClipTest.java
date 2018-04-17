begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fixed.marshall.simple
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
name|marshall
operator|.
name|simple
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
name|Calendar
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
name|List
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
name|annotation
operator|.
name|DataField
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
name|annotation
operator|.
name|FixedLengthRecord
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
DECL|class|BindySimpleFixedLengthMarshallWithClipTest
specifier|public
class|class
name|BindySimpleFixedLengthMarshallWithClipTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|models
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
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
name|BindyFixedLengthDataFormat
name|bindy
init|=
operator|new
name|BindyFixedLengthDataFormat
argument_list|(
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
name|bindy
operator|.
name|setLocale
argument_list|(
literal|"en"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|bindy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
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
name|String
name|expected
init|=
literal|"10A9Madame deM    ISINXD12345678BUYShare000002500.45USD01-08-2009\r\n"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
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
literal|"direct:start"
argument_list|,
name|generateModel
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|generateModel ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|generateModel
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|modelObjects
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
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
literal|"A98"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setFirstName
argument_list|(
literal|"Madame de Sol"
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
name|modelObjects
operator|.
name|put
argument_list|(
name|order
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|modelObjects
argument_list|)
expr_stmt|;
return|return
name|models
return|;
block|}
annotation|@
name|FixedLengthRecord
argument_list|(
name|length
operator|=
literal|65
argument_list|,
name|paddingChar
operator|=
literal|' '
argument_list|)
DECL|class|Order
specifier|public
specifier|static
class|class
name|Order
block|{
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|1
argument_list|,
name|length
operator|=
literal|2
argument_list|)
DECL|field|orderNr
specifier|private
name|int
name|orderNr
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|3
argument_list|,
name|length
operator|=
literal|2
argument_list|,
name|clip
operator|=
literal|true
argument_list|)
DECL|field|clientNr
specifier|private
name|String
name|clientNr
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|5
argument_list|,
name|length
operator|=
literal|9
argument_list|,
name|clip
operator|=
literal|true
argument_list|)
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|14
argument_list|,
name|length
operator|=
literal|5
argument_list|,
name|align
operator|=
literal|"L"
argument_list|)
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|19
argument_list|,
name|length
operator|=
literal|4
argument_list|)
DECL|field|instrumentCode
specifier|private
name|String
name|instrumentCode
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|23
argument_list|,
name|length
operator|=
literal|10
argument_list|)
DECL|field|instrumentNumber
specifier|private
name|String
name|instrumentNumber
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|33
argument_list|,
name|length
operator|=
literal|3
argument_list|)
DECL|field|orderType
specifier|private
name|String
name|orderType
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|36
argument_list|,
name|length
operator|=
literal|5
argument_list|)
DECL|field|instrumentType
specifier|private
name|String
name|instrumentType
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|41
argument_list|,
name|precision
operator|=
literal|2
argument_list|,
name|length
operator|=
literal|12
argument_list|,
name|paddingChar
operator|=
literal|'0'
argument_list|)
DECL|field|amount
specifier|private
name|BigDecimal
name|amount
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|53
argument_list|,
name|length
operator|=
literal|3
argument_list|)
DECL|field|currency
specifier|private
name|String
name|currency
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|56
argument_list|,
name|length
operator|=
literal|10
argument_list|,
name|pattern
operator|=
literal|"dd-MM-yyyy"
argument_list|)
DECL|field|orderDate
specifier|private
name|Date
name|orderDate
decl_stmt|;
DECL|method|getOrderNr ()
specifier|public
name|int
name|getOrderNr
parameter_list|()
block|{
return|return
name|orderNr
return|;
block|}
DECL|method|setOrderNr (int orderNr)
specifier|public
name|void
name|setOrderNr
parameter_list|(
name|int
name|orderNr
parameter_list|)
block|{
name|this
operator|.
name|orderNr
operator|=
name|orderNr
expr_stmt|;
block|}
DECL|method|getClientNr ()
specifier|public
name|String
name|getClientNr
parameter_list|()
block|{
return|return
name|clientNr
return|;
block|}
DECL|method|setClientNr (String clientNr)
specifier|public
name|void
name|setClientNr
parameter_list|(
name|String
name|clientNr
parameter_list|)
block|{
name|this
operator|.
name|clientNr
operator|=
name|clientNr
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
DECL|method|getInstrumentCode ()
specifier|public
name|String
name|getInstrumentCode
parameter_list|()
block|{
return|return
name|instrumentCode
return|;
block|}
DECL|method|setInstrumentCode (String instrumentCode)
specifier|public
name|void
name|setInstrumentCode
parameter_list|(
name|String
name|instrumentCode
parameter_list|)
block|{
name|this
operator|.
name|instrumentCode
operator|=
name|instrumentCode
expr_stmt|;
block|}
DECL|method|getInstrumentNumber ()
specifier|public
name|String
name|getInstrumentNumber
parameter_list|()
block|{
return|return
name|instrumentNumber
return|;
block|}
DECL|method|setInstrumentNumber (String instrumentNumber)
specifier|public
name|void
name|setInstrumentNumber
parameter_list|(
name|String
name|instrumentNumber
parameter_list|)
block|{
name|this
operator|.
name|instrumentNumber
operator|=
name|instrumentNumber
expr_stmt|;
block|}
DECL|method|getOrderType ()
specifier|public
name|String
name|getOrderType
parameter_list|()
block|{
return|return
name|orderType
return|;
block|}
DECL|method|setOrderType (String orderType)
specifier|public
name|void
name|setOrderType
parameter_list|(
name|String
name|orderType
parameter_list|)
block|{
name|this
operator|.
name|orderType
operator|=
name|orderType
expr_stmt|;
block|}
DECL|method|getInstrumentType ()
specifier|public
name|String
name|getInstrumentType
parameter_list|()
block|{
return|return
name|instrumentType
return|;
block|}
DECL|method|setInstrumentType (String instrumentType)
specifier|public
name|void
name|setInstrumentType
parameter_list|(
name|String
name|instrumentType
parameter_list|)
block|{
name|this
operator|.
name|instrumentType
operator|=
name|instrumentType
expr_stmt|;
block|}
DECL|method|getAmount ()
specifier|public
name|BigDecimal
name|getAmount
parameter_list|()
block|{
return|return
name|amount
return|;
block|}
DECL|method|setAmount (BigDecimal amount)
specifier|public
name|void
name|setAmount
parameter_list|(
name|BigDecimal
name|amount
parameter_list|)
block|{
name|this
operator|.
name|amount
operator|=
name|amount
expr_stmt|;
block|}
DECL|method|getCurrency ()
specifier|public
name|String
name|getCurrency
parameter_list|()
block|{
return|return
name|currency
return|;
block|}
DECL|method|setCurrency (String currency)
specifier|public
name|void
name|setCurrency
parameter_list|(
name|String
name|currency
parameter_list|)
block|{
name|this
operator|.
name|currency
operator|=
name|currency
expr_stmt|;
block|}
DECL|method|getOrderDate ()
specifier|public
name|Date
name|getOrderDate
parameter_list|()
block|{
return|return
name|orderDate
return|;
block|}
DECL|method|setOrderDate (Date orderDate)
specifier|public
name|void
name|setOrderDate
parameter_list|(
name|Date
name|orderDate
parameter_list|)
block|{
name|this
operator|.
name|orderDate
operator|=
name|orderDate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Model : "
operator|+
name|Order
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|this
operator|.
name|orderNr
operator|+
literal|", "
operator|+
name|this
operator|.
name|orderType
operator|+
literal|", "
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|amount
argument_list|)
operator|+
literal|", "
operator|+
name|this
operator|.
name|instrumentCode
operator|+
literal|", "
operator|+
name|this
operator|.
name|instrumentNumber
operator|+
literal|", "
operator|+
name|this
operator|.
name|instrumentType
operator|+
literal|", "
operator|+
name|this
operator|.
name|currency
operator|+
literal|", "
operator|+
name|this
operator|.
name|clientNr
operator|+
literal|", "
operator|+
name|this
operator|.
name|firstName
operator|+
literal|", "
operator|+
name|this
operator|.
name|lastName
operator|+
literal|", "
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|this
operator|.
name|orderDate
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

