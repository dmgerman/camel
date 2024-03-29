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
name|Message
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
name|DataCodings
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
name|GSMSpecificFeature
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
name|MessageMode
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
name|MessageType
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
name|SubmitSm
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

begin_class
DECL|class|SmppSubmitSmCommand
specifier|public
class|class
name|SmppSubmitSmCommand
extends|extends
name|SmppSmCommand
block|{
DECL|method|SmppSubmitSmCommand (SMPPSession session, SmppConfiguration config)
specifier|public
name|SmppSubmitSmCommand
parameter_list|(
name|SMPPSession
name|session
parameter_list|,
name|SmppConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (Exchange exchange)
specifier|public
name|void
name|execute
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SmppException
block|{
name|SubmitSm
index|[]
name|submitSms
init|=
name|createSubmitSm
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|messageIDs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|submitSms
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|submitSms
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|SubmitSm
name|submitSm
init|=
name|submitSms
index|[
name|i
index|]
decl_stmt|;
name|String
name|messageID
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending short message {} for exchange id '{}'..."
argument_list|,
name|i
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|messageID
operator|=
name|session
operator|.
name|submitShortMessage
argument_list|(
name|submitSm
operator|.
name|getServiceType
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getSourceAddr
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|submitSm
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getDestAddress
argument_list|()
argument_list|,
operator|new
name|ESMClass
argument_list|(
name|submitSm
operator|.
name|getEsmClass
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getProtocolId
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getPriorityFlag
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getValidityPeriod
argument_list|()
argument_list|,
operator|new
name|RegisteredDelivery
argument_list|(
name|submitSm
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
argument_list|,
name|submitSm
operator|.
name|getReplaceIfPresent
argument_list|()
argument_list|,
name|DataCodings
operator|.
name|newInstance
argument_list|(
name|submitSm
operator|.
name|getDataCoding
argument_list|()
argument_list|)
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|submitSm
operator|.
name|getShortMessage
argument_list|()
argument_list|,
name|submitSm
operator|.
name|getOptionalParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SmppException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|messageIDs
operator|.
name|add
argument_list|(
name|messageID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sent short message for exchange id '{}' and received message ids '{}'"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|messageIDs
argument_list|)
expr_stmt|;
block|}
name|Message
name|message
init|=
name|getResponseMessage
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|,
name|messageIDs
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SENT_MESSAGE_COUNT
argument_list|,
name|messageIDs
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createSubmitSm (Exchange exchange)
specifier|protected
name|SubmitSm
index|[]
name|createSubmitSm
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SmppException
block|{
name|SubmitSm
name|template
init|=
name|createSubmitSmTemplate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|byte
index|[]
index|[]
name|segments
init|=
name|splitBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
name|ESMClass
name|esmClass
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ESM_CLASS
argument_list|,
name|ESMClass
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|esmClass
condition|)
block|{
name|template
operator|.
name|setEsmClass
argument_list|(
name|esmClass
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
comment|// multipart message
block|}
elseif|else
if|if
condition|(
name|segments
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|template
operator|.
name|setEsmClass
argument_list|(
operator|new
name|ESMClass
argument_list|(
name|MessageMode
operator|.
name|DEFAULT
argument_list|,
name|MessageType
operator|.
name|DEFAULT
argument_list|,
name|GSMSpecificFeature
operator|.
name|UDHI
argument_list|)
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SubmitSm
index|[]
name|submitSms
init|=
operator|new
name|SubmitSm
index|[
name|segments
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|segments
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|SubmitSm
name|submitSm
init|=
name|SmppUtils
operator|.
name|copySubmitSm
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|submitSm
operator|.
name|setShortMessage
argument_list|(
name|segments
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|submitSms
index|[
name|i
index|]
operator|=
name|submitSm
expr_stmt|;
block|}
return|return
name|submitSms
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|createSubmitSmTemplate (Exchange exchange)
specifier|protected
name|SubmitSm
name|createSubmitSmTemplate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|SubmitSm
name|submitSm
init|=
operator|new
name|SubmitSm
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|DATA_CODING
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setDataCoding
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DATA_CODING
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|ALPHABET
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setDataCoding
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ALPHABET
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setDataCoding
argument_list|(
name|config
operator|.
name|getDataCoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setDestAddress
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setDestAddress
argument_list|(
name|config
operator|.
name|getDestAddr
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setDestAddrTon
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_TON
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setDestAddrTon
argument_list|(
name|config
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setDestAddrNpi
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR_NPI
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setDestAddrNpi
argument_list|(
name|config
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setSourceAddr
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setSourceAddr
argument_list|(
name|config
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setSourceAddrTon
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setSourceAddrTon
argument_list|(
name|config
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setSourceAddrNpi
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setSourceAddrNpi
argument_list|(
name|config
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setServiceType
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SERVICE_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setServiceType
argument_list|(
name|config
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|REGISTERED_DELIVERY
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setRegisteredDelivery
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|REGISTERED_DELIVERY
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setRegisteredDelivery
argument_list|(
name|config
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|PROTOCOL_ID
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setProtocolId
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|PROTOCOL_ID
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setProtocolId
argument_list|(
name|config
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|PRIORITY_FLAG
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setPriorityFlag
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|PRIORITY_FLAG
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setPriorityFlag
argument_list|(
name|config
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SCHEDULE_DELIVERY_TIME
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setScheduleDeliveryTime
argument_list|(
name|SmppUtils
operator|.
name|formatTime
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SCHEDULE_DELIVERY_TIME
argument_list|,
name|Date
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|VALIDITY_PERIOD
argument_list|)
condition|)
block|{
name|Object
name|validityPeriod
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|VALIDITY_PERIOD
argument_list|)
decl_stmt|;
if|if
condition|(
name|validityPeriod
operator|instanceof
name|String
condition|)
block|{
name|submitSm
operator|.
name|setValidityPeriod
argument_list|(
operator|(
name|String
operator|)
name|validityPeriod
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|validityPeriod
operator|instanceof
name|Date
condition|)
block|{
name|submitSm
operator|.
name|setValidityPeriod
argument_list|(
name|SmppUtils
operator|.
name|formatTime
argument_list|(
operator|(
name|Date
operator|)
name|validityPeriod
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|in
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|REPLACE_IF_PRESENT_FLAG
argument_list|)
condition|)
block|{
name|submitSm
operator|.
name|setReplaceIfPresent
argument_list|(
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|REPLACE_IF_PRESENT_FLAG
argument_list|,
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setReplaceIfPresent
argument_list|(
name|config
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|submitSm
operator|.
name|setEsmClass
argument_list|(
operator|new
name|ESMClass
argument_list|()
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|java
operator|.
name|lang
operator|.
name|Short
argument_list|,
name|Object
argument_list|>
name|optinalParamater
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETER
argument_list|,
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|optinalParamater
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|OptionalParameter
argument_list|>
name|optParams
init|=
name|createOptionalParametersByCode
argument_list|(
name|optinalParamater
argument_list|)
decl_stmt|;
name|submitSm
operator|.
name|setOptionalParameters
argument_list|(
name|optParams
operator|.
name|toArray
argument_list|(
operator|new
name|OptionalParameter
index|[
name|optParams
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|optinalParamaters
init|=
name|in
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
if|if
condition|(
name|optinalParamaters
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|OptionalParameter
argument_list|>
name|optParams
init|=
name|createOptionalParametersByName
argument_list|(
name|optinalParamaters
argument_list|)
decl_stmt|;
name|submitSm
operator|.
name|setOptionalParameters
argument_list|(
name|optParams
operator|.
name|toArray
argument_list|(
operator|new
name|OptionalParameter
index|[
name|optParams
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|submitSm
operator|.
name|setOptionalParameters
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|submitSm
return|;
block|}
block|}
end_class

end_unit

