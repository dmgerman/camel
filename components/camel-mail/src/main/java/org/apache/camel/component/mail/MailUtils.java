begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|text
operator|.
name|DateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|util
operator|.
name|StringHelper
import|;
end_import

begin_comment
comment|/**  * Mail utility class.  *<p>  * Parts of the code copied from Apache ServiceMix.  */
end_comment

begin_class
DECL|class|MailUtils
specifier|public
specifier|final
class|class
name|MailUtils
block|{
DECL|field|DEFAULT_PORT_SMTP
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_SMTP
init|=
literal|25
decl_stmt|;
DECL|field|DEFAULT_PORT_SMTPS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_SMTPS
init|=
literal|465
decl_stmt|;
DECL|field|DEFAULT_PORT_POP3
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_POP3
init|=
literal|110
decl_stmt|;
DECL|field|DEFAULT_PORT_POP3S
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_POP3S
init|=
literal|995
decl_stmt|;
DECL|field|DEFAULT_PORT_NNTP
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_NNTP
init|=
literal|119
decl_stmt|;
DECL|field|DEFAULT_PORT_IMAP
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_IMAP
init|=
literal|143
decl_stmt|;
DECL|field|DEFAULT_PORT_IMAPS
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT_IMAPS
init|=
literal|993
decl_stmt|;
DECL|field|PROTOCOL_SMTP
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_SMTP
init|=
literal|"smtp"
decl_stmt|;
DECL|field|PROTOCOL_SMTPS
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_SMTPS
init|=
literal|"smtps"
decl_stmt|;
DECL|field|PROTOCOL_POP3
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_POP3
init|=
literal|"pop3"
decl_stmt|;
DECL|field|PROTOCOL_POP3S
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_POP3S
init|=
literal|"pop3s"
decl_stmt|;
DECL|field|PROTOCOL_NNTP
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_NNTP
init|=
literal|"nntp"
decl_stmt|;
DECL|field|PROTOCOL_IMAP
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_IMAP
init|=
literal|"imap"
decl_stmt|;
DECL|field|PROTOCOL_IMAPS
specifier|public
specifier|static
specifier|final
name|String
name|PROTOCOL_IMAPS
init|=
literal|"imaps"
decl_stmt|;
DECL|method|MailUtils ()
specifier|private
name|MailUtils
parameter_list|()
block|{     }
comment|/**      * Returns the default port for a given protocol.      *<p>      * If a protocol could not successfully be determined the default port number for SMTP protocol is returned.      *      * @param protocol the protocol      * @return the default port      */
DECL|method|getDefaultPortForProtocol (final String protocol)
specifier|public
specifier|static
name|int
name|getDefaultPortForProtocol
parameter_list|(
specifier|final
name|String
name|protocol
parameter_list|)
block|{
name|int
name|port
init|=
name|DEFAULT_PORT_SMTP
decl_stmt|;
if|if
condition|(
name|protocol
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_IMAP
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_IMAP
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_IMAPS
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_IMAPS
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_NNTP
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_NNTP
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_POP3
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_POP3
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_POP3S
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_POP3S
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_SMTP
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_SMTP
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|protocol
operator|.
name|equalsIgnoreCase
argument_list|(
name|PROTOCOL_SMTPS
argument_list|)
condition|)
block|{
name|port
operator|=
name|DEFAULT_PORT_SMTPS
expr_stmt|;
block|}
else|else
block|{
name|port
operator|=
name|DEFAULT_PORT_SMTP
expr_stmt|;
block|}
block|}
return|return
name|port
return|;
block|}
comment|/**      * Gets a log dump of the given message that can be used for tracing etc.      *      * @param message the Mail message      * @return a log string with important fields dumped      */
DECL|method|dumpMessage (Message message)
specifier|public
specifier|static
name|String
name|dumpMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|"null"
return|;
block|}
try|try
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|int
name|number
init|=
name|message
operator|.
name|getMessageNumber
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"messageNumber=["
argument_list|)
operator|.
name|append
argument_list|(
name|number
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
name|Address
index|[]
name|from
init|=
name|message
operator|.
name|getFrom
argument_list|()
decl_stmt|;
if|if
condition|(
name|from
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Address
name|adr
range|:
name|from
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", from=["
argument_list|)
operator|.
name|append
argument_list|(
name|adr
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
name|Address
index|[]
name|to
init|=
name|message
operator|.
name|getRecipients
argument_list|(
name|Message
operator|.
name|RecipientType
operator|.
name|TO
argument_list|)
decl_stmt|;
if|if
condition|(
name|to
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Address
name|adr
range|:
name|to
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", to=["
argument_list|)
operator|.
name|append
argument_list|(
name|adr
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|subject
init|=
name|message
operator|.
name|getSubject
argument_list|()
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", subject=["
argument_list|)
operator|.
name|append
argument_list|(
name|subject
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|Date
name|sentDate
init|=
name|message
operator|.
name|getSentDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|sentDate
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", sentDate=["
argument_list|)
operator|.
name|append
argument_list|(
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|()
operator|.
name|format
argument_list|(
name|sentDate
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
name|Date
name|receivedDate
init|=
name|message
operator|.
name|getReceivedDate
argument_list|()
decl_stmt|;
if|if
condition|(
name|receivedDate
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", receivedDate=["
argument_list|)
operator|.
name|append
argument_list|(
name|DateFormat
operator|.
name|getDateTimeInstance
argument_list|()
operator|.
name|format
argument_list|(
name|receivedDate
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
comment|// ignore the error and just return tostring
return|return
name|message
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
comment|/**      * Pads the content-type so it has a space after semi colon that separate pairs.      *<p/>      * This is needed as some mail servers will choke otherwise      *      * @param contentType the content type      * @return the padded content type      */
DECL|method|padContentType (String contentType)
specifier|public
specifier|static
name|String
name|padContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|contentType
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
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
name|parts
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|part
init|=
name|parts
index|[
name|i
index|]
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|part
argument_list|)
condition|)
block|{
name|part
operator|=
name|part
operator|.
name|trim
argument_list|()
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|part
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|parts
operator|.
name|length
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Replaces the charset in the content-type      *      * @param contentType the content-type      * @param charset  the charset to replace, can be<tt>null</tt> to remove charset      * @return the content-type with replaced charset      */
DECL|method|replaceCharSet (String contentType, String charset)
specifier|public
specifier|static
name|String
name|replaceCharSet
parameter_list|(
name|String
name|contentType
parameter_list|,
name|String
name|charset
parameter_list|)
block|{
name|boolean
name|replaced
init|=
literal|false
decl_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|parts
init|=
name|contentType
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|part
operator|=
name|part
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|part
operator|.
name|startsWith
argument_list|(
literal|"charset"
argument_list|)
condition|)
block|{
name|part
operator|=
name|part
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
comment|// replace with new charset
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"charset="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|charset
argument_list|)
expr_stmt|;
name|replaced
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// if we did not replace any existing charset, then append new charset at the end
if|if
condition|(
operator|!
name|replaced
operator|&&
name|charset
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"; "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"charset="
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|charset
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Gets the charset from the content-type      *      * @param contentType the content-type      * @return the charset, or<tt>null</tt> if no charset existed      */
DECL|method|getCharSetFromContentType (String contentType)
specifier|public
specifier|static
name|String
name|getCharSetFromContentType
parameter_list|(
name|String
name|contentType
parameter_list|)
block|{
if|if
condition|(
name|contentType
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
index|[]
name|parts
init|=
name|contentType
operator|.
name|split
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|part
range|:
name|parts
control|)
block|{
name|part
operator|=
name|part
operator|.
name|trim
argument_list|()
expr_stmt|;
if|if
condition|(
name|part
operator|.
name|startsWith
argument_list|(
literal|"charset"
argument_list|)
condition|)
block|{
return|return
name|StringHelper
operator|.
name|after
argument_list|(
name|part
argument_list|,
literal|"charset="
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

