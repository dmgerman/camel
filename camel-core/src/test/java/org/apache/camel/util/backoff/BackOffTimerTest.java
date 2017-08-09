begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.backoff
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|backoff
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
name|Executors
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
name|ScheduledExecutorService
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
name|AtomicBoolean
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BackOffTimerTest
specifier|public
class|class
name|BackOffTimerTest
block|{
annotation|@
name|Test
DECL|method|testBackOffTimer ()
specifier|public
name|void
name|testBackOffTimer
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|BackOff
name|backOff
init|=
name|BackOff
operator|.
name|builder
argument_list|()
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|BackOffTimer
name|timer
init|=
operator|new
name|BackOffTimer
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|BackOffTimer
operator|.
name|Task
name|task
init|=
name|timer
operator|.
name|schedule
argument_list|(
name|backOff
argument_list|,
name|context
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|counter
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentAttempts
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
operator|*
name|counter
operator|.
name|get
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentElapsedTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|counter
operator|.
name|get
argument_list|()
operator|<
literal|5
return|;
block|}
argument_list|)
decl_stmt|;
name|task
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|context
parameter_list|,
name|throwable
parameter_list|)
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|counter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBackOffTimerWithMaxAttempts ()
specifier|public
name|void
name|testBackOffTimerWithMaxAttempts
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|BackOff
name|backOff
init|=
name|BackOff
operator|.
name|builder
argument_list|()
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|maxAttempts
argument_list|(
literal|5L
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|BackOffTimer
name|timer
init|=
operator|new
name|BackOffTimer
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|BackOffTimer
operator|.
name|Task
name|task
init|=
name|timer
operator|.
name|schedule
argument_list|(
name|backOff
argument_list|,
name|context
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|counter
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentAttempts
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
operator|*
name|counter
operator|.
name|get
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentElapsedTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
argument_list|)
decl_stmt|;
name|task
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|context
parameter_list|,
name|throwable
parameter_list|)
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|counter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BackOffTimer
operator|.
name|Task
operator|.
name|Status
operator|.
name|Exhausted
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBackOffTimerWithMaxElapsedTime ()
specifier|public
name|void
name|testBackOffTimerWithMaxElapsedTime
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
specifier|final
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|BackOff
name|backOff
init|=
name|BackOff
operator|.
name|builder
argument_list|()
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|maxElapsedTime
argument_list|(
literal|400
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|BackOffTimer
name|timer
init|=
operator|new
name|BackOffTimer
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|BackOffTimer
operator|.
name|Task
name|task
init|=
name|timer
operator|.
name|schedule
argument_list|(
name|backOff
argument_list|,
name|context
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|counter
operator|.
name|incrementAndGet
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentAttempts
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|context
operator|.
name|getCurrentDelay
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|100
operator|*
name|counter
operator|.
name|get
argument_list|()
argument_list|,
name|context
operator|.
name|getCurrentElapsedTime
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
argument_list|)
decl_stmt|;
name|task
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|context
parameter_list|,
name|throwable
parameter_list|)
lambda|->
block|{
name|Assert
operator|.
name|assertTrue
argument_list|(
name|counter
operator|.
name|get
argument_list|()
operator|<=
literal|5
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BackOffTimer
operator|.
name|Task
operator|.
name|Status
operator|.
name|Exhausted
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testBackOffTimerStop ()
specifier|public
name|void
name|testBackOffTimerStop
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
specifier|final
name|AtomicBoolean
name|done
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
specifier|final
name|ScheduledExecutorService
name|executor
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|BackOff
name|backOff
init|=
name|BackOff
operator|.
name|builder
argument_list|()
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|BackOffTimer
name|timer
init|=
operator|new
name|BackOffTimer
argument_list|(
name|executor
argument_list|)
decl_stmt|;
name|BackOffTimer
operator|.
name|Task
name|task
init|=
name|timer
operator|.
name|schedule
argument_list|(
name|backOff
argument_list|,
name|context
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BackOffTimer
operator|.
name|Task
operator|.
name|Status
operator|.
name|Active
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
argument_list|)
decl_stmt|;
name|task
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|context
parameter_list|,
name|throwable
parameter_list|)
lambda|->
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
name|BackOffTimer
operator|.
name|Task
operator|.
name|Status
operator|.
name|Inactive
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|done
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|task
operator|.
name|cancel
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|done
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

