begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
package|;
end_package

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
name|CountDownLatch
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
name|TimeUnit
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
name|AtomicInteger
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
name|Consumer
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
name|SuspendableService
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
name|processor
operator|.
name|MulticastProcessor
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
name|spi
operator|.
name|Synchronization
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
name|UnitOfWorkHelper
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
comment|/**  * A Consumer for the SEDA component.  *<p/>  * In this implementation there is a little<i>slack period</i> when you suspend/stop the consumer, by which  * the consumer may pickup a newly arrived messages and process it. That period is up till 1 second.  *  * @version   */
end_comment

begin_class
DECL|class|SedaConsumer
specifier|public
class|class
name|SedaConsumer
extends|extends
name|ServiceSupport
implements|implements
name|Consumer
implements|,
name|Runnable
implements|,
name|ShutdownAware
implements|,
name|SuspendableService
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SedaConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|taskCount
specifier|private
specifier|final
name|AtomicInteger
name|taskCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|private
specifier|volatile
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|shutdownPending
specifier|private
specifier|volatile
name|boolean
name|shutdownPending
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SedaEndpoint
name|endpoint
decl_stmt|;
DECL|field|processor
specifier|private
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
DECL|field|pollTimeout
specifier|private
specifier|final
name|int
name|pollTimeout
decl_stmt|;
DECL|method|SedaConsumer (SedaEndpoint endpoint, Processor processor)
specifier|public
name|SedaConsumer
parameter_list|(
name|SedaEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|pollTimeout
operator|=
name|endpoint
operator|.
name|getPollTimeout
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
literal|"SedaConsumer["
operator|+
name|endpoint
operator|+
literal|"]"
return|;
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
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
DECL|method|getProcessor ()
specifier|public
name|Processor
name|getProcessor
parameter_list|()
block|{
return|return
name|processor
return|;
block|}
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
specifier|public
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
block|{
comment|// deny stopping on shutdown as we want seda consumers to run in case some other queues
comment|// depend on this consumer to run, so it can complete its exchanges
return|return
literal|true
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
comment|// number of pending messages on the queue
return|return
name|endpoint
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|prepareShutdown (boolean forced)
specifier|public
name|void
name|prepareShutdown
parameter_list|(
name|boolean
name|forced
parameter_list|)
block|{
comment|// signal we want to shutdown
name|shutdownPending
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Preparing to shutdown, waiting for {} consumer threads to complete."
argument_list|,
name|latch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
comment|// wait for all threads to end
try|try
block|{
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|isRunAllowed ()
specifier|public
name|boolean
name|isRunAllowed
parameter_list|()
block|{
if|if
condition|(
name|isSuspending
argument_list|()
operator|||
name|isSuspended
argument_list|()
condition|)
block|{
comment|// allow to run even if we are suspended as we want to
comment|// keep the thread task running
return|return
literal|true
return|;
block|}
return|return
name|super
operator|.
name|isRunAllowed
argument_list|()
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|taskCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
try|try
block|{
name|doRun
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|taskCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doRun ()
specifier|protected
name|void
name|doRun
parameter_list|()
block|{
name|BlockingQueue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
name|endpoint
operator|.
name|getQueue
argument_list|()
decl_stmt|;
comment|// loop while we are allowed, or if we are stopping loop until the queue is empty
while|while
condition|(
name|queue
operator|!=
literal|null
operator|&&
operator|(
name|isRunAllowed
argument_list|()
operator|)
condition|)
block|{
comment|// do not poll during CamelContext is starting, as we should only poll when CamelContext is fully started
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarting
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"CamelContext is starting so skip polling"
argument_list|)
expr_stmt|;
try|try
block|{
comment|// sleep at most 1 sec
name|Thread
operator|.
name|sleep
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|pollTimeout
argument_list|,
literal|1000
argument_list|)
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
literal|"Sleep interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
comment|// do not poll if we are suspended
if|if
condition|(
name|isSuspending
argument_list|()
operator|||
name|isSuspended
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Consumer is suspended so skip polling"
argument_list|)
expr_stmt|;
try|try
block|{
comment|// sleep at most 1 sec
name|Thread
operator|.
name|sleep
argument_list|(
name|Math
operator|.
name|min
argument_list|(
name|pollTimeout
argument_list|,
literal|1000
argument_list|)
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
literal|"Sleep interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
block|}
continue|continue;
block|}
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// use the end user configured poll timeout
name|exchange
operator|=
name|queue
operator|.
name|poll
argument_list|(
name|pollTimeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// send a new copied exchange with new camel context
name|Exchange
name|newExchange
init|=
name|prepareExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// process the exchange
name|sendToConsumers
argument_list|(
name|newExchange
argument_list|)
expr_stmt|;
comment|// copy the message back
if|if
condition|(
name|newExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setOut
argument_list|(
name|newExchange
operator|.
name|getOut
argument_list|()
operator|.
name|copy
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setIn
argument_list|(
name|newExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// log exception if an exception occurred and was not handled
if|if
condition|(
name|newExchange
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
name|newExchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|shutdownPending
operator|&&
name|queue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutdown is pending, so this consumer thread is breaking out because the task queue is empty."
argument_list|)
expr_stmt|;
comment|// we want to shutdown so break out if there queue is empty
break|break;
block|}
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
literal|"Sleep interrupted, are we stopping? {}"
argument_list|,
name|isStopping
argument_list|()
operator|||
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
continue|continue;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
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
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ending this polling consumer thread, there are still {} consumer threads left."
argument_list|,
name|latch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy to prepare exchange for being processed by this consumer      *      * @param exchange the exchange      * @return the exchange to process by this consumer.      */
DECL|method|prepareExchange (Exchange exchange)
specifier|protected
name|Exchange
name|prepareExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// send a new copied exchange with new camel context
name|Exchange
name|newExchange
init|=
name|ExchangeHelper
operator|.
name|copyExchangeAndSetCamelContext
argument_list|(
name|exchange
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// set the from endpoint
name|newExchange
operator|.
name|setFromEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|newExchange
return|;
block|}
comment|/**      * Send the given {@link Exchange} to the consumer(s).      *<p/>      * If multiple consumers then they will each receive a copy of the Exchange.      * A multicast processor will send the exchange in parallel to the multiple consumers.      *<p/>      * If there is only a single consumer then its dispatched directly to it using same thread.      *       * @param exchange the exchange      * @throws Exception can be thrown if processing of the exchange failed      */
DECL|method|sendToConsumers (final Exchange exchange)
specifier|protected
name|void
name|sendToConsumers
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|size
init|=
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
comment|// if there are multiple consumers then multicast to them
if|if
condition|(
name|size
operator|>
literal|1
condition|)
block|{
comment|// validate multiple consumers has been enabled
if|if
condition|(
operator|!
name|endpoint
operator|.
name|isMultipleConsumersSupported
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Multiple consumers for the same endpoint is not allowed: "
operator|+
name|endpoint
argument_list|)
throw|;
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
literal|"Multicasting to {} consumers for Exchange: {}"
argument_list|,
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// handover completions, as we need to done this when the multicast is done
specifier|final
name|List
argument_list|<
name|Synchronization
argument_list|>
name|completions
init|=
name|exchange
operator|.
name|handoverCompletions
argument_list|()
decl_stmt|;
comment|// use a multicast processor to process it
name|MulticastProcessor
name|mp
init|=
name|endpoint
operator|.
name|getConsumerMulticastProcessor
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|mp
argument_list|,
literal|"ConsumerMulticastProcessor"
argument_list|,
name|this
argument_list|)
expr_stmt|;
comment|// and use the asynchronous routing engine to support it
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|mp
argument_list|,
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
comment|// done the uow on the completions
name|UnitOfWorkHelper
operator|.
name|doneSynchronizations
argument_list|(
name|exchange
argument_list|,
name|completions
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// use the regular processor and use the asynchronous routing engine to support it
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
argument_list|,
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
comment|// noop
block|}
block|}
argument_list|)
expr_stmt|;
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
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
name|shutdownPending
operator|=
literal|false
expr_stmt|;
name|setupTasks
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|onStarted
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|onStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|doStart
argument_list|()
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
name|endpoint
operator|.
name|onStopped
argument_list|(
name|this
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
comment|// only shutdown thread pool when we shutdown
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdown
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Setup the thread pool and ensures tasks gets executed (if needed)      */
DECL|method|setupTasks ()
specifier|private
name|void
name|setupTasks
parameter_list|()
block|{
name|int
name|poolSize
init|=
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
decl_stmt|;
comment|// create thread pool if needed
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
block|}
comment|// submit needed number of tasks
name|int
name|tasks
init|=
name|poolSize
operator|-
name|taskCount
operator|.
name|get
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Creating {} consumer tasks with poll timeout {} ms."
argument_list|,
name|tasks
argument_list|,
name|pollTimeout
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|tasks
condition|;
name|i
operator|++
control|)
block|{
name|executor
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

