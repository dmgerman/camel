begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.ftp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|ftp
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|FtpServerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|filesystem
operator|.
name|nativefs
operator|.
name|NativeFileSystemFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|ftplet
operator|.
name|UserManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|listener
operator|.
name|ListenerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|usermanager
operator|.
name|ClearTextPasswordEncryptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ftpserver
operator|.
name|usermanager
operator|.
name|impl
operator|.
name|PropertiesUserManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
comment|/**  *  */
end_comment

begin_class
DECL|class|FtpXQueryTest
specifier|public
class|class
name|FtpXQueryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|ftpPort
specifier|protected
specifier|static
name|int
name|ftpPort
decl_stmt|;
DECL|field|ftpServer
specifier|protected
name|FtpServer
name|ftpServer
decl_stmt|;
DECL|field|ftp
specifier|private
name|String
name|ftp
init|=
literal|"ftp:localhost:"
operator|+
name|ftpPort
operator|+
literal|"/myapp?password=admin&username=admin"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|initPort ()
specifier|public
specifier|static
name|void
name|initPort
parameter_list|()
throws|throws
name|Exception
block|{
name|ftpPort
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20127
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testXQueryFromFtp ()
specifier|public
name|void
name|testXQueryFromFtp
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:davsclaus"
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
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|MockEndpoint
name|other
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:other"
argument_list|)
decl_stmt|;
name|other
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|other
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftp
argument_list|,
literal|"<mail from=\"davsclaus@apache.org\"><subject>Hey</subject><body>Hello World!</body></mail>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"claus.xml"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|ftp
argument_list|,
literal|"<mail from=\"janstey@apache.org\"><subject>Hey</subject><body>Bye World!</body></mail>"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"janstey.xml"
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
name|ftp
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xquery
argument_list|(
literal|"/mail/@from = 'davsclaus@apache.org'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:davsclaus"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:other"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
name|initFtpServer
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ftpServer
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|initFtpServer ()
specifier|protected
name|void
name|initFtpServer
parameter_list|()
throws|throws
name|Exception
block|{
name|FtpServerFactory
name|serverFactory
init|=
operator|new
name|FtpServerFactory
argument_list|()
decl_stmt|;
comment|// setup user management to read our users.properties and use clear text passwords
name|File
name|file
init|=
operator|new
name|File
argument_list|(
literal|"src/test/resources/users.properties"
argument_list|)
decl_stmt|;
name|UserManager
name|uman
init|=
operator|new
name|PropertiesUserManager
argument_list|(
operator|new
name|ClearTextPasswordEncryptor
argument_list|()
argument_list|,
name|file
argument_list|,
literal|"admin"
argument_list|)
decl_stmt|;
name|serverFactory
operator|.
name|setUserManager
argument_list|(
name|uman
argument_list|)
expr_stmt|;
name|NativeFileSystemFactory
name|fsf
init|=
operator|new
name|NativeFileSystemFactory
argument_list|()
decl_stmt|;
name|fsf
operator|.
name|setCreateHome
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|setFileSystem
argument_list|(
name|fsf
argument_list|)
expr_stmt|;
name|ListenerFactory
name|factory
init|=
operator|new
name|ListenerFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
name|ftpPort
argument_list|)
expr_stmt|;
name|serverFactory
operator|.
name|addListener
argument_list|(
literal|"default"
argument_list|,
name|factory
operator|.
name|createListener
argument_list|()
argument_list|)
expr_stmt|;
name|ftpServer
operator|=
name|serverFactory
operator|.
name|createServer
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

