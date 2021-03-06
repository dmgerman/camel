begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DataInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|DataOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|crypto
operator|.
name|Cipher
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|CipherInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|CipherOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|spec
operator|.
name|IvParameterSpec
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
name|spi
operator|.
name|DataFormatName
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
name|Dataformat
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
name|support
operator|.
name|builder
operator|.
name|OutputStreamBuilder
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
name|service
operator|.
name|ServiceSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|crypto
operator|.
name|Cipher
operator|.
name|DECRYPT_MODE
import|;
end_import

begin_import
import|import static
name|javax
operator|.
name|crypto
operator|.
name|Cipher
operator|.
name|ENCRYPT_MODE
import|;
end_import

begin_comment
comment|/**  *<code>CryptoDataFormat</code> uses a specified key and algorithm to encrypt,  * decrypt and verify exchange payloads. The Data format allows an  * initialization vector to be supplied. The use of this initialization vector  * or IV is different depending on the algorithm type block or streaming, but it  * is desirable to be able to control it. Also in certain cases it may be  * necessary to have access to the IV in the decryption phase and as the IV  * doens't necessarily need to be kept secret it is ok to inline this in the  * stream and read it out on the other side prior to decryption. For more  * information on Initialization vectors see  *<ul>  *<li>http://en.wikipedia.org/wiki/Initialization_vector</li>  *<li>http://www.herongyang.com/Cryptography/</li>  *<li>http://en.wikipedia.org/wiki/Block_cipher_modes_of_operation</li>  *<ul>  *<p/>  * To avoid attacks against the encrypted data while it is in transit the  * {@link CryptoDataFormat} can also calculate a Message Authentication Code for  * the encrypted exchange contents based on a configurable MAC algorithm. The  * calculated HMAC is appended to the stream after encryption. It is separated  * from the stream in the decryption phase. The MAC is recalculated and verified  * against the transmitted version to insure nothing was tampered with in  * transit.For more information on Message Authentication Codes see  *<ul>  *<li>http://en.wikipedia.org/wiki/HMAC</li>  *</ul>  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"crypto"
argument_list|)
DECL|class|CryptoDataFormat
specifier|public
class|class
name|CryptoDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"CamelCryptoKey"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CryptoDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|INIT_VECTOR
specifier|private
specifier|static
specifier|final
name|String
name|INIT_VECTOR
init|=
literal|"CamelCryptoInitVector"
decl_stmt|;
DECL|field|algorithm
specifier|private
name|String
name|algorithm
decl_stmt|;
DECL|field|cryptoProvider
specifier|private
name|String
name|cryptoProvider
decl_stmt|;
DECL|field|configuredkey
specifier|private
name|Key
name|configuredkey
decl_stmt|;
DECL|field|bufferSize
specifier|private
name|int
name|bufferSize
init|=
literal|4096
decl_stmt|;
DECL|field|initializationVector
specifier|private
name|byte
index|[]
name|initializationVector
decl_stmt|;
DECL|field|inline
specifier|private
name|boolean
name|inline
decl_stmt|;
DECL|field|macAlgorithm
specifier|private
name|String
name|macAlgorithm
init|=
literal|"HmacSHA1"
decl_stmt|;
DECL|field|shouldAppendHMAC
specifier|private
name|boolean
name|shouldAppendHMAC
decl_stmt|;
DECL|field|parameterSpec
specifier|private
name|AlgorithmParameterSpec
name|parameterSpec
decl_stmt|;
DECL|method|CryptoDataFormat ()
specifier|public
name|CryptoDataFormat
parameter_list|()
block|{     }
DECL|method|CryptoDataFormat (String algorithm, Key key)
specifier|public
name|CryptoDataFormat
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|Key
name|key
parameter_list|)
block|{
name|this
argument_list|(
name|algorithm
argument_list|,
name|key
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|CryptoDataFormat (String algorithm, Key key, String cryptoProvider)
specifier|public
name|CryptoDataFormat
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|Key
name|key
parameter_list|,
name|String
name|cryptoProvider
parameter_list|)
block|{
name|this
operator|.
name|algorithm
operator|=
name|algorithm
expr_stmt|;
name|this
operator|.
name|configuredkey
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|cryptoProvider
operator|=
name|cryptoProvider
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"crypto"
return|;
block|}
DECL|method|initializeCipher (int mode, Key key, byte[] iv)
specifier|private
name|Cipher
name|initializeCipher
parameter_list|(
name|int
name|mode
parameter_list|,
name|Key
name|key
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
throws|throws
name|Exception
block|{
name|Cipher
name|cipher
init|=
name|cryptoProvider
operator|==
literal|null
condition|?
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
else|:
name|Cipher
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|,
name|cryptoProvider
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
name|IllegalStateException
argument_list|(
literal|"A valid encryption key is required. Either configure the CryptoDataFormat "
operator|+
literal|"with a key or provide one in a header using the header name 'CamelCryptoKey'"
argument_list|)
throw|;
block|}
if|if
condition|(
name|mode
operator|==
name|ENCRYPT_MODE
operator|||
name|mode
operator|==
name|DECRYPT_MODE
condition|)
block|{
if|if
condition|(
name|iv
operator|!=
literal|null
condition|)
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|,
operator|new
name|IvParameterSpec
argument_list|(
name|iv
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|parameterSpec
operator|!=
literal|null
condition|)
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|,
name|parameterSpec
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cipher
operator|.
name|init
argument_list|(
name|mode
argument_list|,
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cipher
return|;
block|}
annotation|@
name|Override
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
name|byte
index|[]
name|iv
init|=
name|getInitializationVector
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|Key
name|key
init|=
name|getKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
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
name|HMACAccumulator
name|hmac
init|=
name|getMessageAuthenticationCode
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|plaintextStream
operator|!=
literal|null
condition|)
block|{
name|inlineInitVector
argument_list|(
name|outputStream
argument_list|,
name|iv
argument_list|)
expr_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|int
name|read
decl_stmt|;
name|CipherOutputStream
name|cipherStream
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cipherStream
operator|=
operator|new
name|CipherOutputStream
argument_list|(
name|outputStream
argument_list|,
name|initializeCipher
argument_list|(
name|ENCRYPT_MODE
argument_list|,
name|key
argument_list|,
name|iv
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|plaintextStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>
literal|0
condition|)
block|{
name|cipherStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
name|cipherStream
operator|.
name|flush
argument_list|()
expr_stmt|;
name|hmac
operator|.
name|encryptUpdate
argument_list|(
name|buffer
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
comment|// only write if there is data to write (IBM JDK throws exception if no data)
name|byte
index|[]
name|mac
init|=
name|hmac
operator|.
name|getCalculatedMac
argument_list|()
decl_stmt|;
if|if
condition|(
name|mac
operator|!=
literal|null
operator|&&
name|mac
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|cipherStream
operator|.
name|write
argument_list|(
name|mac
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|cipherStream
argument_list|,
literal|"cipher"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|plaintextStream
argument_list|,
literal|"plaintext"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|unmarshal (final Exchange exchange, final InputStream encryptedStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|encryptedStream
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|encryptedStream
operator|!=
literal|null
condition|)
block|{
name|byte
index|[]
name|iv
init|=
name|getInlinedInitializationVector
argument_list|(
name|exchange
argument_list|,
name|encryptedStream
argument_list|)
decl_stmt|;
name|Key
name|key
init|=
name|getKey
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|CipherInputStream
name|cipherStream
init|=
literal|null
decl_stmt|;
name|OutputStreamBuilder
name|osb
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cipherStream
operator|=
operator|new
name|CipherInputStream
argument_list|(
name|encryptedStream
argument_list|,
name|initializeCipher
argument_list|(
name|DECRYPT_MODE
argument_list|,
name|key
argument_list|,
name|iv
argument_list|)
argument_list|)
expr_stmt|;
name|osb
operator|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|HMACAccumulator
name|hmac
init|=
name|getMessageAuthenticationCode
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|bufferSize
index|]
decl_stmt|;
name|hmac
operator|.
name|attachStream
argument_list|(
name|osb
argument_list|)
expr_stmt|;
name|int
name|read
decl_stmt|;
while|while
condition|(
operator|(
name|read
operator|=
name|cipherStream
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
operator|)
operator|>=
literal|0
condition|)
block|{
name|hmac
operator|.
name|decryptUpdate
argument_list|(
name|buffer
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
name|hmac
operator|.
name|validate
argument_list|()
expr_stmt|;
return|return
name|osb
operator|.
name|build
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|cipherStream
argument_list|,
literal|"cipher"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|osb
argument_list|,
literal|"plaintext"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|null
return|;
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
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|inlineInitVector (OutputStream outputStream, byte[] iv)
specifier|private
name|void
name|inlineInitVector
parameter_list|(
name|OutputStream
name|outputStream
parameter_list|,
name|byte
index|[]
name|iv
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|inline
condition|)
block|{
if|if
condition|(
name|iv
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Inlining cannot be performed, as no initialization vector was specified"
argument_list|)
throw|;
block|}
name|DataOutputStream
name|dout
init|=
operator|new
name|DataOutputStream
argument_list|(
name|outputStream
argument_list|)
decl_stmt|;
name|dout
operator|.
name|writeInt
argument_list|(
name|iv
operator|.
name|length
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|iv
argument_list|)
expr_stmt|;
name|outputStream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getInlinedInitializationVector (Exchange exchange, InputStream encryptedStream)
specifier|private
name|byte
index|[]
name|getInlinedInitializationVector
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|encryptedStream
parameter_list|)
throws|throws
name|IOException
block|{
name|byte
index|[]
name|iv
init|=
name|getInitializationVector
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|inline
condition|)
block|{
try|try
block|{
name|int
name|ivLength
init|=
operator|new
name|DataInputStream
argument_list|(
name|encryptedStream
argument_list|)
operator|.
name|readInt
argument_list|()
decl_stmt|;
name|iv
operator|=
operator|new
name|byte
index|[
name|ivLength
index|]
expr_stmt|;
name|int
name|read
init|=
name|encryptedStream
operator|.
name|read
argument_list|(
name|iv
argument_list|)
decl_stmt|;
if|if
condition|(
name|read
operator|!=
name|ivLength
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Attempted to read a '%d' byte initialization vector from inputStream but only"
operator|+
literal|" '%d' bytes were retrieved"
argument_list|,
name|ivLength
argument_list|,
name|read
argument_list|)
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Error reading initialization vector from encrypted stream"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
name|iv
return|;
block|}
DECL|method|getMessageAuthenticationCode (Key key)
specifier|private
name|HMACAccumulator
name|getMessageAuthenticationCode
parameter_list|(
name|Key
name|key
parameter_list|)
throws|throws
name|Exception
block|{
comment|// return an actual Hmac Calculator or a 'Null' noop version.
return|return
name|shouldAppendHMAC
condition|?
operator|new
name|HMACAccumulator
argument_list|(
name|key
argument_list|,
name|macAlgorithm
argument_list|,
name|cryptoProvider
argument_list|,
name|bufferSize
argument_list|)
else|:
operator|new
name|HMACAccumulator
argument_list|()
block|{
name|byte
index|[]
name|empty
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
specifier|public
name|void
name|encryptUpdate
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|read
parameter_list|)
block|{             }
specifier|public
name|void
name|decryptUpdate
parameter_list|(
name|byte
index|[]
name|buffer
parameter_list|,
name|int
name|read
parameter_list|)
throws|throws
name|IOException
block|{
name|outputStream
operator|.
name|write
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|read
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|validate
parameter_list|()
block|{             }
specifier|public
name|byte
index|[]
name|getCalculatedMac
parameter_list|()
block|{
return|return
name|empty
return|;
block|}
block|}
return|;
block|}
DECL|method|getInitializationVector (Exchange exchange)
specifier|private
name|byte
index|[]
name|getInitializationVector
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|byte
index|[]
name|iv
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|INIT_VECTOR
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|iv
operator|==
literal|null
condition|)
block|{
name|iv
operator|=
name|initializationVector
expr_stmt|;
block|}
return|return
name|iv
return|;
block|}
DECL|method|getKey (Exchange exchange)
specifier|private
name|Key
name|getKey
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Key
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KEY
argument_list|,
name|Key
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KEY
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|key
operator|=
name|configuredkey
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
DECL|method|setInitializationVector (byte[] initializationVector)
specifier|public
name|void
name|setInitializationVector
parameter_list|(
name|byte
index|[]
name|initializationVector
parameter_list|)
block|{
if|if
condition|(
name|initializationVector
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|initializationVector
operator|=
name|initializationVector
expr_stmt|;
block|}
block|}
comment|/**      * Meant for use with a Symmetric block Cipher and specifies that the      * initialization vector should be written to the cipher stream ahead of the      * encrypted ciphertext. When the payload is to be decrypted this      * initialization vector will need to be read from the stream. Requires that      * the formatter has been configured with an init vector that is valid for      * the given algorithm.      *      * @param inline true if the initialization vector should be inlined in the stream.      */
DECL|method|setShouldInlineInitializationVector (boolean inline)
specifier|public
name|void
name|setShouldInlineInitializationVector
parameter_list|(
name|boolean
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
comment|/**      * Sets the JCE name of the Encryption Algorithm that should be used      */
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
comment|/**      * Sets a custom {@link AlgorithmParameterSpec} that should be used to      * configure the Cipher. Note that if an Initalization vector is provided      * then the IvParameterSpec will be used and any value set here will be      * ignored      */
DECL|method|setAlgorithmParameterSpec (AlgorithmParameterSpec parameterSpec)
specifier|public
name|void
name|setAlgorithmParameterSpec
parameter_list|(
name|AlgorithmParameterSpec
name|parameterSpec
parameter_list|)
block|{
name|this
operator|.
name|parameterSpec
operator|=
name|parameterSpec
expr_stmt|;
block|}
comment|/**      * Sets the name of the JCE provider e.g. SUN or BC for Bouncy      */
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
comment|/**      * Sets the algorithm used to create the Hash-based Message Authentication      * Code (HMAC) appended to the stream.      */
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
comment|/**      * Whether a Hash-based Message Authentication Code (HMAC) should be      * calculated and appended to the stream.      */
DECL|method|setShouldAppendHMAC (boolean shouldAppendHMAC)
specifier|public
name|void
name|setShouldAppendHMAC
parameter_list|(
name|boolean
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
comment|/**      * Set the key that should be used to encrypt or decrypt incoming encrypted exchanges.      */
DECL|method|setKey (Key key)
specifier|public
name|void
name|setKey
parameter_list|(
name|Key
name|key
parameter_list|)
block|{
name|this
operator|.
name|configuredkey
operator|=
name|key
expr_stmt|;
block|}
comment|/**      * Set the size of the buffer used to      */
DECL|method|setBufferSize (int bufferSize)
specifier|public
name|void
name|setBufferSize
parameter_list|(
name|int
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
block|}
end_class

end_unit

