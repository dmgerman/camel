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
DECL|interface|ManagedSchedulePollConsumerMBean
specifier|public
interface|interface
name|ManagedSchedulePollConsumerMBean
extends|extends
name|ManagedConsumerMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Delay"
argument_list|)
DECL|method|getDelay ()
name|long
name|getDelay
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Delay"
argument_list|)
DECL|method|setDelay (long delay)
name|void
name|setDelay
parameter_list|(
name|long
name|delay
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Initial Delay"
argument_list|)
DECL|method|getInitialDelay ()
name|long
name|getInitialDelay
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Initial Delay"
argument_list|)
DECL|method|setInitialDelay (long initialDelay)
name|void
name|setInitialDelay
parameter_list|(
name|long
name|initialDelay
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Fixed Delay"
argument_list|)
DECL|method|isUseFixedDelay ()
name|boolean
name|isUseFixedDelay
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled Fixed Delay"
argument_list|)
DECL|method|setUseFixedDelay (boolean useFixedDelay)
name|void
name|setUseFixedDelay
parameter_list|(
name|boolean
name|useFixedDelay
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled TimeUnit"
argument_list|)
DECL|method|getTimeUnit ()
name|String
name|getTimeUnit
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduled TimeUnit"
argument_list|)
DECL|method|setTimeUnit (String timeUnit)
name|void
name|setTimeUnit
parameter_list|(
name|String
name|timeUnit
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is the scheduler currently polling"
argument_list|)
DECL|method|isPolling ()
name|boolean
name|isPolling
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Is the scheduler started"
argument_list|)
DECL|method|isSchedulerStarted ()
name|boolean
name|isSchedulerStarted
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Starts the scheduler"
argument_list|)
DECL|method|startScheduler ()
name|void
name|startScheduler
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scheduler classname"
argument_list|)
DECL|method|getSchedulerClassName ()
name|String
name|getSchedulerClassName
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Backoff multiplier"
argument_list|)
DECL|method|getBackoffMultiplier ()
name|int
name|getBackoffMultiplier
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Backoff idle threshold"
argument_list|)
DECL|method|getBackoffIdleThreshold ()
name|int
name|getBackoffIdleThreshold
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Backoff error threshold"
argument_list|)
DECL|method|getBackoffErrorThreshold ()
name|int
name|getBackoffErrorThreshold
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current backoff counter"
argument_list|)
DECL|method|getBackoffCounter ()
name|int
name|getBackoffCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Repeat count"
argument_list|)
DECL|method|getRepeatCount ()
name|long
name|getRepeatCount
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

