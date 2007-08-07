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
name|javax
operator|.
name|mail
operator|.
name|Flags
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|Folder
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
name|event
operator|.
name|MessageCountEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|event
operator|.
name|MessageCountListener
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
name|Processor
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
name|ScheduledPollConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A {@link Consumer} which consumes messages from JavaMail using a  * {@link Transport} and dispatches them to the {@link Processor}  *   * @version $Revision: 523430 $  */
end_comment

begin_class
DECL|class|MailConsumer
specifier|public
class|class
name|MailConsumer
extends|extends
name|ScheduledPollConsumer
argument_list|<
name|MailExchange
argument_list|>
implements|implements
name|MessageCountListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|MailConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|MailEndpoint
name|endpoint
decl_stmt|;
DECL|field|folder
specifier|private
specifier|final
name|Folder
name|folder
decl_stmt|;
DECL|method|MailConsumer (MailEndpoint endpoint, Processor processor, Folder folder)
specifier|public
name|MailConsumer
parameter_list|(
name|MailEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Folder
name|folder
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|folder
operator|=
name|folder
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ensureFolderIsOpen
argument_list|()
expr_stmt|;
name|folder
operator|.
name|addMessageCountListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|folder
operator|.
name|removeMessageCountListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|folder
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|messagesAdded (MessageCountEvent event)
specifier|public
name|void
name|messagesAdded
parameter_list|(
name|MessageCountEvent
name|event
parameter_list|)
block|{
name|Message
index|[]
name|messages
init|=
name|event
operator|.
name|getMessages
argument_list|()
decl_stmt|;
for|for
control|(
name|Message
name|message
range|:
name|messages
control|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|message
operator|.
name|getFlags
argument_list|()
operator|.
name|contains
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|DELETED
argument_list|)
condition|)
block|{
name|processMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|flagMessageDeleted
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|messagesRemoved (MessageCountEvent event)
specifier|public
name|void
name|messagesRemoved
parameter_list|(
name|MessageCountEvent
name|event
parameter_list|)
block|{
name|Message
index|[]
name|messages
init|=
name|event
operator|.
name|getMessages
argument_list|()
decl_stmt|;
for|for
control|(
name|Message
name|message
range|:
name|messages
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Removing message: "
operator|+
name|message
operator|.
name|getSubject
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignored: "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|ensureFolderIsOpen
argument_list|()
expr_stmt|;
name|int
name|count
init|=
name|folder
operator|.
name|getMessageCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|Message
index|[]
name|messages
init|=
name|folder
operator|.
name|getMessages
argument_list|()
decl_stmt|;
name|MessageCountEvent
name|event
init|=
operator|new
name|MessageCountEvent
argument_list|(
name|folder
argument_list|,
name|MessageCountEvent
operator|.
name|ADDED
argument_list|,
literal|true
argument_list|,
name|messages
argument_list|)
decl_stmt|;
name|messagesAdded
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|count
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|MessagingException
argument_list|(
literal|"Folder: "
operator|+
name|folder
operator|.
name|getFullName
argument_list|()
operator|+
literal|" is closed"
argument_list|)
throw|;
block|}
name|folder
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|processMessage (Message message)
specifier|protected
name|void
name|processMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|MailExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ensureFolderIsOpen ()
specifier|protected
name|void
name|ensureFolderIsOpen
parameter_list|()
throws|throws
name|MessagingException
block|{
if|if
condition|(
operator|!
name|folder
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|folder
operator|.
name|open
argument_list|(
name|Folder
operator|.
name|READ_WRITE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|flagMessageDeleted (Message message)
specifier|protected
name|void
name|flagMessageDeleted
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|MessagingException
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDeleteProcessedMessages
argument_list|()
condition|)
block|{
name|message
operator|.
name|setFlag
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|DELETED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setFlag
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|SEEN
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

