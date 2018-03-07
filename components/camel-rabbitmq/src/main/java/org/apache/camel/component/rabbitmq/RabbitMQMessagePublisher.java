begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|io
operator|.
name|NotSerializableException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
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
name|ReturnListener
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
name|NoTypeConversionAvailableException
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
name|RuntimeCamelException
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
name|TypeConversionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * A method object for publishing to RabbitMQ  */
end_comment

begin_class
DECL|class|RabbitMQMessagePublisher
specifier|public
class|class
name|RabbitMQMessagePublisher
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RabbitMQMessagePublisher
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelExchange
specifier|private
specifier|final
name|Exchange
name|camelExchange
decl_stmt|;
DECL|field|channel
specifier|private
specifier|final
name|Channel
name|channel
decl_stmt|;
DECL|field|routingKey
specifier|private
specifier|final
name|String
name|routingKey
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|RabbitMQEndpoint
name|endpoint
decl_stmt|;
DECL|field|message
specifier|private
specifier|final
name|Message
name|message
decl_stmt|;
DECL|field|basicReturnReceived
specifier|private
specifier|volatile
name|boolean
name|basicReturnReceived
decl_stmt|;
DECL|field|guaranteedDeliveryReturnListener
specifier|private
specifier|final
name|ReturnListener
name|guaranteedDeliveryReturnListener
init|=
operator|new
name|ReturnListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|handleReturn
parameter_list|(
name|int
name|replyCode
parameter_list|,
name|String
name|replyText
parameter_list|,
name|String
name|exchange
parameter_list|,
name|String
name|routingKey
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Delivery failed for exchange: {} and routing key: {}; replyCode: {}; replyText: {}"
argument_list|,
name|exchange
argument_list|,
name|routingKey
argument_list|,
name|replyCode
argument_list|,
name|replyText
argument_list|)
expr_stmt|;
name|basicReturnReceived
operator|=
literal|true
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|method|RabbitMQMessagePublisher (final Exchange camelExchange, final Channel channel, final String routingKey, final RabbitMQEndpoint endpoint)
specifier|public
name|RabbitMQMessagePublisher
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|Channel
name|channel
parameter_list|,
specifier|final
name|String
name|routingKey
parameter_list|,
specifier|final
name|RabbitMQEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|camelExchange
operator|=
name|camelExchange
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
name|this
operator|.
name|routingKey
operator|=
name|routingKey
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|message
operator|=
name|resolveMessageFrom
argument_list|(
name|camelExchange
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveMessageFrom (final Exchange camelExchange)
specifier|private
name|Message
name|resolveMessageFrom
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|)
block|{
name|Message
name|message
init|=
name|camelExchange
operator|.
name|hasOut
argument_list|()
condition|?
name|camelExchange
operator|.
name|getOut
argument_list|()
else|:
name|camelExchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Remove the SERIALIZE_HEADER in case it was previously set
if|if
condition|(
name|message
operator|.
name|getHeaders
argument_list|()
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsKey
argument_list|(
name|RabbitMQEndpoint
operator|.
name|SERIALIZE_HEADER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Removing header: {}"
argument_list|,
name|RabbitMQEndpoint
operator|.
name|SERIALIZE_HEADER
argument_list|)
expr_stmt|;
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|remove
argument_list|(
name|RabbitMQEndpoint
operator|.
name|SERIALIZE_HEADER
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|routingKey
operator|!=
literal|null
operator|&&
name|routingKey
operator|.
name|startsWith
argument_list|(
name|RabbitMQConstants
operator|.
name|RABBITMQ_DIRECT_REPLY_ROUTING_KEY
argument_list|)
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|RabbitMQConstants
operator|.
name|RABBITMQ_DIRECT_REPLY_EXCHANGE
argument_list|)
expr_stmt|;
comment|// use default exchange for reply-to messages
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_OVERRIDE_NAME
argument_list|,
name|RabbitMQConstants
operator|.
name|RABBITMQ_DIRECT_REPLY_EXCHANGE
argument_list|)
expr_stmt|;
comment|// use default exchange for reply-to messages
block|}
return|return
name|message
return|;
block|}
DECL|method|publish ()
specifier|public
name|void
name|publish
parameter_list|()
throws|throws
name|IOException
block|{
name|AMQP
operator|.
name|BasicProperties
name|properties
decl_stmt|;
name|byte
index|[]
name|body
decl_stmt|;
try|try
block|{
comment|// To maintain backwards compatibility try the TypeConverter (The DefaultTypeConverter seems to only work on Strings)
name|body
operator|=
name|camelExchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|camelExchange
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|properties
operator|=
name|endpoint
operator|.
name|getMessageConverter
argument_list|()
operator|.
name|buildProperties
argument_list|(
name|camelExchange
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
decl||
name|TypeConversionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|instanceof
name|Serializable
condition|)
block|{
comment|// Add the header so the reply processor knows to de-serialize it
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|RabbitMQEndpoint
operator|.
name|SERIALIZE_HEADER
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|properties
operator|=
name|endpoint
operator|.
name|getMessageConverter
argument_list|()
operator|.
name|buildProperties
argument_list|(
name|camelExchange
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|body
operator|=
name|serializeBodyFrom
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|.
name|getBody
argument_list|()
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
name|endpoint
operator|.
name|getMessageConverter
argument_list|()
operator|.
name|buildProperties
argument_list|(
name|camelExchange
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|body
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot convert {} to byte[]"
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
name|publishToRabbitMQ
argument_list|(
name|properties
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|publishToRabbitMQ (final AMQP.BasicProperties properties, final byte[] body)
specifier|private
name|void
name|publishToRabbitMQ
parameter_list|(
specifier|final
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|,
specifier|final
name|byte
index|[]
name|body
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|rabbitExchange
init|=
name|endpoint
operator|.
name|getExchangeName
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Boolean
name|mandatory
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|MANDATORY
argument_list|,
name|endpoint
operator|.
name|isMandatory
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Boolean
name|immediate
init|=
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|IMMEDIATE
argument_list|,
name|endpoint
operator|.
name|isImmediate
argument_list|()
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sending message to exchange: {} with CorrelationId: {}"
argument_list|,
name|rabbitExchange
argument_list|,
name|properties
operator|.
name|getCorrelationId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|isPublisherAcknowledgements
argument_list|()
condition|)
block|{
name|channel
operator|.
name|confirmSelect
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isGuaranteedDeliveries
argument_list|()
condition|)
block|{
name|basicReturnReceived
operator|=
literal|false
expr_stmt|;
name|channel
operator|.
name|addReturnListener
argument_list|(
name|guaranteedDeliveryReturnListener
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|channel
operator|.
name|basicPublish
argument_list|(
name|rabbitExchange
argument_list|,
name|routingKey
argument_list|,
name|mandatory
argument_list|,
name|immediate
argument_list|,
name|properties
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|isPublisherAcknowledgements
argument_list|()
condition|)
block|{
name|waitForConfirmation
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|endpoint
operator|.
name|isGuaranteedDeliveries
argument_list|()
condition|)
block|{
name|channel
operator|.
name|removeReturnListener
argument_list|(
name|guaranteedDeliveryReturnListener
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isPublisherAcknowledgements ()
specifier|private
name|boolean
name|isPublisherAcknowledgements
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|isPublisherAcknowledgements
argument_list|()
operator|||
name|endpoint
operator|.
name|isGuaranteedDeliveries
argument_list|()
return|;
block|}
DECL|method|waitForConfirmation ()
specifier|private
name|void
name|waitForConfirmation
parameter_list|()
throws|throws
name|IOException
block|{
try|try
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Waiting for publisher acknowledgements for {}ms"
argument_list|,
name|endpoint
operator|.
name|getPublisherAcknowledgementsTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|waitForConfirmsOrDie
argument_list|(
name|endpoint
operator|.
name|getPublisherAcknowledgementsTimeout
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|basicReturnReceived
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Failed to deliver message; basic.return received"
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
decl||
name|TimeoutException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Acknowledgement error for {}"
argument_list|,
name|camelExchange
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|serializeBodyFrom (final Message msg)
specifier|private
name|byte
index|[]
name|serializeBodyFrom
parameter_list|(
specifier|final
name|Message
name|msg
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|ByteArrayOutputStream
name|b
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|;
name|ObjectOutputStream
name|o
operator|=
operator|new
name|ObjectOutputStream
argument_list|(
name|b
argument_list|)
init|)
block|{
name|o
operator|.
name|writeObject
argument_list|(
name|msg
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|b
operator|.
name|toByteArray
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|NotSerializableException
name|nse
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Cannot send object {} via RabbitMQ because it contains non-serializable objects."
argument_list|,
name|msg
operator|.
name|getBody
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|nse
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

