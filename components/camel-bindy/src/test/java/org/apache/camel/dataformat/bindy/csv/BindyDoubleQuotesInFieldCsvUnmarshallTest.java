begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
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
name|csv
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
name|Date
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
name|Produce
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
name|ProducerTemplate
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
name|annotation
operator|.
name|CsvRecord
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|BindyDoubleQuotesInFieldCsvUnmarshallTest
specifier|public
class|class
name|BindyDoubleQuotesInFieldCsvUnmarshallTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|URI_MOCK_RESULT
specifier|private
specifier|static
specifier|final
name|String
name|URI_MOCK_RESULT
init|=
literal|"mock:result"
decl_stmt|;
DECL|field|URI_DIRECT_START
specifier|private
specifier|static
specifier|final
name|String
name|URI_DIRECT_START
init|=
literal|"direct:start"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|URI_DIRECT_START
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|expected
specifier|private
name|String
name|expected
decl_stmt|;
annotation|@
name|Test
annotation|@
name|DirtiesContext
DECL|method|testUnMarshallMessage ()
specifier|public
name|void
name|testUnMarshallMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|expected
operator|=
literal|"\"10\",\"A9\",\"Pauline de \"\"Quotes\"\"\",\"M\",\"ISIN\",\"XD12345678\",\"BUY\",\"Share\",\"2500.45\",\"USD\",\"08-01-2009\""
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Order
name|order
init|=
name|result
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Pauline de \"Quotes\""
argument_list|,
name|order
operator|.
name|getFirstName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
DECL|field|camelDataFormat
name|BindyCsvDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|URI_DIRECT_START
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|camelDataFormat
argument_list|)
operator|.
name|to
argument_list|(
name|URI_MOCK_RESULT
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|CsvRecord
argument_list|(
name|separator
operator|=
literal|","
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
literal|2
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
literal|3
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
literal|4
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
literal|5
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
literal|6
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
literal|7
argument_list|)
DECL|field|orderType
specifier|private
name|String
name|orderType
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|name
operator|=
literal|"Name"
argument_list|,
name|pos
operator|=
literal|8
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
literal|9
argument_list|,
name|precision
operator|=
literal|2
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
literal|10
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
literal|11
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

