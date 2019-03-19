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
DECL|interface|ManagedThrottlerMBean
specifier|public
interface|interface
name|ManagedThrottlerMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum requests per period"
argument_list|)
DECL|method|getMaximumRequestsPerPeriod ()
name|long
name|getMaximumRequestsPerPeriod
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum requests per period"
argument_list|)
DECL|method|setMaximumRequestsPerPeriod (long maximumRequestsPerPeriod)
name|void
name|setMaximumRequestsPerPeriod
parameter_list|(
name|long
name|maximumRequestsPerPeriod
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Time period in millis"
argument_list|)
DECL|method|getTimePeriodMillis ()
name|long
name|getTimePeriodMillis
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Time period in millis"
argument_list|)
DECL|method|setTimePeriodMillis (long timePeriodMillis)
name|void
name|setTimePeriodMillis
parameter_list|(
name|long
name|timePeriodMillis
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Enables asynchronous delay which means the thread will not block while delaying"
argument_list|)
DECL|method|isAsyncDelayed ()
name|Boolean
name|isAsyncDelayed
parameter_list|()
function_decl|;
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
literal|"Whether or not throttler throws the ThrottlerRejectedExecutionException when the exchange exceeds the request limit"
argument_list|)
DECL|method|isRejectExecution ()
name|Boolean
name|isRejectExecution
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

