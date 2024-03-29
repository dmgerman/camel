begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|security
operator|.
name|GeneralSecurityException
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|RestConfigurationDefinition
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
name|support
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
name|support
operator|.
name|jsse
operator|.
name|TrustManagersParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|CoapEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|config
operator|.
name|NetworkConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|elements
operator|.
name|tcp
operator|.
name|TcpClientConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|elements
operator|.
name|tcp
operator|.
name|TlsClientConnector
import|;
end_import

begin_comment
comment|/**  * Test the CoAP Rest Component with TCP + TLS  */
end_comment

begin_class
DECL|class|CoAPRestComponentTCPTLSTest
specifier|public
class|class
name|CoAPRestComponentTCPTLSTest
extends|extends
name|CoAPRestComponentTestBase
block|{
annotation|@
name|Override
DECL|method|getProtocol ()
specifier|protected
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|"coaps+tcp"
return|;
block|}
annotation|@
name|Override
DECL|method|decorateClient (CoapClient client)
specifier|protected
name|void
name|decorateClient
parameter_list|(
name|CoapClient
name|client
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|NetworkConfig
name|config
init|=
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
decl_stmt|;
name|int
name|tcpThreads
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_WORKER_THREADS
argument_list|)
decl_stmt|;
name|int
name|tcpConnectTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECT_TIMEOUT
argument_list|)
decl_stmt|;
name|int
name|tcpIdleTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECTION_IDLE_TIMEOUT
argument_list|)
decl_stmt|;
name|KeyStoreParameters
name|truststoreParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|truststoreParameters
operator|.
name|setResource
argument_list|(
literal|"truststore.jks"
argument_list|)
expr_stmt|;
name|truststoreParameters
operator|.
name|setPassword
argument_list|(
literal|"storepass"
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|clientSSLContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|TrustManagersParameters
name|clientSSLTrustManagers
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|clientSSLTrustManagers
operator|.
name|setKeyStore
argument_list|(
name|truststoreParameters
argument_list|)
expr_stmt|;
name|clientSSLContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|clientSSLTrustManagers
argument_list|)
expr_stmt|;
name|SSLContext
name|sslContext
init|=
name|clientSSLContextParameters
operator|.
name|createSSLContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|TcpClientConnector
name|tcpConnector
init|=
operator|new
name|TlsClientConnector
argument_list|(
name|sslContext
argument_list|,
name|tcpThreads
argument_list|,
name|tcpConnectTimeout
argument_list|,
name|tcpIdleTimeout
argument_list|)
decl_stmt|;
name|CoapEndpoint
operator|.
name|Builder
name|tcpBuilder
init|=
operator|new
name|CoapEndpoint
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|tcpBuilder
operator|.
name|setConnector
argument_list|(
name|tcpConnector
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEndpoint
argument_list|(
name|tcpBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|decorateRestConfiguration (RestConfigurationDefinition restConfig)
specifier|protected
name|void
name|decorateRestConfiguration
parameter_list|(
name|RestConfigurationDefinition
name|restConfig
parameter_list|)
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
name|setResource
argument_list|(
literal|"service.jks"
argument_list|)
expr_stmt|;
name|keystoreParameters
operator|.
name|setPassword
argument_list|(
literal|"security"
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|serviceSSLContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|KeyManagersParameters
name|serviceSSLKeyManagers
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|serviceSSLKeyManagers
operator|.
name|setKeyPassword
argument_list|(
literal|"security"
argument_list|)
expr_stmt|;
name|serviceSSLKeyManagers
operator|.
name|setKeyStore
argument_list|(
name|keystoreParameters
argument_list|)
expr_stmt|;
name|serviceSSLContextParameters
operator|.
name|setKeyManagers
argument_list|(
name|serviceSSLKeyManagers
argument_list|)
expr_stmt|;
name|KeyStoreParameters
name|truststoreParameters
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
name|truststoreParameters
operator|.
name|setResource
argument_list|(
literal|"truststore.jks"
argument_list|)
expr_stmt|;
name|truststoreParameters
operator|.
name|setPassword
argument_list|(
literal|"storepass"
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|clientSSLContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|TrustManagersParameters
name|clientSSLTrustManagers
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|clientSSLTrustManagers
operator|.
name|setKeyStore
argument_list|(
name|truststoreParameters
argument_list|)
expr_stmt|;
name|clientSSLContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|clientSSLTrustManagers
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"serviceSSLContextParameters"
argument_list|,
name|serviceSSLContextParameters
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRegistry
argument_list|()
operator|.
name|bind
argument_list|(
literal|"clientSSLContextParameters"
argument_list|,
name|clientSSLContextParameters
argument_list|)
expr_stmt|;
name|restConfig
operator|.
name|endpointProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
literal|"#serviceSSLContextParameters"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getClientURI ()
specifier|protected
name|String
name|getClientURI
parameter_list|()
block|{
return|return
name|super
operator|.
name|getClientURI
argument_list|()
operator|+
literal|"?sslContextParameters=#clientSSLContextParameters"
return|;
block|}
block|}
end_class

end_unit

