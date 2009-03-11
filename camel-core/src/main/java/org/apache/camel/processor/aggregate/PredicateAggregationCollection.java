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
name|Predicate
import|;
end_import

begin_comment
comment|/**  * An aggregator collection which uses a predicate to decide when an aggregation is completed for  * a particular correlation key  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|PredicateAggregationCollection
specifier|public
class|class
name|PredicateAggregationCollection
extends|extends
name|DefaultAggregationCollection
block|{
DECL|field|aggregationCompletedPredicate
specifier|private
name|Predicate
name|aggregationCompletedPredicate
decl_stmt|;
DECL|field|collection
specifier|private
name|List
argument_list|<
name|Exchange
argument_list|>
name|collection
init|=
operator|new
name|ArrayList
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|PredicateAggregationCollection (Expression correlationExpression, AggregationStrategy aggregationStrategy, Predicate aggregationCompletedPredicate)
specifier|public
name|PredicateAggregationCollection
parameter_list|(
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
name|super
argument_list|(
name|correlationExpression
argument_list|,
name|aggregationStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|aggregationCompletedPredicate
operator|=
name|aggregationCompletedPredicate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onAggregation (Object correlationKey, Exchange newExchange)
specifier|public
name|void
name|onAggregation
parameter_list|(
name|Object
name|correlationKey
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|aggregationCompletedPredicate
operator|.
name|matches
argument_list|(
name|newExchange
argument_list|)
condition|)
block|{
comment|// this exchange has now aggregated so lets add it to the collection of things to send
name|super
operator|.
name|getAggregated
argument_list|()
operator|.
name|remove
argument_list|(
name|correlationKey
argument_list|)
expr_stmt|;
name|collection
operator|.
name|add
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Exchange
argument_list|>
name|iterator
parameter_list|()
block|{
return|return
name|collection
operator|.
name|iterator
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|collection
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|collection
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

