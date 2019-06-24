begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.errorhandler
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|errorhandler
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
name|Map
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
name|builder
operator|.
name|DeadLetterChannelBuilder
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
name|DefaultErrorHandlerBuilder
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
name|builder
operator|.
name|ErrorHandlerBuilderRef
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
name|ErrorHandlerBuilderSupport
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
name|NoErrorHandlerBuilder
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
name|RedeliveryPolicyDefinition
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
name|ExceptionPolicy
operator|.
name|RedeliveryOption
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
name|processor
operator|.
name|errorhandler
operator|.
name|RedeliveryPolicy
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
DECL|class|ErrorHandlerReifier
specifier|public
specifier|abstract
class|class
name|ErrorHandlerReifier
parameter_list|<
name|T
extends|extends
name|ErrorHandlerBuilderSupport
parameter_list|>
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
DECL|field|ERROR_HANDLERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Function
argument_list|<
name|ErrorHandlerFactory
argument_list|,
name|ErrorHandlerReifier
argument_list|<
name|?
extends|extends
name|ErrorHandlerFactory
argument_list|>
argument_list|>
argument_list|>
name|ERROR_HANDLERS
decl_stmt|;
static|static
block|{
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Function
argument_list|<
name|ErrorHandlerFactory
argument_list|,
name|ErrorHandlerReifier
argument_list|<
name|?
extends|extends
name|ErrorHandlerFactory
argument_list|>
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|DeadLetterChannelBuilder
operator|.
name|class
argument_list|,
name|DeadLetterChannelReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|DefaultErrorHandlerBuilder
operator|.
name|class
argument_list|,
name|DefaultErrorHandlerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|ErrorHandlerBuilderRef
operator|.
name|class
argument_list|,
name|ErrorHandlerRefReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|NoErrorHandlerBuilder
operator|.
name|class
argument_list|,
name|NoErrorHandlerReifier
operator|::
operator|new
argument_list|)
expr_stmt|;
name|ERROR_HANDLERS
operator|=
name|map
expr_stmt|;
block|}
DECL|field|definition
specifier|protected
name|T
name|definition
decl_stmt|;
comment|/**      * Utility classes should not have a public constructor.      */
DECL|method|ErrorHandlerReifier (T definition)
name|ErrorHandlerReifier
parameter_list|(
name|T
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|registerReifier (Class<?> errorHandlerClass, Function<ErrorHandlerFactory, ErrorHandlerReifier<? extends ErrorHandlerFactory>> creator)
specifier|public
specifier|static
name|void
name|registerReifier
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|errorHandlerClass
parameter_list|,
name|Function
argument_list|<
name|ErrorHandlerFactory
argument_list|,
name|ErrorHandlerReifier
argument_list|<
name|?
extends|extends
name|ErrorHandlerFactory
argument_list|>
argument_list|>
name|creator
parameter_list|)
block|{
name|ERROR_HANDLERS
operator|.
name|put
argument_list|(
name|errorHandlerClass
argument_list|,
name|creator
argument_list|)
expr_stmt|;
block|}
DECL|method|reifier (ErrorHandlerFactory definition)
specifier|public
specifier|static
name|ErrorHandlerReifier
argument_list|<
name|?
extends|extends
name|ErrorHandlerFactory
argument_list|>
name|reifier
parameter_list|(
name|ErrorHandlerFactory
name|definition
parameter_list|)
block|{
name|Function
argument_list|<
name|ErrorHandlerFactory
argument_list|,
name|ErrorHandlerReifier
argument_list|<
name|?
extends|extends
name|ErrorHandlerFactory
argument_list|>
argument_list|>
name|reifier
init|=
name|ERROR_HANDLERS
operator|.
name|get
argument_list|(
name|definition
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|reifier
operator|!=
literal|null
condition|)
block|{
return|return
name|reifier
operator|.
name|apply
argument_list|(
name|definition
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|instanceof
name|ErrorHandlerBuilderSupport
condition|)
block|{
return|return
operator|new
name|ErrorHandlerReifier
argument_list|<
name|ErrorHandlerBuilderSupport
argument_list|>
argument_list|(
operator|(
name|ErrorHandlerBuilderSupport
operator|)
name|definition
argument_list|)
block|{
annotation|@
name|Override
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
return|return
name|definition
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported definition: "
operator|+
name|definition
argument_list|)
throw|;
block|}
block|}
DECL|method|createExceptionPolicy (OnExceptionDefinition def, RouteContext routeContext)
specifier|public
specifier|static
name|ExceptionPolicy
name|createExceptionPolicy
parameter_list|(
name|OnExceptionDefinition
name|def
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
block|{
return|return
operator|new
name|ExceptionPolicy
argument_list|(
name|def
operator|.
name|getId
argument_list|()
argument_list|,
name|CamelContextHelper
operator|.
name|getRouteId
argument_list|(
name|def
argument_list|)
argument_list|,
name|def
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
operator|!=
literal|null
operator|&&
name|def
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
argument_list|,
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|def
operator|.
name|getOutputs
argument_list|()
argument_list|)
argument_list|,
name|def
operator|.
name|getHandledPolicy
argument_list|()
argument_list|,
name|def
operator|.
name|getContinuedPolicy
argument_list|()
argument_list|,
name|def
operator|.
name|getRetryWhilePolicy
argument_list|()
argument_list|,
name|def
operator|.
name|getOnRedelivery
argument_list|()
argument_list|,
name|def
operator|.
name|getOnExceptionOccurred
argument_list|()
argument_list|,
name|def
operator|.
name|getRedeliveryPolicyRef
argument_list|()
argument_list|,
name|getRedeliveryPolicy
argument_list|(
name|def
operator|.
name|getRedeliveryPolicyType
argument_list|()
argument_list|)
argument_list|,
name|def
operator|.
name|getExceptions
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRedeliveryPolicy (RedeliveryPolicyDefinition definition)
specifier|private
specifier|static
name|Map
argument_list|<
name|RedeliveryOption
argument_list|,
name|String
argument_list|>
name|getRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyDefinition
name|definition
parameter_list|)
block|{
if|if
condition|(
name|definition
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|RedeliveryOption
argument_list|,
name|String
argument_list|>
name|policy
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|maximumRedeliveries
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|redeliveryDelay
argument_list|,
name|definition
operator|.
name|getRedeliveryDelay
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|asyncDelayedRedelivery
argument_list|,
name|definition
operator|.
name|getAsyncDelayedRedelivery
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|backOffMultiplier
argument_list|,
name|definition
operator|.
name|getBackOffMultiplier
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|useExponentialBackOff
argument_list|,
name|definition
operator|.
name|getUseExponentialBackOff
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|collisionAvoidanceFactor
argument_list|,
name|definition
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|useCollisionAvoidance
argument_list|,
name|definition
operator|.
name|getUseCollisionAvoidance
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|maximumRedeliveryDelay
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|retriesExhaustedLogLevel
argument_list|,
name|definition
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|retryAttemptedLogLevel
argument_list|,
name|definition
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|retryAttemptedLogInterval
argument_list|,
name|definition
operator|.
name|getRetryAttemptedLogInterval
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logRetryAttempted
argument_list|,
name|definition
operator|.
name|getLogRetryAttempted
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logStackTrace
argument_list|,
name|definition
operator|.
name|getLogStackTrace
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logRetryStackTrace
argument_list|,
name|definition
operator|.
name|getLogRetryStackTrace
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logHandled
argument_list|,
name|definition
operator|.
name|getLogHandled
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logNewException
argument_list|,
name|definition
operator|.
name|getLogNewException
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logContinued
argument_list|,
name|definition
operator|.
name|getLogContinued
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logExhausted
argument_list|,
name|definition
operator|.
name|getLogExhausted
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logExhaustedMessageHistory
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageHistory
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|logExhaustedMessageBody
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageBody
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|disableRedelivery
argument_list|,
name|definition
operator|.
name|getDisableRedelivery
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|delayPattern
argument_list|,
name|definition
operator|.
name|getDelayPattern
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|allowRedeliveryWhileStopping
argument_list|,
name|definition
operator|.
name|getAllowRedeliveryWhileStopping
argument_list|()
argument_list|)
expr_stmt|;
name|setOption
argument_list|(
name|policy
argument_list|,
name|RedeliveryOption
operator|.
name|exchangeFormatterRef
argument_list|,
name|definition
operator|.
name|getExchangeFormatterRef
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|policy
return|;
block|}
DECL|method|setOption (Map<RedeliveryOption, String> policy, RedeliveryOption option, Object value)
specifier|private
specifier|static
name|void
name|setOption
parameter_list|(
name|Map
argument_list|<
name|RedeliveryOption
argument_list|,
name|String
argument_list|>
name|policy
parameter_list|,
name|RedeliveryOption
name|option
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|policy
operator|.
name|put
argument_list|(
name|option
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|routeContext
operator|.
name|addErrorHandlerFactoryReference
argument_list|(
name|other
argument_list|,
name|answer
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
comment|/**      * Creates the error handler      *      * @param routeContext the route context      * @param processor the outer processor      * @return the error handler      * @throws Exception is thrown if the error handler could not be created      */
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
specifier|public
specifier|abstract
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
function_decl|;
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
for|for
control|(
name|NamedNode
name|exception
range|:
name|routeContext
operator|.
name|getErrorHandlers
argument_list|(
name|definition
argument_list|)
control|)
block|{
name|ErrorHandlerBuilderSupport
operator|.
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
comment|/**      * Note: Not for end users - this method is used internally by camel-blueprint      */
DECL|method|createRedeliveryPolicy (RedeliveryPolicyDefinition definition, CamelContext context, RedeliveryPolicy parentPolicy)
specifier|public
specifier|static
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicyDefinition
name|definition
parameter_list|,
name|CamelContext
name|context
parameter_list|,
name|RedeliveryPolicy
name|parentPolicy
parameter_list|)
block|{
name|RedeliveryPolicy
name|answer
decl_stmt|;
if|if
condition|(
name|parentPolicy
operator|!=
literal|null
condition|)
block|{
name|answer
operator|=
name|parentPolicy
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|RedeliveryPolicy
argument_list|()
expr_stmt|;
block|}
try|try
block|{
comment|// copy across the properties - if they are set
if|if
condition|(
name|definition
operator|.
name|getMaximumRedeliveries
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveries
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRedeliveryDelay
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRedeliveryDelay
argument_list|(
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getRedeliveryDelay
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAsyncDelayedRedelivery
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAsyncDelayedRedelivery
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getAsyncDelayedRedelivery
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetriesExhaustedLogLevel
argument_list|(
name|definition
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetryAttemptedLogLevel
argument_list|(
name|definition
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getRetryAttemptedLogInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setRetryAttemptedLogInterval
argument_list|(
name|CamelContextHelper
operator|.
name|parseInteger
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getRetryAttemptedLogInterval
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getBackOffMultiplier
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setBackOffMultiplier
argument_list|(
name|CamelContextHelper
operator|.
name|parseDouble
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getBackOffMultiplier
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getUseExponentialBackOff
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseExponentialBackOff
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getUseExponentialBackOff
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setCollisionAvoidanceFactor
argument_list|(
name|CamelContextHelper
operator|.
name|parseDouble
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getUseCollisionAvoidance
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setUseCollisionAvoidance
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getUseCollisionAvoidance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveryDelay
argument_list|(
name|CamelContextHelper
operator|.
name|parseLong
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogStackTrace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogStackTrace
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogStackTrace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogRetryStackTrace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogRetryStackTrace
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogRetryStackTrace
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogHandled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogHandled
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogHandled
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogNewException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogNewException
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogNewException
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogContinued
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogContinued
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogContinued
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogRetryAttempted
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogRetryAttempted
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogRetryAttempted
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhausted
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhausted
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhausted
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhaustedMessageHistory
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhaustedMessageHistory
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageHistory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getLogExhaustedMessageBody
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setLogExhaustedMessageBody
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getLogExhaustedMessageBody
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getDisableRedelivery
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getDisableRedelivery
argument_list|()
argument_list|)
condition|)
block|{
name|answer
operator|.
name|setMaximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getDelayPattern
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setDelayPattern
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getDelayPattern
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getAllowRedeliveryWhileStopping
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAllowRedeliveryWhileStopping
argument_list|(
name|CamelContextHelper
operator|.
name|parseBoolean
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getAllowRedeliveryWhileStopping
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getExchangeFormatterRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setExchangeFormatterRef
argument_list|(
name|CamelContextHelper
operator|.
name|parseText
argument_list|(
name|context
argument_list|,
name|definition
operator|.
name|getExchangeFormatterRef
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

