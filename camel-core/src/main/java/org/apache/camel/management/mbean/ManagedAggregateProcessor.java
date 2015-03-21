begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedAggregateProcessorMBean
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
name|AggregateDefinition
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
name|spi
operator|.
name|ManagementStrategy
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed AggregateProcessor"
argument_list|)
DECL|class|ManagedAggregateProcessor
specifier|public
class|class
name|ManagedAggregateProcessor
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedAggregateProcessorMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|AggregateProcessor
name|processor
decl_stmt|;
DECL|method|ManagedAggregateProcessor (CamelContext context, AggregateProcessor processor, AggregateDefinition definition)
specifier|public
name|ManagedAggregateProcessor
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|AggregateProcessor
name|processor
parameter_list|,
name|AggregateDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
name|super
operator|.
name|init
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
DECL|method|getProcessor ()
specifier|public
name|AggregateProcessor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|getCorrelationExpression ()
specifier|public
name|String
name|getCorrelationExpression
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getCorrelationExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getCorrelationExpression
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getCompletionTimeout ()
specifier|public
name|long
name|getCompletionTimeout
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCompletionTimeout
argument_list|()
return|;
block|}
DECL|method|getCompletionTimeoutExpression ()
specifier|public
name|String
name|getCompletionTimeoutExpression
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getCompletionTimeoutExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getCompletionTimeoutExpression
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getCompletionInterval ()
specifier|public
name|long
name|getCompletionInterval
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCompletionInterval
argument_list|()
return|;
block|}
DECL|method|getCompletionSize ()
specifier|public
name|int
name|getCompletionSize
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCompletionSize
argument_list|()
return|;
block|}
DECL|method|getCompletionSizeExpression ()
specifier|public
name|String
name|getCompletionSizeExpression
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getCompletionSizeExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getCompletionSizeExpression
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|isCompletionFromBatchConsumer ()
specifier|public
name|boolean
name|isCompletionFromBatchConsumer
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isCompletionFromBatchConsumer
argument_list|()
return|;
block|}
DECL|method|isIgnoreInvalidCorrelationKeys ()
specifier|public
name|boolean
name|isIgnoreInvalidCorrelationKeys
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isIgnoreInvalidCorrelationKeys
argument_list|()
return|;
block|}
DECL|method|getCloseCorrelationKeyOnCompletion ()
specifier|public
name|Integer
name|getCloseCorrelationKeyOnCompletion
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getCloseCorrelationKeyOnCompletion
argument_list|()
return|;
block|}
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isParallelProcessing
argument_list|()
return|;
block|}
DECL|method|isOptimisticLocking ()
specifier|public
name|boolean
name|isOptimisticLocking
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isOptimisticLocking
argument_list|()
return|;
block|}
DECL|method|isEagerCheckCompletion ()
specifier|public
name|boolean
name|isEagerCheckCompletion
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isEagerCheckCompletion
argument_list|()
return|;
block|}
DECL|method|getCompletionPredicate ()
specifier|public
name|String
name|getCompletionPredicate
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getCompletionPredicate
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|isDiscardOnCompletionTimeout ()
specifier|public
name|boolean
name|isDiscardOnCompletionTimeout
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isDiscardOnCompletionTimeout
argument_list|()
return|;
block|}
DECL|method|isForceCompletionOnStop ()
specifier|public
name|boolean
name|isForceCompletionOnStop
parameter_list|()
block|{
return|return
name|processor
operator|.
name|isCompletionFromBatchConsumer
argument_list|()
return|;
block|}
DECL|method|getInProgressCompleteExchanges ()
specifier|public
name|int
name|getInProgressCompleteExchanges
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getInProgressCompleteExchanges
argument_list|()
return|;
block|}
DECL|method|aggregationRepositoryGroups ()
specifier|public
name|int
name|aggregationRepositoryGroups
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|processor
operator|.
name|getAggregationRepository
argument_list|()
operator|.
name|getKeys
argument_list|()
decl_stmt|;
if|if
condition|(
name|keys
operator|!=
literal|null
condition|)
block|{
return|return
name|keys
operator|.
name|size
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|forceCompletionOfGroup (String key)
specifier|public
name|int
name|forceCompletionOfGroup
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|.
name|getAggregateController
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getAggregateController
argument_list|()
operator|.
name|forceCompletionOfGroup
argument_list|(
name|key
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|forceCompletionOfAllGroups ()
specifier|public
name|int
name|forceCompletionOfAllGroups
parameter_list|()
block|{
if|if
condition|(
name|processor
operator|.
name|getAggregateController
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|processor
operator|.
name|getAggregateController
argument_list|()
operator|.
name|forceCompletionOfAllGroups
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
DECL|method|getTotalIn ()
specifier|public
name|long
name|getTotalIn
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getTotalIn
argument_list|()
return|;
block|}
DECL|method|getTotalCompleted ()
specifier|public
name|long
name|getTotalCompleted
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getTotalCompleted
argument_list|()
return|;
block|}
DECL|method|getCompletedBySize ()
specifier|public
name|long
name|getCompletedBySize
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedBySize
argument_list|()
return|;
block|}
DECL|method|getCompletedByStrategy ()
specifier|public
name|long
name|getCompletedByStrategy
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByStrategy
argument_list|()
return|;
block|}
DECL|method|getCompletedByInterval ()
specifier|public
name|long
name|getCompletedByInterval
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByInterval
argument_list|()
return|;
block|}
DECL|method|getCompletedByTimeout ()
specifier|public
name|long
name|getCompletedByTimeout
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByTimeout
argument_list|()
return|;
block|}
DECL|method|getCompletedByPredicate ()
specifier|public
name|long
name|getCompletedByPredicate
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByPredicate
argument_list|()
return|;
block|}
DECL|method|getCompletedByBatchConsumer ()
specifier|public
name|long
name|getCompletedByBatchConsumer
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByBatchConsumer
argument_list|()
return|;
block|}
DECL|method|getCompletedByForce ()
specifier|public
name|long
name|getCompletedByForce
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|getCompletedByForce
argument_list|()
return|;
block|}
DECL|method|resetStatistics ()
specifier|public
name|void
name|resetStatistics
parameter_list|()
block|{
name|processor
operator|.
name|getStatistics
argument_list|()
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

