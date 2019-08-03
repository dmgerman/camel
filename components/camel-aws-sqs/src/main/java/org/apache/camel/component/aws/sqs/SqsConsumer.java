begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.sqs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|sqs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledFuture
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|AmazonClientException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|AmazonSQS
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|ChangeMessageVisibilityRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|DeleteMessageRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
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
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|MessageNotInflightException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|QueueDeletedRecentlyException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|QueueDoesNotExistException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|ReceiptHandleIsInvalidException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|ReceiveMessageRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|sqs
operator|.
name|model
operator|.
name|ReceiveMessageResult
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
name|NoFactoryAvailableException
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * A Consumer of messages from the Amazon Web Service Simple Queue Service  *<a href="http://aws.amazon.com/sqs/">AWS SQS</a>  */
end_comment

begin_class
DECL|class|SqsConsumer
specifier|public
class|class
name|SqsConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|scheduledExecutor
specifier|private
name|ScheduledExecutorService
name|scheduledExecutor
decl_stmt|;
DECL|field|sqsConsumerToString
specifier|private
specifier|transient
name|String
name|sqsConsumerToString
decl_stmt|;
DECL|field|attributeNames
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|attributeNames
decl_stmt|;
DECL|field|messageAttributeNames
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|messageAttributeNames
decl_stmt|;
DECL|method|SqsConsumer (SqsEndpoint endpoint, Processor processor)
specifier|public
name|SqsConsumer
parameter_list|(
name|SqsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|NoFactoryAvailableException
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|names
init|=
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|attributeNames
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getMessageAttributeNames
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|names
init|=
name|getConfiguration
argument_list|()
operator|.
name|getMessageAttributeNames
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|messageAttributeNames
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|names
argument_list|)
expr_stmt|;
block|}
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
comment|// must reset for each poll
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|ReceiveMessageRequest
name|request
init|=
operator|new
name|ReceiveMessageRequest
argument_list|(
name|getQueueUrl
argument_list|()
argument_list|)
decl_stmt|;
name|request
operator|.
name|setMaxNumberOfMessages
argument_list|(
name|getMaxMessagesPerPoll
argument_list|()
operator|>
literal|0
condition|?
name|getMaxMessagesPerPoll
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|request
operator|.
name|setVisibilityTimeout
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getVisibilityTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setWaitTimeSeconds
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getWaitTimeSeconds
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|attributeNames
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|setAttributeNames
argument_list|(
name|attributeNames
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageAttributeNames
operator|!=
literal|null
condition|)
block|{
name|request
operator|.
name|setMessageAttributeNames
argument_list|(
name|messageAttributeNames
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Receiving messages with request [{}]..."
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|ReceiveMessageResult
name|messageResult
decl_stmt|;
try|try
block|{
name|messageResult
operator|=
name|getClient
argument_list|()
operator|.
name|receiveMessage
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|QueueDoesNotExistException
name|e
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Queue does not exist....recreating now..."
argument_list|)
expr_stmt|;
name|reConnectToQueue
argument_list|()
expr_stmt|;
name|messageResult
operator|=
name|getClient
argument_list|()
operator|.
name|receiveMessage
argument_list|(
name|request
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Received {} messages"
argument_list|,
name|messageResult
operator|.
name|getMessages
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|createExchanges
argument_list|(
name|messageResult
operator|.
name|getMessages
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
return|;
block|}
DECL|method|reConnectToQueue ()
specifier|public
name|void
name|reConnectToQueue
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isAutoCreateQueue
argument_list|()
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|createQueue
argument_list|(
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|QueueDeletedRecentlyException
name|qdr
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Queue recently deleted, will retry in 30 seconds."
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|30000
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|createQueue
argument_list|(
name|getClient
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"failed to retry queue connection."
argument_list|,
name|e
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
name|log
operator|.
name|warn
argument_list|(
literal|"Could not connect to queue in amazon."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createExchanges (List<Message> messages)
specifier|protected
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|List
argument_list|<
name|Message
argument_list|>
name|messages
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Received {} messages in this poll"
argument_list|,
name|messages
operator|.
name|size
argument_list|()
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
for|for
control|(
name|Message
name|message
range|:
name|messages
control|)
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
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
comment|// schedule task to extend visibility if enabled
name|Integer
name|visibilityTimeout
init|=
name|getConfiguration
argument_list|()
operator|.
name|getVisibilityTimeout
argument_list|()
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|scheduledExecutor
operator|!=
literal|null
operator|&&
name|visibilityTimeout
operator|!=
literal|null
operator|&&
operator|(
name|visibilityTimeout
operator|.
name|intValue
argument_list|()
operator|/
literal|2
operator|)
operator|>
literal|0
condition|)
block|{
name|int
name|delay
init|=
name|visibilityTimeout
operator|.
name|intValue
argument_list|()
operator|/
literal|2
decl_stmt|;
name|int
name|period
init|=
name|visibilityTimeout
operator|.
name|intValue
argument_list|()
decl_stmt|;
name|int
name|repeatSeconds
init|=
name|Double
operator|.
name|valueOf
argument_list|(
name|visibilityTimeout
operator|.
name|doubleValue
argument_list|()
operator|*
literal|1.5
argument_list|)
operator|.
name|intValue
argument_list|()
decl_stmt|;
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
literal|"Scheduled TimeoutExtender task to start after {} delay, and run with {}/{} period/repeat (seconds), to extend exchangeId: {}"
argument_list|,
name|delay
argument_list|,
name|period
argument_list|,
name|repeatSeconds
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ScheduledFuture
argument_list|<
name|?
argument_list|>
name|scheduledFuture
init|=
name|this
operator|.
name|scheduledExecutor
operator|.
name|scheduleAtFixedRate
argument_list|(
operator|new
name|TimeoutExtender
argument_list|(
name|exchange
argument_list|,
name|repeatSeconds
argument_list|)
argument_list|,
name|delay
argument_list|,
name|period
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|cancelExtender
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|cancelExtender
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|cancelExtender
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// cancel task as we are done
name|log
operator|.
name|trace
argument_list|(
literal|"Processing done so cancelling TimeoutExtender task for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|scheduledFuture
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
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
literal|"SqsConsumerOnCompletion"
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}] done."
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
comment|/**      * Strategy to delete the message after being processed.      *      * @param exchange the exchange      */
DECL|method|processCommit (Exchange exchange)
specifier|protected
name|void
name|processCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|shouldDelete
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|String
name|receiptHandle
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|DeleteMessageRequest
name|deleteRequest
init|=
operator|new
name|DeleteMessageRequest
argument_list|(
name|getQueueUrl
argument_list|()
argument_list|,
name|receiptHandle
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Deleting message with receipt handle {}..."
argument_list|,
name|receiptHandle
argument_list|)
expr_stmt|;
name|getClient
argument_list|()
operator|.
name|deleteMessage
argument_list|(
name|deleteRequest
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Deleted message with receipt handle {}..."
argument_list|,
name|receiptHandle
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|AmazonClientException
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error occurred during deleting message. This exception is ignored."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldDelete (Exchange exchange)
specifier|private
name|boolean
name|shouldDelete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|boolean
name|shouldDeleteByFilter
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|)
operator|!=
literal|null
operator|&&
name|getConfiguration
argument_list|()
operator|.
name|isDeleteIfFiltered
argument_list|()
operator|&&
name|passedThroughFilter
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
operator|||
name|shouldDeleteByFilter
return|;
block|}
DECL|method|passedThroughFilter (Exchange exchange)
specifier|private
name|boolean
name|passedThroughFilter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_MATCHED
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Strategy when processing the exchange failed.      *      * @param exchange the exchange      */
DECL|method|processRollback (Exchange exchange)
specifier|protected
name|void
name|processRollback
parameter_list|(
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
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error during processing exchange. Will attempt to process the message on next poll."
argument_list|,
name|exchange
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|protected
name|SqsConfiguration
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
name|AmazonSQS
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
DECL|method|getQueueUrl ()
specifier|protected
name|String
name|getQueueUrl
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getQueueUrl
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SqsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SqsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|sqsConsumerToString
operator|==
literal|null
condition|)
block|{
name|sqsConsumerToString
operator|=
literal|"SqsConsumer["
operator|+
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|+
literal|"]"
expr_stmt|;
block|}
return|return
name|sqsConsumerToString
return|;
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
comment|// start scheduler first
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|isExtendMessageVisibility
argument_list|()
operator|&&
name|scheduledExecutor
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|scheduledExecutor
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadScheduledExecutor
argument_list|(
name|this
argument_list|,
literal|"SqsTimeoutExtender"
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|scheduledExecutor
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|scheduledExecutor
argument_list|)
expr_stmt|;
name|scheduledExecutor
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
DECL|class|TimeoutExtender
specifier|private
class|class
name|TimeoutExtender
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|repeatSeconds
specifier|private
specifier|final
name|int
name|repeatSeconds
decl_stmt|;
DECL|method|TimeoutExtender (Exchange exchange, int repeatSeconds)
name|TimeoutExtender
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|repeatSeconds
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|repeatSeconds
operator|=
name|repeatSeconds
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|ChangeMessageVisibilityRequest
name|request
init|=
operator|new
name|ChangeMessageVisibilityRequest
argument_list|(
name|getQueueUrl
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqsConstants
operator|.
name|RECEIPT_HANDLE
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|repeatSeconds
argument_list|)
decl_stmt|;
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Extending visibility window by {} seconds for exchange {}"
argument_list|,
name|this
operator|.
name|repeatSeconds
argument_list|,
name|this
operator|.
name|exchange
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|changeMessageVisibility
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Extended visibility window by {} seconds for exchange {}"
argument_list|,
name|this
operator|.
name|repeatSeconds
argument_list|,
name|this
operator|.
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ReceiptHandleIsInvalidException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|MessageNotInflightException
name|e
parameter_list|)
block|{
comment|// Ignore.
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Extending visibility window failed for exchange "
operator|+
name|exchange
operator|+
literal|". Will not attempt to extend visibility further. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

