begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq.reply
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
operator|.
name|reply
package|;
end_package

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|AMQP
import|;
end_import

begin_comment
comment|/**  * Handles a reply.  */
end_comment

begin_interface
DECL|interface|ReplyHandler
specifier|public
interface|interface
name|ReplyHandler
block|{
comment|/**      * The reply message was received      *      * @param correlationId  the correlation id      * @param reply  the reply message      */
DECL|method|onReply (String correlationId, AMQP.BasicProperties properties, byte[] reply)
name|void
name|onReply
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|,
name|byte
index|[]
name|reply
parameter_list|)
function_decl|;
comment|/**      * The reply message was not received and a timeout triggered      *      * @param correlationId  the correlation id      */
DECL|method|onTimeout (String correlationId)
name|void
name|onTimeout
parameter_list|(
name|String
name|correlationId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

