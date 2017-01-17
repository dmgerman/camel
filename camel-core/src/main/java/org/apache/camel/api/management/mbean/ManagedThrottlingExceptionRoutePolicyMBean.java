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

begin_interface
DECL|interface|ManagedThrottlingExceptionRoutePolicyMBean
specifier|public
interface|interface
name|ManagedThrottlingExceptionRoutePolicyMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"how long to wait before moving open circuit to half open"
argument_list|)
DECL|method|getHalfOpenAfter ()
name|long
name|getHalfOpenAfter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"how long to wait before moving open circuit to half open"
argument_list|)
DECL|method|setHalfOpenAfter (long milliseconds)
name|void
name|setHalfOpenAfter
parameter_list|(
name|long
name|milliseconds
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"the range of time that failures should occur within"
argument_list|)
DECL|method|getFailureWindow ()
name|long
name|getFailureWindow
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"the range of time that failures should occur within"
argument_list|)
DECL|method|setFailureWindow (long milliseconds)
name|void
name|setFailureWindow
parameter_list|(
name|long
name|milliseconds
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"number of failures before opening circuit"
argument_list|)
DECL|method|getFailureThreshold ()
name|int
name|getFailureThreshold
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"number of failures before opening circuit"
argument_list|)
DECL|method|setFailureThreshold (int numberOfFailures)
name|void
name|setFailureThreshold
parameter_list|(
name|int
name|numberOfFailures
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"State"
argument_list|)
DECL|method|currentState ()
name|String
name|currentState
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The half open handler registered (if any)"
argument_list|)
DECL|method|hasHalfOpenHandler ()
name|String
name|hasHalfOpenHandler
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"the number of failures caught"
argument_list|)
DECL|method|currentFailures ()
name|int
name|currentFailures
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"number of ms since the last failure was recorded"
argument_list|)
DECL|method|getLastFailure ()
name|long
name|getLastFailure
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"number ms since the circuit was opened"
argument_list|)
DECL|method|getOpenAt ()
name|long
name|getOpenAt
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

