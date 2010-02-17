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
name|UnitOfWorkProcessor
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
name|AggregateProcessor
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
name|GroupedExchangeAggregationStrategy
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
literal|true
argument_list|)
DECL|field|correlationExpression
specifier|private
name|ExpressionSubElementDefinition
name|correlationExpression
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
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|parallelProcessing
specifier|private
name|Boolean
name|parallelProcessing
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
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
DECL|field|completionSize
specifier|private
name|Integer
name|completionSize
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|completionTimeout
specifier|private
name|Long
name|completionTimeout
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|completionFromBatchConsumer
specifier|private
name|Boolean
name|completionFromBatchConsumer
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
DECL|field|eagerCheckCompletion
specifier|private
name|Boolean
name|eagerCheckCompletion
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|ignoreBadCorrelationKeys
specifier|private
name|Boolean
name|ignoreBadCorrelationKeys
decl_stmt|;
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|false
argument_list|)
DECL|field|closeCorrelationKeyOnCompletion
specifier|private
name|Boolean
name|closeCorrelationKeyOnCompletion
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
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
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
name|AggregateProcessor
name|createAggregator
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
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
comment|// wrap the aggregated route in a unit of work processor
name|processor
operator|=
operator|new
name|UnitOfWorkProcessor
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|Expression
name|correlation
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|AggregationStrategy
name|strategy
init|=
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|AggregateProcessor
name|answer
init|=
operator|new
name|AggregateProcessor
argument_list|(
name|processor
argument_list|,
name|correlation
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|createExecutorService
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExecutorService
argument_list|(
name|executor
argument_list|)
expr_stmt|;
if|if
condition|(
name|isParallelProcessing
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setParallelProcessing
argument_list|(
name|isParallelProcessing
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Predicate
name|predicate
init|=
name|getCompletionPredicate
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCompletionPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCompletionSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionSize
argument_list|(
name|getCompletionSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getCompletionTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionTimeout
argument_list|(
name|getCompletionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCompletionFromBatchConsumer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionFromBatchConsumer
argument_list|(
name|isCompletionFromBatchConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isCloseCorrelationKeyOnCompletion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCloseCorrelationKeyOnCompletion
argument_list|(
name|isCloseCorrelationKeyOnCompletion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isEagerCheckCompletion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setEagerCheckCompletion
argument_list|(
name|isEagerCheckCompletion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isIgnoreBadCorrelationKeys
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setIgnoreBadCorrelationKeys
argument_list|(
name|isIgnoreBadCorrelationKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
operator|&&
name|groupExchanges
operator|!=
literal|null
operator|&&
name|groupExchanges
condition|)
block|{
comment|// if grouped exchange is enabled then use special strategy for that
name|strategy
operator|=
operator|new
name|GroupedExchangeAggregationStrategy
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"AggregationStrategy or AggregationStrategyRef must be set on "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|strategy
return|;
block|}
DECL|method|createExecutorService (RouteContext routeContext)
specifier|private
name|ExecutorService
name|createExecutorService
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
operator|&&
name|executorServiceRef
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|executorService
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
DECL|method|getAggregationStrategyRef ()
specifier|public
name|String
name|getAggregationStrategyRef
parameter_list|()
block|{
return|return
name|strategyRef
return|;
block|}
DECL|method|setAggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|void
name|setAggregationStrategyRef
parameter_list|(
name|String
name|aggregationStrategyRef
parameter_list|)
block|{
name|this
operator|.
name|strategyRef
operator|=
name|aggregationStrategyRef
expr_stmt|;
block|}
DECL|method|getCompletionSize ()
specifier|public
name|Integer
name|getCompletionSize
parameter_list|()
block|{
return|return
name|completionSize
return|;
block|}
DECL|method|setCompletionSize (Integer completionSize)
specifier|public
name|void
name|setCompletionSize
parameter_list|(
name|Integer
name|completionSize
parameter_list|)
block|{
name|this
operator|.
name|completionSize
operator|=
name|completionSize
expr_stmt|;
block|}
DECL|method|getCompletionTimeout ()
specifier|public
name|Long
name|getCompletionTimeout
parameter_list|()
block|{
return|return
name|completionTimeout
return|;
block|}
DECL|method|setCompletionTimeout (Long completionTimeout)
specifier|public
name|void
name|setCompletionTimeout
parameter_list|(
name|Long
name|completionTimeout
parameter_list|)
block|{
name|this
operator|.
name|completionTimeout
operator|=
name|completionTimeout
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
DECL|method|isGroupExchanges ()
specifier|public
name|Boolean
name|isGroupExchanges
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
DECL|method|isCompletionFromBatchConsumer ()
specifier|public
name|Boolean
name|isCompletionFromBatchConsumer
parameter_list|()
block|{
return|return
name|completionFromBatchConsumer
return|;
block|}
DECL|method|setCompletionFromBatchConsumer (Boolean completionFromBatchConsumer)
specifier|public
name|void
name|setCompletionFromBatchConsumer
parameter_list|(
name|Boolean
name|completionFromBatchConsumer
parameter_list|)
block|{
name|this
operator|.
name|completionFromBatchConsumer
operator|=
name|completionFromBatchConsumer
expr_stmt|;
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
DECL|method|isParallelProcessing ()
specifier|public
name|Boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
return|;
block|}
DECL|method|setParallelProcessing (Boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|Boolean
name|parallelProcessing
parameter_list|)
block|{
name|this
operator|.
name|parallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
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
DECL|method|isEagerCheckCompletion ()
specifier|public
name|Boolean
name|isEagerCheckCompletion
parameter_list|()
block|{
return|return
name|eagerCheckCompletion
return|;
block|}
DECL|method|setEagerCheckCompletion (Boolean eagerCheckCompletion)
specifier|public
name|void
name|setEagerCheckCompletion
parameter_list|(
name|Boolean
name|eagerCheckCompletion
parameter_list|)
block|{
name|this
operator|.
name|eagerCheckCompletion
operator|=
name|eagerCheckCompletion
expr_stmt|;
block|}
DECL|method|isIgnoreBadCorrelationKeys ()
specifier|public
name|Boolean
name|isIgnoreBadCorrelationKeys
parameter_list|()
block|{
return|return
name|ignoreBadCorrelationKeys
return|;
block|}
DECL|method|setIgnoreBadCorrelationKeys (Boolean ignoreBadCorrelationKeys)
specifier|public
name|void
name|setIgnoreBadCorrelationKeys
parameter_list|(
name|Boolean
name|ignoreBadCorrelationKeys
parameter_list|)
block|{
name|this
operator|.
name|ignoreBadCorrelationKeys
operator|=
name|ignoreBadCorrelationKeys
expr_stmt|;
block|}
DECL|method|isCloseCorrelationKeyOnCompletion ()
specifier|public
name|Boolean
name|isCloseCorrelationKeyOnCompletion
parameter_list|()
block|{
return|return
name|closeCorrelationKeyOnCompletion
return|;
block|}
DECL|method|setCloseCorrelationKeyOnCompletion (Boolean closeCorrelationKeyOnCompletion)
specifier|public
name|void
name|setCloseCorrelationKeyOnCompletion
parameter_list|(
name|Boolean
name|closeCorrelationKeyOnCompletion
parameter_list|)
block|{
name|this
operator|.
name|closeCorrelationKeyOnCompletion
operator|=
name|closeCorrelationKeyOnCompletion
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * Use eager completion checking which means that the {{completionPredicate}} will use the incoming Exchange.      * At opposed to without eager completion checking the {{completionPredicate}} will use the aggregated Exchange.      *      * @return builder      */
DECL|method|eagerCheckCompletion ()
specifier|public
name|AggregateDefinition
name|eagerCheckCompletion
parameter_list|()
block|{
name|setEagerCheckCompletion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If a correlation key cannot be successfully evaluated it will be ignored by logging a {{DEBUG}} and then just      * ignore the incoming Exchange.      *      * @return builder      */
DECL|method|ignoreBadCorrelationKeys ()
specifier|public
name|AggregateDefinition
name|ignoreBadCorrelationKeys
parameter_list|()
block|{
name|setIgnoreBadCorrelationKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Closes a correlation key when its complete. Any<i>late</i> received exchanges which has a correlation key      * that has been closed, it will be defined and a {@link org.apache.camel.processor.aggregate.ClosedCorrelationKeyException}      * is thrown.      *      * @return builder      */
DECL|method|closeCorrelationKeyOnCompletion ()
specifier|public
name|AggregateDefinition
name|closeCorrelationKeyOnCompletion
parameter_list|()
block|{
name|setCloseCorrelationKeyOnCompletion
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables the batch completion mode where we aggregate from a {@link org.apache.camel.BatchConsumer}      * and aggregate the total number of exchanges the {@link org.apache.camel.BatchConsumer} has reported      * as total by checking the exchange property {@link org.apache.camel.Exchange#BATCH_COMPLETE} when its complete.      *      * @return builder      */
DECL|method|completionFromBatchConsumer ()
specifier|public
name|AggregateDefinition
name|completionFromBatchConsumer
parameter_list|()
block|{
name|setCompletionFromBatchConsumer
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the completion size, which is the number of aggregated exchanges which would      * cause the aggregate to consider the group as complete and send out the aggregated exchange.      *      * @param completionSize  the completion size      * @return builder      */
DECL|method|completionSize (int completionSize)
specifier|public
name|AggregateDefinition
name|completionSize
parameter_list|(
name|int
name|completionSize
parameter_list|)
block|{
name|setCompletionSize
argument_list|(
name|completionSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the completion timeout, which would cause the aggregate to consider the group as complete      * and send out the aggregated exchange.      *      * @param completionTimeout  the timeout in millis      * @return the builder      */
DECL|method|completionTimeout (long completionTimeout)
specifier|public
name|AggregateDefinition
name|completionTimeout
parameter_list|(
name|long
name|completionTimeout
parameter_list|)
block|{
name|setCompletionTimeout
argument_list|(
name|completionTimeout
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
comment|/**      * Sets the aggregate strategy to use      *      * @param aggregationStrategyRef  reference to the strategy to lookup in the registry      * @return the builder      */
DECL|method|aggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|AggregateDefinition
name|aggregationStrategyRef
parameter_list|(
name|String
name|aggregationStrategyRef
parameter_list|)
block|{
name|setAggregationStrategyRef
argument_list|(
name|aggregationStrategyRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Enables grouped exchanges, so the aggregator will group all aggregated exchanges into a single      * combined Exchange holding all the aggregated exchanges in a {@link java.util.List} as a exchange      * property with the key {@link org.apache.camel.Exchange#GROUPED_EXCHANGE}.      *      * @return the builder      */
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
comment|/**      * Sets the predicate used to determine if the aggregation is completed      *      * @param predicate  the predicate      * @return the builder      */
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
comment|/**      * Sending the aggregated output in parallel      *      * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|AggregateDefinition
name|parallelProcessing
parameter_list|()
block|{
name|setParallelProcessing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Setting the executor service for executing the sending the aggregated output.      *      * @param executorService the executor service      * @return the builder      */
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|AggregateDefinition
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Setting the executor service for executing the sending the aggregated output.      *      * @param executorServiceRef reference to the executor service      * @return the builder      */
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|AggregateDefinition
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
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

