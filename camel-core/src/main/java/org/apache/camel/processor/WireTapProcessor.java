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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Callable
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
name|ExecutorService
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
name|atomic
operator|.
name|LongAdder
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
name|CamelContextAware
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
name|RuntimeCamelException
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
name|ShutdownRunningTask
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
name|StreamCache
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
name|ShutdownAware
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
name|AsyncProcessorConverterHelper
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
name|ServiceHelper
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

begin_comment
comment|/**  * Processor for wire tapping exchanges to an endpoint destination.  */
end_comment

begin_class
DECL|class|WireTapProcessor
specifier|public
class|class
name|WireTapProcessor
extends|extends
name|AsyncProcessorSupport
implements|implements
name|Traceable
implements|,
name|ShutdownAware
implements|,
name|IdAware
implements|,
name|CamelContextAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|dynamicProcessor
specifier|private
specifier|final
name|SendDynamicProcessor
name|dynamicProcessor
decl_stmt|;
DECL|field|uri
specifier|private
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|dynamicUri
specifier|private
specifier|final
name|boolean
name|dynamicUri
decl_stmt|;
DECL|field|processor
specifier|private
specifier|final
name|Processor
name|processor
decl_stmt|;
DECL|field|exchangePattern
specifier|private
specifier|final
name|ExchangePattern
name|exchangePattern
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|final
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
specifier|volatile
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|field|taskCount
specifier|private
specifier|final
name|LongAdder
name|taskCount
init|=
operator|new
name|LongAdder
argument_list|()
decl_stmt|;
comment|// expression or processor used for populating a new exchange to send
comment|// as opposed to traditional wiretap that sends a copy of the original exchange
DECL|field|newExchangeExpression
specifier|private
name|Expression
name|newExchangeExpression
decl_stmt|;
DECL|field|newExchangeProcessors
specifier|private
name|List
argument_list|<
name|Processor
argument_list|>
name|newExchangeProcessors
decl_stmt|;
DECL|field|copy
specifier|private
name|boolean
name|copy
decl_stmt|;
DECL|field|onPrepare
specifier|private
name|Processor
name|onPrepare
decl_stmt|;
DECL|method|WireTapProcessor (SendDynamicProcessor dynamicProcessor, Processor processor, ExchangePattern exchangePattern, ExecutorService executorService, boolean shutdownExecutorService, boolean dynamicUri)
specifier|public
name|WireTapProcessor
parameter_list|(
name|SendDynamicProcessor
name|dynamicProcessor
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|boolean
name|dynamicUri
parameter_list|)
block|{
name|this
operator|.
name|dynamicProcessor
operator|=
name|dynamicProcessor
expr_stmt|;
name|this
operator|.
name|uri
operator|=
name|dynamicProcessor
operator|.
name|getUri
argument_list|()
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|)
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|shutdownExecutorService
operator|=
name|shutdownExecutorService
expr_stmt|;
name|this
operator|.
name|dynamicUri
operator|=
name|dynamicUri
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
literal|"WireTap["
operator|+
name|uri
operator|+
literal|"]"
return|;
block|}
annotation|@
name|Override
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
literal|"wireTap("
operator|+
name|uri
operator|+
literal|")"
return|;
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
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
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
annotation|@
name|Override
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// not in use
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
return|return
name|taskCount
operator|.
name|intValue
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean suspendOnly, boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|suspendOnly
parameter_list|,
name|boolean
name|forced
parameter_list|)
block|{
comment|// noop
block|}
DECL|method|getEndpointUtilizationStatistics ()
specifier|public
name|EndpointUtilizationStatistics
name|getEndpointUtilizationStatistics
parameter_list|()
block|{
return|return
name|dynamicProcessor
operator|.
name|getEndpointUtilizationStatistics
argument_list|()
return|;
block|}
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
literal|"WireTapProcessor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// must configure the wire tap beforehand
name|Exchange
name|target
decl_stmt|;
try|try
block|{
name|target
operator|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|exchangePattern
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
specifier|final
name|Exchange
name|wireTapExchange
init|=
name|target
decl_stmt|;
comment|// send the exchange to the destination using an executor service
name|executorService
operator|.
name|submit
argument_list|(
parameter_list|()
lambda|->
block|{
name|taskCount
operator|.
name|increment
argument_list|()
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|">>>> (wiretap) {} {}"
argument_list|,
name|uri
argument_list|,
name|wireTapExchange
argument_list|)
expr_stmt|;
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
operator|.
name|process
argument_list|(
name|wireTapExchange
argument_list|,
name|doneSync
lambda|->
block|{
if|if
condition|(
name|wireTapExchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error occurred during processing "
operator|+
name|wireTapExchange
operator|+
literal|" wiretap to "
operator|+
name|uri
operator|+
literal|". This exception will be ignored."
argument_list|,
name|wireTapExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|taskCount
operator|.
name|decrement
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
comment|// continue routing this synchronously
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
DECL|method|configureExchange (Exchange exchange, ExchangePattern pattern)
specifier|protected
name|Exchange
name|configureExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|answer
decl_stmt|;
if|if
condition|(
name|copy
condition|)
block|{
comment|// use a copy of the original exchange
name|answer
operator|=
name|configureCopyExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use a new exchange
name|answer
operator|=
name|configureNewExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// prepare the exchange
if|if
condition|(
name|newExchangeExpression
operator|!=
literal|null
condition|)
block|{
name|Object
name|body
init|=
name|newExchangeExpression
operator|.
name|evaluate
argument_list|(
name|answer
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|newExchangeProcessors
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Processor
name|processor
range|:
name|newExchangeProcessors
control|)
block|{
try|try
block|{
name|processor
operator|.
name|process
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|// if the body is a stream cache we must use a copy of the stream in the wire tapped exchange
name|Message
name|msg
init|=
name|answer
operator|.
name|hasOut
argument_list|()
condition|?
name|answer
operator|.
name|getOut
argument_list|()
else|:
name|answer
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|.
name|getBody
argument_list|()
operator|instanceof
name|StreamCache
condition|)
block|{
comment|// in parallel processing case, the stream must be copied, therefore get the stream
name|StreamCache
name|cache
init|=
operator|(
name|StreamCache
operator|)
name|msg
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|StreamCache
name|copied
init|=
name|cache
operator|.
name|copy
argument_list|(
name|answer
argument_list|)
decl_stmt|;
if|if
condition|(
name|copied
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|setBody
argument_list|(
name|copied
argument_list|)
expr_stmt|;
block|}
block|}
comment|// invoke on prepare on the exchange if specified
if|if
condition|(
name|onPrepare
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|onPrepare
operator|.
name|process
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|configureCopyExchange (Exchange exchange)
specifier|private
name|Exchange
name|configureCopyExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// must use a copy as we dont want it to cause side effects of the original exchange
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|false
argument_list|)
decl_stmt|;
comment|// set MEP to InOnly as this wire tap is a fire and forget
name|copy
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
comment|// remove STREAM_CACHE_UNIT_OF_WORK property because this wire tap will
comment|// close its own created stream cache(s)
name|copy
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|STREAM_CACHE_UNIT_OF_WORK
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
DECL|method|configureNewExchange (Exchange exchange)
specifier|private
name|Exchange
name|configureNewExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
return|;
block|}
DECL|method|getNewExchangeProcessors ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|getNewExchangeProcessors
parameter_list|()
block|{
return|return
name|newExchangeProcessors
return|;
block|}
DECL|method|setNewExchangeProcessors (List<Processor> newExchangeProcessors)
specifier|public
name|void
name|setNewExchangeProcessors
parameter_list|(
name|List
argument_list|<
name|Processor
argument_list|>
name|newExchangeProcessors
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessors
operator|=
name|newExchangeProcessors
expr_stmt|;
block|}
DECL|method|getNewExchangeExpression ()
specifier|public
name|Expression
name|getNewExchangeExpression
parameter_list|()
block|{
return|return
name|newExchangeExpression
return|;
block|}
DECL|method|setNewExchangeExpression (Expression newExchangeExpression)
specifier|public
name|void
name|setNewExchangeExpression
parameter_list|(
name|Expression
name|newExchangeExpression
parameter_list|)
block|{
name|this
operator|.
name|newExchangeExpression
operator|=
name|newExchangeExpression
expr_stmt|;
block|}
DECL|method|addNewExchangeProcessor (Processor processor)
specifier|public
name|void
name|addNewExchangeProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
if|if
condition|(
name|newExchangeProcessors
operator|==
literal|null
condition|)
block|{
name|newExchangeProcessors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|newExchangeProcessors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|isCopy ()
specifier|public
name|boolean
name|isCopy
parameter_list|()
block|{
return|return
name|copy
return|;
block|}
DECL|method|setCopy (boolean copy)
specifier|public
name|void
name|setCopy
parameter_list|(
name|boolean
name|copy
parameter_list|)
block|{
name|this
operator|.
name|copy
operator|=
name|copy
expr_stmt|;
block|}
DECL|method|getOnPrepare ()
specifier|public
name|Processor
name|getOnPrepare
parameter_list|()
block|{
return|return
name|onPrepare
return|;
block|}
DECL|method|setOnPrepare (Processor onPrepare)
specifier|public
name|void
name|setOnPrepare
parameter_list|(
name|Processor
name|onPrepare
parameter_list|)
block|{
name|this
operator|.
name|onPrepare
operator|=
name|onPrepare
expr_stmt|;
block|}
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|int
name|getCacheSize
parameter_list|()
block|{
return|return
name|dynamicProcessor
operator|.
name|getCacheSize
argument_list|()
return|;
block|}
DECL|method|isIgnoreInvalidEndpoint ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|dynamicProcessor
operator|.
name|isIgnoreInvalidEndpoint
argument_list|()
return|;
block|}
DECL|method|isDynamicUri ()
specifier|public
name|boolean
name|isDynamicUri
parameter_list|()
block|{
return|return
name|dynamicUri
return|;
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
name|startService
argument_list|(
name|processor
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
name|stopService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|stopAndShutdownService
argument_list|(
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownExecutorService
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

