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
name|Expression
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
name|processor
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
name|processor
operator|.
name|Logger
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
name|RecipientList
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
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A builder of a<a  * href="http://camel.apache.org/dead-letter-channel.html">Dead Letter  * Channel</a>  *  * @version $Revision$  */
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
name|DeadLetterChannel
name|answer
init|=
operator|new
name|DeadLetterChannel
argument_list|(
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
name|getHandledPolicy
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
name|isUseOriginalMessage
argument_list|()
argument_list|)
decl_stmt|;
comment|// configure error handler before we can use it
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
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
comment|// Properties
comment|// -------------------------------------------------------------------------
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
if|if
condition|(
name|deadLetter
operator|!=
literal|null
condition|)
block|{
name|failureProcessor
operator|=
operator|new
name|SendProcessor
argument_list|(
name|deadLetter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a recipient list since we only have an uri for the endpoint
name|failureProcessor
operator|=
operator|new
name|RecipientList
argument_list|(
operator|new
name|Expression
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|deadLetterUri
return|;
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|deadLetterUri
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|failureProcessor
return|;
block|}
DECL|method|createHandledPolicy ()
specifier|protected
name|Predicate
name|createHandledPolicy
parameter_list|()
block|{
comment|// should be handled by default for dead letter channel
return|return
name|PredicateBuilder
operator|.
name|toPredicate
argument_list|(
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|true
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRedeliveryPolicy ()
specifier|protected
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|()
block|{
return|return
operator|new
name|RedeliveryPolicy
argument_list|()
return|;
block|}
DECL|method|createLogger ()
specifier|protected
name|Logger
name|createLogger
parameter_list|()
block|{
return|return
operator|new
name|Logger
argument_list|(
name|LogFactory
operator|.
name|getLog
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

