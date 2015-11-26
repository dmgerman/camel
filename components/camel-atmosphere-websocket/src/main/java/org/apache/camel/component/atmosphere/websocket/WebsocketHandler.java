begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|cpr
operator|.
name|AtmosphereRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocketProcessor
operator|.
name|WebSocketException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|atmosphere
operator|.
name|websocket
operator|.
name|WebSocketProtocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|WebsocketHandler
specifier|public
class|class
name|WebsocketHandler
implements|implements
name|WebSocketProtocol
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|WebsocketHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|protected
name|WebsocketConsumer
name|consumer
decl_stmt|;
DECL|field|store
specifier|protected
name|WebSocketStore
name|store
decl_stmt|;
annotation|@
name|Override
DECL|method|configure (AtmosphereConfig config)
specifier|public
name|void
name|configure
parameter_list|(
name|AtmosphereConfig
name|config
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onClose (WebSocket webSocket)
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"closing websocket"
argument_list|)
expr_stmt|;
name|String
name|connectionKey
init|=
name|store
operator|.
name|getConnectionKey
argument_list|(
name|webSocket
argument_list|)
decl_stmt|;
name|sendEventNotification
argument_list|(
name|connectionKey
argument_list|,
name|WebsocketConstants
operator|.
name|ONCLOSE_EVENT_TYPE
argument_list|)
expr_stmt|;
name|store
operator|.
name|removeWebSocket
argument_list|(
name|webSocket
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"websocket closed"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onError (WebSocket webSocket, WebSocketException t)
specifier|public
name|void
name|onError
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|WebSocketException
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|error
argument_list|(
literal|"websocket on error"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|String
name|connectionKey
init|=
name|store
operator|.
name|getConnectionKey
argument_list|(
name|webSocket
argument_list|)
decl_stmt|;
name|sendEventNotification
argument_list|(
name|connectionKey
argument_list|,
name|WebsocketConstants
operator|.
name|ONERROR_EVENT_TYPE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onOpen (WebSocket webSocket)
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"opening websocket"
argument_list|)
expr_stmt|;
name|String
name|connectionKey
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|store
operator|.
name|addWebSocket
argument_list|(
name|connectionKey
argument_list|,
name|webSocket
argument_list|)
expr_stmt|;
name|sendEventNotification
argument_list|(
name|connectionKey
argument_list|,
name|WebsocketConstants
operator|.
name|ONOPEN_EVENT_TYPE
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"websocket opened"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMessage (WebSocket webSocket, String data)
specifier|public
name|List
argument_list|<
name|AtmosphereRequest
argument_list|>
name|onMessage
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|String
name|data
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processing text message {}"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|String
name|connectionKey
init|=
name|store
operator|.
name|getConnectionKey
argument_list|(
name|webSocket
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|sendMessage
argument_list|(
name|connectionKey
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"text message sent"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|onMessage (WebSocket webSocket, byte[] data, int offset, int length)
specifier|public
name|List
argument_list|<
name|AtmosphereRequest
argument_list|>
name|onMessage
parameter_list|(
name|WebSocket
name|webSocket
parameter_list|,
name|byte
index|[]
name|data
parameter_list|,
name|int
name|offset
parameter_list|,
name|int
name|length
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"processing byte message {}"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|String
name|connectionKey
init|=
name|store
operator|.
name|getConnectionKey
argument_list|(
name|webSocket
argument_list|)
decl_stmt|;
if|if
condition|(
name|length
operator|<
name|data
operator|.
name|length
condition|)
block|{
comment|// create a copy that contains the relevant section as camel expects bytes without offset.
comment|// alternatively, we could pass a BAIS reading this byte array from the offset.
name|byte
index|[]
name|rawdata
init|=
name|data
decl_stmt|;
name|data
operator|=
operator|new
name|byte
index|[
name|length
index|]
expr_stmt|;
name|System
operator|.
name|arraycopy
argument_list|(
name|rawdata
argument_list|,
name|offset
argument_list|,
name|data
argument_list|,
literal|0
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
name|consumer
operator|.
name|sendMessage
argument_list|(
name|connectionKey
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"byte message sent"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
DECL|method|setConsumer (WebsocketConsumer consumer)
specifier|public
name|void
name|setConsumer
parameter_list|(
name|WebsocketConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|store
operator|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getWebSocketStore
argument_list|()
expr_stmt|;
block|}
DECL|method|sendEventNotification (final String connectionKey, final int eventType)
specifier|private
name|void
name|sendEventNotification
parameter_list|(
specifier|final
name|String
name|connectionKey
parameter_list|,
specifier|final
name|int
name|eventType
parameter_list|)
block|{
if|if
condition|(
name|consumer
operator|.
name|isEnableEventsResending
argument_list|()
condition|)
block|{
name|consumer
operator|.
name|sendEventNotification
argument_list|(
name|connectionKey
argument_list|,
name|eventType
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

