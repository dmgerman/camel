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
comment|/**  * A content enricher that enriches input data by first obtaining additional  * data from a<i>resource</i> represented by an endpoint<code>producer</code>  * and second by aggregating input data and additional data. Aggregation of  * input data and additional data is delegated to an {@link org.apache.camel.processor.aggregate.AggregationStrategy}  * object.  *<p/>  * Uses a {@link org.apache.camel.PollingConsumer} to obtain the additional data as opposed to {@link Enricher}  * that uses a {@link org.apache.camel.Producer}.  *  * @see Enricher  */
end_comment

begin_class
DECL|class|PollEnricher
specifier|public
class|class
name|PollEnricher
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
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
name|PollEnricher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
DECL|field|consumer
specifier|private
name|PollingConsumer
name|consumer
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
comment|/**      * Creates a new {@link PollEnricher}. The default aggregation strategy is to      * copy the additional data obtained from the enricher's resource over the      * input data. When using the copy aggregation strategy the enricher      * degenerates to a normal transformer.      *      * @param consumer consumer to resource endpoint.      */
DECL|method|PollEnricher (PollingConsumer consumer)
specifier|public
name|PollEnricher
parameter_list|(
name|PollingConsumer
name|consumer
parameter_list|)
block|{
name|this
argument_list|(
name|defaultAggregationStrategy
argument_list|()
argument_list|,
name|consumer
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new {@link PollEnricher}.      *      * @param aggregationStrategy  aggregation strategy to aggregate input data and additional data.      * @param consumer consumer to resource endpoint.      * @param timeout timeout in millis      */
DECL|method|PollEnricher (AggregationStrategy aggregationStrategy, PollingConsumer consumer, long timeout)
specifier|public
name|PollEnricher
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|PollingConsumer
name|consumer
parameter_list|,
name|long
name|timeout
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
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
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
comment|/**      * Enriches the input data (<code>exchange</code>) by first obtaining      * additional data from an endpoint represented by an endpoint      *<code>producer</code> and second by aggregating input data and additional      * data. Aggregation of input data and additional data is delegated to an      * {@link org.apache.camel.processor.aggregate.AggregationStrategy} object set at construction time. If the      * message exchange with the resource endpoint fails then no aggregation      * will be done and the failed exchange content is copied over to the      * original message exchange.      *      * @param exchange input data.      */
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
name|Exchange
name|resourceExchange
decl_stmt|;
if|if
condition|(
name|timeout
operator|<
literal|0
condition|)
block|{
name|LOG
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
name|LOG
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
name|LOG
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer received no exchange"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Consumer received: {}"
argument_list|,
name|resourceExchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resourceExchange
operator|!=
literal|null
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
name|consumer
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
name|consumer
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
name|consumer
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

