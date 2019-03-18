begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ByteArrayInputStream
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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Envelope
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
name|LongString
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
name|support
operator|.
name|DefaultMessage
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
name|util
operator|.
name|ObjectHelper
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

begin_class
DECL|class|RabbitMQMessageConverter
specifier|public
class|class
name|RabbitMQMessageConverter
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RabbitMQMessageConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|allowNullHeaders
specifier|private
name|boolean
name|allowNullHeaders
decl_stmt|;
comment|/**      * Will take an {@link Exchange} and add header values back to the {@link Exchange#getIn()}      */
DECL|method|mergeAmqpProperties (Exchange exchange, AMQP.BasicProperties properties)
specifier|public
name|void
name|mergeAmqpProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|.
name|getType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TYPE
argument_list|,
name|properties
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getAppId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|APP_ID
argument_list|,
name|properties
operator|.
name|getAppId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getClusterId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CLUSTERID
argument_list|,
name|properties
operator|.
name|getClusterId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getContentEncoding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_ENCODING
argument_list|,
name|properties
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getContentType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_TYPE
argument_list|,
name|properties
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getCorrelationId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CORRELATIONID
argument_list|,
name|properties
operator|.
name|getCorrelationId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getExpiration
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXPIRATION
argument_list|,
name|properties
operator|.
name|getExpiration
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getMessageId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|properties
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getPriority
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|PRIORITY
argument_list|,
name|properties
operator|.
name|getPriority
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getReplyTo
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REPLY_TO
argument_list|,
name|properties
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getTimestamp
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TIMESTAMP
argument_list|,
name|properties
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getUserId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|USERID
argument_list|,
name|properties
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|properties
operator|.
name|getDeliveryMode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_MODE
argument_list|,
name|properties
operator|.
name|getDeliveryMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildProperties (Exchange exchange)
specifier|public
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
name|buildProperties
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
name|properties
init|=
operator|new
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|Message
name|msg
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
specifier|final
name|Object
name|contentType
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentType
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|contentType
argument_list|(
name|contentType
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|priority
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|PRIORITY
argument_list|)
decl_stmt|;
if|if
condition|(
name|priority
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|priority
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|priority
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|messageId
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|MESSAGE_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageId
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|messageId
argument_list|(
name|messageId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|clusterId
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CLUSTERID
argument_list|)
decl_stmt|;
if|if
condition|(
name|clusterId
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|clusterId
argument_list|(
name|clusterId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|replyTo
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REPLY_TO
argument_list|)
decl_stmt|;
if|if
condition|(
name|replyTo
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|replyTo
argument_list|(
name|replyTo
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|correlationId
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CORRELATIONID
argument_list|)
decl_stmt|;
if|if
condition|(
name|correlationId
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|correlationId
argument_list|(
name|correlationId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|deliveryMode
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|deliveryMode
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|deliveryMode
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|deliveryMode
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|userId
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|USERID
argument_list|)
decl_stmt|;
if|if
condition|(
name|userId
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|userId
argument_list|(
name|userId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|type
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|type
argument_list|(
name|type
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|contentEncoding
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
name|contentEncoding
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|contentEncoding
argument_list|(
name|contentEncoding
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|expiration
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXPIRATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|expiration
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|expiration
argument_list|(
name|expiration
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|appId
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|APP_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|appId
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|appId
argument_list|(
name|appId
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Object
name|timestamp
init|=
name|msg
operator|.
name|removeHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TIMESTAMP
argument_list|)
decl_stmt|;
if|if
condition|(
name|timestamp
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|timestamp
argument_list|(
name|convertTimestamp
argument_list|(
name|timestamp
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|msg
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filteredHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// TODO: Add support for a HeaderFilterStrategy. See: org.apache.camel.component.jms.JmsBinding#shouldOutputHeader
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|header
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// filter header values.
name|Object
name|value
init|=
name|getValidRabbitMQHeaderValue
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|||
name|isAllowNullHeaders
argument_list|()
condition|)
block|{
name|filteredHeaders
operator|.
name|put
argument_list|(
name|header
operator|.
name|getKey
argument_list|()
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
if|if
condition|(
name|header
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring header: {} with null value"
argument_list|,
name|header
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignoring header: {} of class: {} with value: {}"
argument_list|,
name|header
operator|.
name|getKey
argument_list|()
argument_list|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|,
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|properties
operator|.
name|headers
argument_list|(
name|filteredHeaders
argument_list|)
expr_stmt|;
return|return
name|properties
return|;
block|}
DECL|method|convertTimestamp (Object timestamp)
specifier|private
name|Date
name|convertTimestamp
parameter_list|(
name|Object
name|timestamp
parameter_list|)
block|{
if|if
condition|(
name|timestamp
operator|instanceof
name|Date
condition|)
block|{
return|return
operator|(
name|Date
operator|)
name|timestamp
return|;
block|}
return|return
operator|new
name|Date
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|timestamp
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Strategy to test if the given header is valid. Without this, the      * com.rabbitmq.client.impl.Frame.java class will throw an      * IllegalArgumentException (invalid value in table) and close the      * connection.      *      * @param headerValue the header value      * @return the value to use,<tt>null</tt> to ignore this header      * @see com.rabbitmq.client.impl.Frame#fieldValueSize      */
DECL|method|getValidRabbitMQHeaderValue (Object headerValue)
specifier|private
name|Object
name|getValidRabbitMQHeaderValue
parameter_list|(
name|Object
name|headerValue
parameter_list|)
block|{
if|if
condition|(
name|headerValue
operator|instanceof
name|String
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Number
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Boolean
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Date
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|byte
index|[]
condition|)
block|{
return|return
name|headerValue
return|;
block|}
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|LongString
condition|)
block|{
return|return
name|headerValue
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|populateRabbitExchange (Exchange camelExchange, Envelope envelope, AMQP.BasicProperties properties, byte[] body, final boolean out)
specifier|public
name|void
name|populateRabbitExchange
parameter_list|(
name|Exchange
name|camelExchange
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
parameter_list|,
specifier|final
name|boolean
name|out
parameter_list|)
block|{
name|Message
name|message
init|=
name|resolveMessageFrom
argument_list|(
name|camelExchange
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|populateMessageHeaders
argument_list|(
name|message
argument_list|,
name|envelope
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|populateMessageBody
argument_list|(
name|message
argument_list|,
name|camelExchange
argument_list|,
name|properties
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|resolveMessageFrom (final Exchange camelExchange, final boolean out)
specifier|private
name|Message
name|resolveMessageFrom
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|boolean
name|out
parameter_list|)
block|{
name|Message
name|message
decl_stmt|;
if|if
condition|(
name|out
condition|)
block|{
comment|// use OUT message
name|message
operator|=
name|camelExchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|camelExchange
operator|.
name|getIn
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Use the existing message so we keep the headers
name|message
operator|=
name|camelExchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|message
operator|=
operator|new
name|DefaultMessage
argument_list|(
name|camelExchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|camelExchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|message
return|;
block|}
DECL|method|populateMessageHeaders (final Message message, final Envelope envelope, final AMQP.BasicProperties properties)
specifier|private
name|void
name|populateMessageHeaders
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Envelope
name|envelope
parameter_list|,
specifier|final
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
name|populateRoutingInfoHeaders
argument_list|(
name|message
argument_list|,
name|envelope
argument_list|)
expr_stmt|;
name|populateMessageHeadersFromRabbitMQHeaders
argument_list|(
name|message
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
DECL|method|populateRoutingInfoHeaders (final Message message, final Envelope envelope)
specifier|private
name|void
name|populateRoutingInfoHeaders
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Envelope
name|envelope
parameter_list|)
block|{
if|if
condition|(
name|envelope
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|,
name|envelope
operator|.
name|getRoutingKey
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|envelope
operator|.
name|getExchange
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_TAG
argument_list|,
name|envelope
operator|.
name|getDeliveryTag
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REDELIVERY_TAG
argument_list|,
name|envelope
operator|.
name|isRedeliver
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|populateMessageHeadersFromRabbitMQHeaders (final Message message, final AMQP.BasicProperties properties)
specifier|private
name|void
name|populateMessageHeadersFromRabbitMQHeaders
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|properties
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|headers
operator|.
name|entrySet
argument_list|()
control|)
block|{
comment|// Convert LongStrings to String.
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|instanceof
name|LongString
condition|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|populateMessageBody (final Message message, final Exchange camelExchange, final AMQP.BasicProperties properties, final byte[] body)
specifier|private
name|void
name|populateMessageBody
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
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
block|{
if|if
condition|(
name|hasSerializeHeader
argument_list|(
name|properties
argument_list|)
condition|)
block|{
name|deserializeBody
argument_list|(
name|camelExchange
argument_list|,
name|message
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Set the body as a byte[] and let the type converter deal with it
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|deserializeBody (final Exchange camelExchange, final Message message, final byte[] body)
specifier|private
name|void
name|deserializeBody
parameter_list|(
specifier|final
name|Exchange
name|camelExchange
parameter_list|,
specifier|final
name|Message
name|message
parameter_list|,
specifier|final
name|byte
index|[]
name|body
parameter_list|)
block|{
name|Object
name|messageBody
init|=
literal|null
decl_stmt|;
try|try
init|(
name|InputStream
name|b
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
argument_list|)
init|;
name|ObjectInputStream
name|o
operator|=
operator|new
name|ObjectInputStream
argument_list|(
name|b
argument_list|)
init|)
block|{
name|messageBody
operator|=
name|o
operator|.
name|readObject
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
decl||
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not deserialize the object"
argument_list|)
expr_stmt|;
name|camelExchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageBody
operator|instanceof
name|Throwable
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Reply was an Exception. Setting the Exception on the Exchange"
argument_list|)
expr_stmt|;
name|camelExchange
operator|.
name|setException
argument_list|(
operator|(
name|Throwable
operator|)
name|messageBody
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|setBody
argument_list|(
name|messageBody
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|hasSerializeHeader (AMQP.BasicProperties properties)
specifier|private
name|boolean
name|hasSerializeHeader
parameter_list|(
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
return|return
name|hasHeaders
argument_list|(
name|properties
argument_list|)
operator|&&
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|isSerializeHeaderEnabled
argument_list|(
name|properties
argument_list|)
argument_list|)
return|;
block|}
DECL|method|hasHeaders (final AMQP.BasicProperties properties)
specifier|private
name|boolean
name|hasHeaders
parameter_list|(
specifier|final
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
return|return
name|properties
operator|!=
literal|null
operator|&&
name|properties
operator|.
name|getHeaders
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|method|isSerializeHeaderEnabled (final AMQP.BasicProperties properties)
specifier|private
name|Object
name|isSerializeHeaderEnabled
parameter_list|(
specifier|final
name|AMQP
operator|.
name|BasicProperties
name|properties
parameter_list|)
block|{
return|return
name|properties
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
name|RabbitMQEndpoint
operator|.
name|SERIALIZE_HEADER
argument_list|)
return|;
block|}
DECL|method|isAllowNullHeaders ()
specifier|public
name|boolean
name|isAllowNullHeaders
parameter_list|()
block|{
return|return
name|allowNullHeaders
return|;
block|}
DECL|method|setAllowNullHeaders (boolean allowNullHeaders)
specifier|public
name|void
name|setAllowNullHeaders
parameter_list|(
name|boolean
name|allowNullHeaders
parameter_list|)
block|{
name|this
operator|.
name|allowNullHeaders
operator|=
name|allowNullHeaders
expr_stmt|;
block|}
block|}
end_class

end_unit

