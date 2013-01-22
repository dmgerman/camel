begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.config
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|config
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
name|ThreadFactory
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
name|apache
operator|.
name|camel
operator|.
name|CamelContext
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
name|DefaultThreadPoolFactory
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
name|spring
operator|.
name|SpringRunWithTestSupport
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CustomThreadPoolFactoryTest
specifier|public
class|class
name|CustomThreadPoolFactoryTest
extends|extends
name|SpringRunWithTestSupport
block|{
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
annotation|@
name|Test
DECL|method|testCustomThreadPoolFactory ()
specifier|public
name|void
name|testCustomThreadPoolFactory
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|MyCustomThreadPoolFactory
name|factory
init|=
name|assertIsInstanceOf
argument_list|(
name|MyCustomThreadPoolFactory
operator|.
name|class
argument_list|,
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getThreadPoolFactory
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should use custom thread pool factory"
argument_list|,
name|factory
operator|.
name|isInvoked
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|MyCustomThreadPoolFactory
specifier|public
specifier|static
class|class
name|MyCustomThreadPoolFactory
extends|extends
name|DefaultThreadPoolFactory
block|{
DECL|field|invoked
specifier|private
specifier|volatile
name|boolean
name|invoked
decl_stmt|;
annotation|@
name|Override
DECL|method|newCachedThreadPool (ThreadFactory threadFactory)
specifier|public
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|newCachedThreadPool
argument_list|(
name|threadFactory
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newThreadPool (int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int maxQueueSize, RejectedExecutionHandler rejectedExecutionHandler, ThreadFactory threadFactory)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|,
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|int
name|maxQueueSize
parameter_list|,
name|RejectedExecutionHandler
name|rejectedExecutionHandler
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|invoked
operator|=
literal|true
expr_stmt|;
return|return
name|super
operator|.
name|newThreadPool
argument_list|(
name|corePoolSize
argument_list|,
name|maxPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|timeUnit
argument_list|,
name|maxQueueSize
argument_list|,
name|rejectedExecutionHandler
argument_list|,
name|threadFactory
argument_list|)
return|;
block|}
DECL|method|isInvoked ()
specifier|public
name|boolean
name|isInvoked
parameter_list|()
block|{
return|return
name|invoked
return|;
block|}
block|}
block|}
end_class

end_unit

