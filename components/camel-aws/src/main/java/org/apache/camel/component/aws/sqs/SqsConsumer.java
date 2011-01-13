begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|AmazonSQSClient
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

begin_comment
comment|/**  * A Consumer of messages from the Amazon Web Service Simple Queue Service  *<a href="http://aws.amazon.com/aws-sqs/">AWS SQS</a>  *   * @version $Revision: $  */
end_comment

begin_class
DECL|class|SqsConsumer
specifier|public
class|class
name|SqsConsumer
extends|extends
name|ScheduledPollConsumer
implements|implements
name|BatchConsumer
implements|,
name|ShutdownAware
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
name|SqsConsumer
operator|.
name|class
argument_list|)
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
block|}
annotation|@
name|Override
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
operator|!=
literal|null
condition|?
name|getConfiguration
argument_list|()
operator|.
name|getVisibilityTimeout
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|request
operator|.
name|setAttributeNames
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
argument_list|()
operator|!=
literal|null
condition|?
name|getConfiguration
argument_list|()
operator|.
name|getAttributeNames
argument_list|()
else|:
literal|null
argument_list|)
expr_stmt|;
name|ReceiveMessageResult
name|messageResult
init|=
name|getClient
argument_list|()
operator|.
name|receiveMessage
argument_list|(
name|request
argument_list|)
decl_stmt|;
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
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
expr_stmt|;
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
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received "
operator|+
name|messages
operator|.
name|size
argument_list|()
operator|+
literal|" messages in this poll"
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
argument_list|<
name|Exchange
argument_list|>
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange ["
operator|+
name|exchange
operator|+
literal|"]..."
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
name|getConfiguration
argument_list|()
operator|.
name|isDeleteAfterRead
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting message with receipt handle "
operator|+
name|receiptHandle
operator|+
literal|"..."
argument_list|)
expr_stmt|;
block|}
name|getClient
argument_list|()
operator|.
name|deleteMessage
argument_list|(
name|deleteRequest
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred during deleting message"
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
DECL|method|prepareShutdown ()
specifier|public
name|void
name|prepareShutdown
parameter_list|()
block|{
comment|// noop
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
name|AmazonSQSClient
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
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|maxMessagesPerPoll
argument_list|)
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getMaxMessagesPerPoll
argument_list|()
return|;
block|}
block|}
end_class

end_unit

