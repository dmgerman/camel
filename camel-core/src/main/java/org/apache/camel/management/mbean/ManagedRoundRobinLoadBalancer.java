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
name|api
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRoundRobinLoadBalancerMBean
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
name|model
operator|.
name|LoadBalanceDefinition
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
name|model
operator|.
name|ProcessorDefinition
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
name|processor
operator|.
name|loadbalancer
operator|.
name|RoundRobinLoadBalancer
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed RoundRobin LoadBalancer"
argument_list|)
DECL|class|ManagedRoundRobinLoadBalancer
specifier|public
class|class
name|ManagedRoundRobinLoadBalancer
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedRoundRobinLoadBalancerMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|RoundRobinLoadBalancer
name|processor
decl_stmt|;
DECL|method|ManagedRoundRobinLoadBalancer (CamelContext context, RoundRobinLoadBalancer processor, LoadBalanceDefinition definition)
specifier|public
name|ManagedRoundRobinLoadBalancer
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RoundRobinLoadBalancer
name|processor
parameter_list|,
name|LoadBalanceDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDefinition ()
specifier|public
name|LoadBalanceDefinition
name|getDefinition
parameter_list|()
block|{
return|return
operator|(
name|LoadBalanceDefinition
operator|)
name|super
operator|.
name|getDefinition
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getSize ()
specifier|public
name|Integer
name|getSize
parameter_list|()
block|{
return|return
name|processor
operator|.
name|getProcessors
argument_list|()
operator|.
name|size
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getLastChosenProcessorId ()
specifier|public
name|String
name|getLastChosenProcessorId
parameter_list|()
block|{
name|int
name|idx
init|=
name|processor
operator|.
name|getLastChosenProcessorIndex
argument_list|()
decl_stmt|;
if|if
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|LoadBalanceDefinition
name|def
init|=
name|getDefinition
argument_list|()
decl_stmt|;
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
init|=
name|def
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
name|idx
argument_list|)
decl_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
return|return
name|output
operator|.
name|getId
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

