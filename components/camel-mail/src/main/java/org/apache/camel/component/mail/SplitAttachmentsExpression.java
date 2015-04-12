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
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|support
operator|.
name|ExpressionAdapter
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Expression} which can be used to split a {@link MailMessage}  * per attachment. For example if a mail message has 5 attachments, then this  * expression will return a<tt>List&lt;Message&gt;</tt> that contains 5 {@link Message}.  * The message can be split 2 ways:  *<table>  *<tr>  *<td>As an attachment</td>  *<td>  *       The message is split into cloned messages, each has only one attachment.  The mail attachment in each message  *       remains unprocessed.  *</td>  *</tr>  *<tr>  *<td>As a byte[]</td>  *<td>  *       The attachments are split into new messages as the body. This allows the split messages to be easily used by  *       other processors / routes, as many other camel components can work on the byte[], e.g. it can be written to disk  *       using camel-file.  *</td>  *</tr>  *</table>  *  * In both cases the attachment name is written to a the camel header&quot;CamelSplitAttachmentId&quot;  */
end_comment

begin_class
DECL|class|SplitAttachmentsExpression
specifier|public
class|class
name|SplitAttachmentsExpression
extends|extends
name|ExpressionAdapter
block|{
DECL|field|HEADER_NAME
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_NAME
init|=
literal|"CamelSplitAttachmentId"
decl_stmt|;
DECL|field|extractAttachments
specifier|private
name|boolean
name|extractAttachments
init|=
literal|false
decl_stmt|;
DECL|method|SplitAttachmentsExpression ()
specifier|public
name|SplitAttachmentsExpression
parameter_list|()
block|{     }
DECL|method|SplitAttachmentsExpression (boolean extractAttachments)
specifier|public
name|SplitAttachmentsExpression
parameter_list|(
name|boolean
name|extractAttachments
parameter_list|)
block|{
name|this
operator|.
name|extractAttachments
operator|=
name|extractAttachments
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// must use getAttachments to ensure attachments is initial populated
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|List
argument_list|<
name|Message
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Message
argument_list|>
argument_list|()
decl_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
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
name|inMessage
operator|.
name|getAttachments
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Message
name|attachmentMessage
decl_stmt|;
if|if
condition|(
name|extractAttachments
condition|)
block|{
name|attachmentMessage
operator|=
name|extractAttachment
argument_list|(
name|inMessage
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|attachmentMessage
operator|=
name|splitAttachment
argument_list|(
name|inMessage
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|attachmentMessage
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|attachmentMessage
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
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
literal|"Unable to split attachments from MimeMultipart message"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|splitAttachment (Message inMessage, String attachmentName, DataHandler attachmentHandler)
specifier|private
name|Message
name|splitAttachment
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|String
name|attachmentName
parameter_list|,
name|DataHandler
name|attachmentHandler
parameter_list|)
block|{
specifier|final
name|Message
name|copy
init|=
name|inMessage
operator|.
name|copy
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
init|=
name|copy
operator|.
name|getAttachments
argument_list|()
decl_stmt|;
name|attachments
operator|.
name|clear
argument_list|()
expr_stmt|;
name|attachments
operator|.
name|put
argument_list|(
name|attachmentName
argument_list|,
name|attachmentHandler
argument_list|)
expr_stmt|;
name|copy
operator|.
name|setHeader
argument_list|(
name|HEADER_NAME
argument_list|,
name|attachmentName
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|extractAttachment (Message inMessage, String attachmentName)
specifier|private
name|Message
name|extractAttachment
parameter_list|(
name|Message
name|inMessage
parameter_list|,
name|String
name|attachmentName
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Message
name|outMessage
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|outMessage
operator|.
name|setHeader
argument_list|(
name|HEADER_NAME
argument_list|,
name|attachmentName
argument_list|)
expr_stmt|;
name|Object
name|attachment
init|=
name|inMessage
operator|.
name|getAttachment
argument_list|(
name|attachmentName
argument_list|)
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|attachment
operator|instanceof
name|InputStream
condition|)
block|{
name|outMessage
operator|.
name|setBody
argument_list|(
name|readMimePart
argument_list|(
operator|(
name|InputStream
operator|)
name|attachment
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|outMessage
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|readMimePart (InputStream mimePartStream)
specifier|private
name|byte
index|[]
name|readMimePart
parameter_list|(
name|InputStream
name|mimePartStream
parameter_list|)
throws|throws
name|Exception
block|{
comment|//  mimePartStream could be base64 encoded, or not, but we don't need to worry about it as
comment|// camel is smart enough to wrap it in a decoder stream (eg Base64DecoderStream) when required
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|int
name|len
decl_stmt|;
name|byte
index|[]
name|buf
init|=
operator|new
name|byte
index|[
literal|1024
index|]
decl_stmt|;
while|while
condition|(
operator|(
name|len
operator|=
name|mimePartStream
operator|.
name|read
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
literal|1024
argument_list|)
operator|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|bos
operator|.
name|write
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
name|mimePartStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|isExtractAttachments ()
specifier|public
name|boolean
name|isExtractAttachments
parameter_list|()
block|{
return|return
name|extractAttachments
return|;
block|}
DECL|method|setExtractAttachments (boolean extractAttachments)
specifier|public
name|void
name|setExtractAttachments
parameter_list|(
name|boolean
name|extractAttachments
parameter_list|)
block|{
name|this
operator|.
name|extractAttachments
operator|=
name|extractAttachments
expr_stmt|;
block|}
block|}
end_class

end_unit

