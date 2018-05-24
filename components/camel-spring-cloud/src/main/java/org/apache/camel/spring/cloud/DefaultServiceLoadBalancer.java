begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|cloud
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
name|cloud
operator|.
name|ServiceDefinition
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
name|cloud
operator|.
name|ServiceLoadBalancer
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
name|cloud
operator|.
name|ServiceLoadBalancerFunction
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
name|impl
operator|.
name|cloud
operator|.
name|DefaultServiceDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|ServiceInstance
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|loadbalancer
operator|.
name|LoadBalancerClient
import|;
end_import

begin_class
DECL|class|DefaultServiceLoadBalancer
specifier|public
class|class
name|DefaultServiceLoadBalancer
implements|implements
name|ServiceLoadBalancer
block|{
DECL|field|client
specifier|private
specifier|final
name|LoadBalancerClient
name|client
decl_stmt|;
DECL|method|DefaultServiceLoadBalancer (LoadBalancerClient client)
specifier|public
name|DefaultServiceLoadBalancer
parameter_list|(
name|LoadBalancerClient
name|client
parameter_list|)
block|{
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (String serviceName, ServiceLoadBalancerFunction<T> function)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|process
parameter_list|(
name|String
name|serviceName
parameter_list|,
name|ServiceLoadBalancerFunction
argument_list|<
name|T
argument_list|>
name|function
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|client
operator|.
name|execute
argument_list|(
name|serviceName
argument_list|,
name|instance
lambda|->
block|{
return|return
name|function
operator|.
name|apply
argument_list|(
name|convertServiceInstanceToServiceDefinition
argument_list|(
name|instance
argument_list|)
argument_list|)
return|;
block|}
argument_list|)
return|;
block|}
DECL|method|convertServiceInstanceToServiceDefinition (ServiceInstance instance)
specifier|protected
name|ServiceDefinition
name|convertServiceInstanceToServiceDefinition
parameter_list|(
name|ServiceInstance
name|instance
parameter_list|)
block|{
return|return
operator|new
name|DefaultServiceDefinition
argument_list|(
name|instance
operator|.
name|getServiceId
argument_list|()
argument_list|,
name|instance
operator|.
name|getHost
argument_list|()
argument_list|,
name|instance
operator|.
name|getPort
argument_list|()
argument_list|,
name|instance
operator|.
name|getMetadata
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

