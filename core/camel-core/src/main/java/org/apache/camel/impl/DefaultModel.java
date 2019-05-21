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
name|ArrayList
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|ConcurrentHashMap
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
name|ExtendedCamelContext
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
name|FailedToStartRouteException
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
name|ModelHelper
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
name|ProcessorDefinitionHelper
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
name|RouteDefinitionHelper
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
name|reifier
operator|.
name|RouteReifier
import|;
end_import

begin_class
DECL|class|DefaultModel
specifier|public
class|class
name|DefaultModel
implements|implements
name|Model
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|routeDefinitions
specifier|private
specifier|final
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|restDefinitions
specifier|private
specifier|final
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|restDefinitions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|dataFormats
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|DataFormatDefinition
argument_list|>
name|dataFormats
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|transformers
specifier|private
name|List
argument_list|<
name|TransformerDefinition
argument_list|>
name|transformers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|validators
specifier|private
name|List
argument_list|<
name|ValidatorDefinition
argument_list|>
name|validators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|serviceCallConfigurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ServiceCallConfigurationDefinition
argument_list|>
name|serviceCallConfigurations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|hystrixConfigurations
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|HystrixConfigurationDefinition
argument_list|>
name|hystrixConfigurations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|DefaultModel (CamelContext camelContext)
specifier|public
name|DefaultModel
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|addRouteDefinitions (InputStream is)
specifier|public
name|void
name|addRouteDefinitions
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
name|RoutesDefinition
name|def
init|=
name|ModelHelper
operator|.
name|loadRoutesDefinition
argument_list|(
name|camelContext
argument_list|,
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
name|addRouteDefinitions
argument_list|(
name|def
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
specifier|public
specifier|synchronized
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
if|if
condition|(
name|routeDefinitions
operator|==
literal|null
operator|||
name|routeDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|removeRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeDefinitions
operator|.
name|addAll
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
if|if
condition|(
name|shouldStartRoutes
argument_list|()
condition|)
block|{
name|startRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
block|}
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
name|addRouteDefinitions
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|routeDefinition
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|removeRouteDefinitions (Collection<RouteDefinition> routeDefinitions)
specifier|public
specifier|synchronized
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
for|for
control|(
name|RouteDefinition
name|routeDefinition
range|:
name|routeDefinitions
control|)
block|{
name|removeRouteDefinition
argument_list|(
name|routeDefinition
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|removeRouteDefinition (RouteDefinition routeDefinition)
specifier|public
specifier|synchronized
name|void
name|removeRouteDefinition
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
block|{
name|RouteDefinition
name|toBeRemoved
init|=
name|routeDefinition
decl_stmt|;
name|String
name|id
init|=
name|routeDefinition
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
comment|// remove existing route
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|removeRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|toBeRemoved
operator|=
name|getRouteDefinition
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|routeDefinitions
operator|.
name|remove
argument_list|(
name|toBeRemoved
argument_list|)
expr_stmt|;
block|}
DECL|method|getRouteDefinitions ()
specifier|public
specifier|synchronized
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|getRouteDefinitions
parameter_list|()
block|{
return|return
name|routeDefinitions
return|;
block|}
DECL|method|getRouteDefinition (String id)
specifier|public
specifier|synchronized
name|RouteDefinition
name|getRouteDefinition
parameter_list|(
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routeDefinitions
control|)
block|{
if|if
condition|(
name|route
operator|.
name|idOrCreate
argument_list|(
name|camelContext
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
return|return
name|route
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRestDefinitions ()
specifier|public
specifier|synchronized
name|List
argument_list|<
name|RestDefinition
argument_list|>
name|getRestDefinitions
parameter_list|()
block|{
return|return
name|restDefinitions
return|;
block|}
DECL|method|addRestDefinitions (InputStream is, boolean addToRoutes)
specifier|public
name|void
name|addRestDefinitions
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|boolean
name|addToRoutes
parameter_list|)
throws|throws
name|Exception
block|{
name|RestsDefinition
name|rests
init|=
name|ModelHelper
operator|.
name|loadRestsDefinition
argument_list|(
name|camelContext
argument_list|,
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|rests
operator|!=
literal|null
condition|)
block|{
name|addRestDefinitions
argument_list|(
name|rests
operator|.
name|getRests
argument_list|()
argument_list|,
name|addToRoutes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addRestDefinitions (Collection<RestDefinition> restDefinitions, boolean addToRoutes)
specifier|public
specifier|synchronized
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
if|if
condition|(
name|restDefinitions
operator|==
literal|null
operator|||
name|restDefinitions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|restDefinitions
operator|.
name|addAll
argument_list|(
name|restDefinitions
argument_list|)
expr_stmt|;
if|if
condition|(
name|addToRoutes
condition|)
block|{
comment|// rests are also routes so need to add them there too
for|for
control|(
specifier|final
name|RestDefinition
name|restDefinition
range|:
name|restDefinitions
control|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routeDefinitions
init|=
name|restDefinition
operator|.
name|asRouteDefinition
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|addRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
block|}
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
if|if
condition|(
name|serviceName
operator|==
literal|null
condition|)
block|{
name|serviceName
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|serviceCallConfigurations
operator|.
name|get
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
name|serviceCallConfigurations
operator|.
name|put
argument_list|(
literal|""
argument_list|,
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
if|if
condition|(
name|configurations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ServiceCallConfigurationDefinition
name|configuration
range|:
name|configurations
control|)
block|{
name|serviceCallConfigurations
operator|.
name|put
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
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
name|serviceCallConfigurations
operator|.
name|put
argument_list|(
name|serviceName
argument_list|,
name|configuration
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
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
literal|""
expr_stmt|;
block|}
return|return
name|hystrixConfigurations
operator|.
name|get
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
name|hystrixConfigurations
operator|.
name|put
argument_list|(
literal|""
argument_list|,
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
if|if
condition|(
name|configurations
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|HystrixConfigurationDefinition
name|configuration
range|:
name|configurations
control|)
block|{
name|hystrixConfigurations
operator|.
name|put
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
block|}
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
name|hystrixConfigurations
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
comment|// lookup type and create the data format from it
name|DataFormatDefinition
name|type
init|=
name|lookup
argument_list|(
name|camelContext
argument_list|,
name|name
argument_list|,
name|DataFormatDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
operator|&&
name|getDataFormats
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|getDataFormats
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|type
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
for|for
control|(
name|RouteDefinition
name|route
range|:
name|getRouteDefinitions
argument_list|()
control|)
block|{
name|Iterator
argument_list|<
name|ProcessorDefinition
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ProcessorDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProcessorDefinition
name|proc
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|proc
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|proc
return|;
block|}
block|}
block|}
return|return
literal|null
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
name|ProcessorDefinition
name|answer
init|=
name|getProcessorDefinition
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
return|;
block|}
return|return
literal|null
return|;
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
name|this
operator|.
name|dataFormats
operator|=
name|dataFormats
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
name|dataFormats
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
name|this
operator|.
name|transformers
operator|=
name|transformers
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
name|transformers
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
name|this
operator|.
name|validators
operator|=
name|validators
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
name|validators
return|;
block|}
DECL|method|startRouteDefinitions ()
specifier|public
name|void
name|startRouteDefinitions
parameter_list|()
throws|throws
name|Exception
block|{
name|startRouteDefinitions
argument_list|(
name|routeDefinitions
argument_list|)
expr_stmt|;
block|}
DECL|method|startRouteDefinitions (Collection<RouteDefinition> list)
specifier|protected
name|void
name|startRouteDefinitions
parameter_list|(
name|Collection
argument_list|<
name|RouteDefinition
argument_list|>
name|list
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|RouteDefinition
name|route
range|:
name|list
control|)
block|{
name|startRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|startRoute (RouteDefinition routeDefinition)
specifier|public
name|void
name|startRoute
parameter_list|(
name|RouteDefinition
name|routeDefinition
parameter_list|)
throws|throws
name|Exception
block|{
comment|// assign ids to the routes and validate that the id's is all unique
name|RouteDefinitionHelper
operator|.
name|forceAssignIds
argument_list|(
name|camelContext
argument_list|,
name|routeDefinitions
argument_list|)
expr_stmt|;
name|String
name|duplicate
init|=
name|RouteDefinitionHelper
operator|.
name|validateUniqueIds
argument_list|(
name|routeDefinition
argument_list|,
name|routeDefinitions
argument_list|)
decl_stmt|;
if|if
condition|(
name|duplicate
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|FailedToStartRouteException
argument_list|(
name|routeDefinition
operator|.
name|getId
argument_list|()
argument_list|,
literal|"duplicate id detected: "
operator|+
name|duplicate
operator|+
literal|". Please correct ids to be unique among all your routes."
argument_list|)
throw|;
block|}
comment|// must ensure route is prepared, before we can start it
if|if
condition|(
operator|!
name|routeDefinition
operator|.
name|isPrepared
argument_list|()
condition|)
block|{
name|RouteDefinitionHelper
operator|.
name|prepareRoute
argument_list|(
name|camelContext
argument_list|,
name|routeDefinition
argument_list|)
expr_stmt|;
name|routeDefinition
operator|.
name|markPrepared
argument_list|()
expr_stmt|;
block|}
comment|// indicate we are staring the route using this thread so
comment|// we are able to query this if needed
name|AbstractCamelContext
name|mcc
init|=
name|camelContext
operator|.
name|adapt
argument_list|(
name|AbstractCamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|mcc
operator|.
name|setStartingRoutes
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|Route
name|route
init|=
operator|new
name|RouteReifier
argument_list|(
name|routeDefinition
argument_list|)
operator|.
name|createRoute
argument_list|(
name|mcc
argument_list|)
decl_stmt|;
name|RouteService
name|routeService
init|=
operator|new
name|RouteService
argument_list|(
name|route
argument_list|)
decl_stmt|;
name|mcc
operator|.
name|startRouteService
argument_list|(
name|routeService
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// we are done staring routes
name|mcc
operator|.
name|setStartingRoutes
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Should we start newly added routes?      */
DECL|method|shouldStartRoutes ()
specifier|protected
name|boolean
name|shouldStartRoutes
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|isStarted
argument_list|()
operator|&&
operator|!
name|camelContext
operator|.
name|isStarting
argument_list|()
return|;
block|}
DECL|method|lookup (CamelContext context, String ref, Class<T> type)
specifier|protected
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|ref
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
try|try
block|{
return|return
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|ref
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// need to ignore not same type and return it as null
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

