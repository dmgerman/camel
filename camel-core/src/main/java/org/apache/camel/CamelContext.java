begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

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
name|RouteBuilder
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
name|converter
operator|.
name|Injector
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
import|;
end_import

begin_comment
comment|/**  * Interface used to represent the context used to configure routes and the  * policies to use during message exchanges between endpoints.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|CamelContext
specifier|public
interface|interface
name|CamelContext
extends|extends
name|Service
block|{
comment|// Component Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Adds a component to the container.      */
DECL|method|addComponent (String componentName, Component component)
name|void
name|addComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
name|Component
name|component
parameter_list|)
function_decl|;
DECL|method|getComponent (String componentName)
name|Component
name|getComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Removes a previously added component.      *      * @param componentName      * @return the previously added component or null if it had not been previously added.      */
DECL|method|removeComponent (String componentName)
name|Component
name|removeComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets the a previously added component by name or lazily creates the component      * using the factory Callback.      *      * @param componentName      * @param factory       used to create a new component instance if the component was not previously added.      * @return      */
DECL|method|getOrCreateComponent (String componentName, Callable<Component> factory)
name|Component
name|getOrCreateComponent
parameter_list|(
name|String
name|componentName
parameter_list|,
name|Callable
argument_list|<
name|Component
argument_list|>
name|factory
parameter_list|)
function_decl|;
comment|// Endpoint Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Resolves the given URI to an endpoint      */
DECL|method|resolveEndpoint (String uri)
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Activates all the starting endpoints in that were added as routes.      */
DECL|method|activateEndpoints ()
name|void
name|activateEndpoints
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Deactivates all the starting endpoints in that were added as routes.      */
DECL|method|deactivateEndpoints ()
name|void
name|deactivateEndpoints
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the collection of all active endpoints currently registered      */
DECL|method|getEndpoints ()
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
function_decl|;
comment|// Route Management Methods
comment|//-----------------------------------------------------------------------
DECL|method|getRoutes ()
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
function_decl|;
DECL|method|setRoutes (List<Route> routes)
name|void
name|setRoutes
parameter_list|(
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
function_decl|;
DECL|method|addRoutes (List<Route> routes)
name|void
name|addRoutes
parameter_list|(
name|List
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
function_decl|;
DECL|method|addRoutes (RouteBuilder builder)
name|void
name|addRoutes
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|addRoutes (RouteFactory factory)
name|void
name|addRoutes
parameter_list|(
name|RouteFactory
name|factory
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// Properties
comment|//-----------------------------------------------------------------------
DECL|method|getEndpointResolver ()
name|EndpointResolver
name|getEndpointResolver
parameter_list|()
function_decl|;
DECL|method|getExchangeConverter ()
name|ExchangeConverter
name|getExchangeConverter
parameter_list|()
function_decl|;
comment|/**      * Returns the type converter used to coerce types from one type to another      */
DECL|method|getTypeConverter ()
name|TypeConverter
name|getTypeConverter
parameter_list|()
function_decl|;
comment|/**      * Returns the injector used to instantiate objects by type      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

