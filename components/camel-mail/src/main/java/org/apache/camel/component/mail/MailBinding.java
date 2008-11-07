begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mail
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mail
package|;
end_package

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
name|util
operator|.
name|Enumeration
import|;
end_import

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
name|Iterator
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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Address
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|BodyPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Header
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|MessagingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Part
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|InternetAddress
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeBodyPart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMessage
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|internet
operator|.
name|MimeMultipart
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|RuntimeCamelException
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
name|ObjectHelper
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
name|converter
operator|.
name|ObjectConverter
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
name|impl
operator|.
name|DefaultHeaderFilterStrategy
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
name|spi
operator|.
name|HeaderFilterStrategy
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
name|CollectionHelper
import|;
end_import

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@link Exchange} and {@link Message} to and  * from a Mail {@link MimeMessage}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MailBinding
specifier|public
class|class
name|MailBinding
block|{
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|MailBinding ()
specifier|public
name|MailBinding
parameter_list|()
block|{
name|headerFilterStrategy
operator|=
operator|new
name|DefaultHeaderFilterStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|MailBinding (HeaderFilterStrategy headerFilterStrategy)
specifier|public
name|MailBinding
parameter_list|(
name|HeaderFilterStrategy
name|headerFilterStrategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|headerFilterStrategy
expr_stmt|;
block|}
DECL|method|populateMailMessage (MailEndpoint endpoint, MimeMessage mimeMessage, Exchange exchange)
specifier|public
name|void
name|populateMailMessage
parameter_list|(
name|MailEndpoint
name|endpoint
parameter_list|,
name|MimeMessage
name|mimeMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|MessagingException
throws|,
name|IOException
block|{
comment|// camel message headers takes presedence over endpoint configuration
if|if
condition|(
name|hasRecipientHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
condition|)
block|{
name|setRecipientFromCamelMessage
argument_list|(
name|mimeMessage
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// fallback to endpoint configuration
name|setRecipientFromEndpointConfiguration
argument_list|(
name|mimeMessage
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
comment|// must have at least one recipients otherwise we do not know where to send the mail
if|if
condition|(
name|mimeMessage
operator|.
name|getAllRecipients
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The mail message does not have any recipients set."
argument_list|)
throw|;
block|}
comment|// append the rest of the headers (no recipients) that could be subject, reply-to etc.
name|appendHeadersFromCamelMessage
argument_list|(
name|mimeMessage
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|empty
argument_list|(
name|mimeMessage
operator|.
name|getFrom
argument_list|()
argument_list|)
condition|)
block|{
comment|// lets default the address to the endpoint destination
name|String
name|from
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFrom
argument_list|()
decl_stmt|;
name|mimeMessage
operator|.
name|setFrom
argument_list|(
operator|new
name|InternetAddress
argument_list|(
name|from
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasAttachments
argument_list|()
condition|)
block|{
name|appendAttachmentsFromCamel
argument_list|(
name|mimeMessage
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
literal|"text/html"
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getContentType
argument_list|()
argument_list|)
condition|)
block|{
name|DataSource
name|ds
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
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
argument_list|,
literal|"text/html"
argument_list|)
decl_stmt|;
name|mimeMessage
operator|.
name|setDataHandler
argument_list|(
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// its just text/plain
name|mimeMessage
operator|.
name|setText
argument_list|(
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
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Extracts the body from the Mail message      */
DECL|method|extractBodyFromMail (MailExchange exchange, Message message)
specifier|public
name|Object
name|extractBodyFromMail
parameter_list|(
name|MailExchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
return|return
name|message
operator|.
name|getContent
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to extract body due to: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". Exchange: "
operator|+
name|exchange
operator|+
literal|". Message: "
operator|+
name|message
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Appends the Mail headers from the Camel {@link MailMessage}      */
DECL|method|appendHeadersFromCamelMessage (MimeMessage mimeMessage, Exchange exchange, org.apache.camel.Message camelMessage)
specifier|protected
name|void
name|appendHeadersFromCamelMessage
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
throws|throws
name|MessagingException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|camelMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|headerName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|headerValue
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToCamelHeaders
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
condition|)
block|{
if|if
condition|(
name|isRecipientHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
comment|// skip any recipients as they are handled specially
continue|continue;
block|}
comment|// Mail messages can repeat the same header...
if|if
condition|(
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
name|headerValue
argument_list|)
condition|)
block|{
name|Iterator
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|headerValue
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|mimeMessage
operator|.
name|addHeader
argument_list|(
name|headerName
argument_list|,
name|asString
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|mimeMessage
operator|.
name|setHeader
argument_list|(
name|headerName
argument_list|,
name|asString
argument_list|(
name|exchange
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|setRecipientFromCamelMessage (MimeMessage mimeMessage, Exchange exchange, org.apache.camel.Message camelMessage)
specifier|private
name|void
name|setRecipientFromCamelMessage
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
throws|throws
name|MessagingException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|camelMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|headerName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|Object
name|headerValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|headerValue
operator|!=
literal|null
operator|&&
name|isRecipientHeader
argument_list|(
name|headerName
argument_list|)
condition|)
block|{
comment|// special handling of recipients
if|if
condition|(
name|ObjectConverter
operator|.
name|isCollection
argument_list|(
name|headerValue
argument_list|)
condition|)
block|{
name|Iterator
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|headerValue
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|recipient
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|headerName
argument_list|,
name|asString
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|headerName
argument_list|,
name|asString
argument_list|(
name|exchange
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Appends the Mail headers from the endpoint configuraiton.      */
DECL|method|setRecipientFromEndpointConfiguration (MimeMessage mimeMessage, MailEndpoint endpoint)
specifier|protected
name|void
name|setRecipientFromEndpointConfiguration
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|,
name|MailEndpoint
name|endpoint
parameter_list|)
throws|throws
name|MessagingException
block|{
name|Map
argument_list|<
name|Message
operator|.
name|RecipientType
argument_list|,
name|String
argument_list|>
name|recipients
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRecipients
argument_list|()
decl_stmt|;
if|if
condition|(
name|recipients
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
condition|)
block|{
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|Message
operator|.
name|RecipientType
operator|.
name|TO
operator|.
name|toString
argument_list|()
argument_list|,
name|recipients
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recipients
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|)
condition|)
block|{
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|Message
operator|.
name|RecipientType
operator|.
name|CC
operator|.
name|toString
argument_list|()
argument_list|,
name|recipients
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recipients
operator|.
name|containsKey
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
argument_list|)
condition|)
block|{
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
operator|.
name|toString
argument_list|()
argument_list|,
name|recipients
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// fallback to use destination if no TO provided at all
name|String
name|destination
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDestination
argument_list|()
decl_stmt|;
if|if
condition|(
name|destination
operator|!=
literal|null
operator|&&
name|mimeMessage
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
operator|==
literal|null
condition|)
block|{
name|appendRecipientToMimeMessage
argument_list|(
name|mimeMessage
argument_list|,
name|Message
operator|.
name|RecipientType
operator|.
name|TO
operator|.
name|toString
argument_list|()
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends the Mail attachments from the Camel {@link MailMessage}      */
DECL|method|appendAttachmentsFromCamel (MimeMessage mimeMessage, org.apache.camel.Message camelMessage, MailConfiguration configuration)
specifier|protected
name|void
name|appendAttachmentsFromCamel
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|,
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|MailConfiguration
name|configuration
parameter_list|)
throws|throws
name|MessagingException
block|{
comment|// Create a Multipart
name|MimeMultipart
name|multipart
init|=
operator|new
name|MimeMultipart
argument_list|()
decl_stmt|;
comment|// fill the body with text
name|multipart
operator|.
name|setSubType
argument_list|(
literal|"mixed"
argument_list|)
expr_stmt|;
name|MimeBodyPart
name|textBodyPart
init|=
operator|new
name|MimeBodyPart
argument_list|()
decl_stmt|;
name|textBodyPart
operator|.
name|setContent
argument_list|(
name|camelMessage
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|configuration
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
name|multipart
operator|.
name|addBodyPart
argument_list|(
name|textBodyPart
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|entry
range|:
name|camelMessage
operator|.
name|getAttachments
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|attachmentFilename
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataHandler
name|handler
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|shouldOutputAttachment
argument_list|(
name|camelMessage
argument_list|,
name|attachmentFilename
argument_list|,
name|handler
argument_list|)
condition|)
block|{
comment|// Create another body part
name|BodyPart
name|messageBodyPart
init|=
operator|new
name|MimeBodyPart
argument_list|()
decl_stmt|;
comment|// Set the data handler to the attachment
name|messageBodyPart
operator|.
name|setDataHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
comment|// Set the filename
name|messageBodyPart
operator|.
name|setFileName
argument_list|(
name|attachmentFilename
argument_list|)
expr_stmt|;
comment|// Set Disposition
name|messageBodyPart
operator|.
name|setDisposition
argument_list|(
name|Part
operator|.
name|ATTACHMENT
argument_list|)
expr_stmt|;
comment|// Add part to multipart
name|multipart
operator|.
name|addBodyPart
argument_list|(
name|messageBodyPart
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Put parts in message
name|mimeMessage
operator|.
name|setContent
argument_list|(
name|multipart
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy to allow filtering of attachments which are put on the Mail message      */
DECL|method|shouldOutputAttachment (org.apache.camel.Message camelMessage, String attachmentFilename, DataHandler handler)
specifier|protected
name|boolean
name|shouldOutputAttachment
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|,
name|String
name|attachmentFilename
parameter_list|,
name|DataHandler
name|handler
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|extractHeadersFromMail (Message mailMessage)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|extractHeadersFromMail
parameter_list|(
name|Message
name|mailMessage
parameter_list|)
throws|throws
name|MessagingException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|answer
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Enumeration
name|names
init|=
name|mailMessage
operator|.
name|getAllHeaders
argument_list|()
decl_stmt|;
while|while
condition|(
name|names
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
operator|(
name|Header
operator|)
name|names
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|String
index|[]
name|value
init|=
name|mailMessage
operator|.
name|getHeader
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerFilterStrategy
operator|!=
literal|null
operator|&&
operator|!
name|headerFilterStrategy
operator|.
name|applyFilterToExternalHeaders
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
condition|)
block|{
comment|// toLowerCase() for doing case insensitive search
if|if
condition|(
name|value
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|answer
argument_list|,
name|header
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|value
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|answer
argument_list|,
name|header
operator|.
name|getName
argument_list|()
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|appendRecipientToMimeMessage (MimeMessage mimeMessage, String type, String recipient)
specifier|private
specifier|static
name|void
name|appendRecipientToMimeMessage
parameter_list|(
name|MimeMessage
name|mimeMessage
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|recipient
parameter_list|)
throws|throws
name|MessagingException
block|{
comment|// we support that multi recipient can be given as a string seperated by comma or semi colon
name|String
index|[]
name|lines
init|=
name|recipient
operator|.
name|split
argument_list|(
literal|"[,|;]"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|line
range|:
name|lines
control|)
block|{
name|line
operator|=
name|line
operator|.
name|trim
argument_list|()
expr_stmt|;
name|mimeMessage
operator|.
name|addRecipients
argument_list|(
name|asRecipientType
argument_list|(
name|type
argument_list|)
argument_list|,
name|line
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Does the given camel message contain any To, CC or BCC header names?      */
DECL|method|hasRecipientHeaders (org.apache.camel.Message camelMessage)
specifier|private
specifier|static
name|boolean
name|hasRecipientHeaders
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|camelMessage
parameter_list|)
block|{
for|for
control|(
name|String
name|key
range|:
name|camelMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|isRecipientHeader
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * Is the given key a mime message recipient header (To, CC or BCC)      */
DECL|method|isRecipientHeader (String key)
specifier|private
specifier|static
name|boolean
name|isRecipientHeader
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
elseif|else
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|key
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
comment|/**      * Returns the RecipientType object.      */
DECL|method|asRecipientType (String type)
specifier|private
specifier|static
name|Message
operator|.
name|RecipientType
name|asRecipientType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Message
operator|.
name|RecipientType
operator|.
name|TO
return|;
block|}
elseif|else
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|CC
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Message
operator|.
name|RecipientType
operator|.
name|CC
return|;
block|}
elseif|else
if|if
condition|(
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
operator|.
name|toString
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|Message
operator|.
name|RecipientType
operator|.
name|BCC
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown recipient type: "
operator|+
name|type
argument_list|)
throw|;
block|}
DECL|method|empty (Address[] addresses)
specifier|private
specifier|static
name|boolean
name|empty
parameter_list|(
name|Address
index|[]
name|addresses
parameter_list|)
block|{
return|return
name|addresses
operator|==
literal|null
operator|||
name|addresses
operator|.
name|length
operator|==
literal|0
return|;
block|}
DECL|method|asString (Exchange exchange, Object value)
specifier|private
specifier|static
name|String
name|asString
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
end_class

end_unit

