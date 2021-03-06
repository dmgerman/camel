begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|UUID
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
name|common
operator|.
name|OpenstackConstants
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
name|neutron
operator|.
name|producer
operator|.
name|NetworkProducer
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Captor
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
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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
name|Builders
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
name|model
operator|.
name|common
operator|.
name|ActionResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|Network
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|network
operator|.
name|NetworkType
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

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|anyString
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
name|doReturn
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
name|verify
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
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|NetworkProducerTest
specifier|public
class|class
name|NetworkProducerTest
extends|extends
name|NeutronProducerTestSupport
block|{
DECL|field|dummyNetwork
specifier|private
name|Network
name|dummyNetwork
decl_stmt|;
annotation|@
name|Mock
DECL|field|testOSnetwork
specifier|private
name|Network
name|testOSnetwork
decl_stmt|;
annotation|@
name|Mock
DECL|field|networkService
specifier|private
name|NetworkService
name|networkService
decl_stmt|;
annotation|@
name|Captor
DECL|field|networkCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|Network
argument_list|>
name|networkCaptor
decl_stmt|;
annotation|@
name|Captor
DECL|field|networkIdCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|networkIdCaptor
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
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
name|producer
operator|=
operator|new
name|NetworkProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkService
operator|.
name|create
argument_list|(
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testOSnetwork
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|networkService
operator|.
name|get
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testOSnetwork
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Network
argument_list|>
name|getAllList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|getAllList
operator|.
name|add
argument_list|(
name|testOSnetwork
argument_list|)
expr_stmt|;
name|getAllList
operator|.
name|add
argument_list|(
name|testOSnetwork
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|getAllList
argument_list|)
operator|.
name|when
argument_list|(
name|networkService
argument_list|)
operator|.
name|list
argument_list|()
expr_stmt|;
name|dummyNetwork
operator|=
name|createNetwork
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|testOSnetwork
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyNetwork
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSnetwork
operator|.
name|getTenantId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyNetwork
operator|.
name|getTenantId
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSnetwork
operator|.
name|getNetworkType
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyNetwork
operator|.
name|getNetworkType
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSnetwork
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createTest ()
specifier|public
name|void
name|createTest
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|OpenstackConstants
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|NAME
argument_list|,
name|dummyNetwork
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NeutronConstants
operator|.
name|NETWORK_TYPE
argument_list|,
name|dummyNetwork
operator|.
name|getNetworkType
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NeutronConstants
operator|.
name|TENANT_ID
argument_list|,
name|dummyNetwork
operator|.
name|getTenantId
argument_list|()
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|networkService
argument_list|)
operator|.
name|create
argument_list|(
name|networkCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsNetwork
argument_list|(
name|dummyNetwork
argument_list|,
name|networkCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|msg
operator|.
name|getBody
argument_list|(
name|Network
operator|.
name|class
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getTest ()
specifier|public
name|void
name|getTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|networkID
init|=
literal|"myNetID"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|OpenstackConstants
operator|.
name|GET
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|NeutronConstants
operator|.
name|NETWORK_ID
argument_list|,
name|networkID
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|networkService
argument_list|)
operator|.
name|get
argument_list|(
name|networkIdCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|networkID
argument_list|,
name|networkIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsNetwork
argument_list|(
name|testOSnetwork
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|Network
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getAllTest ()
specifier|public
name|void
name|getAllTest
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|OpenstackConstants
operator|.
name|GET_ALL
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
specifier|final
name|List
argument_list|<
name|Network
argument_list|>
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|size
argument_list|()
operator|==
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testOSnetwork
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteTest ()
specifier|public
name|void
name|deleteTest
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|networkService
operator|.
name|delete
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ActionResponse
operator|.
name|actionSuccess
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|String
name|networkID
init|=
literal|"myNetID"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|OpenstackConstants
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
argument_list|,
name|networkID
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|networkService
argument_list|)
operator|.
name|delete
argument_list|(
name|networkIdCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|networkID
argument_list|,
name|networkIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createNetwork ()
specifier|private
name|Network
name|createNetwork
parameter_list|()
block|{
return|return
name|Builders
operator|.
name|network
argument_list|()
operator|.
name|name
argument_list|(
literal|"name"
argument_list|)
operator|.
name|tenantId
argument_list|(
literal|"tenantID"
argument_list|)
operator|.
name|networkType
argument_list|(
name|NetworkType
operator|.
name|LOCAL
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|assertEqualsNetwork (Network old, Network newNetwork)
specifier|private
name|void
name|assertEqualsNetwork
parameter_list|(
name|Network
name|old
parameter_list|,
name|Network
name|newNetwork
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|old
operator|.
name|getName
argument_list|()
argument_list|,
name|newNetwork
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getTenantId
argument_list|()
argument_list|,
name|newNetwork
operator|.
name|getTenantId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getNetworkType
argument_list|()
argument_list|,
name|newNetwork
operator|.
name|getNetworkType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

