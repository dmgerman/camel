begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ahc.ws
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ahc
operator|.
name|ws
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClient
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpClientConfig
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|AsyncHttpProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|providers
operator|.
name|grizzly
operator|.
name|GrizzlyAsyncHttpProvider
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|ws
operator|.
name|DefaultWebSocketListener
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
operator|.
name|ws
operator|.
name|WebSocket
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ning
operator|.
name|http
operator|.
name|client
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
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Producer
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
name|ahc
operator|.
name|AhcEndpoint
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
comment|/**  * To exchange data with external Websocket servers using<a href="http://github.com/sonatype/async-http-client">Async Http Client</a>.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"ahc-ws,ahc-wss"
argument_list|,
name|extendsScheme
operator|=
literal|"ahc,ahc"
argument_list|,
name|title
operator|=
literal|"AHC Websocket,AHC Secure Websocket"
argument_list|,
name|syntax
operator|=
literal|"ahc-ws:httpUri"
argument_list|,
name|consumerClass
operator|=
name|WsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"websocket"
argument_list|)
DECL|class|WsEndpoint
specifier|public
class|class
name|WsEndpoint
extends|extends
name|AhcEndpoint
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
name|WsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// for using websocket streaming/fragments
DECL|field|GRIZZLY_AVAILABLE
specifier|private
specifier|static
specifier|final
name|boolean
name|GRIZZLY_AVAILABLE
init|=
name|probeClass
argument_list|(
literal|"com.ning.http.client.providers.grizzly.GrizzlyAsyncHttpProvider"
argument_list|)
decl_stmt|;
DECL|field|consumers
specifier|private
specifier|final
name|Set
argument_list|<
name|WsConsumer
argument_list|>
name|consumers
init|=
operator|new
name|HashSet
argument_list|<
name|WsConsumer
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|listener
specifier|private
specifier|final
name|WsListener
name|listener
init|=
operator|new
name|WsListener
argument_list|()
decl_stmt|;
DECL|field|websocket
specifier|private
specifier|transient
name|WebSocket
name|websocket
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|useStreaming
specifier|private
name|boolean
name|useStreaming
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|sendMessageOnError
specifier|private
name|boolean
name|sendMessageOnError
decl_stmt|;
DECL|method|WsEndpoint (String endpointUri, WsComponent component)
specifier|public
name|WsEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|WsComponent
name|component
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|WsComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|WsComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|WsProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|WsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|getWebSocket ()
name|WebSocket
name|getWebSocket
parameter_list|()
throws|throws
name|Exception
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
comment|// ensure we are connected
name|reConnect
argument_list|()
expr_stmt|;
block|}
return|return
name|websocket
return|;
block|}
DECL|method|setWebSocket (WebSocket websocket)
name|void
name|setWebSocket
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|this
operator|.
name|websocket
operator|=
name|websocket
expr_stmt|;
block|}
DECL|method|isUseStreaming ()
specifier|public
name|boolean
name|isUseStreaming
parameter_list|()
block|{
return|return
name|useStreaming
return|;
block|}
comment|/**      * To enable streaming to send data as multiple text fragments.      */
DECL|method|setUseStreaming (boolean useStreaming)
specifier|public
name|void
name|setUseStreaming
parameter_list|(
name|boolean
name|useStreaming
parameter_list|)
block|{
name|this
operator|.
name|useStreaming
operator|=
name|useStreaming
expr_stmt|;
block|}
DECL|method|isSendMessageOnError ()
specifier|public
name|boolean
name|isSendMessageOnError
parameter_list|()
block|{
return|return
name|sendMessageOnError
return|;
block|}
comment|/**      * Whether to send an message if the web-socket listener received an error.      */
DECL|method|setSendMessageOnError (boolean sendMessageOnError)
specifier|public
name|void
name|setSendMessageOnError
parameter_list|(
name|boolean
name|sendMessageOnError
parameter_list|)
block|{
name|this
operator|.
name|sendMessageOnError
operator|=
name|sendMessageOnError
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createClient (AsyncHttpClientConfig config)
specifier|protected
name|AsyncHttpClient
name|createClient
parameter_list|(
name|AsyncHttpClientConfig
name|config
parameter_list|)
block|{
name|AsyncHttpClient
name|client
decl_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
operator|new
name|AsyncHttpClientConfig
operator|.
name|Builder
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
name|AsyncHttpProvider
name|ahp
init|=
name|getAsyncHttpProvider
argument_list|(
name|config
argument_list|)
decl_stmt|;
if|if
condition|(
name|ahp
operator|==
literal|null
condition|)
block|{
name|client
operator|=
operator|new
name|AsyncHttpClient
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|client
operator|=
operator|new
name|AsyncHttpClient
argument_list|(
name|ahp
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
DECL|method|connect ()
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connecting to {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|websocket
operator|=
name|getClient
argument_list|()
operator|.
name|prepareGet
argument_list|(
name|uri
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
name|listener
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|websocket
operator|!=
literal|null
operator|&&
name|websocket
operator|.
name|isOpen
argument_list|()
condition|)
block|{
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
literal|"Disconnecting from {}"
argument_list|,
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|websocket
operator|.
name|removeWebSocketListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|websocket
operator|.
name|close
argument_list|()
expr_stmt|;
name|websocket
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|connect (WsConsumer wsConsumer)
name|void
name|connect
parameter_list|(
name|WsConsumer
name|wsConsumer
parameter_list|)
throws|throws
name|Exception
block|{
name|consumers
operator|.
name|add
argument_list|(
name|wsConsumer
argument_list|)
expr_stmt|;
name|reConnect
argument_list|()
expr_stmt|;
block|}
DECL|method|disconnect (WsConsumer wsConsumer)
name|void
name|disconnect
parameter_list|(
name|WsConsumer
name|wsConsumer
parameter_list|)
block|{
name|consumers
operator|.
name|remove
argument_list|(
name|wsConsumer
argument_list|)
expr_stmt|;
block|}
DECL|method|reConnect ()
name|void
name|reConnect
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|websocket
operator|==
literal|null
operator|||
operator|!
name|websocket
operator|.
name|isOpen
argument_list|()
condition|)
block|{
name|String
name|uri
init|=
name|getHttpUri
argument_list|()
operator|.
name|toASCIIString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Reconnecting websocket: {}"
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|connect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|WsListener
class|class
name|WsListener
extends|extends
name|DefaultWebSocketListener
block|{
annotation|@
name|Override
DECL|method|onOpen (WebSocket websocket)
specifier|public
name|void
name|onOpen
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Websocket opened"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onClose (WebSocket websocket)
specifier|public
name|void
name|onClose
parameter_list|(
name|WebSocket
name|websocket
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"websocket closed - reconnecting"
argument_list|)
expr_stmt|;
try|try
block|{
name|reConnect
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error re-connecting to websocket"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onError (Throwable t)
specifier|public
name|void
name|onError
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"websocket on error"
argument_list|,
name|t
argument_list|)
expr_stmt|;
if|if
condition|(
name|isSendMessageOnError
argument_list|()
condition|)
block|{
for|for
control|(
name|WsConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|sendMessage
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|onMessage (byte[] message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received message --> {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
for|for
control|(
name|WsConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Received message --> {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
for|for
control|(
name|WsConsumer
name|consumer
range|:
name|consumers
control|)
block|{
name|consumer
operator|.
name|sendMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getAsyncHttpProvider (AsyncHttpClientConfig config)
specifier|protected
name|AsyncHttpProvider
name|getAsyncHttpProvider
parameter_list|(
name|AsyncHttpClientConfig
name|config
parameter_list|)
block|{
if|if
condition|(
name|GRIZZLY_AVAILABLE
condition|)
block|{
return|return
operator|new
name|GrizzlyAsyncHttpProvider
argument_list|(
name|config
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|probeClass (String name)
specifier|private
specifier|static
name|boolean
name|probeClass
parameter_list|(
name|String
name|name
parameter_list|)
block|{
try|try
block|{
name|Class
operator|.
name|forName
argument_list|(
name|name
argument_list|,
literal|true
argument_list|,
name|WsEndpoint
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

