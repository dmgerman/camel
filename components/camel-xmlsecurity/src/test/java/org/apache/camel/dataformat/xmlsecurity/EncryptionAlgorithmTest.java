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
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Provider
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
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * Test all available encryption algorithms  */
end_comment

begin_class
DECL|class|EncryptionAlgorithmTest
specifier|public
class|class
name|EncryptionAlgorithmTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|xmlsecTestHelper
name|TestHelper
name|xmlsecTestHelper
init|=
operator|new
name|TestHelper
argument_list|()
decl_stmt|;
DECL|method|EncryptionAlgorithmTest ()
specifier|public
name|EncryptionAlgorithmTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// BouncyCastle is required for some algorithms
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
name|Constructor
argument_list|<
name|?
argument_list|>
name|cons
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|c
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"org.bouncycastle.jce.provider.BouncyCastleProvider"
argument_list|)
decl_stmt|;
name|cons
operator|=
name|c
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{}
argument_list|)
expr_stmt|;
name|Provider
name|provider
init|=
operator|(
name|java
operator|.
name|security
operator|.
name|Provider
operator|)
name|cons
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Security
operator|.
name|insertProviderAt
argument_list|(
name|provider
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|context
operator|.
name|getGlobalOptions
argument_list|()
operator|.
name|put
argument_list|(
name|XmlConverter
operator|.
name|OUTPUT_PROPERTIES_PREFIX
operator|+
name|OutputKeys
operator|.
name|ENCODING
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES128 ()
specifier|public
name|void
name|testAES128
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_128
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES128GCM ()
specifier|public
name|void
name|testAES128GCM
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_128_GCM
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES192 ()
specifier|public
name|void
name|testAES192
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|192
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_192
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES192GCM ()
specifier|public
name|void
name|testAES192GCM
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|192
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_192_GCM
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES256 ()
specifier|public
name|void
name|testAES256
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_256
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAES256GCM ()
specifier|public
name|void
name|testAES256GCM
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"AES"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_256_GCM
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTRIPLEDES ()
specifier|public
name|void
name|testTRIPLEDES
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"DESede"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|192
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|TRIPLEDES
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSEED128 ()
specifier|public
name|void
name|testSEED128
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"SEED"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|SEED_128
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCAMELLIA128 ()
specifier|public
name|void
name|testCAMELLIA128
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"CAMELLIA"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|CAMELLIA_128
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCAMELLIA192 ()
specifier|public
name|void
name|testCAMELLIA192
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"CAMELLIA"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|192
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|CAMELLIA_192
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCAMELLIA256 ()
specifier|public
name|void
name|testCAMELLIA256
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestHelper
operator|.
name|UNRESTRICTED_POLICIES_INSTALLED
condition|)
block|{
return|return;
block|}
comment|// Set up the Key
name|KeyGenerator
name|keygen
init|=
name|KeyGenerator
operator|.
name|getInstance
argument_list|(
literal|"CAMELLIA"
argument_list|)
decl_stmt|;
name|keygen
operator|.
name|init
argument_list|(
literal|256
argument_list|)
expr_stmt|;
name|SecretKey
name|key
init|=
name|keygen
operator|.
name|generateKey
argument_list|()
decl_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|xmlEncDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|xmlEncDataFormat
operator|.
name|setPassPhrase
argument_list|(
name|key
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|xmlEncDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|CAMELLIA_256
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|xmlEncDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRSAOAEPKW ()
specifier|public
name|void
name|testRSAOAEPKW
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|XMLSecurityDataFormat
name|sendingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_128
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_OAEP
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|tsParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|tsParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|tsParameters
operator|.
name|setResource
argument_list|(
literal|"sender.ts"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|tsParameters
argument_list|)
expr_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|receivingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|receivingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_OAEP
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|ksParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|ksParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|ksParameters
operator|.
name|setResource
argument_list|(
literal|"recipient.ks"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|ksParameters
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|sendingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|receivingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRSAv15KW ()
specifier|public
name|void
name|testRSAv15KW
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|XMLSecurityDataFormat
name|sendingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_128
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_v1dot5
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|tsParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|tsParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|tsParameters
operator|.
name|setResource
argument_list|(
literal|"sender.ts"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|tsParameters
argument_list|)
expr_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|receivingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|receivingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_v1dot5
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|ksParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|ksParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|ksParameters
operator|.
name|setResource
argument_list|(
literal|"recipient.ks"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|ksParameters
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|sendingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|receivingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRSAOAEP11KW ()
specifier|public
name|void
name|testRSAOAEP11KW
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|XMLSecurityDataFormat
name|sendingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTagContents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setXmlCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|AES_128
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_OAEP_11
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|tsParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|tsParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|tsParameters
operator|.
name|setResource
argument_list|(
literal|"sender.ts"
argument_list|)
expr_stmt|;
name|sendingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|tsParameters
argument_list|)
expr_stmt|;
specifier|final
name|XMLSecurityDataFormat
name|receivingDataFormat
init|=
operator|new
name|XMLSecurityDataFormat
argument_list|()
decl_stmt|;
name|receivingDataFormat
operator|.
name|setKeyCipherAlgorithm
argument_list|(
name|XMLCipher
operator|.
name|RSA_OAEP_11
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setRecipientKeyAlias
argument_list|(
literal|"recipient"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setSecureTag
argument_list|(
literal|"//cheesesites/italy/cheese"
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|ksParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|ksParameters
operator|.
name|setPassword
argument_list|(
literal|"password"
argument_list|)
expr_stmt|;
name|ksParameters
operator|.
name|setResource
argument_list|(
literal|"recipient.ks"
argument_list|)
expr_stmt|;
name|receivingDataFormat
operator|.
name|setKeyOrTrustStoreParameters
argument_list|(
name|ksParameters
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|sendingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:encrypted"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Body: + ${body}"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|receivingDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:decrypted"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|xmlsecTestHelper
operator|.
name|testDecryption
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

