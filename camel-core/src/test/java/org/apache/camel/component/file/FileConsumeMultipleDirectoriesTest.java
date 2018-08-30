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
name|java
operator|.
name|io
operator|.
name|File
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * Unit test for consuming multiple directories.  */
end_comment

begin_class
DECL|class|FileConsumeMultipleDirectoriesTest
specifier|public
class|class
name|FileConsumeMultipleDirectoriesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/multidir/?initialDelay=0&delay=10&recursive=true&delete=true&sortBy=file:path"
decl_stmt|;
annotation|@
name|Override
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
name|deleteDirectory
argument_list|(
literal|"target/multidir"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testMultiDir ()
specifier|public
name|void
name|testMultiDir
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|,
literal|"Hello World"
argument_list|,
literal|"Godday World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|fileUrl
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|fileUrl
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"sub/hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|fileUrl
argument_list|,
literal|"Godday World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"sub/sub2/godday.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|GenericFile
argument_list|<
name|File
argument_list|>
name|gf
init|=
operator|(
name|GenericFile
argument_list|<
name|File
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
decl_stmt|;
name|File
name|file
init|=
name|gf
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/multidir/bye.txt"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bye.txt"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|gf
operator|=
operator|(
name|GenericFile
argument_list|<
name|File
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
expr_stmt|;
name|file
operator|=
name|gf
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/multidir/sub/hello.txt"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello.txt"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|gf
operator|=
operator|(
name|GenericFile
argument_list|<
name|File
argument_list|>
operator|)
name|exchange
operator|.
name|getProperty
argument_list|(
name|FileComponent
operator|.
name|FILE_EXCHANGE_FILE
argument_list|)
expr_stmt|;
name|file
operator|=
name|gf
operator|.
name|getFile
argument_list|()
expr_stmt|;
name|assertDirectoryEquals
argument_list|(
literal|"target/multidir/sub/sub2/godday.txt"
argument_list|,
name|file
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"godday.txt"
argument_list|,
name|file
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|fileUrl
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

