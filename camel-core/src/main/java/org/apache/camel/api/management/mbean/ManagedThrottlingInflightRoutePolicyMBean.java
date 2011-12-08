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
DECL|interface|ManagedThrottlingInflightRoutePolicyMBean
specifier|public
interface|interface
name|ManagedThrottlingInflightRoutePolicyMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum inflight exchanges"
argument_list|)
DECL|method|getMaxInflightExchanges ()
name|int
name|getMaxInflightExchanges
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Maximum inflight exchanges"
argument_list|)
DECL|method|setMaxInflightExchanges (int maxInflightExchanges)
name|void
name|setMaxInflightExchanges
parameter_list|(
name|int
name|maxInflightExchanges
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Resume percentage of maximum inflight exchanges"
argument_list|)
DECL|method|getResumePercentOfMax ()
name|int
name|getResumePercentOfMax
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Resume percentage of maximum inflight exchanges"
argument_list|)
DECL|method|setResumePercentOfMax (int resumePercentOfMax)
name|void
name|setResumePercentOfMax
parameter_list|(
name|int
name|resumePercentOfMax
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scope"
argument_list|)
DECL|method|getScope ()
name|String
name|getScope
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Scope"
argument_list|)
DECL|method|setScope (String scope)
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|getLoggingLevel ()
name|String
name|getLoggingLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging Level"
argument_list|)
DECL|method|setLoggingLevel (String loggingLevel)
name|void
name|setLoggingLevel
parameter_list|(
name|String
name|loggingLevel
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

