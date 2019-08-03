begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.model.date
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
name|model
operator|.
name|date
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalTime
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
name|Format
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
name|FormattingOptions
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
name|FormatFactories
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
name|csv
operator|.
name|BindyCsvDataFormat
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
name|format
operator|.
name|factories
operator|.
name|AbstractFormatFactory
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
DECL|class|BindyDatePatternCsvUnmarshallTest
specifier|public
class|class
name|BindyDatePatternCsvUnmarshallTest
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
literal|"10,Christian,Mueller,12-24-2013,12-26-2015,01-06-2016 12:14:49,13:15:01,broken"
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|expected
operator|+
literal|"\r\n"
argument_list|)
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
name|assertIsSatisfied
argument_list|()
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
name|marshal
argument_list|(
name|camelDataFormat
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
comment|// because the marshaler will return an OutputStream
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
annotation|@
name|FormatFactories
argument_list|(
block|{
name|OrderNumberFormatFactory
operator|.
name|class
block|}
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
name|OrderNumber
name|orderNr
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|2
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
literal|3
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
literal|4
argument_list|,
name|pattern
operator|=
literal|"MM-dd-yyyy"
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
literal|5
argument_list|,
name|pattern
operator|=
literal|"MM-dd-yyyy"
argument_list|)
DECL|field|deliveryDate
specifier|private
name|LocalDate
name|deliveryDate
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|6
argument_list|,
name|pattern
operator|=
literal|"MM-dd-yyyy HH:mm:ss"
argument_list|)
DECL|field|returnedDateTime
specifier|private
name|LocalDateTime
name|returnedDateTime
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|7
argument_list|,
name|pattern
operator|=
literal|"HH:mm:ss"
argument_list|)
DECL|field|receivedTime
specifier|private
name|LocalTime
name|receivedTime
decl_stmt|;
annotation|@
name|DataField
argument_list|(
name|pos
operator|=
literal|8
argument_list|)
DECL|field|returnReason
specifier|private
name|ReturnReason
name|returnReason
decl_stmt|;
DECL|method|getOrderNr ()
specifier|public
name|OrderNumber
name|getOrderNr
parameter_list|()
block|{
return|return
name|orderNr
return|;
block|}
DECL|method|setOrderNr (OrderNumber orderNr)
specifier|public
name|void
name|setOrderNr
parameter_list|(
name|OrderNumber
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
DECL|method|getDeliveryDate ()
specifier|public
name|LocalDate
name|getDeliveryDate
parameter_list|()
block|{
return|return
name|deliveryDate
return|;
block|}
DECL|method|setDeliveryDate (LocalDate deliveryDate)
specifier|public
name|void
name|setDeliveryDate
parameter_list|(
name|LocalDate
name|deliveryDate
parameter_list|)
block|{
name|this
operator|.
name|deliveryDate
operator|=
name|deliveryDate
expr_stmt|;
block|}
DECL|method|getReturnedDateTime ()
specifier|public
name|LocalDateTime
name|getReturnedDateTime
parameter_list|()
block|{
return|return
name|returnedDateTime
return|;
block|}
DECL|method|setReturnedDateTime (LocalDateTime returnedDateTime)
specifier|public
name|void
name|setReturnedDateTime
parameter_list|(
name|LocalDateTime
name|returnedDateTime
parameter_list|)
block|{
name|this
operator|.
name|returnedDateTime
operator|=
name|returnedDateTime
expr_stmt|;
block|}
DECL|method|getReceivedTime ()
specifier|public
name|LocalTime
name|getReceivedTime
parameter_list|()
block|{
return|return
name|receivedTime
return|;
block|}
DECL|method|setReceivedTime (LocalTime receivedTime)
specifier|public
name|void
name|setReceivedTime
parameter_list|(
name|LocalTime
name|receivedTime
parameter_list|)
block|{
name|this
operator|.
name|receivedTime
operator|=
name|receivedTime
expr_stmt|;
block|}
DECL|method|getReturnReason ()
specifier|public
name|ReturnReason
name|getReturnReason
parameter_list|()
block|{
return|return
name|returnReason
return|;
block|}
DECL|method|setReturnReason (ReturnReason returnReason)
specifier|public
name|void
name|setReturnReason
parameter_list|(
name|ReturnReason
name|returnReason
parameter_list|)
block|{
name|this
operator|.
name|returnReason
operator|=
name|returnReason
expr_stmt|;
block|}
block|}
DECL|enum|ReturnReason
specifier|public
enum|enum
name|ReturnReason
block|{
DECL|enumConstant|broken
name|broken
block|,
DECL|enumConstant|other
name|other
block|}
DECL|class|OrderNumber
specifier|public
specifier|static
class|class
name|OrderNumber
block|{
DECL|field|orderNr
specifier|private
name|int
name|orderNr
decl_stmt|;
DECL|method|ofString (String orderNumber)
specifier|public
specifier|static
name|OrderNumber
name|ofString
parameter_list|(
name|String
name|orderNumber
parameter_list|)
block|{
name|OrderNumber
name|result
init|=
operator|new
name|OrderNumber
argument_list|()
decl_stmt|;
name|result
operator|.
name|orderNr
operator|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|orderNumber
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
DECL|class|OrderNumberFormatFactory
specifier|public
specifier|static
class|class
name|OrderNumberFormatFactory
extends|extends
name|AbstractFormatFactory
block|{
block|{
name|supportedClasses
operator|.
name|add
parameter_list|(
name|OrderNumber
operator|.
name|class
parameter_list|)
constructor_decl|;
block|}
annotation|@
name|Override
DECL|method|build (FormattingOptions formattingOptions)
specifier|public
name|Format
argument_list|<
name|?
argument_list|>
name|build
parameter_list|(
name|FormattingOptions
name|formattingOptions
parameter_list|)
block|{
return|return
operator|new
name|Format
argument_list|<
name|OrderNumber
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|format
parameter_list|(
name|OrderNumber
name|object
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|object
operator|.
name|orderNr
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|OrderNumber
name|parse
parameter_list|(
name|String
name|string
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|OrderNumber
operator|.
name|ofString
argument_list|(
name|string
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
block|}
end_class

end_unit

