begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.karaf.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|karaf
operator|.
name|commands
package|;
end_package

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
name|model
operator|.
name|RouteDefinition
import|;
end_import

begin_comment
comment|/**  * CamelController interface defines the expected behaviors to manipulate Camel resources (context, route, etc).  */
end_comment

begin_interface
DECL|interface|CamelController
specifier|public
interface|interface
name|CamelController
block|{
comment|/**      * Get the list of Camel context.      *      * @return the list of Camel contexts.      */
DECL|method|getCamelContexts ()
name|List
argument_list|<
name|CamelContext
argument_list|>
name|getCamelContexts
parameter_list|()
function_decl|;
comment|/**      * Get a Camel context identified by the given name.      *      * @param name the Camel context name.      * @return the Camel context or null if not found.      */
DECL|method|getCamelContext (String name)
name|CamelContext
name|getCamelContext
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Get all routes. If Camel context name is null, all routes from all contexts are listed.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @return the list of the Camel routes.      */
DECL|method|getRoutes (String camelContextName)
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Get all routes filtered by the regex.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @param filter the filter which supports * and ? as wildcards      * @return the list of the Camel routes.      */
DECL|method|getRoutes (String camelContextName, String filter)
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Get all route definitions. If Camel context name is null, all route definitions from all contexts are listed.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @return the list of the Camel route definitions.      */
DECL|method|getRouteDefinitions (String camelContextName)
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRouteDefinitions
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Return the route with the given route ID.      *      * @param routeId the route ID.      * @param camelContextName the Camel context name.      * @return the route.      */
DECL|method|getRoute (String routeId, String camelContextName)
name|Route
name|getRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Return the definition of a route identified by a ID and a Camel context.      *      * @param routeId the route ID.      * @param camelContextName the Camel context.      * @return the<code>RouteDefinition</code>.      */
DECL|method|getRouteDefinition (String routeId, String camelContextName)
name|RouteDefinition
name|getRouteDefinition
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

