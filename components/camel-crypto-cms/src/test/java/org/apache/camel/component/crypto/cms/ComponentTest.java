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
name|io
operator|.
name|InputStream
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
name|crypto
operator|.
name|cms
operator|.
name|crypt
operator|.
name|DefaultKeyTransRecipientInfo
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
name|exception
operator|.
name|CryptoCmsVerifierCertificateNotValidException
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
name|DefaultSignerInfo
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
name|util
operator|.
name|KeystoreUtil
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
name|util
operator|.
name|TestAttributesGeneratorProvider
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
name|util
operator|.
name|TestOriginatorInformationProvider
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
name|Test
import|;
end_import

begin_class
DECL|class|ComponentTest
specifier|public
class|class
name|ComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|simpleReg
specifier|private
name|SimpleRegistry
name|simpleReg
decl_stmt|;
annotation|@
name|Test
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"Testmessage"
decl_stmt|;
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
name|message
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|signedWithOutdatedCert ()
specifier|public
name|void
name|signedWithOutdatedCert
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"Testmessage"
decl_stmt|;
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mockException
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:exception"
argument_list|)
decl_stmt|;
name|mockException
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:outdated"
argument_list|,
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|decryptAndVerify ()
specifier|public
name|void
name|decryptAndVerify
parameter_list|()
throws|throws
name|Exception
block|{
name|InputStream
name|input
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"signed_enveloped_other_CMS_vendor.binary"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
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
literal|"Testmessage"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:decryptAndVerify"
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|input
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|orignatorUnprotectedAttributes ()
specifier|public
name|void
name|orignatorUnprotectedAttributes
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|message
init|=
literal|"Testmessage"
decl_stmt|;
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
name|message
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:encryptDecryptOriginatorAttributes"
argument_list|,
name|message
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|simpleReg
operator|=
operator|new
name|SimpleRegistry
argument_list|()
expr_stmt|;
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|simpleReg
argument_list|)
decl_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setStreamCaching
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|keystore
init|=
name|KeystoreUtil
operator|.
name|getKeyStoreParameters
argument_list|(
literal|"system.jks"
argument_list|)
decl_stmt|;
name|DefaultKeyTransRecipientInfo
name|recipient
init|=
operator|new
name|DefaultKeyTransRecipientInfo
argument_list|()
decl_stmt|;
name|recipient
operator|.
name|setCertificateAlias
argument_list|(
literal|"rsa"
argument_list|)
expr_stmt|;
name|recipient
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|DefaultSignerInfo
name|signerInfo
init|=
operator|new
name|DefaultSignerInfo
argument_list|()
decl_stmt|;
name|signerInfo
operator|.
name|setIncludeCertificates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|signerInfo
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"SHA256withRSA"
argument_list|)
expr_stmt|;
name|signerInfo
operator|.
name|setPrivateKeyAlias
argument_list|(
literal|"rsa"
argument_list|)
expr_stmt|;
name|signerInfo
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|DefaultSignerInfo
name|signerInfo2
init|=
operator|new
name|DefaultSignerInfo
argument_list|()
decl_stmt|;
name|signerInfo2
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"SHA256withDSA"
argument_list|)
expr_stmt|;
comment|// mandatory
name|signerInfo2
operator|.
name|setPrivateKeyAlias
argument_list|(
literal|"dsa"
argument_list|)
expr_stmt|;
name|signerInfo2
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"keyStoreParameters"
argument_list|,
name|keystore
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"signer1"
argument_list|,
name|signerInfo
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"signer2"
argument_list|,
name|signerInfo2
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"recipient1"
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|CryptoCmsVerifierCertificateNotValidException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:sign://testsign?signer=#signer1,#signer2&includeContent=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:encrypt://testencrpyt?toBase64=true&recipient=#recipient1&contentEncryptionAlgorithm=DESede/CBC/PKCS5Padding&secretKeyLength=128"
argument_list|)
comment|// .to("file:target/test_signed_encrypted.base64")
operator|.
name|to
argument_list|(
literal|"crypto-cms:decrypt://testdecrypt?fromBase64=true&keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:verify://testverify?keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:after"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|DefaultSignerInfo
name|signerOutdated
init|=
operator|new
name|DefaultSignerInfo
argument_list|()
decl_stmt|;
name|signerOutdated
operator|.
name|setIncludeCertificates
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|signerOutdated
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"SHA1withRSA"
argument_list|)
expr_stmt|;
name|signerOutdated
operator|.
name|setPrivateKeyAlias
argument_list|(
literal|"outdated"
argument_list|)
expr_stmt|;
name|signerOutdated
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"signerOutdated"
argument_list|,
name|signerOutdated
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:outdated"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:sign://outdated?signer=#signerOutdated&includeContent=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:verify://outdated?keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:decryptAndVerify"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:decrypt://testdecrypt?fromBase64=true&keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:verify://testverify?keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|TestOriginatorInformationProvider
name|originatorInformationProvider
init|=
operator|new
name|TestOriginatorInformationProvider
argument_list|()
decl_stmt|;
name|TestAttributesGeneratorProvider
name|attributesGeneratorProvider
init|=
operator|new
name|TestAttributesGeneratorProvider
argument_list|()
decl_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"originatorInformationProvider1"
argument_list|,
name|originatorInformationProvider
argument_list|)
expr_stmt|;
name|simpleReg
operator|.
name|bind
argument_list|(
literal|"attributesGeneratorProvider1"
argument_list|,
name|attributesGeneratorProvider
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:encryptDecryptOriginatorAttributes"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:encrypt://testencrpyt?toBase64=true&recipient=#recipient1&contentEncryptionAlgorithm=DESede/CBC/PKCS5Padding&secretKeyLength=128&"
operator|+
literal|"originatorInformationProvider=#originatorInformationProvider1&unprotectedAttributesGeneratorProvider=#attributesGeneratorProvider1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"crypto-cms:decrypt://testdecrypt?fromBase64=true&keyStoreParameters=#keyStoreParameters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalStateException
operator|.
name|class
argument_list|)
DECL|method|wrongOperation ()
specifier|public
name|void
name|wrongOperation
parameter_list|()
throws|throws
name|Exception
block|{
name|CryptoCmsComponent
name|c
init|=
operator|new
name|CryptoCmsComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|c
operator|.
name|createEndpoint
argument_list|(
literal|"uri"
argument_list|,
literal|"wrongoperation"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

