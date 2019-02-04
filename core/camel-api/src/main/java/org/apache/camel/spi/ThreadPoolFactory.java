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

begin_comment
comment|/**  * Creates ExecutorService and ScheduledExecutorService objects that work with a thread pool for a given ThreadPoolProfile and ThreadFactory.  *   * This interface allows to customize the creation of these objects to adapt camel for application servers and other environments where thread pools  * should not be created with the jdk methods  */
end_comment

begin_interface
DECL|interface|ThreadPoolFactory
specifier|public
interface|interface
name|ThreadPoolFactory
block|{
comment|/**      * Creates a new cached thread pool      *<p/>      * The cached thread pool is a term from the JDK from the method {@link java.util.concurrent.Executors#newCachedThreadPool()}.      * Typically it will have no size limit (this is why it is handled separately      *      * @param threadFactory factory for creating threads      * @return the created thread pool      */
DECL|method|newCachedThreadPool (ThreadFactory threadFactory)
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
comment|/**      * Create a thread pool using the given thread pool profile      *       * @param profile parameters of the thread pool      * @param threadFactory factory for creating threads      * @return the created thread pool      */
DECL|method|newThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
name|ExecutorService
name|newThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
comment|/**      * Create a scheduled thread pool using the given thread pool profile      * @param profile parameters of the thread pool      * @param threadFactory factory for creating threads      * @return the created thread pool      */
DECL|method|newScheduledThreadPool (ThreadPoolProfile profile, ThreadFactory threadFactory)
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|ThreadPoolProfile
name|profile
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

