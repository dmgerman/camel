begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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
name|Route
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
name|ExpressionClause
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
name|model
operator|.
name|language
operator|.
name|ExpressionType
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
name|Aggregator
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
name|UseLatestAggregationStrategy
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

begin_comment
comment|/**  * Represents an XML&lt;aggregator/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"aggregator"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AggregatorType
specifier|public
class|class
name|AggregatorType
extends|extends
name|ExpressionNode
block|{
annotation|@
name|XmlTransient
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|aggregationCollection
specifier|private
name|AggregationCollection
name|aggregationCollection
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|batchSize
specifier|private
name|Integer
name|batchSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|batchTimeout
specifier|private
name|Long
name|batchTimeout
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|strategyRef
specifier|private
name|String
name|strategyRef
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"completedPredicate"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|completedPredicate
specifier|private
name|CompletedPredicate
name|completedPredicate
decl_stmt|;
DECL|method|AggregatorType ()
specifier|public
name|AggregatorType
parameter_list|()
block|{     }
DECL|method|AggregatorType (Expression correlationExpression)
specifier|public
name|AggregatorType
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|)
block|{
name|super
argument_list|(
name|correlationExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|AggregatorType (ExpressionType correlationExpression)
specifier|public
name|AggregatorType
parameter_list|(
name|ExpressionType
name|correlationExpression
parameter_list|)
block|{
name|super
argument_list|(
name|correlationExpression
argument_list|)
expr_stmt|;
block|}
DECL|method|AggregatorType (Expression correlationExpression, AggregationStrategy aggregationStrategy)
specifier|public
name|AggregatorType
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|correlationExpression
argument_list|)
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
literal|"Aggregator[ "
operator|+
name|getExpression
argument_list|()
operator|+
literal|" -> "
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"aggregator"
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
DECL|method|addRoutes (RouteContext routeContext, Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Aggregator
name|aggregator
init|=
name|createAggregator
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Route
name|route
init|=
operator|new
name|Route
argument_list|<
name|Exchange
argument_list|>
argument_list|(
name|aggregator
operator|.
name|getEndpoint
argument_list|()
argument_list|,
name|aggregator
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"AggregatorRoute["
operator|+
name|getEndpoint
argument_list|()
operator|+
literal|" -> "
operator|+
name|aggregator
operator|.
name|getProcessor
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
decl_stmt|;
name|routes
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|createAggregator
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
DECL|method|createAggregator (RouteContext routeContext)
specifier|protected
name|Aggregator
name|createAggregator
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Endpoint
name|from
init|=
name|routeContext
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
specifier|final
name|Processor
name|processor
init|=
name|routeContext
operator|.
name|createProcessor
argument_list|(
name|this
argument_list|)
decl_stmt|;
specifier|final
name|Aggregator
name|aggregator
decl_stmt|;
if|if
condition|(
name|aggregationCollection
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|=
operator|new
name|Aggregator
argument_list|(
name|from
argument_list|,
name|processor
argument_list|,
name|aggregationCollection
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|AggregationStrategy
name|strategy
init|=
name|getAggregationStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
operator|&&
name|strategyRef
operator|!=
literal|null
condition|)
block|{
name|strategy
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|strategyRef
argument_list|,
name|AggregationStrategy
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
name|strategy
operator|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
expr_stmt|;
block|}
name|Expression
name|aggregateExpression
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Predicate
name|predicate
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|completedPredicate
operator|!=
literal|null
condition|)
block|{
name|predicate
operator|=
name|completedPredicate
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|=
operator|new
name|Aggregator
argument_list|(
name|from
argument_list|,
name|processor
argument_list|,
name|aggregateExpression
argument_list|,
name|strategy
argument_list|,
name|predicate
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|aggregator
operator|=
operator|new
name|Aggregator
argument_list|(
name|from
argument_list|,
name|processor
argument_list|,
name|aggregateExpression
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|batchSize
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|.
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|batchTimeout
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|.
name|setBatchTimeout
argument_list|(
name|batchTimeout
argument_list|)
expr_stmt|;
block|}
return|return
name|aggregator
return|;
block|}
DECL|method|getAggregationCollection ()
specifier|public
name|AggregationCollection
name|getAggregationCollection
parameter_list|()
block|{
return|return
name|aggregationCollection
return|;
block|}
DECL|method|setAggregationCollection (AggregationCollection aggregationCollection)
specifier|public
name|void
name|setAggregationCollection
parameter_list|(
name|AggregationCollection
name|aggregationCollection
parameter_list|)
block|{
name|this
operator|.
name|aggregationCollection
operator|=
name|aggregationCollection
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
DECL|method|getBatchSize ()
specifier|public
name|Integer
name|getBatchSize
parameter_list|()
block|{
return|return
name|batchSize
return|;
block|}
DECL|method|setBatchSize (Integer batchSize)
specifier|public
name|void
name|setBatchSize
parameter_list|(
name|Integer
name|batchSize
parameter_list|)
block|{
name|this
operator|.
name|batchSize
operator|=
name|batchSize
expr_stmt|;
block|}
DECL|method|getBatchTimeout ()
specifier|public
name|Long
name|getBatchTimeout
parameter_list|()
block|{
return|return
name|batchTimeout
return|;
block|}
DECL|method|setBatchTimeout (Long batchTimeout)
specifier|public
name|void
name|setBatchTimeout
parameter_list|(
name|Long
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
DECL|method|getStrategyRef ()
specifier|public
name|String
name|getStrategyRef
parameter_list|()
block|{
return|return
name|strategyRef
return|;
block|}
DECL|method|setStrategyRef (String strategyRef)
specifier|public
name|void
name|setStrategyRef
parameter_list|(
name|String
name|strategyRef
parameter_list|)
block|{
name|this
operator|.
name|strategyRef
operator|=
name|strategyRef
expr_stmt|;
block|}
DECL|method|getCompletePredicate ()
specifier|public
name|CompletedPredicate
name|getCompletePredicate
parameter_list|()
block|{
return|return
name|completedPredicate
return|;
block|}
DECL|method|setCompletePredicate (CompletedPredicate completedPredicate)
specifier|public
name|void
name|setCompletePredicate
parameter_list|(
name|CompletedPredicate
name|completedPredicate
parameter_list|)
block|{
name|this
operator|.
name|completedPredicate
operator|=
name|completedPredicate
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
DECL|method|batchSize (int batchSize)
specifier|public
name|AggregatorType
name|batchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|setBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|batchTimeout (long batchTimeout)
specifier|public
name|AggregatorType
name|batchTimeout
parameter_list|(
name|long
name|batchTimeout
parameter_list|)
block|{
name|setBatchTimeout
argument_list|(
name|batchTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the predicate used to determine if the aggregation is completed      *      * @return the clause used to create the predicate      */
DECL|method|completedPredicate ()
specifier|public
name|ExpressionClause
argument_list|<
name|AggregatorType
argument_list|>
name|completedPredicate
parameter_list|()
block|{
name|checkNoCompletedPredicate
argument_list|()
expr_stmt|;
name|ExpressionClause
argument_list|<
name|AggregatorType
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|AggregatorType
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|completedPredicate
operator|=
operator|new
name|CompletedPredicate
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Sets the predicate used to determine if the aggregation is completed      */
DECL|method|completedPredicate (Predicate predicate)
specifier|public
name|AggregatorType
name|completedPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|checkNoCompletedPredicate
argument_list|()
expr_stmt|;
name|completedPredicate
operator|=
operator|new
name|CompletedPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|checkNoCompletedPredicate ()
specifier|protected
name|void
name|checkNoCompletedPredicate
parameter_list|()
block|{
if|if
condition|(
name|completedPredicate
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There already is a completedPredicate defined for this aggregator: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

