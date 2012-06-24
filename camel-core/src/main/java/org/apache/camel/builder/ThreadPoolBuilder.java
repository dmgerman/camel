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
name|spi
operator|.
name|ThreadPoolProfile
import|;
end_import

begin_comment
comment|/**  * A builder to create thread pools.  *  * @version   */
end_comment

begin_class
DECL|class|ThreadPoolBuilder
specifier|public
specifier|final
class|class
name|ThreadPoolBuilder
block|{
comment|// reuse a profile to store the settings
DECL|field|profile
specifier|private
specifier|final
name|ThreadPoolProfile
name|profile
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|ThreadPoolBuilder (CamelContext context)
specifier|public
name|ThreadPoolBuilder
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|profile
operator|=
operator|new
name|ThreadPoolProfile
argument_list|()
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
name|profile
operator|.
name|setPoolSize
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
name|profile
operator|.
name|setMaxPoolSize
argument_list|(
name|maxPoolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|keepAliveTime (long keepAliveTime, TimeUnit timeUnit)
specifier|public
name|ThreadPoolBuilder
name|keepAliveTime
parameter_list|(
name|long
name|keepAliveTime
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
argument_list|)
expr_stmt|;
name|profile
operator|.
name|setTimeUnit
argument_list|(
name|timeUnit
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
name|profile
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTime
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
name|profile
operator|.
name|setMaxQueueSize
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
name|profile
operator|.
name|setRejectedPolicy
argument_list|(
name|rejectedPolicy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Builds the new thread pool      *      * @return the created thread pool      * @throws Exception is thrown if error building the thread pool      */
DECL|method|build ()
specifier|public
name|ExecutorService
name|build
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|build
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Builds the new thread pool      *      * @param name name which is appended to the thread name      * @return the created thread pool      * @throws Exception is thrown if error building the thread pool      */
DECL|method|build (String name)
specifier|public
name|ExecutorService
name|build
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
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
comment|/**      * Builds the new thread pool      *      * @param source the source object, usually it should be<tt>this</tt> passed in as parameter      * @param name   name which is appended to the thread name      * @return the created thread pool      * @throws Exception is thrown if error building the thread pool      */
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
throws|throws
name|Exception
block|{
return|return
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
comment|/**      * Builds the new scheduled thread pool      *      * @return the created scheduled thread pool      * @throws Exception is thrown if error building the scheduled thread pool      */
DECL|method|buildScheduled ()
specifier|public
name|ScheduledExecutorService
name|buildScheduled
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|buildScheduled
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Builds the new scheduled thread pool      *      * @param name name which is appended to the thread name      * @return the created scheduled thread pool      * @throws Exception is thrown if error building the scheduled thread pool      */
DECL|method|buildScheduled (String name)
specifier|public
name|ScheduledExecutorService
name|buildScheduled
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|buildScheduled
argument_list|(
literal|null
argument_list|,
name|name
argument_list|)
return|;
block|}
comment|/**      * Builds the new scheduled thread pool      *      * @param source the source object, usually it should be<tt>this</tt> passed in as parameter      * @param name   name which is appended to the thread name      * @return the created scheduled thread pool      * @throws Exception is thrown if error building the scheduled thread pool      */
DECL|method|buildScheduled (Object source, String name)
specifier|public
name|ScheduledExecutorService
name|buildScheduled
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|source
argument_list|,
name|name
argument_list|,
name|profile
argument_list|)
return|;
block|}
block|}
end_class

end_unit

