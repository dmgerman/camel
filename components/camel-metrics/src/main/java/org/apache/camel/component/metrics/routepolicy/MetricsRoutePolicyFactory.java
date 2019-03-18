begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics.routepolicy
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
name|routepolicy
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
name|TimeUnit
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
name|NamedNode
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
name|RoutePolicy
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
name|RoutePolicyFactory
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.RoutePolicyFactory} to plugin and use metrics for gathering route utilization statistics  */
end_comment

begin_class
DECL|class|MetricsRoutePolicyFactory
specifier|public
class|class
name|MetricsRoutePolicyFactory
implements|implements
name|RoutePolicyFactory
block|{
DECL|field|metricsRegistry
specifier|private
name|MetricRegistry
name|metricsRegistry
decl_stmt|;
DECL|field|useJmx
specifier|private
name|boolean
name|useJmx
decl_stmt|;
DECL|field|jmxDomain
specifier|private
name|String
name|jmxDomain
init|=
literal|"org.apache.camel.metrics"
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
decl_stmt|;
DECL|field|rateUnit
specifier|private
name|TimeUnit
name|rateUnit
init|=
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
DECL|field|durationUnit
specifier|private
name|TimeUnit
name|durationUnit
init|=
name|TimeUnit
operator|.
name|MILLISECONDS
decl_stmt|;
comment|/**      * To use a specific {@link com.codahale.metrics.MetricRegistry} instance.      *<p/>      * If no instance has been configured, then Camel will create a shared instance to be used.      */
DECL|method|setMetricsRegistry (MetricRegistry metricsRegistry)
specifier|public
name|void
name|setMetricsRegistry
parameter_list|(
name|MetricRegistry
name|metricsRegistry
parameter_list|)
block|{
name|this
operator|.
name|metricsRegistry
operator|=
name|metricsRegistry
expr_stmt|;
block|}
DECL|method|getMetricsRegistry ()
specifier|public
name|MetricRegistry
name|getMetricsRegistry
parameter_list|()
block|{
return|return
name|metricsRegistry
return|;
block|}
DECL|method|isUseJmx ()
specifier|public
name|boolean
name|isUseJmx
parameter_list|()
block|{
return|return
name|useJmx
return|;
block|}
comment|/**      * Whether to use JMX reported to enlist JMX MBeans with the metrics statistics.      */
DECL|method|setUseJmx (boolean useJmx)
specifier|public
name|void
name|setUseJmx
parameter_list|(
name|boolean
name|useJmx
parameter_list|)
block|{
name|this
operator|.
name|useJmx
operator|=
name|useJmx
expr_stmt|;
block|}
DECL|method|getJmxDomain ()
specifier|public
name|String
name|getJmxDomain
parameter_list|()
block|{
return|return
name|jmxDomain
return|;
block|}
comment|/**      * The JMX domain name to use for the enlisted JMX MBeans.      */
DECL|method|setJmxDomain (String jmxDomain)
specifier|public
name|void
name|setJmxDomain
parameter_list|(
name|String
name|jmxDomain
parameter_list|)
block|{
name|this
operator|.
name|jmxDomain
operator|=
name|jmxDomain
expr_stmt|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
comment|/**      * Whether to use pretty print when outputting JSon      */
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|getRateUnit ()
specifier|public
name|TimeUnit
name|getRateUnit
parameter_list|()
block|{
return|return
name|rateUnit
return|;
block|}
comment|/**      * Sets the time unit to use for requests per unit (eg requests per second)      */
DECL|method|setRateUnit (TimeUnit rateUnit)
specifier|public
name|void
name|setRateUnit
parameter_list|(
name|TimeUnit
name|rateUnit
parameter_list|)
block|{
name|this
operator|.
name|rateUnit
operator|=
name|rateUnit
expr_stmt|;
block|}
DECL|method|getDurationUnit ()
specifier|public
name|TimeUnit
name|getDurationUnit
parameter_list|()
block|{
return|return
name|durationUnit
return|;
block|}
comment|/**      * Sets the time unit to use for timing the duration of processing a message in the route      */
DECL|method|setDurationUnit (TimeUnit durationUnit)
specifier|public
name|void
name|setDurationUnit
parameter_list|(
name|TimeUnit
name|durationUnit
parameter_list|)
block|{
name|this
operator|.
name|durationUnit
operator|=
name|durationUnit
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, NamedNode routeDefinition)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|NamedNode
name|routeDefinition
parameter_list|)
block|{
name|MetricsRoutePolicy
name|answer
init|=
operator|new
name|MetricsRoutePolicy
argument_list|()
decl_stmt|;
name|answer
operator|.
name|setMetricsRegistry
argument_list|(
name|getMetricsRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setUseJmx
argument_list|(
name|isUseJmx
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setJmxDomain
argument_list|(
name|getJmxDomain
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setPrettyPrint
argument_list|(
name|isPrettyPrint
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setRateUnit
argument_list|(
name|getRateUnit
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setDurationUnit
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

