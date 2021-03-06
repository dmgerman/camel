begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
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
name|CompletionService
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
name|DelayQueue
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
name|Delayed
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
name|Executor
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
name|Future
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
name|FutureTask
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

begin_comment
comment|/**  * A {@link java.util.concurrent.CompletionService} that orders the completed tasks  * in the same order as they where submitted.  */
end_comment

begin_class
DECL|class|SubmitOrderedCompletionService
specifier|public
class|class
name|SubmitOrderedCompletionService
parameter_list|<
name|V
parameter_list|>
implements|implements
name|CompletionService
argument_list|<
name|V
argument_list|>
block|{
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
comment|// the idea to order the completed task in the same order as they where submitted is to leverage
comment|// the delay queue. With the delay queue we can control the order by the getDelay and compareTo methods
comment|// where we can order the tasks in the same order as they where submitted.
DECL|field|completionQueue
specifier|private
specifier|final
name|DelayQueue
argument_list|<
name|SubmitOrderFutureTask
argument_list|>
name|completionQueue
init|=
operator|new
name|DelayQueue
argument_list|<>
argument_list|()
decl_stmt|;
comment|// id is the unique id that determines the order in which tasks was submitted (incrementing)
DECL|field|id
specifier|private
specifier|final
name|AtomicInteger
name|id
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
comment|// index is the index of the next id that should expire and thus be ready to take from the delayed queue
DECL|field|index
specifier|private
specifier|final
name|AtomicInteger
name|index
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|class|SubmitOrderFutureTask
specifier|private
class|class
name|SubmitOrderFutureTask
extends|extends
name|FutureTask
argument_list|<
name|V
argument_list|>
implements|implements
name|Delayed
block|{
comment|// the id this task was assigned
DECL|field|id
specifier|private
specifier|final
name|long
name|id
decl_stmt|;
DECL|method|SubmitOrderFutureTask (long id, Callable<V> voidCallable)
name|SubmitOrderFutureTask
parameter_list|(
name|long
name|id
parameter_list|,
name|Callable
argument_list|<
name|V
argument_list|>
name|voidCallable
parameter_list|)
block|{
name|super
argument_list|(
name|voidCallable
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|SubmitOrderFutureTask (long id, Runnable runnable, V result)
name|SubmitOrderFutureTask
parameter_list|(
name|long
name|id
parameter_list|,
name|Runnable
name|runnable
parameter_list|,
name|V
name|result
parameter_list|)
block|{
name|super
argument_list|(
name|runnable
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDelay (TimeUnit unit)
specifier|public
name|long
name|getDelay
parameter_list|(
name|TimeUnit
name|unit
parameter_list|)
block|{
comment|// if the answer is 0 then this task is ready to be taken
name|long
name|answer
init|=
name|id
operator|-
name|index
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|<=
literal|0
condition|)
block|{
return|return
name|answer
return|;
block|}
comment|// okay this task is not ready yet, and we don't really know when it would be
comment|// so we have to return a delay value of one time unit
if|if
condition|(
name|TimeUnit
operator|.
name|NANOSECONDS
operator|==
name|unit
condition|)
block|{
comment|// okay this is too fast so use a little more delay to avoid CPU burning cycles
comment|// To avoid align with java 11 impl of
comment|// "java.util.concurrent.locks.AbstractQueuedSynchronizer.SPIN_FOR_TIMEOUT_THRESHOLD", otherwise
comment|// no sleep with very high CPU usage
name|answer
operator|=
literal|1001L
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|unit
operator|.
name|convert
argument_list|(
literal|1
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|compareTo (Delayed o)
specifier|public
name|int
name|compareTo
parameter_list|(
name|Delayed
name|o
parameter_list|)
block|{
name|SubmitOrderFutureTask
name|other
init|=
operator|(
name|SubmitOrderFutureTask
operator|)
name|o
decl_stmt|;
return|return
call|(
name|int
call|)
argument_list|(
name|this
operator|.
name|id
operator|-
name|other
operator|.
name|id
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|done ()
specifier|protected
name|void
name|done
parameter_list|()
block|{
comment|// when we are done add to the completion queue
name|completionQueue
operator|.
name|add
argument_list|(
name|this
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
comment|// output using zero-based index
return|return
literal|"SubmitOrderedFutureTask["
operator|+
operator|(
name|id
operator|-
literal|1
operator|)
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|SubmitOrderedCompletionService (Executor executor)
specifier|public
name|SubmitOrderedCompletionService
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|submit (Callable<V> task)
specifier|public
name|Future
argument_list|<
name|V
argument_list|>
name|submit
parameter_list|(
name|Callable
argument_list|<
name|V
argument_list|>
name|task
parameter_list|)
block|{
if|if
condition|(
name|task
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Task must be provided"
argument_list|)
throw|;
block|}
name|SubmitOrderFutureTask
name|f
init|=
operator|new
name|SubmitOrderFutureTask
argument_list|(
name|id
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|task
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
annotation|@
name|Override
DECL|method|submit (Runnable task, Object result)
specifier|public
name|Future
argument_list|<
name|V
argument_list|>
name|submit
parameter_list|(
name|Runnable
name|task
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|task
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Task must be provided"
argument_list|)
throw|;
block|}
name|SubmitOrderFutureTask
name|f
init|=
operator|new
name|SubmitOrderFutureTask
argument_list|(
name|id
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|task
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|f
argument_list|)
expr_stmt|;
return|return
name|f
return|;
block|}
annotation|@
name|Override
DECL|method|take ()
specifier|public
name|Future
argument_list|<
name|V
argument_list|>
name|take
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|index
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
return|return
name|completionQueue
operator|.
name|take
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|public
name|Future
argument_list|<
name|V
argument_list|>
name|poll
parameter_list|()
block|{
name|index
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|Future
argument_list|<
name|V
argument_list|>
name|answer
init|=
name|completionQueue
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// decrease counter if we didnt get any data
name|index
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|poll (long timeout, TimeUnit unit)
specifier|public
name|Future
argument_list|<
name|V
argument_list|>
name|poll
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|index
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|Future
argument_list|<
name|V
argument_list|>
name|answer
init|=
name|completionQueue
operator|.
name|poll
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// decrease counter if we didnt get any data
name|index
operator|.
name|decrementAndGet
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Marks the current task as timeout, which allows you to poll the next      * tasks which may already have been completed.      */
DECL|method|timeoutTask ()
specifier|public
name|void
name|timeoutTask
parameter_list|()
block|{
name|index
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

