begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.crypt
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
name|crypt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Key
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
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|UnrecoverableKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Exchange
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|DefaultCryptoCmsUnMarshallerConfiguration
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

begin_comment
comment|/**  * The defualt implementation fetches the private key and certificate from a keystore.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DefaultEnvelopedDataDecryptorConfiguration
specifier|public
class|class
name|DefaultEnvelopedDataDecryptorConfiguration
extends|extends
name|DefaultCryptoCmsUnMarshallerConfiguration
implements|implements
name|EnvelopedDataDecryptorConfiguration
implements|,
name|Cloneable
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"decrypt"
argument_list|)
DECL|field|password
specifier|private
name|char
index|[]
name|password
decl_stmt|;
DECL|method|DefaultEnvelopedDataDecryptorConfiguration ()
specifier|public
name|DefaultEnvelopedDataDecryptorConfiguration
parameter_list|()
block|{     }
comment|/**      * Sets the password of the private keys. It is assumed that all private      * keys in the keystore have the same password. If not set then it is      * assumed that the password of the private keys is given by the keystore      * password given in the {@link KeyStoreParameters}.      */
DECL|method|setPassword (char[] password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|char
index|[]
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
DECL|method|getPassword ()
specifier|public
name|char
index|[]
name|getPassword
parameter_list|()
block|{
if|if
condition|(
name|password
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|getKeyStoreParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|passwordS
init|=
name|getKeyStoreParameters
argument_list|()
operator|.
name|getPassword
argument_list|()
decl_stmt|;
if|if
condition|(
name|passwordS
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Password for private keys not configured"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|passwordS
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Password for private keys not configured"
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
name|password
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getPrivateKeyCertificateCollection (Exchange exchange)
specifier|public
name|Collection
argument_list|<
name|PrivateKeyWithCertificate
argument_list|>
name|getPrivateKeyCertificateCollection
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
name|KeyStore
name|keystore
init|=
name|getKeyStore
argument_list|()
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|PrivateKeyWithCertificate
argument_list|>
name|privateKeys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|keystore
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Enumeration
argument_list|<
name|String
argument_list|>
name|aliases
init|=
name|keystore
operator|.
name|aliases
argument_list|()
init|;
name|aliases
operator|.
name|hasMoreElements
argument_list|()
condition|;
control|)
block|{
name|String
name|alias
init|=
name|aliases
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|keystore
operator|.
name|isKeyEntry
argument_list|(
name|alias
argument_list|)
condition|)
block|{
comment|// only key entries are relevant!
continue|continue;
block|}
name|Key
name|privateKey
init|=
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|privateKey
operator|instanceof
name|PrivateKey
condition|)
block|{
comment|// we currently only support assymmetric keys
name|Certificate
name|cert
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
decl_stmt|;
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
name|privateKeys
operator|.
name|add
argument_list|(
operator|new
name|PrivateKeyWithCertificate
argument_list|(
operator|(
name|PrivateKey
operator|)
name|privateKey
argument_list|,
operator|(
name|X509Certificate
operator|)
name|cert
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|privateKeys
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"No private keys in keystore found. Check your configuration."
argument_list|)
throw|;
block|}
return|return
name|privateKeys
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
decl||
name|UnrecoverableKeyException
decl||
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Problem during reading the private keys from the keystore"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|DefaultEnvelopedDataDecryptorConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DefaultEnvelopedDataDecryptorConfiguration
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
comment|// should never happen
block|}
block|}
block|}
end_class

end_unit

