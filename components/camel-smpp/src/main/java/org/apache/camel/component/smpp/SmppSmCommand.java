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
name|Alphabet
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
DECL|class|SmppSmCommand
specifier|public
specifier|abstract
class|class
name|SmppSmCommand
extends|extends
name|AbstractSmppCommand
block|{
DECL|field|charset
specifier|protected
name|Charset
name|charset
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
name|this
operator|.
name|charset
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
DECL|method|getProvidedAlphabet (Exchange exchange)
specifier|protected
name|byte
name|getProvidedAlphabet
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
name|byte
name|alphabet
init|=
name|SmppConstants
operator|.
name|UNKNOWN_ALPHABET
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
name|ALPHABET
argument_list|)
condition|)
block|{
name|alphabet
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|alphabet
operator|=
name|config
operator|.
name|getAlphabet
argument_list|()
expr_stmt|;
block|}
return|return
name|alphabet
return|;
block|}
DECL|method|determineCharset (byte providedAlphabet, byte determinedAlphabet)
specifier|protected
name|Charset
name|determineCharset
parameter_list|(
name|byte
name|providedAlphabet
parameter_list|,
name|byte
name|determinedAlphabet
parameter_list|)
block|{
if|if
condition|(
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
condition|)
block|{
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
comment|// change charset to use multilang messages
block|}
return|return
name|charset
return|;
block|}
DECL|method|determineAlphabet (Exchange exchange)
specifier|protected
name|Alphabet
name|determineAlphabet
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
name|alphabet
init|=
name|getProvidedAlphabet
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
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
name|byte
index|[]
name|message
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
name|message
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
else|else
block|{
name|alphabetObj
operator|=
name|Alphabet
operator|.
name|ALPHA_UCS2
expr_stmt|;
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
DECL|method|createSplitter (Exchange exchange)
specifier|protected
name|SmppSplitter
name|createSplitter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Alphabet
name|alphabet
init|=
name|determineAlphabet
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|body
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppSplitter
name|splitter
decl_stmt|;
switch|switch
condition|(
name|alphabet
condition|)
block|{
case|case
name|ALPHA_8_BIT
case|:
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
break|break;
case|case
name|ALPHA_UCS2
case|:
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
break|break;
case|case
name|ALPHA_DEFAULT
case|:
default|default:
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
break|break;
block|}
return|return
name|splitter
return|;
block|}
block|}
end_class

end_unit

