begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|net
operator|.
name|URL
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
name|Properties
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
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketTextListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|websocket
operator|.
name|WebSocketUpgradeHandler
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
name|util
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
DECL|class|WebsocketSSLRouteExampleTest
specifier|public
class|class
name|WebsocketSSLRouteExampleTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|NULL_VALUE_MARKER
specifier|private
specifier|static
specifier|final
name|String
name|NULL_VALUE_MARKER
init|=
name|CamelTestSupport
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
decl_stmt|;
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
argument_list|<
name|String
argument_list|>
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
DECL|field|originalValues
specifier|protected
name|Properties
name|originalValues
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
DECL|field|pwd
specifier|protected
name|String
name|pwd
init|=
literal|"changeit"
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|URL
name|trustStoreUrl
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"jsse/localhost.ks"
argument_list|)
decl_stmt|;
name|setSystemProp
argument_list|(
literal|"javax.net.ssl.trustStore"
argument_list|,
name|trustStoreUrl
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setSystemProp (String key, String value)
specifier|protected
name|void
name|setSystemProp
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|String
name|originalValue
init|=
name|System
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|originalValues
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|originalValue
operator|!=
literal|null
condition|?
name|originalValue
else|:
name|NULL_VALUE_MARKER
argument_list|)
expr_stmt|;
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
name|AsyncHttpClientConfig
operator|.
name|Builder
name|builder
init|=
operator|new
name|AsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|builder
operator|.
name|setSSLContext
argument_list|(
operator|new
name|SSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|()
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
name|AsyncHttpClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
DECL|method|defineSSLContextParameters ()
specifier|protected
name|SSLContextParameters
name|defineSSLContextParameters
parameter_list|()
block|{
name|KeyStoreParameters
name|ksp
init|=
operator|new
name|KeyStoreParameters
argument_list|()
decl_stmt|;
comment|// ksp.setResource(this.getClass().getClassLoader().getResource("jsse/localhost.ks").toString());
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
literal|"wss://127.0.0.1:8443/test"
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
name|onFragment
parameter_list|(
name|String
name|fragment
parameter_list|,
name|boolean
name|last
parameter_list|)
block|{                             }
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
name|sendTextMessage
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
name|setSslContextParameters
argument_list|(
name|defineSSLContextParameters
argument_list|()
argument_list|)
expr_stmt|;
name|websocketComponent
operator|.
name|setPort
argument_list|(
literal|8443
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"websocket://test"
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
literal|"websocket://test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

