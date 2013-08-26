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
name|Enumeration
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|UUID
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
name|Store
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
name|impl
operator|.
name|ScheduledBatchPollingConsumer
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
name|support
operator|.
name|SynchronizationAdapter
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.Consumer Consumer} which consumes messages from JavaMail using a  * {@link javax.mail.Transport Transport} and dispatches them to the {@link Processor}  */
end_comment

begin_class
DECL|class|MailConsumer
specifier|public
class|class
name|MailConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|POP3_UID
specifier|public
specifier|static
specifier|final
name|String
name|POP3_UID
init|=
literal|"CamelPop3Uid"
decl_stmt|;
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MailConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|sender
specifier|private
specifier|final
name|JavaMailSender
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
DECL|method|MailConsumer (MailEndpoint endpoint, Processor processor, JavaMailSender sender)
specifier|public
name|MailConsumer
parameter_list|(
name|MailEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|JavaMailSender
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
name|int
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
name|int
name|polledMessages
init|=
literal|0
decl_stmt|;
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
name|getEndpoint
argument_list|()
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
literal|"Polling mailbox folder: "
operator|+
name|getEndpoint
argument_list|()
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
name|getEndpoint
argument_list|()
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
return|return
literal|0
return|;
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
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getSearchTerm
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// use custom search term
name|messages
operator|=
name|folder
operator|.
name|search
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSearchTerm
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isUnseen
argument_list|()
condition|)
block|{
comment|// only unseen messages
name|messages
operator|=
name|folder
operator|.
name|search
argument_list|(
operator|new
name|SearchTermBuilder
argument_list|()
operator|.
name|unseen
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// get all messages
name|messages
operator|=
name|folder
operator|.
name|getMessages
argument_list|()
expr_stmt|;
block|}
name|polledMessages
operator|=
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
comment|// need to ensure we release resources, but only if closeFolder or disconnect = true
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isCloseFolder
argument_list|()
operator|||
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnect
argument_list|()
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
literal|"Close mailbox folder {} from {}"
argument_list|,
name|folder
operator|.
name|getName
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// should we disconnect, the header can override the configuration
name|boolean
name|disconnect
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDisconnect
argument_list|()
decl_stmt|;
if|if
condition|(
name|disconnect
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
literal|"Disconnecting from {}"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|store
operator|.
name|close
argument_list|()
expr_stmt|;
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
literal|"Could not disconnect from {}: "
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMailStoreLogInformation
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|store
operator|=
literal|null
expr_stmt|;
name|folder
operator|=
literal|null
expr_stmt|;
block|}
return|return
name|polledMessages
return|;
block|}
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Limiting to maximum messages to poll {} as there was {} messages in this poll."
argument_list|,
name|maxMessagesPerPoll
argument_list|,
name|total
argument_list|)
expr_stmt|;
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
name|SynchronizationAdapter
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
name|boolean
name|allowHandover
parameter_list|()
block|{
comment|// do not allow handover as the commit/rollback logic needs to be executed
comment|// on the same session that polled the messages
return|return
literal|false
return|;
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
return|return
name|total
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
name|getEndpoint
argument_list|()
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
literal|"Fetching {} messages. Total {} messages."
argument_list|,
name|count
argument_list|,
name|messages
operator|.
name|length
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
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isMapMailMessage
argument_list|()
condition|)
block|{
comment|// ensure the mail message is mapped, which can be ensured by touching the body/header/attachment
name|LOG
operator|.
name|trace
argument_list|(
literal|"Mapping #{} from javax.mail.Message to Camel MailMessage"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getAttachments
argument_list|()
expr_stmt|;
block|}
comment|// If the protocol is POP3 we need to remember the uid on the exchange
comment|// so we can find the mail message again later to be able to delete it
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"pop3"
argument_list|)
condition|)
block|{
name|String
name|uid
init|=
name|generatePop3Uid
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|uid
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|POP3_UID
argument_list|,
name|uid
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"POP3 mail message using uid {}"
argument_list|,
name|uid
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"Skipping message as it was flagged as deleted: {}"
argument_list|,
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
literal|"Processing message: {}"
argument_list|,
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
comment|/**      * Strategy to flag the message after being processed.      *      * @param mail     the mail message      * @param exchange the exchange      */
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
comment|// If the protocol is POP3, the message needs to be synced with the folder via the UID.
comment|// Otherwise setting the DELETE/SEEN flag won't delete the message.
name|String
name|uid
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|POP3_UID
argument_list|)
decl_stmt|;
if|if
condition|(
name|uid
operator|!=
literal|null
condition|)
block|{
name|int
name|count
init|=
name|folder
operator|.
name|getMessageCount
argument_list|()
decl_stmt|;
name|Message
name|found
init|=
literal|null
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Looking for POP3Message with UID {} from folder with {} mails"
argument_list|,
name|uid
argument_list|,
name|count
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|count
condition|;
operator|++
name|i
control|)
block|{
name|Message
name|msg
init|=
name|folder
operator|.
name|getMessage
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|uid
operator|.
name|equals
argument_list|(
name|generatePop3Uid
argument_list|(
name|msg
argument_list|)
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Found POP3Message with UID {} from folder with {} mails"
argument_list|,
name|uid
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|found
operator|=
name|msg
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|found
operator|==
literal|null
condition|)
block|{
name|boolean
name|delete
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isDelete
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|warn
argument_list|(
literal|"POP3message not found in folder. Message cannot be marked as "
operator|+
operator|(
name|delete
condition|?
literal|"DELETED"
else|:
literal|"SEEN"
operator|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mail
operator|=
name|found
expr_stmt|;
block|}
block|}
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|MailConfiguration
name|config
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// header values override configuration values
name|String
name|copyTo
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"copyTo"
argument_list|,
name|config
operator|.
name|getCopyTo
argument_list|()
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|delete
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"delete"
argument_list|,
name|config
operator|.
name|isDelete
argument_list|()
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Copy message into different imap folder if asked
if|if
condition|(
name|config
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
name|MailUtils
operator|.
name|PROTOCOL_IMAP
argument_list|)
operator|||
name|config
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
name|MailUtils
operator|.
name|PROTOCOL_IMAPS
argument_list|)
condition|)
block|{
if|if
condition|(
name|copyTo
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"IMAP message needs to be copied to {}"
argument_list|,
name|copyTo
argument_list|)
expr_stmt|;
name|Folder
name|destFolder
init|=
name|store
operator|.
name|getFolder
argument_list|(
name|copyTo
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|destFolder
operator|.
name|exists
argument_list|()
condition|)
block|{
name|destFolder
operator|.
name|create
argument_list|(
name|Folder
operator|.
name|HOLDS_MESSAGES
argument_list|)
expr_stmt|;
block|}
name|folder
operator|.
name|copyMessages
argument_list|(
operator|new
name|Message
index|[]
block|{
name|mail
block|}
argument_list|,
name|destFolder
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"IMAP message {} copied to {}"
argument_list|,
name|mail
argument_list|,
name|copyTo
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|delete
condition|)
block|{
name|LOG
operator|.
name|trace
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
name|trace
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error occurred during committing mail message: "
operator|+
name|mail
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when processing the exchange failed.      *      * @param mail     the mail message      * @param exchange the exchange      */
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
comment|/**      * Generates an UID of the POP3Message      *      * @param message the POP3Message      * @return the generated uid      */
DECL|method|generatePop3Uid (Message message)
specifier|protected
name|String
name|generatePop3Uid
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|uid
init|=
literal|null
decl_stmt|;
comment|// create an UID based on message headers on the POP3Message, that ought
comment|// to be unique
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
try|try
block|{
name|Enumeration
argument_list|<
name|?
argument_list|>
name|it
init|=
name|message
operator|.
name|getAllHeaders
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
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
name|it
operator|.
name|nextElement
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
operator|.
name|append
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Generating UID from the following:\n {}"
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|uid
operator|=
name|UUID
operator|.
name|nameUUIDFromBytes
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
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
literal|"Cannot reader headers from mail message. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|uid
return|;
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
name|getEndpoint
argument_list|()
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
name|getEndpoint
argument_list|()
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
literal|"Connecting to MailStore: {}"
argument_list|,
name|getEndpoint
argument_list|()
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
literal|"Getting folder {}"
argument_list|,
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

