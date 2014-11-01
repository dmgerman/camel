begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|IOException
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|UUID
operator|.
name|randomUUID
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
name|spi
operator|.
name|IdempotentRepository
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
name|processor
operator|.
name|idempotent
operator|.
name|FileIdempotentRepository
operator|.
name|fileIdempotentRepository
import|;
end_import

begin_class
DECL|class|FileIdempotentConsumerCreateRepoTest
specifier|public
class|class
name|FileIdempotentConsumerCreateRepoTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|shouldCreateParentOfRepositoryFileStore ()
specifier|public
name|void
name|shouldCreateParentOfRepositoryFileStore
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|File
name|parentDirectory
init|=
operator|new
name|File
argument_list|(
literal|"target/repositoryParent_"
operator|+
name|randomUUID
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|store
init|=
operator|new
name|File
argument_list|(
name|parentDirectory
argument_list|,
literal|"store"
argument_list|)
decl_stmt|;
name|IdempotentRepository
argument_list|<
name|String
argument_list|>
name|repo
init|=
name|fileIdempotentRepository
argument_list|(
name|store
argument_list|)
decl_stmt|;
comment|// must start repo
name|repo
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// When
name|repo
operator|.
name|add
argument_list|(
literal|"anyKey"
argument_list|)
expr_stmt|;
comment|// Then
name|assertTrue
argument_list|(
name|store
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

