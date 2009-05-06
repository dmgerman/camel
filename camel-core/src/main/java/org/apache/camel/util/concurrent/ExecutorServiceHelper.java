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
name|ScheduledExecutorService
import|;
end_import

begin_comment
comment|/**  * Helper for {@link java.util.concurrent.ExecutorService} to construct executors using a thread factory that  * create thread names with Camel prefix.  *  * @version $Revision$  */
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
name|int
name|threadCounter
decl_stmt|;
DECL|method|ExecutorServiceHelper ()
specifier|private
name|ExecutorServiceHelper
parameter_list|()
block|{     }
comment|/**      * Creates a new thread name with the given prefix      */
DECL|method|getThreadName (String name)
specifier|protected
specifier|static
name|String
name|getThreadName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
literal|"Camel "
operator|+
name|name
operator|+
literal|" thread:"
operator|+
name|nextThreadCounter
argument_list|()
return|;
block|}
DECL|method|nextThreadCounter ()
specifier|protected
specifier|static
specifier|synchronized
name|int
name|nextThreadCounter
parameter_list|()
block|{
return|return
operator|++
name|threadCounter
return|;
block|}
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
name|getThreadName
argument_list|(
name|name
argument_list|)
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
name|getThreadName
argument_list|(
name|name
argument_list|)
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
name|getThreadName
argument_list|(
name|name
argument_list|)
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
block|}
end_class

end_unit

