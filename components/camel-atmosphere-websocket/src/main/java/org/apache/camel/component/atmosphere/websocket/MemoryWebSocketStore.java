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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ConcurrentHashMap
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|MemoryWebSocketStore
specifier|public
class|class
name|MemoryWebSocketStore
implements|implements
name|WebSocketStore
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
name|MemoryWebSocketStore
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|values
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocket
argument_list|>
name|values
decl_stmt|;
DECL|field|keys
specifier|private
name|Map
argument_list|<
name|WebSocket
argument_list|,
name|String
argument_list|>
name|keys
decl_stmt|;
DECL|method|MemoryWebSocketStore ()
specifier|public
name|MemoryWebSocketStore
parameter_list|()
block|{
name|values
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|keys
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.Service#start()      */
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{     }
comment|/* (non-Javadoc)      * @see org.apache.camel.Service#stop()      */
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|values
operator|.
name|clear
argument_list|()
expr_stmt|;
name|keys
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#addWebSocket(java.lang.String, org.atmosphere.websocket.WebSocket)      */
annotation|@
name|Override
DECL|method|addWebSocket (String connectionKey, WebSocket websocket)
specifier|public
name|void
name|addWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|,
name|WebSocket
name|websocket
parameter_list|)
block|{
name|values
operator|.
name|put
argument_list|(
name|connectionKey
argument_list|,
name|websocket
argument_list|)
expr_stmt|;
name|keys
operator|.
name|put
argument_list|(
name|websocket
argument_list|,
name|connectionKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"added websocket {} => {}"
argument_list|,
name|connectionKey
argument_list|,
name|websocket
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#removeWebSocket(java.lang.String)      */
annotation|@
name|Override
DECL|method|removeWebSocket (String connectionKey)
specifier|public
name|void
name|removeWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
block|{
name|Object
name|obj
init|=
name|values
operator|.
name|remove
argument_list|(
name|connectionKey
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|keys
operator|.
name|remove
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"removed websocket {}"
argument_list|,
name|connectionKey
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#removeWebSocket(org.atmosphere.websocket.WebSocket)      */
annotation|@
name|Override
DECL|method|removeWebSocket (WebSocket websocket)
specifier|public
name|void
name|removeWebSocket
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|Object
name|obj
init|=
name|keys
operator|.
name|remove
argument_list|(
name|websocket
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|!=
literal|null
condition|)
block|{
name|values
operator|.
name|remove
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"removed websocket {}"
argument_list|,
name|websocket
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#getConnectionKey(org.atmosphere.websocket.WebSocket)      */
annotation|@
name|Override
DECL|method|getConnectionKey (WebSocket websocket)
specifier|public
name|String
name|getConnectionKey
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
return|return
name|keys
operator|.
name|get
argument_list|(
name|websocket
argument_list|)
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#getWebSocket(java.lang.String)      */
annotation|@
name|Override
DECL|method|getWebSocket (String connectionKey)
specifier|public
name|WebSocket
name|getWebSocket
parameter_list|(
name|String
name|connectionKey
parameter_list|)
block|{
return|return
name|values
operator|.
name|get
argument_list|(
name|connectionKey
argument_list|)
return|;
block|}
comment|/* (non-Javadoc)      * @see org.apache.camel.component.websocket2.WebsocketStore#getAllWebSockets()      */
annotation|@
name|Override
DECL|method|getAllWebSockets ()
specifier|public
name|Collection
argument_list|<
name|WebSocket
argument_list|>
name|getAllWebSockets
parameter_list|()
block|{
return|return
name|values
operator|.
name|values
argument_list|()
return|;
block|}
block|}
end_class

end_unit

