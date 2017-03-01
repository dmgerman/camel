begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consul
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
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|Assert
operator|.
name|assertFalse
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
name|assertNotNull
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|ConsulServiceDiscoveryTest
specifier|public
class|class
name|ConsulServiceDiscoveryTest
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
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|=
name|Consul
operator|.
name|builder
argument_list|()
operator|.
name|build
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
literal|3
condition|;
name|i
operator|++
control|)
block|{
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
name|port
argument_list|(
literal|9000
operator|+
name|i
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
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
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
argument_list|(
literal|null
argument_list|)
decl_stmt|;
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
literal|3
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
name|assertFalse
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"service_name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"service_id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"a-tag"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|service
operator|.
name|getMetadata
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

