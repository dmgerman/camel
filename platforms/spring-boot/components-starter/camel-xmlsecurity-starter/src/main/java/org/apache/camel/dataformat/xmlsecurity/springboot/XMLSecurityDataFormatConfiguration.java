begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmlsecurity.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|xmlsecurity
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel Partial XML Encryption/Decryption and XML Signature support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.securexml"
argument_list|)
DECL|class|XMLSecurityDataFormatConfiguration
specifier|public
class|class
name|XMLSecurityDataFormatConfiguration
block|{
comment|/**      * The cipher algorithm to be used for encryption/decryption of the XML      * message content. The available choices are: XMLCipher.TRIPLEDES      * XMLCipher.AES_128 XMLCipher.AES_128_GCM XMLCipher.AES_192      * XMLCipher.AES_192_GCM XMLCipher.AES_256 XMLCipher.AES_256_GCM      * XMLCipher.SEED_128 XMLCipher.CAMELLIA_128 XMLCipher.CAMELLIA_192      * XMLCipher.CAMELLIA_256 The default value is MLCipher.TRIPLEDES      */
DECL|field|xmlCipherAlgorithm
specifier|private
name|String
name|xmlCipherAlgorithm
decl_stmt|;
comment|/**      * A String used as passPhrase to encrypt/decrypt content. The passPhrase      * has to be provided. If no passPhrase is specified a default passPhrase is      * used. The passPhrase needs to be put together in conjunction with the      * appropriate encryption algorithm. For example using TRIPLEDES the      * passPhase can be a Only another 24 Byte key      */
DECL|field|passPhrase
specifier|private
name|String
name|passPhrase
decl_stmt|;
comment|/**      * The XPath reference to the XML Element selected for      * encryption/decryption. If no tag is specified the entire payload is      * encrypted/decrypted.      */
DECL|field|secureTag
specifier|private
name|String
name|secureTag
decl_stmt|;
comment|/**      * A boolean value to specify whether the XML Element is to be encrypted or      * the contents of the XML Element false = Element Level true = Element      * Content Level      */
DECL|field|secureTagContents
specifier|private
name|Boolean
name|secureTagContents
init|=
literal|false
decl_stmt|;
comment|/**      * The cipher algorithm to be used for encryption/decryption of the      * asymmetric key. The available choices are: XMLCipher.RSA_v1dot5      * XMLCipher.RSA_OAEP XMLCipher.RSA_OAEP_11 The default value is      * XMLCipher.RSA_OAEP      */
DECL|field|keyCipherAlgorithm
specifier|private
name|String
name|keyCipherAlgorithm
decl_stmt|;
comment|/**      * The key alias to be used when retrieving the recipient's public or      * private key from a KeyStore when performing asymmetric key encryption or      * decryption.      */
DECL|field|recipientKeyAlias
specifier|private
name|String
name|recipientKeyAlias
decl_stmt|;
comment|/**      * Refers to a KeyStore instance to lookup in the registry which is used for      * configuration options for creating and loading a KeyStore instance that      * represents the sender's trustStore or recipient's keyStore.      */
DECL|field|keyOrTrustStoreParametersId
specifier|private
name|String
name|keyOrTrustStoreParametersId
decl_stmt|;
comment|/**      * The password to be used for retrieving the private key from the KeyStore.      * This key is used for asymmetric decryption.      */
DECL|field|keyPassword
specifier|private
name|String
name|keyPassword
decl_stmt|;
comment|/**      * The digest algorithm to use with the RSA OAEP algorithm. The available      * choices are: XMLCipher.SHA1 XMLCipher.SHA256 XMLCipher.SHA512 The default      * value is XMLCipher.SHA1      */
DECL|field|digestAlgorithm
specifier|private
name|String
name|digestAlgorithm
decl_stmt|;
comment|/**      * The MGF Algorithm to use with the RSA OAEP algorithm. The available      * choices are: EncryptionConstants.MGF1_SHA1      * EncryptionConstants.MGF1_SHA256 EncryptionConstants.MGF1_SHA512 The      * default value is EncryptionConstants.MGF1_SHA1      */
DECL|field|mgfAlgorithm
specifier|private
name|String
name|mgfAlgorithm
decl_stmt|;
comment|/**      * Whether to add the public key used to encrypt the session key as a      * KeyValue in the EncryptedKey structure or not.      */
DECL|field|addKeyValueForEncryptedKey
specifier|private
name|Boolean
name|addKeyValueForEncryptedKey
init|=
literal|true
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getXmlCipherAlgorithm ()
specifier|public
name|String
name|getXmlCipherAlgorithm
parameter_list|()
block|{
return|return
name|xmlCipherAlgorithm
return|;
block|}
DECL|method|setXmlCipherAlgorithm (String xmlCipherAlgorithm)
specifier|public
name|void
name|setXmlCipherAlgorithm
parameter_list|(
name|String
name|xmlCipherAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|xmlCipherAlgorithm
operator|=
name|xmlCipherAlgorithm
expr_stmt|;
block|}
DECL|method|getPassPhrase ()
specifier|public
name|String
name|getPassPhrase
parameter_list|()
block|{
return|return
name|passPhrase
return|;
block|}
DECL|method|setPassPhrase (String passPhrase)
specifier|public
name|void
name|setPassPhrase
parameter_list|(
name|String
name|passPhrase
parameter_list|)
block|{
name|this
operator|.
name|passPhrase
operator|=
name|passPhrase
expr_stmt|;
block|}
DECL|method|getSecureTag ()
specifier|public
name|String
name|getSecureTag
parameter_list|()
block|{
return|return
name|secureTag
return|;
block|}
DECL|method|setSecureTag (String secureTag)
specifier|public
name|void
name|setSecureTag
parameter_list|(
name|String
name|secureTag
parameter_list|)
block|{
name|this
operator|.
name|secureTag
operator|=
name|secureTag
expr_stmt|;
block|}
DECL|method|getSecureTagContents ()
specifier|public
name|Boolean
name|getSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
return|;
block|}
DECL|method|setSecureTagContents (Boolean secureTagContents)
specifier|public
name|void
name|setSecureTagContents
parameter_list|(
name|Boolean
name|secureTagContents
parameter_list|)
block|{
name|this
operator|.
name|secureTagContents
operator|=
name|secureTagContents
expr_stmt|;
block|}
DECL|method|getKeyCipherAlgorithm ()
specifier|public
name|String
name|getKeyCipherAlgorithm
parameter_list|()
block|{
return|return
name|keyCipherAlgorithm
return|;
block|}
DECL|method|setKeyCipherAlgorithm (String keyCipherAlgorithm)
specifier|public
name|void
name|setKeyCipherAlgorithm
parameter_list|(
name|String
name|keyCipherAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|keyCipherAlgorithm
operator|=
name|keyCipherAlgorithm
expr_stmt|;
block|}
DECL|method|getRecipientKeyAlias ()
specifier|public
name|String
name|getRecipientKeyAlias
parameter_list|()
block|{
return|return
name|recipientKeyAlias
return|;
block|}
DECL|method|setRecipientKeyAlias (String recipientKeyAlias)
specifier|public
name|void
name|setRecipientKeyAlias
parameter_list|(
name|String
name|recipientKeyAlias
parameter_list|)
block|{
name|this
operator|.
name|recipientKeyAlias
operator|=
name|recipientKeyAlias
expr_stmt|;
block|}
DECL|method|getKeyOrTrustStoreParametersId ()
specifier|public
name|String
name|getKeyOrTrustStoreParametersId
parameter_list|()
block|{
return|return
name|keyOrTrustStoreParametersId
return|;
block|}
DECL|method|setKeyOrTrustStoreParametersId ( String keyOrTrustStoreParametersId)
specifier|public
name|void
name|setKeyOrTrustStoreParametersId
parameter_list|(
name|String
name|keyOrTrustStoreParametersId
parameter_list|)
block|{
name|this
operator|.
name|keyOrTrustStoreParametersId
operator|=
name|keyOrTrustStoreParametersId
expr_stmt|;
block|}
DECL|method|getKeyPassword ()
specifier|public
name|String
name|getKeyPassword
parameter_list|()
block|{
return|return
name|keyPassword
return|;
block|}
DECL|method|setKeyPassword (String keyPassword)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|String
name|keyPassword
parameter_list|)
block|{
name|this
operator|.
name|keyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
DECL|method|getDigestAlgorithm ()
specifier|public
name|String
name|getDigestAlgorithm
parameter_list|()
block|{
return|return
name|digestAlgorithm
return|;
block|}
DECL|method|setDigestAlgorithm (String digestAlgorithm)
specifier|public
name|void
name|setDigestAlgorithm
parameter_list|(
name|String
name|digestAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|digestAlgorithm
operator|=
name|digestAlgorithm
expr_stmt|;
block|}
DECL|method|getMgfAlgorithm ()
specifier|public
name|String
name|getMgfAlgorithm
parameter_list|()
block|{
return|return
name|mgfAlgorithm
return|;
block|}
DECL|method|setMgfAlgorithm (String mgfAlgorithm)
specifier|public
name|void
name|setMgfAlgorithm
parameter_list|(
name|String
name|mgfAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|mgfAlgorithm
operator|=
name|mgfAlgorithm
expr_stmt|;
block|}
DECL|method|getAddKeyValueForEncryptedKey ()
specifier|public
name|Boolean
name|getAddKeyValueForEncryptedKey
parameter_list|()
block|{
return|return
name|addKeyValueForEncryptedKey
return|;
block|}
DECL|method|setAddKeyValueForEncryptedKey (Boolean addKeyValueForEncryptedKey)
specifier|public
name|void
name|setAddKeyValueForEncryptedKey
parameter_list|(
name|Boolean
name|addKeyValueForEncryptedKey
parameter_list|)
block|{
name|this
operator|.
name|addKeyValueForEncryptedKey
operator|=
name|addKeyValueForEncryptedKey
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

