begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ExecutorService
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SynchronousExecutorServiceTest
specifier|public
class|class
name|SynchronousExecutorServiceTest
extends|extends
name|TestCase
block|{
DECL|field|invoked
specifier|private
specifier|static
name|boolean
name|invoked
decl_stmt|;
DECL|field|name1
specifier|private
specifier|static
name|String
name|name1
decl_stmt|;
DECL|field|name2
specifier|private
specifier|static
name|String
name|name2
decl_stmt|;
DECL|method|testSynchronousExecutorService ()
specifier|public
name|void
name|testSynchronousExecutorService
parameter_list|()
throws|throws
name|Exception
block|{
name|name1
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
name|ExecutorService
name|service
init|=
operator|new
name|SynchronousExecutorService
argument_list|()
decl_stmt|;
name|service
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
name|name2
operator|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have been invoked"
argument_list|,
name|invoked
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should use same thread"
argument_list|,
name|name1
argument_list|,
name|name2
argument_list|)
expr_stmt|;
block|}
DECL|method|testSynchronousExecutorServiceShutdown ()
specifier|public
name|void
name|testSynchronousExecutorServiceShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecutorService
name|service
init|=
operator|new
name|SynchronousExecutorService
argument_list|()
decl_stmt|;
name|service
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|service
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|isTerminated
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

