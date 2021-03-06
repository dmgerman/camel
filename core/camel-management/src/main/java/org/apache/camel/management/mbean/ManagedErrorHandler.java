begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

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
name|LoggingLevel
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedErrorHandlerMBean
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
name|spi
operator|.
name|ManagementStrategy
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

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed ErrorHandler"
argument_list|)
DECL|class|ManagedErrorHandler
specifier|public
class|class
name|ManagedErrorHandler
implements|implements
name|ManagedErrorHandlerMBean
block|{
DECL|field|routeContext
specifier|private
specifier|final
name|RouteContext
name|routeContext
decl_stmt|;
DECL|field|errorHandler
specifier|private
specifier|final
name|Processor
name|errorHandler
decl_stmt|;
DECL|field|errorHandlerBuilder
specifier|private
specifier|final
name|ErrorHandlerFactory
name|errorHandlerBuilder
decl_stmt|;
DECL|method|ManagedErrorHandler (RouteContext routeContext, Processor errorHandler, ErrorHandlerFactory builder)
specifier|public
name|ManagedErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|errorHandler
parameter_list|,
name|ErrorHandlerFactory
name|builder
parameter_list|)
block|{
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
expr_stmt|;
name|this
operator|.
name|errorHandlerBuilder
operator|=
name|builder
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getRouteContext ()
specifier|public
name|RouteContext
name|getRouteContext
parameter_list|()
block|{
return|return
name|routeContext
return|;
block|}
DECL|method|getErrorHandler ()
specifier|public
name|Processor
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|getErrorHandlerBuilder ()
specifier|public
name|ErrorHandlerFactory
name|getErrorHandlerBuilder
parameter_list|()
block|{
return|return
name|errorHandlerBuilder
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSupportRedelivery ()
specifier|public
name|boolean
name|isSupportRedelivery
parameter_list|()
block|{
return|return
name|errorHandler
operator|instanceof
name|RedeliveryErrorHandler
return|;
block|}
annotation|@
name|Override
DECL|method|isDeadLetterChannel ()
specifier|public
name|boolean
name|isDeadLetterChannel
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getDeadLetter
argument_list|()
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isDeadLetterUseOriginalMessage ()
specifier|public
name|boolean
name|isDeadLetterUseOriginalMessage
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|isUseOriginalMessagePolicy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDeadLetterUseOriginalBody ()
specifier|public
name|boolean
name|isDeadLetterUseOriginalBody
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|isUseOriginalBodyPolicy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isDeadLetterHandleNewException ()
specifier|public
name|boolean
name|isDeadLetterHandleNewException
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// must be a dead letter channel
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|isDeadLetterChannel
argument_list|()
operator|&&
name|redelivery
operator|.
name|isDeadLetterHandleNewException
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSupportTransactions ()
specifier|public
name|boolean
name|isSupportTransactions
parameter_list|()
block|{
if|if
condition|(
name|errorHandler
operator|instanceof
name|ErrorHandlerSupport
condition|)
block|{
name|ErrorHandlerSupport
name|ehs
init|=
operator|(
name|ErrorHandlerSupport
operator|)
name|errorHandler
decl_stmt|;
return|return
name|ehs
operator|.
name|supportTransacted
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getDeadLetterChannelEndpointUri ()
specifier|public
name|String
name|getDeadLetterChannelEndpointUri
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getDeadLetterUri
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMaximumRedeliveries ()
specifier|public
name|Integer
name|getMaximumRedeliveries
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getMaximumRedeliveries
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumRedeliveries (Integer maximum)
specifier|public
name|void
name|setMaximumRedeliveries
parameter_list|(
name|Integer
name|maximum
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setMaximumRedeliveries
argument_list|(
name|maximum
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMaximumRedeliveryDelay ()
specifier|public
name|Long
name|getMaximumRedeliveryDelay
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getMaximumRedeliveryDelay
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setMaximumRedeliveryDelay (Long delay)
specifier|public
name|void
name|setMaximumRedeliveryDelay
parameter_list|(
name|Long
name|delay
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setMaximumRedeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRedeliveryDelay ()
specifier|public
name|Long
name|getRedeliveryDelay
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getRedeliveryDelay
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setRedeliveryDelay (Long delay)
specifier|public
name|void
name|setRedeliveryDelay
parameter_list|(
name|Long
name|delay
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setRedeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBackOffMultiplier ()
specifier|public
name|Double
name|getBackOffMultiplier
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getBackOffMultiplier
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setBackOffMultiplier (Double multiplier)
specifier|public
name|void
name|setBackOffMultiplier
parameter_list|(
name|Double
name|multiplier
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setBackOffMultiplier
argument_list|(
name|multiplier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCollisionAvoidanceFactor ()
specifier|public
name|Double
name|getCollisionAvoidanceFactor
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getCollisionAvoidanceFactor
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setCollisionAvoidanceFactor (Double factor)
specifier|public
name|void
name|setCollisionAvoidanceFactor
parameter_list|(
name|Double
name|factor
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setCollisionAvoidanceFactor
argument_list|(
name|factor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCollisionAvoidancePercent ()
specifier|public
name|Double
name|getCollisionAvoidancePercent
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
operator|(
name|double
operator|)
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getCollisionAvoidancePercent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setCollisionAvoidancePercent (Double percent)
specifier|public
name|void
name|setCollisionAvoidancePercent
parameter_list|(
name|Double
name|percent
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setCollisionAvoidancePercent
argument_list|(
name|percent
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDelayPattern ()
specifier|public
name|String
name|getDelayPattern
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getDelayPattern
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setDelayPattern (String pattern)
specifier|public
name|void
name|setDelayPattern
parameter_list|(
name|String
name|pattern
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setDelayPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRetriesExhaustedLogLevel ()
specifier|public
name|String
name|getRetriesExhaustedLogLevel
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setRetriesExhaustedLogLevel (String level)
specifier|public
name|void
name|setRetriesExhaustedLogLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setRetriesExhaustedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRetryAttemptedLogLevel ()
specifier|public
name|String
name|getRetryAttemptedLogLevel
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setRetryAttemptedLogLevel (String level)
specifier|public
name|void
name|setRetryAttemptedLogLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setRetryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogStackTrace ()
specifier|public
name|Boolean
name|getLogStackTrace
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogStackTrace
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogStackTrace (Boolean log)
specifier|public
name|void
name|setLogStackTrace
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogStackTrace
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogRetryStackTrace ()
specifier|public
name|Boolean
name|getLogRetryStackTrace
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogRetryStackTrace
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogRetryStackTrace (Boolean log)
specifier|public
name|void
name|setLogRetryStackTrace
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogRetryStackTrace
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogHandled ()
specifier|public
name|Boolean
name|getLogHandled
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogHandled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogHandled (Boolean log)
specifier|public
name|void
name|setLogHandled
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogHandled
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogNewException ()
specifier|public
name|Boolean
name|getLogNewException
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogNewException
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogNewException (Boolean log)
specifier|public
name|void
name|setLogNewException
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogNewException
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogExhaustedMessageHistory ()
specifier|public
name|Boolean
name|getLogExhaustedMessageHistory
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogExhaustedMessageHistory
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogExhaustedMessageHistory (Boolean log)
specifier|public
name|void
name|setLogExhaustedMessageHistory
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhaustedMessageHistory
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogExhaustedMessageBody ()
specifier|public
name|Boolean
name|getLogExhaustedMessageBody
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogExhaustedMessageBody
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogExhaustedMessageBody (Boolean log)
specifier|public
name|void
name|setLogExhaustedMessageBody
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhaustedMessageBody
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogContinued ()
specifier|public
name|Boolean
name|getLogContinued
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogHandled
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogContinued (Boolean log)
specifier|public
name|void
name|setLogContinued
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogContinued
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getLogExhausted ()
specifier|public
name|Boolean
name|getLogExhausted
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isLogExhausted
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setLogExhausted (Boolean log)
specifier|public
name|void
name|setLogExhausted
parameter_list|(
name|Boolean
name|log
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhausted
argument_list|(
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUseCollisionAvoidance ()
specifier|public
name|Boolean
name|getUseCollisionAvoidance
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isUseCollisionAvoidance
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setUseCollisionAvoidance (Boolean avoidance)
specifier|public
name|void
name|setUseCollisionAvoidance
parameter_list|(
name|Boolean
name|avoidance
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setUseCollisionAvoidance
argument_list|(
name|avoidance
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUseExponentialBackOff ()
specifier|public
name|Boolean
name|getUseExponentialBackOff
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isUseExponentialBackOff
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setUseExponentialBackOff (Boolean backoff)
specifier|public
name|void
name|setUseExponentialBackOff
parameter_list|(
name|Boolean
name|backoff
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setUseExponentialBackOff
argument_list|(
name|backoff
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getAllowRedeliveryWhileStopping ()
specifier|public
name|Boolean
name|getAllowRedeliveryWhileStopping
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|isAllowRedeliveryWhileStopping
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|setAllowRedeliveryWhileStopping (Boolean allow)
specifier|public
name|void
name|setAllowRedeliveryWhileStopping
parameter_list|(
name|Boolean
name|allow
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"This error handler does not support redelivery"
argument_list|)
throw|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
name|redelivery
operator|.
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setAllowRedeliveryWhileStopping
argument_list|(
name|allow
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getPendingRedeliveryCount ()
specifier|public
name|Integer
name|getPendingRedeliveryCount
parameter_list|()
block|{
if|if
condition|(
operator|!
name|isSupportRedelivery
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|RedeliveryErrorHandler
name|redelivery
init|=
operator|(
name|RedeliveryErrorHandler
operator|)
name|errorHandler
decl_stmt|;
return|return
name|redelivery
operator|.
name|getPendingRedeliveryCount
argument_list|()
return|;
block|}
block|}
end_class

end_unit

