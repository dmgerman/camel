begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.springboot
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
name|springboot
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
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|SecureRandom
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
name|CamelContext
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
name|CryptoOperation
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
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
name|jsse
operator|.
name|KeyStoreParameters
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
name|NestedConfigurationProperty
import|;
end_import

begin_comment
comment|/**  * The crypto component is used for signing and verifying exchanges using the  * Signature Service of the Java Cryptographic Extension (JCE).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.component.crypto"
argument_list|)
DECL|class|DigitalSignatureComponentConfiguration
specifier|public
class|class
name|DigitalSignatureComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * To use the shared DigitalSignatureConfiguration as configuration      */
DECL|field|configuration
specifier|private
name|DigitalSignatureConfigurationNestedConfiguration
name|configuration
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getConfiguration ()
specifier|public
name|DigitalSignatureConfigurationNestedConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration ( DigitalSignatureConfigurationNestedConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DigitalSignatureConfigurationNestedConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
DECL|class|DigitalSignatureConfigurationNestedConfiguration
specifier|public
specifier|static
class|class
name|DigitalSignatureConfigurationNestedConfiguration
block|{
DECL|field|CAMEL_NESTED_CLASS
specifier|public
specifier|static
specifier|final
name|Class
name|CAMEL_NESTED_CLASS
init|=
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
name|DigitalSignatureConfiguration
operator|.
name|class
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|/**          * The logical name of this operation.          */
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
comment|/**          * Sets the JCE name of the Algorithm that should be used for the          * signer.          */
DECL|field|algorithm
specifier|private
name|String
name|algorithm
init|=
literal|"SHA1WithDSA"
decl_stmt|;
comment|/**          * Sets the alias used to query the KeyStore for keys and          * {@link java.security.cert.Certificate Certificates} to be used in          * signing and verifying exchanges. This value can be provided at          * runtime via the message header          * {@link org.apache.camel.component.crypto.DigitalSignatureConstants#KEYSTORE_ALIAS}          */
DECL|field|alias
specifier|private
name|String
name|alias
decl_stmt|;
comment|/**          * Set the PrivateKey that should be used to sign the exchange          *           * @param privateKey          *            the key with with to sign the exchange.          */
DECL|field|privateKey
specifier|private
name|PrivateKey
name|privateKey
decl_stmt|;
comment|/**          * Sets the reference name for a PrivateKey that can be fond in the          * registry.          */
DECL|field|privateKeyName
specifier|private
name|String
name|privateKeyName
decl_stmt|;
comment|/**          * Set the PublicKey that should be used to verify the signature in the          * exchange.          */
DECL|field|publicKey
specifier|private
name|PublicKey
name|publicKey
decl_stmt|;
comment|/**          * Sets the reference name for a publicKey that can be fond in the          * registry.          */
DECL|field|publicKeyName
specifier|private
name|String
name|publicKeyName
decl_stmt|;
comment|/**          * Set the Certificate that should be used to verify the signature in          * the exchange based on its payload.          */
DECL|field|certificate
specifier|private
name|Certificate
name|certificate
decl_stmt|;
comment|/**          * Sets the reference name for a PrivateKey that can be fond in the          * registry.          */
DECL|field|certificateName
specifier|private
name|String
name|certificateName
decl_stmt|;
comment|/**          * Sets the KeyStore that can contain keys and Certficates for use in          * signing and verifying exchanges. A {@link KeyStore} is typically used          * with an alias, either one supplied in the Route definition or          * dynamically via the message header "CamelSignatureKeyStoreAlias". If          * no alias is supplied and there is only a single entry in the          * Keystore, then this single entry will be used.          */
DECL|field|keystore
specifier|private
name|KeyStore
name|keystore
decl_stmt|;
comment|/**          * Sets the reference name for a Keystore that can be fond in the          * registry.          */
DECL|field|keystoreName
specifier|private
name|String
name|keystoreName
decl_stmt|;
comment|/**          * Sets the password used to access an aliased {@link PrivateKey} in the          * KeyStore.          */
DECL|field|password
specifier|private
name|char
index|[]
name|password
decl_stmt|;
comment|/**          * Sets the KeyStore that can contain keys and Certficates for use in          * signing and verifying exchanges based on the given          * KeyStoreParameters. A {@link KeyStore} is typically used with an          * alias, either one supplied in the Route definition or dynamically via          * the message header "CamelSignatureKeyStoreAlias". If no alias is          * supplied and there is only a single entry in the Keystore, then this          * single entry will be used.          */
annotation|@
name|NestedConfigurationProperty
DECL|field|keyStoreParameters
specifier|private
name|KeyStoreParameters
name|keyStoreParameters
decl_stmt|;
comment|/**          * Set the SecureRandom used to initialize the Signature service          *           * @param secureRandom          *            the random used to init the Signature service          */
DECL|field|secureRandom
specifier|private
name|SecureRandom
name|secureRandom
decl_stmt|;
comment|/**          * Sets the reference name for a SecureRandom that can be fond in the          * registry.          */
DECL|field|secureRandomName
specifier|private
name|String
name|secureRandomName
decl_stmt|;
comment|/**          * Set the size of the buffer used to read in the Exchange payload data.          */
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
init|=
literal|2048
decl_stmt|;
comment|/**          * Set the id of the security provider that provides the configured          * {@link Signature} algorithm.          *           * @param provider          *            the id of the security provider          */
DECL|field|provider
specifier|private
name|String
name|provider
decl_stmt|;
comment|/**          * Set the name of the message header that should be used to store the          * base64 encoded signature. This defaults to 'CamelDigitalSignature'          */
DECL|field|signatureHeaderName
specifier|private
name|String
name|signatureHeaderName
decl_stmt|;
comment|/**          * Determines if the Signature specific headers be cleared after signing          * and verification. Defaults to true, and should only be made otherwise          * at your extreme peril as vital private information such as Keys and          * passwords may escape if unset.          */
DECL|field|clearHeaders
specifier|private
name|Boolean
name|clearHeaders
init|=
literal|true
decl_stmt|;
DECL|field|cryptoOperation
specifier|private
name|CryptoOperation
name|cryptoOperation
decl_stmt|;
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
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
DECL|method|getAlias ()
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|alias
return|;
block|}
DECL|method|setAlias (String alias)
specifier|public
name|void
name|setAlias
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|this
operator|.
name|alias
operator|=
name|alias
expr_stmt|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|()
block|{
return|return
name|privateKey
return|;
block|}
DECL|method|setPrivateKey (PrivateKey privateKey)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|PrivateKey
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
DECL|method|getPrivateKeyName ()
specifier|public
name|String
name|getPrivateKeyName
parameter_list|()
block|{
return|return
name|privateKeyName
return|;
block|}
DECL|method|setPrivateKeyName (String privateKeyName)
specifier|public
name|void
name|setPrivateKeyName
parameter_list|(
name|String
name|privateKeyName
parameter_list|)
block|{
name|this
operator|.
name|privateKeyName
operator|=
name|privateKeyName
expr_stmt|;
block|}
DECL|method|getPublicKey ()
specifier|public
name|PublicKey
name|getPublicKey
parameter_list|()
block|{
return|return
name|publicKey
return|;
block|}
DECL|method|setPublicKey (PublicKey publicKey)
specifier|public
name|void
name|setPublicKey
parameter_list|(
name|PublicKey
name|publicKey
parameter_list|)
block|{
name|this
operator|.
name|publicKey
operator|=
name|publicKey
expr_stmt|;
block|}
DECL|method|getPublicKeyName ()
specifier|public
name|String
name|getPublicKeyName
parameter_list|()
block|{
return|return
name|publicKeyName
return|;
block|}
DECL|method|setPublicKeyName (String publicKeyName)
specifier|public
name|void
name|setPublicKeyName
parameter_list|(
name|String
name|publicKeyName
parameter_list|)
block|{
name|this
operator|.
name|publicKeyName
operator|=
name|publicKeyName
expr_stmt|;
block|}
DECL|method|getCertificate ()
specifier|public
name|Certificate
name|getCertificate
parameter_list|()
block|{
return|return
name|certificate
return|;
block|}
DECL|method|setCertificate (Certificate certificate)
specifier|public
name|void
name|setCertificate
parameter_list|(
name|Certificate
name|certificate
parameter_list|)
block|{
name|this
operator|.
name|certificate
operator|=
name|certificate
expr_stmt|;
block|}
DECL|method|getCertificateName ()
specifier|public
name|String
name|getCertificateName
parameter_list|()
block|{
return|return
name|certificateName
return|;
block|}
DECL|method|setCertificateName (String certificateName)
specifier|public
name|void
name|setCertificateName
parameter_list|(
name|String
name|certificateName
parameter_list|)
block|{
name|this
operator|.
name|certificateName
operator|=
name|certificateName
expr_stmt|;
block|}
DECL|method|getKeystore ()
specifier|public
name|KeyStore
name|getKeystore
parameter_list|()
block|{
return|return
name|keystore
return|;
block|}
DECL|method|setKeystore (KeyStore keystore)
specifier|public
name|void
name|setKeystore
parameter_list|(
name|KeyStore
name|keystore
parameter_list|)
block|{
name|this
operator|.
name|keystore
operator|=
name|keystore
expr_stmt|;
block|}
DECL|method|getKeystoreName ()
specifier|public
name|String
name|getKeystoreName
parameter_list|()
block|{
return|return
name|keystoreName
return|;
block|}
DECL|method|setKeystoreName (String keystoreName)
specifier|public
name|void
name|setKeystoreName
parameter_list|(
name|String
name|keystoreName
parameter_list|)
block|{
name|this
operator|.
name|keystoreName
operator|=
name|keystoreName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|char
index|[]
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
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
DECL|method|getKeyStoreParameters ()
specifier|public
name|KeyStoreParameters
name|getKeyStoreParameters
parameter_list|()
block|{
return|return
name|keyStoreParameters
return|;
block|}
DECL|method|setKeyStoreParameters (KeyStoreParameters keyStoreParameters)
specifier|public
name|void
name|setKeyStoreParameters
parameter_list|(
name|KeyStoreParameters
name|keyStoreParameters
parameter_list|)
block|{
name|this
operator|.
name|keyStoreParameters
operator|=
name|keyStoreParameters
expr_stmt|;
block|}
DECL|method|getSecureRandom ()
specifier|public
name|SecureRandom
name|getSecureRandom
parameter_list|()
block|{
return|return
name|secureRandom
return|;
block|}
DECL|method|setSecureRandom (SecureRandom secureRandom)
specifier|public
name|void
name|setSecureRandom
parameter_list|(
name|SecureRandom
name|secureRandom
parameter_list|)
block|{
name|this
operator|.
name|secureRandom
operator|=
name|secureRandom
expr_stmt|;
block|}
DECL|method|getSecureRandomName ()
specifier|public
name|String
name|getSecureRandomName
parameter_list|()
block|{
return|return
name|secureRandomName
return|;
block|}
DECL|method|setSecureRandomName (String secureRandomName)
specifier|public
name|void
name|setSecureRandomName
parameter_list|(
name|String
name|secureRandomName
parameter_list|)
block|{
name|this
operator|.
name|secureRandomName
operator|=
name|secureRandomName
expr_stmt|;
block|}
DECL|method|getBufferSize ()
specifier|public
name|Integer
name|getBufferSize
parameter_list|()
block|{
return|return
name|bufferSize
return|;
block|}
DECL|method|setBufferSize (Integer bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|Integer
name|bufferSize
parameter_list|)
block|{
name|this
operator|.
name|bufferSize
operator|=
name|bufferSize
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
DECL|method|getSignatureHeaderName ()
specifier|public
name|String
name|getSignatureHeaderName
parameter_list|()
block|{
return|return
name|signatureHeaderName
return|;
block|}
DECL|method|setSignatureHeaderName (String signatureHeaderName)
specifier|public
name|void
name|setSignatureHeaderName
parameter_list|(
name|String
name|signatureHeaderName
parameter_list|)
block|{
name|this
operator|.
name|signatureHeaderName
operator|=
name|signatureHeaderName
expr_stmt|;
block|}
DECL|method|getClearHeaders ()
specifier|public
name|Boolean
name|getClearHeaders
parameter_list|()
block|{
return|return
name|clearHeaders
return|;
block|}
DECL|method|setClearHeaders (Boolean clearHeaders)
specifier|public
name|void
name|setClearHeaders
parameter_list|(
name|Boolean
name|clearHeaders
parameter_list|)
block|{
name|this
operator|.
name|clearHeaders
operator|=
name|clearHeaders
expr_stmt|;
block|}
DECL|method|getCryptoOperation ()
specifier|public
name|CryptoOperation
name|getCryptoOperation
parameter_list|()
block|{
return|return
name|cryptoOperation
return|;
block|}
DECL|method|setCryptoOperation (CryptoOperation cryptoOperation)
specifier|public
name|void
name|setCryptoOperation
parameter_list|(
name|CryptoOperation
name|cryptoOperation
parameter_list|)
block|{
name|this
operator|.
name|cryptoOperation
operator|=
name|cryptoOperation
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

