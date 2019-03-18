begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|CamelContextAware
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
name|model
operator|.
name|ProcessorDefinition
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
name|ProcessorDefinitionHelper
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
name|CamelInternalProcessor
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
name|AggregateController
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
name|AggregationStrategyBeanAdapter
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
name|AggregationRepository
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
name|concurrent
operator|.
name|SynchronousExecutorService
import|;
end_import

begin_class
DECL|class|AggregateReifier
class|class
name|AggregateReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|AggregateDefinition
argument_list|>
block|{
DECL|method|AggregateReifier (ProcessorDefinition<?> definition)
name|AggregateReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|AggregateDefinition
operator|.
name|class
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
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
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// wrap the aggregate route in a unit of work processor
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|childProcessor
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|Expression
name|correlation
init|=
name|definition
operator|.
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
name|boolean
name|parallel
init|=
name|definition
operator|.
name|getParallelProcessing
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getParallelProcessing
argument_list|()
decl_stmt|;
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|definition
argument_list|,
name|parallel
argument_list|)
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"Aggregator"
argument_list|,
name|definition
argument_list|,
name|parallel
argument_list|)
decl_stmt|;
if|if
condition|(
name|threadPool
operator|==
literal|null
operator|&&
operator|!
name|parallel
condition|)
block|{
comment|// executor service is mandatory for the Aggregator
comment|// we do not run in parallel mode, but use a synchronous executor, so we run in current thread
name|threadPool
operator|=
operator|new
name|SynchronousExecutorService
argument_list|()
expr_stmt|;
name|shutdownThreadPool
operator|=
literal|true
expr_stmt|;
block|}
name|AggregateProcessor
name|answer
init|=
operator|new
name|AggregateProcessor
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|internal
argument_list|,
name|correlation
argument_list|,
name|strategy
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|)
decl_stmt|;
name|AggregationRepository
name|repository
init|=
name|createAggregationRepository
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|repository
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAggregationRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAggregateController
argument_list|()
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getAggregateControllerRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setAggregateController
argument_list|(
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|definition
operator|.
name|getAggregateControllerRef
argument_list|()
argument_list|,
name|AggregateController
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// this EIP supports using a shared timeout checker thread pool or fallback to create a new thread pool
name|boolean
name|shutdownTimeoutThreadPool
init|=
literal|false
decl_stmt|;
name|ScheduledExecutorService
name|timeoutThreadPool
init|=
name|definition
operator|.
name|getTimeoutCheckerExecutorService
argument_list|()
decl_stmt|;
if|if
condition|(
name|timeoutThreadPool
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getTimeoutCheckerExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup existing thread pool
name|timeoutThreadPool
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|definition
operator|.
name|getTimeoutCheckerExecutorServiceRef
argument_list|()
argument_list|,
name|ScheduledExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeoutThreadPool
operator|==
literal|null
condition|)
block|{
comment|// then create a thread pool assuming the ref is a thread pool profile id
name|timeoutThreadPool
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
name|AggregateProcessor
operator|.
name|AGGREGATE_TIMEOUT_CHECKER
argument_list|,
name|definition
operator|.
name|getTimeoutCheckerExecutorServiceRef
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeoutThreadPool
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|definition
operator|.
name|getTimeoutCheckerExecutorServiceRef
argument_list|()
operator|+
literal|" not found in registry (as an ScheduledExecutorService instance) or as a thread pool profile."
argument_list|)
throw|;
block|}
name|shutdownTimeoutThreadPool
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|setTimeoutCheckerExecutorService
argument_list|(
name|timeoutThreadPool
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setShutdownTimeoutCheckerExecutorService
argument_list|(
name|shutdownTimeoutThreadPool
argument_list|)
expr_stmt|;
comment|// set other options
name|answer
operator|.
name|setParallelProcessing
argument_list|(
name|parallel
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getOptimisticLocking
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOptimisticLocking
argument_list|(
name|definition
operator|.
name|getOptimisticLocking
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Predicate
name|predicate
init|=
name|definition
operator|.
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
elseif|else
if|if
condition|(
name|strategy
operator|instanceof
name|Predicate
condition|)
block|{
comment|// if aggregation strategy implements predicate and was not configured then use as fallback
name|log
operator|.
name|debug
argument_list|(
literal|"Using AggregationStrategy as completion predicate: {}"
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setCompletionPredicate
argument_list|(
operator|(
name|Predicate
operator|)
name|strategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionTimeoutExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Expression
name|expression
init|=
name|definition
operator|.
name|getCompletionTimeoutExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCompletionTimeoutExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
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
name|definition
operator|.
name|getCompletionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionInterval
argument_list|(
name|definition
operator|.
name|getCompletionInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionSizeExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Expression
name|expression
init|=
name|definition
operator|.
name|getCompletionSizeExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCompletionSizeExpression
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
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
name|definition
operator|.
name|getCompletionSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionFromBatchConsumer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionFromBatchConsumer
argument_list|(
name|definition
operator|.
name|getCompletionFromBatchConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionOnNewCorrelationGroup
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionOnNewCorrelationGroup
argument_list|(
name|definition
operator|.
name|getCompletionOnNewCorrelationGroup
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getEagerCheckCompletion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setEagerCheckCompletion
argument_list|(
name|definition
operator|.
name|getEagerCheckCompletion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getIgnoreInvalidCorrelationKeys
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setIgnoreInvalidCorrelationKeys
argument_list|(
name|definition
operator|.
name|getIgnoreInvalidCorrelationKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCloseCorrelationKeyOnCompletion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCloseCorrelationKeyOnCompletion
argument_list|(
name|definition
operator|.
name|getCloseCorrelationKeyOnCompletion
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getDiscardOnCompletionTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setDiscardOnCompletionTimeout
argument_list|(
name|definition
operator|.
name|getDiscardOnCompletionTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getForceCompletionOnStop
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setForceCompletionOnStop
argument_list|(
name|definition
operator|.
name|getForceCompletionOnStop
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompleteAllOnStop
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompleteAllOnStop
argument_list|(
name|definition
operator|.
name|getCompleteAllOnStop
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getOptimisticLockRetryPolicy
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|definition
operator|.
name|getOptimisticLockRetryPolicyDefinition
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOptimisticLockRetryPolicy
argument_list|(
name|definition
operator|.
name|getOptimisticLockRetryPolicyDefinition
argument_list|()
operator|.
name|createOptimisticLockRetryPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|answer
operator|.
name|setOptimisticLockRetryPolicy
argument_list|(
name|definition
operator|.
name|getOptimisticLockRetryPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAggregateController
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAggregateController
argument_list|(
name|definition
operator|.
name|getAggregateController
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCompletionTimeoutCheckerInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCompletionTimeoutCheckerInterval
argument_list|(
name|definition
operator|.
name|getCompletionTimeoutCheckerInterval
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
name|definition
operator|.
name|getAggregationStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getStrategyRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Object
name|aggStrategy
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|definition
operator|.
name|getStrategyRef
argument_list|()
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|aggStrategy
operator|instanceof
name|AggregationStrategy
condition|)
block|{
name|strategy
operator|=
operator|(
name|AggregationStrategy
operator|)
name|aggStrategy
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|aggStrategy
operator|!=
literal|null
condition|)
block|{
name|AggregationStrategyBeanAdapter
name|adapter
init|=
operator|new
name|AggregationStrategyBeanAdapter
argument_list|(
name|aggStrategy
argument_list|,
name|definition
operator|.
name|getAggregationStrategyMethodName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getStrategyMethodAllowNull
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|adapter
operator|.
name|setAllowNullNewExchange
argument_list|(
name|definition
operator|.
name|getStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setAllowNullOldExchange
argument_list|(
name|definition
operator|.
name|getStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|strategy
operator|=
name|adapter
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find AggregationStrategy in Registry with name: "
operator|+
name|definition
operator|.
name|getStrategyRef
argument_list|()
argument_list|)
throw|;
block|}
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
if|if
condition|(
name|strategy
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|strategy
operator|)
operator|.
name|setCamelContext
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
DECL|method|createAggregationRepository (RouteContext routeContext)
specifier|private
name|AggregationRepository
name|createAggregationRepository
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|AggregationRepository
name|repository
init|=
name|definition
operator|.
name|getAggregationRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|repository
operator|==
literal|null
operator|&&
name|definition
operator|.
name|getAggregationRepositoryRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|repository
operator|=
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|definition
operator|.
name|getAggregationRepositoryRef
argument_list|()
argument_list|,
name|AggregationRepository
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|repository
return|;
block|}
block|}
end_class

end_unit

