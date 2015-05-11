begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
operator|.
name|util
package|;
end_package

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
name|security
operator|.
name|GeneralSecurityException
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
name|Provider
operator|.
name|Service
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
name|CertificateException
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
name|X509Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|X509TrustManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|internal
operator|.
name|ApnsFeedbackParsingUtilsAcessor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|internal
operator|.
name|Utilities
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|utils
operator|.
name|ApnsServerStub
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|utils
operator|.
name|FixedCertificates
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
name|component
operator|.
name|apns
operator|.
name|factory
operator|.
name|ApnsServiceFactory
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
name|KeyManagersParameters
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
name|camel
operator|.
name|util
operator|.
name|jsse
operator|.
name|SSLContextParameters
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
name|TrustManagersParameters
import|;
end_import

begin_class
DECL|class|ApnsUtils
specifier|public
specifier|final
class|class
name|ApnsUtils
block|{
DECL|field|random
specifier|private
specifier|static
name|Random
name|random
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
DECL|method|ApnsUtils ()
specifier|private
name|ApnsUtils
parameter_list|()
block|{     }
DECL|method|createRandomDeviceTokenBytes ()
specifier|public
specifier|static
name|byte
index|[]
name|createRandomDeviceTokenBytes
parameter_list|()
block|{
name|byte
index|[]
name|deviceTokenBytes
init|=
operator|new
name|byte
index|[
literal|32
index|]
decl_stmt|;
name|random
operator|.
name|nextBytes
argument_list|(
name|deviceTokenBytes
argument_list|)
expr_stmt|;
return|return
name|deviceTokenBytes
return|;
block|}
DECL|method|encodeHexToken (byte[] deviceTokenBytes)
specifier|public
specifier|static
name|String
name|encodeHexToken
parameter_list|(
name|byte
index|[]
name|deviceTokenBytes
parameter_list|)
block|{
name|String
name|deviceToken
init|=
name|Utilities
operator|.
name|encodeHex
argument_list|(
name|deviceTokenBytes
argument_list|)
decl_stmt|;
return|return
name|deviceToken
return|;
block|}
DECL|method|prepareAndStartServer (int gatePort, int feedPort)
specifier|public
specifier|static
name|ApnsServerStub
name|prepareAndStartServer
parameter_list|(
name|int
name|gatePort
parameter_list|,
name|int
name|feedPort
parameter_list|)
block|{
name|InputStream
name|stream
init|=
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|FixedCertificates
operator|.
name|SERVER_STORE
argument_list|)
decl_stmt|;
name|SSLContext
name|context
init|=
name|Utilities
operator|.
name|newSSLContext
argument_list|(
name|stream
argument_list|,
name|FixedCertificates
operator|.
name|SERVER_PASSWORD
argument_list|,
literal|"PKCS12"
argument_list|,
name|getAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|ApnsServerStub
name|server
init|=
operator|new
name|ApnsServerStub
argument_list|(
name|context
operator|.
name|getServerSocketFactory
argument_list|()
argument_list|,
name|gatePort
argument_list|,
name|feedPort
argument_list|)
decl_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|server
return|;
block|}
DECL|method|getAlgorithm ()
specifier|public
specifier|static
name|String
name|getAlgorithm
parameter_list|()
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|trusts
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Provider
name|p
range|:
name|Security
operator|.
name|getProviders
argument_list|()
control|)
block|{
for|for
control|(
name|Service
name|s
range|:
name|p
operator|.
name|getServices
argument_list|()
control|)
block|{
if|if
condition|(
literal|"KeyManagerFactory"
operator|.
name|equals
argument_list|(
name|s
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|s
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"509"
argument_list|)
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|s
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"TrustManagerFactory"
operator|.
name|equals
argument_list|(
name|s
operator|.
name|getType
argument_list|()
argument_list|)
operator|&&
name|s
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"509"
argument_list|)
condition|)
block|{
name|trusts
operator|.
name|add
argument_list|(
name|s
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|keys
operator|.
name|retainAll
argument_list|(
name|trusts
argument_list|)
expr_stmt|;
return|return
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|clientContext ()
specifier|public
specifier|static
name|SSLContextParameters
name|clientContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|KeyStoreParameters
name|ksp
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|ksp
operator|.
name|setResource
argument_list|(
name|ClassLoader
operator|.
name|getSystemResource
argument_list|(
name|FixedCertificates
operator|.
name|CLIENT_STORE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setType
argument_list|(
literal|"PKCS12"
argument_list|)
expr_stmt|;
specifier|final
name|KeyManagersParameters
name|kmp
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|kmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
name|kmp
operator|.
name|setKeyPassword
argument_list|(
name|FixedCertificates
operator|.
name|CLIENT_PASSWORD
argument_list|)
expr_stmt|;
name|kmp
operator|.
name|setAlgorithm
argument_list|(
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SSLContextParameters
name|contextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|contextParameters
operator|.
name|setKeyManagers
argument_list|(
name|kmp
argument_list|)
expr_stmt|;
name|contextParameters
operator|.
name|setTrustManagers
argument_list|(
operator|new
name|TrustManagersParameters
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|TrustManager
index|[]
name|createTrustManagers
parameter_list|()
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
return|return
operator|new
name|TrustManager
index|[]
block|{
operator|new
name|X509TrustManager
argument_list|()
block|{
specifier|public
name|void
name|checkClientTrusted
parameter_list|(
name|X509Certificate
index|[]
name|chain
parameter_list|,
name|String
name|authType
parameter_list|)
throws|throws
name|CertificateException
block|{
block|}
specifier|public
name|void
name|checkServerTrusted
argument_list|(
name|X509Certificate
index|[]
name|chain
argument_list|,
name|String
name|authType
argument_list|)
throws|throws
name|CertificateException
block|{                     }
specifier|public
name|X509Certificate
index|[]
name|getAcceptedIssuers
argument_list|()
block|{
return|return
operator|new
name|X509Certificate
index|[
literal|0
index|]
return|;
block|}
block|}
block|}
empty_stmt|;
block|}
end_class

begin_empty_stmt
unit|})
empty_stmt|;
end_empty_stmt

begin_return
return|return
name|contextParameters
return|;
end_return

begin_function
unit|}          public
DECL|method|createDefaultTestConfiguration (CamelContext camelContext)
specifier|static
name|ApnsServiceFactory
name|createDefaultTestConfiguration
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
operator|new
name|ApnsServiceFactory
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|apnsServiceFactory
operator|.
name|setFeedbackHost
argument_list|(
name|TestConstants
operator|.
name|TEST_HOST
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setFeedbackPort
argument_list|(
name|TestConstants
operator|.
name|TEST_FEEDBACK_PORT
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setGatewayHost
argument_list|(
name|TestConstants
operator|.
name|TEST_HOST
argument_list|)
expr_stmt|;
name|apnsServiceFactory
operator|.
name|setGatewayPort
argument_list|(
name|TestConstants
operator|.
name|TEST_GATEWAY_PORT
argument_list|)
expr_stmt|;
comment|// apnsServiceFactory.setCertificatePath("classpath:/" +
comment|// FixedCertificates.CLIENT_STORE);
comment|// apnsServiceFactory.setCertificatePassword(FixedCertificates.CLIENT_PASSWD);
name|apnsServiceFactory
operator|.
name|setSslContextParameters
argument_list|(
name|clientContext
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|apnsServiceFactory
return|;
block|}
end_function

begin_function
DECL|method|generateFeedbackBytes (byte[] deviceTokenBytes)
specifier|public
specifier|static
name|byte
index|[]
name|generateFeedbackBytes
parameter_list|(
name|byte
index|[]
name|deviceTokenBytes
parameter_list|)
block|{
name|byte
index|[]
name|feedbackBytes
init|=
name|ApnsFeedbackParsingUtilsAcessor
operator|.
name|pack
argument_list|(
comment|/* time_t */
operator|new
name|byte
index|[]
block|{
literal|0
block|,
literal|0
block|,
literal|0
block|,
literal|0
block|}
argument_list|,
comment|/* length */
operator|new
name|byte
index|[]
block|{
literal|0
block|,
literal|32
block|}
argument_list|,
comment|/* device token */
name|deviceTokenBytes
argument_list|)
decl_stmt|;
return|return
name|feedbackBytes
return|;
block|}
end_function

unit|}
end_unit

