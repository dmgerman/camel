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
name|ThreadPoolExecutor
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
comment|/**  * Implements a dynamic<a  * href="http://activemq.apache.org/camel/splitter.html">Splitter</a> pattern  * where an expression is evaluated to iterate through each of the parts of a  * message and then each part is then send to some endpoint.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|Splitter
specifier|public
class|class
name|Splitter
extends|extends
name|MulticastProcessor
implements|implements
name|Processor
block|{
DECL|field|SPLIT_SIZE
specifier|public
specifier|static
specifier|final
name|String
name|SPLIT_SIZE
init|=
literal|"org.apache.camel.splitSize"
decl_stmt|;
DECL|field|SPLIT_COUNTER
specifier|public
specifier|static
specifier|final
name|String
name|SPLIT_COUNTER
init|=
literal|"org.apache.camel.splitCounter"
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|method|Splitter (Expression expression, Processor destination, AggregationStrategy aggregationStrategy)
specifier|public
name|Splitter
parameter_list|(
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
argument_list|)
expr_stmt|;
block|}
DECL|method|Splitter (Expression expression, Processor destination, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ThreadPoolExecutor threadPoolExecutor, boolean streaming)
specifier|public
name|Splitter
parameter_list|(
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
name|ThreadPoolExecutor
name|threadPoolExecutor
parameter_list|,
name|boolean
name|streaming
parameter_list|)
block|{
name|super
argument_list|(
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
name|threadPoolExecutor
argument_list|,
name|streaming
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
DECL|method|createProcessorExchangePairsIterable (final Exchange exchange, Object value)
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
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|iterator
operator|.
name|hasNext
argument_list|()
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
return|return
operator|new
name|ProcessorExchangePair
argument_list|(
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
literal|"remove is not supported by this iterator"
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
name|result
operator|.
name|add
argument_list|(
operator|new
name|ProcessorExchangePair
argument_list|(
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SPLIT_COUNTER
argument_list|,
name|i
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
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SPLIT_SIZE
argument_list|,
operator|(
operator|(
name|Collection
operator|)
name|allPairs
operator|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

