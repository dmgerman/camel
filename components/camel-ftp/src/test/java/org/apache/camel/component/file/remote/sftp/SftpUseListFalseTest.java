begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.sftp
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
operator|.
name|sftp
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
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|SftpUseListFalseTest
specifier|public
class|class
name|SftpUseListFalseTest
extends|extends
name|SftpServerTestSupport
block|{
annotation|@
name|Test
DECL|method|testSftpUseListFalse ()
specifier|public
name|void
name|testSftpUseListFalse
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|String
name|expected
init|=
literal|"Hello World"
decl_stmt|;
comment|// create file using regular file
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file://"
operator|+
name|FTP_ROOT_DIR
argument_list|,
name|expected
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
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
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedHeaderReceived
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"report.txt"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|expected
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
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
literal|"sftp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|FTP_ROOT_DIR
operator|+
literal|"?username=admin&password=admin&delay=10s&disconnect=true&stepwise=false&useList=false&fileName=report.txt&delete=true"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
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

