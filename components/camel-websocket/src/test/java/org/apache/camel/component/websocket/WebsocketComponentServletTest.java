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
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|api
operator|.
name|Session
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
name|websocket
operator|.
name|servlet
operator|.
name|ServletUpgradeRequest
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|InOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|inOrder
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|times
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|WebsocketComponentServletTest
specifier|public
class|class
name|WebsocketComponentServletTest
block|{
DECL|field|PROTOCOL
specifier|private
specifier|static
specifier|final
name|String
name|PROTOCOL
init|=
literal|"ws"
decl_stmt|;
DECL|field|MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE
init|=
literal|"message"
decl_stmt|;
DECL|field|CONNECTION_KEY
specifier|private
specifier|static
specifier|final
name|String
name|CONNECTION_KEY
init|=
literal|"random-connection-key"
decl_stmt|;
DECL|field|ADDRESS
specifier|private
specifier|static
specifier|final
name|InetSocketAddress
name|ADDRESS
init|=
name|InetSocketAddress
operator|.
name|createUnresolved
argument_list|(
literal|"127.0.0.1"
argument_list|,
literal|12345
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|session
specifier|private
name|Session
name|session
decl_stmt|;
annotation|@
name|Mock
DECL|field|consumer
specifier|private
name|WebsocketConsumer
name|consumer
decl_stmt|;
annotation|@
name|Mock
DECL|field|sync
specifier|private
name|NodeSynchronization
name|sync
decl_stmt|;
annotation|@
name|Mock
DECL|field|request
specifier|private
name|ServletUpgradeRequest
name|request
decl_stmt|;
DECL|field|websocketComponentServlet
specifier|private
name|WebsocketComponentServlet
name|websocketComponentServlet
decl_stmt|;
DECL|field|socketFactory
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocketFactory
argument_list|>
name|socketFactory
decl_stmt|;
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
name|socketFactory
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|socketFactory
operator|.
name|put
argument_list|(
literal|"default"
argument_list|,
operator|new
name|DefaultWebsocketFactory
argument_list|()
argument_list|)
expr_stmt|;
name|websocketComponentServlet
operator|=
operator|new
name|WebsocketComponentServlet
argument_list|(
name|sync
argument_list|,
literal|null
argument_list|,
name|socketFactory
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|session
operator|.
name|getRemoteAddress
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ADDRESS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConsumer ()
specifier|public
name|void
name|testGetConsumer
parameter_list|()
block|{
name|assertNull
argument_list|(
name|websocketComponentServlet
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|websocketComponentServlet
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|consumer
argument_list|,
name|websocketComponentServlet
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetConsumer ()
specifier|public
name|void
name|testSetConsumer
parameter_list|()
block|{
name|testGetConsumer
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoWebSocketConnect ()
specifier|public
name|void
name|testDoWebSocketConnect
parameter_list|()
block|{
name|websocketComponentServlet
operator|.
name|setConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|DefaultWebsocket
name|webSocket
init|=
name|websocketComponentServlet
operator|.
name|doWebSocketConnect
argument_list|(
name|request
argument_list|,
name|PROTOCOL
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|webSocket
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DefaultWebsocket
operator|.
name|class
argument_list|,
name|webSocket
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultWebsocket
name|defaultWebsocket
init|=
name|webSocket
decl_stmt|;
name|defaultWebsocket
operator|.
name|setConnectionKey
argument_list|(
name|CONNECTION_KEY
argument_list|)
expr_stmt|;
name|defaultWebsocket
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|defaultWebsocket
operator|.
name|onMessage
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|consumer
argument_list|,
name|sync
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verify
argument_list|(
name|consumer
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|sendMessage
argument_list|(
name|CONNECTION_KEY
argument_list|,
name|MESSAGE
argument_list|,
name|ADDRESS
argument_list|)
expr_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDoWebSocketConnectConsumerIsNull ()
specifier|public
name|void
name|testDoWebSocketConnectConsumerIsNull
parameter_list|()
block|{
name|DefaultWebsocket
name|webSocket
init|=
name|websocketComponentServlet
operator|.
name|doWebSocketConnect
argument_list|(
name|request
argument_list|,
name|PROTOCOL
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|webSocket
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DefaultWebsocket
operator|.
name|class
argument_list|,
name|webSocket
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|DefaultWebsocket
name|defaultWebsocket
init|=
name|webSocket
decl_stmt|;
name|defaultWebsocket
operator|.
name|setConnectionKey
argument_list|(
name|CONNECTION_KEY
argument_list|)
expr_stmt|;
name|defaultWebsocket
operator|.
name|onMessage
argument_list|(
name|MESSAGE
argument_list|)
expr_stmt|;
name|InOrder
name|inOrder
init|=
name|inOrder
argument_list|(
name|consumer
argument_list|,
name|sync
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|inOrder
operator|.
name|verifyNoMoreInteractions
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

