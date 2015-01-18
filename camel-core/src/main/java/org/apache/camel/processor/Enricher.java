begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExchangePattern
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
name|Producer
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
name|DefaultExchange
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
name|support
operator|.
name|ServiceSupport
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorHelper
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
name|EventHelper
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
name|util
operator|.
name|ServiceHelper
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
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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
name|util
operator|.
name|ExchangeHelper
operator|.
name|copyResultsPreservePattern
import|;
end_import

begin_comment
comment|/**  * A content enricher that enriches input data by first obtaining additional  * data from a<i>resource</i> represented by an endpoint<code>producer</code>  * and second by aggregating input data and additional data. Aggregation of  * input data and additional data is delegated to an {@link AggregationStrategy}  * object.  *<p/>  * Uses a {@link org.apache.camel.Producer} to obtain the additional data as opposed to {@link PollEnricher}  * that uses a {@link org.apache.camel.PollingConsumer}.  *  * @see PollEnricher  */
end_comment

begin_class
DECL|class|Enricher
specifier|public
class|class
name|Enricher
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|EndpointAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Enricher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
name|producer
decl_stmt|;
DECL|field|aggregateOnException
specifier|private
name|boolean
name|aggregateOnException
decl_stmt|;
comment|/**      * Creates a new {@link Enricher}. The default aggregation strategy is to      * copy the additional data obtained from the enricher's resource over the      * input data. When using the copy aggregation strategy the enricher      * degenerates to a normal transformer.      *       * @param producer producer to resource endpoint.      */
DECL|method|Enricher (Producer producer)
specifier|public
name|Enricher
parameter_list|(
name|Producer
name|producer
parameter_list|)
block|{
name|this
argument_list|(
name|defaultAggregationStrategy
argument_list|()
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link Enricher}.      *       * @param aggregationStrategy  aggregation strategy to aggregate input data and additional data.      * @param producer producer to resource endpoint.      */
DECL|method|Enricher (AggregationStrategy aggregationStrategy, Producer producer)
specifier|public
name|Enricher
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|Producer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
comment|/**      * Sets the aggregation strategy for this enricher.      *      * @param aggregationStrategy the aggregationStrategy to set      */
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
comment|/**      * Whether to call {@link org.apache.camel.processor.aggregate.AggregationStrategy#aggregate(org.apache.camel.Exchange, org.apache.camel.Exchange)} if      * an exception was thrown.      */
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
comment|/**      * Sets the default aggregation strategy for this enricher.      */
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
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|producer
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Enriches the input data (<code>exchange</code>) by first obtaining      * additional data from an endpoint represented by an endpoint      *<code>producer</code> and second by aggregating input data and additional      * data. Aggregation of input data and additional data is delegated to an      * {@link AggregationStrategy} object set at construction time. If the      * message exchange with the resource endpoint fails then no aggregation      * will be done and the failed exchange content is copied over to the      * original message exchange.      *      * @param exchange input data.      */
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Exchange
name|resourceExchange
init|=
name|createResourceExchange
argument_list|(
name|exchange
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
specifier|final
name|Endpoint
name|destination
init|=
name|producer
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|resourceExchange
argument_list|,
name|destination
argument_list|)
expr_stmt|;
comment|// record timing for sending the exchange using the producer
specifier|final
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|AsyncProcessor
name|ap
init|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|ap
operator|.
name|process
argument_list|(
name|resourceExchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// we only have to handle async completion of the routing slip
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
comment|// emit event that the exchange was sent to the endpoint
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|resourceExchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|resourceExchange
argument_list|,
name|destination
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isAggregateOnException
argument_list|()
operator|&&
name|resourceExchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// copy resource exchange onto original exchange (preserving pattern)
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
try|try
block|{
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
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// if the aggregationStrategy threw an exception, set it on the original exchange
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
literal|false
argument_list|)
expr_stmt|;
comment|// we failed so break out now
return|return;
block|}
block|}
comment|// set property with the uri of the endpoint enriched so we can use that for tracing etc
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed asynchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// the remainder of the routing slip will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed synchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// emit event that the exchange was sent to the endpoint
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|resourceExchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|resourceExchange
argument_list|,
name|destination
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isAggregateOnException
argument_list|()
operator|&&
name|resourceExchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// copy resource exchange onto original exchange (preserving pattern)
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
try|try
block|{
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
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// if the aggregationStrategy threw an exception, set it on the original exchange
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
comment|// we failed so break out now
return|return
literal|true
return|;
block|}
block|}
comment|// set property with the uri of the endpoint enriched so we can use that for tracing etc
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
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
comment|/**      * Creates a new {@link DefaultExchange} instance from the given      *<code>exchange</code>. The resulting exchange's pattern is defined by      *<code>pattern</code>.      *      * @param source  exchange to copy from.      * @param pattern exchange pattern to set.      * @return created exchange.      */
DECL|method|createResourceExchange (Exchange source, ExchangePattern pattern)
specifier|protected
name|Exchange
name|createResourceExchange
parameter_list|(
name|Exchange
name|source
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
comment|// copy exchange, and do not share the unit of work
name|Exchange
name|target
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|source
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|target
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
return|return
name|target
return|;
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
literal|"Enrich["
operator|+
name|producer
operator|.
name|getEndpoint
argument_list|()
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
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|aggregationStrategy
argument_list|,
name|producer
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
name|stopServices
argument_list|(
name|producer
argument_list|,
name|aggregationStrategy
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
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

