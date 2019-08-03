begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fixed.unmarshall.simple.method
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
name|unmarshall
operator|.
name|simple
operator|.
name|method
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
DECL|class|BindySimpleFixedLengthUnmarshallClassMethodTest
specifier|public
class|class
name|BindySimpleFixedLengthUnmarshallClassMethodTest
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
literal|"10A9  PaulineM    ISINXD12345678BUYShare000002500.45USD01-08-2009Hello     "
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
comment|// check the model
name|BindySimpleFixedLengthUnmarshallClassMethodTest
operator|.
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
name|BindySimpleFixedLengthUnmarshallClassMethodTest
operator|.
name|Order
operator|.
name|class
argument_list|)
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
literal|"  PAULINE"
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
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Hello     "
argument_list|,
name|order
operator|.
name|getComment
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
name|BindyFixedLengthDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyFixedLengthDataFormat
argument_list|(
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
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
name|FixedLengthRecord
argument_list|(
name|length
operator|=
literal|75
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
name|method
operator|=
literal|"toUpperCase"
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
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|66
argument_list|,
name|length
operator|=
literal|10
argument_list|)
DECL|field|comment
specifier|private
name|String
name|comment
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
DECL|method|getComment ()
specifier|public
name|String
name|getComment
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
DECL|method|setComment (String comment)
specifier|public
name|void
name|setComment
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
name|this
operator|.
name|comment
operator|=
name|comment
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

