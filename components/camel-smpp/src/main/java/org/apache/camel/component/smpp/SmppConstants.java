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

begin_comment
comment|/**  * Constants used in Camel SMPP module  *  */
end_comment

begin_interface
DECL|interface|SmppConstants
specifier|public
interface|interface
name|SmppConstants
block|{
DECL|field|ALPHABET
name|String
name|ALPHABET
init|=
literal|"CamelSmppAlphabet"
decl_stmt|;
DECL|field|COMMAND
name|String
name|COMMAND
init|=
literal|"CamelSmppCommand"
decl_stmt|;
DECL|field|COMMAND_ID
name|String
name|COMMAND_ID
init|=
literal|"CamelSmppCommandId"
decl_stmt|;
DECL|field|COMMAND_STATUS
name|String
name|COMMAND_STATUS
init|=
literal|"CamelSmppCommandStatus"
decl_stmt|;
DECL|field|DATA_CODING
name|String
name|DATA_CODING
init|=
literal|"CamelSmppDataCoding"
decl_stmt|;
DECL|field|DELIVERED
name|String
name|DELIVERED
init|=
literal|"CamelSmppDelivered"
decl_stmt|;
DECL|field|DEST_ADDR
name|String
name|DEST_ADDR
init|=
literal|"CamelSmppDestAddr"
decl_stmt|;
DECL|field|DEST_ADDR_NPI
name|String
name|DEST_ADDR_NPI
init|=
literal|"CamelSmppDestAddrNpi"
decl_stmt|;
DECL|field|DEST_ADDR_TON
name|String
name|DEST_ADDR_TON
init|=
literal|"CamelSmppDestAddrTon"
decl_stmt|;
DECL|field|DONE_DATE
name|String
name|DONE_DATE
init|=
literal|"CamelSmppDoneDate"
decl_stmt|;
DECL|field|ENCODING
name|String
name|ENCODING
init|=
literal|"CamelSmppEncoding"
decl_stmt|;
DECL|field|ERROR
name|String
name|ERROR
init|=
literal|"CamelSmppError"
decl_stmt|;
DECL|field|ESME_ADDR
name|String
name|ESME_ADDR
init|=
literal|"CamelSmppEsmeAddr"
decl_stmt|;
DECL|field|ESME_ADDR_NPI
name|String
name|ESME_ADDR_NPI
init|=
literal|"CamelSmppEsmeAddrNpi"
decl_stmt|;
DECL|field|ESME_ADDR_TON
name|String
name|ESME_ADDR_TON
init|=
literal|"CamelSmppEsmeAddrTon"
decl_stmt|;
DECL|field|FINAL_DATE
name|String
name|FINAL_DATE
init|=
literal|"CamelSmppFinalDate"
decl_stmt|;
DECL|field|FINAL_STATUS
name|String
name|FINAL_STATUS
init|=
literal|"CamelSmppStatus"
decl_stmt|;
DECL|field|ID
name|String
name|ID
init|=
literal|"CamelSmppId"
decl_stmt|;
DECL|field|MESSAGE_STATE
name|String
name|MESSAGE_STATE
init|=
literal|"CamelSmppMessageState"
decl_stmt|;
DECL|field|MESSAGE_TYPE
name|String
name|MESSAGE_TYPE
init|=
literal|"CamelSmppMessageType"
decl_stmt|;
DECL|field|PRIORITY_FLAG
name|String
name|PRIORITY_FLAG
init|=
literal|"CamelSmppPriorityFlag"
decl_stmt|;
DECL|field|PROTOCOL_ID
name|String
name|PROTOCOL_ID
init|=
literal|"CamelSmppProtocolId"
decl_stmt|;
DECL|field|REGISTERED_DELIVERY
name|String
name|REGISTERED_DELIVERY
init|=
literal|"CamelSmppRegisteredDelivery"
decl_stmt|;
DECL|field|REPLACE_IF_PRESENT_FLAG
name|String
name|REPLACE_IF_PRESENT_FLAG
init|=
literal|"CamelSmppReplaceIfPresentFlag"
decl_stmt|;
DECL|field|SCHEDULE_DELIVERY_TIME
name|String
name|SCHEDULE_DELIVERY_TIME
init|=
literal|"CamelSmppScheduleDeliveryTime"
decl_stmt|;
DECL|field|SENT_MESSAGE_COUNT
name|String
name|SENT_MESSAGE_COUNT
init|=
literal|"CamelSmppSentMessageCount"
decl_stmt|;
DECL|field|SEQUENCE_NUMBER
name|String
name|SEQUENCE_NUMBER
init|=
literal|"CamelSmppSequenceNumber"
decl_stmt|;
DECL|field|SERVICE_TYPE
name|String
name|SERVICE_TYPE
init|=
literal|"CamelSmppServiceType"
decl_stmt|;
DECL|field|SOURCE_ADDR
name|String
name|SOURCE_ADDR
init|=
literal|"CamelSmppSourceAddr"
decl_stmt|;
DECL|field|SOURCE_ADDR_NPI
name|String
name|SOURCE_ADDR_NPI
init|=
literal|"CamelSmppSourceAddrNpi"
decl_stmt|;
DECL|field|SOURCE_ADDR_TON
name|String
name|SOURCE_ADDR_TON
init|=
literal|"CamelSmppSourceAddrTon"
decl_stmt|;
DECL|field|SUBMITTED
name|String
name|SUBMITTED
init|=
literal|"CamelSmppSubmitted"
decl_stmt|;
DECL|field|SUBMIT_DATE
name|String
name|SUBMIT_DATE
init|=
literal|"CamelSmppSubmitDate"
decl_stmt|;
DECL|field|SYSTEM_ID
name|String
name|SYSTEM_ID
init|=
literal|"CamelSmppSystemId"
decl_stmt|;
DECL|field|PASSWORD
name|String
name|PASSWORD
init|=
literal|"CamelSmppPassword"
decl_stmt|;
DECL|field|VALIDITY_PERIOD
name|String
name|VALIDITY_PERIOD
init|=
literal|"CamelSmppValidityPeriod"
decl_stmt|;
DECL|field|OPTIONAL_PARAMETERS
name|String
name|OPTIONAL_PARAMETERS
init|=
literal|"CamelSmppOptionalParameters"
decl_stmt|;
DECL|field|OPTIONAL_PARAMETER
name|String
name|OPTIONAL_PARAMETER
init|=
literal|"CamelSmppOptionalParameter"
decl_stmt|;
DECL|field|SPLITTING_POLICY
name|String
name|SPLITTING_POLICY
init|=
literal|"CamelSmppSplittingPolicy"
decl_stmt|;
DECL|field|UCS2_ENCODING
name|String
name|UCS2_ENCODING
init|=
literal|"UTF-16BE"
decl_stmt|;
DECL|field|UNKNOWN_ALPHABET
name|byte
name|UNKNOWN_ALPHABET
init|=
operator|-
literal|1
decl_stmt|;
block|}
end_interface

end_unit

