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
name|KeeperException
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
DECL|class|ExistenceChangedOperationTest
specifier|public
class|class
name|ExistenceChangedOperationTest
extends|extends
name|ZooKeeperTestSupport
block|{
annotation|@
name|Test
DECL|method|getStatsWhenNodeIsCreated ()
specifier|public
name|void
name|getStatsWhenNodeIsCreated
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"/doesNotYetExist"
decl_stmt|;
name|ExistenceChangedOperation
name|future
init|=
name|setupMonitor
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|client
operator|.
name|create
argument_list|(
name|path
argument_list|,
literal|"This is a test"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|path
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
name|assertNotNull
argument_list|(
name|future
operator|.
name|get
argument_list|()
operator|.
name|getStatistics
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
name|String
name|path
init|=
literal|"/soonToBeDeleted"
decl_stmt|;
name|client
operator|.
name|create
argument_list|(
name|path
argument_list|,
literal|"This is a test"
argument_list|)
expr_stmt|;
name|ExistenceChangedOperation
name|future
init|=
name|setupMonitor
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|client
operator|.
name|delete
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|path
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
name|assertNull
argument_list|(
name|future
operator|.
name|get
argument_list|()
operator|.
name|getStatistics
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setupMonitor (String path)
specifier|private
name|ExistenceChangedOperation
name|setupMonitor
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|KeeperException
throws|,
name|InterruptedException
block|{
name|ZooKeeper
name|connection
init|=
name|getConnection
argument_list|()
decl_stmt|;
name|ExistenceChangedOperation
name|future
init|=
operator|new
name|ExistenceChangedOperation
argument_list|(
name|connection
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|connection
operator|.
name|exists
argument_list|(
name|path
argument_list|,
name|future
argument_list|)
expr_stmt|;
return|return
name|future
return|;
block|}
block|}
end_class

end_unit

