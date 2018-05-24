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
name|zookeeper
operator|.
name|discovery
operator|.
name|ZookeeperServer
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
DECL|class|ZookeeperServerToServiceDefinition
specifier|public
specifier|final
class|class
name|ZookeeperServerToServiceDefinition
implements|implements
name|Converter
argument_list|<
name|ZookeeperServer
argument_list|,
name|ServiceDefinition
argument_list|>
block|{
annotation|@
name|Override
DECL|method|convert (ZookeeperServer source)
specifier|public
name|ServiceDefinition
name|convert
parameter_list|(
name|ZookeeperServer
name|source
parameter_list|)
block|{
return|return
operator|new
name|DefaultServiceDefinition
argument_list|(
name|source
operator|.
name|getId
argument_list|()
argument_list|,
name|source
operator|.
name|getHost
argument_list|()
argument_list|,
name|source
operator|.
name|getPort
argument_list|()
argument_list|,
name|source
operator|.
name|getInstance
argument_list|()
operator|.
name|getPayload
argument_list|()
operator|.
name|getMetadata
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

