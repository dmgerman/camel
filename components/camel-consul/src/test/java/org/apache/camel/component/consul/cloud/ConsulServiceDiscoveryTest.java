begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul.cloud
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
name|cloud
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|ThreadLocalRandom
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
name|model
operator|.
name|agent
operator|.
name|ImmutableRegCheck
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
name|model
operator|.
name|agent
operator|.
name|ImmutableRegistration
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
name|model
operator|.
name|agent
operator|.
name|Registration
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
name|ConsulTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|SocketUtils
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNotNull
import|;
end_import

begin_class
DECL|class|ConsulServiceDiscoveryTest
specifier|public
class|class
name|ConsulServiceDiscoveryTest
extends|extends
name|ConsulTestSupport
block|{
DECL|field|client
specifier|private
name|AgentClient
name|client
decl_stmt|;
DECL|field|registrations
specifier|private
name|List
argument_list|<
name|Registration
argument_list|>
name|registrations
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
name|client
operator|=
name|getConsul
argument_list|()
operator|.
name|agentClient
argument_list|()
expr_stmt|;
name|registrations
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|6
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|boolean
name|healty
init|=
name|ThreadLocalRandom
operator|.
name|current
argument_list|()
operator|.
name|nextBoolean
argument_list|()
decl_stmt|;
specifier|final
name|int
name|port
init|=
name|SocketUtils
operator|.
name|findAvailableTcpPort
argument_list|()
decl_stmt|;
name|Registration
operator|.
name|RegCheck
name|c
init|=
name|ImmutableRegCheck
operator|.
name|builder
argument_list|()
operator|.
name|ttl
argument_list|(
literal|"1m"
argument_list|)
operator|.
name|status
argument_list|(
name|healty
condition|?
literal|"passing"
else|:
literal|"critical"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|Registration
name|r
init|=
name|ImmutableRegistration
operator|.
name|builder
argument_list|()
operator|.
name|id
argument_list|(
literal|"service-"
operator|+
name|i
argument_list|)
operator|.
name|name
argument_list|(
literal|"my-service"
argument_list|)
operator|.
name|address
argument_list|(
literal|"127.0.0.1"
argument_list|)
operator|.
name|addTags
argument_list|(
literal|"a-tag"
argument_list|)
operator|.
name|addTags
argument_list|(
literal|"key1=value1"
argument_list|)
operator|.
name|addTags
argument_list|(
literal|"key2=value2"
argument_list|)
operator|.
name|addTags
argument_list|(
literal|"healthy="
operator|+
name|healty
argument_list|)
operator|.
name|putMeta
argument_list|(
literal|"meta-key"
argument_list|,
literal|"meta-val"
argument_list|)
operator|.
name|port
argument_list|(
name|port
argument_list|)
operator|.
name|check
argument_list|(
name|c
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|client
operator|.
name|register
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|registrations
operator|.
name|add
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doPostTearDown ()
specifier|public
name|void
name|doPostTearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPostTearDown
argument_list|()
expr_stmt|;
name|registrations
operator|.
name|forEach
argument_list|(
name|r
lambda|->
name|client
operator|.
name|deregister
argument_list|(
name|r
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************************************
comment|// Test
comment|// *************************************************************************
annotation|@
name|Test
DECL|method|testServiceDiscovery ()
specifier|public
name|void
name|testServiceDiscovery
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsulConfiguration
name|configuration
init|=
operator|new
name|ConsulConfiguration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setUrl
argument_list|(
name|consulUrl
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceDiscovery
name|discovery
init|=
operator|new
name|ConsulServiceDiscovery
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ServiceDefinition
argument_list|>
name|services
init|=
name|discovery
operator|.
name|getServices
argument_list|(
literal|"my-service"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|services
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|services
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ServiceDefinition
name|service
range|:
name|services
control|)
block|{
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsEntry
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_NAME
argument_list|,
literal|"my-service"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsKey
argument_list|(
name|ServiceDefinition
operator|.
name|SERVICE_META_ID
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"a-tag"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsEntry
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsEntry
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
argument_list|)
operator|.
name|containsEntry
argument_list|(
literal|"meta-key"
argument_list|,
literal|"meta-val"
argument_list|)
expr_stmt|;
name|Assertions
operator|.
name|assertThat
argument_list|(
literal|""
operator|+
name|service
operator|.
name|getHealth
argument_list|()
operator|.
name|isHealthy
argument_list|()
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|get
argument_list|(
literal|"healthy"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

