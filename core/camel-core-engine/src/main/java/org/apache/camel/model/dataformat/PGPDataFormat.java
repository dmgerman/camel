begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|DataFormatDefinition
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

begin_comment
comment|/**  * PGP data format is used for encrypting and decrypting of messages using Java  * Cryptographic Extension and PGP.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.9.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,security"
argument_list|,
name|title
operator|=
literal|"PGP"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"pgp"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|PGPDataFormat
specifier|public
class|class
name|PGPDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|keyUserid
specifier|private
name|String
name|keyUserid
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|signatureKeyUserid
specifier|private
name|String
name|signatureKeyUserid
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|signaturePassword
specifier|private
name|String
name|signaturePassword
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keyFileName
specifier|private
name|String
name|keyFileName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|signatureKeyFileName
specifier|private
name|String
name|signatureKeyFileName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|signatureKeyRing
specifier|private
name|String
name|signatureKeyRing
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|armored
specifier|private
name|String
name|armored
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|javaType
operator|=
literal|"java.lang.Boolean"
argument_list|)
DECL|field|integrity
specifier|private
name|String
name|integrity
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|provider
specifier|private
name|String
name|provider
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Integer"
argument_list|)
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Integer"
argument_list|)
DECL|field|compressionAlgorithm
specifier|private
name|String
name|compressionAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|javaType
operator|=
literal|"java.lang.Integer"
argument_list|)
DECL|field|hashAlgorithm
specifier|private
name|String
name|hashAlgorithm
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|signatureVerificationOption
specifier|private
name|String
name|signatureVerificationOption
decl_stmt|;
DECL|method|PGPDataFormat ()
specifier|public
name|PGPDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"pgp"
argument_list|)
expr_stmt|;
block|}
DECL|method|getSignatureKeyUserid ()
specifier|public
name|String
name|getSignatureKeyUserid
parameter_list|()
block|{
return|return
name|signatureKeyUserid
return|;
block|}
comment|/**      * User ID of the key in the PGP keyring used for signing (during      * encryption) or signature verification (during decryption). During the      * signature verification process the specified User ID restricts the public      * keys from the public keyring which can be used for the verification. If      * no User ID is specified for the signature verficiation then any public      * key in the public keyring can be used for the verification. Can also be      * only a part of a user ID. For example, if the user ID is "Test User      *<test@camel.com>" then you can use the part "Test User" or      * "<test@camel.com>" to address the User ID.      */
DECL|method|setSignatureKeyUserid (String signatureKeyUserid)
specifier|public
name|void
name|setSignatureKeyUserid
parameter_list|(
name|String
name|signatureKeyUserid
parameter_list|)
block|{
name|this
operator|.
name|signatureKeyUserid
operator|=
name|signatureKeyUserid
expr_stmt|;
block|}
DECL|method|getSignaturePassword ()
specifier|public
name|String
name|getSignaturePassword
parameter_list|()
block|{
return|return
name|signaturePassword
return|;
block|}
comment|/**      * Password used when opening the private key used for signing (during      * encryption).      */
DECL|method|setSignaturePassword (String signaturePassword)
specifier|public
name|void
name|setSignaturePassword
parameter_list|(
name|String
name|signaturePassword
parameter_list|)
block|{
name|this
operator|.
name|signaturePassword
operator|=
name|signaturePassword
expr_stmt|;
block|}
DECL|method|getSignatureKeyFileName ()
specifier|public
name|String
name|getSignatureKeyFileName
parameter_list|()
block|{
return|return
name|signatureKeyFileName
return|;
block|}
comment|/**      * Filename of the keyring to use for signing (during encryption) or for      * signature verification (during decryption); must be accessible as a      * classpath resource (but you can specify a location in the file system by      * using the "file:" prefix).      */
DECL|method|setSignatureKeyFileName (String signatureKeyFileName)
specifier|public
name|void
name|setSignatureKeyFileName
parameter_list|(
name|String
name|signatureKeyFileName
parameter_list|)
block|{
name|this
operator|.
name|signatureKeyFileName
operator|=
name|signatureKeyFileName
expr_stmt|;
block|}
DECL|method|getSignatureKeyRing ()
specifier|public
name|String
name|getSignatureKeyRing
parameter_list|()
block|{
return|return
name|signatureKeyRing
return|;
block|}
comment|/**      * Keyring used for signing/verifying as byte array. You can not set the      * signatureKeyFileName and signatureKeyRing at the same time.      */
DECL|method|setSignatureKeyRing (String signatureKeyRing)
specifier|public
name|void
name|setSignatureKeyRing
parameter_list|(
name|String
name|signatureKeyRing
parameter_list|)
block|{
name|this
operator|.
name|signatureKeyRing
operator|=
name|signatureKeyRing
expr_stmt|;
block|}
DECL|method|getHashAlgorithm ()
specifier|public
name|String
name|getHashAlgorithm
parameter_list|()
block|{
return|return
name|hashAlgorithm
return|;
block|}
comment|/**      * Signature hash algorithm; possible values are defined in      * org.bouncycastle.bcpg.HashAlgorithmTags; for example 2 (= SHA1), 8 (=      * SHA256), 9 (= SHA384), 10 (= SHA512), 11 (=SHA224). Only relevant for      * signing.      */
DECL|method|setHashAlgorithm (String hashAlgorithm)
specifier|public
name|void
name|setHashAlgorithm
parameter_list|(
name|String
name|hashAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|hashAlgorithm
operator|=
name|hashAlgorithm
expr_stmt|;
block|}
DECL|method|getArmored ()
specifier|public
name|String
name|getArmored
parameter_list|()
block|{
return|return
name|armored
return|;
block|}
comment|/**      * This option will cause PGP to base64 encode the encrypted text, making it      * available for copy/paste, etc.      */
DECL|method|setArmored (String armored)
specifier|public
name|void
name|setArmored
parameter_list|(
name|String
name|armored
parameter_list|)
block|{
name|this
operator|.
name|armored
operator|=
name|armored
expr_stmt|;
block|}
DECL|method|getIntegrity ()
specifier|public
name|String
name|getIntegrity
parameter_list|()
block|{
return|return
name|integrity
return|;
block|}
comment|/**      * Adds an integrity check/sign into the encryption file.      *<p/>      * The default value is true.      */
DECL|method|setIntegrity (String integrity)
specifier|public
name|void
name|setIntegrity
parameter_list|(
name|String
name|integrity
parameter_list|)
block|{
name|this
operator|.
name|integrity
operator|=
name|integrity
expr_stmt|;
block|}
DECL|method|getKeyFileName ()
specifier|public
name|String
name|getKeyFileName
parameter_list|()
block|{
return|return
name|keyFileName
return|;
block|}
comment|/**      * Filename of the keyring; must be accessible as a classpath resource (but      * you can specify a location in the file system by using the "file:"      * prefix).      */
DECL|method|setKeyFileName (String keyFileName)
specifier|public
name|void
name|setKeyFileName
parameter_list|(
name|String
name|keyFileName
parameter_list|)
block|{
name|this
operator|.
name|keyFileName
operator|=
name|keyFileName
expr_stmt|;
block|}
DECL|method|getKeyUserid ()
specifier|public
name|String
name|getKeyUserid
parameter_list|()
block|{
return|return
name|keyUserid
return|;
block|}
comment|/**      * The user ID of the key in the PGP keyring used during encryption. Can      * also be only a part of a user ID. For example, if the user ID is "Test      * User<test@camel.com>" then you can use the part "Test User" or      * "<test@camel.com>" to address the user ID.      */
DECL|method|setKeyUserid (String keyUserid)
specifier|public
name|void
name|setKeyUserid
parameter_list|(
name|String
name|keyUserid
parameter_list|)
block|{
name|this
operator|.
name|keyUserid
operator|=
name|keyUserid
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
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|algorithm
return|;
block|}
comment|/**      * Symmetric key encryption algorithm; possible values are defined in      * org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags; for example 2 (= TRIPLE      * DES), 3 (= CAST5), 4 (= BLOWFISH), 6 (= DES), 7 (= AES_128). Only      * relevant for encrypting.      */
DECL|method|setAlgorithm (String algorithm)
specifier|public
name|void
name|setAlgorithm
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
block|}
DECL|method|getCompressionAlgorithm ()
specifier|public
name|String
name|getCompressionAlgorithm
parameter_list|()
block|{
return|return
name|compressionAlgorithm
return|;
block|}
comment|/**      * Compression algorithm; possible values are defined in      * org.bouncycastle.bcpg.CompressionAlgorithmTags; for example 0 (=      * UNCOMPRESSED), 1 (= ZIP), 2 (= ZLIB), 3 (= BZIP2). Only relevant for      * encrypting.      */
DECL|method|setCompressionAlgorithm (String compressionAlgorithm)
specifier|public
name|void
name|setCompressionAlgorithm
parameter_list|(
name|String
name|compressionAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|compressionAlgorithm
operator|=
name|compressionAlgorithm
expr_stmt|;
block|}
comment|/**      * Password used when opening the private key (not used for encryption).      */
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
DECL|method|getProvider ()
specifier|public
name|String
name|getProvider
parameter_list|()
block|{
return|return
name|provider
return|;
block|}
comment|/**      * Java Cryptography Extension (JCE) provider, default is Bouncy Castle      * ("BC"). Alternatively you can use, for example, the IAIK JCE provider; in      * this case the provider must be registered beforehand and the Bouncy      * Castle provider must not be registered beforehand. The Sun JCE provider      * does not work.      */
DECL|method|setProvider (String provider)
specifier|public
name|void
name|setProvider
parameter_list|(
name|String
name|provider
parameter_list|)
block|{
name|this
operator|.
name|provider
operator|=
name|provider
expr_stmt|;
block|}
DECL|method|getSignatureVerificationOption ()
specifier|public
name|String
name|getSignatureVerificationOption
parameter_list|()
block|{
return|return
name|signatureVerificationOption
return|;
block|}
comment|/**      * Controls the behavior for verifying the signature during unmarshaling.      * There are 4 values possible: "optional": The PGP message may or may not      * contain signatures; if it does contain signatures, then a signature      * verification is executed. "required": The PGP message must contain at      * least one signature; if this is not the case an exception (PGPException)      * is thrown. A signature verification is executed. "ignore": Contained      * signatures in the PGP message are ignored; no signature verification is      * executed. "no_signature_allowed": The PGP message must not contain a      * signature; otherwise an exception (PGPException) is thrown.      */
DECL|method|setSignatureVerificationOption (String signatureVerificationOption)
specifier|public
name|void
name|setSignatureVerificationOption
parameter_list|(
name|String
name|signatureVerificationOption
parameter_list|)
block|{
name|this
operator|.
name|signatureVerificationOption
operator|=
name|signatureVerificationOption
expr_stmt|;
block|}
block|}
end_class

end_unit

