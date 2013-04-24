begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

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
name|UriParam
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
comment|/**  * A useful base class for any consumer which is polling batch based  */
end_comment

begin_class
DECL|class|ScheduledBatchPollingConsumer
specifier|public
specifier|abstract
class|class
name|ScheduledBatchPollingConsumer
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScheduledBatchPollingConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|shutdownRunningTask
specifier|protected
specifier|volatile
name|ShutdownRunningTask
name|shutdownRunningTask
decl_stmt|;
DECL|field|pendingExchanges
specifier|protected
specifier|volatile
name|int
name|pendingExchanges
decl_stmt|;
annotation|@
name|UriParam
DECL|field|maxMessagesPerPoll
specifier|protected
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|method|ScheduledBatchPollingConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|ScheduledBatchPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|ScheduledBatchPollingConsumer (Endpoint endpoint, Processor processor, ScheduledExecutorService executor)
specifier|public
name|ScheduledBatchPollingConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ScheduledExecutorService
name|executor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
name|int
name|answer
decl_stmt|;
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
name|answer
operator|=
name|pendingExchanges
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
literal|0
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|0
operator|&&
name|isPolling
argument_list|()
condition|)
block|{
comment|// force at least one pending exchange if we are polling as there is a little gap
comment|// in the processBatch method and until an exchange gets enlisted as in-flight
comment|// which happens later, so we need to signal back to the shutdown strategy that
comment|// there is a pending exchange. When we are no longer polling, then we will return 0
name|LOG
operator|.
name|trace
argument_list|(
literal|"Currently polling so returning 1 as pending exchanges"
argument_list|)
expr_stmt|;
name|answer
operator|=
literal|1
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|forced
parameter_list|)
block|{
comment|// reset task as the state of the task is not to be preserved
comment|// which otherwise may cause isBatchAllowed() to return a wrong answer
name|this
operator|.
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
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
comment|/**      * Gets the maximum number of messages as a limit to poll at each polling.      *<p/>      * Is default unlimited, but use 0 or negative number to disable it as unlimited.      *      * @return max messages to poll      */
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|processEmptyMessage ()
specifier|protected
name|void
name|processEmptyMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
comment|// enrich exchange, so we send an empty message with the batch details
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
literal|0
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
literal|1
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
literal|true
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sending empty message as there were no messages from polling: {}"
argument_list|,
name|this
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
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
end_class

end_unit

