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
name|List
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

begin_comment
comment|/**  * Strategy to create thread pools.  *<p/>  * This strategy is pluggable so you can plugin a custom provider, for example if you want to leverage  * the WorkManager for a J2EE server.  *<p/>  * This strategy has fine grained methods for creating various thread pools, however custom strategies  * do not have to exactly create those kind of pools. Feel free to return a shared or different kind of pool.  *<p/>  * However there are two types of pools: regular and scheduled.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ExecutorServiceStrategy
specifier|public
interface|interface
name|ExecutorServiceStrategy
block|{
comment|/**      * Creates a full thread name      *      * @param nameSuffix  suffix which is appended to the thread name      * @return the full thread name      */
DECL|method|getThreadName (String nameSuffix)
name|String
name|getThreadName
parameter_list|(
name|String
name|nameSuffix
parameter_list|)
function_decl|;
comment|/**      * Lookup a {@link java.util.concurrent.ExecutorService} from the {@link org.apache.camel.spi.Registry}.      *      * @param executorServiceRef  reference to lookup      * @return the {@link java.util.concurrent.ExecutorService} or<tt>null</tt> if not found      */
DECL|method|lookup (String executorServiceRef)
name|ExecutorService
name|lookup
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
function_decl|;
comment|/**      * Creates a new cached thread pool.      *      * @param nameSuffix  suffix which is appended to the thread name      * @return the thread pool      */
DECL|method|newCachedThreadPool (String nameSuffix)
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|String
name|nameSuffix
parameter_list|)
function_decl|;
comment|/**      * Creates a new scheduled thread pool.      *      * @param nameSuffix  suffix which is appended to the thread name      * @param poolSize    the core pool size      * @return the thread pool      */
DECL|method|newScheduledThreadPool (String nameSuffix, int poolSize)
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|String
name|nameSuffix
parameter_list|,
name|int
name|poolSize
parameter_list|)
function_decl|;
comment|/**      * Creates a new fixed thread pool.      *      * @param nameSuffix  suffix which is appended to the thread name      * @param poolSize    the core pool size      * @return the thread pool      */
DECL|method|newFixedThreadPool (String nameSuffix, int poolSize)
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
name|String
name|nameSuffix
parameter_list|,
name|int
name|poolSize
parameter_list|)
function_decl|;
comment|/**      * Creates a new single-threaded thread pool. This is often used for background threads.      *      * @param nameSuffix  suffix which is appended to the thread name      * @return the thread pool      */
DECL|method|newSingleThreadExecutor (String nameSuffix)
name|ExecutorService
name|newSingleThreadExecutor
parameter_list|(
name|String
name|nameSuffix
parameter_list|)
function_decl|;
comment|/**      * Creates a new custom thread pool.      *<p/>      * Will by default use 60 seconds for keep alive time for idle threads.      *      * @param nameSuffix    suffix which is appended to the thread name      * @param corePoolSize  the core pool size      * @param maxPoolSize   the maximum pool size      * @return the thread pool      */
DECL|method|newThreadPool (String nameSuffix, int corePoolSize, int maxPoolSize)
name|ExecutorService
name|newThreadPool
parameter_list|(
name|String
name|nameSuffix
parameter_list|,
name|int
name|corePoolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|)
function_decl|;
comment|/**      * Creates a new custom thread pool.      *      * @param nameSuffix    suffix which is appended to the thread name      * @param corePoolSize  the core pool size      * @param maxPoolSize   the maximum pool size      * @param keepAliveTime keep alive time for idle threads      * @param timeUnit      time unit for keep alive time      * @param daemon        whether or not the created threads is daemon or not      * @return the thread pool      */
DECL|method|newThreadPool (final String nameSuffix, int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, boolean daemon)
name|ExecutorService
name|newThreadPool
parameter_list|(
specifier|final
name|String
name|nameSuffix
parameter_list|,
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
name|boolean
name|daemon
parameter_list|)
function_decl|;
comment|/**      * Shutdown the given executor service.      *      * @param executorService the executor service to shutdown      * @see java.util.concurrent.ExecutorService#shutdown()      */
DECL|method|shutdown (ExecutorService executorService)
name|void
name|shutdown
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
comment|/**      * Shutdown now the given executor service.      *      * @param executorService the executor service to shutdown now      * @return list of tasks that never commenced execution      * @see java.util.concurrent.ExecutorService#shutdownNow()      */
DECL|method|shutdownNow (ExecutorService executorService)
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

