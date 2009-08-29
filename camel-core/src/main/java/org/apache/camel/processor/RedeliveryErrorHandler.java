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
name|concurrent
operator|.
name|RejectedExecutionException
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
name|Message
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
name|util
operator|.
name|EventHelper
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
name|ExchangeHelper
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
name|MessageHelper
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
name|ServiceHelper
import|;
end_import

begin_comment
comment|/**  * Base redeliverable error handler that also supports a final dead letter queue in case  * all redelivery attempts fail.  *<p/>  * This implementation should contain all the error handling logic and the sub classes  * should only configure it according to what they support.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|RedeliveryErrorHandler
specifier|public
specifier|abstract
class|class
name|RedeliveryErrorHandler
extends|extends
name|ErrorHandlerSupport
implements|implements
name|Processor
block|{
DECL|field|deadLetter
specifier|protected
specifier|final
name|Processor
name|deadLetter
decl_stmt|;
DECL|field|deadLetterUri
specifier|protected
specifier|final
name|String
name|deadLetterUri
decl_stmt|;
DECL|field|output
specifier|protected
specifier|final
name|Processor
name|output
decl_stmt|;
DECL|field|redeliveryProcessor
specifier|protected
specifier|final
name|Processor
name|redeliveryProcessor
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|protected
specifier|final
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|field|handledPolicy
specifier|protected
specifier|final
name|Predicate
name|handledPolicy
decl_stmt|;
DECL|field|logger
specifier|protected
specifier|final
name|Logger
name|logger
decl_stmt|;
DECL|field|useOriginalMessagePolicy
specifier|protected
specifier|final
name|boolean
name|useOriginalMessagePolicy
decl_stmt|;
DECL|class|RedeliveryData
specifier|protected
class|class
name|RedeliveryData
block|{
DECL|field|redeliveryCounter
name|int
name|redeliveryCounter
decl_stmt|;
DECL|field|redeliveryDelay
name|long
name|redeliveryDelay
decl_stmt|;
DECL|field|retryUntilPredicate
name|Predicate
name|retryUntilPredicate
decl_stmt|;
comment|// default behavior which can be overloaded on a per exception basis
DECL|field|currentRedeliveryPolicy
name|RedeliveryPolicy
name|currentRedeliveryPolicy
init|=
name|redeliveryPolicy
decl_stmt|;
DECL|field|deadLetterProcessor
name|Processor
name|deadLetterProcessor
init|=
name|deadLetter
decl_stmt|;
DECL|field|failureProcessor
name|Processor
name|failureProcessor
decl_stmt|;
DECL|field|onRedeliveryProcessor
name|Processor
name|onRedeliveryProcessor
init|=
name|redeliveryProcessor
decl_stmt|;
DECL|field|handledPredicate
name|Predicate
name|handledPredicate
init|=
name|handledPolicy
decl_stmt|;
DECL|field|useOriginalInMessage
name|boolean
name|useOriginalInMessage
init|=
name|useOriginalMessagePolicy
decl_stmt|;
block|}
DECL|method|RedeliveryErrorHandler (Processor output, Logger logger, Processor redeliveryProcessor, RedeliveryPolicy redeliveryPolicy, Predicate handledPolicy, Processor deadLetter, String deadLetterUri, boolean useOriginalMessagePolicy)
specifier|public
name|RedeliveryErrorHandler
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|Processor
name|redeliveryProcessor
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|Predicate
name|handledPolicy
parameter_list|,
name|Processor
name|deadLetter
parameter_list|,
name|String
name|deadLetterUri
parameter_list|,
name|boolean
name|useOriginalMessagePolicy
parameter_list|)
block|{
name|this
operator|.
name|redeliveryProcessor
operator|=
name|redeliveryProcessor
expr_stmt|;
name|this
operator|.
name|deadLetter
operator|=
name|deadLetter
expr_stmt|;
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
name|this
operator|.
name|redeliveryPolicy
operator|=
name|redeliveryPolicy
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|logger
expr_stmt|;
name|this
operator|.
name|deadLetterUri
operator|=
name|deadLetterUri
expr_stmt|;
name|this
operator|.
name|handledPolicy
operator|=
name|handledPolicy
expr_stmt|;
name|this
operator|.
name|useOriginalMessagePolicy
operator|=
name|useOriginalMessagePolicy
expr_stmt|;
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|output
operator|==
literal|null
condition|)
block|{
comment|// no output then just return
return|return;
block|}
name|processErrorHandler
argument_list|(
name|exchange
argument_list|,
operator|new
name|RedeliveryData
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Processes the exchange decorated with this dead letter channel.      */
DECL|method|processErrorHandler (final Exchange exchange, final RedeliveryData data)
specifier|protected
name|void
name|processErrorHandler
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
throws|throws
name|Exception
block|{
while|while
condition|(
literal|true
condition|)
block|{
comment|// we can't keep retrying if the route is being shutdown.
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Rejected execution as we are not started for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// do not handle transacted exchanges that failed as this error handler does not support it
if|if
condition|(
name|exchange
operator|.
name|isTransacted
argument_list|()
operator|&&
operator|!
name|supportTransacted
argument_list|()
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"This error handler does not support transacted exchanges."
operator|+
literal|" Bypassing this error handler: "
operator|+
name|this
operator|+
literal|" for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|// did previous processing cause an exception?
name|boolean
name|handle
init|=
name|shouldHandleException
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|handle
condition|)
block|{
name|handleException
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
comment|// compute if we should redeliver or not
name|boolean
name|shouldRedeliver
init|=
name|shouldRedeliver
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|shouldRedeliver
condition|)
block|{
comment|// no we should not redeliver to the same output so either try an onException (if any given)
comment|// or the dead letter queue
name|Processor
name|target
init|=
name|data
operator|.
name|failureProcessor
operator|!=
literal|null
condition|?
name|data
operator|.
name|failureProcessor
else|:
name|data
operator|.
name|deadLetterProcessor
decl_stmt|;
comment|// deliver to the failure processor (either an on exception or dead letter queue
name|deliverToFailureProcessor
argument_list|(
name|target
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// prepare the exchange for failure before returning
name|prepareExchangeAfterFailure
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|// fire event if we had a failure processor to handle it
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
name|boolean
name|deadLetterChannel
init|=
name|target
operator|==
name|data
operator|.
name|deadLetterProcessor
operator|&&
name|data
operator|.
name|deadLetterProcessor
operator|!=
literal|null
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeFailureHandled
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|target
argument_list|,
name|deadLetterChannel
argument_list|)
expr_stmt|;
block|}
comment|// and then return
return|return;
block|}
comment|// if we are redelivering then sleep before trying again
if|if
condition|(
name|shouldRedeliver
operator|&&
name|data
operator|.
name|redeliveryCounter
operator|>
literal|0
condition|)
block|{
name|prepareExchangeForRedelivery
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// wait until we should redeliver
try|try
block|{
name|data
operator|.
name|redeliveryDelay
operator|=
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|sleep
argument_list|(
name|data
operator|.
name|redeliveryDelay
argument_list|,
name|data
operator|.
name|redeliveryCounter
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sleep interrupted, are we stopping? "
operator|+
operator|(
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
operator|)
argument_list|)
expr_stmt|;
comment|// continue from top
continue|continue;
block|}
comment|// letting onRedeliver be executed
name|deliverToRedeliveryProcessor
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange (also redelivery)
try|try
block|{
name|processExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|boolean
name|done
init|=
name|isDone
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|done
condition|)
block|{
return|return;
block|}
comment|// error occurred so loop back around.....
block|}
block|}
comment|/**      * Strategy whether the exchange has an exception that we should try to handle.      *<p/>      * Standard implementations should just look for an exception.      */
DECL|method|shouldHandleException (Exchange exchange)
specifier|protected
name|boolean
name|shouldHandleException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**      * Strategy to process the given exchange to the destinated output.      *<p/>      * This happens when the exchange is processed the first time and also for redeliveries      * to the same destination.      */
DECL|method|processExchange (Exchange exchange)
specifier|protected
name|void
name|processExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// process the exchange (also redelivery)
name|output
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy to determine if the exchange is done so we can continue      */
DECL|method|isDone (Exchange exchange)
specifier|protected
name|boolean
name|isDone
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// only done if the exchange hasn't failed
comment|// and it has not been handled by the failure processor
return|return
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
operator|||
name|ExchangeHelper
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Returns the output processor      */
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
return|return
name|output
return|;
block|}
comment|/**      * Returns the dead letter that message exchanges will be sent to if the      * redelivery attempts fail      */
DECL|method|getDeadLetter ()
specifier|public
name|Processor
name|getDeadLetter
parameter_list|()
block|{
return|return
name|deadLetter
return|;
block|}
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
DECL|method|prepareExchangeForRedelivery (Exchange exchange)
specifier|protected
name|void
name|prepareExchangeForRedelivery
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// okay we will give it another go so clear the exception so we can try again
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
comment|// clear rollback flags
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// reset cached streams so they can be read again
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|handleException (Exchange exchange, RedeliveryData data)
specifier|protected
name|void
name|handleException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|RedeliveryData
name|data
parameter_list|)
block|{
name|Throwable
name|e
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
comment|// store the original caused exception in a property, so we can restore it later
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// find the error handler to use (if any)
name|OnExceptionDefinition
name|exceptionPolicy
init|=
name|getExceptionPolicy
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|exceptionPolicy
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|currentRedeliveryPolicy
operator|=
name|exceptionPolicy
operator|.
name|createRedeliveryPolicy
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|data
operator|.
name|currentRedeliveryPolicy
argument_list|)
expr_stmt|;
name|data
operator|.
name|handledPredicate
operator|=
name|exceptionPolicy
operator|.
name|getHandledPolicy
argument_list|()
expr_stmt|;
name|data
operator|.
name|retryUntilPredicate
operator|=
name|exceptionPolicy
operator|.
name|getRetryUntilPolicy
argument_list|()
expr_stmt|;
name|data
operator|.
name|useOriginalInMessage
operator|=
name|exceptionPolicy
operator|.
name|getUseOriginalMessagePolicy
argument_list|()
expr_stmt|;
comment|// route specific failure handler?
name|Processor
name|processor
init|=
name|exceptionPolicy
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
name|data
operator|.
name|failureProcessor
operator|=
name|processor
expr_stmt|;
block|}
comment|// route specific on redelivey?
name|processor
operator|=
name|exceptionPolicy
operator|.
name|getOnRedelivery
argument_list|()
expr_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|data
operator|.
name|onRedeliveryProcessor
operator|=
name|processor
expr_stmt|;
block|}
block|}
name|String
name|msg
init|=
literal|"Failed delivery for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|". On delivery attempt: "
operator|+
name|data
operator|.
name|redeliveryCounter
operator|+
literal|" caught: "
operator|+
name|e
decl_stmt|;
name|logFailedDelivery
argument_list|(
literal|true
argument_list|,
name|exchange
argument_list|,
name|msg
argument_list|,
name|data
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|data
operator|.
name|redeliveryCounter
operator|=
name|incrementRedeliveryCounter
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gives an optional configure redelivery processor a chance to process before the Exchange      * will be redelivered. This can be used to alter the Exchange.      */
DECL|method|deliverToRedeliveryProcessor (final Exchange exchange, final RedeliveryData data)
specifier|protected
name|void
name|deliverToRedeliveryProcessor
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
if|if
condition|(
name|data
operator|.
name|onRedeliveryProcessor
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Redelivery processor "
operator|+
name|data
operator|.
name|onRedeliveryProcessor
operator|+
literal|" is processing Exchange: "
operator|+
name|exchange
operator|+
literal|" before its redelivered"
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|data
operator|.
name|onRedeliveryProcessor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Redelivery processor done"
argument_list|)
expr_stmt|;
block|}
comment|/**      * All redelivery attempts failed so move the exchange to the dead letter queue      */
DECL|method|deliverToFailureProcessor (final Processor processor, final Exchange exchange, final RedeliveryData data)
specifier|protected
name|void
name|deliverToFailureProcessor
parameter_list|(
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
comment|// we did not success with the redelivery so now we let the failure processor handle it
comment|// clear exception as we let the failure processor handle it
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|data
operator|.
name|handledPredicate
operator|!=
literal|null
operator|&&
name|data
operator|.
name|handledPredicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// its handled then remove traces of redelivery attempted
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// must decrement the redelivery counter as we didn't process the redelivery but is
comment|// handling by the failure handler. So we must -1 to not let the counter be out-of-sync
name|decrementRedeliveryCounter
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// reset cached streams so they can be read again
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
comment|// prepare original IN body if it should be moved instead of current body
if|if
condition|(
name|data
operator|.
name|useOriginalInMessage
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Using the original IN message instead of current"
argument_list|)
expr_stmt|;
block|}
name|Message
name|original
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getOriginalInMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|original
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Failure processor "
operator|+
name|processor
operator|+
literal|" is processing Exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Failure processor done"
argument_list|)
expr_stmt|;
name|String
name|msg
init|=
literal|"Failed delivery for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|". Processed by failure processor: "
operator|+
name|processor
decl_stmt|;
name|logFailedDelivery
argument_list|(
literal|false
argument_list|,
name|exchange
argument_list|,
name|msg
argument_list|,
name|data
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|prepareExchangeAfterFailure (final Exchange exchange, final RedeliveryData data)
specifier|protected
name|void
name|prepareExchangeAfterFailure
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
comment|// we could not process the exchange so we let the failure processor handled it
name|ExchangeHelper
operator|.
name|setFailureHandled
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// honor if already set a handling
name|boolean
name|alreadySet
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
operator|!=
literal|null
decl_stmt|;
if|if
condition|(
name|alreadySet
condition|)
block|{
name|boolean
name|handled
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"This exchange has already been marked for handling: "
operator|+
name|handled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handled
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// exception not handled, put exception back in the exchange
name|exchange
operator|.
name|setException
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|Predicate
name|handledPredicate
init|=
name|data
operator|.
name|handledPredicate
decl_stmt|;
if|if
condition|(
name|handledPredicate
operator|==
literal|null
operator|||
operator|!
name|handledPredicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"This exchange is not handled so its marked as failed: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// exception not handled, put exception back in the exchange
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Exception
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"This exchange is handled so its marked as not failed: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logFailedDelivery (boolean shouldRedeliver, Exchange exchange, String message, RedeliveryData data, Throwable e)
specifier|private
name|void
name|logFailedDelivery
parameter_list|(
name|boolean
name|shouldRedeliver
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|String
name|message
parameter_list|,
name|RedeliveryData
name|data
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|LoggingLevel
name|newLogLevel
decl_stmt|;
if|if
condition|(
name|shouldRedeliver
condition|)
block|{
name|newLogLevel
operator|=
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|getRetryAttemptedLogLevel
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|newLogLevel
operator|=
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|getRetriesExhaustedLogLevel
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|String
name|msg
init|=
literal|"Rollback exchange"
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|msg
operator|+
literal|" due: "
operator|+
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|newLogLevel
operator|==
name|LoggingLevel
operator|.
name|ERROR
operator|||
name|newLogLevel
operator|==
name|LoggingLevel
operator|.
name|FATAL
condition|)
block|{
comment|// log intented rollback on maximum WARN level (no ERROR or FATAL)
name|logger
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|LoggingLevel
operator|.
name|WARN
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// otherwise use the desired logging level
name|logger
operator|.
name|log
argument_list|(
name|msg
argument_list|,
name|newLogLevel
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|isLogStackTrace
argument_list|()
operator|&&
name|e
operator|!=
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|,
name|e
argument_list|,
name|newLogLevel
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|log
argument_list|(
name|message
argument_list|,
name|newLogLevel
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldRedeliver (Exchange exchange, RedeliveryData data)
specifier|private
name|boolean
name|shouldRedeliver
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|RedeliveryData
name|data
parameter_list|)
block|{
comment|// if marked as rollback only then do not redeliver
name|Boolean
name|rollback
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|rollback
operator|!=
literal|null
operator|&&
name|rollback
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"This exchange is marked as rollback only, should not be redelivered: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
return|return
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|shouldRedeliver
argument_list|(
name|exchange
argument_list|,
name|data
operator|.
name|redeliveryCounter
argument_list|,
name|data
operator|.
name|retryUntilPredicate
argument_list|)
return|;
block|}
comment|/**      * Increments the redelivery counter and adds the redelivered flag if the      * message has been redelivered      */
DECL|method|incrementRedeliveryCounter (Exchange exchange, Throwable e)
specifier|private
name|int
name|incrementRedeliveryCounter
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Integer
name|counter
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|next
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
name|counter
operator|+
literal|1
expr_stmt|;
block|}
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|next
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|next
return|;
block|}
comment|/**      * Prepares the redelivery counter and boolean flag for the failure handle processor      */
DECL|method|decrementRedeliveryCounter (Exchange exchange)
specifier|private
name|void
name|decrementRedeliveryCounter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Integer
name|counter
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|counter
operator|!=
literal|null
condition|)
block|{
name|int
name|prev
init|=
name|counter
operator|-
literal|1
decl_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
name|prev
argument_list|)
expr_stmt|;
comment|// set boolean flag according to counter
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
name|prev
operator|>
literal|0
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// not redelivered
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERY_COUNTER
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|REDELIVERED
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|output
argument_list|,
name|deadLetter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|deadLetter
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

