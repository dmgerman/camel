begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|management
operator|.
name|ManagedAttribute
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
name|management
operator|.
name|ManagedOperation
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
name|management
operator|.
name|ManagedResource
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
name|ManagementStrategy
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed ThreadPool"
argument_list|)
DECL|class|ManagedThreadPool
specifier|public
class|class
name|ManagedThreadPool
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|threadPool
specifier|private
specifier|final
name|ThreadPoolExecutor
name|threadPool
decl_stmt|;
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|sourceId
specifier|private
specifier|final
name|String
name|sourceId
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|threadPoolProfileId
specifier|private
specifier|final
name|String
name|threadPoolProfileId
decl_stmt|;
DECL|method|ManagedThreadPool (CamelContext camelContext, ThreadPoolExecutor threadPool, String id, String sourceId, String routeId, String threadPoolProfileId)
specifier|public
name|ManagedThreadPool
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ThreadPoolExecutor
name|threadPool
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|sourceId
parameter_list|,
name|String
name|routeId
parameter_list|,
name|String
name|threadPoolProfileId
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|threadPool
operator|=
name|threadPool
expr_stmt|;
name|this
operator|.
name|sourceId
operator|=
name|sourceId
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|routeId
expr_stmt|;
name|this
operator|.
name|threadPoolProfileId
operator|=
name|threadPoolProfileId
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|getThreadPool ()
specifier|public
name|ThreadPoolExecutor
name|getThreadPool
parameter_list|()
block|{
return|return
name|threadPool
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Thread Pool id"
argument_list|)
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Id of source for creating Thread Pool"
argument_list|)
DECL|method|getSourceId ()
specifier|public
name|String
name|getSourceId
parameter_list|()
block|{
return|return
name|sourceId
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route id for the source, which created the Thread Pool"
argument_list|)
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
return|return
name|routeId
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Id of the thread pool profile which this pool is based upon"
argument_list|)
DECL|method|getThreadPoolProfileId ()
specifier|public
name|String
name|getThreadPoolProfileId
parameter_list|()
block|{
return|return
name|threadPoolProfileId
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Core pool size"
argument_list|)
DECL|method|getCorePoolSize ()
specifier|public
name|int
name|getCorePoolSize
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getCorePoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Core pool size"
argument_list|)
DECL|method|setCorePoolSize (int corePoolSize)
specifier|public
name|void
name|setCorePoolSize
parameter_list|(
name|int
name|corePoolSize
parameter_list|)
block|{
name|threadPool
operator|.
name|setCorePoolSize
argument_list|(
name|corePoolSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Pool size"
argument_list|)
DECL|method|getPoolSize ()
specifier|public
name|int
name|getPoolSize
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getPoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum pool size"
argument_list|)
DECL|method|getMaximumPoolSize ()
specifier|public
name|int
name|getMaximumPoolSize
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getMaximumPoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum pool size"
argument_list|)
DECL|method|setMaximumPoolSize (int maximumPoolSize)
specifier|public
name|void
name|setMaximumPoolSize
parameter_list|(
name|int
name|maximumPoolSize
parameter_list|)
block|{
name|threadPool
operator|.
name|setMaximumPoolSize
argument_list|(
name|maximumPoolSize
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Largest pool size"
argument_list|)
DECL|method|getLargestPoolSize ()
specifier|public
name|int
name|getLargestPoolSize
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getLargestPoolSize
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Active count"
argument_list|)
DECL|method|getActiveCount ()
specifier|public
name|int
name|getActiveCount
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getActiveCount
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Task count"
argument_list|)
DECL|method|getTaskCount ()
specifier|public
name|long
name|getTaskCount
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getTaskCount
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completed task count"
argument_list|)
DECL|method|getCompletedTaskCount ()
specifier|public
name|long
name|getCompletedTaskCount
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getCompletedTaskCount
argument_list|()
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Task queue size"
argument_list|)
DECL|method|getTaskQueueSize ()
specifier|public
name|long
name|getTaskQueueSize
parameter_list|()
block|{
if|if
condition|(
name|threadPool
operator|.
name|getQueue
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|threadPool
operator|.
name|getQueue
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|0
return|;
block|}
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is task queue empty"
argument_list|)
DECL|method|isTaskQueueEmpty ()
specifier|public
name|boolean
name|isTaskQueueEmpty
parameter_list|()
block|{
if|if
condition|(
name|threadPool
operator|.
name|getQueue
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|threadPool
operator|.
name|getQueue
argument_list|()
operator|.
name|isEmpty
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Keep alive time in seconds"
argument_list|)
DECL|method|getKeepAliveTime ()
specifier|public
name|long
name|getKeepAliveTime
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|getKeepAliveTime
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Keep alive time in seconds"
argument_list|)
DECL|method|setKeepAliveTime (long keepAliveTimeInSeconds)
specifier|public
name|void
name|setKeepAliveTime
parameter_list|(
name|long
name|keepAliveTimeInSeconds
parameter_list|)
block|{
name|threadPool
operator|.
name|setKeepAliveTime
argument_list|(
name|keepAliveTimeInSeconds
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is shutdown"
argument_list|)
DECL|method|isShutdown ()
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|threadPool
operator|.
name|isShutdown
argument_list|()
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Purges the pool"
argument_list|)
DECL|method|purge ()
specifier|public
name|void
name|purge
parameter_list|()
block|{
name|threadPool
operator|.
name|purge
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

