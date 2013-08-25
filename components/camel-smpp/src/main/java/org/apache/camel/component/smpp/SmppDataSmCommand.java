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
name|Byte
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
name|COctetString
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
name|Int
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
name|Null
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
name|OptionalParameter
operator|.
name|Short
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

begin_class
DECL|class|SmppDataSmCommand
specifier|public
class|class
name|SmppDataSmCommand
extends|extends
name|AbstractSmppCommand
block|{
DECL|method|SmppDataSmCommand (SMPPSession session, SmppConfiguration config)
specifier|public
name|SmppDataSmCommand
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
name|DataSm
name|dataSm
init|=
name|createDataSm
argument_list|(
name|exchange
argument_list|)
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
literal|"Sending a data short message for exchange id '{}'..."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DataSmResult
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|session
operator|.
name|dataShortMessage
argument_list|(
name|dataSm
operator|.
name|getServiceType
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|dataSm
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|dataSm
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|dataSm
operator|.
name|getSourceAddr
argument_list|()
argument_list|,
name|TypeOfNumber
operator|.
name|valueOf
argument_list|(
name|dataSm
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
argument_list|,
name|NumberingPlanIndicator
operator|.
name|valueOf
argument_list|(
name|dataSm
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
argument_list|,
name|dataSm
operator|.
name|getDestAddress
argument_list|()
argument_list|,
operator|new
name|ESMClass
argument_list|(
name|dataSm
operator|.
name|getEsmClass
argument_list|()
argument_list|)
argument_list|,
operator|new
name|RegisteredDelivery
argument_list|(
name|dataSm
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
argument_list|,
name|DataCoding
operator|.
name|newInstance
argument_list|(
name|dataSm
operator|.
name|getDataCoding
argument_list|()
argument_list|)
argument_list|,
name|dataSm
operator|.
name|getOptionalParametes
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
literal|"Sent a data short message for exchange id '{}' and message id '{}'"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
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
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|OPTIONAL_PARAMETERS
argument_list|,
name|getOptionalParametersAsMap
argument_list|(
name|result
operator|.
name|getOptionalParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getOptionalParametersAsMap (OptionalParameter[] optionalParameters)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getOptionalParametersAsMap
parameter_list|(
name|OptionalParameter
index|[]
name|optionalParameters
parameter_list|)
block|{
if|if
condition|(
name|optionalParameters
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|optParams
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|OptionalParameter
name|optionalParameter
range|:
name|optionalParameters
control|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|optionalParameter
operator|instanceof
name|COctetString
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|COctetString
operator|)
name|optionalParameter
operator|)
operator|.
name|getValueAsString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|optionalParameter
operator|instanceof
name|OctetString
condition|)
block|{
name|value
operator|=
operator|(
operator|(
name|OctetString
operator|)
name|optionalParameter
operator|)
operator|.
name|getValueAsString
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|optionalParameter
operator|instanceof
name|Int
condition|)
block|{
name|value
operator|=
name|String
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|Int
operator|)
name|optionalParameter
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|optionalParameter
operator|instanceof
name|Short
condition|)
block|{
name|value
operator|=
name|String
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|Short
operator|)
name|optionalParameter
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|optionalParameter
operator|instanceof
name|Byte
condition|)
block|{
name|value
operator|=
name|String
operator|.
name|valueOf
argument_list|(
operator|(
operator|(
name|Byte
operator|)
name|optionalParameter
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|optionalParameter
operator|instanceof
name|Null
condition|)
block|{
name|value
operator|=
literal|null
expr_stmt|;
block|}
name|optParams
operator|.
name|put
argument_list|(
name|Tag
operator|.
name|valueOf
argument_list|(
name|optionalParameter
operator|.
name|tag
argument_list|)
operator|.
name|name
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|optParams
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|createDataSm (Exchange exchange)
specifier|protected
name|DataSm
name|createDataSm
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
name|DataSm
name|dataSm
init|=
operator|new
name|DataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|dataSm
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
name|java
operator|.
name|lang
operator|.
name|Byte
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|dataSm
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
name|dataSm
operator|.
name|setOptionalParametes
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
name|dataSm
operator|.
name|setOptionalParametes
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
name|dataSm
operator|.
name|setOptionalParametes
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|dataSm
return|;
block|}
block|}
end_class

end_unit

