begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms.sig
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
name|sig
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

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSAttributeTableGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|DefaultSignedAttributeTableGenerator
import|;
end_import

begin_comment
comment|/**  * Reads the signer information from a Java keystore. You have to specify an  * alias for the private key entry, the signature algorithm, and the keystore.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|DefaultSignerInfo
specifier|public
class|class
name|DefaultSignerInfo
extends|extends
name|DefaultCryptoCmsConfiguration
implements|implements
name|SignerInfo
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|privateKeyAlias
specifier|private
name|String
name|privateKeyAlias
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|password
specifier|private
name|char
index|[]
name|password
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"SHA256withRSA"
argument_list|)
DECL|field|signatureAlgorithm
specifier|private
name|String
name|signatureAlgorithm
init|=
literal|"SHA256withRSA"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|includeCertificates
specifier|private
name|boolean
name|includeCertificates
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|signedAttributeGenerator
specifier|private
name|CMSAttributeTableGenerator
name|signedAttributeGenerator
init|=
operator|new
name|DefaultSignedAttributeTableGenerator
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"null"
argument_list|)
DECL|field|unsignedAttributeGenerator
specifier|private
name|CMSAttributeTableGenerator
name|unsignedAttributeGenerator
decl_stmt|;
comment|/**      * Password of the private key. If not set then the password set in the      * parameter 'keystoreParameters' is used.      */
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
DECL|method|getPassword (Exchange exchange)
specifier|protected
name|char
index|[]
name|getPassword
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|password
operator|!=
literal|null
condition|)
block|{
return|return
name|password
return|;
block|}
name|String
name|pw
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getKeyStoreParameters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|pw
operator|=
name|getKeyStoreParameters
argument_list|()
operator|.
name|getPassword
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|pw
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"No password for accessing the private key from the keystore found for the singer infor "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|pw
operator|.
name|toCharArray
argument_list|()
return|;
block|}
DECL|method|getPrivateKeyAlias (Exchange exchange)
specifier|protected
name|String
name|getPrivateKeyAlias
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|privateKeyAlias
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"No alias defined for signer info "
operator|+
name|this
argument_list|)
throw|;
block|}
return|return
name|privateKeyAlias
return|;
block|}
comment|/**      * Alias of the private key entry in the keystore which is used for signing.      */
DECL|method|setPrivateKeyAlias (String privateKeyAlias)
specifier|public
name|void
name|setPrivateKeyAlias
parameter_list|(
name|String
name|privateKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|privateKeyAlias
operator|=
name|privateKeyAlias
expr_stmt|;
block|}
comment|/**      * Signature algorithm. The default algorithm is "SHA256withRSA".      *<p>      * Attention, the signature algorithm must fit to the signer private key.      */
DECL|method|setSignatureAlgorithm (String signatureAlgorithm)
specifier|public
name|void
name|setSignatureAlgorithm
parameter_list|(
name|String
name|signatureAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|signatureAlgorithm
operator|=
name|signatureAlgorithm
expr_stmt|;
block|}
comment|/**      * If<tt>true</tt> then the certificate chain corresponding to the alias of      * the private key is added to the certificate list of the Signed Data      * instance.      */
DECL|method|setIncludeCertificates (boolean includeCertificates)
specifier|public
name|void
name|setIncludeCertificates
parameter_list|(
name|boolean
name|includeCertificates
parameter_list|)
block|{
name|this
operator|.
name|includeCertificates
operator|=
name|includeCertificates
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSignatureAlgorithm (Exchange exchange)
specifier|public
name|String
name|getSignatureAlgorithm
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
return|return
name|signatureAlgorithm
return|;
block|}
annotation|@
name|Override
DECL|method|getPrivateKey (Exchange exchange)
specifier|public
name|PrivateKey
name|getPrivateKey
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
name|getPrivateKeyAlias
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
name|Key
name|key
init|=
name|getKeyStore
argument_list|()
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|getPassword
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|instanceof
name|PrivateKey
condition|)
block|{
return|return
operator|(
name|PrivateKey
operator|)
name|key
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnrecoverableKeyException
decl||
name|KeyStoreException
decl||
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoCmsException
argument_list|(
literal|"Problem occured during accessing the private key for the alias '"
operator|+
name|alias
operator|+
literal|"' in the keystore of signer "
operator|+
name|this
argument_list|)
throw|;
block|}
throw|throw
operator|new
name|CryptoCmsNoKeyOrCertificateForAliasException
argument_list|(
literal|"No private key found  for the alias '"
operator|+
name|alias
operator|+
literal|"' in the keystore of signer "
operator|+
name|this
argument_list|)
throw|;
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
name|getPrivateKeyAlias
argument_list|(
name|exchange
argument_list|)
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
literal|"Problem during accessing the certificate for the alias '"
operator|+
name|alias
operator|+
literal|"' in the signer "
operator|+
name|this
argument_list|,
name|e
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
literal|"No X.509 certificate found for alias '"
operator|+
name|alias
operator|+
literal|"' in the keystore of signer "
operator|+
name|this
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|getCertificateChain (Exchange exchange)
specifier|public
name|Certificate
index|[]
name|getCertificateChain
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
if|if
condition|(
name|includeCertificates
condition|)
block|{
name|String
name|alias
init|=
name|getPrivateKeyAlias
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Certificate
index|[]
name|certs
decl_stmt|;
try|try
block|{
name|certs
operator|=
name|getKeyStore
argument_list|()
operator|.
name|getCertificateChain
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
literal|"Problem during accessing the certificate chain for the alias '"
operator|+
name|alias
operator|+
literal|"' in the keystore of signer "
operator|+
name|this
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|certs
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|Certificate
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
return|return
name|certs
return|;
block|}
block|}
else|else
block|{
return|return
operator|new
name|Certificate
index|[
literal|0
index|]
return|;
block|}
block|}
comment|/**      * Signed attributes of the Signed Data instance. By default contentType,      * signingTime, messageDigest, and id-aa-CMSAlgorithmProtection are set.      */
DECL|method|setSignedAttributeGenerator (CMSAttributeTableGenerator signedAttributeGenerator)
specifier|public
name|void
name|setSignedAttributeGenerator
parameter_list|(
name|CMSAttributeTableGenerator
name|signedAttributeGenerator
parameter_list|)
block|{
name|this
operator|.
name|signedAttributeGenerator
operator|=
name|signedAttributeGenerator
expr_stmt|;
block|}
comment|/**      * Unsigned attributes of the Signed Data instance. By default no unsigned      * attribute is set.      */
DECL|method|setUnsignedAttributeGenerator (CMSAttributeTableGenerator unsignedAttributeGenerator)
specifier|public
name|void
name|setUnsignedAttributeGenerator
parameter_list|(
name|CMSAttributeTableGenerator
name|unsignedAttributeGenerator
parameter_list|)
block|{
name|this
operator|.
name|unsignedAttributeGenerator
operator|=
name|unsignedAttributeGenerator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSignedAttributeGenerator (Exchange exchange)
specifier|public
name|CMSAttributeTableGenerator
name|getSignedAttributeGenerator
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
return|return
name|signedAttributeGenerator
return|;
block|}
annotation|@
name|Override
DECL|method|getUnsignedAttributeGenerator (Exchange exchange)
specifier|public
name|CMSAttributeTableGenerator
name|getUnsignedAttributeGenerator
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|CryptoCmsException
block|{
return|return
name|unsignedAttributeGenerator
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"private key alias="
operator|+
name|privateKeyAlias
operator|+
literal|", signature algorithm="
operator|+
name|signatureAlgorithm
operator|+
literal|", isIncludeCertificates="
operator|+
name|includeCertificates
return|;
block|}
block|}
end_class

end_unit

