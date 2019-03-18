begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.nova
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
name|nova
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
name|HashMap
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
name|Map
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
name|nova
operator|.
name|producer
operator|.
name|FlavorsProducer
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
name|compute
operator|.
name|FlavorService
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
name|compute
operator|.
name|Flavor
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
name|compute
operator|.
name|builder
operator|.
name|FlavorBuilder
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
name|assertNull
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
DECL|class|FlavorProducerTest
specifier|public
class|class
name|FlavorProducerTest
extends|extends
name|NovaProducerTestSupport
block|{
annotation|@
name|Mock
DECL|field|testOSFlavor
specifier|private
name|Flavor
name|testOSFlavor
decl_stmt|;
annotation|@
name|Mock
DECL|field|flavorService
specifier|private
name|FlavorService
name|flavorService
decl_stmt|;
annotation|@
name|Captor
DECL|field|flavorCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|Flavor
argument_list|>
name|flavorCaptor
decl_stmt|;
annotation|@
name|Captor
DECL|field|flavorIdCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|flavorIdCaptor
decl_stmt|;
DECL|field|dummyFlavor
specifier|private
name|Flavor
name|dummyFlavor
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
name|computeService
operator|.
name|flavors
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|flavorService
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|FlavorsProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|flavorService
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
name|testOSFlavor
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|flavorService
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
name|testOSFlavor
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|compute
operator|.
name|Flavor
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
name|testOSFlavor
argument_list|)
expr_stmt|;
name|getAllList
operator|.
name|add
argument_list|(
name|testOSFlavor
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|getAllList
argument_list|)
operator|.
name|when
argument_list|(
name|flavorService
argument_list|)
operator|.
name|list
argument_list|()
expr_stmt|;
name|dummyFlavor
operator|=
name|createTestFlavor
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|testOSFlavor
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
name|when
argument_list|(
name|testOSFlavor
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyFlavor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSFlavor
operator|.
name|getRam
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyFlavor
operator|.
name|getRam
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSFlavor
operator|.
name|getVcpus
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyFlavor
operator|.
name|getVcpus
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSFlavor
operator|.
name|getDisk
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyFlavor
operator|.
name|getDisk
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFlavor ()
specifier|public
name|void
name|createFlavor
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|OpenstackConstants
operator|.
name|CREATE
argument_list|)
expr_stmt|;
specifier|final
name|String
name|expectedFlavorID
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|when
argument_list|(
name|testOSFlavor
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|expectedFlavorID
argument_list|)
expr_stmt|;
comment|//send dummyFlavor to create
name|msg
operator|.
name|setBody
argument_list|(
name|dummyFlavor
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
name|flavorService
argument_list|)
operator|.
name|create
argument_list|(
name|flavorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyFlavor
argument_list|,
name|flavorCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Flavor
name|createdFlavor
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Flavor
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEqualsFlavors
argument_list|(
name|dummyFlavor
argument_list|,
name|createdFlavor
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|createdFlavor
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createFlavorWithHeaders ()
specifier|public
name|void
name|createFlavorWithHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
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
name|headers
operator|.
name|put
argument_list|(
name|OpenstackConstants
operator|.
name|NAME
argument_list|,
name|dummyFlavor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NovaConstants
operator|.
name|VCPU
argument_list|,
name|dummyFlavor
operator|.
name|getVcpus
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NovaConstants
operator|.
name|DISK
argument_list|,
name|dummyFlavor
operator|.
name|getDisk
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NovaConstants
operator|.
name|SWAP
argument_list|,
name|dummyFlavor
operator|.
name|getSwap
argument_list|()
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|NovaConstants
operator|.
name|RAM
argument_list|,
name|dummyFlavor
operator|.
name|getRam
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeaders
argument_list|(
name|headers
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
name|flavorService
argument_list|)
operator|.
name|create
argument_list|(
name|flavorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsFlavors
argument_list|(
name|dummyFlavor
argument_list|,
name|flavorCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Flavor
name|created
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Flavor
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|created
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsFlavors
argument_list|(
name|dummyFlavor
argument_list|,
name|created
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
name|OpenstackConstants
operator|.
name|ID
argument_list|,
literal|"anything - client is mocked"
argument_list|)
expr_stmt|;
comment|//should return dummyFlavor
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
specifier|final
name|Flavor
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Flavor
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEqualsFlavors
argument_list|(
name|dummyFlavor
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
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
name|when
argument_list|(
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
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
name|List
argument_list|<
name|Flavor
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
for|for
control|(
name|Flavor
name|f
range|:
name|result
control|)
block|{
name|assertEqualsFlavors
argument_list|(
name|dummyFlavor
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|f
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|deleteSuccess ()
specifier|public
name|void
name|deleteSuccess
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|flavorService
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
name|when
argument_list|(
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|OpenstackConstants
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|String
name|id
init|=
literal|"myID"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
argument_list|,
name|id
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
name|flavorService
argument_list|)
operator|.
name|delete
argument_list|(
name|flavorIdCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|flavorIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|msg
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|msg
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteFailure ()
specifier|public
name|void
name|deleteFailure
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|failReason
init|=
literal|"unknown"
decl_stmt|;
name|when
argument_list|(
name|flavorService
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
name|actionFailed
argument_list|(
name|failReason
argument_list|,
literal|401
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|OpenstackConstants
operator|.
name|DELETE
argument_list|)
expr_stmt|;
name|String
name|id
init|=
literal|"myID"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|ID
argument_list|,
name|id
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
name|flavorService
argument_list|)
operator|.
name|delete
argument_list|(
name|flavorIdCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|flavorIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|isFault
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|msg
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
name|failReason
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestFlavor ()
specifier|private
name|Flavor
name|createTestFlavor
parameter_list|()
block|{
name|FlavorBuilder
name|builder
init|=
name|Builders
operator|.
name|flavor
argument_list|()
operator|.
name|name
argument_list|(
literal|"dummy flavor"
argument_list|)
operator|.
name|ram
argument_list|(
literal|3
argument_list|)
operator|.
name|vcpus
argument_list|(
literal|2
argument_list|)
operator|.
name|disk
argument_list|(
literal|5
argument_list|)
operator|.
name|swap
argument_list|(
literal|2
argument_list|)
decl_stmt|;
return|return
name|builder
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|assertEqualsFlavors (Flavor old, Flavor createdFlavor)
specifier|private
name|void
name|assertEqualsFlavors
parameter_list|(
name|Flavor
name|old
parameter_list|,
name|Flavor
name|createdFlavor
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|old
operator|.
name|getName
argument_list|()
argument_list|,
name|createdFlavor
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getRam
argument_list|()
argument_list|,
name|createdFlavor
operator|.
name|getRam
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getVcpus
argument_list|()
argument_list|,
name|createdFlavor
operator|.
name|getVcpus
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getDisk
argument_list|()
argument_list|,
name|createdFlavor
operator|.
name|getDisk
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

