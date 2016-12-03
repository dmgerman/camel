begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.keystone
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|openstack
operator|.
name|keystone
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
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
name|openstack
operator|.
name|AbstractProducerTestSupport
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
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|DomainService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|GroupService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|IdentityService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|ProjectService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|RegionService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|api
operator|.
name|identity
operator|.
name|v3
operator|.
name|UserService
import|;
end_import

begin_class
DECL|class|KeystoneProducerTestSupport
specifier|public
class|class
name|KeystoneProducerTestSupport
extends|extends
name|AbstractProducerTestSupport
block|{
annotation|@
name|Mock
DECL|field|endpoint
specifier|protected
name|KeystoneEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|identityService
specifier|protected
name|IdentityService
name|identityService
decl_stmt|;
annotation|@
name|Mock
DECL|field|domainService
specifier|protected
name|DomainService
name|domainService
decl_stmt|;
annotation|@
name|Mock
DECL|field|groupService
specifier|protected
name|GroupService
name|groupService
decl_stmt|;
annotation|@
name|Mock
DECL|field|projectService
specifier|protected
name|ProjectService
name|projectService
decl_stmt|;
annotation|@
name|Mock
DECL|field|regionService
specifier|protected
name|RegionService
name|regionService
decl_stmt|;
annotation|@
name|Mock
DECL|field|userService
specifier|protected
name|UserService
name|userService
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpComputeService ()
specifier|public
name|void
name|setUpComputeService
parameter_list|()
block|{
name|when
argument_list|(
name|client
operator|.
name|identity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|identityService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|identityService
operator|.
name|domains
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|domainService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|identityService
operator|.
name|groups
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|groupService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|identityService
operator|.
name|projects
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|projectService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|identityService
operator|.
name|regions
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|regionService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|identityService
operator|.
name|users
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|userService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

