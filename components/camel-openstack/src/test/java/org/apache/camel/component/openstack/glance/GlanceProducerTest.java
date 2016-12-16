begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.glance
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
name|glance
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|mockito
operator|.
name|Spy
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
name|image
operator|.
name|ImageService
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
name|Payload
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
name|image
operator|.
name|ContainerFormat
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
name|image
operator|.
name|DiskFormat
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
name|image
operator|.
name|Image
import|;
end_import

begin_import
import|import
name|org
operator|.
name|openstack4j
operator|.
name|openstack
operator|.
name|image
operator|.
name|domain
operator|.
name|GlanceImage
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
name|assertNull
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
DECL|class|GlanceProducerTest
specifier|public
class|class
name|GlanceProducerTest
extends|extends
name|AbstractProducerTestSupport
block|{
annotation|@
name|Mock
DECL|field|endpoint
specifier|private
name|GlanceEndpoint
name|endpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|imageService
specifier|private
name|ImageService
name|imageService
decl_stmt|;
DECL|field|dummyImage
specifier|private
name|Image
name|dummyImage
decl_stmt|;
annotation|@
name|Spy
DECL|field|osImage
specifier|private
name|Image
name|osImage
init|=
name|Builders
operator|.
name|image
argument_list|()
operator|.
name|build
argument_list|()
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
name|GlanceProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|client
operator|.
name|images
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|imageService
argument_list|)
expr_stmt|;
name|dummyImage
operator|=
name|createImage
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|imageService
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
name|osImage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|imageService
operator|.
name|create
argument_list|(
name|any
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|Payload
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osImage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|imageService
operator|.
name|reserve
argument_list|(
name|any
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osImage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|imageService
operator|.
name|upload
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|(
name|Payload
operator|.
name|class
argument_list|)
argument_list|,
name|any
argument_list|(
name|GlanceImage
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osImage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getContainerFormat
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ContainerFormat
operator|.
name|BARE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getDiskFormat
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DiskFormat
operator|.
name|ISO
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyImage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getOwner
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyImage
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
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
DECL|method|reserveTest ()
specifier|public
name|void
name|reserveTest
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
name|GlanceConstants
operator|.
name|RESERVE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|dummyImage
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
name|Image
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|reserve
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyImage
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|Image
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|reserveWithHeadersTest ()
specifier|public
name|void
name|reserveWithHeadersTest
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
name|GlanceConstants
operator|.
name|RESERVE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|NAME
argument_list|,
name|dummyImage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CONTAINER_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getContainerFormat
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|DISK_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getDiskFormat
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CHECKSUM
argument_list|,
name|dummyImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_DISK
argument_list|,
name|dummyImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_RAM
argument_list|,
name|dummyImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OWNER
argument_list|,
name|dummyImage
operator|.
name|getOwner
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
specifier|final
name|ArgumentCaptor
argument_list|<
name|Image
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|reserve
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|result
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
name|GlanceConstants
operator|.
name|OPERATION
argument_list|,
name|GlanceConstants
operator|.
name|CREATE
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|NAME
argument_list|,
name|dummyImage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OWNER
argument_list|,
name|dummyImage
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_DISK
argument_list|,
name|dummyImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_RAM
argument_list|,
name|dummyImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CHECKSUM
argument_list|,
name|dummyImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|DISK_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getDiskFormat
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CONTAINER_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getContainerFormat
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|InputStream
name|is
init|=
operator|new
name|FileInputStream
argument_list|(
name|File
operator|.
name|createTempFile
argument_list|(
literal|"image"
argument_list|,
literal|".iso"
argument_list|)
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|is
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
name|ArgumentCaptor
argument_list|<
name|Payload
argument_list|>
name|payloadCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Payload
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
argument_list|>
name|imageCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|create
argument_list|(
name|imageCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|payloadCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|is
argument_list|,
name|payloadCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|open
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|uploadWithoutUpdatingTest ()
specifier|public
name|void
name|uploadWithoutUpdatingTest
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OPERATION
argument_list|,
name|GlanceConstants
operator|.
name|UPLOAD
argument_list|)
expr_stmt|;
specifier|final
name|String
name|id
init|=
literal|"id"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
specifier|final
name|File
name|file
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"image"
argument_list|,
literal|".iso"
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|file
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
name|ArgumentCaptor
argument_list|<
name|Payload
argument_list|>
name|payloadCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Payload
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|imageIdCaptor
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
specifier|final
name|ArgumentCaptor
argument_list|<
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
argument_list|>
name|imageCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|upload
argument_list|(
name|imageIdCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|payloadCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|imageCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|file
argument_list|,
name|payloadCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getRaw
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|imageIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|imageCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|uploadWithUpdatingTest ()
specifier|public
name|void
name|uploadWithUpdatingTest
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|newName
init|=
literal|"newName"
decl_stmt|;
name|dummyImage
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OPERATION
argument_list|,
name|GlanceConstants
operator|.
name|UPLOAD
argument_list|)
expr_stmt|;
specifier|final
name|String
name|id
init|=
literal|"id"
decl_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|ID
argument_list|,
name|id
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|NAME
argument_list|,
name|dummyImage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OWNER
argument_list|,
name|dummyImage
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_DISK
argument_list|,
name|dummyImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|MIN_RAM
argument_list|,
name|dummyImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CHECKSUM
argument_list|,
name|dummyImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|DISK_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getDiskFormat
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|CONTAINER_FORMAT
argument_list|,
name|dummyImage
operator|.
name|getContainerFormat
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|File
name|file
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"image"
argument_list|,
literal|".iso"
argument_list|)
decl_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|file
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
name|Payload
argument_list|>
name|payloadCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Payload
operator|.
name|class
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|imageIdCaptor
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
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
argument_list|>
name|imageCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|upload
argument_list|(
name|imageIdCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|payloadCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|imageCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|imageIdCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|file
argument_list|,
name|payloadCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getRaw
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|newName
argument_list|,
name|imageCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|Image
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|updateTest ()
specifier|public
name|void
name|updateTest
parameter_list|()
throws|throws
name|Exception
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|GlanceConstants
operator|.
name|OPERATION
argument_list|,
name|GlanceConstants
operator|.
name|UPDATE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|imageService
operator|.
name|update
argument_list|(
name|any
argument_list|(
name|Image
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|osImage
argument_list|)
expr_stmt|;
specifier|final
name|String
name|newName
init|=
literal|"newName"
decl_stmt|;
name|when
argument_list|(
name|osImage
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|dummyImage
operator|.
name|setName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
name|msg
operator|.
name|setBody
argument_list|(
name|dummyImage
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
name|ArgumentCaptor
argument_list|<
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
argument_list|>
name|imageCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|org
operator|.
name|openstack4j
operator|.
name|model
operator|.
name|image
operator|.
name|Image
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|imageService
argument_list|)
operator|.
name|update
argument_list|(
name|imageCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|dummyImage
argument_list|,
name|imageCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEqualsImages
argument_list|(
name|dummyImage
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|Image
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createImage ()
specifier|private
name|Image
name|createImage
parameter_list|()
block|{
return|return
name|Builders
operator|.
name|image
argument_list|()
operator|.
name|name
argument_list|(
literal|"Image Name"
argument_list|)
operator|.
name|diskFormat
argument_list|(
name|DiskFormat
operator|.
name|ISO
argument_list|)
operator|.
name|containerFormat
argument_list|(
name|ContainerFormat
operator|.
name|BARE
argument_list|)
operator|.
name|checksum
argument_list|(
literal|"checksum"
argument_list|)
operator|.
name|minDisk
argument_list|(
literal|10L
argument_list|)
operator|.
name|minRam
argument_list|(
literal|5L
argument_list|)
operator|.
name|owner
argument_list|(
literal|"owner"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|assertEqualsImages (Image original, Image newImage)
specifier|private
name|void
name|assertEqualsImages
parameter_list|(
name|Image
name|original
parameter_list|,
name|Image
name|newImage
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|original
operator|.
name|getContainerFormat
argument_list|()
argument_list|,
name|newImage
operator|.
name|getContainerFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getDiskFormat
argument_list|()
argument_list|,
name|newImage
operator|.
name|getDiskFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getChecksum
argument_list|()
argument_list|,
name|newImage
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getMinDisk
argument_list|()
argument_list|,
name|newImage
operator|.
name|getMinDisk
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getMinRam
argument_list|()
argument_list|,
name|newImage
operator|.
name|getMinRam
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getOwner
argument_list|()
argument_list|,
name|newImage
operator|.
name|getOwner
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|original
operator|.
name|getName
argument_list|()
argument_list|,
name|newImage
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

