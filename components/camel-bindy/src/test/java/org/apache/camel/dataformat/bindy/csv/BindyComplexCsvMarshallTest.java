begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|model
operator|.
name|complex
operator|.
name|twoclassesandonelink
operator|.
name|Client
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
name|complex
operator|.
name|twoclassesandonelink
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|complex
operator|.
name|twoclassesandonelink
operator|.
name|Security
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
DECL|class|BindyComplexCsvMarshallTest
specifier|public
class|class
name|BindyComplexCsvMarshallTest
extends|extends
name|AbstractJUnit4SpringContextTests
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
DECL|field|result
specifier|private
name|String
name|result
init|=
literal|"10,A1,Julia,Roberts,ISIN,LU123456789,BUY,Share,150.00,USD,14-01-2009\r\n"
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|private
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|private
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
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
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|generateModel
argument_list|()
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|generateModel ()
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
name|generateModel
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
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
name|setAmount
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"150"
argument_list|)
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
name|Client
name|client
init|=
operator|new
name|Client
argument_list|()
decl_stmt|;
name|client
operator|.
name|setClientNr
argument_list|(
literal|"A1"
argument_list|)
expr_stmt|;
name|client
operator|.
name|setFirstName
argument_list|(
literal|"Julia"
argument_list|)
expr_stmt|;
name|client
operator|.
name|setLastName
argument_list|(
literal|"Roberts"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setClient
argument_list|(
name|client
argument_list|)
expr_stmt|;
name|Security
name|security
init|=
operator|new
name|Security
argument_list|()
decl_stmt|;
name|security
operator|.
name|setInstrumentCode
argument_list|(
literal|"ISIN"
argument_list|)
expr_stmt|;
name|security
operator|.
name|setInstrumentNumber
argument_list|(
literal|"LU123456789"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSecurity
argument_list|(
name|security
argument_list|)
expr_stmt|;
name|model
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
name|model
operator|.
name|put
argument_list|(
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|model
operator|.
name|put
argument_list|(
name|security
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|security
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|model
argument_list|)
expr_stmt|;
return|return
name|models
return|;
block|}
DECL|class|ContextConfig
specifier|public
specifier|static
class|class
name|ContextConfig
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|BindyCsvDataFormat
name|camelDataFormat
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
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
name|complex
operator|.
name|twoclassesandonelink
operator|.
name|Order
operator|.
name|class
argument_list|)
decl_stmt|;
name|camelDataFormat
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
name|camelDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

