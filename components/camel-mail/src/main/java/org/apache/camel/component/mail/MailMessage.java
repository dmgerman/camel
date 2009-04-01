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
name|MailUtils
operator|.
name|dumpMessage
argument_list|(
name|mailMessage
argument_list|)
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
annotation|@
name|Override
DECL|method|getHeader (String name)
specifier|public
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|Object
name|answer
init|=
name|super
operator|.
name|getHeader
argument_list|(
name|name
argument_list|)
decl_stmt|;
comment|// mimic case insensitive search of mail message getHeader
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|super
operator|.
name|getHeader
argument_list|(
name|name
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
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
try|try
block|{
name|map
operator|.
name|putAll
argument_list|(
name|getExchange
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|extractHeadersFromMail
argument_list|(
name|mailMessage
argument_list|,
name|getExchange
argument_list|()
argument_list|)
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
name|RuntimeCamelException
argument_list|(
literal|"Error accessing headers due to: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
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
name|mailMessage
argument_list|,
name|map
argument_list|)
expr_stmt|;
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
literal|"Error populating the initial mail message attachments"
argument_list|,
name|e
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
comment|/**      * Parses the attachments of the given mail message and adds them to the map      *      * @param  message  the mail message with attachments      * @param  map      the map to add found attachments (attachmentFilename is the key)      */
DECL|method|extractAttachments (Message message, Map<String, DataHandler> map)
specifier|protected
specifier|static
name|void
name|extractAttachments
parameter_list|(
name|Message
name|message
parameter_list|,
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
throws|,
name|IOException
block|{
name|Object
name|content
init|=
name|message
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|content
operator|instanceof
name|Multipart
condition|)
block|{
name|extractFromMultipart
argument_list|(
operator|(
name|Multipart
operator|)
name|content
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|extractFromMultipart (Multipart mp, Map<String, DataHandler> map)
specifier|protected
specifier|static
name|void
name|extractFromMultipart
parameter_list|(
name|Multipart
name|mp
parameter_list|,
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
throws|,
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|mp
operator|.
name|getCount
argument_list|()
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
if|if
condition|(
name|part
operator|.
name|isMimeType
argument_list|(
literal|"multipart/*"
argument_list|)
condition|)
block|{
name|extractFromMultipart
argument_list|(
operator|(
name|Multipart
operator|)
name|part
operator|.
name|getContent
argument_list|()
argument_list|,
name|map
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
condition|)
block|{
if|if
condition|(
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
comment|// Parts marked with a disposition of Part.ATTACHMENT
comment|// are clearly attachments
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
name|part
operator|.
name|getDataHandler
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

