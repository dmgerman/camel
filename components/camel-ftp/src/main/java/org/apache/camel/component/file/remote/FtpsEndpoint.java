begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSocket
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|camel
operator|.
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPClientConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|net
operator|.
name|ftp
operator|.
name|FTPSClient
import|;
end_import

begin_comment
comment|/**  * The ftps (FTP secure SSL/TLS) component is used for uploading or downloading files from FTP servers.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.2.0"
argument_list|,
name|scheme
operator|=
literal|"ftps"
argument_list|,
name|extendsScheme
operator|=
literal|"file"
argument_list|,
name|title
operator|=
literal|"FTPS"
argument_list|,
name|syntax
operator|=
literal|"ftps:host:port/directoryName"
argument_list|,
name|alternativeSyntax
operator|=
literal|"ftps:username:password@host:port/directoryName"
argument_list|,
name|label
operator|=
literal|"file"
argument_list|)
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed FtpsEndpoint"
argument_list|)
DECL|class|FtpsEndpoint
specifier|public
class|class
name|FtpsEndpoint
extends|extends
name|FtpEndpoint
argument_list|<
name|FTPFile
argument_list|>
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|protected
name|FtpsConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|protected
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|prefix
operator|=
literal|"ftpClient.keyStore."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|ftpClientKeyStoreParameters
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientKeyStoreParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|prefix
operator|=
literal|"ftpClient.trustStore."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|ftpClientTrustStoreParameters
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ftpClientTrustStoreParameters
decl_stmt|;
DECL|method|FtpsEndpoint ()
specifier|public
name|FtpsEndpoint
parameter_list|()
block|{     }
DECL|method|FtpsEndpoint (String uri, RemoteFileComponent<FTPFile> remoteFileComponent, FtpsConfiguration configuration)
specifier|public
name|FtpsEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RemoteFileComponent
argument_list|<
name|FTPFile
argument_list|>
name|remoteFileComponent
parameter_list|,
name|FtpsConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|remoteFileComponent
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|getFtpsConfiguration
argument_list|()
operator|.
name|getProtocol
argument_list|()
return|;
block|}
comment|/**      * Create the FTPS client.      */
DECL|method|createFtpClient ()
specifier|protected
name|FTPClient
name|createFtpClient
parameter_list|()
throws|throws
name|Exception
block|{
name|FTPSClient
name|client
decl_stmt|;
if|if
condition|(
name|sslContextParameters
operator|!=
literal|null
condition|)
block|{
name|SSLContext
name|context
init|=
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|client
operator|=
operator|new
name|FTPSClient
argument_list|(
name|getFtpsConfiguration
argument_list|()
operator|.
name|isImplicit
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
comment|// The FTPSClient tries to manage the following SSLSocket related configuration options
comment|// on its own based on internal configuration options.  FTPSClient does not lend itself
comment|// to subclassing for the purpose of overriding this behavior (private methods, fields, etc.).
comment|// As such, we create a socket (preconfigured by SSLContextParameters) from the context
comment|// we gave to FTPSClient and then setup FTPSClient to reuse the already configured configuration
comment|// from the socket for all future sockets it creates.  Not sexy and a little brittle, but it works.
name|SSLSocket
name|socket
init|=
operator|(
name|SSLSocket
operator|)
name|context
operator|.
name|getSocketFactory
argument_list|()
operator|.
name|createSocket
argument_list|()
decl_stmt|;
name|client
operator|.
name|setEnabledCipherSuites
argument_list|(
name|socket
operator|.
name|getEnabledCipherSuites
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEnabledProtocols
argument_list|(
name|socket
operator|.
name|getEnabledProtocols
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setNeedClientAuth
argument_list|(
name|socket
operator|.
name|getNeedClientAuth
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setWantClientAuth
argument_list|(
name|socket
operator|.
name|getWantClientAuth
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEnabledSessionCreation
argument_list|(
name|socket
operator|.
name|getEnableSessionCreation
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|=
operator|new
name|FTPSClient
argument_list|(
name|getFtpsConfiguration
argument_list|()
operator|.
name|getSecurityProtocol
argument_list|()
argument_list|,
name|getFtpsConfiguration
argument_list|()
operator|.
name|isImplicit
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|ftpClientKeyStoreParameters
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
operator|(
name|ftpClientKeyStoreParameters
operator|.
name|containsKey
argument_list|(
literal|"type"
argument_list|)
operator|)
condition|?
operator|(
name|String
operator|)
name|ftpClientKeyStoreParameters
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
else|:
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|String
name|file
init|=
operator|(
name|String
operator|)
name|ftpClientKeyStoreParameters
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
operator|(
name|String
operator|)
name|ftpClientKeyStoreParameters
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|algorithm
init|=
operator|(
name|ftpClientKeyStoreParameters
operator|.
name|containsKey
argument_list|(
literal|"algorithm"
argument_list|)
operator|)
condition|?
operator|(
name|String
operator|)
name|ftpClientKeyStoreParameters
operator|.
name|get
argument_list|(
literal|"algorithm"
argument_list|)
else|:
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|String
name|keyPassword
init|=
operator|(
name|String
operator|)
name|ftpClientKeyStoreParameters
operator|.
name|get
argument_list|(
literal|"keyPassword"
argument_list|)
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|FileInputStream
name|keyStoreFileInputStream
init|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|keyStore
operator|.
name|load
argument_list|(
name|keyStoreFileInputStream
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|keyStoreFileInputStream
argument_list|,
literal|"keyStore"
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
name|KeyManagerFactory
name|keyMgrFactory
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|keyMgrFactory
operator|.
name|init
argument_list|(
name|keyStore
argument_list|,
name|keyPassword
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|setNeedClientAuth
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|client
operator|.
name|setKeyManager
argument_list|(
name|keyMgrFactory
operator|.
name|getKeyManagers
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ftpClientTrustStoreParameters
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
operator|(
name|ftpClientTrustStoreParameters
operator|.
name|containsKey
argument_list|(
literal|"type"
argument_list|)
operator|)
condition|?
operator|(
name|String
operator|)
name|ftpClientTrustStoreParameters
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
else|:
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
decl_stmt|;
name|String
name|file
init|=
operator|(
name|String
operator|)
name|ftpClientTrustStoreParameters
operator|.
name|get
argument_list|(
literal|"file"
argument_list|)
decl_stmt|;
name|String
name|password
init|=
operator|(
name|String
operator|)
name|ftpClientTrustStoreParameters
operator|.
name|get
argument_list|(
literal|"password"
argument_list|)
decl_stmt|;
name|String
name|algorithm
init|=
operator|(
name|ftpClientTrustStoreParameters
operator|.
name|containsKey
argument_list|(
literal|"algorithm"
argument_list|)
operator|)
condition|?
operator|(
name|String
operator|)
name|ftpClientTrustStoreParameters
operator|.
name|get
argument_list|(
literal|"algorithm"
argument_list|)
else|:
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
decl_stmt|;
name|KeyStore
name|trustStore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|FileInputStream
name|trustStoreFileInputStream
init|=
operator|new
name|FileInputStream
argument_list|(
operator|new
name|File
argument_list|(
name|file
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|trustStore
operator|.
name|load
argument_list|(
name|trustStoreFileInputStream
argument_list|,
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|trustStoreFileInputStream
argument_list|,
literal|"trustStore"
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
name|TrustManagerFactory
name|trustMgrFactory
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|trustMgrFactory
operator|.
name|init
argument_list|(
name|trustStore
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrustManager
argument_list|(
name|trustMgrFactory
operator|.
name|getTrustManagers
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|client
return|;
block|}
annotation|@
name|Override
DECL|method|createRemoteFileOperations ()
specifier|public
name|RemoteFileOperations
argument_list|<
name|FTPFile
argument_list|>
name|createRemoteFileOperations
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure ftp client
name|FTPSClient
name|client
init|=
name|getFtpsClient
argument_list|()
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
comment|// must use a new client if not explicit configured to use a custom client
name|client
operator|=
operator|(
name|FTPSClient
operator|)
name|createFtpClient
argument_list|()
expr_stmt|;
block|}
comment|// use configured buffer size which is larger and therefore faster (as the default is no buffer)
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|setBufferSize
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getReceiveBufferSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// set any endpoint configured timeouts
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|client
operator|.
name|setConnectTimeout
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getConnectTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getSoTimeout
argument_list|()
operator|>
operator|-
literal|1
condition|)
block|{
name|soTimeout
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getSoTimeout
argument_list|()
expr_stmt|;
block|}
name|dataTimeout
operator|=
name|getConfiguration
argument_list|()
operator|.
name|getTimeout
argument_list|()
expr_stmt|;
if|if
condition|(
name|ftpClientParameters
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|localParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ftpClientParameters
argument_list|)
decl_stmt|;
comment|// setting soTimeout has to be done later on FTPClient (after it has connected)
name|Object
name|timeout
init|=
name|localParameters
operator|.
name|remove
argument_list|(
literal|"soTimeout"
argument_list|)
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|soTimeout
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
comment|// and we want to keep data timeout so we can log it later
name|timeout
operator|=
name|localParameters
operator|.
name|remove
argument_list|(
literal|"dataTimeout"
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|null
condition|)
block|{
name|dataTimeout
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|int
operator|.
name|class
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|client
argument_list|,
name|localParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ftpClientConfigParameters
operator|!=
literal|null
condition|)
block|{
comment|// client config is optional so create a new one if we have parameter for it
if|if
condition|(
name|ftpClientConfig
operator|==
literal|null
condition|)
block|{
name|ftpClientConfig
operator|=
operator|new
name|FTPClientConfig
argument_list|()
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|localConfigParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|ftpClientConfigParameters
argument_list|)
decl_stmt|;
name|setProperties
argument_list|(
name|ftpClientConfig
argument_list|,
name|localConfigParameters
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|dataTimeout
operator|>
literal|0
condition|)
block|{
name|client
operator|.
name|setDataTimeout
argument_list|(
name|dataTimeout
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Created FTPSClient [connectTimeout: {}, soTimeout: {}, dataTimeout: {}, bufferSize: {}"
operator|+
literal|", receiveDataSocketBufferSize: {}, sendDataSocketBufferSize: {}]: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|client
operator|.
name|getConnectTimeout
argument_list|()
block|,
name|getSoTimeout
argument_list|()
block|,
name|dataTimeout
block|,
name|client
operator|.
name|getBufferSize
argument_list|()
block|,
name|client
operator|.
name|getReceiveDataSocketBufferSize
argument_list|()
block|,
name|client
operator|.
name|getSendDataSocketBufferSize
argument_list|()
block|,
name|client
block|}
argument_list|)
expr_stmt|;
block|}
name|FtpsOperations
name|operations
init|=
operator|new
name|FtpsOperations
argument_list|(
name|client
argument_list|,
name|getFtpClientConfig
argument_list|()
argument_list|)
decl_stmt|;
name|operations
operator|.
name|setEndpoint
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|operations
return|;
block|}
comment|/**      * Returns the FTPSClient. This method exists only for convenient.      */
DECL|method|getFtpsClient ()
specifier|public
name|FTPSClient
name|getFtpsClient
parameter_list|()
block|{
return|return
operator|(
name|FTPSClient
operator|)
name|getFtpClient
argument_list|()
return|;
block|}
comment|/**      * Returns the FtpsConfiguration. This method exists only for convenient.      */
DECL|method|getFtpsConfiguration ()
specifier|public
name|FtpsConfiguration
name|getFtpsConfiguration
parameter_list|()
block|{
return|return
operator|(
name|FtpsConfiguration
operator|)
name|getConfiguration
argument_list|()
return|;
block|}
comment|/**      * Set the key store parameters      */
DECL|method|setFtpClientKeyStoreParameters (Map<String, Object> param)
specifier|public
name|void
name|setFtpClientKeyStoreParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
parameter_list|)
block|{
name|this
operator|.
name|ftpClientKeyStoreParameters
operator|=
name|param
expr_stmt|;
block|}
comment|/**      * Set the trust store parameters      */
DECL|method|setFtpClientTrustStoreParameters (Map<String, Object> param)
specifier|public
name|void
name|setFtpClientTrustStoreParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|param
parameter_list|)
block|{
name|this
operator|.
name|ftpClientTrustStoreParameters
operator|=
name|param
expr_stmt|;
block|}
comment|/**      * Gets the JSSE configuration that overrides any settings in {@link FtpsEndpoint#ftpClientKeyStoreParameters},      * {@link #ftpClientTrustStoreParameters}, and {@link FtpsConfiguration#getSecurityProtocol()}.      */
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * Gets the JSSE configuration that overrides any settings in {@link FtpsEndpoint#ftpClientKeyStoreParameters},      * {@link #ftpClientTrustStoreParameters}, and {@link FtpsConfiguration#getSecurityProtocol()}.      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
block|}
end_class

end_unit

