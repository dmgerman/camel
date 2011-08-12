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
name|ExecutorServiceManager
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
comment|/**  * Represents an XML&lt;recipientList/&gt; element  *  * @version   */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"recipientList"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RecipientListDefinition
specifier|public
class|class
name|RecipientListDefinition
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|>
extends|extends
name|NoOutputExpressionNode
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|RecipientListDefinition
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
DECL|field|delimiter
specifier|private
name|String
name|delimiter
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
DECL|field|executorServiceRef
specifier|private
name|String
name|executorServiceRef
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
DECL|field|ignoreInvalidEndpoints
specifier|private
name|Boolean
name|ignoreInvalidEndpoints
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
DECL|method|RecipientListDefinition ()
specifier|public
name|RecipientListDefinition
parameter_list|()
block|{     }
DECL|method|RecipientListDefinition (ExpressionDefinition expression)
specifier|public
name|RecipientListDefinition
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
DECL|method|RecipientListDefinition (Expression expression)
specifier|public
name|RecipientListDefinition
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RecipientList["
operator|+
name|getExpression
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
literal|"recipientList"
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
literal|"recipientList["
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
name|Expression
name|expression
init|=
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|RecipientList
name|answer
decl_stmt|;
if|if
condition|(
name|delimiter
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
name|delimiter
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
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setStreaming
argument_list|(
name|isStreaming
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setShareUnitOfWork
argument_list|(
name|isShareUnitOfWork
argument_list|()
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|onPrepare
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOnPrepare
argument_list|(
name|onPrepare
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stopOnException
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setStopOnException
argument_list|(
name|isStopOnException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreInvalidEndpoints
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|ignoreInvalidEndpoints
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
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
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isParallelProcessing
argument_list|()
operator|&&
name|executorService
operator|==
literal|null
condition|)
block|{
name|String
name|ref
init|=
name|this
operator|.
name|executorServiceRef
operator|!=
literal|null
condition|?
name|this
operator|.
name|executorServiceRef
else|:
literal|"RecipientList"
decl_stmt|;
name|ExecutorServiceManager
name|manager
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|executorService
operator|=
name|manager
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
name|ref
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setExecutorService
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|aggregationStrategy
operator|==
literal|null
operator|&&
name|strategyRef
operator|!=
literal|null
condition|)
block|{
name|aggregationStrategy
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
name|aggregationStrategy
operator|==
literal|null
condition|)
block|{
comment|// fallback to use latest
name|aggregationStrategy
operator|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|aggregationStrategy
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|end ()
specifier|public
name|Type
name|end
parameter_list|()
block|{
comment|// allow end() to return to previous type so you can continue in the DSL
return|return
operator|(
name|Type
operator|)
name|super
operator|.
name|end
argument_list|()
return|;
block|}
comment|/**      * Set the aggregationStrategy      *      * @param aggregationStrategy the strategy      * @return the builder      */
DECL|method|aggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * Ignore the invalidate endpoint exception when try to create a producer with that endpoint      *      * @return the builder      */
DECL|method|ignoreInvalidEndpoints ()
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
name|ignoreInvalidEndpoints
parameter_list|()
block|{
name|setIgnoreInvalidEndpoints
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Doing the recipient list work in parallel      *      * @return the builder      */
DECL|method|parallelProcessing ()
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * Doing the recipient list work in streaming model      *      * @return the builder      */
DECL|method|streaming ()
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * Will now stop further processing if an exception or failure occurred during processing of an      * {@link org.apache.camel.Exchange} and the caused exception will be thrown.      *<p/>      * Will also stop if processing the exchange failed (has a fault message) or an exception      * was thrown and handled by the error handler (such as using onException). In all situations      * the recipient list will stop further processing. This is the same behavior as in pipeline, which      * is used by the routing engine.      *<p/>      * The default behavior is to<b>not</b> stop but continue processing till the end      *      * @return the builder      */
DECL|method|stopOnException ()
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * Uses the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be used send.      * This can be used to deep-clone messages that should be send, or any custom logic needed before      * the exchange is send.      *      * @param onPrepare the processor      * @return the builder      */
DECL|method|onPrepare (Processor onPrepare)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (String delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
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
DECL|method|getIgnoreInvalidEndpoints ()
specifier|public
name|Boolean
name|getIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoints
return|;
block|}
DECL|method|setIgnoreInvalidEndpoints (Boolean ignoreInvalidEndpoints)
specifier|public
name|void
name|setIgnoreInvalidEndpoints
parameter_list|(
name|Boolean
name|ignoreInvalidEndpoints
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoints
operator|=
name|ignoreInvalidEndpoints
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidEndpoints ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoints
operator|!=
literal|null
operator|&&
name|ignoreInvalidEndpoints
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
name|boolean
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
block|}
end_class

end_unit

