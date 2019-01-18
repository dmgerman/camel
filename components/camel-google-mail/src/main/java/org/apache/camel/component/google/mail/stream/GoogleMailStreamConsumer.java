begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.mail.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|mail
operator|.
name|stream
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
name|ArrayList
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
name|List
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
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|Gmail
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|ListMessagesResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|model
operator|.
name|ModifyMessageRequest
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
name|AsyncCallback
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
name|Endpoint
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
comment|/**  * The GoogleMail consumer.  */
end_comment

begin_class
DECL|class|GoogleMailStreamConsumer
specifier|public
class|class
name|GoogleMailStreamConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
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
name|GoogleMailStreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|unreadLabelId
specifier|private
name|String
name|unreadLabelId
decl_stmt|;
DECL|field|labelsIds
specifier|private
name|List
name|labelsIds
decl_stmt|;
DECL|method|GoogleMailStreamConsumer (Endpoint endpoint, Processor processor, String unreadLabelId, List labelsIds)
specifier|public
name|GoogleMailStreamConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|String
name|unreadLabelId
parameter_list|,
name|List
name|labelsIds
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
name|unreadLabelId
operator|=
name|unreadLabelId
expr_stmt|;
name|this
operator|.
name|labelsIds
operator|=
name|labelsIds
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|protected
name|GoogleMailStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getClient ()
specifier|protected
name|Gmail
name|getClient
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|GoogleMailStreamEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GoogleMailStreamEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|gmail
operator|.
name|Gmail
operator|.
name|Users
operator|.
name|Messages
operator|.
name|List
name|request
init|=
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|messages
argument_list|()
operator|.
name|list
argument_list|(
literal|"me"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getQuery
argument_list|()
argument_list|)
condition|)
block|{
name|request
operator|.
name|setQ
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxResults
argument_list|()
argument_list|)
condition|)
block|{
name|request
operator|.
name|setMaxResults
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxResults
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|labelsIds
argument_list|)
condition|)
block|{
name|request
operator|.
name|setLabelIds
argument_list|(
name|labelsIds
argument_list|)
expr_stmt|;
block|}
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|ListMessagesResponse
name|c
init|=
name|request
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getMessages
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Message
name|message
range|:
name|c
operator|.
name|getMessages
argument_list|()
control|)
block|{
name|Message
name|mess
init|=
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|messages
argument_list|()
operator|.
name|get
argument_list|(
literal|"me"
argument_list|,
name|message
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|setFormat
argument_list|(
literal|"FULL"
argument_list|)
operator|.
name|execute
argument_list|()
decl_stmt|;
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getExchangePattern
argument_list|()
argument_list|,
name|mess
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
block|}
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
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
specifier|final
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
name|exchange
argument_list|,
name|unreadLabelId
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
name|exchange
argument_list|,
name|unreadLabelId
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
literal|"GoogleMailStreamConsumerOnCompletion"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange done"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**      * Strategy to delete the message after being processed.      *      * @param exchange the exchange      * @throws IOException      */
DECL|method|processCommit (Exchange exchange, String unreadLabelId)
specifier|protected
name|void
name|processCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|unreadLabelId
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isMarkAsRead
argument_list|()
condition|)
block|{
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Marking email {} as read"
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|remove
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|remove
operator|.
name|add
argument_list|(
name|unreadLabelId
argument_list|)
expr_stmt|;
name|ModifyMessageRequest
name|mods
init|=
operator|new
name|ModifyMessageRequest
argument_list|()
operator|.
name|setRemoveLabelIds
argument_list|(
name|remove
argument_list|)
decl_stmt|;
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|messages
argument_list|()
operator|.
name|modify
argument_list|(
literal|"me"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|mods
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Marked email {} as read"
argument_list|,
name|id
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error occurred mark as read mail. This exception is ignored."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy when processing the exchange failed.      *      * @param exchange the exchange      * @throws IOException      */
DECL|method|processRollback (Exchange exchange, String unreadLabelId)
specifier|protected
name|void
name|processRollback
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|unreadLabelId
parameter_list|)
block|{
try|try
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exchange failed, so rolling back mail {} to un {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|add
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|add
operator|.
name|add
argument_list|(
name|unreadLabelId
argument_list|)
expr_stmt|;
name|ModifyMessageRequest
name|mods
init|=
operator|new
name|ModifyMessageRequest
argument_list|()
operator|.
name|setAddLabelIds
argument_list|(
name|add
argument_list|)
decl_stmt|;
name|getClient
argument_list|()
operator|.
name|users
argument_list|()
operator|.
name|messages
argument_list|()
operator|.
name|modify
argument_list|(
literal|"me"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|GoogleMailStreamConstants
operator|.
name|MAIL_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|mods
argument_list|)
operator|.
name|execute
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error occurred mark as read mail. This exception is ignored."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

