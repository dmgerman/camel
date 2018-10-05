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
name|processor
operator|.
name|aggregate
operator|.
name|UseLatestAggregationStrategy
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
name|support
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
name|StringHelper
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
comment|/**  * Implements a dynamic<a  * href="http://camel.apache.org/recipient-list.html">Recipient List</a>  * pattern where the list of actual endpoints to send a message exchange to are  * dependent on some dynamic expression.  */
end_comment

begin_class
DECL|class|RecipientList
specifier|public
class|class
name|RecipientList
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|IdAware
block|{
DECL|field|IGNORE_DELIMITER_MARKER
specifier|private
specifier|static
specifier|final
name|String
name|IGNORE_DELIMITER_MARKER
init|=
literal|"false"
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|producerCache
specifier|private
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|expression
specifier|private
name|Expression
name|expression
decl_stmt|;
DECL|field|delimiter
specifier|private
specifier|final
name|String
name|delimiter
decl_stmt|;
DECL|field|parallelProcessing
specifier|private
name|boolean
name|parallelProcessing
decl_stmt|;
DECL|field|parallelAggregate
specifier|private
name|boolean
name|parallelAggregate
decl_stmt|;
DECL|field|stopOnAggregateException
specifier|private
name|boolean
name|stopOnAggregateException
decl_stmt|;
DECL|field|stopOnException
specifier|private
name|boolean
name|stopOnException
decl_stmt|;
DECL|field|ignoreInvalidEndpoints
specifier|private
name|boolean
name|ignoreInvalidEndpoints
decl_stmt|;
DECL|field|streaming
specifier|private
name|boolean
name|streaming
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|cacheSize
specifier|private
name|int
name|cacheSize
decl_stmt|;
DECL|field|onPrepare
specifier|private
name|Processor
name|onPrepare
decl_stmt|;
DECL|field|shareUnitOfWork
specifier|private
name|boolean
name|shareUnitOfWork
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|field|aggregateExecutorService
specifier|private
name|ExecutorService
name|aggregateExecutorService
decl_stmt|;
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
init|=
operator|new
name|UseLatestAggregationStrategy
argument_list|()
decl_stmt|;
DECL|method|RecipientList (CamelContext camelContext)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
comment|// use comma by default as delimiter
name|this
argument_list|(
name|camelContext
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, String delimiter)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|delimiter
argument_list|,
literal|"delimiter"
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
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, Expression expression)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
comment|// use comma by default as delimiter
name|this
argument_list|(
name|camelContext
argument_list|,
name|expression
argument_list|,
literal|","
argument_list|)
expr_stmt|;
block|}
DECL|method|RecipientList (CamelContext camelContext, Expression expression, String delimiter)
specifier|public
name|RecipientList
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Expression
name|expression
parameter_list|,
name|String
name|delimiter
parameter_list|)
block|{
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
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
argument_list|(
name|expression
argument_list|,
literal|"expression"
argument_list|)
expr_stmt|;
name|StringHelper
operator|.
name|notEmpty
argument_list|(
name|delimiter
argument_list|,
literal|"delimiter"
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
name|delimiter
operator|=
name|delimiter
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
literal|"RecipientList["
operator|+
operator|(
name|expression
operator|!=
literal|null
condition|?
name|expression
else|:
literal|""
operator|)
operator|+
literal|"]"
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
literal|"RecipientList has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// use the evaluate expression result if exists
name|Object
name|recipientList
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
name|recipientList
operator|==
literal|null
operator|&&
name|expression
operator|!=
literal|null
condition|)
block|{
comment|// fallback and evaluate the expression
name|recipientList
operator|=
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
expr_stmt|;
block|}
return|return
name|sendToRecipientList
argument_list|(
name|exchange
argument_list|,
name|recipientList
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|/**      * Sends the given exchange to the recipient list      */
DECL|method|sendToRecipientList (Exchange exchange, Object recipientList, AsyncCallback callback)
specifier|public
name|boolean
name|sendToRecipientList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipientList
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
decl_stmt|;
if|if
condition|(
name|delimiter
operator|!=
literal|null
operator|&&
name|delimiter
operator|.
name|equalsIgnoreCase
argument_list|(
name|IGNORE_DELIMITER_MARKER
argument_list|)
condition|)
block|{
name|iter
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|recipientList
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iter
operator|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|recipientList
argument_list|,
name|delimiter
argument_list|)
expr_stmt|;
block|}
name|RecipientListProcessor
name|rlp
init|=
operator|new
name|RecipientListProcessor
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|producerCache
argument_list|,
name|iter
argument_list|,
name|getAggregationStrategy
argument_list|()
argument_list|,
name|isParallelProcessing
argument_list|()
argument_list|,
name|getExecutorService
argument_list|()
argument_list|,
name|isShutdownExecutorService
argument_list|()
argument_list|,
name|isStreaming
argument_list|()
argument_list|,
name|isStopOnException
argument_list|()
argument_list|,
name|getTimeout
argument_list|()
argument_list|,
name|getOnPrepare
argument_list|()
argument_list|,
name|isShareUnitOfWork
argument_list|()
argument_list|,
name|isParallelAggregate
argument_list|()
argument_list|,
name|isStopOnAggregateException
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
specifier|synchronized
name|ExecutorService
name|createAggregateExecutorService
parameter_list|(
name|String
name|name
parameter_list|)
block|{
comment|// use a shared executor service to avoid creating new thread pools
if|if
condition|(
name|aggregateExecutorService
operator|==
literal|null
condition|)
block|{
name|aggregateExecutorService
operator|=
name|super
operator|.
name|createAggregateExecutorService
argument_list|(
literal|"RecipientList-AggregateTask"
argument_list|)
expr_stmt|;
block|}
return|return
name|aggregateExecutorService
return|;
block|}
block|}
decl_stmt|;
name|rlp
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|isIgnoreInvalidEndpoints
argument_list|()
argument_list|)
expr_stmt|;
comment|// start the service
try|try
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|rlp
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
comment|// now let the multicast process the exchange
return|return
name|rlp
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
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
literal|"RecipientList {} using ProducerCache with cacheSize={}"
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
name|aggregationStrategy
argument_list|,
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
argument_list|,
name|aggregationStrategy
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
name|aggregationStrategy
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownExecutorService
operator|&&
name|executorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
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
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|isStreaming ()
specifier|public
name|boolean
name|isStreaming
parameter_list|()
block|{
return|return
name|streaming
return|;
block|}
DECL|method|setStreaming (boolean streaming)
specifier|public
name|void
name|setStreaming
parameter_list|(
name|boolean
name|streaming
parameter_list|)
block|{
name|this
operator|.
name|streaming
operator|=
name|streaming
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
DECL|method|isParallelProcessing ()
specifier|public
name|boolean
name|isParallelProcessing
parameter_list|()
block|{
return|return
name|parallelProcessing
return|;
block|}
DECL|method|setParallelProcessing (boolean parallelProcessing)
specifier|public
name|void
name|setParallelProcessing
parameter_list|(
name|boolean
name|parallelProcessing
parameter_list|)
block|{
name|this
operator|.
name|parallelProcessing
operator|=
name|parallelProcessing
expr_stmt|;
block|}
DECL|method|isParallelAggregate ()
specifier|public
name|boolean
name|isParallelAggregate
parameter_list|()
block|{
return|return
name|parallelAggregate
return|;
block|}
DECL|method|setParallelAggregate (boolean parallelAggregate)
specifier|public
name|void
name|setParallelAggregate
parameter_list|(
name|boolean
name|parallelAggregate
parameter_list|)
block|{
name|this
operator|.
name|parallelAggregate
operator|=
name|parallelAggregate
expr_stmt|;
block|}
DECL|method|isStopOnAggregateException ()
specifier|public
name|boolean
name|isStopOnAggregateException
parameter_list|()
block|{
return|return
name|stopOnAggregateException
return|;
block|}
DECL|method|setStopOnAggregateException (boolean stopOnAggregateException)
specifier|public
name|void
name|setStopOnAggregateException
parameter_list|(
name|boolean
name|stopOnAggregateException
parameter_list|)
block|{
name|this
operator|.
name|stopOnAggregateException
operator|=
name|stopOnAggregateException
expr_stmt|;
block|}
DECL|method|isStopOnException ()
specifier|public
name|boolean
name|isStopOnException
parameter_list|()
block|{
return|return
name|stopOnException
return|;
block|}
DECL|method|setStopOnException (boolean stopOnException)
specifier|public
name|void
name|setStopOnException
parameter_list|(
name|boolean
name|stopOnException
parameter_list|)
block|{
name|this
operator|.
name|stopOnException
operator|=
name|stopOnException
expr_stmt|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
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
DECL|method|isShutdownExecutorService ()
specifier|public
name|boolean
name|isShutdownExecutorService
parameter_list|()
block|{
return|return
name|shutdownExecutorService
return|;
block|}
DECL|method|setShutdownExecutorService (boolean shutdownExecutorService)
specifier|public
name|void
name|setShutdownExecutorService
parameter_list|(
name|boolean
name|shutdownExecutorService
parameter_list|)
block|{
name|this
operator|.
name|shutdownExecutorService
operator|=
name|shutdownExecutorService
expr_stmt|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
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
DECL|method|isShareUnitOfWork ()
specifier|public
name|boolean
name|isShareUnitOfWork
parameter_list|()
block|{
return|return
name|shareUnitOfWork
return|;
block|}
DECL|method|setShareUnitOfWork (boolean shareUnitOfWork)
specifier|public
name|void
name|setShareUnitOfWork
parameter_list|(
name|boolean
name|shareUnitOfWork
parameter_list|)
block|{
name|this
operator|.
name|shareUnitOfWork
operator|=
name|shareUnitOfWork
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
block|}
end_class

end_unit

