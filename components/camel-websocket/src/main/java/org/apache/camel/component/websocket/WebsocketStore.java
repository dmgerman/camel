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

begin_interface
DECL|interface|WebsocketStore
specifier|public
interface|interface
name|WebsocketStore
extends|extends
name|Service
block|{
DECL|method|add (DefaultWebsocket ws)
name|void
name|add
parameter_list|(
name|DefaultWebsocket
name|ws
parameter_list|)
function_decl|;
DECL|method|remove (DefaultWebsocket ws)
name|void
name|remove
parameter_list|(
name|DefaultWebsocket
name|ws
parameter_list|)
function_decl|;
DECL|method|remove (String key)
name|void
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
DECL|method|get (String key)
name|DefaultWebsocket
name|get
parameter_list|(
name|String
name|key
parameter_list|)
function_decl|;
DECL|method|getAll ()
name|Collection
argument_list|<
name|DefaultWebsocket
argument_list|>
name|getAll
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

