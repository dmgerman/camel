begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Represents a proxy to an error handler builder which is resolved by named reference  *  * @version $Revision$  */
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
DECL|field|handler
specifier|private
name|ErrorHandlerBuilder
name|handler
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
DECL|method|addErrorHandlers (OnExceptionDefinition exception)
specifier|public
name|void
name|addErrorHandlers
parameter_list|(
name|OnExceptionDefinition
name|exception
parameter_list|)
block|{
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
name|exception
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|addErrorHandlers
argument_list|(
name|exception
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
name|handler
operator|=
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
name|handler
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
comment|/**      * Lookup the error handler by the given ref      *      * @param routeContext the route context      * @param ref          reference id for the error handler      * @return the error handler      */
DECL|method|lookupErrorHandlerBuilder (RouteContext routeContext, String ref)
specifier|public
specifier|static
name|ErrorHandlerBuilder
name|lookupErrorHandlerBuilder
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|ErrorHandlerBuilder
name|answer
decl_stmt|;
comment|// if the ref is the default then we do not have any explicit error handler configured
comment|// if that is the case then use error handlers configured on the route, as for instance
comment|// the transacted error handler could have been configured on the route so we should use that one
if|if
condition|(
operator|!
name|isErrorHandlerBuilderConfigured
argument_list|(
name|ref
argument_list|)
condition|)
block|{
comment|// see if there has been configured a route builder on the route
name|answer
operator|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getErrorHandlerBuilder
argument_list|()
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|routeContext
operator|.
name|getRoute
argument_list|()
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
name|isErrorHandlerBuilderConfigured
argument_list|(
name|otherRef
argument_list|)
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
comment|// inherit the error handlers from the other as they are to be shared
comment|// this is needed by camel-spring when none error handler has been explicit configured
name|answer
operator|.
name|setErrorHandlers
argument_list|(
name|other
operator|.
name|getErrorHandlers
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|// use specific configured error handler
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
return|return
name|answer
return|;
block|}
comment|/**      * Returns whether a specific error handler builder has been configured or not.      *<p/>      * Can be used to test if none has been configured and then install a custom error handler builder      * replacing the default error handler (that would have been used as fallback otherwise).      *<br/>      * This is for instance used by the transacted policy to setup a TransactedErrorHandlerBuilder      * in camel-spring.      */
DECL|method|isErrorHandlerBuilderConfigured (String ref)
specifier|public
specifier|static
name|boolean
name|isErrorHandlerBuilderConfigured
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
DECL|method|getHandler ()
specifier|public
name|ErrorHandlerBuilder
name|getHandler
parameter_list|()
block|{
return|return
name|handler
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
name|handler
operator|=
name|lookupErrorHandlerBuilder
argument_list|(
name|routeContext
argument_list|,
name|getRef
argument_list|()
argument_list|)
expr_stmt|;
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
argument_list|()
decl_stmt|;
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
name|exceptionType
argument_list|)
expr_stmt|;
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

