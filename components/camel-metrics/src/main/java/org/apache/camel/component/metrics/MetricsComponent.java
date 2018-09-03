begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.metrics
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

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
name|Slf4jReporter
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
name|impl
operator|.
name|UriEndpointComponent
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
name|Registry
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
name|StringHelper
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

begin_comment
comment|/**  * Represents the component that manages metrics endpoints.  */
end_comment

begin_class
DECL|class|MetricsComponent
specifier|public
class|class
name|MetricsComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|METRIC_REGISTRY_NAME
specifier|public
specifier|static
specifier|final
name|String
name|METRIC_REGISTRY_NAME
init|=
literal|"metricRegistry"
decl_stmt|;
DECL|field|DEFAULT_METRICS_TYPE
specifier|public
specifier|static
specifier|final
name|MetricsType
name|DEFAULT_METRICS_TYPE
init|=
name|MetricsType
operator|.
name|METER
decl_stmt|;
DECL|field|DEFAULT_REPORTING_INTERVAL_SECONDS
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_REPORTING_INTERVAL_SECONDS
init|=
literal|60L
decl_stmt|;
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
name|MetricsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|metricRegistry
specifier|private
name|MetricRegistry
name|metricRegistry
decl_stmt|;
DECL|method|MetricsComponent ()
specifier|public
name|MetricsComponent
parameter_list|()
block|{
name|super
argument_list|(
name|MetricsEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|metricRegistry
operator|==
literal|null
condition|)
block|{
name|Registry
name|camelRegistry
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|metricRegistry
operator|=
name|getOrCreateMetricRegistry
argument_list|(
name|camelRegistry
argument_list|,
name|METRIC_REGISTRY_NAME
argument_list|)
expr_stmt|;
block|}
name|String
name|metricsName
init|=
name|getMetricsName
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|MetricsType
name|metricsType
init|=
name|getMetricsType
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Metrics type: {}; name: {}"
argument_list|,
name|metricsType
argument_list|,
name|metricsName
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
operator|new
name|MetricsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|metricRegistry
argument_list|,
name|metricsType
argument_list|,
name|metricsName
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|getMetricsName (String remaining)
name|String
name|getMetricsName
parameter_list|(
name|String
name|remaining
parameter_list|)
block|{
name|String
name|name
init|=
name|StringHelper
operator|.
name|after
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
return|return
name|name
operator|==
literal|null
condition|?
name|remaining
else|:
name|name
return|;
block|}
DECL|method|getMetricsType (String remaining)
name|MetricsType
name|getMetricsType
parameter_list|(
name|String
name|remaining
parameter_list|)
block|{
name|String
name|name
init|=
name|StringHelper
operator|.
name|before
argument_list|(
name|remaining
argument_list|,
literal|":"
argument_list|)
decl_stmt|;
name|MetricsType
name|type
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|DEFAULT_METRICS_TYPE
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
name|MetricsType
operator|.
name|getByName
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unknown metrics type \""
operator|+
name|name
operator|+
literal|"\""
argument_list|)
throw|;
block|}
return|return
name|type
return|;
block|}
DECL|method|getOrCreateMetricRegistry (Registry camelRegistry, String registryName)
name|MetricRegistry
name|getOrCreateMetricRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Looking up MetricRegistry from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|MetricRegistry
name|result
init|=
name|getMetricRegistryFromCamelRegistry
argument_list|(
name|camelRegistry
argument_list|,
name|registryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"MetricRegistry not found from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating new default MetricRegistry"
argument_list|)
expr_stmt|;
name|result
operator|=
name|createMetricRegistry
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getMetricRegistryFromCamelRegistry (Registry camelRegistry, String registryName)
name|MetricRegistry
name|getMetricRegistryFromCamelRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
name|MetricRegistry
name|registry
init|=
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
name|registryName
argument_list|,
name|MetricRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|!=
literal|null
condition|)
block|{
return|return
name|registry
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|MetricRegistry
argument_list|>
name|registries
init|=
name|camelRegistry
operator|.
name|findByType
argument_list|(
name|MetricRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registries
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|registries
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|createMetricRegistry ()
name|MetricRegistry
name|createMetricRegistry
parameter_list|()
block|{
name|MetricRegistry
name|registry
init|=
operator|new
name|MetricRegistry
argument_list|()
decl_stmt|;
specifier|final
name|Slf4jReporter
name|reporter
init|=
name|Slf4jReporter
operator|.
name|forRegistry
argument_list|(
name|registry
argument_list|)
operator|.
name|outputTo
argument_list|(
name|LOG
argument_list|)
operator|.
name|convertRatesTo
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|convertDurationsTo
argument_list|(
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|withLoggingLevel
argument_list|(
name|Slf4jReporter
operator|.
name|LoggingLevel
operator|.
name|DEBUG
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|reporter
operator|.
name|start
argument_list|(
name|DEFAULT_REPORTING_INTERVAL_SECONDS
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
comment|/**      * To use a custom configured MetricRegistry.      */
DECL|method|setMetricRegistry (MetricRegistry metricRegistry)
specifier|public
name|void
name|setMetricRegistry
parameter_list|(
name|MetricRegistry
name|metricRegistry
parameter_list|)
block|{
name|this
operator|.
name|metricRegistry
operator|=
name|metricRegistry
expr_stmt|;
block|}
block|}
end_class

end_unit

