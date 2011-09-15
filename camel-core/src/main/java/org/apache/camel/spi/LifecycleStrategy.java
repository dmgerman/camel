begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|concurrent
operator|.
name|ThreadPoolExecutor
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
name|ErrorHandlerFactory
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
name|VetoCamelContextStartException
import|;
end_import

begin_comment
comment|/**  * Strategy for lifecycle notifications.  */
end_comment

begin_interface
DECL|interface|LifecycleStrategy
specifier|public
interface|interface
name|LifecycleStrategy
block|{
comment|/**      * Notification on starting a {@link CamelContext}.      *      * @param context the camel context      * @throws VetoCamelContextStartException can be thrown to veto starting {@link CamelContext}.      *                                        Any other runtime exceptions will be logged at<tt>WARN</tt> level by Camel will continue starting itself.      */
DECL|method|onContextStart (CamelContext context)
name|void
name|onContextStart
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|VetoCamelContextStartException
function_decl|;
comment|/**      * Notification on stopping a {@link CamelContext}.      *      * @param context the camel context      */
DECL|method|onContextStop (CamelContext context)
name|void
name|onContextStop
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Notification on adding an {@link org.apache.camel.Component}.      *      * @param name      the unique name of this component      * @param component the added component      */
DECL|method|onComponentAdd (String name, Component component)
name|void
name|onComponentAdd
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
function_decl|;
comment|/**      * Notification on removing an {@link org.apache.camel.Component}.      *      * @param name      the unique name of this component      * @param component the removed component      */
DECL|method|onComponentRemove (String name, Component component)
name|void
name|onComponentRemove
parameter_list|(
name|String
name|name
parameter_list|,
name|Component
name|component
parameter_list|)
function_decl|;
comment|/**      * Notification on adding an {@link Endpoint}.      *      * @param endpoint the added endpoint      */
DECL|method|onEndpointAdd (Endpoint endpoint)
name|void
name|onEndpointAdd
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Notification on removing an {@link Endpoint}.      *      * @param endpoint the removed endpoint      */
DECL|method|onEndpointRemove (Endpoint endpoint)
name|void
name|onEndpointRemove
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Notification on adding a {@link Service}.      *      * @param context the camel context      * @param service the added service      * @param route   the route the service belongs to if any possible to determine      */
DECL|method|onServiceAdd (CamelContext context, Service service, Route route)
name|void
name|onServiceAdd
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Notification on removing a {@link Service}.      *      * @param context the camel context      * @param service the removed service      * @param route   the route the service belongs to if any possible to determine      */
DECL|method|onServiceRemove (CamelContext context, Service service, Route route)
name|void
name|onServiceRemove
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Service
name|service
parameter_list|,
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Notification on adding {@link Route}(s).      *      * @param routes the added routes      */
DECL|method|onRoutesAdd (Collection<Route> routes)
name|void
name|onRoutesAdd
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
function_decl|;
comment|/**      * Notification on removing {@link Route}(s).      *      * @param routes the removed routes      */
DECL|method|onRoutesRemove (Collection<Route> routes)
name|void
name|onRoutesRemove
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
function_decl|;
comment|/**      * Notification on adding {@link RouteContext}(s).      *      * @param routeContext the added route context      */
DECL|method|onRouteContextCreate (RouteContext routeContext)
name|void
name|onRouteContextCreate
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
function_decl|;
comment|/**      * Notification on adding error handler.      *      * @param routeContext        the added route context      * @param errorHandler        the error handler      * @param errorHandlerBuilder the error handler builder      */
DECL|method|onErrorHandlerAdd (RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory errorHandlerBuilder)
name|void
name|onErrorHandlerAdd
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerFactory
name|errorHandlerBuilder
parameter_list|)
function_decl|;
comment|/**      * Notification on adding a thread pool.      *      * @param camelContext        the camel context      * @param threadPool          the thread pool      * @param id                  id of the thread pool (can be null in special cases)      * @param sourceId            id of the source creating the thread pool (can be null in special cases)      * @param routeId             id of the route for the source (is null if no source)      * @param threadPoolProfileId id of the thread pool profile, if used for creating this thread pool (can be null)      */
DECL|method|onThreadPoolAdd (CamelContext camelContext, ThreadPoolExecutor threadPool, String id, String sourceId, String routeId, String threadPoolProfileId)
name|void
name|onThreadPoolAdd
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ThreadPoolExecutor
name|threadPool
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|sourceId
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|threadPoolProfileId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

