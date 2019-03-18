begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|endsWith
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|startsWith
import|;
end_import

begin_class
DECL|class|MllpTcpServerConsumerAutoAcknowledgementWithoutBridgeErrorHandlerTest
specifier|public
class|class
name|MllpTcpServerConsumerAutoAcknowledgementWithoutBridgeErrorHandlerTest
extends|extends
name|TcpServerConsumerAcknowledgementTestSupport
block|{
annotation|@
name|Override
DECL|method|isBridgeErrorHandler ()
specifier|protected
name|boolean
name|isBridgeErrorHandler
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|isAutoAck ()
specifier|protected
name|boolean
name|isAutoAck
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testReceiveSingleMessage ()
specifier|public
name|void
name|testReceiveSingleMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedHeaderReceived
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_TYPE
argument_list|,
literal|"AA"
argument_list|)
expr_stmt|;
name|receiveSingleMessage
argument_list|()
expr_stmt|;
name|Exchange
name|completeExchange
init|=
name|complete
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|completeExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|completeExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_STRING
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|acknowledgement
init|=
name|completeExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_STRING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|acknowledgement
argument_list|,
name|startsWith
argument_list|(
literal|"MSH|^~\\&|^org^sys||APP_A|FAC_A|"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|acknowledgement
argument_list|,
name|endsWith
argument_list|(
literal|"||ACK^A04^ADT_A04|||2.6\rMSA|AA|\r"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAcknowledgementDeliveryFailure ()
specifier|public
name|void
name|testAcknowledgementDeliveryFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedBodiesReceived
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedHeaderReceived
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_TYPE
argument_list|,
literal|"AA"
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedHeaderReceived
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT
argument_list|,
name|EXPECTED_ACKNOWLEDGEMENT
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedHeaderReceived
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_STRING
argument_list|,
name|EXPECTED_ACKNOWLEDGEMENT
argument_list|)
expr_stmt|;
name|acknowledgementDeliveryFailure
argument_list|()
expr_stmt|;
name|Exchange
name|failureExchange
init|=
name|failure
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Object
name|failureException
init|=
name|failureExchange
operator|.
name|getProperty
argument_list|(
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_EXCEPTION
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"OnFailureOnly exchange should have a "
operator|+
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_EXCEPTION
operator|+
literal|" property"
argument_list|,
name|failureException
argument_list|)
expr_stmt|;
name|assertIsInstanceOf
argument_list|(
name|Exception
operator|.
name|class
argument_list|,
name|failureException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnparsableMessage ()
specifier|public
name|void
name|testUnparsableMessage
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|testMessage
init|=
literal|"MSH"
operator|+
name|TEST_MESSAGE
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|unparsableMessage
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
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
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
name|complete
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageWithEmptySegment ()
specifier|public
name|void
name|testMessageWithEmptySegment
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|testMessage
init|=
name|TEST_MESSAGE
operator|.
name|replace
argument_list|(
literal|"\rPID|"
argument_list|,
literal|"\r\rPID|"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|unparsableMessage
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
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
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
name|complete
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageWithEmbeddedNewlines ()
specifier|public
name|void
name|testMessageWithEmbeddedNewlines
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|testMessage
init|=
name|TEST_MESSAGE
operator|.
name|replace
argument_list|(
literal|"\rPID|"
argument_list|,
literal|"\r\n\rPID|\n"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|unparsableMessage
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
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
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Should not have the exception in the exchange property"
argument_list|,
name|complete
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

