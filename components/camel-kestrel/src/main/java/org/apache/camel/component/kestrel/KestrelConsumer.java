begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kestrel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kestrel
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
name|Exchanger
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|net
operator|.
name|spy
operator|.
name|memcached
operator|.
name|MemcachedClient
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
name|DefaultConsumer
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
comment|/**  * A Camel consumer that polls a kestrel queue.  */
end_comment

begin_class
DECL|class|KestrelConsumer
specifier|public
class|class
name|KestrelConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|ShutdownAware
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|KestrelEndpoint
name|endpoint
decl_stmt|;
DECL|field|memcachedClient
specifier|private
specifier|final
name|MemcachedClient
name|memcachedClient
decl_stmt|;
DECL|field|exchangerQueue
specifier|private
specifier|final
name|BlockingQueue
argument_list|<
name|Exchanger
argument_list|>
name|exchangerQueue
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Exchanger
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|pollerExecutor
specifier|private
name|ExecutorService
name|pollerExecutor
decl_stmt|;
DECL|field|handlerExecutor
specifier|private
name|ExecutorService
name|handlerExecutor
decl_stmt|;
DECL|field|shutdownPending
specifier|private
specifier|volatile
name|boolean
name|shutdownPending
decl_stmt|;
DECL|field|shutdownLatch
specifier|private
name|CountDownLatch
name|shutdownLatch
decl_stmt|;
DECL|field|pendingExchangeCount
specifier|private
name|AtomicInteger
name|pendingExchangeCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|method|KestrelConsumer (final KestrelEndpoint endpoint, Processor processor, final MemcachedClient memcachedClient)
specifier|public
name|KestrelConsumer
parameter_list|(
specifier|final
name|KestrelEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
specifier|final
name|MemcachedClient
name|memcachedClient
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|memcachedClient
operator|=
name|memcachedClient
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
name|log
operator|.
name|info
argument_list|(
literal|"Starting consumer for "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|poolSize
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
decl_stmt|;
name|shutdownPending
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|poolSize
operator|>
literal|1
condition|)
block|{
comment|// We'll set the shutdown latch to poolSize + 1, since we'll also
comment|// wait for the poller thread when shutting down.
name|shutdownLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
name|poolSize
operator|+
literal|1
argument_list|)
expr_stmt|;
comment|// Fire up the handler thread pool
name|handlerExecutor
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
literal|"Handlers-"
operator|+
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
name|k
init|=
literal|0
init|;
name|k
operator|<
name|poolSize
condition|;
operator|++
name|k
control|)
block|{
name|handlerExecutor
operator|.
name|execute
argument_list|(
operator|new
name|Handler
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// Since we only have concurrentConsumers=1, we'll do the handling
comment|// inside the poller thread, so there will only be one thread to
comment|// wait for on this latch.
name|shutdownLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|// Fire up the single poller thread
name|pollerExecutor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"Poller-"
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|pollerExecutor
operator|.
name|submit
argument_list|(
operator|new
name|Poller
argument_list|(
name|poolSize
operator|>
literal|1
argument_list|)
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|info
argument_list|(
literal|"Stopping consumer for "
operator|+
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|pollerExecutor
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
name|pollerExecutor
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handlerExecutor
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
name|handlerExecutor
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
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
return|return
literal|false
return|;
block|}
DECL|method|getPendingExchangesSize ()
specifier|public
name|int
name|getPendingExchangesSize
parameter_list|()
block|{
return|return
name|pendingExchangeCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|prepareShutdown ()
specifier|public
name|void
name|prepareShutdown
parameter_list|()
block|{
comment|// Signal to our threads that shutdown is happening
name|shutdownPending
operator|=
literal|true
expr_stmt|;
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
literal|"Preparing to shutdown, waiting for {} threads to complete."
argument_list|,
name|shutdownLatch
operator|.
name|getCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Wait for all threads to end
try|try
block|{
name|shutdownLatch
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
comment|/**      * This single thread is responsible for reading objects from kestrel and      * dispatching them to the handler threads.  The catch is that we don't      * want to poll kestrel until we know we have a handler thread available      * and waiting to handle whatever comes up.  So the way we deal with that      * is...each handler thread has an exchanger used to "receive" objects      * from the kestrel reader thread.  When a handler thread is ready for      * work, it simply puts its exchanger in the queue.  The kestrel reader      * thread takes an exchanger from the queue (which will block until one      * is there), and *then* it can poll kestrel.  Once an object is received      * from kestrel, it gets exchanged with the handler thread, which can      * take the object and process it.  Repeat...      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|Poller
specifier|private
specifier|final
class|class
name|Poller
implements|implements
name|Runnable
block|{
DECL|field|concurrent
specifier|private
name|boolean
name|concurrent
decl_stmt|;
DECL|method|Poller (boolean concurrent)
specifier|private
name|Poller
parameter_list|(
name|boolean
name|concurrent
parameter_list|)
block|{
name|this
operator|.
name|concurrent
operator|=
name|concurrent
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Kestrel poller is running"
argument_list|)
expr_stmt|;
comment|// Construct the target key that we'll be requesting from kestrel.
comment|// Include the /t=... wait time as applicable.
name|String
name|target
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWaitTimeMs
argument_list|()
operator|>
literal|0
condition|)
block|{
name|target
operator|=
name|endpoint
operator|.
name|getQueue
argument_list|()
operator|+
literal|"/t="
operator|+
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWaitTimeMs
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|target
operator|=
name|endpoint
operator|.
name|getQueue
argument_list|()
expr_stmt|;
block|}
name|Exchanger
name|exchanger
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|shutdownPending
condition|)
block|{
if|if
condition|(
name|concurrent
condition|)
block|{
comment|// Wait until an exchanger is available, indicating that a
comment|// handler thread is ready to handle the next request.
comment|// Don't read from kestrel until we know a handler is ready.
try|try
block|{
name|exchanger
operator|=
name|exchangerQueue
operator|.
name|take
argument_list|()
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
literal|"Interrupted, are we stopping? {}"
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
comment|// We have the exchanger, so there's a handler thread ready
comment|// to handle whatever we may read...so read the next object
comment|// from the queue.
block|}
comment|// Poll kestrel until we get an object back
name|Object
name|value
init|=
literal|null
decl_stmt|;
while|while
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|shutdownPending
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Polling {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
try|try
block|{
name|value
operator|=
name|memcachedClient
operator|.
name|get
argument_list|(
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|shutdownPending
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Failed to get object from kestrel"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|// We didn't get a value back from kestrel
if|if
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|shutdownPending
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getWaitTimeMs
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Kestrel did the blocking for us
block|}
else|else
block|{
comment|// We're doing non-blocking get, so in between we
comment|// should at least sleep some short period of time
comment|// so this loop doesn't go nuts so tightly.
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ignored
parameter_list|)
block|{                             }
block|}
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Got object from {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
if|if
condition|(
name|concurrent
condition|)
block|{
comment|// Pass the object to the handler thread via the exchanger.
comment|// The handler will take it from there.
try|try
block|{
name|exchanger
operator|.
name|exchange
argument_list|(
name|value
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
literal|"Interrupted, are we stopping? {}"
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
block|}
else|else
block|{
comment|// We're non-concurrent, so handle it right here
name|pendingExchangeCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
try|try
block|{
comment|// Create the exchange and let camel process/route it
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
try|try
block|{
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
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
finally|finally
block|{
comment|// Decrement our pending exchange counter
name|pendingExchangeCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|log
operator|.
name|trace
argument_list|(
literal|"Finished polling {}"
argument_list|,
name|target
argument_list|)
expr_stmt|;
comment|// Decrement the shutdown countdown latch
name|shutdownLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|class|Handler
specifier|private
specifier|final
class|class
name|Handler
implements|implements
name|Runnable
block|{
DECL|field|exchanger
specifier|private
name|Exchanger
name|exchanger
init|=
operator|new
name|Exchanger
argument_list|()
decl_stmt|;
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} is starting"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|isRunAllowed
argument_list|()
operator|&&
operator|!
name|shutdownPending
condition|)
block|{
comment|// First things first, add our exchanger to the queue,
comment|// indicating that we're ready for a hand-off of work
try|try
block|{
name|exchangerQueue
operator|.
name|put
argument_list|(
name|exchanger
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
literal|"Interrupted, are we stopping? {}"
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
comment|// Optimistically increment our internal pending exchange
comment|// counter, anticipating getting a value back from the exchanger
name|pendingExchangeCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
try|try
block|{
comment|// Now wait for an object to come through the exchanger
name|Object
name|value
decl_stmt|;
try|try
block|{
name|value
operator|=
name|exchanger
operator|.
name|exchange
argument_list|(
name|this
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
literal|"Interrupted, are we stopping? {}"
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
name|log
operator|.
name|trace
argument_list|(
literal|"Got a value from the exchanger"
argument_list|)
expr_stmt|;
comment|// Create the exchange and let camel process/route it
name|Exchange
name|exchange
init|=
literal|null
decl_stmt|;
try|try
block|{
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
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
finally|finally
block|{
comment|// Decrement our pending exchange counter
name|pendingExchangeCount
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
comment|// Decrement the shutdown countdown latch
name|shutdownLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"{} is finished"
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

