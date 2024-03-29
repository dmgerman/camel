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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|ExchangePattern
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
name|NoSuchEndpointException
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
name|processor
operator|.
name|SendProcessor
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
name|DeadLetterChannel
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
name|CamelLogger
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
name|StringHelper
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
comment|/**  * A builder of a  *<a href="http://camel.apache.org/dead-letter-channel.html">Dead Letter  * Channel</a>  */
end_comment

begin_class
DECL|class|DeadLetterChannelBuilder
specifier|public
class|class
name|DeadLetterChannelBuilder
extends|extends
name|DefaultErrorHandlerBuilder
block|{
DECL|method|DeadLetterChannelBuilder ()
specifier|public
name|DeadLetterChannelBuilder
parameter_list|()
block|{
comment|// no-arg constructor used by Spring DSL
block|}
DECL|method|DeadLetterChannelBuilder (Endpoint deadLetter)
specifier|public
name|DeadLetterChannelBuilder
parameter_list|(
name|Endpoint
name|deadLetter
parameter_list|)
block|{
name|setDeadLetter
argument_list|(
name|deadLetter
argument_list|)
expr_stmt|;
comment|// DLC do not log exhausted by default
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhausted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|DeadLetterChannelBuilder (String uri)
specifier|public
name|DeadLetterChannelBuilder
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|setDeadLetterUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|validateDeadLetterUri
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|DeadLetterChannel
name|answer
init|=
operator|new
name|DeadLetterChannel
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|processor
argument_list|,
name|getLogger
argument_list|()
argument_list|,
name|getOnRedelivery
argument_list|()
argument_list|,
name|getRedeliveryPolicy
argument_list|()
argument_list|,
name|getExceptionPolicyStrategy
argument_list|()
argument_list|,
name|getFailureProcessor
argument_list|()
argument_list|,
name|getDeadLetterUri
argument_list|()
argument_list|,
name|isDeadLetterHandleNewException
argument_list|()
argument_list|,
name|isUseOriginalMessage
argument_list|()
argument_list|,
name|isUseOriginalBody
argument_list|()
argument_list|,
name|getRetryWhilePolicy
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|,
name|getExecutorService
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
argument_list|,
name|getOnPrepareFailure
argument_list|()
argument_list|,
name|getOnExceptionOccurred
argument_list|()
argument_list|)
decl_stmt|;
comment|// configure error handler before we can use it
name|configure
argument_list|(
name|routeContext
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|supportTransacted ()
specifier|public
name|boolean
name|supportTransacted
parameter_list|()
block|{
return|return
literal|false
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
name|DeadLetterChannelBuilder
name|answer
init|=
operator|new
name|DeadLetterChannelBuilder
argument_list|()
decl_stmt|;
name|super
operator|.
name|cloneBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
annotation|@
name|Override
DECL|method|getFailureProcessor ()
specifier|public
name|Processor
name|getFailureProcessor
parameter_list|()
block|{
if|if
condition|(
name|failureProcessor
operator|==
literal|null
condition|)
block|{
comment|// wrap in our special safe fallback error handler if sending to
comment|// dead letter channel fails
name|Processor
name|child
init|=
operator|new
name|SendProcessor
argument_list|(
name|deadLetter
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
comment|// force MEP to be InOnly so when sending to DLQ we would not expect
comment|// a reply if the MEP was InOut
name|failureProcessor
operator|=
operator|new
name|FatalFallbackErrorHandler
argument_list|(
name|child
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
return|return
name|failureProcessor
return|;
block|}
DECL|method|validateDeadLetterUri (RouteContext routeContext)
specifier|protected
name|void
name|validateDeadLetterUri
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|deadLetter
operator|==
literal|null
condition|)
block|{
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|deadLetterUri
argument_list|,
literal|"deadLetterUri"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|deadLetter
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getEndpoint
argument_list|(
name|deadLetterUri
argument_list|)
expr_stmt|;
if|if
condition|(
name|deadLetter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchEndpointException
argument_list|(
name|deadLetterUri
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|createLogger ()
specifier|protected
name|CamelLogger
name|createLogger
parameter_list|()
block|{
return|return
operator|new
name|CamelLogger
argument_list|(
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|)
argument_list|,
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
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
literal|"DeadLetterChannelBuilder("
operator|+
name|deadLetterUri
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

