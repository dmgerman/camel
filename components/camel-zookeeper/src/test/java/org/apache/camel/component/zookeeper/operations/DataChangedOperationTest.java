begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.operations
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|operations
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
name|zookeeper
operator|.
name|ZooKeeperTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|ZooKeeper
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

begin_class
DECL|class|DataChangedOperationTest
specifier|public
class|class
name|DataChangedOperationTest
extends|extends
name|ZooKeeperTestSupport
block|{
annotation|@
name|Test
DECL|method|getsDataWhenNodeChanges ()
specifier|public
name|void
name|getsDataWhenNodeChanges
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|create
argument_list|(
literal|"/datachanged"
argument_list|,
literal|"this won't hurt a bit"
argument_list|)
expr_stmt|;
name|ZooKeeper
name|connection
init|=
name|getConnection
argument_list|()
decl_stmt|;
name|DataChangedOperation
name|future
init|=
operator|new
name|DataChangedOperation
argument_list|(
name|connection
argument_list|,
literal|"/datachanged"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|connection
operator|.
name|getData
argument_list|(
literal|"/datachanged"
argument_list|,
name|future
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setData
argument_list|(
literal|"/datachanged"
argument_list|,
literal|"Really trust us"
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"Really trust us"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|future
operator|.
name|get
argument_list|()
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getsNotifiedWhenNodeIsDeleted ()
specifier|public
name|void
name|getsNotifiedWhenNodeIsDeleted
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|create
argument_list|(
literal|"/existedButWasDeleted"
argument_list|,
literal|"this won't hurt a bit"
argument_list|)
expr_stmt|;
name|ZooKeeper
name|connection
init|=
name|getConnection
argument_list|()
decl_stmt|;
name|DataChangedOperation
name|future
init|=
operator|new
name|DataChangedOperation
argument_list|(
name|connection
argument_list|,
literal|"/existedButWasDeleted"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|connection
operator|.
name|getData
argument_list|(
literal|"/existedButWasDeleted"
argument_list|,
name|future
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|delete
argument_list|(
literal|"/existedButWasDeleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|future
operator|.
name|get
argument_list|()
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

