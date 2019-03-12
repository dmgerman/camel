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
name|cloud
operator|.
name|ServiceCallConfigurationDefinition
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
name|rest
operator|.
name|RestDefinition
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
name|rest
operator|.
name|RestsDefinition
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
name|transformer
operator|.
name|TransformerDefinition
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
name|validator
operator|.
name|ValidatorDefinition
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
annotation|@
name|Deprecated
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
comment|/**      * Loads a collection of rest definitions from the given {@link java.io.InputStream}.      *      * @param is input stream with the rest(s) definition to add      * @return the rest definitions      * @throws Exception if the rest definitions could not be loaded for whatever reason      */
annotation|@
name|Deprecated
DECL|method|loadRestsDefinition (InputStream is)
name|RestsDefinition
name|loadRestsDefinition
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a collection of route definitions to the context      *<p/>      *<b>Important:</b> Each route in the same {@link org.apache.camel.CamelContext} must have an<b>unique</b> route id.      * If you use the API from {@link org.apache.camel.CamelContext} or {@link org.apache.camel.model.ModelCamelContext} to add routes, then any      * new routes which has a route id that matches an old route, then the old route is replaced by the new route.      *      * @param is input stream with the route(s) definition to add      * @throws Exception if the route definitions could not be created for whatever reason      */
DECL|method|addRouteDefinitions (InputStream is)
name|void
name|addRouteDefinitions
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
comment|/**      * Returns a list of the current REST definitions      *      * @return list of the current REST definitions      */
DECL|method|getRestDefinitions ()
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRestDefinitions
parameter_list|()
function_decl|;
comment|/**      * Adds a collection of rest definitions to the context      *      * @param is input stream with the rest(s) definition to add      * @throws Exception if the rest definitions could not be created for whatever reason      */
DECL|method|addRestDefinitions (InputStream is)
name|void
name|addRestDefinitions
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a collection of rest definitions to the context      *      * @param restDefinitions the rest(s) definition to add      * @throws Exception if the rest definitions could not be created for whatever reason      */
DECL|method|addRestDefinitions (Collection<RestDefinition> restDefinitions)
name|void
name|addRestDefinitions
parameter_list|(
name|Collection
argument_list|<
name|RestDefinition
argument_list|>
name|restDefinitions
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
comment|/**      * Gets the processor definition from any of the routes which with the given id      *      * @param id id of the processor definition      * @return the processor definition or<tt>null</tt> if not found      */
DECL|method|getProcessorDefinition (String id)
name|ProcessorDefinition
name|getProcessorDefinition
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Gets the processor definition from any of the routes which with the given id      *      * @param id id of the processor definition      * @param type the processor definition type      * @return the processor definition or<tt>null</tt> if not found      * @throws java.lang.ClassCastException is thrown if the type is not correct type      */
DECL|method|getProcessorDefinition (String id, Class<T> type)
parameter_list|<
name|T
extends|extends
name|ProcessorDefinition
parameter_list|>
name|T
name|getProcessorDefinition
parameter_list|(
name|String
name|id
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the validators that can be referenced in the routes.      *      * @param validators the validators      */
DECL|method|setValidators (List<ValidatorDefinition> validators)
name|void
name|setValidators
parameter_list|(
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|validators
parameter_list|)
function_decl|;
comment|/**      * Gets the Hystrix configuration by the given name. If no name is given      * the default configuration is returned, see<tt>setHystrixConfiguration</tt>      *      * @param id id of the configuration, or<tt>null</tt> to return the default configuration      * @return the configuration, or<tt>null</tt> if no configuration has been registered      */
DECL|method|getHystrixConfiguration (String id)
name|HystrixConfigurationDefinition
name|getHystrixConfiguration
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Sets the default Hystrix configuration      *      * @param configuration the configuration      */
DECL|method|setHystrixConfiguration (HystrixConfigurationDefinition configuration)
name|void
name|setHystrixConfiguration
parameter_list|(
name|HystrixConfigurationDefinition
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Sets the Hystrix configurations      *      * @param configurations the configuration list      */
DECL|method|setHystrixConfigurations (List<HystrixConfigurationDefinition> configurations)
name|void
name|setHystrixConfigurations
parameter_list|(
name|List
argument_list|<
name|HystrixConfigurationDefinition
argument_list|>
name|configurations
parameter_list|)
function_decl|;
comment|/**      * Adds the Hystrix configuration      *      * @param id name of the configuration      * @param configuration the configuration      */
DECL|method|addHystrixConfiguration (String id, HystrixConfigurationDefinition configuration)
name|void
name|addHystrixConfiguration
parameter_list|(
name|String
name|id
parameter_list|,
name|HystrixConfigurationDefinition
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Gets the validators that can be referenced in the routes.      *      * @return the validators available      */
DECL|method|getValidators ()
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|getValidators
parameter_list|()
function_decl|;
comment|/**      * Sets the transformers that can be referenced in the routes.      *      * @param transformers the transformers      */
DECL|method|setTransformers (List<TransformerDefinition> transformers)
name|void
name|setTransformers
parameter_list|(
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|transformers
parameter_list|)
function_decl|;
comment|/**      * Gets the transformers that can be referenced in the routes.      *      * @return the transformers available      */
DECL|method|getTransformers ()
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|getTransformers
parameter_list|()
function_decl|;
comment|/**      * Gets the service call configuration by the given name. If no name is given      * the default configuration is returned, see<tt>setServiceCallConfiguration</tt>      *      * @param serviceName name of service, or<tt>null</tt> to return the default configuration      * @return the configuration, or<tt>null</tt> if no configuration has been registered      */
DECL|method|getServiceCallConfiguration (String serviceName)
name|ServiceCallConfigurationDefinition
name|getServiceCallConfiguration
parameter_list|(
name|String
name|serviceName
parameter_list|)
function_decl|;
comment|/**      * Sets the default service call configuration      *      * @param configuration the configuration      */
DECL|method|setServiceCallConfiguration (ServiceCallConfigurationDefinition configuration)
name|void
name|setServiceCallConfiguration
parameter_list|(
name|ServiceCallConfigurationDefinition
name|configuration
parameter_list|)
function_decl|;
comment|/**      * Sets the service call configurations      *      * @param configurations the configuration list      */
DECL|method|setServiceCallConfigurations (List<ServiceCallConfigurationDefinition> configurations)
name|void
name|setServiceCallConfigurations
parameter_list|(
name|List
argument_list|<
name|ServiceCallConfigurationDefinition
argument_list|>
name|configurations
parameter_list|)
function_decl|;
comment|/**      * Adds the service call configuration      *      * @param serviceName name of the service      * @param configuration the configuration      */
DECL|method|addServiceCallConfiguration (String serviceName, ServiceCallConfigurationDefinition configuration)
name|void
name|addServiceCallConfiguration
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|ServiceCallConfigurationDefinition
name|configuration
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

