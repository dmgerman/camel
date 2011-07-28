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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ShutdownableService
import|;
end_import

begin_comment
comment|/**  * Strategy to create thread pools.  *<p/>  * This strategy is pluggable so you can plugin a custom provider, for example if you want to leverage  * the WorkManager for a J2EE server.  *<p/>  * This strategy has fine grained methods for creating various thread pools, however custom strategies  * do not have to exactly create those kind of pools. Feel free to return a shared or different kind of pool.  *<p/>  * However there are two types of pools: regular and scheduled.  *<p/>  * If you use the<tt>newXXX</tt> methods to create thread pools, then Camel will by default take care of  * shutting down those created pools when {@link org.apache.camel.CamelContext} is shutting down.  *  * @version   */
end_comment

begin_interface
DECL|interface|ExecutorServiceManager
specifier|public
interface|interface
name|ExecutorServiceManager
extends|extends
name|ShutdownableService
block|{
DECL|method|resolveThreadName (String name)
name|String
name|resolveThreadName
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|getThreadPoolProfile (String id)
name|ThreadPoolProfile
name|getThreadPoolProfile
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Registers the given thread pool profile      *      * @param profile the profile      */
DECL|method|registerThreadPoolProfile (ThreadPoolProfile profile)
name|void
name|registerThreadPoolProfile
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|)
function_decl|;
comment|/**      * Sets the default thread pool profile      *      * @param defaultThreadPoolProfile the new default thread pool profile      */
DECL|method|setDefaultThreadPoolProfile (ThreadPoolProfile defaultThreadPoolProfile)
name|void
name|setDefaultThreadPoolProfile
parameter_list|(
name|ThreadPoolProfile
name|defaultThreadPoolProfile
parameter_list|)
function_decl|;
DECL|method|getDefaultThreadPoolProfile ()
name|ThreadPoolProfile
name|getDefaultThreadPoolProfile
parameter_list|()
function_decl|;
comment|/**      * Sets the thread name pattern used for creating the full thread name.      *<p/>      * The default pattern is:<tt>Camel (${camelId}) thread #${counter} - ${name}</tt>      *<p/>      * Where<tt>${camelId}</tt> is the name of the {@link org.apache.camel.CamelContext}      *<br/>and<tt>${counter}</tt> is a unique incrementing counter.      *<br/>and<tt>${name}</tt> is the regular thread name.      *<br/>You can also use<tt>${longName}</tt> is the long thread name which can includes endpoint parameters etc.      *      * @param pattern  the pattern      * @throws IllegalArgumentException if the pattern is invalid.      */
DECL|method|setThreadNamePattern (String pattern)
name|void
name|setThreadNamePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
comment|/**      * Creates an executorservice with a default thread pool      *       * @param ref      * @param source      * @return      */
DECL|method|getDefaultExecutorService (String ref, Object source)
name|ExecutorService
name|getDefaultExecutorService
parameter_list|(
name|String
name|ref
parameter_list|,
name|Object
name|source
parameter_list|)
function_decl|;
DECL|method|getExecutorService (ThreadPoolProfile profile, Object source)
name|ExecutorService
name|getExecutorService
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|Object
name|source
parameter_list|)
function_decl|;
DECL|method|createExecutorService (ThreadPoolProfile profile, Object source)
name|ExecutorService
name|createExecutorService
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|Object
name|source
parameter_list|)
function_decl|;
DECL|method|getScheduledExecutorService (String ref, Object source)
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|(
name|String
name|ref
parameter_list|,
name|Object
name|source
parameter_list|)
function_decl|;
DECL|method|getScheduledExecutorService (ThreadPoolProfile profile, Object source)
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|Object
name|source
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
DECL|method|newCachedThreadPool (Object source, String name)
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|newSynchronousExecutorService (String string, Object source)
name|ExecutorService
name|newSynchronousExecutorService
parameter_list|(
name|String
name|string
parameter_list|,
name|Object
name|source
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

