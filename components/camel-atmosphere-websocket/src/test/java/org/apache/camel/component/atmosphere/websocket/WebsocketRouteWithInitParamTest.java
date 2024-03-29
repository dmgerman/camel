begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atmosphere.websocket
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atmosphere
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
name|HashMap
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
name|Map
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|WebsocketRouteWithInitParamTest
specifier|public
class|class
name|WebsocketRouteWithInitParamTest
extends|extends
name|WebsocketCamelRouterWithInitParamTestSupport
block|{
DECL|field|EXISTED_USERS
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|EXISTED_USERS
init|=
block|{
literal|"Kim"
block|,
literal|"Pavlo"
block|,
literal|"Peter"
block|}
decl_stmt|;
DECL|field|broadcastMessageTo
specifier|private
specifier|static
name|String
index|[]
name|broadcastMessageTo
init|=
block|{}
decl_stmt|;
DECL|field|connectionKeyUserMap
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|connectionKeyUserMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testWebsocketEventsResendingEnabled ()
specifier|public
name|void
name|testWebsocketEventsResendingEnabled
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPassParametersWebsocketOnOpen ()
specifier|public
name|void
name|testPassParametersWebsocketOnOpen
parameter_list|()
throws|throws
name|Exception
block|{
name|TestClient
name|wsclient
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola1?param1=value1&param2=value2"
argument_list|)
decl_stmt|;
name|wsclient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketSingleClientBroadcastMultipleClients ()
specifier|public
name|void
name|testWebsocketSingleClientBroadcastMultipleClients
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|awaitTime
init|=
literal|5
decl_stmt|;
name|connectionKeyUserMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|TestClient
name|wsclient1
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|TestClient
name|wsclient2
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|TestClient
name|wsclient3
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola2"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|wsclient1
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient1
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|wsclient2
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|wsclient3
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient3
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
comment|//all connections were registered in external store
name|assertTrue
argument_list|(
name|connectionKeyUserMap
operator|.
name|size
argument_list|()
operator|==
name|EXISTED_USERS
operator|.
name|length
argument_list|)
expr_stmt|;
name|broadcastMessageTo
operator|=
operator|new
name|String
index|[]
block|{
name|EXISTED_USERS
index|[
literal|0
index|]
block|,
name|EXISTED_USERS
index|[
literal|1
index|]
block|}
expr_stmt|;
name|wsclient1
operator|.
name|sendTextMessage
argument_list|(
literal|"Gambas"
argument_list|)
expr_stmt|;
name|wsclient1
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received1
init|=
name|wsclient1
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|element
range|:
name|broadcastMessageTo
control|)
block|{
name|assertTrue
argument_list|(
name|received1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|received2
init|=
name|wsclient2
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|element
range|:
name|broadcastMessageTo
control|)
block|{
name|assertTrue
argument_list|(
name|received2
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|received3
init|=
name|wsclient3
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|received3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|wsclient1
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsclient3
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testWebsocketSingleClientBroadcastMultipleClientsGuaranteeDelivery ()
specifier|public
name|void
name|testWebsocketSingleClientBroadcastMultipleClientsGuaranteeDelivery
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|int
name|awaitTime
init|=
literal|5
decl_stmt|;
name|connectionKeyUserMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|TestClient
name|wsclient1
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola3"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|TestClient
name|wsclient2
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola3"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|TestClient
name|wsclient3
init|=
operator|new
name|TestClient
argument_list|(
literal|"ws://localhost:"
operator|+
name|PORT
operator|+
literal|"/hola3"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|wsclient1
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient1
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|wsclient2
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|wsclient3
operator|.
name|connect
argument_list|()
expr_stmt|;
name|wsclient3
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
comment|//all connections were registered in external store
name|assertTrue
argument_list|(
name|connectionKeyUserMap
operator|.
name|size
argument_list|()
operator|==
name|EXISTED_USERS
operator|.
name|length
argument_list|)
expr_stmt|;
name|wsclient2
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsclient2
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|broadcastMessageTo
operator|=
operator|new
name|String
index|[]
block|{
name|EXISTED_USERS
index|[
literal|0
index|]
block|,
name|EXISTED_USERS
index|[
literal|1
index|]
block|}
expr_stmt|;
name|wsclient1
operator|.
name|sendTextMessage
argument_list|(
literal|"Gambas"
argument_list|)
expr_stmt|;
name|wsclient1
operator|.
name|await
argument_list|(
name|awaitTime
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received1
init|=
name|wsclient1
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|element
range|:
name|broadcastMessageTo
control|)
block|{
name|assertTrue
argument_list|(
name|received1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|contains
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|received2
init|=
name|wsclient2
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|received2
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|received3
init|=
name|wsclient3
operator|.
name|getReceived
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|received3
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|wsclient1
operator|.
name|close
argument_list|()
expr_stmt|;
name|wsclient3
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|// START SNIPPET: payload
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
comment|// route for events resending enabled
name|from
argument_list|(
literal|"atmosphere-websocket:///hola"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|checkEventsResendingEnabled
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// route for events resending enabled
name|from
argument_list|(
literal|"atmosphere-websocket:///hola1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|checkPassedParameters
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// route for single client broadcast to multiple clients
name|from
argument_list|(
literal|"atmosphere-websocket:///hola2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONCLOSE_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|removeExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONERROR_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|removeExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createBroadcastMultipleClientsResponse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola2"
argument_list|)
expr_stmt|;
comment|// route for single client broadcast to multiple clients guarantee delivery
name|from
argument_list|(
literal|"atmosphere-websocket:///hola3"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:info"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONCLOSE_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|removeExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|ONERROR_EVENT_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|removeExternalConnectionRegister
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|WebsocketConstants
operator|.
name|ERROR_TYPE
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|WebsocketConstants
operator|.
name|MESSAGE_NOT_SENT_ERROR_TYPE
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|handleNotDeliveredMessage
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|createBroadcastMultipleClientsResponse
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"atmosphere-websocket:///hola3"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|handleNotDeliveredMessage (Exchange exchange)
specifier|private
specifier|static
name|void
name|handleNotDeliveredMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|connectionKeyList
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY_LIST
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|connectionKeyList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|connectionKeyList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|connectionKeyUserMap
operator|.
name|get
argument_list|(
name|broadcastMessageTo
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createExternalConnectionRegister (Exchange exchange)
specifier|private
specifier|static
name|void
name|createExternalConnectionRegister
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|connectionKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|)
decl_stmt|;
name|String
name|userName
init|=
name|EXISTED_USERS
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|connectionKeyUserMap
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|userName
operator|=
name|EXISTED_USERS
index|[
name|connectionKeyUserMap
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
block|}
name|connectionKeyUserMap
operator|.
name|put
argument_list|(
name|userName
argument_list|,
operator|(
name|String
operator|)
name|connectionKey
argument_list|)
expr_stmt|;
block|}
DECL|method|removeExternalConnectionRegister (Exchange exchange)
specifier|private
specifier|static
name|void
name|removeExternalConnectionRegister
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// remove connectionKey from external store
block|}
DECL|method|createBroadcastMultipleClientsResponse (Exchange exchange)
specifier|private
specifier|static
name|void
name|createBroadcastMultipleClientsResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|connectionKeyList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Object
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|String
name|additionalMessage
init|=
literal|""
decl_stmt|;
comment|//send the message only to selected connections
for|for
control|(
name|String
name|element
range|:
name|broadcastMessageTo
control|)
block|{
name|connectionKeyList
operator|.
name|add
argument_list|(
name|connectionKeyUserMap
operator|.
name|get
argument_list|(
name|element
argument_list|)
argument_list|)
expr_stmt|;
name|additionalMessage
operator|+=
name|element
operator|+
literal|" "
expr_stmt|;
block|}
name|additionalMessage
operator|+=
literal|" Received the message: "
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|additionalMessage
operator|+
name|msg
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY_LIST
argument_list|,
name|connectionKeyList
argument_list|)
expr_stmt|;
block|}
DECL|method|checkEventsResendingEnabled (Exchange exchange)
specifier|private
specifier|static
name|void
name|checkEventsResendingEnabled
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|connectionKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|)
decl_stmt|;
name|Object
name|eventType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
decl_stmt|;
name|Object
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connectionKey
operator|!=
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|eventType
operator|instanceof
name|Integer
condition|)
block|{
name|assertTrue
argument_list|(
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
operator|||
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONCLOSE_EVENT_TYPE
argument_list|)
operator|||
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONERROR_EVENT_TYPE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|checkPassedParameters (Exchange exchange)
specifier|private
specifier|static
name|void
name|checkPassedParameters
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|connectionKey
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|)
decl_stmt|;
name|Object
name|eventType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|EVENT_TYPE
argument_list|)
decl_stmt|;
name|Object
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|msg
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|connectionKey
operator|!=
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
operator|(
name|eventType
operator|instanceof
name|Integer
operator|)
operator|&&
name|eventType
operator|.
name|equals
argument_list|(
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
condition|)
block|{
name|String
name|param1
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"param1"
argument_list|)
decl_stmt|;
name|String
name|param2
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"param2"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|param1
operator|.
name|equals
argument_list|(
literal|"value1"
argument_list|)
operator|&&
name|param2
operator|.
name|equals
argument_list|(
literal|"value2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: payload
block|}
end_class

end_unit

