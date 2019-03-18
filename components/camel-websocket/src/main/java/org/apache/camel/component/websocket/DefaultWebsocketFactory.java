begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|websocket
operator|.
name|servlet
operator|.
name|ServletUpgradeRequest
import|;
end_import

begin_comment
comment|/**  * Default websocket factory.  * Used when no custom websocket is needed.  */
end_comment

begin_class
DECL|class|DefaultWebsocketFactory
specifier|public
class|class
name|DefaultWebsocketFactory
implements|implements
name|WebSocketFactory
block|{
annotation|@
name|Override
DECL|method|newInstance (ServletUpgradeRequest request, String protocol, String pathSpec, NodeSynchronization sync, WebsocketConsumer consumer)
specifier|public
name|DefaultWebsocket
name|newInstance
parameter_list|(
name|ServletUpgradeRequest
name|request
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|pathSpec
parameter_list|,
name|NodeSynchronization
name|sync
parameter_list|,
name|WebsocketConsumer
name|consumer
parameter_list|)
block|{
return|return
operator|new
name|DefaultWebsocket
argument_list|(
name|sync
argument_list|,
name|pathSpec
argument_list|,
name|consumer
argument_list|)
return|;
block|}
block|}
end_class

end_unit

