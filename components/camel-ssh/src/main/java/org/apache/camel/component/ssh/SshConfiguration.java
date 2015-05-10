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
name|net
operator|.
name|URI
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
name|spi
operator|.
name|Metadata
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
name|camel
operator|.
name|spi
operator|.
name|UriParams
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
name|UriPath
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
name|common
operator|.
name|KeyPairProvider
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|SshConfiguration
specifier|public
class|class
name|SshConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|DEFAULT_SSH_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SSH_PORT
init|=
literal|22
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|host
specifier|private
name|String
name|host
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|DEFAULT_SSH_PORT
argument_list|)
DECL|field|port
specifier|private
name|int
name|port
init|=
name|DEFAULT_SSH_PORT
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|pollCommand
specifier|private
name|String
name|pollCommand
decl_stmt|;
DECL|field|keyPairProvider
specifier|private
name|KeyPairProvider
name|keyPairProvider
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|KeyPairProvider
operator|.
name|SSH_RSA
argument_list|)
DECL|field|keyType
specifier|private
name|String
name|keyType
init|=
name|KeyPairProvider
operator|.
name|SSH_RSA
decl_stmt|;
annotation|@
name|UriParam
DECL|field|certResource
specifier|private
name|String
name|certResource
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000
decl_stmt|;
DECL|method|SshConfiguration ()
specifier|public
name|SshConfiguration
parameter_list|()
block|{     }
DECL|method|SshConfiguration (URI uri)
specifier|public
name|SshConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|configure
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|configure (URI uri)
specifier|public
name|void
name|configure
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
comment|// UserInfo can contain both username and password as: user:pwd@sshserver
comment|// see: http://en.wikipedia.org/wiki/URI_scheme
name|String
name|username
init|=
name|uri
operator|.
name|getUserInfo
argument_list|()
decl_stmt|;
name|String
name|pw
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|username
operator|!=
literal|null
operator|&&
name|username
operator|.
name|contains
argument_list|(
literal|":"
argument_list|)
condition|)
block|{
name|pw
operator|=
name|ObjectHelper
operator|.
name|after
argument_list|(
name|username
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
name|username
operator|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|username
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|username
operator|!=
literal|null
condition|)
block|{
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pw
operator|!=
literal|null
condition|)
block|{
name|setPassword
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
name|setHost
argument_list|(
name|uri
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
comment|// URI.getPort returns -1 if port not defined, else use default port
name|int
name|uriPort
init|=
name|uri
operator|.
name|getPort
argument_list|()
decl_stmt|;
if|if
condition|(
name|uriPort
operator|!=
operator|-
literal|1
condition|)
block|{
name|setPort
argument_list|(
name|uriPort
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copy ()
specifier|public
name|SshConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|SshConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
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
comment|/**      * Sets the username to use in logging into the remote SSH server.      *      * @param username String representing login username.      */
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
comment|/**      * Sets the hostname of the remote SSH server.      *      * @param host String representing hostname of SSH server.      */
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
comment|/**      * Sets the port number for the remote SSH server.      *      * @param port int representing port number on remote host. Defaults to 22.      */
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
comment|/**      * Sets the password to use in connecting to remote SSH server.      * Requires keyPairProvider to be set to null.      *      * @param password String representing password for username at remote host.      */
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
comment|/**      * Sets the command string to send to the remote SSH server during every poll cycle.      * Only works with camel-ssh component being used as a consumer, i.e. from("ssh://...")      * You may need to end your command with a newline, and that must be URL encoded %0A      *      * @param pollCommand String representing the command to send.      */
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
comment|/**      * Sets the KeyPairProvider reference to use when connecting using Certificates to the remote SSH Server.      *      * @param keyPairProvider KeyPairProvider reference to use in authenticating. If set to 'null',      *                        then will attempt to connect using username/password settings.      *      * @see KeyPairProvider      */
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
comment|/**      * Sets the key type to pass to the KeyPairProvider as part of authentication.      * KeyPairProvider.loadKey(...) will be passed this value. Defaults to "ssh-rsa".      *      * @param keyType String defining the type of KeyPair to use for authentication.      *      * @see KeyPairProvider      */
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
comment|/**      * Sets the timeout in milliseconds to wait in establishing the remote SSH server connection.      * Defaults to 30000 milliseconds.      *      * @param timeout long milliseconds to wait.      */
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
operator|(
operator|(
name|certResource
operator|!=
literal|null
operator|)
operator|&&
name|certResource
operator|.
name|startsWith
argument_list|(
literal|"file:"
argument_list|)
operator|)
condition|?
name|certResource
operator|.
name|substring
argument_list|(
literal|5
argument_list|)
else|:
literal|null
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
name|this
operator|.
name|certResource
operator|=
literal|"file:"
operator|+
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
comment|/**      * Sets the resource path of the certificate to use for Authentication.      * Will use {@link ResourceHelperKeyPairProvider} to resolve file based certificate, and depends on keyType setting.      *      * @param certResource String file, classpath, or http url for the certificate      */
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

