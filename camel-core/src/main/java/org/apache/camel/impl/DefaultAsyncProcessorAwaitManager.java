begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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
name|RejectedExecutionException
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
name|AtomicLong
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
name|MessageHistory
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
name|NamedNode
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
name|DefaultExchangeFormatter
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
name|AsyncProcessorAwaitManager
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
name|ExchangeFormatter
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
name|ObjectHelper
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

begin_class
DECL|class|DefaultAsyncProcessorAwaitManager
specifier|public
class|class
name|DefaultAsyncProcessorAwaitManager
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessorAwaitManager
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
name|DefaultAsyncProcessorAwaitManager
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|statistics
specifier|private
specifier|final
name|AsyncProcessorAwaitManager
operator|.
name|Statistics
name|statistics
init|=
operator|new
name|UtilizationStatistics
argument_list|()
decl_stmt|;
DECL|field|blockedCounter
specifier|private
specifier|final
name|AtomicLong
name|blockedCounter
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|interruptedCounter
specifier|private
specifier|final
name|AtomicLong
name|interruptedCounter
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|totalDuration
specifier|private
specifier|final
name|AtomicLong
name|totalDuration
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|minDuration
specifier|private
specifier|final
name|AtomicLong
name|minDuration
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|maxDuration
specifier|private
specifier|final
name|AtomicLong
name|maxDuration
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|meanDuration
specifier|private
specifier|final
name|AtomicLong
name|meanDuration
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|inflight
specifier|private
specifier|final
name|Map
argument_list|<
name|Exchange
argument_list|,
name|AwaitThread
argument_list|>
name|inflight
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|exchangeFormatter
specifier|private
specifier|final
name|ExchangeFormatter
name|exchangeFormatter
decl_stmt|;
DECL|field|interruptThreadsWhileStopping
specifier|private
name|boolean
name|interruptThreadsWhileStopping
init|=
literal|true
decl_stmt|;
DECL|method|DefaultAsyncProcessorAwaitManager ()
specifier|public
name|DefaultAsyncProcessorAwaitManager
parameter_list|()
block|{
comment|// setup exchange formatter to be used for message history dump
name|DefaultExchangeFormatter
name|formatter
init|=
operator|new
name|DefaultExchangeFormatter
argument_list|()
decl_stmt|;
name|formatter
operator|.
name|setShowExchangeId
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setMultiline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setShowHeaders
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|formatter
operator|.
name|setStyle
argument_list|(
name|DefaultExchangeFormatter
operator|.
name|OutputStyle
operator|.
name|Fixed
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchangeFormatter
operator|=
name|formatter
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|await (Exchange exchange, CountDownLatch latch)
specifier|public
name|void
name|await
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for asynchronous callback before continuing for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|statistics
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|blockedCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
name|inflight
operator|.
name|put
argument_list|(
name|exchange
argument_list|,
operator|new
name|AwaitThreadEntry
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|latch
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received, will continue routing exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
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
name|trace
argument_list|(
literal|"Interrupted while waiting for callback, will continue routing exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|AwaitThread
name|thread
init|=
name|inflight
operator|.
name|remove
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|statistics
operator|.
name|isStatisticsEnabled
argument_list|()
operator|&&
name|thread
operator|!=
literal|null
condition|)
block|{
name|long
name|time
init|=
name|thread
operator|.
name|getWaitDuration
argument_list|()
decl_stmt|;
name|long
name|total
init|=
name|totalDuration
operator|.
name|get
argument_list|()
operator|+
name|time
decl_stmt|;
name|totalDuration
operator|.
name|set
argument_list|(
name|total
argument_list|)
expr_stmt|;
if|if
condition|(
name|time
operator|<
name|minDuration
operator|.
name|get
argument_list|()
condition|)
block|{
name|minDuration
operator|.
name|set
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|time
operator|>
name|maxDuration
operator|.
name|get
argument_list|()
condition|)
block|{
name|maxDuration
operator|.
name|set
argument_list|(
name|time
argument_list|)
expr_stmt|;
block|}
comment|// update mean
name|long
name|count
init|=
name|blockedCounter
operator|.
name|get
argument_list|()
decl_stmt|;
name|long
name|mean
init|=
name|count
operator|>
literal|0
condition|?
name|total
operator|/
name|count
else|:
literal|0
decl_stmt|;
name|meanDuration
operator|.
name|set
argument_list|(
name|mean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|countDown (Exchange exchange, CountDownLatch latch)
specifier|public
name|void
name|countDown
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received for exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|inflight
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|browse ()
specifier|public
name|Collection
argument_list|<
name|AwaitThread
argument_list|>
name|browse
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableCollection
argument_list|(
name|inflight
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|interrupt (String exchangeId)
specifier|public
name|void
name|interrupt
parameter_list|(
name|String
name|exchangeId
parameter_list|)
block|{
comment|// need to find the exchange with the given exchange id
name|Exchange
name|found
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AsyncProcessorAwaitManager
operator|.
name|AwaitThread
name|entry
range|:
name|browse
argument_list|()
control|)
block|{
name|Exchange
name|exchange
init|=
name|entry
operator|.
name|getExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchangeId
operator|.
name|equals
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
condition|)
block|{
name|found
operator|=
name|exchange
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|found
operator|!=
literal|null
condition|)
block|{
name|interrupt
argument_list|(
name|found
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|interrupt (Exchange exchange)
specifier|public
name|void
name|interrupt
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|AwaitThreadEntry
name|entry
init|=
operator|(
name|AwaitThreadEntry
operator|)
name|inflight
operator|.
name|get
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Interrupted while waiting for asynchronous callback, will release the following blocked thread which was waiting for exchange to finish processing with exchangeId: "
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|dumpBlockedThread
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
comment|// dump a route stack trace of the exchange
name|String
name|routeStackTrace
init|=
name|MessageHelper
operator|.
name|dumpMessageHistoryStacktrace
argument_list|(
name|exchange
argument_list|,
name|exchangeFormatter
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|routeStackTrace
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|routeStackTrace
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|warn
argument_list|(
name|sb
operator|.
name|toString
argument_list|()
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
finally|finally
block|{
if|if
condition|(
name|statistics
operator|.
name|isStatisticsEnabled
argument_list|()
condition|)
block|{
name|interruptedCounter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Interrupted while waiting for asynchronous callback for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|INTERRUPTED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|entry
operator|.
name|getLatch
argument_list|()
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|isInterruptThreadsWhileStopping ()
specifier|public
name|boolean
name|isInterruptThreadsWhileStopping
parameter_list|()
block|{
return|return
name|interruptThreadsWhileStopping
return|;
block|}
DECL|method|setInterruptThreadsWhileStopping (boolean interruptThreadsWhileStopping)
specifier|public
name|void
name|setInterruptThreadsWhileStopping
parameter_list|(
name|boolean
name|interruptThreadsWhileStopping
parameter_list|)
block|{
name|this
operator|.
name|interruptThreadsWhileStopping
operator|=
name|interruptThreadsWhileStopping
expr_stmt|;
block|}
DECL|method|getStatistics ()
specifier|public
name|Statistics
name|getStatistics
parameter_list|()
block|{
return|return
name|statistics
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
comment|// noop
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
name|Collection
argument_list|<
name|AwaitThread
argument_list|>
name|threads
init|=
name|browse
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|threads
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Shutting down while there are still {} inflight threads currently blocked."
argument_list|,
name|count
argument_list|)
expr_stmt|;
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|AwaitThread
name|entry
range|:
name|threads
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|dumpBlockedThread
argument_list|(
name|entry
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isInterruptThreadsWhileStopping
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The following threads are blocked and will be interrupted so the threads are released:\n{}"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AwaitThread
name|entry
range|:
name|threads
control|)
block|{
try|try
block|{
name|interrupt
argument_list|(
name|entry
operator|.
name|getExchange
argument_list|()
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
literal|"Error while interrupting thread: "
operator|+
name|entry
operator|.
name|getBlockedThread
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"The following threads are blocked, and may reside in the JVM:\n{}"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Shutting down with no inflight threads."
argument_list|)
expr_stmt|;
block|}
name|inflight
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|dumpBlockedThread (AwaitThread entry)
specifier|private
specifier|static
name|String
name|dumpBlockedThread
parameter_list|(
name|AwaitThread
name|entry
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"Blocked Thread\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"---------------------------------------------------------------------------------------------------------------------------------------\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"Id:"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getBlockedThread
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"Name:"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getBlockedThread
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"RouteId:"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|safeNull
argument_list|(
name|entry
operator|.
name|getRouteId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"NodeId:"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|safeNull
argument_list|(
name|entry
operator|.
name|getNodeId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|style
argument_list|(
literal|"Duration:"
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getWaitDuration
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|" msec.\n"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|style (String label)
specifier|private
specifier|static
name|String
name|style
parameter_list|(
name|String
name|label
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"\t%-20s"
argument_list|,
name|label
argument_list|)
return|;
block|}
DECL|method|safeNull (Object value)
specifier|private
specifier|static
name|String
name|safeNull
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
return|return
name|value
operator|!=
literal|null
condition|?
name|value
operator|.
name|toString
argument_list|()
else|:
literal|""
return|;
block|}
DECL|class|AwaitThreadEntry
specifier|private
specifier|static
specifier|final
class|class
name|AwaitThreadEntry
implements|implements
name|AwaitThread
block|{
DECL|field|thread
specifier|private
specifier|final
name|Thread
name|thread
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
decl_stmt|;
DECL|field|start
specifier|private
specifier|final
name|long
name|start
decl_stmt|;
DECL|method|AwaitThreadEntry (Thread thread, Exchange exchange, CountDownLatch latch)
specifier|private
name|AwaitThreadEntry
parameter_list|(
name|Thread
name|thread
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|)
block|{
name|this
operator|.
name|thread
operator|=
name|thread
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|latch
operator|=
name|latch
expr_stmt|;
name|this
operator|.
name|start
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBlockedThread ()
specifier|public
name|Thread
name|getBlockedThread
parameter_list|()
block|{
return|return
name|thread
return|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
DECL|method|getWaitDuration ()
specifier|public
name|long
name|getWaitDuration
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
return|;
block|}
annotation|@
name|Override
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
name|MessageHistory
name|lastMessageHistory
init|=
name|getLastMessageHistory
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastMessageHistory
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|lastMessageHistory
operator|.
name|getRouteId
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getNodeId ()
specifier|public
name|String
name|getNodeId
parameter_list|()
block|{
name|NamedNode
name|node
init|=
name|getNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|node
operator|.
name|getId
argument_list|()
return|;
block|}
DECL|method|getLatch ()
specifier|public
name|CountDownLatch
name|getLatch
parameter_list|()
block|{
return|return
name|latch
return|;
block|}
DECL|method|getNode ()
specifier|private
name|NamedNode
name|getNode
parameter_list|()
block|{
name|MessageHistory
name|lastMessageHistory
init|=
name|getLastMessageHistory
argument_list|()
decl_stmt|;
if|if
condition|(
name|lastMessageHistory
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|lastMessageHistory
operator|.
name|getNode
argument_list|()
return|;
block|}
DECL|method|getLastMessageHistory ()
specifier|private
name|MessageHistory
name|getLastMessageHistory
parameter_list|()
block|{
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|list
init|=
name|getMessageHistories
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
operator|||
name|list
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|list
operator|.
name|getLast
argument_list|()
return|;
block|}
DECL|method|getMessageHistories ()
specifier|private
name|LinkedList
argument_list|<
name|MessageHistory
argument_list|>
name|getMessageHistories
parameter_list|()
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|MESSAGE_HISTORY
argument_list|,
name|LinkedList
operator|.
name|class
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
literal|"AwaitThreadEntry[name="
operator|+
name|thread
operator|.
name|getName
argument_list|()
operator|+
literal|", exchangeId="
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
comment|/**      * Represents utilization statistics      */
DECL|class|UtilizationStatistics
specifier|private
specifier|final
class|class
name|UtilizationStatistics
implements|implements
name|AsyncProcessorAwaitManager
operator|.
name|Statistics
block|{
DECL|field|statisticsEnabled
specifier|private
name|boolean
name|statisticsEnabled
decl_stmt|;
annotation|@
name|Override
DECL|method|getThreadsBlocked ()
specifier|public
name|long
name|getThreadsBlocked
parameter_list|()
block|{
return|return
name|blockedCounter
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getThreadsInterrupted ()
specifier|public
name|long
name|getThreadsInterrupted
parameter_list|()
block|{
return|return
name|interruptedCounter
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getTotalDuration ()
specifier|public
name|long
name|getTotalDuration
parameter_list|()
block|{
return|return
name|totalDuration
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMinDuration ()
specifier|public
name|long
name|getMinDuration
parameter_list|()
block|{
return|return
name|minDuration
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMaxDuration ()
specifier|public
name|long
name|getMaxDuration
parameter_list|()
block|{
return|return
name|maxDuration
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getMeanDuration ()
specifier|public
name|long
name|getMeanDuration
parameter_list|()
block|{
return|return
name|meanDuration
operator|.
name|get
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
name|void
name|reset
parameter_list|()
block|{
name|blockedCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|interruptedCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|totalDuration
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|minDuration
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|maxDuration
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|meanDuration
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isStatisticsEnabled ()
specifier|public
name|boolean
name|isStatisticsEnabled
parameter_list|()
block|{
return|return
name|statisticsEnabled
return|;
block|}
annotation|@
name|Override
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
specifier|public
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
block|{
name|this
operator|.
name|statisticsEnabled
operator|=
name|statisticsEnabled
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
name|String
operator|.
name|format
argument_list|(
literal|"AsyncProcessAwaitManager utilization[blocked=%s, interrupted=%s, total=%s min=%s, max=%s, mean=%s]"
argument_list|,
name|getThreadsBlocked
argument_list|()
argument_list|,
name|getThreadsInterrupted
argument_list|()
argument_list|,
name|getTotalDuration
argument_list|()
argument_list|,
name|getMinDuration
argument_list|()
argument_list|,
name|getMaxDuration
argument_list|()
argument_list|,
name|getMeanDuration
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

