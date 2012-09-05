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
name|RunnableFuture
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

begin_comment
comment|/**  * Thread pool executor that creates {@link RejectableFutureTask} instead of  * {@link java.util.concurrent.FutureTask} when registering new tasks for execution.  *<p/>  * Instances of {@link RejectableFutureTask} are required to handle {@link org.apache.camel.ThreadPoolRejectedPolicy#Discard}  * and {@link org.apache.camel.ThreadPoolRejectedPolicy#DiscardOldest} policies correctly, e.g. notify  * {@link Callable} and {@link Runnable} tasks when they are rejected.  * To be notified of rejection tasks have to implement {@link org.apache.camel.Rejectable} interface:<br/>  *<code><pre>  * public class RejectableTask implements Runnable, Rejectable {  *&#064;Override  *     public void run() {  *         // execute task  *     }  *&#064;Override  *     public void reject() {  *         // do something useful on rejection  *     }  * }  *</pre></code>  *<p/>  * If the task does not implement {@link org.apache.camel.Rejectable} interface the behavior is exactly the same as with  * ordinary {@link ThreadPoolExecutor}.  *  * @see RejectableFutureTask  * @see org.apache.camel.Rejectable  * @see RejectableScheduledThreadPoolExecutor  */
end_comment

begin_class
DECL|class|RejectableThreadPoolExecutor
specifier|public
class|class
name|RejectableThreadPoolExecutor
extends|extends
name|ThreadPoolExecutor
block|{
DECL|method|RejectableThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue)
specifier|public
name|RejectableThreadPoolExecutor
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|int
name|maximumPoolSize
parameter_list|,
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|workQueue
parameter_list|)
block|{
name|super
argument_list|(
name|corePoolSize
argument_list|,
name|maximumPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|unit
argument_list|,
name|workQueue
argument_list|)
expr_stmt|;
block|}
DECL|method|RejectableThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory)
specifier|public
name|RejectableThreadPoolExecutor
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|int
name|maximumPoolSize
parameter_list|,
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|workQueue
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
name|super
argument_list|(
name|corePoolSize
argument_list|,
name|maximumPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|unit
argument_list|,
name|workQueue
argument_list|,
name|threadFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|RejectableThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler)
specifier|public
name|RejectableThreadPoolExecutor
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|int
name|maximumPoolSize
parameter_list|,
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|workQueue
parameter_list|,
name|RejectedExecutionHandler
name|handler
parameter_list|)
block|{
name|super
argument_list|(
name|corePoolSize
argument_list|,
name|maximumPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|unit
argument_list|,
name|workQueue
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
DECL|method|RejectableThreadPoolExecutor (int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler)
specifier|public
name|RejectableThreadPoolExecutor
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|int
name|maximumPoolSize
parameter_list|,
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|unit
parameter_list|,
name|BlockingQueue
argument_list|<
name|Runnable
argument_list|>
name|workQueue
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|,
name|RejectedExecutionHandler
name|handler
parameter_list|)
block|{
name|super
argument_list|(
name|corePoolSize
argument_list|,
name|maximumPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|unit
argument_list|,
name|workQueue
argument_list|,
name|threadFactory
argument_list|,
name|handler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|newTaskFor (Runnable runnable, T value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|RunnableFuture
argument_list|<
name|T
argument_list|>
name|newTaskFor
parameter_list|(
name|Runnable
name|runnable
parameter_list|,
name|T
name|value
parameter_list|)
block|{
return|return
operator|new
name|RejectableFutureTask
argument_list|<
name|T
argument_list|>
argument_list|(
name|runnable
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|newTaskFor (Callable<T> callable)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|RunnableFuture
argument_list|<
name|T
argument_list|>
name|newTaskFor
parameter_list|(
name|Callable
argument_list|<
name|T
argument_list|>
name|callable
parameter_list|)
block|{
return|return
operator|new
name|RejectableFutureTask
argument_list|<
name|T
argument_list|>
argument_list|(
name|callable
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
comment|// the thread factory often have more precise details what the thread pool is used for
if|if
condition|(
name|getThreadFactory
argument_list|()
operator|instanceof
name|CamelThreadFactory
condition|)
block|{
name|String
name|name
init|=
operator|(
operator|(
name|CamelThreadFactory
operator|)
name|getThreadFactory
argument_list|()
operator|)
operator|.
name|getName
argument_list|()
decl_stmt|;
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|"["
operator|+
name|name
operator|+
literal|"]"
return|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

