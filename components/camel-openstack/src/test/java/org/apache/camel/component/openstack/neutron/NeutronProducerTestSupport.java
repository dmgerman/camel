begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.neutron
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
name|neutron
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
name|networking
operator|.
name|NetworkService
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
name|networking
operator|.
name|NetworkingService
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
name|networking
operator|.
name|PortService
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
name|networking
operator|.
name|RouterService
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
name|networking
operator|.
name|SubnetService
import|;
end_import

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

begin_class
DECL|class|NeutronProducerTestSupport
specifier|public
class|class
name|NeutronProducerTestSupport
extends|extends
name|AbstractProducerTestSupport
block|{
annotation|@
name|Mock
DECL|field|endpoint
specifier|protected
name|NeutronEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|networkingService
specifier|protected
name|NetworkingService
name|networkingService
decl_stmt|;
annotation|@
name|Mock
DECL|field|portService
name|PortService
name|portService
decl_stmt|;
annotation|@
name|Mock
DECL|field|routerService
name|RouterService
name|routerService
decl_stmt|;
annotation|@
name|Mock
DECL|field|subnetService
name|SubnetService
name|subnetService
decl_stmt|;
annotation|@
name|Mock
DECL|field|networkService
name|NetworkService
name|networkService
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
name|networking
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|networkingService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkingService
operator|.
name|port
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|portService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkingService
operator|.
name|router
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|routerService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkingService
operator|.
name|subnet
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|subnetService
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkingService
operator|.
name|network
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|networkService
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

