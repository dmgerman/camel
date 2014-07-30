begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.histogram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|metrics
operator|.
name|histogram
package|;
end_package

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Histogram
import|;
end_import

begin_import
import|import
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|MetricRegistry
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
name|Message
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
name|component
operator|.
name|metrics
operator|.
name|AbstractMetricsProducer
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
name|component
operator|.
name|metrics
operator|.
name|MetricsConstants
operator|.
name|HEADER_HISTOGRAM_VALUE
import|;
end_import

begin_class
DECL|class|HistogramProducer
specifier|public
class|class
name|HistogramProducer
extends|extends
name|AbstractMetricsProducer
argument_list|<
name|HistogramEndpoint
argument_list|>
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
name|HistogramProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|HistogramProducer (HistogramEndpoint endpoint)
specifier|public
name|HistogramProducer
parameter_list|(
name|HistogramEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doProcess (Exchange exchange, HistogramEndpoint endpoint, MetricRegistry registry, String metricsName)
specifier|protected
name|void
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|HistogramEndpoint
name|endpoint
parameter_list|,
name|MetricRegistry
name|registry
parameter_list|,
name|String
name|metricsName
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Histogram
name|histogram
init|=
name|registry
operator|.
name|histogram
argument_list|(
name|metricsName
argument_list|)
decl_stmt|;
name|Long
name|value
init|=
name|endpoint
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|Long
name|finalValue
init|=
name|getLongHeader
argument_list|(
name|in
argument_list|,
name|HEADER_HISTOGRAM_VALUE
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|finalValue
operator|!=
literal|null
condition|)
block|{
name|histogram
operator|.
name|update
argument_list|(
name|finalValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot update histogram \"{}\" with null value"
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

