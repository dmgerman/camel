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
name|ArrayList
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
name|HashMap
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
name|Address
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
name|GeneralDataCoding
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
name|MessageClass
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
name|ReplaceIfPresentFlag
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
name|SubmitMulti
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
name|SubmitMultiResult
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
name|bean
operator|.
name|UnsuccessDelivery
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
DECL|class|SmppSubmitMultiCommand
specifier|public
class|class
name|SmppSubmitMultiCommand
extends|extends
name|SmppSmCommand
block|{
DECL|method|SmppSubmitMultiCommand (SMPPSession session, SmppConfiguration config)
specifier|public
name|SmppSubmitMultiCommand
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
name|SubmitMulti
index|[]
name|submitMulties
init|=
name|createSubmitMulti
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|SubmitMultiResult
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|SubmitMultiResult
argument_list|>
argument_list|(
name|submitMulties
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|SubmitMulti
name|submitMulti
range|:
name|submitMulties
control|)
block|{
name|SubmitMultiResult
name|result
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sending multiple short messages for exchange id '{}'..."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|result
operator|=
name|session
operator|.
name|submitMultiple
argument_list|(
name|submitMulti
operator|.
name|getServiceType
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|submitMulti
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|submitMulti
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|submitMulti
operator|.
name|getSourceAddr
argument_list|()
argument_list|,
operator|(
name|Address
index|[]
operator|)
name|submitMulti
operator|.
name|getDestAddresses
argument_list|()
argument_list|,
operator|new
name|ESMClass
argument_list|(
name|submitMulti
operator|.
name|getEsmClass
argument_list|()
argument_list|)
argument_list|,
name|submitMulti
operator|.
name|getProtocolId
argument_list|()
argument_list|,
name|submitMulti
operator|.
name|getPriorityFlag
argument_list|()
argument_list|,
name|submitMulti
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|,
name|submitMulti
operator|.
name|getValidityPeriod
argument_list|()
argument_list|,
operator|new
name|RegisteredDelivery
argument_list|(
name|submitMulti
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ReplaceIfPresentFlag
argument_list|(
name|submitMulti
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
argument_list|,
operator|new
name|GeneralDataCoding
argument_list|(
name|submitMulti
operator|.
name|getDataCoding
argument_list|()
argument_list|)
argument_list|,
name|submitMulti
operator|.
name|getSmDefaultMsgId
argument_list|()
argument_list|,
name|submitMulti
operator|.
name|getShortMessage
argument_list|()
argument_list|,
operator|new
name|OptionalParameter
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|results
operator|.
name|add
argument_list|(
name|result
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
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Sent multiple short messages for exchange id '{}' and received results '{}'"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|messageIDs
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|results
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
comment|// {messageID : [{destAddr : address, error : errorCode}]}
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|>
name|errors
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|SubmitMultiResult
name|result
range|:
name|results
control|)
block|{
name|UnsuccessDelivery
index|[]
name|deliveries
init|=
name|result
operator|.
name|getUnsuccessDeliveries
argument_list|()
decl_stmt|;
if|if
condition|(
name|deliveries
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|undelivered
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|UnsuccessDelivery
name|delivery
range|:
name|deliveries
control|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|error
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|error
operator|.
name|put
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
name|delivery
operator|.
name|getDestinationAddress
argument_list|()
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|error
operator|.
name|put
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|,
name|delivery
operator|.
name|getErrorStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|undelivered
operator|.
name|add
argument_list|(
name|error
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|undelivered
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|errors
operator|.
name|put
argument_list|(
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|,
name|undelivered
argument_list|)
expr_stmt|;
block|}
block|}
name|messageIDs
operator|.
name|add
argument_list|(
name|result
operator|.
name|getMessageId
argument_list|()
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
if|if
condition|(
operator|!
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|,
name|errors
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createSubmitMulti (Exchange exchange)
specifier|protected
name|SubmitMulti
index|[]
name|createSubmitMulti
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|body
init|=
name|exchange
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
decl_stmt|;
name|byte
name|providedAlphabet
init|=
name|getProvidedAlphabet
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Alphabet
name|determinedAlphabet
init|=
name|determineAlphabet
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
name|createSplitter
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Charset
name|charset
init|=
name|determineCharset
argument_list|(
name|providedAlphabet
argument_list|,
name|determinedAlphabet
operator|.
name|value
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
index|[]
name|segments
init|=
name|splitter
operator|.
name|split
argument_list|(
name|body
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
argument_list|)
decl_stmt|;
name|DataCoding
name|dataCoding
init|=
operator|new
name|GeneralDataCoding
argument_list|(
literal|false
argument_list|,
literal|true
argument_list|,
name|MessageClass
operator|.
name|CLASS1
argument_list|,
name|determinedAlphabet
argument_list|)
decl_stmt|;
name|ESMClass
name|esmClass
decl_stmt|;
comment|// multipart message
if|if
condition|(
name|segments
operator|.
name|length
operator|>
literal|1
condition|)
block|{
name|esmClass
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|esmClass
operator|=
operator|new
name|ESMClass
argument_list|()
expr_stmt|;
block|}
name|SubmitMulti
name|template
init|=
name|createSubmitMultiTemplate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|SubmitMulti
index|[]
name|submitMulties
init|=
operator|new
name|SubmitMulti
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
name|SubmitMulti
name|submitMulti
init|=
name|SmppUtils
operator|.
name|copySubmitMulti
argument_list|(
name|template
argument_list|)
decl_stmt|;
name|submitMulti
operator|.
name|setEsmClass
argument_list|(
name|esmClass
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|submitMulti
operator|.
name|setDataCoding
argument_list|(
name|dataCoding
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|submitMulti
operator|.
name|setShortMessage
argument_list|(
name|segments
index|[
name|i
index|]
argument_list|)
expr_stmt|;
name|submitMulties
index|[
name|i
index|]
operator|=
name|submitMulti
expr_stmt|;
block|}
return|return
name|submitMulties
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createSubmitMultiTemplate (Exchange exchange)
specifier|protected
name|SubmitMulti
name|createSubmitMultiTemplate
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
name|SubmitMulti
name|submitMulti
init|=
operator|new
name|SubmitMulti
argument_list|()
decl_stmt|;
name|byte
name|destAddrTon
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
name|DEST_ADDR_TON
argument_list|)
condition|)
block|{
name|destAddrTon
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|destAddrTon
operator|=
name|config
operator|.
name|getDestAddrTon
argument_list|()
expr_stmt|;
block|}
name|byte
name|destAddrNpi
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
name|DEST_ADDR_NPI
argument_list|)
condition|)
block|{
name|destAddrNpi
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|destAddrNpi
operator|=
name|config
operator|.
name|getDestAddrNpi
argument_list|()
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|destAddresses
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
name|DEST_ADDR
argument_list|)
condition|)
block|{
name|destAddresses
operator|=
name|in
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DEST_ADDR
argument_list|,
name|List
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|destAddresses
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|config
operator|.
name|getDestAddr
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Address
index|[]
name|addresses
init|=
operator|new
name|Address
index|[
name|destAddresses
operator|.
name|size
argument_list|()
index|]
decl_stmt|;
name|int
name|addrNum
init|=
literal|0
decl_stmt|;
for|for
control|(
name|String
name|destAddr
range|:
name|destAddresses
control|)
block|{
name|Address
name|addr
init|=
operator|new
name|Address
argument_list|(
name|destAddrTon
argument_list|,
name|destAddrNpi
argument_list|,
name|destAddr
argument_list|)
decl_stmt|;
name|addresses
index|[
name|addrNum
operator|++
index|]
operator|=
name|addr
expr_stmt|;
block|}
name|submitMulti
operator|.
name|setDestAddresses
argument_list|(
name|addresses
argument_list|)
expr_stmt|;
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
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
name|submitMulti
operator|.
name|setReplaceIfPresentFlag
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
name|submitMulti
operator|.
name|setReplaceIfPresentFlag
argument_list|(
name|config
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|submitMulti
return|;
block|}
block|}
end_class

end_unit

