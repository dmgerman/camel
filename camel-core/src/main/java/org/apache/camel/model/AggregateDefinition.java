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
name|ArrayList
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
name|XmlElementRef
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
name|ExpressionDefinition
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
comment|/**  * Represents an XML&lt;aggregate/&gt; element  *  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"aggregate"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|AggregateDefinition
specifier|public
class|class
name|AggregateDefinition
extends|extends
name|ProcessorDefinition
argument_list|<
name|AggregateDefinition
argument_list|>
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"correlationExpression"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|correlationExpression
specifier|private
name|ExpressionSubElementDefinition
name|correlationExpression
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|expression
specifier|private
name|ExpressionDefinition
name|expression
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
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
DECL|field|outBatchSize
specifier|private
name|Integer
name|outBatchSize
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
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|collectionRef
specifier|private
name|String
name|collectionRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|groupExchanges
specifier|private
name|Boolean
name|groupExchanges
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|batchSizeFromConsumer
specifier|private
name|Boolean
name|batchSizeFromConsumer
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"completionPredicate"
argument_list|,
name|required
operator|=
literal|false
argument_list|)
DECL|field|completionPredicate
specifier|private
name|ExpressionSubElementDefinition
name|completionPredicate
decl_stmt|;
DECL|method|AggregateDefinition ()
specifier|public
name|AggregateDefinition
parameter_list|()
block|{     }
DECL|method|AggregateDefinition (Predicate predicate)
specifier|public
name|AggregateDefinition
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
name|setExpression
argument_list|(
operator|new
name|ExpressionDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|AggregateDefinition (Expression correlationExpression)
specifier|public
name|AggregateDefinition
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|)
block|{
if|if
condition|(
name|correlationExpression
operator|!=
literal|null
condition|)
block|{
name|setExpression
argument_list|(
operator|new
name|ExpressionDefinition
argument_list|(
name|correlationExpression
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|AggregateDefinition (ExpressionDefinition correlationExpression)
specifier|public
name|AggregateDefinition
parameter_list|(
name|ExpressionDefinition
name|correlationExpression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|correlationExpression
expr_stmt|;
block|}
DECL|method|AggregateDefinition (Expression correlationExpression, AggregationStrategy aggregationStrategy)
specifier|public
name|AggregateDefinition
parameter_list|(
name|Expression
name|correlationExpression
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
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
name|String
name|expressionString
init|=
operator|(
name|getExpression
argument_list|()
operator|!=
literal|null
operator|)
condition|?
name|getExpression
argument_list|()
operator|.
name|getLabel
argument_list|()
else|:
literal|""
decl_stmt|;
return|return
literal|"Aggregate["
operator|+
name|expressionString
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
literal|"aggregate"
return|;
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
DECL|method|createAndSetExpression ()
specifier|public
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
name|createAndSetExpression
parameter_list|()
block|{
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|this
operator|.
name|setExpression
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
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
name|getAggregationCollection
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setAggregationCollection
argument_list|(
name|createAggregationCollection
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|aggregationCollection
operator|!=
literal|null
condition|)
block|{
comment|// create the aggregator using the collection
comment|// pre configure the collection if its expression and strategy is not set, then
comment|// use the ones that is pre configured with this type
if|if
condition|(
name|aggregationCollection
operator|.
name|getCorrelationExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
name|aggregationCollection
operator|.
name|setCorrelationExpression
argument_list|(
name|getExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|aggregationCollection
operator|.
name|getAggregationStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|AggregationStrategy
name|strategy
init|=
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|aggregationCollection
operator|.
name|setAggregationStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
name|aggregator
operator|=
operator|new
name|Aggregator
argument_list|(
name|processor
argument_list|,
name|aggregationCollection
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create the aggregator using a default collection
name|AggregationStrategy
name|strategy
init|=
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|getExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"You need to specify an expression or "
operator|+
literal|"aggregation collection for this aggregator: "
operator|+
name|this
argument_list|)
throw|;
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
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|predicate
operator|=
name|getCompletionPredicate
argument_list|()
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
if|if
condition|(
name|outBatchSize
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|.
name|setOutBatchSize
argument_list|(
name|outBatchSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|groupExchanges
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|.
name|setGroupExchanges
argument_list|(
name|groupExchanges
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|batchSizeFromConsumer
operator|!=
literal|null
condition|)
block|{
name|aggregator
operator|.
name|setBatchConsumer
argument_list|(
name|batchSizeFromConsumer
argument_list|)
expr_stmt|;
block|}
return|return
name|aggregator
return|;
block|}
DECL|method|createAggregationStrategy (RouteContext routeContext)
specifier|private
name|AggregationStrategy
name|createAggregationStrategy
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
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
comment|// fallback to use latest
name|strategy
operator|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
DECL|method|createAggregationCollection (RouteContext routeContext)
specifier|private
name|AggregationCollection
name|createAggregationCollection
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|AggregationCollection
name|collection
init|=
name|getAggregationCollection
argument_list|()
decl_stmt|;
if|if
condition|(
name|collection
operator|==
literal|null
operator|&&
name|collectionRef
operator|!=
literal|null
condition|)
block|{
name|collection
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|collectionRef
argument_list|,
name|AggregationCollection
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|collection
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
DECL|method|getOutBatchSize ()
specifier|public
name|Integer
name|getOutBatchSize
parameter_list|()
block|{
return|return
name|outBatchSize
return|;
block|}
DECL|method|setOutBatchSize (Integer outBatchSize)
specifier|public
name|void
name|setOutBatchSize
parameter_list|(
name|Integer
name|outBatchSize
parameter_list|)
block|{
name|this
operator|.
name|outBatchSize
operator|=
name|outBatchSize
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
DECL|method|getCollectionRef ()
specifier|public
name|String
name|getCollectionRef
parameter_list|()
block|{
return|return
name|collectionRef
return|;
block|}
DECL|method|setCollectionRef (String collectionRef)
specifier|public
name|void
name|setCollectionRef
parameter_list|(
name|String
name|collectionRef
parameter_list|)
block|{
name|this
operator|.
name|collectionRef
operator|=
name|collectionRef
expr_stmt|;
block|}
DECL|method|setCompletionPredicate (ExpressionSubElementDefinition completionPredicate)
specifier|public
name|void
name|setCompletionPredicate
parameter_list|(
name|ExpressionSubElementDefinition
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
DECL|method|getCompletionPredicate ()
specifier|public
name|ExpressionSubElementDefinition
name|getCompletionPredicate
parameter_list|()
block|{
return|return
name|completionPredicate
return|;
block|}
DECL|method|getGroupExchanges ()
specifier|public
name|Boolean
name|getGroupExchanges
parameter_list|()
block|{
return|return
name|groupExchanges
return|;
block|}
DECL|method|setGroupExchanges (Boolean groupExchanges)
specifier|public
name|void
name|setGroupExchanges
parameter_list|(
name|Boolean
name|groupExchanges
parameter_list|)
block|{
name|this
operator|.
name|groupExchanges
operator|=
name|groupExchanges
expr_stmt|;
block|}
DECL|method|getBatchSizeFromConsumer ()
specifier|public
name|Boolean
name|getBatchSizeFromConsumer
parameter_list|()
block|{
return|return
name|batchSizeFromConsumer
return|;
block|}
DECL|method|setBatchSizeFromConsumer (Boolean batchSizeFromConsumer)
specifier|public
name|void
name|setBatchSizeFromConsumer
parameter_list|(
name|Boolean
name|batchSizeFromConsumer
parameter_list|)
block|{
name|this
operator|.
name|batchSizeFromConsumer
operator|=
name|batchSizeFromConsumer
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Enables the batch completion mode where we aggregate from a {@link org.apache.camel.BatchConsumer}      * and aggregate the total number of exchanges the {@link org.apache.camel.BatchConsumer} has reported      * as total by setting the exchange property {@link org.apache.camel.Exchange#BATCH_SIZE}.      *      * @return builder      */
DECL|method|batchSizeFromConsumer ()
specifier|public
name|AggregateDefinition
name|batchSizeFromConsumer
parameter_list|()
block|{
name|setBatchSizeFromConsumer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the in batch size for number of exchanges received      *      * @param batchSize  the batch size      * @return builder      */
DECL|method|batchSize (int batchSize)
specifier|public
name|AggregateDefinition
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
comment|/**      * Sets the out batch size for number of exchanges sent      *      * @param batchSize  the batch size      * @return builder      */
DECL|method|outBatchSize (int batchSize)
specifier|public
name|AggregateDefinition
name|outBatchSize
parameter_list|(
name|int
name|batchSize
parameter_list|)
block|{
name|setOutBatchSize
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the batch timeout      *      * @param batchTimeout  the timeout in millis      * @return the builder      */
DECL|method|batchTimeout (long batchTimeout)
specifier|public
name|AggregateDefinition
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
comment|/**      * Sets the aggregate collection to use      *      * @param aggregationCollection  the aggregate collection to use      * @return the builder      */
DECL|method|aggregationCollection (AggregationCollection aggregationCollection)
specifier|public
name|AggregateDefinition
name|aggregationCollection
parameter_list|(
name|AggregationCollection
name|aggregationCollection
parameter_list|)
block|{
name|setAggregationCollection
argument_list|(
name|aggregationCollection
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the aggregate strategy to use      *      * @param aggregationStrategy  the aggregate strategy to use      * @return the builder      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|AggregateDefinition
name|aggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|setAggregationStrategy
argument_list|(
name|aggregationStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the aggregate collection to use      *      * @param collectionRef  reference to the aggregate collection to lookup in the registry      * @return the builder      */
DECL|method|collectionRef (String collectionRef)
specifier|public
name|AggregateDefinition
name|collectionRef
parameter_list|(
name|String
name|collectionRef
parameter_list|)
block|{
name|setCollectionRef
argument_list|(
name|collectionRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the aggregate strategy to use      *      * @param strategyRef  reference to the strategy to lookup in the registry      * @return the builder      */
DECL|method|strategyRef (String strategyRef)
specifier|public
name|AggregateDefinition
name|strategyRef
parameter_list|(
name|String
name|strategyRef
parameter_list|)
block|{
name|setStrategyRef
argument_list|(
name|strategyRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables grouped exchanges, so the aggregator will group all aggregated exchanges into a single      * combined {@link org.apache.camel.impl.GroupedExchange} class holding all the aggregated exchanges.      *      * @return the builder      */
DECL|method|groupExchanges ()
specifier|public
name|AggregateDefinition
name|groupExchanges
parameter_list|()
block|{
name|setGroupExchanges
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the predicate used to determine if the aggregation is completed      *      * @return the clause used to create the predicate      */
DECL|method|completionPredicate ()
specifier|public
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
name|completionPredicate
parameter_list|()
block|{
name|checkNoCompletedPredicate
argument_list|()
expr_stmt|;
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
name|clause
init|=
operator|new
name|ExpressionClause
argument_list|<
name|AggregateDefinition
argument_list|>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|setCompletionPredicate
argument_list|(
operator|new
name|ExpressionSubElementDefinition
argument_list|(
operator|(
name|Expression
operator|)
name|clause
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|clause
return|;
block|}
comment|/**      * Sets the predicate used to determine if the aggregation is completed      *      * @param predicate  the predicate      */
DECL|method|completionPredicate (Predicate predicate)
specifier|public
name|AggregateDefinition
name|completionPredicate
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|checkNoCompletedPredicate
argument_list|()
expr_stmt|;
name|setCompletionPredicate
argument_list|(
operator|new
name|ExpressionSubElementDefinition
argument_list|(
name|predicate
argument_list|)
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
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"There is already a completionPredicate defined for this aggregator: "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
DECL|method|setCorrelationExpression (ExpressionSubElementDefinition correlationExpression)
specifier|public
name|void
name|setCorrelationExpression
parameter_list|(
name|ExpressionSubElementDefinition
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
DECL|method|getCorrelationExpression ()
specifier|public
name|ExpressionSubElementDefinition
name|getCorrelationExpression
parameter_list|()
block|{
return|return
name|correlationExpression
return|;
block|}
comment|// Section - Methods from ExpressionNode
comment|// Needed to copy methods from ExpressionNode here so that I could specify the
comment|// correlation expression as optional in JAXB
DECL|method|getExpression ()
specifier|public
name|ExpressionDefinition
name|getExpression
parameter_list|()
block|{
if|if
condition|(
name|expression
operator|==
literal|null
operator|&&
name|correlationExpression
operator|!=
literal|null
condition|)
block|{
name|expression
operator|=
name|correlationExpression
operator|.
name|getExpressionType
argument_list|()
expr_stmt|;
block|}
return|return
name|expression
return|;
block|}
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
block|}
end_class

end_unit

