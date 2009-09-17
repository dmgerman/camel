begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Navigate
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
name|Service
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
name|model
operator|.
name|RouteDefinition
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
name|LifecycleStrategy
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
name|RouteContext
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
name|EventHelper
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
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Represents the runtime objects for a given {@link RouteDefinition} so that it can be stopped independently  * of other routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RouteService
specifier|public
class|class
name|RouteService
extends|extends
name|ServiceSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|RouteService
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|DefaultCamelContext
name|camelContext
decl_stmt|;
DECL|field|routeDefinition
specifier|private
specifier|final
name|RouteDefinition
name|routeDefinition
decl_stmt|;
DECL|field|routeContexts
specifier|private
specifier|final
name|List
argument_list|<
name|RouteContext
argument_list|>
name|routeContexts
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
name|List
argument_list|<
name|Route
argument_list|>
name|routes
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|method|RouteService (DefaultCamelContext camelContext, RouteDefinition routeDefinition, List<RouteContext> routeContexts, List<Route> routes)
specifier|public
name|RouteService
parameter_list|(
name|DefaultCamelContext
name|camelContext
parameter_list|,
name|RouteDefinition
name|routeDefinition
parameter_list|,
name|List
argument_list|<
name|RouteContext
argument_list|>
name|routeContexts
parameter_list|,
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|routeDefinition
operator|=
name|routeDefinition
expr_stmt|;
name|this
operator|.
name|routeContexts
operator|=
name|routeContexts
expr_stmt|;
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|routeDefinition
operator|.
name|idOrCreate
argument_list|(
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
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
DECL|method|getRouteContexts ()
specifier|public
name|List
argument_list|<
name|RouteContext
argument_list|>
name|getRouteContexts
parameter_list|()
block|{
return|return
name|routeContexts
return|;
block|}
DECL|method|getRouteDefinition ()
specifier|public
name|RouteDefinition
name|getRouteDefinition
parameter_list|()
block|{
return|return
name|routeDefinition
return|;
block|}
DECL|method|getRoutes ()
specifier|public
name|Collection
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|.
name|addRouteCollection
argument_list|(
name|routes
argument_list|)
expr_stmt|;
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRoutesAdd
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Starting route: "
operator|+
name|route
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|route
operator|.
name|getServices
argument_list|()
decl_stmt|;
comment|// callback that we are staring these services
name|route
operator|.
name|onStartingServices
argument_list|(
name|services
argument_list|)
expr_stmt|;
comment|// gather list of services to start as we need to start child services as well
name|List
argument_list|<
name|Service
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|doGetChildServies
argument_list|(
name|list
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
name|startChildService
argument_list|(
name|route
argument_list|,
name|list
argument_list|)
expr_stmt|;
comment|// start the route itself
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteStarted
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onRoutesRemove
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Route
name|route
range|:
name|routes
control|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Stopping route: "
operator|+
name|route
argument_list|)
expr_stmt|;
block|}
comment|// getServices will not add services again
name|List
argument_list|<
name|Service
argument_list|>
name|services
init|=
name|route
operator|.
name|getServices
argument_list|()
decl_stmt|;
comment|// gather list of services to stop as we need to start child services as well
name|List
argument_list|<
name|Service
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Service
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
name|doGetChildServies
argument_list|(
name|list
argument_list|,
name|service
argument_list|)
expr_stmt|;
block|}
name|stopChildService
argument_list|(
name|route
argument_list|,
name|list
argument_list|)
expr_stmt|;
comment|// stop the route itself
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|route
argument_list|)
expr_stmt|;
comment|// fire event
name|EventHelper
operator|.
name|notifyRouteStopped
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|camelContext
operator|.
name|removeRouteCollection
argument_list|(
name|routes
argument_list|)
expr_stmt|;
block|}
DECL|method|startChildService (Route route, List<Service> services)
specifier|protected
name|void
name|startChildService
parameter_list|(
name|Route
name|route
parameter_list|,
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onServiceAdd
argument_list|(
name|camelContext
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|addChildService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|stopChildService (Route route, List<Service> services)
specifier|protected
name|void
name|stopChildService
parameter_list|(
name|Route
name|route
parameter_list|,
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Service
name|service
range|:
name|services
control|)
block|{
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
name|strategy
operator|.
name|onServiceRemove
argument_list|(
name|camelContext
argument_list|,
name|service
argument_list|,
name|route
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|removeChildService
argument_list|(
name|service
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Need to recursive start child services for routes      */
DECL|method|doGetChildServies (List<Service> services, Service service)
specifier|private
specifier|static
name|void
name|doGetChildServies
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|,
name|Service
name|service
parameter_list|)
throws|throws
name|Exception
block|{
name|services
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
if|if
condition|(
name|service
operator|instanceof
name|Navigate
condition|)
block|{
name|Navigate
argument_list|<
name|?
argument_list|>
name|nav
init|=
operator|(
name|Navigate
argument_list|<
name|?
argument_list|>
operator|)
name|service
decl_stmt|;
if|if
condition|(
name|nav
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|children
init|=
name|nav
operator|.
name|next
argument_list|()
decl_stmt|;
for|for
control|(
name|Object
name|child
range|:
name|children
control|)
block|{
if|if
condition|(
name|child
operator|instanceof
name|Service
condition|)
block|{
name|doGetChildServies
argument_list|(
name|services
argument_list|,
operator|(
name|Service
operator|)
name|child
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

