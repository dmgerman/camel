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

begin_interface
DECL|interface|NodeSynchronization
specifier|public
interface|interface
name|NodeSynchronization
block|{
comment|/**      * Adds the web socket to both (always if present) stores.      *      * @param socket the web socket      */
DECL|method|addSocket (DefaultWebsocket socket)
name|void
name|addSocket
parameter_list|(
name|DefaultWebsocket
name|socket
parameter_list|)
function_decl|;
comment|/**      * Removes the Websocket from both stores      *      * @param id id of the web socket      */
DECL|method|removeSocket (String id)
name|void
name|removeSocket
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Removes the Websocket from both stores      *      * @param socket web socket to remove      */
DECL|method|removeSocket (DefaultWebsocket socket)
name|void
name|removeSocket
parameter_list|(
name|DefaultWebsocket
name|socket
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

