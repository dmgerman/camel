begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rest.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rest
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|common
operator|.
name|HttpsSettings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|common
operator|.
name|JettySettings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|core
operator|.
name|Options
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|http
operator|.
name|AdminRequestHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|http
operator|.
name|HttpServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|http
operator|.
name|StubRequestHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|jetty9
operator|.
name|JettyHttpServer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|tomakehurst
operator|.
name|wiremock
operator|.
name|jetty9
operator|.
name|JettyHttpServerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|io
operator|.
name|NetworkTrafficListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|HttpConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|SecureRequestCustomizer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|ServerConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|server
operator|.
name|SslConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|ssl
operator|.
name|SslContextFactory
import|;
end_import

begin_comment
comment|/**  * Wiremock 2.18.0 ships with Jetty 9.2, Camel (currently) uses 9.4 and the  * {@link org.eclipse.jetty.util.ssl.SslContextFactory} removed  * {@code selectCipherSuites} method.  */
end_comment

begin_class
DECL|class|Jetty94ServerFactory
specifier|public
specifier|final
class|class
name|Jetty94ServerFactory
extends|extends
name|JettyHttpServerFactory
block|{
annotation|@
name|Override
DECL|method|buildHttpServer (final Options options, final AdminRequestHandler adminRequestHandler, final StubRequestHandler stubRequestHandler)
specifier|public
name|HttpServer
name|buildHttpServer
parameter_list|(
specifier|final
name|Options
name|options
parameter_list|,
specifier|final
name|AdminRequestHandler
name|adminRequestHandler
parameter_list|,
specifier|final
name|StubRequestHandler
name|stubRequestHandler
parameter_list|)
block|{
return|return
operator|new
name|JettyHttpServer
argument_list|(
name|options
argument_list|,
name|adminRequestHandler
argument_list|,
name|stubRequestHandler
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|ServerConnector
name|createHttpsConnector
parameter_list|(
specifier|final
name|String
name|bindAddress
parameter_list|,
specifier|final
name|HttpsSettings
name|httpsSettings
parameter_list|,
specifier|final
name|JettySettings
name|jettySettings
parameter_list|,
specifier|final
name|NetworkTrafficListener
name|listener
parameter_list|)
block|{
specifier|final
name|SslContextFactory
name|sslContextFactory
init|=
operator|new
name|SslContextFactory
operator|.
name|Server
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setKeyStorePath
argument_list|(
name|httpsSettings
operator|.
name|keyStorePath
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setKeyManagerPassword
argument_list|(
name|httpsSettings
operator|.
name|keyStorePassword
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setKeyStorePassword
argument_list|(
name|httpsSettings
operator|.
name|keyStorePassword
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setKeyStoreType
argument_list|(
name|httpsSettings
operator|.
name|keyStoreType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpsSettings
operator|.
name|hasTrustStore
argument_list|()
condition|)
block|{
name|sslContextFactory
operator|.
name|setTrustStorePath
argument_list|(
name|httpsSettings
operator|.
name|trustStorePath
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setTrustStorePassword
argument_list|(
name|httpsSettings
operator|.
name|trustStorePassword
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setTrustStoreType
argument_list|(
name|httpsSettings
operator|.
name|trustStoreType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|sslContextFactory
operator|.
name|setNeedClientAuth
argument_list|(
name|httpsSettings
operator|.
name|needClientAuth
argument_list|()
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setIncludeCipherSuites
argument_list|(
literal|"TLS_DHE_RSA_WITH_AES_128_GCM_SHA256"
argument_list|)
expr_stmt|;
name|sslContextFactory
operator|.
name|setProtocol
argument_list|(
literal|"TLSv1.2"
argument_list|)
expr_stmt|;
specifier|final
name|HttpConfiguration
name|httpConfig
init|=
name|createHttpConfig
argument_list|(
name|jettySettings
argument_list|)
decl_stmt|;
name|httpConfig
operator|.
name|addCustomizer
argument_list|(
operator|new
name|SecureRequestCustomizer
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|int
name|port
init|=
name|httpsSettings
operator|.
name|port
argument_list|()
decl_stmt|;
return|return
name|createServerConnector
argument_list|(
name|bindAddress
argument_list|,
name|jettySettings
argument_list|,
name|port
argument_list|,
name|listener
argument_list|,
operator|new
name|SslConnectionFactory
argument_list|(
name|sslContextFactory
argument_list|,
literal|"http/1.1"
argument_list|)
argument_list|,
operator|new
name|HttpConnectionFactory
argument_list|(
name|httpConfig
argument_list|)
argument_list|)
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

