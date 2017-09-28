begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|crypto
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
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|DataFormatConfigurationPropertiesCommon
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
comment|/**  * Crypto data format is used for encrypting and decrypting of messages using  * Java Cryptographic Extension.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
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
literal|"camel.dataformat.crypto"
argument_list|)
DECL|class|CryptoDataFormatConfiguration
specifier|public
class|class
name|CryptoDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * The JCE algorithm name indicating the cryptographic algorithm that will      * be used. Is by default DES/CBC/PKCS5Padding.      */
DECL|field|algorithm
specifier|private
name|String
name|algorithm
init|=
literal|"DES/CBC/PKCS5Padding"
decl_stmt|;
comment|/**      * The name of the JCE Security Provider that should be used.      */
DECL|field|cryptoProvider
specifier|private
name|String
name|cryptoProvider
decl_stmt|;
comment|/**      * Refers to the secret key to lookup from the register to use.      */
DECL|field|keyRef
specifier|private
name|String
name|keyRef
decl_stmt|;
comment|/**      * Refers to a byte array containing the Initialization Vector that will be      * used to initialize the Cipher.      */
DECL|field|initVectorRef
specifier|private
name|String
name|initVectorRef
decl_stmt|;
comment|/**      * A JCE AlgorithmParameterSpec used to initialize the Cipher. Will lookup      * the type using the given name as a      * java.security.spec.AlgorithmParameterSpec type.      */
DECL|field|algorithmParameterRef
specifier|private
name|String
name|algorithmParameterRef
decl_stmt|;
comment|/**      * The size of the buffer used in the signature process.      */
DECL|field|buffersize
specifier|private
name|Integer
name|buffersize
decl_stmt|;
comment|/**      * The JCE algorithm name indicating the Message Authentication algorithm.      */
DECL|field|macAlgorithm
specifier|private
name|String
name|macAlgorithm
init|=
literal|"HmacSHA1"
decl_stmt|;
comment|/**      * Flag indicating that a Message Authentication Code should be calculated      * and appended to the encrypted data.      */
DECL|field|shouldAppendHMAC
specifier|private
name|Boolean
name|shouldAppendHMAC
init|=
literal|false
decl_stmt|;
comment|/**      * Flag indicating that the configured IV should be inlined into the      * encrypted data stream. Is by default false.      */
DECL|field|inline
specifier|private
name|Boolean
name|inline
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
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

