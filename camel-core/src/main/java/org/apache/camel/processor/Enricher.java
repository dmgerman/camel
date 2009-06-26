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
name|impl
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
comment|/**  * A content enricher that enriches input data by first obtaining additional  * data from a<i>resource</i> represented by an endpoint<code>producer</code>  * and second by aggregating input data and additional data. Aggregation of  * input data and additional data is delegated to an {@link AggregationStrategy}  * object.  */
end_comment

begin_class
DECL|class|Enricher
specifier|public
class|class
name|Enricher
extends|extends
name|ServiceSupport
implements|implements
name|Processor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
comment|/**      * Enriches the input data (<code>exchange</code>) by first obtaining      * additional data from an endpoint represented by an endpoint      *<code>producer</code> and second by aggregating input data and additional      * data. Aggregation of input data and additional data is delegated to an      * {@link AggregationStrategy} object set at construction time. If the      * message exchange with the resource endpoint fails then no aggregation      * will be done and the failed exchange content is copied over to the      * original message exchange.      *       * @param exchange input data.      */
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
name|producer
operator|.
name|process
argument_list|(
name|resourceExchange
argument_list|)
expr_stmt|;
if|if
condition|(
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
comment|// aggregate original exchange and resource exchange
comment|// but do not aggregate if the resource exchange was filtered
name|Boolean
name|filtered
init|=
name|resourceExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTERED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|filtered
operator|==
literal|null
operator|||
operator|!
name|filtered
condition|)
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
comment|// copy aggregation result onto original exchange (preserving pattern)
name|copyResultsPreservePattern
argument_list|(
name|exchange
argument_list|,
name|aggregatedExchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot aggregate exchange as its filtered: "
operator|+
name|resourceExchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|DefaultExchange
name|target
init|=
operator|new
name|DefaultExchange
argument_list|(
name|source
operator|.
name|getContext
argument_list|()
argument_list|)
decl_stmt|;
name|target
operator|.
name|copyFrom
argument_list|(
name|source
argument_list|)
expr_stmt|;
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
operator|.
name|getEndpointUri
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
name|producer
operator|.
name|start
argument_list|()
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
name|producer
operator|.
name|stop
argument_list|()
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
name|copyResultsPreservePattern
argument_list|(
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
return|return
name|oldExchange
return|;
block|}
block|}
block|}
end_class

end_unit

