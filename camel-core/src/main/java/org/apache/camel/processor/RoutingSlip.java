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
name|Iterator
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
name|AsyncProducerCallback
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
name|FailedToCreateProducerException
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
name|Producer
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
name|ExpressionBuilder
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
name|DefaultExchange
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
name|ProducerCache
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
name|ServiceSupport
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
name|RoutingSlipDefinition
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
name|ObjectHelper
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
import|;
end_import

begin_comment
comment|/**  * Implements a<a href="http://camel.apache.org/routing-slip.html">Routing Slip</a>  * pattern where the list of actual endpoints to send a message exchange to are  * dependent on the value of a message header.  *<p/>  * This implementation mirrors the logic from the {@link org.apache.camel.processor.Pipeline} in the async variation  * as the failover load balancer is a specialized pipeline. So the trick is to keep doing the same as the  * pipeline to ensure it works the same and the async routing engine is flawless.  */
end_comment

begin_class
DECL|class|RoutingSlip
specifier|public
class|class
name|RoutingSlip
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
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
name|RoutingSlip
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|ignoreInvalidEndpoints
specifier|private
name|boolean
name|ignoreInvalidEndpoints
decl_stmt|;
DECL|field|header
specifier|private
name|String
name|header
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|uriDelimiter
specifier|private
name|String
name|uriDelimiter
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|method|RoutingSlip (CamelContext camelContext)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * Will be removed in Camel 2.5      */
annotation|@
name|Deprecated
DECL|method|RoutingSlip (CamelContext camelContext, String header)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|header
parameter_list|)
block|{
name|this
argument_list|(
name|camelContext
argument_list|,
name|header
argument_list|,
name|RoutingSlipDefinition
operator|.
name|DEFAULT_DELIMITER
argument_list|)
expr_stmt|;
block|}
comment|/**      * Will be removed in Camel 2.5      */
annotation|@
name|Deprecated
DECL|method|RoutingSlip (CamelContext camelContext, String header, String uriDelimiter)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|header
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|header
argument_list|,
literal|"header"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|uriDelimiter
argument_list|,
literal|"uriDelimiter"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
name|expression
operator|=
name|ExpressionBuilder
operator|.
name|headerExpression
argument_list|(
name|header
argument_list|)
expr_stmt|;
name|this
operator|.
name|uriDelimiter
operator|=
name|uriDelimiter
expr_stmt|;
block|}
DECL|method|RoutingSlip (CamelContext camelContext, Expression expression, String uriDelimiter)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|String
name|uriDelimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|notNull
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|uriDelimiter
operator|=
name|uriDelimiter
expr_stmt|;
name|this
operator|.
name|header
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|setDelimiter (String delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|uriDelimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidEndpoints ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoints
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoints
return|;
block|}
DECL|method|setIgnoreInvalidEndpoints (boolean ignoreInvalidEndpoints)
specifier|public
name|void
name|setIgnoreInvalidEndpoints
parameter_list|(
name|boolean
name|ignoreInvalidEndpoints
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoints
operator|=
name|ignoreInvalidEndpoints
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
literal|"RoutingSlip[expression="
operator|+
name|expression
operator|+
literal|" uriDelimiter="
operator|+
name|uriDelimiter
operator|+
literal|"]"
return|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"routingSlip["
operator|+
name|expression
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
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"RoutingSlip has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
name|Object
name|routingSlip
init|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|doRoutingSlip
argument_list|(
name|exchange
argument_list|,
name|routingSlip
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|doRoutingSlip (Exchange exchange, Object routingSlip)
specifier|public
name|boolean
name|doRoutingSlip
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|routingSlip
parameter_list|)
block|{
return|return
name|doRoutingSlip
argument_list|(
name|exchange
argument_list|,
name|routingSlip
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
comment|// noop
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|doRoutingSlip (Exchange exchange, Object routingSlip, AsyncCallback callback)
specifier|public
name|boolean
name|doRoutingSlip
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|routingSlip
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|routingSlip
argument_list|,
name|uriDelimiter
argument_list|)
decl_stmt|;
name|Exchange
name|current
init|=
name|exchange
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Endpoint
name|endpoint
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|iter
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// if no endpoint was resolved then try the next
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// error resolving endpoint so we should break out
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// prepare and process the routing slip
name|Exchange
name|copy
init|=
name|prepareExchangeForRoutingSlip
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|processExchange
argument_list|(
name|endpoint
argument_list|,
name|copy
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|iter
argument_list|)
decl_stmt|;
name|current
operator|=
name|copy
expr_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
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
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed asynchronously"
argument_list|)
expr_stmt|;
block|}
comment|// the remainder of the routing slip will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
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
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed synchronously"
argument_list|)
expr_stmt|;
block|}
comment|// we ignore some kind of exceptions and allow us to continue
if|if
condition|(
name|isIgnoreInvalidEndpoints
argument_list|()
condition|)
block|{
name|FailedToCreateProducerException
name|e
init|=
name|current
operator|.
name|getException
argument_list|(
name|FailedToCreateProducerException
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint uri is invalid: "
operator|+
name|endpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|current
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Decide whether to continue with the recipients or not; similar logic to the Pipeline
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|current
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|isFailed
argument_list|()
operator|||
name|current
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// The Exchange.ERRORHANDLED_HANDLED property is only set if satisfactory handling was done
comment|// by the error handler. It's still an exception, the exchange still failed.
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Message exchange has failed so breaking out of the routing slip: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
argument_list|)
expr_stmt|;
if|if
condition|(
name|current
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Marked as rollback only."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Exception: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|hasOut
argument_list|()
operator|&&
name|current
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Fault: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exceptionHandled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Handled by the error handler."
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|">>> "
operator|+
name|current
argument_list|)
expr_stmt|;
block|}
comment|// copy results back to the original exchange
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|current
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
DECL|method|resolveEndpoint (Iterator<Object> iter, Exchange exchange)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|nextRecipient
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
literal|null
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|nextRecipient
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|isIgnoreInvalidEndpoints
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint uri is invalid: "
operator|+
name|nextRecipient
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
name|e
throw|;
block|}
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|prepareExchangeForRoutingSlip (Exchange current)
specifier|protected
name|Exchange
name|prepareExchangeForRoutingSlip
parameter_list|(
name|Exchange
name|current
parameter_list|)
block|{
name|Exchange
name|copy
init|=
operator|new
name|DefaultExchange
argument_list|(
name|current
argument_list|)
decl_stmt|;
comment|// we must use the same id as this is a snapshot strategy where Camel copies a snapshot
comment|// before processing the next step in the pipeline, so we have a snapshot of the exchange
comment|// just before. This snapshot is used if Camel should do redeliveries (re try) using
comment|// DeadLetterChannel. That is why it's important the id is the same, as it is the *same*
comment|// exchange being routed.
name|copy
operator|.
name|setExchangeId
argument_list|(
name|current
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|updateRoutingSlipHeader
argument_list|(
name|current
argument_list|)
expr_stmt|;
name|copyOutToIn
argument_list|(
name|copy
argument_list|,
name|current
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|processExchange (final Endpoint endpoint, final Exchange exchange, final Exchange original, final AsyncCallback callback, final Iterator<Object> iter)
specifier|protected
name|boolean
name|processExchange
parameter_list|(
specifier|final
name|Endpoint
name|endpoint
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Exchange
name|original
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// this does the actual processing so log at trace level
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|">>> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|boolean
name|sync
init|=
name|producerCache
operator|.
name|doInAsyncProducer
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
literal|null
argument_list|,
name|callback
argument_list|,
operator|new
name|AsyncProducerCallback
argument_list|()
block|{
specifier|public
name|boolean
name|doInAsyncProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|AsyncProcessor
name|asyncProducer
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// set property which endpoint we send to
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|boolean
name|sync
init|=
name|asyncProducer
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
comment|// we only have to handle async completion of the pipeline
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
comment|// continue processing the routing slip asynchronously
name|Exchange
name|current
init|=
name|exchange
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// we ignore some kind of exceptions and allow us to continue
if|if
condition|(
name|isIgnoreInvalidEndpoints
argument_list|()
condition|)
block|{
name|FailedToCreateProducerException
name|e
init|=
name|current
operator|.
name|getException
argument_list|(
name|FailedToCreateProducerException
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Endpoint uri is invalid: "
operator|+
name|endpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|current
operator|.
name|setException
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Decide whether to continue with the recipients or not; similar logic to the Pipeline
name|boolean
name|exceptionHandled
init|=
name|hasExceptionBeenHandledByErrorHandler
argument_list|(
name|current
argument_list|)
decl_stmt|;
if|if
condition|(
name|current
operator|.
name|isFailed
argument_list|()
operator|||
name|current
operator|.
name|isRollbackOnly
argument_list|()
operator|||
name|exceptionHandled
condition|)
block|{
comment|// The Exchange.ERRORHANDLED_HANDLED property is only set if satisfactory handling was done
comment|// by the error handler. It's still an exception, the exchange still failed.
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Message exchange has failed so breaking out of the routing slip: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
argument_list|)
expr_stmt|;
if|if
condition|(
name|current
operator|.
name|isRollbackOnly
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Marked as rollback only."
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Exception: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|current
operator|.
name|hasOut
argument_list|()
operator|&&
name|current
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Fault: "
argument_list|)
operator|.
name|append
argument_list|(
name|current
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exceptionHandled
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" Handled by the error handler."
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
name|Endpoint
name|endpoint
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|iter
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// if no endpoint was resolved then try the next
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// error resolving endpoint so we should break out
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// prepare and process the routing slip
name|Exchange
name|copy
init|=
name|prepareExchangeForRoutingSlip
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|boolean
name|sync
init|=
name|processExchange
argument_list|(
name|endpoint
argument_list|,
name|copy
argument_list|,
name|original
argument_list|,
name|callback
argument_list|,
name|iter
argument_list|)
decl_stmt|;
name|current
operator|=
name|copy
expr_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
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
literal|"Processing exchangeId: "
operator|+
name|original
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" is continued being processed asynchronously"
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: "
operator|+
name|original
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|">>> "
operator|+
name|current
argument_list|)
expr_stmt|;
block|}
comment|// copy results back to the original exchange
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|original
argument_list|,
name|current
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|sync
return|;
block|}
DECL|method|hasExceptionBeenHandledByErrorHandler (Exchange nextExchange)
specifier|private
specifier|static
name|boolean
name|hasExceptionBeenHandledByErrorHandler
parameter_list|(
name|Exchange
name|nextExchange
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|nextExchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|)
argument_list|)
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|producerCache
operator|==
literal|null
condition|)
block|{
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
comment|// add it as a service so we can manage it
name|camelContext
operator|.
name|addService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
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
name|stopService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
DECL|method|updateRoutingSlipHeader (Exchange current)
specifier|private
name|void
name|updateRoutingSlipHeader
parameter_list|(
name|Exchange
name|current
parameter_list|)
block|{
comment|// only update the header value which used as the routing slip
if|if
condition|(
name|header
operator|!=
literal|null
condition|)
block|{
name|Message
name|message
init|=
name|getResultMessage
argument_list|(
name|current
argument_list|)
decl_stmt|;
name|String
name|oldSlip
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|header
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldSlip
operator|!=
literal|null
condition|)
block|{
name|int
name|delimiterIndex
init|=
name|oldSlip
operator|.
name|indexOf
argument_list|(
name|uriDelimiter
argument_list|)
decl_stmt|;
name|String
name|newSlip
init|=
name|delimiterIndex
operator|>
literal|0
condition|?
name|oldSlip
operator|.
name|substring
argument_list|(
name|delimiterIndex
operator|+
literal|1
argument_list|)
else|:
literal|""
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|newSlip
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the outbound message if available. Otherwise return the inbound message.      */
DECL|method|getResultMessage (Exchange exchange)
specifier|private
name|Message
name|getResultMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|exchange
operator|.
name|getOut
argument_list|()
return|;
block|}
else|else
block|{
comment|// if this endpoint had no out (like a mock endpoint) just take the in
return|return
name|exchange
operator|.
name|getIn
argument_list|()
return|;
block|}
block|}
comment|/**      * Copy the outbound data in 'source' to the inbound data in 'result'.      */
DECL|method|copyOutToIn (Exchange result, Exchange source)
specifier|private
name|void
name|copyOutToIn
parameter_list|(
name|Exchange
name|result
parameter_list|,
name|Exchange
name|source
parameter_list|)
block|{
name|result
operator|.
name|setException
argument_list|(
name|source
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|source
operator|.
name|hasOut
argument_list|()
operator|&&
name|source
operator|.
name|getOut
argument_list|()
operator|.
name|isFault
argument_list|()
condition|)
block|{
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|source
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|setIn
argument_list|(
name|getResultMessage
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|result
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|source
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

