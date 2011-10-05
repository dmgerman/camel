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
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|List
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
name|Command
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
name|DeliveryReceipt
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
name|session
operator|.
name|SMPPSession
import|;
end_import

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@link Exchange} and  * {@link SmppMessage} to and from a SMPP {@link Command}  *   * @version   */
end_comment

begin_class
DECL|class|SmppBinding
specifier|public
class|class
name|SmppBinding
block|{
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|method|SmppBinding ()
specifier|public
name|SmppBinding
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|SmppBinding (SmppConfiguration configuration)
specifier|public
name|SmppBinding
parameter_list|(
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
comment|/**      * Create the SmppCommand object from the inbound exchange      *       * @throws UnsupportedEncodingException if the encoding is not supported      */
DECL|method|createSmppCommand (SMPPSession session, Exchange exchange)
specifier|public
name|SmppCommand
name|createSmppCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|SmppCommandType
name|commandType
init|=
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|SmppCommand
name|command
init|=
name|commandType
operator|.
name|createCommand
argument_list|(
name|session
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
return|return
name|command
return|;
block|}
comment|/**      * Create a new SmppMessage from the inbound alert notification      */
DECL|method|createSmppMessage (AlertNotification alertNotification)
specifier|public
name|SmppMessage
name|createSmppMessage
parameter_list|(
name|AlertNotification
name|alertNotification
parameter_list|)
block|{
name|SmppMessage
name|smppMessage
init|=
operator|new
name|SmppMessage
argument_list|(
name|alertNotification
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|SmppMessageType
operator|.
name|AlertNotification
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|alertNotification
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|,
name|alertNotification
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_STATUS
argument_list|,
name|alertNotification
operator|.
name|getCommandStatus
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
name|alertNotification
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|,
name|alertNotification
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|,
name|alertNotification
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR
argument_list|,
name|alertNotification
operator|.
name|getEsmeAddr
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR_NPI
argument_list|,
name|alertNotification
operator|.
name|getEsmeAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ESME_ADDR_TON
argument_list|,
name|alertNotification
operator|.
name|getEsmeAddrTon
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|smppMessage
return|;
block|}
comment|/**      * Create a new SmppMessage from the inbound deliver sm or deliver receipt      */
DECL|method|createSmppMessage (DeliverSm deliverSm)
specifier|public
name|SmppMessage
name|createSmppMessage
parameter_list|(
name|DeliverSm
name|deliverSm
parameter_list|)
throws|throws
name|Exception
block|{
name|SmppMessage
name|smppMessage
init|=
operator|new
name|SmppMessage
argument_list|(
name|deliverSm
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
if|if
condition|(
name|deliverSm
operator|.
name|isSmscDeliveryReceipt
argument_list|()
condition|)
block|{
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|SmppMessageType
operator|.
name|DeliveryReceipt
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|DeliveryReceipt
name|smscDeliveryReceipt
init|=
name|deliverSm
operator|.
name|getShortMessageAsDeliveryReceipt
argument_list|()
decl_stmt|;
name|smppMessage
operator|.
name|setBody
argument_list|(
name|smscDeliveryReceipt
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DELIVERED
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getDelivered
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DONE_DATE
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getDoneDate
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
literal|"000"
operator|.
name|equals
argument_list|(
name|smscDeliveryReceipt
operator|.
name|getError
argument_list|()
argument_list|)
condition|)
block|{
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getError
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SUBMIT_DATE
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getSubmitDate
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SUBMITTED
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getSubmitted
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|FINAL_STATUS
argument_list|,
name|smscDeliveryReceipt
operator|.
name|getFinalStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|SmppMessageType
operator|.
name|DeliverSm
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|deliverSm
operator|.
name|getShortMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|smppMessage
operator|.
name|setBody
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
operator|new
name|String
argument_list|(
name|deliverSm
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|configuration
operator|.
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|deliverSm
operator|.
name|getOptionalParametes
argument_list|()
operator|!=
literal|null
operator|&&
name|deliverSm
operator|.
name|getOptionalParametes
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|List
argument_list|<
name|OptionalParameter
argument_list|>
name|oplist
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|deliverSm
operator|.
name|getOptionalParametes
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|OptionalParameter
name|optPara
range|:
name|oplist
control|)
block|{
if|if
condition|(
name|OptionalParameter
operator|.
name|Tag
operator|.
name|MESSAGE_PAYLOAD
operator|.
name|code
argument_list|()
operator|==
name|optPara
operator|.
name|tag
operator|&&
name|OctetString
operator|.
name|class
operator|.
name|isInstance
argument_list|(
name|optPara
argument_list|)
condition|)
block|{
name|smppMessage
operator|.
name|setBody
argument_list|(
operator|(
operator|(
name|OctetString
operator|)
name|optPara
operator|)
operator|.
name|getValueAsString
argument_list|()
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|deliverSm
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|,
name|deliverSm
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
name|deliverSm
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
name|deliverSm
operator|.
name|getDestAddress
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SCHEDULE_DELIVERY_TIME
argument_list|,
name|deliverSm
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|VALIDITY_PERIOD
argument_list|,
name|deliverSm
operator|.
name|getValidityPeriod
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|,
name|deliverSm
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|smppMessage
return|;
block|}
DECL|method|createSmppMessage (DataSm dataSm, String smppMessageId)
specifier|public
name|SmppMessage
name|createSmppMessage
parameter_list|(
name|DataSm
name|dataSm
parameter_list|,
name|String
name|smppMessageId
parameter_list|)
block|{
name|SmppMessage
name|smppMessage
init|=
operator|new
name|SmppMessage
argument_list|(
name|dataSm
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_TYPE
argument_list|,
name|SmppMessageType
operator|.
name|DataSm
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|,
name|smppMessageId
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|dataSm
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_ID
argument_list|,
name|dataSm
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND_STATUS
argument_list|,
name|dataSm
operator|.
name|getCommandStatus
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
name|dataSm
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|,
name|dataSm
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|,
name|dataSm
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
name|dataSm
operator|.
name|getDestAddress
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|,
name|dataSm
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|,
name|dataSm
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|,
name|dataSm
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|REGISTERED_DELIVERY
argument_list|,
name|dataSm
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|smppMessage
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|DATA_CODING
argument_list|,
name|dataSm
operator|.
name|getDataCoding
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|smppMessage
return|;
block|}
comment|/**      * Returns the current date. Externalized for better test support.      *       * @return the current date      */
DECL|method|getCurrentDate ()
name|Date
name|getCurrentDate
parameter_list|()
block|{
return|return
operator|new
name|Date
argument_list|()
return|;
block|}
comment|/**      * Returns the smpp configuration      *       * @return the configuration      */
DECL|method|getConfiguration ()
specifier|public
name|SmppConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
comment|/**      * Set the smpp configuration.      *       * @param configuration smppConfiguration      */
DECL|method|setConfiguration (SmppConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SmppConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

