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
name|net
operator|.
name|URI
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
name|component
operator|.
name|servlet
operator|.
name|ServletComponent
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

begin_comment
comment|/**  * To exchange data with external Websocket clients using Atmosphere  */
end_comment

begin_class
DECL|class|WebsocketComponent
specifier|public
class|class
name|WebsocketComponent
extends|extends
name|ServletComponent
block|{
DECL|field|stores
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|WebSocketStore
argument_list|>
name|stores
decl_stmt|;
DECL|method|WebsocketComponent ()
specifier|public
name|WebsocketComponent
parameter_list|()
block|{
comment|// override the default servlet name of ServletComponent
name|super
argument_list|(
name|WebsocketEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|setServletName
argument_list|(
literal|"CamelWsServlet"
argument_list|)
expr_stmt|;
name|this
operator|.
name|stores
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createServletEndpoint (String endpointUri, ServletComponent component, URI httpUri)
specifier|protected
name|ServletEndpoint
name|createServletEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|ServletComponent
name|component
parameter_list|,
name|URI
name|httpUri
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|WebsocketEndpoint
argument_list|(
name|endpointUri
argument_list|,
operator|(
name|WebsocketComponent
operator|)
name|component
argument_list|,
name|httpUri
argument_list|)
return|;
block|}
DECL|method|getWebSocketStore (String name)
name|WebSocketStore
name|getWebSocketStore
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|WebSocketStore
name|store
decl_stmt|;
synchronized|synchronized
init|(
name|stores
init|)
block|{
name|store
operator|=
name|stores
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
name|store
operator|==
literal|null
condition|)
block|{
name|store
operator|=
operator|new
name|MemoryWebSocketStore
argument_list|()
expr_stmt|;
name|stores
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|store
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|store
return|;
block|}
block|}
end_class

end_unit

