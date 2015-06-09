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
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Timestamp
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
name|getHeader
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
operator|new
name|Object
index|[]
block|{
name|header
operator|.
name|getKey
argument_list|()
block|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|header
operator|.
name|getValue
argument_list|()
argument_list|)
block|,
name|header
operator|.
name|getValue
argument_list|()
block|}
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
name|BigDecimal
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
elseif|else
if|if
condition|(
name|headerValue
operator|instanceof
name|Timestamp
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
name|Byte
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
name|Double
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
name|Float
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
name|Long
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
block|}
end_class

end_unit

