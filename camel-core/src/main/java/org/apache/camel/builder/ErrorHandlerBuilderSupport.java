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
name|ArrayList
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
name|processor
operator|.
name|exceptionpolicy
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
comment|/**  * Base class for builders of error handling.  *  * @version   */
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
DECL|field|onExceptions
specifier|private
name|Map
argument_list|<
name|RouteContext
argument_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|>
name|onExceptions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|exceptionPolicyStrategy
specifier|private
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
decl_stmt|;
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
comment|// only add if we not already have it
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|list
init|=
name|onExceptions
operator|.
name|get
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|onExceptions
operator|.
name|put
argument_list|(
name|routeContext
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|list
operator|.
name|contains
argument_list|(
name|exception
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|cloneBuilder (ErrorHandlerBuilderSupport other)
specifier|protected
name|void
name|cloneBuilder
parameter_list|(
name|ErrorHandlerBuilderSupport
name|other
parameter_list|)
block|{
if|if
condition|(
operator|!
name|onExceptions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|RouteContext
argument_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
argument_list|>
name|copy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|onExceptions
argument_list|)
decl_stmt|;
name|other
operator|.
name|onExceptions
operator|=
name|copy
expr_stmt|;
block|}
name|other
operator|.
name|exceptionPolicyStrategy
operator|=
name|exceptionPolicyStrategy
expr_stmt|;
block|}
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
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|list
init|=
name|onExceptions
operator|.
name|get
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
name|exception
range|:
name|list
control|)
block|{
name|handlerSupport
operator|.
name|addExceptionPolicy
argument_list|(
name|routeContext
argument_list|,
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|handler
operator|instanceof
name|RedeliveryErrorHandler
condition|)
block|{
name|boolean
name|original
init|=
operator|(
operator|(
name|RedeliveryErrorHandler
operator|)
name|handler
operator|)
operator|.
name|isUseOriginalMessagePolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|original
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
block|}
block|}
DECL|method|getErrorHandlers (RouteContext routeContext)
specifier|public
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|getErrorHandlers
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
name|onExceptions
operator|.
name|get
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
DECL|method|setErrorHandlers (RouteContext routeContext, List<OnExceptionDefinition> exceptions)
specifier|public
name|void
name|setErrorHandlers
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|exceptions
parameter_list|)
block|{
name|this
operator|.
name|onExceptions
operator|.
name|put
argument_list|(
name|routeContext
argument_list|,
name|exceptions
argument_list|)
expr_stmt|;
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
comment|/**      * Remove the OnExceptionList by look up the route id from the ErrorHandlerBuilder internal map      * @param id the route id      * @return true if the route context is found and removed      */
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
name|onExceptions
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|getRouteId
argument_list|(
name|routeContext
argument_list|)
operator|.
name|equals
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|onExceptions
operator|.
name|remove
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|getRouteId (RouteContext routeContext)
specifier|protected
name|String
name|getRouteId
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|CamelContext
name|context
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
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
return|return
name|route
operator|.
name|idOrCreate
argument_list|(
name|context
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

