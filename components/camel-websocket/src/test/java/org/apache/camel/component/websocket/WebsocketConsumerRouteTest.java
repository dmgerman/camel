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
name|DefaultAsyncHttpClient
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
name|WebSocketListener
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
DECL|class|WebsocketConsumerRouteTest
specifier|public
class|class
name|WebsocketConsumerRouteTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port
specifier|protected
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
argument_list|(
literal|16200
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
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
operator|new
name|DefaultAsyncHttpClient
argument_list|()
decl_stmt|;
name|WebSocket
name|websocket
init|=
name|c
operator|.
name|prepareGet
argument_list|(
literal|"ws://127.0.0.1:"
operator|+
name|port
operator|+
literal|"/echo"
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
name|WebSocketListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                     }
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                     }
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
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|sendMessage
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
name|Test
DECL|method|testWSBytesHttpCall ()
specifier|public
name|void
name|testWSBytesHttpCall
parameter_list|()
throws|throws
name|Exception
block|{
name|AsyncHttpClient
name|c
init|=
operator|new
name|DefaultAsyncHttpClient
argument_list|()
decl_stmt|;
name|WebSocket
name|websocket
init|=
name|c
operator|.
name|prepareGet
argument_list|(
literal|"ws://127.0.0.1:"
operator|+
name|port
operator|+
literal|"/echo"
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
name|WebSocketListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                     }
annotation|@
name|Override
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{                     }
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
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
specifier|final
name|byte
index|[]
name|testmessage
init|=
literal|"Test"
operator|.
name|getBytes
argument_list|(
literal|"utf-8"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|testmessage
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|sendMessage
argument_list|(
name|testmessage
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
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
name|setPort
argument_list|(
name|port
argument_list|)
expr_stmt|;
name|websocketComponent
operator|.
name|setMaxThreads
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|websocketComponent
operator|.
name|setMinThreads
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"websocket://echo"
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> Message received from WebSocket Client : ${body}"
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

