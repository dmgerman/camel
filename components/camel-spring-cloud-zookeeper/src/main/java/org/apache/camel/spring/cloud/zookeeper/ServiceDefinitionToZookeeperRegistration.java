begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.cloud.zookeeper
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
operator|.
name|zookeeper
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
name|springframework
operator|.
name|cloud
operator|.
name|zookeeper
operator|.
name|discovery
operator|.
name|ZookeeperDiscoveryProperties
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
name|zookeeper
operator|.
name|discovery
operator|.
name|ZookeeperInstance
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
name|zookeeper
operator|.
name|serviceregistry
operator|.
name|ServiceInstanceRegistration
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
name|zookeeper
operator|.
name|serviceregistry
operator|.
name|ZookeeperRegistration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|convert
operator|.
name|converter
operator|.
name|Converter
import|;
end_import

begin_class
DECL|class|ServiceDefinitionToZookeeperRegistration
specifier|public
specifier|final
class|class
name|ServiceDefinitionToZookeeperRegistration
implements|implements
name|Converter
argument_list|<
name|ServiceDefinition
argument_list|,
name|ZookeeperRegistration
argument_list|>
block|{
DECL|field|properties
specifier|private
specifier|final
name|CamelCloudConfigurationProperties
name|properties
decl_stmt|;
DECL|method|ServiceDefinitionToZookeeperRegistration (CamelCloudConfigurationProperties properties)
specifier|public
name|ServiceDefinitionToZookeeperRegistration
parameter_list|(
name|CamelCloudConfigurationProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|convert (ServiceDefinition source)
specifier|public
name|ZookeeperRegistration
name|convert
parameter_list|(
name|ServiceDefinition
name|source
parameter_list|)
block|{
name|ZookeeperInstance
name|instance
init|=
operator|new
name|ZookeeperInstance
argument_list|(
name|source
operator|.
name|getId
argument_list|()
argument_list|,
name|source
operator|.
name|getName
argument_list|()
argument_list|,
name|source
operator|.
name|getMetadata
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ServiceInstanceRegistration
operator|.
name|builder
argument_list|()
operator|.
name|address
argument_list|(
name|properties
operator|.
name|getServiceRegistry
argument_list|()
operator|.
name|getServiceHost
argument_list|()
argument_list|)
operator|.
name|port
argument_list|(
name|source
operator|.
name|getPort
argument_list|()
argument_list|)
operator|.
name|name
argument_list|(
name|source
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|payload
argument_list|(
name|instance
argument_list|)
operator|.
name|uriSpec
argument_list|(
name|ZookeeperDiscoveryProperties
operator|.
name|DEFAULT_URI_SPEC
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

