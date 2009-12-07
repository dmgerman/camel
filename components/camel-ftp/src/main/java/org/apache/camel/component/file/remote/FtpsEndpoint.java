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
name|TrustManagerFactory
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
comment|/**  * FTP Secure (FTP over SSL/TLS) endpoint  *   * @version $Revision$  * @author muellerc  */
end_comment

begin_class
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
DECL|method|FtpsEndpoint (String uri, RemoteFileComponent<FTPFile> remoteFileComponent, RemoteFileConfiguration configuration)
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
name|RemoteFileConfiguration
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
comment|/**      * create the FTPS client.      *       * @throws Exception       */
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
init|=
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
decl_stmt|;
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
name|keyStoreFileInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
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
name|trustStoreFileInputStream
operator|.
name|close
argument_list|()
expr_stmt|;
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
return|return
name|client
return|;
block|}
comment|/**      * Returns the FTPSClient. This method exists only for convenient.      *       * @return ftpsClient      */
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
comment|/**      * Returns the FtpsConfiguration. This method exists only for convenient.      *       * @return ftpsConfiguration      */
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
comment|/**      * Set the key store parameter      */
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
comment|/**      * Set the trust store parameter      */
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
block|}
end_class

end_unit

