begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|CreateMode
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
name|ZooDefs
operator|.
name|Ids
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
name|ZooDefs
operator|.
name|Perms
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
name|apache
operator|.
name|zookeeper
operator|.
name|data
operator|.
name|ACL
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

begin_class
DECL|class|CreateOperationTest
specifier|public
class|class
name|CreateOperationTest
extends|extends
name|ZooKeeperTestSupport
block|{
DECL|field|connection
specifier|private
name|ZooKeeper
name|connection
decl_stmt|;
annotation|@
name|Before
DECL|method|setupConnection ()
specifier|public
name|void
name|setupConnection
parameter_list|()
block|{
name|connection
operator|=
name|getConnection
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBasic ()
specifier|public
name|void
name|createBasic
parameter_list|()
throws|throws
name|Exception
block|{
name|CreateOperation
name|create
init|=
operator|new
name|CreateOperation
argument_list|(
name|connection
argument_list|,
literal|"/one"
argument_list|)
decl_stmt|;
name|OperationResult
argument_list|<
name|String
argument_list|>
name|result
init|=
name|create
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/one"
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|verifyNodeContainsData
argument_list|(
literal|"/one"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createBasicWithData ()
specifier|public
name|void
name|createBasicWithData
parameter_list|()
throws|throws
name|Exception
block|{
name|CreateOperation
name|create
init|=
operator|new
name|CreateOperation
argument_list|(
name|connection
argument_list|,
literal|"/two"
argument_list|)
decl_stmt|;
name|create
operator|.
name|setData
argument_list|(
name|testPayload
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|OperationResult
argument_list|<
name|String
argument_list|>
name|result
init|=
name|create
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/two"
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|verifyNodeContainsData
argument_list|(
literal|"/two"
argument_list|,
name|testPayloadBytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSequencedNodeToTestCreateMode ()
specifier|public
name|void
name|createSequencedNodeToTestCreateMode
parameter_list|()
throws|throws
name|Exception
block|{
name|CreateOperation
name|create
init|=
operator|new
name|CreateOperation
argument_list|(
name|connection
argument_list|,
literal|"/three"
argument_list|)
decl_stmt|;
name|create
operator|.
name|setData
argument_list|(
name|testPayload
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|create
operator|.
name|setCreateMode
argument_list|(
name|CreateMode
operator|.
name|EPHEMERAL_SEQUENTIAL
argument_list|)
expr_stmt|;
name|OperationResult
argument_list|<
name|String
argument_list|>
name|result
init|=
name|create
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/three0000000002"
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|verifyNodeContainsData
argument_list|(
literal|"/three0000000002"
argument_list|,
name|testPayloadBytes
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createNodeWithSpecificAccess ()
specifier|public
name|void
name|createNodeWithSpecificAccess
parameter_list|()
throws|throws
name|Exception
block|{
name|CreateOperation
name|create
init|=
operator|new
name|CreateOperation
argument_list|(
name|connection
argument_list|,
literal|"/four"
argument_list|)
decl_stmt|;
name|create
operator|.
name|setData
argument_list|(
name|testPayload
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ACL
argument_list|>
name|perms
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|ACL
argument_list|(
name|Perms
operator|.
name|CREATE
argument_list|,
name|Ids
operator|.
name|ANYONE_ID_UNSAFE
argument_list|)
argument_list|)
decl_stmt|;
name|create
operator|.
name|setPermissions
argument_list|(
name|perms
argument_list|)
expr_stmt|;
name|OperationResult
argument_list|<
name|String
argument_list|>
name|result
init|=
name|create
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"/four"
argument_list|,
name|result
operator|.
name|getResult
argument_list|()
argument_list|)
expr_stmt|;
name|verifyAccessControlList
argument_list|(
literal|"/four"
argument_list|,
name|perms
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

