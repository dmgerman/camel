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
name|LoadBalancer
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
name|spring
operator|.
name|boot
operator|.
name|cloud
operator|.
name|CamelCloudAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|cloud
operator|.
name|CamelCloudConfigurationProperties
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
name|spring
operator|.
name|boot
operator|.
name|cloud
operator|.
name|CamelCloudServiceDiscovery
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
name|spring
operator|.
name|boot
operator|.
name|cloud
operator|.
name|CamelCloudServiceDiscoveryAutoConfiguration
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
name|spring
operator|.
name|boot
operator|.
name|util
operator|.
name|GroupCondition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|condition
operator|.
name|ConditionalOnMissingBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|EnableConfigurationProperties
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
name|discovery
operator|.
name|DiscoveryClient
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
name|LoadBalancerAutoConfiguration
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Conditional
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_class
annotation|@
name|Configuration
annotation|@
name|ConditionalOnBean
argument_list|(
block|{
name|CamelCloudAutoConfiguration
operator|.
name|class
block|,
name|LoadBalancerClient
operator|.
name|class
block|}
argument_list|)
annotation|@
name|AutoConfigureAfter
argument_list|(
block|{
name|LoadBalancerAutoConfiguration
operator|.
name|class
block|,
name|CamelCloudServiceDiscoveryAutoConfiguration
operator|.
name|class
block|}
argument_list|)
annotation|@
name|EnableConfigurationProperties
argument_list|(
name|CamelCloudConfigurationProperties
operator|.
name|class
argument_list|)
annotation|@
name|Conditional
argument_list|(
name|CamelSpringCloudLoadBalancerAutoConfiguration
operator|.
name|LoadBalancerCondition
operator|.
name|class
argument_list|)
DECL|class|CamelSpringCloudLoadBalancerAutoConfiguration
specifier|public
class|class
name|CamelSpringCloudLoadBalancerAutoConfiguration
block|{
annotation|@
name|Bean
argument_list|(
name|name
operator|=
literal|"load-balancer"
argument_list|)
annotation|@
name|ConditionalOnMissingBean
DECL|method|cloudLoadBalancer (LoadBalancerClient loadBalancerClient)
specifier|public
name|LoadBalancer
name|cloudLoadBalancer
parameter_list|(
name|LoadBalancerClient
name|loadBalancerClient
parameter_list|)
block|{
return|return
operator|new
name|CamelSpringCloudLoadBalancer
argument_list|(
name|loadBalancerClient
argument_list|)
return|;
block|}
comment|// *******************************
comment|// Condition
comment|// *******************************
DECL|class|LoadBalancerCondition
specifier|public
specifier|static
class|class
name|LoadBalancerCondition
extends|extends
name|GroupCondition
block|{
DECL|method|LoadBalancerCondition ()
specifier|public
name|LoadBalancerCondition
parameter_list|()
block|{
name|super
argument_list|(
literal|"camel.cloud"
argument_list|,
literal|"camel.cloud.load-balancer"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

