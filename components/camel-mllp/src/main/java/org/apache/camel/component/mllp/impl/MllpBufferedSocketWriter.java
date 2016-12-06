begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp.impl
package|package
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
name|impl
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketException
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
name|component
operator|.
name|mllp
operator|.
name|MllpAcknowledgementDeliveryException
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
name|component
operator|.
name|mllp
operator|.
name|MllpException
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
name|component
operator|.
name|mllp
operator|.
name|MllpWriteException
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
name|END_OF_BLOCK
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
name|END_OF_DATA
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
name|START_OF_BLOCK
import|;
end_import

begin_class
DECL|class|MllpBufferedSocketWriter
specifier|public
class|class
name|MllpBufferedSocketWriter
extends|extends
name|MllpSocketWriter
block|{
DECL|field|DEFAULT_SO_SNDBUF
specifier|static
specifier|final
name|int
name|DEFAULT_SO_SNDBUF
init|=
literal|65535
decl_stmt|;
DECL|field|outputBuffer
name|ByteArrayOutputStream
name|outputBuffer
decl_stmt|;
DECL|method|MllpBufferedSocketWriter (Socket socket, boolean acknowledgementWriter)
specifier|public
name|MllpBufferedSocketWriter
parameter_list|(
name|Socket
name|socket
parameter_list|,
name|boolean
name|acknowledgementWriter
parameter_list|)
block|{
name|super
argument_list|(
name|socket
argument_list|,
name|acknowledgementWriter
argument_list|)
expr_stmt|;
try|try
block|{
name|outputBuffer
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|socket
operator|.
name|getSendBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SocketException
name|socketEx
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Ignoring exception encountered retrieving SO_SNDBUF from the socket - using default size of %d bytes"
argument_list|,
name|DEFAULT_SO_SNDBUF
argument_list|)
argument_list|,
name|socketEx
argument_list|)
expr_stmt|;
name|outputBuffer
operator|=
operator|new
name|ByteArrayOutputStream
argument_list|(
name|DEFAULT_SO_SNDBUF
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|writeEnvelopedPayload (byte[] hl7MessageBytes, byte[] hl7AcknowledgementBytes)
specifier|public
name|void
name|writeEnvelopedPayload
parameter_list|(
name|byte
index|[]
name|hl7MessageBytes
parameter_list|,
name|byte
index|[]
name|hl7AcknowledgementBytes
parameter_list|)
throws|throws
name|MllpException
block|{
if|if
condition|(
name|socket
operator|==
literal|null
condition|)
block|{
specifier|final
name|String
name|errorMessage
init|=
literal|"Socket is null"
decl_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MllpAcknowledgementDeliveryException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|MllpWriteException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|socket
operator|.
name|isConnected
argument_list|()
condition|)
block|{
specifier|final
name|String
name|errorMessage
init|=
literal|"Socket is not connected"
decl_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MllpAcknowledgementDeliveryException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|MllpWriteException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
block|}
elseif|else
if|if
condition|(
name|socket
operator|.
name|isClosed
argument_list|()
condition|)
block|{
specifier|final
name|String
name|errorMessage
init|=
literal|"Socket is closed"
decl_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MllpAcknowledgementDeliveryException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|MllpWriteException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
block|}
name|OutputStream
name|socketOutputStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|socketOutputStream
operator|=
name|socket
operator|.
name|getOutputStream
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|errorMessage
init|=
literal|"Failed to retrieve the OutputStream from the Socket"
decl_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MllpAcknowledgementDeliveryException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|MllpWriteException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
block|}
name|outputBuffer
operator|.
name|write
argument_list|(
name|START_OF_BLOCK
argument_list|)
expr_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
if|if
condition|(
name|hl7AcknowledgementBytes
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"HL7 Acknowledgement payload is null - sending empty MLLP payload"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hl7AcknowledgementBytes
operator|.
name|length
operator|<=
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"HL7 Acknowledgement payload is empty - sending empty MLLP payload"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputBuffer
operator|.
name|write
argument_list|(
name|hl7AcknowledgementBytes
argument_list|,
literal|0
argument_list|,
name|hl7AcknowledgementBytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|hl7MessageBytes
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"HL7 Message payload is null - sending empty MLLP payload"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|hl7MessageBytes
operator|.
name|length
operator|<=
literal|0
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"HL7 Message payload is empty - sending empty MLLP payload"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|outputBuffer
operator|.
name|write
argument_list|(
name|hl7MessageBytes
argument_list|,
literal|0
argument_list|,
name|hl7MessageBytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
block|}
name|outputBuffer
operator|.
name|write
argument_list|(
name|END_OF_BLOCK
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|write
argument_list|(
name|END_OF_DATA
argument_list|)
expr_stmt|;
try|try
block|{
name|outputBuffer
operator|.
name|writeTo
argument_list|(
name|socketOutputStream
argument_list|)
expr_stmt|;
name|socketOutputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
specifier|final
name|String
name|errorMessage
init|=
literal|"Failed to write the MLLP payload to the Socket's OutputStream"
decl_stmt|;
if|if
condition|(
name|isAcknowledgementWriter
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|MllpAcknowledgementDeliveryException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|,
name|hl7AcknowledgementBytes
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|MllpWriteException
argument_list|(
name|errorMessage
argument_list|,
name|hl7MessageBytes
argument_list|)
throw|;
block|}
block|}
finally|finally
block|{
name|outputBuffer
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

