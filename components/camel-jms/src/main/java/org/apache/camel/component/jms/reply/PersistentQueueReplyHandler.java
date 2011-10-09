begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.reply
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
operator|.
name|reply
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncCallback
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
comment|/**  * {@link ReplyHandler} to handle processing replies when using persistent queues.  *  * @version   */
end_comment

begin_class
DECL|class|PersistentQueueReplyHandler
specifier|public
class|class
name|PersistentQueueReplyHandler
extends|extends
name|TemporaryQueueReplyHandler
block|{
DECL|field|dynamicMessageSelector
specifier|private
name|MessageSelectorCreator
name|dynamicMessageSelector
decl_stmt|;
DECL|method|PersistentQueueReplyHandler (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, long timeout, MessageSelectorCreator dynamicMessageSelector)
specifier|public
name|PersistentQueueReplyHandler
parameter_list|(
name|ReplyManager
name|replyManager
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|String
name|originalCorrelationId
parameter_list|,
name|long
name|timeout
parameter_list|,
name|MessageSelectorCreator
name|dynamicMessageSelector
parameter_list|)
block|{
name|super
argument_list|(
name|replyManager
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|this
operator|.
name|dynamicMessageSelector
operator|=
name|dynamicMessageSelector
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onReply (String correlationId, Message reply)
specifier|public
name|void
name|onReply
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|Message
name|reply
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|dynamicMessageSelector
operator|!=
literal|null
condition|)
block|{
comment|// remove correlation id from message selector
name|dynamicMessageSelector
operator|.
name|removeCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|onReply
argument_list|(
name|correlationId
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onTimeout (String correlationId)
specifier|public
name|void
name|onTimeout
parameter_list|(
name|String
name|correlationId
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|dynamicMessageSelector
operator|!=
literal|null
condition|)
block|{
comment|// remove correlation id from message selector
name|dynamicMessageSelector
operator|.
name|removeCorrelationID
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|super
operator|.
name|onTimeout
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

