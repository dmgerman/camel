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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPairGenerator
import|;
end_import

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
name|NoSuchAlgorithmException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|Message
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
name|ProducerTemplate
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
name|impl
operator|.
name|DefaultCamelContext
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
name|KEYSTORE_ALIAS
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
name|SIGNATURE_PRIVATE_KEY
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
name|SIGNATURE_PUBLIC_KEY_OR_CERT
import|;
end_import

begin_class
DECL|class|SignatureTest
specifier|public
class|class
name|SignatureTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|keyPair
specifier|private
name|KeyPair
name|keyPair
decl_stmt|;
DECL|field|dsaKeyPair
specifier|private
name|KeyPair
name|dsaKeyPair
decl_stmt|;
DECL|field|payload
specifier|private
name|String
name|payload
init|=
literal|"Dear Alice, Rest assured it's me, signed Bob"
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"someRandom"
argument_list|)
DECL|field|random
specifier|private
name|SecureRandom
name|random
init|=
operator|new
name|SecureRandom
argument_list|()
decl_stmt|;
annotation|@
name|Override
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
name|from
argument_list|(
literal|"direct:keypair"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:basic?privateKey=#myPrivateKey"
argument_list|,
literal|"crypto:verify:basic?publicKey=#myPublicKey"
argument_list|,
literal|"mock:result"
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
comment|// START SNIPPET: algorithm
name|keyPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|PrivateKey
name|privateKey
init|=
name|keyPair
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|PublicKey
name|publicKey
init|=
name|keyPair
operator|.
name|getPublic
argument_list|()
decl_stmt|;
comment|// we can set the keys explicitly on the endpoint instances.
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:sign:rsa?algorithm=MD5withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPrivateKey
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:verify:rsa?algorithm=MD5withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:algorithm"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:rsa?algorithm=MD5withRSA"
argument_list|,
literal|"crypto:verify:rsa?algorithm=MD5withRSA"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: algorithm
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
comment|// START SNIPPET: rsa-sha1
name|keyPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|PrivateKey
name|privateKey
init|=
name|keyPair
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|PublicKey
name|publicKey
init|=
name|keyPair
operator|.
name|getPublic
argument_list|()
decl_stmt|;
comment|// we can set the keys explicitly on the endpoint instances.
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:sign:rsa?algorithm=SHA1withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPrivateKey
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:verify:rsa?algorithm=SHA1withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:rsa-sha1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:rsa?algorithm=SHA1withRSA"
argument_list|,
literal|"crypto:verify:rsa?algorithm=SHA1withRSA"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: rsa-sha1
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
comment|// START SNIPPET: rsa-sha256
name|keyPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|PrivateKey
name|privateKey
init|=
name|keyPair
operator|.
name|getPrivate
argument_list|()
decl_stmt|;
name|PublicKey
name|publicKey
init|=
name|keyPair
operator|.
name|getPublic
argument_list|()
decl_stmt|;
comment|// we can set the keys explicitly on the endpoint instances.
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:sign:rsa?algorithm=SHA256withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPrivateKey
argument_list|(
name|privateKey
argument_list|)
expr_stmt|;
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"crypto:verify:rsa?algorithm=SHA256withRSA"
argument_list|,
name|DigitalSignatureEndpoint
operator|.
name|class
argument_list|)
operator|.
name|setPublicKey
argument_list|(
name|publicKey
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:rsa-sha256"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:rsa?algorithm=SHA256withRSA"
argument_list|,
literal|"crypto:verify:rsa?algorithm=SHA256withRSA"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: rsa-sha256
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
comment|// START SNIPPET: buffersize
name|from
argument_list|(
literal|"direct:buffersize"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:buffer?privateKey=#myPrivateKey&buffersize=1024"
argument_list|,
literal|"crypto:verify:buffer?publicKey=#myPublicKey&buffersize=1024"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: buffersize
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
comment|// START SNIPPET: provider
name|from
argument_list|(
literal|"direct:provider"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:provider?algorithm=SHA1withDSA&privateKey=#myDSAPrivateKey&provider=SUN"
argument_list|,
literal|"crypto:verify:provider?algorithm=SHA1withDSA&publicKey=#myDSAPublicKey&provider=SUN"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: provider
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
comment|// START SNIPPET: certificate
name|from
argument_list|(
literal|"direct:certificate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:withcert?privateKey=#myPrivateKey"
argument_list|,
literal|"crypto:verify:withcert?certificate=#myCert"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: certificate
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
comment|// START SNIPPET: keystore
name|from
argument_list|(
literal|"direct:keystore"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:keystore?keystore=#keystore&alias=bob&password=letmein"
argument_list|,
literal|"crypto:verify:keystore?keystore=#keystore&alias=bob"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: keystore
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
comment|// START SNIPPET: keystore
name|from
argument_list|(
literal|"direct:keystoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:keyStoreParameters?keyStoreParameters=#signatureParams&alias=bob&password=letmein"
argument_list|,
literal|"crypto:verify:keyStoreParameters?keyStoreParameters=#signatureParams&alias=bob"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: keystore
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
comment|// START SNIPPET: signature-header
name|from
argument_list|(
literal|"direct:signature-header"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:another?privateKey=#myPrivateKey&signatureHeader=AnotherDigitalSignature"
argument_list|,
literal|"crypto:verify:another?publicKey=#myPublicKey&signatureHeader=AnotherDigitalSignature"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: signature-header
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
comment|// START SNIPPET: random
name|from
argument_list|(
literal|"direct:random"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:another?privateKey=#myPrivateKey&secureRandom=#someRandom"
argument_list|,
literal|"crypto:verify:another?publicKey=#myPublicKey&secureRandom=#someRandom"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: random
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
comment|// START SNIPPET: alias
name|from
argument_list|(
literal|"direct:alias-sign"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:alias?keystore=#keystore"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:alias-verify"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:verify:alias?keystore=#keystore"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: alias
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
comment|// START SNIPPET: headerkey
name|from
argument_list|(
literal|"direct:headerkey-sign"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:alias"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:headerkey-verify"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:verify:alias"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: headerkey
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
comment|// START SNIPPET: clearheaders
name|from
argument_list|(
literal|"direct:headers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto:sign:headers?privateKey=#myPrivateKey"
argument_list|,
literal|"crypto:verify:headers?publicKey=#myPublicKey&clearHeaders=false"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: clearheaders
block|}
block|}
block|}
end_class

begin_empty_stmt
empty_stmt|;
end_empty_stmt

begin_function
unit|}      @
name|Test
DECL|method|testBasicSignatureRoute ()
specifier|public
name|void
name|testBasicSignatureRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:keypair"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|Exchange
name|e
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|result
init|=
name|e
operator|==
literal|null
condition|?
literal|null
else|:
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
name|result
operator|.
name|getHeader
argument_list|(
name|DigitalSignatureConstants
operator|.
name|SIGNATURE
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetAlgorithmInRouteDefinition ()
specifier|public
name|void
name|testSetAlgorithmInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:algorithm"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testRSASHA1 ()
specifier|public
name|void
name|testRSASHA1
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:rsa-sha1"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testRSASHA256 ()
specifier|public
name|void
name|testRSASHA256
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:rsa-sha256"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetBufferInRouteDefinition ()
specifier|public
name|void
name|testSetBufferInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:buffersize"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetRandomInRouteDefinition ()
specifier|public
name|void
name|testSetRandomInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:random"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetProviderInRouteDefinition ()
specifier|public
name|void
name|testSetProviderInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isJavaVendor
argument_list|(
literal|"ibm"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// can only be run on SUN JDK
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:provider"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetCertificateInRouteDefinition ()
specifier|public
name|void
name|testSetCertificateInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:certificate"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetKeystoreInRouteDefinition ()
specifier|public
name|void
name|testSetKeystoreInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:keystore"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSetKeystoreParametersInRouteDefinition ()
specifier|public
name|void
name|testSetKeystoreParametersInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:keystoreParameters"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testSignatureHeaderInRouteDefinition ()
specifier|public
name|void
name|testSignatureHeaderInRouteDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|Exchange
name|signed
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:signature-header"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:signature-header"
argument_list|,
name|signed
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"AnotherDigitalSignature"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testProvideAliasInHeader ()
specifier|public
name|void
name|testProvideAliasInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
comment|// START SNIPPET: alias-send
name|Exchange
name|unsigned
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:alias-sign"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DigitalSignatureConstants
operator|.
name|KEYSTORE_ALIAS
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|DigitalSignatureConstants
operator|.
name|KEYSTORE_PASSWORD
argument_list|,
literal|"letmein"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:alias-sign"
argument_list|,
name|unsigned
argument_list|)
expr_stmt|;
name|Exchange
name|signed
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:alias-sign"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|unsigned
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|KEYSTORE_ALIAS
argument_list|,
literal|"bob"
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:alias-verify"
argument_list|,
name|signed
argument_list|)
expr_stmt|;
comment|// START SNIPPET: alias-send
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testProvideKeysInHeader ()
specifier|public
name|void
name|testProvideKeysInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|Exchange
name|unsigned
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:headerkey-sign"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
comment|// create a keypair
name|KeyPair
name|pair
init|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
decl_stmt|;
comment|// sign with the private key
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SIGNATURE_PRIVATE_KEY
argument_list|,
name|pair
operator|.
name|getPrivate
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:headerkey-sign"
argument_list|,
name|unsigned
argument_list|)
expr_stmt|;
comment|// verify with the public key
name|Exchange
name|signed
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:alias-sign"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|unsigned
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SIGNATURE_PUBLIC_KEY_OR_CERT
argument_list|,
name|pair
operator|.
name|getPublic
argument_list|()
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:headerkey-verify"
argument_list|,
name|signed
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testProvideCertificateInHeader ()
specifier|public
name|void
name|testProvideCertificateInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|Exchange
name|unsigned
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:signature-property"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|payload
argument_list|)
expr_stmt|;
comment|// create a keypair
name|KeyStore
name|keystore
init|=
name|loadKeystore
argument_list|()
decl_stmt|;
name|Certificate
name|certificate
init|=
name|keystore
operator|.
name|getCertificate
argument_list|(
literal|"bob"
argument_list|)
decl_stmt|;
name|PrivateKey
name|pk
init|=
operator|(
name|PrivateKey
operator|)
name|keystore
operator|.
name|getKey
argument_list|(
literal|"bob"
argument_list|,
literal|"letmein"
operator|.
name|toCharArray
argument_list|()
argument_list|)
decl_stmt|;
comment|// sign with the private key
name|unsigned
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SIGNATURE_PRIVATE_KEY
argument_list|,
name|pk
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:headerkey-sign"
argument_list|,
name|unsigned
argument_list|)
expr_stmt|;
comment|// verify with the public key
name|Exchange
name|signed
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"direct:alias-sign"
argument_list|)
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|unsigned
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
name|signed
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SIGNATURE_PUBLIC_KEY_OR_CERT
argument_list|,
name|certificate
argument_list|)
expr_stmt|;
name|template
operator|.
name|send
argument_list|(
literal|"direct:headerkey-verify"
argument_list|,
name|signed
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
DECL|method|testVerifyHeadersNotCleared ()
specifier|public
name|void
name|testVerifyHeadersNotCleared
parameter_list|()
throws|throws
name|Exception
block|{
name|setupMock
argument_list|()
expr_stmt|;
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:headers"
argument_list|,
name|payload
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|Exchange
name|e
init|=
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|Message
name|result
init|=
name|e
operator|==
literal|null
condition|?
literal|null
else|:
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getHeader
argument_list|(
name|DigitalSignatureConstants
operator|.
name|SIGNATURE
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|setupMock ()
specifier|private
name|MockEndpoint
name|setupMock
parameter_list|()
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|payload
argument_list|)
expr_stmt|;
return|return
name|mock
return|;
block|}
end_function

begin_function
DECL|method|doTestSignatureRoute (RouteBuilder builder)
specifier|public
name|Exchange
name|doTestSignatureRoute
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|doSignatureRouteTest
argument_list|(
name|builder
argument_list|,
literal|null
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
return|;
block|}
end_function

begin_function
DECL|method|doSignatureRouteTest (RouteBuilder builder, Exchange e, Map<String, Object> headers)
specifier|public
name|Exchange
name|doSignatureRouteTest
parameter_list|(
name|RouteBuilder
name|builder
parameter_list|,
name|Exchange
name|e
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
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
try|try
block|{
name|context
operator|.
name|addRoutes
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"mock:result"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|!=
literal|null
condition|)
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:in"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:in"
argument_list|,
name|payload
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
return|return
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
finally|finally
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_function

begin_function
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
name|setUpKeys
argument_list|()
expr_stmt|;
name|disableJMX
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
end_function

begin_function
DECL|method|setUpKeys ()
specifier|public
name|void
name|setUpKeys
parameter_list|()
throws|throws
name|Exception
block|{
name|keyPair
operator|=
name|getKeyPair
argument_list|(
literal|"RSA"
argument_list|)
expr_stmt|;
name|dsaKeyPair
operator|=
name|getKeyPair
argument_list|(
literal|"DSA"
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
DECL|method|getKeyPair (String algorithm)
specifier|public
name|KeyPair
name|getKeyPair
parameter_list|(
name|String
name|algorithm
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
block|{
name|KeyPairGenerator
name|keyGen
init|=
name|KeyPairGenerator
operator|.
name|getInstance
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
name|keyGen
operator|.
name|initialize
argument_list|(
literal|512
argument_list|,
operator|new
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|keyGen
operator|.
name|generateKeyPair
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"keystore"
argument_list|)
DECL|method|loadKeystore ()
specifier|public
specifier|static
name|KeyStore
name|loadKeystore
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|SignatureTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/ks.keystore"
argument_list|)
decl_stmt|;
name|keystore
operator|.
name|load
argument_list|(
name|in
argument_list|,
literal|"letmein"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|keystore
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"myCert"
argument_list|)
DECL|method|getCertificateFromKeyStore ()
specifier|public
name|Certificate
name|getCertificateFromKeyStore
parameter_list|()
throws|throws
name|Exception
block|{
name|Certificate
name|c
init|=
name|loadKeystore
argument_list|()
operator|.
name|getCertificate
argument_list|(
literal|"bob"
argument_list|)
decl_stmt|;
return|return
name|c
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"myPublicKey"
argument_list|)
DECL|method|getPublicKey ()
specifier|public
name|PublicKey
name|getPublicKey
parameter_list|()
throws|throws
name|Exception
block|{
name|Certificate
name|c
init|=
name|loadKeystore
argument_list|()
operator|.
name|getCertificate
argument_list|(
literal|"bob"
argument_list|)
decl_stmt|;
return|return
name|c
operator|.
name|getPublicKey
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"myDSAPublicKey"
argument_list|)
DECL|method|getDSAPublicKey ()
specifier|public
name|PublicKey
name|getDSAPublicKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|dsaKeyPair
operator|.
name|getPublic
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"myPrivateKey"
argument_list|)
DECL|method|getKeyFromKeystore ()
specifier|public
name|PrivateKey
name|getKeyFromKeystore
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|(
name|PrivateKey
operator|)
name|loadKeystore
argument_list|()
operator|.
name|getKey
argument_list|(
literal|"bob"
argument_list|,
literal|"letmein"
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"myDSAPrivateKey"
argument_list|)
DECL|method|getDSAPrivateKey ()
specifier|public
name|PrivateKey
name|getDSAPrivateKey
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|dsaKeyPair
operator|.
name|getPrivate
argument_list|()
return|;
block|}
end_function

begin_function
annotation|@
name|BindToRegistry
argument_list|(
literal|"signatureParams"
argument_list|)
DECL|method|getParams ()
specifier|public
name|KeyStoreParameters
name|getParams
parameter_list|()
block|{
name|KeyStoreParameters
name|keystoreParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|keystoreParameters
operator|.
name|setPassword
argument_list|(
literal|"letmein"
argument_list|)
expr_stmt|;
name|keystoreParameters
operator|.
name|setResource
argument_list|(
literal|"./ks.keystore"
argument_list|)
expr_stmt|;
return|return
name|keystoreParameters
return|;
block|}
end_function

unit|}
end_unit

