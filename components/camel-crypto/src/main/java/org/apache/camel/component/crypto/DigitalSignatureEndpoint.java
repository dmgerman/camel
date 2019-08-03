begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|processor
operator|.
name|SigningProcessor
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
name|processor
operator|.
name|VerifyingProcessor
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
name|UriEndpoint
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The crypto component is used for signing and verifying exchanges using the Signature Service of the Java Cryptographic Extension (JCE).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|scheme
operator|=
literal|"crypto"
argument_list|,
name|title
operator|=
literal|"Crypto (JCE)"
argument_list|,
name|syntax
operator|=
literal|"crypto:cryptoOperation:name"
argument_list|,
name|producerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"security,transformation"
argument_list|)
DECL|class|DigitalSignatureEndpoint
specifier|public
class|class
name|DigitalSignatureEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|DigitalSignatureConfiguration
name|configuration
decl_stmt|;
DECL|method|DigitalSignatureEndpoint (String uri, DigitalSignatureComponent component, DigitalSignatureConfiguration configuration)
specifier|public
name|DigitalSignatureEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|DigitalSignatureComponent
name|component
parameter_list|,
name|DigitalSignatureConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|CryptoOperation
operator|.
name|sign
operator|==
name|configuration
operator|.
name|getCryptoOperation
argument_list|()
condition|)
block|{
return|return
operator|new
name|DigitalSignatureProducer
argument_list|(
name|this
argument_list|,
operator|new
name|SigningProcessor
argument_list|(
name|configuration
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DigitalSignatureProducer
argument_list|(
name|this
argument_list|,
operator|new
name|VerifyingProcessor
argument_list|(
name|configuration
argument_list|)
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Digital Signatures endpoints are not meant to be consumed from. They are meant be used as an intermediate endpoints"
argument_list|)
throw|;
block|}
comment|/**      * Sets the configuration to use      */
DECL|method|setConfiguration (DigitalSignatureConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|DigitalSignatureConfiguration
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
DECL|method|getConfiguration ()
specifier|public
name|DigitalSignatureConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getPublicKey ()
specifier|public
name|PublicKey
name|getPublicKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPublicKey
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
block|}
DECL|method|setPublicKey (String publicKeyName)
specifier|public
name|void
name|setPublicKey
parameter_list|(
name|String
name|publicKeyName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPublicKeyName
argument_list|(
name|publicKeyName
argument_list|)
expr_stmt|;
block|}
DECL|method|getCertificate ()
specifier|public
name|Certificate
name|getCertificate
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getCertificate
argument_list|()
return|;
block|}
DECL|method|getPrivateKey ()
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getPrivateKey
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setPrivateKey
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeystore ()
specifier|public
name|KeyStore
name|getKeystore
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getKeystore
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setKeystore
argument_list|(
name|keystore
argument_list|)
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
name|getConfiguration
argument_list|()
operator|.
name|getPassword
argument_list|()
return|;
block|}
DECL|method|setKeyPassword (char[] keyPassword)
specifier|public
name|void
name|setKeyPassword
parameter_list|(
name|char
index|[]
name|keyPassword
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPassword
argument_list|(
name|keyPassword
argument_list|)
expr_stmt|;
block|}
DECL|method|getSecureRandom ()
specifier|public
name|SecureRandom
name|getSecureRandom
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getSecureRandom
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setSecureRandom
argument_list|(
name|secureRandom
argument_list|)
expr_stmt|;
block|}
DECL|method|getAlgorithm ()
specifier|public
name|String
name|getAlgorithm
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setAlgorithm
argument_list|(
name|algorithm
argument_list|)
expr_stmt|;
block|}
DECL|method|getBuffersize ()
specifier|public
name|Integer
name|getBuffersize
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getBufferSize
argument_list|()
return|;
block|}
DECL|method|setBuffersize (Integer buffersize)
specifier|public
name|void
name|setBuffersize
parameter_list|(
name|Integer
name|buffersize
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setBufferSize
argument_list|(
name|buffersize
argument_list|)
expr_stmt|;
block|}
DECL|method|getProvider ()
specifier|public
name|String
name|getProvider
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getProvider
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
block|}
DECL|method|getSignatureHeader ()
specifier|public
name|String
name|getSignatureHeader
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getSignatureHeaderName
argument_list|()
return|;
block|}
DECL|method|setSignatureHeader (String signatureHeaderName)
specifier|public
name|void
name|setSignatureHeader
parameter_list|(
name|String
name|signatureHeaderName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSignatureHeaderName
argument_list|(
name|signatureHeaderName
argument_list|)
expr_stmt|;
block|}
DECL|method|getAlias ()
specifier|public
name|String
name|getAlias
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getAlias
argument_list|()
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
name|getConfiguration
argument_list|()
operator|.
name|setAlias
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
DECL|method|isClearHeaders ()
specifier|public
name|boolean
name|isClearHeaders
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isClearHeaders
argument_list|()
return|;
block|}
DECL|method|setClearHeaders (boolean clearHeaders)
specifier|public
name|void
name|setClearHeaders
parameter_list|(
name|boolean
name|clearHeaders
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setClearHeaders
argument_list|(
name|clearHeaders
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

