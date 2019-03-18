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

begin_comment
comment|/**  * Unit test for the file sort by expression with nested groups  */
end_comment

begin_class
DECL|class|FileSortByNestedExpressionTest
specifier|public
class|class
name|FileSortByNestedExpressionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/data/filesorter/"
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
literal|"target/data/filesorter"
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
DECL|method|prepareFolder (String folder)
specifier|private
name|void
name|prepareFolder
parameter_list|(
name|String
name|folder
parameter_list|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/filesorter/"
operator|+
name|folder
argument_list|,
literal|"Hello Paris"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"paris.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/filesorter/"
operator|+
name|folder
argument_list|,
literal|"Hello London"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"london.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/filesorter/"
operator|+
name|folder
argument_list|,
literal|"Hello Copenhagen"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"copenhagen.xml"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/filesorter/"
operator|+
name|folder
argument_list|,
literal|"Hello Dublin"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"dublin.txt"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSortNestedFiles ()
specifier|public
name|void
name|testSortNestedFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareFolder
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
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
name|fileUrl
operator|+
literal|"a/?initialDelay=0&delay=10&sortBy=file:ext;file:name"
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello Dublin"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"Hello Paris"
argument_list|,
literal|"Hello Copenhagen"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSortNestedFilesReverse ()
specifier|public
name|void
name|testSortNestedFilesReverse
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareFolder
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
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
name|fileUrl
operator|+
literal|"b/?initialDelay=0&delay=10&sortBy=file:ext;reverse:file:name"
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
literal|"mock:reverse"
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
name|reverse
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:reverse"
argument_list|)
decl_stmt|;
name|reverse
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"Hello Dublin"
argument_list|,
literal|"Hello Copenhagen"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

