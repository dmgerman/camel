begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|net
operator|.
name|URI
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
name|LoggingLevel
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
comment|/**  * Secure FTP configuration  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|SftpConfiguration
specifier|public
class|class
name|SftpConfiguration
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
DECL|field|knownHostsUri
specifier|private
name|String
name|knownHostsUri
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
DECL|field|knownHosts
specifier|private
name|byte
index|[]
name|knownHosts
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
DECL|field|privateKeyUri
specifier|private
name|String
name|privateKeyUri
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
DECL|field|privateKey
specifier|private
name|byte
index|[]
name|privateKey
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
DECL|field|privateKeyPassphrase
specifier|private
name|String
name|privateKeyPassphrase
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
DECL|field|keyPair
specifier|private
name|KeyPair
name|keyPair
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"no"
argument_list|,
name|enums
operator|=
literal|"no,yes"
argument_list|,
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|strictHostKeyChecking
specifier|private
name|String
name|strictHostKeyChecking
init|=
literal|"no"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|serverAliveInterval
specifier|private
name|int
name|serverAliveInterval
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|serverAliveCountMax
specifier|private
name|int
name|serverAliveCountMax
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|)
DECL|field|chmod
specifier|private
name|String
name|chmod
decl_stmt|;
comment|// comma separated list of ciphers.
comment|// null means default jsch list will be used
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
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
literal|"advanced"
argument_list|)
DECL|field|compression
specifier|private
name|int
name|compression
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|preferredAuthentications
specifier|private
name|String
name|preferredAuthentications
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"WARN"
argument_list|)
DECL|field|jschLoggingLevel
specifier|private
name|LoggingLevel
name|jschLoggingLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|bulkRequests
specifier|private
name|Integer
name|bulkRequests
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|bindAddress
specifier|private
name|String
name|bindAddress
decl_stmt|;
DECL|method|SftpConfiguration ()
specifier|public
name|SftpConfiguration
parameter_list|()
block|{
name|setProtocol
argument_list|(
literal|"sftp"
argument_list|)
expr_stmt|;
block|}
DECL|method|SftpConfiguration (URI uri)
specifier|public
name|SftpConfiguration
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
comment|/**      * Sets the known_hosts file, so that the SFTP endpoint can do host key verification.      */
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
DECL|method|getKnownHostsUri ()
specifier|public
name|String
name|getKnownHostsUri
parameter_list|()
block|{
return|return
name|knownHostsUri
return|;
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
comment|/**      * If knownHostFile has not been explicit configured then use the host file from System.getProperty(user.home)/.ssh/known_hosts      */
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
comment|/**      * Sets the known_hosts file (loaded from classpath by default), so that the SFTP endpoint can do host key verification.      */
DECL|method|setKnownHostsUri (String knownHostsUri)
specifier|public
name|void
name|setKnownHostsUri
parameter_list|(
name|String
name|knownHostsUri
parameter_list|)
block|{
name|this
operator|.
name|knownHostsUri
operator|=
name|knownHostsUri
expr_stmt|;
block|}
DECL|method|getKnownHosts ()
specifier|public
name|byte
index|[]
name|getKnownHosts
parameter_list|()
block|{
return|return
name|knownHosts
return|;
block|}
comment|/**      * Sets the known_hosts from the byte array, so that the SFTP endpoint can do host key verification.      */
DECL|method|setKnownHosts (byte[] knownHosts)
specifier|public
name|void
name|setKnownHosts
parameter_list|(
name|byte
index|[]
name|knownHosts
parameter_list|)
block|{
name|this
operator|.
name|knownHosts
operator|=
name|knownHosts
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
comment|/**      * Set the private key file so that the SFTP endpoint can do private key verification.      */
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
DECL|method|getPrivateKeyUri ()
specifier|public
name|String
name|getPrivateKeyUri
parameter_list|()
block|{
return|return
name|privateKeyUri
return|;
block|}
comment|/**      * Set the private key file (loaded from classpath by default) so that the SFTP endpoint can do private key verification.      */
DECL|method|setPrivateKeyUri (String privateKeyUri)
specifier|public
name|void
name|setPrivateKeyUri
parameter_list|(
name|String
name|privateKeyUri
parameter_list|)
block|{
name|this
operator|.
name|privateKeyUri
operator|=
name|privateKeyUri
expr_stmt|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|byte
index|[]
name|getPrivateKey
parameter_list|()
block|{
return|return
name|privateKey
return|;
block|}
comment|/**      * Set the private key as byte[] so that the SFTP endpoint can do private key verification.      */
DECL|method|setPrivateKey (byte[] privateKey)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|byte
index|[]
name|privateKey
parameter_list|)
block|{
name|this
operator|.
name|privateKey
operator|=
name|privateKey
expr_stmt|;
block|}
DECL|method|getPrivateKeyPassphrase ()
specifier|public
name|String
name|getPrivateKeyPassphrase
parameter_list|()
block|{
return|return
name|privateKeyPassphrase
return|;
block|}
comment|/**      * Set the private key file passphrase so that the SFTP endpoint can do private key verification.      */
DECL|method|setPrivateKeyPassphrase (String privateKeyFilePassphrase)
specifier|public
name|void
name|setPrivateKeyPassphrase
parameter_list|(
name|String
name|privateKeyFilePassphrase
parameter_list|)
block|{
name|this
operator|.
name|privateKeyPassphrase
operator|=
name|privateKeyFilePassphrase
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|getPrivateKeyFilePassphrase ()
specifier|public
name|String
name|getPrivateKeyFilePassphrase
parameter_list|()
block|{
return|return
name|privateKeyPassphrase
return|;
block|}
annotation|@
name|Deprecated
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
name|privateKeyPassphrase
operator|=
name|privateKeyFilePassphrase
expr_stmt|;
block|}
DECL|method|getKeyPair ()
specifier|public
name|KeyPair
name|getKeyPair
parameter_list|()
block|{
return|return
name|keyPair
return|;
block|}
comment|/**      * Sets a key pair of the public and private key so to that the SFTP endpoint can do public/private key verification.      */
DECL|method|setKeyPair (KeyPair keyPair)
specifier|public
name|void
name|setKeyPair
parameter_list|(
name|KeyPair
name|keyPair
parameter_list|)
block|{
name|this
operator|.
name|keyPair
operator|=
name|keyPair
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
comment|/**      * Sets whether to use strict host key checking.      */
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
comment|/**      * Allows you to set the serverAliveInterval of the sftp session      */
DECL|method|setServerAliveInterval (int serverAliveInterval)
specifier|public
name|void
name|setServerAliveInterval
parameter_list|(
name|int
name|serverAliveInterval
parameter_list|)
block|{
name|this
operator|.
name|serverAliveInterval
operator|=
name|serverAliveInterval
expr_stmt|;
block|}
DECL|method|getServerAliveInterval ()
specifier|public
name|int
name|getServerAliveInterval
parameter_list|()
block|{
return|return
name|serverAliveInterval
return|;
block|}
comment|/**      * Allows you to set the serverAliveCountMax of the sftp session      */
DECL|method|setServerAliveCountMax (int serverAliveCountMax)
specifier|public
name|void
name|setServerAliveCountMax
parameter_list|(
name|int
name|serverAliveCountMax
parameter_list|)
block|{
name|this
operator|.
name|serverAliveCountMax
operator|=
name|serverAliveCountMax
expr_stmt|;
block|}
DECL|method|getServerAliveCountMax ()
specifier|public
name|int
name|getServerAliveCountMax
parameter_list|()
block|{
return|return
name|serverAliveCountMax
return|;
block|}
comment|/**      * Allows you to set chmod on the stored file. For example chmod=640.      */
DECL|method|setChmod (String chmod)
specifier|public
name|void
name|setChmod
parameter_list|(
name|String
name|chmod
parameter_list|)
block|{
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
DECL|method|getCompression ()
specifier|public
name|int
name|getCompression
parameter_list|()
block|{
return|return
name|compression
return|;
block|}
comment|/**      * To use compression. Specify a level from 1 to 10.      * Important: You must manually add the needed JSCH zlib JAR to the classpath for compression support.      */
DECL|method|setCompression (int compression)
specifier|public
name|void
name|setCompression
parameter_list|(
name|int
name|compression
parameter_list|)
block|{
name|this
operator|.
name|compression
operator|=
name|compression
expr_stmt|;
block|}
comment|/**      * Set the preferred authentications which SFTP endpoint will used. Some example include:password,publickey.      * If not specified the default list from JSCH will be used.      */
DECL|method|setPreferredAuthentications (String pAuthentications)
specifier|public
name|void
name|setPreferredAuthentications
parameter_list|(
name|String
name|pAuthentications
parameter_list|)
block|{
name|this
operator|.
name|preferredAuthentications
operator|=
name|pAuthentications
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
DECL|method|getJschLoggingLevel ()
specifier|public
name|LoggingLevel
name|getJschLoggingLevel
parameter_list|()
block|{
return|return
name|jschLoggingLevel
return|;
block|}
comment|/**      * The logging level to use for JSCH activity logging.      * As JSCH is verbose at by default at INFO level the threshold is WARN by default.      */
DECL|method|setJschLoggingLevel (LoggingLevel jschLoggingLevel)
specifier|public
name|void
name|setJschLoggingLevel
parameter_list|(
name|LoggingLevel
name|jschLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|jschLoggingLevel
operator|=
name|jschLoggingLevel
expr_stmt|;
block|}
comment|/**      * Specifies how many requests may be outstanding at any one time. Increasing this value may      * slightly improve file transfer speed but will increase memory usage.      */
DECL|method|setBulkRequests (Integer bulkRequests)
specifier|public
name|void
name|setBulkRequests
parameter_list|(
name|Integer
name|bulkRequests
parameter_list|)
block|{
name|this
operator|.
name|bulkRequests
operator|=
name|bulkRequests
expr_stmt|;
block|}
DECL|method|getBulkRequests ()
specifier|public
name|Integer
name|getBulkRequests
parameter_list|()
block|{
return|return
name|bulkRequests
return|;
block|}
comment|/**      * Specifies the address of the local interface against which the connection should bind.      */
DECL|method|setBindAddress (String bindAddress)
specifier|public
name|void
name|setBindAddress
parameter_list|(
name|String
name|bindAddress
parameter_list|)
block|{
name|this
operator|.
name|bindAddress
operator|=
name|bindAddress
expr_stmt|;
block|}
DECL|method|getBindAddress ()
specifier|public
name|String
name|getBindAddress
parameter_list|()
block|{
return|return
name|bindAddress
return|;
block|}
block|}
end_class

end_unit

