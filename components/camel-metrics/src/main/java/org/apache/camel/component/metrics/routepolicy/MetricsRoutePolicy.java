begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|codahale
operator|.
name|metrics
operator|.
name|Timer
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
name|Route
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
name|RoutePolicySupport
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.RoutePolicy} which gathers statistics and reports them using {@link com.codahale.metrics.MetricRegistry}.  *<p/>  * The metrics is reported in JMX by default, but this can be configured.  */
end_comment

begin_class
DECL|class|MetricsRoutePolicy
specifier|public
class|class
name|MetricsRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|field|metricsRegistry
specifier|private
name|MetricRegistry
name|metricsRegistry
decl_stmt|;
DECL|field|registryService
specifier|private
name|MetricsRegistryService
name|registryService
decl_stmt|;
DECL|field|useJmx
specifier|private
name|boolean
name|useJmx
init|=
literal|true
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
DECL|field|statistics
specifier|private
name|MetricsStatistics
name|statistics
decl_stmt|;
DECL|field|route
specifier|private
name|Route
name|route
decl_stmt|;
DECL|class|MetricsStatistics
specifier|private
specifier|static
specifier|final
class|class
name|MetricsStatistics
block|{
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|responses
specifier|private
specifier|final
name|Timer
name|responses
decl_stmt|;
DECL|method|MetricsStatistics (Route route, Timer responses)
specifier|private
name|MetricsStatistics
parameter_list|(
name|Route
name|route
parameter_list|,
name|Timer
name|responses
parameter_list|)
block|{
name|this
operator|.
name|routeId
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|responses
operator|=
name|responses
expr_stmt|;
block|}
DECL|method|onExchangeBegin (Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Timer
operator|.
name|Context
name|context
init|=
name|responses
operator|.
name|time
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"MetricsRoutePolicy-"
operator|+
name|routeId
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
DECL|method|onExchangeDone (Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Timer
operator|.
name|Context
name|context
init|=
operator|(
name|Timer
operator|.
name|Context
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
literal|"MetricsRoutePolicy-"
operator|+
name|routeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
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
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|super
operator|.
name|onInit
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
try|try
block|{
name|registryService
operator|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|hasService
argument_list|(
name|MetricsRegistryService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|registryService
operator|==
literal|null
condition|)
block|{
name|registryService
operator|=
operator|new
name|MetricsRegistryService
argument_list|()
expr_stmt|;
name|registryService
operator|.
name|setMetricsRegistry
argument_list|(
name|getMetricsRegistry
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setUseJmx
argument_list|(
name|isUseJmx
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setJmxDomain
argument_list|(
name|getJmxDomain
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setPrettyPrint
argument_list|(
name|isPrettyPrint
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setRateUnit
argument_list|(
name|getRateUnit
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setDurationUnit
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|)
expr_stmt|;
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|registryService
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
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// create statistics holder
comment|// for know we record only all the timings of a complete exchange (responses)
comment|// we have in-flight / total statistics already from camel-core
name|Timer
name|responses
init|=
name|registryService
operator|.
name|getMetricsRegistry
argument_list|()
operator|.
name|timer
argument_list|(
name|createName
argument_list|(
literal|"responses"
argument_list|)
argument_list|)
decl_stmt|;
name|statistics
operator|=
operator|new
name|MetricsStatistics
argument_list|(
name|route
argument_list|,
name|responses
argument_list|)
expr_stmt|;
block|}
DECL|method|createName (String type)
specifier|private
name|String
name|createName
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|context
operator|.
name|getManagementName
argument_list|()
operator|!=
literal|null
condition|?
name|context
operator|.
name|getManagementName
argument_list|()
else|:
name|context
operator|.
name|getName
argument_list|()
decl_stmt|;
comment|// use colon to separate context from route, and dot for the type name
return|return
name|name
operator|+
literal|":"
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|"."
operator|+
name|type
return|;
block|}
annotation|@
name|Override
DECL|method|onExchangeBegin (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|onExchangeBegin
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|statistics
operator|!=
literal|null
condition|)
block|{
name|statistics
operator|.
name|onExchangeDone
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

