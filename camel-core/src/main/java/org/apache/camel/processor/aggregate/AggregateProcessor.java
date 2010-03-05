begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
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
name|HashMap
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
name|Map
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
name|ExecutorService
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
name|atomic
operator|.
name|AtomicInteger
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
name|CamelExchangeException
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
name|Expression
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
name|processor
operator|.
name|Traceable
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
name|AggregationRepository
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
name|DefaultTimeoutMap
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
name|ExchangeHelper
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
name|LRUCache
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
name|camel
operator|.
name|util
operator|.
name|TimeoutMap
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
name|concurrent
operator|.
name|ExecutorServiceHelper
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
comment|/**  * An implementation of the<a  * href="http://camel.apache.org/aggregator2.html">Aggregator</a>  * pattern where a batch of messages are processed (up to a maximum amount or  * until some timeout is reached) and messages for the same correlation key are  * combined together using some kind of {@link AggregationStrategy}  * (by default the latest message is used) to compress many message exchanges  * into a smaller number of exchanges.  *<p/>  * A good example of this is stock market data; you may be receiving 30,000  * messages/second and you may want to throttle it right down so that multiple  * messages for the same stock are combined (or just the latest message is used  * and older prices are discarded). Another idea is to combine line item messages  * together into a single invoice message.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|AggregateProcessor
specifier|public
class|class
name|AggregateProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
implements|,
name|Traceable
block|{
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
name|AggregateProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
specifier|final
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|correlationExpression
specifier|private
specifier|final
name|Expression
name|correlationExpression
decl_stmt|;
DECL|field|timeoutMap
specifier|private
name|TimeoutMap
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
name|timeoutMap
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|field|aggregationRepository
specifier|private
name|AggregationRepository
argument_list|<
name|Object
argument_list|>
name|aggregationRepository
init|=
operator|new
name|MemoryAggregationRepository
argument_list|()
decl_stmt|;
DECL|field|closedCorrelationKeys
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|closedCorrelationKeys
decl_stmt|;
comment|// options
DECL|field|ignoreBadCorrelationKeys
specifier|private
name|boolean
name|ignoreBadCorrelationKeys
decl_stmt|;
DECL|field|closeCorrelationKeyOnCompletion
specifier|private
name|Integer
name|closeCorrelationKeyOnCompletion
decl_stmt|;
DECL|field|parallelProcessing
specifier|private
name|boolean
name|parallelProcessing
decl_stmt|;
comment|// different ways to have completion triggered
DECL|field|eagerCheckCompletion
specifier|private
name|boolean
name|eagerCheckCompletion
decl_stmt|;
DECL|field|completionPredicate
specifier|private
name|Predicate
name|completionPredicate
decl_stmt|;
DECL|field|completionTimeout
specifier|private
name|long
name|completionTimeout
decl_stmt|;
DECL|field|completionTimeoutExpression
specifier|private
name|Expression
name|completionTimeoutExpression
decl_stmt|;
DECL|field|completionSize
specifier|private
name|int
name|completionSize
decl_stmt|;
DECL|field|completionSizeExpression
specifier|private
name|Expression
name|completionSizeExpression
decl_stmt|;
DECL|field|completionFromBatchConsumer
specifier|private
name|boolean
name|completionFromBatchConsumer
decl_stmt|;
DECL|field|batchConsumerCounter
specifier|private
name|AtomicInteger
name|batchConsumerCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|AggregateProcessor (Processor processor, Expression correlationExpression, AggregationStrategy aggregationStrategy)
specifier|public
name|AggregateProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Expression
name|correlationExpression
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
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
name|correlationExpression
argument_list|,
literal|"correlationExpression"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|aggregationStrategy
argument_list|,
literal|"aggregationStrategy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|correlationExpression
operator|=
name|correlationExpression
expr_stmt|;
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
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
literal|"AggregateProcessor[to: "
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"aggregate["
operator|+
name|correlationExpression
operator|+
literal|"]"
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
comment|// compute correlation expression
name|Object
name|key
init|=
name|correlationExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
condition|)
block|{
comment|// we have a bad correlation key
if|if
condition|(
name|isIgnoreBadCorrelationKeys
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
literal|"Correlation key could not be evaluated to a value. Exchange will be ignored: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Correlation key could not be evaluated to a value"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
comment|// is the correlation key closed?
if|if
condition|(
name|closedCorrelationKeys
operator|!=
literal|null
operator|&&
name|closedCorrelationKeys
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClosedCorrelationKeyException
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|doAggregation
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Aggregates the exchange with the given correlation key      *<p/>      * This method<b>must</b> be run synchronized as we cannot aggregate the same correlation key      * in parallel.      *      * @param key the correlation key      * @param exchange the exchange      * @return the aggregated exchange      */
DECL|method|doAggregation (Object key, Exchange exchange)
specifier|private
specifier|synchronized
name|Exchange
name|doAggregation
parameter_list|(
name|Object
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// when memory based then its fast using synchronized, but if the aggregation repository is IO
comment|// bound such as JPA etc then concurrent aggregation per correlation key could
comment|// improve performance as we can run aggregation repository get/add in parallel
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
literal|"onAggregation +++ start +++ with correlation key: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|answer
decl_stmt|;
name|Exchange
name|oldExchange
init|=
name|aggregationRepository
operator|.
name|get
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|Exchange
name|newExchange
init|=
name|exchange
decl_stmt|;
name|Integer
name|size
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|size
operator|=
name|oldExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
literal|0
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
expr_stmt|;
name|size
operator|++
expr_stmt|;
block|}
comment|// check if we are complete
name|boolean
name|complete
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|isEagerCheckCompletion
argument_list|()
condition|)
block|{
comment|// put the current aggregated size on the exchange so its avail during completion check
name|newExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|complete
operator|=
name|isCompleted
argument_list|(
name|key
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
comment|// remove it afterwards
name|newExchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|)
expr_stmt|;
block|}
comment|// prepare the exchanges for aggregation and aggregate it
name|ExchangeHelper
operator|.
name|prepareAggregation
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
name|answer
operator|=
name|onAggregation
argument_list|(
name|oldExchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|size
argument_list|)
expr_stmt|;
comment|// maybe we should check completion after the aggregation
if|if
condition|(
operator|!
name|isEagerCheckCompletion
argument_list|()
condition|)
block|{
comment|// put the current aggregated size on the exchange so its avail during completion check
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_SIZE
argument_list|,
name|size
argument_list|)
expr_stmt|;
name|complete
operator|=
name|isCompleted
argument_list|(
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
comment|// only need to update aggregation repository if we are not complete
if|if
condition|(
operator|!
name|complete
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
literal|"In progress aggregated exchange: "
operator|+
name|answer
operator|+
literal|" with correlation key:"
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|aggregationRepository
operator|.
name|add
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|key
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|complete
condition|)
block|{
name|onCompletion
argument_list|(
name|key
argument_list|,
name|answer
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
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
literal|"onAggregation +++  end  +++ with correlation key: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|isCompleted (Object key, Exchange exchange)
specifier|protected
name|boolean
name|isCompleted
parameter_list|(
name|Object
name|key
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|answer
init|=
name|getCompletionPredicate
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"predicate"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
if|if
condition|(
name|getCompletionSizeExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Integer
name|value
init|=
name|getCompletionSizeExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|>
literal|0
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
name|AGGREGATED_SIZE
argument_list|,
literal|1
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|>=
name|value
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"size"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
if|if
condition|(
name|getCompletionSize
argument_list|()
operator|>
literal|0
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
name|AGGREGATED_SIZE
argument_list|,
literal|1
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|>=
name|getCompletionSize
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"size"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
comment|// timeout can be either evaluated based on an expression or from a fixed value
comment|// expression takes precedence
name|boolean
name|timeoutSet
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|getCompletionTimeoutExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Long
name|value
init|=
name|getCompletionTimeoutExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|value
operator|>
literal|0
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
literal|"Updating correlation key "
operator|+
name|key
operator|+
literal|" to timeout after "
operator|+
name|value
operator|+
literal|" ms. as exchange received: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|timeoutMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|timeoutSet
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|timeoutSet
operator|&&
name|getCompletionTimeout
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// timeout is used so use the timeout map to keep an eye on this
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
literal|"Updating correlation key "
operator|+
name|key
operator|+
literal|" to timeout after "
operator|+
name|getCompletionTimeout
argument_list|()
operator|+
literal|" ms. as exchange received: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|timeoutMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|,
name|getCompletionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCompletionFromBatchConsumer
argument_list|()
condition|)
block|{
name|batchConsumerCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
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
literal|0
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|size
operator|>
literal|0
operator|&&
name|batchConsumerCounter
operator|.
name|intValue
argument_list|()
operator|>=
name|size
condition|)
block|{
comment|// batch consumer is complete then reset the counter
name|batchConsumerCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"consumer"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|onAggregation (Exchange oldExchange, Exchange newExchange)
specifier|protected
name|Exchange
name|onAggregation
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
return|return
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
return|;
block|}
DECL|method|onCompletion (Object key, final Exchange exchange, boolean fromTimeout)
specifier|protected
name|void
name|onCompletion
parameter_list|(
name|Object
name|key
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|fromTimeout
parameter_list|)
block|{
comment|// remove from repository as its completed
name|aggregationRepository
operator|.
name|remove
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|key
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|fromTimeout
operator|&&
name|timeoutMap
operator|!=
literal|null
condition|)
block|{
comment|// cleanup timeout map if it was a incoming exchange which triggered the timeout (and not the timeout checker)
name|timeoutMap
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
comment|// this key has been closed so add it to the closed map
if|if
condition|(
name|closedCorrelationKeys
operator|!=
literal|null
condition|)
block|{
name|closedCorrelationKeys
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|key
argument_list|)
expr_stmt|;
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
literal|"Aggregation complete for correlation key "
operator|+
name|key
operator|+
literal|" sending aggregated exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// send this exchange
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// must catch throwable so we will handle all exceptions as the executor service will by default ignore them
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error processing aggregated exchange"
argument_list|,
name|exchange
argument_list|,
name|t
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// if there was an exception then let the exception handler handle it
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
literal|"Error processing aggregated exchange"
argument_list|,
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
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
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
DECL|method|isEagerCheckCompletion ()
specifier|public
name|boolean
name|isEagerCheckCompletion
parameter_list|()
block|{
return|return
name|eagerCheckCompletion
return|;
block|}
DECL|method|setEagerCheckCompletion (boolean eagerCheckCompletion)
specifier|public
name|void
name|setEagerCheckCompletion
parameter_list|(
name|boolean
name|eagerCheckCompletion
parameter_list|)
block|{
name|this
operator|.
name|eagerCheckCompletion
operator|=
name|eagerCheckCompletion
expr_stmt|;
block|}
DECL|method|getCompletionTimeout ()
specifier|public
name|long
name|getCompletionTimeout
parameter_list|()
block|{
return|return
name|completionTimeout
return|;
block|}
DECL|method|setCompletionTimeout (long completionTimeout)
specifier|public
name|void
name|setCompletionTimeout
parameter_list|(
name|long
name|completionTimeout
parameter_list|)
block|{
name|this
operator|.
name|completionTimeout
operator|=
name|completionTimeout
expr_stmt|;
block|}
DECL|method|getCompletionTimeoutExpression ()
specifier|public
name|Expression
name|getCompletionTimeoutExpression
parameter_list|()
block|{
return|return
name|completionTimeoutExpression
return|;
block|}
DECL|method|setCompletionTimeoutExpression (Expression completionTimeoutExpression)
specifier|public
name|void
name|setCompletionTimeoutExpression
parameter_list|(
name|Expression
name|completionTimeoutExpression
parameter_list|)
block|{
name|this
operator|.
name|completionTimeoutExpression
operator|=
name|completionTimeoutExpression
expr_stmt|;
block|}
DECL|method|getCompletionSize ()
specifier|public
name|int
name|getCompletionSize
parameter_list|()
block|{
return|return
name|completionSize
return|;
block|}
DECL|method|setCompletionSize (int completionSize)
specifier|public
name|void
name|setCompletionSize
parameter_list|(
name|int
name|completionSize
parameter_list|)
block|{
name|this
operator|.
name|completionSize
operator|=
name|completionSize
expr_stmt|;
block|}
DECL|method|getCompletionSizeExpression ()
specifier|public
name|Expression
name|getCompletionSizeExpression
parameter_list|()
block|{
return|return
name|completionSizeExpression
return|;
block|}
DECL|method|setCompletionSizeExpression (Expression completionSizeExpression)
specifier|public
name|void
name|setCompletionSizeExpression
parameter_list|(
name|Expression
name|completionSizeExpression
parameter_list|)
block|{
name|this
operator|.
name|completionSizeExpression
operator|=
name|completionSizeExpression
expr_stmt|;
block|}
DECL|method|isIgnoreBadCorrelationKeys ()
specifier|public
name|boolean
name|isIgnoreBadCorrelationKeys
parameter_list|()
block|{
return|return
name|ignoreBadCorrelationKeys
return|;
block|}
DECL|method|setIgnoreBadCorrelationKeys (boolean ignoreBadCorrelationKeys)
specifier|public
name|void
name|setIgnoreBadCorrelationKeys
parameter_list|(
name|boolean
name|ignoreBadCorrelationKeys
parameter_list|)
block|{
name|this
operator|.
name|ignoreBadCorrelationKeys
operator|=
name|ignoreBadCorrelationKeys
expr_stmt|;
block|}
DECL|method|getCloseCorrelationKeyOnCompletion ()
specifier|public
name|Integer
name|getCloseCorrelationKeyOnCompletion
parameter_list|()
block|{
return|return
name|closeCorrelationKeyOnCompletion
return|;
block|}
DECL|method|setCloseCorrelationKeyOnCompletion (Integer closeCorrelationKeyOnCompletion)
specifier|public
name|void
name|setCloseCorrelationKeyOnCompletion
parameter_list|(
name|Integer
name|closeCorrelationKeyOnCompletion
parameter_list|)
block|{
name|this
operator|.
name|closeCorrelationKeyOnCompletion
operator|=
name|closeCorrelationKeyOnCompletion
expr_stmt|;
block|}
DECL|method|isCompletionFromBatchConsumer ()
specifier|public
name|boolean
name|isCompletionFromBatchConsumer
parameter_list|()
block|{
return|return
name|completionFromBatchConsumer
return|;
block|}
DECL|method|setCompletionFromBatchConsumer (boolean completionFromBatchConsumer)
specifier|public
name|void
name|setCompletionFromBatchConsumer
parameter_list|(
name|boolean
name|completionFromBatchConsumer
parameter_list|)
block|{
name|this
operator|.
name|completionFromBatchConsumer
operator|=
name|completionFromBatchConsumer
expr_stmt|;
block|}
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
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
return|;
block|}
DECL|method|setParallelProcessing (boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|boolean
name|parallelProcessing
parameter_list|)
block|{
name|this
operator|.
name|parallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
block|}
DECL|method|getAggregationRepository ()
specifier|public
name|AggregationRepository
argument_list|<
name|Object
argument_list|>
name|getAggregationRepository
parameter_list|()
block|{
return|return
name|aggregationRepository
return|;
block|}
DECL|method|setAggregationRepository (AggregationRepository<Object> aggregationRepository)
specifier|public
name|void
name|setAggregationRepository
parameter_list|(
name|AggregationRepository
argument_list|<
name|Object
argument_list|>
name|aggregationRepository
parameter_list|)
block|{
name|this
operator|.
name|aggregationRepository
operator|=
name|aggregationRepository
expr_stmt|;
block|}
comment|/**      * Background tasks that looks for aggregated exchanges which is triggered by completion timeouts.      */
DECL|class|AggregationTimeoutMap
specifier|private
specifier|final
class|class
name|AggregationTimeoutMap
extends|extends
name|DefaultTimeoutMap
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
block|{
DECL|method|AggregationTimeoutMap (ScheduledExecutorService executor, long requestMapPollTimeMillis)
specifier|private
name|AggregationTimeoutMap
parameter_list|(
name|ScheduledExecutorService
name|executor
parameter_list|,
name|long
name|requestMapPollTimeMillis
parameter_list|)
block|{
name|super
argument_list|(
name|executor
argument_list|,
name|requestMapPollTimeMillis
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onEviction (Object key, Exchange exchange)
specifier|public
name|void
name|onEviction
parameter_list|(
name|Object
name|key
parameter_list|,
name|Exchange
name|exchange
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
literal|"Completion timeout triggered for correlation key: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COMPLETED_BY
argument_list|,
literal|"timeout"
argument_list|)
expr_stmt|;
name|onCompletion
argument_list|(
name|key
argument_list|,
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|getCompletionTimeout
argument_list|()
operator|<=
literal|0
operator|&&
name|getCompletionSize
argument_list|()
operator|<=
literal|0
operator|&&
name|getCompletionPredicate
argument_list|()
operator|==
literal|null
operator|&&
operator|!
name|isCompletionFromBatchConsumer
argument_list|()
operator|&&
name|getCompletionTimeoutExpression
argument_list|()
operator|==
literal|null
operator|&&
name|getCompletionSizeExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"At least one of the completions options"
operator|+
literal|" [completionTimeout, completionSize, completionPredicate, completionFromBatchConsumer] must be set"
argument_list|)
throw|;
block|}
if|if
condition|(
name|getCloseCorrelationKeyOnCompletion
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getCloseCorrelationKeyOnCompletion
argument_list|()
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using ClosedCorrelationKeys with a LRUCache with a capacity of "
operator|+
name|getCloseCorrelationKeyOnCompletion
argument_list|()
argument_list|)
expr_stmt|;
name|closedCorrelationKeys
operator|=
operator|new
name|LRUCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|(
name|getCloseCorrelationKeyOnCompletion
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Using ClosedCorrelationKeys with unbounded capacity"
argument_list|)
expr_stmt|;
name|closedCorrelationKeys
operator|=
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|aggregationRepository
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|isParallelProcessing
argument_list|()
condition|)
block|{
comment|// we are running in parallel so create a cached thread pool which grows/shrinks automatic
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
literal|"Aggregator"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a single threaded if we are not running in parallel
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|newSingleThreadExecutor
argument_list|(
literal|"Aggregator"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|// start timeout service if its in use
if|if
condition|(
name|getCompletionTimeout
argument_list|()
operator|>
literal|0
operator|||
name|getCompletionTimeoutExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ScheduledExecutorService
name|scheduler
init|=
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|,
literal|"AggregateTimeoutChecker"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// check for timed out aggregated messages once every second
name|timeoutMap
operator|=
operator|new
name|AggregationTimeoutMap
argument_list|(
name|scheduler
argument_list|,
literal|1000L
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|timeoutMap
argument_list|)
expr_stmt|;
block|}
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
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|timeoutMap
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|aggregationRepository
argument_list|)
expr_stmt|;
if|if
condition|(
name|closedCorrelationKeys
operator|!=
literal|null
condition|)
block|{
name|closedCorrelationKeys
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

