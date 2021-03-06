begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer.routepolicy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
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
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Tags
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
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
name|NonManagedService
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
name|RuntimeCamelException
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
name|micrometer
operator|.
name|MicrometerUtils
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
name|support
operator|.
name|service
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
name|ObjectHelper
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|METRICS_REGISTRY_NAME
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
name|micrometer
operator|.
name|MicrometerConstants
operator|.
name|SERVICE_NAME
import|;
end_import

begin_comment
comment|/**  * A {@link org.apache.camel.spi.RoutePolicy} which gathers statistics and reports them using {@link MeterRegistry}.  *<p/>  * The metrics is reported in JMX by default, but this can be configured.  */
end_comment

begin_class
DECL|class|MicrometerRoutePolicy
specifier|public
class|class
name|MicrometerRoutePolicy
extends|extends
name|RoutePolicySupport
implements|implements
name|NonManagedService
block|{
DECL|field|meterRegistry
specifier|private
name|MeterRegistry
name|meterRegistry
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
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
DECL|field|namingStrategy
specifier|private
name|MicrometerRoutePolicyNamingStrategy
name|namingStrategy
init|=
name|MicrometerRoutePolicyNamingStrategy
operator|.
name|DEFAULT
decl_stmt|;
DECL|class|MetricsStatistics
specifier|private
specifier|static
specifier|final
class|class
name|MetricsStatistics
block|{
DECL|field|meterRegistry
specifier|private
specifier|final
name|MeterRegistry
name|meterRegistry
decl_stmt|;
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|namingStrategy
specifier|private
specifier|final
name|MicrometerRoutePolicyNamingStrategy
name|namingStrategy
decl_stmt|;
DECL|method|MetricsStatistics (MeterRegistry meterRegistry, Route route, MicrometerRoutePolicyNamingStrategy namingStrategy)
specifier|private
name|MetricsStatistics
parameter_list|(
name|MeterRegistry
name|meterRegistry
parameter_list|,
name|Route
name|route
parameter_list|,
name|MicrometerRoutePolicyNamingStrategy
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|meterRegistry
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|meterRegistry
argument_list|,
literal|"MeterRegistry"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|namingStrategy
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|namingStrategy
argument_list|,
literal|"MicrometerRoutePolicyNamingStrategy"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
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
name|Sample
name|sample
init|=
name|Timer
operator|.
name|start
argument_list|(
name|meterRegistry
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|propertyName
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|sample
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
name|Sample
name|sample
init|=
operator|(
name|Timer
operator|.
name|Sample
operator|)
name|exchange
operator|.
name|removeProperty
argument_list|(
name|propertyName
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|sample
operator|!=
literal|null
condition|)
block|{
name|Timer
name|timer
init|=
name|Timer
operator|.
name|builder
argument_list|(
name|namingStrategy
operator|.
name|getName
argument_list|(
name|route
argument_list|)
argument_list|)
operator|.
name|tags
argument_list|(
name|namingStrategy
operator|.
name|getTags
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|)
argument_list|)
operator|.
name|description
argument_list|(
name|route
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|.
name|register
argument_list|(
name|meterRegistry
argument_list|)
decl_stmt|;
name|sample
operator|.
name|stop
argument_list|(
name|timer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|propertyName (Exchange exchange)
specifier|private
name|String
name|propertyName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%s-%s-%s"
argument_list|,
name|DEFAULT_CAMEL_ROUTE_POLICY_METER_NAME
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getMeterRegistry ()
specifier|public
name|MeterRegistry
name|getMeterRegistry
parameter_list|()
block|{
return|return
name|meterRegistry
return|;
block|}
DECL|method|setMeterRegistry (MeterRegistry meterRegistry)
specifier|public
name|void
name|setMeterRegistry
parameter_list|(
name|MeterRegistry
name|meterRegistry
parameter_list|)
block|{
name|this
operator|.
name|meterRegistry
operator|=
name|meterRegistry
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
DECL|method|getNamingStrategy ()
specifier|public
name|MicrometerRoutePolicyNamingStrategy
name|getNamingStrategy
parameter_list|()
block|{
return|return
name|namingStrategy
return|;
block|}
DECL|method|setNamingStrategy (MicrometerRoutePolicyNamingStrategy namingStrategy)
specifier|public
name|void
name|setNamingStrategy
parameter_list|(
name|MicrometerRoutePolicyNamingStrategy
name|namingStrategy
parameter_list|)
block|{
name|this
operator|.
name|namingStrategy
operator|=
name|namingStrategy
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
if|if
condition|(
name|getMeterRegistry
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setMeterRegistry
argument_list|(
name|MicrometerUtils
operator|.
name|getOrCreateMeterRegistry
argument_list|(
name|route
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
argument_list|,
name|METRICS_REGISTRY_NAME
argument_list|)
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|MicrometerRoutePolicyService
name|registryService
init|=
name|route
operator|.
name|getCamelContext
argument_list|()
operator|.
name|hasService
argument_list|(
name|MicrometerRoutePolicyService
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|MicrometerRoutePolicyService
argument_list|()
expr_stmt|;
name|registryService
operator|.
name|setMeterRegistry
argument_list|(
name|getMeterRegistry
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
name|setDurationUnit
argument_list|(
name|getDurationUnit
argument_list|()
argument_list|)
expr_stmt|;
name|registryService
operator|.
name|setMatchingTags
argument_list|(
name|Tags
operator|.
name|of
argument_list|(
name|SERVICE_NAME
argument_list|,
name|MicrometerRoutePolicyService
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|route
operator|.
name|getCamelContext
argument_list|()
operator|.
name|addService
argument_list|(
name|registryService
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// create statistics holder
comment|// for now we record only all the timings of a complete exchange (responses)
comment|// we have in-flight / total statistics already from camel-core
name|statistics
operator|=
operator|new
name|MetricsStatistics
argument_list|(
name|getMeterRegistry
argument_list|()
argument_list|,
name|route
argument_list|,
name|getNamingStrategy
argument_list|()
argument_list|)
expr_stmt|;
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

