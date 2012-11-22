begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Set
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
name|impl
operator|.
name|DefaultCamelContext
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
name|jsmpp
operator|.
name|bean
operator|.
name|AlertNotification
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DataSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|DeliverSm
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|NumberingPlanIndicator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|OptionalParameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|OptionalParameter
operator|.
name|OctetString
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|TypeOfNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SMPPSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|util
operator|.
name|DeliveryReceiptState
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppBinding</code>  *   * @version   */
end_comment

begin_class
DECL|class|SmppBindingTest
specifier|public
class|class
name|SmppBindingTest
block|{
DECL|field|binding
specifier|private
name|SmppBinding
name|binding
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|binding
operator|=
operator|new
name|SmppBinding
argument_list|()
block|{
name|Date
name|getCurrentDate
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|(
literal|1251666387000L
argument_list|)
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|emptyConstructorShouldSetTheSmppConfiguration ()
specifier|public
name|void
name|emptyConstructorShouldSetTheSmppConfiguration
parameter_list|()
block|{
name|assertNotNull
argument_list|(
name|binding
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|constructorSmppConfigurationShouldSetTheSmppConfiguration ()
specifier|public
name|void
name|constructorSmppConfigurationShouldSetTheSmppConfiguration
parameter_list|()
block|{
name|SmppConfiguration
name|configuration
init|=
operator|new
name|SmppConfiguration
argument_list|()
decl_stmt|;
name|binding
operator|=
operator|new
name|SmppBinding
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|configuration
argument_list|,
name|binding
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFromAlertNotificationShouldReturnASmppMessage ()
specifier|public
name|void
name|createSmppMessageFromAlertNotificationShouldReturnASmppMessage
parameter_list|()
block|{
name|AlertNotification
name|alertNotification
init|=
operator|new
name|AlertNotification
argument_list|()
decl_stmt|;
name|alertNotification
operator|.
name|setCommandId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setSequenceNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setSourceAddr
argument_list|(
literal|"1616"
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setSourceAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setSourceAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setEsmeAddr
argument_list|(
literal|"1717"
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setEsmeAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|alertNotification
operator|.
name|setEsmeAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|alertNotification
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|smppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|smppMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1616"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1717"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SmppMessageType
operator|.
name|AlertNotification
operator|.
name|toString
argument_list|()
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFromDeliveryReceiptShouldReturnASmppMessage ()
specifier|public
name|void
name|createSmppMessageFromDeliveryReceiptShouldReturnASmppMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|DeliverSm
name|deliverSm
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|deliverSm
operator|.
name|setSmscDeliveryReceipt
argument_list|()
expr_stmt|;
name|deliverSm
operator|.
name|setShortMessage
argument_list|(
literal|"id:2 sub:001 dlvrd:001 submit date:0908312310 done date:0908312311 stat:DELIVRD err:xxx Text:Hello SMPP world!"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello SMPP world!"
argument_list|,
name|smppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DELIVERED
argument_list|)
argument_list|)
expr_stmt|;
comment|// To avoid the test failure when running in different TimeZone
comment|//assertEquals(new Date(1251753060000L), smppMessage.getHeader(SmppConstants.DONE_DATE));
name|assertEquals
argument_list|(
literal|"xxx"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|)
argument_list|)
expr_stmt|;
comment|//assertEquals(new Date(1251753000000L), smppMessage.getHeader(SmppConstants.SUBMIT_DATE));
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SUBMITTED
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DeliveryReceiptState
operator|.
name|DELIVRD
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|FINAL_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SmppMessageType
operator|.
name|DeliveryReceipt
operator|.
name|toString
argument_list|()
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFromDeliverSmShouldReturnASmppMessage ()
specifier|public
name|void
name|createSmppMessageFromDeliverSmShouldReturnASmppMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|DeliverSm
name|deliverSm
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|deliverSm
operator|.
name|setShortMessage
argument_list|(
literal|"Hello SMPP world!"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSequenceNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setCommandId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddr
argument_list|(
literal|"1818"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddress
argument_list|(
literal|"1919"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|INTERNET
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NETWORK_SPECIFIC
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setScheduleDeliveryTime
argument_list|(
literal|"090831230627004+"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setValidityPeriod
argument_list|(
literal|"090901230627004+"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setServiceType
argument_list|(
literal|"WAP"
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello SMPP world!"
argument_list|,
name|smppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|smppMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1818"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1919"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|20
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|3
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"090831230627004+"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SCHEDULE_DELIVERY_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"090901230627004+"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|VALIDITY_PERIOD
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WAP"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SmppMessageType
operator|.
name|DeliverSm
operator|.
name|toString
argument_list|()
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFromDeliverSmWithPayloadInOptionalParameterShouldReturnASmppMessage ()
specifier|public
name|void
name|createSmppMessageFromDeliverSmWithPayloadInOptionalParameterShouldReturnASmppMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|DeliverSm
name|deliverSm
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|deliverSm
operator|.
name|setSequenceNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setCommandId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddr
argument_list|(
literal|"1818"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setSourceAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddress
argument_list|(
literal|"1919"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|INTERNET
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setDestAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NETWORK_SPECIFIC
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setScheduleDeliveryTime
argument_list|(
literal|"090831230627004+"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setValidityPeriod
argument_list|(
literal|"090901230627004+"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setServiceType
argument_list|(
literal|"WAP"
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setOptionalParametes
argument_list|(
operator|new
name|OctetString
argument_list|(
name|OptionalParameter
operator|.
name|Tag
operator|.
name|MESSAGE_PAYLOAD
argument_list|,
literal|"Hello SMPP world!"
argument_list|)
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Hello SMPP world!"
argument_list|,
name|smppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|12
argument_list|,
name|smppMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1818"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1919"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|20
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|3
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"090831230627004+"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SCHEDULE_DELIVERY_TIME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"090901230627004+"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|VALIDITY_PERIOD
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WAP"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SmppMessageType
operator|.
name|DeliverSm
operator|.
name|toString
argument_list|()
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFromDataSmShouldReturnASmppMessage ()
specifier|public
name|void
name|createSmppMessageFromDataSmShouldReturnASmppMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|DataSm
name|dataSm
init|=
operator|new
name|DataSm
argument_list|()
decl_stmt|;
name|dataSm
operator|.
name|setSequenceNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setCommandId
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setCommandStatus
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setSourceAddr
argument_list|(
literal|"1818"
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setSourceAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setSourceAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setDestAddress
argument_list|(
literal|"1919"
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setDestAddrNpi
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setDestAddrTon
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setServiceType
argument_list|(
literal|"WAP"
argument_list|)
expr_stmt|;
name|dataSm
operator|.
name|setRegisteredDelivery
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|dataSm
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|smppMessage
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|smppMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1818"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1919"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|8
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"WAP"
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|REGISTERED_DELIVERY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DATA_CODING
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|SmppMessageType
operator|.
name|DataSm
operator|.
name|toString
argument_list|()
argument_list|,
name|smppMessage
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppMessageFrom8bitDataCodingDeliverSmShouldNotModifyBody ()
specifier|public
name|void
name|createSmppMessageFrom8bitDataCodingDeliverSmShouldNotModifyBody
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|encodings
init|=
name|Charset
operator|.
name|availableCharsets
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
specifier|final
name|byte
index|[]
name|dataCodings
init|=
block|{
operator|(
name|byte
operator|)
literal|0x02
block|,
operator|(
name|byte
operator|)
literal|0x04
block|,
operator|(
name|byte
operator|)
literal|0xF6
block|,
operator|(
name|byte
operator|)
literal|0xF4
block|}
decl_stmt|;
name|byte
index|[]
name|body
init|=
block|{
operator|(
name|byte
operator|)
literal|0xFF
block|,
literal|'A'
block|,
literal|'B'
block|,
operator|(
name|byte
operator|)
literal|0x00
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|,
operator|(
name|byte
operator|)
literal|0x7F
block|,
literal|'C'
block|,
operator|(
name|byte
operator|)
literal|0xFF
block|}
decl_stmt|;
name|DeliverSm
name|deliverSm
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
name|dataCoding
range|:
name|dataCodings
control|)
block|{
name|deliverSm
operator|.
name|setDataCoding
argument_list|(
name|dataCoding
argument_list|)
expr_stmt|;
name|deliverSm
operator|.
name|setShortMessage
argument_list|(
name|body
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|encoding
range|:
name|encodings
control|)
block|{
name|binding
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|SmppMessage
name|smppMessage
init|=
name|binding
operator|.
name|createSmppMessage
argument_list|(
name|deliverSm
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"data coding=0x%02X; encoding=%s"
argument_list|,
name|dataCoding
argument_list|,
name|encoding
argument_list|)
argument_list|,
name|body
argument_list|,
name|smppMessage
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheSetValues ()
specifier|public
name|void
name|getterShouldReturnTheSetValues
parameter_list|()
block|{
name|SmppConfiguration
name|configuration
init|=
operator|new
name|SmppConfiguration
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|configuration
argument_list|,
name|binding
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppSubmitSmCommand ()
specifier|public
name|void
name|createSmppSubmitSmCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppSubmitSmCommand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppSubmitMultiCommand ()
specifier|public
name|void
name|createSmppSubmitMultiCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"SubmitMulti"
argument_list|)
expr_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppSubmitMultiCommand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppDataSmCommand ()
specifier|public
name|void
name|createSmppDataSmCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"DataSm"
argument_list|)
expr_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppDataSmCommand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppReplaceSmCommand ()
specifier|public
name|void
name|createSmppReplaceSmCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"ReplaceSm"
argument_list|)
expr_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppReplaceSmCommand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppQuerySmCommand ()
specifier|public
name|void
name|createSmppQuerySmCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"QuerySm"
argument_list|)
expr_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppQuerySmCommand
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppCancelSmCommand ()
specifier|public
name|void
name|createSmppCancelSmCommand
parameter_list|()
block|{
name|SMPPSession
name|session
init|=
operator|new
name|SMPPSession
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"CancelSm"
argument_list|)
expr_stmt|;
name|SmppCommand
name|command
init|=
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|command
operator|instanceof
name|SmppCancelSmCommand
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

