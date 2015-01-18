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
name|support
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

begin_comment
comment|/**  * Processor for wire tapping exchanges to an endpoint destination.  *  * @version   */
end_comment

begin_class
DECL|class|WireTapProcessor
specifier|public
class|class
name|WireTapProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
implements|,
name|EndpointAware
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
name|WireTapProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|destination
specifier|private
specifier|final
name|Endpoint
name|destination
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
DECL|method|WireTapProcessor (Endpoint destination, Processor processor, ExchangePattern exchangePattern, ExecutorService executorService, boolean shutdownExecutorService)
specifier|public
name|WireTapProcessor
parameter_list|(
name|Endpoint
name|destination
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
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
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
name|destination
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
name|destination
operator|+
literal|")"
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|destination
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
specifier|final
name|Exchange
name|wireTapExchange
init|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|exchangePattern
argument_list|)
decl_stmt|;
comment|// send the exchange to the destination using an executor service
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> (wiretap) {} {}"
argument_list|,
name|destination
argument_list|,
name|wireTapExchange
argument_list|)
expr_stmt|;
name|processor
operator|.
name|process
argument_list|(
name|wireTapExchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred during processing "
operator|+
name|wireTapExchange
operator|+
literal|" wiretap to "
operator|+
name|destination
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|wireTapExchange
return|;
block|}
empty_stmt|;
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
comment|// set property which endpoint we send to
name|answer
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|destination
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
name|ObjectHelper
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
argument_list|<
name|Processor
argument_list|>
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
name|destination
operator|.
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

