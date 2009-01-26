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
name|CamelContext
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
name|TestSupport
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
name|DefaultCamelContext
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|UriConfigurationTest
specifier|public
class|class
name|UriConfigurationTest
extends|extends
name|TestSupport
block|{
DECL|field|context
specifier|protected
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|method|testFtpConfigurationDefaults ()
specifier|public
name|void
name|testFtpConfigurationDefaults
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ftp://hostname"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FtpRemoteFileEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|FtpRemoteFileEndpoint
name|ftpEndpoint
init|=
operator|(
name|FtpRemoteFileEndpoint
operator|)
name|endpoint
decl_stmt|;
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|ftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hostname"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|21
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|config
operator|.
name|isBinary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|config
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSftpConfigurationDefaults ()
specifier|public
name|void
name|testSftpConfigurationDefaults
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sftp://hostname"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SftpRemoteFileEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpRemoteFileEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpRemoteFileEndpoint
operator|)
name|endpoint
decl_stmt|;
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|sftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sftp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hostname"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|22
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|config
operator|.
name|isBinary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|config
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testFtpExplicitConfiguration ()
specifier|public
name|void
name|testFtpExplicitConfiguration
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ftp://user@hostname:1021/some/file?password=secret&binary=true&directory=false"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FtpRemoteFileEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|FtpRemoteFileEndpoint
name|ftpEndpoint
init|=
operator|(
name|FtpRemoteFileEndpoint
operator|)
name|endpoint
decl_stmt|;
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|ftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ftp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hostname"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1021
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|config
operator|.
name|isBinary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|config
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testSftpExplicitConfiguration ()
specifier|public
name|void
name|testSftpExplicitConfiguration
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sftp://user@hostname:1021/some/file?password=secret&binary=true&directory=false"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SftpRemoteFileEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpRemoteFileEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpRemoteFileEndpoint
operator|)
name|endpoint
decl_stmt|;
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|sftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sftp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hostname"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1021
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|config
operator|.
name|isBinary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|config
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testRemoteFileEndpointFiles ()
specifier|public
name|void
name|testRemoteFileEndpointFiles
parameter_list|()
block|{
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname/foo/bar"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname/foo/"
argument_list|,
literal|"foo/"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname/foo"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname//"
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname//foo/bar"
argument_list|,
literal|"/foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://user@hostname:123//foo/bar?password=secret"
argument_list|,
literal|"/foo/bar"
argument_list|)
expr_stmt|;
block|}
DECL|method|assertRemoteFileEndpointFile (String endpointUri, String expectedFile)
specifier|private
name|void
name|assertRemoteFileEndpointFile
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|expectedFile
parameter_list|)
block|{
name|RemoteFileEndpoint
name|endpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
name|context
argument_list|,
name|endpointUri
argument_list|,
name|RemoteFileEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Could not find endpoint: "
operator|+
name|endpointUri
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|String
name|file
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFile
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"For uri: "
operator|+
name|endpointUri
operator|+
literal|" the file is not equal"
argument_list|,
name|expectedFile
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
DECL|method|testSftpKnownHostsFileConfiguration ()
specifier|public
name|void
name|testSftpKnownHostsFileConfiguration
parameter_list|()
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"sftp://user@hostname:1021/some/file?password=secret&binary=true&directory=false&knownHostsFile=/home/janstey/.ssh/known_hosts"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SftpRemoteFileEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpRemoteFileEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpRemoteFileEndpoint
operator|)
name|endpoint
decl_stmt|;
name|RemoteFileConfiguration
name|config
init|=
operator|(
name|RemoteFileConfiguration
operator|)
name|sftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sftp"
argument_list|,
name|config
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hostname"
argument_list|,
name|config
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1021
argument_list|,
name|config
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user"
argument_list|,
name|config
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"secret"
argument_list|,
name|config
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|config
operator|.
name|isBinary
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|config
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"/home/janstey/.ssh/known_hosts"
argument_list|,
name|config
operator|.
name|getKnownHostsFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

