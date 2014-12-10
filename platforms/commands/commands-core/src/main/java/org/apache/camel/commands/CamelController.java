begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.commands
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|spi
operator|.
name|RestRegistry
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
comment|/**      * Get all routes filtered by the regex.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @param filter           the filter which supports * and ? as wildcards      * @return the list of the Camel routes.      */
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
comment|/**      * Reset all the route stats for the given Camel context      *      * @param camelContextName the Camel context.      */
DECL|method|resetRouteStats (String camelContextName)
name|void
name|resetRouteStats
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Return the definition of a route as XML identified by a ID and a Camel context.      *      * @param routeId          the route ID.      * @param camelContextName the Camel context.      * @return the route model as XML      */
DECL|method|getRouteModelAsXml (String routeId, String camelContextName)
name|String
name|getRouteModelAsXml
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Returns detailed route statistics as XML identified by a ID and a Camel context.      *      * @param routeId           the route ID.      * @param camelContextName  the Camel context.      * @param fullStats         whether to include verbose stats      * @param includeProcessors whether to embed per processor stats from the route      * @return the route statistics as XML      */
DECL|method|getRouteStatsAsXml (String routeId, String camelContextName, boolean fullStats, boolean includeProcessors)
name|String
name|getRouteStatsAsXml
parameter_list|(
name|String
name|routeId
parameter_list|,
name|String
name|camelContextName
parameter_list|,
name|boolean
name|fullStats
parameter_list|,
name|boolean
name|includeProcessors
parameter_list|)
function_decl|;
comment|/**      * Return the endpoints      *      * @param camelContextName the Camel context.      * @return the endpoints      */
DECL|method|getEndpoints (String camelContextName)
name|List
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Return the definition of the REST services as XML for the given Camel context.      *      * @param camelContextName the Camel context.      * @return the REST model as xml      */
DECL|method|getRestModelAsXml (String camelContextName)
name|String
name|getRestModelAsXml
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Return the REST services      *      * @param camelContextName the Camel context.      * @return the REST services      */
DECL|method|getRestServices (String camelContextName)
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|RestRegistry
operator|.
name|RestService
argument_list|>
argument_list|>
name|getRestServices
parameter_list|(
name|String
name|camelContextName
parameter_list|)
function_decl|;
comment|/**      * Explains an endpoint uri      *      * @param camelContextName the Camel context.      * @param uri              the endpoint uri      * @param allOptions       whether to explain all options, or only the explicit configured options from the uri      * @return a JSON schema with explanation of the options      * @throws java.lang.Exception is thrown if error loading resources to explain the endpoint      */
DECL|method|explainEndpoint (String camelContextName, String uri, boolean allOptions)
name|String
name|explainEndpoint
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|uri
parameter_list|,
name|boolean
name|allOptions
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists Components which are in use or available on the classpath and include information      *      * @param camelContextName the Camel context.      * @return a list of key/value pairs with component information      * @throws java.lang.Exception is thrown if error loading resources to gather component information      */
DECL|method|listComponents (String camelContextName)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listComponents
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all components from the Camel components catalog      *      * @param filter optional filter to filter by labels      * @return a list of key/value pairs with component information      * @throws java.lang.Exception is thrown if error loading resources to gather component information      */
DECL|method|listComponentsCatalog (String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listComponentsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all the labels from the Camel components catalog      *      * @return a map which key is the label, and the set is the component names that has the given label      * @throws java.lang.Exception is thrown if error loading resources to gather component information      */
DECL|method|listLabelCatalog ()
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listLabelCatalog
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

