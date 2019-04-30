begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|LinkedHashMap
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
name|Exchange
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
name|exceptionpolicy
operator|.
name|DefaultExceptionPolicyStrategy
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
name|ChildServiceSupport
import|;
end_import

begin_comment
comment|/**  * Support class for {@link ErrorHandler} implementations.  */
end_comment

begin_class
DECL|class|ErrorHandlerSupport
specifier|public
specifier|abstract
class|class
name|ErrorHandlerSupport
extends|extends
name|ChildServiceSupport
implements|implements
name|ErrorHandler
block|{
DECL|field|exceptionPolicies
specifier|protected
specifier|final
name|Map
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|OnExceptionDefinition
argument_list|>
name|exceptionPolicies
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|exceptionPolicy
specifier|protected
name|ExceptionPolicyStrategy
name|exceptionPolicy
init|=
name|createDefaultExceptionPolicyStrategy
argument_list|()
decl_stmt|;
DECL|method|addExceptionPolicy (RouteContext routeContext, OnExceptionDefinition exceptionType)
specifier|public
name|void
name|addExceptionPolicy
parameter_list|(
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
name|exceptionType
operator|.
name|getErrorHandler
argument_list|(
name|routeContext
operator|.
name|getRouteId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|errorHandler
operator|!=
literal|null
condition|)
block|{
name|addChildService
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
block|}
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
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exceptionType
operator|.
name|getExceptions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|exceptionType
operator|.
name|getExceptions
argument_list|()
operator|.
name|isEmpty
argument_list|()
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
name|exceptionType
operator|.
name|getOnWhen
argument_list|()
argument_list|)
decl_stmt|;
name|exceptionPolicies
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|exceptionType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createExceptionClasses (OnExceptionDefinition exceptionType, ClassResolver resolver)
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
comment|/**      * Attempts to find the best suited {@link OnExceptionDefinition} to be used for handling the given thrown exception.      *      * @param exchange  the exchange      * @param exception the exception that was thrown      * @return the best exception type to handle this exception,<tt>null</tt> if none found.      */
DECL|method|getExceptionPolicy (Exchange exchange, Throwable exception)
specifier|protected
name|OnExceptionDefinition
name|getExceptionPolicy
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
block|{
if|if
condition|(
name|exceptionPolicy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"The exception policy has not been set"
argument_list|)
throw|;
block|}
return|return
name|exceptionPolicy
operator|.
name|getExceptionPolicy
argument_list|(
name|exceptionPolicies
argument_list|,
name|exchange
argument_list|,
name|exception
argument_list|)
return|;
block|}
comment|/**      * Sets the strategy to use for resolving the {@link OnExceptionDefinition} to use      * for handling thrown exceptions.      */
DECL|method|setExceptionPolicy (ExceptionPolicyStrategy exceptionPolicy)
specifier|public
name|void
name|setExceptionPolicy
parameter_list|(
name|ExceptionPolicyStrategy
name|exceptionPolicy
parameter_list|)
block|{
if|if
condition|(
name|exceptionPolicy
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|exceptionPolicy
operator|=
name|exceptionPolicy
expr_stmt|;
block|}
block|}
comment|/**      * Creates the default exception policy strategy to use.      */
DECL|method|createDefaultExceptionPolicyStrategy ()
specifier|public
specifier|static
name|ExceptionPolicyStrategy
name|createDefaultExceptionPolicyStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultExceptionPolicyStrategy
argument_list|()
return|;
block|}
comment|/**      * Whether this error handler supports transacted exchanges or not.      */
DECL|method|supportTransacted ()
specifier|public
specifier|abstract
name|boolean
name|supportTransacted
parameter_list|()
function_decl|;
comment|/**      * Whether this error handler handles exhausted errors by moving the exchange to a dead letter channel.      */
DECL|method|isDeadLetterChannel ()
specifier|public
name|boolean
name|isDeadLetterChannel
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Gets the output      */
DECL|method|getOutput ()
specifier|public
specifier|abstract
name|Processor
name|getOutput
parameter_list|()
function_decl|;
block|}
end_class

end_unit

