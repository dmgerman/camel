begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ssh
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ssh
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PipedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|RuntimeCamelException
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
name|client
operator|.
name|SshClient
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
name|client
operator|.
name|channel
operator|.
name|ClientChannel
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
name|client
operator|.
name|channel
operator|.
name|ClientChannelEvent
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
name|client
operator|.
name|future
operator|.
name|AuthFuture
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
name|client
operator|.
name|future
operator|.
name|ConnectFuture
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
name|client
operator|.
name|future
operator|.
name|OpenFuture
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
name|client
operator|.
name|session
operator|.
name|ClientSession
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
name|channel
operator|.
name|Channel
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
name|KeyPairProvider
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
DECL|class|SshHelper
specifier|public
specifier|final
class|class
name|SshHelper
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
name|SshHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|SshHelper ()
specifier|private
name|SshHelper
parameter_list|()
block|{     }
DECL|method|sendExecCommand (Map<String, Object> headers, String command, SshEndpoint endpoint, SshClient client)
specifier|public
specifier|static
name|SshResult
name|sendExecCommand
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|command
parameter_list|,
name|SshEndpoint
name|endpoint
parameter_list|,
name|SshClient
name|client
parameter_list|)
throws|throws
name|Exception
block|{
name|SshConfiguration
name|configuration
init|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Configuration must be set"
argument_list|)
throw|;
block|}
name|String
name|userName
init|=
name|configuration
operator|.
name|getUsername
argument_list|()
decl_stmt|;
name|Object
name|userNameHeaderObj
init|=
name|headers
operator|.
name|get
argument_list|(
name|SshConstants
operator|.
name|USERNAME_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|userNameHeaderObj
operator|instanceof
name|String
condition|)
block|{
name|userName
operator|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|SshConstants
operator|.
name|USERNAME_HEADER
argument_list|)
expr_stmt|;
block|}
name|ConnectFuture
name|connectFuture
init|=
name|client
operator|.
name|connect
argument_list|(
name|userName
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
comment|// wait getTimeout milliseconds for connect operation to complete
name|connectFuture
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|connectFuture
operator|.
name|isDone
argument_list|()
operator|||
operator|!
name|connectFuture
operator|.
name|isConnected
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to connect to "
operator|+
name|configuration
operator|.
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|configuration
operator|.
name|getPort
argument_list|()
operator|+
literal|" within timeout "
operator|+
name|configuration
operator|.
name|getTimeout
argument_list|()
operator|+
literal|"ms"
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connected to {}:{}"
argument_list|,
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|ClientChannel
name|channel
init|=
literal|null
decl_stmt|;
name|ClientSession
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|AuthFuture
name|authResult
decl_stmt|;
name|session
operator|=
name|connectFuture
operator|.
name|getSession
argument_list|()
expr_stmt|;
name|KeyPairProvider
name|keyPairProvider
decl_stmt|;
specifier|final
name|String
name|certResource
init|=
name|configuration
operator|.
name|getCertResource
argument_list|()
decl_stmt|;
if|if
condition|(
name|certResource
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempting to authenticate using ResourceKey '{}'..."
argument_list|,
name|certResource
argument_list|)
expr_stmt|;
name|keyPairProvider
operator|=
operator|new
name|ResourceHelperKeyPairProvider
argument_list|(
operator|new
name|String
index|[]
block|{
name|certResource
block|}
argument_list|,
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyPairProvider
operator|=
name|configuration
operator|.
name|getKeyPairProvider
argument_list|()
expr_stmt|;
block|}
comment|// either provide a keypair or password identity first
if|if
condition|(
name|keyPairProvider
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempting to authenticate username '{}' using a key identity"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
name|KeyPair
name|pair
init|=
name|keyPairProvider
operator|.
name|loadKey
argument_list|(
name|configuration
operator|.
name|getKeyType
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|.
name|addPublicKeyIdentity
argument_list|(
name|pair
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|password
init|=
name|configuration
operator|.
name|getPassword
argument_list|()
decl_stmt|;
name|Object
name|passwordHeaderObj
init|=
name|headers
operator|.
name|get
argument_list|(
name|SshConstants
operator|.
name|PASSWORD_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|passwordHeaderObj
operator|instanceof
name|String
condition|)
block|{
name|password
operator|=
operator|(
name|String
operator|)
name|headers
operator|.
name|get
argument_list|(
name|SshConstants
operator|.
name|PASSWORD_HEADER
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempting to authenticate username '{}' using a password identity"
argument_list|,
name|userName
argument_list|)
expr_stmt|;
name|session
operator|.
name|addPasswordIdentity
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|// now start the authentication process
name|authResult
operator|=
name|session
operator|.
name|auth
argument_list|()
expr_stmt|;
name|authResult
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|authResult
operator|.
name|isDone
argument_list|()
operator|||
name|authResult
operator|.
name|isFailure
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Failed to authenticate"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to authenticate username "
operator|+
name|configuration
operator|.
name|getUsername
argument_list|()
argument_list|)
throw|;
block|}
name|InputStream
name|in
init|=
literal|null
decl_stmt|;
name|PipedOutputStream
name|reply
init|=
operator|new
name|PipedOutputStream
argument_list|()
decl_stmt|;
comment|// for now only two channel types are supported
comment|// shell option is added for specific purpose for now
comment|// may need further maintainance for further use cases
if|if
condition|(
name|Channel
operator|.
name|CHANNEL_EXEC
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getChannelType
argument_list|()
argument_list|)
condition|)
block|{
name|channel
operator|=
name|session
operator|.
name|createChannel
argument_list|(
name|Channel
operator|.
name|CHANNEL_EXEC
argument_list|,
name|command
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|ByteArrayInputStream
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0
block|}
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Channel
operator|.
name|CHANNEL_SHELL
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getChannelType
argument_list|()
argument_list|)
condition|)
block|{
comment|// PipedOutputStream and PipedInputStream both are connected to each other to create a communication pipe
comment|// this approach is used to send the command and evaluate the response
name|channel
operator|=
name|session
operator|.
name|createChannel
argument_list|(
name|Channel
operator|.
name|CHANNEL_SHELL
argument_list|)
expr_stmt|;
name|in
operator|=
operator|new
name|PipedInputStream
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
name|channel
operator|.
name|setIn
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setOut
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|err
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|channel
operator|.
name|setErr
argument_list|(
name|err
argument_list|)
expr_stmt|;
name|OpenFuture
name|openFuture
init|=
name|channel
operator|.
name|open
argument_list|()
decl_stmt|;
name|openFuture
operator|.
name|await
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|SshResult
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Channel
operator|.
name|CHANNEL_EXEC
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getChannelType
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|openFuture
operator|.
name|isOpened
argument_list|()
condition|)
block|{
name|Set
argument_list|<
name|ClientChannelEvent
argument_list|>
name|events
init|=
name|channel
operator|.
name|waitFor
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|ClientChannelEvent
operator|.
name|CLOSED
argument_list|)
argument_list|,
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|events
operator|.
name|contains
argument_list|(
name|ClientChannelEvent
operator|.
name|TIMEOUT
argument_list|)
condition|)
block|{
name|result
operator|=
operator|new
name|SshResult
argument_list|(
name|command
argument_list|,
name|channel
operator|.
name|getExitStatus
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|err
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|Channel
operator|.
name|CHANNEL_SHELL
operator|.
name|equals
argument_list|(
name|endpoint
operator|.
name|getChannelType
argument_list|()
argument_list|)
condition|)
block|{
name|getPrompt
argument_list|(
name|channel
argument_list|,
name|out
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|reply
operator|.
name|write
argument_list|(
name|command
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|reply
operator|.
name|write
argument_list|(
name|System
operator|.
name|lineSeparator
argument_list|()
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|response
init|=
name|getPrompt
argument_list|(
name|channel
argument_list|,
name|out
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|result
operator|=
operator|new
name|SshResult
argument_list|(
name|command
argument_list|,
name|channel
operator|.
name|getExitStatus
argument_list|()
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|response
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|err
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|close
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|// need to make sure the session is closed
if|if
condition|(
name|session
operator|!=
literal|null
condition|)
block|{
name|session
operator|.
name|close
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getPrompt (ClientChannel channel, ByteArrayOutputStream output, SshEndpoint endpoint)
specifier|private
specifier|static
name|String
name|getPrompt
parameter_list|(
name|ClientChannel
name|channel
parameter_list|,
name|ByteArrayOutputStream
name|output
parameter_list|,
name|SshEndpoint
name|endpoint
parameter_list|)
throws|throws
name|UnsupportedEncodingException
throws|,
name|InterruptedException
block|{
while|while
condition|(
operator|!
name|channel
operator|.
name|isClosed
argument_list|()
condition|)
block|{
name|String
name|response
init|=
operator|new
name|String
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|trim
argument_list|()
operator|.
name|endsWith
argument_list|(
name|endpoint
operator|.
name|getShellPrompt
argument_list|()
argument_list|)
condition|)
block|{
name|output
operator|.
name|reset
argument_list|()
expr_stmt|;
return|return
name|SshShellOutputStringHelper
operator|.
name|betweenBeforeLast
argument_list|(
name|response
argument_list|,
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|,
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|)
return|;
block|}
comment|// avoid cpu burning cycles
name|Thread
operator|.
name|sleep
argument_list|(
name|endpoint
operator|.
name|getSleepForShellPrompt
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

