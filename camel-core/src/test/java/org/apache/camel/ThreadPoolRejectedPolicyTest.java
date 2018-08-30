begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|RejectedExecutionHandler
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
name|util
operator|.
name|concurrent
operator|.
name|RejectableThreadPoolExecutor
import|;
end_import

begin_class
DECL|class|ThreadPoolRejectedPolicyTest
specifier|public
class|class
name|ThreadPoolRejectedPolicyTest
extends|extends
name|TestSupport
block|{
annotation|@
name|Test
DECL|method|testAbortAsRejectedExecutionHandler ()
specifier|public
name|void
name|testAbortAsRejectedExecutionHandler
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Abort
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockCallable
argument_list|<
name|String
argument_list|>
name|task1
init|=
operator|new
name|MockCallable
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task2
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockCallable
argument_list|<
name|String
argument_list|>
name|task3
init|=
operator|new
name|MockCallable
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Third task should have been rejected by a threadpool is full with 1 task and queue is full with 1 task."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{         }
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task3
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAbortAsRejectedExecutionHandlerWithRejectableTasks ()
specifier|public
name|void
name|testAbortAsRejectedExecutionHandlerWithRejectableTasks
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Abort
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task1
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableCallable
argument_list|<
name|String
argument_list|>
name|task2
init|=
operator|new
name|MockRejectableCallable
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task3
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableCallable
argument_list|<
name|String
argument_list|>
name|task4
init|=
operator|new
name|MockRejectableCallable
argument_list|<>
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result4
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task4
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task4
argument_list|,
name|result4
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallerRunsAsRejectedExecutionHandler ()
specifier|public
name|void
name|testCallerRunsAsRejectedExecutionHandler
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|CallerRuns
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task1
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task2
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task3
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCallerRunsAsRejectedExecutionHandlerWithRejectableTasks ()
specifier|public
name|void
name|testCallerRunsAsRejectedExecutionHandlerWithRejectableTasks
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|CallerRuns
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task1
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task2
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task3
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDiscardAsRejectedExecutionHandler ()
specifier|public
name|void
name|testDiscardAsRejectedExecutionHandler
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Discard
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task1
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task2
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task3
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDiscardAsRejectedExecutionHandlerWithRejectableTasks ()
specifier|public
name|void
name|testDiscardAsRejectedExecutionHandlerWithRejectableTasks
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|Discard
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task1
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task2
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task3
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDiscardOldestAsRejectedExecutionHandler ()
specifier|public
name|void
name|testDiscardOldestAsRejectedExecutionHandler
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|DiscardOldest
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task1
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task2
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRunnable
name|task3
init|=
operator|new
name|MockRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDiscardOldestAsRejectedExecutionHandlerWithRejectableTasks ()
specifier|public
name|void
name|testDiscardOldestAsRejectedExecutionHandlerWithRejectableTasks
parameter_list|()
throws|throws
name|InterruptedException
block|{
specifier|final
name|ExecutorService
name|executorService
init|=
name|createTestExecutorService
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|DiscardOldest
operator|.
name|asRejectedExecutionHandler
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task1
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result1
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task1
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task2
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result2
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task2
argument_list|)
decl_stmt|;
specifier|final
name|MockRejectableRunnable
name|task3
init|=
operator|new
name|MockRejectableRunnable
argument_list|()
decl_stmt|;
specifier|final
name|Future
argument_list|<
name|?
argument_list|>
name|result3
init|=
name|executorService
operator|.
name|submit
argument_list|(
name|task3
argument_list|)
decl_stmt|;
name|shutdownAndAwait
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task1
argument_list|,
name|result1
argument_list|)
expr_stmt|;
name|assertRejected
argument_list|(
name|task2
argument_list|,
name|result2
argument_list|)
expr_stmt|;
name|assertInvoked
argument_list|(
name|task3
argument_list|,
name|result3
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestExecutorService (final RejectedExecutionHandler rejectedExecutionHandler)
specifier|private
name|ExecutorService
name|createTestExecutorService
parameter_list|(
specifier|final
name|RejectedExecutionHandler
name|rejectedExecutionHandler
parameter_list|)
block|{
return|return
operator|new
name|RejectableThreadPoolExecutor
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|,
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
literal|1
argument_list|)
argument_list|,
name|rejectedExecutionHandler
argument_list|)
return|;
block|}
DECL|method|shutdownAndAwait (final ExecutorService executorService)
specifier|private
name|void
name|shutdownAndAwait
parameter_list|(
specifier|final
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
try|try
block|{
name|assertTrue
argument_list|(
literal|"Test ExecutorService shutdown is not expected to take longer than 10 seconds."
argument_list|,
name|executorService
operator|.
name|awaitTermination
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
name|fail
argument_list|(
literal|"Test ExecutorService shutdown is not expected to be interrupted."
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertInvoked (MockTask task, Future<?> result)
specifier|private
name|void
name|assertInvoked
parameter_list|(
name|MockTask
name|task
parameter_list|,
name|Future
argument_list|<
name|?
argument_list|>
name|result
parameter_list|)
block|{
name|assertTrue
argument_list|(
name|result
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|task
operator|.
name|getInvocationCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|Rejectable
condition|)
block|{
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|task
operator|.
name|getRejectionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|assertRejected (MockTask task, Future<?> result)
specifier|private
name|void
name|assertRejected
parameter_list|(
name|MockTask
name|task
parameter_list|,
name|Future
argument_list|<
name|?
argument_list|>
name|result
parameter_list|)
block|{
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
name|assertFalse
argument_list|(
name|result
operator|.
name|isDone
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|task
operator|.
name|getInvocationCount
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|task
operator|instanceof
name|Rejectable
condition|)
block|{
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|task
operator|.
name|getRejectionCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|MockTask
specifier|private
specifier|abstract
specifier|static
class|class
name|MockTask
block|{
DECL|field|invocationCount
specifier|private
specifier|final
name|AtomicInteger
name|invocationCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|rejectionCount
specifier|private
specifier|final
name|AtomicInteger
name|rejectionCount
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|getInvocationCount ()
specifier|public
name|int
name|getInvocationCount
parameter_list|()
block|{
return|return
name|invocationCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|countInvocation ()
specifier|protected
name|void
name|countInvocation
parameter_list|()
block|{
name|invocationCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
DECL|method|getRejectionCount ()
specifier|public
name|int
name|getRejectionCount
parameter_list|()
block|{
return|return
name|rejectionCount
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|countRejection ()
specifier|protected
name|void
name|countRejection
parameter_list|()
block|{
name|rejectionCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|MockRunnable
specifier|private
specifier|static
class|class
name|MockRunnable
extends|extends
name|MockTask
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|countInvocation
argument_list|()
expr_stmt|;
try|try
block|{
name|TimeUnit
operator|.
name|MILLISECONDS
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
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"MockRunnable task is not expected to be interrupted."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|class|MockRejectableRunnable
specifier|private
specifier|static
class|class
name|MockRejectableRunnable
extends|extends
name|MockRunnable
implements|implements
name|Rejectable
block|{
annotation|@
name|Override
DECL|method|reject ()
specifier|public
name|void
name|reject
parameter_list|()
block|{
name|countRejection
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|MockCallable
specifier|private
specifier|static
class|class
name|MockCallable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|MockTask
implements|implements
name|Callable
argument_list|<
name|T
argument_list|>
block|{
annotation|@
name|Override
DECL|method|call ()
specifier|public
name|T
name|call
parameter_list|()
throws|throws
name|Exception
block|{
name|countInvocation
argument_list|()
expr_stmt|;
try|try
block|{
name|TimeUnit
operator|.
name|MILLISECONDS
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
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"MockCallable task is not expected to be interrupted."
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
DECL|class|MockRejectableCallable
specifier|private
specifier|static
class|class
name|MockRejectableCallable
parameter_list|<
name|T
parameter_list|>
extends|extends
name|MockCallable
argument_list|<
name|T
argument_list|>
implements|implements
name|Rejectable
block|{
annotation|@
name|Override
DECL|method|reject ()
specifier|public
name|void
name|reject
parameter_list|()
block|{
name|countRejection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

