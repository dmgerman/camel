begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
import|;
end_import

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

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|AMQP
operator|.
name|Queue
operator|.
name|DeclareOk
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Envelope
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
name|CamelContext
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
comment|/**  * A {@link ReplyManager} when using temporary queues.  *  * @version   */
end_comment

begin_class
DECL|class|TemporaryQueueReplyManager
specifier|public
class|class
name|TemporaryQueueReplyManager
extends|extends
name|ReplyManagerSupport
block|{
DECL|field|consumer
specifier|private
name|RabbitConsumer
name|consumer
decl_stmt|;
DECL|method|TemporaryQueueReplyManager (CamelContext camelContext)
specifier|public
name|TemporaryQueueReplyManager
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
DECL|method|createReplyHandler (ReplyManager replyManager, Exchange exchange, AsyncCallback callback, String originalCorrelationId, String correlationId, long requestTimeout)
specifier|protected
name|ReplyHandler
name|createReplyHandler
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
block|{
return|return
operator|new
name|TemporaryQueueReplyHandler
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|originalCorrelationId
argument_list|,
name|correlationId
argument_list|,
name|requestTimeout
argument_list|)
return|;
block|}
DECL|method|updateCorrelationId (String correlationId, String newCorrelationId, long requestTimeout)
specifier|public
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
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Updated provisional correlationId [{}] to expected correlationId [{}]"
argument_list|,
name|correlationId
argument_list|,
name|newCorrelationId
argument_list|)
expr_stmt|;
name|ReplyHandler
name|handler
init|=
name|correlation
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|correlation
operator|.
name|put
argument_list|(
name|newCorrelationId
argument_list|,
name|handler
argument_list|,
name|requestTimeout
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|handleReplyMessage (String correlationID, AMQP.BasicProperties properties, byte[] message)
specifier|protected
name|void
name|handleReplyMessage
parameter_list|(
name|String
name|correlationID
parameter_list|,
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|,
name|byte
index|[]
name|message
parameter_list|)
block|{
name|ReplyHandler
name|handler
init|=
name|correlation
operator|.
name|get
argument_list|(
name|correlationID
argument_list|)
decl_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
operator|&&
name|endpoint
operator|.
name|isUseMessageIDAsCorrelationID
argument_list|()
condition|)
block|{
name|handler
operator|=
name|waitForProvisionCorrelationToBeUpdated
argument_list|(
name|correlationID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|correlation
operator|.
name|remove
argument_list|(
name|correlationID
argument_list|)
expr_stmt|;
name|handler
operator|.
name|onReply
argument_list|(
name|correlationID
argument_list|,
name|properties
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we could not correlate the received reply message to a matching request and therefore
comment|// we cannot continue routing the unknown message
comment|// log a warn and then ignore the message
name|log
operator|.
name|warn
argument_list|(
literal|"Reply received for unknown correlationID [{}]. The message will be ignored: {}"
argument_list|,
name|correlationID
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createListenerContainer ()
specifier|protected
name|Connection
name|createListenerContainer
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating connection"
argument_list|)
expr_stmt|;
name|Connection
name|conn
init|=
name|endpoint
operator|.
name|connect
argument_list|(
name|executorService
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Creating channel"
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|conn
operator|.
name|createChannel
argument_list|()
decl_stmt|;
comment|// setup the basicQos
if|if
condition|(
name|endpoint
operator|.
name|isPrefetchEnabled
argument_list|()
condition|)
block|{
name|channel
operator|.
name|basicQos
argument_list|(
name|endpoint
operator|.
name|getPrefetchSize
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPrefetchCount
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isPrefetchGlobal
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//Let the server pick a random name for us
name|DeclareOk
name|result
init|=
name|channel
operator|.
name|queueDeclare
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Temporary queue name {}"
argument_list|,
name|result
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|setReplyTo
argument_list|(
name|result
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
comment|//TODO check for the RabbitMQConstants.EXCHANGE_NAME header
name|channel
operator|.
name|queueBind
argument_list|(
name|getReplyTo
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getExchangeName
argument_list|()
argument_list|,
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|new
name|RabbitConsumer
argument_list|(
name|this
argument_list|,
name|channel
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|conn
return|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|consumer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
comment|//TODO combine with class in RabbitMQConsumer
DECL|class|RabbitConsumer
class|class
name|RabbitConsumer
extends|extends
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|DefaultConsumer
block|{
DECL|field|consumer
specifier|private
specifier|final
name|TemporaryQueueReplyManager
name|consumer
decl_stmt|;
DECL|field|channel
specifier|private
specifier|final
name|Channel
name|channel
decl_stmt|;
DECL|field|tag
specifier|private
name|String
name|tag
decl_stmt|;
comment|/**          * Constructs a new instance and records its association to the          * passed-in channel.          *          * @param channel the channel to which this consumer is attached          */
DECL|method|RabbitConsumer (TemporaryQueueReplyManager consumer, Channel channel)
specifier|public
name|RabbitConsumer
parameter_list|(
name|TemporaryQueueReplyManager
name|consumer
parameter_list|,
name|Channel
name|channel
parameter_list|)
block|{
name|super
argument_list|(
name|channel
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleDelivery (String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
specifier|public
name|void
name|handleDelivery
parameter_list|(
name|String
name|consumerTag
parameter_list|,
name|Envelope
name|envelope
parameter_list|,
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|,
name|byte
index|[]
name|body
parameter_list|)
throws|throws
name|IOException
block|{
name|consumer
operator|.
name|onMessage
argument_list|(
name|properties
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
comment|/**          * Bind consumer to channel          */
DECL|method|start ()
specifier|private
name|void
name|start
parameter_list|()
throws|throws
name|IOException
block|{
name|tag
operator|=
name|channel
operator|.
name|basicConsume
argument_list|(
name|getReplyTo
argument_list|()
argument_list|,
name|endpoint
operator|.
name|isAutoAck
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**          * Unbind consumer from channel          */
DECL|method|stop ()
specifier|private
name|void
name|stop
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
if|if
condition|(
name|channel
operator|.
name|isOpen
argument_list|()
condition|)
block|{
if|if
condition|(
name|tag
operator|!=
literal|null
condition|)
block|{
name|channel
operator|.
name|basicCancel
argument_list|(
name|tag
argument_list|)
expr_stmt|;
block|}
name|channel
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

