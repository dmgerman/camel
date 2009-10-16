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
name|Processor
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
name|junit
operator|.
name|Test
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
name|CamelTestSupport
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
annotation|@
name|Test
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
name|FtpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|FtpEndpoint
name|ftpEndpoint
init|=
operator|(
name|FtpEndpoint
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
block|}
annotation|@
name|Test
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
name|SftpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpEndpoint
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
block|}
annotation|@
name|Test
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
literal|"ftp://user@hostname:1021/some/file?password=secret&binary=true"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|FtpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|FtpEndpoint
name|ftpEndpoint
init|=
operator|(
name|FtpEndpoint
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
block|}
annotation|@
name|Test
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
literal|"sftp://user@hostname:1021/some/file?password=secret&binary=true"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SftpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpEndpoint
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
block|}
annotation|@
name|Test
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
literal|"ftp://hostname/foo/bar/"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname/foo/"
argument_list|,
literal|"foo"
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
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname//foo/bar"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"ftp://hostname//foo/bar/"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://user@hostname:123//foo/bar?password=secret"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://user@hostname:123?password=secret"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname/foo/bar"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname/foo/bar/"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname/foo/"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname/foo"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname/"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname//"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname//foo/bar"
argument_list|,
literal|"foo/bar"
argument_list|)
expr_stmt|;
name|assertRemoteFileEndpointFile
argument_list|(
literal|"sftp://hostname//foo/bar/"
argument_list|,
literal|"foo/bar"
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
name|getDirectory
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
annotation|@
name|Test
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
literal|"sftp://user@hostname:1021/some/file?password=secret&binary=true&knownHostsFile=/home/janstey/.ssh/known_hosts"
argument_list|)
decl_stmt|;
name|assertIsInstanceOf
argument_list|(
name|SftpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|SftpEndpoint
name|sftpEndpoint
init|=
operator|(
name|SftpEndpoint
operator|)
name|endpoint
decl_stmt|;
name|SftpConfiguration
name|config
init|=
operator|(
name|SftpConfiguration
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
literal|"/home/janstey/.ssh/known_hosts"
argument_list|,
name|config
operator|.
name|getKnownHostsFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvalidStartingDirectory ()
specifier|public
name|void
name|testInvalidStartingDirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ftp://user@hostname?password=secret"
argument_list|)
decl_stmt|;
name|FtpEndpoint
name|ftpEndpoint
init|=
name|assertIsInstanceOf
argument_list|(
name|FtpEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|FtpConfiguration
name|config
init|=
operator|(
name|FtpConfiguration
operator|)
name|ftpEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setHost
argument_list|(
literal|"somewhere"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDirectory
argument_list|(
literal|"some/file.txt"
argument_list|)
expr_stmt|;
try|try
block|{
name|ftpEndpoint
operator|.
name|createConsumer
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// do nothing
block|}
block|}
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Only directory is supported. Endpoint must be configured with a valid directory: some/file.txt"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

