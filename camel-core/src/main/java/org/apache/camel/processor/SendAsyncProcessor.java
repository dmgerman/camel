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
name|BlockingQueue
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
name|LinkedBlockingQueue
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
name|TimeUnit
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
name|Navigate
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
name|LoggingExceptionHandler
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
name|ExceptionHandler
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

begin_comment
comment|/**  * @version $Revision$  * @deprecated will be replaced with a new async routing engine in Camel 2.4  */
end_comment

begin_class
annotation|@
name|Deprecated
DECL|class|SendAsyncProcessor
specifier|public
class|class
name|SendAsyncProcessor
extends|extends
name|SendProcessor
implements|implements
name|Runnable
implements|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|target
specifier|private
specifier|final
name|Processor
name|target
decl_stmt|;
DECL|field|completedTasks
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|completedTasks
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchange
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|producerExecutorService
specifier|private
name|ExecutorService
name|producerExecutorService
decl_stmt|;
DECL|field|poolSize
specifier|private
name|int
name|poolSize
init|=
literal|10
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|method|SendAsyncProcessor (Endpoint destination, Processor target)
specifier|public
name|SendAsyncProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|Processor
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|destination
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
block|}
DECL|method|SendAsyncProcessor (Endpoint destination, ExchangePattern pattern, Processor target)
specifier|public
name|SendAsyncProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|Processor
name|target
parameter_list|)
block|{
name|super
argument_list|(
name|destination
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|destination
operator|.
name|getCamelContext
argument_list|()
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
comment|// use a new copy of the exchange to route async and handover the on completion to the new copy
comment|// so its the new copy that performs the on completion callback when its done
specifier|final
name|Exchange
name|copy
init|=
name|ExchangeHelper
operator|.
name|createCorrelatedCopy
argument_list|(
name|exchange
argument_list|,
literal|true
argument_list|)
decl_stmt|;
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|copy
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// default to use in out as we do request reply over async
name|copy
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
block|}
comment|// configure the endpoint we are sending to
name|copy
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
comment|// send the copy
return|return
name|copy
return|;
block|}
annotation|@
name|Override
DECL|method|doProcess (Exchange exchange)
specifier|public
name|Exchange
name|doProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// now we are done, we should have a API callback for this
comment|// send the exchange to the destination using a producer
comment|// acquire the producer from the service pool
specifier|final
name|Producer
name|producer
init|=
name|producerCache
operator|.
name|acquireProducer
argument_list|(
name|destination
argument_list|)
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|producer
argument_list|,
literal|"producer"
argument_list|)
expr_stmt|;
comment|// pass in the callback that adds the exchange to the completed list of tasks
specifier|final
name|AsyncCallback
name|callback
init|=
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|onTaskCompleted
parameter_list|(
name|Exchange
name|exchange
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"onTaskCompleted "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|completedTasks
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// must return the producer to service pool when we are done
try|try
block|{
name|producerCache
operator|.
name|releaseProducer
argument_list|(
name|destination
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
name|LOG
operator|.
name|warn
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
decl_stmt|;
comment|// prepare exchange for async processing
name|exchange
operator|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
comment|// process the exchange async
if|if
condition|(
name|producer
operator|instanceof
name|AsyncProcessor
condition|)
block|{
comment|// producer is async capable so let it process it directly
name|doAsyncProcess
argument_list|(
operator|(
name|AsyncProcessor
operator|)
name|producer
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// producer is a regular processor so simulate async behaviour
name|doSimulateAsyncProcess
argument_list|(
name|producer
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|// and return the exchange
return|return
name|exchange
return|;
block|}
comment|/**      * The producer is already capable of async processing so let it process it directly.      *      * @param producer the async producer      * @param exchange the exchange      * @param callback the callback      *      * @throws Exception can be thrown in case of processing errors      */
DECL|method|doAsyncProcess (AsyncProcessor producer, Exchange exchange, AsyncCallback callback)
specifier|protected
name|void
name|doAsyncProcess
parameter_list|(
name|AsyncProcessor
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|/**      * The producer is<b>not</b> capable of async processing so lets simulate this by transferring the task      * to another {@link ExecutorService} for async processing.      *      * @param producer the producer      * @param exchange the exchange      * @param callback the callback      *      * @throws Exception can be thrown in case of processing errors      */
DECL|method|doSimulateAsyncProcess (final Processor producer, final Exchange exchange, final AsyncCallback callback)
specifier|protected
name|void
name|doSimulateAsyncProcess
parameter_list|(
specifier|final
name|Processor
name|producer
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|Exception
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
literal|"Producer "
operator|+
name|producer
operator|+
literal|" is not an instanceof AsyncProcessor"
operator|+
literal|". Will fallback to simulate async behavior by transferring task to a producer thread pool for further processing."
argument_list|)
expr_stmt|;
block|}
comment|// let the producer thread pool handle the task of sending the request which then will simulate the async
comment|// behavior as the original thread is not blocking while we wait for the reply
name|getProducerExecutorService
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
comment|// convert the async producer which just blocks until the task is complete
try|try
block|{
name|AsyncProcessor
name|asyncProducer
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|AsyncProcessor
operator|.
name|class
argument_list|,
name|producer
argument_list|)
decl_stmt|;
name|asyncProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
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
literal|"Caught exception while processing: "
operator|+
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// set the exception on the exchange so Camel error handling can deal with it
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"sendAsyncTo("
operator|+
name|destination
operator|+
operator|(
name|pattern
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|pattern
else|:
literal|""
operator|)
operator|+
literal|" -> "
operator|+
name|target
operator|+
literal|")"
return|;
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
comment|/**      * Sets the {@link java.util.concurrent.ExecutorService} to use for consuming replies.      *      * @param executorService the custom executor service      */
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
DECL|method|getProducerExecutorService ()
specifier|public
specifier|synchronized
name|ExecutorService
name|getProducerExecutorService
parameter_list|()
block|{
if|if
condition|(
name|producerExecutorService
operator|==
literal|null
condition|)
block|{
comment|// use a default pool for the producers which can grow/schrink itself
name|producerExecutorService
operator|=
name|destination
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"SendAsyncProcessor-Producer"
argument_list|)
expr_stmt|;
block|}
return|return
name|producerExecutorService
return|;
block|}
comment|/**      * Sets the {@link java.util.concurrent.ExecutorService} to use for simulating async producers      * by transferring the {@link Exchange} to this {@link java.util.concurrent.ExecutorService} for      * sending the request and block while waiting for the reply. However the original thread      * will not block and as such it all appears as real async request/reply mechanism.      *      * @param producerExecutorService the custom executor service for producers      */
DECL|method|setProducerExecutorService (ExecutorService producerExecutorService)
specifier|public
name|void
name|setProducerExecutorService
parameter_list|(
name|ExecutorService
name|producerExecutorService
parameter_list|)
block|{
name|this
operator|.
name|producerExecutorService
operator|=
name|producerExecutorService
expr_stmt|;
block|}
DECL|method|getPoolSize ()
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|poolSize
return|;
block|}
DECL|method|setPoolSize (int poolSize)
specifier|public
name|void
name|setPoolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|this
operator|.
name|poolSize
operator|=
name|poolSize
expr_stmt|;
block|}
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exceptionHandler
return|;
block|}
DECL|method|setExceptionHandler (ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|target
operator|!=
literal|null
return|;
block|}
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|target
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
name|isRunAllowed
argument_list|()
condition|)
block|{
name|Exchange
name|exchange
decl_stmt|;
try|try
block|{
name|exchange
operator|=
name|completedTasks
operator|.
name|poll
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
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
block|}
continue|continue;
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// copy OUT to IN
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
comment|// replace OUT with IN as async processing changed something
name|exchange
operator|.
name|setIn
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
literal|"Async reply received now routing the Exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|target
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch throwable to avoid existing this method and thus the thread terminates
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
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
name|poolSize
operator|<=
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"PoolSize must be a positive number, was: "
operator|+
name|poolSize
argument_list|)
throw|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|poolSize
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|destination
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"SendAsyncProcessor-Consumer"
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
block|}
name|executorService
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
comment|// must shutdown executor service as its used for concurrent consumers
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
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
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
comment|// clear the completed tasks when we shutdown
name|completedTasks
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

