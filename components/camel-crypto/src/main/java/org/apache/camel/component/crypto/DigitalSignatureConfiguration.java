begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto
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
name|Signature
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
name|CamelContextAware
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
import|import static
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
name|DigitalSignatureConstants
operator|.
name|SIGNATURE
import|;
end_import

begin_class
DECL|class|DigitalSignatureConfiguration
specifier|public
class|class
name|DigitalSignatureConfiguration
implements|implements
name|Cloneable
implements|,
name|CamelContextAware
block|{
DECL|field|privateKey
specifier|private
name|PrivateKey
name|privateKey
decl_stmt|;
DECL|field|keystore
specifier|private
name|KeyStore
name|keystore
decl_stmt|;
DECL|field|secureRandom
specifier|private
name|SecureRandom
name|secureRandom
decl_stmt|;
DECL|field|algorithm
specifier|private
name|String
name|algorithm
init|=
literal|"DSA"
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|Integer
name|bufferSize
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|2048
argument_list|)
decl_stmt|;
DECL|field|provider
specifier|private
name|String
name|provider
decl_stmt|;
DECL|field|signatureHeaderName
specifier|private
name|String
name|signatureHeaderName
decl_stmt|;
DECL|field|alias
specifier|private
name|String
name|alias
decl_stmt|;
DECL|field|password
specifier|private
name|char
index|[]
name|password
decl_stmt|;
DECL|field|publicKey
specifier|private
name|PublicKey
name|publicKey
decl_stmt|;
DECL|field|certificate
specifier|private
name|Certificate
name|certificate
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
comment|/** references that should be resolved when the context changes */
DECL|field|publicKeyName
specifier|private
name|String
name|publicKeyName
decl_stmt|;
DECL|field|certificateName
specifier|private
name|String
name|certificateName
decl_stmt|;
DECL|field|privateKeyName
specifier|private
name|String
name|privateKeyName
decl_stmt|;
DECL|field|keystoreName
specifier|private
name|String
name|keystoreName
decl_stmt|;
DECL|field|randomName
specifier|private
name|String
name|randomName
decl_stmt|;
DECL|field|clearHeaders
specifier|private
name|boolean
name|clearHeaders
decl_stmt|;
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
DECL|method|copy ()
specifier|public
name|DigitalSignatureConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|DigitalSignatureConfiguration
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
block|}
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
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
name|context
operator|=
name|camelContext
expr_stmt|;
comment|// try to retrieve the references once the context is available.
name|setKeystore
argument_list|(
name|keystoreName
argument_list|)
expr_stmt|;
name|setPublicKey
argument_list|(
name|publicKeyName
argument_list|)
expr_stmt|;
name|setPrivateKey
argument_list|(
name|privateKeyName
argument_list|)
expr_stmt|;
name|setCertificate
argument_list|(
name|certificateName
argument_list|)
expr_stmt|;
name|setSecureRandom
argument_list|(
name|randomName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the JCE name of the Algorithm that should be used for the signer.      */
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
comment|/**      * Sets the JCE name of the Algorithm that should be used for the signer.      */
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
comment|/**      * Gets the alias used to query the KeyStore for keys and {@link java.security.cert.Certificate Certificates}      * to be used in signing and verifying exchanges. This value can be provided at runtime via the message header      * {@link org.apache.camel.component.crypto.DigitalSignatureConstants#KEYSTORE_ALIAS}      */
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
comment|/**      * Sets the alias used to query the KeyStore for keys and {@link java.security.cert.Certificate Certificates}      * to be used in signing and verifying exchanges. This value can be provided at runtime via the message header      * {@link org.apache.camel.component.crypto.DigitalSignatureConstants#KEYSTORE_ALIAS}      */
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
comment|/**      * Get the PrivateKey that should be used to sign the exchange      */
DECL|method|getPrivateKey ()
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getPrivateKey
argument_list|(
name|alias
argument_list|,
name|password
argument_list|)
return|;
block|}
comment|/**      * Get the PrivateKey that should be used to sign the signature in the      * exchange using the supplied alias.      *      * @param alias the alias used to retrieve the Certificate from the keystore.      */
DECL|method|getPrivateKey (String alias)
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|getPrivateKey
argument_list|(
name|alias
argument_list|,
name|password
argument_list|)
return|;
block|}
comment|/**      * Get the PrivateKey that should be used to sign the signature in the      * exchange using the supplied alias.      *      * @param alias the alias used to retrieve the Certificate from the keystore.      */
DECL|method|getPrivateKey (String alias, char[] password)
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|alias
parameter_list|,
name|char
index|[]
name|password
parameter_list|)
throws|throws
name|Exception
block|{
name|PrivateKey
name|pk
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
operator|&&
name|keystore
operator|!=
literal|null
condition|)
block|{
name|pk
operator|=
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|password
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|pk
operator|==
literal|null
condition|)
block|{
name|pk
operator|=
name|privateKey
expr_stmt|;
block|}
return|return
name|pk
return|;
block|}
comment|/**      * Set the PrivateKey that should be used to sign the exchange      *      * @param privateKey the key with with to sign the exchange.      */
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
comment|/**      * Sets the reference name for a PrivateKey that can be fond in the registry.      */
DECL|method|setPrivateKey (String privateKeyName)
specifier|public
name|void
name|setPrivateKey
parameter_list|(
name|String
name|privateKeyName
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|privateKeyName
operator|!=
literal|null
condition|)
block|{
name|PrivateKey
name|pk
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|privateKeyName
argument_list|,
name|PrivateKey
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|pk
operator|!=
literal|null
condition|)
block|{
name|setPrivateKey
argument_list|(
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|privateKeyName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|privateKeyName
operator|=
name|privateKeyName
expr_stmt|;
block|}
block|}
comment|/**      * Set the PublicKey that should be used to verify the signature in the exchange.      */
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
comment|/**      * Sets the reference name for a publicKey that can be fond in the registry.      */
DECL|method|setPublicKey (String publicKeyName)
specifier|public
name|void
name|setPublicKey
parameter_list|(
name|String
name|publicKeyName
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|publicKeyName
operator|!=
literal|null
condition|)
block|{
name|PublicKey
name|pk
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|publicKeyName
argument_list|,
name|PublicKey
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|pk
operator|!=
literal|null
condition|)
block|{
name|setPublicKey
argument_list|(
name|pk
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|publicKeyName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|publicKeyName
operator|=
name|publicKeyName
expr_stmt|;
block|}
block|}
comment|/**      * get the PublicKey that should be used to verify the signature in the exchange.      */
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
comment|/**      * Set the Certificate that should be used to verify the signature in the      * exchange. If a {@link KeyStore} has been configured then this will      * attempt to retrieve the {@link Certificate}from it using hte supplied      * alias. If either the alias or the Keystore is invalid then the configured      * certificate will be returned      *      * @param alias the alias used to retrieve the Certificate from the keystore.      */
DECL|method|getCertificate (String alias)
specifier|public
name|Certificate
name|getCertificate
parameter_list|(
name|String
name|alias
parameter_list|)
throws|throws
name|Exception
block|{
name|Certificate
name|cert
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|alias
operator|!=
literal|null
operator|&&
name|keystore
operator|!=
literal|null
condition|)
block|{
name|cert
operator|=
name|keystore
operator|.
name|getCertificate
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cert
operator|==
literal|null
condition|)
block|{
name|cert
operator|=
name|certificate
expr_stmt|;
block|}
return|return
name|cert
return|;
block|}
comment|/**      * Get the explicitly configured {@link Certificate} that should be used to      * verify the signature in the exchange.      */
DECL|method|getCertificate ()
specifier|public
name|Certificate
name|getCertificate
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|certificate
return|;
block|}
comment|/**      * Set the Certificate that should be used to verify the signature in the      * exchange based on its payload.      */
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
comment|/**      * Sets the reference name for a PrivateKey that can be fond in the registry.      */
DECL|method|setCertificate (String certificateName)
specifier|public
name|void
name|setCertificate
parameter_list|(
name|String
name|certificateName
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|certificateName
operator|!=
literal|null
condition|)
block|{
name|Certificate
name|certificate
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|certificateName
argument_list|,
name|Certificate
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|certificate
operator|!=
literal|null
condition|)
block|{
name|setCertificate
argument_list|(
name|certificate
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|certificateName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|certificateName
operator|=
name|certificateName
expr_stmt|;
block|}
block|}
comment|/**      * Gets the KeyStore that can contain keys and Certficates for use in      * signing and verifying exchanges. A {@link KeyStore} is typically used      * with an alias, either one supplied in the Route definition or dynamically      * via the message header "CamelSignatureKeyStoreAlias". If no alias is      * supplied and there is only a single entry in the Keystore, then this      * single entry will be used.      */
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
comment|/**      * Sets the KeyStore that can contain keys and Certficates for use in      * signing and verifying exchanges. A {@link KeyStore} is typically used      * with an alias, either one supplied in the Route definition or dynamically      * via the message header "CamelSignatureKeyStoreAlias". If no alias is      * supplied and there is only a single entry in the Keystore, then this      * single entry will be used.      */
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
comment|/**      * Sets the reference name for a Keystore that can be fond in the registry.      */
DECL|method|setKeystore (String keystoreName)
specifier|public
name|void
name|setKeystore
parameter_list|(
name|String
name|keystoreName
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|keystoreName
operator|!=
literal|null
condition|)
block|{
name|KeyStore
name|keystore
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|keystoreName
argument_list|,
name|KeyStore
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystore
operator|!=
literal|null
condition|)
block|{
name|setKeystore
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|keystoreName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|keystoreName
operator|=
name|keystoreName
expr_stmt|;
block|}
block|}
comment|/**      * Gets the password used to access an aliased {@link PrivateKey} in the KeyStore.      */
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
comment|/**      * Sets the password used to access an aliased {@link PrivateKey} in the KeyStore.      */
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
comment|/**      * Get the SecureRandom used to initialize the Signature service      */
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
comment|/**      * Sets the reference name for a SecureRandom that can be fond in the registry.      */
DECL|method|setSecureRandom (String randomName)
specifier|public
name|void
name|setSecureRandom
parameter_list|(
name|String
name|randomName
parameter_list|)
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|randomName
operator|!=
literal|null
condition|)
block|{
name|SecureRandom
name|random
init|=
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|randomName
argument_list|,
name|SecureRandom
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|keystore
operator|!=
literal|null
condition|)
block|{
name|setSecureRandom
argument_list|(
name|random
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|randomName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|randomName
operator|=
name|randomName
expr_stmt|;
block|}
block|}
comment|/**      * Set the SecureRandom used to initialize the Signature service      *      * @param secureRandom the random used to init the Signature service      */
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
comment|/**      * Get the size of the buffer used to read in the Exchange payload data.      */
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
comment|/**      * Set the size of the buffer used to read in the Exchange payload data.      */
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
comment|/**      * Get the id of the security provider that provides the configured      * {@link Signature} algorithm.      */
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
comment|/**      * Set the id of the security provider that provides the configured      * {@link Signature} algorithm.      *      * @param provider the id of the security provider      */
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
comment|/**      * Get the name of the message header that should be used to store the      * base64 encoded signature. This defaults to 'CamelDigitalSignature'      */
DECL|method|getSignatureHeader ()
specifier|public
name|String
name|getSignatureHeader
parameter_list|()
block|{
return|return
name|signatureHeaderName
operator|!=
literal|null
condition|?
name|signatureHeaderName
else|:
name|SIGNATURE
return|;
block|}
comment|/**      * Set the name of the message header that should be used to store the      * base64 encoded signature. This defaults to 'CamelDigitalSignature'      */
DECL|method|setSignatureHeader (String signatureHeaderName)
specifier|public
name|void
name|setSignatureHeader
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
comment|/**      * Determines if the Signature specific headers be cleared after signing and      * verification. Defaults to true, and should only be made otherwise at your      * extreme peril as vital private information such as Keys and passwords may      * escape if unset.      *      * @return true if the Signature headers should be unset, false otherwise      */
DECL|method|getClearHeaders ()
specifier|public
name|boolean
name|getClearHeaders
parameter_list|()
block|{
return|return
name|clearHeaders
return|;
block|}
comment|/**      * Determines if the Signature specific headers be cleared after signing and      * verification. Defaults to true, and should only be made otherwise at your      * extreme peril as vital private information such as Keys and passwords may      * escape if unset.      */
DECL|method|setClearHeaders (boolean clearHeaders)
specifier|public
name|void
name|setClearHeaders
parameter_list|(
name|boolean
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
comment|/**      * Set the Crypto operation from that supplied after the crypto scheme in the      * endpoint uri e.g. crypto:sign sets sign as the operation.      *      * @param operation the operation supplied after the crypto scheme      */
DECL|method|setCryptoOperation (String operation)
specifier|public
name|void
name|setCryptoOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
comment|/**      * Gets the Crypto operation that was supplied in the the crypto scheme in the endpoint uri      */
DECL|method|getCryptoOperation ()
specifier|public
name|String
name|getCryptoOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
block|}
end_class

end_unit

