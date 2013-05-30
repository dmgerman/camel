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
name|util
operator|.
name|LinkedHashMap
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
name|ExchangePattern
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
name|DataCoding
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
name|ESMClass
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
name|Tag
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
name|RegisteredDelivery
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
name|SMSCDeliveryReceipt
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
name|DataSmResult
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
name|MessageId
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
name|Before
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

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|assertNull
import|;
end_import

begin_class
DECL|class|SmppDataSmCommandTest
specifier|public
class|class
name|SmppDataSmCommandTest
block|{
DECL|field|defaultTimeZone
specifier|private
specifier|static
name|TimeZone
name|defaultTimeZone
decl_stmt|;
DECL|field|session
specifier|private
name|SMPPSession
name|session
decl_stmt|;
DECL|field|config
specifier|private
name|SmppConfiguration
name|config
decl_stmt|;
DECL|field|command
specifier|private
name|SmppDataSmCommand
name|command
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpBeforeClass ()
specifier|public
specifier|static
name|void
name|setUpBeforeClass
parameter_list|()
block|{
name|defaultTimeZone
operator|=
name|TimeZone
operator|.
name|getDefault
argument_list|()
expr_stmt|;
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|TimeZone
operator|.
name|getTimeZone
argument_list|(
literal|"GMT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|tearDownAfterClass ()
specifier|public
specifier|static
name|void
name|tearDownAfterClass
parameter_list|()
block|{
if|if
condition|(
name|defaultTimeZone
operator|!=
literal|null
condition|)
block|{
name|TimeZone
operator|.
name|setDefault
argument_list|(
name|defaultTimeZone
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|session
operator|=
name|createMock
argument_list|(
name|SMPPSession
operator|.
name|class
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
name|command
operator|=
operator|new
name|SmppDataSmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeWithConfigurationData ()
specifier|public
name|void
name|executeWithConfigurationData
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
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
name|expect
argument_list|(
name|session
operator|.
name|dataShortMessage
argument_list|(
name|eq
argument_list|(
literal|"CMT"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1616"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1717"
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ESMClass
argument_list|()
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
name|DataCoding
operator|.
name|newInstance
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|DataSmResult
argument_list|(
operator|new
name|MessageId
argument_list|(
literal|"1"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|,
literal|"XXX"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|,
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|,
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
literal|"1818"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|,
name|TypeOfNumber
operator|.
name|INTERNATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|,
name|NumberingPlanIndicator
operator|.
name|INTERNET
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
literal|"1919"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|REGISTERED_DELIVERY
argument_list|,
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS
argument_list|)
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|dataShortMessage
argument_list|(
name|eq
argument_list|(
literal|"XXX"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|NATIONAL
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|NATIONAL
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1818"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|INTERNATIONAL
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|INTERNET
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1919"
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ESMClass
argument_list|()
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
operator|(
name|byte
operator|)
literal|2
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
name|DataCoding
operator|.
name|newInstance
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|DataSmResult
argument_list|(
operator|new
name|MessageId
argument_list|(
literal|"1"
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETERS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|executeWithOptionalParameter ()
specifier|public
name|void
name|executeWithOptionalParameter
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
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
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|optionalParameters
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"SOURCE_SUBADDRESS"
argument_list|,
literal|"1292"
argument_list|)
expr_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"ADDITIONAL_STATUS_INFO_TEXT"
argument_list|,
literal|"urgent"
argument_list|)
expr_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"DEST_ADDR_SUBUNIT"
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"DEST_TELEMATICS_ID"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"QOS_TIME_TO_LIVE"
argument_list|,
literal|"3600000"
argument_list|)
expr_stmt|;
name|optionalParameters
operator|.
name|put
argument_list|(
literal|"ALERT_ON_MESSAGE_DELIVERY"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETERS
argument_list|,
name|optionalParameters
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|session
operator|.
name|dataShortMessage
argument_list|(
name|eq
argument_list|(
literal|"CMT"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1616"
argument_list|)
argument_list|,
name|eq
argument_list|(
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"1717"
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ESMClass
argument_list|()
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
operator|(
name|byte
operator|)
literal|1
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
name|DataCoding
operator|.
name|newInstance
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|OctetString
argument_list|(
name|Tag
operator|.
name|SOURCE_SUBADDRESS
argument_list|,
literal|"1292"
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|COctetString
argument_list|(
name|Tag
operator|.
name|ADDITIONAL_STATUS_INFO_TEXT
operator|.
name|code
argument_list|()
argument_list|,
literal|"urgent"
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|Byte
argument_list|(
name|Tag
operator|.
name|DEST_ADDR_SUBUNIT
argument_list|,
operator|(
name|byte
operator|)
literal|4
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|Short
argument_list|(
name|Tag
operator|.
name|DEST_TELEMATICS_ID
operator|.
name|code
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|Int
argument_list|(
name|Tag
operator|.
name|QOS_TIME_TO_LIVE
argument_list|,
literal|3600000
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|OptionalParameter
operator|.
name|Null
argument_list|(
name|Tag
operator|.
name|ALERT_ON_MESSAGE_DELIVERY
argument_list|)
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|DataSmResult
argument_list|(
operator|new
name|MessageId
argument_list|(
literal|"1"
argument_list|)
argument_list|,
operator|new
name|OptionalParameter
index|[]
block|{
operator|new
name|OptionalParameter
operator|.
name|OctetString
argument_list|(
name|Tag
operator|.
name|SOURCE_SUBADDRESS
argument_list|,
literal|"1292"
argument_list|)
block|,
operator|new
name|OptionalParameter
operator|.
name|COctetString
argument_list|(
name|Tag
operator|.
name|ADDITIONAL_STATUS_INFO_TEXT
operator|.
name|code
argument_list|()
argument_list|,
literal|"urgent"
argument_list|)
block|,
operator|new
name|OptionalParameter
operator|.
name|Byte
argument_list|(
name|Tag
operator|.
name|DEST_ADDR_SUBUNIT
argument_list|,
operator|(
name|byte
operator|)
literal|4
argument_list|)
block|,
operator|new
name|OptionalParameter
operator|.
name|Short
argument_list|(
name|Tag
operator|.
name|DEST_TELEMATICS_ID
operator|.
name|code
argument_list|()
argument_list|,
operator|(
name|short
operator|)
literal|2
argument_list|)
block|,
operator|new
name|OptionalParameter
operator|.
name|Int
argument_list|(
name|Tag
operator|.
name|QOS_TIME_TO_LIVE
argument_list|,
literal|3600000
argument_list|)
block|,
operator|new
name|OptionalParameter
operator|.
name|Null
argument_list|(
name|Tag
operator|.
name|ALERT_ON_MESSAGE_DELIVERY
argument_list|)
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|optParamMap
init|=
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETERS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|optParamMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1292"
argument_list|,
name|optParamMap
operator|.
name|get
argument_list|(
literal|"SOURCE_SUBADDRESS"
argument_list|)
argument_list|)
expr_stmt|;
comment|// FIXME: fix required in JSMPP. See http://code.google.com/p/jsmpp/issues/detail?id=140
comment|//assertEquals("urgent", optParamMap.get("ADDITIONAL_STATUS_INFO_TEXT"));
name|assertEquals
argument_list|(
literal|"4"
argument_list|,
name|optParamMap
operator|.
name|get
argument_list|(
literal|"DEST_ADDR_SUBUNIT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|optParamMap
operator|.
name|get
argument_list|(
literal|"DEST_TELEMATICS_ID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3600000"
argument_list|,
name|optParamMap
operator|.
name|get
argument_list|(
literal|"QOS_TIME_TO_LIVE"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|optParamMap
operator|.
name|get
argument_list|(
literal|"ALERT_ON_MESSAGE_DELIVERY"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

