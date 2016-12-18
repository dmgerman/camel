begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.swift
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
name|swift
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
name|swift
operator|.
name|producer
operator|.
name|ContainerProducer
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
name|Mock
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
name|storage
operator|.
name|object
operator|.
name|SwiftContainer
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
name|storage
operator|.
name|object
operator|.
name|options
operator|.
name|ContainerListOptions
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
name|storage
operator|.
name|object
operator|.
name|options
operator|.
name|CreateUpdateContainerOptions
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
name|Matchers
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
name|Matchers
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
DECL|class|ContainerProducerTest
specifier|public
class|class
name|ContainerProducerTest
extends|extends
name|SwiftProducerTestSupport
block|{
DECL|field|CONTAINER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CONTAINER_NAME
init|=
literal|"containerName"
decl_stmt|;
annotation|@
name|Mock
DECL|field|mockOsContainer
specifier|private
name|SwiftContainer
name|mockOsContainer
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|producer
operator|=
operator|new
name|ContainerProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createTestWithoutOptions ()
specifier|public
name|void
name|createTestWithoutOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|containerService
operator|.
name|create
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|(
name|CreateUpdateContainerOptions
operator|.
name|class
argument_list|)
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
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|containerNameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|CreateUpdateContainerOptions
argument_list|>
name|optionsCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|CreateUpdateContainerOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|containerService
argument_list|)
operator|.
name|create
argument_list|(
name|containerNameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|optionsCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTAINER_NAME
argument_list|,
name|containerNameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|optionsCaptor
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
block|}
annotation|@
name|Test
DECL|method|createTestWithOptions ()
specifier|public
name|void
name|createTestWithOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|containerService
operator|.
name|create
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|(
name|CreateUpdateContainerOptions
operator|.
name|class
argument_list|)
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
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
specifier|final
name|CreateUpdateContainerOptions
name|options
init|=
name|CreateUpdateContainerOptions
operator|.
name|create
argument_list|()
operator|.
name|accessAnybodyRead
argument_list|()
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|options
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|containerNameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|CreateUpdateContainerOptions
argument_list|>
name|optionsCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|CreateUpdateContainerOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|containerService
argument_list|)
operator|.
name|create
argument_list|(
name|containerNameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|optionsCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTAINER_NAME
argument_list|,
name|containerNameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|options
argument_list|,
name|optionsCaptor
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
name|List
argument_list|<
name|SwiftContainer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|mockOsContainer
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|list
argument_list|)
operator|.
name|when
argument_list|(
name|containerService
argument_list|)
operator|.
name|list
argument_list|(
name|any
argument_list|(
name|ContainerListOptions
operator|.
name|class
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
name|GET
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SwiftConstants
operator|.
name|LIMIT
argument_list|,
literal|10
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SwiftConstants
operator|.
name|DELIMITER
argument_list|,
literal|'x'
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|ContainerListOptions
argument_list|>
name|optionsCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ContainerListOptions
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|containerService
argument_list|)
operator|.
name|list
argument_list|(
name|optionsCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|options
init|=
name|optionsCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getOptions
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
literal|10
argument_list|)
argument_list|,
name|options
operator|.
name|get
argument_list|(
name|SwiftConstants
operator|.
name|LIMIT
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"x"
argument_list|,
name|options
operator|.
name|get
argument_list|(
name|SwiftConstants
operator|.
name|DELIMITER
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|list
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getAllFromContainerTest ()
specifier|public
name|void
name|getAllFromContainerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|SwiftContainer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|mockOsContainer
argument_list|)
expr_stmt|;
name|doReturn
argument_list|(
name|list
argument_list|)
operator|.
name|when
argument_list|(
name|containerService
argument_list|)
operator|.
name|list
argument_list|()
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
name|GET_ALL
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|mockOsContainer
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
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
DECL|method|deleteObjectTest ()
specifier|public
name|void
name|deleteObjectTest
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|containerService
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
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|containerNameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|containerService
argument_list|)
operator|.
name|delete
argument_list|(
name|containerNameCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTAINER_NAME
argument_list|,
name|containerNameCaptor
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
block|}
annotation|@
name|Test
DECL|method|deleteObjectFailTest ()
specifier|public
name|void
name|deleteObjectFailTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|failMessage
init|=
literal|"fail"
decl_stmt|;
name|when
argument_list|(
name|containerService
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
name|failMessage
argument_list|,
literal|401
argument_list|)
argument_list|)
expr_stmt|;
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
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
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
name|failMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createUpdateMetadataTest ()
specifier|public
name|void
name|createUpdateMetadataTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|md
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|md
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|SwiftConstants
operator|.
name|CREATE_UPDATE_METADATA
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|nameCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|Map
argument_list|>
name|dataCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|containerService
argument_list|)
operator|.
name|updateMetadata
argument_list|(
name|nameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|dataCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CONTAINER_NAME
argument_list|,
name|nameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|md
argument_list|,
name|dataCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getMetadataTest ()
specifier|public
name|void
name|getMetadataTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|md
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|md
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"val"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|containerService
operator|.
name|getMetadata
argument_list|(
name|CONTAINER_NAME
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|md
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|OPERATION
argument_list|,
name|SwiftConstants
operator|.
name|GET_METADATA
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|SwiftConstants
operator|.
name|CONTAINER_NAME
argument_list|,
name|CONTAINER_NAME
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|md
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

