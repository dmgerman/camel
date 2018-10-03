begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsException
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
name|support
operator|.
name|jsse
operator|.
name|KeyStoreParameters
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|DefaultCryptoCmsConfiguration
specifier|public
specifier|abstract
class|class
name|DefaultCryptoCmsConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|keyStoreParameters
specifier|private
name|KeyStoreParameters
name|keyStoreParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"common"
argument_list|)
DECL|field|keyStore
specifier|private
name|KeyStore
name|keyStore
decl_stmt|;
comment|/**      * Keystore containing signer private keys, verifier public keys, encryptor      * public keys, decryptor private keys depending on the operation. Use      * either this parameter or the parameter 'keystore'.      */
DECL|method|setKeyStoreParameters (KeyStoreParameters keyStoreParameters)
specifier|public
name|void
name|setKeyStoreParameters
parameter_list|(
name|KeyStoreParameters
name|keyStoreParameters
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
name|this
operator|.
name|keyStoreParameters
operator|=
name|keyStoreParameters
expr_stmt|;
if|if
condition|(
name|keyStoreParameters
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|this
operator|.
name|keyStore
operator|=
name|keyStoreParameters
operator|.
name|createKeyStore
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
decl||
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Problem during generating the keystore"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
comment|/**      * Keystore which contains signer private keys, verifier public keys,      * encryptor public keys, decryptor private keys depending on the operation.      * Use either this parameter or the parameter 'keyStoreParameters'.      */
DECL|method|setKeyStore (KeyStore keyStore)
specifier|public
name|void
name|setKeyStore
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|)
block|{
name|this
operator|.
name|keyStore
operator|=
name|keyStore
expr_stmt|;
block|}
DECL|method|getKeyStore ()
specifier|protected
name|KeyStore
name|getKeyStore
parameter_list|()
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|keyStore
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Keystore not configured"
argument_list|)
throw|;
block|}
return|return
name|keyStore
return|;
block|}
DECL|method|getKeyStoreParameters ()
specifier|protected
name|KeyStoreParameters
name|getKeyStoreParameters
parameter_list|()
block|{
return|return
name|keyStoreParameters
return|;
block|}
block|}
end_class

end_unit

