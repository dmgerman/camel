begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

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
name|Collections
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
name|StopWatch
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
name|TimeUtils
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
comment|/**  * Base class for file consumers.  */
end_comment

begin_class
DECL|class|GenericFileConsumer
specifier|public
specifier|abstract
class|class
name|GenericFileConsumer
parameter_list|<
name|T
parameter_list|>
extends|extends
name|ScheduledPollConsumer
implements|implements
name|BatchConsumer
implements|,
name|ShutdownAware
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|operations
specifier|protected
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
decl_stmt|;
DECL|field|loggedIn
specifier|protected
name|boolean
name|loggedIn
decl_stmt|;
DECL|field|fileExpressionResult
specifier|protected
name|String
name|fileExpressionResult
decl_stmt|;
DECL|field|maxMessagesPerPoll
specifier|protected
name|int
name|maxMessagesPerPoll
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
DECL|field|customProcessor
specifier|protected
name|Processor
name|customProcessor
decl_stmt|;
DECL|method|GenericFileConsumer (GenericFileEndpoint<T> endpoint, Processor processor, GenericFileOperations<T> operations)
specifier|public
name|GenericFileConsumer
parameter_list|(
name|GenericFileEndpoint
argument_list|<
name|T
argument_list|>
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
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
name|operations
operator|=
name|operations
expr_stmt|;
block|}
DECL|method|getCustomProcessor ()
specifier|public
name|Processor
name|getCustomProcessor
parameter_list|()
block|{
return|return
name|customProcessor
return|;
block|}
comment|/**      * Use a custom processor to process the exchange.      *<p/>      * Only set this if you need to do custom processing, instead of the regular processing.      *<p/>      * This is for example used to browse file endpoints by leveraging the file consumer to poll      * the directory to gather the list of exchanges. But to avoid processing the files regularly      * we can use a custom processor.      *      * @param processor a custom processor      */
DECL|method|setCustomProcessor (Processor processor)
specifier|public
name|void
name|setCustomProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|customProcessor
operator|=
name|processor
expr_stmt|;
block|}
comment|/**      * Poll for files      */
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must reset for each poll
name|fileExpressionResult
operator|=
literal|null
expr_stmt|;
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
comment|// before we poll is there anything we need to check?
comment|// such as are we connected to the FTP Server still?
if|if
condition|(
operator|!
name|prePollCheck
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Skipping poll as pre poll check returned false"
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
comment|// gather list of files to process
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|files
init|=
operator|new
name|ArrayList
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getDirectory
argument_list|()
decl_stmt|;
comment|// time how long time it takes to poll
name|StopWatch
name|stop
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|boolean
name|limitHit
init|=
operator|!
name|pollDirectory
argument_list|(
name|name
argument_list|,
name|files
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|long
name|delta
init|=
name|stop
operator|.
name|stop
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
literal|"Took {} to poll: {}"
argument_list|,
name|TimeUtils
operator|.
name|printDuration
argument_list|(
name|delta
argument_list|)
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
comment|// log if we hit the limit
if|if
condition|(
name|limitHit
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Limiting maximum messages to poll at {} files as there was more messages in this poll."
argument_list|,
name|maxMessagesPerPoll
argument_list|)
expr_stmt|;
block|}
comment|// sort files using file comparator if provided
if|if
condition|(
name|endpoint
operator|.
name|getSorter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|files
argument_list|,
name|endpoint
operator|.
name|getSorter
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// sort using build in sorters so we can use expressions
name|LinkedList
argument_list|<
name|Exchange
argument_list|>
name|exchanges
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
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
range|:
name|files
control|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|configureExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|configureMessage
argument_list|(
name|file
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|exchanges
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// sort files using exchange comparator if provided
if|if
condition|(
name|endpoint
operator|.
name|getSortBy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|exchanges
argument_list|,
name|endpoint
operator|.
name|getSortBy
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// consume files one by one
name|int
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|total
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Total {} files to consume"
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|q
init|=
name|exchanges
decl_stmt|;
name|int
name|polledMessages
init|=
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|q
argument_list|)
argument_list|)
decl_stmt|;
name|postPollCheck
argument_list|()
expr_stmt|;
return|return
name|polledMessages
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|log
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
comment|// use poll to remove the head so it does not consume memory even after we have processed it
name|Exchange
name|exchange
init|=
operator|(
name|Exchange
operator|)
name|exchanges
operator|.
name|poll
argument_list|()
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
if|if
condition|(
name|customProcessor
operator|!=
literal|null
condition|)
block|{
comment|// use a custom processor
name|customProcessExchange
argument_list|(
name|exchange
argument_list|,
name|customProcessor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// process the exchange regular
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// remove the file from the in progress list in case the batch was limited by max messages per poll
while|while
condition|(
name|exchanges
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Exchange
name|exchange
init|=
operator|(
name|Exchange
operator|)
name|exchanges
operator|.
name|poll
argument_list|()
decl_stmt|;
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
init|=
operator|(
name|GenericFile
argument_list|<
name|T
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|String
name|key
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
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
name|log
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
comment|/**      * Whether or not we can continue polling for more files      *      * @param fileList  the current list of gathered files      * @return<tt>true</tt> to continue,<tt>false</tt> to stop due hitting maxMessagesPerPoll limit      */
DECL|method|canPollMoreFiles (List<?> fileList)
specifier|public
name|boolean
name|canPollMoreFiles
parameter_list|(
name|List
argument_list|<
name|?
argument_list|>
name|fileList
parameter_list|)
block|{
if|if
condition|(
name|maxMessagesPerPoll
operator|<=
literal|0
condition|)
block|{
comment|// no limitation
return|return
literal|true
return|;
block|}
comment|// then only poll if we haven't reached the max limit
return|return
name|fileList
operator|.
name|size
argument_list|()
operator|<
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * Override if required. Perform some checks (and perhaps actions) before we poll.      *      * @return<tt>true</tt> to poll,<tt>false</tt> to skip this poll.      */
DECL|method|prePollCheck ()
specifier|protected
name|boolean
name|prePollCheck
parameter_list|()
throws|throws
name|Exception
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Override if required. Perform some checks (and perhaps actions) after we have polled.      */
DECL|method|postPollCheck ()
specifier|protected
name|void
name|postPollCheck
parameter_list|()
block|{
comment|// noop
block|}
comment|/**      * Polls the given directory for files to process      *      * @param fileName current directory or file      * @param fileList current list of files gathered      * @param depth the current depth of the directory (will start from 0)      * @return whether or not to continue polling,<tt>false</tt> means the maxMessagesPerPoll limit has been hit      */
DECL|method|pollDirectory (String fileName, List<GenericFile<T>> fileList, int depth)
specifier|protected
specifier|abstract
name|boolean
name|pollDirectory
parameter_list|(
name|String
name|fileName
parameter_list|,
name|List
argument_list|<
name|GenericFile
argument_list|<
name|T
argument_list|>
argument_list|>
name|fileList
parameter_list|,
name|int
name|depth
parameter_list|)
function_decl|;
comment|/**      * Sets the operations to be used.      *<p/>      * Can be used to set a fresh operations in case of recovery attempts      *      * @param operations the operations      */
DECL|method|setOperations (GenericFileOperations<T> operations)
specifier|public
name|void
name|setOperations
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|)
block|{
name|this
operator|.
name|operations
operator|=
name|operations
expr_stmt|;
block|}
comment|/**      * Processes the exchange      *      * @param exchange the exchange      */
DECL|method|processExchange (final Exchange exchange)
specifier|protected
name|void
name|processExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
init|=
name|getExchangeFileProperty
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Processing file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// must extract the absolute name before the begin strategy as the file could potentially be pre moved
comment|// and then the file name would be changed
name|String
name|absoluteFileName
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
comment|// check if we can begin processing the file
try|try
block|{
specifier|final
name|GenericFileProcessStrategy
argument_list|<
name|T
argument_list|>
name|processStrategy
init|=
name|endpoint
operator|.
name|getGenericFileProcessStrategy
argument_list|()
decl_stmt|;
name|boolean
name|begin
init|=
name|processStrategy
operator|.
name|begin
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|begin
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|endpoint
operator|+
literal|" cannot begin processing file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// begin returned false, so remove file from the in progress list as its no longer in progress
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
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
name|endpoint
operator|+
literal|" cannot begin processing file: "
operator|+
name|file
operator|+
literal|" due to: "
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
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// must use file from exchange as it can be updated due the
comment|// preMoveNamePrefix/preMoveNamePostfix options
specifier|final
name|GenericFile
argument_list|<
name|T
argument_list|>
name|target
init|=
name|getExchangeFileProperty
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// must use full name when downloading so we have the correct path
specifier|final
name|String
name|name
init|=
name|target
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
try|try
block|{
comment|// retrieve the file using the stream
name|log
operator|.
name|trace
argument_list|(
literal|"Retrieving file: {} from: {}"
argument_list|,
name|name
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// retrieve the file and check it was a success
name|boolean
name|retrieved
init|=
name|operations
operator|.
name|retrieveFile
argument_list|(
name|name
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|retrieved
condition|)
block|{
comment|// throw exception to handle the problem with retrieving the file
comment|// then if the method return false or throws an exception is handled the same in here
comment|// as in both cases an exception is being thrown
throw|throw
operator|new
name|GenericFileOperationFailedException
argument_list|(
literal|"Cannot retrieve file: "
operator|+
name|file
operator|+
literal|" from: "
operator|+
name|endpoint
argument_list|)
throw|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Retrieved file: {} from: {}"
argument_list|,
name|name
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// register on completion callback that does the completion strategies
comment|// (for instance to move the file after we have processed it)
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|GenericFileOnCompletion
argument_list|<
name|T
argument_list|>
argument_list|(
name|endpoint
argument_list|,
name|operations
argument_list|,
name|target
argument_list|,
name|absoluteFileName
argument_list|)
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"About to process file: {} using exchange: {}"
argument_list|,
name|target
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// process the exchange using the async consumer to support async routing engine
comment|// which can be supported by this file consumer as all the done work is
comment|// provided in the GenericFileOnCompletion
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
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// noop
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
literal|"Done processing file: {} {}"
argument_list|,
name|target
argument_list|,
name|doneSync
condition|?
literal|"synchronously"
else|:
literal|"asynchronously"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// remove file from the in progress list due to failure
comment|// (cannot be in finally block due to GenericFileOnCompletion will remove it
comment|// from in progress when it takes over and processes the file, which may happen
comment|// by another thread at a later time. So its only safe to remove it if there was an exception)
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Processes the exchange using a custom processor.      *      * @param exchange the exchange      * @param processor the custom processor      */
DECL|method|customProcessExchange (final Exchange exchange, final Processor processor)
specifier|protected
name|void
name|customProcessExchange
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|)
block|{
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
init|=
name|getExchangeFileProperty
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Custom processing file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
comment|// must extract the absolute name before the begin strategy as the file could potentially be pre moved
comment|// and then the file name would be changed
name|String
name|absoluteFileName
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
try|try
block|{
comment|// process using the custom processor
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
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
name|endpoint
operator|+
literal|" error custom processing: "
operator|+
name|file
operator|+
literal|" due to: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// always remove file from the in progress list as its no longer in progress
comment|// use the original file name that was used to add it to the repository
comment|// as the name can be different when using preMove option
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|absoluteFileName
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Strategy for validating if the given remote file should be included or not      *      * @param file        the file      * @param isDirectory whether the file is a directory or a file      * @return<tt>true</tt> to include the file,<tt>false</tt> to skip it      */
DECL|method|isValidFile (GenericFile<T> file, boolean isDirectory)
specifier|protected
name|boolean
name|isValidFile
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|boolean
name|isDirectory
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isMatched
argument_list|(
name|file
argument_list|,
name|isDirectory
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"File did not match. Will skip this file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|isIdempotent
argument_list|()
operator|&&
name|endpoint
operator|.
name|getIdempotentRepository
argument_list|()
operator|.
name|contains
argument_list|(
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"This consumer is idempotent and the file has been consumed before. Will skip this file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// file matched
return|return
literal|true
return|;
block|}
comment|/**      * Strategy to perform file matching based on endpoint configuration.      *<p/>      * Will always return<tt>false</tt> for certain files/folders:      *<ul>      *<li>Starting with a dot</li>      *<li>lock files</li>      *</ul>      * And then<tt>true</tt> for directories.      *      * @param file        the file      * @param isDirectory whether the file is a directory or a file      * @return<tt>true</tt> if the file is matched,<tt>false</tt> if not      */
DECL|method|isMatched (GenericFile<T> file, boolean isDirectory)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|boolean
name|isDirectory
parameter_list|)
block|{
name|String
name|name
init|=
name|file
operator|.
name|getFileNameOnly
argument_list|()
decl_stmt|;
comment|// folders/names starting with dot is always skipped (eg. ".", ".camel", ".camelLock")
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"."
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// lock files should be skipped
if|if
condition|(
name|name
operator|.
name|endsWith
argument_list|(
name|FileComponent
operator|.
name|DEFAULT_LOCK_FILE_POSTFIX
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// directories so far is always regarded as matched (matching on the name is only for files)
if|if
condition|(
name|isDirectory
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getFilter
argument_list|()
operator|.
name|accept
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|endpoint
operator|.
name|getAntFilter
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|endpoint
operator|.
name|getAntFilter
argument_list|()
operator|.
name|accept
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getExclude
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|name
operator|.
name|matches
argument_list|(
name|endpoint
operator|.
name|getExclude
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|endpoint
operator|.
name|getInclude
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|matches
argument_list|(
name|endpoint
operator|.
name|getInclude
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|// use file expression for a simple dynamic file filter
if|if
condition|(
name|endpoint
operator|.
name|getFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|evaluateFileExpression
argument_list|()
expr_stmt|;
if|if
condition|(
name|fileExpressionResult
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|name
operator|.
name|equals
argument_list|(
name|fileExpressionResult
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
comment|// if done file name is enabled, then the file is only valid if a done file exists
if|if
condition|(
name|endpoint
operator|.
name|getDoneFileName
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// done file must be in same path as the file
name|String
name|doneFileName
init|=
name|endpoint
operator|.
name|createDoneFileName
argument_list|(
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|doneFileName
argument_list|,
literal|"doneFileName"
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
comment|// is it a done file name?
if|if
condition|(
name|endpoint
operator|.
name|isDoneFile
argument_list|(
name|file
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Skipping done file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|isMatched
argument_list|(
name|file
argument_list|,
name|doneFileName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Strategy to perform file matching based on endpoint configuration in terms of done file name.      *      * @param file         the file      * @param doneFileName the done file name      * @return<tt>true</tt> if the file is matched,<tt>false</tt> if not      */
DECL|method|isMatched (GenericFile<T> file, String doneFileName)
specifier|protected
name|boolean
name|isMatched
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|String
name|doneFileName
parameter_list|)
block|{
comment|// the file is only valid if the done file exist
if|if
condition|(
operator|!
name|operations
operator|.
name|existsFile
argument_list|(
name|doneFileName
argument_list|)
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Done file: {} does not exist"
argument_list|,
name|doneFileName
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// assume matched
return|return
literal|true
return|;
block|}
comment|/**      * Is the given file already in progress.      *      * @param file the file      * @return<tt>true</tt> if the file is already in progress      */
DECL|method|isInProgress (GenericFile<T> file)
specifier|protected
name|boolean
name|isInProgress
parameter_list|(
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|)
block|{
name|String
name|key
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
return|return
operator|!
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
operator|.
name|add
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|evaluateFileExpression ()
specifier|private
name|void
name|evaluateFileExpression
parameter_list|()
block|{
if|if
condition|(
name|fileExpressionResult
operator|==
literal|null
condition|)
block|{
comment|// create a dummy exchange as Exchange is needed for expression evaluation
name|Exchange
name|dummy
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|fileExpressionResult
operator|=
name|endpoint
operator|.
name|getFileName
argument_list|()
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getExchangeFileProperty (Exchange exchange)
specifier|private
name|GenericFile
argument_list|<
name|T
argument_list|>
name|getExchangeFileProperty
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|(
name|GenericFile
argument_list|<
name|T
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
comment|// prepare on startup
name|endpoint
operator|.
name|getGenericFileProcessStrategy
argument_list|()
operator|.
name|prepareOnStartup
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

