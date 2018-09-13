begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
DECL|class|MemoryStateRepositoryTest
specifier|public
class|class
name|MemoryStateRepositoryTest
block|{
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
name|MemoryStateRepository
name|repository
init|=
operator|new
name|MemoryStateRepository
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
name|MemoryStateRepository
name|repository
init|=
operator|new
name|MemoryStateRepository
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
block|}
end_class

end_unit

