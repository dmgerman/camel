begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
package|;
end_package

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
name|telegram
operator|.
name|model
operator|.
name|OutgoingMessage
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
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
import|;
end_import

begin_comment
comment|/**  * Allows interacting with the Telegram server to exchange messages.  */
end_comment

begin_interface
DECL|interface|TelegramService
specifier|public
interface|interface
name|TelegramService
block|{
DECL|method|getUpdates (String authorizationToken, Long offset, Integer limit, Integer timeoutSeconds)
name|UpdateResult
name|getUpdates
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|Long
name|offset
parameter_list|,
name|Integer
name|limit
parameter_list|,
name|Integer
name|timeoutSeconds
parameter_list|)
function_decl|;
DECL|method|sendMessage (String authorizationToken, OutgoingMessage message)
name|Object
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingMessage
name|message
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

