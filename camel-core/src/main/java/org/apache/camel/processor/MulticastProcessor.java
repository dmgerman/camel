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
name|concurrent
operator|.
name|ArrayBlockingQueue
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
name|Executor
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
name|RejectedExecutionException
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
name|RejectedExecutionHandler
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
name|ThreadPoolExecutor
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
name|CountingLatch
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
block|{
comment|// TODO: Use JDK CompletionService to get rid of the AsyncProcessor/AsyncCallback
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
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|isParallelProcessing
specifier|private
name|boolean
name|isParallelProcessing
decl_stmt|;
DECL|field|executor
specifier|private
name|Executor
name|executor
decl_stmt|;
DECL|field|streaming
specifier|private
specifier|final
name|boolean
name|streaming
decl_stmt|;
DECL|field|shutdown
specifier|private
specifier|final
name|AtomicBoolean
name|shutdown
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
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
argument_list|)
expr_stmt|;
block|}
DECL|method|MulticastProcessor (Collection<Processor> processors, AggregationStrategy aggregationStrategy, boolean parallelProcessing, Executor executor)
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
name|Executor
name|executor
parameter_list|)
block|{
name|this
argument_list|(
name|processors
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executor
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|MulticastProcessor (Collection<Processor> processors, AggregationStrategy aggregationStrategy, boolean parallelProcessing, Executor executor, boolean streaming)
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
name|Executor
name|executor
parameter_list|,
name|boolean
name|streaming
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
if|if
condition|(
name|isParallelProcessing
condition|)
block|{
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
else|else
block|{
comment|// setup default Executor
name|this
operator|.
name|executor
operator|=
operator|new
name|ThreadPoolExecutor
argument_list|(
name|processors
operator|.
name|size
argument_list|()
argument_list|,
name|processors
operator|.
name|size
argument_list|()
argument_list|,
literal|0
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|,
operator|new
name|ArrayBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
name|processors
operator|.
name|size
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|this
operator|.
name|streaming
operator|=
name|streaming
expr_stmt|;
block|}
comment|/**      * A helper method to convert a list of endpoints into a list of processors      */
DECL|method|toProducers (Collection<Endpoint> endpoints)
specifier|public
specifier|static
parameter_list|<
name|E
extends|extends
name|Exchange
parameter_list|>
name|Collection
argument_list|<
name|Processor
argument_list|>
name|toProducers
parameter_list|(
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
throws|throws
name|Exception
block|{
name|Collection
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
argument_list|()
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|endpoints
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|endpoint
operator|.
name|createProducer
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
return|return
literal|"Multicast"
operator|+
name|getProcessors
argument_list|()
return|;
block|}
DECL|class|ProcessCall
class|class
name|ProcessCall
implements|implements
name|Runnable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|method|ProcessCall (Exchange exchange, Processor processor, AsyncCallback callback)
specifier|public
name|ProcessCall
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AsyncCallback
name|callback
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
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
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
name|ex
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
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
comment|// Parallel Processing the producer
if|if
condition|(
name|isParallelProcessing
condition|)
block|{
name|List
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
specifier|final
name|CountingLatch
name|completedExchanges
init|=
operator|new
name|CountingLatch
argument_list|()
decl_stmt|;
name|int
name|i
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
name|i
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
name|exchanges
operator|.
name|add
argument_list|(
name|subExchange
argument_list|)
expr_stmt|;
name|completedExchanges
operator|.
name|increment
argument_list|()
expr_stmt|;
name|ProcessCall
name|call
init|=
operator|new
name|ProcessCall
argument_list|(
name|subExchange
argument_list|,
name|producer
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
name|sync
parameter_list|)
block|{
if|if
condition|(
name|streaming
operator|&&
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
name|completedExchanges
operator|.
name|decrement
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|call
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|completedExchanges
operator|.
name|await
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|streaming
operator|&&
name|aggregationStrategy
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Exchange
name|resultExchange
range|:
name|exchanges
control|)
block|{
name|doAggregate
argument_list|(
name|result
argument_list|,
name|resultExchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// we call the producer one by one sequentially
name|int
name|i
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
name|i
argument_list|,
name|pairs
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|subExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|subExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|doAggregate
argument_list|(
name|result
argument_list|,
name|subExchange
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
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
if|if
condition|(
name|result
operator|.
name|get
argument_list|()
operator|==
literal|null
condition|)
block|{
name|result
operator|.
name|set
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|set
argument_list|(
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|result
operator|.
name|get
argument_list|()
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|updateNewExchange (Exchange exchange, int i, Iterable<ProcessorExchangePair> allPairs)
specifier|protected
name|void
name|updateNewExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|i
parameter_list|,
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|allPairs
parameter_list|)
block|{
comment|// No updates needed
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
name|shutdown
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
operator|&&
name|executor
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
operator|(
operator|(
name|ThreadPoolExecutor
operator|)
name|executor
operator|)
operator|.
name|shutdown
argument_list|()
expr_stmt|;
operator|(
operator|(
name|ThreadPoolExecutor
operator|)
name|executor
operator|)
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
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
if|if
condition|(
name|executor
operator|!=
literal|null
operator|&&
name|executor
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
operator|(
operator|(
name|ThreadPoolExecutor
operator|)
name|executor
operator|)
operator|.
name|setRejectedExecutionHandler
argument_list|(
operator|new
name|RejectedExecutionHandler
argument_list|()
block|{
specifier|public
name|void
name|rejectedExecution
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|ThreadPoolExecutor
name|executor
parameter_list|)
block|{
name|ProcessCall
name|call
init|=
operator|(
name|ProcessCall
operator|)
name|runnable
decl_stmt|;
name|call
operator|.
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
name|call
operator|.
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|processors
argument_list|)
expr_stmt|;
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
DECL|method|getExecutor ()
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
return|return
name|executor
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

