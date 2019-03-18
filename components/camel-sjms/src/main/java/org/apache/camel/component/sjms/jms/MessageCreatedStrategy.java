begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.jms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
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

begin_comment
comment|/**  * A strategy that allows custom components to plugin and perform custom logic when Camel creates {@link javax.jms.Message} instance.  *<p/>  * For example to populate the message with custom information that are component specific and not part of the JMS specification.  */
end_comment

begin_interface
DECL|interface|MessageCreatedStrategy
specifier|public
interface|interface
name|MessageCreatedStrategy
block|{
comment|/**      * Callback when the JMS message has<i>just</i> been created, which allows custom modifications afterwards.      *      * @param exchange the current exchange      * @param session the JMS session used to create the message      * @param cause optional exception occurred that should be sent as reply instead of a regular body      */
DECL|method|onMessageCreated (Message message, Session session, Exchange exchange, Throwable cause)
name|void
name|onMessageCreated
parameter_list|(
name|Message
name|message
parameter_list|,
name|Session
name|session
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|cause
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

