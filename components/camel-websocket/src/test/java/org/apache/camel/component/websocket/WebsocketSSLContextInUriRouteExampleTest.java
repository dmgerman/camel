begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|websocket
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|ClientAuth
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|ssl
operator|.
name|JdkSslContext
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|AvailablePortFinder
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
name|asynchttpclient
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|AsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|DefaultAsyncHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|DefaultAsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocketTextListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|asynchttpclient
operator|.
name|ws
operator|.
name|WebSocketUpgradeHandler
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

begin_class
DECL|class|WebsocketSSLContextInUriRouteExampleTest
specifier|public
class|class
name|WebsocketSSLContextInUriRouteExampleTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|received
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|latch
specifier|private
specifier|static
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|10
argument_list|)
decl_stmt|;
DECL|field|pwd
specifier|private
name|String
name|pwd
init|=
literal|"changeit"
decl_stmt|;
DECL|field|uri
specifier|private
name|String
name|uri
decl_stmt|;
DECL|field|server
specifier|private
name|String
name|server
init|=
literal|"127.0.0.1"
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
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
name|port
operator|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
expr_stmt|;
name|uri
operator|=
literal|"websocket://"
operator|+
name|server
operator|+
literal|":"
operator|+
name|port
operator|+
literal|"/test?sslContextParameters=#sslContextParameters"
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
literal|"jsse/localhost.ks"
argument_list|)
expr_stmt|;
name|ksp
operator|.
name|setPassword
argument_list|(
name|pwd
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
name|pwd
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
name|Registry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|createAsyncHttpSSLClient ()
specifier|protected
name|AsyncHttpClient
name|createAsyncHttpSSLClient
parameter_list|()
throws|throws
name|IOException
throws|,
name|GeneralSecurityException
block|{
name|AsyncHttpClient
name|c
decl_stmt|;
name|AsyncHttpClientConfig
name|config
decl_stmt|;
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|DefaultAsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|SSLContextParameters
name|sslContextParameters
init|=
operator|new
name|SSLContextParameters
argument_list|()
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
literal|"jsse/localhost.ks"
argument_list|)
expr_stmt|;
name|truststoreParameters
operator|.
name|setPassword
argument_list|(
name|pwd
argument_list|)
expr_stmt|;
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
name|sslContextParameters
operator|.
name|setTrustManagers
argument_list|(
name|clientSSLTrustManagers
argument_list|)
expr_stmt|;
name|SSLContext
name|sslContext
init|=
name|sslContextParameters
operator|.
name|createSSLContext
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
name|JdkSslContext
name|ssl
init|=
operator|new
name|JdkSslContext
argument_list|(
name|sslContext
argument_list|,
literal|true
argument_list|,
name|ClientAuth
operator|.
name|REQUIRE
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setSslContext
argument_list|(
name|ssl
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setAcceptAnyCertificate
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|c
operator|=
operator|new
name|DefaultAsyncHttpClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
annotation|@
name|Test
DECL|method|testWSHttpCall ()
specifier|public
name|void
name|testWSHttpCall
parameter_list|()
throws|throws
name|Exception
block|{
name|AsyncHttpClient
name|c
init|=
name|createAsyncHttpSSLClient
argument_list|()
decl_stmt|;
name|WebSocket
name|websocket
init|=
name|c
operator|.
name|prepareGet
argument_list|(
literal|"wss://127.0.0.1:"
operator|+
name|port
operator|+
literal|"/test"
argument_list|)
operator|.
name|execute
argument_list|(
operator|new
name|WebSocketUpgradeHandler
operator|.
name|Builder
argument_list|()
operator|.
name|addWebSocketListener
argument_list|(
operator|new
name|WebSocketTextListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|received
operator|.
name|add
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"received --> "
operator|+
name|message
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                             }
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                             }
annotation|@
name|Override
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:client"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello from WS client"
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|sendMessage
argument_list|(
literal|"Hello from WS client"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|">> Welcome on board!"
argument_list|,
name|received
operator|.
name|get
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|websocket
operator|.
name|close
argument_list|()
expr_stmt|;
name|c
operator|.
name|close
argument_list|()
expr_stmt|;
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|WebsocketComponent
name|websocketComponent
init|=
operator|(
name|WebsocketComponent
operator|)
name|context
operator|.
name|getComponent
argument_list|(
literal|"websocket"
argument_list|)
decl_stmt|;
name|websocketComponent
operator|.
name|setMinThreads
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|websocketComponent
operator|.
name|setMaxThreads
argument_list|(
literal|25
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|uri
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> Message received from WebSocket Client : ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:client"
argument_list|)
operator|.
name|loop
argument_list|(
literal|10
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
literal|">> Welcome on board!"
argument_list|)
operator|.
name|to
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

