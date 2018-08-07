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
name|io
operator|.
name|Closeable
import|;
end_import

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
name|Collection
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
name|Iterator
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
name|ExecutorService
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
name|AsyncProcessor
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
name|RuntimeCamelException
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
name|processor
operator|.
name|aggregate
operator|.
name|ShareUnitOfWorkAggregationStrategy
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
name|UseOriginalAggregationStrategy
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
name|IOHelper
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
comment|/**  * Implements a dynamic<a  * href="http://camel.apache.org/splitter.html">Splitter</a> pattern  * where an expression is evaluated to iterate through each of the parts of a  * message and then each part is then send to some endpoint.  *  * @version   */
end_comment

begin_class
DECL|class|Splitter
specifier|public
class|class
name|Splitter
extends|extends
name|MulticastProcessor
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
block|{
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|method|Splitter (CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy)
specifier|public
name|Splitter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Processor
name|destination
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
name|destination
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
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|Splitter (CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService, boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean useSubUnitOfWork)
specifier|public
name|Splitter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Processor
name|destination
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
name|shutdownExecutorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Processor
name|onPrepare
parameter_list|,
name|boolean
name|useSubUnitOfWork
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
name|destination
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|,
name|streaming
argument_list|,
name|stopOnException
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|useSubUnitOfWork
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|Splitter (CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService, boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean useSubUnitOfWork, boolean parallelAggregate)
specifier|public
name|Splitter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Processor
name|destination
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
name|shutdownExecutorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Processor
name|onPrepare
parameter_list|,
name|boolean
name|useSubUnitOfWork
parameter_list|,
name|boolean
name|parallelAggregate
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
name|destination
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|,
name|streaming
argument_list|,
name|stopOnException
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|useSubUnitOfWork
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|Splitter (CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService, boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean useSubUnitOfWork, boolean parallelAggregate, boolean stopOnAggregateException)
specifier|public
name|Splitter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|Processor
name|destination
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
name|shutdownExecutorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Processor
name|onPrepare
parameter_list|,
name|boolean
name|useSubUnitOfWork
parameter_list|,
name|boolean
name|parallelAggregate
parameter_list|,
name|boolean
name|stopOnAggregateException
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
name|Collections
operator|.
name|singleton
argument_list|(
name|destination
argument_list|)
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|,
name|streaming
argument_list|,
name|stopOnException
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|useSubUnitOfWork
argument_list|,
name|parallelAggregate
argument_list|,
name|stopOnAggregateException
argument_list|)
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|destination
argument_list|,
literal|"destination"
argument_list|)
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
literal|"Splitter[on: "
operator|+
name|expression
operator|+
literal|" to: "
operator|+
name|getProcessors
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|+
literal|" aggregate: "
operator|+
name|getAggregationStrategy
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"split["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|AggregationStrategy
name|strategy
init|=
name|getAggregationStrategy
argument_list|()
decl_stmt|;
comment|// set original exchange if not already pre-configured
if|if
condition|(
name|strategy
operator|instanceof
name|UseOriginalAggregationStrategy
condition|)
block|{
comment|// need to create a new private instance, as we can also have concurrency issue so we cannot store state
name|UseOriginalAggregationStrategy
name|original
init|=
operator|(
name|UseOriginalAggregationStrategy
operator|)
name|strategy
decl_stmt|;
name|UseOriginalAggregationStrategy
name|clone
init|=
name|original
operator|.
name|newInstance
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|setAggregationStrategyOnExchange
argument_list|(
name|exchange
argument_list|,
name|clone
argument_list|)
expr_stmt|;
block|}
comment|// if no custom aggregation strategy is being used then fallback to keep the original
comment|// and propagate exceptions which is done by a per exchange specific aggregation strategy
comment|// to ensure it supports async routing
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
name|AggregationStrategy
name|original
init|=
operator|new
name|UseOriginalAggregationStrategy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|isShareUnitOfWork
argument_list|()
condition|)
block|{
name|original
operator|=
operator|new
name|ShareUnitOfWorkAggregationStrategy
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
name|setAggregationStrategyOnExchange
argument_list|(
name|exchange
argument_list|,
name|original
argument_list|)
expr_stmt|;
block|}
return|return
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
annotation|@
name|Override
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
throws|throws
name|Exception
block|{
name|Object
name|value
init|=
name|expression
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
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// force any exceptions occurred during evaluation to be thrown
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|answer
decl_stmt|;
if|if
condition|(
name|isStreaming
argument_list|()
condition|)
block|{
name|answer
operator|=
name|createProcessorExchangePairsIterable
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|createProcessorExchangePairsList
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
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
comment|// force any exceptions occurred during creation of exchange paris to be thrown
comment|// before returning the answer;
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createProcessorExchangePairsIterable (final Exchange exchange, final Object value)
specifier|private
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|createProcessorExchangePairsIterable
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
return|return
operator|new
name|SplitterIterable
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|class|SplitterIterable
specifier|private
specifier|final
class|class
name|SplitterIterable
implements|implements
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
implements|,
name|Closeable
block|{
comment|// create a copy which we use as master to copy during splitting
comment|// this avoids any side effect reflected upon the incoming exchange
DECL|field|value
specifier|final
name|Object
name|value
decl_stmt|;
DECL|field|iterator
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
decl_stmt|;
DECL|field|copy
specifier|private
specifier|final
name|Exchange
name|copy
decl_stmt|;
DECL|field|routeContext
specifier|private
specifier|final
name|RouteContext
name|routeContext
decl_stmt|;
DECL|field|original
specifier|private
specifier|final
name|Exchange
name|original
decl_stmt|;
DECL|method|SplitterIterable (Exchange exchange, Object value)
specifier|private
name|SplitterIterable
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|original
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
name|this
operator|.
name|iterator
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|this
operator|.
name|copy
operator|=
name|copyExchangeNoAttachments
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeContext
operator|=
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
name|getRouteContext
argument_list|()
else|:
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|ProcessorExchangePair
argument_list|>
argument_list|()
block|{
specifier|private
name|int
name|index
decl_stmt|;
specifier|private
name|boolean
name|closed
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
name|closed
condition|)
block|{
return|return
literal|false
return|;
block|}
name|boolean
name|answer
init|=
name|iterator
operator|.
name|hasNext
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|answer
condition|)
block|{
comment|// we are now closed
name|closed
operator|=
literal|true
expr_stmt|;
comment|// nothing more so we need to close the expression value in case it needs to be
try|try
block|{
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Scanner aborted because of an IOException!"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
specifier|public
name|ProcessorExchangePair
name|next
parameter_list|()
block|{
name|Object
name|part
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|part
operator|!=
literal|null
condition|)
block|{
comment|// create a correlated copy as the new exchange to be routed in the splitter from the copy
comment|// and do not share the unit of work
name|Exchange
name|newExchange
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|copy
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// If the splitter has an aggregation strategy
comment|// then the StreamCache created by the child routes must not be
comment|// closed by the unit of work of the child route, but by the unit of
comment|// work of the parent route or grand parent route or grand grand parent route... (in case of nesting).
comment|// Therefore, set the unit of work of the parent route as stream cache unit of work, if not already set.
if|if
condition|(
name|newExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|STREAM_CACHE_UNIT_OF_WORK
argument_list|)
operator|==
literal|null
condition|)
block|{
name|newExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|STREAM_CACHE_UNIT_OF_WORK
argument_list|,
name|original
operator|.
name|getUnitOfWork
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// if we share unit of work, we need to prepare the child exchange
if|if
condition|(
name|isShareUnitOfWork
argument_list|()
condition|)
block|{
name|prepareSharedUnitOfWork
argument_list|(
name|newExchange
argument_list|,
name|copy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|part
operator|instanceof
name|Message
condition|)
block|{
name|newExchange
operator|.
name|setIn
argument_list|(
operator|(
name|Message
operator|)
name|part
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|in
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|in
operator|.
name|setBody
argument_list|(
name|part
argument_list|)
expr_stmt|;
block|}
return|return
name|createProcessorExchangePair
argument_list|(
name|index
operator|++
argument_list|,
name|getProcessors
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
argument_list|,
name|newExchange
argument_list|,
name|routeContext
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Remove is not supported by this iterator"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|IOException
block|{
name|IOHelper
operator|.
name|closeIterator
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createProcessorExchangePairsList (Exchange exchange, Object value)
specifier|private
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|createProcessorExchangePairsList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
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
argument_list|<>
argument_list|()
decl_stmt|;
comment|// reuse iterable and add it to the result list
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|pairs
init|=
name|createProcessorExchangePairsIterable
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|ProcessorExchangePair
name|pair
range|:
name|pairs
control|)
block|{
if|if
condition|(
name|pair
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|pair
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|pairs
operator|instanceof
name|Closeable
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
operator|(
name|Closeable
operator|)
name|pairs
argument_list|,
literal|"Splitter:ProcessorExchangePairs"
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|updateNewExchange (Exchange exchange, int index, Iterable<ProcessorExchangePair> allPairs, Iterator<ProcessorExchangePair> it)
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
parameter_list|,
name|Iterator
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|it
parameter_list|)
block|{
comment|// do not share unit of work
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
if|if
condition|(
name|allPairs
operator|instanceof
name|Collection
condition|)
block|{
comment|// non streaming mode, so we know the total size already
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_SIZE
argument_list|,
operator|(
operator|(
name|Collection
argument_list|<
name|?
argument_list|>
operator|)
name|allPairs
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_COMPLETE
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_COMPLETE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// streaming mode, so set total size when we are complete based on the index
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SPLIT_SIZE
argument_list|,
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExchangeIndex (Exchange exchange)
specifier|protected
name|Integer
name|getExchangeIndex
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
name|SPLIT_INDEX
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|copyExchangeNoAttachments (Exchange exchange, boolean preserveExchangeId)
specifier|private
specifier|static
name|Exchange
name|copyExchangeNoAttachments
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|boolean
name|preserveExchangeId
parameter_list|)
block|{
name|Exchange
name|answer
init|=
name|ExchangeHelper
operator|.
name|createCopy
argument_list|(
name|exchange
argument_list|,
name|preserveExchangeId
argument_list|)
decl_stmt|;
comment|// we do not want attachments for the splitted sub-messages
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|setAttachmentObjects
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// we do not want to copy the message history for splitted sub-messages
name|answer
operator|.
name|getProperties
argument_list|()
operator|.
name|remove
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

