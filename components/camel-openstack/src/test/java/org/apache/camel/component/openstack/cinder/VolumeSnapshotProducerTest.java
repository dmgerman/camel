begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.openstack.cinder
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
name|cinder
package|;
end_package

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
name|cinder
operator|.
name|producer
operator|.
name|SnapshotProducer
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
name|storage
operator|.
name|BlockVolumeSnapshotService
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
name|block
operator|.
name|VolumeSnapshot
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
DECL|class|VolumeSnapshotProducerTest
specifier|public
class|class
name|VolumeSnapshotProducerTest
extends|extends
name|CinderProducerTestSupport
block|{
annotation|@
name|Mock
DECL|field|snapshotService
specifier|private
name|BlockVolumeSnapshotService
name|snapshotService
decl_stmt|;
annotation|@
name|Mock
DECL|field|testOSVolumeSnapshot
specifier|private
name|VolumeSnapshot
name|testOSVolumeSnapshot
decl_stmt|;
annotation|@
name|Captor
DECL|field|idCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|idCaptor
decl_stmt|;
annotation|@
name|Captor
DECL|field|nameCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|nameCaptor
decl_stmt|;
annotation|@
name|Captor
DECL|field|descCaptor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|descCaptor
decl_stmt|;
annotation|@
name|Captor
DECL|field|captor
specifier|private
name|ArgumentCaptor
argument_list|<
name|String
argument_list|>
name|captor
decl_stmt|;
DECL|field|dummyVolumeSnapshot
specifier|private
name|VolumeSnapshot
name|dummyVolumeSnapshot
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
name|blockStorageService
operator|.
name|snapshots
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|snapshotService
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|SnapshotProducer
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|snapshotService
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
name|testOSVolumeSnapshot
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|snapshotService
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
name|testOSVolumeSnapshot
argument_list|)
expr_stmt|;
name|dummyVolumeSnapshot
operator|=
name|createTestVolume
argument_list|()
expr_stmt|;
name|when
argument_list|(
name|testOSVolumeSnapshot
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
name|testOSVolumeSnapshot
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyVolumeSnapshot
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSVolumeSnapshot
operator|.
name|getDescription
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyVolumeSnapshot
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|testOSVolumeSnapshot
operator|.
name|getVolumeId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|dummyVolumeSnapshot
operator|.
name|getVolumeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createVolumeTest ()
specifier|public
name|void
name|createVolumeTest
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
name|msg
operator|.
name|setBody
argument_list|(
name|dummyVolumeSnapshot
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
name|VolumeSnapshot
name|result
init|=
name|msg
operator|.
name|getBody
argument_list|(
name|VolumeSnapshot
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEqualsVolumeSnapshots
argument_list|(
name|dummyVolumeSnapshot
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
DECL|method|updateVolumeSnapshotTest ()
specifier|public
name|void
name|updateVolumeSnapshotTest
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|snapshotService
operator|.
name|update
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
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
name|UPDATE
argument_list|)
expr_stmt|;
specifier|final
name|String
name|id
init|=
literal|"id"
decl_stmt|;
specifier|final
name|String
name|desc
init|=
literal|"newDesc"
decl_stmt|;
specifier|final
name|String
name|name
init|=
literal|"newName"
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
name|msg
operator|.
name|setHeader
argument_list|(
name|OpenstackConstants
operator|.
name|DESCRIPTION
argument_list|,
name|desc
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
name|name
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
name|snapshotService
argument_list|)
operator|.
name|update
argument_list|(
name|idCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|nameCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|descCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|idCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|nameCaptor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|desc
argument_list|,
name|descCaptor
operator|.
name|getValue
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
DECL|method|getVolumeSnapshotTest ()
specifier|public
name|void
name|getVolumeSnapshotTest
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
literal|"anyID"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEqualsVolumeSnapshots
argument_list|(
name|dummyVolumeSnapshot
argument_list|,
name|msg
operator|.
name|getBody
argument_list|(
name|VolumeSnapshot
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|deleteVolumeSnapshotTest ()
specifier|public
name|void
name|deleteVolumeSnapshotTest
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
name|DELETE
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|snapshotService
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
name|id
init|=
literal|"id"
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
name|snapshotService
argument_list|)
operator|.
name|delete
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|assertEqualsVolumeSnapshots (VolumeSnapshot old, VolumeSnapshot newVolumeSn)
specifier|private
name|void
name|assertEqualsVolumeSnapshots
parameter_list|(
name|VolumeSnapshot
name|old
parameter_list|,
name|VolumeSnapshot
name|newVolumeSn
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|old
operator|.
name|getName
argument_list|()
argument_list|,
name|newVolumeSn
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getDescription
argument_list|()
argument_list|,
name|newVolumeSn
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|old
operator|.
name|getVolumeId
argument_list|()
argument_list|,
name|newVolumeSn
operator|.
name|getVolumeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createTestVolume ()
specifier|private
name|VolumeSnapshot
name|createTestVolume
parameter_list|()
block|{
return|return
name|Builders
operator|.
name|volumeSnapshot
argument_list|()
operator|.
name|description
argument_list|(
literal|"descr"
argument_list|)
operator|.
name|name
argument_list|(
literal|"name"
argument_list|)
operator|.
name|volume
argument_list|(
literal|"volId"
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

