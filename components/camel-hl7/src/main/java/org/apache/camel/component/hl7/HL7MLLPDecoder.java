begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hl7
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hl7
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
name|CharacterCodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|CharsetDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|buffer
operator|.
name|IoBuffer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|core
operator|.
name|session
operator|.
name|IoSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|CumulativeProtocolDecoder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|mina
operator|.
name|filter
operator|.
name|codec
operator|.
name|ProtocolDecoderOutput
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

begin_comment
comment|/**  * HL7MLLPDecoder that is aware that a HL7 message can span several TCP packets.  * In addition, it avoids rescanning packets by keeping state in the IOSession.  */
end_comment

begin_class
DECL|class|HL7MLLPDecoder
class|class
name|HL7MLLPDecoder
extends|extends
name|CumulativeProtocolDecoder
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
name|HL7MLLPDecoder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|CHARSET_DECODER
specifier|private
specifier|static
specifier|final
name|String
name|CHARSET_DECODER
init|=
name|HL7MLLPDecoder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".charsetdecoder"
decl_stmt|;
DECL|field|DECODER_STATE
specifier|private
specifier|static
specifier|final
name|String
name|DECODER_STATE
init|=
name|HL7MLLPDecoder
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".STATE"
decl_stmt|;
DECL|field|config
specifier|private
name|HL7MLLPConfig
name|config
decl_stmt|;
DECL|method|HL7MLLPDecoder (HL7MLLPConfig config)
name|HL7MLLPDecoder
parameter_list|(
name|HL7MLLPConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doDecode (IoSession session, IoBuffer in, ProtocolDecoderOutput out)
specifier|protected
name|boolean
name|doDecode
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|IoBuffer
name|in
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
block|{
comment|// Scan the buffer of start and/or end bytes
name|boolean
name|foundEnd
init|=
name|scan
argument_list|(
name|session
argument_list|,
name|in
argument_list|)
decl_stmt|;
comment|// Write HL7 string or wait until message end arrives or buffer ends
if|if
condition|(
name|foundEnd
condition|)
block|{
name|writeString
argument_list|(
name|session
argument_list|,
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No complete message in this packet"
argument_list|)
expr_stmt|;
block|}
return|return
name|foundEnd
return|;
block|}
DECL|method|writeString (IoSession session, IoBuffer in, ProtocolDecoderOutput out)
specifier|private
name|void
name|writeString
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|IoBuffer
name|in
parameter_list|,
name|ProtocolDecoderOutput
name|out
parameter_list|)
block|{
name|DecoderState
name|state
init|=
name|decoderState
argument_list|(
name|session
argument_list|)
decl_stmt|;
if|if
condition|(
name|state
operator|.
name|posStart
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No start byte found, reading from beginning of data"
argument_list|)
expr_stmt|;
block|}
comment|// start reading from the buffer after the start markers
name|in
operator|.
name|position
argument_list|(
name|state
operator|.
name|posStart
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|body
init|=
name|in
operator|.
name|getString
argument_list|(
name|state
operator|.
name|length
argument_list|()
argument_list|,
name|charsetDecoder
argument_list|(
name|session
argument_list|)
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Decoded HL7 from byte stream of length {} to String of length {}"
argument_list|,
name|state
operator|.
name|length
argument_list|()
argument_list|,
name|body
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|.
name|isConvertLFtoCR
argument_list|()
condition|)
block|{
name|body
operator|=
name|body
operator|.
name|replace
argument_list|(
literal|'\n'
argument_list|,
literal|'\r'
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|write
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// Avoid redelivery of scanned message
name|state
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CharacterCodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|charsetDecoder (IoSession session)
specifier|private
name|CharsetDecoder
name|charsetDecoder
parameter_list|(
name|IoSession
name|session
parameter_list|)
block|{
comment|// convert to string using the charset decoder
name|CharsetDecoder
name|decoder
init|=
operator|(
name|CharsetDecoder
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|CHARSET_DECODER
argument_list|)
decl_stmt|;
if|if
condition|(
name|decoder
operator|==
literal|null
condition|)
block|{
name|decoder
operator|=
name|config
operator|.
name|getCharset
argument_list|()
operator|.
name|newDecoder
argument_list|()
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|CHARSET_DECODER
argument_list|,
name|decoder
argument_list|)
expr_stmt|;
block|}
return|return
name|decoder
return|;
block|}
comment|/**      * Scans the buffer for start and end bytes and stores its position in the      * session state object.      *      * @return<code>true</code> if the end bytes were found,<code>false</code>      *         otherwise      */
DECL|method|scan (IoSession session, IoBuffer in)
specifier|private
name|boolean
name|scan
parameter_list|(
name|IoSession
name|session
parameter_list|,
name|IoBuffer
name|in
parameter_list|)
block|{
name|DecoderState
name|state
init|=
name|decoderState
argument_list|(
name|session
argument_list|)
decl_stmt|;
comment|// Start scanning where we left
name|in
operator|.
name|position
argument_list|(
name|state
operator|.
name|current
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Start scanning buffer at position {}"
argument_list|,
name|in
operator|.
name|position
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|in
operator|.
name|hasRemaining
argument_list|()
condition|)
block|{
name|byte
name|b
init|=
name|in
operator|.
name|get
argument_list|()
decl_stmt|;
comment|// Check start byte
if|if
condition|(
name|b
operator|==
name|config
operator|.
name|getStartByte
argument_list|()
condition|)
block|{
if|if
condition|(
name|state
operator|.
name|posStart
operator|>
literal|0
operator|||
name|state
operator|.
name|waitingForEndByte2
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring message start at position {} before previous message has ended."
argument_list|,
name|in
operator|.
name|position
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|state
operator|.
name|posStart
operator|=
name|in
operator|.
name|position
argument_list|()
expr_stmt|;
name|state
operator|.
name|waitingForEndByte2
operator|=
literal|false
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message starts at position {}"
argument_list|,
name|state
operator|.
name|posStart
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Check end byte1
if|if
condition|(
name|b
operator|==
name|config
operator|.
name|getEndByte1
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|state
operator|.
name|waitingForEndByte2
operator|&&
name|state
operator|.
name|posStart
operator|>
literal|0
condition|)
block|{
name|state
operator|.
name|waitingForEndByte2
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring unexpected 1st end byte {}. Expected 2nd endpoint {}"
argument_list|,
name|b
argument_list|,
name|config
operator|.
name|getEndByte2
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Check end byte2
if|if
condition|(
name|b
operator|==
name|config
operator|.
name|getEndByte2
argument_list|()
operator|&&
name|state
operator|.
name|waitingForEndByte2
condition|)
block|{
name|state
operator|.
name|posEnd
operator|=
name|in
operator|.
name|position
argument_list|()
operator|-
literal|2
expr_stmt|;
comment|// use -2 to skip these
comment|// last 2 end markers
name|state
operator|.
name|waitingForEndByte2
operator|=
literal|false
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Message ends at position {}"
argument_list|,
name|state
operator|.
name|posEnd
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// Remember where we are
name|state
operator|.
name|current
operator|=
name|in
operator|.
name|position
argument_list|()
expr_stmt|;
name|in
operator|.
name|rewind
argument_list|()
expr_stmt|;
return|return
name|state
operator|.
name|posEnd
operator|>
literal|0
return|;
block|}
DECL|method|decoderState (IoSession session)
specifier|private
name|DecoderState
name|decoderState
parameter_list|(
name|IoSession
name|session
parameter_list|)
block|{
name|DecoderState
name|decoderState
init|=
operator|(
name|DecoderState
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|DECODER_STATE
argument_list|)
decl_stmt|;
if|if
condition|(
name|decoderState
operator|==
literal|null
condition|)
block|{
name|decoderState
operator|=
operator|new
name|DecoderState
argument_list|()
expr_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|DECODER_STATE
argument_list|,
name|decoderState
argument_list|)
expr_stmt|;
block|}
return|return
name|decoderState
return|;
block|}
annotation|@
name|Override
DECL|method|dispose (IoSession session)
specifier|public
name|void
name|dispose
parameter_list|(
name|IoSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|session
operator|.
name|removeAttribute
argument_list|(
name|CHARSET_DECODER
argument_list|)
expr_stmt|;
name|session
operator|.
name|removeAttribute
argument_list|(
name|DECODER_STATE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Holds the state of the decoding process      */
DECL|class|DecoderState
specifier|private
specifier|static
class|class
name|DecoderState
block|{
DECL|field|posStart
name|int
name|posStart
decl_stmt|;
DECL|field|posEnd
name|int
name|posEnd
decl_stmt|;
DECL|field|current
name|int
name|current
decl_stmt|;
DECL|field|waitingForEndByte2
name|boolean
name|waitingForEndByte2
decl_stmt|;
DECL|method|length ()
name|int
name|length
parameter_list|()
block|{
return|return
name|posEnd
operator|-
name|posStart
return|;
block|}
DECL|method|reset ()
name|void
name|reset
parameter_list|()
block|{
name|posStart
operator|=
literal|0
expr_stmt|;
name|posEnd
operator|=
literal|0
expr_stmt|;
name|waitingForEndByte2
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

