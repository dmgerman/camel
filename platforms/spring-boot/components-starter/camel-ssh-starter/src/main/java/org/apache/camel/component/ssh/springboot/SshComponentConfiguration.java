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
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|DeprecatedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The ssh component enables access to SSH servers such that you can send an SSH  * command, and process the response.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
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
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared SSH configuration      */
DECL|field|configuration
specifier|private
name|SshConfigurationNestedConfiguration
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
name|Integer
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
comment|/**      * Sets the command string to send to the remote SSH server during every      * poll cycle. Only works with camel-ssh component being used as a consumer,      * i.e. from(ssh://...). You may need to end your command with a newline,      * and that must be URL encoded %0A      */
DECL|field|pollCommand
specifier|private
name|String
name|pollCommand
decl_stmt|;
comment|/**      * Sets the KeyPairProvider reference to use when connecting using      * Certificates to the remote SSH Server. The option is a      * org.apache.sshd.common.keyprovider.KeyPairProvider type.      */
DECL|field|keyPairProvider
specifier|private
name|String
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
name|Long
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
comment|/**      * Sets the resource path of the certificate to use for Authentication. Will      * use ResourceHelperKeyPairProvider to resolve file based certificate, and      * depends on keyType setting.      */
DECL|field|certResource
specifier|private
name|String
name|certResource
decl_stmt|;
comment|/**      * Sets the channel type to pass to the Channel as part of command      * execution. Defaults to exec.      */
DECL|field|channelType
specifier|private
name|String
name|channelType
decl_stmt|;
comment|/**      * Sets the shellPrompt to be dropped when response is read after command      * execution      */
DECL|field|shellPrompt
specifier|private
name|String
name|shellPrompt
decl_stmt|;
comment|/**      * Sets the sleep period in milliseconds to wait reading response from shell      * prompt. Defaults to 100 milliseconds.      */
DECL|field|sleepForShellPrompt
specifier|private
name|Long
name|sleepForShellPrompt
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|SshConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( SshConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|SshConfigurationNestedConfiguration
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
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
name|String
name|getKeyPairProvider
parameter_list|()
block|{
return|return
name|keyPairProvider
return|;
block|}
DECL|method|setKeyPairProvider (String keyPairProvider)
specifier|public
name|void
name|setKeyPairProvider
parameter_list|(
name|String
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
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
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
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
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
DECL|method|getChannelType ()
specifier|public
name|String
name|getChannelType
parameter_list|()
block|{
return|return
name|channelType
return|;
block|}
DECL|method|setChannelType (String channelType)
specifier|public
name|void
name|setChannelType
parameter_list|(
name|String
name|channelType
parameter_list|)
block|{
name|this
operator|.
name|channelType
operator|=
name|channelType
expr_stmt|;
block|}
DECL|method|getShellPrompt ()
specifier|public
name|String
name|getShellPrompt
parameter_list|()
block|{
return|return
name|shellPrompt
return|;
block|}
DECL|method|setShellPrompt (String shellPrompt)
specifier|public
name|void
name|setShellPrompt
parameter_list|(
name|String
name|shellPrompt
parameter_list|)
block|{
name|this
operator|.
name|shellPrompt
operator|=
name|shellPrompt
expr_stmt|;
block|}
DECL|method|getSleepForShellPrompt ()
specifier|public
name|Long
name|getSleepForShellPrompt
parameter_list|()
block|{
return|return
name|sleepForShellPrompt
return|;
block|}
DECL|method|setSleepForShellPrompt (Long sleepForShellPrompt)
specifier|public
name|void
name|setSleepForShellPrompt
parameter_list|(
name|Long
name|sleepForShellPrompt
parameter_list|)
block|{
name|this
operator|.
name|sleepForShellPrompt
operator|=
name|sleepForShellPrompt
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|SshConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|SshConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
operator|.
name|class
decl_stmt|;
comment|/**          * Sets the username to use in logging into the remote SSH server.          *           * @param usernameString          *            representing login username.          */
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
comment|/**          * Sets the hostname of the remote SSH server.          *           * @param hostString          *            representing hostname of SSH server.          */
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
comment|/**          * Sets the port number for the remote SSH server.          *           * @param portint          *            representing port number on remote host. Defaults to 22.          */
DECL|field|port
specifier|private
name|Integer
name|port
init|=
literal|22
decl_stmt|;
comment|/**          * Sets the password to use in connecting to remote SSH server. Requires          * keyPairProvider to be set to null.          *           * @param passwordString          *            representing password for username at remote host.          */
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
comment|/**          * Sets the command string to send to the remote SSH server during every          * poll cycle. Only works with camel-ssh component being used as a          * consumer, i.e. from("ssh://...") You may need to end your command          * with a newline, and that must be URL encoded %0A          *           * @param pollCommandString          *            representing the command to send.          */
DECL|field|pollCommand
specifier|private
name|String
name|pollCommand
decl_stmt|;
comment|/**          * Sets the KeyPairProvider reference to use when connecting using          * Certificates to the remote SSH Server.          *           * @param keyPairProviderKeyPairProvider          *            reference to use in authenticating. If set to 'null', then          *            will attempt to connect using username/password settings.          * @see KeyPairProvider          */
DECL|field|keyPairProvider
specifier|private
name|KeyPairProvider
name|keyPairProvider
decl_stmt|;
comment|/**          * Sets the key type to pass to the KeyPairProvider as part of          * authentication. KeyPairProvider.loadKey(...) will be passed this          * value. Defaults to "ssh-rsa".          *           * @param keyTypeString          *            defining the type of KeyPair to use for authentication.          * @see KeyPairProvider          */
DECL|field|keyType
specifier|private
name|String
name|keyType
init|=
literal|"ssh-rsa"
decl_stmt|;
comment|/**          * Sets the timeout in milliseconds to wait in establishing the remote          * SSH server connection. Defaults to 30000 milliseconds.          *           * @param timeoutlong          *            milliseconds to wait.          */
DECL|field|timeout
specifier|private
name|Long
name|timeout
init|=
literal|30000L
decl_stmt|;
comment|/**          * @deprecated As of version 2.11, replaced by          *             {@link #setCertResource(String)}          */
annotation|@
name|Deprecated
DECL|field|certFilename
specifier|private
name|String
name|certFilename
decl_stmt|;
comment|/**          * Sets the resource path of the certificate to use for Authentication.          * Will use {@link ResourceHelperKeyPairProvider} to resolve file based          * certificate, and depends on keyType setting.          *           * @param certResourceString          *            file, classpath, or http url for the certificate          */
DECL|field|certResource
specifier|private
name|String
name|certResource
decl_stmt|;
comment|/**          * Sets the resource path for a known_hosts file          *           * @param knownHostsString          *            file, classpath, or http url for the certificate          */
DECL|field|knownHostsResource
specifier|private
name|String
name|knownHostsResource
decl_stmt|;
comment|/**          * Specifies whether a connection to an unknown host should fail or not.          * This value is only checked when the property knownHosts is set.          *           * @param boolean boolean flag, whether a connection to an unknown host          *        should fail          */
DECL|field|failOnUnknownHost
specifier|private
name|Boolean
name|failOnUnknownHost
init|=
literal|false
decl_stmt|;
comment|/**          * Sets the channel type to pass to the Channel as part of command          * execution. Defaults to "exec".          *           * @param channelTypeString          *            defining the type of Channel to use for command execution.          * @seeorg.apache.sshd.common.channel.Channel          */
DECL|field|channelType
specifier|private
name|String
name|channelType
init|=
literal|"exec"
decl_stmt|;
comment|/**          * Sets the shellPrompt to be dropped when response is read after          * command execution          *           * @param shellPromptString          *            defining ending string of command line which has to be          *            dropped when response is read after command execution.          */
DECL|field|shellPrompt
specifier|private
name|String
name|shellPrompt
decl_stmt|;
comment|/**          * Sets the sleep period in milliseconds to wait reading response from          * shell prompt. Defaults to 100 milliseconds.          *           * @param sleepForShellPromptlong          *            milliseconds to wait.          */
DECL|field|sleepForShellPrompt
specifier|private
name|Long
name|sleepForShellPrompt
init|=
literal|100L
decl_stmt|;
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
name|Integer
name|getPort
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|setPort (Integer port)
specifier|public
name|void
name|setPort
parameter_list|(
name|Integer
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
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
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
annotation|@
name|Deprecated
annotation|@
name|DeprecatedConfigurationProperty
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
DECL|method|getKnownHostsResource ()
specifier|public
name|String
name|getKnownHostsResource
parameter_list|()
block|{
return|return
name|knownHostsResource
return|;
block|}
DECL|method|setKnownHostsResource (String knownHostsResource)
specifier|public
name|void
name|setKnownHostsResource
parameter_list|(
name|String
name|knownHostsResource
parameter_list|)
block|{
name|this
operator|.
name|knownHostsResource
operator|=
name|knownHostsResource
expr_stmt|;
block|}
DECL|method|getFailOnUnknownHost ()
specifier|public
name|Boolean
name|getFailOnUnknownHost
parameter_list|()
block|{
return|return
name|failOnUnknownHost
return|;
block|}
DECL|method|setFailOnUnknownHost (Boolean failOnUnknownHost)
specifier|public
name|void
name|setFailOnUnknownHost
parameter_list|(
name|Boolean
name|failOnUnknownHost
parameter_list|)
block|{
name|this
operator|.
name|failOnUnknownHost
operator|=
name|failOnUnknownHost
expr_stmt|;
block|}
DECL|method|getChannelType ()
specifier|public
name|String
name|getChannelType
parameter_list|()
block|{
return|return
name|channelType
return|;
block|}
DECL|method|setChannelType (String channelType)
specifier|public
name|void
name|setChannelType
parameter_list|(
name|String
name|channelType
parameter_list|)
block|{
name|this
operator|.
name|channelType
operator|=
name|channelType
expr_stmt|;
block|}
DECL|method|getShellPrompt ()
specifier|public
name|String
name|getShellPrompt
parameter_list|()
block|{
return|return
name|shellPrompt
return|;
block|}
DECL|method|setShellPrompt (String shellPrompt)
specifier|public
name|void
name|setShellPrompt
parameter_list|(
name|String
name|shellPrompt
parameter_list|)
block|{
name|this
operator|.
name|shellPrompt
operator|=
name|shellPrompt
expr_stmt|;
block|}
DECL|method|getSleepForShellPrompt ()
specifier|public
name|Long
name|getSleepForShellPrompt
parameter_list|()
block|{
return|return
name|sleepForShellPrompt
return|;
block|}
DECL|method|setSleepForShellPrompt (Long sleepForShellPrompt)
specifier|public
name|void
name|setSleepForShellPrompt
parameter_list|(
name|Long
name|sleepForShellPrompt
parameter_list|)
block|{
name|this
operator|.
name|sleepForShellPrompt
operator|=
name|sleepForShellPrompt
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

