begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * {@link ReplyHandler} to handle processing replies when using temporary queues.  */
end_comment

begin_class
DECL|class|TemporaryQueueReplyHandler
specifier|public
class|class
name|TemporaryQueueReplyHandler
implements|implements
name|ReplyHandler
block|{
comment|// task queue to add the holder so we can process the reply
DECL|field|replyManager
specifier|protected
specifier|final
name|ReplyManager
name|replyManager
decl_stmt|;
DECL|field|exchange
specifier|protected
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|protected
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
comment|// remember the original correlation id, in case the server returns back a reply with a messed up correlation id
DECL|field|originalCorrelationId
specifier|protected
specifier|final
name|String
name|originalCorrelationId
decl_stmt|;
DECL|field|correlationId
specifier|protected
specifier|final
name|String
name|correlationId
decl_stmt|;
DECL|field|timeout
specifier|protected
specifier|final
name|long
name|timeout
decl_stmt|;
DECL|method|TemporaryQueueReplyHandler (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, String correlationId, long timeout)
specifier|public
name|TemporaryQueueReplyHandler
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
name|String
name|correlationId
parameter_list|,
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|replyManager
operator|=
name|replyManager
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|originalCorrelationId
operator|=
name|originalCorrelationId
expr_stmt|;
name|this
operator|.
name|correlationId
operator|=
name|correlationId
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onReply (String correlationId, Message reply, Session session)
specifier|public
name|void
name|onReply
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|Message
name|reply
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
comment|// create holder object with the reply
name|ReplyHolder
name|holder
init|=
operator|new
name|ReplyHolder
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|correlationId
argument_list|,
name|reply
argument_list|,
name|session
argument_list|)
decl_stmt|;
comment|// process the reply
name|replyManager
operator|.
name|processReply
argument_list|(
name|holder
argument_list|)
expr_stmt|;
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
comment|// create holder object without the reply which means a timeout occurred
name|ReplyHolder
name|holder
init|=
operator|new
name|ReplyHolder
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|correlationId
argument_list|,
name|timeout
argument_list|)
decl_stmt|;
comment|// process timeout
name|replyManager
operator|.
name|processReply
argument_list|(
name|holder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

