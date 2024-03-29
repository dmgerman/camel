begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|nio
operator|.
name|file
operator|.
name|Files
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
name|impl
operator|.
name|engine
operator|.
name|FileStateRepository
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
operator|.
name|FileStateRepository
operator|.
name|fileStateRepository
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|FileStateRepositoryTest
specifier|public
class|class
name|FileStateRepositoryTest
block|{
DECL|field|repositoryStore
specifier|private
specifier|final
name|File
name|repositoryStore
init|=
operator|new
name|File
argument_list|(
literal|"target/data/file-state-repository.dat"
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Remove the repository file if needed
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|repositoryStore
operator|.
name|toPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|shouldPreventUsingDelimiterInKey ()
specifier|public
name|void
name|shouldPreventUsingDelimiterInKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository
name|FileStateRepository
name|repository
init|=
name|fileStateRepository
argument_list|(
name|repositoryStore
argument_list|)
decl_stmt|;
comment|// When trying to use the key delimiter in a key
name|repository
operator|.
name|setState
argument_list|(
literal|"="
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// Then an exception is thrown
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|shouldPreventUsingNewLineInKey ()
specifier|public
name|void
name|shouldPreventUsingNewLineInKey
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
comment|// When trying to use new line in a key
name|repository
operator|.
name|setState
argument_list|(
literal|"\n"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// Then an exception is thrown
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|shouldPreventUsingNewLineInValue ()
specifier|public
name|void
name|shouldPreventUsingNewLineInValue
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
comment|// When trying to use new line in a key
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"\n"
argument_list|)
expr_stmt|;
comment|// Then an exception is thrown
block|}
annotation|@
name|Test
DECL|method|shouldSaveState ()
specifier|public
name|void
name|shouldSaveState
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given an empty FileStateRepository
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
comment|// When saving a state
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// Then it should be retrieved afterwards
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|repository
operator|.
name|getState
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUpdateState ()
specifier|public
name|void
name|shouldUpdateState
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository with a state in it
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// When updating the state
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
comment|// Then the new value should be retrieved afterwards
name|assertEquals
argument_list|(
literal|"value2"
argument_list|,
name|repository
operator|.
name|getState
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSynchronizeInFile ()
specifier|public
name|void
name|shouldSynchronizeInFile
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository with some content
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setState
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setState
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setState
argument_list|(
literal|"key3"
argument_list|,
literal|"value3"
argument_list|)
expr_stmt|;
comment|// When creating a new FileStateRepository with same file
name|FileStateRepository
name|newRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
comment|// Then the new one should have the same content
name|assertEquals
argument_list|(
literal|"value1"
argument_list|,
name|newRepository
operator|.
name|getState
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value2"
argument_list|,
name|newRepository
operator|.
name|getState
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value3"
argument_list|,
name|newRepository
operator|.
name|getState
argument_list|(
literal|"key3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldPreventRepositoryFileFromGrowingInfinitely ()
specifier|public
name|void
name|shouldPreventRepositoryFileFromGrowingInfinitely
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given a FileStateRepository with a maximum size of 100 bytes
name|FileStateRepository
name|repository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|repository
operator|.
name|setMaxFileStoreSize
argument_list|(
literal|100
argument_list|)
expr_stmt|;
comment|// And content just to this limit (10x10 bytes)
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"xxxxx"
operator|.
name|replace
argument_list|(
literal|'x'
argument_list|,
call|(
name|char
call|)
argument_list|(
literal|'0'
operator|+
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|long
name|previousSize
init|=
name|repositoryStore
operator|.
name|length
argument_list|()
decl_stmt|;
comment|// When updating the state
name|repository
operator|.
name|setState
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
comment|// Then it should be truncated
name|assertTrue
argument_list|(
name|repositoryStore
operator|.
name|length
argument_list|()
operator|<
name|previousSize
argument_list|)
expr_stmt|;
block|}
DECL|method|createRepository ()
specifier|private
name|FileStateRepository
name|createRepository
parameter_list|()
throws|throws
name|Exception
block|{
name|FileStateRepository
name|repository
init|=
name|fileStateRepository
argument_list|(
name|repositoryStore
argument_list|)
decl_stmt|;
name|repository
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|repository
return|;
block|}
block|}
end_class

end_unit

