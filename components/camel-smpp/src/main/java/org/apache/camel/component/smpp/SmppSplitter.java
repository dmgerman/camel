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

begin_class
DECL|class|SmppSplitter
specifier|public
class|class
name|SmppSplitter
block|{
comment|/**      * The length of the UDH in bytes.      *<p/>      * The real length of the header must be 6 bytes, but the first byte that      * contains the length of the header must not be counted.      */
DECL|field|UDHIE_HEADER_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_HEADER_LENGTH
init|=
literal|0x05
decl_stmt|;
comment|/**      * The real length of the UDH header.      *<p/>      * The real length of the UDH header is {@link #UDHIE_HEADER_LENGTH}      * {@code + 1}.      *       * @see #UDHIE_HEADER_LENGTH      */
DECL|field|UDHIE_HEADER_REAL_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_HEADER_REAL_LENGTH
init|=
name|UDHIE_HEADER_LENGTH
operator|+
literal|1
decl_stmt|;
comment|/**      * The length of the reference number of the SAR fragmet of the UDH header.      *<p/>      * The length can be 1 or 2 bytes and is considered to be 1 byte.      */
DECL|field|UDHIE_SAR_REF_NUM_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|UDHIE_SAR_REF_NUM_LENGTH
init|=
literal|1
decl_stmt|;
comment|/**      * The value that identifier length of the SAR fragment.      *<p/>      * {@code 0x00} value must be used if the length of the reference number is      * 1 byte.<br/>      * {@code 0x08} value must be used if the length of the reference number is      * 2 bytes.      */
DECL|field|UDHIE_IDENTIFIER_SAR
specifier|protected
specifier|static
specifier|final
name|byte
name|UDHIE_IDENTIFIER_SAR
init|=
literal|0x00
decl_stmt|;
comment|/**      * The length of the SAR fragment.      *<p/>      * {@code 0x03} value must be used if the length of the reference number is      * 1 byte.<br/>      * {@code 0x04} value must be used if the length of the reference number is      * 2 bytes.      */
DECL|field|UDHIE_SAR_LENGTH
specifier|protected
specifier|static
specifier|final
name|byte
name|UDHIE_SAR_LENGTH
init|=
literal|0x03
decl_stmt|;
comment|/**      * The maximum length of the message in bytes.      */
DECL|field|MAX_MSG_BYTE_LENGTH
specifier|protected
specifier|static
specifier|final
name|int
name|MAX_MSG_BYTE_LENGTH
init|=
literal|140
decl_stmt|;
comment|/**      * The maximum amount of segments in the multipart message.      */
DECL|field|MAX_SEG_COUNT
specifier|protected
specifier|static
specifier|final
name|int
name|MAX_SEG_COUNT
init|=
literal|255
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SmppSplitter
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Current reference number.      */
DECL|field|refNum
specifier|private
specifier|static
name|int
name|refNum
decl_stmt|;
DECL|field|messageLength
specifier|private
name|int
name|messageLength
decl_stmt|;
DECL|field|segmentLength
specifier|private
name|int
name|segmentLength
decl_stmt|;
DECL|field|currentLength
specifier|private
name|int
name|currentLength
decl_stmt|;
DECL|method|SmppSplitter (int messageLength, int segmentLength, int currentLength)
specifier|protected
name|SmppSplitter
parameter_list|(
name|int
name|messageLength
parameter_list|,
name|int
name|segmentLength
parameter_list|,
name|int
name|currentLength
parameter_list|)
block|{
name|this
operator|.
name|messageLength
operator|=
name|messageLength
expr_stmt|;
name|this
operator|.
name|segmentLength
operator|=
name|segmentLength
expr_stmt|;
name|this
operator|.
name|currentLength
operator|=
name|currentLength
expr_stmt|;
block|}
comment|/**      * Returns reference number which length is      * {@link #UDHIE_SAR_REF_NUM_LENGTH}.      *       * @return the reference number of the multipart message      */
DECL|method|getReferenceNumber ()
specifier|protected
specifier|static
specifier|synchronized
name|byte
name|getReferenceNumber
parameter_list|()
block|{
name|refNum
operator|++
expr_stmt|;
if|if
condition|(
name|refNum
operator|==
literal|256
condition|)
block|{
name|refNum
operator|=
literal|1
expr_stmt|;
block|}
return|return
operator|(
name|byte
operator|)
name|refNum
return|;
block|}
DECL|method|getCurrentReferenceNumber ()
specifier|protected
specifier|static
specifier|synchronized
name|byte
name|getCurrentReferenceNumber
parameter_list|()
block|{
return|return
operator|(
name|byte
operator|)
name|refNum
return|;
block|}
comment|/**      * only needed for the unit tests       */
DECL|method|resetCurrentReferenceNumber ()
specifier|protected
specifier|static
specifier|synchronized
name|void
name|resetCurrentReferenceNumber
parameter_list|()
block|{
name|SmppSplitter
operator|.
name|refNum
operator|=
literal|0
expr_stmt|;
block|}
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
return|return
operator|new
name|byte
index|[]
index|[]
block|{
name|message
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
name|LOG
operator|.
name|trace
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
name|LOG
operator|.
name|trace
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
name|UDHIE_HEADER_REAL_LENGTH
operator|+
name|lengthOfData
index|]
expr_stmt|;
name|LOG
operator|.
name|trace
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
name|UDHIE_HEADER_LENGTH
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
name|UDHIE_HEADER_REAL_LENGTH
argument_list|,
name|lengthOfData
argument_list|)
expr_stmt|;
block|}
return|return
name|segments
return|;
block|}
DECL|method|isSplitRequired ()
specifier|protected
name|boolean
name|isSplitRequired
parameter_list|()
block|{
return|return
name|getCurrentLength
argument_list|()
operator|>
name|getMessageLength
argument_list|()
return|;
block|}
comment|/**      * Gets maximum message length.      *       * @return maximum message length      */
DECL|method|getMessageLength ()
specifier|public
name|int
name|getMessageLength
parameter_list|()
block|{
return|return
name|messageLength
return|;
block|}
comment|/**      * Gets maximum segment length.      *       * @return maximum segment length      */
DECL|method|getSegmentLength ()
specifier|public
name|int
name|getSegmentLength
parameter_list|()
block|{
return|return
name|segmentLength
return|;
block|}
comment|/**      * Gets length of the message to split.      *       * @return length of the message to split      */
DECL|method|getCurrentLength ()
specifier|public
name|int
name|getCurrentLength
parameter_list|()
block|{
return|return
name|currentLength
return|;
block|}
block|}
end_class

end_unit

