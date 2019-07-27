begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|List
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
name|RuntimeCamelException
import|;
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
name|processor
operator|.
name|ErrorHandler
import|;
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
name|errorhandler
operator|.
name|ErrorHandlerSupport
import|;
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
name|errorhandler
operator|.
name|ExceptionPolicy
import|;
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
name|errorhandler
operator|.
name|ExceptionPolicyKey
import|;
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
name|errorhandler
operator|.
name|ExceptionPolicyStrategy
import|;
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
name|errorhandler
operator|.
name|RedeliveryErrorHandler
import|;
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
name|errorhandler
operator|.
name|ErrorHandlerReifier
import|;
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Base class for builders of error handling.  */
end_comment

begin_class
DECL|class|ErrorHandlerBuilderSupport
specifier|public
specifier|abstract
class|class
name|ErrorHandlerBuilderSupport
implements|implements
name|ErrorHandlerBuilder
block|{
DECL|field|exceptionPolicyStrategy
specifier|private
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
decl_stmt|;
DECL|method|cloneBuilder (ErrorHandlerBuilderSupport other)
specifier|protected
name|void
name|cloneBuilder
parameter_list|(
name|ErrorHandlerBuilderSupport
name|other
parameter_list|)
block|{
name|other
operator|.
name|exceptionPolicyStrategy
operator|=
name|exceptionPolicyStrategy
expr_stmt|;
block|}
comment|/**      * Configures the other error handler based on this error handler.      *      * @param routeContext the route context      * @param handler the other error handler      */
DECL|method|configure (RouteContext routeContext, ErrorHandler handler)
specifier|public
name|void
name|configure
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ErrorHandler
name|handler
parameter_list|)
block|{
if|if
condition|(
name|handler
operator|instanceof
name|ErrorHandlerSupport
condition|)
block|{
name|ErrorHandlerSupport
name|handlerSupport
init|=
operator|(
name|ErrorHandlerSupport
operator|)
name|handler
decl_stmt|;
name|Set
argument_list|<
name|NamedNode
argument_list|>
name|list
init|=
name|routeContext
operator|.
name|getErrorHandlers
argument_list|(
name|this
argument_list|)
decl_stmt|;
for|for
control|(
name|NamedNode
name|exception
range|:
name|list
control|)
block|{
name|addExceptionPolicy
argument_list|(
name|handlerSupport
argument_list|,
name|routeContext
argument_list|,
operator|(
name|OnExceptionDefinition
operator|)
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|handler
operator|instanceof
name|RedeliveryErrorHandler
condition|)
block|{
name|RedeliveryErrorHandler
name|reh
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|handler
decl_stmt|;
name|boolean
name|original
init|=
name|reh
operator|.
name|isUseOriginalMessagePolicy
argument_list|()
operator|||
name|reh
operator|.
name|isUseOriginalBodyPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|original
condition|)
block|{
if|if
condition|(
name|reh
operator|.
name|isUseOriginalMessagePolicy
argument_list|()
operator|&&
name|reh
operator|.
name|isUseOriginalBodyPolicy
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot set both useOriginalMessage and useOriginalBody on the error handler"
argument_list|)
throw|;
block|}
comment|// ensure allow original is turned on
name|routeContext
operator|.
name|setAllowUseOriginalMessage
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addExceptionPolicy (ErrorHandlerSupport handlerSupport, RouteContext routeContext, OnExceptionDefinition exceptionType)
specifier|public
specifier|static
name|void
name|addExceptionPolicy
parameter_list|(
name|ErrorHandlerSupport
name|handlerSupport
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|,
name|OnExceptionDefinition
name|exceptionType
parameter_list|)
block|{
if|if
condition|(
name|routeContext
operator|!=
literal|null
condition|)
block|{
comment|// add error handler as child service so they get lifecycle handled
name|Processor
name|errorHandler
init|=
name|routeContext
operator|.
name|getOnException
argument_list|(
name|exceptionType
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|handlerSupport
operator|.
name|addErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
comment|// load exception classes
name|List
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
argument_list|>
name|list
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|exceptionType
operator|.
name|getExceptions
argument_list|()
argument_list|)
condition|)
block|{
name|list
operator|=
name|createExceptionClasses
argument_list|(
name|exceptionType
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Class
argument_list|<
name|?
extends|extends
name|Throwable
argument_list|>
name|clazz
range|:
name|list
control|)
block|{
name|String
name|routeId
init|=
literal|null
decl_stmt|;
comment|// only get the route id, if the exception type is route scoped
if|if
condition|(
name|exceptionType
operator|.
name|isRouteScoped
argument_list|()
condition|)
block|{
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
name|exceptionType
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
name|routeId
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
block|}
block|}
name|Predicate
name|when
init|=
name|exceptionType
operator|.
name|getOnWhen
argument_list|()
operator|!=
literal|null
condition|?
name|exceptionType
operator|.
name|getOnWhen
argument_list|()
operator|.
name|getExpression
argument_list|()
else|:
literal|null
decl_stmt|;
name|ExceptionPolicyKey
name|key
init|=
operator|new
name|ExceptionPolicyKey
argument_list|(
name|routeId
argument_list|,
name|clazz
argument_list|,
name|when
argument_list|)
decl_stmt|;
name|ExceptionPolicy
name|policy
init|=
name|toExceptionPolicy
argument_list|(
name|exceptionType
argument_list|,
name|routeContext
argument_list|)
decl_stmt|;
name|handlerSupport
operator|.
name|addExceptionPolicy
argument_list|(
name|key
argument_list|,
name|policy
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|toExceptionPolicy (OnExceptionDefinition exceptionType, RouteContext routeContext)
specifier|protected
specifier|static
name|ExceptionPolicy
name|toExceptionPolicy
parameter_list|(
name|OnExceptionDefinition
name|exceptionType
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
name|ErrorHandlerReifier
operator|.
name|createExceptionPolicy
argument_list|(
name|exceptionType
argument_list|,
name|routeContext
argument_list|)
return|;
block|}
DECL|method|createExceptionClasses (OnExceptionDefinition exceptionType, ClassResolver resolver)
specifier|protected
specifier|static
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
name|OnExceptionDefinition
name|exceptionType
parameter_list|,
name|ClassResolver
name|resolver
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|exceptionType
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
try|try
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
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Sets the exception policy to use      */
DECL|method|exceptionPolicyStrategy (ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|ErrorHandlerBuilderSupport
name|exceptionPolicyStrategy
parameter_list|(
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|)
block|{
name|setExceptionPolicyStrategy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets the exception policy strategy      */
DECL|method|getExceptionPolicyStrategy ()
specifier|public
name|ExceptionPolicyStrategy
name|getExceptionPolicyStrategy
parameter_list|()
block|{
return|return
name|exceptionPolicyStrategy
return|;
block|}
comment|/**      * Sets the exception policy strategy to use for resolving the {@link org.apache.camel.model.OnExceptionDefinition}      * to use for a given thrown exception      *      * @param exceptionPolicyStrategy  the exception policy strategy      */
DECL|method|setExceptionPolicyStrategy (ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|void
name|setExceptionPolicyStrategy
parameter_list|(
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|exceptionPolicyStrategy
argument_list|,
literal|"ExceptionPolicyStrategy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|exceptionPolicyStrategy
operator|=
name|exceptionPolicyStrategy
expr_stmt|;
block|}
block|}
end_class

end_unit

