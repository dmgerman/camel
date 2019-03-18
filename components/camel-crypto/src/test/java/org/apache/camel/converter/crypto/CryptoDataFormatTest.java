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
name|ByteArrayInputStream
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
name|SecureRandom
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|KeyGenerator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|SecretKey
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CryptoDataFormatTest
specifier|public
class|class
name|CryptoDataFormatTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testBasicSymmetric ()
specifier|public
name|void
name|testBasicSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:basic-encryption"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSymmetricWithInitVector ()
specifier|public
name|void
name|testSymmetricWithInitVector
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:init-vector"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSymmetricWithInlineInitVector ()
specifier|public
name|void
name|testSymmetricWithInlineInitVector
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:inline"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSymmetricWithHMAC ()
specifier|public
name|void
name|testSymmetricWithHMAC
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:hmac"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSymmetricWithMD5HMAC ()
specifier|public
name|void
name|testSymmetricWithMD5HMAC
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:hmac-algorithm"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSymmetricWithSHA256HMAC ()
specifier|public
name|void
name|testSymmetricWithSHA256HMAC
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:hmac-sha-256-algorithm"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testKeySuppliedAsHeader ()
specifier|public
name|void
name|testKeySuppliedAsHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|Key
name|key
init|=
name|generator
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|Exchange
name|unecrypted
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:key-in-header-encrypt"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|unecrypted
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hi Alice, Be careful Eve is listening, signed Bob"
argument_list|)
expr_stmt|;
name|unecrypted
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|unecrypted
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:key-in-header-encrypt"
argument_list|,
name|unecrypted
argument_list|)
expr_stmt|;
name|validateHeaderIsCleared
argument_list|(
name|unecrypted
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|setupExpectations
argument_list|(
name|context
argument_list|,
literal|1
argument_list|,
literal|"mock:unencrypted"
argument_list|)
decl_stmt|;
name|Exchange
name|encrypted
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:key-in-header-decrypt"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|encrypted
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|unecrypted
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|encrypted
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|,
name|key
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:key-in-header-decrypt"
argument_list|,
name|encrypted
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|received
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|validateHeaderIsCleared
argument_list|(
name|received
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test3DESECBSymmetric ()
specifier|public
name|void
name|test3DESECBSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:3des-ecb-encryption"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test3DESCBCSymmetric ()
specifier|public
name|void
name|test3DESCBCSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:3des-cbc-encryption"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES128ECBSymmetric ()
specifier|public
name|void
name|testAES128ECBSymmetric
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|checkUnrestrictedPoliciesInstalled
argument_list|()
condition|)
block|{
name|doRoundTripEncryptionTests
argument_list|(
literal|"direct:aes-128-ecb-encryption"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|validateHeaderIsCleared (Exchange ex)
specifier|private
name|void
name|validateHeaderIsCleared
parameter_list|(
name|Exchange
name|ex
parameter_list|)
block|{
name|Object
name|header
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
operator|!
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|)
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|header
argument_list|)
operator|||
name|header
operator|==
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|doRoundTripEncryptionTests (String endpointUri)
specifier|private
name|void
name|doRoundTripEncryptionTests
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|doRoundTripEncryptionTests
argument_list|(
name|endpointUri
argument_list|,
name|Collections
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|doRoundTripEncryptionTests (String endpoint, Map<String, Object> headers)
specifier|private
name|void
name|doRoundTripEncryptionTests
parameter_list|(
name|String
name|endpoint
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|Exception
block|{
name|MockEndpoint
name|encrypted
init|=
name|setupExpectations
argument_list|(
name|context
argument_list|,
literal|3
argument_list|,
literal|"mock:encrypted"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|unencrypted
init|=
name|setupExpectations
argument_list|(
name|context
argument_list|,
literal|3
argument_list|,
literal|"mock:unencrypted"
argument_list|)
decl_stmt|;
name|String
name|payload
init|=
literal|"Hi Alice, Be careful Eve is listening, signed Bob"
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|endpoint
argument_list|,
name|payload
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|endpoint
argument_list|,
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|endpoint
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|payload
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertMocksSatisfied
argument_list|(
name|encrypted
argument_list|,
name|unencrypted
argument_list|,
name|payload
argument_list|)
expr_stmt|;
block|}
DECL|method|assertMocksSatisfied (MockEndpoint encrypted, MockEndpoint unencrypted, String payload)
specifier|private
name|void
name|assertMocksSatisfied
parameter_list|(
name|MockEndpoint
name|encrypted
parameter_list|,
name|MockEndpoint
name|unencrypted
parameter_list|,
name|String
name|payload
parameter_list|)
throws|throws
name|Exception
block|{
name|awaitAndAssert
argument_list|(
name|unencrypted
argument_list|)
expr_stmt|;
name|awaitAndAssert
argument_list|(
name|encrypted
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|e
range|:
name|unencrypted
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|assertEquals
argument_list|(
name|payload
argument_list|,
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Exchange
name|e
range|:
name|encrypted
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|byte
index|[]
name|ciphertext
init|=
name|e
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|assertNotSame
argument_list|(
name|payload
argument_list|,
operator|new
name|String
argument_list|(
name|ciphertext
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createRouteBuilders ()
specifier|protected
name|RouteBuilder
index|[]
name|createRouteBuilders
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: basic
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:basic-encryption"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: basic
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: init-vector
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|initializationVector
init|=
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|cryptoFormat
operator|.
name|setInitializationVector
argument_list|(
name|initializationVector
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:init-vector"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: init-vector
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: inline-init-vector
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|initializationVector
init|=
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key
init|=
name|generator
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|cryptoFormat
operator|.
name|setInitializationVector
argument_list|(
name|initializationVector
argument_list|)
expr_stmt|;
name|cryptoFormat
operator|.
name|setShouldInlineInitializationVector
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CryptoDataFormat
name|decryptFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|decryptFormat
operator|.
name|setShouldInlineInitializationVector
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:inline"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|decryptFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: inline-init-vector
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: hmac
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|cryptoFormat
operator|.
name|setShouldAppendHMAC
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hmac"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: hmac
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: hmac-algorithm
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|cryptoFormat
operator|.
name|setShouldAppendHMAC
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cryptoFormat
operator|.
name|setMacAlgorithm
argument_list|(
literal|"HmacMD5"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hmac-algorithm"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: hmac-algorithm
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: hmac-sha256-algorithm
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|cryptoFormat
operator|.
name|setShouldAppendHMAC
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|cryptoFormat
operator|.
name|setMacAlgorithm
argument_list|(
literal|"HmacSHA256"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hmac-sha-256-algorithm"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: hmac-sha256-algorithm
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: key-in-header
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|/**                  * Note: the header containing the key should be cleared after                  * marshalling to stop it from leaking by accident and                  * potentially being compromised. The processor version below is                  * arguably better as the key is left in the header when you use                  * the DSL leaks the fact that camel encryption was used.                  */
name|from
argument_list|(
literal|"direct:key-in-header-encrypt"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|removeHeader
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:key-in-header-decrypt"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|remove
argument_list|(
name|CryptoDataFormat
operator|.
name|KEY
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: key-in-header
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: 3DES-ECB
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DESede"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DESede/ECB/PKCS5Padding"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:3des-ecb-encryption"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: 3DES-ECB
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: 3DES-CBC
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DES"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|iv
init|=
operator|new
name|byte
index|[
literal|8
index|]
decl_stmt|;
name|SecureRandom
name|random
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
name|random
operator|.
name|nextBytes
argument_list|(
name|iv
argument_list|)
expr_stmt|;
name|Key
name|key
init|=
name|generator
operator|.
name|generateKey
argument_list|()
decl_stmt|;
name|CryptoDataFormat
name|encCryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|encCryptoFormat
operator|.
name|setInitializationVector
argument_list|(
name|iv
argument_list|)
expr_stmt|;
name|encCryptoFormat
operator|.
name|setShouldInlineInitializationVector
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|CryptoDataFormat
name|decCryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"DES/CBC/PKCS5Padding"
argument_list|,
name|key
argument_list|)
decl_stmt|;
name|decCryptoFormat
operator|.
name|setShouldInlineInitializationVector
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:3des-cbc-encryption"
argument_list|)
operator|.
name|marshal
argument_list|(
name|encCryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|decCryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: 3DES-CBC
block|}
block|}
operator|,
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: AES-128-ECB
name|KeyGenerator
name|generator
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|CryptoDataFormat
name|cryptoFormat
init|=
operator|new
name|CryptoDataFormat
argument_list|(
literal|"AES/ECB/PKCS5Padding"
argument_list|,
name|generator
operator|.
name|generateKey
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:aes-128-ecb-encryption"
argument_list|)
operator|.
name|marshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|cryptoFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unencrypted"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: AES-128-ECB
block|}
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_function
unit|}      private
DECL|method|awaitAndAssert (MockEndpoint mock)
name|void
name|awaitAndAssert
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|setupExpectations (CamelContext context, int expected, String mock)
specifier|public
name|MockEndpoint
name|setupExpectations
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|int
name|expected
parameter_list|,
name|String
name|mock
parameter_list|)
block|{
name|MockEndpoint
name|mockEp
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|mock
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mockEp
operator|.
name|expectedMessageCount
argument_list|(
name|expected
argument_list|)
expr_stmt|;
return|return
name|mockEp
return|;
block|}
end_function

begin_function
DECL|method|checkUnrestrictedPoliciesInstalled ()
specifier|public
specifier|static
name|boolean
name|checkUnrestrictedPoliciesInstalled
parameter_list|()
block|{
try|try
block|{
name|byte
index|[]
name|data
init|=
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|}
decl_stmt|;
name|SecretKey
name|key192
init|=
operator|new
name|SecretKeySpec
argument_list|(
operator|new
name|byte
index|[]
block|{
literal|0x00
block|,
literal|0x01
block|,
literal|0x02
block|,
literal|0x03
block|,
literal|0x04
block|,
literal|0x05
block|,
literal|0x06
block|,
literal|0x07
block|,
literal|0x08
block|,
literal|0x09
block|,
literal|0x0a
block|,
literal|0x0b
block|,
literal|0x0c
block|,
literal|0x0d
block|,
literal|0x0e
block|,
literal|0x0f
block|,
literal|0x10
block|,
literal|0x11
block|,
literal|0x12
block|,
literal|0x13
block|,
literal|0x14
block|,
literal|0x15
block|,
literal|0x16
block|,
literal|0x17
block|}
argument_list|,
literal|"AES"
argument_list|)
decl_stmt|;
name|Cipher
name|c
init|=
name|Cipher
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|c
operator|.
name|init
argument_list|(
name|Cipher
operator|.
name|ENCRYPT_MODE
argument_list|,
name|key192
argument_list|)
expr_stmt|;
name|c
operator|.
name|doFinal
argument_list|(
name|data
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//
block|}
return|return
literal|false
return|;
block|}
end_function

unit|}
end_unit

