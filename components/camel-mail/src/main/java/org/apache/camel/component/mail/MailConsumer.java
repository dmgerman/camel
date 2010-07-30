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
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

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
name|FolderNotFoundException
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
name|Store
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|search
operator|.
name|FlagTerm
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
name|BatchConsumer
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
name|ShutdownRunningTask
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
name|camel
operator|.
name|spi
operator|.
name|ShutdownAware
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
name|Synchronization
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
name|CastUtils
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mail
operator|.
name|javamail
operator|.
name|JavaMailSenderImpl
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Consumer Consumer} which consumes messages from JavaMail using a  * {@link javax.mail.Transport Transport} and dispatches them to the {@link Processor}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|MailConsumer
specifier|public
class|class
name|MailConsumer
extends|extends
name|ScheduledPollConsumer
implements|implements
name|BatchConsumer
implements|,
name|ShutdownAware
block|{
DECL|field|DEFAULT_CONSUMER_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CONSUMER_DELAY
init|=
literal|60
operator|*
literal|1000L
decl_stmt|;
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
DECL|field|sender
specifier|private
specifier|final
name|JavaMailSenderImpl
name|sender
decl_stmt|;
DECL|field|folder
specifier|private
name|Folder
name|folder
decl_stmt|;
DECL|field|store
specifier|private
name|Store
name|store
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|field|shutdownRunningTask
specifier|private
specifier|volatile
name|ShutdownRunningTask
name|shutdownRunningTask
decl_stmt|;
DECL|field|pendingExchanges
specifier|private
specifier|volatile
name|int
name|pendingExchanges
decl_stmt|;
DECL|method|MailConsumer (MailEndpoint endpoint, Processor processor, JavaMailSenderImpl sender)
specifier|public
name|MailConsumer
parameter_list|(
name|MailEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|JavaMailSenderImpl
name|sender
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
name|sender
operator|=
name|sender
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
if|if
condition|(
name|folder
operator|!=
literal|null
operator|&&
name|folder
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|folder
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|store
operator|!=
literal|null
operator|&&
name|store
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|store
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|poll ()
specifier|protected
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must reset for each poll
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|ensureIsConnected
argument_list|()
expr_stmt|;
if|if
condition|(
name|store
operator|==
literal|null
operator|||
name|folder
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"MailConsumer did not connect properly to the MailStore: "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Polling mailfolder: "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFetchSize
argument_list|()
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Fetch size is 0 meaning the configuration is set to poll no new messages at all. Camel will skip this poll."
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// ensure folder is open
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
try|try
block|{
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
decl_stmt|;
comment|// should we process all messages or only unseen messages
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUnseen
argument_list|()
condition|)
block|{
name|messages
operator|=
name|folder
operator|.
name|search
argument_list|(
operator|new
name|FlagTerm
argument_list|(
operator|new
name|Flags
argument_list|(
name|Flags
operator|.
name|Flag
operator|.
name|SEEN
argument_list|)
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messages
operator|=
name|folder
operator|.
name|getMessages
argument_list|()
expr_stmt|;
block|}
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|createExchanges
argument_list|(
name|messages
argument_list|)
argument_list|)
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// need to ensure we release resources
try|try
block|{
if|if
condition|(
name|folder
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|folder
operator|.
name|close
argument_list|(
literal|true
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
comment|// some mail servers will lock the folder so we ignore in this case (CAMEL-1263)
name|LOG
operator|.
name|debug
argument_list|(
literal|"Could not close mailbox folder: "
operator|+
name|folder
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|void
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// limit if needed
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
operator|&&
name|total
operator|>
name|maxMessagesPerPoll
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Limiting to maximum messages to poll "
operator|+
name|maxMessagesPerPoll
operator|+
literal|" as there was "
operator|+
name|total
operator|+
literal|" messages in this poll."
argument_list|)
expr_stmt|;
block|}
name|total
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
comment|// only loop if we are started (allowed to run)
name|Exchange
name|exchange
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
comment|// add current index and total as properties
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
comment|// must use the original message in case we need to workaround a charset issue when extracting mail content
specifier|final
name|Message
name|mail
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|MailMessage
operator|.
name|class
argument_list|)
operator|.
name|getOriginalMessage
argument_list|()
decl_stmt|;
comment|// add on completion to handle after work when the exchange is done
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processCommit
argument_list|(
name|mail
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|processRollback
argument_list|(
name|mail
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"MailConsumerOnCompletion"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// process the exchange
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// store a reference what to do in case when shutting down and we have pending messages
name|this
operator|.
name|shutdownRunningTask
operator|=
name|shutdownRunningTask
expr_stmt|;
comment|// do not defer shutdown
return|return
literal|false
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
comment|// only return the real pending size in case we are configured to complete all tasks
if|if
condition|(
name|ShutdownRunningTask
operator|.
name|CompleteAllTasks
operator|==
name|shutdownRunningTask
condition|)
block|{
return|return
name|pendingExchanges
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|isBatchAllowed ()
specifier|public
name|boolean
name|isBatchAllowed
parameter_list|()
block|{
comment|// stop if we are not running
name|boolean
name|answer
init|=
name|isRunAllowed
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|answer
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|shutdownRunningTask
operator|==
literal|null
condition|)
block|{
comment|// we are not shutting down so continue to run
return|return
literal|true
return|;
block|}
comment|// we are shutting down so only continue if we are configured to complete all tasks
return|return
name|ShutdownRunningTask
operator|.
name|CompleteAllTasks
operator|==
name|shutdownRunningTask
return|;
block|}
DECL|method|createExchanges (Message[] messages)
specifier|protected
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|Message
index|[]
name|messages
parameter_list|)
throws|throws
name|MessagingException
block|{
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|fetchSize
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFetchSize
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|fetchSize
operator|==
operator|-
literal|1
condition|?
name|messages
operator|.
name|length
else|:
name|Math
operator|.
name|min
argument_list|(
name|fetchSize
argument_list|,
name|messages
operator|.
name|length
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Fetching "
operator|+
name|count
operator|+
literal|" messages. Total "
operator|+
name|messages
operator|.
name|length
operator|+
literal|" messages."
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|Message
name|message
init|=
name|messages
index|[
name|i
index|]
decl_stmt|;
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
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Skipping message as it was flagged as deleted: "
operator|+
name|MailUtils
operator|.
name|dumpMessage
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Strategy to process the mail message.      */
DECL|method|processExchange (Exchange exchange)
specifier|protected
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|MailMessage
name|msg
init|=
operator|(
name|MailMessage
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing message: "
operator|+
name|MailUtils
operator|.
name|dumpMessage
argument_list|(
name|msg
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy to flag the message after being processed.      *      * @param mail the mail message      * @param exchange the exchange      */
DECL|method|processCommit (Message mail, Exchange exchange)
specifier|protected
name|void
name|processCommit
parameter_list|(
name|Message
name|mail
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDelete
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange processed, so flagging message as DELETED"
argument_list|)
expr_stmt|;
name|mail
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exchange processed, so flagging message as SEEN"
argument_list|)
expr_stmt|;
name|mail
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
catch|catch
parameter_list|(
name|MessagingException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred during flagging message as DELETED/SEEN"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when processing the exchange failed.      *      * @param mail the mail message      * @param exchange the exchange      */
DECL|method|processRollback (Message mail, Exchange exchange)
specifier|protected
name|void
name|processRollback
parameter_list|(
name|Message
name|mail
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Exception
name|cause
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exchange failed, so rolling back message status: "
operator|+
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exchange failed, so rolling back message status: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ensureIsConnected ()
specifier|private
name|void
name|ensureIsConnected
parameter_list|()
throws|throws
name|MessagingException
block|{
name|MailConfiguration
name|config
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|boolean
name|connected
init|=
literal|false
decl_stmt|;
try|try
block|{
if|if
condition|(
name|store
operator|!=
literal|null
operator|&&
name|store
operator|.
name|isConnected
argument_list|()
condition|)
block|{
name|connected
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Exception while testing for is connected to MailStore: "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
operator|+
literal|". Caused by: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|connected
condition|)
block|{
comment|// ensure resources get recreated on reconnection
name|store
operator|=
literal|null
expr_stmt|;
name|folder
operator|=
literal|null
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connecting to MailStore: "
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|store
operator|=
name|sender
operator|.
name|getSession
argument_list|()
operator|.
name|getStore
argument_list|(
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|store
operator|.
name|connect
argument_list|(
name|config
operator|.
name|getHost
argument_list|()
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|folder
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Getting folder "
operator|+
name|config
operator|.
name|getFolderName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|folder
operator|=
name|store
operator|.
name|getFolder
argument_list|(
name|config
operator|.
name|getFolderName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|folder
operator|==
literal|null
operator|||
operator|!
name|folder
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|FolderNotFoundException
argument_list|(
name|folder
argument_list|,
literal|"Folder not found or invalid: "
operator|+
name|config
operator|.
name|getFolderName
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

