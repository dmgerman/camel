begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

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
name|List
import|;
end_import

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
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The crypto cms component is used for encrypting data in CMS Enveloped Data  * format, decrypting CMS Enveloped Data, signing data in CMS Signed Data  * format, and verifying CMS Signed Data.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|CryptoCmsEndpointBuilderFactory
specifier|public
interface|interface
name|CryptoCmsEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Crypto CMS component.      */
DECL|interface|CryptoCmsEndpointBuilder
specifier|public
interface|interface
name|CryptoCmsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedCryptoCmsEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedCryptoCmsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Keystore which contains signer private keys, verifier public keys,          * encryptor public keys, decryptor private keys depending on the          * operation. Use either this parameter or the parameter          * 'keyStoreParameters'.          *           * The option is a:<code>java.security.KeyStore</code> type.          *           * Group: common          */
DECL|method|keyStore (KeyStore keyStore)
specifier|default
name|CryptoCmsEndpointBuilder
name|keyStore
parameter_list|(
name|KeyStore
name|keyStore
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"keyStore"
argument_list|,
name|keyStore
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Keystore which contains signer private keys, verifier public keys,          * encryptor public keys, decryptor private keys depending on the          * operation. Use either this parameter or the parameter          * 'keyStoreParameters'.          *           * The option will be converted to a<code>java.security.KeyStore</code>          * type.          *           * Group: common          */
DECL|method|keyStore (String keyStore)
specifier|default
name|CryptoCmsEndpointBuilder
name|keyStore
parameter_list|(
name|String
name|keyStore
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"keyStore"
argument_list|,
name|keyStore
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Keystore containing signer private keys, verifier public keys,          * encryptor public keys, decryptor private keys depending on the          * operation. Use either this parameter or the parameter 'keystore'.          *           * The option is a:          *<code>org.apache.camel.support.jsse.KeyStoreParameters</code> type.          *           * Group: common          */
DECL|method|keyStoreParameters ( Object keyStoreParameters)
specifier|default
name|CryptoCmsEndpointBuilder
name|keyStoreParameters
parameter_list|(
name|Object
name|keyStoreParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"keyStoreParameters"
argument_list|,
name|keyStoreParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Keystore containing signer private keys, verifier public keys,          * encryptor public keys, decryptor private keys depending on the          * operation. Use either this parameter or the parameter 'keystore'.          *           * The option will be converted to a          *<code>org.apache.camel.support.jsse.KeyStoreParameters</code> type.          *           * Group: common          */
DECL|method|keyStoreParameters ( String keyStoreParameters)
specifier|default
name|CryptoCmsEndpointBuilder
name|keyStoreParameters
parameter_list|(
name|String
name|keyStoreParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"keyStoreParameters"
argument_list|,
name|keyStoreParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the password of the private keys. It is assumed that all private          * keys in the keystore have the same password. If not set then it is          * assumed that the password of the private keys is given by the          * keystore password given in the KeyStoreParameters.          *           * The option is a:<code>char[]</code> type.          *           * Group: decrypt          */
DECL|method|password (Character[] password)
specifier|default
name|CryptoCmsEndpointBuilder
name|password
parameter_list|(
name|Character
index|[]
name|password
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the password of the private keys. It is assumed that all private          * keys in the keystore have the same password. If not set then it is          * assumed that the password of the private keys is given by the          * keystore password given in the KeyStoreParameters.          *           * The option will be converted to a<code>char[]</code> type.          *           * Group: decrypt          */
DECL|method|password (String password)
specifier|default
name|CryptoCmsEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true then the CMS message is base 64 encoded and must be decoded          * during the processing. Default value is false.          *           * The option is a:<code>boolean</code> type.          *           * Group: decrypt_verify          */
DECL|method|fromBase64 (boolean fromBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|fromBase64
parameter_list|(
name|boolean
name|fromBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fromBase64"
argument_list|,
name|fromBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true then the CMS message is base 64 encoded and must be decoded          * during the processing. Default value is false.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: decrypt_verify          */
DECL|method|fromBase64 (String fromBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|fromBase64
parameter_list|(
name|String
name|fromBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"fromBase64"
argument_list|,
name|fromBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Encryption algorithm, for example DESede/CBC/PKCS5Padding. Further          * possible values: DESede/CBC/PKCS5Padding, AES/CBC/PKCS5Padding,          * Camellia/CBC/PKCS5Padding, CAST5/CBC/PKCS5Padding.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: encrypt          */
DECL|method|contentEncryptionAlgorithm ( String contentEncryptionAlgorithm)
specifier|default
name|CryptoCmsEndpointBuilder
name|contentEncryptionAlgorithm
parameter_list|(
name|String
name|contentEncryptionAlgorithm
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"contentEncryptionAlgorithm"
argument_list|,
name|contentEncryptionAlgorithm
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provider for the originator info. See          * https://tools.ietf.org/html/rfc5652#section-6.1. The default value is          * null.          *           * The option is a:          *<code>org.apache.camel.component.crypto.cms.common.OriginatorInformationProvider</code> type.          *           * Group: encrypt          */
DECL|method|originatorInformationProvider ( Object originatorInformationProvider)
specifier|default
name|CryptoCmsEndpointBuilder
name|originatorInformationProvider
parameter_list|(
name|Object
name|originatorInformationProvider
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"originatorInformationProvider"
argument_list|,
name|originatorInformationProvider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provider for the originator info. See          * https://tools.ietf.org/html/rfc5652#section-6.1. The default value is          * null.          *           * The option will be converted to a          *<code>org.apache.camel.component.crypto.cms.common.OriginatorInformationProvider</code> type.          *           * Group: encrypt          */
DECL|method|originatorInformationProvider ( String originatorInformationProvider)
specifier|default
name|CryptoCmsEndpointBuilder
name|originatorInformationProvider
parameter_list|(
name|String
name|originatorInformationProvider
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"originatorInformationProvider"
argument_list|,
name|originatorInformationProvider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Recipient Info: reference to a bean which implements the interface          * org.apache.camel.component.crypto.cms.api.TransRecipientInfo.          *           * The option is a:          *<code>java.util.List&lt;org.apache.camel.component.crypto.cms.crypt.RecipientInfo&gt;</code> type.          *           * Group: encrypt          */
DECL|method|recipient (List<Object> recipient)
specifier|default
name|CryptoCmsEndpointBuilder
name|recipient
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|recipient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"recipient"
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Recipient Info: reference to a bean which implements the interface          * org.apache.camel.component.crypto.cms.api.TransRecipientInfo.          *           * The option will be converted to a          *<code>java.util.List&lt;org.apache.camel.component.crypto.cms.crypt.RecipientInfo&gt;</code> type.          *           * Group: encrypt          */
DECL|method|recipient (String recipient)
specifier|default
name|CryptoCmsEndpointBuilder
name|recipient
parameter_list|(
name|String
name|recipient
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"recipient"
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Key length for the secret symmetric key used for the content          * encryption. Only used if the specified content-encryption algorithm          * allows keys of different sizes. If          * contentEncryptionAlgorithm=AES/CBC/PKCS5Padding or          * Camellia/CBC/PKCS5Padding then 128; if          * contentEncryptionAlgorithm=DESede/CBC/PKCS5Padding then 192, 128; if          * strong encryption is enabled then for AES/CBC/PKCS5Padding and          * Camellia/CBC/PKCS5Padding also the key lengths 192 and 256 are          * possible.          *           * The option is a:<code>int</code> type.          *           * Group: encrypt          */
DECL|method|secretKeyLength (int secretKeyLength)
specifier|default
name|CryptoCmsEndpointBuilder
name|secretKeyLength
parameter_list|(
name|int
name|secretKeyLength
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"secretKeyLength"
argument_list|,
name|secretKeyLength
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Key length for the secret symmetric key used for the content          * encryption. Only used if the specified content-encryption algorithm          * allows keys of different sizes. If          * contentEncryptionAlgorithm=AES/CBC/PKCS5Padding or          * Camellia/CBC/PKCS5Padding then 128; if          * contentEncryptionAlgorithm=DESede/CBC/PKCS5Padding then 192, 128; if          * strong encryption is enabled then for AES/CBC/PKCS5Padding and          * Camellia/CBC/PKCS5Padding also the key lengths 192 and 256 are          * possible.          *           * The option will be converted to a<code>int</code> type.          *           * Group: encrypt          */
DECL|method|secretKeyLength (String secretKeyLength)
specifier|default
name|CryptoCmsEndpointBuilder
name|secretKeyLength
parameter_list|(
name|String
name|secretKeyLength
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"secretKeyLength"
argument_list|,
name|secretKeyLength
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provider of the generator for the unprotected attributes. The default          * value is null which means no unprotected attribute is added to the          * Enveloped Data object. See          * https://tools.ietf.org/html/rfc5652#section-6.1.          *           * The option is a:          *<code>org.apache.camel.component.crypto.cms.common.AttributesGeneratorProvider</code> type.          *           * Group: encrypt          */
DECL|method|unprotectedAttributesGeneratorProvider ( Object unprotectedAttributesGeneratorProvider)
specifier|default
name|CryptoCmsEndpointBuilder
name|unprotectedAttributesGeneratorProvider
parameter_list|(
name|Object
name|unprotectedAttributesGeneratorProvider
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"unprotectedAttributesGeneratorProvider"
argument_list|,
name|unprotectedAttributesGeneratorProvider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Provider of the generator for the unprotected attributes. The default          * value is null which means no unprotected attribute is added to the          * Enveloped Data object. See          * https://tools.ietf.org/html/rfc5652#section-6.1.          *           * The option will be converted to a          *<code>org.apache.camel.component.crypto.cms.common.AttributesGeneratorProvider</code> type.          *           * Group: encrypt          */
DECL|method|unprotectedAttributesGeneratorProvider ( String unprotectedAttributesGeneratorProvider)
specifier|default
name|CryptoCmsEndpointBuilder
name|unprotectedAttributesGeneratorProvider
parameter_list|(
name|String
name|unprotectedAttributesGeneratorProvider
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"unprotectedAttributesGeneratorProvider"
argument_list|,
name|unprotectedAttributesGeneratorProvider
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the Signed Data or Enveloped Data instance shall be          * base 64 encoded. Default value is false.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: encrypt_sign          */
DECL|method|toBase64 (Boolean toBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|toBase64
parameter_list|(
name|Boolean
name|toBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"toBase64"
argument_list|,
name|toBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the Signed Data or Enveloped Data instance shall be          * base 64 encoded. Default value is false.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: encrypt_sign          */
DECL|method|toBase64 (String toBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|toBase64
parameter_list|(
name|String
name|toBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"toBase64"
argument_list|,
name|toBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the signed content should be included into the          * Signed Data instance. If false then a detached Signed Data instance          * is created in the header CamelCryptoCmsSignedData.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: sign          */
DECL|method|includeContent (Boolean includeContent)
specifier|default
name|CryptoCmsEndpointBuilder
name|includeContent
parameter_list|(
name|Boolean
name|includeContent
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"includeContent"
argument_list|,
name|includeContent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the signed content should be included into the          * Signed Data instance. If false then a detached Signed Data instance          * is created in the header CamelCryptoCmsSignedData.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: sign          */
DECL|method|includeContent (String includeContent)
specifier|default
name|CryptoCmsEndpointBuilder
name|includeContent
parameter_list|(
name|String
name|includeContent
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"includeContent"
argument_list|,
name|includeContent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Signer information: reference to bean(s) which implements          * org.apache.camel.component.crypto.cms.api.SignerInfo. Multiple values          * can be separated by comma.          *           * The option is a:          *<code>java.util.List&lt;org.apache.camel.component.crypto.cms.sig.SignerInfo&gt;</code> type.          *           * Group: sign          */
DECL|method|signer (List<Object> signer)
specifier|default
name|CryptoCmsEndpointBuilder
name|signer
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|signer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"signer"
argument_list|,
name|signer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Signer information: reference to bean(s) which implements          * org.apache.camel.component.crypto.cms.api.SignerInfo. Multiple values          * can be separated by comma.          *           * The option will be converted to a          *<code>java.util.List&lt;org.apache.camel.component.crypto.cms.sig.SignerInfo&gt;</code> type.          *           * Group: sign          */
DECL|method|signer (String signer)
specifier|default
name|CryptoCmsEndpointBuilder
name|signer
parameter_list|(
name|String
name|signer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"signer"
argument_list|,
name|signer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the value in the header CamelCryptoCmsSignedData is          * base64 encoded. Default value is false. Only relevant for detached          * signatures. In the detached signature case, the header contains the          * Signed Data object.          *           * The option is a:<code>boolean</code> type.          *           * Group: verify          */
DECL|method|signedDataHeaderBase64 ( boolean signedDataHeaderBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|signedDataHeaderBase64
parameter_list|(
name|boolean
name|signedDataHeaderBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"signedDataHeaderBase64"
argument_list|,
name|signedDataHeaderBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Indicates whether the value in the header CamelCryptoCmsSignedData is          * base64 encoded. Default value is false. Only relevant for detached          * signatures. In the detached signature case, the header contains the          * Signed Data object.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: verify          */
DECL|method|signedDataHeaderBase64 ( String signedDataHeaderBase64)
specifier|default
name|CryptoCmsEndpointBuilder
name|signedDataHeaderBase64
parameter_list|(
name|String
name|signedDataHeaderBase64
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"signedDataHeaderBase64"
argument_list|,
name|signedDataHeaderBase64
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true then the signatures of all signers contained in the Signed          * Data object are verified. If false then only one signature whose          * signer info matches with one of the specified certificates is          * verified. Default value is true.          *           * The option is a:<code>boolean</code> type.          *           * Group: verify          */
DECL|method|verifySignaturesOfAllSigners ( boolean verifySignaturesOfAllSigners)
specifier|default
name|CryptoCmsEndpointBuilder
name|verifySignaturesOfAllSigners
parameter_list|(
name|boolean
name|verifySignaturesOfAllSigners
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"verifySignaturesOfAllSigners"
argument_list|,
name|verifySignaturesOfAllSigners
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If true then the signatures of all signers contained in the Signed          * Data object are verified. If false then only one signature whose          * signer info matches with one of the specified certificates is          * verified. Default value is true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: verify          */
DECL|method|verifySignaturesOfAllSigners ( String verifySignaturesOfAllSigners)
specifier|default
name|CryptoCmsEndpointBuilder
name|verifySignaturesOfAllSigners
parameter_list|(
name|String
name|verifySignaturesOfAllSigners
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"verifySignaturesOfAllSigners"
argument_list|,
name|verifySignaturesOfAllSigners
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Crypto CMS component.      */
DECL|interface|AdvancedCryptoCmsEndpointBuilder
specifier|public
interface|interface
name|AdvancedCryptoCmsEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|CryptoCmsEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|CryptoCmsEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedCryptoCmsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedCryptoCmsEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedCryptoCmsEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedCryptoCmsEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Crypto CMS (camel-crypto-cms)      * The crypto cms component is used for encrypting data in CMS Enveloped      * Data format, decrypting CMS Enveloped Data, signing data in CMS Signed      * Data format, and verifying CMS Signed Data.      *       * Category: security,transformation      * Available as of version: 2.20      * Maven coordinates: org.apache.camel:camel-crypto-cms      *       * Syntax:<code>crypto-cms:cryptoOperation:name</code>      *       * Path parameter: cryptoOperation (required)      * Set the Crypto operation from that supplied after the crypto scheme in      * the endpoint uri e.g. crypto-cms:sign sets sign as the operation.      * Possible values: sign, verify, encrypt, or decrypt.      * The value can be one of: sign, verify, encrypt, decrypt      *       * Path parameter: name (required)      * The name part in the URI can be chosen by the user to distinguish between      * different signer/verifier/encryptor/decryptor endpoints within the camel      * context.      */
DECL|method|cryptoCms (String path)
specifier|default
name|CryptoCmsEndpointBuilder
name|cryptoCms
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|CryptoCmsEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|CryptoCmsEndpointBuilder
implements|,
name|AdvancedCryptoCmsEndpointBuilder
block|{
specifier|public
name|CryptoCmsEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"crypto-cms"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|CryptoCmsEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

