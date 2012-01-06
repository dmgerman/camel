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
name|Serializable
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
name|eclipse
operator|.
name|jetty
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
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|WebSocket
operator|.
name|OnTextMessage
import|;
end_import

begin_class
DECL|class|DefaultWebsocket
specifier|public
class|class
name|DefaultWebsocket
implements|implements
name|WebSocket
implements|,
name|OnTextMessage
implements|,
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|575701599776801400L
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|connectionKey
specifier|private
name|String
name|connectionKey
decl_stmt|;
DECL|field|sync
specifier|private
name|NodeSynchronization
name|sync
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|transient
name|WebsocketConsumer
name|consumer
decl_stmt|;
DECL|method|DefaultWebsocket (NodeSynchronization sync, WebsocketConsumer consumer)
specifier|public
name|DefaultWebsocket
parameter_list|(
name|NodeSynchronization
name|sync
parameter_list|,
name|WebsocketConsumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClose (int closeCode, String message)
specifier|public
name|void
name|onClose
parameter_list|(
name|int
name|closeCode
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|sync
operator|.
name|removeSocket
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onOpen (Connection connection)
specifier|public
name|void
name|onOpen
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
name|this
operator|.
name|connectionKey
operator|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
name|sync
operator|.
name|addSocket
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onMessage (String message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|String
name|message
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|consumer
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|consumer
operator|.
name|sendExchange
argument_list|(
name|this
operator|.
name|connectionKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
comment|// consumer is not set, this is produce only websocket
comment|// TODO - 06.06.2011, LK - deliver exchange to dead letter channel
block|}
comment|// getters and setters
DECL|method|getConnection ()
specifier|public
name|Connection
name|getConnection
parameter_list|()
block|{
return|return
name|connection
return|;
block|}
DECL|method|setConnection (Connection connection)
specifier|public
name|void
name|setConnection
parameter_list|(
name|Connection
name|connection
parameter_list|)
block|{
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
block|}
DECL|method|getConnectionKey ()
specifier|public
name|String
name|getConnectionKey
parameter_list|()
block|{
return|return
name|connectionKey
return|;
block|}
DECL|method|setConnectionKey (String connectionKey)
specifier|public
name|void
name|setConnectionKey
parameter_list|(
name|String
name|connectionKey
parameter_list|)
block|{
name|this
operator|.
name|connectionKey
operator|=
name|connectionKey
expr_stmt|;
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
block|}
block|}
end_class

end_unit

