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
name|HashMap
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
comment|/**  * Represents a proxy to an error handler builder which is resolved by named reference  */
end_comment

begin_class
DECL|class|ErrorHandlerBuilderRef
specifier|public
class|class
name|ErrorHandlerBuilderRef
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|field|DEFAULT_ERROR_HANDLER_BUILDER
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_ERROR_HANDLER_BUILDER
init|=
literal|"CamelDefaultErrorHandlerBuilder"
decl_stmt|;
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|handlers
specifier|private
specifier|final
name|Map
argument_list|<
name|RouteContext
argument_list|,
name|ErrorHandlerBuilder
argument_list|>
name|handlers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|supportTransacted
specifier|private
name|boolean
name|supportTransacted
decl_stmt|;
DECL|method|ErrorHandlerBuilderRef (String ref)
specifier|public
name|ErrorHandlerBuilderRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addErrorHandlers (RouteContext routeContext, OnExceptionDefinition exception)
specifier|public
name|void
name|addErrorHandlers
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|OnExceptionDefinition
name|exception
parameter_list|)
block|{
name|ErrorHandlerBuilder
name|handler
init|=
name|handlers
operator|.
name|get
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|addErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|addErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|removeOnExceptionList (String id)
specifier|public
name|boolean
name|removeOnExceptionList
parameter_list|(
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|RouteContext
name|routeContext
range|:
name|handlers
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|routeContext
operator|.
name|getRouteId
argument_list|()
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|handlers
operator|.
name|remove
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
return|return
name|super
operator|.
name|removeOnExceptionList
argument_list|(
name|id
argument_list|)
return|;
block|}
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
specifier|public
name|Processor
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ErrorHandlerFactory
name|handler
init|=
name|handlers
operator|.
name|computeIfAbsent
argument_list|(
name|routeContext
argument_list|,
name|this
operator|::
name|createErrorHandler
argument_list|)
decl_stmt|;
return|return
name|ErrorHandlerReifier
operator|.
name|reifier
argument_list|(
name|handler
argument_list|)
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
name|supportTransacted
return|;
block|}
annotation|@
name|Override
DECL|method|cloneBuilder ()
specifier|public
name|ErrorHandlerBuilder
name|cloneBuilder
parameter_list|()
block|{
name|ErrorHandlerBuilderRef
name|answer
init|=
operator|new
name|ErrorHandlerBuilderRef
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|cloneBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|cloneBuilder (ErrorHandlerBuilderRef other)
specifier|protected
name|void
name|cloneBuilder
parameter_list|(
name|ErrorHandlerBuilderRef
name|other
parameter_list|)
block|{
name|super
operator|.
name|cloneBuilder
argument_list|(
name|other
argument_list|)
expr_stmt|;
comment|// no need to copy the handlers
name|other
operator|.
name|supportTransacted
operator|=
name|supportTransacted
expr_stmt|;
block|}
comment|/**      * Lookup the error handler by the given ref      *      * @param routeContext the route context      * @param ref          reference id for the error handler      * @return the error handler      */
DECL|method|lookupErrorHandlerFactory (RouteContext routeContext, String ref)
specifier|public
specifier|static
name|ErrorHandlerFactory
name|lookupErrorHandlerFactory
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
return|return
name|lookupErrorHandlerFactory
argument_list|(
name|routeContext
argument_list|,
name|ref
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Lookup the error handler by the given ref      *      * @param routeContext the route context      * @param ref          reference id for the error handler      * @param mandatory    whether the error handler must exists, if not a {@link org.apache.camel.NoSuchBeanException} is thrown      * @return the error handler      */
DECL|method|lookupErrorHandlerFactory (RouteContext routeContext, String ref, boolean mandatory)
specifier|public
specifier|static
name|ErrorHandlerFactory
name|lookupErrorHandlerFactory
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|ref
parameter_list|,
name|boolean
name|mandatory
parameter_list|)
block|{
name|ErrorHandlerFactory
name|answer
decl_stmt|;
comment|// if the ref is the default then we do not have any explicit error handler configured
comment|// if that is the case then use error handlers configured on the route, as for instance
comment|// the transacted error handler could have been configured on the route so we should use that one
if|if
condition|(
operator|!
name|isErrorHandlerFactoryConfigured
argument_list|(
name|ref
argument_list|)
condition|)
block|{
comment|// see if there has been configured a route builder on the route
name|RouteDefinition
name|route
init|=
operator|(
name|RouteDefinition
operator|)
name|routeContext
operator|.
name|getRoute
argument_list|()
decl_stmt|;
name|answer
operator|=
name|route
operator|.
name|getErrorHandlerFactory
argument_list|()
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|route
operator|.
name|getErrorHandlerRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|route
operator|.
name|getErrorHandlerRef
argument_list|()
argument_list|,
name|ErrorHandlerBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback to the default error handler if none configured on the route
name|answer
operator|=
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
expr_stmt|;
block|}
comment|// check if its also a ref with no error handler configuration like me
if|if
condition|(
name|answer
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
name|ErrorHandlerBuilderRef
name|other
init|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|answer
decl_stmt|;
name|String
name|otherRef
init|=
name|other
operator|.
name|getRef
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|isErrorHandlerFactoryConfigured
argument_list|(
name|otherRef
argument_list|)
condition|)
block|{
comment|// the other has also no explicit error handler configured then fallback to the handler
comment|// configured on the parent camel context
name|answer
operator|=
name|lookupErrorHandlerFactory
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// the other has also no explicit error handler configured then fallback to the default error handler
comment|// otherwise we could recursive loop forever (triggered by createErrorHandler method)
name|answer
operator|=
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
expr_stmt|;
block|}
comment|// inherit the error handlers from the other as they are to be shared
comment|// this is needed by camel-spring when none error handler has been explicit configured
operator|(
operator|(
name|ErrorHandlerBuilder
operator|)
name|answer
operator|)
operator|.
name|setErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|other
operator|.
name|getErrorHandlers
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// use specific configured error handler
if|if
condition|(
name|mandatory
condition|)
block|{
name|answer
operator|=
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|ref
argument_list|,
name|ErrorHandlerBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|ref
argument_list|,
name|ErrorHandlerBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|lookupErrorHandlerFactory (CamelContext camelContext)
specifier|protected
specifier|static
name|ErrorHandlerFactory
name|lookupErrorHandlerFactory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|ErrorHandlerFactory
name|answer
init|=
name|camelContext
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
if|if
condition|(
name|answer
operator|instanceof
name|ErrorHandlerBuilderRef
condition|)
block|{
name|ErrorHandlerBuilderRef
name|other
init|=
operator|(
name|ErrorHandlerBuilderRef
operator|)
name|answer
decl_stmt|;
name|String
name|otherRef
init|=
name|other
operator|.
name|getRef
argument_list|()
decl_stmt|;
if|if
condition|(
name|isErrorHandlerFactoryConfigured
argument_list|(
name|otherRef
argument_list|)
condition|)
block|{
name|answer
operator|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|otherRef
argument_list|,
name|ErrorHandlerBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ErrorHandlerBuilder with id "
operator|+
name|otherRef
operator|+
literal|" not found in registry."
argument_list|)
throw|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns whether a specific error handler builder has been configured or not.      *<p/>      * Can be used to test if none has been configured and then install a custom error handler builder      * replacing the default error handler (that would have been used as fallback otherwise).      *<br/>      * This is for instance used by the transacted policy to setup a TransactedErrorHandlerBuilder      * in camel-spring.      */
DECL|method|isErrorHandlerFactoryConfigured (String ref)
specifier|public
specifier|static
name|boolean
name|isErrorHandlerFactoryConfigured
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
return|return
operator|!
name|DEFAULT_ERROR_HANDLER_BUILDER
operator|.
name|equals
argument_list|(
name|ref
argument_list|)
return|;
block|}
DECL|method|getRef ()
specifier|public
name|String
name|getRef
parameter_list|()
block|{
return|return
name|ref
return|;
block|}
DECL|method|createErrorHandler (RouteContext routeContext)
specifier|private
name|ErrorHandlerBuilder
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|ErrorHandlerBuilder
name|handler
init|=
operator|(
name|ErrorHandlerBuilder
operator|)
name|lookupErrorHandlerFactory
argument_list|(
name|routeContext
argument_list|,
name|getRef
argument_list|()
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|handler
argument_list|,
literal|"error handler '"
operator|+
name|ref
operator|+
literal|"'"
argument_list|)
expr_stmt|;
comment|// configure if the handler support transacted
name|supportTransacted
operator|=
name|handler
operator|.
name|supportTransacted
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|list
init|=
name|getErrorHandlers
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|OnExceptionDefinition
name|exceptionType
range|:
name|list
control|)
block|{
name|handler
operator|.
name|addErrorHandlers
argument_list|(
name|routeContext
argument_list|,
name|exceptionType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|handler
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ErrorHandlerBuilderRef["
operator|+
name|ref
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

