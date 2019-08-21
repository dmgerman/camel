begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.microprofile.metrics
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|microprofile
operator|.
name|metrics
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Component
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|DefaultEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
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
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|MetricType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|metrics
operator|.
name|Tag
import|;
end_import

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"3.0.0"
argument_list|,
name|scheme
operator|=
literal|"microprofile-metrics"
argument_list|,
name|title
operator|=
literal|"MicroProfile Metrics"
argument_list|,
name|syntax
operator|=
literal|"microprofile-metrics:metricsType:metricsName"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"monitoring"
argument_list|)
DECL|class|MicroProfileMetricsEndpoint
specifier|public
class|class
name|MicroProfileMetricsEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|metricRegistry
specifier|protected
specifier|final
name|MetricRegistry
name|metricRegistry
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Metric type"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|enums
operator|=
literal|"concurrent gauge,counter,histogram,meter,timer"
argument_list|)
DECL|field|metricType
specifier|private
specifier|final
name|MetricType
name|metricType
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Metric name"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|metricName
specifier|private
specifier|final
name|String
name|metricName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Comma delimited list of tags associated with the metric in the format tagName=tagValue"
argument_list|)
DECL|field|tags
specifier|private
specifier|final
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Action to use when using the timer type"
argument_list|)
DECL|field|action
specifier|private
name|String
name|action
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Mark value to set when using the meter type"
argument_list|)
DECL|field|mark
specifier|private
name|Long
name|mark
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Value to set when using the histogram type"
argument_list|)
DECL|field|value
specifier|private
name|Long
name|value
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Increment value when using the counter type"
argument_list|)
DECL|field|counterIncrement
specifier|private
name|Long
name|counterIncrement
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Increment metric value when using the concurrent gauge type"
argument_list|)
DECL|field|gaugeIncrement
specifier|private
name|Boolean
name|gaugeIncrement
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Decrement metric value when using concurrent gauge type"
argument_list|)
DECL|field|gaugeDecrement
specifier|private
name|Boolean
name|gaugeDecrement
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Metric description"
argument_list|)
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Metric display name"
argument_list|)
DECL|field|displayName
specifier|private
name|String
name|displayName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Metric unit. See org.eclipse.microprofile.metrics.MetricUnits"
argument_list|)
DECL|field|metricUnit
specifier|private
name|String
name|metricUnit
decl_stmt|;
DECL|method|MicroProfileMetricsEndpoint (String uri, Component component, MetricRegistry metricRegistry, MetricType metricType, String metricsName, List<Tag> tags)
specifier|public
name|MicroProfileMetricsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|MetricRegistry
name|metricRegistry
parameter_list|,
name|MetricType
name|metricType
parameter_list|,
name|String
name|metricsName
parameter_list|,
name|List
argument_list|<
name|Tag
argument_list|>
name|tags
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|metricRegistry
operator|=
name|metricRegistry
expr_stmt|;
name|this
operator|.
name|metricType
operator|=
name|metricType
expr_stmt|;
name|this
operator|.
name|metricName
operator|=
name|metricsName
expr_stmt|;
name|this
operator|.
name|tags
operator|=
name|tags
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|COUNTER
argument_list|)
condition|)
block|{
return|return
operator|new
name|MicroProfileMetricsCounterProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|GAUGE
argument_list|)
operator|||
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|CONCURRENT_GAUGE
argument_list|)
condition|)
block|{
return|return
operator|new
name|MicroProfileMetricsGaugeProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|HISTOGRAM
argument_list|)
condition|)
block|{
return|return
operator|new
name|MicroProfileMetricsHistogramProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|METERED
argument_list|)
condition|)
block|{
return|return
operator|new
name|MicroProfileMetricsMeteredProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|metricType
operator|.
name|equals
argument_list|(
name|MetricType
operator|.
name|TIMER
argument_list|)
condition|)
block|{
return|return
operator|new
name|MicroProfileMetricsTimerProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unknown metric type "
operator|+
name|metricType
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The microprofile-metrics endpoint does not support consumers"
argument_list|)
throw|;
block|}
DECL|method|getMetricRegistry ()
specifier|public
name|MetricRegistry
name|getMetricRegistry
parameter_list|()
block|{
return|return
name|metricRegistry
return|;
block|}
DECL|method|getMetricType ()
specifier|public
name|MetricType
name|getMetricType
parameter_list|()
block|{
return|return
name|metricType
return|;
block|}
DECL|method|getMetricName ()
specifier|public
name|String
name|getMetricName
parameter_list|()
block|{
return|return
name|metricName
return|;
block|}
DECL|method|getTags ()
specifier|public
name|List
argument_list|<
name|Tag
argument_list|>
name|getTags
parameter_list|()
block|{
return|return
name|tags
return|;
block|}
DECL|method|getAction ()
specifier|public
name|String
name|getAction
parameter_list|()
block|{
return|return
name|action
return|;
block|}
comment|/**      * The action to use when using the Timer metric type      */
DECL|method|setAction (String action)
specifier|public
name|void
name|setAction
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|this
operator|.
name|action
operator|=
name|action
expr_stmt|;
block|}
DECL|method|getMark ()
specifier|public
name|Long
name|getMark
parameter_list|()
block|{
return|return
name|mark
return|;
block|}
comment|/**      * The mark value to set when using the Meter metric type      */
DECL|method|setMark (Long mark)
specifier|public
name|void
name|setMark
parameter_list|(
name|Long
name|mark
parameter_list|)
block|{
name|this
operator|.
name|mark
operator|=
name|mark
expr_stmt|;
block|}
DECL|method|getValue ()
specifier|public
name|Long
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
comment|/**      * The value to set when using the Histogram metric type      */
DECL|method|setValue (Long value)
specifier|public
name|void
name|setValue
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|getCounterIncrement ()
specifier|public
name|Long
name|getCounterIncrement
parameter_list|()
block|{
return|return
name|counterIncrement
return|;
block|}
comment|/**      * The amount to increment to use when using the Counter metric type      */
DECL|method|setCounterIncrement (Long counterIncrement)
specifier|public
name|void
name|setCounterIncrement
parameter_list|(
name|Long
name|counterIncrement
parameter_list|)
block|{
name|this
operator|.
name|counterIncrement
operator|=
name|counterIncrement
expr_stmt|;
block|}
DECL|method|getGaugeIncrement ()
specifier|public
name|Boolean
name|getGaugeIncrement
parameter_list|()
block|{
return|return
name|gaugeIncrement
return|;
block|}
comment|/**      * Increments a counter when using the ConcurrentGauge metric type      */
DECL|method|setGaugeIncrement (Boolean gaugeIncrement)
specifier|public
name|void
name|setGaugeIncrement
parameter_list|(
name|Boolean
name|gaugeIncrement
parameter_list|)
block|{
name|this
operator|.
name|gaugeIncrement
operator|=
name|gaugeIncrement
expr_stmt|;
block|}
DECL|method|getGaugeDecrement ()
specifier|public
name|Boolean
name|getGaugeDecrement
parameter_list|()
block|{
return|return
name|gaugeDecrement
return|;
block|}
comment|/**      * Decrements a counter when using the ConcurrentGauge metric type      */
DECL|method|setGaugeDecrement (Boolean gaugeDecrement)
specifier|public
name|void
name|setGaugeDecrement
parameter_list|(
name|Boolean
name|gaugeDecrement
parameter_list|)
block|{
name|this
operator|.
name|gaugeDecrement
operator|=
name|gaugeDecrement
expr_stmt|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
comment|/**      * Sets a description within the metric metadata      */
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
comment|/**      * Sets a display name within the metric metadata      */
DECL|method|getDisplayName ()
specifier|public
name|String
name|getDisplayName
parameter_list|()
block|{
return|return
name|displayName
return|;
block|}
DECL|method|setDisplayName (String displayName)
specifier|public
name|void
name|setDisplayName
parameter_list|(
name|String
name|displayName
parameter_list|)
block|{
name|this
operator|.
name|displayName
operator|=
name|displayName
expr_stmt|;
block|}
DECL|method|getMetricUnit ()
specifier|public
name|String
name|getMetricUnit
parameter_list|()
block|{
return|return
name|metricUnit
return|;
block|}
comment|/**      * Sets a metric unit within the metric metadata      */
DECL|method|setMetricUnit (String metricUnit)
specifier|public
name|void
name|setMetricUnit
parameter_list|(
name|String
name|metricUnit
parameter_list|)
block|{
name|this
operator|.
name|metricUnit
operator|=
name|metricUnit
expr_stmt|;
block|}
block|}
end_class

end_unit

