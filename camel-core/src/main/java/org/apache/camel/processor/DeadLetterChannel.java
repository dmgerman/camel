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
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
import|;
end_import

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
name|AsyncCallback
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
name|AsyncProcessor
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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|util
operator|.
name|AsyncProcessorHelper
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
comment|/**  * Implements a<a  * href="http://camel.apache.org/dead-letter-channel.html">Dead Letter  * Channel</a> after attempting to redeliver the message using the  * {@link RedeliveryPolicy}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DeadLetterChannel
specifier|public
class|class
name|DeadLetterChannel
extends|extends
name|ErrorHandlerSupport
implements|implements
name|AsyncProcessor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DeadLetterChannel
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|timer
specifier|private
specifier|static
name|Timer
name|timer
init|=
operator|new
name|Timer
argument_list|()
decl_stmt|;
DECL|field|output
specifier|private
specifier|final
name|Processor
name|output
decl_stmt|;
DECL|field|deadLetter
specifier|private
specifier|final
name|Processor
name|deadLetter
decl_stmt|;
DECL|field|deadLetterUri
specifier|private
specifier|final
name|String
name|deadLetterUri
decl_stmt|;
DECL|field|outputAsync
specifier|private
specifier|final
name|AsyncProcessor
name|outputAsync
decl_stmt|;
DECL|field|redeliveryPolicy
specifier|private
name|RedeliveryPolicy
name|redeliveryPolicy
decl_stmt|;
DECL|field|logger
specifier|private
name|Logger
name|logger
decl_stmt|;
DECL|field|redeliveryProcessor
specifier|private
specifier|final
name|Processor
name|redeliveryProcessor
decl_stmt|;
DECL|class|RedeliveryData
specifier|private
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
DECL|field|sync
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
DECL|field|handledPredicate
name|Predicate
name|handledPredicate
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
DECL|field|failureProcessor
name|Processor
name|failureProcessor
init|=
name|deadLetter
decl_stmt|;
DECL|field|onRedeliveryProcessor
name|Processor
name|onRedeliveryProcessor
init|=
name|redeliveryProcessor
decl_stmt|;
block|}
DECL|class|RedeliverTimerTask
specifier|private
class|class
name|RedeliverTimerTask
extends|extends
name|TimerTask
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|data
specifier|private
specifier|final
name|RedeliveryData
name|data
decl_stmt|;
DECL|method|RedeliverTimerTask (Exchange exchange, AsyncCallback callback, RedeliveryData data)
specifier|public
name|RedeliverTimerTask
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|RedeliveryData
name|data
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|//only handle the real AsyncProcess the exchange
name|outputAsync
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
comment|// Only handle the async case...
if|if
condition|(
name|sync
condition|)
block|{
return|return;
block|}
name|data
operator|.
name|sync
operator|=
literal|false
expr_stmt|;
comment|// only process if the exchange hasn't failed
comment|// and it has not been handled by the error processor
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|ExchangeHelper
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// if we are redelivering then sleep before trying again
name|asyncProcess
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|done
argument_list|(
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates the dead letter channel.      *      * @param output                    outer processor that should use this dead letter channel      * @param deadLetter                the failure processor to send failed exchanges to      * @param deadLetterUri             an optional uri for logging purpose      * @param redeliveryProcessor       an optional processor to run before redelivert attempt      * @param redeliveryPolicy          policy for redelivery      * @param logger                    logger to use for logging failures and redelivery attempts      * @param exceptionPolicyStrategy   strategy for onException handling      */
DECL|method|DeadLetterChannel (Processor output, Processor deadLetter, String deadLetterUri, Processor redeliveryProcessor, RedeliveryPolicy redeliveryPolicy, Logger logger, ExceptionPolicyStrategy exceptionPolicyStrategy)
specifier|public
name|DeadLetterChannel
parameter_list|(
name|Processor
name|output
parameter_list|,
name|Processor
name|deadLetter
parameter_list|,
name|String
name|deadLetterUri
parameter_list|,
name|Processor
name|redeliveryProcessor
parameter_list|,
name|RedeliveryPolicy
name|redeliveryPolicy
parameter_list|,
name|Logger
name|logger
parameter_list|,
name|ExceptionPolicyStrategy
name|exceptionPolicyStrategy
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
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
name|deadLetterUri
expr_stmt|;
name|this
operator|.
name|redeliveryProcessor
operator|=
name|redeliveryProcessor
expr_stmt|;
name|this
operator|.
name|outputAsync
operator|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|output
argument_list|)
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
name|setExceptionPolicy
argument_list|(
name|exceptionPolicyStrategy
argument_list|)
expr_stmt|;
block|}
DECL|method|createDefaultLogger ()
specifier|public
specifier|static
name|Logger
name|createDefaultLogger
parameter_list|()
block|{
return|return
operator|new
name|Logger
argument_list|(
name|LOG
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
literal|"DeadLetterChannel["
operator|+
name|output
operator|+
literal|", "
operator|+
operator|(
name|deadLetterUri
operator|!=
literal|null
condition|?
name|deadLetterUri
else|:
name|deadLetter
operator|)
operator|+
literal|"]"
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
return|return
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
operator|new
name|RedeliveryData
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Processes the exchange using decorated with this dead letter channel.      */
DECL|method|process (final Exchange exchange, final AsyncCallback callback, final RedeliveryData data)
specifier|protected
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
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
block|}
name|callback
operator|.
name|done
argument_list|(
name|data
operator|.
name|sync
argument_list|)
expr_stmt|;
return|return
name|data
operator|.
name|sync
return|;
block|}
comment|// if the exchange is transacted then let the underlying system handle the redelivery etc.
comment|// this DeadLetterChannel is only for non transacted exchanges
if|if
condition|(
name|exchange
operator|.
name|isTransacted
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"This is a transacted exchange, bypassing this DeadLetterChannel: "
operator|+
name|this
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|data
operator|.
name|sync
return|;
block|}
comment|// did previous processing caused an exception?
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
return|return
name|deliverToFaultProcessor
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
return|;
block|}
comment|// if we are redelivering then sleep before trying again
if|if
condition|(
name|data
operator|.
name|redeliveryCounter
operator|>
literal|0
condition|)
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
name|LOG
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
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange
name|boolean
name|sync
init|=
name|outputAsync
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
comment|// Only handle the async case...
if|if
condition|(
name|sync
condition|)
block|{
return|return;
block|}
name|data
operator|.
name|sync
operator|=
literal|false
expr_stmt|;
comment|// only process if the exchange hasn't failed
comment|// and it has not been handled by the error processor
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|ExchangeHelper
operator|.
name|isFailureHandled
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|//TODO Call the Timer for the asyncProcessor
name|asyncProcess
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|callback
operator|.
name|done
argument_list|(
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
comment|// It is going to be processed async..
return|return
literal|false
return|;
block|}
if|if
condition|(
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
condition|)
block|{
comment|// If everything went well.. then we exit here..
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// error occurred so loop back around.....
block|}
block|}
DECL|method|asyncProcess (final Exchange exchange, final AsyncCallback callback, final RedeliveryData data)
specifier|protected
name|void
name|asyncProcess
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
comment|// set the timer here
if|if
condition|(
operator|!
name|isRunAllowed
argument_list|()
condition|)
block|{
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
block|}
name|callback
operator|.
name|done
argument_list|(
name|data
operator|.
name|sync
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// if the exchange is transacted then let the underlying system handle the redelivery etc.
comment|// this DeadLetterChannel is only for non transacted exchanges
if|if
condition|(
name|exchange
operator|.
name|isTransacted
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"This is a transacted exchange, bypassing this DeadLetterChannel: "
operator|+
name|this
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
comment|// did previous processing caused an exception?
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
name|deliverToFaultProcessor
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// process the next try
comment|// if we are redelivering then sleep before trying again
if|if
condition|(
name|data
operator|.
name|redeliveryCounter
operator|>
literal|0
condition|)
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
comment|// wait until we should redeliver
name|data
operator|.
name|redeliveryDelay
operator|=
name|data
operator|.
name|currentRedeliveryPolicy
operator|.
name|calculateRedeliveryDelay
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
name|timer
operator|.
name|schedule
argument_list|(
operator|new
name|RedeliverTimerTask
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
argument_list|,
name|data
operator|.
name|redeliveryDelay
argument_list|)
expr_stmt|;
comment|// letting onRedeliver be executed
name|deliverToRedeliveryProcessor
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleException (Exchange exchange, RedeliveryData data)
specifier|private
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
DECL|method|deliverToRedeliveryProcessor (final Exchange exchange, final AsyncCallback callback, final RedeliveryData data)
specifier|private
name|void
name|deliverToRedeliveryProcessor
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
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
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"RedeliveryProcessor "
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
name|AsyncProcessor
name|afp
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|data
operator|.
name|onRedeliveryProcessor
argument_list|)
decl_stmt|;
name|afp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Redelivery processor done"
argument_list|)
expr_stmt|;
comment|// do NOT call done on callback as this is the redelivery processor that
comment|// is done. we should not mark the entire exchange as done.
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|deliverToFaultProcessor (final Exchange exchange, final AsyncCallback callback, final RedeliveryData data)
specifier|private
name|boolean
name|deliverToFaultProcessor
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|RedeliveryData
name|data
parameter_list|)
block|{
comment|// we did not success with the redelivery so now we let the failure processor handle it
name|ExchangeHelper
operator|.
name|setFailureHandled
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// must decrement the redelivery counter as we didn't process the redelivery but is
comment|// handling by the failure handler. So we must -1 to not let the counter be out-of-sync
name|decrementRedeliveryCounter
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|AsyncProcessor
name|afp
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|data
operator|.
name|failureProcessor
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|afp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|sync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Fault processor done"
argument_list|)
expr_stmt|;
name|restoreExceptionOnExchange
argument_list|(
name|exchange
argument_list|,
name|data
operator|.
name|handledPredicate
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|data
operator|.
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
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
literal|". Handled by the failure processor: "
operator|+
name|data
operator|.
name|failureProcessor
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
return|return
name|sync
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
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
return|return
name|logger
return|;
block|}
comment|/**      * Sets the logger strategy; which {@link Log} to use and which      * {@link LoggingLevel} to use      */
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
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
DECL|method|restoreExceptionOnExchange (Exchange exchange, Predicate handledPredicate)
specifier|protected
specifier|static
name|void
name|restoreExceptionOnExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Predicate
name|handledPredicate
parameter_list|)
block|{
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
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
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
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
name|EXCEPTION_HANDLED
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
specifier|protected
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

