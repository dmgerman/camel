begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|security
operator|.
name|KeyPair
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
name|Consumer
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
name|Producer
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
name|camel
operator|.
name|impl
operator|.
name|ScheduledPollEndpoint
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
name|common
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

begin_comment
comment|/**  * Represents an SSH endpoint.  */
end_comment

begin_class
DECL|class|SshEndpoint
specifier|public
class|class
name|SshEndpoint
extends|extends
name|ScheduledPollEndpoint
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|client
specifier|private
name|SshClient
name|client
decl_stmt|;
DECL|field|sshConfiguration
specifier|private
name|SshConfiguration
name|sshConfiguration
decl_stmt|;
DECL|method|SshEndpoint ()
specifier|public
name|SshEndpoint
parameter_list|()
block|{     }
DECL|method|SshEndpoint (String uri, SshComponent component)
specifier|public
name|SshEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SshComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|SshEndpoint (String uri, SshComponent component, SshConfiguration configuration)
specifier|public
name|SshEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|SshComponent
name|component
parameter_list|,
name|SshConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|sshConfiguration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|SshProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|SshConsumer
name|consumer
init|=
operator|new
name|SshConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
comment|// SshClient is not thread-safe to be shared
return|return
literal|false
return|;
block|}
DECL|method|sendExecCommand (String command)
specifier|public
name|SshResult
name|sendExecCommand
parameter_list|(
name|String
name|command
parameter_list|)
throws|throws
name|Exception
block|{
name|SshResult
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
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
name|ConnectFuture
name|connectFuture
init|=
name|client
operator|.
name|connect
argument_list|(
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
comment|// Wait getTimeout milliseconds for connect operation to complete
name|connectFuture
operator|.
name|await
argument_list|(
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
specifier|final
name|String
name|msg
init|=
literal|"Failed to connect to "
operator|+
name|getHost
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
operator|+
literal|" within timeout "
operator|+
name|getTimeout
argument_list|()
operator|+
literal|"ms"
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|msg
argument_list|)
throw|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Connected to {}:{}"
argument_list|,
name|getHost
argument_list|()
argument_list|,
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|AuthFuture
name|authResult
decl_stmt|;
name|ClientSession
name|session
init|=
name|connectFuture
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|KeyPairProvider
name|keyPairProvider
decl_stmt|;
specifier|final
name|String
name|certResource
init|=
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
name|log
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
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyPairProvider
operator|=
name|getKeyPairProvider
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|keyPairProvider
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Attempting to authenticate username '{}' using Key..."
argument_list|,
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|KeyPair
name|pair
init|=
name|keyPairProvider
operator|.
name|loadKey
argument_list|(
name|getKeyType
argument_list|()
argument_list|)
decl_stmt|;
name|authResult
operator|=
name|session
operator|.
name|authPublicKey
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|pair
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Attempting to authenticate username '{}' using Password..."
argument_list|,
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|authResult
operator|=
name|session
operator|.
name|authPassword
argument_list|(
name|getUsername
argument_list|()
argument_list|,
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|authResult
operator|.
name|await
argument_list|(
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
name|log
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
name|getUsername
argument_list|()
argument_list|)
throw|;
block|}
name|ClientChannel
name|channel
init|=
name|session
operator|.
name|createChannel
argument_list|(
name|ClientChannel
operator|.
name|CHANNEL_EXEC
argument_list|,
name|command
argument_list|)
decl_stmt|;
name|ByteArrayInputStream
name|in
init|=
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
decl_stmt|;
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
name|getTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|openFuture
operator|.
name|isOpened
argument_list|()
condition|)
block|{
name|channel
operator|.
name|waitFor
argument_list|(
name|ClientChannel
operator|.
name|CLOSED
argument_list|,
literal|0
argument_list|)
expr_stmt|;
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
return|return
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|client
operator|=
name|SshClient
operator|.
name|setUpDefaultClient
argument_list|()
expr_stmt|;
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|client
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
name|client
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|SshConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|sshConfiguration
return|;
block|}
DECL|method|setConfiguration (SshConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SshConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|sshConfiguration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getHost ()
specifier|public
name|String
name|getHost
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getHost
argument_list|()
return|;
block|}
DECL|method|setHost (String host)
specifier|public
name|void
name|setHost
parameter_list|(
name|String
name|host
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setHost
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
DECL|method|getPort ()
specifier|public
name|int
name|getPort
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPort
argument_list|()
return|;
block|}
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getUsername
argument_list|()
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPassword
argument_list|()
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
DECL|method|getPollCommand ()
specifier|public
name|String
name|getPollCommand
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPollCommand
argument_list|()
return|;
block|}
DECL|method|setPollCommand (String pollCommand)
specifier|public
name|void
name|setPollCommand
parameter_list|(
name|String
name|pollCommand
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPollCommand
argument_list|(
name|pollCommand
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeyPairProvider ()
specifier|public
name|KeyPairProvider
name|getKeyPairProvider
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getKeyPairProvider
argument_list|()
return|;
block|}
DECL|method|setKeyPairProvider (KeyPairProvider keyPairProvider)
specifier|public
name|void
name|setKeyPairProvider
parameter_list|(
name|KeyPairProvider
name|keyPairProvider
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setKeyPairProvider
argument_list|(
name|keyPairProvider
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeyType ()
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getKeyType
argument_list|()
return|;
block|}
DECL|method|setKeyType (String keyType)
specifier|public
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setKeyType
argument_list|(
name|keyType
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getTimeout
argument_list|()
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated As of version 2.11, replaced by {@link #getCertResource()}      */
DECL|method|getCertFilename ()
specifier|public
name|String
name|getCertFilename
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getCertFilename
argument_list|()
return|;
block|}
comment|/**      * @deprecated As of version 2.11, replaced by {@link #setCertResource(String)}      */
DECL|method|setCertFilename (String certFilename)
specifier|public
name|void
name|setCertFilename
parameter_list|(
name|String
name|certFilename
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCertFilename
argument_list|(
name|certFilename
argument_list|)
expr_stmt|;
block|}
DECL|method|getCertResource ()
specifier|public
name|String
name|getCertResource
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getCertResource
argument_list|()
return|;
block|}
DECL|method|setCertResource (String certResource)
specifier|public
name|void
name|setCertResource
parameter_list|(
name|String
name|certResource
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCertResource
argument_list|(
name|certResource
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

