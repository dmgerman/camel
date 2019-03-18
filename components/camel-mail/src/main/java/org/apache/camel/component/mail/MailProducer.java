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
name|io
operator|.
name|IOException
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
name|internet
operator|.
name|MimeMessage
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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_comment
comment|/**  * A Producer to send messages using JavaMail.  */
end_comment

begin_class
DECL|class|MailProducer
specifier|public
class|class
name|MailProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|sender
specifier|private
specifier|final
name|JavaMailSender
name|sender
decl_stmt|;
DECL|method|MailProducer (MailEndpoint endpoint, JavaMailSender sender)
specifier|public
name|MailProducer
parameter_list|(
name|MailEndpoint
name|endpoint
parameter_list|,
name|JavaMailSender
name|sender
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|ClassLoader
name|tccl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|ClassLoader
name|applicationClassLoader
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|applicationClassLoader
operator|!=
literal|null
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|applicationClassLoader
argument_list|)
expr_stmt|;
block|}
name|MimeMessage
name|mimeMessage
decl_stmt|;
specifier|final
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|MimeMessage
condition|)
block|{
comment|// Body is directly a MimeMessage
name|mimeMessage
operator|=
operator|(
name|MimeMessage
operator|)
name|body
expr_stmt|;
block|}
else|else
block|{
comment|// Create a message with exchange data
name|mimeMessage
operator|=
operator|new
name|MimeMessage
argument_list|(
name|sender
operator|.
name|getSession
argument_list|()
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|populateMailMessage
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|mimeMessage
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending MimeMessage: {}"
argument_list|,
name|MailUtils
operator|.
name|dumpMessage
argument_list|(
name|mimeMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|sender
operator|.
name|send
argument_list|(
name|mimeMessage
argument_list|)
expr_stmt|;
comment|// set the message ID for further processing
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MailConstants
operator|.
name|MAIL_MESSAGE_ID
argument_list|,
name|mimeMessage
operator|.
name|getMessageID
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|tccl
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MailEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MailEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

