begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedThreadPoolMBean
specifier|public
interface|interface
name|ManagedThreadPoolMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Thread Pool id"
argument_list|)
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Id of source for creating Thread Pool"
argument_list|)
DECL|method|getSourceId ()
name|String
name|getSourceId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route id for the source, which created the Thread Pool"
argument_list|)
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Id of the thread pool profile which this pool is based upon"
argument_list|)
DECL|method|getThreadPoolProfileId ()
name|String
name|getThreadPoolProfileId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Core pool size"
argument_list|)
DECL|method|getCorePoolSize ()
name|int
name|getCorePoolSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Core pool size"
argument_list|)
DECL|method|setCorePoolSize (int corePoolSize)
name|void
name|setCorePoolSize
parameter_list|(
name|int
name|corePoolSize
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Pool size"
argument_list|)
DECL|method|getPoolSize ()
name|int
name|getPoolSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum pool size"
argument_list|)
DECL|method|getMaximumPoolSize ()
name|int
name|getMaximumPoolSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum pool size"
argument_list|)
DECL|method|setMaximumPoolSize (int maximumPoolSize)
name|void
name|setMaximumPoolSize
parameter_list|(
name|int
name|maximumPoolSize
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Largest pool size"
argument_list|)
DECL|method|getLargestPoolSize ()
name|int
name|getLargestPoolSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Active count"
argument_list|)
DECL|method|getActiveCount ()
name|int
name|getActiveCount
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Task count"
argument_list|)
DECL|method|getTaskCount ()
name|long
name|getTaskCount
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Completed task count"
argument_list|)
DECL|method|getCompletedTaskCount ()
name|long
name|getCompletedTaskCount
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Task queue size"
argument_list|)
DECL|method|getTaskQueueSize ()
name|long
name|getTaskQueueSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is task queue empty"
argument_list|)
DECL|method|isTaskQueueEmpty ()
name|boolean
name|isTaskQueueEmpty
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Keep alive time in seconds"
argument_list|)
DECL|method|getKeepAliveTime ()
name|long
name|getKeepAliveTime
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Keep alive time in seconds"
argument_list|)
DECL|method|setKeepAliveTime (long keepAliveTimeInSeconds)
name|void
name|setKeepAliveTime
parameter_list|(
name|long
name|keepAliveTimeInSeconds
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is shutdown"
argument_list|)
DECL|method|isShutdown ()
name|boolean
name|isShutdown
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Purges the pool"
argument_list|)
DECL|method|purge ()
name|void
name|purge
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

