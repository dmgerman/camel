begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_interface
DECL|interface|ManagedThreadsMBean
specifier|public
interface|interface
name|ManagedThreadsMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not the caller should run the task when it was rejected by the thread pool"
argument_list|)
DECL|method|isCallerRunsWhenRejected ()
name|Boolean
name|isCallerRunsWhenRejected
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"How to handle tasks which cannot be accepted by the thread pool"
argument_list|)
DECL|method|getRejectedPolicy ()
name|String
name|getRejectedPolicy
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
literal|"Whether core threads is allowed to timeout if no tasks in queue to process"
argument_list|)
DECL|method|isAllowCoreThreadTimeout ()
name|boolean
name|isAllowCoreThreadTimeout
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

