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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Splitter for messages use National Language Lock Table  *<p/>  * @see 3GPP 23.038 Reference  */
end_comment

begin_class
DECL|class|SmppNLSTSplitter
specifier|public
class|class
name|SmppNLSTSplitter
extends|extends
name|SmppSplitter
block|{
comment|/**      * The length of the UDH for single short message in bytes.      * 0x25 - UDHIE_NLI_IDENTIFIER      * 0x01 - Length of the header      * 0xXX - Locking shift table indicator the Language      */
DECL|field|UDHIE_NLI_SINGLE_MSG_HEADER_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_SINGLE_MSG_HEADER_LENGTH
init|=
literal|0x03
decl_stmt|;
comment|// header length for single message
comment|/**      * The real length of the UDH for single short message      */
DECL|field|UDHIE_NLI_SINGLE_MSG_HEADER_REAL_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_SINGLE_MSG_HEADER_REAL_LENGTH
init|=
name|UDHIE_NLI_SINGLE_MSG_HEADER_LENGTH
operator|+
literal|1
decl_stmt|;
comment|/**      * The length of the UDH for splitted short messages, in bytes.      * 0x08 Overall header length      * 0x00 The value that identifier length of the SAR fragment. (8bit reference number only)      * 0x03 The length of the SAR fragment      * 0xXX The reference number for SAR      * 0xXX Total number of splitted / segmented messages      * 0xXX Segment number      * 0x25 National language locking shift element identifier      * 0x01 Length of the header      * 0xXX Locking shift table indicator for the Language (ie: 0x01 for Turkish)      */
DECL|field|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
init|=
literal|0x08
decl_stmt|;
comment|/**      * The real length of the UDH for segmentet short messages      */
DECL|field|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
init|=
name|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
operator|+
literal|1
decl_stmt|;
comment|/**      * The element identifier value for the National Language Locking Table      */
DECL|field|UDHIE_NLI_IDENTIFIER
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_IDENTIFIER
init|=
literal|0x25
decl_stmt|;
comment|/**      * The length of the NLI header      */
DECL|field|UDHIE_NLI_HEADER_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_NLI_HEADER_LENGTH
init|=
literal|0x01
decl_stmt|;
comment|/**      * The maximum length in chars of the NLI messages.      *<p/>      * Each letter will be represented as 7 bit (like GSM8)      */
DECL|field|MAX_MSG_CHAR_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|MAX_MSG_CHAR_SIZE
init|=
operator|(
name|MAX_MSG_BYTE_LENGTH
operator|*
literal|8
operator|/
literal|7
operator|)
operator|-
operator|(
name|UDHIE_NLI_SINGLE_MSG_HEADER_REAL_LENGTH
operator|+
literal|1
operator|)
decl_stmt|;
DECL|field|MAX_SEG_BYTE_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|MAX_SEG_BYTE_SIZE
init|=
operator|(
name|MAX_MSG_BYTE_LENGTH
operator|-
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
operator|)
operator|*
literal|8
operator|/
literal|7
decl_stmt|;
comment|/**      * Locking shift table indicator for the Language, single byte      */
DECL|field|languageIdentifier
specifier|private
name|byte
name|languageIdentifier
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SmppNLSTSplitter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SmppNLSTSplitter (int currentLength, byte languageIdentifier)
specifier|public
name|SmppNLSTSplitter
parameter_list|(
name|int
name|currentLength
parameter_list|,
name|byte
name|languageIdentifier
parameter_list|)
block|{
name|super
argument_list|(
name|MAX_MSG_CHAR_SIZE
argument_list|,
name|MAX_SEG_BYTE_SIZE
argument_list|,
name|currentLength
argument_list|)
expr_stmt|;
name|this
operator|.
name|languageIdentifier
operator|=
name|languageIdentifier
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|split (byte[] message)
specifier|public
name|byte
index|[]
index|[]
name|split
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSplitRequired
argument_list|()
condition|)
block|{
name|byte
index|[]
name|nliMessage
init|=
operator|new
name|byte
index|[
name|UDHIE_NLI_SINGLE_MSG_HEADER_REAL_LENGTH
operator|+
name|message
operator|.
name|length
index|]
decl_stmt|;
name|nliMessage
index|[
literal|0
index|]
operator|=
operator|(
name|byte
operator|)
name|UDHIE_NLI_SINGLE_MSG_HEADER_LENGTH
expr_stmt|;
name|nliMessage
index|[
literal|1
index|]
operator|=
operator|(
name|byte
operator|)
name|UDHIE_NLI_IDENTIFIER
expr_stmt|;
name|nliMessage
index|[
literal|2
index|]
operator|=
operator|(
name|byte
operator|)
name|UDHIE_NLI_HEADER_LENGTH
expr_stmt|;
name|nliMessage
index|[
literal|3
index|]
operator|=
name|this
operator|.
name|languageIdentifier
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|message
argument_list|,
literal|0
argument_list|,
name|nliMessage
argument_list|,
literal|4
argument_list|,
name|message
operator|.
name|length
argument_list|)
expr_stmt|;
return|return
operator|new
name|byte
index|[]
index|[]
block|{
name|nliMessage
block|}
return|;
block|}
name|int
name|segmentLength
init|=
name|getSegmentLength
argument_list|()
decl_stmt|;
comment|// determine how many messages
name|int
name|segmentNum
init|=
name|message
operator|.
name|length
operator|/
name|segmentLength
decl_stmt|;
name|int
name|messageLength
init|=
name|message
operator|.
name|length
decl_stmt|;
if|if
condition|(
name|segmentNum
operator|>
name|MAX_SEG_COUNT
condition|)
block|{
comment|// this is too long, can't fit, so chop
name|segmentNum
operator|=
name|MAX_SEG_COUNT
expr_stmt|;
name|messageLength
operator|=
name|segmentNum
operator|*
name|segmentLength
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|messageLength
operator|%
name|segmentLength
operator|)
operator|>
literal|0
condition|)
block|{
name|segmentNum
operator|++
expr_stmt|;
block|}
name|byte
index|[]
index|[]
name|segments
init|=
operator|new
name|byte
index|[
name|segmentNum
index|]
index|[]
decl_stmt|;
name|int
name|lengthOfData
decl_stmt|;
name|byte
name|refNum
init|=
name|getReferenceNumber
argument_list|()
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
name|segmentNum
condition|;
name|i
operator|++
control|)
block|{
name|logger
operator|.
name|debug
argument_list|(
literal|"segment number = {}"
argument_list|,
name|i
argument_list|)
expr_stmt|;
if|if
condition|(
name|segmentNum
operator|-
name|i
operator|==
literal|1
condition|)
block|{
name|lengthOfData
operator|=
name|messageLength
operator|-
name|i
operator|*
name|segmentLength
expr_stmt|;
block|}
else|else
block|{
name|lengthOfData
operator|=
name|segmentLength
expr_stmt|;
block|}
name|logger
operator|.
name|debug
argument_list|(
literal|"Length of data = {}"
argument_list|,
name|lengthOfData
argument_list|)
expr_stmt|;
name|segments
index|[
name|i
index|]
operator|=
operator|new
name|byte
index|[
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
operator|+
name|lengthOfData
index|]
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"segments[{}].length = {}"
argument_list|,
name|i
argument_list|,
name|segments
index|[
name|i
index|]
operator|.
name|length
argument_list|)
expr_stmt|;
name|segments
index|[
name|i
index|]
index|[
literal|0
index|]
operator|=
name|UDHIE_NLI_MULTI_MSG_HEADER_LENGTH
expr_stmt|;
comment|// doesn't include itself, is header length
comment|// SAR identifier
name|segments
index|[
name|i
index|]
index|[
literal|1
index|]
operator|=
name|UDHIE_IDENTIFIER_SAR
expr_stmt|;
comment|// SAR length
name|segments
index|[
name|i
index|]
index|[
literal|2
index|]
operator|=
name|UDHIE_SAR_LENGTH
expr_stmt|;
comment|// DATAGRAM REFERENCE NUMBER
name|segments
index|[
name|i
index|]
index|[
literal|3
index|]
operator|=
name|refNum
expr_stmt|;
comment|// total number of segments
name|segments
index|[
name|i
index|]
index|[
literal|4
index|]
operator|=
operator|(
name|byte
operator|)
name|segmentNum
expr_stmt|;
comment|// segment #
name|segments
index|[
name|i
index|]
index|[
literal|5
index|]
operator|=
call|(
name|byte
call|)
argument_list|(
name|i
operator|+
literal|1
argument_list|)
expr_stmt|;
comment|// national language locking shift table, element identifier
name|segments
index|[
name|i
index|]
index|[
literal|6
index|]
operator|=
operator|(
name|byte
operator|)
name|UDHIE_NLI_IDENTIFIER
expr_stmt|;
name|segments
index|[
name|i
index|]
index|[
literal|7
index|]
operator|=
operator|(
name|byte
operator|)
name|UDHIE_NLI_HEADER_LENGTH
expr_stmt|;
name|segments
index|[
name|i
index|]
index|[
literal|8
index|]
operator|=
name|this
operator|.
name|languageIdentifier
expr_stmt|;
comment|// now copy the data
name|System
operator|.
name|arraycopy
argument_list|(
name|message
argument_list|,
name|i
operator|*
name|segmentLength
argument_list|,
name|segments
index|[
name|i
index|]
argument_list|,
name|UDHIE_NLI_MULTI_MSG_HEADER_REAL_LENGTH
argument_list|,
name|lengthOfData
argument_list|)
expr_stmt|;
block|}
return|return
name|segments
return|;
block|}
block|}
end_class

end_unit

