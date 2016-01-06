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
name|camel
operator|.
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
comment|/**  * The ssh component enables access to SSH servers such that you can send an SSH command, and process the response.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"ssh"
argument_list|,
name|title
operator|=
literal|"SSH"
argument_list|,
name|syntax
operator|=
literal|"ssh:host:port"
argument_list|,
name|alternativeSyntax
operator|=
literal|"ssh:username:password@host:port"
argument_list|,
name|consumerClass
operator|=
name|SshConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"file"
argument_list|)
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
annotation|@
name|UriParam
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
literal|true
return|;
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
annotation|@
name|Deprecated
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
annotation|@
name|Deprecated
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

