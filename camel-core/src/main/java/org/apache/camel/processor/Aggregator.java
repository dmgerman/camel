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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationCollection
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
name|PredicateAggregationCollection
import|;
end_import

begin_comment
comment|/**  * An implementation of the<a  * href="http://activemq.apache.org/camel/aggregator.html">Aggregator</a>  * pattern where a batch of messages are processed (up to a maximum amount or  * until some timeout is reached) and messages for the same correlation key are  * combined together using some kind of  * {@link AggregationStrategy ) (by default the latest message is used) to compress  * many message exchanges * into a smaller number of exchanges.<p/> A good  * example of this is stock market data; you may be receiving 30,000  * messages/second and you may want to throttle it right down so that multiple  * messages for the same stock are combined (or just the latest message is used  * and older prices are discarded). Another idea is to combine line item  * messages together into a single invoice message.  *  * @version $Revision$  * @param correlationExpression the expression used to calculate the correlation  *                key. For a JMS message this could be the expression  *<code>header("JMSDestination")</code> or  *<code>header("JMSCorrelationID")</code>  */
end_comment

begin_class
DECL|class|Aggregator
specifier|public
class|class
name|Aggregator
extends|extends
name|BatchProcessor
block|{
DECL|field|aggregationCompletedPredicate
specifier|private
name|Predicate
name|aggregationCompletedPredicate
decl_stmt|;
DECL|method|Aggregator (Endpoint endpoint, Processor processor, Expression correlationExpression, AggregationStrategy aggregationStrategy)
specifier|public
name|Aggregator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
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
name|this
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
operator|new
name|AggregationCollection
argument_list|(
name|correlationExpression
argument_list|,
name|aggregationStrategy
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|Aggregator (Endpoint endpoint, Processor processor, Expression correlationExpression, AggregationStrategy aggregationStrategy, Predicate aggregationCompletedPredicate)
specifier|public
name|Aggregator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|Expression
name|correlationExpression
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|Predicate
name|aggregationCompletedPredicate
parameter_list|)
block|{
name|this
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
operator|new
name|PredicateAggregationCollection
argument_list|(
name|correlationExpression
argument_list|,
name|aggregationStrategy
argument_list|,
name|aggregationCompletedPredicate
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|aggregationCompletedPredicate
operator|=
name|aggregationCompletedPredicate
expr_stmt|;
block|}
DECL|method|Aggregator (Endpoint endpoint, Processor processor, AggregationCollection collection)
specifier|public
name|Aggregator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|AggregationCollection
name|collection
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|collection
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
literal|"Aggregator[to: "
operator|+
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|isBatchCompleted (int index)
specifier|protected
name|boolean
name|isBatchCompleted
parameter_list|(
name|int
name|index
parameter_list|)
block|{
if|if
condition|(
name|aggregationCompletedPredicate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getCollection
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
name|super
operator|.
name|isBatchCompleted
argument_list|(
name|index
argument_list|)
return|;
block|}
block|}
end_class

end_unit

