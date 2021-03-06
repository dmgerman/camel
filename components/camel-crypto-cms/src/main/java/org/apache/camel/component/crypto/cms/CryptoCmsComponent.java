begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.crypto.cms
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Endpoint
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
name|component
operator|.
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|DefaultEnvelopedDataDecryptorConfiguration
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
name|crypt
operator|.
name|EnvelopedDataDecryptor
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
name|crypt
operator|.
name|EnvelopedDataDecryptorConfiguration
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
name|crypt
operator|.
name|EnvelopedDataEncryptor
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
name|crypt
operator|.
name|EnvelopedDataEncryptorConfiguration
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
name|sig
operator|.
name|DefaultSignedDataVerifierConfiguration
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
name|sig
operator|.
name|SignedDataCreator
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
name|sig
operator|.
name|SignedDataCreatorConfiguration
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
name|sig
operator|.
name|SignedDataVerifierConfiguration
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
name|sig
operator|.
name|SignedDataVerifierFromHeader
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_class
annotation|@
name|Component
argument_list|(
literal|"crypto-cms"
argument_list|)
DECL|class|CryptoCmsComponent
specifier|public
class|class
name|CryptoCmsComponent
extends|extends
name|DefaultComponent
block|{
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|signedDataVerifierConfiguration
specifier|private
name|SignedDataVerifierConfiguration
name|signedDataVerifierConfiguration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|envelopedDataDecryptorConfiguration
specifier|private
name|EnvelopedDataDecryptorConfiguration
name|envelopedDataDecryptorConfiguration
decl_stmt|;
DECL|method|CryptoCmsComponent ()
specifier|public
name|CryptoCmsComponent
parameter_list|()
block|{     }
DECL|method|CryptoCmsComponent (CamelContext context)
specifier|public
name|CryptoCmsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
comment|// NOPMD
comment|// called
comment|// method
comment|// setProperties
comment|// throws
comment|// Exception
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
name|String
name|scheme
decl_stmt|;
name|String
name|name
decl_stmt|;
try|try
block|{
name|URI
name|u
init|=
operator|new
name|URI
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
name|scheme
operator|=
name|u
operator|.
name|getScheme
argument_list|()
expr_stmt|;
name|name
operator|=
name|u
operator|.
name|getPath
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MalformedURLException
argument_list|(
comment|// NOPMD -- the stack trace does not
comment|// help in this case.
name|String
operator|.
name|format
argument_list|(
literal|"An invalid crypto-cms uri was provided '%s'."
operator|+
literal|" Check that the uri matches the format crypto-cms:sign://<name>, crypto-cms:verify://<name>, "
operator|+
literal|"crypto-cms:encrypt://<name>, or crypto-cms:decrpyt://<name>"
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|Processor
name|processor
decl_stmt|;
name|CryptoOperation
name|operation
decl_stmt|;
if|if
condition|(
name|CryptoOperation
operator|.
name|sign
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|operation
operator|=
name|CryptoOperation
operator|.
name|sign
expr_stmt|;
name|SignedDataCreatorConfiguration
name|config
init|=
operator|new
name|SignedDataCreatorConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// properties must be set to config before processor is initialized
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|config
operator|.
name|init
argument_list|()
expr_stmt|;
name|processor
operator|=
operator|new
name|SignedDataCreator
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CryptoOperation
operator|.
name|verify
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|operation
operator|=
name|CryptoOperation
operator|.
name|verify
expr_stmt|;
name|SignedDataVerifierConfiguration
name|config
init|=
name|getSignedDataVerifierConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// properties must be set to config before processor is initialized
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|processor
operator|=
operator|new
name|SignedDataVerifierFromHeader
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CryptoOperation
operator|.
name|encrypt
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|operation
operator|=
name|CryptoOperation
operator|.
name|encrypt
expr_stmt|;
name|EnvelopedDataEncryptorConfiguration
name|config
init|=
operator|new
name|EnvelopedDataEncryptorConfiguration
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
decl_stmt|;
comment|// properties must be set to config before processor is initialized
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|config
operator|.
name|init
argument_list|()
expr_stmt|;
name|processor
operator|=
operator|new
name|EnvelopedDataEncryptor
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|CryptoOperation
operator|.
name|decrypt
operator|.
name|name
argument_list|()
operator|.
name|equals
argument_list|(
name|scheme
argument_list|)
condition|)
block|{
name|operation
operator|=
name|CryptoOperation
operator|.
name|decrypt
expr_stmt|;
name|EnvelopedDataDecryptorConfiguration
name|config
init|=
name|getEnvelopedDataDecryptorConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
comment|// properties must be set to config before processor is initialized
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|processor
operator|=
operator|new
name|EnvelopedDataDecryptor
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|error
init|=
literal|"Endpoint uri "
operator|+
name|uri
operator|+
literal|" is wrong configured. Operation "
operator|+
name|scheme
operator|+
literal|" is not supported. Supported operations are: sign, verify, encrypt, decrypt"
decl_stmt|;
name|log
operator|.
name|error
argument_list|(
name|error
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|error
argument_list|)
throw|;
block|}
name|CryptoCmsEndpoint
name|endpoint
init|=
operator|new
name|CryptoCmsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setCryptoOperation
argument_list|(
name|operation
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * To configure the shared SignedDataVerifierConfiguration, which determines      * the uri parameters for the verify operation.      */
DECL|method|setSignedDataVerifierConfiguration (SignedDataVerifierConfiguration signedDataVerifierConfiguration)
specifier|public
name|void
name|setSignedDataVerifierConfiguration
parameter_list|(
name|SignedDataVerifierConfiguration
name|signedDataVerifierConfiguration
parameter_list|)
block|{
name|this
operator|.
name|signedDataVerifierConfiguration
operator|=
name|signedDataVerifierConfiguration
expr_stmt|;
block|}
DECL|method|getSignedDataVerifierConfiguration ()
specifier|public
name|SignedDataVerifierConfiguration
name|getSignedDataVerifierConfiguration
parameter_list|()
block|{
if|if
condition|(
name|signedDataVerifierConfiguration
operator|==
literal|null
condition|)
block|{
name|signedDataVerifierConfiguration
operator|=
operator|new
name|DefaultSignedDataVerifierConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|signedDataVerifierConfiguration
return|;
block|}
DECL|method|getEnvelopedDataDecryptorConfiguration ()
specifier|public
name|EnvelopedDataDecryptorConfiguration
name|getEnvelopedDataDecryptorConfiguration
parameter_list|()
block|{
if|if
condition|(
name|envelopedDataDecryptorConfiguration
operator|==
literal|null
condition|)
block|{
name|envelopedDataDecryptorConfiguration
operator|=
operator|new
name|DefaultEnvelopedDataDecryptorConfiguration
argument_list|()
expr_stmt|;
block|}
return|return
name|envelopedDataDecryptorConfiguration
return|;
block|}
comment|/**      * To configure the shared EnvelopedDataDecryptorConfiguration, which      * determines the uri parameters for the decrypt operation.      */
DECL|method|setEnvelopedDataDecryptorConfiguration (EnvelopedDataDecryptorConfiguration envelopedDataDecryptorConfiguration)
specifier|public
name|void
name|setEnvelopedDataDecryptorConfiguration
parameter_list|(
name|EnvelopedDataDecryptorConfiguration
name|envelopedDataDecryptorConfiguration
parameter_list|)
block|{
name|this
operator|.
name|envelopedDataDecryptorConfiguration
operator|=
name|envelopedDataDecryptorConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// NOPMD
if|if
condition|(
name|Security
operator|.
name|getProvider
argument_list|(
name|BouncyCastleProvider
operator|.
name|PROVIDER_NAME
argument_list|)
operator|==
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding BouncyCastleProvider as security provider"
argument_list|)
expr_stmt|;
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

