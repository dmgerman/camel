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

begin_class
DECL|class|DeadLetterChannelReifier
specifier|public
class|class
name|DeadLetterChannelReifier
extends|extends
name|DefaultErrorHandlerReifier
argument_list|<
name|DeadLetterChannelBuilder
argument_list|>
block|{
DECL|method|DeadLetterChannelReifier (ErrorHandlerFactory definition)
specifier|public
name|DeadLetterChannelReifier
parameter_list|(
name|ErrorHandlerFactory
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|definition
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
name|definition
operator|.
name|getLogger
argument_list|()
argument_list|,
name|definition
operator|.
name|getOnRedelivery
argument_list|()
argument_list|,
name|definition
operator|.
name|getRedeliveryPolicy
argument_list|()
argument_list|,
name|definition
operator|.
name|getExceptionPolicyStrategy
argument_list|()
argument_list|,
name|definition
operator|.
name|getFailureProcessor
argument_list|()
argument_list|,
name|definition
operator|.
name|getDeadLetterUri
argument_list|()
argument_list|,
name|definition
operator|.
name|isDeadLetterHandleNewException
argument_list|()
argument_list|,
name|definition
operator|.
name|isUseOriginalMessage
argument_list|()
argument_list|,
name|definition
operator|.
name|isUseOriginalBody
argument_list|()
argument_list|,
name|definition
operator|.
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
name|definition
operator|.
name|getOnPrepareFailure
argument_list|()
argument_list|,
name|definition
operator|.
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
DECL|method|validateDeadLetterUri (RouteContext routeContext)
specifier|protected
name|void
name|validateDeadLetterUri
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|Endpoint
name|deadLetter
init|=
name|definition
operator|.
name|getDeadLetter
argument_list|()
decl_stmt|;
name|String
name|deadLetterUri
init|=
name|definition
operator|.
name|getDeadLetterUri
argument_list|()
decl_stmt|;
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
comment|// TODO: ErrorHandler: no modification to the model should be done
name|definition
operator|.
name|setDeadLetter
argument_list|(
name|deadLetter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

