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
name|component
operator|.
name|file
operator|.
name|remote
operator|.
name|RemoteFileConfiguration
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

begin_comment
comment|/**  * SCP configuration  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|ScpConfiguration
specifier|public
class|class
name|ScpConfiguration
extends|extends
name|RemoteFileConfiguration
block|{
DECL|field|DEFAULT_SFTP_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_SFTP_PORT
init|=
literal|22
decl_stmt|;
DECL|field|DEFAULT_MOD
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_MOD
init|=
literal|"664"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useUserKnownHostsFile
specifier|private
name|boolean
name|useUserKnownHostsFile
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|knownHostsFile
specifier|private
name|String
name|knownHostsFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privateKeyFile
specifier|private
name|String
name|privateKeyFile
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privateKeyBytes
specifier|private
name|byte
index|[]
name|privateKeyBytes
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|privateKeyFilePassphrase
specifier|private
name|String
name|privateKeyFilePassphrase
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"no,yes"
argument_list|,
name|defaultValue
operator|=
literal|"no"
argument_list|)
DECL|field|strictHostKeyChecking
specifier|private
name|String
name|strictHostKeyChecking
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
name|DEFAULT_MOD
argument_list|)
DECL|field|chmod
specifier|private
name|String
name|chmod
init|=
name|DEFAULT_MOD
decl_stmt|;
comment|// comma separated list of ciphers.
comment|// null means default jsch list will be used
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security,advanced"
argument_list|)
DECL|field|ciphers
specifier|private
name|String
name|ciphers
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|preferredAuthentications
specifier|private
name|String
name|preferredAuthentications
decl_stmt|;
DECL|method|ScpConfiguration ()
specifier|public
name|ScpConfiguration
parameter_list|()
block|{
name|setProtocol
argument_list|(
literal|"scp"
argument_list|)
expr_stmt|;
block|}
DECL|method|ScpConfiguration (URI uri)
specifier|public
name|ScpConfiguration
parameter_list|(
name|URI
name|uri
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setDefaultPort ()
specifier|protected
name|void
name|setDefaultPort
parameter_list|()
block|{
name|setPort
argument_list|(
name|DEFAULT_SFTP_PORT
argument_list|)
expr_stmt|;
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
comment|/**      * Sets the known_hosts file, so that the jsch endpoint can do host key verification.      * You can prefix with classpath: to load the file from classpath instead of file system.      */
DECL|method|setKnownHostsFile (String knownHostsFile)
specifier|public
name|void
name|setKnownHostsFile
parameter_list|(
name|String
name|knownHostsFile
parameter_list|)
block|{
name|this
operator|.
name|knownHostsFile
operator|=
name|knownHostsFile
expr_stmt|;
block|}
DECL|method|isUseUserKnownHostsFile ()
specifier|public
name|boolean
name|isUseUserKnownHostsFile
parameter_list|()
block|{
return|return
name|useUserKnownHostsFile
return|;
block|}
comment|/**      * If knownHostFile has not been explicit configured, then use the host file from System.getProperty("user.home") + "/.ssh/known_hosts"      */
DECL|method|setUseUserKnownHostsFile (boolean useUserKnownHostsFile)
specifier|public
name|void
name|setUseUserKnownHostsFile
parameter_list|(
name|boolean
name|useUserKnownHostsFile
parameter_list|)
block|{
name|this
operator|.
name|useUserKnownHostsFile
operator|=
name|useUserKnownHostsFile
expr_stmt|;
block|}
DECL|method|getPrivateKeyFile ()
specifier|public
name|String
name|getPrivateKeyFile
parameter_list|()
block|{
return|return
name|privateKeyFile
return|;
block|}
comment|/**      * Set the private key file to that the endpoint can do private key verification.      * You can prefix with classpath: to load the file from classpath instead of file system.      */
DECL|method|setPrivateKeyFile (String privateKeyFile)
specifier|public
name|void
name|setPrivateKeyFile
parameter_list|(
name|String
name|privateKeyFile
parameter_list|)
block|{
name|this
operator|.
name|privateKeyFile
operator|=
name|privateKeyFile
expr_stmt|;
block|}
DECL|method|getPrivateKeyBytes ()
specifier|public
name|byte
index|[]
name|getPrivateKeyBytes
parameter_list|()
block|{
return|return
name|privateKeyBytes
return|;
block|}
comment|/**      * Set the private key bytes to that the endpoint can do private key verification.      * This must be used only if privateKeyFile wasn't set. Otherwise the file will have the priority.      */
DECL|method|setPrivateKeyBytes (byte[] privateKeyBytes)
specifier|public
name|void
name|setPrivateKeyBytes
parameter_list|(
name|byte
index|[]
name|privateKeyBytes
parameter_list|)
block|{
name|this
operator|.
name|privateKeyBytes
operator|=
name|privateKeyBytes
expr_stmt|;
block|}
DECL|method|getPrivateKeyFilePassphrase ()
specifier|public
name|String
name|getPrivateKeyFilePassphrase
parameter_list|()
block|{
return|return
name|privateKeyFilePassphrase
return|;
block|}
comment|/**      * Set the private key file passphrase to that the endpoint can do private key verification.      */
DECL|method|setPrivateKeyFilePassphrase (String privateKeyFilePassphrase)
specifier|public
name|void
name|setPrivateKeyFilePassphrase
parameter_list|(
name|String
name|privateKeyFilePassphrase
parameter_list|)
block|{
name|this
operator|.
name|privateKeyFilePassphrase
operator|=
name|privateKeyFilePassphrase
expr_stmt|;
block|}
DECL|method|getStrictHostKeyChecking ()
specifier|public
name|String
name|getStrictHostKeyChecking
parameter_list|()
block|{
return|return
name|strictHostKeyChecking
return|;
block|}
comment|/**      * Sets whether to use strict host key checking. Possible values are: no, yes      */
DECL|method|setStrictHostKeyChecking (String strictHostKeyChecking)
specifier|public
name|void
name|setStrictHostKeyChecking
parameter_list|(
name|String
name|strictHostKeyChecking
parameter_list|)
block|{
name|this
operator|.
name|strictHostKeyChecking
operator|=
name|strictHostKeyChecking
expr_stmt|;
block|}
comment|/**      * Allows you to set chmod on the stored file. For example chmod=664.      */
DECL|method|setChmod (String chmod)
specifier|public
name|void
name|setChmod
parameter_list|(
name|String
name|chmod
parameter_list|)
block|{
if|if
condition|(
name|chmod
operator|.
name|length
argument_list|()
operator|==
literal|3
condition|)
block|{
for|for
control|(
name|byte
name|c
range|:
name|chmod
operator|.
name|getBytes
argument_list|()
control|)
block|{
if|if
condition|(
name|c
argument_list|<
literal|'0'
operator|||
name|c
argument_list|>
literal|'7'
condition|)
block|{
name|chmod
operator|=
name|DEFAULT_MOD
expr_stmt|;
break|break;
block|}
block|}
block|}
else|else
block|{
name|chmod
operator|=
name|DEFAULT_MOD
expr_stmt|;
block|}
comment|// May be interesting to log the fallback to DEFAULT_MOD for invalid configuration
name|this
operator|.
name|chmod
operator|=
name|chmod
expr_stmt|;
block|}
DECL|method|getChmod ()
specifier|public
name|String
name|getChmod
parameter_list|()
block|{
return|return
name|chmod
return|;
block|}
comment|/**      * Set a comma separated list of ciphers that will be used in order of preference.      * Possible cipher names are defined by JCraft JSCH. Some examples include: aes128-ctr,aes128-cbc,3des-ctr,3des-cbc,blowfish-cbc,aes192-cbc,aes256-cbc.      * If not specified the default list from JSCH will be used.      */
DECL|method|setCiphers (String ciphers)
specifier|public
name|void
name|setCiphers
parameter_list|(
name|String
name|ciphers
parameter_list|)
block|{
name|this
operator|.
name|ciphers
operator|=
name|ciphers
expr_stmt|;
block|}
DECL|method|getCiphers ()
specifier|public
name|String
name|getCiphers
parameter_list|()
block|{
return|return
name|ciphers
return|;
block|}
comment|/**      * Set a comma separated list of authentications that will be used in order of preference.      * Possible authentication methods are defined by JCraft JSCH. Some examples include: gssapi-with-mic,publickey,keyboard-interactive,password      * If not specified the JSCH and/or system defaults will be used.      */
DECL|method|setPreferredAuthentications (final String preferredAuthentications)
specifier|public
name|void
name|setPreferredAuthentications
parameter_list|(
specifier|final
name|String
name|preferredAuthentications
parameter_list|)
block|{
name|this
operator|.
name|preferredAuthentications
operator|=
name|preferredAuthentications
expr_stmt|;
block|}
DECL|method|getPreferredAuthentications ()
specifier|public
name|String
name|getPreferredAuthentications
parameter_list|()
block|{
return|return
name|preferredAuthentications
return|;
block|}
block|}
end_class

end_unit

