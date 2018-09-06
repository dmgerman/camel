begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
operator|.
name|createDirectory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|TestSupport
operator|.
name|deleteDirectory
import|;
end_import

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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|collection
operator|.
name|IsIterableContainingInOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|FileIdempotentStoreOrderingTest
specifier|public
class|class
name|FileIdempotentStoreOrderingTest
block|{
DECL|field|fileIdempotentRepository
specifier|private
name|FileIdempotentRepository
name|fileIdempotentRepository
decl_stmt|;
DECL|field|files
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|files
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
block|{
name|files
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"file1.txt.20171123"
argument_list|,
literal|"file2.txt.20171123"
argument_list|,
literal|"file1.txt.20171124"
argument_list|,
literal|"file3.txt.20171125"
argument_list|,
literal|"file2.txt.20171126"
argument_list|,
literal|"fixed.income.lamr.out.20171126"
argument_list|,
literal|"pricing.px.20171126"
argument_list|,
literal|"test.out.20171126"
argument_list|,
literal|"processing.source.lamr.out.20171126"
argument_list|)
expr_stmt|;
name|this
operator|.
name|fileIdempotentRepository
operator|=
operator|new
name|FileIdempotentRepository
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrunkStoreNotMaxHit ()
specifier|public
name|void
name|testTrunkStoreNotMaxHit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure empty folder
name|deleteDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
comment|//given
name|File
name|fileStore
init|=
operator|new
name|File
argument_list|(
literal|"target/mystore/data.dat"
argument_list|)
decl_stmt|;
name|fileIdempotentRepository
operator|.
name|setFileStore
argument_list|(
name|fileStore
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|setCacheSize
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|start
argument_list|()
expr_stmt|;
name|files
operator|.
name|forEach
argument_list|(
name|e
lambda|->
name|fileIdempotentRepository
operator|.
name|add
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
comment|//when (will rebalance)
name|fileIdempotentRepository
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|//then
name|Stream
argument_list|<
name|String
argument_list|>
name|fileContent
init|=
name|Files
operator|.
name|lines
argument_list|(
name|fileStore
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileEntries
init|=
name|fileContent
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|fileContent
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//expected order
name|Assert
operator|.
name|assertThat
argument_list|(
name|fileEntries
argument_list|,
name|IsIterableContainingInOrder
operator|.
name|contains
argument_list|(
literal|"file1.txt.20171123"
argument_list|,
literal|"file2.txt.20171123"
argument_list|,
literal|"file1.txt.20171124"
argument_list|,
literal|"file3.txt.20171125"
argument_list|,
literal|"file2.txt.20171126"
argument_list|,
literal|"fixed.income.lamr.out.20171126"
argument_list|,
literal|"pricing.px.20171126"
argument_list|,
literal|"test.out.20171126"
argument_list|,
literal|"processing.source.lamr.out.20171126"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrunkStoreFirstLevelMaxHit ()
specifier|public
name|void
name|testTrunkStoreFirstLevelMaxHit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure empty folder
name|deleteDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
comment|//given
name|File
name|fileStore
init|=
operator|new
name|File
argument_list|(
literal|"target/mystore/data.dat"
argument_list|)
decl_stmt|;
name|fileIdempotentRepository
operator|.
name|setFileStore
argument_list|(
name|fileStore
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|setCacheSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|start
argument_list|()
expr_stmt|;
name|files
operator|.
name|forEach
argument_list|(
name|e
lambda|->
name|fileIdempotentRepository
operator|.
name|add
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
comment|//when (will rebalance)
name|fileIdempotentRepository
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|//then
name|Stream
argument_list|<
name|String
argument_list|>
name|fileContent
init|=
name|Files
operator|.
name|lines
argument_list|(
name|fileStore
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileEntries
init|=
name|fileContent
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|fileContent
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//expected order
name|Assert
operator|.
name|assertThat
argument_list|(
name|fileEntries
argument_list|,
name|IsIterableContainingInOrder
operator|.
name|contains
argument_list|(
literal|"file1.txt.20171123"
argument_list|,
literal|"file2.txt.20171123"
argument_list|,
literal|"file1.txt.20171124"
argument_list|,
literal|"file3.txt.20171125"
argument_list|,
literal|"file2.txt.20171126"
argument_list|,
literal|"fixed.income.lamr.out.20171126"
argument_list|,
literal|"pricing.px.20171126"
argument_list|,
literal|"test.out.20171126"
argument_list|,
literal|"processing.source.lamr.out.20171126"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrunkStoreFileMaxHit ()
specifier|public
name|void
name|testTrunkStoreFileMaxHit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ensure empty folder
name|deleteDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
literal|"target/mystore"
argument_list|)
expr_stmt|;
comment|//given
name|File
name|fileStore
init|=
operator|new
name|File
argument_list|(
literal|"target/mystore/data.dat"
argument_list|)
decl_stmt|;
name|fileIdempotentRepository
operator|.
name|setFileStore
argument_list|(
name|fileStore
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|setCacheSize
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|setMaxFileStoreSize
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|setDropOldestFileStore
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|start
argument_list|()
expr_stmt|;
name|files
operator|.
name|forEach
argument_list|(
name|e
lambda|->
name|fileIdempotentRepository
operator|.
name|add
argument_list|(
name|e
argument_list|)
argument_list|)
expr_stmt|;
comment|// force cleanup and trunk
name|fileIdempotentRepository
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|trunkStore
argument_list|()
expr_stmt|;
name|fileIdempotentRepository
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|//then
name|Stream
argument_list|<
name|String
argument_list|>
name|fileContent
init|=
name|Files
operator|.
name|lines
argument_list|(
name|fileStore
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|fileEntries
init|=
name|fileContent
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
name|fileContent
operator|.
name|close
argument_list|()
expr_stmt|;
comment|// all old entries is removed
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|fileEntries
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

