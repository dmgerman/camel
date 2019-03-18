begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
operator|.
name|remote
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
comment|/**  * Unit test to verify remotefile sortby option.  */
end_comment

begin_class
DECL|class|FromFtpRemoteFileSortByIgnoreCaseExpressionTest
specifier|public
class|class
name|FromFtpRemoteFileSortByIgnoreCaseExpressionTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/sortbyignore?password=admin&consumer.delay=5000"
return|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|prepareFtpServer
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
annotation|@
name|Test
DECL|method|testSortFiles ()
specifier|public
name|void
name|testSortFiles
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
name|getFtpUrl
argument_list|()
operator|+
literal|"&sortBy=file:name"
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
literal|"Hello London"
argument_list|,
literal|"Hello Copenhagen"
argument_list|,
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSortFilesNoCase ()
specifier|public
name|void
name|testSortFilesNoCase
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
name|getFtpUrl
argument_list|()
operator|+
literal|"&sortBy=ignoreCase:file:name"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:nocase"
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
name|nocase
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:nocase"
argument_list|)
decl_stmt|;
name|nocase
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Copenhagen"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"Hello Paris"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSortFilesNoCaseReverse ()
specifier|public
name|void
name|testSortFilesNoCaseReverse
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
name|getFtpUrl
argument_list|()
operator|+
literal|"&sortBy=reverse:ignoreCase:file:name"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:nocasereverse"
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
name|nocasereverse
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:nocasereverse"
argument_list|)
decl_stmt|;
name|nocasereverse
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello Paris"
argument_list|,
literal|"Hello London"
argument_list|,
literal|"Hello Copenhagen"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|prepareFtpServer ()
specifier|private
name|void
name|prepareFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// prepares the FTP Server by creating files on the server that we want to unit
comment|// test that we can pool
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello Paris"
argument_list|,
literal|"report-3.dat"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello London"
argument_list|,
literal|"REPORT-2.txt"
argument_list|)
expr_stmt|;
name|sendFile
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello Copenhagen"
argument_list|,
literal|"Report-1.xml"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

