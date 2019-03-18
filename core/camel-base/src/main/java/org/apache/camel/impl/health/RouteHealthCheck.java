begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.health
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|health
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ServiceStatus
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
name|ManagedCamelContext
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
name|mbean
operator|.
name|ManagedRouteMBean
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
name|health
operator|.
name|HealthCheckResultBuilder
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

begin_class
DECL|class|RouteHealthCheck
specifier|public
class|class
name|RouteHealthCheck
extends|extends
name|AbstractHealthCheck
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RouteHealthCheck
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|evaluators
specifier|private
specifier|final
name|List
argument_list|<
name|PerformanceCounterEvaluator
argument_list|<
name|ManagedRouteMBean
argument_list|>
argument_list|>
name|evaluators
decl_stmt|;
DECL|method|RouteHealthCheck (Route route)
specifier|public
name|RouteHealthCheck
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|this
argument_list|(
name|route
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|RouteHealthCheck (Route route, Collection<PerformanceCounterEvaluator<ManagedRouteMBean>> evaluators)
specifier|public
name|RouteHealthCheck
parameter_list|(
name|Route
name|route
parameter_list|,
name|Collection
argument_list|<
name|PerformanceCounterEvaluator
argument_list|<
name|ManagedRouteMBean
argument_list|>
argument_list|>
name|evaluators
parameter_list|)
block|{
name|super
argument_list|(
literal|"camel"
argument_list|,
literal|"route:"
operator|+
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|evaluators
argument_list|)
condition|)
block|{
name|this
operator|.
name|evaluators
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|evaluators
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|evaluators
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doCall (HealthCheckResultBuilder builder, Map<String, Object> options)
specifier|protected
name|void
name|doCall
parameter_list|(
name|HealthCheckResultBuilder
name|builder
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
if|if
condition|(
name|route
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
specifier|final
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
specifier|final
name|ServiceStatus
name|status
init|=
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|getRouteStatus
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"route.id"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"route.status"
argument_list|,
name|status
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|detail
argument_list|(
literal|"route.context.name"
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRouteController
argument_list|()
operator|!=
literal|null
operator|||
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|isAutoStartup
argument_list|()
condition|)
block|{
if|if
condition|(
name|status
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|builder
operator|.
name|up
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|status
operator|.
name|isStopped
argument_list|()
condition|)
block|{
name|builder
operator|.
name|down
argument_list|()
expr_stmt|;
name|builder
operator|.
name|message
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Route %s has status %s"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|status
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Route {} marked as UP (controlled={}, auto-startup={})"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRouteController
argument_list|()
operator|!=
literal|null
argument_list|,
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|isAutoStartup
argument_list|()
argument_list|)
expr_stmt|;
comment|// Assuming that if no route controller is configured or if a
comment|// route is configured to not to automatically start, then the
comment|// route is always up as it is externally managed.
name|builder
operator|.
name|up
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|builder
operator|.
name|state
argument_list|()
operator|!=
name|State
operator|.
name|DOWN
condition|)
block|{
comment|// If JMX is enabled, use the Managed MBeans to determine route
comment|// health based on performance counters.
name|ManagedRouteMBean
name|managedRoute
init|=
name|context
operator|.
name|getExtension
argument_list|(
name|ManagedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getManagedRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedRoute
operator|!=
literal|null
operator|&&
operator|!
name|evaluators
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|PerformanceCounterEvaluator
argument_list|<
name|ManagedRouteMBean
argument_list|>
name|evaluator
range|:
name|evaluators
control|)
block|{
name|evaluator
operator|.
name|test
argument_list|(
name|managedRoute
argument_list|,
name|builder
argument_list|,
name|options
argument_list|)
expr_stmt|;
if|if
condition|(
name|builder
operator|.
name|state
argument_list|()
operator|==
name|State
operator|.
name|DOWN
condition|)
block|{
break|break;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

