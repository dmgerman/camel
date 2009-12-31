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
name|List
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
name|Callable
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
name|CompletionService
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
name|ExecutionException
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
name|ExecutorCompletionService
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
name|Future
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
name|atomic
operator|.
name|AtomicBoolean
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
name|Producer
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
name|builder
operator|.
name|ErrorHandlerBuilder
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
name|aggregate
operator|.
name|AggregationStrategy
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
name|RouteContext
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
name|TracedRouteNodes
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
name|concurrent
operator|.
name|AtomicExchange
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
name|camel
operator|.
name|util
operator|.
name|concurrent
operator|.
name|SubmitOrderedCompletionService
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Implements the Multicast pattern to send a message exchange to a number of  * endpoints, each endpoint receiving a copy of the message exchange.  *  * @see Pipeline  * @version $Revision$  */
end_comment

begin_class
DECL|class|MulticastProcessor
specifier|public
class|class
name|MulticastProcessor
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
DECL|field|DEFAULT_THREADPOOL_SIZE
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_THREADPOOL_SIZE
init|=
literal|10
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
name|MulticastProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Class that represent each step in the multicast route to do      */
DECL|class|ProcessorExchangePair
specifier|static
class|class
name|ProcessorExchangePair
block|{
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|method|ProcessorExchangePair (Processor processor, Exchange exchange)
specifier|public
name|ProcessorExchangePair
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
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
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
block|}
DECL|field|processors
specifier|private
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
specifier|final
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|isParallelProcessing
specifier|private
specifier|final
name|boolean
name|isParallelProcessing
decl_stmt|;
DECL|field|streaming
specifier|private
specifier|final
name|boolean
name|streaming
decl_stmt|;
DECL|field|stopOnException
specifier|private
specifier|final
name|boolean
name|stopOnException
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|method|MulticastProcessor (Collection<Processor> processors)
specifier|public
name|MulticastProcessor
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|)
block|{
name|this
argument_list|(
name|processors
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|MulticastProcessor (Collection<Processor> processors, AggregationStrategy aggregationStrategy)
specifier|public
name|MulticastProcessor
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
argument_list|(
name|processors
argument_list|,
name|aggregationStrategy
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|MulticastProcessor (Collection<Processor> processors, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean streaming, boolean stopOnException)
specifier|public
name|MulticastProcessor
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|boolean
name|parallelProcessing
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|)
block|{
name|notNull
argument_list|(
name|processors
argument_list|,
literal|"processors"
argument_list|)
expr_stmt|;
name|this
operator|.
name|processors
operator|=
name|processors
expr_stmt|;
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
name|this
operator|.
name|isParallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|streaming
operator|=
name|streaming
expr_stmt|;
name|this
operator|.
name|stopOnException
operator|=
name|stopOnException
expr_stmt|;
if|if
condition|(
name|isParallelProcessing
argument_list|()
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|executorService
operator|==
literal|null
condition|)
block|{
comment|// setup default executor as parallel processing requires an executor
name|this
operator|.
name|executorService
operator|=
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|DEFAULT_THREADPOOL_SIZE
argument_list|,
literal|"Multicast"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"Multicast["
operator|+
name|getProcessors
argument_list|()
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
literal|"multicast"
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
specifier|final
name|AtomicExchange
name|result
init|=
operator|new
name|AtomicExchange
argument_list|()
decl_stmt|;
specifier|final
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|pairs
init|=
name|createProcessorExchangePairs
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// multicast uses fine grained error handling on the output processors
comment|// so use try .. catch to cater for this
try|try
block|{
if|if
condition|(
name|isParallelProcessing
argument_list|()
condition|)
block|{
name|doProcessParallel
argument_list|(
name|result
argument_list|,
name|pairs
argument_list|,
name|isStreaming
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doProcessSequential
argument_list|(
name|result
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|result
operator|.
name|get
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|result
operator|.
name|get
argument_list|()
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
comment|// multicast uses error handling on its output processors and they have tried to redeliver
comment|// so we shall signal back to the other error handlers that we are exhausted and they should not
comment|// also try to redeliver as we will then do that twice
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|,
name|Boolean
operator|.
name|TRUE
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
DECL|method|doProcessParallel (final AtomicExchange result, Iterable<ProcessorExchangePair> pairs, boolean streaming)
specifier|protected
name|void
name|doProcessParallel
parameter_list|(
specifier|final
name|AtomicExchange
name|result
parameter_list|,
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|pairs
parameter_list|,
name|boolean
name|streaming
parameter_list|)
throws|throws
name|InterruptedException
throws|,
name|ExecutionException
block|{
specifier|final
name|CompletionService
argument_list|<
name|Exchange
argument_list|>
name|completion
decl_stmt|;
specifier|final
name|AtomicBoolean
name|running
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|streaming
condition|)
block|{
comment|// execute tasks in parallel+streaming and aggregate in the order they are finished (out of order sequence)
name|completion
operator|=
operator|new
name|ExecutorCompletionService
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// execute tasks in parallel and aggregate in the order the tasks are submitted (in order sequence)
name|completion
operator|=
operator|new
name|SubmitOrderedCompletionService
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
specifier|final
name|AtomicInteger
name|total
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
for|for
control|(
name|ProcessorExchangePair
name|pair
range|:
name|pairs
control|)
block|{
specifier|final
name|Processor
name|producer
init|=
name|pair
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|subExchange
init|=
name|pair
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|updateNewExchange
argument_list|(
name|subExchange
argument_list|,
name|total
operator|.
name|intValue
argument_list|()
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
name|completion
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|running
operator|.
name|get
argument_list|()
condition|)
block|{
comment|// do not start processing the task if we are not running
return|return
name|subExchange
return|;
block|}
name|doProcess
argument_list|(
name|producer
argument_list|,
name|subExchange
argument_list|)
expr_stmt|;
comment|// should we stop in case of an exception occurred during processing?
if|if
condition|(
name|stopOnException
operator|&&
name|subExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// signal to stop running
name|running
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Parallel processing failed for number "
operator|+
name|total
operator|.
name|intValue
argument_list|()
argument_list|,
name|subExchange
argument_list|,
name|subExchange
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
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
literal|"Parallel processing complete for exchange: "
operator|+
name|subExchange
argument_list|)
expr_stmt|;
block|}
return|return
name|subExchange
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|total
operator|.
name|incrementAndGet
argument_list|()
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
name|total
operator|.
name|intValue
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future
init|=
name|completion
operator|.
name|take
argument_list|()
decl_stmt|;
name|Exchange
name|subExchange
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|aggregationStrategy
operator|!=
literal|null
condition|)
block|{
name|doAggregate
argument_list|(
name|result
argument_list|,
name|subExchange
argument_list|)
expr_stmt|;
block|}
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
literal|"Done parallel processing "
operator|+
name|total
operator|+
literal|" exchanges"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doProcessSequential (AtomicExchange result, Iterable<ProcessorExchangePair> pairs)
specifier|protected
name|void
name|doProcessSequential
parameter_list|(
name|AtomicExchange
name|result
parameter_list|,
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|pairs
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ProcessorExchangePair
name|pair
range|:
name|pairs
control|)
block|{
name|Processor
name|producer
init|=
name|pair
operator|.
name|getProcessor
argument_list|()
decl_stmt|;
name|Exchange
name|subExchange
init|=
name|pair
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|updateNewExchange
argument_list|(
name|subExchange
argument_list|,
name|total
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
name|doProcess
argument_list|(
name|producer
argument_list|,
name|subExchange
argument_list|)
expr_stmt|;
comment|// should we stop in case of an exception occurred during processing?
if|if
condition|(
name|stopOnException
operator|&&
name|subExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Sequential processing failed for number "
operator|+
name|total
argument_list|,
name|subExchange
argument_list|,
name|subExchange
operator|.
name|getException
argument_list|()
argument_list|)
throw|;
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
literal|"Sequential processing complete for number "
operator|+
name|total
operator|+
literal|" exchange: "
operator|+
name|subExchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|aggregationStrategy
operator|!=
literal|null
condition|)
block|{
name|doAggregate
argument_list|(
name|result
argument_list|,
name|subExchange
argument_list|)
expr_stmt|;
block|}
name|total
operator|++
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
literal|"Done sequential processing "
operator|+
name|total
operator|+
literal|" exchanges"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doProcess (Processor producer, Exchange exchange)
specifier|private
name|void
name|doProcess
parameter_list|(
name|Processor
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|TracedRouteNodes
name|traced
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
else|:
literal|null
decl_stmt|;
try|try
block|{
comment|// prepare tracing starting from a new block
if|if
condition|(
name|traced
operator|!=
literal|null
condition|)
block|{
name|traced
operator|.
name|pushBlock
argument_list|()
expr_stmt|;
block|}
comment|// set property which endpoint we send to
name|setToEndpoint
argument_list|(
name|exchange
argument_list|,
name|producer
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// wrap the producer in error handler so we have fine grained error handling on
comment|// the output side instead of the input side
comment|// this is needed to support redelivery on that output alone and not doing redelivery
comment|// for the entire multicast block again which will start from scratch again
name|RouteContext
name|routeContext
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
decl_stmt|;
name|ErrorHandlerBuilder
name|builder
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
comment|// create error handler (create error handler directly to keep it light weight,
comment|// instead of using ProcessorDefinition.wrapInErrorHandler)
name|producer
operator|=
name|builder
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
comment|// let the producer process it
name|producer
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
finally|finally
block|{
comment|// pop the block so by next round we have the same staring point and thus the tracing looks accurate
if|if
condition|(
name|traced
operator|!=
literal|null
condition|)
block|{
name|traced
operator|.
name|popBlock
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Aggregate the {@link Exchange} with the current result      *      * @param result the current result      * @param exchange the exchange to be added to the result      */
DECL|method|doAggregate (AtomicExchange result, Exchange exchange)
specifier|protected
specifier|synchronized
name|void
name|doAggregate
parameter_list|(
name|AtomicExchange
name|result
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|aggregationStrategy
operator|!=
literal|null
condition|)
block|{
comment|// prepare the exchanges for aggregation
name|Exchange
name|oldExchange
init|=
name|result
operator|.
name|get
argument_list|()
decl_stmt|;
name|ExchangeHelper
operator|.
name|prepareAggregation
argument_list|(
name|oldExchange
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|result
operator|.
name|set
argument_list|(
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|updateNewExchange (Exchange exchange, int index, Iterable<ProcessorExchangePair> allPairs)
specifier|protected
name|void
name|updateNewExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|index
parameter_list|,
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|allPairs
parameter_list|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|MULTICAST_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
DECL|method|createProcessorExchangePairs (Exchange exchange)
specifier|protected
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|createProcessorExchangePairs
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorExchangePair
argument_list|>
argument_list|(
name|processors
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Processor
name|processor
range|:
name|processors
control|)
block|{
name|Exchange
name|copy
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
name|result
operator|.
name|add
argument_list|(
operator|new
name|ProcessorExchangePair
argument_list|(
name|processor
argument_list|,
name|copy
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
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
operator|.
name|awaitTermination
argument_list|(
literal|0
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|processors
argument_list|)
expr_stmt|;
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
name|processors
argument_list|)
expr_stmt|;
block|}
DECL|method|setToEndpoint (Exchange exchange, Processor processor)
specifier|private
specifier|static
name|void
name|setToEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|Producer
condition|)
block|{
name|Producer
name|producer
init|=
operator|(
name|Producer
operator|)
name|processor
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Is the multicast processor working in streaming mode?      *       * In streaming mode:      *<ul>      *<li>we use {@link Iterable} to ensure we can send messages as soon as the data becomes available</li>      *<li>for parallel processing, we start aggregating responses as they get send back to the processor;      * this means the {@link org.apache.camel.processor.aggregate.AggregationStrategy} has to take care of handling out-of-order arrival of exchanges</li>      *</ul>      */
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
name|streaming
return|;
block|}
comment|/**      * Should the multicast processor stop processing further exchanges in case of an exception occurred?      */
DECL|method|isStopOnException ()
specifier|public
name|boolean
name|isStopOnException
parameter_list|()
block|{
return|return
name|stopOnException
return|;
block|}
comment|/**      * Returns the producers to multicast to      */
DECL|method|getProcessors ()
specifier|public
name|Collection
argument_list|<
name|Processor
argument_list|>
name|getProcessors
parameter_list|()
block|{
return|return
name|processors
return|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|isParallelProcessing
return|;
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
return|return
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
name|processors
argument_list|)
return|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|processors
operator|!=
literal|null
operator|&&
operator|!
name|processors
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

