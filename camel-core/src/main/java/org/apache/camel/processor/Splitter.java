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
name|Scanner
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
name|util
operator|.
name|CollectionHelper
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
comment|/**  * Implements a dynamic<a  * href="http://camel.apache.org/splitter.html">Splitter</a> pattern  * where an expression is evaluated to iterate through each of the parts of a  * message and then each part is then send to some endpoint.  *  * @version $Revision$  */
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
name|Splitter
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|)
expr_stmt|;
block|}
DECL|method|Splitter (CamelContext camelContext, Expression expression, Processor destination, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean streaming, boolean stopOnException)
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
name|streaming
parameter_list|,
name|boolean
name|stopOnException
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
name|streaming
argument_list|,
name|stopOnException
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
name|UseOriginalAggregationStrategy
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATION_STRATEGY
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
name|isStreaming
argument_list|()
condition|)
block|{
return|return
name|createProcessorExchangePairsIterable
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|createProcessorExchangePairsList
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
specifier|final
name|Iterator
name|iterator
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|value
argument_list|)
decl_stmt|;
return|return
operator|new
name|Iterable
argument_list|()
block|{
specifier|public
name|Iterator
name|iterator
parameter_list|()
block|{
return|return
operator|new
name|Iterator
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
if|if
condition|(
name|value
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
name|value
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Scanner
condition|)
block|{
comment|// special for Scanner as it does not implement Closeable
operator|(
operator|(
name|Scanner
operator|)
name|value
operator|)
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
specifier|public
name|Object
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
name|Exchange
name|newExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
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
argument_list|)
return|;
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
block|}
return|;
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
decl_stmt|;
name|Integer
name|collectionSize
init|=
name|CollectionHelper
operator|.
name|size
argument_list|(
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|collectionSize
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorExchangePair
argument_list|>
argument_list|(
name|collectionSize
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorExchangePair
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|int
name|index
init|=
literal|0
decl_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|value
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|part
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Exchange
name|newExchange
init|=
name|exchange
operator|.
name|copy
argument_list|()
decl_stmt|;
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
name|result
operator|.
name|add
argument_list|(
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
argument_list|)
argument_list|)
expr_stmt|;
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
block|}
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
block|}
end_class

end_unit

