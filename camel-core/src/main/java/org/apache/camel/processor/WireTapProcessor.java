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
name|ProducerCallback
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
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Processor for wire tapping exchanges to an endpoint destination.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|WireTapProcessor
specifier|public
class|class
name|WireTapProcessor
extends|extends
name|SendProcessor
block|{
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
comment|// expression or processor used for populating a new exchange to send
comment|// as opposed to traditional wiretap that sends a copy of the original exchange
DECL|field|newExchangeExpression
specifier|private
name|Expression
name|newExchangeExpression
decl_stmt|;
DECL|field|newExchangeProcessor
specifier|private
name|Processor
name|newExchangeProcessor
decl_stmt|;
DECL|method|WireTapProcessor (Endpoint destination)
specifier|public
name|WireTapProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{
name|super
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
DECL|method|WireTapProcessor (Endpoint destination, ExchangePattern pattern)
specifier|public
name|WireTapProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|super
argument_list|(
name|destination
argument_list|,
name|pattern
argument_list|)
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
name|super
operator|.
name|doStart
argument_list|()
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
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
comment|// must null it so we can restart
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
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
operator|.
name|getEndpointUri
argument_list|()
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
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|")"
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
name|getProducerCache
argument_list|(
name|exchange
argument_list|)
operator|.
name|doInProducer
argument_list|(
name|destination
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|,
operator|new
name|ProducerCallback
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|doInProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|wireTapExchange
init|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|procesWireTap
argument_list|(
name|producer
argument_list|,
name|wireTapExchange
argument_list|)
expr_stmt|;
return|return
name|wireTapExchange
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * Wiretaps the exchange.      *      * @param exchange  the exchange to wire tap      */
DECL|method|procesWireTap (final Producer producer, final Exchange exchange)
specifier|protected
name|void
name|procesWireTap
parameter_list|(
specifier|final
name|Producer
name|producer
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// use submit instead of execute to force it to use a new thread, execute might
comment|// decide to use current thread, so we must submit a new task
comment|// as we dont care for the response we dont hold the future object and wait for the result
name|getExecutorService
argument_list|()
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
literal|"Processing wiretap: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|newExchangeProcessor
operator|==
literal|null
operator|&&
name|newExchangeExpression
operator|==
literal|null
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
name|Exchange
name|answer
init|=
operator|new
name|DefaultExchange
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
decl_stmt|;
comment|// use destination os origin of this new exchange
name|answer
operator|.
name|setFromEndpoint
argument_list|(
name|getDestination
argument_list|()
argument_list|)
expr_stmt|;
comment|// prepare the exchange
if|if
condition|(
name|newExchangeProcessor
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|newExchangeProcessor
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
else|else
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
return|return
name|answer
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
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
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|createExecutorService ()
specifier|protected
name|ExecutorService
name|createExecutorService
parameter_list|()
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
name|this
operator|.
name|toString
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|setExecutorService (ExecutorService executorService)
specifier|public
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
block|}
DECL|method|getNewExchangeProcessor ()
specifier|public
name|Processor
name|getNewExchangeProcessor
parameter_list|()
block|{
return|return
name|newExchangeProcessor
return|;
block|}
DECL|method|setNewExchangeProcessor (Processor newExchangeProcessor)
specifier|public
name|void
name|setNewExchangeProcessor
parameter_list|(
name|Processor
name|newExchangeProcessor
parameter_list|)
block|{
name|this
operator|.
name|newExchangeProcessor
operator|=
name|newExchangeProcessor
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
block|}
end_class

end_unit

