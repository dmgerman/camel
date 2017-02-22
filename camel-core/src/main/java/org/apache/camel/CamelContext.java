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
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Properties
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
name|ScheduledExecutorService
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
name|TimeUnit
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedCamelContextMBean
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedProcessorMBean
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRouteMBean
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
name|ProcessorDefinition
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
name|model
operator|.
name|RoutesDefinition
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
name|AsyncProcessorAwaitManager
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
name|CamelContextNameStrategy
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
name|DataType
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
name|Debugger
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
name|EndpointRegistry
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
name|ExecutorServiceManager
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
name|ManagementMBeanAssembler
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
name|ManagementNameStrategy
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
name|MessageHistoryFactory
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
name|ModelJAXBContextFactory
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
name|ProcessorFactory
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
name|ReloadStrategy
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
name|RestConfiguration
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
name|RoutePolicyFactory
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
name|RouteStartupOrder
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
name|RuntimeEndpointRegistry
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
name|StreamCachingStrategy
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
name|Transformer
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
name|TransformerRegistry
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
name|UnitOfWorkFactory
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
name|UuidGenerator
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
name|Validator
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
name|ValidatorRegistry
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
name|util
operator|.
name|LoadPropertiesException
import|;
end_import

begin_comment
comment|/**  * Interface used to represent the CamelContext used to configure routes and the  * policies to use during message exchanges between endpoints.  *<p/>  * The CamelContext offers the following methods to control the lifecycle:  *<ul>  *<li>{@link #start()}  - to start (<b>important:</b> the start method is not blocked, see more details  *<a href="http://camel.apache.org/running-camel-standalone-and-have-it-keep-running.html">here</a>)</li>  *<li>{@link #stop()} - to shutdown (will stop all routes/components/endpoints etc and clear internal state/cache)</li>  *<li>{@link #suspend()} - to pause routing messages</li>  *<li>{@link #resume()} - to resume after a suspend</li>  *</ul>  *<p/>  *<b>Notice:</b> {@link #stop()} and {@link #suspend()} will gracefully stop/suspend routes ensuring any messages  * in progress will be given time to complete. See more details at {@link org.apache.camel.spi.ShutdownStrategy}.  *<p/>  * If you are doing a hot restart then it's advised to use the suspend/resume methods which ensure a faster  * restart but also allows any internal state to be kept as is.  * The stop/start approach will do a<i>cold</i> restart of Camel, where all internal state is reset.  *<p/>  * End users are advised to use suspend/resume. Using stop is for shutting down Camel and it's not guaranteed that  * when it's being started again using the start method that Camel will operate consistently.  *  * @version   */
end_comment

begin_interface
DECL|interface|CamelContext
specifier|public
interface|interface
name|CamelContext
extends|extends
name|SuspendableService
extends|,
name|RuntimeConfiguration
block|{
comment|/**      * Adapts this {@link org.apache.camel.CamelContext} to the specialized type.      *<p/>      * For example to adapt to {@link org.apache.camel.model.ModelCamelContext},      * or<tt>SpringCamelContext</tt>, or<tt>CdiCamelContext</tt>, etc.      *      * @param type the type to adapt to      * @return this {@link org.apache.camel.CamelContext} adapted to the given type      */
DECL|method|adapt (Class<T> type)
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
name|T
name|adapt
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * If CamelContext during the start procedure was vetoed, and therefore causing Camel to not start.      */
DECL|method|isVetoStarted ()
name|boolean
name|isVetoStarted
parameter_list|()
function_decl|;
comment|/**      * Starts the {@link CamelContext} (<b>important:</b> the start method is not blocked, see more details      *<a href="http://camel.apache.org/running-camel-standalone-and-have-it-keep-running.html">here</a>)</li>.      *<p/>      * See more details at the class-level javadoc of this class.      *      * @throws Exception is thrown if starting failed      */
DECL|method|start ()
name|void
name|start
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Stop and shutdown the {@link CamelContext} (will stop all routes/components/endpoints etc and clear internal state/cache).      *<p/>      * See more details at the class-level javadoc of this class.      *      * @throws Exception is thrown if stopping failed      */
DECL|method|stop ()
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets the name (id) of the this CamelContext.      *      * @return the name      */
DECL|method|getName ()
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Gets the current name strategy      *      * @return name strategy      */
DECL|method|getNameStrategy ()
name|CamelContextNameStrategy
name|getNameStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom name strategy      *      * @param nameStrategy name strategy      */
DECL|method|setNameStrategy (CamelContextNameStrategy nameStrategy)
name|void
name|setNameStrategy
parameter_list|(
name|CamelContextNameStrategy
name|nameStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the current management name strategy      *      * @return management name strategy      */
DECL|method|getManagementNameStrategy ()
name|ManagementNameStrategy
name|getManagementNameStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom management name strategy      *      * @param nameStrategy name strategy      */
DECL|method|setManagementNameStrategy (ManagementNameStrategy nameStrategy)
name|void
name|setManagementNameStrategy
parameter_list|(
name|ManagementNameStrategy
name|nameStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the name this {@link CamelContext} was registered in JMX.      *<p/>      * The reason that a {@link CamelContext} can have a different name in JMX is the fact to remedy for name clash      * in JMX when having multiple {@link CamelContext}s in the same JVM. Camel will automatic reassign and use      * a free name to avoid failing to start.      *      * @return the management name      */
DECL|method|getManagementName ()
name|String
name|getManagementName
parameter_list|()
function_decl|;
comment|/**      * Gets the version of the this CamelContext.      *      * @return the version      */
DECL|method|getVersion ()
name|String
name|getVersion
parameter_list|()
function_decl|;
comment|/**      * Get the status of this CamelContext      *      * @return the status      */
DECL|method|getStatus ()
name|ServiceStatus
name|getStatus
parameter_list|()
function_decl|;
comment|/**      * Gets the uptime in a human readable format      *      * @return the uptime in days/hours/minutes      */
DECL|method|getUptime ()
name|String
name|getUptime
parameter_list|()
function_decl|;
comment|/**      * Gets the uptime in milli seconds      *      * @return the uptime in millis seconds      */
DECL|method|getUptimeMillis ()
name|long
name|getUptimeMillis
parameter_list|()
function_decl|;
comment|// Service Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Adds a service to this CamelContext, which allows this CamelContext to control the lifecycle, ensuring      * the service is stopped when the CamelContext stops.      *<p/>      * The service will also have {@link CamelContext} injected if its {@link CamelContextAware}.      * The service will also be enlisted in JMX for management (if JMX is enabled).      * The service will be started, if its not already started.      *      * @param object the service      * @throws Exception can be thrown when starting the service      */
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
comment|/**      * Adds a service to this CamelContext.      *<p/>      * The service will also have {@link CamelContext} injected if its {@link CamelContextAware}.      * The service will also be enlisted in JMX for management (if JMX is enabled).      * The service will be started, if its not already started.      *<p/>      * If the option<tt>closeOnShutdown</tt> is<tt>true</tt> then this CamelContext will control the lifecycle, ensuring      * the service is stopped when the CamelContext stops.      * If the option<tt>closeOnShutdown</tt> is<tt>false</tt> then this CamelContext will not stop the service when the CamelContext stops.      *      * @param object the service      * @param stopOnShutdown whether to stop the service when this CamelContext shutdown.      * @throws Exception can be thrown when starting the service      */
DECL|method|addService (Object object, boolean stopOnShutdown)
name|void
name|addService
parameter_list|(
name|Object
name|object
parameter_list|,
name|boolean
name|stopOnShutdown
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds a service to this CamelContext.      *<p/>      * The service will also have {@link CamelContext} injected if its {@link CamelContextAware}.      * The service will also be enlisted in JMX for management (if JMX is enabled).      * The service will be started, if its not already started.      *<p/>      * If the option<tt>closeOnShutdown</tt> is<tt>true</tt> then this CamelContext will control the lifecycle, ensuring      * the service is stopped when the CamelContext stops.      * If the option<tt>closeOnShutdown</tt> is<tt>false</tt> then this CamelContext will not stop the service when the CamelContext stops.      *      * @param object the service      * @param stopOnShutdown whether to stop the service when this CamelContext shutdown.      * @param forceStart whether to force starting the service right now, as otherwise the service may be deferred being started      *                   to later using {@link #deferStartService(Object, boolean)}      * @throws Exception can be thrown when starting the service      */
DECL|method|addService (Object object, boolean stopOnShutdown, boolean forceStart)
name|void
name|addService
parameter_list|(
name|Object
name|object
parameter_list|,
name|boolean
name|stopOnShutdown
parameter_list|,
name|boolean
name|forceStart
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes a service from this CamelContext.      *<p/>      * The service is assumed to have been previously added using {@link #addService(Object)} method.      * This method will<b>not</b> change the service lifecycle.      *      * @param object the service      * @throws Exception can be thrown if error removing the service      * @return<tt>true</tt> if the service was removed,<tt>false</tt> if no service existed      */
DECL|method|removeService (Object object)
name|boolean
name|removeService
parameter_list|(
name|Object
name|object
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Has the given service already been added to this CamelContext?      *      * @param object the service      * @return<tt>true</tt> if already added,<tt>false</tt> if not.      */
DECL|method|hasService (Object object)
name|boolean
name|hasService
parameter_list|(
name|Object
name|object
parameter_list|)
function_decl|;
comment|/**      * Has the given service type already been added to this CamelContext?      *      * @param type the class type      * @return the service instance or<tt>null</tt> if not already added.      */
DECL|method|hasService (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|hasService
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Defers starting the service until {@link CamelContext} is (almost started) or started and has initialized all its prior services and routes.      *<p/>      * If {@link CamelContext} is already started then the service is started immediately.      *      * @param object the service      * @param stopOnShutdown whether to stop the service when this CamelContext shutdown. Setting this to<tt>true</tt> will keep a reference to the service in      *                       this {@link CamelContext} until the CamelContext is stopped. So do not use it for short lived services.      * @throws Exception can be thrown when starting the service, which is only attempted if {@link CamelContext} has already been started when calling this method.      */
DECL|method|deferStartService (Object object, boolean stopOnShutdown)
name|void
name|deferStartService
parameter_list|(
name|Object
name|object
parameter_list|,
name|boolean
name|stopOnShutdown
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds the given listener to be invoked when {@link CamelContext} have just been started.      *<p/>      * This allows listeners to do any custom work after the routes and other services have been started and are running.      *<p/><b>Important:</b> The listener will always be invoked, also if the {@link CamelContext} has already been      * started, see the {@link org.apache.camel.StartupListener#onCamelContextStarted(CamelContext, boolean)} method.      *      * @param listener the listener      * @throws Exception can be thrown if {@link CamelContext} is already started and the listener is invoked      *                   and cause an exception to be thrown      */
DECL|method|addStartupListener (StartupListener listener)
name|void
name|addStartupListener
parameter_list|(
name|StartupListener
name|listener
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|// Component Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Adds a component to the context.      *      * @param componentName the name the component is registered as      * @param component     the component      */
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
comment|/**      * Gets a component from the CamelContext by name.      *<p/>      * Notice the returned component will be auto-started. If you do not intend to do that      * then use {@link #getComponent(String, boolean, boolean)}.      *      * @param componentName the name of the component      * @return the component      */
DECL|method|getComponent (String componentName)
name|Component
name|getComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the CamelContext by name.      *<p/>      * Notice the returned component will be auto-started. If you do not intend to do that      * then use {@link #getComponent(String, boolean, boolean)}.      *      * @param name                 the name of the component      * @param autoCreateComponents whether or not the component should      *                             be lazily created if it does not already exist      * @return the component      */
DECL|method|getComponent (String name, boolean autoCreateComponents)
name|Component
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|autoCreateComponents
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the CamelContext by name.      *      * @param name                 the name of the component      * @param autoCreateComponents whether or not the component should      *                             be lazily created if it does not already exist      * @param autoStart            whether to auto start the component if {@link CamelContext} is already started.      * @return the component      */
DECL|method|getComponent (String name, boolean autoCreateComponents, boolean autoStart)
name|Component
name|getComponent
parameter_list|(
name|String
name|name
parameter_list|,
name|boolean
name|autoCreateComponents
parameter_list|,
name|boolean
name|autoStart
parameter_list|)
function_decl|;
comment|/**      * Gets a component from the CamelContext by name and specifying the expected type of component.      *      * @param name          the name to lookup      * @param componentType the expected type      * @return the component      */
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
comment|/**      * Removes a previously added component.      *<p/>      * The component being removed will be stopped first.      *      * @param componentName the component name to remove      * @return the previously added component or null if it had not been previously added.      */
DECL|method|removeComponent (String componentName)
name|Component
name|removeComponent
parameter_list|(
name|String
name|componentName
parameter_list|)
function_decl|;
comment|// Endpoint Management Methods
comment|//-----------------------------------------------------------------------
comment|/**      * Gets the {@link org.apache.camel.spi.EndpointRegistry}      */
DECL|method|getEndpointRegistry ()
name|EndpointRegistry
argument_list|<
name|String
argument_list|>
name|getEndpointRegistry
parameter_list|()
function_decl|;
comment|/**      * Resolves the given name to an {@link Endpoint} of the specified type.      * If the name has a singleton endpoint registered, then the singleton is returned.      * Otherwise, a new {@link Endpoint} is created and registered in the {@link org.apache.camel.spi.EndpointRegistry}.      *      * @param uri the URI of the endpoint      * @return the endpoint      */
DECL|method|getEndpoint (String uri)
name|Endpoint
name|getEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Resolves the given name to an {@link Endpoint} of the specified type.      * If the name has a singleton endpoint registered, then the singleton is returned.      * Otherwise, a new {@link Endpoint} is created and registered in the {@link org.apache.camel.spi.EndpointRegistry}.      *      * @param name         the name of the endpoint      * @param endpointType the expected type      * @return the endpoint      */
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
comment|/**      * Returns a new {@link Collection} of all of the endpoints from the {@link org.apache.camel.spi.EndpointRegistry}      *      * @return all endpoints      */
DECL|method|getEndpoints ()
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|getEndpoints
parameter_list|()
function_decl|;
comment|/**      * Returns a new {@link Map} containing all of the endpoints from the {@link org.apache.camel.spi.EndpointRegistry}      *      * @return map of endpoints      */
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
comment|/**      * Is the given endpoint already registered in the {@link org.apache.camel.spi.EndpointRegistry}      *      * @param uri the URI of the endpoint      * @return the registered endpoint or<tt>null</tt> if not registered      */
DECL|method|hasEndpoint (String uri)
name|Endpoint
name|hasEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
comment|/**      * Adds the endpoint to the {@link org.apache.camel.spi.EndpointRegistry} using the given URI.      *      * @param uri      the URI to be used to resolve this endpoint      * @param endpoint the endpoint to be added to the registry      * @return the old endpoint that was previously registered or<tt>null</tt> if none was registered      * @throws Exception if the new endpoint could not be started or the old endpoint could not be stopped      */
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
comment|/**      * Removes the endpoint from the {@link org.apache.camel.spi.EndpointRegistry}.      *<p/>      * The endpoint being removed will be stopped first.      *      * @param endpoint  the endpoint      * @throws Exception if the endpoint could not be stopped      */
DECL|method|removeEndpoint (Endpoint endpoint)
name|void
name|removeEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes all endpoints with the given URI from the {@link org.apache.camel.spi.EndpointRegistry}.      *<p/>      * The endpoints being removed will be stopped first.      *      * @param pattern an uri or pattern to match      * @return a collection of endpoints removed which could be empty if there are no endpoints found for the given<tt>pattern</tt>      * @throws Exception if at least one endpoint could not be stopped      * @see org.apache.camel.util.EndpointHelper#matchEndpoint(CamelContext, String, String) for pattern      */
DECL|method|removeEndpoints (String pattern)
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|removeEndpoints
parameter_list|(
name|String
name|pattern
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Registers a {@link org.apache.camel.spi.EndpointStrategy callback} to allow you to do custom      * logic when an {@link Endpoint} is about to be registered to the {@link org.apache.camel.spi.EndpointRegistry}.      *<p/>      * When a callback is added it will be executed on the already registered endpoints allowing you to catch-up      *      * @param strategy callback to be invoked      */
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
comment|/**      * Method to signal to {@link CamelContext} that the process to initialize setup routes is in progress.      *      * @param done<tt>false</tt> to start the process, call again with<tt>true</tt> to signal its done.      * @see #isSetupRoutes()      */
DECL|method|setupRoutes (boolean done)
name|void
name|setupRoutes
parameter_list|(
name|boolean
name|done
parameter_list|)
function_decl|;
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
comment|/**      * Returns a list of the current REST definitions      *      * @return list of the current REST definitions      */
DECL|method|getRestDefinitions ()
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRestDefinitions
parameter_list|()
function_decl|;
comment|/**      * Adds a collection of rest definitions to the context      *      * @param restDefinitions the rest(s) definition to add      */
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
comment|/**      * Sets a custom {@link org.apache.camel.spi.RestConfiguration}      *      * @param restConfiguration the REST configuration      */
DECL|method|setRestConfiguration (RestConfiguration restConfiguration)
name|void
name|setRestConfiguration
parameter_list|(
name|RestConfiguration
name|restConfiguration
parameter_list|)
function_decl|;
comment|/**      * Gets the default REST configuration      *      * @return the configuration, or<tt>null</tt> if none has been configured.      */
DECL|method|getRestConfiguration ()
name|RestConfiguration
name|getRestConfiguration
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.RestConfiguration}      *      * @param restConfiguration the REST configuration      */
DECL|method|addRestConfiguration (RestConfiguration restConfiguration)
name|void
name|addRestConfiguration
parameter_list|(
name|RestConfiguration
name|restConfiguration
parameter_list|)
function_decl|;
comment|/**      * Gets the REST configuration for the given component      *      * @param component the component name to get the configuration      * @param defaultIfNotFound determine if the default configuration is returned if there isn't a       *        specific configuration for the given component        * @return the configuration, or<tt>null</tt> if none has been configured.      */
DECL|method|getRestConfiguration (String component, boolean defaultIfNotFound)
name|RestConfiguration
name|getRestConfiguration
parameter_list|(
name|String
name|component
parameter_list|,
name|boolean
name|defaultIfNotFound
parameter_list|)
function_decl|;
comment|/**      * Gets all the RestConfiguration's      */
DECL|method|getRestConfigurations ()
name|Collection
argument_list|<
name|RestConfiguration
argument_list|>
name|getRestConfigurations
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
comment|/**      * Returns the order in which the route inputs was started.      *<p/>      * The order may not be according to the startupOrder defined on the route.      * For example a route could be started manually later, or new routes added at runtime.      *      * @return a list in the order how routes was started      */
DECL|method|getRouteStartupOrder ()
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|getRouteStartupOrder
parameter_list|()
function_decl|;
comment|/**      * Returns the current routes in this CamelContext      *      * @return the current routes      */
DECL|method|getRoutes ()
name|List
argument_list|<
name|Route
argument_list|>
name|getRoutes
parameter_list|()
function_decl|;
comment|/**      * Gets the route with the given id      *      * @param id id of the route      * @return the route or<tt>null</tt> if not found      */
DECL|method|getRoute (String id)
name|Route
name|getRoute
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Gets the processor from any of the routes which with the given id      *      * @param id id of the processor      * @return the processor or<tt>null</tt> if not found      */
DECL|method|getProcessor (String id)
name|Processor
name|getProcessor
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Gets the processor from any of the routes which with the given id      *      * @param id id of the processor      * @param type the processor type      * @return the processor or<tt>null</tt> if not found      * @throws java.lang.ClassCastException is thrown if the type is not correct type      */
DECL|method|getProcessor (String id, Class<T> type)
parameter_list|<
name|T
extends|extends
name|Processor
parameter_list|>
name|T
name|getProcessor
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
comment|/**      * Gets the managed processor client api from any of the routes which with the given id      *      * @param id id of the processor      * @param type the managed processor type from the {@link org.apache.camel.api.management.mbean} package.      * @return the processor or<tt>null</tt> if not found      * @throws IllegalArgumentException if the type is not compliant      */
DECL|method|getManagedProcessor (String id, Class<T> type)
parameter_list|<
name|T
extends|extends
name|ManagedProcessorMBean
parameter_list|>
name|T
name|getManagedProcessor
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
comment|/**      * Gets the managed route client api with the given route id      *      * @param routeId id of the route      * @param type the managed route type from the {@link org.apache.camel.api.management.mbean} package.      * @return the route or<tt>null</tt> if not found      * @throws IllegalArgumentException if the type is not compliant      */
DECL|method|getManagedRoute (String routeId, Class<T> type)
parameter_list|<
name|T
extends|extends
name|ManagedRouteMBean
parameter_list|>
name|T
name|getManagedRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Gets the managed Camel CamelContext client api      */
DECL|method|getManagedCamelContext ()
name|ManagedCamelContextMBean
name|getManagedCamelContext
parameter_list|()
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
comment|/**      * Adds a collection of routes to this CamelContext using the given builder      * to build them.      *<p/>      *<b>Important:</b> The added routes will<b>only</b> be started, if {@link CamelContext}      * is already started. You may want to check the state of {@link CamelContext} before      * adding the routes, using the {@link org.apache.camel.CamelContext#getStatus()} method.      *<p/>      *<b>Important:</b> Each route in the same {@link org.apache.camel.CamelContext} must have an<b>unique</b> route id.      * If you use the API from {@link org.apache.camel.CamelContext} or {@link org.apache.camel.model.ModelCamelContext} to add routes, then any      * new routes which has a route id that matches an old route, then the old route is replaced by the new route.      *      * @param builder the builder which will create the routes and add them to this CamelContext      * @throws Exception if the routes could not be created for whatever reason      */
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
comment|/**      * Loads a collection of route definitions from the given {@link java.io.InputStream}.      *      * @param is input stream with the route(s) definition to add      * @throws Exception if the route definitions could not be loaded for whatever reason      * @return the route definitions      */
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
comment|/**      * Loads a collection of rest definitions from the given {@link java.io.InputStream}.      *      * @param is input stream with the rest(s) definition to add      * @throws Exception if the rest definitions could not be loaded for whatever reason      * @return the rest definitions      */
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
comment|/**      * Adds a collection of route definitions to the context      *      * @param routeDefinitions the route(s) definition to add      * @throws Exception if the route definitions could not be created for whatever reason      */
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
comment|/**      * Add a route definition to the context      *      * @param routeDefinition the route definition to add      * @throws Exception if the route definition could not be created for whatever reason      */
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
comment|/**      * Removes a collection of route definitions from the CamelContext - stopping any previously running      * routes if any of them are actively running      *      * @param routeDefinitions route(s) definitions to remove      * @throws Exception if the route definitions could not be removed for whatever reason      */
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
comment|/**      * Removes a route definition from the CamelContext - stopping any previously running      * routes if any of them are actively running      *      * @param routeDefinition route definition to remove      * @throws Exception if the route definition could not be removed for whatever reason      */
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
comment|/**      * Starts all the routes which currently is not started.      *      * @throws Exception is thrown if a route could not be started for whatever reason      */
DECL|method|startAllRoutes ()
name|void
name|startAllRoutes
parameter_list|()
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
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy}.      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String)      */
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
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout.      *      * @param routeId the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String, long, java.util.concurrent.TimeUnit)      */
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit)
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout       * and optional abortAfterTimeout mode.      *      * @param routeId the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @param abortAfterTimeout should abort shutdown after timeout      * @return<tt>true</tt> if the route is stopped before the timeout      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String, long, java.util.concurrent.TimeUnit)      */
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
name|boolean
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|boolean
name|abortAfterTimeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Shutdown and<b>removes</b> the given route using {@link org.apache.camel.spi.ShutdownStrategy}.      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be shutdown for whatever reason      * @deprecated use {@link #stopRoute(String)} and {@link #removeRoute(String)}      */
annotation|@
name|Deprecated
DECL|method|shutdownRoute (String routeId)
name|void
name|shutdownRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Shutdown and<b>removes</b> the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout.      *      * @param routeId  the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @throws Exception is thrown if the route could not be shutdown for whatever reason      * @deprecated use {@link #stopRoute(String, long, java.util.concurrent.TimeUnit)} and {@link #removeRoute(String)}      */
annotation|@
name|Deprecated
DECL|method|shutdownRoute (String routeId, long timeout, TimeUnit timeUnit)
name|void
name|shutdownRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes the given route (the route<b>must</b> be stopped before it can be removed).      *<p/>      * A route which is removed will be unregistered from JMX, have its services stopped/shutdown and the route      * definition etc. will also be removed. All the resources related to the route will be stopped and cleared.      *<p/>      *<b>Important:</b> When removing a route, the {@link Endpoint}s which are in the static cache of      * {@link org.apache.camel.spi.EndpointRegistry} and are<b>only</b> used by the route (not used by other routes)      * will also be removed. But {@link Endpoint}s which may have been created as part of routing messages by the route,      * and those endpoints are enlisted in the dynamic cache of {@link org.apache.camel.spi.EndpointRegistry} are      *<b>not</b> removed. To remove those dynamic kind of endpoints, use the {@link #removeEndpoints(String)} method.      * If not removing those endpoints, they will be kept in the dynamic cache of {@link org.apache.camel.spi.EndpointRegistry},      * but my eventually be removed (evicted) when they have not been in use for a longer period of time; and the      * dynamic cache upper limit is hit, and it evicts the least used endpoints.      *<p/>      * End users can use this method to remove unwanted routes or temporary routes which no longer is in demand.      *      * @param routeId the route id      * @return<tt>true</tt> if the route was removed,<tt>false</tt> if the route could not be removed because its not stopped      * @throws Exception is thrown if the route could not be shutdown for whatever reason      */
DECL|method|removeRoute (String routeId)
name|boolean
name|removeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resumes the given route if it has been previously suspended      *<p/>      * If the route does<b>not</b> support suspension the route will be started instead      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be resumed for whatever reason      */
DECL|method|resumeRoute (String routeId)
name|void
name|resumeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the given route using {@link org.apache.camel.spi.ShutdownStrategy}.      *<p/>      * Suspending a route is more gently than stopping, as the route consumers will be suspended (if they support)      * otherwise the consumers will be stopped.      *<p/>      * By suspending the route services will be kept running (if possible) and therefore its faster to resume the route.      *<p/>      * If the route does<b>not</b> support suspension the route will be stopped instead      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be suspended for whatever reason      */
DECL|method|suspendRoute (String routeId)
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout.      *<p/>      * Suspending a route is more gently than stopping, as the route consumers will be suspended (if they support)      * otherwise the consumers will be stopped.      *<p/>      * By suspending the route services will be kept running (if possible) and therefore its faster to resume the route.      *<p/>      * If the route does<b>not</b> support suspension the route will be stopped instead      *      * @param routeId  the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @throws Exception is thrown if the route could not be suspended for whatever reason      */
DECL|method|suspendRoute (String routeId, long timeout, TimeUnit timeUnit)
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
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
comment|/**      * Indicates whether current thread is starting route(s).      *<p/>      * This can be useful to know by {@link LifecycleStrategy} or the likes, in case      * they need to react differently.      *      * @return<tt>true</tt> if current thread is starting route(s), or<tt>false</tt> if not.      */
DECL|method|isStartingRoutes ()
name|boolean
name|isStartingRoutes
parameter_list|()
function_decl|;
comment|/**      * Indicates whether current thread is setting up route(s) as part of starting Camel from spring/blueprint.      *<p/>      * This can be useful to know by {@link LifecycleStrategy} or the likes, in case      * they need to react differently.      *<p/>      * As the startup procedure of {@link CamelContext} is slightly different when using plain Java versus      * Spring or Blueprint, then we need to know when Spring/Blueprint is setting up the routes, which      * can happen after the {@link CamelContext} itself is in started state, due the asynchronous event nature      * of especially Blueprint.      *      * @return<tt>true</tt> if current thread is setting up route(s), or<tt>false</tt> if not.      */
DECL|method|isSetupRoutes ()
name|boolean
name|isSetupRoutes
parameter_list|()
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
comment|/**      * Returns the registry used to lookup components by name and as the given type      *      * @param type the registry type such as {@link org.apache.camel.impl.JndiRegistry}      * @return the registry, or<tt>null</tt> if the given type was not found as a registry implementation      */
DECL|method|getRegistry (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getRegistry
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Returns the injector used to instantiate objects by type      *      * @return the injector      */
DECL|method|getInjector ()
name|Injector
name|getInjector
parameter_list|()
function_decl|;
comment|/**      * Returns the management mbean assembler      *      * @return the mbean assembler      */
DECL|method|getManagementMBeanAssembler ()
name|ManagementMBeanAssembler
name|getManagementMBeanAssembler
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
comment|/**      * Adds the given lifecycle strategy to be used.      *      * @param lifecycleStrategy the strategy      */
DECL|method|addLifecycleStrategy (LifecycleStrategy lifecycleStrategy)
name|void
name|addLifecycleStrategy
parameter_list|(
name|LifecycleStrategy
name|lifecycleStrategy
parameter_list|)
function_decl|;
comment|/**      * Resolves a language for creating expressions      *      * @param language name of the language      * @return the resolved language      */
DECL|method|resolveLanguage (String language)
name|Language
name|resolveLanguage
parameter_list|(
name|String
name|language
parameter_list|)
function_decl|;
comment|/**      * Parses the given text and resolve any property placeholders - using {{key}}.      *      * @param text the text such as an endpoint uri or the likes      * @return the text with resolved property placeholders      * @throws Exception is thrown if property placeholders was used and there was an error resolving them      */
DECL|method|resolvePropertyPlaceholders (String text)
name|String
name|resolvePropertyPlaceholders
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the configured property placeholder prefix token if and only if the CamelContext has      * property placeholder abilities, otherwise returns {@code null}.      *       * @return the prefix token or {@code null}      */
DECL|method|getPropertyPrefixToken ()
name|String
name|getPropertyPrefixToken
parameter_list|()
function_decl|;
comment|/**      * Returns the configured property placeholder suffix token if and only if the CamelContext has      * property placeholder abilities, otherwise returns {@code null}.      *       * @return the suffix token or {@code null}      */
DECL|method|getPropertySuffixToken ()
name|String
name|getPropertySuffixToken
parameter_list|()
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
comment|/**      * Creates a new {@link ProducerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *<p/>      *<b>Important:</b> Make sure to call {@link org.apache.camel.ProducerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *<p/>      * Will use cache size defined in Camel property with key {@link Exchange#MAXIMUM_CACHE_POOL_SIZE}.      * If no key was defined then it will fallback to a default size of 1000.      * You can also use the {@link org.apache.camel.ProducerTemplate#setMaximumCacheSize(int)} method to use a custom value      * before starting the template.      *      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createProducerTemplate ()
name|ProducerTemplate
name|createProducerTemplate
parameter_list|()
function_decl|;
comment|/**      * Creates a new {@link ProducerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *<p/>      *<b>Important:</b> Make sure to call {@link ProducerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *      * @param maximumCacheSize the maximum cache size      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createProducerTemplate (int maximumCacheSize)
name|ProducerTemplate
name|createProducerTemplate
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Creates a new {@link FluentProducerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *<p/>      *<b>Important:</b> Make sure to call {@link org.apache.camel.ProducerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *<p/>      * Will use cache size defined in Camel property with key {@link Exchange#MAXIMUM_CACHE_POOL_SIZE}.      * If no key was defined then it will fallback to a default size of 1000.      * You can also use the {@link org.apache.camel.FluentProducerTemplate#setMaximumCacheSize(int)} method to use a custom value      * before starting the template.      *      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createFluentProducerTemplate ()
name|FluentProducerTemplate
name|createFluentProducerTemplate
parameter_list|()
function_decl|;
comment|/**      * Creates a new {@link FluentProducerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *<p/>      *<b>Important:</b> Make sure to call {@link FluentProducerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *      * @param maximumCacheSize the maximum cache size      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createFluentProducerTemplate (int maximumCacheSize)
name|FluentProducerTemplate
name|createFluentProducerTemplate
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
function_decl|;
comment|/**      * Creates a new {@link ConsumerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a> as it also applies for ConsumerTemplate.      *<p/>      *<b>Important:</b> Make sure to call {@link ConsumerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *<p/>      * Will use cache size defined in Camel property with key {@link Exchange#MAXIMUM_CACHE_POOL_SIZE}.      * If no key was defined then it will fallback to a default size of 1000.      * You can also use the {@link org.apache.camel.ConsumerTemplate#setMaximumCacheSize(int)} method to use a custom value      * before starting the template.      *      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createConsumerTemplate ()
name|ConsumerTemplate
name|createConsumerTemplate
parameter_list|()
function_decl|;
comment|/**      * Creates a new {@link ConsumerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a> as it also applies for ConsumerTemplate.      *<p/>      *<b>Important:</b> Make sure to call {@link ConsumerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *      * @param maximumCacheSize the maximum cache size      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
DECL|method|createConsumerTemplate (int maximumCacheSize)
name|ConsumerTemplate
name|createConsumerTemplate
parameter_list|(
name|int
name|maximumCacheSize
parameter_list|)
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
comment|/**      * Gets the default error handler builder which is inherited by the routes      *      * @return the builder      * @deprecated The return type will be switched to {@link ErrorHandlerFactory} in Camel 3.0      */
annotation|@
name|Deprecated
DECL|method|getErrorHandlerBuilder ()
name|ErrorHandlerBuilder
name|getErrorHandlerBuilder
parameter_list|()
function_decl|;
comment|/**      * Sets the default error handler builder which is inherited by the routes      *      * @param errorHandlerBuilder the builder      */
DECL|method|setErrorHandlerBuilder (ErrorHandlerFactory errorHandlerBuilder)
name|void
name|setErrorHandlerBuilder
parameter_list|(
name|ErrorHandlerFactory
name|errorHandlerBuilder
parameter_list|)
function_decl|;
comment|/**      * Gets the default shared thread pool for error handlers which      * leverages this for asynchronous redelivery tasks.      */
DECL|method|getErrorHandlerExecutorService ()
name|ScheduledExecutorService
name|getErrorHandlerExecutorService
parameter_list|()
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
comment|/**      * Creates the given data format given its name.      *      * @param name the data format name or a reference to a data format factory in the {@link Registry}      * @return the resolved data format, or<tt>null</tt> if not found      */
DECL|method|createDataFormat (String name)
name|DataFormat
name|createDataFormat
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
comment|/**      * Sets a custom data format resolver      *      * @param dataFormatResolver the resolver      */
DECL|method|setDataFormatResolver (DataFormatResolver dataFormatResolver)
name|void
name|setDataFormatResolver
parameter_list|(
name|DataFormatResolver
name|dataFormatResolver
parameter_list|)
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
comment|/**      * Resolve a transformer given a scheme      *      * @param model data model name.      * @return the resolved transformer, or<tt>null</tt> if not found      */
DECL|method|resolveTransformer (String model)
name|Transformer
name|resolveTransformer
parameter_list|(
name|String
name|model
parameter_list|)
function_decl|;
comment|/**      * Resolve a transformer given from/to data type.      *      * @param from from data type      * @param to to data type      * @return the resolved transformer, or<tt>null</tt> if not found      */
DECL|method|resolveTransformer (DataType from, DataType to)
name|Transformer
name|resolveTransformer
parameter_list|(
name|DataType
name|from
parameter_list|,
name|DataType
name|to
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link org.apache.camel.spi.TransformerRegistry}      * @return the TransformerRegistry      */
DECL|method|getTransformerRegistry ()
name|TransformerRegistry
name|getTransformerRegistry
parameter_list|()
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
comment|/**      * Gets the validators that can be referenced in the routes.      *      * @return the validators available      */
DECL|method|getValidators ()
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|getValidators
parameter_list|()
function_decl|;
comment|/**      * Resolve a validator given from/to data type.      *      * @param from the data type      * @return the resolved validator, or<tt>null</tt> if not found      */
DECL|method|resolveValidator (DataType type)
name|Validator
name|resolveValidator
parameter_list|(
name|DataType
name|type
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link org.apache.camel.spi.ValidatorRegistry}      * @return the ValidatorRegistry      */
DECL|method|getValidatorRegistry ()
name|ValidatorRegistry
name|getValidatorRegistry
parameter_list|()
function_decl|;
comment|/**      * @deprecated use {@link #setGlobalOptions(Map) setGlobalOptions(Map<String,String>) instead}.      */
annotation|@
name|Deprecated
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
comment|/**      * Sets global options that can be referenced in the camel context      *<p/>      *<b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs      * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.      * For property placeholders use {@link #resolvePropertyPlaceholders(String)} method and see more details      * at the<a href="http://camel.apache.org/using-propertyplaceholder.html">property placeholder</a> documentation.      *      * @param globalOptions global options that can be referenced in the camel context      */
DECL|method|setGlobalOptions (Map<String, String> globalOptions)
name|void
name|setGlobalOptions
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|globalOptions
parameter_list|)
function_decl|;
comment|/**      * @deprecated use {@link #getGlobalOptions()} instead.      */
annotation|@
name|Deprecated
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
comment|/**      * Gets global options that can be referenced in the camel context.      *<p/>      *<b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs      * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.      * For property placeholders use {@link #resolvePropertyPlaceholders(String)} method and see more details      * at the<a href="http://camel.apache.org/using-propertyplaceholder.html">property placeholder</a> documentation.      *      * @return global options for this context      */
DECL|method|getGlobalOptions ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getGlobalOptions
parameter_list|()
function_decl|;
comment|/**      * @deprecated use {@link #getGlobalOption(String)} instead.      */
DECL|method|getProperty (String key)
name|String
name|getProperty
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
comment|/**      * Gets the global option value that can be referenced in the camel context      *<p/>      *<b>Important:</b> This has nothing to do with property placeholders, and is just a plain set of key/value pairs      * which are used to configure global options on CamelContext, such as a maximum debug logging length etc.      * For property placeholders use {@link #resolvePropertyPlaceholders(String)} method and see more details      * at the<a href="http://camel.apache.org/using-propertyplaceholder.html">property placeholder</a> documentation.      *      * @return the string value of the global option      */
DECL|method|getGlobalOption (String key)
name|String
name|getGlobalOption
parameter_list|(
name|String
name|key
parameter_list|)
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
comment|/**      * Sets a pluggable service pool to use for {@link PollingConsumer} pooling.      *      * @param servicePool the pool      */
DECL|method|setPollingConsumerServicePool (ServicePool<Endpoint, PollingConsumer> servicePool)
name|void
name|setPollingConsumerServicePool
parameter_list|(
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|PollingConsumer
argument_list|>
name|servicePool
parameter_list|)
function_decl|;
comment|/**      * Gets the service pool for {@link Producer} pooling.      *      * @return the service pool      */
DECL|method|getPollingConsumerServicePool ()
name|ServicePool
argument_list|<
name|Endpoint
argument_list|,
name|PollingConsumer
argument_list|>
name|getPollingConsumerServicePool
parameter_list|()
function_decl|;
comment|/**      * Uses a custom node id factory when generating auto assigned ids to the nodes in the route definitions      *      * @param factory custom factory to use      */
DECL|method|setNodeIdFactory (NodeIdFactory factory)
name|void
name|setNodeIdFactory
parameter_list|(
name|NodeIdFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Gets the node id factory      *      * @return the node id factory      */
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
comment|/**      * Sets a custom tracer to be used as the default tracer.      *<p/>      *<b>Note:</b> This must be set before any routes are created,      * changing the default tracer for existing routes is not supported.      *      * @param tracer the custom tracer to use as default tracer      */
DECL|method|setDefaultTracer (InterceptStrategy tracer)
name|void
name|setDefaultTracer
parameter_list|(
name|InterceptStrategy
name|tracer
parameter_list|)
function_decl|;
comment|/**      * Gets the default backlog tracer      *      * @return the default backlog tracer      */
DECL|method|getDefaultBacklogTracer ()
name|InterceptStrategy
name|getDefaultBacklogTracer
parameter_list|()
function_decl|;
comment|/**      * Sets a custom backlog tracer to be used as the default backlog tracer.      *<p/>      *<b>Note:</b> This must be set before any routes are created,      * changing the default backlog tracer for existing routes is not supported.      *      * @param backlogTracer the custom tracer to use as default backlog tracer      */
DECL|method|setDefaultBacklogTracer (InterceptStrategy backlogTracer)
name|void
name|setDefaultBacklogTracer
parameter_list|(
name|InterceptStrategy
name|backlogTracer
parameter_list|)
function_decl|;
comment|/**      * Gets the default backlog debugger      *      * @return the default backlog debugger      */
DECL|method|getDefaultBacklogDebugger ()
name|InterceptStrategy
name|getDefaultBacklogDebugger
parameter_list|()
function_decl|;
comment|/**      * Sets a custom backlog debugger to be used as the default backlog debugger.      *<p/>      *<b>Note:</b> This must be set before any routes are created,      * changing the default backlog debugger for existing routes is not supported.      *      * @param backlogDebugger the custom debugger to use as default backlog debugger      */
DECL|method|setDefaultBacklogDebugger (InterceptStrategy backlogDebugger)
name|void
name|setDefaultBacklogDebugger
parameter_list|(
name|InterceptStrategy
name|backlogDebugger
parameter_list|)
function_decl|;
comment|/**      * Disables using JMX as {@link org.apache.camel.spi.ManagementStrategy}.      *<p/>      *<b>Important:</b> This method must be called<b>before</b> the {@link CamelContext} is started.      *      * @throws IllegalStateException is thrown if the {@link CamelContext} is not in stopped state.      */
DECL|method|disableJMX ()
name|void
name|disableJMX
parameter_list|()
throws|throws
name|IllegalStateException
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
comment|/**      * Gets the {@link org.apache.camel.AsyncProcessor} await manager.      *      * @return the manager      */
DECL|method|getAsyncProcessorAwaitManager ()
name|AsyncProcessorAwaitManager
name|getAsyncProcessorAwaitManager
parameter_list|()
function_decl|;
comment|/**      * Sets a custom  {@link org.apache.camel.AsyncProcessor} await manager.      *      * @param manager the manager      */
DECL|method|setAsyncProcessorAwaitManager (AsyncProcessorAwaitManager manager)
name|void
name|setAsyncProcessorAwaitManager
parameter_list|(
name|AsyncProcessorAwaitManager
name|manager
parameter_list|)
function_decl|;
comment|/**      * Gets the the application CamelContext class loader which may be helpful for running camel in other containers      *      * @return the application CamelContext class loader      */
DECL|method|getApplicationContextClassLoader ()
name|ClassLoader
name|getApplicationContextClassLoader
parameter_list|()
function_decl|;
comment|/**      * Sets the application CamelContext class loader      *      * @param classLoader the class loader      */
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
comment|/**      * Gets the current {@link org.apache.camel.spi.ExecutorServiceManager}      *      * @return the manager      */
DECL|method|getExecutorServiceManager ()
name|ExecutorServiceManager
name|getExecutorServiceManager
parameter_list|()
function_decl|;
comment|/**      * Gets the current {@link org.apache.camel.spi.ExecutorServiceStrategy}      *      * @return the manager      * @deprecated use {@link #getExecutorServiceManager()}      */
annotation|@
name|Deprecated
DECL|method|getExecutorServiceStrategy ()
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|ExecutorServiceStrategy
name|getExecutorServiceStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.ExecutorServiceManager}      *      * @param executorServiceManager the custom manager      */
DECL|method|setExecutorServiceManager (ExecutorServiceManager executorServiceManager)
name|void
name|setExecutorServiceManager
parameter_list|(
name|ExecutorServiceManager
name|executorServiceManager
parameter_list|)
function_decl|;
comment|/**      * Gets the current {@link org.apache.camel.spi.ProcessorFactory}      *      * @return the factory, can be<tt>null</tt> if no custom factory has been set      */
DECL|method|getProcessorFactory ()
name|ProcessorFactory
name|getProcessorFactory
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.ProcessorFactory}      *      * @param processorFactory the custom factory      */
DECL|method|setProcessorFactory (ProcessorFactory processorFactory)
name|void
name|setProcessorFactory
parameter_list|(
name|ProcessorFactory
name|processorFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the current {@link org.apache.camel.spi.MessageHistoryFactory}      *      * @return the factory      */
DECL|method|getMessageHistoryFactory ()
name|MessageHistoryFactory
name|getMessageHistoryFactory
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.MessageHistoryFactory}      *      * @param messageHistoryFactory the custom factory      */
DECL|method|setMessageHistoryFactory (MessageHistoryFactory messageHistoryFactory)
name|void
name|setMessageHistoryFactory
parameter_list|(
name|MessageHistoryFactory
name|messageHistoryFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the current {@link Debugger}      *      * @return the debugger      */
DECL|method|getDebugger ()
name|Debugger
name|getDebugger
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link Debugger}      *      * @param debugger the debugger      */
DECL|method|setDebugger (Debugger debugger)
name|void
name|setDebugger
parameter_list|(
name|Debugger
name|debugger
parameter_list|)
function_decl|;
comment|/**      * Gets the current {@link UuidGenerator}      *      * @return the uuidGenerator      */
DECL|method|getUuidGenerator ()
name|UuidGenerator
name|getUuidGenerator
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link UuidGenerator} (should only be set once)       *      * @param uuidGenerator the UUID Generator      */
DECL|method|setUuidGenerator (UuidGenerator uuidGenerator)
name|void
name|setUuidGenerator
parameter_list|(
name|UuidGenerator
name|uuidGenerator
parameter_list|)
function_decl|;
comment|/**      * Whether or not type converters should be loaded lazy      *      * @return<tt>true</tt> to load lazy,<tt>false</tt> to load on startup      * @deprecated this option is no longer supported, will be removed in a future Camel release.      */
annotation|@
name|Deprecated
DECL|method|isLazyLoadTypeConverters ()
name|Boolean
name|isLazyLoadTypeConverters
parameter_list|()
function_decl|;
comment|/**      * Sets whether type converters should be loaded lazy      *      * @param lazyLoadTypeConverters<tt>true</tt> to load lazy,<tt>false</tt> to load on startup      * @deprecated this option is no longer supported, will be removed in a future Camel release.      */
annotation|@
name|Deprecated
DECL|method|setLazyLoadTypeConverters (Boolean lazyLoadTypeConverters)
name|void
name|setLazyLoadTypeConverters
parameter_list|(
name|Boolean
name|lazyLoadTypeConverters
parameter_list|)
function_decl|;
comment|/**      * Whether or not type converter statistics is enabled.      *<p/>      * By default the type converter utilization statistics is disabled.      *<b>Notice:</b> If enabled then there is a slight performance impact under very heavy load.      *      * @return<tt>true</tt> if enabled,<tt>false</tt> if disabled (default).      */
DECL|method|isTypeConverterStatisticsEnabled ()
name|Boolean
name|isTypeConverterStatisticsEnabled
parameter_list|()
function_decl|;
comment|/**      * Sets whether or not type converter statistics is enabled.      *<p/>      * By default the type converter utilization statistics is disabled.      *<b>Notice:</b> If enabled then there is a slight performance impact under very heavy load.      *<p/>      * You can enable/disable the statistics at runtime using the      * {@link org.apache.camel.spi.TypeConverterRegistry#getStatistics()#setTypeConverterStatisticsEnabled(Boolean)} method,      * or from JMX on the {@link org.apache.camel.api.management.mbean.ManagedTypeConverterRegistryMBean} mbean.      *      * @param typeConverterStatisticsEnabled<tt>true</tt> to enable,<tt>false</tt> to disable      */
DECL|method|setTypeConverterStatisticsEnabled (Boolean typeConverterStatisticsEnabled)
name|void
name|setTypeConverterStatisticsEnabled
parameter_list|(
name|Boolean
name|typeConverterStatisticsEnabled
parameter_list|)
function_decl|;
comment|/**      * Whether or not<a href="http://www.slf4j.org/api/org/slf4j/MDC.html">MDC</a> logging is being enabled.      *      * @return<tt>true</tt> if MDC logging is enabled      */
DECL|method|isUseMDCLogging ()
name|Boolean
name|isUseMDCLogging
parameter_list|()
function_decl|;
comment|/**      * Set whether<a href="http://www.slf4j.org/api/org/slf4j/MDC.html">MDC</a> is enabled.      *      * @param useMDCLogging<tt>true</tt> to enable MDC logging,<tt>false</tt> to disable      */
DECL|method|setUseMDCLogging (Boolean useMDCLogging)
name|void
name|setUseMDCLogging
parameter_list|(
name|Boolean
name|useMDCLogging
parameter_list|)
function_decl|;
comment|/**      * Whether or not breadcrumb is enabled.      *      * @return<tt>true</tt> if breadcrumb is enabled      */
DECL|method|isUseBreadcrumb ()
name|Boolean
name|isUseBreadcrumb
parameter_list|()
function_decl|;
comment|/**      * Set whether breadcrumb is enabled.      *      * @param useBreadcrumb<tt>true</tt> to enable breadcrumb,<tt>false</tt> to disable      */
DECL|method|setUseBreadcrumb (Boolean useBreadcrumb)
name|void
name|setUseBreadcrumb
parameter_list|(
name|Boolean
name|useBreadcrumb
parameter_list|)
function_decl|;
comment|/**      * Resolves a component's default name from its java type.      *<p/>      * A component may be used with a non default name such as<tt>activemq</tt>,<tt>wmq</tt> for the JMS component.      * This method can resolve the default component name by its java type.      *      * @param javaType the FQN name of the java type      * @return the default component name.      */
DECL|method|resolveComponentDefaultName (String javaType)
name|String
name|resolveComponentDefaultName
parameter_list|(
name|String
name|javaType
parameter_list|)
function_decl|;
comment|/**      * Find information about all the Camel components available in the classpath and {@link org.apache.camel.spi.Registry}.      *      * @return a map with the component name, and value with component details.      * @throws LoadPropertiesException is thrown if error during classpath discovery of the components      * @throws IOException is thrown if error during classpath discovery of the components      */
DECL|method|findComponents ()
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
function_decl|;
comment|/**      * Find information about all the EIPs from camel-core.      *      * @return a map with node id, and value with EIP details.      * @throws LoadPropertiesException is thrown if error during classpath discovery of the EIPs      * @throws IOException is thrown if error during classpath discovery of the EIPs      */
DECL|method|findEips ()
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findEips
parameter_list|()
throws|throws
name|LoadPropertiesException
throws|,
name|IOException
function_decl|;
comment|/**      * Returns the HTML documentation for the given Camel component      *      * @return the HTML or<tt>null</tt> if the component is<b>not</b> built with HTML document included.      * @deprecated use camel-catalog instead      */
annotation|@
name|Deprecated
DECL|method|getComponentDocumentation (String componentName)
name|String
name|getComponentDocumentation
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the component and endpoint parameters for the given component name.      *      * @return the json or<tt>null</tt> if the component is<b>not</b> built with JSon schema support      */
DECL|method|getComponentParameterJsonSchema (String componentName)
name|String
name|getComponentParameterJsonSchema
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link DataFormat} parameters for the given data format name.      *      * @return the json or<tt>null</tt> if the data format does not exist      */
DECL|method|getDataFormatParameterJsonSchema (String dataFormatName)
name|String
name|getDataFormatParameterJsonSchema
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link Language} parameters for the given language name.      *      * @return the json or<tt>null</tt> if the language does not exist      */
DECL|method|getLanguageParameterJsonSchema (String languageName)
name|String
name|getLanguageParameterJsonSchema
parameter_list|(
name|String
name|languageName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the EIP parameters for the given EIP name.      *      * @return the json or<tt>null</tt> if the EIP does not exist      */
DECL|method|getEipParameterJsonSchema (String eipName)
name|String
name|getEipParameterJsonSchema
parameter_list|(
name|String
name|eipName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns a JSON schema representation of the EIP parameters for the given EIP by its id.      *      * @param nameOrId the name of the EIP ({@link NamedNode#getShortName()} or a node id to refer to a specific node from the routes.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if the eipName or the id was not found      */
DECL|method|explainEipJson (String nameOrId, boolean includeAllOptions)
name|String
name|explainEipJson
parameter_list|(
name|String
name|nameOrId
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the component parameters (not endpoint parameters) for the given component by its id.      *      * @param componentName the name of the component.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if the component was not found      */
DECL|method|explainComponentJson (String componentName, boolean includeAllOptions)
name|String
name|explainComponentJson
parameter_list|(
name|String
name|componentName
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the component parameters (not endpoint parameters) for the given component by its id.      *      * @param dataFormat the data format instance.      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json      */
DECL|method|explainDataFormatJson (String dataFormatName, DataFormat dataFormat, boolean includeAllOptions)
name|String
name|explainDataFormatJson
parameter_list|(
name|String
name|dataFormatName
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Returns a JSON schema representation of the endpoint parameters for the given endpoint uri.      *      * @param uri the endpoint uri      * @param includeAllOptions whether to include non configured options also (eg default options)      * @return the json or<tt>null</tt> if uri parameters is invalid, or the component is<b>not</b> built with JSon schema support      */
DECL|method|explainEndpointJson (String uri, boolean includeAllOptions)
name|String
name|explainEndpointJson
parameter_list|(
name|String
name|uri
parameter_list|,
name|boolean
name|includeAllOptions
parameter_list|)
function_decl|;
comment|/**      * Creates a JSON representation of all the<b>static</b> and<b>dynamic</b> configured endpoints defined in the given route(s).      *      * @param routeId for a particular route, or<tt>null</tt> for all routes      * @return a JSON string      */
DECL|method|createRouteStaticEndpointJson (String routeId)
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Creates a JSON representation of all the<b>static</b> (and possible<b>dynamic</b>) configured endpoints defined in the given route(s).      *      * @param routeId for a particular route, or<tt>null</tt> for all routes      * @param includeDynamic whether to include dynamic endpoints      * @return a JSON string      */
DECL|method|createRouteStaticEndpointJson (String routeId, boolean includeDynamic)
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|String
name|routeId
parameter_list|,
name|boolean
name|includeDynamic
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link StreamCachingStrategy} to use.      */
DECL|method|getStreamCachingStrategy ()
name|StreamCachingStrategy
name|getStreamCachingStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link StreamCachingStrategy} to use.      */
DECL|method|setStreamCachingStrategy (StreamCachingStrategy streamCachingStrategy)
name|void
name|setStreamCachingStrategy
parameter_list|(
name|StreamCachingStrategy
name|streamCachingStrategy
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link UnitOfWorkFactory} to use.      */
DECL|method|getUnitOfWorkFactory ()
name|UnitOfWorkFactory
name|getUnitOfWorkFactory
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link UnitOfWorkFactory} to use.      */
DECL|method|setUnitOfWorkFactory (UnitOfWorkFactory unitOfWorkFactory)
name|void
name|setUnitOfWorkFactory
parameter_list|(
name|UnitOfWorkFactory
name|unitOfWorkFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link org.apache.camel.spi.RuntimeEndpointRegistry} to use, or<tt>null</tt> if none is in use.      */
DECL|method|getRuntimeEndpointRegistry ()
name|RuntimeEndpointRegistry
name|getRuntimeEndpointRegistry
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.RuntimeEndpointRegistry} to use.      */
DECL|method|setRuntimeEndpointRegistry (RuntimeEndpointRegistry runtimeEndpointRegistry)
name|void
name|setRuntimeEndpointRegistry
parameter_list|(
name|RuntimeEndpointRegistry
name|runtimeEndpointRegistry
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link org.apache.camel.spi.RestRegistry} to use      */
DECL|method|getRestRegistry ()
name|RestRegistry
name|getRestRegistry
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.spi.RestRegistry} to use.      */
DECL|method|setRestRegistry (RestRegistry restRegistry)
name|void
name|setRestRegistry
parameter_list|(
name|RestRegistry
name|restRegistry
parameter_list|)
function_decl|;
comment|/**      * Adds the given route policy factory      *      * @param routePolicyFactory the factory      */
DECL|method|addRoutePolicyFactory (RoutePolicyFactory routePolicyFactory)
name|void
name|addRoutePolicyFactory
parameter_list|(
name|RoutePolicyFactory
name|routePolicyFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the route policy factories      *      * @return the list of current route policy factories      */
DECL|method|getRoutePolicyFactories ()
name|List
argument_list|<
name|RoutePolicyFactory
argument_list|>
name|getRoutePolicyFactories
parameter_list|()
function_decl|;
comment|/**      * Returns the JAXB Context factory used to create Models.      *      * @return the JAXB Context factory used to create Models.      */
DECL|method|getModelJAXBContextFactory ()
name|ModelJAXBContextFactory
name|getModelJAXBContextFactory
parameter_list|()
function_decl|;
comment|/**      * Sets a custom JAXB Context factory to be used      *      * @param modelJAXBContextFactory a JAXB Context factory      */
DECL|method|setModelJAXBContextFactory (ModelJAXBContextFactory modelJAXBContextFactory)
name|void
name|setModelJAXBContextFactory
parameter_list|(
name|ModelJAXBContextFactory
name|modelJAXBContextFactory
parameter_list|)
function_decl|;
comment|/**      * Returns the {@link ReloadStrategy} if in use.      *      * @return the strategy, or<tt>null</tt> if none has been configured.      */
DECL|method|getReloadStrategy ()
name|ReloadStrategy
name|getReloadStrategy
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link ReloadStrategy} to be used      */
DECL|method|setReloadStrategy (ReloadStrategy reloadStrategy)
name|void
name|setReloadStrategy
parameter_list|(
name|ReloadStrategy
name|reloadStrategy
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

