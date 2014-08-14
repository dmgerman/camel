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
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|Counter
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
name|Meter
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
name|impl
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
comment|// TODO: allow to configure which counters/meters/timers to capture
comment|// TODO: allow to configur the reporer and jmx domain etc on MetricsRegistryService
comment|// TODO: RoutePolicyFactory to make this configurable once and apply automatic for all routes
comment|// TODO: allow to lookup and get hold of com.codahale.metrics.MetricRegistry from java api
DECL|field|registry
specifier|private
name|MetricsRegistryService
name|registry
decl_stmt|;
DECL|field|statistics
specifier|private
specifier|final
name|ConcurrentMap
argument_list|<
name|Route
argument_list|,
name|MetricsStatistics
argument_list|>
name|statistics
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|Route
argument_list|,
name|MetricsStatistics
argument_list|>
argument_list|()
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
DECL|field|total
specifier|private
name|Counter
name|total
decl_stmt|;
DECL|field|inflight
specifier|private
name|Counter
name|inflight
decl_stmt|;
DECL|field|requests
specifier|private
name|Meter
name|requests
decl_stmt|;
DECL|field|responses
specifier|private
name|Timer
name|responses
decl_stmt|;
DECL|method|MetricsStatistics (Counter total, Counter inflight, Meter requests, Timer responses)
specifier|private
name|MetricsStatistics
parameter_list|(
name|Counter
name|total
parameter_list|,
name|Counter
name|inflight
parameter_list|,
name|Meter
name|requests
parameter_list|,
name|Timer
name|responses
parameter_list|)
block|{
name|this
operator|.
name|total
operator|=
name|total
expr_stmt|;
name|this
operator|.
name|inflight
operator|=
name|inflight
expr_stmt|;
name|this
operator|.
name|requests
operator|=
name|requests
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
name|total
operator|.
name|inc
argument_list|()
expr_stmt|;
name|inflight
operator|.
name|inc
argument_list|()
expr_stmt|;
name|requests
operator|.
name|mark
argument_list|()
expr_stmt|;
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
literal|"MetricsRoutePolicy"
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
name|inflight
operator|.
name|dec
argument_list|()
expr_stmt|;
name|Timer
operator|.
name|Context
name|context
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"MetricsRoutePolicy"
argument_list|,
name|Timer
operator|.
name|Context
operator|.
name|class
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
name|registry
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
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
operator|new
name|MetricsRegistryService
argument_list|()
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
name|registry
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
name|MetricsStatistics
name|stats
init|=
name|statistics
operator|.
name|get
argument_list|(
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|==
literal|null
condition|)
block|{
name|Counter
name|total
init|=
name|registry
operator|.
name|getRegistry
argument_list|()
operator|.
name|counter
argument_list|(
name|createName
argument_list|(
literal|"total"
argument_list|)
argument_list|)
decl_stmt|;
name|Counter
name|inflight
init|=
name|registry
operator|.
name|getRegistry
argument_list|()
operator|.
name|counter
argument_list|(
name|createName
argument_list|(
literal|"inflight"
argument_list|)
argument_list|)
decl_stmt|;
name|Meter
name|requests
init|=
name|registry
operator|.
name|getRegistry
argument_list|()
operator|.
name|meter
argument_list|(
name|createName
argument_list|(
literal|"requests"
argument_list|)
argument_list|)
decl_stmt|;
name|Timer
name|responses
init|=
name|registry
operator|.
name|getRegistry
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
name|stats
operator|=
operator|new
name|MetricsStatistics
argument_list|(
name|total
argument_list|,
name|inflight
argument_list|,
name|requests
argument_list|,
name|responses
argument_list|)
expr_stmt|;
name|statistics
operator|.
name|putIfAbsent
argument_list|(
name|route
argument_list|,
name|stats
argument_list|)
expr_stmt|;
block|}
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
return|return
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
operator|+
literal|"-"
operator|+
name|route
operator|.
name|getId
argument_list|()
operator|+
literal|"-"
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
name|MetricsStatistics
name|stats
init|=
name|statistics
operator|.
name|get
argument_list|(
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|!=
literal|null
condition|)
block|{
name|stats
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
name|MetricsStatistics
name|stats
init|=
name|statistics
operator|.
name|get
argument_list|(
name|route
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|!=
literal|null
condition|)
block|{
name|stats
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

