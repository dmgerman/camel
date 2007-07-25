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
name|spi
operator|.
name|ExchangeConverter
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
name|Injector
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
name|Language
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
comment|/**      * Adds a component to the context.      */
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
comment|/**      * Gets a component from the context by name.      */
DECL|method|getComponent (String componentName)
name|Component
name|getComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the context by name and specifying the expected type of component.      */
DECL|method|getComponent (String name, Class<T> componentType)
parameter_list|<
name|T
extends|extends
name|Component
parameter_list|>
name|T
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|componentType
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
comment|/**      * Gets the a previously added component by name or lazily creates the component      * using the factory Callback.      *      * @param componentName the name of the component      * @param factory       used to create a new component instance if the component was not previously added.      * @return      */
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
comment|/**      * Resolves the given URI to an {@see Endpoint}.  If the URI has a singleton endpoint      * registered, then the singleton is returned.  Otherwise, a new {@see Endpoint} is created      * and if the endpoint is a singleton it is registered as a singleton endpoint.      */
DECL|method|getEndpoint (String uri)
name|Endpoint
name|getEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Resolves the given URI to an {@see Endpoint} of the specified type.      * If the URI has a singleton endpoint registered, then the singleton is returned.      * Otherwise, a new {@see Endpoint} is created and if the endpoint is a      * singleton it is registered as a singleton endpoint.      */
DECL|method|getEndpoint (String name, Class<T> endpointType)
parameter_list|<
name|T
extends|extends
name|Endpoint
parameter_list|>
name|T
name|getEndpoint
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|endpointType
parameter_list|)
function_decl|;
comment|/**      * Returns the collection of all registered singleton endpoints.      */
DECL|method|getSingletonEndpoints ()
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getSingletonEndpoints
parameter_list|()
function_decl|;
comment|/**      * Adds the endpoint to the context using the given URI.  The endpoint will be registered as a singleton.      *      * @param uri the URI to be used to resolve this endpoint      * @param endpoint the endpoint to be added to the context      * @return the old endpoint that was previously registered to the context if there was      * already an endpoint for that URI      * @throws Exception if the new endpoint could not be started or the old endpoint could not be stopped      */
DECL|method|addSingletonEndpoint (String uri, Endpoint endpoint)
name|Endpoint
name|addSingletonEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes the singleton endpoint with the given URI      *      * @param uri the URI to be used to remove      * @return the endpoint that was removed or null if there is no endpoint for this URI      * @throws Exception if endpoint could not be stopped      */
DECL|method|removeSingletonEndpoint (String uri)
name|Endpoint
name|removeSingletonEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// Route Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Returns the current routes in this context      *      * @return the current routes in this context      */
DECL|method|getRoutes ()
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
function_decl|;
comment|/**      * Sets the routes for this context, replacing any current routes      *      * @param routes the new routes to use      */
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
comment|/**      * Adds a collection of routes to this context      *      * @param routes the routes to add      */
DECL|method|addRoutes (Collection<Route> routes)
name|void
name|addRoutes
parameter_list|(
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a collection of routes to this context using the given builder      * to build them      *      * @param builder the builder which will create the routes and add them to this context      * @throws Exception if the routes could not be created for whatever reason      */
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
comment|// Properties
comment|//-----------------------------------------------------------------------
comment|/**      * Returns the converter of exchanges from one type to another      * @return      */
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
comment|/**      * Returns the registry used to lookup components by name and type such as the Spring ApplicationContext,      * JNDI or the OSGi Service Registry      */
DECL|method|getRegistry ()
name|Registry
name|getRegistry
parameter_list|()
function_decl|;
comment|/**      * Returns the injector used to instantiate objects by type      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
comment|/**      * Resolves a language for creating expressions      */
DECL|method|resolveLanguage (String language)
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|language
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

