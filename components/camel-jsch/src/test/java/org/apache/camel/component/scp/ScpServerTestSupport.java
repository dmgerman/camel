begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.scp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|scp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Provider
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
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
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSch
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|JSchException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|UserInfo
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
name|server
operator|.
name|auth
operator|.
name|password
operator|.
name|PasswordAuthenticator
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
name|auth
operator|.
name|pubkey
operator|.
name|PublickeyAuthenticator
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
name|scp
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
name|session
operator|.
name|ServerSession
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
name|subsystem
operator|.
name|sftp
operator|.
name|SftpSubsystemFactory
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
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ScpServerTestSupport
specifier|public
specifier|abstract
class|class
name|ScpServerTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ScpServerTestSupport
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SCP_ROOT_DIR
specifier|protected
specifier|static
specifier|final
name|String
name|SCP_ROOT_DIR
init|=
literal|"target/test-classes/scp"
decl_stmt|;
DECL|field|KNOWN_HOSTS
specifier|protected
specifier|static
specifier|final
name|String
name|KNOWN_HOSTS
init|=
literal|"known_hosts"
decl_stmt|;
DECL|field|port
specifier|protected
specifier|static
name|int
name|port
decl_stmt|;
DECL|field|acceptLocalhostConnections
specifier|private
name|boolean
name|acceptLocalhostConnections
init|=
literal|true
decl_stmt|;
DECL|field|knownHostsFile
specifier|private
name|String
name|knownHostsFile
decl_stmt|;
DECL|field|setupComplete
specifier|private
name|boolean
name|setupComplete
decl_stmt|;
DECL|field|sshd
specifier|private
name|SshServer
name|sshd
decl_stmt|;
DECL|method|ScpServerTestSupport ()
specifier|protected
name|ScpServerTestSupport
parameter_list|()
block|{
name|this
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|ScpServerTestSupport (boolean acceptLocalhostConnections)
specifier|protected
name|ScpServerTestSupport
parameter_list|(
name|boolean
name|acceptLocalhostConnections
parameter_list|)
block|{
name|this
operator|.
name|acceptLocalhostConnections
operator|=
name|acceptLocalhostConnections
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|protected
name|int
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|getSshd ()
specifier|protected
name|SshServer
name|getSshd
parameter_list|()
block|{
return|return
name|sshd
return|;
block|}
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|21000
argument_list|)
expr_stmt|;
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
name|deleteDirectory
argument_list|(
name|getScpPath
argument_list|()
argument_list|)
expr_stmt|;
name|createDirectory
argument_list|(
name|getScpPath
argument_list|()
argument_list|)
expr_stmt|;
name|setupComplete
operator|=
name|startSshd
argument_list|()
expr_stmt|;
name|setupKnownHosts
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
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
argument_list|(
literal|true
argument_list|)
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
comment|// and get errors when the ssh server is stopping.
block|}
block|}
name|deleteDirectory
argument_list|(
name|getScpPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getScpPath ()
specifier|protected
specifier|final
name|String
name|getScpPath
parameter_list|()
block|{
comment|// use this convention and use separate directories for tests
comment|// (easier to debug and avoid interference)
return|return
name|SCP_ROOT_DIR
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
return|;
block|}
DECL|method|getScpUri ()
specifier|protected
name|String
name|getScpUri
parameter_list|()
block|{
return|return
literal|"scp://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/"
operator|+
name|getScpPath
argument_list|()
return|;
block|}
DECL|method|startSshd ()
specifier|protected
name|boolean
name|startSshd
parameter_list|()
block|{
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
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/resources/hostkey.pem"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setSubsystemFactories
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|SftpSubsystemFactory
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
name|PasswordAuthenticator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|ServerSession
name|session
parameter_list|)
block|{
comment|// dummy authentication: allow any user whose password is the same as the username
return|return
name|username
operator|!=
literal|null
operator|&&
name|username
operator|.
name|equals
argument_list|(
name|password
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|sshd
operator|.
name|setPublickeyAuthenticator
argument_list|(
operator|new
name|PublickeyAuthenticator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|authenticate
parameter_list|(
name|String
name|username
parameter_list|,
name|PublicKey
name|key
parameter_list|,
name|ServerSession
name|session
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
try|try
block|{
name|sshd
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Failed to start ssh server."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
DECL|method|setupKnownHosts ()
specifier|protected
name|void
name|setupKnownHosts
parameter_list|()
block|{
name|knownHostsFile
operator|=
name|SCP_ROOT_DIR
operator|+
literal|"/"
operator|+
name|KNOWN_HOSTS
expr_stmt|;
if|if
condition|(
operator|!
name|acceptLocalhostConnections
condition|)
block|{
return|return;
block|}
comment|// For security reasons (avoiding man in the middle attacks),
comment|// camel-jsch will only connect to known hosts. For unit testing
comment|// we use a known key, but since the port is dynamic, the
comment|// known_hosts file will be generated by the following code and
comment|// should contain a line like below (if
comment|// "HashKnownHosts"=="yes" the hostname:port part will be
comment|// hashed and look a bit more complicated).
comment|//
comment|// [localhost]:21000 ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAAAgQDd \
comment|// fIWeSV4o68dRrKSzFd/Bk51E65UTmmSrmW0O1ohtzi6HzsDPjXgCtlTt3F \
comment|// qTcfFfI92IlTr4JWqC9UK1QT1ZTeng0MkPQmv68hDANHbt5CpETZHjW5q4 \
comment|// OOgWhVvj5IyOC2NZHtKlJBkdsMAa15ouOOJLzBvAvbqOR/yUROsEiQ==
name|JSch
name|jsch
init|=
operator|new
name|JSch
argument_list|()
decl_stmt|;
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Using '{}' for known hosts."
argument_list|,
name|knownHostsFile
argument_list|)
expr_stmt|;
name|jsch
operator|.
name|setKnownHosts
argument_list|(
name|knownHostsFile
argument_list|)
expr_stmt|;
name|Session
name|s
init|=
name|jsch
operator|.
name|getSession
argument_list|(
literal|"admin"
argument_list|,
literal|"localhost"
argument_list|,
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
name|s
operator|.
name|setConfig
argument_list|(
literal|"StrictHostKeyChecking"
argument_list|,
literal|"ask"
argument_list|)
expr_stmt|;
comment|// TODO: by the current jsch (0.1.51) setting "HashKnownHosts" to "no" is a workaround
comment|// to make the tests run green, see also http://sourceforge.net/p/jsch/bugs/63/
name|s
operator|.
name|setConfig
argument_list|(
literal|"HashKnownHosts"
argument_list|,
literal|"no"
argument_list|)
expr_stmt|;
name|s
operator|.
name|setUserInfo
argument_list|(
operator|new
name|UserInfo
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|getPassphrase
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
literal|"admin"
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|promptPassword
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|promptPassphrase
parameter_list|(
name|String
name|message
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|promptYesNo
parameter_list|(
name|String
name|message
parameter_list|)
block|{
comment|// accept host authenticity
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|showMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{                 }
block|}
argument_list|)
expr_stmt|;
comment|// in the process of connecting, "[localhost]:<port>" is added to the knownHostsFile
name|s
operator|.
name|connect
argument_list|()
expr_stmt|;
name|s
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JSchException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Could not add [localhost] to known hosts"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getKnownHostsFile ()
specifier|public
name|String
name|getKnownHostsFile
parameter_list|()
block|{
return|return
name|knownHostsFile
return|;
block|}
DECL|method|isSetupComplete ()
specifier|public
name|boolean
name|isSetupComplete
parameter_list|()
block|{
return|return
name|setupComplete
return|;
block|}
DECL|method|traceSecurityProviders ()
specifier|protected
specifier|static
name|void
name|traceSecurityProviders
parameter_list|()
block|{
for|for
control|(
name|Provider
name|p
range|:
name|Security
operator|.
name|getProviders
argument_list|()
control|)
block|{
for|for
control|(
name|Service
name|s
range|:
name|p
operator|.
name|getServices
argument_list|()
control|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Security provider {} for '{}' algorithm"
argument_list|,
name|s
operator|.
name|getClassName
argument_list|()
argument_list|,
name|s
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

