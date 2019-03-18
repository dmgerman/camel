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
name|Collection
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
name|Service
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

begin_comment
comment|/**  *  */
end_comment

begin_interface
DECL|interface|WebSocketStore
specifier|public
interface|interface
name|WebSocketStore
extends|extends
name|Service
block|{
DECL|method|addWebSocket (String connectionKey, WebSocket websocket)
name|void
name|addWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|,
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|removeWebSocket (String connectionKey)
name|void
name|removeWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
function_decl|;
DECL|method|removeWebSocket (WebSocket websocket)
name|void
name|removeWebSocket
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|getConnectionKey (WebSocket websocket)
name|String
name|getConnectionKey
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
function_decl|;
DECL|method|getWebSocket (String connectionKey)
name|WebSocket
name|getWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
function_decl|;
DECL|method|getAllWebSockets ()
name|Collection
argument_list|<
name|WebSocket
argument_list|>
name|getAllWebSockets
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

