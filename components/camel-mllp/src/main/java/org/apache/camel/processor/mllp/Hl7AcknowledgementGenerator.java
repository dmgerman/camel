begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpConstants
operator|.
name|MLLP_ACKNOWLEDGEMENT_TYPE
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|MESSAGE_TERMINATOR
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
operator|.
name|MllpEndpoint
operator|.
name|SEGMENT_DELIMITER
import|;
end_import

begin_comment
comment|/**  * A Camel Processor for generating HL7 Acknowledgements  */
end_comment

begin_class
DECL|class|Hl7AcknowledgementGenerator
specifier|public
class|class
name|Hl7AcknowledgementGenerator
implements|implements
name|Processor
block|{
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
name|Hl7AcknowledgementGenerator
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|defaultNack
name|String
name|defaultNack
init|=
literal|"MSH|^~\\&|||||||NACK||P|2.2"
operator|+
name|SEGMENT_DELIMITER
operator|+
literal|"MSA|AR|"
operator|+
name|SEGMENT_DELIMITER
operator|+
name|MESSAGE_TERMINATOR
decl_stmt|;
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|message
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
name|byte
index|[]
name|hl7Bytes
init|=
name|message
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|byte
index|[]
name|acknowledgementBytes
init|=
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|exchange
operator|.
name|getException
argument_list|()
condition|)
block|{
name|acknowledgementBytes
operator|=
name|generateApplicationAcceptAcknowledgementMessage
argument_list|(
name|hl7Bytes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|MLLP_ACKNOWLEDGEMENT_TYPE
argument_list|,
literal|"AA"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|acknowledgementBytes
operator|=
name|generateApplicationErrorAcknowledgementMessage
argument_list|(
name|hl7Bytes
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|MLLP_ACKNOWLEDGEMENT_TYPE
argument_list|,
literal|"AE"
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|MLLP_ACKNOWLEDGEMENT
argument_list|,
name|acknowledgementBytes
argument_list|)
expr_stmt|;
block|}
DECL|method|generateApplicationAcceptAcknowledgementMessage (byte[] hl7MessageBytes)
specifier|public
name|byte
index|[]
name|generateApplicationAcceptAcknowledgementMessage
parameter_list|(
name|byte
index|[]
name|hl7MessageBytes
parameter_list|)
throws|throws
name|Hl7AcknowledgementGenerationException
block|{
return|return
name|generateAcknowledgementMessage
argument_list|(
name|hl7MessageBytes
argument_list|,
literal|"AA"
argument_list|)
return|;
block|}
DECL|method|generateApplicationRejectAcknowledgementMessage (byte[] hl7MessageBytes)
specifier|public
name|byte
index|[]
name|generateApplicationRejectAcknowledgementMessage
parameter_list|(
name|byte
index|[]
name|hl7MessageBytes
parameter_list|)
throws|throws
name|Hl7AcknowledgementGenerationException
block|{
return|return
name|generateAcknowledgementMessage
argument_list|(
name|hl7MessageBytes
argument_list|,
literal|"AR"
argument_list|)
return|;
block|}
DECL|method|generateApplicationErrorAcknowledgementMessage (byte[] hl7MessageBytes)
specifier|public
name|byte
index|[]
name|generateApplicationErrorAcknowledgementMessage
parameter_list|(
name|byte
index|[]
name|hl7MessageBytes
parameter_list|)
throws|throws
name|Hl7AcknowledgementGenerationException
block|{
return|return
name|generateAcknowledgementMessage
argument_list|(
name|hl7MessageBytes
argument_list|,
literal|"AE"
argument_list|)
return|;
block|}
DECL|method|generateAcknowledgementMessage (byte[] hl7MessageBytes, String acknowledgementCode)
name|byte
index|[]
name|generateAcknowledgementMessage
parameter_list|(
name|byte
index|[]
name|hl7MessageBytes
parameter_list|,
name|String
name|acknowledgementCode
parameter_list|)
throws|throws
name|Hl7AcknowledgementGenerationException
block|{
if|if
condition|(
name|hl7MessageBytes
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Hl7AcknowledgementGenerationException
argument_list|(
literal|"Null HL7 message received for parsing operation"
argument_list|)
throw|;
block|}
specifier|final
name|byte
name|fieldSeparator
init|=
name|hl7MessageBytes
index|[
literal|3
index|]
decl_stmt|;
specifier|final
name|byte
name|componentSeparator
init|=
name|hl7MessageBytes
index|[
literal|4
index|]
decl_stmt|;
name|List
argument_list|<
name|Integer
argument_list|>
name|fieldSeparatorIndexes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|10
argument_list|)
decl_stmt|;
comment|// We need at least 10 fields to create the acknowledgment
comment|// Find the end of the MSH and indexes of the fields in the MSH
name|int
name|endOfMSH
init|=
operator|-
literal|1
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
name|hl7MessageBytes
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
if|if
condition|(
name|fieldSeparator
operator|==
name|hl7MessageBytes
index|[
name|i
index|]
condition|)
block|{
name|fieldSeparatorIndexes
operator|.
name|add
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|SEGMENT_DELIMITER
operator|==
name|hl7MessageBytes
index|[
name|i
index|]
condition|)
block|{
name|endOfMSH
operator|=
name|i
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|-
literal|1
operator|==
name|endOfMSH
condition|)
block|{
throw|throw
operator|new
name|Hl7AcknowledgementGenerationException
argument_list|(
literal|"Failed to find the end of the  MSH Segment while attempting to generate response"
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
if|if
condition|(
literal|8
operator|>
name|fieldSeparatorIndexes
operator|.
name|size
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|Hl7AcknowledgementGenerationException
argument_list|(
literal|"Insufficient number of fields in after MSH-2 in MSH to generate a response - 8 are required but "
operator|+
name|fieldSeparatorIndexes
operator|.
name|size
argument_list|()
operator|+
literal|" "
operator|+
literal|"were found"
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
comment|// Build the MSH Segment
name|ByteArrayOutputStream
name|acknowledgement
init|=
operator|new
name|ByteArrayOutputStream
argument_list|(
literal|1024
argument_list|)
decl_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
literal|0
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// through MSH-2 (without trailing field separator)
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-5
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|5
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-6
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-3
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-4
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|5
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-7 and MSH-8
comment|// Need to generate the correct MSH-9
name|acknowledgement
operator|.
name|write
argument_list|(
name|fieldSeparator
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
literal|"ACK"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
comment|// MSH-9.1
name|int
name|msh92start
init|=
operator|-
literal|1
decl_stmt|;
for|for
control|(
name|int
name|j
init|=
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|7
argument_list|)
operator|+
literal|1
init|;
name|j
operator|<
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
condition|;
operator|++
name|j
control|)
block|{
if|if
condition|(
name|componentSeparator
operator|==
name|hl7MessageBytes
index|[
name|j
index|]
condition|)
block|{
name|msh92start
operator|=
name|j
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|-
literal|1
operator|==
name|msh92start
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Didn't find component separator for MSH-9.2 - sending ACK in MSH-9"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|msh92start
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
operator|-
name|msh92start
argument_list|)
expr_stmt|;
comment|// MSH-9.2
block|}
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|,
name|endOfMSH
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-10 through the end of the MSH
name|acknowledgement
operator|.
name|write
argument_list|(
name|SEGMENT_DELIMITER
argument_list|)
expr_stmt|;
comment|// Build the MSA Segment
name|acknowledgement
operator|.
name|write
argument_list|(
literal|"MSA"
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
name|fieldSeparator
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
name|acknowledgementCode
operator|.
name|getBytes
argument_list|()
argument_list|,
literal|0
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|,
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|9
argument_list|)
operator|-
name|fieldSeparatorIndexes
operator|.
name|get
argument_list|(
literal|8
argument_list|)
argument_list|)
expr_stmt|;
comment|// MSH-10 end
name|acknowledgement
operator|.
name|write
argument_list|(
name|SEGMENT_DELIMITER
argument_list|)
expr_stmt|;
comment|// Terminate the message
name|acknowledgement
operator|.
name|write
argument_list|(
name|SEGMENT_DELIMITER
argument_list|)
expr_stmt|;
name|acknowledgement
operator|.
name|write
argument_list|(
name|MESSAGE_TERMINATOR
argument_list|)
expr_stmt|;
return|return
name|acknowledgement
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

