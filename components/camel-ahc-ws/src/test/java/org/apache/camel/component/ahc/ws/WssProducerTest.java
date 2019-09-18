begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
package|;
end_package

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
name|ClientAuthentication
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
name|SSLContextServerParameters
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
name|jetty
operator|.
name|server
operator|.
name|Connector
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Disabled
import|;
end_import

begin_class
annotation|@
name|Disabled
argument_list|(
literal|"Not yet migrated to work with Jetty 9"
argument_list|)
DECL|class|WssProducerTest
specifier|public
class|class
name|WssProducerTest
extends|extends
name|WsProducerTestBase
block|{
DECL|field|PW
specifier|protected
specifier|static
specifier|final
name|String
name|PW
init|=
literal|"changeit"
decl_stmt|;
annotation|@
name|Override
DECL|method|getConnector ()
specifier|protected
name|Connector
name|getConnector
parameter_list|()
throws|throws
name|Exception
block|{
name|SslContextFactory
name|sslContextFactory
init|=
operator|new
name|SslContextFactory
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setSslContext
argument_list|(
name|defineSSLContextServerParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
name|camelContext
argument_list|)
argument_list|)
expr_stmt|;
name|ServerConnector
name|https
init|=
operator|new
name|ServerConnector
argument_list|(
name|server
argument_list|,
operator|new
name|SslConnectionFactory
argument_list|(
name|sslContextFactory
argument_list|,
literal|null
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|https
return|;
block|}
annotation|@
name|Override
DECL|method|getTargetURL ()
specifier|protected
name|String
name|getTargetURL
parameter_list|()
block|{
return|return
literal|"ahc-wss://localhost:"
operator|+
name|PORT
return|;
block|}
annotation|@
name|Override
DECL|method|setUpComponent ()
specifier|protected
name|void
name|setUpComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|WsComponent
name|wsComponent
init|=
operator|(
name|WsComponent
operator|)
name|camelContext
operator|.
name|getComponent
argument_list|(
literal|"ahc-wss"
argument_list|)
decl_stmt|;
name|wsComponent
operator|.
name|setSslContextParameters
argument_list|(
name|defineSSLContextClientParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|defineSSLContextServerParameters ()
specifier|private
specifier|static
name|SSLContextParameters
name|defineSSLContextServerParameters
parameter_list|()
block|{
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
literal|"jsse/localhost.p12"
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
name|PW
argument_list|)
expr_stmt|;
name|KeyManagersParameters
name|kmp
init|=
operator|new
name|KeyManagersParameters
argument_list|()
decl_stmt|;
name|kmp
operator|.
name|setKeyPassword
argument_list|(
name|PW
argument_list|)
expr_stmt|;
name|kmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
name|TrustManagersParameters
name|tmp
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|tmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
comment|// NOTE: Needed since the client uses a loose trust configuration when no ssl context
comment|// is provided.  We turn on WANT client-auth to prefer using authentication
name|SSLContextServerParameters
name|scsp
init|=
operator|new
name|SSLContextServerParameters
argument_list|()
decl_stmt|;
name|scsp
operator|.
name|setClientAuthentication
argument_list|(
name|ClientAuthentication
operator|.
name|WANT
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|sslContextParameters
operator|.
name|setKeyManagers
argument_list|(
name|kmp
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|sslContextParameters
operator|.
name|setServerParameters
argument_list|(
name|scsp
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
DECL|method|defineSSLContextClientParameters ()
specifier|private
specifier|static
name|SSLContextParameters
name|defineSSLContextClientParameters
parameter_list|()
block|{
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
literal|"jsse/localhost.p12"
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
name|PW
argument_list|)
expr_stmt|;
name|TrustManagersParameters
name|tmp
init|=
operator|new
name|TrustManagersParameters
argument_list|()
decl_stmt|;
name|tmp
operator|.
name|setKeyStore
argument_list|(
name|ksp
argument_list|)
expr_stmt|;
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
decl_stmt|;
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
return|return
name|sslContextParameters
return|;
block|}
block|}
end_class

end_unit

