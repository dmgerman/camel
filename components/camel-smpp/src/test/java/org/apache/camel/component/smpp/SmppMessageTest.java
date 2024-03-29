begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
operator|.
name|DefaultCamelContext
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
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppMessage</code>  */
end_comment

begin_class
DECL|class|SmppMessageTest
specifier|public
class|class
name|SmppMessageTest
block|{
DECL|field|message
specifier|private
name|SmppMessage
name|message
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|emptyConstructorShouldReturnAnInstanceWithoutACommand ()
specifier|public
name|void
name|emptyConstructorShouldReturnAnInstanceWithoutACommand
parameter_list|()
block|{
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|getCommand
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|alertNotificationConstructorShouldReturnAnInstanceWithACommandAndHeaderAttributes ()
specifier|public
name|void
name|alertNotificationConstructorShouldReturnAnInstanceWithACommandAndHeaderAttributes
parameter_list|()
block|{
name|AlertNotification
name|command
init|=
operator|new
name|AlertNotification
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getCommand
argument_list|()
operator|instanceof
name|AlertNotification
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|isAlertNotification
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSmppMessageDataSm ()
specifier|public
name|void
name|testSmppMessageDataSm
parameter_list|()
block|{
name|DataSm
name|command
init|=
operator|new
name|DataSm
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getCommand
argument_list|()
operator|instanceof
name|DataSm
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|isDataSm
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSmppMessageDeliverSm ()
specifier|public
name|void
name|testSmppMessageDeliverSm
parameter_list|()
block|{
name|DeliverSm
name|command
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getCommand
argument_list|()
operator|instanceof
name|DeliverSm
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|isDeliverSm
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSmppMessageDeliverReceipt ()
specifier|public
name|void
name|testSmppMessageDeliverReceipt
parameter_list|()
block|{
name|DeliverSm
name|command
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|command
operator|.
name|setSmscDeliveryReceipt
argument_list|()
expr_stmt|;
name|command
operator|.
name|setShortMessage
argument_list|(
literal|"id:2 sub:001 dlvrd:001 submit date:0908312310 done date:0908312311 stat:DELIVRD err:xxx Text:Hello SMPP world!"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getCommand
argument_list|()
operator|instanceof
name|DeliverSm
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|isDeliveryReceipt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|newInstanceShouldReturnAnInstanceWithoutACommand ()
specifier|public
name|void
name|newInstanceShouldReturnAnInstanceWithoutACommand
parameter_list|()
block|{
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|SmppMessage
name|msg
init|=
name|message
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getCommand
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBodyShouldNotMangle8bitDataCodingShortMessage ()
specifier|public
name|void
name|createBodyShouldNotMangle8bitDataCodingShortMessage
parameter_list|()
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
name|command
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|SmppConfiguration
name|config
init|=
operator|new
name|SmppConfiguration
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
name|command
operator|.
name|setDataCoding
argument_list|(
name|dataCoding
argument_list|)
expr_stmt|;
name|command
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
name|config
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
name|config
argument_list|)
expr_stmt|;
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
operator|(
name|byte
index|[]
operator|)
name|message
operator|.
name|createBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|createBodyShouldReturnNullIfTheCommandIsNull ()
specifier|public
name|void
name|createBodyShouldReturnNullIfTheCommandIsNull
parameter_list|()
block|{
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|createBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBodyShouldReturnNullIfTheCommandIsNotAMessageRequest ()
specifier|public
name|void
name|createBodyShouldReturnNullIfTheCommandIsNotAMessageRequest
parameter_list|()
block|{
name|AlertNotification
name|command
init|=
operator|new
name|AlertNotification
argument_list|()
decl_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|message
operator|.
name|createBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBodyShouldReturnTheShortMessageIfTheCommandIsAMessageRequest ()
specifier|public
name|void
name|createBodyShouldReturnTheShortMessageIfTheCommandIsAMessageRequest
parameter_list|()
block|{
name|DeliverSm
name|command
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|command
operator|.
name|setShortMessage
argument_list|(
literal|"Hello SMPP world!"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello SMPP world!"
argument_list|,
name|message
operator|.
name|createBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toStringShouldReturnTheBodyIfTheCommandIsNull ()
specifier|public
name|void
name|toStringShouldReturnTheBodyIfTheCommandIsNull
parameter_list|()
block|{
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SmppMessage: null"
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|toStringShouldReturnTheShortMessageIfTheCommandIsNotNull ()
specifier|public
name|void
name|toStringShouldReturnTheShortMessageIfTheCommandIsNotNull
parameter_list|()
block|{
name|DeliverSm
name|command
init|=
operator|new
name|DeliverSm
argument_list|()
decl_stmt|;
name|command
operator|.
name|setShortMessage
argument_list|(
literal|"Hello SMPP world!"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|SmppMessage
argument_list|(
name|camelContext
argument_list|,
name|command
argument_list|,
operator|new
name|SmppConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"SmppMessage: PDUHeader(0, 00000000, 00000000, 0)"
argument_list|,
name|message
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

