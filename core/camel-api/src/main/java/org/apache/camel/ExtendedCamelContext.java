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
name|BeanProcessorFactory
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
name|UnitOfWorkFactory
import|;
end_import

begin_comment
comment|/**  * Extended {@link CamelContext} which contains the methods and APIs that are not primary intended for Camel end users  * but for SPI, custom components, or more advanced used-cases with Camel.  */
end_comment

begin_interface
DECL|interface|ExtendedCamelContext
specifier|public
interface|interface
name|ExtendedCamelContext
extends|extends
name|CamelContext
block|{
comment|/**      * Method to signal to {@link CamelContext} that the process to initialize setup routes is in progress.      *      * @param done<tt>false</tt> to start the process, call again with<tt>true</tt> to signal its done.      * @see #isSetupRoutes()      */
DECL|method|setupRoutes (boolean done)
name|void
name|setupRoutes
parameter_list|(
name|boolean
name|done
parameter_list|)
function_decl|;
comment|/**      * Indicates whether current thread is setting up route(s) as part of starting Camel from spring/blueprint.      *<p/>      * This can be useful to know by {@link LifecycleStrategy} or the likes, in case      * they need to react differently.      *<p/>      * As the startup procedure of {@link CamelContext} is slightly different when using plain Java versus      * Spring or Blueprint, then we need to know when Spring/Blueprint is setting up the routes, which      * can happen after the {@link CamelContext} itself is in started state, due the asynchronous event nature      * of especially Blueprint.      *      * @return<tt>true</tt> if current thread is setting up route(s), or<tt>false</tt> if not.      */
DECL|method|isSetupRoutes ()
name|boolean
name|isSetupRoutes
parameter_list|()
function_decl|;
comment|/**      * Registers a {@link org.apache.camel.spi.EndpointStrategy callback} to allow you to do custom      * logic when an {@link Endpoint} is about to be registered to the {@link org.apache.camel.spi.EndpointRegistry}.      *<p/>      * When a callback is registered it will be executed on the already registered endpoints allowing you to catch-up      *      * @param strategy callback to be invoked      */
DECL|method|registerEndpointCallback (EndpointStrategy strategy)
name|void
name|registerEndpointCallback
parameter_list|(
name|EndpointStrategy
name|strategy
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
comment|/**      * Returns the package scanning class resolver      *      * @return the resolver      */
DECL|method|getPackageScanClassResolver ()
name|PackageScanClassResolver
name|getPackageScanClassResolver
parameter_list|()
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
comment|/**      * Gets the default FactoryFinder which will be used for the loading the factory class from META-INF      *      * @return the default factory finder      */
DECL|method|getDefaultFactoryFinder ()
name|FactoryFinder
name|getDefaultFactoryFinder
parameter_list|()
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
comment|/**      * Sets the factory finder resolver to use.      *      * @param resolver the factory finder resolver      */
DECL|method|setFactoryFinderResolver (FactoryFinderResolver resolver)
name|void
name|setFactoryFinderResolver
parameter_list|(
name|FactoryFinderResolver
name|resolver
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
comment|/**      * Gets the {@link DeferServiceFactory} to use.      */
DECL|method|getDeferServiceFactory ()
name|DeferServiceFactory
name|getDeferServiceFactory
parameter_list|()
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
comment|/**      * Gets the {@link BeanProcessorFactory} to use.      */
DECL|method|getBeanProcessorFactory ()
name|BeanProcessorFactory
name|getBeanProcessorFactory
parameter_list|()
function_decl|;
comment|/**      * Gets the default shared thread pool for error handlers which      * leverages this for asynchronous redelivery tasks.      */
DECL|method|getErrorHandlerExecutorService ()
name|ScheduledExecutorService
name|getErrorHandlerExecutorService
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
comment|/**      * Gets the {@link org.apache.camel.AsyncProcessor} await manager.      *      * @return the manager      */
DECL|method|getAsyncProcessorAwaitManager ()
name|AsyncProcessorAwaitManager
name|getAsyncProcessorAwaitManager
parameter_list|()
function_decl|;
comment|/**      * Sets a custom {@link org.apache.camel.AsyncProcessor} await manager.      *      * @param manager the manager      */
DECL|method|setAsyncProcessorAwaitManager (AsyncProcessorAwaitManager manager)
name|void
name|setAsyncProcessorAwaitManager
parameter_list|(
name|AsyncProcessorAwaitManager
name|manager
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

