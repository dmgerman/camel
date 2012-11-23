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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|FileConsumeSimpleRelativeMoveToAbsoluteTest
specifier|public
class|class
name|FileConsumeSimpleRelativeMoveToAbsoluteTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/move"
decl_stmt|;
DECL|field|base
specifier|private
name|String
name|base
decl_stmt|;
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
literal|"target/move"
argument_list|)
expr_stmt|;
comment|// use current dir as base as absolute path
name|base
operator|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"/target/move"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
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
literal|"Goodday World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"sub/sub2/goodday.txt"
argument_list|)
expr_stmt|;
block|}
DECL|method|testMoveToSubDir ()
specifier|public
name|void
name|testMoveToSubDir
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
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// will flatten when using absolute path in move
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|base
operator|+
literal|"/.done/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|base
operator|+
literal|"/.done/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|base
operator|+
literal|"/.done/goodday.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file://target/move?recursive=true&move="
operator|+
name|base
operator|+
literal|"/.done"
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

