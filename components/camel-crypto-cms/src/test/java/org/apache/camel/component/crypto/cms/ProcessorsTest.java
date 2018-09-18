begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|DefaultEnvelopedDataDecryptorConfiguration
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
name|crypt
operator|.
name|EnvelopedDataDecryptor
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
name|EnvelopedDataEncryptor
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
name|EnvelopedDataEncryptorConfiguration
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
name|DefaultSignedDataVerifierConfiguration
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
name|sig
operator|.
name|SignedDataCreator
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
name|SignedDataCreatorConfiguration
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
name|SignedDataVerifier
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
name|impl
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
name|junit
operator|.
name|BeforeClass
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
DECL|class|ProcessorsTest
specifier|public
class|class
name|ProcessorsTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|simpleReg
specifier|private
name|SimpleRegistry
name|simpleReg
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|setUpProvider ()
specifier|public
specifier|static
name|void
name|setUpProvider
parameter_list|()
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
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
name|String
name|keystoreName
init|=
literal|"system.jks"
decl_stmt|;
name|KeyStoreParameters
name|keystore
init|=
name|KeystoreUtil
operator|.
name|getKeyStoreParameters
argument_list|(
name|keystoreName
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
name|EnvelopedDataEncryptorConfiguration
name|enConf
init|=
operator|new
name|EnvelopedDataEncryptorConfiguration
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|enConf
operator|.
name|setContentEncryptionAlgorithm
argument_list|(
literal|"DESede/CBC/PKCS5Padding"
argument_list|)
expr_stmt|;
name|enConf
operator|.
name|setRecipient
argument_list|(
name|recipient
argument_list|)
expr_stmt|;
name|enConf
operator|.
name|setSecretKeyLength
argument_list|(
literal|192
argument_list|)
expr_stmt|;
comment|// mandatory
name|enConf
operator|.
name|init
argument_list|()
expr_stmt|;
name|EnvelopedDataEncryptor
name|encryptor
init|=
operator|new
name|EnvelopedDataEncryptor
argument_list|(
name|enConf
argument_list|)
decl_stmt|;
name|DefaultEnvelopedDataDecryptorConfiguration
name|conf
init|=
operator|new
name|DefaultEnvelopedDataDecryptorConfiguration
argument_list|()
decl_stmt|;
name|conf
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|EnvelopedDataDecryptor
name|decryptor
init|=
operator|new
name|EnvelopedDataDecryptor
argument_list|(
name|conf
argument_list|)
decl_stmt|;
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
comment|// optional default
comment|// value is true
name|signerInfo
operator|.
name|setSignatureAlgorithm
argument_list|(
literal|"SHA256withRSA"
argument_list|)
expr_stmt|;
comment|// mandatory
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
name|SignedDataCreatorConfiguration
name|config
init|=
operator|new
name|SignedDataCreatorConfiguration
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|config
operator|.
name|setSigner
argument_list|(
name|signerInfo
argument_list|)
expr_stmt|;
name|config
operator|.
name|setIncludeContent
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// optional default value is
comment|// true
name|config
operator|.
name|init
argument_list|()
expr_stmt|;
name|SignedDataCreator
name|signer
init|=
operator|new
name|SignedDataCreator
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|DefaultSignedDataVerifierConfiguration
name|verifierConf
init|=
operator|new
name|DefaultSignedDataVerifierConfiguration
argument_list|()
decl_stmt|;
name|verifierConf
operator|.
name|setKeyStoreParameters
argument_list|(
name|keystore
argument_list|)
expr_stmt|;
name|SignedDataVerifier
name|verifier
init|=
operator|new
name|SignedDataVerifier
argument_list|(
name|verifierConf
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:before"
argument_list|)
operator|.
name|process
argument_list|(
name|signer
argument_list|)
operator|.
name|process
argument_list|(
name|encryptor
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:signed_encrypted"
argument_list|)
operator|.
name|process
argument_list|(
name|decryptor
argument_list|)
operator|.
name|process
argument_list|(
name|verifier
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

