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
name|ContextTestSupport
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|CustomThreadPoolFactoryTest
specifier|public
class|class
name|CustomThreadPoolFactoryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|factory
specifier|private
name|MyCustomThreadPoolFactory
name|factory
init|=
operator|new
name|MyCustomThreadPoolFactory
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|setThreadPoolFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|testCustomThreadPoolFactory ()
specifier|public
name|void
name|testCustomThreadPoolFactory
parameter_list|()
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
specifier|final
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
DECL|method|newThreadPool (int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int maxQueueSize, boolean allowCoreThreadTimeOut, RejectedExecutionHandler rejectedExecutionHandler, ThreadFactory threadFactory)
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
name|boolean
name|allowCoreThreadTimeOut
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
name|allowCoreThreadTimeOut
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

