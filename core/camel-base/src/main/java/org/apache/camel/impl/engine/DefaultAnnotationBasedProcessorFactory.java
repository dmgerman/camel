begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
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
name|AsyncProcessor
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
name|DynamicRouter
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
name|RoutingSlip
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
name|AnnotationBasedProcessorFactory
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
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|DefaultAnnotationBasedProcessorFactory
specifier|public
specifier|final
class|class
name|DefaultAnnotationBasedProcessorFactory
implements|implements
name|AnnotationBasedProcessorFactory
block|{
annotation|@
name|Override
DECL|method|createDynamicRouter (CamelContext camelContext, DynamicRouter annotation)
specifier|public
name|AsyncProcessor
name|createDynamicRouter
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DynamicRouter
name|annotation
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|DynamicRouter
name|dynamicRouter
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|DynamicRouter
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|dynamicRouter
operator|.
name|setDelimiter
argument_list|(
name|annotation
operator|.
name|delimiter
argument_list|()
argument_list|)
expr_stmt|;
name|dynamicRouter
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|annotation
operator|.
name|ignoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
name|dynamicRouter
operator|.
name|setCacheSize
argument_list|(
name|annotation
operator|.
name|cacheSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dynamicRouter
return|;
block|}
annotation|@
name|Override
DECL|method|createRecipientList (CamelContext camelContext, RecipientList annotation)
specifier|public
name|AsyncProcessor
name|createRecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RecipientList
name|annotation
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|RecipientList
name|recipientList
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|RecipientList
argument_list|(
name|camelContext
argument_list|,
name|annotation
operator|.
name|delimiter
argument_list|()
argument_list|)
decl_stmt|;
name|recipientList
operator|.
name|setStopOnException
argument_list|(
name|annotation
operator|.
name|stopOnException
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setStopOnAggregateException
argument_list|(
name|annotation
operator|.
name|stopOnAggregateException
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|annotation
operator|.
name|ignoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setParallelProcessing
argument_list|(
name|annotation
operator|.
name|parallelProcessing
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setParallelAggregate
argument_list|(
name|annotation
operator|.
name|parallelAggregate
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setStreaming
argument_list|(
name|annotation
operator|.
name|streaming
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setTimeout
argument_list|(
name|annotation
operator|.
name|timeout
argument_list|()
argument_list|)
expr_stmt|;
name|recipientList
operator|.
name|setShareUnitOfWork
argument_list|(
name|annotation
operator|.
name|shareUnitOfWork
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|annotation
operator|.
name|executorServiceRef
argument_list|()
argument_list|)
condition|)
block|{
name|ExecutorService
name|executor
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
name|annotation
operator|.
name|executorServiceRef
argument_list|()
argument_list|)
decl_stmt|;
name|recipientList
operator|.
name|setExecutorService
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|annotation
operator|.
name|parallelProcessing
argument_list|()
operator|&&
name|recipientList
operator|.
name|getExecutorService
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// we are running in parallel so we need a thread pool
name|ExecutorService
name|executor
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"@RecipientList"
argument_list|)
decl_stmt|;
name|recipientList
operator|.
name|setExecutorService
argument_list|(
name|executor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|annotation
operator|.
name|strategyRef
argument_list|()
argument_list|)
condition|)
block|{
name|AggregationStrategy
name|strategy
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|annotation
operator|.
name|strategyRef
argument_list|()
argument_list|,
name|AggregationStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
name|recipientList
operator|.
name|setAggregationStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|annotation
operator|.
name|onPrepareRef
argument_list|()
argument_list|)
condition|)
block|{
name|Processor
name|onPrepare
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|annotation
operator|.
name|onPrepareRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
decl_stmt|;
name|recipientList
operator|.
name|setOnPrepare
argument_list|(
name|onPrepare
argument_list|)
expr_stmt|;
block|}
return|return
name|recipientList
return|;
block|}
annotation|@
name|Override
DECL|method|createRoutingSlip (CamelContext camelContext, RoutingSlip annotation)
specifier|public
name|AsyncProcessor
name|createRoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|RoutingSlip
name|annotation
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|RoutingSlip
name|routingSlip
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|RoutingSlip
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|routingSlip
operator|.
name|setDelimiter
argument_list|(
name|annotation
operator|.
name|delimiter
argument_list|()
argument_list|)
expr_stmt|;
name|routingSlip
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|annotation
operator|.
name|ignoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
name|routingSlip
operator|.
name|setCacheSize
argument_list|(
name|annotation
operator|.
name|cacheSize
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|routingSlip
return|;
block|}
block|}
end_class

end_unit
