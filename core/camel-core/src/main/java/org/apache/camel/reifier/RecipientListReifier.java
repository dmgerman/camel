begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|model
operator|.
name|RecipientListDefinition
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
name|EvaluateExpressionProcessor
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
name|Pipeline
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
name|RecipientList
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
name|processor
operator|.
name|aggregate
operator|.
name|ShareUnitOfWorkAggregationStrategy
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|CamelContextHelper
import|;
end_import

begin_class
DECL|class|RecipientListReifier
class|class
name|RecipientListReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|RecipientListDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|method|RecipientListReifier (ProcessorDefinition<?> definition)
name|RecipientListReifier
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
operator|(
name|RecipientListDefinition
argument_list|<
name|?
argument_list|>
operator|)
name|definition
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
specifier|final
name|Expression
name|expression
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
name|boolean
name|isParallelProcessing
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
name|isStreaming
init|=
name|definition
operator|.
name|getStreaming
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getStreaming
argument_list|()
decl_stmt|;
name|boolean
name|isParallelAggregate
init|=
name|definition
operator|.
name|getParallelAggregate
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getParallelAggregate
argument_list|()
decl_stmt|;
name|boolean
name|isShareUnitOfWork
init|=
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
decl_stmt|;
name|boolean
name|isStopOnException
init|=
name|definition
operator|.
name|getStopOnException
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getStopOnException
argument_list|()
decl_stmt|;
name|boolean
name|isIgnoreInvalidEndpoints
init|=
name|definition
operator|.
name|getIgnoreInvalidEndpoints
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getIgnoreInvalidEndpoints
argument_list|()
decl_stmt|;
name|boolean
name|isStopOnAggregateException
init|=
name|definition
operator|.
name|getStopOnAggregateException
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getStopOnAggregateException
argument_list|()
decl_stmt|;
name|RecipientList
name|answer
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getDelimiter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
operator|new
name|RecipientList
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|expression
argument_list|,
name|definition
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|RecipientList
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setAggregationStrategy
argument_list|(
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setParallelProcessing
argument_list|(
name|isParallelProcessing
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setParallelAggregate
argument_list|(
name|isParallelAggregate
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setStreaming
argument_list|(
name|isStreaming
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setShareUnitOfWork
argument_list|(
name|isShareUnitOfWork
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setStopOnException
argument_list|(
name|isStopOnException
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|isIgnoreInvalidEndpoints
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setStopOnAggregateException
argument_list|(
name|isStopOnAggregateException
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getCacheSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCacheSize
argument_list|(
name|definition
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getOnPrepareRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setOnPrepare
argument_list|(
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|definition
operator|.
name|getOnPrepareRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getOnPrepare
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOnPrepare
argument_list|(
name|definition
operator|.
name|getOnPrepare
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getTimeout
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setTimeout
argument_list|(
name|definition
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|isParallelProcessing
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
literal|"RecipientList"
argument_list|,
name|definition
argument_list|,
name|isParallelProcessing
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExecutorService
argument_list|(
name|threadPool
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setShutdownExecutorService
argument_list|(
name|shutdownThreadPool
argument_list|)
expr_stmt|;
name|long
name|timeout
init|=
name|definition
operator|.
name|getTimeout
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getTimeout
argument_list|()
else|:
literal|0
decl_stmt|;
if|if
condition|(
name|timeout
operator|>
literal|0
operator|&&
operator|!
name|isParallelProcessing
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Timeout is used but ParallelProcessing has not been enabled."
argument_list|)
throw|;
block|}
comment|// create a pipeline with two processors
comment|// the first is the eval processor which evaluates the expression to use
comment|// the second is the recipient list
name|List
argument_list|<
name|Processor
argument_list|>
name|pipe
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
comment|// the eval processor must be wrapped in error handler, so in case there was an
comment|// error during evaluation, the error handler can deal with it
comment|// the recipient list is not in error handler, as its has its own special error handling
comment|// when sending to the recipients individually
name|Processor
name|evalProcessor
init|=
operator|new
name|EvaluateExpressionProcessor
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|evalProcessor
operator|=
name|super
operator|.
name|wrapInErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|evalProcessor
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|add
argument_list|(
name|evalProcessor
argument_list|)
expr_stmt|;
name|pipe
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
comment|// wrap in nested pipeline so this appears as one processor
comment|// (threads definition does this as well)
return|return
operator|new
name|Pipeline
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|pipe
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
literal|"RecipientList["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
block|}
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
name|getStrategyMethodName
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
comment|// default to use latest aggregation strategy
name|strategy
operator|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getShareUnitOfWork
argument_list|()
condition|)
block|{
comment|// wrap strategy in share unit of work
name|strategy
operator|=
operator|new
name|ShareUnitOfWorkAggregationStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
return|return
name|strategy
return|;
block|}
block|}
end_class

end_unit
