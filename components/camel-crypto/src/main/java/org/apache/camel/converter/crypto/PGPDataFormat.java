begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.crypto
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|Security
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
name|util
operator|.
name|ExchangeHelper
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|IOUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|ArmoredOutputStream
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|bcpg
operator|.
name|CompressionAlgorithmTags
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

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPCompressedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPEncryptedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPEncryptedDataGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPEncryptedDataList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPLiteralData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPrivateKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPPublicKeyEncryptedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|openpgp
operator|.
name|PGPUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|util
operator|.
name|io
operator|.
name|Streams
import|;
end_import

begin_comment
comment|/**  *<code>PGPDataFormat</code> uses the<a href="http://www.bouncycastle.org/java.htm">bouncy castle</a>  * libraries to enable encryption and decryption in the PGP format.  */
end_comment

begin_class
DECL|class|PGPDataFormat
specifier|public
class|class
name|PGPDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|keyUserid
specifier|private
name|String
name|keyUserid
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|keyFileName
specifier|private
name|String
name|keyFileName
decl_stmt|;
DECL|field|armored
specifier|private
name|boolean
name|armored
decl_stmt|;
DECL|field|integrity
specifier|private
name|boolean
name|integrity
init|=
literal|true
decl_stmt|;
DECL|method|PGPDataFormat ()
specifier|public
name|PGPDataFormat
parameter_list|()
block|{
if|if
condition|(
name|Security
operator|.
name|getProvider
argument_list|(
literal|"BC"
argument_list|)
operator|==
literal|null
condition|)
block|{
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
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|PGPPublicKey
name|key
init|=
name|PGPDataFormatUtil
operator|.
name|findPublicKey
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|this
operator|.
name|keyFileName
argument_list|,
name|this
operator|.
name|keyUserid
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Public key is null, cannot proceed"
argument_list|)
throw|;
block|}
name|InputStream
name|plaintextStream
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|byte
index|[]
name|compressedData
init|=
name|PGPDataFormatUtil
operator|.
name|compress
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|plaintextStream
argument_list|)
argument_list|,
name|PGPLiteralData
operator|.
name|CONSOLE
argument_list|,
name|CompressionAlgorithmTags
operator|.
name|ZIP
argument_list|)
decl_stmt|;
if|if
condition|(
name|armored
condition|)
block|{
name|outputStream
operator|=
operator|new
name|ArmoredOutputStream
argument_list|(
name|outputStream
argument_list|)
expr_stmt|;
block|}
name|PGPEncryptedDataGenerator
name|encGen
init|=
operator|new
name|PGPEncryptedDataGenerator
argument_list|(
name|PGPEncryptedData
operator|.
name|CAST5
argument_list|,
name|integrity
argument_list|,
operator|new
name|SecureRandom
argument_list|()
argument_list|,
literal|"BC"
argument_list|)
decl_stmt|;
name|encGen
operator|.
name|addMethod
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|OutputStream
name|encOut
init|=
name|encGen
operator|.
name|open
argument_list|(
name|outputStream
argument_list|,
name|compressedData
operator|.
name|length
argument_list|)
decl_stmt|;
try|try
block|{
name|encOut
operator|.
name|write
argument_list|(
name|compressedData
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|encOut
argument_list|,
name|outputStream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream encryptedStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|encryptedStream
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|encryptedStream
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|PGPPrivateKey
name|key
init|=
name|PGPDataFormatUtil
operator|.
name|findPrivateKey
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|keyFileName
argument_list|,
name|keyUserid
argument_list|,
name|password
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Private key is null, cannot proceed"
argument_list|)
throw|;
block|}
name|InputStream
name|in
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|IOUtils
operator|.
name|toByteArray
argument_list|(
name|encryptedStream
argument_list|)
argument_list|)
decl_stmt|;
name|in
operator|=
name|PGPUtil
operator|.
name|getDecoderStream
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|PGPObjectFactory
name|pgpF
init|=
operator|new
name|PGPObjectFactory
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|PGPEncryptedDataList
name|enc
decl_stmt|;
name|Object
name|o
init|=
name|pgpF
operator|.
name|nextObject
argument_list|()
decl_stmt|;
comment|// the first object might be a PGP marker packet.
if|if
condition|(
name|o
operator|instanceof
name|PGPEncryptedDataList
condition|)
block|{
name|enc
operator|=
operator|(
name|PGPEncryptedDataList
operator|)
name|o
expr_stmt|;
block|}
else|else
block|{
name|enc
operator|=
operator|(
name|PGPEncryptedDataList
operator|)
name|pgpF
operator|.
name|nextObject
argument_list|()
expr_stmt|;
block|}
name|PGPPublicKeyEncryptedData
name|pbe
init|=
operator|(
name|PGPPublicKeyEncryptedData
operator|)
name|enc
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|InputStream
name|clear
init|=
name|pbe
operator|.
name|getDataStream
argument_list|(
name|key
argument_list|,
literal|"BC"
argument_list|)
decl_stmt|;
name|PGPObjectFactory
name|pgpFact
init|=
operator|new
name|PGPObjectFactory
argument_list|(
name|clear
argument_list|)
decl_stmt|;
name|PGPCompressedData
name|cData
init|=
operator|(
name|PGPCompressedData
operator|)
name|pgpFact
operator|.
name|nextObject
argument_list|()
decl_stmt|;
name|pgpFact
operator|=
operator|new
name|PGPObjectFactory
argument_list|(
name|cData
operator|.
name|getDataStream
argument_list|()
argument_list|)
expr_stmt|;
name|PGPLiteralData
name|ld
init|=
operator|(
name|PGPLiteralData
operator|)
name|pgpFact
operator|.
name|nextObject
argument_list|()
decl_stmt|;
return|return
name|Streams
operator|.
name|readAll
argument_list|(
name|ld
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets if the encrypted file should be written in ascii visible text      */
DECL|method|setArmored (boolean armored)
specifier|public
name|void
name|setArmored
parameter_list|(
name|boolean
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
DECL|method|getArmored ()
specifier|public
name|boolean
name|getArmored
parameter_list|()
block|{
return|return
name|this
operator|.
name|armored
return|;
block|}
comment|/**      * Whether or not to add a integrity check/sign to the encrypted file      */
DECL|method|setIntegrity (boolean integrity)
specifier|public
name|void
name|setIntegrity
parameter_list|(
name|boolean
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
DECL|method|getIntegrity ()
specifier|public
name|boolean
name|getIntegrity
parameter_list|()
block|{
return|return
name|this
operator|.
name|integrity
return|;
block|}
comment|/**      * Userid of the key used to encrypt/decrypt      */
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
comment|/**      * filename of the keyring that will be used, classpathResource      */
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
comment|/**      * Password used to open the private keyring      */
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
block|}
end_class

end_unit

