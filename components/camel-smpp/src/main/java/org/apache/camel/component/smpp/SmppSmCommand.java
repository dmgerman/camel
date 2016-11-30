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
name|Alphabet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|extra
operator|.
name|NegativeResponseException
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
DECL|class|SmppSmCommand
specifier|public
specifier|abstract
class|class
name|SmppSmCommand
extends|extends
name|AbstractSmppCommand
block|{
comment|// FIXME: these constants should be defined somewhere in jSMPP:
DECL|field|SMPP_NEG_RESPONSE_MSG_TOO_LONG
specifier|public
specifier|static
specifier|final
name|int
name|SMPP_NEG_RESPONSE_MSG_TOO_LONG
init|=
literal|1
decl_stmt|;
DECL|field|ascii
specifier|protected
name|Charset
name|ascii
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"US-ASCII"
argument_list|)
decl_stmt|;
DECL|field|latin1
specifier|protected
name|Charset
name|latin1
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
DECL|field|defaultCharset
specifier|protected
name|Charset
name|defaultCharset
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
name|SmppSmCommand
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SmppSmCommand (SMPPSession session, SmppConfiguration config)
specifier|public
name|SmppSmCommand
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
name|defaultCharset
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|config
operator|.
name|getEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|splitBody (Message message)
specifier|protected
name|byte
index|[]
index|[]
name|splitBody
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|SmppException
block|{
name|byte
index|[]
name|shortMessage
init|=
name|getShortMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|SmppSplitter
name|splitter
init|=
name|createSplitter
argument_list|(
name|message
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
name|shortMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|segments
operator|.
name|length
operator|>
literal|1
condition|)
block|{
comment|// Message body is split into multiple parts,
comment|// check if this is permitted
name|SmppSplittingPolicy
name|policy
init|=
name|getSplittingPolicy
argument_list|(
name|message
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|policy
condition|)
block|{
case|case
name|ALLOW
case|:
return|return
name|segments
return|;
case|case
name|TRUNCATE
case|:
return|return
operator|new
name|byte
index|[]
index|[]
block|{
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|copyOfRange
argument_list|(
name|shortMessage
argument_list|,
literal|0
argument_list|,
name|segments
index|[
literal|0
index|]
operator|.
name|length
argument_list|)
block|}
return|;
case|case
name|REJECT
case|:
comment|// FIXME - JSMPP needs to have an enum of the negative response
comment|// codes instead of just using them like this
name|NegativeResponseException
name|nre
init|=
operator|new
name|NegativeResponseException
argument_list|(
name|SMPP_NEG_RESPONSE_MSG_TOO_LONG
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|SmppException
argument_list|(
name|nre
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|SmppException
argument_list|(
literal|"Unknown splitting policy: "
operator|+
name|policy
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|segments
return|;
block|}
block|}
DECL|method|getSplittingPolicy (Message message)
specifier|private
name|SmppSplittingPolicy
name|getSplittingPolicy
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|SmppException
block|{
if|if
condition|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|SPLITTING_POLICY
argument_list|)
condition|)
block|{
name|String
name|policyName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|SPLITTING_POLICY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|SmppSplittingPolicy
operator|.
name|fromString
argument_list|(
name|policyName
argument_list|)
return|;
block|}
return|return
name|config
operator|.
name|getSplittingPolicy
argument_list|()
return|;
block|}
DECL|method|createSplitter (Message message)
specifier|protected
name|SmppSplitter
name|createSplitter
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|SmppSplitter
name|splitter
decl_stmt|;
comment|// use the splitter if provided via header
if|if
condition|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|DATA_SPLITTER
argument_list|)
condition|)
block|{
name|splitter
operator|=
name|message
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|DATA_SPLITTER
argument_list|,
name|SmppSplitter
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|splitter
condition|)
block|{
return|return
name|splitter
return|;
block|}
name|logger
operator|.
name|warn
argument_list|(
literal|"Invalid splitter given. Must be instance of SmppSplitter"
argument_list|)
expr_stmt|;
block|}
name|Alphabet
name|alphabet
init|=
name|determineAlphabet
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|SmppUtils
operator|.
name|is8Bit
argument_list|(
name|alphabet
argument_list|)
condition|)
block|{
name|splitter
operator|=
operator|new
name|Smpp8BitSplitter
argument_list|(
name|body
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|alphabet
operator|==
name|Alphabet
operator|.
name|ALPHA_UCS2
condition|)
block|{
name|splitter
operator|=
operator|new
name|SmppUcs2Splitter
argument_list|(
name|body
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|splitter
operator|=
operator|new
name|SmppDefaultSplitter
argument_list|(
name|body
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|splitter
return|;
block|}
DECL|method|getShortMessage (Message message)
specifier|protected
specifier|final
name|byte
index|[]
name|getShortMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|has8bitDataCoding
argument_list|(
name|message
argument_list|)
condition|)
block|{
return|return
name|message
operator|.
name|getBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
return|;
block|}
else|else
block|{
name|byte
name|providedAlphabet
init|=
name|getProvidedAlphabet
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Alphabet
name|determinedAlphabet
init|=
name|determineAlphabet
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Charset
name|charset
init|=
name|determineCharset
argument_list|(
name|message
argument_list|,
name|providedAlphabet
argument_list|,
name|determinedAlphabet
operator|.
name|value
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|body
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
return|;
block|}
block|}
DECL|method|has8bitDataCoding (Message message)
specifier|private
specifier|static
name|boolean
name|has8bitDataCoding
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Byte
name|dcs
init|=
name|message
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
decl_stmt|;
if|if
condition|(
name|dcs
operator|!=
literal|null
condition|)
block|{
return|return
name|SmppUtils
operator|.
name|is8Bit
argument_list|(
name|Alphabet
operator|.
name|parseDataCoding
argument_list|(
name|dcs
operator|.
name|byteValue
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|Byte
name|alphabet
init|=
name|message
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
decl_stmt|;
return|return
name|alphabet
operator|!=
literal|null
operator|&&
name|SmppUtils
operator|.
name|is8Bit
argument_list|(
name|Alphabet
operator|.
name|valueOf
argument_list|(
name|alphabet
argument_list|)
argument_list|)
return|;
block|}
block|}
DECL|method|getProvidedAlphabet (Message message)
specifier|private
name|byte
name|getProvidedAlphabet
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|byte
name|alphabet
init|=
name|config
operator|.
name|getAlphabet
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
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
name|alphabet
operator|=
name|message
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
expr_stmt|;
block|}
return|return
name|alphabet
return|;
block|}
DECL|method|getCharsetForMessage (Message message)
specifier|private
name|Charset
name|getCharsetForMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|SmppConstants
operator|.
name|ENCODING
argument_list|)
condition|)
block|{
name|String
name|encoding
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|Charset
operator|.
name|isSupported
argument_list|(
name|encoding
argument_list|)
condition|)
block|{
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
return|;
block|}
else|else
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Unsupported encoding \"{}\" requested in header."
argument_list|,
name|encoding
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|determineCharset (Message message, byte providedAlphabet, byte determinedAlphabet)
specifier|private
name|Charset
name|determineCharset
parameter_list|(
name|Message
name|message
parameter_list|,
name|byte
name|providedAlphabet
parameter_list|,
name|byte
name|determinedAlphabet
parameter_list|)
block|{
name|Charset
name|result
init|=
name|getCharsetForMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
if|if
condition|(
name|providedAlphabet
operator|==
name|Alphabet
operator|.
name|ALPHA_UCS2
operator|.
name|value
argument_list|()
operator|||
operator|(
name|providedAlphabet
operator|==
name|SmppConstants
operator|.
name|UNKNOWN_ALPHABET
operator|&&
name|determinedAlphabet
operator|==
name|Alphabet
operator|.
name|ALPHA_UCS2
operator|.
name|value
argument_list|()
operator|)
condition|)
block|{
comment|// change charset to use multilang messages
return|return
name|Charset
operator|.
name|forName
argument_list|(
name|SmppConstants
operator|.
name|UCS2_ENCODING
argument_list|)
return|;
block|}
return|return
name|defaultCharset
return|;
block|}
DECL|method|determineAlphabet (Message message)
specifier|private
name|Alphabet
name|determineAlphabet
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|body
init|=
name|message
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|byte
name|alphabet
init|=
name|getProvidedAlphabet
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Charset
name|charset
init|=
name|getCharsetForMessage
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|charset
operator|=
name|defaultCharset
expr_stmt|;
block|}
name|Alphabet
name|alphabetObj
decl_stmt|;
if|if
condition|(
name|alphabet
operator|==
name|SmppConstants
operator|.
name|UNKNOWN_ALPHABET
condition|)
block|{
name|alphabetObj
operator|=
name|Alphabet
operator|.
name|ALPHA_UCS2
expr_stmt|;
if|if
condition|(
name|isLatin1Compatible
argument_list|(
name|charset
argument_list|)
condition|)
block|{
name|byte
index|[]
name|messageBytes
init|=
name|body
operator|.
name|getBytes
argument_list|(
name|charset
argument_list|)
decl_stmt|;
if|if
condition|(
name|SmppUtils
operator|.
name|isGsm0338Encodeable
argument_list|(
name|messageBytes
argument_list|)
condition|)
block|{
name|alphabetObj
operator|=
name|Alphabet
operator|.
name|ALPHA_DEFAULT
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|alphabetObj
operator|=
name|Alphabet
operator|.
name|valueOf
argument_list|(
name|alphabet
argument_list|)
expr_stmt|;
block|}
return|return
name|alphabetObj
return|;
block|}
DECL|method|isLatin1Compatible (Charset c)
specifier|private
name|boolean
name|isLatin1Compatible
parameter_list|(
name|Charset
name|c
parameter_list|)
block|{
if|if
condition|(
name|c
operator|.
name|equals
argument_list|(
name|ascii
argument_list|)
operator|||
name|c
operator|.
name|equals
argument_list|(
name|latin1
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

