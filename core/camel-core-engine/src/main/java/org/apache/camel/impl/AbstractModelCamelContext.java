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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
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
name|AsyncProcessor
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
name|CatalogCamelContext
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
name|engine
operator|.
name|AbstractCamelContext
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
name|engine
operator|.
name|BaseRouteService
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
name|engine
operator|.
name|DefaultTransformerRegistry
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
name|engine
operator|.
name|DefaultValidatorRegistry
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
name|HystrixConfigurationDefinition
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
name|Model
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
name|ModelCamelContext
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
name|Resilience4jConfigurationDefinition
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
name|processor
operator|.
name|MulticastProcessor
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
name|reifier
operator|.
name|dataformat
operator|.
name|DataFormatReifier
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
name|reifier
operator|.
name|transformer
operator|.
name|TransformerReifier
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
name|reifier
operator|.
name|validator
operator|.
name|ValidatorReifier
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
name|CamelContextHelper
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents the context used to configure routes and the policies to use.  */
end_comment

begin_class
DECL|class|AbstractModelCamelContext
specifier|public
specifier|abstract
class|class
name|AbstractModelCamelContext
extends|extends
name|AbstractCamelContext
implements|implements
name|ModelCamelContext
implements|,
name|CatalogCamelContext
block|{
DECL|field|model
specifier|private
specifier|final
name|Model
name|model
init|=
operator|new
name|DefaultModel
argument_list|(
name|this
argument_list|)
decl_stmt|;
comment|/**      * Creates the {@link ModelCamelContext} using      * {@link org.apache.camel.support.DefaultRegistry} as registry.      *<p/>      * Use one of the other constructors to force use an explicit registry.      */
DECL|method|AbstractModelCamelContext ()
specifier|public
name|AbstractModelCamelContext
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the {@link ModelCamelContext} using the given registry      *      * @param registry the registry      */
DECL|method|AbstractModelCamelContext (Registry registry)
specifier|public
name|AbstractModelCamelContext
parameter_list|(
name|Registry
name|registry
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
block|}
DECL|method|AbstractModelCamelContext (boolean init)
specifier|public
name|AbstractModelCamelContext
parameter_list|(
name|boolean
name|init
parameter_list|)
block|{
name|super
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|setDefaultExtension
argument_list|(
name|HealthCheckRegistry
operator|.
name|class
argument_list|,
name|this
operator|::
name|createHealthCheckRegistry
argument_list|)
expr_stmt|;
name|setDefaultExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|,
name|this
operator|::
name|createRuntimeCamelCatalog
argument_list|)
expr_stmt|;
if|if
condition|(
name|init
condition|)
block|{
name|init
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getRouteDefinitions ()
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRouteDefinitions
parameter_list|()
block|{
return|return
name|model
operator|.
name|getRouteDefinitions
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRouteDefinition (String id)
specifier|public
name|RouteDefinition
name|getRouteDefinition
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|model
operator|.
name|getRouteDefinition
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
specifier|public
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
block|{
name|model
operator|.
name|addRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addRouteDefinition (RouteDefinition routeDefinition)
specifier|public
name|void
name|addRouteDefinition
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
block|{
name|model
operator|.
name|addRouteDefinition
argument_list|(
name|routeDefinition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
specifier|public
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
block|{
name|model
operator|.
name|removeRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeRouteDefinition (RouteDefinition routeDefinition)
specifier|public
name|void
name|removeRouteDefinition
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
block|{
name|model
operator|.
name|removeRouteDefinition
argument_list|(
name|routeDefinition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRestDefinitions ()
specifier|public
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRestDefinitions
parameter_list|()
block|{
return|return
name|model
operator|.
name|getRestDefinitions
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|addRestDefinitions (Collection<RestDefinition> restDefinitions, boolean addToRoutes)
specifier|public
name|void
name|addRestDefinitions
parameter_list|(
name|Collection
argument_list|<
name|RestDefinition
argument_list|>
name|restDefinitions
parameter_list|,
name|boolean
name|addToRoutes
parameter_list|)
throws|throws
name|Exception
block|{
name|model
operator|.
name|addRestDefinitions
argument_list|(
name|restDefinitions
argument_list|,
name|addToRoutes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDataFormats (Map<String, DataFormatDefinition> dataFormats)
specifier|public
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
block|{
name|model
operator|.
name|setDataFormats
argument_list|(
name|dataFormats
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormats ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|getDataFormats
parameter_list|()
block|{
return|return
name|model
operator|.
name|getDataFormats
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|resolveDataFormatDefinition (String name)
specifier|public
name|DataFormatDefinition
name|resolveDataFormatDefinition
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|model
operator|.
name|resolveDataFormatDefinition
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getProcessorDefinition (String id)
specifier|public
name|ProcessorDefinition
name|getProcessorDefinition
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|model
operator|.
name|getProcessorDefinition
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getProcessorDefinition (String id, Class<T> type)
specifier|public
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
block|{
return|return
name|model
operator|.
name|getProcessorDefinition
argument_list|(
name|id
argument_list|,
name|type
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setValidators (List<ValidatorDefinition> validators)
specifier|public
name|void
name|setValidators
parameter_list|(
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|validators
parameter_list|)
block|{
name|model
operator|.
name|setValidators
argument_list|(
name|validators
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHystrixConfiguration (String id)
specifier|public
name|HystrixConfigurationDefinition
name|getHystrixConfiguration
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|model
operator|.
name|getHystrixConfiguration
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setHystrixConfiguration (HystrixConfigurationDefinition configuration)
specifier|public
name|void
name|setHystrixConfiguration
parameter_list|(
name|HystrixConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|setHystrixConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setHystrixConfigurations (List<HystrixConfigurationDefinition> configurations)
specifier|public
name|void
name|setHystrixConfigurations
parameter_list|(
name|List
argument_list|<
name|HystrixConfigurationDefinition
argument_list|>
name|configurations
parameter_list|)
block|{
name|model
operator|.
name|setHystrixConfigurations
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addHystrixConfiguration (String id, HystrixConfigurationDefinition configuration)
specifier|public
name|void
name|addHystrixConfiguration
parameter_list|(
name|String
name|id
parameter_list|,
name|HystrixConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|addHystrixConfiguration
argument_list|(
name|id
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getResilience4jConfiguration (String id)
specifier|public
name|Resilience4jConfigurationDefinition
name|getResilience4jConfiguration
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|model
operator|.
name|getResilience4jConfiguration
argument_list|(
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setResilience4jConfiguration (Resilience4jConfigurationDefinition configuration)
specifier|public
name|void
name|setResilience4jConfiguration
parameter_list|(
name|Resilience4jConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|setResilience4jConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setResilience4jConfigurations (List<Resilience4jConfigurationDefinition> configurations)
specifier|public
name|void
name|setResilience4jConfigurations
parameter_list|(
name|List
argument_list|<
name|Resilience4jConfigurationDefinition
argument_list|>
name|configurations
parameter_list|)
block|{
name|model
operator|.
name|setResilience4jConfigurations
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addResilience4jConfiguration (String id, Resilience4jConfigurationDefinition configuration)
specifier|public
name|void
name|addResilience4jConfiguration
parameter_list|(
name|String
name|id
parameter_list|,
name|Resilience4jConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|addResilience4jConfiguration
argument_list|(
name|id
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getValidators ()
specifier|public
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|getValidators
parameter_list|()
block|{
return|return
name|model
operator|.
name|getValidators
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setTransformers (List<TransformerDefinition> transformers)
specifier|public
name|void
name|setTransformers
parameter_list|(
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|transformers
parameter_list|)
block|{
name|model
operator|.
name|setTransformers
argument_list|(
name|transformers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getTransformers ()
specifier|public
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|getTransformers
parameter_list|()
block|{
return|return
name|model
operator|.
name|getTransformers
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getServiceCallConfiguration (String serviceName)
specifier|public
name|ServiceCallConfigurationDefinition
name|getServiceCallConfiguration
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
return|return
name|model
operator|.
name|getServiceCallConfiguration
argument_list|(
name|serviceName
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setServiceCallConfiguration (ServiceCallConfigurationDefinition configuration)
specifier|public
name|void
name|setServiceCallConfiguration
parameter_list|(
name|ServiceCallConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|setServiceCallConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setServiceCallConfigurations (List<ServiceCallConfigurationDefinition> configurations)
specifier|public
name|void
name|setServiceCallConfigurations
parameter_list|(
name|List
argument_list|<
name|ServiceCallConfigurationDefinition
argument_list|>
name|configurations
parameter_list|)
block|{
name|model
operator|.
name|setServiceCallConfigurations
argument_list|(
name|configurations
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addServiceCallConfiguration (String serviceName, ServiceCallConfigurationDefinition configuration)
specifier|public
name|void
name|addServiceCallConfiguration
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|ServiceCallConfigurationDefinition
name|configuration
parameter_list|)
block|{
name|model
operator|.
name|addServiceCallConfiguration
argument_list|(
name|serviceName
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRouteFilterPattern (String include, String exclude)
specifier|public
name|void
name|setRouteFilterPattern
parameter_list|(
name|String
name|include
parameter_list|,
name|String
name|exclude
parameter_list|)
block|{
name|model
operator|.
name|setRouteFilterPattern
argument_list|(
name|include
argument_list|,
name|exclude
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setRouteFilter (Function<RouteDefinition, Boolean> filter)
specifier|public
name|void
name|setRouteFilter
parameter_list|(
name|Function
argument_list|<
name|RouteDefinition
argument_list|,
name|Boolean
argument_list|>
name|filter
parameter_list|)
block|{
name|model
operator|.
name|setRouteFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRouteFilter ()
specifier|public
name|Function
argument_list|<
name|RouteDefinition
argument_list|,
name|Boolean
argument_list|>
name|getRouteFilter
parameter_list|()
block|{
return|return
name|model
operator|.
name|getRouteFilter
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createValidatorRegistry ()
specifier|protected
name|ValidatorRegistry
argument_list|<
name|ValidatorKey
argument_list|>
name|createValidatorRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultValidatorRegistry
name|registry
init|=
operator|new
name|DefaultValidatorRegistry
argument_list|(
name|this
argument_list|)
decl_stmt|;
for|for
control|(
name|ValidatorDefinition
name|def
range|:
name|getValidators
argument_list|()
control|)
block|{
name|Validator
name|validator
init|=
name|ValidatorReifier
operator|.
name|reifier
argument_list|(
name|def
argument_list|)
operator|.
name|createValidator
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
name|createKey
argument_list|(
name|def
argument_list|)
argument_list|,
name|doAddService
argument_list|(
name|validator
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
DECL|method|createKey (ValidatorDefinition def)
specifier|private
name|ValidatorKey
name|createKey
parameter_list|(
name|ValidatorDefinition
name|def
parameter_list|)
block|{
return|return
operator|new
name|ValidatorKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|def
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createTransformerRegistry ()
specifier|protected
name|TransformerRegistry
argument_list|<
name|TransformerKey
argument_list|>
name|createTransformerRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTransformerRegistry
name|registry
init|=
operator|new
name|DefaultTransformerRegistry
argument_list|(
name|this
argument_list|)
decl_stmt|;
for|for
control|(
name|TransformerDefinition
name|def
range|:
name|getTransformers
argument_list|()
control|)
block|{
name|Transformer
name|transformer
init|=
name|TransformerReifier
operator|.
name|reifier
argument_list|(
name|def
argument_list|)
operator|.
name|createTransformer
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
name|createKey
argument_list|(
name|def
argument_list|)
argument_list|,
name|doAddService
argument_list|(
name|transformer
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
DECL|method|createKey (TransformerDefinition def)
specifier|private
name|TransformerKey
name|createKey
parameter_list|(
name|TransformerDefinition
name|def
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|def
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|?
operator|new
name|TransformerKey
argument_list|(
name|def
operator|.
name|getScheme
argument_list|()
argument_list|)
else|:
operator|new
name|TransformerKey
argument_list|(
operator|new
name|DataType
argument_list|(
name|def
operator|.
name|getFromType
argument_list|()
argument_list|)
argument_list|,
operator|new
name|DataType
argument_list|(
name|def
operator|.
name|getToType
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createHealthCheckRegistry ()
specifier|protected
specifier|abstract
name|HealthCheckRegistry
name|createHealthCheckRegistry
parameter_list|()
function_decl|;
DECL|method|createRuntimeCamelCatalog ()
specifier|protected
specifier|abstract
name|RuntimeCamelCatalog
name|createRuntimeCamelCatalog
parameter_list|()
function_decl|;
annotation|@
name|Override
DECL|method|doStartStandardServices ()
specifier|protected
name|void
name|doStartStandardServices
parameter_list|()
block|{
name|super
operator|.
name|doStartStandardServices
argument_list|()
expr_stmt|;
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStartEagerServices ()
specifier|protected
name|void
name|doStartEagerServices
parameter_list|()
block|{
name|getExtension
argument_list|(
name|HealthCheckRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStartEagerServices
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|bindDataFormats ()
specifier|protected
name|void
name|bindDataFormats
parameter_list|()
throws|throws
name|Exception
block|{
comment|// eager lookup data formats and bind to registry so the dataformats can
comment|// be looked up and used
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|e
range|:
name|model
operator|.
name|getDataFormats
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|id
init|=
name|e
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DataFormatDefinition
name|def
init|=
name|e
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Creating Dataformat with id: {} and definition: {}"
argument_list|,
name|id
argument_list|,
name|def
argument_list|)
expr_stmt|;
name|DataFormat
name|df
init|=
name|DataFormatReifier
operator|.
name|reifier
argument_list|(
name|def
argument_list|)
operator|.
name|createDataFormat
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|addService
argument_list|(
name|df
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
name|id
argument_list|,
name|df
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|shutdownRouteService (BaseRouteService routeService)
specifier|protected
specifier|synchronized
name|void
name|shutdownRouteService
parameter_list|(
name|BaseRouteService
name|routeService
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|routeService
operator|instanceof
name|RouteService
condition|)
block|{
name|model
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|remove
argument_list|(
operator|(
operator|(
name|RouteService
operator|)
name|routeService
operator|)
operator|.
name|getRouteDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|shutdownRouteService
argument_list|(
name|routeService
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isStreamCachingInUse ()
specifier|protected
name|boolean
name|isStreamCachingInUse
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|streamCachingInUse
init|=
name|super
operator|.
name|isStreamCachingInUse
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|streamCachingInUse
condition|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|model
operator|.
name|getRouteDefinitions
argument_list|()
control|)
block|{
name|Boolean
name|routeCache
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|this
argument_list|,
name|route
operator|.
name|getStreamCache
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|routeCache
operator|!=
literal|null
operator|&&
name|routeCache
condition|)
block|{
name|streamCachingInUse
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|streamCachingInUse
return|;
block|}
annotation|@
name|Override
DECL|method|startRouteDefinitions ()
specifier|public
name|void
name|startRouteDefinitions
parameter_list|()
throws|throws
name|Exception
block|{
name|model
operator|.
name|startRouteDefinitions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createMulticast (Collection<Processor> processors, ExecutorService executor, boolean shutdownExecutorService)
specifier|public
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
block|{
return|return
operator|new
name|MulticastProcessor
argument_list|(
name|this
argument_list|,
name|processors
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|,
name|executor
argument_list|,
name|shutdownExecutorService
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

