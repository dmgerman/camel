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
name|Collections
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|springframework
operator|.
name|cloud
operator|.
name|client
operator|.
name|DefaultServiceInstance
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
name|discovery
operator|.
name|DiscoveryClient
import|;
end_import

begin_class
DECL|class|CamelCloudDiscoveryClient
specifier|public
class|class
name|CamelCloudDiscoveryClient
implements|implements
name|DiscoveryClient
block|{
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|serviceDiscovery
specifier|private
specifier|final
name|ServiceDiscovery
name|serviceDiscovery
decl_stmt|;
DECL|field|localInstance
specifier|private
name|ServiceInstance
name|localInstance
decl_stmt|;
DECL|method|CamelCloudDiscoveryClient (String description, ServiceDiscovery serviceDiscovery)
specifier|public
name|CamelCloudDiscoveryClient
parameter_list|(
name|String
name|description
parameter_list|,
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|)
block|{
name|this
argument_list|(
name|description
argument_list|,
literal|null
argument_list|,
name|serviceDiscovery
argument_list|)
expr_stmt|;
block|}
DECL|method|CamelCloudDiscoveryClient (String description, ServiceInstance localServiceDiscovery, ServiceDiscovery serviceDiscovery)
specifier|public
name|CamelCloudDiscoveryClient
parameter_list|(
name|String
name|description
parameter_list|,
name|ServiceInstance
name|localServiceDiscovery
parameter_list|,
name|ServiceDiscovery
name|serviceDiscovery
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
name|this
operator|.
name|serviceDiscovery
operator|=
name|serviceDiscovery
expr_stmt|;
name|this
operator|.
name|localInstance
operator|=
name|localServiceDiscovery
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|description ()
specifier|public
name|String
name|description
parameter_list|()
block|{
return|return
name|description
return|;
block|}
annotation|@
name|Override
DECL|method|getLocalServiceInstance ()
specifier|public
name|ServiceInstance
name|getLocalServiceInstance
parameter_list|()
block|{
return|return
name|this
operator|.
name|localInstance
return|;
block|}
DECL|method|setLocalServiceInstance (ServiceInstance instance)
specifier|public
name|CamelCloudDiscoveryClient
name|setLocalServiceInstance
parameter_list|(
name|ServiceInstance
name|instance
parameter_list|)
block|{
name|this
operator|.
name|localInstance
operator|=
name|instance
expr_stmt|;
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|getInstances (String serviceId)
specifier|public
name|List
argument_list|<
name|ServiceInstance
argument_list|>
name|getInstances
parameter_list|(
name|String
name|serviceId
parameter_list|)
block|{
return|return
name|serviceDiscovery
operator|.
name|getUpdatedListOfServices
argument_list|(
name|serviceId
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|s
lambda|->
operator|new
name|DefaultServiceInstance
argument_list|(
name|s
operator|.
name|getName
argument_list|()
argument_list|,
name|s
operator|.
name|getHost
argument_list|()
argument_list|,
name|s
operator|.
name|getPort
argument_list|()
argument_list|,
literal|false
argument_list|,
name|s
operator|.
name|getMetadata
argument_list|()
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getServices ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getServices
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
block|}
end_class

end_unit

