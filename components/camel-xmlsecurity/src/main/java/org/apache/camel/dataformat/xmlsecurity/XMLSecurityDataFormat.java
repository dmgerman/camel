begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.xmlsecurity
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
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
name|InvalidKeyException
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
name|NoSuchAlgorithmException
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
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKeyFactory
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
name|DESedeKeySpec
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
name|SecretKeySpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|traversal
operator|.
name|NodeIterator
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
name|converter
operator|.
name|IOConverter
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
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|EncryptedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|EncryptedKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLCipher
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLEncryptionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|keys
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xpath
operator|.
name|XPathAPI
import|;
end_import

begin_class
DECL|class|XMLSecurityDataFormat
specifier|public
class|class
name|XMLSecurityDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|xmlCipherAlgorithm
specifier|private
name|String
name|xmlCipherAlgorithm
decl_stmt|;
DECL|field|passPhrase
specifier|private
name|byte
index|[]
name|passPhrase
decl_stmt|;
DECL|field|secureTag
specifier|private
name|String
name|secureTag
decl_stmt|;
DECL|field|secureTagContents
specifier|private
name|boolean
name|secureTagContents
decl_stmt|;
DECL|method|XMLSecurityDataFormat ()
specifier|public
name|XMLSecurityDataFormat
parameter_list|()
block|{
name|this
operator|.
name|xmlCipherAlgorithm
operator|=
name|XMLCipher
operator|.
name|TRIPLEDES
expr_stmt|;
comment|// set a default pass phrase as its required
name|this
operator|.
name|passPhrase
operator|=
literal|"Just another 24 Byte key"
operator|.
name|getBytes
argument_list|()
expr_stmt|;
name|this
operator|.
name|secureTag
operator|=
literal|""
expr_stmt|;
name|this
operator|.
name|secureTagContents
operator|=
literal|true
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|Init
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, byte[] passPhrase)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
block|}
DECL|method|XMLSecurityDataFormat (String secureTag, boolean secureTagContents, byte[] passPhrase, String xmlCipherAlgorithm)
specifier|public
name|XMLSecurityDataFormat
parameter_list|(
name|String
name|secureTag
parameter_list|,
name|boolean
name|secureTagContents
parameter_list|,
name|byte
index|[]
name|passPhrase
parameter_list|,
name|String
name|xmlCipherAlgorithm
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|setSecureTag
argument_list|(
name|secureTag
argument_list|)
expr_stmt|;
name|this
operator|.
name|setSecureTagContents
argument_list|(
name|secureTagContents
argument_list|)
expr_stmt|;
name|this
operator|.
name|setPassPhrase
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
name|this
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
expr_stmt|;
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
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
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Retrieve the message body as byte array
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot get the inputstream for XMLSecurityDataFormat mashalling"
argument_list|)
throw|;
block|}
name|Document
name|document
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|Key
name|keyEncryptionkey
decl_stmt|;
name|Key
name|dataEncryptionkey
decl_stmt|;
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equals
argument_list|(
name|XMLCipher
operator|.
name|TRIPLEDES
argument_list|)
condition|)
block|{
name|keyEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"DESede"
argument_list|)
expr_stmt|;
name|dataEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"DESede"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"AES"
argument_list|)
expr_stmt|;
name|dataEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"AES"
argument_list|)
expr_stmt|;
block|}
name|XMLCipher
name|keyCipher
init|=
name|XMLCipher
operator|.
name|getInstance
argument_list|(
name|generateXmlCipherAlgorithmKeyWrap
argument_list|()
argument_list|)
decl_stmt|;
name|keyCipher
operator|.
name|init
argument_list|(
name|XMLCipher
operator|.
name|WRAP_MODE
argument_list|,
name|keyEncryptionkey
argument_list|)
expr_stmt|;
name|XMLCipher
name|xmlCipher
init|=
name|XMLCipher
operator|.
name|getInstance
argument_list|(
name|xmlCipherAlgorithm
argument_list|)
decl_stmt|;
name|xmlCipher
operator|.
name|init
argument_list|(
name|XMLCipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|dataEncryptionkey
argument_list|)
expr_stmt|;
if|if
condition|(
name|secureTag
operator|.
name|equalsIgnoreCase
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|embedKeyInfoInEncryptedData
argument_list|(
name|document
argument_list|,
name|keyCipher
argument_list|,
name|xmlCipher
argument_list|,
name|dataEncryptionkey
argument_list|)
expr_stmt|;
name|document
operator|=
name|xmlCipher
operator|.
name|doFinal
argument_list|(
name|document
argument_list|,
name|document
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NodeIterator
name|iter
init|=
name|XPathAPI
operator|.
name|selectNodeIterator
argument_list|(
name|document
argument_list|,
name|secureTag
argument_list|)
decl_stmt|;
name|Node
name|node
decl_stmt|;
while|while
condition|(
operator|(
name|node
operator|=
name|iter
operator|.
name|nextNode
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|embedKeyInfoInEncryptedData
argument_list|(
name|document
argument_list|,
name|keyCipher
argument_list|,
name|xmlCipher
argument_list|,
name|dataEncryptionkey
argument_list|)
expr_stmt|;
name|Document
name|temp
init|=
name|xmlCipher
operator|.
name|doFinal
argument_list|(
name|document
argument_list|,
operator|(
name|Element
operator|)
name|node
argument_list|,
name|getSecureTagContents
argument_list|()
argument_list|)
decl_stmt|;
name|document
operator|.
name|importNode
argument_list|(
name|temp
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|cloneNode
argument_list|(
literal|true
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|IOConverter
operator|.
name|toInputStrean
argument_list|(
name|source
argument_list|)
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|stream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|ExchangeHelper
operator|.
name|getMandatoryInBody
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Key
name|keyEncryptionkey
decl_stmt|;
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equals
argument_list|(
name|XMLCipher
operator|.
name|TRIPLEDES
argument_list|)
condition|)
block|{
name|keyEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"DESede"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|keyEncryptionkey
operator|=
name|generateEncryptionKey
argument_list|(
literal|"AES"
argument_list|)
expr_stmt|;
block|}
name|XMLCipher
name|xmlCipher
init|=
name|XMLCipher
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|xmlCipher
operator|.
name|init
argument_list|(
name|XMLCipher
operator|.
name|DECRYPT_MODE
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|xmlCipher
operator|.
name|setKEK
argument_list|(
name|keyEncryptionkey
argument_list|)
expr_stmt|;
name|Document
name|encodedDocument
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|Document
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|secureTag
operator|.
name|equalsIgnoreCase
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|encodedDocument
operator|=
name|xmlCipher
operator|.
name|doFinal
argument_list|(
name|encodedDocument
argument_list|,
name|encodedDocument
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|NodeIterator
name|iter
init|=
name|XPathAPI
operator|.
name|selectNodeIterator
argument_list|(
name|encodedDocument
argument_list|,
name|secureTag
argument_list|)
decl_stmt|;
name|Node
name|node
decl_stmt|;
while|while
condition|(
operator|(
name|node
operator|=
name|iter
operator|.
name|nextNode
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|Document
name|temp
init|=
name|xmlCipher
operator|.
name|doFinal
argument_list|(
name|encodedDocument
argument_list|,
operator|(
name|Element
operator|)
name|node
argument_list|,
name|getSecureTagContents
argument_list|()
argument_list|)
decl_stmt|;
name|encodedDocument
operator|.
name|importNode
argument_list|(
name|temp
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|cloneNode
argument_list|(
literal|true
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|encodedDocument
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|IOConverter
operator|.
name|toInputStrean
argument_list|(
name|source
argument_list|)
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|bos
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// Return the decrypted data
return|return
name|bos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
DECL|method|generateEncryptionKey (String algorithm)
specifier|private
name|Key
name|generateEncryptionKey
parameter_list|(
name|String
name|algorithm
parameter_list|)
throws|throws
name|InvalidKeyException
throws|,
name|NoSuchAlgorithmException
throws|,
name|InvalidKeySpecException
block|{
name|DESedeKeySpec
name|keySpec
decl_stmt|;
name|Key
name|secretKey
decl_stmt|;
try|try
block|{
if|if
condition|(
name|algorithm
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"DESede"
argument_list|)
condition|)
block|{
name|keySpec
operator|=
operator|new
name|DESedeKeySpec
argument_list|(
name|passPhrase
argument_list|)
expr_stmt|;
name|SecretKeyFactory
name|keyFactory
init|=
name|SecretKeyFactory
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|secretKey
operator|=
name|keyFactory
operator|.
name|generateSecret
argument_list|(
name|keySpec
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|secretKey
operator|=
operator|new
name|SecretKeySpec
argument_list|(
name|passPhrase
argument_list|,
literal|"AES"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|InvalidKeyException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidKeyException
argument_list|(
literal|"InvalidKeyException due to invalid passPhrase: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|passPhrase
argument_list|)
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|NoSuchAlgorithmException
argument_list|(
literal|"NoSuchAlgorithmException while using XMLCipher.TRIPLEDES algorithm: DESede"
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|InvalidKeySpecException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|InvalidKeySpecException
argument_list|(
literal|"Invalid Key generated while using passPhrase: "
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|passPhrase
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|secretKey
return|;
block|}
DECL|method|embedKeyInfoInEncryptedData (Document document, XMLCipher keyCipher, XMLCipher xmlCipher, Key dataEncryptionkey)
specifier|private
name|void
name|embedKeyInfoInEncryptedData
parameter_list|(
name|Document
name|document
parameter_list|,
name|XMLCipher
name|keyCipher
parameter_list|,
name|XMLCipher
name|xmlCipher
parameter_list|,
name|Key
name|dataEncryptionkey
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|EncryptedKey
name|encryptedKey
init|=
name|keyCipher
operator|.
name|encryptKey
argument_list|(
name|document
argument_list|,
name|dataEncryptionkey
argument_list|)
decl_stmt|;
name|KeyInfo
name|keyInfo
init|=
operator|new
name|KeyInfo
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|keyInfo
operator|.
name|add
argument_list|(
name|encryptedKey
argument_list|)
expr_stmt|;
name|EncryptedData
name|encryptedDataElement
init|=
name|xmlCipher
operator|.
name|getEncryptedData
argument_list|()
decl_stmt|;
name|encryptedDataElement
operator|.
name|setKeyInfo
argument_list|(
name|keyInfo
argument_list|)
expr_stmt|;
block|}
DECL|method|generateXmlCipherAlgorithmKeyWrap ()
specifier|private
name|String
name|generateXmlCipherAlgorithmKeyWrap
parameter_list|()
block|{
name|String
name|algorithmKeyWrap
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equalsIgnoreCase
argument_list|(
name|XMLCipher
operator|.
name|TRIPLEDES
argument_list|)
condition|)
block|{
name|algorithmKeyWrap
operator|=
name|XMLCipher
operator|.
name|TRIPLEDES_KeyWrap
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equalsIgnoreCase
argument_list|(
name|XMLCipher
operator|.
name|AES_128
argument_list|)
condition|)
block|{
name|algorithmKeyWrap
operator|=
name|XMLCipher
operator|.
name|AES_128_KeyWrap
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equalsIgnoreCase
argument_list|(
name|XMLCipher
operator|.
name|AES_192
argument_list|)
condition|)
block|{
name|algorithmKeyWrap
operator|=
name|XMLCipher
operator|.
name|AES_192_KeyWrap
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xmlCipherAlgorithm
operator|.
name|equalsIgnoreCase
argument_list|(
name|XMLCipher
operator|.
name|AES_256
argument_list|)
condition|)
block|{
name|algorithmKeyWrap
operator|=
name|XMLCipher
operator|.
name|AES_256_KeyWrap
expr_stmt|;
block|}
return|return
name|algorithmKeyWrap
return|;
block|}
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
name|byte
index|[]
name|getPassPhrase
parameter_list|()
block|{
return|return
name|passPhrase
return|;
block|}
DECL|method|setPassPhrase (byte[] passPhrase)
specifier|public
name|void
name|setPassPhrase
parameter_list|(
name|byte
index|[]
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
DECL|method|isSecureTagContents ()
specifier|public
name|boolean
name|isSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
return|;
block|}
DECL|method|getSecureTagContents ()
specifier|public
name|boolean
name|getSecureTagContents
parameter_list|()
block|{
return|return
name|secureTagContents
return|;
block|}
DECL|method|setSecureTagContents (boolean secureTagContents)
specifier|public
name|void
name|setSecureTagContents
parameter_list|(
name|boolean
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
block|}
end_class

end_unit

