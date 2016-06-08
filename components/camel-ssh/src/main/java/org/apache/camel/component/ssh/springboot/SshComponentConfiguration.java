begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ssh.springboot
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
operator|.
name|springboot
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
name|component
operator|.
name|ssh
operator|.
name|SshConfiguration
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
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The ssh component enables access to SSH servers such that you can send an SSH  * command and process the response.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.ssh"
argument_list|)
DECL|class|SshComponentConfiguration
specifier|public
class|class
name|SshComponentConfiguration
block|{
comment|/**      * To use the shared SSH configuration      */
DECL|field|configuration
specifier|private
name|SshConfiguration
name|configuration
decl_stmt|;
comment|/**      * Sets the hostname of the remote SSH server.      */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**      * Sets the port number for the remote SSH server.      */
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
comment|/**      * Sets the username to use in logging into the remote SSH server.      */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Sets the password to use in connecting to remote SSH server. Requires      * keyPairProvider to be set to null.      */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      * Sets the command string to send to the remote SSH server during every      * poll cycle. Only works with camel-ssh component being used as a consumer      * i.e. from(ssh://...). You may need to end your command with a newline and      * that must be URL encoded 0A      */
DECL|field|pollCommand
specifier|private
name|String
name|pollCommand
decl_stmt|;
comment|/**      * Sets the KeyPairProvider reference to use when connecting using      * Certificates to the remote SSH Server.      */
DECL|field|keyPairProvider
specifier|private
name|KeyPairProvider
name|keyPairProvider
decl_stmt|;
comment|/**      * Sets the key type to pass to the KeyPairProvider as part of      * authentication. KeyPairProvider.loadKey(...) will be passed this value.      * Defaults to ssh-rsa.      */
DECL|field|keyType
specifier|private
name|String
name|keyType
decl_stmt|;
comment|/**      * Sets the timeout in milliseconds to wait in establishing the remote SSH      * server connection. Defaults to 30000 milliseconds.      */
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
comment|/**      * Sets the resource path of the certificate to use for Authentication.      */
annotation|@
name|Deprecated
DECL|field|certFilename
specifier|private
name|String
name|certFilename
decl_stmt|;
comment|/**      * Sets the resource path of the certificate to use for Authentication. Will      * use ResourceHelperKeyPairProvider to resolve file based certificate and      * depends on keyType setting.      */
DECL|field|certResource
specifier|private
name|String
name|certResource
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|SshConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
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
name|configuration
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
name|host
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
name|this
operator|.
name|host
operator|=
name|host
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
DECL|method|setPort (int port)
specifier|public
name|void
name|setPort
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
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
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
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
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getPollCommand ()
specifier|public
name|String
name|getPollCommand
parameter_list|()
block|{
return|return
name|pollCommand
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
name|this
operator|.
name|pollCommand
operator|=
name|pollCommand
expr_stmt|;
block|}
DECL|method|getKeyPairProvider ()
specifier|public
name|KeyPairProvider
name|getKeyPairProvider
parameter_list|()
block|{
return|return
name|keyPairProvider
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
name|this
operator|.
name|keyPairProvider
operator|=
name|keyPairProvider
expr_stmt|;
block|}
DECL|method|getKeyType ()
specifier|public
name|String
name|getKeyType
parameter_list|()
block|{
return|return
name|keyType
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
name|this
operator|.
name|keyType
operator|=
name|keyType
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
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
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getCertFilename ()
specifier|public
name|String
name|getCertFilename
parameter_list|()
block|{
return|return
name|certFilename
return|;
block|}
DECL|method|setCertFilename (String certFilename)
specifier|public
name|void
name|setCertFilename
parameter_list|(
name|String
name|certFilename
parameter_list|)
block|{
name|this
operator|.
name|certFilename
operator|=
name|certFilename
expr_stmt|;
block|}
DECL|method|getCertResource ()
specifier|public
name|String
name|getCertResource
parameter_list|()
block|{
return|return
name|certResource
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
name|this
operator|.
name|certResource
operator|=
name|certResource
expr_stmt|;
block|}
block|}
end_class

end_unit

