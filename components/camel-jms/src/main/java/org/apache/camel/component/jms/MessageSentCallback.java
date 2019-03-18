begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_comment
comment|/**  * Callback when a {@link Message} has been sent.  */
end_comment

begin_interface
DECL|interface|MessageSentCallback
specifier|public
interface|interface
name|MessageSentCallback
block|{
comment|/**      * Callback when the message has been sent.      *      * @param session     the session      * @param message     the message      * @param destination the destination      */
DECL|method|sent (Session session, Message message, Destination destination)
name|void
name|sent
parameter_list|(
name|Session
name|session
parameter_list|,
name|Message
name|message
parameter_list|,
name|Destination
name|destination
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

