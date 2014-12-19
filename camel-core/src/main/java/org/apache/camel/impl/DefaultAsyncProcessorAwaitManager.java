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
name|support
operator|.
name|ServiceSupport
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
comment|// TODO: capture message history of the exchange when it was interrupted
comment|// TODO: capture route id, node id where thread is blocked
comment|// TODO: rename to AsyncInflightRepository?
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
argument_list|<
name|Exchange
argument_list|,
name|AwaitThread
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|interruptThreadsWhileStopping
specifier|private
name|boolean
name|interruptThreadsWhileStopping
init|=
literal|true
decl_stmt|;
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
name|inflight
operator|.
name|remove
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
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
name|latch
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
name|latch
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Interrupted while waiting for asynchronous callback, will continue routing exchangeId: {} -> {}"
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
operator|new
name|RejectedExecutionException
argument_list|(
literal|"Interrupted while waiting for asynchronous callback"
argument_list|)
argument_list|)
expr_stmt|;
name|latch
operator|.
name|getLatch
argument_list|()
operator|.
name|countDown
argument_list|()
expr_stmt|;
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
literal|"Shutting down while there are still "
operator|+
name|count
operator|+
literal|" inflight threads currently blocked."
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
literal|"\tBlocked thread: "
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
literal|", exchangeId="
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|", duration="
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
literal|" msec."
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
literal|"The following threads are blocked and will be interrupted so the threads are released:\n"
operator|+
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
literal|"The following threads are blocked, and may reside in the JVM:\n"
operator|+
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
block|}
block|}
end_class

end_unit

