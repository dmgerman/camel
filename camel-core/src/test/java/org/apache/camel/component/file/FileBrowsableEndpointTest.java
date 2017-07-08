begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
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
name|ContextTestSupport
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
name|Exchange
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
name|processor
operator|.
name|idempotent
operator|.
name|MemoryIdempotentRepository
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
name|BrowsableEndpoint
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|FileBrowsableEndpointTest
specifier|public
class|class
name|FileBrowsableEndpointTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/browse"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|testBrowsableNoFiles ()
specifier|public
name|void
name|testBrowsableNoFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|BrowsableEndpoint
name|browse
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file:target/browse?initialDelay=0&delay=10"
argument_list|,
name|BrowsableEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|browse
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|browse
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBrowsableOneFile ()
specifier|public
name|void
name|testBrowsableOneFile
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"A"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|FileEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file:target/browse?initialDelay=0&delay=10"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|MemoryIdempotentRepository
name|repo
init|=
operator|(
name|MemoryIdempotentRepository
operator|)
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
comment|// the in progress repo should not leak
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// and the file is still there
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/a.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|file
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBrowsableTwoFiles ()
specifier|public
name|void
name|testBrowsableTwoFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"A"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"B"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"b.txt"
argument_list|)
expr_stmt|;
name|FileEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file:target/browse?initialDelay=0&delay=10&sortBy=file:name"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|MemoryIdempotentRepository
name|repo
init|=
operator|(
name|MemoryIdempotentRepository
operator|)
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
comment|// the in progress repo should not leak
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// and the files is still there
name|File
name|fileA
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/a.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|fileA
argument_list|,
name|fileA
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|fileB
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/b.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|fileB
argument_list|,
name|fileB
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testBrowsableThreeFilesRecursive ()
specifier|public
name|void
name|testBrowsableThreeFilesRecursive
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"A"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"a.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"B"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"foo/b.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/browse"
argument_list|,
literal|"C"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bar/c.txt"
argument_list|)
expr_stmt|;
name|FileEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file:target/browse?initialDelay=0&delay=10&recursive=true&sortBy=file:name"
argument_list|,
name|FileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|MemoryIdempotentRepository
name|repo
init|=
operator|(
name|MemoryIdempotentRepository
operator|)
name|endpoint
operator|.
name|getInProgressRepository
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|list
init|=
name|endpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"a.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"c.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"b.txt"
argument_list|,
name|list
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|)
argument_list|)
expr_stmt|;
comment|// the in progress repo should not leak
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCacheSize
argument_list|()
argument_list|)
expr_stmt|;
comment|// and the files is still there
name|File
name|fileA
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/a.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|fileA
argument_list|,
name|fileA
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|fileB
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/foo/b.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|fileB
argument_list|,
name|fileB
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|fileC
init|=
operator|new
name|File
argument_list|(
literal|"target/browse/bar/c.txt"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File should exist "
operator|+
name|fileC
argument_list|,
name|fileC
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

