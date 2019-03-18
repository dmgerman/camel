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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|rabbitmq
operator|.
name|RabbitMQEndpoint
import|;
end_import

begin_comment
comment|/**  * The {@link ReplyManager} is responsible for handling<a href="http://camel.apache.org/request-reply.html">request-reply</a>  * over RabbitMQ.  */
end_comment

begin_interface
DECL|interface|ReplyManager
specifier|public
interface|interface
name|ReplyManager
block|{
comment|/**      * Sets the belonging {@link RabbitMQEndpoint}      */
DECL|method|setEndpoint (RabbitMQEndpoint endpoint)
name|void
name|setEndpoint
parameter_list|(
name|RabbitMQEndpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Sets the reply to queue the manager should listen for replies.      *<p/>      * The queue is either a temporary or a persistent queue.      */
DECL|method|setReplyTo (String replyTo)
name|void
name|setReplyTo
parameter_list|(
name|String
name|replyTo
parameter_list|)
function_decl|;
comment|/**      * Gets the reply to queue being used      */
DECL|method|getReplyTo ()
name|String
name|getReplyTo
parameter_list|()
function_decl|;
comment|/**      * Register a reply      *      * @param replyManager    the reply manager being used      * @param exchange        the exchange      * @param callback        the callback      * @param originalCorrelationId  an optional original correlation id      * @param correlationId   the correlation id to expect being used      * @param requestTimeout  the timeout      * @return the correlation id used      */
DECL|method|registerReply (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, String correlationId, long requestTimeout)
name|String
name|registerReply
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
name|requestTimeout
parameter_list|)
function_decl|;
comment|/**      * Sets the scheduled to use when checking for timeouts (no reply received within a given time period)      */
DECL|method|setScheduledExecutorService (ScheduledExecutorService executorService)
name|void
name|setScheduledExecutorService
parameter_list|(
name|ScheduledExecutorService
name|executorService
parameter_list|)
function_decl|;
comment|/**      * Updates the correlation id to the new correlation id.      *<p/>      * This is only used when<tt>useMessageIDasCorrelationID</tt> option is used, which means a      * provisional correlation id is first used, then after the message has been sent, the real      * correlation id is known. This allows us then to update the internal mapping to expect the      * real correlation id.      *      * @param correlationId     the provisional correlation id      * @param newCorrelationId  the real correlation id      * @param requestTimeout    the timeout      */
DECL|method|updateCorrelationId (String correlationId, String newCorrelationId, long requestTimeout)
name|void
name|updateCorrelationId
parameter_list|(
name|String
name|correlationId
parameter_list|,
name|String
name|newCorrelationId
parameter_list|,
name|long
name|requestTimeout
parameter_list|)
function_decl|;
comment|/**      * Process the reply      *      * @param holder  containing needed data to process the reply and continue routing      */
DECL|method|processReply (ReplyHolder holder)
name|void
name|processReply
parameter_list|(
name|ReplyHolder
name|holder
parameter_list|)
function_decl|;
comment|/**      * Unregister a correlationId when you no longer need a reply      */
DECL|method|cancelCorrelationId (String correlationId)
name|void
name|cancelCorrelationId
parameter_list|(
name|String
name|correlationId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

