begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
package|;
end_package

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
name|CamelContext
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
name|Message
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
name|Processor
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
name|apache
operator|.
name|http
operator|.
name|StatusLine
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
DECL|class|HipchatComponentProducerTest
specifier|public
class|class
name|HipchatComponentProducerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
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
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|callback
specifier|private
name|PostCallback
name|callback
init|=
operator|new
name|PostCallback
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|sendInOnly ()
specifier|public
name|void
name|sendInOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|,
literal|"CamelUnitTestUser"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my unit test message."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertCommonResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertNullExchangeHeader
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertResponseMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOut ()
specifier|public
name|void
name|sendInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|,
literal|"CamelUnitTestUser"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|,
literal|"CamelUnitTestBkColor"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|,
literal|"CamelUnitTestFormat"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|,
literal|"CamelUnitTestNotify"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my unit test message."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertCommonResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertRemainingResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertResponseMessage
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOutRoomOnly ()
specifier|public
name|void
name|sendInOutRoomOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|,
literal|"CamelUnitTestBkColor"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|,
literal|"CamelUnitTestFormat"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|,
literal|"CamelUnitTestNotify"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my unit test message."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|resultExchange
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my unit test message."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTest"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertRemainingResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|,
name|StatusLine
operator|.
name|class
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|callback
operator|.
name|called
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my unit test message."
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestBkColor"
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_COLOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestFormat"
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_FORMAT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestNotify"
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_NOTIFY
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sendInOutUserOnly ()
specifier|public
name|void
name|sendInOutUserOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|,
literal|"CamelUnitTest"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|,
literal|"CamelUnitTestBkColor"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|,
literal|"CamelUnitTestFormat"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|,
literal|"CamelUnitTestNotify"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is my unit test message."
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|resultExchange
init|=
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my unit test message."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTest"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertRemainingResultExchange
argument_list|(
name|result
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|,
name|StatusLine
operator|.
name|class
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|callback
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|callback
operator|.
name|called
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my unit test message."
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_COLOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestFormat"
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_FORMAT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestNotify"
argument_list|,
name|callback
operator|.
name|called
operator|.
name|get
argument_list|(
name|HipchatApiConstants
operator|.
name|API_MESSAGE_NOTIFY
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertNullExchangeHeader (Exchange resultExchange)
specifier|private
name|void
name|assertNullExchangeHeader
parameter_list|(
name|Exchange
name|resultExchange
parameter_list|)
block|{
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|FROM_USER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRemainingResultExchange (Exchange resultExchange)
specifier|private
name|void
name|assertRemainingResultExchange
parameter_list|(
name|Exchange
name|resultExchange
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"CamelUnitTestBkColor"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_BACKGROUND_COLOR
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestFormat"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|MESSAGE_FORMAT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestNotify"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TRIGGER_NOTIFY
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|assertResponseMessage (Message message)
specifier|private
name|void
name|assertResponseMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|,
name|StatusLine
operator|.
name|class
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|204
argument_list|,
name|message
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|,
name|StatusLine
operator|.
name|class
argument_list|)
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertCommonResultExchange (Exchange resultExchange)
specifier|private
name|void
name|assertCommonResultExchange
parameter_list|(
name|Exchange
name|resultExchange
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"This is my unit test message."
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTest"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelUnitTestUser"
argument_list|,
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_USER_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|resultExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HipchatConstants
operator|.
name|TO_ROOM_RESPONSE_STATUS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|HipchatComponent
name|component
init|=
operator|new
name|HipchatComponent
argument_list|(
name|context
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|HipchatEndpoint
name|getHipchatEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|HipchatEPSuccessTestSupport
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|callback
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"hipchat"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|context
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"hipchat:http:api.hipchat.com?authToken=anything"
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
DECL|class|PostCallback
specifier|public
specifier|static
class|class
name|PostCallback
block|{
DECL|field|called
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|called
decl_stmt|;
DECL|method|call (Map<String, String> postParam)
specifier|public
name|void
name|call
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|postParam
parameter_list|)
block|{
name|this
operator|.
name|called
operator|=
name|postParam
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

