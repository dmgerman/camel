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

begin_comment
comment|/**  * CamelController interface defines the expected behaviors to manipulate Camel resources (context, route, etc).  */
end_comment

begin_interface
DECL|interface|CamelController
specifier|public
interface|interface
name|CamelController
block|{
comment|/**      * Gets information about a given Camel context by the given name.      *      * @param name the Camel context name.      * @return a list of key/value pairs with CamelContext information      * @throws java.lang.Exception can be thrown      */
DECL|method|getCamelContextInformation (String name)
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getCamelContextInformation
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the list of Camel context.      *      * @return a list of key/value pairs with CamelContext information      * @throws java.lang.Exception can be thrown      */
DECL|method|getCamelContexts ()
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getCamelContexts
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Get the list of Camel context filter by reg ex.      *      * @param filter           the filter which supports * and ? as wildcards      * @return a list of key/value pairs with CamelContext information      * @throws java.lang.Exception can be thrown      */
DECL|method|getCamelContexts (String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getCamelContexts
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns detailed CamelContext and route statistics as XML identified by a ID and a Camel context.      *      * @param camelContextName  the Camel context.      * @param fullStats         whether to include verbose stats      * @param includeProcessors whether to embed per processor stats from the route      * @return the CamelContext statistics as XML      * @throws java.lang.Exception can be thrown      */
DECL|method|getCamelContextStatsAsXml (String camelContextName, boolean fullStats, boolean includeProcessors)
name|String
name|getCamelContextStatsAsXml
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|boolean
name|fullStats
parameter_list|,
name|boolean
name|includeProcessors
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Browses the inflight exchanges      *      * @param camelContextName        the Camel context.      * @param limit                   maximum number of exchanges to return      * @param sortByLongestDuration<tt>true</tt> to sort by longest duration,<tt>false</tt> to sort by exchange id      * @return a list of key/value pairs with inflight exchange information      * @throws java.lang.Exception can be thrown      */
DECL|method|browseInflightExchanges (String camelContextName, int limit, boolean sortByLongestDuration)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|browseInflightExchanges
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|int
name|limit
parameter_list|,
name|boolean
name|sortByLongestDuration
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Starts the given Camel context.      *      * @param camelContextName the Camel context.      * @throws java.lang.Exception can be thrown      */
DECL|method|startContext (String camelContextName)
name|void
name|startContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given Camel context.      *      * @param camelContextName the Camel context.      * @throws java.lang.Exception can be thrown      */
DECL|method|stopContext (String camelContextName)
name|void
name|stopContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the given Camel context.      *      * @param camelContextName the Camel context.      * @throws java.lang.Exception can be thrown      */
DECL|method|suspendContext (String camelContextName)
name|void
name|suspendContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resumes the given Camel context.      *      * @param camelContextName the Camel context.      * @throws java.lang.Exception can be thrown      */
DECL|method|resumeContext (String camelContextName)
name|void
name|resumeContext
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get all routes. If Camel context name is null, all routes from all contexts are listed.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @return a list of key/value pairs with routes information      * @throws java.lang.Exception can be thrown      */
DECL|method|getRoutes (String camelContextName)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Get all routes filtered by the regex.      *      * @param camelContextName the Camel context name. If null, all contexts are considered.      * @param filter           the filter which supports * and ? as wildcards      * @return a list of key/value pairs with routes information      * @throws java.lang.Exception can be thrown      */
DECL|method|getRoutes (String camelContextName, String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getRoutes
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Reset all the route stats for the given Camel context      *      * @param camelContextName the Camel context.      * @throws java.lang.Exception can be thrown      */
DECL|method|resetRouteStats (String camelContextName)
name|void
name|resetRouteStats
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Starts the given route      *      * @param camelContextName the Camel context.      * @param routeId          the route ID.      * @throws java.lang.Exception can be thrown      */
DECL|method|startRoute (String camelContextName, String routeId)
name|void
name|startRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route      *      * @param camelContextName the Camel context.      * @param routeId          the route ID.      * @throws java.lang.Exception can be thrown      */
DECL|method|stopRoute (String camelContextName, String routeId)
name|void
name|stopRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the given route      *      * @param camelContextName the Camel context.      * @param routeId          the route ID.      * @throws java.lang.Exception can be thrown      */
DECL|method|suspendRoute (String camelContextName, String routeId)
name|void
name|suspendRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resumes the given route      *      * @param camelContextName the Camel context.      * @param routeId          the route ID.      * @throws java.lang.Exception can be thrown      */
DECL|method|resumeRoute (String camelContextName, String routeId)
name|void
name|resumeRoute
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the definition of a route as XML identified by a ID and a Camel context.      *      * @param routeId          the route ID.      * @param camelContextName the Camel context.      * @return the route model as XML      * @throws java.lang.Exception can be thrown      */
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
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns detailed route statistics as XML identified by a ID and a Camel context.      *      * @param routeId           the route ID.      * @param camelContextName  the Camel context.      * @param fullStats         whether to include verbose stats      * @param includeProcessors whether to embed per processor stats from the route      * @return the route statistics as XML      * @throws java.lang.Exception can be thrown      */
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
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the endpoints      *      * @param camelContextName the Camel context.      * @return a list of key/value pairs with endpoint information      * @throws java.lang.Exception can be thrown      */
DECL|method|getEndpoints (String camelContextName)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getEndpoints
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return endpoint runtime statistics      *      * @param camelContextName the Camel context      * @return a list of key/value pairs with endpoint runtime statistics      * @throws java.lang.Exception can be thrown      */
DECL|method|getEndpointRuntimeStatistics (String camelContextName)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getEndpointRuntimeStatistics
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the definition of the REST services as XML for the given Camel context.      *      * @param camelContextName the Camel context.      * @return the REST model as xml      * @throws java.lang.Exception can be thrown      */
DECL|method|getRestModelAsXml (String camelContextName)
name|String
name|getRestModelAsXml
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the REST services API documentation as JSon (requires camel-swagger-java on classpath)      *      * @param camelContextName the Camel context.      * @return the REST API documentation as JSon      * @throws java.lang.Exception can be thrown      */
DECL|method|getRestApiDocAsJson (String camelContextName)
name|String
name|getRestApiDocAsJson
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Return the REST services for the given Camel context.      *      * @param camelContextName the Camel context.      * @return a list of key/value pairs with REST information      * @throws java.lang.Exception can be thrown      */
DECL|method|getRestServices (String camelContextName)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getRestServices
parameter_list|(
name|String
name|camelContextName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Explains an endpoint uri      *      * @param camelContextName the Camel context.      * @param uri              the endpoint uri      * @param allOptions       whether to explain all options, or only the explicit configured options from the uri      * @return a JSON schema with explanation of the options      * @throws java.lang.Exception can be thrown      */
DECL|method|explainEndpointAsJSon (String camelContextName, String uri, boolean allOptions)
name|String
name|explainEndpointAsJSon
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
comment|/**      * Explains an EIP      *      * @param camelContextName the Camel context.      * @param nameOrId         the name of the EIP ({@link org.apache.camel.NamedNode#getShortName()} or a node id to refer to a specific node from the routes.      * @param allOptions       whether to explain all options, or only the explicit configured options from the uri      * @return a JSON schema with explanation of the options      * @throws java.lang.Exception can be thrown      */
DECL|method|explainEipAsJSon (String camelContextName, String nameOrId, boolean allOptions)
name|String
name|explainEipAsJSon
parameter_list|(
name|String
name|camelContextName
parameter_list|,
name|String
name|nameOrId
parameter_list|,
name|boolean
name|allOptions
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists Components which are in use or available on the classpath and include information      *      * @param camelContextName the Camel context.      * @return a list of key/value pairs with component information      * @throws java.lang.Exception can be thrown      */
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
comment|/**      * Lists all EIPs from the Camel EIP catalog      *      * @param filter optional filter to filter by labels      * @return a list of key/value pairs with model information      * @throws java.lang.Exception can be thrown      */
DECL|method|listEipsCatalog (String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listEipsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all the labels from the Camel EIP catalog      *      * @return a map which key is the label, and the set is the models names that has the given label      * @throws java.lang.Exception can be thrown      */
DECL|method|listEipsLabelCatalog ()
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listEipsLabelCatalog
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all components from the Camel components catalog      *      * @param filter optional filter to filter by labels      * @return a list of key/value pairs with component information      * @throws java.lang.Exception can be thrown      */
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
comment|/**      * Lists all the labels from the Camel components catalog      *      * @return a map which key is the label, and the set is the component names that has the given label      * @throws java.lang.Exception can be thrown      */
DECL|method|listComponentsLabelCatalog ()
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listComponentsLabelCatalog
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all data formats from the Camel components catalog      *      * @param filter optional filter to filter by labels      * @return a list of key/value pairs with data format information      * @throws java.lang.Exception can be thrown      */
DECL|method|listDataFormatsCatalog (String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listDataFormatsCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all the labels from the Camel data formats catalog      *      * @return a map which key is the label, and the set is the data format names that has the given label      * @throws java.lang.Exception can be thrown      */
DECL|method|listDataFormatsLabelCatalog ()
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listDataFormatsLabelCatalog
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all languages from the Camel components catalog      *      * @param filter optional filter to filter by labels      * @return a list of key/value pairs with language information      * @throws java.lang.Exception can be thrown      */
DECL|method|listLanguagesCatalog (String filter)
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|listLanguagesCatalog
parameter_list|(
name|String
name|filter
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Lists all the labels from the Camel languages catalog      *      * @return a map which key is the label, and the set is the language names that has the given label      * @throws java.lang.Exception can be thrown      */
DECL|method|listLanguagesLabelCatalog ()
name|Map
argument_list|<
name|String
argument_list|,
name|Set
argument_list|<
name|String
argument_list|>
argument_list|>
name|listLanguagesLabelCatalog
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

