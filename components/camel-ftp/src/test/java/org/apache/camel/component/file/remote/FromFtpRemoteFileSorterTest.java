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
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|file
operator|.
name|FileComponent
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_comment
comment|/**  * Unit test to verify remotefile sorter option.  */
end_comment

begin_class
DECL|class|FromFtpRemoteFileSorterTest
specifier|public
class|class
name|FromFtpRemoteFileSorterTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|port
specifier|private
name|int
name|port
init|=
literal|20095
decl_stmt|;
DECL|field|ftpUrl
specifier|private
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:"
operator|+
name|port
operator|+
literal|"/sorter?password=admin&sorter=#mySorter"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"mySorter"
argument_list|,
operator|new
name|MyRemoteFileSorter
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|testFtpSorter ()
specifier|public
name|void
name|testFtpSorter
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
name|mock
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
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|prepareFtpServer
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
name|String
name|ftpUrl
init|=
literal|"ftp://admin@localhost:"
operator|+
name|port
operator|+
literal|"/sorter/?password=admin"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
argument_list|,
literal|"Hello Paris"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"paris.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
argument_list|,
literal|"Hello London"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"london.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftpUrl
argument_list|,
literal|"Hello Copenhagen"
argument_list|,
name|FileComponent
operator|.
name|HEADER_FILE_NAME
argument_list|,
literal|"copenhagen.txt"
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
name|ftpUrl
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
comment|// START SNIPPET: e1
DECL|class|MyRemoteFileSorter
specifier|public
class|class
name|MyRemoteFileSorter
implements|implements
name|Comparator
argument_list|<
name|RemoteFile
argument_list|>
block|{
DECL|method|compare (RemoteFile o1, RemoteFile o2)
specifier|public
name|int
name|compare
parameter_list|(
name|RemoteFile
name|o1
parameter_list|,
name|RemoteFile
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getFileName
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getFileName
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|// END SNIPPET: e1
block|}
end_class

end_unit

