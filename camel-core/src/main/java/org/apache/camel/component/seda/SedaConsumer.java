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
name|impl
operator|.
name|converter
operator|.
name|AsyncProcessorTypeConverter
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
name|util
operator|.
name|AsyncProcessorHelper
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
comment|/**  * A Consumer for the SEDA component.  *  * @version $Revision$  */
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
DECL|field|latch
specifier|private
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
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
name|processor
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
literal|"SedaConsumer["
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
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
DECL|method|prepareShutdown ()
specifier|public
name|void
name|prepareShutdown
parameter_list|()
block|{
comment|// signal we want to shutdown
name|shutdownPending
operator|=
literal|true
expr_stmt|;
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
literal|"Preparing to shutdown, waiting for "
operator|+
name|latch
operator|.
name|getCount
argument_list|()
operator|+
literal|" consumer threads to complete."
argument_list|)
expr_stmt|;
block|}
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
DECL|method|run ()
specifier|public
name|void
name|run
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
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
try|try
block|{
name|exchange
operator|=
name|queue
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
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sendToConsumers
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// log exception if an exception occurred and was not handled
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
literal|"Shutdown is pending, so this consumer thread is breaking out because the task queue is empty."
argument_list|)
expr_stmt|;
block|}
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
literal|"Ending this polling consumer thread, there are still "
operator|+
name|latch
operator|.
name|getCount
argument_list|()
operator|+
literal|" consumer threads left."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Send the given {@link Exchange} to the consumer(s).      *<p/>      * If multiple consumers then they will each receive a copy of the Exchange.      * A multicast processor will send the exchange in parallel to the multiple consumers.      *<p/>      * If there is only a single consumer then its dispatched directly to it using same thread.      *       * @param exchange the exchange      * @throws Exception can be thrown if processing of the exchange failed      */
DECL|method|sendToConsumers (Exchange exchange)
specifier|protected
name|void
name|sendToConsumers
parameter_list|(
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
literal|"Multicasting to "
operator|+
name|endpoint
operator|.
name|getConsumers
argument_list|()
operator|.
name|size
argument_list|()
operator|+
literal|" consumers for Exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// use a multicast processor to process it
name|MulticastProcessor
name|mp
init|=
name|endpoint
operator|.
name|getConsumerMulticastProcessor
argument_list|()
decl_stmt|;
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
comment|// noop
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
name|int
name|poolSize
init|=
name|endpoint
operator|.
name|getConcurrentConsumers
argument_list|()
decl_stmt|;
name|executor
operator|=
name|endpoint
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
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|poolSize
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
name|poolSize
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
name|endpoint
operator|.
name|onStarted
argument_list|(
name|this
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
name|endpoint
operator|.
name|onStopped
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// must shutdown executor on stop to avoid overhead of having them running
comment|// use shutdown now to force the tasks which are polling for new exchanges
comment|// to stop immediately to avoid them picking up new exchanges arriving in the mean time
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
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|shutdownNow
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
block|}
end_class

end_unit

