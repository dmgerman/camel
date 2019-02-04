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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
DECL|interface|ManagedFailoverLoadBalancerMBean
specifier|public
interface|interface
name|ManagedFailoverLoadBalancerMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of processors in the load balancer"
argument_list|)
DECL|method|getSize ()
name|Integer
name|getSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not the failover load balancer should operate in round robin mode or not."
argument_list|)
DECL|method|isRoundRobin ()
name|Boolean
name|isRoundRobin
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether or not the failover load balancer should operate in sticky mode or not."
argument_list|)
DECL|method|isSticky ()
name|Boolean
name|isSticky
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"A value to indicate after X failover attempts we should exhaust (give up)."
argument_list|)
DECL|method|getMaximumFailoverAttempts ()
name|Integer
name|getMaximumFailoverAttempts
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The class names of the exceptions the load balancer uses (separated by comma)"
argument_list|)
DECL|method|getExceptions ()
name|String
name|getExceptions
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Processor id of the last known good processor that succeed processing the exchange"
argument_list|)
DECL|method|getLastGoodProcessorId ()
name|String
name|getLastGoodProcessorId
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Statistics of the content based router for each exception"
argument_list|)
DECL|method|exceptionStatistics ()
name|TabularData
name|exceptionStatistics
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

