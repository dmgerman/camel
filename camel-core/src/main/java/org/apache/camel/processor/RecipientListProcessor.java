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
name|Iterator
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
name|impl
operator|.
name|DefaultEndpoint
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
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
comment|/**  * Implements a dynamic<a  * href="http://camel.apache.org/recipient-list.html">Recipient List</a>  * pattern where the list of actual endpoints to send a message exchange to are  * dependent on some dynamic expression.  *<p/>  * This implementation is a specialized {@link org.apache.camel.processor.MulticastProcessor} which is based  * on recipient lists. This implementation have to handle the fact the processors is not known at design time  * but evaluated at runtime from the dynamic recipient list. Therefore this implementation have to at runtime  * lookup endpoints and create producers which should act as the processors for the multicast processors which  * runs under the hood. Also this implementation supports the asynchronous routing engine which makes the code  * more trickier.  *  * @version   */
end_comment

begin_class
DECL|class|RecipientListProcessor
specifier|public
class|class
name|RecipientListProcessor
extends|extends
name|MulticastProcessor
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
name|RecipientListProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|iter
specifier|private
specifier|final
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
decl_stmt|;
DECL|field|ignoreInvalidEndpoints
specifier|private
name|boolean
name|ignoreInvalidEndpoints
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
name|producerCache
decl_stmt|;
comment|/**      * Class that represent each step in the recipient list to do      *<p/>      * This implementation ensures the provided producer is being released back in the producer cache when      * its done using it.      */
DECL|class|RecipientProcessorExchangePair
specifier|static
specifier|final
class|class
name|RecipientProcessorExchangePair
implements|implements
name|ProcessorExchangePair
block|{
DECL|field|index
specifier|private
specifier|final
name|int
name|index
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|producer
specifier|private
specifier|final
name|Producer
name|producer
decl_stmt|;
DECL|field|prepared
specifier|private
name|Processor
name|prepared
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|producerCache
specifier|private
specifier|final
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|originalExchangePattern
specifier|private
specifier|final
name|ExchangePattern
name|originalExchangePattern
decl_stmt|;
DECL|method|RecipientProcessorExchangePair (int index, ProducerCache producerCache, Endpoint endpoint, Producer producer, Processor prepared, Exchange exchange)
specifier|private
name|RecipientProcessorExchangePair
parameter_list|(
name|int
name|index
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Processor
name|prepared
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
name|this
operator|.
name|prepared
operator|=
name|prepared
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|originalExchangePattern
operator|=
name|exchange
operator|.
name|getPattern
argument_list|()
expr_stmt|;
block|}
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|index
return|;
block|}
DECL|method|getExchange ()
specifier|public
name|Exchange
name|getExchange
parameter_list|()
block|{
return|return
name|exchange
return|;
block|}
DECL|method|getProducer ()
specifier|public
name|Producer
name|getProducer
parameter_list|()
block|{
return|return
name|producer
return|;
block|}
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|prepared
return|;
block|}
DECL|method|begin ()
specifier|public
name|void
name|begin
parameter_list|()
block|{
comment|// we have already acquired and prepare the producer
name|LOG
operator|.
name|trace
argument_list|(
literal|"RecipientProcessorExchangePair #{} begin: {}"
argument_list|,
name|index
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|RECIPIENT_LIST_ENDPOINT
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// ensure stream caching is reset
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
comment|// if the MEP on the endpoint is different then
if|if
condition|(
name|endpoint
operator|instanceof
name|DefaultEndpoint
condition|)
block|{
name|ExchangePattern
name|pattern
init|=
operator|(
operator|(
name|DefaultEndpoint
operator|)
name|endpoint
operator|)
operator|.
name|getExchangePattern
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
operator|&&
operator|!
name|pattern
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using exchangePattern: {} on exchange: {}"
argument_list|,
name|pattern
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|done ()
specifier|public
name|void
name|done
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"RecipientProcessorExchangePair #{} done: {}"
argument_list|,
name|index
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
comment|// preserve original MEP
name|exchange
operator|.
name|setPattern
argument_list|(
name|originalExchangePattern
argument_list|)
expr_stmt|;
comment|// when we are done we should release back in pool
name|producerCache
operator|.
name|releaseProducer
argument_list|(
name|endpoint
argument_list|,
name|producer
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
literal|"Error releasing producer: "
operator|+
name|producer
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|RecipientListProcessor (CamelContext camelContext, ProducerCache producerCache, Iterator<Object> iter)
specifier|public
name|RecipientListProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|,
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
name|this
operator|.
name|iter
operator|=
name|iter
expr_stmt|;
block|}
DECL|method|RecipientListProcessor (CamelContext camelContext, ProducerCache producerCache, Iterator<Object> iter, AggregationStrategy aggregationStrategy)
specifier|public
name|RecipientListProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|,
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
name|aggregationStrategy
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
name|this
operator|.
name|iter
operator|=
name|iter
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|RecipientListProcessor (CamelContext camelContext, ProducerCache producerCache, Iterator<Object> iter, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService, boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean shareUnitOfWork)
specifier|public
name|RecipientListProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|,
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|boolean
name|parallelProcessing
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Processor
name|onPrepare
parameter_list|,
name|boolean
name|shareUnitOfWork
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|,
name|streaming
argument_list|,
name|stopOnException
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|shareUnitOfWork
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
name|this
operator|.
name|iter
operator|=
name|iter
expr_stmt|;
block|}
DECL|method|RecipientListProcessor (CamelContext camelContext, ProducerCache producerCache, Iterator<Object> iter, AggregationStrategy aggregationStrategy, boolean parallelProcessing, ExecutorService executorService, boolean shutdownExecutorService, boolean streaming, boolean stopOnException, long timeout, Processor onPrepare, boolean shareUnitOfWork, boolean parallelAggregate)
specifier|public
name|RecipientListProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ProducerCache
name|producerCache
parameter_list|,
name|Iterator
argument_list|<
name|Object
argument_list|>
name|iter
parameter_list|,
name|AggregationStrategy
name|aggregationStrategy
parameter_list|,
name|boolean
name|parallelProcessing
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|boolean
name|streaming
parameter_list|,
name|boolean
name|stopOnException
parameter_list|,
name|long
name|timeout
parameter_list|,
name|Processor
name|onPrepare
parameter_list|,
name|boolean
name|shareUnitOfWork
parameter_list|,
name|boolean
name|parallelAggregate
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|,
literal|null
argument_list|,
name|aggregationStrategy
argument_list|,
name|parallelProcessing
argument_list|,
name|executorService
argument_list|,
name|shutdownExecutorService
argument_list|,
name|streaming
argument_list|,
name|stopOnException
argument_list|,
name|timeout
argument_list|,
name|onPrepare
argument_list|,
name|shareUnitOfWork
argument_list|,
name|parallelAggregate
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerCache
operator|=
name|producerCache
expr_stmt|;
name|this
operator|.
name|iter
operator|=
name|iter
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
DECL|method|createProcessorExchangePairs (Exchange exchange)
specifier|protected
name|Iterable
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|createProcessorExchangePairs
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// here we iterate the recipient lists and create the exchange pair for each of those
name|List
argument_list|<
name|ProcessorExchangePair
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorExchangePair
argument_list|>
argument_list|()
decl_stmt|;
comment|// at first we must lookup the endpoint and acquire the producer which can send to the endpoint
name|int
name|index
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|recipient
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
decl_stmt|;
name|Producer
name|producer
decl_stmt|;
try|try
block|{
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
name|producer
operator|=
name|producerCache
operator|.
name|acquireProducer
argument_list|(
name|endpoint
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
literal|"Endpoint uri is invalid: "
operator|+
name|recipient
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
else|else
block|{
comment|// failure so break out
throw|throw
name|e
throw|;
block|}
block|}
comment|// then create the exchange pair
name|result
operator|.
name|add
argument_list|(
name|createProcessorExchangePair
argument_list|(
name|index
operator|++
argument_list|,
name|endpoint
argument_list|,
name|producer
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|/**      * This logic is similar to MulticastProcessor but we have to return a RecipientProcessorExchangePair instead      */
DECL|method|createProcessorExchangePair (int index, Endpoint endpoint, Producer producer, Exchange exchange)
specifier|protected
name|ProcessorExchangePair
name|createProcessorExchangePair
parameter_list|(
name|int
name|index
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Processor
name|prepared
init|=
name|producer
decl_stmt|;
comment|// copy exchange, and do not share the unit of work
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
comment|// if we share unit of work, we need to prepare the child exchange
if|if
condition|(
name|isShareUnitOfWork
argument_list|()
condition|)
block|{
name|prepareSharedUnitOfWork
argument_list|(
name|copy
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// set property which endpoint we send to
name|setToEndpoint
argument_list|(
name|copy
argument_list|,
name|prepared
argument_list|)
expr_stmt|;
comment|// rework error handling to support fine grained error handling
name|RouteContext
name|routeContext
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getRouteContext
argument_list|()
else|:
literal|null
decl_stmt|;
name|prepared
operator|=
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|copy
argument_list|,
name|prepared
argument_list|)
expr_stmt|;
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
name|copy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|copy
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// and create the pair
return|return
operator|new
name|RecipientProcessorExchangePair
argument_list|(
name|index
argument_list|,
name|producerCache
argument_list|,
name|endpoint
argument_list|,
name|producer
argument_list|,
name|prepared
argument_list|,
name|copy
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
specifier|static
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
block|{
comment|// trim strings as end users might have added spaces between separators
if|if
condition|(
name|recipient
operator|instanceof
name|String
condition|)
block|{
name|recipient
operator|=
operator|(
operator|(
name|String
operator|)
name|recipient
operator|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
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
name|getCamelContext
argument_list|()
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
name|super
operator|.
name|doStop
argument_list|()
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
name|stopAndShutdownService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
name|super
operator|.
name|doShutdown
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
literal|"RecipientList"
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
literal|"recipientList"
return|;
block|}
block|}
end_class

end_unit

