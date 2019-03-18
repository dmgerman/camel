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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|servlet
operator|.
name|ServletEndpoint
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
name|Metadata
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * To exchange data with external Websocket clients using Atmosphere.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"atmosphere-websocket"
argument_list|,
name|extendsScheme
operator|=
literal|"servlet"
argument_list|,
name|title
operator|=
literal|"Atmosphere Websocket"
argument_list|,
name|syntax
operator|=
literal|"atmosphere-websocket:servicePath"
argument_list|,
name|label
operator|=
literal|"websocket"
argument_list|,
name|excludeProperties
operator|=
literal|"httpUri,contextPath,authMethod,authMethodPriority,authUsername,authPassword,authDomain,authHost"
operator|+
literal|"proxyAuthScheme,proxyAuthMethod,proxyAuthUsername,proxyAuthPassword,proxyAuthHost,proxyAuthPort,proxyAuthDomain"
argument_list|)
DECL|class|WebsocketEndpoint
specifier|public
class|class
name|WebsocketEndpoint
extends|extends
name|ServletEndpoint
block|{
DECL|field|store
specifier|private
name|WebSocketStore
name|store
decl_stmt|;
DECL|field|websocketConsumer
specifier|private
name|WebsocketConsumer
name|websocketConsumer
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of websocket endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|servicePath
specifier|private
name|String
name|servicePath
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sendToAll
specifier|private
name|boolean
name|sendToAll
decl_stmt|;
annotation|@
name|UriParam
DECL|field|useStreaming
specifier|private
name|boolean
name|useStreaming
decl_stmt|;
DECL|method|WebsocketEndpoint (String endPointURI, WebsocketComponent component, URI httpUri)
specifier|public
name|WebsocketEndpoint
parameter_list|(
name|String
name|endPointURI
parameter_list|,
name|WebsocketComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endPointURI
argument_list|,
name|component
argument_list|,
name|httpUri
argument_list|)
expr_stmt|;
comment|//TODO find a better way of assigning the store
name|int
name|idx
init|=
name|endPointURI
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|idx
operator|>
operator|-
literal|1
condition|?
name|endPointURI
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
else|:
name|endPointURI
decl_stmt|;
name|this
operator|.
name|servicePath
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|store
operator|=
name|component
operator|.
name|getWebSocketStore
argument_list|(
name|servicePath
argument_list|)
expr_stmt|;
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
name|WebsocketProducer
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
name|websocketConsumer
operator|=
operator|new
name|WebsocketConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return
name|websocketConsumer
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|isSendToAll ()
specifier|public
name|boolean
name|isSendToAll
parameter_list|()
block|{
return|return
name|sendToAll
return|;
block|}
comment|/**      * Whether to send to all (broadcast) or send to a single receiver.      */
DECL|method|setSendToAll (boolean sendToAll)
specifier|public
name|void
name|setSendToAll
parameter_list|(
name|boolean
name|sendToAll
parameter_list|)
block|{
name|this
operator|.
name|sendToAll
operator|=
name|sendToAll
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
DECL|method|getWebSocketStore ()
name|WebSocketStore
name|getWebSocketStore
parameter_list|()
block|{
return|return
name|store
return|;
block|}
DECL|method|getWebsocketConsumer ()
specifier|public
name|WebsocketConsumer
name|getWebsocketConsumer
parameter_list|()
block|{
return|return
name|websocketConsumer
return|;
block|}
block|}
end_class

end_unit

