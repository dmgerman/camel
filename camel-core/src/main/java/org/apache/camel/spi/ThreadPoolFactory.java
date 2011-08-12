begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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

begin_comment
comment|/**  * Factory to crate {@link ExecutorService} and {@link ScheduledExecutorService} instances  *<p/>  * This interface allows to customize the creation of these objects to adapt Camel  * for application servers and other environments where thread pools should  * not be created with the JDK methods, as provided by the {@link org.apache.camel.impl.DefaultThreadPoolFactory}.  *  * @see ExecutorServiceManager  */
end_comment

begin_interface
DECL|interface|ThreadPoolFactory
specifier|public
interface|interface
name|ThreadPoolFactory
block|{
comment|/**      * Creates a new cached thread pool      *<p/>      * The cached thread pool is a term from the JDK from the method {@link java.util.concurrent.Executors#newCachedThreadPool()}.      * Implementators of this interface, may create a different kind of pool than the cached, or check the source code      * of the JDK to create a pool using the same settings.      *      * @param threadFactory factory for creating threads      * @return the created thread pool      */
DECL|method|newCachedThreadPool (ThreadFactory threadFactory)
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
comment|/**      * Creates a new fixed thread pool      *<p/>      * The fixed thread pool is a term from the JDK from the method {@link java.util.concurrent.Executors#newFixedThreadPool(int)}.      * Implementators of this interface, may create a different kind of pool than the fixed, or check the source code      * of the JDK to create a pool using the same settings.      *      * @param poolSize  the number of threads in the pool      * @param threadFactory factory for creating threads      * @return the created thread pool      */
DECL|method|newFixedThreadPool (int poolSize, ThreadFactory threadFactory)
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
name|int
name|poolSize
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
comment|/**      * Creates a new scheduled thread pool      *      * @param corePoolSize  the core pool size      * @param threadFactory factory for creating threads      * @return the created thread pool      * @throws IllegalArgumentException if parameters is not valid      */
DECL|method|newScheduledThreadPool (int corePoolSize, ThreadFactory threadFactory)
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|int
name|corePoolSize
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
comment|/**      * Creates a new thread pool      *      * @param corePoolSize             the core pool size      * @param maxPoolSize              the maximum pool size      * @param keepAliveTime            keep alive time      * @param timeUnit                 keep alive time unit      * @param maxQueueSize             the maximum number of tasks in the queue, use<tt>Integer.MAX_VALUE</tt> or<tt>-1</tt> to indicate unbounded      * @param rejectedExecutionHandler the handler for tasks which cannot be executed by the thread pool.      *                                 If<tt>null</tt> is provided then {@link java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy CallerRunsPolicy} is used.      * @param threadFactory            factory for creating threads      * @return the created thread pool      * @throws IllegalArgumentException if parameters is not valid      */
DECL|method|newThreadPool (int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, int maxQueueSize, RejectedExecutionHandler rejectedExecutionHandler, ThreadFactory threadFactory)
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
function_decl|;
block|}
end_interface

end_unit

