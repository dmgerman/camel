begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Traceable
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
name|DefaultProducerCache
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
name|EndpointUtilizationStatistics
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
name|IdAware
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
name|support
operator|.
name|AsyncProcessorSupport
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
name|support
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
name|support
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
name|support
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
name|support
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
name|support
operator|.
name|service
operator|.
name|ServiceHelper
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
name|processor
operator|.
name|PipelineHelper
operator|.
name|continueProcessing
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
name|AsyncProcessorSupport
implements|implements
name|Traceable
implements|,
name|IdAware
block|{
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|field|producerCache
specifier|protected
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|cacheSize
specifier|protected
name|int
name|cacheSize
decl_stmt|;
DECL|field|ignoreInvalidEndpoints
specifier|protected
name|boolean
name|ignoreInvalidEndpoints
decl_stmt|;
DECL|field|header
specifier|protected
name|String
name|header
decl_stmt|;
DECL|field|expression
specifier|protected
name|Expression
name|expression
decl_stmt|;
DECL|field|uriDelimiter
specifier|protected
name|String
name|uriDelimiter
decl_stmt|;
DECL|field|camelContext
specifier|protected
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|errorHandler
specifier|protected
name|AsyncProcessor
name|errorHandler
decl_stmt|;
comment|/**      * The iterator to be used for retrieving the next routing slip(s) to be used.      */
DECL|interface|RoutingSlipIterator
specifier|protected
interface|interface
name|RoutingSlipIterator
block|{
comment|/**          * Are the more routing slip(s)?          *          * @param exchange the current exchange          * @return<tt>true</tt> if more slips,<tt>false</tt> otherwise.          */
DECL|method|hasNext (Exchange exchange)
name|boolean
name|hasNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**          * Returns the next routing slip(s).          *          * @param exchange the current exchange          * @return the slip(s).          */
DECL|method|next (Exchange exchange)
name|Object
name|next
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
DECL|method|RoutingSlip (CamelContext camelContext)
specifier|public
name|RoutingSlip
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
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
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
DECL|method|getUriDelimiter ()
specifier|public
name|String
name|getUriDelimiter
parameter_list|()
block|{
return|return
name|uriDelimiter
return|;
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
DECL|method|getCacheSize ()
specifier|public
name|int
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (int cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
DECL|method|getErrorHandler ()
specifier|public
name|AsyncProcessor
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|setErrorHandler (AsyncProcessor errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|AsyncProcessor
name|errorHandler
parameter_list|)
block|{
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
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
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"RoutingSlip has not been started: "
operator|+
name|this
argument_list|)
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
name|Expression
name|exp
init|=
name|expression
decl_stmt|;
name|Object
name|slip
init|=
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|EVALUATE_EXPRESSION_RESULT
argument_list|)
decl_stmt|;
if|if
condition|(
name|slip
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|slip
operator|instanceof
name|Expression
condition|)
block|{
name|exp
operator|=
operator|(
name|Expression
operator|)
name|slip
expr_stmt|;
block|}
else|else
block|{
name|exp
operator|=
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|slip
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|doRoutingSlipWithExpression
argument_list|(
name|exchange
argument_list|,
name|exp
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/**      * Creates the route slip iterator to be used.      *      * @param exchange the exchange      * @param expression the expression      * @return the iterator, should never be<tt>null</tt>      */
DECL|method|createRoutingSlipIterator (final Exchange exchange, final Expression expression)
specifier|protected
name|RoutingSlipIterator
name|createRoutingSlipIterator
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|slip
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
comment|// force any exceptions occurred during evaluation to be thrown
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
specifier|final
name|Iterator
argument_list|<
name|?
argument_list|>
name|delegate
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|slip
argument_list|,
name|uriDelimiter
argument_list|)
decl_stmt|;
return|return
operator|new
name|RoutingSlipIterator
argument_list|()
block|{
specifier|public
name|boolean
name|hasNext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|hasNext
argument_list|()
return|;
block|}
specifier|public
name|Object
name|next
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|doRoutingSlipWithExpression (final Exchange exchange, final Expression expression, final AsyncCallback originalCallback)
specifier|private
name|boolean
name|doRoutingSlipWithExpression
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Expression
name|expression
parameter_list|,
specifier|final
name|AsyncCallback
name|originalCallback
parameter_list|)
block|{
name|Exchange
name|current
init|=
name|exchange
decl_stmt|;
name|RoutingSlipIterator
name|iter
decl_stmt|;
try|try
block|{
name|iter
operator|=
name|createRoutingSlipIterator
argument_list|(
name|exchange
argument_list|,
name|expression
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
name|originalCallback
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
comment|// ensure the slip is empty when we start
if|if
condition|(
name|current
operator|.
name|hasProperties
argument_list|()
condition|)
block|{
name|current
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SLIP_ENDPOINT
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|(
name|current
argument_list|)
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
name|current
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
comment|//process and prepare the routing slip
name|boolean
name|sync
init|=
name|processExchange
argument_list|(
name|endpoint
argument_list|,
name|current
argument_list|,
name|exchange
argument_list|,
name|originalCallback
argument_list|,
name|iter
argument_list|)
decl_stmt|;
name|current
operator|=
name|prepareExchangeForRoutingSlip
argument_list|(
name|current
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed asynchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
comment|// the remainder of the routing slip will be completed async
comment|// so we break out now, then the callback will be invoked which then continue routing from where we left here
return|return
literal|false
return|;
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed synchronously"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Endpoint uri is invalid: "
operator|+
name|endpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|current
argument_list|,
literal|"so breaking out of the routing slip"
argument_list|,
name|log
argument_list|)
condition|)
block|{
break|break;
block|}
block|}
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|log
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|current
argument_list|)
expr_stmt|;
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
comment|// okay we are completely done with the routing slip
comment|// so we need to signal done on the original callback so it can continue
name|originalCallback
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
DECL|method|resolveEndpoint (RoutingSlipIterator iter, Exchange exchange)
specifier|protected
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|RoutingSlipIterator
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
argument_list|(
name|exchange
argument_list|)
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
name|log
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
DECL|method|prepareExchangeForRoutingSlip (Exchange current, Endpoint endpoint)
specifier|protected
name|Exchange
name|prepareExchangeForRoutingSlip
parameter_list|(
name|Exchange
name|current
parameter_list|,
name|Endpoint
name|endpoint
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
name|copyOutToIn
argument_list|(
name|copy
argument_list|,
name|current
argument_list|)
expr_stmt|;
comment|// ensure stream caching is reset
name|MessageHelper
operator|.
name|resetStreamCache
argument_list|(
name|copy
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|createErrorHandler (RouteContext routeContext, Exchange exchange, AsyncProcessor processor, Endpoint endpoint)
specifier|protected
name|AsyncProcessor
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|AsyncProcessor
name|answer
init|=
name|processor
decl_stmt|;
name|boolean
name|tryBlock
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TRY_ROUTE_BLOCK
argument_list|,
literal|false
argument_list|,
name|boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// do not wrap in error handler if we are inside a try block
if|if
condition|(
operator|!
name|tryBlock
operator|&&
name|routeContext
operator|!=
literal|null
operator|&&
name|errorHandler
operator|!=
literal|null
condition|)
block|{
comment|// wrap the producer in error handler so we have fine grained error handling on
comment|// the output side instead of the input side
comment|// this is needed to support redelivery on that output alone and not doing redelivery
comment|// for the entire routingslip/dynamic-router block again which will start from scratch again
name|answer
operator|=
name|errorHandler
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|processExchange (final Endpoint endpoint, final Exchange exchange, final Exchange original, final AsyncCallback originalCallback, final RoutingSlipIterator iter)
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
name|originalCallback
parameter_list|,
specifier|final
name|RoutingSlipIterator
name|iter
parameter_list|)
block|{
comment|// this does the actual processing so log at trace level
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {}>>> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// routing slip callback which are used when
comment|// - routing slip was routed asynchronously
comment|// - and we are completely done with the routing slip
comment|// so we need to signal done on the original callback so it can continue
name|AsyncCallback
name|callback
init|=
name|doneSync
lambda|->
block|{
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
name|originalCallback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|producerCache
operator|.
name|doInAsyncProducer
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
parameter_list|(
name|p
parameter_list|,
name|ex
parameter_list|,
name|cb
parameter_list|)
lambda|->
block|{
comment|// rework error handling to support fine grained error handling
name|RouteContext
name|routeContext
init|=
name|ex
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|?
name|ex
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
else|:
literal|null
decl_stmt|;
name|AsyncProcessor
name|target
init|=
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|ex
argument_list|,
name|p
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
comment|// set property which endpoint we send to and the producer that can do it
name|ex
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SLIP_ENDPOINT
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|SLIP_PRODUCER
argument_list|,
name|p
argument_list|)
expr_stmt|;
return|return
name|target
operator|.
name|process
argument_list|(
name|ex
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
comment|// cleanup producer after usage
name|ex
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|SLIP_PRODUCER
argument_list|)
expr_stmt|;
comment|// we only have to handle async completion of the routing slip
if|if
condition|(
name|doneSync
condition|)
block|{
name|cb
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return;
block|}
try|try
block|{
comment|// continue processing the routing slip asynchronously
name|Exchange
name|current
init|=
name|prepareExchangeForRoutingSlip
argument_list|(
name|ex
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|(
name|current
argument_list|)
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
literal|"Endpoint uri is invalid: "
operator|+
name|endpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
comment|// check for error if so we should break out
if|if
condition|(
operator|!
name|continueProcessing
argument_list|(
name|current
argument_list|,
literal|"so breaking out of the routing slip"
argument_list|,
name|log
argument_list|)
condition|)
block|{
break|break;
block|}
name|Endpoint
name|endpoint1
decl_stmt|;
try|try
block|{
name|endpoint1
operator|=
name|resolveEndpoint
argument_list|(
name|iter
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|// if no endpoint was resolved then try the next
if|if
condition|(
name|endpoint1
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
name|current
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
break|break;
block|}
comment|// prepare and process the routing slip
name|boolean
name|sync
init|=
name|processExchange
argument_list|(
name|endpoint1
argument_list|,
name|current
argument_list|,
name|original
argument_list|,
name|cb
argument_list|,
name|iter
argument_list|)
decl_stmt|;
name|current
operator|=
name|prepareExchangeForRoutingSlip
argument_list|(
name|current
argument_list|,
name|endpoint1
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Processing exchangeId: {} is continued being processed asynchronously"
argument_list|,
name|original
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// logging nextExchange as it contains the exchange that might have altered the payload and since
comment|// we are logging the completion if will be confusing if we log the original instead
comment|// we could also consider logging the original and the nextExchange then we have *before* and *after* snapshots
name|log
operator|.
name|trace
argument_list|(
literal|"Processing complete for exchangeId: {}>>> {}"
argument_list|,
name|original
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|current
argument_list|)
expr_stmt|;
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
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|ex
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// okay we are completely done with the routing slip
comment|// so we need to signal done on the original callback so it can continue
name|cb
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
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
name|DefaultProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|,
name|cacheSize
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"RoutingSlip {} using ProducerCache with cacheSize={}"
argument_list|,
name|this
argument_list|,
name|producerCache
operator|.
name|getCapacity
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|,
name|errorHandler
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
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|producerCache
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|getEndpointUtilizationStatistics ()
specifier|public
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
block|{
return|return
name|producerCache
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
return|;
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
comment|/**      * Creates the embedded processor to use when wrapping this routing slip in an error handler.      */
DECL|method|newRoutingSlipProcessorForErrorHandler ()
specifier|public
name|AsyncProcessor
name|newRoutingSlipProcessorForErrorHandler
parameter_list|()
block|{
return|return
operator|new
name|RoutingSlipProcessor
argument_list|()
return|;
block|}
comment|/**      * Embedded processor that routes to the routing slip that has been set via the      * exchange property {@link Exchange#SLIP_PRODUCER}.      */
DECL|class|RoutingSlipProcessor
specifier|private
specifier|final
class|class
name|RoutingSlipProcessor
extends|extends
name|AsyncProcessorSupport
block|{
annotation|@
name|Override
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
name|AsyncProcessor
name|producer
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|SLIP_PRODUCER
argument_list|,
name|AsyncProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
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
literal|"RoutingSlipProcessor"
return|;
block|}
block|}
block|}
end_class

end_unit

