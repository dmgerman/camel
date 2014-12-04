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
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|Connection
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
name|pool
operator|.
name|PoolableChannelFactory
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
name|impl
operator|.
name|DefaultProducer
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
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|ObjectPool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|impl
operator|.
name|GenericObjectPool
import|;
end_import

begin_class
DECL|class|RabbitMQProducer
specifier|public
class|class
name|RabbitMQProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|conn
specifier|private
name|Connection
name|conn
decl_stmt|;
DECL|field|channelPool
specifier|private
name|ObjectPool
argument_list|<
name|Channel
argument_list|>
name|channelPool
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|closeTimeout
specifier|private
name|int
name|closeTimeout
init|=
literal|30
operator|*
literal|1000
decl_stmt|;
DECL|method|RabbitMQProducer (RabbitMQEndpoint endpoint)
specifier|public
name|RabbitMQProducer
parameter_list|(
name|RabbitMQEndpoint
name|endpoint
parameter_list|)
throws|throws
name|IOException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|RabbitMQEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|RabbitMQEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Channel callback (similar to Spring JDBC ConnectionCallback)      */
DECL|interface|ChannelCallback
specifier|private
specifier|static
interface|interface
name|ChannelCallback
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|doWithChannel (Channel channel)
name|T
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
comment|/**      * Do something with a pooled channel (similar to Spring JDBC TransactionTemplate#execute)      */
DECL|method|execute (ChannelCallback<T> callback)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|execute
parameter_list|(
name|ChannelCallback
argument_list|<
name|T
argument_list|>
name|callback
parameter_list|)
throws|throws
name|Exception
block|{
name|Channel
name|channel
init|=
name|channelPool
operator|.
name|borrowObject
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|callback
operator|.
name|doWithChannel
argument_list|(
name|channel
argument_list|)
return|;
block|}
finally|finally
block|{
name|channelPool
operator|.
name|returnObject
argument_list|(
name|channel
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Open connection and initialize channel pool      */
DECL|method|openConnectionAndChannelPool ()
specifier|private
name|void
name|openConnectionAndChannelPool
parameter_list|()
throws|throws
name|Exception
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Creating connection..."
argument_list|)
expr_stmt|;
name|this
operator|.
name|conn
operator|=
name|getEndpoint
argument_list|()
operator|.
name|connect
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Created connection: {}"
argument_list|,
name|conn
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Creating channel pool..."
argument_list|)
expr_stmt|;
name|channelPool
operator|=
operator|new
name|GenericObjectPool
argument_list|<
name|Channel
argument_list|>
argument_list|(
operator|new
name|PoolableChannelFactory
argument_list|(
name|this
operator|.
name|conn
argument_list|)
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getChannelPoolMaxSize
argument_list|()
argument_list|,
name|GenericObjectPool
operator|.
name|WHEN_EXHAUSTED_BLOCK
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getChannelPoolMaxWait
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isDeclare
argument_list|()
condition|)
block|{
name|execute
argument_list|(
operator|new
name|ChannelCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
name|getEndpoint
argument_list|()
operator|.
name|declareExchangeAndQueue
argument_list|(
name|channel
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|executorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"CamelRabbitMQProducer["
operator|+
name|getEndpoint
argument_list|()
operator|.
name|getQueue
argument_list|()
operator|+
literal|"]"
argument_list|)
expr_stmt|;
try|try
block|{
name|openConnectionAndChannelPool
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Failed to create connection"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * If needed, close Connection and Channel      */
DECL|method|closeConnectionAndChannel ()
specifier|private
name|void
name|closeConnectionAndChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|channelPool
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Closing connection: {} with timeout: {} ms."
argument_list|,
name|conn
argument_list|,
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|.
name|close
argument_list|(
name|closeTimeout
argument_list|)
expr_stmt|;
name|conn
operator|=
literal|null
expr_stmt|;
block|}
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
name|closeConnectionAndChannel
argument_list|()
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|exchangeName
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// If it is BridgeEndpoint we should ignore the message header of EXCHANGE_NAME
if|if
condition|(
name|exchangeName
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|exchangeName
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getExchangeName
argument_list|()
expr_stmt|;
block|}
name|String
name|key
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// we just need to make sure RoutingKey option take effect if it is not BridgeEndpoint
if|if
condition|(
name|key
operator|==
literal|null
operator|||
name|getEndpoint
argument_list|()
operator|.
name|isBridgeEndpoint
argument_list|()
condition|)
block|{
name|key
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|getEndpoint
argument_list|()
operator|.
name|getRoutingKey
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|key
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|exchangeName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"ExchangeName and RoutingKey is not provided in the endpoint: "
operator|+
name|getEndpoint
argument_list|()
argument_list|)
throw|;
block|}
name|byte
index|[]
name|messageBodyBytes
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|)
decl_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|properties
init|=
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|basicPublish
argument_list|(
name|exchangeName
argument_list|,
name|key
argument_list|,
name|properties
argument_list|,
name|messageBodyBytes
argument_list|)
expr_stmt|;
block|}
comment|/**      * Send a message borrowing a channel from the pool.      *      * @param exchange   Target exchange      * @param routingKey Routing key      * @param properties Header properties      * @param body       Body content      */
DECL|method|basicPublish (final String exchange, final String routingKey, final AMQP.BasicProperties properties, final byte[] body)
specifier|private
name|void
name|basicPublish
parameter_list|(
specifier|final
name|String
name|exchange
parameter_list|,
specifier|final
name|String
name|routingKey
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
throws|throws
name|Exception
block|{
if|if
condition|(
name|channelPool
operator|==
literal|null
condition|)
block|{
comment|// Open connection and channel lazily
name|openConnectionAndChannelPool
argument_list|()
expr_stmt|;
block|}
name|execute
argument_list|(
operator|new
name|ChannelCallback
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Void
name|doWithChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
throws|throws
name|Exception
block|{
name|channel
operator|.
name|basicPublish
argument_list|(
name|exchange
argument_list|,
name|routingKey
argument_list|,
name|properties
argument_list|,
name|body
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|buildProperties (Exchange exchange)
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
specifier|final
name|Object
name|contentType
init|=
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
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
name|log
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
name|log
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
name|log
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
comment|/**      * Strategy to test if the given header is valid      *      * @param headerValue the header value      * @return the value to use,<tt>null</tt> to ignore this header      * @see com.rabbitmq.client.impl.Frame#fieldValueSize      */
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
return|return
literal|null
return|;
block|}
DECL|method|getCloseTimeout ()
specifier|public
name|int
name|getCloseTimeout
parameter_list|()
block|{
return|return
name|closeTimeout
return|;
block|}
DECL|method|setCloseTimeout (int closeTimeout)
specifier|public
name|void
name|setCloseTimeout
parameter_list|(
name|int
name|closeTimeout
parameter_list|)
block|{
name|this
operator|.
name|closeTimeout
operator|=
name|closeTimeout
expr_stmt|;
block|}
block|}
end_class

end_unit

