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
name|Service
import|;
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
name|model
operator|.
name|OnCompletionDefinition
import|;
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
name|OnExceptionDefinition
import|;
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
name|support
operator|.
name|CamelContextHelper
import|;
end_import

begin_comment
comment|/**  * Represents the runtime objects for a given {@link RouteDefinition} so that it  * can be stopped independently of other routes  */
end_comment

begin_class
DECL|class|RouteService
specifier|public
class|class
name|RouteService
extends|extends
name|BaseRouteService
block|{
DECL|field|routeDefinition
specifier|private
specifier|final
name|RouteDefinition
name|routeDefinition
decl_stmt|;
DECL|method|RouteService (Route route)
specifier|public
name|RouteService
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|super
argument_list|(
name|route
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeDefinition
operator|=
operator|(
name|RouteDefinition
operator|)
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoute
argument_list|()
expr_stmt|;
block|}
DECL|method|getRouteDefinition ()
specifier|public
name|RouteDefinition
name|getRouteDefinition
parameter_list|()
block|{
return|return
name|routeDefinition
return|;
block|}
annotation|@
name|Override
DECL|method|getStartupOrder ()
specifier|public
name|Integer
name|getStartupOrder
parameter_list|()
block|{
return|return
name|routeDefinition
operator|.
name|getStartupOrder
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRouteDescription ()
specifier|protected
name|String
name|getRouteDescription
parameter_list|()
block|{
return|return
name|RouteDefinitionHelper
operator|.
name|getRouteMessage
argument_list|(
name|routeDefinition
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isAutoStartup ()
specifier|public
name|boolean
name|isAutoStartup
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|getCamelContext
argument_list|()
operator|.
name|isAutoStartup
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|getRouteContext
argument_list|()
operator|.
name|isAutoStartup
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|routeDefinition
operator|.
name|getAutoStartup
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// should auto startup by default
return|return
literal|true
return|;
block|}
name|Boolean
name|isAutoStartup
init|=
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|routeDefinition
operator|.
name|getAutoStartup
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|isAutoStartup
operator|!=
literal|null
operator|&&
name|isAutoStartup
return|;
block|}
annotation|@
name|Override
DECL|method|isContextScopedErrorHandler ()
specifier|public
name|boolean
name|isContextScopedErrorHandler
parameter_list|()
block|{
if|if
condition|(
operator|!
name|routeDefinition
operator|.
name|isContextScopedErrorHandler
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// if error handler ref is configured it may refer to a context scoped,
comment|// so we need to check this first
comment|// the XML DSL will configure error handlers using refs, so we need this
comment|// additional test
if|if
condition|(
name|routeDefinition
operator|.
name|getErrorHandlerRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ErrorHandlerFactory
name|routeScoped
init|=
name|getRouteContext
argument_list|()
operator|.
name|getErrorHandlerFactory
argument_list|()
decl_stmt|;
name|ErrorHandlerFactory
name|contextScoped
init|=
name|getCamelContext
argument_list|()
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getErrorHandlerFactory
argument_list|()
decl_stmt|;
return|return
name|routeScoped
operator|!=
literal|null
operator|&&
name|contextScoped
operator|!=
literal|null
operator|&&
name|routeScoped
operator|==
name|contextScoped
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Gather all other kind of route scoped services from the given route,      * except error handler      */
annotation|@
name|Override
DECL|method|doGetRouteScopedServices (List<Service> services)
specifier|protected
name|void
name|doGetRouteScopedServices
parameter_list|(
name|List
argument_list|<
name|Service
argument_list|>
name|services
parameter_list|)
block|{
for|for
control|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
range|:
name|routeDefinition
operator|.
name|getOutputs
argument_list|()
control|)
block|{
if|if
condition|(
name|output
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
name|OnExceptionDefinition
name|onExceptionDefinition
init|=
operator|(
name|OnExceptionDefinition
operator|)
name|output
decl_stmt|;
if|if
condition|(
name|onExceptionDefinition
operator|.
name|isRouteScoped
argument_list|()
condition|)
block|{
name|Processor
name|errorHandler
init|=
name|getRouteContext
argument_list|()
operator|.
name|getOnException
argument_list|(
name|onExceptionDefinition
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|errorHandler
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|errorHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|output
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|OnCompletionDefinition
name|onCompletionDefinition
init|=
operator|(
name|OnCompletionDefinition
operator|)
name|output
decl_stmt|;
if|if
condition|(
name|onCompletionDefinition
operator|.
name|isRouteScoped
argument_list|()
condition|)
block|{
name|Processor
name|onCompletionProcessor
init|=
name|getRouteContext
argument_list|()
operator|.
name|getOnCompletion
argument_list|(
name|onCompletionDefinition
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|onCompletionProcessor
operator|instanceof
name|Service
condition|)
block|{
name|services
operator|.
name|add
argument_list|(
operator|(
name|Service
operator|)
name|onCompletionProcessor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

