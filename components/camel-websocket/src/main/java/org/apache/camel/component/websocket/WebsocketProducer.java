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
name|IOException
import|;
end_import

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
name|CamelExchangeException
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
name|Message
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|WebsocketProducer
specifier|public
class|class
name|WebsocketProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|store
specifier|private
specifier|final
name|WebsocketStore
name|store
decl_stmt|;
DECL|field|sendToAll
specifier|private
specifier|final
name|Boolean
name|sendToAll
decl_stmt|;
DECL|method|WebsocketProducer (WebsocketEndpoint endpoint, WebsocketStore store)
specifier|public
name|WebsocketProducer
parameter_list|(
name|WebsocketEndpoint
name|endpoint
parameter_list|,
name|WebsocketStore
name|store
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|store
operator|=
name|store
expr_stmt|;
name|this
operator|.
name|sendToAll
operator|=
name|endpoint
operator|.
name|getSendToAll
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Configured with "
operator|+
name|sendToAll
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|message
init|=
name|in
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|isSendToAllSet
argument_list|(
name|in
argument_list|)
condition|)
block|{
name|sendToAll
argument_list|(
name|store
argument_list|,
name|message
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// look for connection key and get Websocket
name|String
name|connectionKey
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|CONNECTION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectionKey
operator|!=
literal|null
condition|)
block|{
name|DefaultWebsocket
name|websocket
init|=
name|store
operator|.
name|get
argument_list|(
name|connectionKey
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to connection key {} -> {}"
argument_list|,
name|connectionKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
name|websocket
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to send message to single connection; connetion key not set."
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|isSendToAllSet (Message in)
name|boolean
name|isSendToAllSet
parameter_list|(
name|Message
name|in
parameter_list|)
block|{
comment|// header may be null; have to be careful here (and fallback to use sendToAll option configured from endpoint)
name|Boolean
name|value
init|=
name|in
operator|.
name|getHeader
argument_list|(
name|WebsocketConstants
operator|.
name|SEND_TO_ALL
argument_list|,
name|sendToAll
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|value
operator|==
literal|null
condition|?
literal|false
else|:
name|value
return|;
block|}
DECL|method|sendToAll (WebsocketStore store, String message, Exchange exchange)
name|void
name|sendToAll
parameter_list|(
name|WebsocketStore
name|store
parameter_list|,
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Sending to all {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|DefaultWebsocket
argument_list|>
name|websockets
init|=
name|store
operator|.
name|getAll
argument_list|()
decl_stmt|;
name|Exception
name|exception
init|=
literal|null
decl_stmt|;
for|for
control|(
name|DefaultWebsocket
name|websocket
range|:
name|websockets
control|)
block|{
try|try
block|{
name|sendMessage
argument_list|(
name|websocket
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
if|if
condition|(
name|exception
operator|==
literal|null
condition|)
block|{
name|exception
operator|=
operator|new
name|CamelExchangeException
argument_list|(
literal|"Failed to deliver message to one or more recipients."
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|exception
operator|!=
literal|null
condition|)
block|{
throw|throw
name|exception
throw|;
block|}
block|}
DECL|method|sendMessage (DefaultWebsocket websocket, String message)
name|void
name|sendMessage
parameter_list|(
name|DefaultWebsocket
name|websocket
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|IOException
block|{
comment|// in case there is web socket and socket connection is open - send message
if|if
condition|(
name|websocket
operator|!=
literal|null
operator|&&
name|websocket
operator|.
name|getConnection
argument_list|()
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Sending to websocket {} -> {}"
argument_list|,
name|websocket
operator|.
name|getConnectionKey
argument_list|()
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|getConnection
argument_list|()
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

