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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|de
operator|.
name|roderick
operator|.
name|weberknecht
operator|.
name|WebSocketConnection
import|;
end_import

begin_import
import|import
name|de
operator|.
name|roderick
operator|.
name|weberknecht
operator|.
name|WebSocketEventHandler
import|;
end_import

begin_import
import|import
name|de
operator|.
name|roderick
operator|.
name|weberknecht
operator|.
name|WebSocketMessage
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
name|Exchange
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
name|ExchangePattern
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
name|Processor
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
name|CamelTestSupport
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
DECL|class|WebsocketClientCamelRoute2Test
specifier|public
class|class
name|WebsocketClientCamelRoute2Test
extends|extends
name|CamelTestSupport
block|{
DECL|field|uriWS
specifier|private
specifier|static
name|URI
name|uriWS
decl_stmt|;
DECL|field|webSocketConnection
specifier|private
specifier|static
name|WebSocketConnection
name|webSocketConnection
decl_stmt|;
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
name|uriWS
operator|=
operator|new
name|URI
argument_list|(
literal|"ws://127.0.0.1:9292/test"
argument_list|)
expr_stmt|;
name|WebSocketConnection
name|webSocketConnection
init|=
operator|new
name|WebSocketConnection
argument_list|(
name|uriWS
argument_list|)
decl_stmt|;
comment|// Register Event Handlers
name|webSocketConnection
operator|.
name|setEventHandler
argument_list|(
operator|new
name|WebSocketEventHandler
argument_list|()
block|{
specifier|public
name|void
name|onOpen
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"--open"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onMessage
parameter_list|(
name|WebSocketMessage
name|message
parameter_list|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"--received message: "
operator|+
name|message
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onClose
parameter_list|()
block|{
name|log
operator|.
name|info
argument_list|(
literal|"--close"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Establish WebSocket Connection
name|webSocketConnection
operator|.
name|connect
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|">>> Connection established."
argument_list|)
expr_stmt|;
comment|// Send Data
name|webSocketConnection
operator|.
name|send
argument_list|(
literal|"Hello from WS Client"
argument_list|)
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
name|from
argument_list|(
literal|"websocket://test"
argument_list|)
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
operator|.
name|log
argument_list|(
literal|">>> Message received from WebSocket Client : ${body}"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|response
init|=
literal|">> welcome on board"
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

