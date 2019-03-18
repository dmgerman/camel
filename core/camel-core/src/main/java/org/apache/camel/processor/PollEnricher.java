begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AsyncCallback
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
name|CamelExchangeException
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
name|Consumer
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
name|Endpoint
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
name|Exchange
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
name|PollingConsumer
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
name|impl
operator|.
name|DefaultConsumerCache
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
name|ConsumerCache
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
name|EndpointUtilizationStatistics
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
name|ExceptionHandler
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
name|IdAware
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
name|AsyncProcessorSupport
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
name|BridgeExceptionHandlerToErrorHandler
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
name|DefaultConsumer
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
name|EventDrivenPollingConsumer
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
name|ExchangeHelper
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ExchangeHelper
operator|.
name|copyResultsPreservePattern
import|;
end_import

begin_comment
comment|/**  * A content enricher that enriches input data by first obtaining additional  * data from a<i>resource</i> represented by an endpoint<code>producer</code>  * and second by aggregating input data and additional data. Aggregation of  * input data and additional data is delegated to an {@link AggregationStrategy}  * object.  *<p/>  * Uses a {@link org.apache.camel.PollingConsumer} to obtain the additional data as opposed to {@link Enricher}  * that uses a {@link org.apache.camel.Producer}.  *  * @see Enricher  */
end_comment

begin_class
DECL|class|PollEnricher
specifier|public
class|class
name|PollEnricher
extends|extends
name|AsyncProcessorSupport
implements|implements
name|IdAware
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|consumerCache
specifier|private
name|ConsumerCache
name|consumerCache
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|aggregateOnException
specifier|private
name|boolean
name|aggregateOnException
decl_stmt|;
DECL|field|cacheSize
specifier|private
name|int
name|cacheSize
decl_stmt|;
DECL|field|ignoreInvalidEndpoint
specifier|private
name|boolean
name|ignoreInvalidEndpoint
decl_stmt|;
comment|/**      * Creates a new {@link PollEnricher}.      *      * @param expression expression to use to compute the endpoint to poll from.      * @param timeout timeout in millis      */
DECL|method|PollEnricher (Expression expression, long timeout)
specifier|public
name|PollEnricher
parameter_list|(
name|Expression
name|expression
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|getEndpointUtilizationStatistics ()
specifier|public
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
block|{
return|return
name|consumerCache
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
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
comment|/**      * Sets the aggregation strategy for this poll enricher.      *      * @param aggregationStrategy the aggregationStrategy to set      */
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
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets the timeout to use when polling.      *<p/>      * Use 0 to use receiveNoWait,      * Use -1 to use receive with no timeout (which will block until data is available).      *      * @param timeout timeout in millis.      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
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
DECL|method|isAggregateOnException ()
specifier|public
name|boolean
name|isAggregateOnException
parameter_list|()
block|{
return|return
name|aggregateOnException
return|;
block|}
DECL|method|setAggregateOnException (boolean aggregateOnException)
specifier|public
name|void
name|setAggregateOnException
parameter_list|(
name|boolean
name|aggregateOnException
parameter_list|)
block|{
name|this
operator|.
name|aggregateOnException
operator|=
name|aggregateOnException
expr_stmt|;
block|}
comment|/**      * Sets the default aggregation strategy for this poll enricher.      */
DECL|method|setDefaultAggregationStrategy ()
specifier|public
name|void
name|setDefaultAggregationStrategy
parameter_list|()
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|defaultAggregationStrategy
argument_list|()
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|int
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (int cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|int
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
DECL|method|isIgnoreInvalidEndpoint ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (boolean ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|boolean
name|ignoreInvalidEndpoint
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoint
operator|=
name|ignoreInvalidEndpoint
expr_stmt|;
block|}
comment|/**      * Enriches the input data (<code>exchange</code>) by first obtaining      * additional data from an endpoint represented by an endpoint      *<code>producer</code> and second by aggregating input data and additional      * data. Aggregation of input data and additional data is delegated to an      * {@link AggregationStrategy} object set at construction time. If the      * message exchange with the resource endpoint fails then no aggregation      * will be done and the failed exchange content is copied over to the      * original message exchange.      *      * @param exchange input data.      */
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
try|try
block|{
name|preCheckPoll
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error during pre poll check"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// which consumer to use
name|PollingConsumer
name|consumer
decl_stmt|;
name|Endpoint
name|endpoint
decl_stmt|;
comment|// use dynamic endpoint so calculate the endpoint to use
name|Object
name|recipient
init|=
literal|null
decl_stmt|;
try|try
block|{
name|recipient
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
comment|// acquire the consumer from the cache
name|consumer
operator|=
name|consumerCache
operator|.
name|acquirePollingConsumer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|isIgnoreInvalidEndpoint
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Endpoint uri is invalid: "
operator|+
name|recipient
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// grab the real delegate consumer that performs the actual polling
name|Consumer
name|delegate
init|=
name|consumer
decl_stmt|;
if|if
condition|(
name|consumer
operator|instanceof
name|EventDrivenPollingConsumer
condition|)
block|{
name|delegate
operator|=
operator|(
operator|(
name|EventDrivenPollingConsumer
operator|)
name|consumer
operator|)
operator|.
name|getDelegateConsumer
argument_list|()
expr_stmt|;
block|}
comment|// is the consumer bridging the error handler?
name|boolean
name|bridgeErrorHandler
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|delegate
operator|instanceof
name|DefaultConsumer
condition|)
block|{
name|ExceptionHandler
name|handler
init|=
operator|(
operator|(
name|DefaultConsumer
operator|)
name|delegate
operator|)
operator|.
name|getExceptionHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|BridgeExceptionHandlerToErrorHandler
condition|)
block|{
name|bridgeErrorHandler
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|Exchange
name|resourceExchange
decl_stmt|;
try|try
block|{
if|if
condition|(
name|timeout
operator|<
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer receive: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|resourceExchange
operator|=
name|consumer
operator|.
name|receive
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|timeout
operator|==
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer receiveNoWait: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|resourceExchange
operator|=
name|consumer
operator|.
name|receiveNoWait
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer receive with timeout: {} ms. {}"
argument_list|,
name|timeout
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|resourceExchange
operator|=
name|consumer
operator|.
name|receive
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resourceExchange
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer received no exchange"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Consumer received: {}"
argument_list|,
name|resourceExchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error during poll"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
finally|finally
block|{
comment|// return the consumer back to the cache
name|consumerCache
operator|.
name|releasePollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|// remember current redelivery stats
name|Object
name|redeliveried
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
decl_stmt|;
name|Object
name|redeliveryCounter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
decl_stmt|;
name|Object
name|redeliveryMaxCounter
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|)
decl_stmt|;
comment|// if we are bridging error handler and failed then remember the caused exception
name|Throwable
name|cause
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|resourceExchange
operator|!=
literal|null
operator|&&
name|bridgeErrorHandler
condition|)
block|{
name|cause
operator|=
name|resourceExchange
operator|.
name|getException
argument_list|()
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
operator|!
name|isAggregateOnException
argument_list|()
operator|&&
operator|(
name|resourceExchange
operator|!=
literal|null
operator|&&
name|resourceExchange
operator|.
name|isFailed
argument_list|()
operator|)
condition|)
block|{
comment|// copy resource exchange onto original exchange (preserving pattern)
comment|// and preserve redelivery headers
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|resourceExchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prepareResult
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// prepare the exchanges for aggregation
name|ExchangeHelper
operator|.
name|prepareAggregation
argument_list|(
name|exchange
argument_list|,
name|resourceExchange
argument_list|)
expr_stmt|;
comment|// must catch any exception from aggregation
name|Exchange
name|aggregatedExchange
init|=
name|aggregationStrategy
operator|.
name|aggregate
argument_list|(
name|exchange
argument_list|,
name|resourceExchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|aggregatedExchange
operator|!=
literal|null
condition|)
block|{
comment|// copy aggregation result onto original exchange (preserving pattern)
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|aggregatedExchange
argument_list|)
expr_stmt|;
comment|// handover any synchronization
if|if
condition|(
name|resourceExchange
operator|!=
literal|null
condition|)
block|{
name|resourceExchange
operator|.
name|handoverCompletions
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// if we failed then restore caused exception
if|if
condition|(
name|cause
operator|!=
literal|null
condition|)
block|{
comment|// restore caused exception
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
comment|// remove the exhausted marker as we want to be able to perform redeliveries with the error handler
name|exchange
operator|.
name|removeProperties
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_EXHAUSTED
argument_list|)
expr_stmt|;
comment|// preserve the redelivery stats
if|if
condition|(
name|redeliveried
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
name|redeliveried
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
name|redeliveried
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|redeliveryCounter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|redeliveryCounter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|redeliveryCounter
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|redeliveryMaxCounter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|,
name|redeliveryMaxCounter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_MAX_COUNTER
argument_list|,
name|redeliveryMaxCounter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// set header with the uri of the endpoint enriched so we can use that for tracing etc
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Error occurred during aggregation"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
block|{
comment|// trim strings as end users might have added spaces between separators
if|if
condition|(
name|recipient
operator|instanceof
name|String
condition|)
block|{
name|recipient
operator|=
operator|(
operator|(
name|String
operator|)
name|recipient
operator|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
return|;
block|}
comment|/**      * Strategy to pre check polling.      *<p/>      * Is currently used to prevent doing poll enrich from a file based endpoint when the current route also      * started from a file based endpoint as that is not currently supported.      *      * @param exchange the current exchange      */
DECL|method|preCheckPoll (Exchange exchange)
specifier|protected
name|void
name|preCheckPoll
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|prepareResult (Exchange exchange)
specifier|private
specifier|static
name|void
name|prepareResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|defaultAggregationStrategy ()
specifier|private
specifier|static
name|AggregationStrategy
name|defaultAggregationStrategy
parameter_list|()
block|{
return|return
operator|new
name|CopyAggregationStrategy
argument_list|()
return|;
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
literal|"PollEnrich["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|consumerCache
operator|==
literal|null
condition|)
block|{
comment|// create consumer cache if we use dynamic expressions for computing the endpoints to poll
name|consumerCache
operator|=
operator|new
name|DefaultConsumerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|,
name|cacheSize
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"PollEnrich {} using ConsumerCache with cacheSize={}"
argument_list|,
name|this
argument_list|,
name|cacheSize
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|aggregationStrategy
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|aggregationStrategy
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumerCache
argument_list|,
name|aggregationStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|aggregationStrategy
argument_list|,
name|consumerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|aggregationStrategy
argument_list|,
name|consumerCache
argument_list|)
expr_stmt|;
block|}
DECL|class|CopyAggregationStrategy
specifier|private
specifier|static
class|class
name|CopyAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
if|if
condition|(
name|newExchange
operator|!=
literal|null
condition|)
block|{
name|copyResultsPreservePattern
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if no newExchange then there was no message from the external resource
comment|// and therefore we should set an empty body to indicate this fact
comment|// but keep headers/attachments as we want to propagate those
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|oldExchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

