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
name|Multipart
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|DefaultMessage
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
comment|/**  * Represents a {@link org.apache.camel.Message} for working with Mail  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|MailMessage
specifier|public
class|class
name|MailMessage
extends|extends
name|DefaultMessage
block|{
DECL|field|mailMessage
specifier|private
name|Message
name|mailMessage
decl_stmt|;
DECL|method|MailMessage ()
specifier|public
name|MailMessage
parameter_list|()
block|{     }
DECL|method|MailMessage (Message message)
specifier|public
name|MailMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|this
operator|.
name|mailMessage
operator|=
name|message
expr_stmt|;
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
name|mailMessage
operator|!=
literal|null
condition|)
block|{
return|return
literal|"MailMessage: "
operator|+
name|mailMessage
return|;
block|}
else|else
block|{
return|return
literal|"MailMessage: "
operator|+
name|getBody
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExchange ()
specifier|public
name|MailExchange
name|getExchange
parameter_list|()
block|{
return|return
operator|(
name|MailExchange
operator|)
name|super
operator|.
name|getExchange
argument_list|()
return|;
block|}
DECL|method|copy ()
specifier|public
name|MailMessage
name|copy
parameter_list|()
block|{
name|MailMessage
name|answer
init|=
operator|(
name|MailMessage
operator|)
name|super
operator|.
name|copy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|mailMessage
operator|=
name|mailMessage
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Returns the underlying Mail message      */
DECL|method|getMessage ()
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|mailMessage
return|;
block|}
DECL|method|setMessage (Message mailMessage)
specifier|public
name|void
name|setMessage
parameter_list|(
name|Message
name|mailMessage
parameter_list|)
block|{
name|this
operator|.
name|mailMessage
operator|=
name|mailMessage
expr_stmt|;
block|}
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|String
index|[]
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|answer
operator|=
name|mailMessage
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageHeaderAccessException
argument_list|(
name|name
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
return|return
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
return|;
block|}
if|if
condition|(
name|answer
operator|.
name|length
operator|==
literal|1
condition|)
block|{
return|return
name|answer
index|[
literal|0
index|]
return|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|newInstance ()
specifier|public
name|MailMessage
name|newInstance
parameter_list|()
block|{
return|return
operator|new
name|MailMessage
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createBody ()
specifier|protected
name|Object
name|createBody
parameter_list|()
block|{
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
return|return
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractBodyFromMail
argument_list|(
name|getExchange
argument_list|()
argument_list|,
name|mailMessage
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|populateInitialHeaders (Map<String, Object> map)
specifier|protected
name|void
name|populateInitialHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
name|Enumeration
name|names
decl_stmt|;
try|try
block|{
name|names
operator|=
name|mailMessage
operator|.
name|getAllHeaders
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MessageHeaderNamesAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
try|try
block|{
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
name|value
init|=
name|header
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|header
operator|.
name|getName
argument_list|()
decl_stmt|;
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|map
argument_list|,
name|name
argument_list|,
name|value
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
name|MessageHeaderNamesAccessException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|populateInitialAttachments (Map<String, DataHandler> map)
specifier|protected
name|void
name|populateInitialAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|map
parameter_list|)
block|{
if|if
condition|(
name|mailMessage
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|extractAttachments
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeMailException
argument_list|(
literal|"Error populating the initial mail message attachments"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|copyFrom (org.apache.camel.Message that)
specifier|public
name|void
name|copyFrom
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|that
parameter_list|)
block|{
name|super
operator|.
name|copyFrom
argument_list|(
name|that
argument_list|)
expr_stmt|;
if|if
condition|(
name|that
operator|instanceof
name|MailMessage
condition|)
block|{
name|MailMessage
name|mailMessage
init|=
operator|(
name|MailMessage
operator|)
name|that
decl_stmt|;
name|this
operator|.
name|mailMessage
operator|=
name|mailMessage
operator|.
name|mailMessage
expr_stmt|;
block|}
block|}
comment|/**      * Parses the attachments of the mail message and puts them to the message      *      * @param map       the attachments map      * @throws javax.mail.MessagingException      */
DECL|method|extractAttachments (Map<String, DataHandler> map)
specifier|protected
name|void
name|extractAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|map
parameter_list|)
throws|throws
name|javax
operator|.
name|mail
operator|.
name|MessagingException
block|{
comment|// TODO: Reuse spring mail support to handle the attachment
comment|// now convert the mail attachments and put it to the msg
name|Multipart
name|mp
decl_stmt|;
name|Object
name|content
decl_stmt|;
try|try
block|{
name|content
operator|=
name|this
operator|.
name|mailMessage
operator|.
name|getContent
argument_list|()
expr_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|Multipart
condition|)
block|{
comment|// mail with attachment
name|mp
operator|=
operator|(
name|Multipart
operator|)
name|content
expr_stmt|;
name|int
name|nbMP
init|=
name|mp
operator|.
name|getCount
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
name|nbMP
condition|;
name|i
operator|++
control|)
block|{
name|Part
name|part
init|=
name|mp
operator|.
name|getBodyPart
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|disposition
init|=
name|part
operator|.
name|getDisposition
argument_list|()
decl_stmt|;
if|if
condition|(
name|disposition
operator|!=
literal|null
operator|&&
operator|(
name|disposition
operator|.
name|equalsIgnoreCase
argument_list|(
name|Part
operator|.
name|ATTACHMENT
argument_list|)
operator|||
name|disposition
operator|.
name|equalsIgnoreCase
argument_list|(
name|Part
operator|.
name|INLINE
argument_list|)
operator|)
condition|)
block|{
comment|// only add named attachments
if|if
condition|(
name|part
operator|.
name|getFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Parts marked with a disposition of
comment|// Part.ATTACHMENT
comment|// from part.getDisposition() are clearly
comment|// attachments
name|DataHandler
name|att
init|=
name|part
operator|.
name|getDataHandler
argument_list|()
decl_stmt|;
comment|// this is clearly a attachment
name|CollectionHelper
operator|.
name|appendValue
argument_list|(
name|map
argument_list|,
name|part
operator|.
name|getFileName
argument_list|()
argument_list|,
name|att
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|javax
operator|.
name|mail
operator|.
name|MessagingException
argument_list|(
literal|"Error while setting content on normalized message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|javax
operator|.
name|mail
operator|.
name|MessagingException
argument_list|(
literal|"Error while fetching content"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

