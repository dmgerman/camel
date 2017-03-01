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
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|ServiceDiscovery
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
name|CachingServiceDiscovery
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
name|ChainedServiceDiscovery
import|;
end_import

begin_class
DECL|class|CamelCloudServiceDiscovery
specifier|public
class|class
name|CamelCloudServiceDiscovery
implements|implements
name|ServiceDiscovery
block|{
DECL|field|delegate
specifier|private
name|ServiceDiscovery
name|delegate
decl_stmt|;
DECL|method|CamelCloudServiceDiscovery (Long timeout, List<ServiceDiscovery> serviceDiscoveryList)
specifier|public
name|CamelCloudServiceDiscovery
parameter_list|(
name|Long
name|timeout
parameter_list|,
name|List
argument_list|<
name|ServiceDiscovery
argument_list|>
name|serviceDiscoveryList
parameter_list|)
block|{
comment|// Created a chained service discovery that collects services from multiple
comment|// ServiceDiscovery
name|this
operator|.
name|delegate
operator|=
operator|new
name|ChainedServiceDiscovery
argument_list|(
name|serviceDiscoveryList
argument_list|)
expr_stmt|;
comment|// If a timeout is provided, wrap the serviceDiscovery with a caching
comment|// strategy so the discovery implementations are not queried for each
comment|// discovery request
if|if
condition|(
name|timeout
operator|!=
literal|null
operator|&&
name|timeout
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|delegate
operator|=
name|CachingServiceDiscovery
operator|.
name|wrap
argument_list|(
name|this
operator|.
name|delegate
argument_list|,
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getServices (String name)
specifier|public
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|getServices
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|getServices
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
end_class

end_unit

