begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ses
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|ses
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Body
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Content
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendEmailRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendEmailResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendRawEmailRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|SendRawEmailResult
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
name|Endpoint
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
name|support
operator|.
name|DefaultProducer
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
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * A Producer which sends messages to the Amazon Simple Email Service  *<a href="http://aws.amazon.com/ses/">AWS SES</a>  */
end_comment

begin_class
DECL|class|SesProducer
specifier|public
class|class
name|SesProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|sesProducerToString
specifier|private
specifier|transient
name|String
name|sesProducerToString
decl_stmt|;
DECL|method|SesProducer (Endpoint endpoint)
specifier|public
name|SesProducer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
operator|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|instanceof
name|javax
operator|.
name|mail
operator|.
name|Message
operator|)
condition|)
block|{
name|SendEmailRequest
name|request
init|=
name|createMailRequest
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending request [{}] from exchange [{}]..."
argument_list|,
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|SendEmailResult
name|result
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSESClient
argument_list|()
operator|.
name|sendEmail
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|SendRawEmailRequest
name|request
init|=
name|createRawMailRequest
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending request [{}] from exchange [{}]..."
argument_list|,
name|request
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|SendRawEmailResult
name|result
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSESClient
argument_list|()
operator|.
name|sendRawEmail
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Received result [{}]"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|getMessageForResponse
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|SesConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|result
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createMailRequest (Exchange exchange)
specifier|private
name|SendEmailRequest
name|createMailRequest
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|SendEmailRequest
name|request
init|=
operator|new
name|SendEmailRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setSource
argument_list|(
name|determineFrom
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDestination
argument_list|(
name|determineTo
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setReturnPath
argument_list|(
name|determineReturnPath
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setReplyToAddresses
argument_list|(
name|determineReplyToAddresses
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setMessage
argument_list|(
name|createMessage
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
DECL|method|createRawMailRequest (Exchange exchange)
specifier|private
name|SendRawEmailRequest
name|createRawMailRequest
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SendRawEmailRequest
name|request
init|=
operator|new
name|SendRawEmailRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setSource
argument_list|(
name|determineFrom
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setDestinations
argument_list|(
name|determineRawTo
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|setRawMessage
argument_list|(
name|createRawMessage
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
DECL|method|createMessage (Exchange exchange)
specifier|private
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Message
name|createMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Message
name|message
init|=
operator|new
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|Message
argument_list|()
decl_stmt|;
name|Boolean
name|isHtmlEmail
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|HTML_EMAIL
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|content
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
if|if
condition|(
name|isHtmlEmail
condition|)
block|{
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|Body
argument_list|()
operator|.
name|withHtml
argument_list|(
operator|new
name|Content
argument_list|()
operator|.
name|withData
argument_list|(
name|content
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
operator|new
name|Body
argument_list|()
operator|.
name|withText
argument_list|(
operator|new
name|Content
argument_list|()
operator|.
name|withData
argument_list|(
name|content
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setSubject
argument_list|(
operator|new
name|Content
argument_list|(
name|determineSubject
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
DECL|method|createRawMessage (Exchange exchange)
specifier|private
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|RawMessage
name|createRawMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|RawMessage
name|message
init|=
operator|new
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|simpleemail
operator|.
name|model
operator|.
name|RawMessage
argument_list|()
decl_stmt|;
name|javax
operator|.
name|mail
operator|.
name|Message
name|content
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|javax
operator|.
name|mail
operator|.
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|OutputStream
name|byteOutput
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|content
operator|.
name|writeTo
argument_list|(
name|byteOutput
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Cannot write to byte Array"
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
name|byte
index|[]
name|messageByteArray
init|=
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|byteOutput
operator|)
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
name|message
operator|.
name|setData
argument_list|(
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|messageByteArray
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineReplyToAddresses (Exchange exchange)
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|determineReplyToAddresses
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|replyToAddresses
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|REPLY_TO_ADDRESSES
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyToAddresses
operator|==
literal|null
condition|)
block|{
name|replyToAddresses
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getReplyToAddresses
argument_list|()
expr_stmt|;
block|}
return|return
name|replyToAddresses
return|;
block|}
DECL|method|determineReturnPath (Exchange exchange)
specifier|private
name|String
name|determineReturnPath
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|returnPath
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|RETURN_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnPath
operator|==
literal|null
condition|)
block|{
name|returnPath
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getReturnPath
argument_list|()
expr_stmt|;
block|}
return|return
name|returnPath
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineTo (Exchange exchange)
specifier|private
name|Destination
name|determineTo
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|to
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|TO
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|to
operator|==
literal|null
condition|)
block|{
name|to
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getTo
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|Destination
argument_list|(
name|to
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|determineRawTo (Exchange exchange)
specifier|private
name|List
name|determineRawTo
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|to
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|TO
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|to
operator|==
literal|null
condition|)
block|{
name|to
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getTo
argument_list|()
expr_stmt|;
block|}
return|return
name|to
return|;
block|}
DECL|method|determineFrom (Exchange exchange)
specifier|private
name|String
name|determineFrom
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|from
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|FROM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|from
operator|==
literal|null
condition|)
block|{
name|from
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getFrom
argument_list|()
expr_stmt|;
block|}
return|return
name|from
return|;
block|}
DECL|method|determineSubject (Exchange exchange)
specifier|private
name|String
name|determineSubject
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|subject
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SesConstants
operator|.
name|SUBJECT
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|==
literal|null
condition|)
block|{
name|subject
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getSubject
argument_list|()
expr_stmt|;
block|}
return|return
name|subject
return|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|SesConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|sesProducerToString
operator|==
literal|null
condition|)
block|{
name|sesProducerToString
operator|=
literal|"SesProducer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|sesProducerToString
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SesEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SesEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getMessageForResponse (final Exchange exchange)
specifier|public
specifier|static
name|Message
name|getMessageForResponse
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|out
return|;
block|}
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
end_class

end_unit

