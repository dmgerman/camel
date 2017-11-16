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
name|Date
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
name|Alphabet
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
name|SMPPSession
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
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|isNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_class
DECL|class|SmppReplaceSmCommandTest
specifier|public
class|class
name|SmppReplaceSmCommandTest
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
name|SmppReplaceSmCommand
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
name|mock
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
name|SmppReplaceSmCommand
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
literal|"ReplaceSm"
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
name|ID
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"new short message body"
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
operator|.
name|replaceShortMessage
argument_list|(
name|eq
argument_list|(
literal|"1"
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"new short message body"
operator|.
name|getBytes
argument_list|()
argument_list|)
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
literal|"ReplaceSm"
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
name|ID
argument_list|,
literal|"1"
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
name|SCHEDULE_DELIVERY_TIME
argument_list|,
operator|new
name|Date
argument_list|(
literal|1111111
argument_list|)
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
name|VALIDITY_PERIOD
argument_list|,
operator|new
name|Date
argument_list|(
literal|2222222
argument_list|)
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
name|FAILURE
argument_list|)
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
name|setBody
argument_list|(
literal|"new short message body"
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
operator|.
name|replaceShortMessage
argument_list|(
name|eq
argument_list|(
literal|"1"
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
literal|"-300101001831100+"
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"-300101003702200+"
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"new short message body"
operator|.
name|getBytes
argument_list|()
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|executeWithValidityPeriodAsString ()
specifier|public
name|void
name|executeWithValidityPeriodAsString
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
literal|"ReplaceSm"
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
name|ID
argument_list|,
literal|"1"
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
name|SCHEDULE_DELIVERY_TIME
argument_list|,
operator|new
name|Date
argument_list|(
literal|1111111
argument_list|)
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
name|VALIDITY_PERIOD
argument_list|,
literal|"000003000000000R"
argument_list|)
expr_stmt|;
comment|// three days
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
name|FAILURE
argument_list|)
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
name|setBody
argument_list|(
literal|"new short message body"
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
operator|.
name|replaceShortMessage
argument_list|(
name|eq
argument_list|(
literal|"1"
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
literal|"-300101001831100+"
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"000003000000000R"
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
literal|"new short message body"
operator|.
name|getBytes
argument_list|()
argument_list|)
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
block|}
annotation|@
name|Test
DECL|method|bodyWithSmscDefaultDataCodingNarrowedToCharset ()
specifier|public
name|void
name|bodyWithSmscDefaultDataCodingNarrowedToCharset
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|dataCoding
init|=
literal|0x00
decl_stmt|;
comment|/* SMSC-default */
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
name|byte
index|[]
name|bodyNarrowed
init|=
block|{
literal|'?'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'\0'
block|,
literal|'?'
block|,
operator|(
name|byte
operator|)
literal|0x7F
block|,
literal|'C'
block|,
literal|'?'
block|}
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
literal|"ReplaceSm"
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
name|DATA_CODING
argument_list|,
name|dataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|bodyNarrowed
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bodyWithLatin1DataCodingNarrowedToCharset ()
specifier|public
name|void
name|bodyWithLatin1DataCodingNarrowedToCharset
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|dataCoding
init|=
literal|0x03
decl_stmt|;
comment|/* ISO-8859-1 (Latin1) */
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
name|byte
index|[]
name|bodyNarrowed
init|=
block|{
literal|'?'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'\0'
block|,
literal|'?'
block|,
operator|(
name|byte
operator|)
literal|0x7F
block|,
literal|'C'
block|,
literal|'?'
block|}
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
literal|"ReplaceSm"
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
name|DATA_CODING
argument_list|,
name|dataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|bodyNarrowed
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bodyWithSMPP8bitDataCodingNotModified ()
specifier|public
name|void
name|bodyWithSMPP8bitDataCodingNotModified
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|dataCoding
init|=
literal|0x04
decl_stmt|;
comment|/* SMPP 8-bit */
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
literal|"ReplaceSm"
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
name|DATA_CODING
argument_list|,
name|dataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|bodyWithGSM8bitDataCodingNotModified ()
specifier|public
name|void
name|bodyWithGSM8bitDataCodingNotModified
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|dataCoding
init|=
literal|0xF7
decl_stmt|;
comment|/* GSM 8-bit class 3 */
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
literal|"ReplaceSm"
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
name|DATA_CODING
argument_list|,
name|dataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|eightBitDataCodingOverridesDefaultAlphabet ()
specifier|public
name|void
name|eightBitDataCodingOverridesDefaultAlphabet
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|binDataCoding
init|=
literal|0xF7
decl_stmt|;
comment|/* GSM 8-bit class 3 */
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
literal|"ReplaceSm"
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
name|ALPHABET
argument_list|,
name|Alphabet
operator|.
name|ALPHA_DEFAULT
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
name|DATA_CODING
argument_list|,
name|binDataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|latin1DataCodingOverridesEightBitAlphabet ()
specifier|public
name|void
name|latin1DataCodingOverridesEightBitAlphabet
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|latin1DataCoding
init|=
literal|0x03
decl_stmt|;
comment|/* ISO-8859-1 (Latin1) */
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
name|byte
index|[]
name|bodyNarrowed
init|=
block|{
literal|'?'
block|,
literal|'A'
block|,
literal|'B'
block|,
literal|'\0'
block|,
literal|'?'
block|,
operator|(
name|byte
operator|)
literal|0x7F
block|,
literal|'C'
block|,
literal|'?'
block|}
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
literal|"ReplaceSm"
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
name|ALPHABET
argument_list|,
name|Alphabet
operator|.
name|ALPHA_8_BIT
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
name|DATA_CODING
argument_list|,
name|latin1DataCoding
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
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
operator|.
name|replaceShortMessage
argument_list|(
operator|(
name|String
operator|)
name|isNull
argument_list|()
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
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|isNull
argument_list|()
argument_list|,
name|eq
argument_list|(
operator|new
name|RegisteredDelivery
argument_list|(
name|SMSCDeliveryReceipt
operator|.
name|SUCCESS_FAILURE
argument_list|)
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|,
name|eq
argument_list|(
name|bodyNarrowed
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

