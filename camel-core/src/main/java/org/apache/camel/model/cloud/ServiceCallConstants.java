begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
DECL|class|ServiceCallConstants
specifier|final
class|class
name|ServiceCallConstants
block|{
DECL|field|SERVICE_DISCOVERY_CONFIGURATIONS
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|ServiceCallServiceDiscoveryConfiguration
argument_list|>
name|SERVICE_DISCOVERY_CONFIGURATIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|ConsulServiceCallServiceDiscoveryConfiguration
argument_list|()
argument_list|,
operator|new
name|DnsServiceCallServiceDiscoveryConfiguration
argument_list|()
argument_list|,
operator|new
name|EtcdServiceCallServiceDiscoveryConfiguration
argument_list|()
argument_list|,
operator|new
name|KubernetesServiceCallServiceDiscoveryConfiguration
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|SERVICE_FILTER_CONFIGURATIONS
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|ServiceCallServiceFilterConfiguration
argument_list|>
name|SERVICE_FILTER_CONFIGURATIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|HealthyServiceCallServiceFilterConfiguration
argument_list|()
argument_list|,
operator|new
name|PassThroughServiceCallServiceFilterConfiguration
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|LOAD_BALANCER_CONFIGURATIONS
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|ServiceCallLoadBalancerConfiguration
argument_list|>
name|LOAD_BALANCER_CONFIGURATIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|RibbonServiceCallLoadBalancerConfiguration
argument_list|()
argument_list|,
operator|new
name|DefaultServiceCallLoadBalancerConfiguration
argument_list|()
argument_list|)
decl_stmt|;
DECL|method|ServiceCallConstants ()
specifier|private
name|ServiceCallConstants
parameter_list|()
block|{     }
block|}
end_class

end_unit

