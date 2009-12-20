begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Callable
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
name|builder
operator|.
name|ErrorHandlerBuilder
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
name|DataFormatDefinition
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
name|ClassResolver
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
name|DataFormat
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
name|DataFormatResolver
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
name|EndpointStrategy
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
name|FactoryFinder
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
name|FactoryFinderResolver
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
name|InflightRepository
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
name|InterceptStrategy
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
name|ManagementStrategy
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
name|NodeIdFactory
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
name|PackageScanClassResolver
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ServicePool
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
name|ShutdownStrategy
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
name|TypeConverterRegistry
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
extends|,
name|RuntimeConfiguration
block|{
comment|/**      * Gets the name of the this context.      *      * @return the name      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Gets the version of the this context.      *      * @return the version      */
DECL|method|getVersion ()
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|// Service Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Adds a service, starting it so that it will be stopped with this context      *      * @param object the service      * @throws Exception can be thrown when starting the service      */
DECL|method|addService (Object object)
name|void
name|addService
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Has the given service already been added?      *      * @param object the service      * @return<tt>true</tt> if already added,<tt>false</tt> if not.      */
DECL|method|hasService (Object object)
name|boolean
name|hasService
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
comment|// Component Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Adds a component to the context.      *      * @param componentName  the name the component is registered as      * @param component      the component      */
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
comment|/**      * Is the given component already registered?      *      * @param componentName the name of the component      * @return the registered Component or<tt>null</tt> if not registered      */
DECL|method|hasComponent (String componentName)
name|Component
name|hasComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the context by name.      *      * @param componentName the name of the component      * @return the component      */
DECL|method|getComponent (String componentName)
name|Component
name|getComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the context by name and specifying the expected type of component.      *      * @param name  the name to lookup      * @param componentType  the expected type      * @return the component      */
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
comment|/**      * Gets a readonly list of names of the components currently registered      *      * @return a readonly list with the names of the the components      */
DECL|method|getComponentNames ()
name|List
argument_list|<
name|String
argument_list|>
name|getComponentNames
parameter_list|()
function_decl|;
comment|/**      * Removes a previously added component.      *      * @param componentName the component name to remove      * @return the previously added component or null if it had not been previously added.      */
DECL|method|removeComponent (String componentName)
name|Component
name|removeComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets the a previously added component by name or lazily creates the component      * using the factory Callback.      *      * @param componentName the name of the component      * @param factory       used to create a new component instance if the component was not previously added.      * @return the component      * @deprecated will be removed in Camel 2.3.      */
annotation|@
name|Deprecated
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
comment|/**      * Resolves the given name to an {@link Endpoint} of the specified type.      * If the name has a singleton endpoint registered, then the singleton is returned.      * Otherwise, a new {@link Endpoint} is created and registered.      *      * @param uri  the URI of the endpoint      * @return  the endpoint      */
DECL|method|getEndpoint (String uri)
name|Endpoint
name|getEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Resolves the given name to an {@link Endpoint} of the specified type.      * If the name has a singleton endpoint registered, then the singleton is returned.      * Otherwise, a new {@link Endpoint} is created and registered.      *      * @param name  the name of the endpoint      * @param endpointType  the expected type      * @return the endpoint      */
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
comment|/**      * Returns the collection of all registered endpoints.      *      * @return  all endpoints      */
DECL|method|getEndpoints ()
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
function_decl|;
comment|/**      * Returns a new Map containing all of the active endpoints with the key of the map being their      * unique key.      *      * @return map of active endpoints      */
DECL|method|getEndpointMap ()
name|Map
argument_list|<
name|String
argument_list|,
name|Endpoint
argument_list|>
name|getEndpointMap
parameter_list|()
function_decl|;
comment|/**      * Is the given endpoint already registered?      *      * @param uri  the URI of the endpoint      * @return the registered endpoint or<tt>null</tt> if not registered      */
DECL|method|hasEndpoint (String uri)
name|Endpoint
name|hasEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Returns the collection of all registered endpoints for a uri or an empty collection.      * For a singleton endpoint the collection will contain exactly one element.      *      * @param uri  the URI of the endpoints      * @return  collection of endpoints      * @deprecated not used will be removed in Camel 2.3.      */
annotation|@
name|Deprecated
DECL|method|getEndpoints (String uri)
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Returns the collection of all registered singleton endpoints.      *      * @return  all the singleton endpoints      * @deprecated not used will be removed in Camel 2.3.      */
annotation|@
name|Deprecated
DECL|method|getSingletonEndpoints ()
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getSingletonEndpoints
parameter_list|()
function_decl|;
comment|/**      * Adds the endpoint to the context using the given URI.      *      * @param uri the URI to be used to resolve this endpoint      * @param endpoint the endpoint to be added to the context      * @return the old endpoint that was previously registered or<tt>null</tt> if none was registered      * @throws Exception if the new endpoint could not be started or the old endpoint could not be stopped      */
DECL|method|addEndpoint (String uri, Endpoint endpoint)
name|Endpoint
name|addEndpoint
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
comment|/**      * Removes all endpoints with the given URI      *      * @param uri the URI to be used to remove      * @return a collection of endpoints removed or null if there are no endpoints for this URI      * @throws Exception if at least one endpoint could not be stopped      * @deprecated not used will be removed in Camel 2.3.      */
annotation|@
name|Deprecated
DECL|method|removeEndpoints (String uri)
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|removeEndpoints
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Registers a {@link org.apache.camel.spi.EndpointStrategy callback} to allow you to do custom      * logic when an {@link Endpoint} is about to be registered to the {@link CamelContext} endpoint registry.      *<p/>      * When a callback is added it will be executed on the already registered endpoints allowing you to catch-up      *      * @param strategy  callback to be invoked      */
DECL|method|addRegisterEndpointCallback (EndpointStrategy strategy)
name|void
name|addRegisterEndpointCallback
parameter_list|(
name|EndpointStrategy
name|strategy
parameter_list|)
function_decl|;
comment|// Route Management Methods
comment|//-----------------------------------------------------------------------
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
comment|/**      * Returns the current routes in this context      *      * @return the current routes      */
DECL|method|getRoutes ()
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
function_decl|;
comment|/**      * Adds a collection of routes to this context using the given builder      * to build them      *      * @param builder the builder which will create the routes and add them to this context      * @throws Exception if the routes could not be created for whatever reason      */
DECL|method|addRoutes (RoutesBuilder builder)
name|void
name|addRoutes
parameter_list|(
name|RoutesBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a collection of route definitions to the context      *      * @param routeDefinitions the route definitions to add      * @throws Exception if the route definition could not be created for whatever reason      */
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
comment|/**      * Removes a collection of route definitions from the context - stopping any previously running      * routes if any of them are actively running      *      * @param routeDefinitions route definitions      * @throws Exception if the route definition could not be removed for whatever reason      */
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
comment|/**      * Starts the given route if it has been previously stopped      *      * @param route the route to start      * @throws Exception is thrown if the route could not be started for whatever reason      */
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
comment|/**      * Starts the given route if it has been previously stopped      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be started for whatever reason      */
DECL|method|startRoute (String routeId)
name|void
name|startRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route. It will remain in the list of route definitions return by {@link #getRouteDefinitions()}      * unless you use the {@link #removeRouteDefinitions(java.util.Collection)}      *      * @param route the route to stop      * @throws Exception is thrown if the route could not be stopped for whatever reason      */
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
comment|/**      * Stops the given route. It will remain in the list of route definitions return by {@link #getRouteDefinitions()}      * unless you use the {@link #removeRouteDefinitions(java.util.Collection)}      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be stopped for whatever reason      */
DECL|method|stopRoute (String routeId)
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the current status of the given route      *      * @param routeId the route id      * @return the status for the route      */
DECL|method|getRouteStatus (String routeId)
name|ServiceStatus
name|getRouteStatus
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Returns the current status of the given route      *      * @param route the route      * @return the status for the route      * @deprecated will be removed in Camel 2.3.      */
annotation|@
name|Deprecated
DECL|method|getRouteStatus (RouteDefinition route)
name|ServiceStatus
name|getRouteStatus
parameter_list|(
name|RouteDefinition
name|route
parameter_list|)
function_decl|;
comment|// Properties
comment|//-----------------------------------------------------------------------
comment|/**      * Returns the type converter used to coerce types from one type to another      *      * @return the converter      */
DECL|method|getTypeConverter ()
name|TypeConverter
name|getTypeConverter
parameter_list|()
function_decl|;
comment|/**      * Returns the type converter registry where type converters can be added or looked up      *      * @return the type converter registry      */
DECL|method|getTypeConverterRegistry ()
name|TypeConverterRegistry
name|getTypeConverterRegistry
parameter_list|()
function_decl|;
comment|/**      * Returns the registry used to lookup components by name and type such as the Spring ApplicationContext,      * JNDI or the OSGi Service Registry      *      * @return the registry      */
DECL|method|getRegistry ()
name|Registry
name|getRegistry
parameter_list|()
function_decl|;
comment|/**      * Returns the injector used to instantiate objects by type      *      * @return the injector      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
comment|/**      * Returns the lifecycle strategies used to handle lifecycle notifications      *      * @return the lifecycle strategies      */
DECL|method|getLifecycleStrategies ()
name|List
argument_list|<
name|LifecycleStrategy
argument_list|>
name|getLifecycleStrategies
parameter_list|()
function_decl|;
comment|/**      * Adds the given lifecycle strategy to be used.      *      * @param lifecycleStrategy  the strategy      */
DECL|method|addLifecycleStrategy (LifecycleStrategy lifecycleStrategy)
name|void
name|addLifecycleStrategy
parameter_list|(
name|LifecycleStrategy
name|lifecycleStrategy
parameter_list|)
function_decl|;
comment|/**      * Resolves a language for creating expressions      *      * @param language  name of the language      * @return the resolved language      */
DECL|method|resolveLanguage (String language)
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|language
parameter_list|)
function_decl|;
comment|/**      * Gets a readonly list with the names of the languages currently registered.      *      * @return a readonly list with the names of the the languages      */
DECL|method|getLanguageNames ()
name|List
argument_list|<
name|String
argument_list|>
name|getLanguageNames
parameter_list|()
function_decl|;
comment|/**      * Creates a new ProducerTemplate.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *      * @return the template      */
DECL|method|createProducerTemplate ()
name|ProducerTemplate
name|createProducerTemplate
parameter_list|()
function_decl|;
comment|/**      * Creates a new ConsumerTemplate.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a> as it also applies for ConsumerTemplate.      *      * @return the template      */
DECL|method|createConsumerTemplate ()
name|ConsumerTemplate
name|createConsumerTemplate
parameter_list|()
function_decl|;
comment|/**      * Adds the given interceptor strategy      *      * @param interceptStrategy the strategy      */
DECL|method|addInterceptStrategy (InterceptStrategy interceptStrategy)
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|interceptStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the interceptor strategies      *      * @return the list of current interceptor strategies      */
DECL|method|getInterceptStrategies ()
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
function_decl|;
comment|/**      * Gets the default error handler builder which is inherited by the routes      *      * @return the builder      */
DECL|method|getErrorHandlerBuilder ()
name|ErrorHandlerBuilder
name|getErrorHandlerBuilder
parameter_list|()
function_decl|;
comment|/**      * Sets the default error handler builder which is inherited by the routes      *      * @param errorHandlerBuilder  the builder      */
DECL|method|setErrorHandlerBuilder (ErrorHandlerBuilder errorHandlerBuilder)
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerBuilder
name|errorHandlerBuilder
parameter_list|)
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
comment|/**      * Resolve a data format given its name      *      * @param name the data format name or a reference to it in the {@link Registry}      * @return the resolved data format, or<tt>null</tt> if not found      */
DECL|method|resolveDataFormat (String name)
name|DataFormat
name|resolveDataFormat
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Resolve a data format definition given its name      *      * @param name the data format definition name or a reference to it in the {@link Registry}      * @return the resolved data format definition, or<tt>null</tt> if not found      */
DECL|method|resolveDataFormatDefinition (String name)
name|DataFormatDefinition
name|resolveDataFormatDefinition
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Gets the current data format resolver      *      * @return the resolver      */
DECL|method|getDataFormatResolver ()
name|DataFormatResolver
name|getDataFormatResolver
parameter_list|()
function_decl|;
comment|/**      * Sets a custom data format resolver      *      * @param dataFormatResolver  the resolver      */
DECL|method|setDataFormatResolver (DataFormatResolver dataFormatResolver)
name|void
name|setDataFormatResolver
parameter_list|(
name|DataFormatResolver
name|dataFormatResolver
parameter_list|)
function_decl|;
comment|/**      * Sets the properties that can be referenced in the camel context      *      * @param properties properties      */
DECL|method|setProperties (Map<String, String> properties)
name|void
name|setProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
parameter_list|)
function_decl|;
comment|/**      * Gets the properties that can be referenced in the camel context      *      * @return the properties      */
DECL|method|getProperties ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * Gets the default FactoryFinder which will be used for the loading the factory class from META-INF      *      * @return the default factory finder      */
DECL|method|getDefaultFactoryFinder ()
name|FactoryFinder
name|getDefaultFactoryFinder
parameter_list|()
function_decl|;
comment|/**      * Sets the factory finder resolver to use.      *      * @param resolver the factory finder resolver      */
DECL|method|setFactoryFinderResolver (FactoryFinderResolver resolver)
name|void
name|setFactoryFinderResolver
parameter_list|(
name|FactoryFinderResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Gets the FactoryFinder which will be used for the loading the factory class from META-INF in the given path      *      * @param path the META-INF path      * @return the factory finder      * @throws NoFactoryAvailableException is thrown if a factory could not be found      */
DECL|method|getFactoryFinder (String path)
name|FactoryFinder
name|getFactoryFinder
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|NoFactoryAvailableException
function_decl|;
comment|/**      * Returns the class resolver to be used for loading/lookup of classes.      *      * @return the resolver      */
DECL|method|getClassResolver ()
name|ClassResolver
name|getClassResolver
parameter_list|()
function_decl|;
comment|/**      * Returns the package scanning class resolver      *      * @return the resolver      */
DECL|method|getPackageScanClassResolver ()
name|PackageScanClassResolver
name|getPackageScanClassResolver
parameter_list|()
function_decl|;
comment|/**      * Sets the class resolver to be use      *      * @param resolver the resolver      */
DECL|method|setClassResolver (ClassResolver resolver)
name|void
name|setClassResolver
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Sets the package scanning class resolver to use      *      * @param resolver the resolver      */
DECL|method|setPackageScanClassResolver (PackageScanClassResolver resolver)
name|void
name|setPackageScanClassResolver
parameter_list|(
name|PackageScanClassResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Sets a pluggable service pool to use for {@link Producer} pooling.      *      * @param servicePool the pool      */
DECL|method|setProducerServicePool (ServicePool<Endpoint, Producer> servicePool)
name|void
name|setProducerServicePool
parameter_list|(
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
name|servicePool
parameter_list|)
function_decl|;
comment|/**      * Gets the service pool for {@link Producer} pooling.      *      * @return the service pool      */
DECL|method|getProducerServicePool ()
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|Producer
argument_list|>
name|getProducerServicePool
parameter_list|()
function_decl|;
comment|/**      * Uses a custom node id factory when generating auto assigned ids to the nodes in the route definitions      *      * @param factory  custom factory to use      */
DECL|method|setNodeIdFactory (NodeIdFactory factory)
name|void
name|setNodeIdFactory
parameter_list|(
name|NodeIdFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Gets the node id factory      *      * @return  the node id factory      */
DECL|method|getNodeIdFactory ()
name|NodeIdFactory
name|getNodeIdFactory
parameter_list|()
function_decl|;
comment|/**      * Gets the management strategy      *      * @return the management strategy      */
DECL|method|getManagementStrategy ()
name|ManagementStrategy
name|getManagementStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets the management strategy to use      *      * @param strategy the management strategy      */
DECL|method|setManagementStrategy (ManagementStrategy strategy)
name|void
name|setManagementStrategy
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
function_decl|;
comment|/**      * Gets the default tracer      *      * @return the default tracer      */
DECL|method|getDefaultTracer ()
name|InterceptStrategy
name|getDefaultTracer
parameter_list|()
function_decl|;
comment|/**      * Disables using JMX as {@link org.apache.camel.spi.ManagementStrategy}.      */
DECL|method|disableJMX ()
name|void
name|disableJMX
parameter_list|()
function_decl|;
comment|/**      * Gets the inflight repository      *      * @return the repository      */
DECL|method|getInflightRepository ()
name|InflightRepository
name|getInflightRepository
parameter_list|()
function_decl|;
comment|/**      * Sets a custom inflight repository to use      *      * @param repository the repository      */
DECL|method|setInflightRepository (InflightRepository repository)
name|void
name|setInflightRepository
parameter_list|(
name|InflightRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Gets the the application context class loader which may be helpful for running camel in other containers      *      * @return the application context class loader      */
DECL|method|getApplicationContextClassLoader ()
name|ClassLoader
name|getApplicationContextClassLoader
parameter_list|()
function_decl|;
comment|/**      * Sets the application context class loader      *      * @param classLoader the class loader      */
DECL|method|setApplicationContextClassLoader (ClassLoader classLoader)
name|void
name|setApplicationContextClassLoader
parameter_list|(
name|ClassLoader
name|classLoader
parameter_list|)
function_decl|;
comment|/**      * Gets the current shutdown strategy      *      * @return the strategy      */
DECL|method|getShutdownStrategy ()
name|ShutdownStrategy
name|getShutdownStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom shutdown strategy      *      * @param shutdownStrategy the custom strategy      */
DECL|method|setShutdownStrategy (ShutdownStrategy shutdownStrategy)
name|void
name|setShutdownStrategy
parameter_list|(
name|ShutdownStrategy
name|shutdownStrategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

