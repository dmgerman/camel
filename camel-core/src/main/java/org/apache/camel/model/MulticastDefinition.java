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
name|MulticastProcessor
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
name|Label
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
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  *  Routes the same message to multiple paths either sequentially or in parallel.  *  * @version   */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"EIP,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"multicast"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|MulticastDefinition
specifier|public
class|class
name|MulticastDefinition
extends|extends
name|OutputDefinition
argument_list|<
name|MulticastDefinition
argument_list|>
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|MulticastDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|parallelProcessing
specifier|private
name|Boolean
name|parallelProcessing
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|strategyRef
specifier|private
name|String
name|strategyRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|strategyMethodName
specifier|private
name|String
name|strategyMethodName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|strategyMethodAllowNull
specifier|private
name|Boolean
name|strategyMethodAllowNull
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
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|streaming
specifier|private
name|Boolean
name|streaming
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|stopOnException
specifier|private
name|Boolean
name|stopOnException
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|onPrepareRef
specifier|private
name|String
name|onPrepareRef
decl_stmt|;
annotation|@
name|XmlTransient
DECL|field|onPrepare
specifier|private
name|Processor
name|onPrepare
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|shareUnitOfWork
specifier|private
name|Boolean
name|shareUnitOfWork
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|parallelAggregate
specifier|private
name|Boolean
name|parallelAggregate
decl_stmt|;
DECL|method|MulticastDefinition ()
specifier|public
name|MulticastDefinition
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Multicast["
operator|+
name|getOutputs
argument_list|()
operator|+
literal|"]"
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
literal|"multicast"
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
name|Processor
name|answer
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
comment|// force the answer as a multicast processor even if there is only one child processor in the multicast
if|if
condition|(
operator|!
operator|(
name|answer
operator|instanceof
name|MulticastProcessor
operator|)
condition|)
block|{
name|List
argument_list|<
name|Processor
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|createCompositeProcessor
argument_list|(
name|routeContext
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Set the multicasting aggregationStrategy      *      * @return the builder      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|MulticastDefinition
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
comment|/**      * Set the aggregationStrategy      *      * @param aggregationStrategyRef a reference to a strategy to lookup      * @return the builder      */
DECL|method|aggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|MulticastDefinition
name|aggregationStrategyRef
parameter_list|(
name|String
name|aggregationStrategyRef
parameter_list|)
block|{
name|setStrategyRef
argument_list|(
name|aggregationStrategyRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the method name to use when using a POJO as {@link AggregationStrategy}.      *      * @param  methodName the method name to call      * @return the builder      */
DECL|method|aggregationStrategyMethodName (String methodName)
specifier|public
name|MulticastDefinition
name|aggregationStrategyMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|setStrategyMethodName
argument_list|(
name|methodName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets allowing null when using a POJO as {@link AggregationStrategy}.      *      * @return the builder      */
DECL|method|aggregationStrategyMethodAllowNull ()
specifier|public
name|MulticastDefinition
name|aggregationStrategyMethodAllowNull
parameter_list|()
block|{
name|setStrategyMethodAllowNull
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the {@link java.util.concurrent.ExecutorService} to do the multicasting work      *           * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|MulticastDefinition
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
comment|/**      * Doing the aggregate work in parallel      *<p/>      * Notice that if enabled, then the {@link org.apache.camel.processor.aggregate.AggregationStrategy} in use      * must be implemented as thread safe, as concurrent threads can call the<tt>aggregate</tt> methods at the same time.      *      * @return the builder      */
DECL|method|parallelAggregate ()
specifier|public
name|MulticastDefinition
name|parallelAggregate
parameter_list|()
block|{
name|setParallelAggregate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Aggregates the responses as the are done (e.g. out of order sequence)      *      * @return the builder      */
DECL|method|streaming ()
specifier|public
name|MulticastDefinition
name|streaming
parameter_list|()
block|{
name|setStreaming
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will now stop further processing if an exception or failure occurred during processing of an      * {@link org.apache.camel.Exchange} and the caused exception will be thrown.      *<p/>      * Will also stop if processing the exchange failed (has a fault message) or an exception      * was thrown and handled by the error handler (such as using onException). In all situations      * the multicast will stop further processing. This is the same behavior as in pipeline, which      * is used by the routing engine.      *<p/>      * The default behavior is to<b>not</b> stop but continue processing till the end      *      * @return the builder      */
DECL|method|stopOnException ()
specifier|public
name|MulticastDefinition
name|stopOnException
parameter_list|()
block|{
name|setStopOnException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|MulticastDefinition
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
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|MulticastDefinition
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
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      *      * @param onPrepare the processor      * @return the builder      */
DECL|method|onPrepare (Processor onPrepare)
specifier|public
name|MulticastDefinition
name|onPrepare
parameter_list|(
name|Processor
name|onPrepare
parameter_list|)
block|{
name|setOnPrepare
argument_list|(
name|onPrepare
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      *      * @param onPrepareRef reference to the processor to lookup in the {@link org.apache.camel.spi.Registry}      * @return the builder      */
DECL|method|onPrepareRef (String onPrepareRef)
specifier|public
name|MulticastDefinition
name|onPrepareRef
parameter_list|(
name|String
name|onPrepareRef
parameter_list|)
block|{
name|setOnPrepareRef
argument_list|(
name|onPrepareRef
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a timeout value in millis to use when using parallelProcessing.      *      * @param timeout timeout in millis      * @return the builder      */
DECL|method|timeout (long timeout)
specifier|public
name|MulticastDefinition
name|timeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Shares the {@link org.apache.camel.spi.UnitOfWork} with the parent and each of the sub messages.      *      * @return the builder.      * @see org.apache.camel.spi.SubUnitOfWork      */
DECL|method|shareUnitOfWork ()
specifier|public
name|MulticastDefinition
name|shareUnitOfWork
parameter_list|()
block|{
name|setShareUnitOfWork
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|createCompositeProcessor (RouteContext routeContext, List<Processor> list)
specifier|protected
name|Processor
name|createCompositeProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|List
argument_list|<
name|Processor
argument_list|>
name|list
parameter_list|)
throws|throws
name|Exception
block|{
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
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|this
argument_list|,
name|isParallelProcessing
argument_list|()
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
literal|"Multicast"
argument_list|,
name|this
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|timeout
init|=
name|getTimeout
argument_list|()
operator|!=
literal|null
condition|?
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
argument_list|()
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
if|if
condition|(
name|onPrepareRef
operator|!=
literal|null
condition|)
block|{
name|onPrepare
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|onPrepareRef
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|MulticastProcessor
name|answer
init|=
operator|new
name|MulticastProcessor
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|list
argument_list|,
name|strategy
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|,
name|isStreaming
argument_list|()
argument_list|,
name|isStopOnException
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|isShareUnitOfWork
argument_list|()
argument_list|,
name|isParallelAggregate
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|isShareUnitOfWork
argument_list|()
condition|)
block|{
comment|// wrap answer in a sub unit of work, since we share the unit of work
name|CamelInternalProcessor
name|internalProcessor
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|answer
argument_list|)
decl_stmt|;
name|internalProcessor
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|SubUnitOfWorkProcessorAdvice
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|internalProcessor
return|;
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
name|Object
name|aggStrategy
init|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|strategyRef
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
name|getStrategyMethodName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
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
name|getStrategyMethodAllowNull
argument_list|()
argument_list|)
expr_stmt|;
name|adapter
operator|.
name|setAllowNullOldExchange
argument_list|(
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
name|strategyRef
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|strategy
operator|!=
literal|null
operator|&&
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
name|MulticastDefinition
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
return|return
name|this
return|;
block|}
DECL|method|getParallelProcessing ()
specifier|public
name|Boolean
name|getParallelProcessing
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
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
operator|!=
literal|null
operator|&&
name|parallelProcessing
return|;
block|}
DECL|method|getStreaming ()
specifier|public
name|Boolean
name|getStreaming
parameter_list|()
block|{
return|return
name|streaming
return|;
block|}
DECL|method|setStreaming (Boolean streaming)
specifier|public
name|void
name|setStreaming
parameter_list|(
name|Boolean
name|streaming
parameter_list|)
block|{
name|this
operator|.
name|streaming
operator|=
name|streaming
expr_stmt|;
block|}
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
name|streaming
operator|!=
literal|null
operator|&&
name|streaming
return|;
block|}
DECL|method|getStopOnException ()
specifier|public
name|Boolean
name|getStopOnException
parameter_list|()
block|{
return|return
name|stopOnException
return|;
block|}
DECL|method|setStopOnException (Boolean stopOnException)
specifier|public
name|void
name|setStopOnException
parameter_list|(
name|Boolean
name|stopOnException
parameter_list|)
block|{
name|this
operator|.
name|stopOnException
operator|=
name|stopOnException
expr_stmt|;
block|}
DECL|method|isStopOnException ()
specifier|public
name|Boolean
name|isStopOnException
parameter_list|()
block|{
return|return
name|stopOnException
operator|!=
literal|null
operator|&&
name|stopOnException
return|;
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
DECL|method|getStrategyMethodName ()
specifier|public
name|String
name|getStrategyMethodName
parameter_list|()
block|{
return|return
name|strategyMethodName
return|;
block|}
DECL|method|setStrategyMethodName (String strategyMethodName)
specifier|public
name|void
name|setStrategyMethodName
parameter_list|(
name|String
name|strategyMethodName
parameter_list|)
block|{
name|this
operator|.
name|strategyMethodName
operator|=
name|strategyMethodName
expr_stmt|;
block|}
DECL|method|getStrategyMethodAllowNull ()
specifier|public
name|Boolean
name|getStrategyMethodAllowNull
parameter_list|()
block|{
return|return
name|strategyMethodAllowNull
return|;
block|}
DECL|method|setStrategyMethodAllowNull (Boolean strategyMethodAllowNull)
specifier|public
name|void
name|setStrategyMethodAllowNull
parameter_list|(
name|Boolean
name|strategyMethodAllowNull
parameter_list|)
block|{
name|this
operator|.
name|strategyMethodAllowNull
operator|=
name|strategyMethodAllowNull
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
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getOnPrepareRef ()
specifier|public
name|String
name|getOnPrepareRef
parameter_list|()
block|{
return|return
name|onPrepareRef
return|;
block|}
DECL|method|setOnPrepareRef (String onPrepareRef)
specifier|public
name|void
name|setOnPrepareRef
parameter_list|(
name|String
name|onPrepareRef
parameter_list|)
block|{
name|this
operator|.
name|onPrepareRef
operator|=
name|onPrepareRef
expr_stmt|;
block|}
DECL|method|getOnPrepare ()
specifier|public
name|Processor
name|getOnPrepare
parameter_list|()
block|{
return|return
name|onPrepare
return|;
block|}
DECL|method|setOnPrepare (Processor onPrepare)
specifier|public
name|void
name|setOnPrepare
parameter_list|(
name|Processor
name|onPrepare
parameter_list|)
block|{
name|this
operator|.
name|onPrepare
operator|=
name|onPrepare
expr_stmt|;
block|}
DECL|method|getShareUnitOfWork ()
specifier|public
name|Boolean
name|getShareUnitOfWork
parameter_list|()
block|{
return|return
name|shareUnitOfWork
return|;
block|}
DECL|method|setShareUnitOfWork (Boolean shareUnitOfWork)
specifier|public
name|void
name|setShareUnitOfWork
parameter_list|(
name|Boolean
name|shareUnitOfWork
parameter_list|)
block|{
name|this
operator|.
name|shareUnitOfWork
operator|=
name|shareUnitOfWork
expr_stmt|;
block|}
DECL|method|isShareUnitOfWork ()
specifier|public
name|boolean
name|isShareUnitOfWork
parameter_list|()
block|{
return|return
name|shareUnitOfWork
operator|!=
literal|null
operator|&&
name|shareUnitOfWork
return|;
block|}
DECL|method|getParallelAggregate ()
specifier|public
name|Boolean
name|getParallelAggregate
parameter_list|()
block|{
return|return
name|parallelAggregate
return|;
block|}
comment|/**      * Whether to aggregate using a sequential single thread, or allow parallel aggregation.      *<p/>      * Notice that if enabled, then the {@link org.apache.camel.processor.aggregate.AggregationStrategy} in use      * must be implemented as thread safe, as concurrent threads can call the<tt>aggregate</tt> methods at the same time.      */
DECL|method|isParallelAggregate ()
specifier|public
name|boolean
name|isParallelAggregate
parameter_list|()
block|{
return|return
name|parallelAggregate
operator|!=
literal|null
operator|&&
name|parallelAggregate
return|;
block|}
DECL|method|setParallelAggregate (Boolean parallelAggregate)
specifier|public
name|void
name|setParallelAggregate
parameter_list|(
name|Boolean
name|parallelAggregate
parameter_list|)
block|{
name|this
operator|.
name|parallelAggregate
operator|=
name|parallelAggregate
expr_stmt|;
block|}
block|}
end_class

end_unit

