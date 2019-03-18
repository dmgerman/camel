begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
package|;
end_package

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
name|Predicate
import|;
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
name|processor
operator|.
name|CatchProcessor
import|;
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
name|FatalFallbackErrorHandler
import|;
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
name|RouteContext
import|;
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

begin_class
DECL|class|OnExceptionReifier
class|class
name|OnExceptionReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|OnExceptionDefinition
argument_list|>
block|{
DECL|method|OnExceptionReifier (ProcessorDefinition<?> definition)
name|OnExceptionReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|OnExceptionDefinition
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addRoutes (RouteContext routeContext, Collection<Route> routes)
specifier|public
name|void
name|addRoutes
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Collection
argument_list|<
name|Route
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
comment|// assign whether this was a route scoped onException or not
comment|// we need to know this later when setting the parent, as only route scoped should have parent
comment|// Note: this logic can possible be removed when the Camel routing engine decides at runtime
comment|// to apply onException in a more dynamic fashion than current code base
comment|// and therefore is in a better position to decide among context/route scoped OnException at runtime
if|if
condition|(
name|definition
operator|.
name|getRouteScoped
argument_list|()
operator|==
literal|null
condition|)
block|{
name|definition
operator|.
name|setRouteScoped
argument_list|(
name|definition
operator|.
name|getParent
argument_list|()
operator|!=
literal|null
argument_list|)
expr_stmt|;
block|}
name|setHandledFromExpressionType
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|setContinuedFromExpressionType
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|setRetryWhileFromExpressionType
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|setOnRedeliveryFromRedeliveryRef
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|setOnExceptionOccurredFromOnExceptionOccurredRef
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
comment|// load exception classes
if|if
condition|(
name|definition
operator|.
name|getExceptions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|definition
operator|.
name|getExceptions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|definition
operator|.
name|setExceptionClasses
argument_list|(
name|createExceptionClasses
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// must validate configuration before creating processor
name|definition
operator|.
name|validateConfiguration
argument_list|()
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
condition|)
block|{
comment|// ensure allow original is turned on
name|routeContext
operator|.
name|setAllowUseOriginalMessage
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// lets attach this on exception to the route error handler
name|Processor
name|child
init|=
name|createOutputsProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
comment|// wrap in our special safe fallback error handler if OnException have child output
name|Processor
name|errorHandler
init|=
operator|new
name|FatalFallbackErrorHandler
argument_list|(
name|child
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|definition
operator|.
name|setErrorHandler
argument_list|(
name|id
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
block|}
comment|// lookup the error handler builder
name|ErrorHandlerBuilder
name|builder
init|=
call|(
name|ErrorHandlerBuilder
call|)
argument_list|(
operator|(
name|RouteDefinition
operator|)
name|routeContext
operator|.
name|getRoute
argument_list|()
argument_list|)
operator|.
name|getErrorHandlerBuilder
argument_list|()
decl_stmt|;
comment|// and add this as error handlers
name|builder
operator|.
name|addErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|CatchProcessor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
comment|// load exception classes
if|if
condition|(
name|definition
operator|.
name|getExceptions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|definition
operator|.
name|getExceptions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|definition
operator|.
name|setExceptionClasses
argument_list|(
name|createExceptionClasses
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
condition|)
block|{
comment|// ensure allow original is turned on
name|routeContext
operator|.
name|setAllowUseOriginalMessage
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// must validate configuration before creating processor
name|definition
operator|.
name|validateConfiguration
argument_list|()
expr_stmt|;
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Predicate
name|when
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getOnWhen
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|when
operator|=
name|definition
operator|.
name|getOnWhen
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
name|Predicate
name|handle
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getHandled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|handle
operator|=
name|definition
operator|.
name|getHandled
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|CatchProcessor
argument_list|(
name|definition
operator|.
name|getExceptionClasses
argument_list|()
argument_list|,
name|childProcessor
argument_list|,
name|when
argument_list|,
name|handle
argument_list|)
return|;
block|}
DECL|method|createExceptionClasses (ClassResolver resolver)
specifier|protected
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|createExceptionClasses
parameter_list|(
name|ClassResolver
name|resolver
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|definition
operator|.
name|getExceptions
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|list
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|list
control|)
block|{
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|type
init|=
name|resolver
operator|.
name|resolveMandatoryClass
argument_list|(
name|name
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|setHandledFromExpressionType (RouteContext routeContext)
specifier|private
name|void
name|setHandledFromExpressionType
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getHandled
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getHandledPolicy
argument_list|()
operator|==
literal|null
operator|&&
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|handled
argument_list|(
name|definition
operator|.
name|getHandled
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setContinuedFromExpressionType (RouteContext routeContext)
specifier|private
name|void
name|setContinuedFromExpressionType
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getContinued
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getContinuedPolicy
argument_list|()
operator|==
literal|null
operator|&&
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|continued
argument_list|(
name|definition
operator|.
name|getContinued
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setRetryWhileFromExpressionType (RouteContext routeContext)
specifier|private
name|void
name|setRetryWhileFromExpressionType
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|.
name|getRetryWhile
argument_list|()
operator|!=
literal|null
operator|&&
name|definition
operator|.
name|getRetryWhilePolicy
argument_list|()
operator|==
literal|null
operator|&&
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|retryWhile
argument_list|(
name|definition
operator|.
name|getRetryWhile
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setOnRedeliveryFromRedeliveryRef (RouteContext routeContext)
specifier|private
name|void
name|setOnRedeliveryFromRedeliveryRef
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// lookup onRedelivery if ref is provided
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getOnRedeliveryRef
argument_list|()
argument_list|)
condition|)
block|{
comment|// if ref is provided then use mandatory lookup to fail if not found
name|Processor
name|onRedelivery
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|definition
operator|.
name|getOnRedeliveryRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
decl_stmt|;
name|definition
operator|.
name|setOnRedelivery
argument_list|(
name|onRedelivery
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setOnExceptionOccurredFromOnExceptionOccurredRef (RouteContext routeContext)
specifier|private
name|void
name|setOnExceptionOccurredFromOnExceptionOccurredRef
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// lookup onRedelivery if ref is provided
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getOnExceptionOccurredRef
argument_list|()
argument_list|)
condition|)
block|{
comment|// if ref is provided then use mandatory lookup to fail if not found
name|Processor
name|onExceptionOccurred
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|definition
operator|.
name|getOnExceptionOccurredRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
decl_stmt|;
name|definition
operator|.
name|setOnExceptionOccurred
argument_list|(
name|onExceptionOccurred
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

