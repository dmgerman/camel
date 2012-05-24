begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_comment
comment|/**  * Model level interface for the {@link CamelContext}  */
end_comment

begin_interface
DECL|interface|ModelCamelContext
specifier|public
interface|interface
name|ModelCamelContext
extends|extends
name|CamelContext
block|{
comment|/**      * Returns a list of the current route definitions      *      * @return list of the current route definitions      */
DECL|method|getRouteDefinitions ()
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRouteDefinitions
parameter_list|()
function_decl|;
comment|/**      * Gets the route definition with the given id      *      * @param id id of the route      * @return the route definition or<tt>null</tt> if not found      */
DECL|method|getRouteDefinition (String id)
name|RouteDefinition
name|getRouteDefinition
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Loads a collection of route definitions from the given {@link java.io.InputStream}.      *      * @param is input stream with the route(s) definition to add      * @return the route definitions      * @throws Exception if the route definitions could not be loaded for whatever reason      */
DECL|method|loadRoutesDefinition (InputStream is)
name|RoutesDefinition
name|loadRoutesDefinition
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a collection of route definitions to the context      *<p/>      *<b>Important:</b> Each route in the same {@link org.apache.camel.CamelContext} must have an<b>unique</b> route id.      * If you use the API from {@link org.apache.camel.CamelContext} or {@link org.apache.camel.model.ModelCamelContext} to add routes, then any      * new routes which has a route id that matches an old route, then the old route is replaced by the new route.      *      * @param routeDefinitions the route(s) definition to add      * @throws Exception if the route definitions could not be created for whatever reason      */
DECL|method|addRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
name|void
name|addRouteDefinitions
parameter_list|(
name|Collection
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Add a route definition to the context      *<p/>      *<b>Important:</b> Each route in the same {@link org.apache.camel.CamelContext} must have an<b>unique</b> route id.      * If you use the API from {@link org.apache.camel.CamelContext} or {@link org.apache.camel.model.ModelCamelContext} to add routes, then any      * new routes which has a route id that matches an old route, then the old route is replaced by the new route.      *      * @param routeDefinition the route definition to add      * @throws Exception if the route definition could not be created for whatever reason      */
DECL|method|addRouteDefinition (RouteDefinition routeDefinition)
name|void
name|addRouteDefinition
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes a collection of route definitions from the context - stopping any previously running      * routes if any of them are actively running      *      * @param routeDefinitions route(s) definitions to remove      * @throws Exception if the route definitions could not be removed for whatever reason      */
DECL|method|removeRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
name|void
name|removeRouteDefinitions
parameter_list|(
name|Collection
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes a route definition from the context - stopping any previously running      * routes if any of them are actively running      *      * @param routeDefinition route definition to remove      * @throws Exception if the route definition could not be removed for whatever reason      */
DECL|method|removeRouteDefinition (RouteDefinition routeDefinition)
name|void
name|removeRouteDefinition
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Starts the given route if it has been previously stopped      *      * @param route the route to start      * @throws Exception is thrown if the route could not be started for whatever reason      * @deprecated favor using {@link CamelContext#startRoute(String)}      */
annotation|@
name|Deprecated
DECL|method|startRoute (RouteDefinition route)
name|void
name|startRoute
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route.      *      * @param route the route to stop      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @deprecated favor using {@link CamelContext#stopRoute(String)}      */
annotation|@
name|Deprecated
DECL|method|stopRoute (RouteDefinition route)
name|void
name|stopRoute
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the data formats that can be referenced in the routes.      *      * @param dataFormats the data formats      */
DECL|method|setDataFormats (Map<String, DataFormatDefinition> dataFormats)
name|void
name|setDataFormats
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|dataFormats
parameter_list|)
function_decl|;
comment|/**      * Gets the data formats that can be referenced in the routes.      *      * @return the data formats available      */
DECL|method|getDataFormats ()
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|getDataFormats
parameter_list|()
function_decl|;
comment|/**      * Resolve a data format definition given its name      *      * @param name the data format definition name or a reference to it in the {@link org.apache.camel.spi.Registry}      * @return the resolved data format definition, or<tt>null</tt> if not found      */
DECL|method|resolveDataFormatDefinition (String name)
name|DataFormatDefinition
name|resolveDataFormatDefinition
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

