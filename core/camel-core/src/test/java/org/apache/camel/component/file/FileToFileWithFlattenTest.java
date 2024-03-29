begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
DECL|class|FileToFileWithFlattenTest
specifier|public
class|class
name|FileToFileWithFlattenTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/data/flatten-in"
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
literal|"target/data/flatten-in"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/data/flatten-out"
argument_list|)
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
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFlatternConsumer ()
specifier|public
name|void
name|testFlatternConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"file://target/data/flatten-in?initialDelay=0&delay=10&recursive=true&flatten=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/data/flatten-out"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
comment|// flatten files
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/goodday.txt"
argument_list|)
expr_stmt|;
comment|// default move files
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/.camel/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/sub/.camel/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/sub/sub2/.camel/goodday.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFlatternProducer ()
specifier|public
name|void
name|testFlatternProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|addRoutes
argument_list|(
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
literal|"file://target/data/flatten-in?initialDelay=0&delay=10&recursive=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"file://target/data/flatten-out?flatten=true"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
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
comment|// flatten files
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-out/goodday.txt"
argument_list|)
expr_stmt|;
comment|// default move files
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/.camel/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/sub/.camel/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
literal|"target/data/flatten-in/sub/sub2/.camel/goodday.txt"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

