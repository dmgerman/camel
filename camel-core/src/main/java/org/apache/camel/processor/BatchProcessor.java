begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|ConcurrentLinkedQueue
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
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
name|CamelContext
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
name|Navigate
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
name|Predicate
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
name|LoggingExceptionHandler
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
name|ServiceSupport
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
name|ExceptionHandler
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
name|ServiceHelper
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
comment|/**  * A base class for any kind of {@link Processor} which implements some kind of batch processing.  *   * @version $Revision$  * @deprecated will be removed in Camel 2.4  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|BatchProcessor
specifier|public
class|class
name|BatchProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
DECL|field|DEFAULT_BATCH_TIMEOUT
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_BATCH_TIMEOUT
init|=
literal|1000L
decl_stmt|;
DECL|field|DEFAULT_BATCH_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_BATCH_SIZE
init|=
literal|100
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|BatchProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|batchTimeout
specifier|private
name|long
name|batchTimeout
init|=
name|DEFAULT_BATCH_TIMEOUT
decl_stmt|;
DECL|field|batchSize
specifier|private
name|int
name|batchSize
init|=
name|DEFAULT_BATCH_SIZE
decl_stmt|;
DECL|field|outBatchSize
specifier|private
name|int
name|outBatchSize
decl_stmt|;
DECL|field|groupExchanges
specifier|private
name|boolean
name|groupExchanges
decl_stmt|;
DECL|field|batchConsumer
specifier|private
name|boolean
name|batchConsumer
decl_stmt|;
DECL|field|completionPredicate
specifier|private
name|Predicate
name|completionPredicate
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|collection
specifier|private
specifier|final
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|collection
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|field|sender
specifier|private
specifier|final
name|BatchSender
name|sender
decl_stmt|;
DECL|method|BatchProcessor (CamelContext camelContext, Processor processor, Collection<Exchange> collection)
specifier|public
name|BatchProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|collection
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|processor
argument_list|,
literal|"processor"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|collection
argument_list|,
literal|"collection"
argument_list|)
expr_stmt|;
comment|// wrap processor in UnitOfWork so what we send out of the batch runs in a UoW
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|processor
operator|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|collection
operator|=
name|collection
expr_stmt|;
name|this
operator|.
name|sender
operator|=
operator|new
name|BatchSender
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"BatchProcessor[to: "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exceptionHandler
return|;
block|}
DECL|method|setExceptionHandler (ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
DECL|method|getBatchSize ()
specifier|public
name|int
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
comment|/**      * Sets the<b>in</b> batch size. This is the number of incoming exchanges that this batch processor will      * process before its completed. The default value is {@link #DEFAULT_BATCH_SIZE}.      *       * @param batchSize the size      */
DECL|method|setBatchSize (int batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
comment|// setting batch size to 0 or negative is like disabling it, so we set it as the max value
comment|// as the code logic is dependent on a batch size having 1..n value
if|if
condition|(
name|batchSize
operator|<=
literal|0
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
literal|"Disabling batch size, will only be triggered by timeout"
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|batchSize
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
block|}
DECL|method|getOutBatchSize ()
specifier|public
name|int
name|getOutBatchSize
parameter_list|()
block|{
return|return
name|outBatchSize
return|;
block|}
comment|/**      * Sets the<b>out</b> batch size. If the batch processor holds more exchanges than this out size then the      * completion is triggered. Can for instance be used to ensure that this batch is completed when a certain      * number of exchanges has been collected. By default this feature is<b>not</b> enabled.      *       * @param outBatchSize the size      */
DECL|method|setOutBatchSize (int outBatchSize)
specifier|public
name|void
name|setOutBatchSize
parameter_list|(
name|int
name|outBatchSize
parameter_list|)
block|{
name|this
operator|.
name|outBatchSize
operator|=
name|outBatchSize
expr_stmt|;
block|}
DECL|method|getBatchTimeout ()
specifier|public
name|long
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
DECL|method|setBatchTimeout (long batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|long
name|batchTimeout
parameter_list|)
block|{
name|this
operator|.
name|batchTimeout
operator|=
name|batchTimeout
expr_stmt|;
block|}
DECL|method|isGroupExchanges ()
specifier|public
name|boolean
name|isGroupExchanges
parameter_list|()
block|{
return|return
name|groupExchanges
return|;
block|}
DECL|method|setGroupExchanges (boolean groupExchanges)
specifier|public
name|void
name|setGroupExchanges
parameter_list|(
name|boolean
name|groupExchanges
parameter_list|)
block|{
name|this
operator|.
name|groupExchanges
operator|=
name|groupExchanges
expr_stmt|;
block|}
DECL|method|isBatchConsumer ()
specifier|public
name|boolean
name|isBatchConsumer
parameter_list|()
block|{
return|return
name|batchConsumer
return|;
block|}
DECL|method|setBatchConsumer (boolean batchConsumer)
specifier|public
name|void
name|setBatchConsumer
parameter_list|(
name|boolean
name|batchConsumer
parameter_list|)
block|{
name|this
operator|.
name|batchConsumer
operator|=
name|batchConsumer
expr_stmt|;
block|}
DECL|method|getCompletionPredicate ()
specifier|public
name|Predicate
name|getCompletionPredicate
parameter_list|()
block|{
return|return
name|completionPredicate
return|;
block|}
DECL|method|setCompletionPredicate (Predicate completionPredicate)
specifier|public
name|void
name|setCompletionPredicate
parameter_list|(
name|Predicate
name|completionPredicate
parameter_list|)
block|{
name|this
operator|.
name|completionPredicate
operator|=
name|completionPredicate
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processor
operator|!=
literal|null
return|;
block|}
comment|/**      * A strategy method to decide if the "in" batch is completed. That is, whether the resulting exchanges in      * the in queue should be drained to the "out" collection.      */
DECL|method|isInBatchCompleted (int num)
specifier|private
name|boolean
name|isInBatchCompleted
parameter_list|(
name|int
name|num
parameter_list|)
block|{
return|return
name|num
operator|>=
name|batchSize
return|;
block|}
comment|/**      * A strategy method to decide if the "out" batch is completed. That is, whether the resulting exchange in      * the out collection should be sent.      */
DECL|method|isOutBatchCompleted ()
specifier|private
name|boolean
name|isOutBatchCompleted
parameter_list|()
block|{
if|if
condition|(
name|outBatchSize
operator|==
literal|0
condition|)
block|{
comment|// out batch is disabled, so go ahead and send.
return|return
literal|true
return|;
block|}
return|return
name|collection
operator|.
name|size
argument_list|()
operator|>
literal|0
operator|&&
name|collection
operator|.
name|size
argument_list|()
operator|>=
name|outBatchSize
return|;
block|}
comment|/**      * Strategy Method to process an exchange in the batch. This method allows derived classes to perform      * custom processing before or after an individual exchange is processed      */
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
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing aggregated exchange: "
operator|+
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|sender
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|sender
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|sender
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * Enqueues an exchange for later batch processing.      */
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// if batch consumer is enabled then we need to adjust the batch size
comment|// with the size from the batch consumer
if|if
condition|(
name|isBatchConsumer
argument_list|()
condition|)
block|{
name|int
name|size
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|batchSize
operator|!=
name|size
condition|)
block|{
name|batchSize
operator|=
name|size
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
literal|"Using batch consumer completion, so setting batch size to: "
operator|+
name|batchSize
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|sender
operator|.
name|enqueueExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sender thread for queued-up exchanges.      */
DECL|class|BatchSender
specifier|private
class|class
name|BatchSender
extends|extends
name|Thread
block|{
DECL|field|queue
specifier|private
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|queue
decl_stmt|;
DECL|field|queueLock
specifier|private
name|Lock
name|queueLock
init|=
operator|new
name|ReentrantLock
argument_list|()
decl_stmt|;
DECL|field|exchangeEnqueued
specifier|private
name|boolean
name|exchangeEnqueued
decl_stmt|;
DECL|field|completionPredicateMatched
specifier|private
specifier|final
name|Queue
argument_list|<
name|String
argument_list|>
name|completionPredicateMatched
init|=
operator|new
name|ConcurrentLinkedQueue
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|exchangeEnqueuedCondition
specifier|private
name|Condition
name|exchangeEnqueuedCondition
init|=
name|queueLock
operator|.
name|newCondition
argument_list|()
decl_stmt|;
DECL|method|BatchSender ()
specifier|public
name|BatchSender
parameter_list|()
block|{
name|super
argument_list|(
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getThreadName
argument_list|(
literal|"Batch Sender"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|queue
operator|=
operator|new
name|LinkedList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
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
comment|// Wait until one of either:
comment|// * an exchange being queued;
comment|// * the batch timeout expiring; or
comment|// * the thread being cancelled.
comment|//
comment|// If an exchange is queued then we need to determine whether the
comment|// batch is complete. If it is complete then we send out the batched
comment|// exchanges. Otherwise we move back into our wait state.
comment|//
comment|// If the batch times out then we send out the batched exchanges
comment|// collected so far.
comment|//
comment|// If we receive an interrupt then all blocking operations are
comment|// interrupted and our thread terminates.
comment|//
comment|// The goal of the following algorithm in terms of synchronisation
comment|// is to provide fine grained locking i.e. retaining the lock only
comment|// when required. Special consideration is given to releasing the
comment|// lock when calling an overloaded method i.e. sendExchanges.
comment|// Unlocking is important as the process of sending out the exchanges
comment|// would otherwise block new exchanges from being queued.
name|queueLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
do|do
block|{
try|try
block|{
if|if
condition|(
operator|!
name|exchangeEnqueued
condition|)
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
literal|"Waiting for new exchange to arrive or batchTimeout to occur after "
operator|+
name|batchTimeout
operator|+
literal|" ms."
argument_list|)
expr_stmt|;
block|}
name|exchangeEnqueuedCondition
operator|.
name|await
argument_list|(
name|batchTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
comment|// if the completion predicate was triggered then there is an exchange id which denotes when to complete
name|String
name|id
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|completionPredicateMatched
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|id
operator|=
name|completionPredicateMatched
operator|.
name|poll
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|id
operator|!=
literal|null
operator|||
operator|!
name|exchangeEnqueued
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Collecting exchanges to be aggregated triggered by completion predicate"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Collecting exchanges to be aggregated triggered by batch timeout"
argument_list|)
expr_stmt|;
block|}
block|}
name|drainQueueTo
argument_list|(
name|collection
argument_list|,
name|batchSize
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchangeEnqueued
operator|=
literal|false
expr_stmt|;
name|boolean
name|drained
init|=
literal|false
decl_stmt|;
while|while
condition|(
name|isInBatchCompleted
argument_list|(
name|queue
operator|.
name|size
argument_list|()
argument_list|)
condition|)
block|{
name|drained
operator|=
literal|true
expr_stmt|;
name|drainQueueTo
argument_list|(
name|collection
argument_list|,
name|batchSize
argument_list|,
name|id
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|drained
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Collecting exchanges to be aggregated triggered by new exchanges received"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isOutBatchCompleted
argument_list|()
condition|)
block|{
continue|continue;
block|}
block|}
name|queueLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
try|try
block|{
try|try
block|{
name|sendExchanges
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// a fail safe to handle all exceptions being thrown
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|queueLock
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
break|break;
block|}
block|}
do|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
do|;
block|}
finally|finally
block|{
name|queueLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**          * This method should be called with queueLock held          */
DECL|method|drainQueueTo (Collection<Exchange> collection, int batchSize, String exchangeId)
specifier|private
name|void
name|drainQueueTo
parameter_list|(
name|Collection
argument_list|<
name|Exchange
argument_list|>
name|collection
parameter_list|,
name|int
name|batchSize
parameter_list|,
name|String
name|exchangeId
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|batchSize
condition|;
operator|++
name|i
control|)
block|{
name|Exchange
name|e
init|=
name|queue
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|collection
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|t
parameter_list|)
block|{
name|e
operator|.
name|setException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchangeId
operator|!=
literal|null
operator|&&
name|exchangeId
operator|.
name|equals
argument_list|(
name|e
operator|.
name|getExchangeId
argument_list|()
argument_list|)
condition|)
block|{
comment|// this batch is complete so stop draining
break|break;
block|}
block|}
else|else
block|{
break|break;
block|}
block|}
block|}
DECL|method|cancel ()
specifier|public
name|void
name|cancel
parameter_list|()
block|{
name|interrupt
argument_list|()
expr_stmt|;
block|}
DECL|method|enqueueExchange (Exchange exchange)
specifier|public
name|void
name|enqueueExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
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
literal|"Received exchange to be batched: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|queueLock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
comment|// pre test whether the completion predicate matched
if|if
condition|(
name|completionPredicate
operator|!=
literal|null
condition|)
block|{
name|boolean
name|matches
init|=
name|completionPredicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|matches
condition|)
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
literal|"Exchange matched completion predicate: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// add this exchange to the list of exchanges which marks the batch as complete
name|completionPredicateMatched
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|queue
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchangeEnqueued
operator|=
literal|true
expr_stmt|;
name|exchangeEnqueuedCondition
operator|.
name|signal
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|queueLock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|sendExchanges ()
specifier|private
name|void
name|sendExchanges
parameter_list|()
throws|throws
name|Exception
block|{
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iter
init|=
name|collection
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|iter
operator|.
name|remove
argument_list|()
expr_stmt|;
try|try
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
literal|"Sending aggregated exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// must catch throwable to avoid growing memory
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing aggregated exchange: "
operator|+
name|exchange
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

