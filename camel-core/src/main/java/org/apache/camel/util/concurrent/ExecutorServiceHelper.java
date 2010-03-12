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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ExecutorServiceAwareDefinition
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
name|ExecutorServiceStrategy
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
name|RouteContext
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Helper for {@link java.util.concurrent.ExecutorService} to construct executors using a thread factory that  * create thread names with Camel prefix.  *<p/>  * This helper should<b>NOT</b> be used by end users of Camel, as you should use  * {@link org.apache.camel.spi.ExecutorServiceStrategy} which you obtain from {@link org.apache.camel.CamelContext}  * to create thread pools.  *<p/>  * This helper should only be used internally in Camel.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ExecutorServiceHelper
specifier|public
specifier|final
class|class
name|ExecutorServiceHelper
block|{
DECL|field|threadCounter
specifier|private
specifier|static
name|AtomicInteger
name|threadCounter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|method|ExecutorServiceHelper ()
specifier|private
name|ExecutorServiceHelper
parameter_list|()
block|{     }
DECL|method|nextThreadCounter ()
specifier|private
specifier|static
specifier|synchronized
name|int
name|nextThreadCounter
parameter_list|()
block|{
return|return
name|threadCounter
operator|.
name|getAndIncrement
argument_list|()
return|;
block|}
comment|/**      * Creates a new thread name with the given prefix      *      * @param pattern the pattern      * @param name the name      * @return the thread name, which is unique      */
DECL|method|getThreadName (String pattern, String name)
specifier|public
specifier|static
name|String
name|getThreadName
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|String
name|answer
init|=
name|pattern
operator|.
name|replaceFirst
argument_list|(
literal|"\\$\\{counter\\}"
argument_list|,
literal|""
operator|+
name|nextThreadCounter
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
name|answer
operator|.
name|replaceFirst
argument_list|(
literal|"\\$\\{name\\}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|.
name|indexOf
argument_list|(
literal|"$"
argument_list|)
operator|>
operator|-
literal|1
operator|||
name|answer
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|>
operator|-
literal|1
operator|||
name|answer
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|)
operator|>
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Pattern is invalid: "
operator|+
name|pattern
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Creates a new scheduled thread pool which can schedule threads.      *      * @param poolSize the core pool size      * @param name     part of the thread name      * @param daemon   whether the threads is daemon or not      * @return the created pool      */
DECL|method|newScheduledThreadPool (final int poolSize, final String name, final boolean daemon)
specifier|public
specifier|static
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
specifier|final
name|int
name|poolSize
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|boolean
name|daemon
parameter_list|)
block|{
return|return
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
name|poolSize
argument_list|,
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|newFixedThreadPool (final int poolSize, final String name, final boolean daemon)
specifier|public
specifier|static
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
specifier|final
name|int
name|poolSize
parameter_list|,
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|boolean
name|daemon
parameter_list|)
block|{
return|return
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|,
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|newSingleThreadExecutor (final String name, final boolean daemon)
specifier|public
specifier|static
name|ExecutorService
name|newSingleThreadExecutor
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|boolean
name|daemon
parameter_list|)
block|{
return|return
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|(
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Creates a new cached thread pool which should be the most commonly used.      *      * @param name    the full thread name      * @param daemon  whether the threads is daemon or not      * @return the created pool      */
DECL|method|newCachedThreadPool (final String name, final boolean daemon)
specifier|public
specifier|static
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
specifier|final
name|boolean
name|daemon
parameter_list|)
block|{
return|return
name|Executors
operator|.
name|newCachedThreadPool
argument_list|(
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
return|;
block|}
comment|/**      * Creates a new custom thread pool using 60 seconds as keep alive      *      * @param name          the full thread name      * @param corePoolSize  the core size      * @param maxPoolSize   the maximum pool size      * @return the created pool      */
DECL|method|newThreadPool (final String name, int corePoolSize, int maxPoolSize)
specifier|public
specifier|static
name|ExecutorService
name|newThreadPool
parameter_list|(
specifier|final
name|String
name|name
parameter_list|,
name|int
name|corePoolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
name|name
argument_list|,
name|corePoolSize
argument_list|,
name|maxPoolSize
argument_list|,
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * Creates a new custom thread pool      *      * @param name          the full thread name      * @param corePoolSize  the core size      * @param maxPoolSize   the maximum pool size      * @param keepAliveTime keep alive      * @param timeUnit      keep alive time unit      * @param daemon        whether the threads is daemon or not      * @return the created pool      * @throws IllegalArgumentException if parameters is not valid      */
DECL|method|newThreadPool (final String name, int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, final boolean daemon)
specifier|public
specifier|static
name|ExecutorService
name|newThreadPool
parameter_list|(
specifier|final
name|String
name|name
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
specifier|final
name|boolean
name|daemon
parameter_list|)
block|{
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
operator|new
name|LinkedBlockingQueue
argument_list|<
name|Runnable
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setThreadFactory
argument_list|(
operator|new
name|ThreadFactory
argument_list|()
block|{
specifier|public
name|Thread
name|newThread
parameter_list|(
name|Runnable
name|r
parameter_list|)
block|{
name|Thread
name|answer
init|=
operator|new
name|Thread
argument_list|(
name|r
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDaemon
argument_list|(
name|daemon
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Will lookup and get the configured {@link java.util.concurrent.ExecutorService} from the given definition.      *<p/>      * This method will lookup for configured thread pool in the following order      *<ul>      *<li>from the definition if any explicit configured executor service.</li>      *<li>if none found, then<tt>null</tt> is returned.</li>      *</ul>      * The various {@link ExecutorServiceAwareDefinition} should use this helper method to ensure they support      * configured executor services in the same coherent way.      *      * @param routeContext  the rout context      * @param definition    the node definition which may leverage executor service.      * @return the configured executor service, or<tt>null</tt> if none was configured.      * @throws IllegalArgumentException is thrown if lookup of executor service in {@link org.apache.camel.spi.Registry} was not found      */
DECL|method|getConfiguredExecutorService (RouteContext routeContext, ExecutorServiceAwareDefinition definition)
specifier|public
specifier|static
name|ExecutorService
name|getConfiguredExecutorService
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|ExecutorServiceAwareDefinition
name|definition
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
name|ExecutorServiceStrategy
name|strategy
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceStrategy
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|strategy
argument_list|,
literal|"ExecutorServiceStrategy"
argument_list|,
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
comment|// prefer to use explicit configured executor on the definition
if|if
condition|(
name|definition
operator|.
name|getExecutorService
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|definition
operator|.
name|getExecutorService
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|ExecutorService
name|answer
init|=
name|strategy
operator|.
name|lookup
argument_list|(
name|definition
argument_list|,
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExecutorServiceRef "
operator|+
name|definition
operator|.
name|getExecutorServiceRef
argument_list|()
operator|+
literal|" not found in registry."
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

