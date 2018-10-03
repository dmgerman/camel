begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spec
operator|.
name|AlgorithmParameterSpec
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
name|DataFormat
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
name|RouteContext
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
name|CamelContextHelper
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

begin_comment
comment|/**  * Crypto data format is used for encrypting and decrypting of messages using Java Cryptographic Extension.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.3.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,security"
argument_list|,
name|title
operator|=
literal|"Crypto (Java Cryptographic Extension)"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"crypto"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CryptoDataFormat
specifier|public
class|class
name|CryptoDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"DES/CBC/PKCS5Padding"
argument_list|)
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|cryptoProvider
specifier|private
name|String
name|cryptoProvider
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|keyRef
specifier|private
name|String
name|keyRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|initVectorRef
specifier|private
name|String
name|initVectorRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|algorithmParameterRef
specifier|private
name|String
name|algorithmParameterRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|buffersize
specifier|private
name|Integer
name|buffersize
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"HmacSHA1"
argument_list|)
DECL|field|macAlgorithm
specifier|private
name|String
name|macAlgorithm
init|=
literal|"HmacSHA1"
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|shouldAppendHMAC
specifier|private
name|Boolean
name|shouldAppendHMAC
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|inline
specifier|private
name|Boolean
name|inline
decl_stmt|;
DECL|method|CryptoDataFormat ()
specifier|public
name|CryptoDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"crypto"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|DataFormat
name|cryptoFormat
init|=
name|super
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|keyRef
argument_list|)
condition|)
block|{
name|Key
name|key
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|keyRef
argument_list|,
name|Key
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|cryptoFormat
argument_list|,
literal|"key"
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|algorithmParameterRef
argument_list|)
condition|)
block|{
name|AlgorithmParameterSpec
name|spec
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|algorithmParameterRef
argument_list|,
name|AlgorithmParameterSpec
operator|.
name|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|cryptoFormat
argument_list|,
literal|"AlgorithmParameterSpec"
argument_list|,
name|spec
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|initVectorRef
argument_list|)
condition|)
block|{
name|byte
index|[]
name|iv
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|initVectorRef
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|cryptoFormat
argument_list|,
literal|"InitializationVector"
argument_list|,
name|iv
argument_list|)
expr_stmt|;
block|}
return|return
name|cryptoFormat
return|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|Boolean
name|answer
init|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|shouldAppendHMAC
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
operator|!
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldAppendHMAC"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldAppendHMAC"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|answer
operator|=
name|ObjectHelper
operator|.
name|toBoolean
argument_list|(
name|inline
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|!=
literal|null
operator|&&
name|answer
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldInlineInitializationVector"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"shouldInlineInitializationVector"
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|algorithm
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"algorithm"
argument_list|,
name|algorithm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cryptoProvider
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"cryptoProvider"
argument_list|,
name|cryptoProvider
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|macAlgorithm
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"macAlgorithm"
argument_list|,
name|macAlgorithm
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|buffersize
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"buffersize"
argument_list|,
name|buffersize
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * The JCE algorithm name indicating the cryptographic algorithm that will be used.      *<p/>      * Is by default DES/CBC/PKCS5Padding.      */
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
DECL|method|getCryptoProvider ()
specifier|public
name|String
name|getCryptoProvider
parameter_list|()
block|{
return|return
name|cryptoProvider
return|;
block|}
comment|/**      * The name of the JCE Security Provider that should be used.      */
DECL|method|setCryptoProvider (String cryptoProvider)
specifier|public
name|void
name|setCryptoProvider
parameter_list|(
name|String
name|cryptoProvider
parameter_list|)
block|{
name|this
operator|.
name|cryptoProvider
operator|=
name|cryptoProvider
expr_stmt|;
block|}
DECL|method|getKeyRef ()
specifier|public
name|String
name|getKeyRef
parameter_list|()
block|{
return|return
name|keyRef
return|;
block|}
comment|/**      * Refers to the secret key to lookup from the register to use.      */
DECL|method|setKeyRef (String keyRef)
specifier|public
name|void
name|setKeyRef
parameter_list|(
name|String
name|keyRef
parameter_list|)
block|{
name|this
operator|.
name|keyRef
operator|=
name|keyRef
expr_stmt|;
block|}
DECL|method|getInitVectorRef ()
specifier|public
name|String
name|getInitVectorRef
parameter_list|()
block|{
return|return
name|initVectorRef
return|;
block|}
comment|/**      * Refers to a byte array containing the Initialization Vector that will be used to initialize the Cipher.      */
DECL|method|setInitVectorRef (String initVectorRef)
specifier|public
name|void
name|setInitVectorRef
parameter_list|(
name|String
name|initVectorRef
parameter_list|)
block|{
name|this
operator|.
name|initVectorRef
operator|=
name|initVectorRef
expr_stmt|;
block|}
DECL|method|getAlgorithmParameterRef ()
specifier|public
name|String
name|getAlgorithmParameterRef
parameter_list|()
block|{
return|return
name|algorithmParameterRef
return|;
block|}
comment|/**      * A JCE AlgorithmParameterSpec used to initialize the Cipher.      *<p/>      * Will lookup the type using the given name as a {@link java.security.spec.AlgorithmParameterSpec} type.      */
DECL|method|setAlgorithmParameterRef (String algorithmParameterRef)
specifier|public
name|void
name|setAlgorithmParameterRef
parameter_list|(
name|String
name|algorithmParameterRef
parameter_list|)
block|{
name|this
operator|.
name|algorithmParameterRef
operator|=
name|algorithmParameterRef
expr_stmt|;
block|}
DECL|method|getBuffersize ()
specifier|public
name|Integer
name|getBuffersize
parameter_list|()
block|{
return|return
name|buffersize
return|;
block|}
comment|/**      * The size of the buffer used in the signature process.      */
DECL|method|setBuffersize (Integer buffersize)
specifier|public
name|void
name|setBuffersize
parameter_list|(
name|Integer
name|buffersize
parameter_list|)
block|{
name|this
operator|.
name|buffersize
operator|=
name|buffersize
expr_stmt|;
block|}
DECL|method|getMacAlgorithm ()
specifier|public
name|String
name|getMacAlgorithm
parameter_list|()
block|{
return|return
name|macAlgorithm
return|;
block|}
comment|/**      * The JCE algorithm name indicating the Message Authentication algorithm.      */
DECL|method|setMacAlgorithm (String macAlgorithm)
specifier|public
name|void
name|setMacAlgorithm
parameter_list|(
name|String
name|macAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|macAlgorithm
operator|=
name|macAlgorithm
expr_stmt|;
block|}
DECL|method|getShouldAppendHMAC ()
specifier|public
name|Boolean
name|getShouldAppendHMAC
parameter_list|()
block|{
return|return
name|shouldAppendHMAC
return|;
block|}
comment|/**      * Flag indicating that a Message Authentication Code should be calculated and appended to the encrypted data.      */
DECL|method|setShouldAppendHMAC (Boolean shouldAppendHMAC)
specifier|public
name|void
name|setShouldAppendHMAC
parameter_list|(
name|Boolean
name|shouldAppendHMAC
parameter_list|)
block|{
name|this
operator|.
name|shouldAppendHMAC
operator|=
name|shouldAppendHMAC
expr_stmt|;
block|}
DECL|method|getInline ()
specifier|public
name|Boolean
name|getInline
parameter_list|()
block|{
return|return
name|inline
return|;
block|}
comment|/**      * Flag indicating that the configured IV should be inlined into the encrypted data stream.      *<p/>      * Is by default false.      */
DECL|method|setInline (Boolean inline)
specifier|public
name|void
name|setInline
parameter_list|(
name|Boolean
name|inline
parameter_list|)
block|{
name|this
operator|.
name|inline
operator|=
name|inline
expr_stmt|;
block|}
block|}
end_class

end_unit

