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
name|BlockingQueue
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
name|SynchronousQueue
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
name|ThreadPoolExecutor
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
name|spi
operator|.
name|ThreadPoolFactory
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
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * Factory for thread pools that uses the JDK methods for handling thread pools  */
end_comment

begin_class
DECL|class|DefaultThreadPoolFactory
specifier|public
class|class
name|DefaultThreadPoolFactory
implements|implements
name|ThreadPoolFactory
block|{
annotation|@
name|Override
DECL|method|newThreadPool (ThreadPoolProfile profile, ThreadFactory factory)
specifier|public
name|ExecutorService
name|newThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|factory
parameter_list|)
block|{
return|return
name|newThreadPool
argument_list|(
name|profile
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|profile
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|,
name|profile
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|,
name|profile
operator|.
name|getTimeUnit
argument_list|()
argument_list|,
name|profile
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|,
name|profile
operator|.
name|getRejectedExecutionHandler
argument_list|()
argument_list|,
name|factory
argument_list|)
return|;
block|}
comment|/**      * Creates a new custom thread pool      *      * @param pattern                  pattern of the thread name      * @param name                     ${name} in the pattern name      * @param corePoolSize             the core pool size      * @param maxPoolSize              the maximum pool size      * @param keepAliveTime            keep alive time      * @param timeUnit                 keep alive time unit      * @param maxQueueSize             the maximum number of tasks in the queue, use<tt>Integer.MAX_VALUE</tt> or<tt>-1</tt> to indicate unbounded      * @param rejectedExecutionHandler the handler for tasks which cannot be executed by the thread pool.      *                                 If<tt>null</tt> is provided then {@link java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy CallerRunsPolicy} is used.      * @param daemon                   whether the threads is daemon or not      * @return the created pool      * @throws IllegalArgumentException if parameters is not valid      */
DECL|method|newThreadPool (int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int maxQueueSize, RejectedExecutionHandler rejectedExecutionHandler, final ThreadFactory factory)
specifier|private
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
specifier|final
name|ThreadFactory
name|factory
parameter_list|)
block|{
comment|// If we set the corePoolSize to be 0, the whole camel application will hang in JDK5
comment|// just add a check here to throw the IllegalArgumentException
if|if
condition|(
name|corePoolSize
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The corePoolSize can't be lower than 1"
argument_list|)
throw|;
block|}
comment|// validate max>= core
if|if
condition|(
name|maxPoolSize
operator|<
name|corePoolSize
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"MaxPoolSize must be>= corePoolSize, was "
operator|+
name|maxPoolSize
operator|+
literal|">= "
operator|+
name|corePoolSize
argument_list|)
throw|;
block|}
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|queue
decl_stmt|;
if|if
condition|(
name|corePoolSize
operator|==
literal|0
operator|&&
name|maxQueueSize
operator|<=
literal|0
condition|)
block|{
comment|// use a synchronous queue
name|queue
operator|=
operator|new
name|SynchronousQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|()
expr_stmt|;
comment|// and force 1 as pool size to be able to create the thread pool by the JDK
name|corePoolSize
operator|=
literal|1
expr_stmt|;
name|maxPoolSize
operator|=
literal|1
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|maxQueueSize
operator|<=
literal|0
condition|)
block|{
comment|// unbounded task queue
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
comment|// bounded task queue
name|queue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|(
name|maxQueueSize
argument_list|)
expr_stmt|;
block|}
name|ThreadPoolExecutor
name|answer
init|=
operator|new
name|ThreadPoolExecutor
argument_list|(
name|corePoolSize
argument_list|,
name|maxPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|timeUnit
argument_list|,
name|queue
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setThreadFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
if|if
condition|(
name|rejectedExecutionHandler
operator|==
literal|null
condition|)
block|{
name|rejectedExecutionHandler
operator|=
operator|new
name|ThreadPoolExecutor
operator|.
name|CallerRunsPolicy
argument_list|()
expr_stmt|;
block|}
name|answer
operator|.
name|setRejectedExecutionHandler
argument_list|(
name|rejectedExecutionHandler
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.impl.ThreadPoolFactory#newScheduledThreadPool(java.lang.Integer, java.util.concurrent.ThreadFactory)      */
annotation|@
name|Override
DECL|method|newScheduledThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
name|profile
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|threadFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

