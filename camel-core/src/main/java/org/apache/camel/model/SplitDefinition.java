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
name|Splitter
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
name|spi
operator|.
name|Metadata
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

begin_comment
comment|/**  * Splits a single message into many sub-messages.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,routing"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"split"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|SplitDefinition
specifier|public
class|class
name|SplitDefinition
extends|extends
name|ExpressionNode
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|SplitDefinition
argument_list|>
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
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
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
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"0"
argument_list|)
DECL|field|timeout
specifier|private
name|Long
name|timeout
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
annotation|@
name|XmlAttribute
DECL|field|stopOnAggregateException
specifier|private
name|Boolean
name|stopOnAggregateException
decl_stmt|;
DECL|method|SplitDefinition ()
specifier|public
name|SplitDefinition
parameter_list|()
block|{     }
DECL|method|SplitDefinition (Expression expression)
specifier|public
name|SplitDefinition
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
expr_stmt|;
block|}
DECL|method|SplitDefinition (ExpressionDefinition expression)
specifier|public
name|SplitDefinition
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|super
argument_list|(
name|expression
argument_list|)
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
literal|"Split["
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
literal|"split"
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
literal|"split["
operator|+
name|getExpression
argument_list|()
operator|+
literal|"]"
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
name|aggregationStrategy
operator|=
name|createAggregationStrategy
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|boolean
name|isParallelProcessing
init|=
name|getParallelProcessing
argument_list|()
operator|!=
literal|null
operator|&&
name|getParallelProcessing
argument_list|()
decl_stmt|;
name|boolean
name|isStreaming
init|=
name|getStreaming
argument_list|()
operator|!=
literal|null
operator|&&
name|getStreaming
argument_list|()
decl_stmt|;
name|boolean
name|isShareUnitOfWork
init|=
name|getShareUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|getShareUnitOfWork
argument_list|()
decl_stmt|;
name|boolean
name|isParallelAggregate
init|=
name|getParallelAggregate
argument_list|()
operator|!=
literal|null
operator|&&
name|getParallelAggregate
argument_list|()
decl_stmt|;
name|boolean
name|isStopOnAggregateException
init|=
name|getStopOnAggregateException
argument_list|()
operator|!=
literal|null
operator|&&
name|getStopOnAggregateException
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
name|this
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
literal|"Split"
argument_list|,
name|this
argument_list|,
name|isParallelProcessing
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
name|Expression
name|exp
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|Splitter
name|answer
init|=
operator|new
name|Splitter
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|exp
argument_list|,
name|childProcessor
argument_list|,
name|aggregationStrategy
argument_list|,
name|isParallelProcessing
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|,
name|isStreaming
argument_list|,
name|isStopOnException
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|isShareUnitOfWork
argument_list|,
name|isParallelAggregate
argument_list|,
name|isStopOnAggregateException
argument_list|)
decl_stmt|;
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
name|strategy
operator|!=
literal|null
operator|&&
name|shareUnitOfWork
operator|!=
literal|null
operator|&&
name|shareUnitOfWork
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
comment|// Fluent API
comment|// -------------------------------------------------------------------------
comment|/**      * Sets the AggregationStrategy to be used to assemble the replies from the splitted messages, into a single outgoing message from the Splitter.      * By default Camel will use the original incoming message to the splitter (leave it unchanged). You can also use a POJO as the AggregationStrategy      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|SplitDefinition
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
comment|/**      * Sets a reference to the AggregationStrategy to be used to assemble the replies from the splitted messages, into a single outgoing message from the Splitter.      * By default Camel will use the original incoming message to the splitter (leave it unchanged). You can also use a POJO as the AggregationStrategy      */
DECL|method|aggregationStrategyRef (String aggregationStrategyRef)
specifier|public
name|SplitDefinition
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
comment|/**      * This option can be used to explicit declare the method name to use, when using POJOs as the AggregationStrategy.      *      * @param  methodName the method name to call      * @return the builder      */
DECL|method|aggregationStrategyMethodName (String methodName)
specifier|public
name|SplitDefinition
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
comment|/**      * If this option is false then the aggregate method is not used if there was no data to enrich.      * If this option is true then null values is used as the oldExchange (when no data to enrich), when using POJOs as the AggregationStrategy      *      * @return the builder      */
DECL|method|aggregationStrategyMethodAllowNull ()
specifier|public
name|SplitDefinition
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
comment|/**      * If enabled then processing each splitted messages occurs concurrently.      * Note the caller thread will still wait until all messages has been fully processed, before it continues.      * Its only processing the sub messages from the splitter which happens concurrently.      *      * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|SplitDefinition
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
comment|/**      * If enabled then processing each splitted messages occurs concurrently.      * Note the caller thread will still wait until all messages has been fully processed, before it continues.      * Its only processing the sub messages from the splitter which happens concurrently.      *      * @return the builder      */
DECL|method|parallelProcessing (boolean parallelProcessing)
specifier|public
name|SplitDefinition
name|parallelProcessing
parameter_list|(
name|boolean
name|parallelProcessing
parameter_list|)
block|{
name|setParallelProcessing
argument_list|(
name|parallelProcessing
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * If enabled then the aggregate method on AggregationStrategy can be called concurrently.      * Notice that this would require the implementation of AggregationStrategy to be implemented as thread-safe.      * By default this is false meaning that Camel synchronizes the call to the aggregate method.      * Though in some use-cases this can be used to archive higher performance when the AggregationStrategy is implemented as thread-safe.      *      * @return the builder      */
DECL|method|parallelAggregate ()
specifier|public
name|SplitDefinition
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
comment|/**      * If enabled, unwind exceptions occurring at aggregation time to the error handler when parallelProcessing is used.      * Currently, aggregation time exceptions do not stop the route processing when parallelProcessing is used.      * Enabling this option allows to work around this behavior.      *      * The default value is<code>false</code> for the sake of backward compatibility.      *      * @return the builder      */
DECL|method|stopOnAggregateException ()
specifier|public
name|SplitDefinition
name|stopOnAggregateException
parameter_list|()
block|{
name|setStopOnAggregateException
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * When in streaming mode, then the splitter splits the original message on-demand, and each splitted      * message is processed one by one. This reduces memory usage as the splitter do not split all the messages first,      * but then we do not know the total size, and therefore the {@link org.apache.camel.Exchange#SPLIT_SIZE} is empty.      *<p/>      * In non-streaming mode (default) the splitter will split each message first, to know the total size, and then      * process each message one by one. This requires to keep all the splitted messages in memory and therefore requires      * more memory. The total size is provided in the {@link org.apache.camel.Exchange#SPLIT_SIZE} header.      *<p/>      * The streaming mode also affects the aggregation behavior.      * If enabled then Camel will process replies out-of-order, eg in the order they come back.      * If disabled, Camel will process replies in the same order as the messages was splitted.      *      * @return the builder      */
DECL|method|streaming ()
specifier|public
name|SplitDefinition
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
comment|/**      * Will now stop further processing if an exception or failure occurred during processing of an      * {@link org.apache.camel.Exchange} and the caused exception will be thrown.      *<p/>      * Will also stop if processing the exchange failed (has a fault message) or an exception      * was thrown and handled by the error handler (such as using onException). In all situations      * the splitter will stop further processing. This is the same behavior as in pipeline, which      * is used by the routing engine.      *<p/>      * The default behavior is to<b>not</b> stop but continue processing till the end      *      * @return the builder      */
DECL|method|stopOnException ()
specifier|public
name|SplitDefinition
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
comment|/**      * To use a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
DECL|method|executorService (ExecutorService executorService)
specifier|public
name|SplitDefinition
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
comment|/**      * Refers to a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
DECL|method|executorServiceRef (String executorServiceRef)
specifier|public
name|SplitDefinition
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
name|SplitDefinition
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
name|SplitDefinition
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
comment|/**      * Sets a total timeout specified in millis, when using parallel processing.      * If the Splitter hasn't been able to split and process all the sub messages within the given timeframe,      * then the timeout triggers and the Splitter breaks out and continues.      * Notice if you provide a TimeoutAwareAggregationStrategy then the timeout method is invoked before breaking out.      * If the timeout is reached with running tasks still remaining, certain tasks for which it is difficult for Camel      * to shut down in a graceful manner may continue to run. So use this option with a bit of care.      *      * @param timeout timeout in millis      * @return the builder      */
DECL|method|timeout (long timeout)
specifier|public
name|SplitDefinition
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
comment|/**      * Shares the {@link org.apache.camel.spi.UnitOfWork} with the parent and each of the sub messages.      * Splitter will by default not share unit of work between the parent exchange and each splitted exchange.      * This means each splitted exchange has its own individual unit of work.      *      * @return the builder.      * @see org.apache.camel.spi.SubUnitOfWork      */
DECL|method|shareUnitOfWork ()
specifier|public
name|SplitDefinition
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
comment|// Properties
comment|//-------------------------------------------------------------------------
comment|/**      * Expression of how to split the message body, such as as-is, using a tokenizer, or using an xpath.      */
annotation|@
name|Override
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// override to include javadoc what the expression is used for
name|super
operator|.
name|setExpression
argument_list|(
name|expression
argument_list|)
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
comment|/**      * Sets the AggregationStrategy to be used to assemble the replies from the splitted messages, into a single outgoing message from the Splitter.      * By default Camel will use the original incoming message to the splitter (leave it unchanged). You can also use a POJO as the AggregationStrategy      */
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
DECL|method|getStopOnAggregateException ()
specifier|public
name|Boolean
name|getStopOnAggregateException
parameter_list|()
block|{
return|return
name|this
operator|.
name|stopOnAggregateException
return|;
block|}
DECL|method|setStopOnAggregateException (Boolean stopOnAggregateException)
specifier|public
name|void
name|setStopOnAggregateException
parameter_list|(
name|Boolean
name|stopOnAggregateException
parameter_list|)
block|{
name|this
operator|.
name|stopOnAggregateException
operator|=
name|stopOnAggregateException
expr_stmt|;
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
comment|/**      * Sets a reference to the AggregationStrategy to be used to assemble the replies from the splitted messages, into a single outgoing message from the Splitter.      * By default Camel will use the original incoming message to the splitter (leave it unchanged). You can also use a POJO as the AggregationStrategy      */
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
comment|/**      * This option can be used to explicit declare the method name to use, when using POJOs as the AggregationStrategy.      */
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
comment|/**      * If this option is false then the aggregate method is not used if there was no data to enrich.      * If this option is true then null values is used as the oldExchange (when no data to enrich), when using POJOs as the AggregationStrategy      */
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
block|}
end_class

end_unit

