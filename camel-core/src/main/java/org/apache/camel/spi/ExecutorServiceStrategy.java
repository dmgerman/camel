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
comment|/**  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ExecutorServiceStrategy
specifier|public
interface|interface
name|ExecutorServiceStrategy
block|{
DECL|method|lookup (String executorServiceRef)
name|ExecutorService
name|lookup
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
function_decl|;
DECL|method|newCachedThreadPool (String name)
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|newScheduledThreadPool (String name, int poolSize)
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
function_decl|;
DECL|method|newFixedThreadPool (String name, int poolSize)
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
function_decl|;
DECL|method|newSingleThreadExecutor (String name)
name|ExecutorService
name|newSingleThreadExecutor
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
DECL|method|newThreadPool (String name, int corePoolSize, int maxPoolSize)
name|ExecutorService
name|newThreadPool
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|corePoolSize
parameter_list|,
name|int
name|maxPoolSize
parameter_list|)
function_decl|;
DECL|method|newThreadPool (final String name, int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, boolean daemon)
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
name|boolean
name|daemon
parameter_list|)
function_decl|;
DECL|method|shutdown (ExecutorService executorService)
name|void
name|shutdown
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
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

