begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Set
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
name|ExecutorService
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|AnnotationBasedProcessorFactory
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
name|BeanProxyFactory
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
name|CamelBeanPostProcessor
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
name|DeferServiceFactory
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
name|HeadersMapFactory
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
name|LogListener
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
name|PropertiesComponent
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
name|RouteController
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
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_comment
comment|/**  * Interface used to represent the CamelContext used to configure routes and the  * policies to use during message exchanges between endpoints.  *<p/>  * The CamelContext offers the following methods to control the lifecycle:  *<ul>  *<li>{@link #start()}  - to start (<b>important:</b> the start method is not blocked, see more details  *<a href="http://camel.apache.org/running-camel-standalone-and-have-it-keep-running.html">here</a>)</li>  *<li>{@link #stop()} - to shutdown (will stop all routes/components/endpoints etc and clear internal state/cache)</li>  *<li>{@link #suspend()} - to pause routing messages</li>  *<li>{@link #resume()} - to resume after a suspend</li>  *</ul>  *<p/>  *<b>Notice:</b> {@link #stop()} and {@link #suspend()} will gracefully stop/suspend routes ensuring any messages  * in progress will be given time to complete. See more details at {@link org.apache.camel.spi.ShutdownStrategy}.  *<p/>  * If you are doing a hot restart then it's advised to use the suspend/resume methods which ensure a faster  * restart but also allows any internal state to be kept as is.  * The stop/start approach will do a<i>cold</i> restart of Camel, where all internal state is reset.  *<p/>  * End users are advised to use suspend/resume. Using stop is for shutting down Camel and it's not guaranteed that  * when it's being started again using the start method that Camel will operate consistently.  */
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
comment|/**      * Adapts this {@link org.apache.camel.CamelContext} to the specialized type.      *<p/>      * For example to adapt to<tt>ModelCamelContext</tt>,      * or<tt>SpringCamelContext</tt>, or<tt>CdiCamelContext</tt>, etc.      *      * @param type the type to adapt to      * @return this {@link org.apache.camel.CamelContext} adapted to the given type      */
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
comment|/**      * Gets the extension of the given type.      *      * @param type  the type of the extension      * @return the extension, or<tt>null</tt> if no extension has been installed.      */
DECL|method|getExtension (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Allows to install custom extensions to the Camel context.      *      * @param type   the type of the extension      * @param module the instance of the extension      */
DECL|method|setExtension (Class<T> type, T module)
parameter_list|<
name|T
parameter_list|>
name|void
name|setExtension
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|T
name|module
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
comment|/**      * Sets the name this {@link CamelContext} will be registered in JMX.      */
DECL|method|setManagementName (String name)
name|void
name|setManagementName
parameter_list|(
name|String
name|name
parameter_list|)
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
comment|/**      * Has the given service type already been added to this CamelContext?      *      * @param type the class type      * @return the services instance or empty set.      */
DECL|method|hasServices (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|Set
argument_list|<
name|T
argument_list|>
name|hasServices
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
comment|/**      * Gets a readonly list of names of the components currently registered      *      * @return a readonly list with the names of the components      */
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
name|?
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
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
comment|/**      * Removes all endpoints with the given URI from the {@link org.apache.camel.spi.EndpointRegistry}.      *<p/>      * The endpoints being removed will be stopped first.      *      * @param pattern an uri or pattern to match      * @return a collection of endpoints removed which could be empty if there are no endpoints found for the given<tt>pattern</tt>      * @throws Exception if at least one endpoint could not be stopped      * @see org.apache.camel.support.EndpointHelper#matchEndpoint(CamelContext, String, String) for pattern      */
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
comment|/**      * NOTE: experimental api      *      * @param routeController the route controller      */
DECL|method|setRouteController (RouteController routeController)
name|void
name|setRouteController
parameter_list|(
name|RouteController
name|routeController
parameter_list|)
function_decl|;
comment|/**      * NOTE: experimental api      *      * @return the route controller or null if not set.      */
DECL|method|getRouteController ()
name|RouteController
name|getRouteController
parameter_list|()
function_decl|;
comment|/**      * Method to signal to {@link CamelContext} that the process to initialize setup routes is in progress.      *      * @param done<tt>false</tt> to start the process, call again with<tt>true</tt> to signal its done.      * @see #isSetupRoutes()      */
DECL|method|setupRoutes (boolean done)
name|void
name|setupRoutes
parameter_list|(
name|boolean
name|done
parameter_list|)
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
comment|/**      * Configures the type converter registry to use, where type converters can be added or looked up.      *      * @param typeConverterRegistry the registry to use      */
DECL|method|setTypeConverterRegistry (TypeConverterRegistry typeConverterRegistry)
name|void
name|setTypeConverterRegistry
parameter_list|(
name|TypeConverterRegistry
name|typeConverterRegistry
parameter_list|)
function_decl|;
comment|/**      * Returns the registry used to lookup components by name and type such as SimpleRegistry, Spring ApplicationContext,      * JNDI, or the OSGi Service Registry.      *      * @return the registry      */
DECL|method|getRegistry ()
name|Registry
name|getRegistry
parameter_list|()
function_decl|;
comment|/**      * Returns the registry used to lookup components by name and as the given type      *      * @param type the registry type such as org.apache.camel.impl.JndiRegistry      * @return the registry, or<tt>null</tt> if the given type was not found as a registry implementation      */
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
comment|/**      * Returns the bean post processor used to do any bean customization.      *      * @return the bean post processor.      */
DECL|method|getBeanPostProcessor ()
name|CamelBeanPostProcessor
name|getBeanPostProcessor
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
comment|/**      * Returns the configured properties component or create one if none has been configured.      *      * @return the properties component      */
DECL|method|getPropertiesComponent ()
name|PropertiesComponent
name|getPropertiesComponent
parameter_list|()
function_decl|;
comment|/**      * Returns the configured properties component or create one if none has been configured.      *      * @param autoCreate whether the component should be created if none is configured      * @return the properties component      */
DECL|method|getPropertiesComponent (boolean autoCreate)
name|PropertiesComponent
name|getPropertiesComponent
parameter_list|(
name|boolean
name|autoCreate
parameter_list|)
function_decl|;
comment|/**      * Gets a readonly list with the names of the languages currently registered.      *      * @return a readonly list with the names of the languages      */
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
comment|/**      * Creates a new {@link FluentProducerTemplate} which is<b>started</b> and therefore ready to use right away.      *<p/>      * See this FAQ before use:<a href="http://camel.apache.org/why-does-camel-use-too-many-threads-with-producertemplate.html">      * Why does Camel use too many threads with ProducerTemplate?</a>      *<p/>      *<b>Important:</b> Make sure to call {@link org.apache.camel.FluentProducerTemplate#stop()} when you are done using the template,      * to clean up any resources.      *<p/>      * Will use cache size defined in Camel property with key {@link Exchange#MAXIMUM_CACHE_POOL_SIZE}.      * If no key was defined then it will fallback to a default size of 1000.      * You can also use the {@link org.apache.camel.FluentProducerTemplate#setMaximumCacheSize(int)} method to use a custom value      * before starting the template.      *      * @return the template      * @throws RuntimeCamelException is thrown if error starting the template      */
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
comment|/**      * Creates a new multicast processor which sends an exchange to all the processors.      *      * @param processors the list of processors to send to      * @param executor the executor to use      * @return a multicasting processor      */
DECL|method|createMulticast (Collection<Processor> processors, ExecutorService executor, boolean shutdownExecutorService)
name|AsyncProcessor
name|createMulticast
parameter_list|(
name|Collection
argument_list|<
name|Processor
argument_list|>
name|processors
parameter_list|,
name|ExecutorService
name|executor
parameter_list|,
name|boolean
name|shutdownExecutorService
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
comment|/**      * Gets the default error handler builder which is inherited by the routes      *      * @return the builder      */
DECL|method|getErrorHandlerFactory ()
name|ErrorHandlerFactory
name|getErrorHandlerFactory
parameter_list|()
function_decl|;
comment|/**      * Sets the default error handler builder which is inherited by the routes      *      * @param errorHandlerFactory the builder      */
DECL|method|setErrorHandlerFactory (ErrorHandlerFactory errorHandlerFactory)
name|void
name|setErrorHandlerFactory
parameter_list|(
name|ErrorHandlerFactory
name|errorHandlerFactory
parameter_list|)
function_decl|;
comment|/**      * Gets the default shared thread pool for error handlers which      * leverages this for asynchronous redelivery tasks.      */
DECL|method|getErrorHandlerExecutorService ()
name|ScheduledExecutorService
name|getErrorHandlerExecutorService
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
argument_list|<
name|?
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
argument_list|>
name|getTransformerRegistry
parameter_list|()
function_decl|;
comment|/**      * Resolve a validator given from/to data type.      *      * @param type the data type      * @return the resolved validator, or<tt>null</tt> if not found      */
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
argument_list|<
name|?
extends|extends
name|ValueHolder
argument_list|<
name|String
argument_list|>
argument_list|>
name|getValidatorRegistry
parameter_list|()
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
comment|/**      * Disables using JMX as {@link org.apache.camel.spi.ManagementStrategy}.      *<p/>      *<b>Important:</b> This method must be called<b>before</b> the {@link CamelContext} is started.      *      * @throws IllegalStateException is thrown if the {@link CamelContext} is not in stopped state.      */
DECL|method|disableJMX ()
name|void
name|disableJMX
parameter_list|()
throws|throws
name|IllegalStateException
function_decl|;
comment|/**      * Setup management according to whether JMX is enabled or disabled.      *      * @param options optional parameters to configure {@link org.apache.camel.spi.ManagementAgent}.      */
DECL|method|setupManagement (Map<String, Object> options)
name|void
name|setupManagement
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
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
comment|/**      * Gets the application CamelContext class loader which may be helpful for running camel in other containers      *      * @return the application CamelContext class loader      */
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
comment|/**      * Whether to load custom type converters by scanning classpath.      * This is used for backwards compatibility with Camel 2.x.      * Its recommended to migrate to use fast type converter loading      * by setting<tt>@Converter(loader = true)</tt> on your custom      * type converter classes.      */
DECL|method|isLoadTypeConverters ()
name|Boolean
name|isLoadTypeConverters
parameter_list|()
function_decl|;
comment|/**      * Whether to load custom type converters by scanning classpath.      * This is used for backwards compatibility with Camel 2.x.      * Its recommended to migrate to use fast type converter loading      * by setting<tt>@Converter(loader = true)</tt> on your custom      * type converter classes.      *      * @param loadTypeConverters whether to load custom type converters using classpath scanning.      */
DECL|method|setLoadTypeConverters (Boolean loadTypeConverters)
name|void
name|setLoadTypeConverters
parameter_list|(
name|Boolean
name|loadTypeConverters
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
comment|/**      * Whether to enable using data type on Camel messages.      *<p/>      * Data type are automatic turned on if one ore more routes has been explicit configured with input and output types.      * Otherwise data type is default off.      *      * @return<tt>true</tt> if data type is enabled      */
DECL|method|isUseDataType ()
name|Boolean
name|isUseDataType
parameter_list|()
function_decl|;
comment|/**      * Whether to enable using data type on Camel messages.      *<p/>      * Data type are automatic turned on if one ore more routes has been explicit configured with input and output types.      * Otherwise data type is default off.      *      * @param  useDataType<tt>true</tt> to enable data type on Camel messages.      */
DECL|method|setUseDataType (Boolean useDataType)
name|void
name|setUseDataType
parameter_list|(
name|Boolean
name|useDataType
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
comment|/**      * Gets a list of {@link LogListener}.      */
DECL|method|getLogListeners ()
name|Set
argument_list|<
name|LogListener
argument_list|>
name|getLogListeners
parameter_list|()
function_decl|;
comment|/**      * Adds a {@link LogListener}.      */
DECL|method|addLogListener (LogListener listener)
name|void
name|addLogListener
parameter_list|(
name|LogListener
name|listener
parameter_list|)
function_decl|;
comment|/**      * Sets the global SSL context parameters.      */
DECL|method|setSSLContextParameters (SSLContextParameters sslContextParameters)
name|void
name|setSSLContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
function_decl|;
comment|/**      * Gets the global SSL context parameters if configured.      */
DECL|method|getSSLContextParameters ()
name|SSLContextParameters
name|getSSLContextParameters
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link HeadersMapFactory} to use.      */
DECL|method|getHeadersMapFactory ()
name|HeadersMapFactory
name|getHeadersMapFactory
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link HeadersMapFactory} to be used.      */
DECL|method|setHeadersMapFactory (HeadersMapFactory factory)
name|void
name|setHeadersMapFactory
parameter_list|(
name|HeadersMapFactory
name|factory
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link DeferServiceFactory} to use.      */
DECL|method|getDeferServiceFactory ()
name|DeferServiceFactory
name|getDeferServiceFactory
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link AnnotationBasedProcessorFactory} to use.      */
DECL|method|getAnnotationBasedProcessorFactory ()
name|AnnotationBasedProcessorFactory
name|getAnnotationBasedProcessorFactory
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link BeanProxyFactory} to use.      */
DECL|method|getBeanProxyFactory ()
name|BeanProxyFactory
name|getBeanProxyFactory
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

