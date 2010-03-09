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
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultExecutorServiceStrategy
specifier|public
class|class
name|DefaultExecutorServiceStrategy
implements|implements
name|ExecutorServiceStrategy
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|threadNamePattern
specifier|private
name|String
name|threadNamePattern
init|=
literal|"Camel Thread ${counter} - ${name}"
decl_stmt|;
DECL|method|DefaultExecutorServiceStrategy (CamelContext camelContext)
specifier|public
name|DefaultExecutorServiceStrategy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getThreadName (String name)
specifier|public
name|String
name|getThreadName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|getThreadName
argument_list|(
name|threadNamePattern
argument_list|,
name|name
argument_list|)
return|;
block|}
DECL|method|getThreadNamePattern ()
specifier|public
name|String
name|getThreadNamePattern
parameter_list|()
block|{
return|return
name|threadNamePattern
return|;
block|}
DECL|method|setThreadNamePattern (String threadNamePattern)
specifier|public
name|void
name|setThreadNamePattern
parameter_list|(
name|String
name|threadNamePattern
parameter_list|)
block|{
name|this
operator|.
name|threadNamePattern
operator|=
name|threadNamePattern
expr_stmt|;
block|}
DECL|method|lookup (String executorServiceRef)
specifier|public
name|ExecutorService
name|lookup
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
block|{
return|return
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|executorServiceRef
argument_list|,
name|ExecutorService
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|newCachedThreadPool (String name)
specifier|public
name|ExecutorService
name|newCachedThreadPool
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newCachedThreadPool
argument_list|(
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|newScheduledThreadPool (String name, int poolSize)
specifier|public
name|ScheduledExecutorService
name|newScheduledThreadPool
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|poolSize
argument_list|,
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|newFixedThreadPool (String name, int poolSize)
specifier|public
name|ExecutorService
name|newFixedThreadPool
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|poolSize
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|,
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|newSingleThreadExecutor (String name)
specifier|public
name|ExecutorService
name|newSingleThreadExecutor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newSingleThreadExecutor
argument_list|(
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|newThreadPool (String name, int corePoolSize, int maxPoolSize)
specifier|public
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
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
name|corePoolSize
argument_list|,
name|maxPoolSize
argument_list|)
return|;
block|}
DECL|method|newThreadPool (String name, int corePoolSize, int maxPoolSize, long keepAliveTime, TimeUnit timeUnit, boolean daemon)
specifier|public
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
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newThreadPool
argument_list|(
name|getThreadName
argument_list|(
name|name
argument_list|)
argument_list|,
name|corePoolSize
argument_list|,
name|maxPoolSize
argument_list|,
name|keepAliveTime
argument_list|,
name|timeUnit
argument_list|,
name|daemon
argument_list|)
return|;
block|}
DECL|method|shutdown (ExecutorService executorService)
specifier|public
name|void
name|shutdown
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|shutdownNow (ExecutorService executorService)
specifier|public
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
block|{
return|return
name|executorService
operator|.
name|shutdownNow
argument_list|()
return|;
block|}
block|}
end_class

end_unit

