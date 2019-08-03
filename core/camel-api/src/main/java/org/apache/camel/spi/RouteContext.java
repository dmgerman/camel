begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|EndpointAware
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
name|NamedNode
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
name|RuntimeConfiguration
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
name|meta
operator|.
name|Experimental
import|;
end_import

begin_comment
comment|/**  * The context used to activate new routing rules  */
end_comment

begin_interface
DECL|interface|RouteContext
specifier|public
interface|interface
name|RouteContext
extends|extends
name|RuntimeConfiguration
extends|,
name|EndpointAware
block|{
comment|/**      * Gets the route id      */
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
comment|/**      * Get the route type      *      * @return the route type      */
DECL|method|getRoute ()
name|NamedNode
name|getRoute
parameter_list|()
function_decl|;
comment|/**      * Gets the camel context      *      * @return the camel context      */
DECL|method|getCamelContext ()
name|CamelContext
name|getCamelContext
parameter_list|()
function_decl|;
comment|/**      * Resolves an endpoint from the URI      *      * @param uri the URI      * @return the resolved endpoint      */
DECL|method|resolveEndpoint (String uri)
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Resolves an endpoint from either a URI or a named reference      *      * @param uri  the URI or      * @param ref  the named reference      * @return the resolved endpoint      */
DECL|method|resolveEndpoint (String uri, String ref)
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|ref
parameter_list|)
function_decl|;
comment|/**      * lookup an object by name and type      *      * @param name  the name to lookup      * @param type  the expected type      * @return the found object      */
DECL|method|lookup (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * lookup an object by name and type or throws {@link org.apache.camel.NoSuchBeanException} if not found.      *      * @param name  the name to lookup      * @param type  the expected type      * @return the found object      */
DECL|method|mandatoryLookup (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryLookup
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * lookup objects by type      *      * @param type the expected type      * @return the found objects with the name as the key in the map. Returns an empty map if none found.      */
DECL|method|lookupByType (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|lookupByType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * For completing the route creation, creating a single event driven route      * for the current from endpoint with any processors required      */
DECL|method|commit ()
name|Route
name|commit
parameter_list|()
function_decl|;
comment|/**      * Adds an event driven processor      *      * @param processor the processor      */
DECL|method|addEventDrivenProcessor (Processor processor)
name|void
name|addEventDrivenProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * This method retrieves the InterceptStrategy instances this route context.      *      * @return the strategy      */
DECL|method|getInterceptStrategies ()
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
function_decl|;
comment|/**      * This method sets the InterceptStrategy instances on this route context.      *      * @param interceptStrategies the strategies      */
DECL|method|setInterceptStrategies (List<InterceptStrategy> interceptStrategies)
name|void
name|setInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptStrategies
parameter_list|)
function_decl|;
comment|/**      * Adds a InterceptStrategy to this route context      *      * @param interceptStrategy the strategy      */
DECL|method|addInterceptStrategy (InterceptStrategy interceptStrategy)
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
function_decl|;
comment|/**      * Sets a special intercept strategy for management.      *<p/>      * Is by default used to correlate managed performance counters with processors      * when the runtime route is being constructed      *      * @param interceptStrategy the managed intercept strategy      */
DECL|method|setManagementInterceptStrategy (ManagementInterceptStrategy interceptStrategy)
name|void
name|setManagementInterceptStrategy
parameter_list|(
name|ManagementInterceptStrategy
name|interceptStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the special managed intercept strategy if any      *      * @return the managed intercept strategy, or<tt>null</tt> if not managed      */
DECL|method|getManagementInterceptStrategy ()
name|ManagementInterceptStrategy
name|getManagementInterceptStrategy
parameter_list|()
function_decl|;
comment|/**      * If this flag is true, {@link org.apache.camel.reifier.ProcessorReifier#addRoutes(RouteContext)}      * will not add processor to addEventDrivenProcessor to the RouteContext and it      * will prevent from adding an EventDrivenRoute.      *      * @param value the flag      */
DECL|method|setIsRouteAdded (boolean value)
name|void
name|setIsRouteAdded
parameter_list|(
name|boolean
name|value
parameter_list|)
function_decl|;
DECL|method|setEndpoint (Endpoint endpoint)
name|void
name|setEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Returns the isRouteAdded flag      *       * @return the flag      */
DECL|method|isRouteAdded ()
name|boolean
name|isRouteAdded
parameter_list|()
function_decl|;
comment|/**      * Gets the route policy List      *      * @return the route policy list if any      */
DECL|method|getRoutePolicyList ()
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|getRoutePolicyList
parameter_list|()
function_decl|;
comment|/**      * Sets a custom route policy List      *      * @param routePolicyList the custom route policy list      */
DECL|method|setRoutePolicyList (List<RoutePolicy> routePolicyList)
name|void
name|setRoutePolicyList
parameter_list|(
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|routePolicyList
parameter_list|)
function_decl|;
comment|/**      * Sets whether the route should automatically start when Camel starts.      *<p/>      * Default is<tt>true</tt> to always start up.      *      * @param autoStartup whether to start up automatically.      */
annotation|@
name|Override
DECL|method|setAutoStartup (Boolean autoStartup)
name|void
name|setAutoStartup
parameter_list|(
name|Boolean
name|autoStartup
parameter_list|)
function_decl|;
comment|/**      * Gets whether the route should automatically start when Camel starts.      *<p/>      * Default is<tt>true</tt> to always start up.      *      * @return<tt>true</tt> if route should automatically start      */
annotation|@
name|Override
DECL|method|isAutoStartup ()
name|Boolean
name|isAutoStartup
parameter_list|()
function_decl|;
DECL|method|setStartupOrder (Integer startupOrder)
name|void
name|setStartupOrder
parameter_list|(
name|Integer
name|startupOrder
parameter_list|)
function_decl|;
DECL|method|getStartupOrder ()
name|Integer
name|getStartupOrder
parameter_list|()
function_decl|;
DECL|method|setErrorHandlerFactory (ErrorHandlerFactory errorHandlerFactory)
name|void
name|setErrorHandlerFactory
parameter_list|(
name|ErrorHandlerFactory
name|errorHandlerFactory
parameter_list|)
function_decl|;
DECL|method|getErrorHandlerFactory ()
name|ErrorHandlerFactory
name|getErrorHandlerFactory
parameter_list|()
function_decl|;
DECL|method|addAdvice (CamelInternalProcessorAdvice<?> advice)
name|void
name|addAdvice
parameter_list|(
name|CamelInternalProcessorAdvice
argument_list|<
name|?
argument_list|>
name|advice
parameter_list|)
function_decl|;
DECL|method|addProperty (String key, Object value)
name|void
name|addProperty
parameter_list|(
name|String
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Gets the last error.      *      * @return the error      */
DECL|method|getLastError ()
specifier|default
name|RouteError
name|getLastError
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Sets the last error.      *      * @param error the error      */
DECL|method|setLastError (RouteError error)
specifier|default
name|void
name|setLastError
parameter_list|(
name|RouteError
name|error
parameter_list|)
block|{     }
comment|/**      * Gets the  {@link RouteController} for this route.      *      * @return the route controller,      */
annotation|@
name|Experimental
DECL|method|getRouteController ()
specifier|default
name|RouteController
name|getRouteController
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Sets the {@link RouteController} for this route.      *      * @param controller the RouteController      */
annotation|@
name|Experimental
DECL|method|setRouteController (RouteController controller)
specifier|default
name|void
name|setRouteController
parameter_list|(
name|RouteController
name|controller
parameter_list|)
block|{     }
DECL|method|getOnCompletion (String onCompletionId)
name|Processor
name|getOnCompletion
parameter_list|(
name|String
name|onCompletionId
parameter_list|)
function_decl|;
DECL|method|setOnCompletion (String onCompletionId, Processor processor)
name|void
name|setOnCompletion
parameter_list|(
name|String
name|onCompletionId
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
DECL|method|getOnException (String onExceptionId)
name|Processor
name|getOnException
parameter_list|(
name|String
name|onExceptionId
parameter_list|)
function_decl|;
DECL|method|setOnException (String onExceptionId, Processor processor)
name|void
name|setOnException
parameter_list|(
name|String
name|onExceptionId
parameter_list|,
name|Processor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Adds error handler for the given exception type      *      * @param factory       the error handler factory      * @param exception     the exception to handle      */
DECL|method|addErrorHandler (ErrorHandlerFactory factory, NamedNode exception)
name|void
name|addErrorHandler
parameter_list|(
name|ErrorHandlerFactory
name|factory
parameter_list|,
name|NamedNode
name|exception
parameter_list|)
function_decl|;
comment|/**      * Gets the error handlers      *      * @param factory       the error handler factory      */
DECL|method|getErrorHandlers (ErrorHandlerFactory factory)
name|Set
argument_list|<
name|NamedNode
argument_list|>
name|getErrorHandlers
parameter_list|(
name|ErrorHandlerFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Link the error handlers from a factory to another      *      * @param source        the source factory      * @param target        the target factory      */
DECL|method|addErrorHandlerFactoryReference (ErrorHandlerFactory source, ErrorHandlerFactory target)
name|void
name|addErrorHandlerFactoryReference
parameter_list|(
name|ErrorHandlerFactory
name|source
parameter_list|,
name|ErrorHandlerFactory
name|target
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

