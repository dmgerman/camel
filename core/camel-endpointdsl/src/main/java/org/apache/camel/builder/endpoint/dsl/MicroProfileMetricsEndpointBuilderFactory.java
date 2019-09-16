begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * Camel metrics exposed with Eclipse MicroProfile Metrics  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|MicroProfileMetricsEndpointBuilderFactory
specifier|public
interface|interface
name|MicroProfileMetricsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the MicroProfile Metrics component.      */
DECL|interface|MicroProfileMetricsEndpointBuilder
specifier|public
interface|interface
name|MicroProfileMetricsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedMicroProfileMetricsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedMicroProfileMetricsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The action to use when using the Timer metric type.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|action (String action)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|action
parameter_list|(
name|String
name|action
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"action"
argument_list|,
name|action
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The amount to increment to use when using the Counter metric type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|counterIncrement ( Long counterIncrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|counterIncrement
parameter_list|(
name|Long
name|counterIncrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"counterIncrement"
argument_list|,
name|counterIncrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The amount to increment to use when using the Counter metric type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|counterIncrement ( String counterIncrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|counterIncrement
parameter_list|(
name|String
name|counterIncrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"counterIncrement"
argument_list|,
name|counterIncrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a description within the metric metadata.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|description ( String description)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|description
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a display name within the metric metadata.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|displayName ( String displayName)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|displayName
parameter_list|(
name|String
name|displayName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"displayName"
argument_list|,
name|displayName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Decrements a gauge value when using the ConcurrentGauge metric type.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|gaugeDecrement ( Boolean gaugeDecrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeDecrement
parameter_list|(
name|Boolean
name|gaugeDecrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeDecrement"
argument_list|,
name|gaugeDecrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Decrements a gauge value when using the ConcurrentGauge metric type.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|gaugeDecrement ( String gaugeDecrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeDecrement
parameter_list|(
name|String
name|gaugeDecrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeDecrement"
argument_list|,
name|gaugeDecrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Increments a gauge value when using the ConcurrentGauge metric type.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|gaugeIncrement ( Boolean gaugeIncrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeIncrement
parameter_list|(
name|Boolean
name|gaugeIncrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeIncrement"
argument_list|,
name|gaugeIncrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Increments a gauge value when using the ConcurrentGauge metric type.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|gaugeIncrement ( String gaugeIncrement)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeIncrement
parameter_list|(
name|String
name|gaugeIncrement
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeIncrement"
argument_list|,
name|gaugeIncrement
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the gauge value when using the Gauge metric type.          *           * The option is a:<code>java.lang.Number</code> type.          *           * Group: producer          */
DECL|method|gaugeValue (Number gaugeValue)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeValue
parameter_list|(
name|Number
name|gaugeValue
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeValue"
argument_list|,
name|gaugeValue
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the gauge value when using the Gauge metric type.          *           * The option will be converted to a<code>java.lang.Number</code> type.          *           * Group: producer          */
DECL|method|gaugeValue (String gaugeValue)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|gaugeValue
parameter_list|(
name|String
name|gaugeValue
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"gaugeValue"
argument_list|,
name|gaugeValue
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The mark value to set when using the Meter metric type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|mark (Long mark)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|mark
parameter_list|(
name|Long
name|mark
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mark"
argument_list|,
name|mark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The mark value to set when using the Meter metric type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|mark (String mark)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|mark
parameter_list|(
name|String
name|mark
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"mark"
argument_list|,
name|mark
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets a metric unit within the metric metadata.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|metricUnit (String metricUnit)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|metricUnit
parameter_list|(
name|String
name|metricUnit
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"metricUnit"
argument_list|,
name|metricUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Comma delimited list of tags associated with the metric in the format          * tagName=tagValue.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|tags (String tags)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|tags
parameter_list|(
name|String
name|tags
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"tags"
argument_list|,
name|tags
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The value to set when using the Histogram metric type.          *           * The option is a:<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|value (Long value)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|value
parameter_list|(
name|Long
name|value
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The value to set when using the Histogram metric type.          *           * The option will be converted to a<code>java.lang.Long</code> type.          *           * Group: producer          */
DECL|method|value (String value)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|value
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"value"
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the MicroProfile Metrics component.      */
DECL|interface|AdvancedMicroProfileMetricsEndpointBuilder
specifier|public
interface|interface
name|AdvancedMicroProfileMetricsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|MicroProfileMetricsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedMicroProfileMetricsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedMicroProfileMetricsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedMicroProfileMetricsEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedMicroProfileMetricsEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * MicroProfile Metrics (camel-microprofile-metrics)      * Camel metrics exposed with Eclipse MicroProfile Metrics      *       * Category: monitoring      * Available as of version: 3.0      * Maven coordinates: org.apache.camel:camel-microprofile-metrics      *       * Syntax:<code>microprofile-metrics:metricType:metricName</code>      *       * Path parameter: metricType (required)      * Metric type      * The value can be one of: CONCURRENT_GAUGE, COUNTER, GAUGE, METERED,      * HISTOGRAM, TIMER, INVALID      *       * Path parameter: metricName (required)      * Metric name      */
DECL|method|microprofileMetrics (String path)
specifier|default
name|MicroProfileMetricsEndpointBuilder
name|microprofileMetrics
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|MicroProfileMetricsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|MicroProfileMetricsEndpointBuilder
implements|,
name|AdvancedMicroProfileMetricsEndpointBuilder
block|{
specifier|public
name|MicroProfileMetricsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"microprofile-metrics"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|MicroProfileMetricsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

