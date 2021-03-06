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
name|TimeUnit
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Before
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
DECL|class|AsyncCompletionServiceTest
specifier|public
class|class
name|AsyncCompletionServiceTest
extends|extends
name|Assert
block|{
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|service
specifier|private
name|AsyncCompletionService
argument_list|<
name|Object
argument_list|>
name|service
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|executor
operator|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|service
operator|=
operator|new
name|AsyncCompletionService
argument_list|<>
argument_list|(
name|executor
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrdered ()
specifier|public
name|void
name|testSubmitOrdered
parameter_list|()
throws|throws
name|Exception
block|{
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedFirstTaskIsSlow ()
specifier|public
name|void
name|testSubmitOrderedFirstTaskIsSlow
parameter_list|()
throws|throws
name|Exception
block|{
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|,
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|300
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedFirstTaskIsSlowUsingPollTimeout ()
specifier|public
name|void
name|testSubmitOrderedFirstTaskIsSlowUsingPollTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|,
literal|200
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|service
operator|.
name|poll
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|poll
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedFirstTaskIsSlowUsingPoll ()
specifier|public
name|void
name|testSubmitOrderedFirstTaskIsSlowUsingPoll
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
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|,
name|latch
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|)
argument_list|)
expr_stmt|;
comment|// poll should not get it the first time
name|Object
name|a
init|=
name|service
operator|.
name|poll
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// and neither the 2nd time
name|a
operator|=
name|service
operator|.
name|poll
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// okay complete task
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// okay take them
name|a
operator|=
name|service
operator|.
name|take
argument_list|()
expr_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedSecondTaskIsSlow ()
specifier|public
name|void
name|testSubmitOrderedSecondTaskIsSlow
parameter_list|()
throws|throws
name|Exception
block|{
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedSecondTaskIsSlowUsingPollTimeout ()
specifier|public
name|void
name|testSubmitOrderedSecondTaskIsSlowUsingPollTimeout
parameter_list|()
throws|throws
name|Exception
block|{
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|,
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|a
init|=
name|service
operator|.
name|poll
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|Object
name|b
init|=
name|service
operator|.
name|poll
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSubmitOrderedLastTaskIsSlowUsingPoll ()
specifier|public
name|void
name|testSubmitOrderedLastTaskIsSlowUsingPoll
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
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|service
operator|.
name|submit
argument_list|(
name|result
argument_list|(
literal|"B"
argument_list|,
name|latch
argument_list|,
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
comment|// take a
name|Object
name|a
init|=
name|service
operator|.
name|take
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|a
argument_list|)
expr_stmt|;
comment|// poll should not get it the first time
name|Object
name|b
init|=
name|service
operator|.
name|poll
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
comment|// and neither the 2nd time
name|b
operator|=
name|service
operator|.
name|poll
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
name|b
argument_list|)
expr_stmt|;
comment|// okay complete task
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// okay take it
name|b
operator|=
name|service
operator|.
name|take
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|a
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|b
argument_list|)
expr_stmt|;
block|}
DECL|method|result (Object r)
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|Object
argument_list|>
argument_list|>
name|result
parameter_list|(
name|Object
name|r
parameter_list|)
block|{
return|return
name|result
lambda|->
name|result
operator|.
name|accept
argument_list|(
name|r
argument_list|)
return|;
block|}
DECL|method|result (Object r, int delay)
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|Object
argument_list|>
argument_list|>
name|result
parameter_list|(
name|Object
name|r
parameter_list|,
name|int
name|delay
parameter_list|)
block|{
return|return
name|result
lambda|->
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|result
operator|.
name|accept
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
return|;
block|}
DECL|method|result (Object r, CountDownLatch latch, int timeout, TimeUnit unit)
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|Object
argument_list|>
argument_list|>
name|result
parameter_list|(
name|Object
name|r
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|,
name|int
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
block|{
return|return
name|result
lambda|->
block|{
try|try
block|{
name|latch
operator|.
name|await
argument_list|(
name|timeout
argument_list|,
name|unit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
name|result
operator|.
name|accept
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
return|;
block|}
block|}
end_class

end_unit

