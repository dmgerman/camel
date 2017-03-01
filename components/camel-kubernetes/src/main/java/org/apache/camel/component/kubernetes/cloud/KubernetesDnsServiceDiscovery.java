begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kubernetes.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kubernetes
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|component
operator|.
name|kubernetes
operator|.
name|KubernetesConfiguration
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

begin_class
DECL|class|KubernetesDnsServiceDiscovery
specifier|public
class|class
name|KubernetesDnsServiceDiscovery
extends|extends
name|KubernetesServiceDiscovery
block|{
DECL|field|cache
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
argument_list|>
name|cache
decl_stmt|;
DECL|method|KubernetesDnsServiceDiscovery (KubernetesConfiguration configuration)
specifier|public
name|KubernetesDnsServiceDiscovery
parameter_list|(
name|KubernetesConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|cache
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
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
name|this
operator|.
name|cache
operator|.
name|computeIfAbsent
argument_list|(
name|name
argument_list|,
name|key
lambda|->
name|Collections
operator|.
name|singletonList
argument_list|(
name|newService
argument_list|(
name|name
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|newService (String name)
specifier|private
name|ServiceDefinition
name|newService
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|DefaultServiceDefinition
argument_list|(
name|name
argument_list|,
name|name
operator|+
literal|"."
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getNamespace
argument_list|()
operator|+
literal|".svc."
operator|+
name|getConfiguration
argument_list|()
operator|.
name|getDnsDomain
argument_list|()
argument_list|,
operator|-
literal|1
argument_list|)
return|;
block|}
block|}
end_class

end_unit

