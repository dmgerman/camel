begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|AgentClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|orbitz
operator|.
name|consul
operator|.
name|Consul
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
name|component
operator|.
name|consul
operator|.
name|ConsulConfiguration
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
name|component
operator|.
name|consul
operator|.
name|ConsulEndpoint
import|;
end_import

begin_class
DECL|class|ConsulAgentProducer
specifier|public
specifier|final
class|class
name|ConsulAgentProducer
extends|extends
name|AbstractConsulProducer
argument_list|<
name|AgentClient
argument_list|>
block|{
DECL|method|ConsulAgentProducer (ConsulEndpoint endpoint, ConsulConfiguration configuration)
specifier|public
name|ConsulAgentProducer
parameter_list|(
name|ConsulEndpoint
name|endpoint
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|,
name|Consul
operator|::
name|agentClient
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ConsulAgentActions
operator|.
name|CHECKS
argument_list|,
name|wrap
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getChecks
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ConsulAgentActions
operator|.
name|SERVICES
argument_list|,
name|wrap
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getServices
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ConsulAgentActions
operator|.
name|MEMBERS
argument_list|,
name|wrap
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getMembers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|ConsulAgentActions
operator|.
name|AGENT
argument_list|,
name|wrap
argument_list|(
name|c
lambda|->
name|c
operator|.
name|getAgent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

