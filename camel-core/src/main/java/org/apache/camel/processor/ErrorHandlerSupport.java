begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
operator|.
name|ServiceSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Support class for {@link ErrorHandler} implementations.  *  * @version   */
end_comment

begin_class
DECL|class|ErrorHandlerSupport
specifier|public
specifier|abstract
class|class
name|ErrorHandlerSupport
extends|extends
name|ServiceSupport
implements|implements
name|ErrorHandler
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|exceptionPolicies
specifier|private
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
argument_list|<
name|ExceptionPolicyKey
argument_list|,
name|OnExceptionDefinition
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|exceptionPolicy
specifier|private
name|ExceptionPolicyStrategy
name|exceptionPolicy
init|=
name|createDefaultExceptionPolicyStrategy
argument_list|()
decl_stmt|;
DECL|method|addExceptionPolicy (OnExceptionDefinition exceptionType)
specifier|public
name|void
name|addExceptionPolicy
parameter_list|(
name|OnExceptionDefinition
name|exceptionType
parameter_list|)
block|{
name|Processor
name|processor
init|=
name|exceptionType
operator|.
name|getErrorHandler
argument_list|()
decl_stmt|;
name|addChildService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|list
init|=
name|exceptionType
operator|.
name|getExceptionClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|clazz
range|:
name|list
control|)
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
name|String
name|routeId
init|=
name|route
operator|!=
literal|null
condition|?
name|route
operator|.
name|getId
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
comment|/**      * Attempts to invoke the handler for this particular exception if one is available      */
DECL|method|customProcessorForException (Exchange exchange, Throwable exception)
specifier|protected
name|boolean
name|customProcessorForException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|exception
parameter_list|)
throws|throws
name|Exception
block|{
name|OnExceptionDefinition
name|policy
init|=
name|getExceptionPolicy
argument_list|(
name|exchange
argument_list|,
name|exception
argument_list|)
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
block|{
name|Processor
name|processor
init|=
name|policy
operator|.
name|getErrorHandler
argument_list|()
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
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

