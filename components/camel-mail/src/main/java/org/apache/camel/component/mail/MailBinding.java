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
name|java
operator|.
name|util
operator|.
name|Set
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
name|converter
operator|.
name|ObjectConverter
import|;
end_import

begin_comment
comment|/**  * A Strategy used to convert between a Camel {@link Exchange} and {@link Message} to and  * from a Mail {@link MimeMessage}  *   * @version $Revision$  */
end_comment

begin_class
DECL|class|MailBinding
specifier|public
class|class
name|MailBinding
block|{
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
block|{
try|try
block|{
name|appendHeadersFromCamel
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
condition|)
block|{
name|mimeMessage
operator|.
name|setRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
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
name|getAttachments
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|appendAttachmentsFromCamel
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
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeMailException
argument_list|(
literal|"Failed to populate body due to: "
operator|+
name|e
operator|+
literal|". Exchange: "
operator|+
name|exchange
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|empty (Address[] addresses)
specifier|protected
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
comment|/**      * Extracts the body from the Mail message      *       * @param exchange      * @param message      */
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
name|RuntimeMailException
argument_list|(
literal|"Failed to extract body due to: "
operator|+
name|e
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
DECL|method|appendHeadersFromCamel (MimeMessage mimeMessage, Exchange exchange, org.apache.camel.Message camelMessage)
specifier|protected
name|void
name|appendHeadersFromCamel
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
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|entries
init|=
name|camelMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|entries
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
name|shouldOutputHeader
argument_list|(
name|camelMessage
argument_list|,
name|headerName
argument_list|,
name|headerValue
argument_list|)
condition|)
block|{
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
name|ObjectConverter
operator|.
name|iterator
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
comment|/**      * Appends the Mail attachments from the Camel {@link MailMessage}      */
DECL|method|appendAttachmentsFromCamel (MimeMessage mimeMessage, Exchange exchange, org.apache.camel.Message camelMessage)
specifier|protected
name|void
name|appendAttachmentsFromCamel
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
literal|"text/plain"
argument_list|)
expr_stmt|;
name|multipart
operator|.
name|addBodyPart
argument_list|(
name|textBodyPart
argument_list|)
expr_stmt|;
name|BodyPart
name|messageBodyPart
init|=
literal|null
decl_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
argument_list|>
name|entries
init|=
name|camelMessage
operator|.
name|getAttachments
argument_list|()
operator|.
name|entrySet
argument_list|()
decl_stmt|;
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
name|entries
control|)
block|{
name|String
name|attName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataHandler
name|attValue
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|attValue
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
name|attName
argument_list|,
name|attValue
argument_list|)
condition|)
block|{
comment|// Create another body part
name|messageBodyPart
operator|=
operator|new
name|MimeBodyPart
argument_list|()
expr_stmt|;
comment|// Set the data handler to the attachment
name|messageBodyPart
operator|.
name|setDataHandler
argument_list|(
name|attValue
argument_list|)
expr_stmt|;
comment|// Set the filename
name|messageBodyPart
operator|.
name|setFileName
argument_list|(
name|attName
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
comment|/**      * Converts the given object value to a String      */
DECL|method|asString (Exchange exchange, Object value)
specifier|protected
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
name|value
argument_list|)
return|;
block|}
comment|/**      * Strategy to allow filtering of headers which are put on the Mail message      */
DECL|method|shouldOutputHeader (org.apache.camel.Message camelMessage, String headerName, Object headerValue)
specifier|protected
name|boolean
name|shouldOutputHeader
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
name|headerName
parameter_list|,
name|Object
name|headerValue
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Strategy to allow filtering of attachments which are put on the Mail      * message      */
DECL|method|shouldOutputAttachment (org.apache.camel.Message camelMessage, String headerName, DataHandler headerValue)
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
name|headerName
parameter_list|,
name|DataHandler
name|headerValue
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

