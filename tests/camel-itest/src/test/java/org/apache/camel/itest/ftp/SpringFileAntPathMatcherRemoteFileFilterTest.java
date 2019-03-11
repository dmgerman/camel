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
name|java
operator|.
name|util
operator|.
name|Locale
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
name|Endpoint
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
name|EndpointInject
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
name|ProducerTemplate
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_comment
comment|/**  * Unit testing FTP ant path matcher  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|SpringFileAntPathMatcherRemoteFileFilterTest
specifier|public
class|class
name|SpringFileAntPathMatcherRemoteFileFilterTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|ftpPort
specifier|private
specifier|static
name|int
name|ftpPort
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|20123
argument_list|)
decl_stmt|;
static|static
block|{
comment|//set them as system properties so Spring can use the property placeholder
comment|//things to set them into the URL's in the spring contexts
name|System
operator|.
name|setProperty
argument_list|(
literal|"SpringFileAntPathMatcherRemoteFileFilterTest.ftpPort"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|ftpPort
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|field|ftpServer
specifier|protected
name|FtpServer
name|ftpServer
decl_stmt|;
DECL|field|expectedBody
specifier|protected
name|String
name|expectedBody
init|=
literal|"Godday World"
decl_stmt|;
annotation|@
name|Autowired
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"ref:myFTPEndpoint"
argument_list|)
DECL|field|inputFTP
specifier|protected
name|Endpoint
name|inputFTP
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|protected
name|MockEndpoint
name|result
decl_stmt|;
DECL|method|canRunOnThisPlatform ()
specifier|protected
name|boolean
name|canRunOnThisPlatform
parameter_list|()
block|{
name|String
name|os
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
name|boolean
name|aix
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"aix"
argument_list|)
decl_stmt|;
name|boolean
name|windows
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"windows"
argument_list|)
decl_stmt|;
name|boolean
name|solaris
init|=
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|ENGLISH
argument_list|)
operator|.
name|contains
argument_list|(
literal|"sunos"
argument_list|)
decl_stmt|;
comment|// Does not work on AIX / solaris and the problem is hard to identify, could be issues not allowing to use a custom port
comment|// java.io.IOException: Failed to retrieve RMIServer stub: javax.naming.NameNotFoundException: jmxrmi/camel
comment|// windows CI servers is often slow/tricky so skip as well
return|return
operator|!
name|aix
operator|&&
operator|!
name|solaris
operator|&&
operator|!
name|windows
return|;
block|}
annotation|@
name|Test
DECL|method|testAntPatchMatherFilter ()
specifier|public
name|void
name|testAntPatchMatherFilter
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canRunOnThisPlatform
argument_list|()
condition|)
block|{
return|return;
block|}
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|inputFTP
argument_list|,
literal|"Hello World"
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
name|inputFTP
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.xml"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|inputFTP
argument_list|,
literal|"Bad world"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"subfolder/badday.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|inputFTP
argument_list|,
literal|"Day world"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"day.xml"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|inputFTP
argument_list|,
name|expectedBody
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"subfolder/foo/godday.txt"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|initFtpServer
argument_list|()
expr_stmt|;
name|ftpServer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
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

