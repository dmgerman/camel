begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|NoFactoryAvailableException
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
name|PollingConsumer
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
name|Producer
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
name|TypeConverter
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
name|health
operator|.
name|HealthCheckRegistry
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
name|impl
operator|.
name|converter
operator|.
name|DefaultTypeConverter
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
name|impl
operator|.
name|health
operator|.
name|DefaultHealthCheckRegistry
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
name|impl
operator|.
name|transformer
operator|.
name|TransformerKey
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
name|impl
operator|.
name|validator
operator|.
name|ValidatorKey
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
name|runtimecatalog
operator|.
name|RuntimeCamelCatalog
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
name|runtimecatalog
operator|.
name|impl
operator|.
name|DefaultRuntimeCamelCatalog
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
name|BeanRepository
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
name|ComponentResolver
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
name|LanguageResolver
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
name|DefaultRegistry
import|;
end_import

begin_comment
comment|/**  * Represents the context used to configure routes and the policies to use.  */
end_comment

begin_class
DECL|class|DefaultCamelContext
specifier|public
class|class
name|DefaultCamelContext
extends|extends
name|AbstractCamelContext
block|{
comment|/**      * Creates the {@link CamelContext} using {@link DefaultRegistry} as registry.      *<p/>      * Use one of the other constructors to force use an explicit registry.      */
DECL|method|DefaultCamelContext ()
specifier|public
name|DefaultCamelContext
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * Creates the {@link CamelContext} using the given {@link BeanRepository}      * as first-choice repository, and the {@link org.apache.camel.support.SimpleRegistry} as fallback, via      * the {@link DefaultRegistry} implementation.      *      * @param repository the bean repository.      */
DECL|method|DefaultCamelContext (BeanRepository repository)
specifier|public
name|DefaultCamelContext
parameter_list|(
name|BeanRepository
name|repository
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|DefaultRegistry
argument_list|(
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link CamelContext} using the given JNDI context as the registry      *      * @param jndiContext the JNDI context      * @deprecated create a new {@link JndiRegistry} and use the constructor that accepts this registry.      */
annotation|@
name|Deprecated
DECL|method|DefaultCamelContext (Context jndiContext)
specifier|public
name|DefaultCamelContext
parameter_list|(
name|Context
name|jndiContext
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|JndiRegistry
argument_list|(
name|jndiContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link CamelContext} using the given registry      *      * @param registry the registry      */
DECL|method|DefaultCamelContext (Registry registry)
specifier|public
name|DefaultCamelContext
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link CamelContext} and allows to control whether the context      * should automatic initialize or not.      *<p/>      * This is used by some Camel components such as camel-cdi and camel-blueprint, however      * this constructor is not intended for regular Camel end users.      *      * @param init whether to automatic initialize.      */
DECL|method|DefaultCamelContext (boolean init)
specifier|public
name|DefaultCamelContext
parameter_list|(
name|boolean
name|init
parameter_list|)
block|{
name|super
argument_list|(
name|init
argument_list|)
expr_stmt|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createTypeConverter ()
specifier|protected
name|TypeConverter
name|createTypeConverter
parameter_list|()
block|{
comment|// lets use the new fast type converter registry
return|return
operator|new
name|DefaultTypeConverter
argument_list|(
name|this
argument_list|,
name|getPackageScanClassResolver
argument_list|()
argument_list|,
name|getInjector
argument_list|()
argument_list|,
name|getDefaultFactoryFinder
argument_list|()
argument_list|,
name|isLoadTypeConverters
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createTypeConverterRegistry ()
specifier|protected
name|TypeConverterRegistry
name|createTypeConverterRegistry
parameter_list|()
block|{
name|TypeConverter
name|typeConverter
init|=
name|getTypeConverter
argument_list|()
decl_stmt|;
if|if
condition|(
name|typeConverter
operator|instanceof
name|TypeConverterRegistry
condition|)
block|{
return|return
operator|(
name|TypeConverterRegistry
operator|)
name|typeConverter
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createInjector ()
specifier|protected
name|Injector
name|createInjector
parameter_list|()
block|{
name|FactoryFinder
name|finder
init|=
name|getDefaultFactoryFinder
argument_list|()
decl_stmt|;
try|try
block|{
return|return
operator|(
name|Injector
operator|)
name|finder
operator|.
name|newInstance
argument_list|(
literal|"Injector"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoFactoryAvailableException
name|e
parameter_list|)
block|{
comment|// lets use the default injector
return|return
operator|new
name|DefaultInjector
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
comment|/**      * Lazily create a default bean post processor      */
DECL|method|createBeanPostProcessor ()
specifier|protected
name|CamelBeanPostProcessor
name|createBeanPostProcessor
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelBeanPostProcessor
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createComponentResolver ()
specifier|protected
name|ComponentResolver
name|createComponentResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultComponentResolver
argument_list|()
return|;
block|}
comment|/**      * Lazily create a default implementation      */
DECL|method|createRegistry ()
specifier|protected
name|Registry
name|createRegistry
parameter_list|()
block|{
return|return
operator|new
name|DefaultRegistry
argument_list|()
return|;
block|}
DECL|method|createUuidGenerator ()
specifier|protected
name|UuidGenerator
name|createUuidGenerator
parameter_list|()
block|{
return|return
operator|new
name|DefaultUuidGenerator
argument_list|()
return|;
block|}
DECL|method|createModelJAXBContextFactory ()
specifier|protected
name|ModelJAXBContextFactory
name|createModelJAXBContextFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultModelJAXBContextFactory
argument_list|()
return|;
block|}
DECL|method|createNodeIdFactory ()
specifier|protected
name|NodeIdFactory
name|createNodeIdFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultNodeIdFactory
argument_list|()
return|;
block|}
DECL|method|createFactoryFinderResolver ()
specifier|protected
name|FactoryFinderResolver
name|createFactoryFinderResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultFactoryFinderResolver
argument_list|()
return|;
block|}
DECL|method|createClassResolver ()
specifier|protected
name|ClassResolver
name|createClassResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultClassResolver
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createProcessorFactory ()
specifier|protected
name|ProcessorFactory
name|createProcessorFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultProcessorFactory
argument_list|()
return|;
block|}
DECL|method|createDataFormatResolver ()
specifier|protected
name|DataFormatResolver
name|createDataFormatResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultDataFormatResolver
argument_list|()
return|;
block|}
DECL|method|createMessageHistoryFactory ()
specifier|protected
name|MessageHistoryFactory
name|createMessageHistoryFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultMessageHistoryFactory
argument_list|()
return|;
block|}
DECL|method|createInflightRepository ()
specifier|protected
name|InflightRepository
name|createInflightRepository
parameter_list|()
block|{
return|return
operator|new
name|DefaultInflightRepository
argument_list|()
return|;
block|}
DECL|method|createAsyncProcessorAwaitManager ()
specifier|protected
name|AsyncProcessorAwaitManager
name|createAsyncProcessorAwaitManager
parameter_list|()
block|{
return|return
operator|new
name|DefaultAsyncProcessorAwaitManager
argument_list|()
return|;
block|}
DECL|method|createRouteController ()
specifier|protected
name|RouteController
name|createRouteController
parameter_list|()
block|{
return|return
operator|new
name|DefaultRouteController
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createHealthCheckRegistry ()
specifier|protected
name|HealthCheckRegistry
name|createHealthCheckRegistry
parameter_list|()
block|{
return|return
operator|new
name|DefaultHealthCheckRegistry
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createShutdownStrategy ()
specifier|protected
name|ShutdownStrategy
name|createShutdownStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultShutdownStrategy
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createPackageScanClassResolver ()
specifier|protected
name|PackageScanClassResolver
name|createPackageScanClassResolver
parameter_list|()
block|{
name|PackageScanClassResolver
name|packageScanClassResolver
decl_stmt|;
comment|// use WebSphere specific resolver if running on WebSphere
if|if
condition|(
name|WebSpherePackageScanClassResolver
operator|.
name|isWebSphereClassLoader
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Using WebSphere specific PackageScanClassResolver"
argument_list|)
expr_stmt|;
name|packageScanClassResolver
operator|=
operator|new
name|WebSpherePackageScanClassResolver
argument_list|(
literal|"META-INF/services/org/apache/camel/TypeConverter"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|packageScanClassResolver
operator|=
operator|new
name|DefaultPackageScanClassResolver
argument_list|()
expr_stmt|;
block|}
return|return
name|packageScanClassResolver
return|;
block|}
DECL|method|createExecutorServiceManager ()
specifier|protected
name|ExecutorServiceManager
name|createExecutorServiceManager
parameter_list|()
block|{
return|return
operator|new
name|DefaultExecutorServiceManager
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createProducerServicePool ()
specifier|protected
name|ServicePool
argument_list|<
name|Producer
argument_list|>
name|createProducerServicePool
parameter_list|()
block|{
return|return
operator|new
name|ServicePool
argument_list|<>
argument_list|(
name|Endpoint
operator|::
name|createProducer
argument_list|,
name|Producer
operator|::
name|getEndpoint
argument_list|,
literal|100
argument_list|)
return|;
block|}
DECL|method|createPollingConsumerServicePool ()
specifier|protected
name|ServicePool
argument_list|<
name|PollingConsumer
argument_list|>
name|createPollingConsumerServicePool
parameter_list|()
block|{
return|return
operator|new
name|ServicePool
argument_list|<>
argument_list|(
name|Endpoint
operator|::
name|createPollingConsumer
argument_list|,
name|PollingConsumer
operator|::
name|getEndpoint
argument_list|,
literal|100
argument_list|)
return|;
block|}
DECL|method|createUnitOfWorkFactory ()
specifier|protected
name|UnitOfWorkFactory
name|createUnitOfWorkFactory
parameter_list|()
block|{
return|return
operator|new
name|DefaultUnitOfWorkFactory
argument_list|()
return|;
block|}
DECL|method|createRuntimeCamelCatalog ()
specifier|protected
name|RuntimeCamelCatalog
name|createRuntimeCamelCatalog
parameter_list|()
block|{
return|return
operator|new
name|DefaultRuntimeCamelCatalog
argument_list|(
name|this
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|createCamelContextNameStrategy ()
specifier|protected
name|CamelContextNameStrategy
name|createCamelContextNameStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultCamelContextNameStrategy
argument_list|()
return|;
block|}
DECL|method|createManagementNameStrategy ()
specifier|protected
name|ManagementNameStrategy
name|createManagementNameStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultManagementNameStrategy
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createHeadersMapFactory ()
specifier|protected
name|HeadersMapFactory
name|createHeadersMapFactory
parameter_list|()
block|{
return|return
operator|new
name|HeadersMapFactoryResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createLanguageResolver ()
specifier|protected
name|LanguageResolver
name|createLanguageResolver
parameter_list|()
block|{
return|return
operator|new
name|DefaultLanguageResolver
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createRestRegistry ()
specifier|protected
name|RestRegistry
name|createRestRegistry
parameter_list|()
block|{
return|return
operator|new
name|RestRegistryFactoryResolver
argument_list|()
operator|.
name|resolve
argument_list|(
name|this
argument_list|)
operator|.
name|createRegistry
argument_list|()
return|;
block|}
DECL|method|createEndpointRegistry (Map<EndpointKey, Endpoint> endpoints)
specifier|protected
name|EndpointRegistry
argument_list|<
name|EndpointKey
argument_list|>
name|createEndpointRegistry
parameter_list|(
name|Map
argument_list|<
name|EndpointKey
argument_list|,
name|Endpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
return|return
operator|new
name|DefaultEndpointRegistry
argument_list|(
name|this
argument_list|,
name|endpoints
argument_list|)
return|;
block|}
DECL|method|createValidatorRegistry (List<ValidatorDefinition> validators)
specifier|protected
name|ValidatorRegistry
argument_list|<
name|ValidatorKey
argument_list|>
name|createValidatorRegistry
parameter_list|(
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|validators
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultValidatorRegistry
argument_list|(
name|this
argument_list|,
name|validators
argument_list|)
return|;
block|}
DECL|method|createTransformerRegistry (List<TransformerDefinition> transformers)
specifier|protected
name|TransformerRegistry
argument_list|<
name|TransformerKey
argument_list|>
name|createTransformerRegistry
parameter_list|(
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|transformers
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultTransformerRegistry
argument_list|(
name|this
argument_list|,
name|transformers
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createStreamCachingStrategy ()
specifier|protected
name|StreamCachingStrategy
name|createStreamCachingStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultStreamCachingStrategy
argument_list|()
return|;
block|}
block|}
end_class

end_unit

