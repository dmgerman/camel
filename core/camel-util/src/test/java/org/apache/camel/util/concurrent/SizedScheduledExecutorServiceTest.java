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
name|ScheduledThreadPoolExecutor
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SizedScheduledExecutorServiceTest
specifier|public
class|class
name|SizedScheduledExecutorServiceTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testSizedScheduledExecutorService ()
specifier|public
name|void
name|testSizedScheduledExecutorService
parameter_list|()
throws|throws
name|Exception
block|{
name|ScheduledThreadPoolExecutor
name|delegate
init|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|5
argument_list|)
decl_stmt|;
name|SizedScheduledExecutorService
name|sized
init|=
operator|new
name|SizedScheduledExecutorService
argument_list|(
name|delegate
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// noop
block|}
block|}
decl_stmt|;
name|sized
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|sized
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
literal|3
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
try|try
block|{
name|sized
operator|.
name|schedule
argument_list|(
name|task
argument_list|,
literal|4
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RejectedExecutionException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Task rejected due queue size limit reached"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sized
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be shutdown"
argument_list|,
name|sized
operator|.
name|isShutdown
argument_list|()
operator|||
name|sized
operator|.
name|isTerminating
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be shutdown"
argument_list|,
name|delegate
operator|.
name|isShutdown
argument_list|()
operator|||
name|sized
operator|.
name|isTerminating
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

