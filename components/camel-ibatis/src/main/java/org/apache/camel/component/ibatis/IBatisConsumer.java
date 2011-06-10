begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
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
name|ExchangePattern
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
comment|/**  * Consumer to read data from databaase.  *  * @see org.apache.camel.component.ibatis.strategy.IBatisProcessingStrategy  */
end_comment

begin_class
DECL|class|IBatisConsumer
specifier|public
class|class
name|IBatisConsumer
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|IBatisConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|class|DataHolder
specifier|private
specifier|final
class|class
name|DataHolder
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|data
specifier|private
name|Object
name|data
decl_stmt|;
DECL|method|DataHolder ()
specifier|private
name|DataHolder
parameter_list|()
block|{         }
block|}
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
comment|/**      * Statement to run after data has been processed in the route      */
DECL|field|onConsume
specifier|private
name|String
name|onConsume
decl_stmt|;
comment|/**      * Process resultset individually or as a list      */
DECL|field|useIterator
specifier|private
name|boolean
name|useIterator
init|=
literal|true
decl_stmt|;
comment|/**      * Whether allow empty resultset to be routed to the next hop      */
DECL|field|routeEmptyResultSet
specifier|private
name|boolean
name|routeEmptyResultSet
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
DECL|method|IBatisConsumer (IBatisEndpoint endpoint, Processor processor)
specifier|public
name|IBatisConsumer
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|IBatisEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|IBatisEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Polls the database      */
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
comment|// poll data from the database
name|IBatisEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Polling: {}"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|data
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
name|endpoint
operator|.
name|getProcessingStrategy
argument_list|()
operator|.
name|poll
argument_list|(
name|this
argument_list|,
name|getEndpoint
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
comment|// create a list of exchange objects with the data
name|Queue
argument_list|<
name|DataHolder
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<
name|DataHolder
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|useIterator
condition|)
block|{
for|for
control|(
name|Object
name|item
range|:
name|data
control|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|item
argument_list|)
decl_stmt|;
name|DataHolder
name|holder
init|=
operator|new
name|DataHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|holder
operator|.
name|data
operator|=
name|item
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
operator|!
name|data
operator|.
name|isEmpty
argument_list|()
operator|||
name|routeEmptyResultSet
condition|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|data
argument_list|)
decl_stmt|;
name|DataHolder
name|holder
init|=
operator|new
name|DataHolder
argument_list|()
decl_stmt|;
name|holder
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|holder
operator|.
name|data
operator|=
name|data
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
comment|// process all the exchanges in this batch
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
specifier|final
name|IBatisEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
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
name|DataHolder
name|holder
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|DataHolder
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|holder
operator|.
name|exchange
decl_stmt|;
name|Object
name|data
init|=
name|holder
operator|.
name|data
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
comment|// process the current exchange
name|LOG
operator|.
name|debug
argument_list|(
literal|"Processing exchange: {}"
argument_list|,
name|exchange
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
try|try
block|{
if|if
condition|(
name|onConsume
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getProcessingStrategy
argument_list|()
operator|.
name|commit
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|,
name|onConsume
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
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|total
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
DECL|method|createExchange (Object data)
specifier|private
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|data
parameter_list|)
block|{
specifier|final
name|IBatisEndpoint
name|endpoint
init|=
name|getEndpoint
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|IBatisConstants
operator|.
name|IBATIS_STATEMENT_NAME
argument_list|,
name|endpoint
operator|.
name|getStatement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Gets the statement(s) to run after successful processing.      * Use comma to separate multiple statements.      */
DECL|method|getOnConsume ()
specifier|public
name|String
name|getOnConsume
parameter_list|()
block|{
return|return
name|onConsume
return|;
block|}
comment|/**      * Sets the statement to run after successful processing.      * Use comma to separate multiple statements.      */
DECL|method|setOnConsume (String onConsume)
specifier|public
name|void
name|setOnConsume
parameter_list|(
name|String
name|onConsume
parameter_list|)
block|{
name|this
operator|.
name|onConsume
operator|=
name|onConsume
expr_stmt|;
block|}
comment|/**      * Indicates how resultset should be delivered to the route      */
DECL|method|isUseIterator ()
specifier|public
name|boolean
name|isUseIterator
parameter_list|()
block|{
return|return
name|useIterator
return|;
block|}
comment|/**      * Sets how resultset should be delivered to route.      * Indicates delivery as either a list or individual object.      * defaults to true.      */
DECL|method|setUseIterator (boolean useIterator)
specifier|public
name|void
name|setUseIterator
parameter_list|(
name|boolean
name|useIterator
parameter_list|)
block|{
name|this
operator|.
name|useIterator
operator|=
name|useIterator
expr_stmt|;
block|}
comment|/**      * Indicates whether empty resultset should be allowed to be sent to the next hop or not      */
DECL|method|isRouteEmptyResultSet ()
specifier|public
name|boolean
name|isRouteEmptyResultSet
parameter_list|()
block|{
return|return
name|routeEmptyResultSet
return|;
block|}
comment|/**      * Sets whether empty resultset should be allowed to be sent to the next hop.      * defaults to false. So the empty resultset will be filtered out.      */
DECL|method|setRouteEmptyResultSet (boolean routeEmptyResultSet)
specifier|public
name|void
name|setRouteEmptyResultSet
parameter_list|(
name|boolean
name|routeEmptyResultSet
parameter_list|)
block|{
name|this
operator|.
name|routeEmptyResultSet
operator|=
name|routeEmptyResultSet
expr_stmt|;
block|}
block|}
end_class

end_unit

