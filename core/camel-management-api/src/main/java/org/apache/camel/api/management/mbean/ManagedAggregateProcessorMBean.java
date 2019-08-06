begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedAggregateProcessorMBean
specifier|public
interface|interface
name|ManagedAggregateProcessorMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the expression"
argument_list|)
DECL|method|getCorrelationExpressionLanguage ()
name|String
name|getCorrelationExpressionLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Correlation Expression"
argument_list|)
DECL|method|getCorrelationExpression ()
name|String
name|getCorrelationExpression
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion timeout in millis"
argument_list|)
DECL|method|getCompletionTimeout ()
name|long
name|getCompletionTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the expression"
argument_list|)
DECL|method|getCompletionTimeoutLanguage ()
name|String
name|getCompletionTimeoutLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion timeout expression"
argument_list|)
DECL|method|getCompletionTimeoutExpression ()
name|String
name|getCompletionTimeoutExpression
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion interval in millis"
argument_list|)
DECL|method|getCompletionInterval ()
name|long
name|getCompletionInterval
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion timeout checker interval in millis"
argument_list|)
DECL|method|getCompletionTimeoutCheckerInterval ()
name|long
name|getCompletionTimeoutCheckerInterval
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion size"
argument_list|)
DECL|method|getCompletionSize ()
name|int
name|getCompletionSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the expression"
argument_list|)
DECL|method|getCompletionSizeExpressionLanguage ()
name|String
name|getCompletionSizeExpressionLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completion size expression"
argument_list|)
DECL|method|getCompletionSizeExpression ()
name|String
name|getCompletionSizeExpression
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Complete from batch consumers"
argument_list|)
DECL|method|isCompletionFromBatchConsumer ()
name|boolean
name|isCompletionFromBatchConsumer
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Complete all previous groups on new incoming correlation group"
argument_list|)
DECL|method|isCompletionOnNewCorrelationGroup ()
name|boolean
name|isCompletionOnNewCorrelationGroup
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Ignore invalid correlation keys"
argument_list|)
DECL|method|isIgnoreInvalidCorrelationKeys ()
name|boolean
name|isIgnoreInvalidCorrelationKeys
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to close the correlation group on completion if this value is> 0."
argument_list|)
DECL|method|getCloseCorrelationKeyOnCompletion ()
name|Integer
name|getCloseCorrelationKeyOnCompletion
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Parallel mode"
argument_list|)
DECL|method|isParallelProcessing ()
name|boolean
name|isParallelProcessing
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Optimistic locking"
argument_list|)
DECL|method|isOptimisticLocking ()
name|boolean
name|isOptimisticLocking
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not to eager check for completion when a new incoming Exchange has been received"
argument_list|)
DECL|method|isEagerCheckCompletion ()
name|boolean
name|isEagerCheckCompletion
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The language for the predicate"
argument_list|)
DECL|method|getCompletionPredicateLanguage ()
name|String
name|getCompletionPredicateLanguage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"A Predicate to indicate when an aggregated exchange is complete"
argument_list|)
DECL|method|getCompletionPredicate ()
name|String
name|getCompletionPredicate
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not exchanges which complete due to a timeout should be discarded"
argument_list|)
DECL|method|isDiscardOnCompletionTimeout ()
name|boolean
name|isDiscardOnCompletionTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Indicates to complete all current aggregated exchanges when the context is stopped"
argument_list|)
DECL|method|isForceCompletionOnStop ()
name|boolean
name|isForceCompletionOnStop
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Indicates to wait to complete all current and partial (pending) aggregated exchanges when the context is stopped"
argument_list|)
DECL|method|isCompleteAllOnStop ()
name|boolean
name|isCompleteAllOnStop
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of completed exchanges which are currently in-flight"
argument_list|)
DECL|method|getInProgressCompleteExchanges ()
name|int
name|getInProgressCompleteExchanges
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Number of groups currently in the aggregation repository"
argument_list|)
DECL|method|aggregationRepositoryGroups ()
name|int
name|aggregationRepositoryGroups
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"To force completing a specific group by its key"
argument_list|)
DECL|method|forceCompletionOfGroup (String key)
name|int
name|forceCompletionOfGroup
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"To force complete of all groups"
argument_list|)
DECL|method|forceCompletionOfAllGroups ()
name|int
name|forceCompletionOfAllGroups
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"To force discarding a specific group by its key"
argument_list|)
DECL|method|forceDiscardingOfGroup (String key)
name|int
name|forceDiscardingOfGroup
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"To force discarding of all groups"
argument_list|)
DECL|method|forceDiscardingOfAllGroups ()
name|int
name|forceDiscardingOfAllGroups
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current number of closed correlation keys in the memory cache"
argument_list|)
DECL|method|getClosedCorrelationKeysCacheSize ()
name|int
name|getClosedCorrelationKeysCacheSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear all the closed correlation keys stored in the cache"
argument_list|)
DECL|method|clearClosedCorrelationKeysCache ()
name|void
name|clearClosedCorrelationKeysCache
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanges arrived into the aggregator"
argument_list|)
DECL|method|getTotalIn ()
name|long
name|getTotalIn
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanges completed and outgoing from the aggregator"
argument_list|)
DECL|method|getTotalCompleted ()
name|long
name|getTotalCompleted
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion size trigger"
argument_list|)
DECL|method|getCompletedBySize ()
name|long
name|getCompletedBySize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion aggregation strategy trigger"
argument_list|)
DECL|method|getCompletedByStrategy ()
name|long
name|getCompletedByStrategy
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion interval (timeout) trigger"
argument_list|)
DECL|method|getCompletedByInterval ()
name|long
name|getCompletedByInterval
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion timeout trigger"
argument_list|)
DECL|method|getCompletedByTimeout ()
name|long
name|getCompletedByTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion predicate trigger"
argument_list|)
DECL|method|getCompletedByPredicate ()
name|long
name|getCompletedByPredicate
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion batch consumer trigger"
argument_list|)
DECL|method|getCompletedByBatchConsumer ()
name|long
name|getCompletedByBatchConsumer
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of exchanged completed by completion force trigger"
argument_list|)
DECL|method|getCompletedByForce ()
name|long
name|getCompletedByForce
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|" Reset the statistics counters"
argument_list|)
DECL|method|resetStatistics ()
name|void
name|resetStatistics
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Sets whether statistics is enabled"
argument_list|)
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Sets whether statistics is enabled"
argument_list|)
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

