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
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|ExecutorServiceManager
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
name|Language
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
name|spi
operator|.
name|ThreadPoolProfile
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
name|util
operator|.
name|ExpressionToPredicateAdapter
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
comment|/**  * The default error handler builder.  *  * @version   */
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
name|CamelLogger
name|logger
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
DECL|field|retryWhile
specifier|protected
name|Predicate
name|retryWhile
decl_stmt|;
DECL|field|retryWhileRef
specifier|protected
name|String
name|retryWhileRef
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
DECL|field|asyncDelayedRedelivery
specifier|protected
name|boolean
name|asyncDelayedRedelivery
decl_stmt|;
DECL|field|executorServiceRef
specifier|protected
name|String
name|executorServiceRef
decl_stmt|;
DECL|field|executorService
specifier|protected
name|ScheduledExecutorService
name|executorService
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
name|getExceptionPolicyStrategy
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
name|DefaultErrorHandlerBuilder
name|answer
init|=
operator|new
name|DefaultErrorHandlerBuilder
argument_list|()
decl_stmt|;
name|cloneBuilder
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|cloneBuilder (DefaultErrorHandlerBuilder other)
specifier|protected
name|void
name|cloneBuilder
parameter_list|(
name|DefaultErrorHandlerBuilder
name|other
parameter_list|)
block|{
name|super
operator|.
name|cloneBuilder
argument_list|(
name|other
argument_list|)
expr_stmt|;
if|if
condition|(
name|logger
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setLogger
argument_list|(
name|logger
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|redeliveryPolicy
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setRedeliveryPolicy
argument_list|(
name|redeliveryPolicy
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onRedelivery
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setOnRedelivery
argument_list|(
name|onRedelivery
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retryWhile
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setRetryWhile
argument_list|(
name|retryWhile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|retryWhileRef
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setRetryWhileRef
argument_list|(
name|retryWhileRef
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|failureProcessor
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setFailureProcessor
argument_list|(
name|failureProcessor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deadLetter
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setDeadLetter
argument_list|(
name|deadLetter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|deadLetterUri
operator|!=
literal|null
condition|)
block|{
name|other
operator|.
name|setDeadLetterUri
argument_list|(
name|deadLetterUri
argument_list|)
expr_stmt|;
block|}
name|other
operator|.
name|setUseOriginalMessage
argument_list|(
name|useOriginalMessage
argument_list|)
expr_stmt|;
name|other
operator|.
name|setAsyncDelayedRedelivery
argument_list|(
name|asyncDelayedRedelivery
argument_list|)
expr_stmt|;
name|other
operator|.
name|setExecutorServiceRef
argument_list|(
name|executorServiceRef
argument_list|)
expr_stmt|;
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
comment|/**      * @deprecated will be removed in the near future. Use {@link #redeliveryDelay(long)} instead      */
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
DECL|method|logExhaustedMessageHistory (boolean logExhaustedMessageHistory)
specifier|public
name|DefaultErrorHandlerBuilder
name|logExhaustedMessageHistory
parameter_list|(
name|boolean
name|logExhaustedMessageHistory
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setLogExhaustedMessageHistory
argument_list|(
name|logExhaustedMessageHistory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Will allow asynchronous delayed redeliveries.      *      * @see org.apache.camel.processor.RedeliveryPolicy#setAsyncDelayedRedelivery(boolean)      * @return the builder      */
DECL|method|asyncDelayedRedelivery ()
specifier|public
name|DefaultErrorHandlerBuilder
name|asyncDelayedRedelivery
parameter_list|()
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setAsyncDelayedRedelivery
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Controls whether to allow redelivery while stopping/shutting down a route that uses error handling.      *      * @param allowRedeliveryWhileStopping<tt>true</tt> to allow redelivery,<tt>false</tt> to reject redeliveries      * @return the builder      */
DECL|method|allowRedeliveryWhileStopping (boolean allowRedeliveryWhileStopping)
specifier|public
name|DefaultErrorHandlerBuilder
name|allowRedeliveryWhileStopping
parameter_list|(
name|boolean
name|allowRedeliveryWhileStopping
parameter_list|)
block|{
name|getRedeliveryPolicy
argument_list|()
operator|.
name|setAllowRedeliveryWhileStopping
argument_list|(
name|allowRedeliveryWhileStopping
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets a reference to a thread pool to be used for redelivery.      *      * @param ref reference to a scheduled thread pool      * @return the builder.      */
DECL|method|executorServiceRef (String ref)
specifier|public
name|DefaultErrorHandlerBuilder
name|executorServiceRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|setExecutorServiceRef
argument_list|(
name|ref
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Sets the logger used for caught exceptions      *      * @param logger the logger      * @return the builder      */
DECL|method|logger (CamelLogger logger)
specifier|public
name|DefaultErrorHandlerBuilder
name|logger
parameter_list|(
name|CamelLogger
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
DECL|method|log (org.slf4j.Logger log)
specifier|public
name|DefaultErrorHandlerBuilder
name|log
parameter_list|(
name|org
operator|.
name|slf4j
operator|.
name|Logger
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
name|LoggerFactory
operator|.
name|getLogger
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
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|log
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Sets a processor that should be processed<b>before</b> a redelivery attempt.      *<p/>      * Can be used to change the {@link org.apache.camel.Exchange}<b>before</b> its being redelivered.      *      * @param processor the processor      * @return the builder      */
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
comment|/**      * Sets the retry while expression.      *<p/>      * Will continue retrying until expression evaluates to<tt>false</tt>.      *      * @param retryWhile expression that determines when to stop retrying      * @return the builder      */
DECL|method|retryWhile (Expression retryWhile)
specifier|public
name|DefaultErrorHandlerBuilder
name|retryWhile
parameter_list|(
name|Expression
name|retryWhile
parameter_list|)
block|{
name|setRetryWhile
argument_list|(
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|retryWhile
argument_list|)
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
name|CamelLogger
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
DECL|method|setLogger (CamelLogger logger)
specifier|public
name|void
name|setLogger
parameter_list|(
name|CamelLogger
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
DECL|method|getRetryWhilePolicy (CamelContext context)
specifier|public
name|Predicate
name|getRetryWhilePolicy
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|Predicate
name|answer
init|=
name|getRetryWhile
argument_list|()
decl_stmt|;
if|if
condition|(
name|getRetryWhileRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// its a bean expression
name|Language
name|bean
init|=
name|context
operator|.
name|resolveLanguage
argument_list|(
literal|"bean"
argument_list|)
decl_stmt|;
name|answer
operator|=
name|bean
operator|.
name|createPredicate
argument_list|(
name|getRetryWhileRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|getRetryWhile ()
specifier|public
name|Predicate
name|getRetryWhile
parameter_list|()
block|{
return|return
name|retryWhile
return|;
block|}
DECL|method|setRetryWhile (Predicate retryWhile)
specifier|public
name|void
name|setRetryWhile
parameter_list|(
name|Predicate
name|retryWhile
parameter_list|)
block|{
name|this
operator|.
name|retryWhile
operator|=
name|retryWhile
expr_stmt|;
block|}
DECL|method|getRetryWhileRef ()
specifier|public
name|String
name|getRetryWhileRef
parameter_list|()
block|{
return|return
name|retryWhileRef
return|;
block|}
DECL|method|setRetryWhileRef (String retryWhileRef)
specifier|public
name|void
name|setRetryWhileRef
parameter_list|(
name|String
name|retryWhileRef
parameter_list|)
block|{
name|this
operator|.
name|retryWhileRef
operator|=
name|retryWhileRef
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
DECL|method|isAsyncDelayedRedelivery ()
specifier|public
name|boolean
name|isAsyncDelayedRedelivery
parameter_list|()
block|{
return|return
name|asyncDelayedRedelivery
return|;
block|}
DECL|method|setAsyncDelayedRedelivery (boolean asyncDelayedRedelivery)
specifier|public
name|void
name|setAsyncDelayedRedelivery
parameter_list|(
name|boolean
name|asyncDelayedRedelivery
parameter_list|)
block|{
name|this
operator|.
name|asyncDelayedRedelivery
operator|=
name|asyncDelayedRedelivery
expr_stmt|;
block|}
DECL|method|getExecutorServiceRef ()
specifier|public
name|String
name|getExecutorServiceRef
parameter_list|()
block|{
return|return
name|executorServiceRef
return|;
block|}
DECL|method|setExecutorServiceRef (String executorServiceRef)
specifier|public
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
name|this
operator|.
name|executorServiceRef
operator|=
name|executorServiceRef
expr_stmt|;
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
DECL|method|getExecutorService (CamelContext camelContext)
specifier|protected
specifier|synchronized
name|ScheduledExecutorService
name|getExecutorService
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
operator|||
name|executorService
operator|.
name|isShutdown
argument_list|()
condition|)
block|{
comment|// camel context will shutdown the executor when it shutdown so no need to shut it down when stopping
if|if
condition|(
name|executorServiceRef
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|executorServiceRef
argument_list|,
name|ScheduledExecutorService
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|ExecutorServiceManager
name|manager
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
name|manager
operator|.
name|getThreadPoolProfile
argument_list|(
name|executorServiceRef
argument_list|)
decl_stmt|;
name|executorService
operator|=
name|manager
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
name|executorServiceRef
argument_list|,
name|profile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|executorServiceRef
operator|+
literal|" not found in registry."
argument_list|)
throw|;
block|}
block|}
else|else
block|{
comment|// no explicit configured thread pool, so leave it up to the error handler to decide if it need
comment|// a default thread pool from CamelContext#getErrorHandlerExecutorService
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|executorService
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

