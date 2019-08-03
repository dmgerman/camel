begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|InputStream
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|KeySelector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|URIDereferencer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|keyinfo
operator|.
name|KeyInfoFactory
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
name|component
operator|.
name|xmlsecurity
operator|.
name|api
operator|.
name|KeyAccessor
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
name|xmlsecurity
operator|.
name|util
operator|.
name|SameDocumentUriDereferencer
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
name|Registry
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
name|SimpleRegistry
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
name|test
operator|.
name|junit4
operator|.
name|TestSupport
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
comment|/**  * Test for the ECDSA algorithms  */
end_comment

begin_class
DECL|class|ECDSASignatureTest
specifier|public
class|class
name|ECDSASignatureTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|payload
specifier|private
specifier|static
name|String
name|payload
decl_stmt|;
DECL|field|canTest
specifier|private
name|boolean
name|canTest
init|=
literal|true
decl_stmt|;
static|static
block|{
name|boolean
name|includeNewLine
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|TestSupport
operator|.
name|getJavaMajorVersion
argument_list|()
operator|>=
literal|9
condition|)
block|{
name|includeNewLine
operator|=
literal|false
expr_stmt|;
block|}
name|payload
operator|=
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
operator|+
operator|(
name|includeNewLine
condition|?
literal|"\n"
else|:
literal|""
operator|)
operator|+
literal|"<root xmlns=\"http://test/test\"><test>Test Message</test></root>"
expr_stmt|;
block|}
DECL|method|ECDSASignatureTest ()
specifier|public
name|ECDSASignatureTest
parameter_list|()
throws|throws
name|Exception
block|{
try|try
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
comment|// This test fails with the IBM JDK
if|if
condition|(
name|isJavaVendor
argument_list|(
literal|"IBM"
argument_list|)
condition|)
block|{
name|canTest
operator|=
literal|false
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|canTest
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|Registry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
comment|// This test fails with the IBM JDK
if|if
condition|(
name|canTest
condition|)
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"accessor"
argument_list|,
name|getKeyAccessor
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"selector"
argument_list|,
name|KeySelector
operator|.
name|singletonKeySelector
argument_list|(
name|getCertificateFromKeyStore
argument_list|()
operator|.
name|getPublicKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"uriDereferencer"
argument_list|,
name|getSameDocumentUriDereferencer
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|registry
return|;
block|}
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
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return
operator|new
name|RouteBuilder
index|[]
block|{}
return|;
block|}
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_sha1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_sha1?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha1"
argument_list|)
comment|// .log("Body: + ${body}")
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_sha224"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_sha224?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha224"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_sha256"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_sha256?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha256"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_sha384"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_sha384?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha384"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_sha512"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_sha512?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2001/04/xmldsig-more#ecdsa-sha512"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
comment|// START SNIPPET: ecdsa signature algorithm
name|from
argument_list|(
literal|"direct:ecdsa_ripemd160"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:sign:ecdsa_ripemd160?keyAccessor=#accessor"
operator|+
literal|"&signatureAlgorithm=http://www.w3.org/2007/05/xmldsig-more#ecdsa-ripemd160"
argument_list|)
operator|.
name|to
argument_list|(
literal|"xmlsecurity:verify:ecdsa?keySelector=#selector"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ecdsa signature algorithm
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
DECL|method|testECDSASHA1 ()
specifier|public
name|void
name|testECDSASHA1
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_sha1"
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
DECL|method|testECDSASHA224 ()
specifier|public
name|void
name|testECDSASHA224
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_sha224"
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
DECL|method|testECDSASHA256 ()
specifier|public
name|void
name|testECDSASHA256
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_sha256"
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
DECL|method|testECDSASHA384 ()
specifier|public
name|void
name|testECDSASHA384
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_sha384"
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
DECL|method|testECDSASHA512 ()
specifier|public
name|void
name|testECDSASHA512
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_sha512"
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
DECL|method|testECDSARIPEMD160 ()
specifier|public
name|void
name|testECDSARIPEMD160
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
condition|)
block|{
return|return;
block|}
name|setupMock
argument_list|()
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:ecdsa_ripemd160"
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
DECL|method|setupMock ()
specifier|private
name|MockEndpoint
name|setupMock
parameter_list|()
block|{
return|return
name|setupMock
argument_list|(
name|payload
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|setupMock (String payload)
specifier|private
name|MockEndpoint
name|setupMock
parameter_list|(
name|String
name|payload
parameter_list|)
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
name|disableJMX
argument_list|()
expr_stmt|;
try|try
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot test due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|canTest
operator|=
literal|false
expr_stmt|;
block|}
block|}
end_function

begin_function
DECL|method|loadKeystore ()
specifier|private
specifier|static
name|KeyStore
name|loadKeystore
parameter_list|()
throws|throws
name|Exception
block|{
name|KeyStore
name|keyStore
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
name|ECDSASignatureTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
literal|"/org/apache/camel/component/xmlsecurity/ecdsa.jks"
argument_list|)
decl_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|in
argument_list|,
literal|"security"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|keyStore
return|;
block|}
end_function

begin_function
DECL|method|getCertificateFromKeyStore ()
specifier|private
specifier|static
name|Certificate
name|getCertificateFromKeyStore
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|loadKeystore
argument_list|()
operator|.
name|getCertificate
argument_list|(
literal|"ECDSA"
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|getKeyFromKeystore ()
specifier|private
specifier|static
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
literal|"ECDSA"
argument_list|,
literal|"security"
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
end_function

begin_function
DECL|method|getKeyAccessor ()
specifier|static
name|KeyAccessor
name|getKeyAccessor
parameter_list|()
block|{
name|KeyAccessor
name|accessor
init|=
operator|new
name|KeyAccessor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|KeySelector
name|getKeySelector
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|KeySelector
operator|.
name|singletonKeySelector
argument_list|(
name|getKeyFromKeystore
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyInfo
name|getKeyInfo
parameter_list|(
name|Message
name|mess
parameter_list|,
name|Node
name|messageBody
parameter_list|,
name|KeyInfoFactory
name|keyInfoFactory
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
decl_stmt|;
return|return
name|accessor
return|;
block|}
end_function

begin_function
DECL|method|getSameDocumentUriDereferencer ()
specifier|public
specifier|static
name|URIDereferencer
name|getSameDocumentUriDereferencer
parameter_list|()
block|{
return|return
name|SameDocumentUriDereferencer
operator|.
name|getInstance
argument_list|()
return|;
block|}
end_function

unit|}
end_unit

