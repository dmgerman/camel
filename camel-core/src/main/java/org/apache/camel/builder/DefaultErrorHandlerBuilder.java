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
name|DefaultErrorHandler
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|PredicateBuilder
operator|.
name|toPredicate
import|;
end_import

begin_comment
comment|/**  * The default error handler builder.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultErrorHandlerBuilder
specifier|public
class|class
name|DefaultErrorHandlerBuilder
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|field|logger
specifier|protected
name|Logger
name|logger
decl_stmt|;
DECL|field|exceptionPolicyStrategy
specifier|protected
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
init|=
name|ErrorHandlerSupport
operator|.
name|createDefaultExceptionPolicyStrategy
argument_list|()
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|protected
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|field|onRedelivery
specifier|protected
name|Processor
name|onRedelivery
decl_stmt|;
DECL|field|handledPolicy
specifier|protected
name|Predicate
name|handledPolicy
decl_stmt|;
DECL|field|failureProcessor
specifier|protected
name|Processor
name|failureProcessor
decl_stmt|;
DECL|field|deadLetter
specifier|protected
name|Endpoint
name|deadLetter
decl_stmt|;
DECL|field|deadLetterUri
specifier|protected
name|String
name|deadLetterUri
decl_stmt|;
DECL|field|useOriginalMessage
specifier|protected
name|boolean
name|useOriginalMessage
decl_stmt|;
DECL|method|DefaultErrorHandlerBuilder ()
specifier|public
name|DefaultErrorHandlerBuilder
parameter_list|()
block|{     }
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
name|DefaultErrorHandler
name|answer
init|=
operator|new
name|DefaultErrorHandler
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
name|getHandledPolicy
argument_list|()
argument_list|,
name|getExceptionPolicyStrategy
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
comment|// Builder methods
comment|// -------------------------------------------------------------------------
DECL|method|backOffMultiplier (double backOffMultiplier)
specifier|public
name|DefaultErrorHandlerBuilder
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
DECL|method|collisionAvoidancePercent (double collisionAvoidancePercent)
specifier|public
name|DefaultErrorHandlerBuilder
name|collisionAvoidancePercent
parameter_list|(
name|double
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
annotation|@
name|Deprecated
DECL|method|redeliverDelay (long delay)
specifier|public
name|DefaultErrorHandlerBuilder
name|redeliverDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|redeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|redeliveryDelay (long delay)
specifier|public
name|DefaultErrorHandlerBuilder
name|redeliveryDelay
parameter_list|(
name|long
name|delay
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|redeliveryDelay
argument_list|(
name|delay
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|syncDelayedRedelivery ()
specifier|public
name|DefaultErrorHandlerBuilder
name|syncDelayedRedelivery
parameter_list|()
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|syncDelayedRedelivery
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|delayPattern (String delayPattern)
specifier|public
name|DefaultErrorHandlerBuilder
name|delayPattern
parameter_list|(
name|String
name|delayPattern
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|delayPattern
argument_list|(
name|delayPattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveries (int maximumRedeliveries)
specifier|public
name|DefaultErrorHandlerBuilder
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
DECL|method|disableRedelivery ()
specifier|public
name|DefaultErrorHandlerBuilder
name|disableRedelivery
parameter_list|()
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maximumRedeliveryDelay (long maximumRedeliveryDelay)
specifier|public
name|DefaultErrorHandlerBuilder
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
name|DefaultErrorHandlerBuilder
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
name|DefaultErrorHandlerBuilder
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
name|DefaultErrorHandlerBuilder
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
name|DefaultErrorHandlerBuilder
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
DECL|method|logStackTrace (boolean logStackTrace)
specifier|public
name|DefaultErrorHandlerBuilder
name|logStackTrace
parameter_list|(
name|boolean
name|logStackTrace
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogStackTrace
argument_list|(
name|logStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|logRetryStackTrace (boolean logRetryStackTrace)
specifier|public
name|DefaultErrorHandlerBuilder
name|logRetryStackTrace
parameter_list|(
name|boolean
name|logRetryStackTrace
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogRetryStackTrace
argument_list|(
name|logRetryStackTrace
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|logHandled (boolean logHandled)
specifier|public
name|DefaultErrorHandlerBuilder
name|logHandled
parameter_list|(
name|boolean
name|logHandled
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogHandled
argument_list|(
name|logHandled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|logExhausted (boolean logExhausted)
specifier|public
name|DefaultErrorHandlerBuilder
name|logExhausted
parameter_list|(
name|boolean
name|logExhausted
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhausted
argument_list|(
name|logExhausted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  handled or not      * @return the builder      */
annotation|@
name|Deprecated
DECL|method|handled (boolean handled)
specifier|public
name|DefaultErrorHandlerBuilder
name|handled
parameter_list|(
name|boolean
name|handled
parameter_list|)
block|{
name|Expression
name|expression
init|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|Boolean
operator|.
name|toString
argument_list|(
name|handled
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|handled
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  predicate that determines true or false      * @return the builder      */
annotation|@
name|Deprecated
DECL|method|handled (Predicate handled)
specifier|public
name|DefaultErrorHandlerBuilder
name|handled
parameter_list|(
name|Predicate
name|handled
parameter_list|)
block|{
name|this
operator|.
name|setHandledPolicy
argument_list|(
name|handled
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets whether the exchange should be marked as handled or not.      *      * @param handled  expression that determines true or false      * @return the builder      */
annotation|@
name|Deprecated
DECL|method|handled (Expression handled)
specifier|public
name|DefaultErrorHandlerBuilder
name|handled
parameter_list|(
name|Expression
name|handled
parameter_list|)
block|{
name|this
operator|.
name|setHandledPolicy
argument_list|(
name|toPredicate
argument_list|(
name|handled
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logger used for caught exceptions      *      * @param logger the logger      * @return the builder      */
DECL|method|logger (Logger logger)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Sets the logging level of exceptions caught      *      * @param level the logging level      * @return the builder      */
DECL|method|loggingLevel (LoggingLevel level)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Sets the log used for caught exceptions      *      * @param log the logger      * @return the builder      */
DECL|method|log (Log log)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Sets the log used for caught exceptions      *      * @param log the log name      * @return the builder      */
DECL|method|log (String log)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Sets the log used for caught exceptions      *      * @param log the log class      * @return the builder      */
DECL|method|log (Class<?> log)
specifier|public
name|DefaultErrorHandlerBuilder
name|log
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
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
comment|/**      * Sets the exception policy to use      *      * @return the builder      */
DECL|method|exceptionPolicyStrategy (ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Sets a processor that should be processed<b>before</b> a redelivey attempt.      *<p/>      * Can be used to change the {@link org.apache.camel.Exchange}<b>before</b> its being redelivered.      *      * @return the builder      */
DECL|method|onRedelivery (Processor processor)
specifier|public
name|DefaultErrorHandlerBuilder
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
comment|/**      * Will use the original input {@link org.apache.camel.Message} when an {@link org.apache.camel.Exchange}      * is moved to the dead letter queue.      *<p/>      *<b>Notice:</b> this only applies when all redeliveries attempt have failed and the {@link org.apache.camel.Exchange}      * is doomed for failure.      *<br/>      * Instead of using the current inprogress {@link org.apache.camel.Exchange} IN message we use the original      * IN message instead. This allows you to store the original input in the dead letter queue instead of the inprogress      * snapshot of the IN message.      * For instance if you route transform the IN body during routing and then failed. With the original exchange      * store in the dead letter queue it might be easier to manually re submit the {@link org.apache.camel.Exchange}      * again as the IN message is the same as when Camel received it.      * So you should be able to send the {@link org.apache.camel.Exchange} to the same input.      *<p/>      * By default this feature is off.      *      * @return the builder      */
DECL|method|useOriginalMessage ()
specifier|public
name|DefaultErrorHandlerBuilder
name|useOriginalMessage
parameter_list|()
block|{
name|setUseOriginalMessage
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
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
return|return
name|failureProcessor
return|;
block|}
DECL|method|setFailureProcessor (Processor failureProcessor)
specifier|public
name|void
name|setFailureProcessor
parameter_list|(
name|Processor
name|failureProcessor
parameter_list|)
block|{
name|this
operator|.
name|failureProcessor
operator|=
name|failureProcessor
expr_stmt|;
block|}
DECL|method|getRedeliveryPolicy ()
specifier|public
name|RedeliveryPolicy
name|getRedeliveryPolicy
parameter_list|()
block|{
if|if
condition|(
name|redeliveryPolicy
operator|==
literal|null
condition|)
block|{
name|redeliveryPolicy
operator|=
name|createRedeliveryPolicy
argument_list|()
expr_stmt|;
block|}
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
DECL|method|getLogger ()
specifier|public
name|Logger
name|getLogger
parameter_list|()
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|createLogger
argument_list|()
expr_stmt|;
block|}
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
comment|/**      * Sets the exception policy strategy to use for resolving the {@link org.apache.camel.model.OnExceptionDefinition}      * to use for a given thrown exception      */
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
name|Deprecated
DECL|method|getHandledPolicy ()
specifier|public
name|Predicate
name|getHandledPolicy
parameter_list|()
block|{
if|if
condition|(
name|handledPolicy
operator|==
literal|null
condition|)
block|{
name|handledPolicy
operator|=
name|createHandledPolicy
argument_list|()
expr_stmt|;
block|}
return|return
name|handledPolicy
return|;
block|}
annotation|@
name|Deprecated
DECL|method|setHandledPolicy (Predicate handled)
specifier|public
name|void
name|setHandledPolicy
parameter_list|(
name|Predicate
name|handled
parameter_list|)
block|{
name|this
operator|.
name|handledPolicy
operator|=
name|handled
expr_stmt|;
block|}
comment|/**      * Sets the handled using a boolean and thus easier to use for Spring XML configuration as well      */
annotation|@
name|Deprecated
DECL|method|setHandled (boolean handled)
specifier|public
name|void
name|setHandled
parameter_list|(
name|boolean
name|handled
parameter_list|)
block|{
name|handled
argument_list|(
name|handled
argument_list|)
expr_stmt|;
block|}
DECL|method|getDeadLetterUri ()
specifier|public
name|String
name|getDeadLetterUri
parameter_list|()
block|{
return|return
name|deadLetterUri
return|;
block|}
DECL|method|setDeadLetterUri (String deadLetterUri)
specifier|public
name|void
name|setDeadLetterUri
parameter_list|(
name|String
name|deadLetterUri
parameter_list|)
block|{
name|this
operator|.
name|deadLetter
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetterUri
expr_stmt|;
block|}
DECL|method|getDeadLetter ()
specifier|public
name|Endpoint
name|getDeadLetter
parameter_list|()
block|{
return|return
name|deadLetter
return|;
block|}
DECL|method|setDeadLetter (Endpoint deadLetter)
specifier|public
name|void
name|setDeadLetter
parameter_list|(
name|Endpoint
name|deadLetter
parameter_list|)
block|{
name|this
operator|.
name|deadLetter
operator|=
name|deadLetter
expr_stmt|;
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetter
operator|.
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
DECL|method|isUseOriginalMessage ()
specifier|public
name|boolean
name|isUseOriginalMessage
parameter_list|()
block|{
return|return
name|useOriginalMessage
return|;
block|}
DECL|method|setUseOriginalMessage (boolean useOriginalMessage)
specifier|public
name|void
name|setUseOriginalMessage
parameter_list|(
name|boolean
name|useOriginalMessage
parameter_list|)
block|{
name|this
operator|.
name|useOriginalMessage
operator|=
name|useOriginalMessage
expr_stmt|;
block|}
DECL|method|createHandledPolicy ()
specifier|protected
name|Predicate
name|createHandledPolicy
parameter_list|()
block|{
comment|// should NOT be handled by default for default error handler
return|return
name|PredicateBuilder
operator|.
name|toPredicate
argument_list|(
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
literal|false
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createRedeliveryPolicy ()
specifier|protected
name|RedeliveryPolicy
name|createRedeliveryPolicy
parameter_list|()
block|{
name|RedeliveryPolicy
name|policy
init|=
operator|new
name|RedeliveryPolicy
argument_list|()
decl_stmt|;
name|policy
operator|.
name|disableRedelivery
argument_list|()
expr_stmt|;
name|policy
operator|.
name|setRedeliveryDelay
argument_list|(
literal|0
argument_list|)
expr_stmt|;
return|return
name|policy
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
name|DefaultErrorHandler
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
literal|"DefaultErrorHandlerBuilder"
return|;
block|}
block|}
end_class

end_unit

