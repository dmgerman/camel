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
name|Calendar
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
name|SubmitSm
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
name|AbsoluteTimeFormatter
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
name|TimeFormatter
import|;
end_import

begin_class
DECL|class|SmppUtils
specifier|public
specifier|final
class|class
name|SmppUtils
block|{
comment|/**      * See http://unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT      */
DECL|field|ISO_GSM_0338
specifier|public
specifier|static
specifier|final
name|short
index|[]
name|ISO_GSM_0338
init|=
block|{
literal|64
block|,
literal|163
block|,
literal|36
block|,
literal|165
block|,
literal|232
block|,
literal|233
block|,
literal|249
block|,
literal|236
block|,
literal|242
block|,
literal|199
block|,
literal|10
block|,
literal|216
block|,
literal|248
block|,
literal|13
block|,
literal|197
block|,
literal|229
block|,
literal|0
block|,
literal|95
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|198
block|,
literal|230
block|,
literal|223
block|,
literal|201
block|,
literal|32
block|,
literal|33
block|,
literal|34
block|,
literal|35
block|,
literal|164
block|,
literal|37
block|,
literal|38
block|,
literal|39
block|,
literal|40
block|,
literal|41
block|,
literal|42
block|,
literal|43
block|,
literal|44
block|,
literal|45
block|,
literal|46
block|,
literal|47
block|,
literal|48
block|,
literal|49
block|,
literal|50
block|,
literal|51
block|,
literal|52
block|,
literal|53
block|,
literal|54
block|,
literal|55
block|,
literal|56
block|,
literal|57
block|,
literal|58
block|,
literal|59
block|,
literal|60
block|,
literal|61
block|,
literal|62
block|,
literal|63
block|,
literal|161
block|,
literal|65
block|,
literal|66
block|,
literal|67
block|,
literal|68
block|,
literal|69
block|,
literal|70
block|,
literal|71
block|,
literal|72
block|,
literal|73
block|,
literal|74
block|,
literal|75
block|,
literal|76
block|,
literal|77
block|,
literal|78
block|,
literal|79
block|,
literal|80
block|,
literal|81
block|,
literal|82
block|,
literal|83
block|,
literal|84
block|,
literal|85
block|,
literal|86
block|,
literal|87
block|,
literal|88
block|,
literal|89
block|,
literal|90
block|,
literal|196
block|,
literal|214
block|,
literal|209
block|,
literal|220
block|,
literal|167
block|,
literal|191
block|,
literal|97
block|,
literal|98
block|,
literal|99
block|,
literal|100
block|,
literal|101
block|,
literal|102
block|,
literal|103
block|,
literal|104
block|,
literal|105
block|,
literal|106
block|,
literal|107
block|,
literal|108
block|,
literal|109
block|,
literal|110
block|,
literal|111
block|,
literal|112
block|,
literal|113
block|,
literal|114
block|,
literal|115
block|,
literal|116
block|,
literal|117
block|,
literal|118
block|,
literal|119
block|,
literal|120
block|,
literal|121
block|,
literal|122
block|,
literal|228
block|,
literal|246
block|,
literal|241
block|,
literal|252
block|,
literal|224
block|}
decl_stmt|;
comment|/**      * See http://unicode.org/Public/MAPPINGS/ETSI/GSM0338.TXT      */
DECL|field|ISO_GSM_0338_EXT
specifier|public
specifier|static
specifier|final
name|short
index|[]
index|[]
name|ISO_GSM_0338_EXT
init|=
block|{
block|{
literal|10
block|,
literal|12
block|}
block|,
block|{
literal|20
block|,
literal|94
block|}
block|,
block|{
literal|40
block|,
literal|123
block|}
block|,
block|{
literal|41
block|,
literal|125
block|}
block|,
block|{
literal|47
block|,
literal|92
block|}
block|,
block|{
literal|60
block|,
literal|91
block|}
block|,
block|{
literal|61
block|,
literal|126
block|}
block|,
block|{
literal|62
block|,
literal|93
block|}
block|,
block|{
literal|64
block|,
literal|124
block|}
block|,
block|{
literal|101
block|,
literal|164
block|}
block|}
decl_stmt|;
DECL|field|TIME_FORMATTER
specifier|private
specifier|static
specifier|final
name|TimeFormatter
name|TIME_FORMATTER
init|=
operator|new
name|AbsoluteTimeFormatter
argument_list|()
decl_stmt|;
DECL|method|SmppUtils ()
specifier|private
name|SmppUtils
parameter_list|()
block|{     }
DECL|method|formatTime (Date date)
specifier|public
specifier|static
name|String
name|formatTime
parameter_list|(
name|Date
name|date
parameter_list|)
block|{
return|return
name|TIME_FORMATTER
operator|.
name|format
argument_list|(
name|date
argument_list|)
return|;
block|}
comment|/**      * YYMMDDhhmmSS where:      *<ul>      *<li>YY = last two digits of the year (00-99)</li>      *<li>MM = month (01-12)</li>      *<li>DD = day (01-31)</li>      *<li>hh = hour (00-23)</li>      *<li>mm = minute (00-59)</li>      *<li>SS = second (00-59)</li>      *</ul>      *      * Java format is (yyMMddHHmmSS).      *      * @param date in<tt>String</tt> format.      * @return the date      * @throws NumberFormatException if there is contains non number on      *<code>date</code> parameter.      * @throws IndexOutOfBoundsException if the date length in<tt>String</tt>      *         format is less than 10.      */
DECL|method|string2Date (String date)
specifier|public
specifier|static
name|Date
name|string2Date
parameter_list|(
name|String
name|date
parameter_list|)
block|{
if|if
condition|(
name|date
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|int
name|year
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|month
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|2
argument_list|,
literal|4
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|day
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|4
argument_list|,
literal|6
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|hour
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|6
argument_list|,
literal|8
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|minute
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|8
argument_list|,
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|int
name|second
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|date
operator|.
name|substring
argument_list|(
literal|10
argument_list|,
literal|12
argument_list|)
argument_list|)
decl_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|convertTwoDigitYear
argument_list|(
name|year
argument_list|)
argument_list|,
name|month
operator|-
literal|1
argument_list|,
name|day
argument_list|,
name|hour
argument_list|,
name|minute
argument_list|,
name|second
argument_list|)
expr_stmt|;
name|cal
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|cal
operator|.
name|getTime
argument_list|()
return|;
block|}
DECL|method|convertTwoDigitYear (int year)
specifier|private
specifier|static
name|int
name|convertTwoDigitYear
parameter_list|(
name|int
name|year
parameter_list|)
block|{
if|if
condition|(
name|year
operator|>=
literal|0
operator|&&
name|year
operator|<=
literal|37
condition|)
block|{
return|return
literal|2000
operator|+
name|year
return|;
block|}
elseif|else
if|if
condition|(
name|year
operator|>=
literal|38
operator|&&
name|year
operator|<=
literal|99
condition|)
block|{
return|return
literal|1900
operator|+
name|year
return|;
block|}
else|else
block|{
comment|// should never happen
return|return
name|year
return|;
block|}
block|}
DECL|method|is8Bit (Alphabet alphabet)
specifier|public
specifier|static
name|boolean
name|is8Bit
parameter_list|(
name|Alphabet
name|alphabet
parameter_list|)
block|{
return|return
name|alphabet
operator|==
name|Alphabet
operator|.
name|ALPHA_UNSPECIFIED_2
operator|||
name|alphabet
operator|==
name|Alphabet
operator|.
name|ALPHA_8_BIT
return|;
block|}
comment|/**      * Decides if the characters in the argument are GSM 3.38 encodeable.      * @param aMessage must be a set of characters encoded in ISO-8859-1      *                 or a compatible character set.  In particular,      *                 UTF-8 encoded text should not be passed to this method.      * @return true if the characters can be represented in GSM 3.38      */
DECL|method|isGsm0338Encodeable (byte[] aMessage)
specifier|public
specifier|static
name|boolean
name|isGsm0338Encodeable
parameter_list|(
name|byte
index|[]
name|aMessage
parameter_list|)
block|{
name|outer
label|:
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|aMessage
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|ISO_GSM_0338
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|ISO_GSM_0338
index|[
name|j
index|]
operator|==
name|aMessage
index|[
name|i
index|]
condition|)
block|{
continue|continue
name|outer
continue|;
block|}
block|}
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|ISO_GSM_0338_EXT
operator|.
name|length
condition|;
name|j
operator|++
control|)
block|{
if|if
condition|(
name|ISO_GSM_0338_EXT
index|[
name|j
index|]
index|[
literal|1
index|]
operator|==
name|aMessage
index|[
name|i
index|]
condition|)
block|{
continue|continue
name|outer
continue|;
block|}
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|copySubmitSm (SubmitSm src)
specifier|public
specifier|static
name|SubmitSm
name|copySubmitSm
parameter_list|(
name|SubmitSm
name|src
parameter_list|)
block|{
name|SubmitSm
name|dest
init|=
operator|new
name|SubmitSm
argument_list|()
decl_stmt|;
name|dest
operator|.
name|setCommandId
argument_list|(
name|src
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandLength
argument_list|(
name|src
operator|.
name|getCommandLength
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandStatus
argument_list|(
name|src
operator|.
name|getCommandStatus
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDataCoding
argument_list|(
name|src
operator|.
name|getDataCoding
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddress
argument_list|(
name|src
operator|.
name|getDestAddress
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddrNpi
argument_list|(
name|src
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddrTon
argument_list|(
name|src
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setEsmClass
argument_list|(
name|src
operator|.
name|getEsmClass
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setOptionalParameters
argument_list|(
name|src
operator|.
name|getOptionalParameters
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setPriorityFlag
argument_list|(
name|src
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setProtocolId
argument_list|(
name|src
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setRegisteredDelivery
argument_list|(
name|src
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setReplaceIfPresent
argument_list|(
name|src
operator|.
name|getReplaceIfPresent
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setScheduleDeliveryTime
argument_list|(
name|src
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSequenceNumber
argument_list|(
name|src
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setServiceType
argument_list|(
name|src
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setShortMessage
argument_list|(
name|src
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSmDefaultMsgId
argument_list|(
name|src
operator|.
name|getSmDefaultMsgId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddr
argument_list|(
name|src
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrNpi
argument_list|(
name|src
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrTon
argument_list|(
name|src
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setValidityPeriod
argument_list|(
name|src
operator|.
name|getValidityPeriod
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|src
operator|.
name|isDatagramMode
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setDatagramMode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isDefaultMessageType
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setDefaultMessageType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isDefaultMode
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setDefaultMode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isEsmeDeliveryAcknowledgement
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setEsmeDelivertAcknowledgement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isEsmeManualAcknowledgement
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setEsmeManualAcknowledgement
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isForwardMode
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setForwardMode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isReplyPath
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setReplyPath
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isSmscDelReceiptFailureRequested
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setSmscDelReceiptFailureRequested
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isSmscDelReceiptNotRequested
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setSmscDelReceiptNotRequested
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isSmscDelReceiptSuccessAndFailureRequested
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setSmscDelReceiptSuccessAndFailureRequested
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isStoreAndForwardMode
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setStoreAndForwardMode
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isUdhi
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setUdhi
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isUdhiAndReplyPath
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setUdhiAndReplyPath
argument_list|()
expr_stmt|;
block|}
return|return
name|dest
return|;
block|}
DECL|method|copySubmitMulti (SubmitMulti src)
specifier|public
specifier|static
name|SubmitMulti
name|copySubmitMulti
parameter_list|(
name|SubmitMulti
name|src
parameter_list|)
block|{
name|SubmitMulti
name|dest
init|=
operator|new
name|SubmitMulti
argument_list|()
decl_stmt|;
name|dest
operator|.
name|setCommandId
argument_list|(
name|src
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandLength
argument_list|(
name|src
operator|.
name|getCommandLength
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandStatus
argument_list|(
name|src
operator|.
name|getCommandStatus
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDataCoding
argument_list|(
name|src
operator|.
name|getDataCoding
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddresses
argument_list|(
name|src
operator|.
name|getDestAddresses
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setEsmClass
argument_list|(
name|src
operator|.
name|getEsmClass
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setOptionalParameters
argument_list|(
name|src
operator|.
name|getOptionalParameters
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setPriorityFlag
argument_list|(
name|src
operator|.
name|getPriorityFlag
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setProtocolId
argument_list|(
name|src
operator|.
name|getProtocolId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setRegisteredDelivery
argument_list|(
name|src
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setReplaceIfPresentFlag
argument_list|(
name|src
operator|.
name|getReplaceIfPresentFlag
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setScheduleDeliveryTime
argument_list|(
name|src
operator|.
name|getScheduleDeliveryTime
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSequenceNumber
argument_list|(
name|src
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setServiceType
argument_list|(
name|src
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setShortMessage
argument_list|(
name|src
operator|.
name|getShortMessage
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSmDefaultMsgId
argument_list|(
name|src
operator|.
name|getSmDefaultMsgId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddr
argument_list|(
name|src
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrNpi
argument_list|(
name|src
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrTon
argument_list|(
name|src
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setValidityPeriod
argument_list|(
name|src
operator|.
name|getValidityPeriod
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dest
return|;
block|}
DECL|method|copyDataSm (DataSm src)
specifier|public
specifier|static
name|DataSm
name|copyDataSm
parameter_list|(
name|DataSm
name|src
parameter_list|)
block|{
name|DataSm
name|dest
init|=
operator|new
name|DataSm
argument_list|()
decl_stmt|;
name|dest
operator|.
name|setCommandId
argument_list|(
name|src
operator|.
name|getCommandId
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandLength
argument_list|(
name|src
operator|.
name|getCommandLength
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setCommandStatus
argument_list|(
name|src
operator|.
name|getCommandStatus
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDataCoding
argument_list|(
name|src
operator|.
name|getDataCoding
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddress
argument_list|(
name|src
operator|.
name|getDestAddress
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddrNpi
argument_list|(
name|src
operator|.
name|getDestAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setDestAddrTon
argument_list|(
name|src
operator|.
name|getDestAddrTon
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setEsmClass
argument_list|(
name|src
operator|.
name|getEsmClass
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setOptionalParameters
argument_list|(
name|src
operator|.
name|getOptionalParameters
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setRegisteredDelivery
argument_list|(
name|src
operator|.
name|getRegisteredDelivery
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSequenceNumber
argument_list|(
name|src
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setServiceType
argument_list|(
name|src
operator|.
name|getServiceType
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddr
argument_list|(
name|src
operator|.
name|getSourceAddr
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrNpi
argument_list|(
name|src
operator|.
name|getSourceAddrNpi
argument_list|()
argument_list|)
expr_stmt|;
name|dest
operator|.
name|setSourceAddrTon
argument_list|(
name|src
operator|.
name|getSourceAddrTon
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|src
operator|.
name|isDefaultMessageType
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setDefaultMessageType
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isReplyPath
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setReplyPath
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isUdhi
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setUdhi
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|src
operator|.
name|isUdhiAndReplyPath
argument_list|()
condition|)
block|{
name|dest
operator|.
name|setUdhiAndReplyPath
argument_list|()
expr_stmt|;
block|}
return|return
name|dest
return|;
block|}
block|}
end_class

end_unit

