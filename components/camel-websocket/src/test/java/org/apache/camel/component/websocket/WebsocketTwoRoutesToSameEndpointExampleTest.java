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
name|Test
import|;
end_import

begin_class
DECL|class|WebsocketTwoRoutesToSameEndpointExampleTest
specifier|public
class|class
name|WebsocketTwoRoutesToSameEndpointExampleTest
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
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
annotation|@
name|Override
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
literal|16310
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
DECL|method|testWSHttpCallEcho ()
specifier|public
name|void
name|testWSHttpCallEcho
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We call the route WebSocket BAR
name|received
operator|.
name|clear
argument_list|()
expr_stmt|;
name|latch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|DefaultAsyncHttpClient
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
literal|"ws://localhost:"
operator|+
name|port
operator|+
literal|"/bar"
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
name|websocket
operator|.
name|sendMessage
argument_list|(
literal|"Beer"
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
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|//Cannot guarantee the order in which messages are received
name|assertTrue
argument_list|(
name|received
operator|.
name|contains
argument_list|(
literal|"The bar has Beer"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|received
operator|.
name|contains
argument_list|(
literal|"Broadcasting to Bar"
argument_list|)
argument_list|)
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
name|setMinThreads
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|websocketComponent
operator|.
name|setMaxThreads
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar"
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> Message received from BAR WebSocket Client : ${body}"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"The bar has ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"timer://foo?fixedRate=true&period=12000"
argument_list|)
comment|//Use a period which is longer then the latch await time
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Broadcasting to Bar"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> Broadcasting message to Bar WebSocket Client"
argument_list|)
operator|.
name|to
argument_list|(
literal|"websocket://localhost:"
operator|+
name|port
operator|+
literal|"/bar?sendToAll=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

