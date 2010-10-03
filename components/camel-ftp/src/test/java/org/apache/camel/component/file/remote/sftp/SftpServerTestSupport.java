begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|remote
operator|.
name|BaseServerTestSupport
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|SshServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|NamedFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|common
operator|.
name|keyprovider
operator|.
name|FileKeyPairProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|Command
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|command
operator|.
name|ScpCommandFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|sshd
operator|.
name|server
operator|.
name|sftp
operator|.
name|SftpSubsystem
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

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|SftpServerTestSupport
specifier|public
class|class
name|SftpServerTestSupport
extends|extends
name|BaseServerTestSupport
block|{
DECL|field|FTP_ROOT_DIR
specifier|protected
specifier|static
specifier|final
name|String
name|FTP_ROOT_DIR
init|=
literal|"res/home/"
decl_stmt|;
DECL|field|sshd
specifier|protected
name|SshServer
name|sshd
decl_stmt|;
DECL|field|canTest
specifier|protected
name|boolean
name|canTest
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
name|FTP_ROOT_DIR
argument_list|)
expr_stmt|;
name|canTest
operator|=
literal|false
expr_stmt|;
try|try
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|sshd
operator|=
name|SshServer
operator|.
name|setUpDefaultServer
argument_list|()
expr_stmt|;
name|sshd
operator|.
name|setPort
argument_list|(
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setKeyPairProvider
argument_list|(
operator|new
name|FileKeyPairProvider
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"src/test/resources/hostkey.pem"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setSubsystemFactories
argument_list|(
name|Arrays
operator|.
expr|<
name|NamedFactory
argument_list|<
name|Command
argument_list|>
operator|>
name|asList
argument_list|(
operator|new
name|SftpSubsystem
operator|.
name|Factory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setCommandFactory
argument_list|(
operator|new
name|ScpCommandFactory
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setPasswordAuthenticator
argument_list|(
operator|new
name|MyPasswordAuthenticator
argument_list|()
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore if algorithm is not on the OS
name|NoSuchAlgorithmException
name|nsae
init|=
name|ObjectHelper
operator|.
name|getException
argument_list|(
name|NoSuchAlgorithmException
operator|.
name|class
argument_list|,
name|e
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsae
operator|!=
literal|null
condition|)
block|{
name|canTest
operator|=
literal|false
expr_stmt|;
name|String
name|name
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"SunX509 is not avail on this platform ["
operator|+
name|name
operator|+
literal|"] Testing is skipped! Real cause: "
operator|+
name|nsae
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// some other error then throw it so the test can fail
throw|throw
name|e
throw|;
block|}
block|}
name|canTest
operator|=
literal|true
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|sshd
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|sshd
operator|.
name|stop
argument_list|()
expr_stmt|;
name|sshd
operator|=
literal|null
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore while shutting down as we could be polling during shutdown
comment|// and get errors when the ftp server is stopping. This is only an issue
comment|// since we host the ftp server embedded in the same jvm for unit testing
block|}
block|}
block|}
DECL|method|canTest ()
specifier|protected
name|boolean
name|canTest
parameter_list|()
block|{
return|return
name|canTest
return|;
block|}
block|}
end_class

end_unit

