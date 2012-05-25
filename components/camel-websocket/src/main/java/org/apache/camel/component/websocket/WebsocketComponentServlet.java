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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
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
name|WebSocketServlet
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentMap
import|;
end_import

begin_class
DECL|class|WebsocketComponentServlet
specifier|public
class|class
name|WebsocketComponentServlet
extends|extends
name|WebSocketServlet
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|sync
specifier|private
specifier|final
name|NodeSynchronization
name|sync
decl_stmt|;
DECL|field|consumer
specifier|private
name|WebsocketConsumer
name|consumer
decl_stmt|;
DECL|field|consumers
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|WebsocketConsumer
argument_list|>
name|consumers
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|WebsocketConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|WebsocketComponentServlet (NodeSynchronization sync)
specifier|public
name|WebsocketComponentServlet
parameter_list|(
name|NodeSynchronization
name|sync
parameter_list|)
block|{
name|this
operator|.
name|sync
operator|=
name|sync
expr_stmt|;
block|}
DECL|method|getConsumer ()
specifier|public
name|WebsocketConsumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
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
block|}
DECL|method|connect (WebsocketConsumer consumer)
specifier|public
name|void
name|connect
parameter_list|(
name|WebsocketConsumer
name|consumer
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting consumer: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|consumer
operator|.
name|getPath
argument_list|()
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|disconnect (WebsocketConsumer consumer)
specifier|public
name|void
name|disconnect
parameter_list|(
name|WebsocketConsumer
name|consumer
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Disconnecting consumer: {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|consumer
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doWebSocketConnect (HttpServletRequest request, String protocol)
specifier|public
name|WebSocket
name|doWebSocketConnect
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|protocol
parameter_list|)
block|{
return|return
operator|new
name|DefaultWebsocket
argument_list|(
name|sync
argument_list|,
name|consumer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

