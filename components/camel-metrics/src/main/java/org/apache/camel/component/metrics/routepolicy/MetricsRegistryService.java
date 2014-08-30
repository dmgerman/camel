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
name|javax
operator|.
name|management
operator|.
name|MBeanServer
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
name|JmxReporter
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
name|json
operator|.
name|MetricsModule
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|JsonProcessingException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectWriter
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
name|CamelContextAware
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
name|StaticService
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|ManagementAgent
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Service holding the {@link MetricRegistry} which registers all metrics.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"MetricsRegistry"
argument_list|)
DECL|class|MetricsRegistryService
specifier|public
specifier|final
class|class
name|MetricsRegistryService
extends|extends
name|ServiceSupport
implements|implements
name|CamelContextAware
implements|,
name|StaticService
implements|,
name|MetricsRegistryMBean
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|metricsRegistry
specifier|private
name|MetricRegistry
name|metricsRegistry
decl_stmt|;
DECL|field|reporter
specifier|private
name|JmxReporter
name|reporter
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
DECL|field|mapper
specifier|private
specifier|transient
name|ObjectMapper
name|mapper
decl_stmt|;
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
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|metricsRegistry
operator|==
literal|null
condition|)
block|{
name|metricsRegistry
operator|=
operator|new
name|MetricRegistry
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|useJmx
condition|)
block|{
name|ManagementAgent
name|agent
init|=
name|getCamelContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
decl_stmt|;
if|if
condition|(
name|agent
operator|!=
literal|null
condition|)
block|{
name|MBeanServer
name|server
init|=
name|agent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|reporter
operator|=
name|JmxReporter
operator|.
name|forRegistry
argument_list|(
name|metricsRegistry
argument_list|)
operator|.
name|registerWith
argument_list|(
name|server
argument_list|)
operator|.
name|inDomain
argument_list|(
name|jmxDomain
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|reporter
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext has not enabled JMX"
argument_list|)
throw|;
block|}
block|}
comment|// json mapper
name|this
operator|.
name|mapper
operator|=
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|registerModule
argument_list|(
operator|new
name|MetricsModule
argument_list|(
name|getRateUnit
argument_list|()
argument_list|,
name|getDurationUnit
argument_list|()
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|reporter
operator|!=
literal|null
condition|)
block|{
name|reporter
operator|.
name|stop
argument_list|()
expr_stmt|;
name|reporter
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|dumpStatisticsAsJson ()
specifier|public
name|String
name|dumpStatisticsAsJson
parameter_list|()
block|{
name|ObjectWriter
name|writer
init|=
name|mapper
operator|.
name|writer
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPrettyPrint
argument_list|()
condition|)
block|{
name|writer
operator|=
name|writer
operator|.
name|withDefaultPrettyPrinter
argument_list|()
expr_stmt|;
block|}
try|try
block|{
return|return
name|writer
operator|.
name|writeValueAsString
argument_list|(
name|getMetricsRegistry
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|JsonProcessingException
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
block|}
block|}
end_class

end_unit

