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
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
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
name|ExpressionBuilder
operator|.
name|routeIdExpression
import|;
end_import

begin_comment
comment|/**  * An {@link org.apache.camel.processor.ErrorHandler} used as a safe fallback when  * processing by other error handlers such as the {@link org.apache.camel.model.OnExceptionDefinition}.  *<p/>  * This error handler is used as a fail-safe to ensure that error handling does not run in endless recursive looping  * which potentially can happen if a new exception is thrown while error handling a previous exception which then  * cause new error handling to process and this then keep on failing with new exceptions in an endless loop.  *  * @version  */
end_comment

begin_class
DECL|class|FatalFallbackErrorHandler
specifier|public
class|class
name|FatalFallbackErrorHandler
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|ErrorHandler
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FatalFallbackErrorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|deadLetterChannel
specifier|private
name|boolean
name|deadLetterChannel
decl_stmt|;
DECL|method|FatalFallbackErrorHandler (Processor processor)
specifier|public
name|FatalFallbackErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
name|processor
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|FatalFallbackErrorHandler (Processor processor, boolean isDeadLetterChannel)
specifier|public
name|FatalFallbackErrorHandler
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|boolean
name|isDeadLetterChannel
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|deadLetterChannel
operator|=
name|isDeadLetterChannel
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
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
parameter_list|)
block|{
comment|// get the current route id we use
specifier|final
name|String
name|id
init|=
name|routeIdExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// prevent endless looping if we end up coming back to ourself
name|Deque
argument_list|<
name|String
argument_list|>
name|fatals
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FATAL_FALLBACK_ERROR_HANDLER
argument_list|,
literal|null
argument_list|,
name|Deque
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|fatals
operator|==
literal|null
condition|)
block|{
name|fatals
operator|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FATAL_FALLBACK_ERROR_HANDLER
argument_list|,
name|fatals
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fatals
operator|.
name|contains
argument_list|(
name|id
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Circular error-handler detected at route: {} - breaking out processing Exchange: {}"
argument_list|,
name|id
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// mark this exchange as already been error handler handled (just by having this property)
comment|// the false value mean the caught exception will be kept on the exchange, causing the
comment|// exception to be propagated back to the caller, and to break out routing
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_CIRCUIT_DETECTED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
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
comment|// okay we run under this fatal error handler now
name|fatals
operator|.
name|push
argument_list|(
name|id
argument_list|)
expr_stmt|;
comment|// support the asynchronous routing engine
name|boolean
name|sync
init|=
name|processor
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
name|doneSync
parameter_list|)
block|{
try|try
block|{
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
comment|// an exception occurred during processing onException
comment|// log detailed error message with as much detail as possible
name|Throwable
name|previous
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// check if previous and this exception are set as the same exception
comment|// which happens when using global scoped onException and you call a direct route that causes the 2nd exception
comment|// then we need to find the original previous exception as the suppressed exception
if|if
condition|(
name|previous
operator|!=
literal|null
operator|&&
name|previous
operator|==
name|exchange
operator|.
name|getException
argument_list|()
condition|)
block|{
name|previous
operator|=
literal|null
expr_stmt|;
comment|// maybe previous was suppressed?
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getSuppressed
argument_list|()
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|previous
operator|=
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getSuppressed
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
block|}
name|String
name|msg
init|=
literal|"Exception occurred while trying to handle previously thrown exception on exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" using: ["
operator|+
name|processor
operator|+
literal|"]."
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
condition|)
block|{
name|msg
operator|+=
literal|" The previous and the new exception will be logged in the following."
expr_stmt|;
name|log
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|log
argument_list|(
literal|"\\--> Previous exception on exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|previous
argument_list|)
expr_stmt|;
name|log
argument_list|(
literal|"\\--> New exception on exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|log
argument_list|(
literal|"\\--> New exception on exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// add previous as suppressed to exception if not already there
if|if
condition|(
name|previous
operator|!=
literal|null
condition|)
block|{
name|Throwable
index|[]
name|suppressed
init|=
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getSuppressed
argument_list|()
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Throwable
name|t
range|:
name|suppressed
control|)
block|{
if|if
condition|(
name|t
operator|==
name|previous
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|addSuppressed
argument_list|(
name|previous
argument_list|)
expr_stmt|;
block|}
block|}
comment|// we can propagated that exception to the caught property on the exchange
comment|// which will shadow any previously caught exception and cause this new exception
comment|// to be visible in the error handler
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|deadLetterChannel
condition|)
block|{
comment|// special for dead letter channel as we want to let it determine what to do, depending how
comment|// it has been configured
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// mark this exchange as already been error handler handled (just by having this property)
comment|// the false value mean the caught exception will be kept on the exchange, causing the
comment|// exception to be propagated back to the caller, and to break out routing
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
comment|// no longer running under this fatal fallback error handler
name|Deque
argument_list|<
name|String
argument_list|>
name|fatals
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FATAL_FALLBACK_ERROR_HANDLER
argument_list|,
literal|null
argument_list|,
name|Deque
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|fatals
operator|!=
literal|null
condition|)
block|{
name|fatals
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
block|}
DECL|method|log (String message)
specifier|private
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|log
argument_list|(
name|message
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|log (String message, Throwable t)
specifier|private
name|void
name|log
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|t
parameter_list|)
block|{
comment|// when using dead letter channel we only want to log at WARN level
if|if
condition|(
name|deadLetterChannel
condition|)
block|{
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|message
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|t
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|error
argument_list|(
name|message
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"FatalFallbackErrorHandler["
operator|+
name|processor
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

