begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FromFtpNoEndpointPathRelativeMoveToAbsoluteTest
specifier|public
class|class
name|FromFtpNoEndpointPathRelativeMoveToAbsoluteTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|method|getFtpUrl ()
specifier|protected
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
literal|"?password=admin&recursive=true&binary=false"
operator|+
literal|"&move=/.done/${file:name}&initialDelay=2500&delay=5000"
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
name|Test
DECL|method|testPollFileAndShouldBeMoved ()
specifier|public
name|void
name|testPollFileAndShouldBeMoved
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
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"Hello"
argument_list|,
literal|"Bye"
argument_list|,
literal|"Goodday"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|".done/hello.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|".done/sub/bye.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedFileExists
argument_list|(
name|FTP_ROOT_DIR
operator|+
literal|".done/sub/sub2/goodday.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|assertIsSatisfied
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Bye"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"sub/bye.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Goodday"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"sub/sub2/goodday.txt"
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
name|getFtpUrl
argument_list|()
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

