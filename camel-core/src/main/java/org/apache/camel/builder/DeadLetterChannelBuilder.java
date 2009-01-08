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
name|commons
operator|.
name|logging
operator|.
name|Log
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
comment|/**  * A builder of a<a  * href="http://activemq.apache.org/camel/dead-letter-channel.html">Dead Letter  * Channel</a>  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DeadLetterChannelBuilder
specifier|public
class|class
name|DeadLetterChannelBuilder
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
init|=
operator|new
name|RedeliveryPolicy
argument_list|()
decl_stmt|;
DECL|field|onRedelivery
specifier|private
name|Processor
name|onRedelivery
decl_stmt|;
DECL|field|exceptionPolicyStrategy
specifier|private
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
init|=
name|ErrorHandlerSupport
operator|.
name|createDefaultExceptionPolicyStrategy
argument_list|()
decl_stmt|;
DECL|field|deadLetterFactory
specifier|private
name|ProcessorFactory
name|deadLetterFactory
decl_stmt|;
DECL|field|defaultDeadLetterEndpoint
specifier|private
name|Processor
name|defaultDeadLetterEndpoint
decl_stmt|;
DECL|field|defaultDeadLetterEndpointExpression
specifier|private
name|Expression
name|defaultDeadLetterEndpointExpression
decl_stmt|;
DECL|field|defaultDeadLetterEndpointUri
specifier|private
name|String
name|defaultDeadLetterEndpointUri
init|=
literal|"log:org.apache.camel.DeadLetterChannel?level=error"
decl_stmt|;
DECL|field|logger
specifier|private
name|Logger
name|logger
init|=
name|DeadLetterChannel
operator|.
name|createDefaultLogger
argument_list|()
decl_stmt|;
DECL|method|DeadLetterChannelBuilder ()
specifier|public
name|DeadLetterChannelBuilder
parameter_list|()
block|{     }
DECL|method|DeadLetterChannelBuilder (Processor processor)
specifier|public
name|DeadLetterChannelBuilder
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|ConstantProcessorBuilder
argument_list|(
name|processor
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|DeadLetterChannelBuilder (ProcessorFactory deadLetterFactory)
specifier|public
name|DeadLetterChannelBuilder
parameter_list|(
name|ProcessorFactory
name|deadLetterFactory
parameter_list|)
block|{
name|this
operator|.
name|deadLetterFactory
operator|=
name|deadLetterFactory
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|ErrorHandlerBuilder
name|copy
parameter_list|()
block|{
name|DeadLetterChannelBuilder
name|answer
init|=
operator|new
name|DeadLetterChannelBuilder
argument_list|(
name|deadLetterFactory
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setRedeliveryPolicy
argument_list|(
name|getRedeliveryPolicy
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
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
name|Processor
name|deadLetter
init|=
name|getDeadLetterFactory
argument_list|()
operator|.
name|createProcessor
argument_list|()
decl_stmt|;
name|DeadLetterChannel
name|answer
init|=
operator|new
name|DeadLetterChannel
argument_list|(
name|processor
argument_list|,
name|deadLetter
argument_list|,
name|onRedelivery
argument_list|,
name|getRedeliveryPolicy
argument_list|()
argument_list|,
name|getLogger
argument_list|()
argument_list|,
name|getExceptionPolicyStrategy
argument_list|()
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|// Builder methods
comment|// -------------------------------------------------------------------------
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|DeadLetterChannelBuilder
name|backOffMultiplier
parameter_list|(
name|double
name|backOffMultiplier
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|backOffMultiplier
argument_list|(
name|backOffMultiplier
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|collisionAvoidancePercent (short collisionAvoidancePercent)
specifier|public
name|DeadLetterChannelBuilder
name|collisionAvoidancePercent
parameter_list|(
name|short
name|collisionAvoidancePercent
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|collisionAvoidancePercent
argument_list|(
name|collisionAvoidancePercent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|delay (long delay)
specifier|public
name|DeadLetterChannelBuilder
name|delay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|delay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|DeadLetterChannelBuilder
name|maximumRedeliveries
parameter_list|(
name|int
name|maximumRedeliveries
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
name|maximumRedeliveries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|DeadLetterChannelBuilder
name|maximumRedeliveryDelay
parameter_list|(
name|long
name|maximumRedeliveryDelay
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|maximumRedeliveryDelay
argument_list|(
name|maximumRedeliveryDelay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|useCollisionAvoidance ()
specifier|public
name|DeadLetterChannelBuilder
name|useCollisionAvoidance
parameter_list|()
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|useCollisionAvoidance
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|useExponentialBackOff ()
specifier|public
name|DeadLetterChannelBuilder
name|useExponentialBackOff
parameter_list|()
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|useExponentialBackOff
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|retriesExhaustedLogLevel (LoggingLevel retriesExhaustedLogLevel)
specifier|public
name|DeadLetterChannelBuilder
name|retriesExhaustedLogLevel
parameter_list|(
name|LoggingLevel
name|retriesExhaustedLogLevel
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setRetriesExhaustedLogLevel
argument_list|(
name|retriesExhaustedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|retryAttemptedLogLevel (LoggingLevel retryAttemptedLogLevel)
specifier|public
name|DeadLetterChannelBuilder
name|retryAttemptedLogLevel
parameter_list|(
name|LoggingLevel
name|retryAttemptedLogLevel
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setRetryAttemptedLogLevel
argument_list|(
name|retryAttemptedLogLevel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logger used for caught exceptions      */
DECL|method|logger (Logger logger)
specifier|public
name|DeadLetterChannelBuilder
name|logger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logging level of exceptions caught      */
DECL|method|loggingLevel (LoggingLevel level)
specifier|public
name|DeadLetterChannelBuilder
name|loggingLevel
parameter_list|(
name|LoggingLevel
name|level
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|setLevel
argument_list|(
name|level
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the log used for caught exceptions      */
DECL|method|log (Log log)
specifier|public
name|DeadLetterChannelBuilder
name|log
parameter_list|(
name|Log
name|log
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|setLog
argument_list|(
name|log
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the log used for caught exceptions      */
DECL|method|log (String log)
specifier|public
name|DeadLetterChannelBuilder
name|log
parameter_list|(
name|String
name|log
parameter_list|)
block|{
return|return
name|log
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|log
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the log used for caught exceptions      */
DECL|method|log (Class log)
specifier|public
name|DeadLetterChannelBuilder
name|log
parameter_list|(
name|Class
name|log
parameter_list|)
block|{
return|return
name|log
argument_list|(
name|LogFactory
operator|.
name|getLog
argument_list|(
name|log
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets the exception policy to use      */
DECL|method|exceptionPolicyStrategy (ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|DeadLetterChannelBuilder
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
comment|/**      * Sets a processor that should be processed<b>before</b> a redelivey attempt.      *<p/>      * Can be used to change the {@link org.apache.camel.Exchange}<b>before</b> its being redelivered.      */
DECL|method|onRedelivery (Processor processor)
specifier|public
name|DeadLetterChannelBuilder
name|onRedelivery
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|setOnRedelivery
argument_list|(
name|processor
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
name|getRedeliveryPolicy
parameter_list|()
block|{
return|return
name|redeliveryPolicy
return|;
block|}
comment|/**      * Sets the redelivery policy      */
DECL|method|setRedeliveryPolicy (RedeliveryPolicy redeliveryPolicy)
specifier|public
name|void
name|setRedeliveryPolicy
parameter_list|(
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
block|}
DECL|method|getDeadLetterFactory ()
specifier|public
name|ProcessorFactory
name|getDeadLetterFactory
parameter_list|()
block|{
if|if
condition|(
name|deadLetterFactory
operator|==
literal|null
condition|)
block|{
name|deadLetterFactory
operator|=
operator|new
name|ProcessorFactory
argument_list|()
block|{
specifier|public
name|Processor
name|createProcessor
parameter_list|()
block|{
return|return
name|getDefaultDeadLetterEndpoint
argument_list|()
return|;
block|}
block|}
expr_stmt|;
block|}
return|return
name|deadLetterFactory
return|;
block|}
comment|/**      * Sets the default dead letter queue factory      */
DECL|method|setDeadLetterFactory (ProcessorFactory deadLetterFactory)
specifier|public
name|void
name|setDeadLetterFactory
parameter_list|(
name|ProcessorFactory
name|deadLetterFactory
parameter_list|)
block|{
name|this
operator|.
name|deadLetterFactory
operator|=
name|deadLetterFactory
expr_stmt|;
block|}
DECL|method|getDefaultDeadLetterEndpoint ()
specifier|public
name|Processor
name|getDefaultDeadLetterEndpoint
parameter_list|()
block|{
if|if
condition|(
name|defaultDeadLetterEndpoint
operator|==
literal|null
condition|)
block|{
name|defaultDeadLetterEndpoint
operator|=
operator|new
name|RecipientList
argument_list|(
name|getDefaultDeadLetterEndpointExpression
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultDeadLetterEndpoint
return|;
block|}
comment|/**      * Sets the default dead letter endpoint used      */
DECL|method|setDefaultDeadLetterEndpoint (Processor defaultDeadLetterEndpoint)
specifier|public
name|void
name|setDefaultDeadLetterEndpoint
parameter_list|(
name|Processor
name|defaultDeadLetterEndpoint
parameter_list|)
block|{
name|this
operator|.
name|defaultDeadLetterEndpoint
operator|=
name|defaultDeadLetterEndpoint
expr_stmt|;
block|}
DECL|method|getDefaultDeadLetterEndpointExpression ()
specifier|public
name|Expression
name|getDefaultDeadLetterEndpointExpression
parameter_list|()
block|{
if|if
condition|(
name|defaultDeadLetterEndpointExpression
operator|==
literal|null
condition|)
block|{
name|defaultDeadLetterEndpointExpression
operator|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|getDefaultDeadLetterEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|defaultDeadLetterEndpointExpression
return|;
block|}
comment|/**      * Sets the expression used to decide the dead letter channel endpoint for      * an exchange if no factory is provided via      * {@link #setDeadLetterFactory(ProcessorFactory)}      */
DECL|method|setDefaultDeadLetterEndpointExpression (Expression defaultDeadLetterEndpointExpression)
specifier|public
name|void
name|setDefaultDeadLetterEndpointExpression
parameter_list|(
name|Expression
name|defaultDeadLetterEndpointExpression
parameter_list|)
block|{
name|this
operator|.
name|defaultDeadLetterEndpointExpression
operator|=
name|defaultDeadLetterEndpointExpression
expr_stmt|;
block|}
DECL|method|getDefaultDeadLetterEndpointUri ()
specifier|public
name|String
name|getDefaultDeadLetterEndpointUri
parameter_list|()
block|{
return|return
name|defaultDeadLetterEndpointUri
return|;
block|}
comment|/**      * Sets the default dead letter endpoint URI used if no factory is provided      * via {@link #setDeadLetterFactory(ProcessorFactory)} and no expression is      * provided via {@link #setDefaultDeadLetterEndpointExpression(Expression)}      *      * @param defaultDeadLetterEndpointUri the default URI if no deadletter      *                factory or expression is provided      */
DECL|method|setDefaultDeadLetterEndpointUri (String defaultDeadLetterEndpointUri)
specifier|public
name|void
name|setDefaultDeadLetterEndpointUri
parameter_list|(
name|String
name|defaultDeadLetterEndpointUri
parameter_list|)
block|{
name|this
operator|.
name|defaultDeadLetterEndpointUri
operator|=
name|defaultDeadLetterEndpointUri
expr_stmt|;
block|}
DECL|method|getLogger ()
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
DECL|method|setLogger (Logger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|Logger
name|logger
parameter_list|)
block|{
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
block|}
comment|/**      * Sets the exception policy strategy to use for resolving the {@link org.apache.camel.model.ExceptionType}      * to use for a given thrown exception      */
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
name|this
operator|.
name|exceptionPolicyStrategy
operator|=
name|exceptionPolicyStrategy
expr_stmt|;
block|}
DECL|method|getOnRedelivery ()
specifier|public
name|Processor
name|getOnRedelivery
parameter_list|()
block|{
return|return
name|onRedelivery
return|;
block|}
DECL|method|setOnRedelivery (Processor onRedelivery)
specifier|public
name|void
name|setOnRedelivery
parameter_list|(
name|Processor
name|onRedelivery
parameter_list|)
block|{
name|this
operator|.
name|onRedelivery
operator|=
name|onRedelivery
expr_stmt|;
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
operator|(
name|deadLetterFactory
operator|!=
literal|null
condition|?
name|deadLetterFactory
else|:
name|defaultDeadLetterEndpoint
operator|)
operator|+
literal|")"
return|;
block|}
block|}
end_class

end_unit

