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
name|AbstractCollection
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
name|LinkedHashMap
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
comment|/**  * A {@link Collection} which aggregates exchanges together using a correlation  * expression so that there is only a single message exchange sent for a single  * correlation key.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultAggregationCollection
specifier|public
class|class
name|DefaultAggregationCollection
extends|extends
name|AbstractCollection
argument_list|<
name|Exchange
argument_list|>
implements|implements
name|AggregationCollection
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
name|DefaultAggregationCollection
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|correlationExpression
specifier|private
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|map
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
name|map
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|DefaultAggregationCollection ()
specifier|public
name|DefaultAggregationCollection
parameter_list|()
block|{     }
DECL|method|DefaultAggregationCollection (Expression<Exchange> correlationExpression, AggregationStrategy aggregationStrategy)
specifier|public
name|DefaultAggregationCollection
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
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
DECL|method|getMap ()
specifier|protected
name|Map
argument_list|<
name|Object
argument_list|,
name|Exchange
argument_list|>
name|getMap
parameter_list|()
block|{
return|return
name|map
return|;
block|}
annotation|@
name|Override
DECL|method|add (Exchange exchange)
specifier|public
name|boolean
name|add
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|correlationKey
init|=
name|correlationExpression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Exchange
name|oldExchange
init|=
name|map
operator|.
name|get
argument_list|(
name|correlationKey
argument_list|)
decl_stmt|;
name|Exchange
name|newExchange
init|=
name|exchange
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|Integer
name|count
init|=
name|oldExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COUNT
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|count
operator|==
literal|null
condition|)
block|{
name|count
operator|=
literal|1
expr_stmt|;
block|}
name|count
operator|++
expr_stmt|;
name|newExchange
operator|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
name|newExchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|AGGREGATED_COUNT
argument_list|,
name|count
argument_list|)
expr_stmt|;
block|}
comment|// the strategy may just update the old exchange and return it
if|if
condition|(
name|newExchange
operator|!=
name|oldExchange
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"put exchange:"
operator|+
name|newExchange
operator|+
literal|" for key:"
operator|+
name|correlationKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|oldExchange
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
name|AGGREGATED_COUNT
argument_list|,
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|put
argument_list|(
name|correlationKey
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
block|}
name|onAggregation
argument_list|(
name|correlationKey
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
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
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|map
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
name|map
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/**      * A strategy method allowing derived classes such as {@link PredicateAggregationCollection}      * to check to see if the aggregation has completed      */
DECL|method|onAggregation (Object correlationKey, Exchange newExchange)
specifier|protected
name|void
name|onAggregation
parameter_list|(
name|Object
name|correlationKey
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{     }
DECL|method|getCorrelationExpression ()
specifier|public
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlationExpression
return|;
block|}
DECL|method|setCorrelationExpression (Expression<Exchange> correlationExpression)
specifier|public
name|void
name|setCorrelationExpression
parameter_list|(
name|Expression
argument_list|<
name|Exchange
argument_list|>
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|correlationExpression
operator|=
name|correlationExpression
expr_stmt|;
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
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

