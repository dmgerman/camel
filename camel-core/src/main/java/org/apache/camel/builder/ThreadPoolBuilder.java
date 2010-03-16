begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|ThreadPoolRejectedPolicy
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
name|ThreadPoolProfileDefinition
import|;
end_import

begin_comment
comment|/**  * A builder to create thread pools.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ThreadPoolBuilder
specifier|public
specifier|final
class|class
name|ThreadPoolBuilder
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|threadPoolDefinition
specifier|private
name|ThreadPoolProfileDefinition
name|threadPoolDefinition
decl_stmt|;
DECL|method|ThreadPoolBuilder (CamelContext camelContext)
specifier|public
name|ThreadPoolBuilder
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
comment|// use the default thread profile as the base
name|this
operator|.
name|threadPoolDefinition
operator|=
operator|new
name|ThreadPoolProfileDefinition
argument_list|(
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|getDefaultThreadPoolProfile
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|poolSize (int poolSize)
specifier|public
name|ThreadPoolBuilder
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|poolSize
argument_list|(
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxPoolSize (int maxPoolSize)
specifier|public
name|ThreadPoolBuilder
name|maxPoolSize
parameter_list|(
name|int
name|maxPoolSize
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|maxPoolSize
argument_list|(
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (long keepAliveTime)
specifier|public
name|ThreadPoolBuilder
name|keepAliveTime
parameter_list|(
name|long
name|keepAliveTime
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|keepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|timeUnit (TimeUnit timeUnit)
specifier|public
name|ThreadPoolBuilder
name|timeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|timeUnit
argument_list|(
name|timeUnit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|maxQueueSize (int maxQueueSize)
specifier|public
name|ThreadPoolBuilder
name|maxQueueSize
parameter_list|(
name|int
name|maxQueueSize
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|maxQueueSize
argument_list|(
name|maxQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|rejectedPolicy (ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|ThreadPoolBuilder
name|rejectedPolicy
parameter_list|(
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|threadPoolDefinition
operator|.
name|rejectedPolicy
argument_list|(
name|rejectedPolicy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Lookup a {@link java.util.concurrent.ExecutorService} from the {@link org.apache.camel.spi.Registry}.      *      * @param source             the source object, usually it should be<tt>this</tt> passed in as parameter      * @param executorServiceRef reference to lookup      * @return the {@link java.util.concurrent.ExecutorService} or<tt>null</tt> if not found      */
DECL|method|lookup (Object source, String executorServiceRef)
specifier|public
name|ExecutorService
name|lookup
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|executorServiceRef
parameter_list|)
block|{
return|return
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|lookup
argument_list|(
name|source
argument_list|,
name|executorServiceRef
argument_list|)
return|;
block|}
comment|/**      * Builds the new thread pool      *      * @param name name which is appended to the thread name      * @return the created thread pool      */
DECL|method|build (String name)
specifier|public
name|ExecutorService
name|build
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|build
argument_list|(
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|/**      * Builds the new thread pool      *      * @param source the source object, usually it should be<tt>this</tt> passed in as parameter      * @param name   name which is appended to the thread name      * @return the created thread pool      */
DECL|method|build (Object source, String name)
specifier|public
name|ExecutorService
name|build
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|ExecutorService
name|answer
init|=
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|threadPoolDefinition
operator|.
name|getPoolSize
argument_list|()
argument_list|,
name|threadPoolDefinition
operator|.
name|getMaxPoolSize
argument_list|()
argument_list|,
name|threadPoolDefinition
operator|.
name|getKeepAliveTime
argument_list|()
argument_list|,
name|threadPoolDefinition
operator|.
name|getTimeUnit
argument_list|()
argument_list|,
name|threadPoolDefinition
operator|.
name|getMaxQueueSize
argument_list|()
argument_list|,
name|threadPoolDefinition
operator|.
name|getRejectedExecutionHandler
argument_list|()
argument_list|,
literal|false
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

