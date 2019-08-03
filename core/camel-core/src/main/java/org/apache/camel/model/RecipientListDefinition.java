begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|builder
operator|.
name|ProcessClause
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Routes messages to a number of dynamically specified recipients (dynamic to)  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"eip,endpoint,routing"
argument_list|)
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
parameter_list|<
name|Type
parameter_list|>
parameter_list|>
extends|extends
name|ExpressionNode
implements|implements
name|ExecutorServiceAwareDefinition
argument_list|<
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|","
argument_list|)
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
DECL|field|cacheSize
specifier|private
name|Integer
name|cacheSize
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
comment|/**      * Delimiter used if the Expression returned multiple endpoints. Can be turned off using the value<tt>false</tt>.      *<p/>      * The default value is ,      *      * @param delimiter the delimiter      * @return the builder      */
DECL|method|delimiter (String delimiter)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
name|delimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|setDelimiter
argument_list|(
name|delimiter
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the AggregationStrategy to be used to assemble the replies from the recipients, into a single outgoing message from the RecipientList.      * By default Camel will use the last reply as the outgoing message. You can also use a POJO as the AggregationStrategy      */
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
comment|/**      * Sets a reference to the AggregationStrategy to be used to assemble the replies from the recipients, into a single outgoing message from the RecipientList.      * By default Camel will use the last reply as the outgoing message. You can also use a POJO as the AggregationStrategy      */
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
comment|/**      * This option can be used to explicit declare the method name to use, when using POJOs as the AggregationStrategy.      *      * @param  methodName the method name to call      * @return the builder      */
DECL|method|aggregationStrategyMethodName (String methodName)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * If enabled then sending messages to the recipients occurs concurrently.      * Note the caller thread will still wait until all messages has been fully processed, before it continues.      * Its only the sending and processing the replies from the recipients which happens concurrently.      *      * @return the builder      */
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
comment|/**      * If enabled then sending messages to the recipients occurs concurrently.      * Note the caller thread will still wait until all messages has been fully processed, before it continues.      * Its only the sending and processing the replies from the recipients which happens concurrently.      *      * @return the builder      */
DECL|method|parallelProcessing (boolean parallelProcessing)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
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
comment|/**      * If enabled then Camel will process replies out-of-order, eg in the order they come back.      * If disabled, Camel will process replies in the same order as defined by the recipient list.      *      * @return the builder      */
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
comment|/**      * To use a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
annotation|@
name|Override
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
comment|/**      * Refers to a custom Thread Pool to be used for parallel processing.      * Notice if you set this option, then parallel processing is automatic implied, and you do not have to enable that option as well.      */
annotation|@
name|Override
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
comment|/**      * Sets the {@link Processor} when preparing the {@link org.apache.camel.Exchange} to be used send using a fluent buidler.      */
DECL|method|onPrepare ()
specifier|public
name|ProcessClause
argument_list|<
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
argument_list|>
name|onPrepare
parameter_list|()
block|{
name|ProcessClause
argument_list|<
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
argument_list|>
name|clause
init|=
operator|new
name|ProcessClause
argument_list|<>
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|setOnPrepare
argument_list|(
name|clause
argument_list|)
expr_stmt|;
return|return
name|clause
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
comment|/**      * Sets a total timeout specified in millis, when using parallel processing.      * If the Recipient List hasn't been able to send and process all replies within the given timeframe,      * then the timeout triggers and the Recipient List breaks out and continues.      * Notice if you provide a TimeoutAwareAggregationStrategy then the timeout method is invoked before breaking out.      * If the timeout is reached with running tasks still remaining, certain tasks for which it is difficult for Camel      * to shut down in a graceful manner may continue to run. So use this option with a bit of care.      *      * @param timeout timeout in millis      * @return the builder      */
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
comment|/**      * Shares the {@link org.apache.camel.spi.UnitOfWork} with the parent and each of the sub messages.      * Recipient List will by default not share unit of work between the parent exchange and each recipient exchange.      * This means each sub exchange has its own individual unit of work.      *      * @return the builder.      */
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
comment|/**      * Sets the maximum size used by the {@link org.apache.camel.spi.ProducerCache} which is used      * to cache and reuse producers when using this recipient list, when uris are reused.      *      * @param cacheSize  the cache size, use<tt>0</tt> for default cache size, or<tt>-1</tt> to turn cache off.      * @return the builder      */
DECL|method|cacheSize (int cacheSize)
specifier|public
name|RecipientListDefinition
argument_list|<
name|Type
argument_list|>
name|cacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|setCacheSize
argument_list|(
name|cacheSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
comment|/**      * Expression that returns which endpoints (url) to send the message to (the recipients).      * If the expression return an empty value then the message is not sent to any recipients.      */
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
comment|/**      * Sets a reference to the AggregationStrategy to be used to assemble the replies from the recipients, into a single outgoing message from the RecipientList.      * By default Camel will use the last reply as the outgoing message. You can also use a POJO as the AggregationStrategy      */
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
annotation|@
name|Override
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
annotation|@
name|Override
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
comment|/**      * Sets the AggregationStrategy to be used to assemble the replies from the recipients, into a single outgoing message from the RecipientList.      * By default Camel will use the last reply as the outgoing message. You can also use a POJO as the AggregationStrategy      */
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
annotation|@
name|Override
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
annotation|@
name|Override
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
DECL|method|getCacheSize ()
specifier|public
name|Integer
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (Integer cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|Integer
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
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
block|}
end_class

end_unit

