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
name|PriorityQueue
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
name|AtomicLong
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
name|locks
operator|.
name|Condition
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
name|locks
operator|.
name|Lock
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
name|locks
operator|.
name|ReentrantLock
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_class
DECL|class|AsyncCompletionService
specifier|public
class|class
name|AsyncCompletionService
parameter_list|<
name|V
parameter_list|>
block|{
DECL|field|executor
specifier|private
specifier|final
name|Executor
name|executor
decl_stmt|;
DECL|field|ordered
specifier|private
specifier|final
name|boolean
name|ordered
decl_stmt|;
DECL|field|queue
specifier|private
specifier|final
name|PriorityQueue
argument_list|<
name|Task
argument_list|>
name|queue
init|=
operator|new
name|PriorityQueue
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|nextId
specifier|private
specifier|final
name|AtomicLong
name|nextId
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|index
specifier|private
specifier|final
name|AtomicLong
name|index
init|=
operator|new
name|AtomicLong
argument_list|()
decl_stmt|;
DECL|field|lock
specifier|private
specifier|final
name|ReentrantLock
name|lock
decl_stmt|;
DECL|field|available
specifier|private
specifier|final
name|Condition
name|available
decl_stmt|;
DECL|method|AsyncCompletionService (Executor executor, boolean ordered)
specifier|public
name|AsyncCompletionService
parameter_list|(
name|Executor
name|executor
parameter_list|,
name|boolean
name|ordered
parameter_list|)
block|{
name|this
argument_list|(
name|executor
argument_list|,
name|ordered
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|AsyncCompletionService (Executor executor, boolean ordered, ReentrantLock lock)
specifier|public
name|AsyncCompletionService
parameter_list|(
name|Executor
name|executor
parameter_list|,
name|boolean
name|ordered
parameter_list|,
name|ReentrantLock
name|lock
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
name|this
operator|.
name|ordered
operator|=
name|ordered
expr_stmt|;
name|this
operator|.
name|lock
operator|=
name|lock
operator|!=
literal|null
condition|?
name|lock
else|:
operator|new
name|ReentrantLock
argument_list|()
expr_stmt|;
name|this
operator|.
name|available
operator|=
name|this
operator|.
name|lock
operator|.
name|newCondition
argument_list|()
expr_stmt|;
block|}
DECL|method|getLock ()
specifier|public
name|ReentrantLock
name|getLock
parameter_list|()
block|{
return|return
name|lock
return|;
block|}
DECL|method|submit (Consumer<Consumer<V>> runner)
specifier|public
name|void
name|submit
parameter_list|(
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|V
argument_list|>
argument_list|>
name|runner
parameter_list|)
block|{
name|Task
name|f
init|=
operator|new
name|Task
argument_list|(
name|nextId
operator|.
name|getAndIncrement
argument_list|()
argument_list|,
name|runner
argument_list|)
decl_stmt|;
name|this
operator|.
name|executor
operator|.
name|execute
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
DECL|method|skip ()
specifier|public
name|void
name|skip
parameter_list|()
block|{
name|index
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
DECL|method|pollUnordered ()
specifier|public
name|V
name|pollUnordered
parameter_list|()
block|{
specifier|final
name|ReentrantLock
name|lock
init|=
name|this
operator|.
name|lock
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|Task
name|t
init|=
name|queue
operator|.
name|poll
argument_list|()
decl_stmt|;
return|return
name|t
operator|!=
literal|null
condition|?
name|t
operator|.
name|result
else|:
literal|null
return|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|poll ()
specifier|public
name|V
name|poll
parameter_list|()
block|{
specifier|final
name|ReentrantLock
name|lock
init|=
name|this
operator|.
name|lock
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|Task
name|t
init|=
name|queue
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|ordered
operator|||
name|index
operator|.
name|compareAndSet
argument_list|(
name|t
operator|.
name|id
argument_list|,
name|t
operator|.
name|id
operator|+
literal|1
argument_list|)
operator|)
condition|)
block|{
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
return|return
name|t
operator|.
name|result
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|poll (long timeout, TimeUnit unit)
specifier|public
name|V
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
name|long
name|nanos
init|=
name|unit
operator|.
name|toNanos
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
specifier|final
name|ReentrantLock
name|lock
init|=
name|this
operator|.
name|lock
decl_stmt|;
name|lock
operator|.
name|lockInterruptibly
argument_list|()
expr_stmt|;
try|try
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|Task
name|t
init|=
name|queue
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|ordered
operator|||
name|index
operator|.
name|compareAndSet
argument_list|(
name|t
operator|.
name|id
argument_list|,
name|t
operator|.
name|id
operator|+
literal|1
argument_list|)
operator|)
condition|)
block|{
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
return|return
name|t
operator|.
name|result
return|;
block|}
if|if
condition|(
name|nanos
operator|<=
literal|0
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|nanos
operator|=
name|available
operator|.
name|awaitNanos
argument_list|(
name|nanos
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|take ()
specifier|public
name|V
name|take
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ReentrantLock
name|lock
init|=
name|this
operator|.
name|lock
decl_stmt|;
name|lock
operator|.
name|lockInterruptibly
argument_list|()
expr_stmt|;
try|try
block|{
for|for
control|(
init|;
condition|;
control|)
block|{
name|Task
name|t
init|=
name|queue
operator|.
name|peek
argument_list|()
decl_stmt|;
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
operator|(
operator|!
name|ordered
operator|||
name|index
operator|.
name|compareAndSet
argument_list|(
name|t
operator|.
name|id
argument_list|,
name|t
operator|.
name|id
operator|+
literal|1
argument_list|)
operator|)
condition|)
block|{
name|queue
operator|.
name|poll
argument_list|()
expr_stmt|;
return|return
name|t
operator|.
name|result
return|;
block|}
name|available
operator|.
name|await
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|complete (Task task)
specifier|private
name|void
name|complete
parameter_list|(
name|Task
name|task
parameter_list|)
block|{
specifier|final
name|ReentrantLock
name|lock
init|=
name|this
operator|.
name|lock
decl_stmt|;
name|lock
operator|.
name|lock
argument_list|()
expr_stmt|;
try|try
block|{
name|queue
operator|.
name|add
argument_list|(
name|task
argument_list|)
expr_stmt|;
name|available
operator|.
name|signalAll
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|lock
operator|.
name|unlock
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|Task
specifier|private
class|class
name|Task
implements|implements
name|Runnable
implements|,
name|Comparable
argument_list|<
name|Task
argument_list|>
block|{
DECL|field|id
specifier|private
specifier|final
name|long
name|id
decl_stmt|;
DECL|field|runner
specifier|private
specifier|final
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|V
argument_list|>
argument_list|>
name|runner
decl_stmt|;
DECL|field|result
specifier|private
name|V
name|result
decl_stmt|;
DECL|method|Task (long id, Consumer<Consumer<V>> runner)
name|Task
parameter_list|(
name|long
name|id
parameter_list|,
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|V
argument_list|>
argument_list|>
name|runner
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|runner
operator|=
name|runner
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|runner
operator|.
name|accept
argument_list|(
name|this
operator|::
name|setResult
argument_list|)
expr_stmt|;
block|}
DECL|method|setResult (V result)
specifier|protected
name|void
name|setResult
parameter_list|(
name|V
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|complete
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|compareTo (Task other)
specifier|public
name|int
name|compareTo
parameter_list|(
name|Task
name|other
parameter_list|)
block|{
return|return
name|Long
operator|.
name|compare
argument_list|(
name|this
operator|.
name|id
argument_list|,
name|other
operator|.
name|id
argument_list|)
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SubmitOrderedFutureTask["
operator|+
name|this
operator|.
name|id
operator|+
literal|"]"
return|;
block|}
block|}
block|}
end_class

end_unit

