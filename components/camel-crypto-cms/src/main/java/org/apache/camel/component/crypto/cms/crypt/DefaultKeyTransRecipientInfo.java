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
name|KeyStoreException
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|common
operator|.
name|DefaultCryptoCmsConfiguration
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|exception
operator|.
name|CryptoCmsNoKeyOrCertificateForAliasException
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
comment|/**  * Information about the receiver of an encrypted message. The encryption public  * key is defined via an alias referencing an entry in a keystore.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DefaultKeyTransRecipientInfo
specifier|public
class|class
name|DefaultKeyTransRecipientInfo
extends|extends
name|DefaultCryptoCmsConfiguration
implements|implements
name|TransRecipientInfo
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"encrypt"
argument_list|)
DECL|field|certificateAlias
specifier|private
name|String
name|certificateAlias
decl_stmt|;
DECL|field|keyEncryptionAlgorithm
specifier|private
name|String
name|keyEncryptionAlgorithm
init|=
literal|"RSA"
decl_stmt|;
DECL|method|getCertificateAlias ()
specifier|protected
name|String
name|getCertificateAlias
parameter_list|()
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|certificateAlias
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Certificate alias not configured in recipient "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|certificateAlias
return|;
block|}
comment|/**      * Keytstore alias for looking up the X.509 certificate whose public key is      * used to encrypt the secret symmetric encryption key.      *       * @param certificateAlias alias      */
DECL|method|setCertificateAlias (String certificateAlias)
specifier|public
name|void
name|setCertificateAlias
parameter_list|(
name|String
name|certificateAlias
parameter_list|)
block|{
name|this
operator|.
name|certificateAlias
operator|=
name|certificateAlias
expr_stmt|;
block|}
comment|// /**
comment|// * Encryption Algorithm used for encrypting the secret key in
comment|// * {@link CmsEnvelopedDataEncryptor}.
comment|// *
comment|// * @param keyEncryptionAlgorithm algorithm, for example "RSA"
comment|// */
comment|// public void setKeyEncryptionAlgorithm(String keyEncryptionAlgorithm) {
comment|// this.keyEncryptionAlgorithm = keyEncryptionAlgorithm;
comment|// }
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"certificate alias="
operator|+
name|certificateAlias
operator|+
literal|", key encryption algorithm="
operator|+
name|keyEncryptionAlgorithm
return|;
block|}
comment|/** Currently, the key encryption algorithm is fixed to "RSA". */
annotation|@
name|Override
DECL|method|getKeyEncryptionAlgorithm (Exchange exchange)
specifier|public
name|String
name|getKeyEncryptionAlgorithm
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
return|return
name|keyEncryptionAlgorithm
return|;
block|}
annotation|@
name|Override
DECL|method|getCertificate (Exchange exchange)
specifier|public
name|X509Certificate
name|getCertificate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
name|String
name|alias
init|=
name|getCertificateAlias
argument_list|()
decl_stmt|;
name|Certificate
name|cert
decl_stmt|;
try|try
block|{
name|cert
operator|=
name|getKeyStore
argument_list|()
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Problem during reading the certificate with the alias '"
operator|+
name|alias
operator|+
literal|"' from the keystore of the recipient "
operator|+
name|this
argument_list|)
throw|;
block|}
if|if
condition|(
name|cert
operator|instanceof
name|X509Certificate
condition|)
block|{
return|return
operator|(
name|X509Certificate
operator|)
name|cert
return|;
block|}
throw|throw
operator|new
name|CryptoCmsNoKeyOrCertificateForAliasException
argument_list|(
literal|"No X509 certificate found for the alias '"
operator|+
name|alias
operator|+
literal|"' in the keystore of the recipient "
operator|+
name|this
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

