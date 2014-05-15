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
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyManagementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|UUID
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
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
name|Address
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
name|ConnectionFactory
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|DefaultEndpoint
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
name|DefaultExchange
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
name|DefaultMessage
import|;
end_import

begin_class
DECL|class|RabbitMQEndpoint
specifier|public
class|class
name|RabbitMQEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|username
specifier|private
name|String
name|username
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_USER
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_PASS
decl_stmt|;
DECL|field|vhost
specifier|private
name|String
name|vhost
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_VHOST
decl_stmt|;
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
DECL|field|threadPoolSize
specifier|private
name|int
name|threadPoolSize
init|=
literal|10
decl_stmt|;
DECL|field|portNumber
specifier|private
name|int
name|portNumber
decl_stmt|;
DECL|field|autoAck
specifier|private
name|boolean
name|autoAck
init|=
literal|true
decl_stmt|;
DECL|field|autoDelete
specifier|private
name|boolean
name|autoDelete
init|=
literal|true
decl_stmt|;
DECL|field|durable
specifier|private
name|boolean
name|durable
init|=
literal|true
decl_stmt|;
DECL|field|bridgeEndpoint
specifier|private
name|boolean
name|bridgeEndpoint
decl_stmt|;
DECL|field|queue
specifier|private
name|String
name|queue
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|exchangeName
specifier|private
name|String
name|exchangeName
decl_stmt|;
DECL|field|exchangeType
specifier|private
name|String
name|exchangeType
init|=
literal|"direct"
decl_stmt|;
DECL|field|routingKey
specifier|private
name|String
name|routingKey
decl_stmt|;
DECL|field|addresses
specifier|private
name|Address
index|[]
name|addresses
decl_stmt|;
DECL|field|connectionTimeout
specifier|private
name|int
name|connectionTimeout
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_CONNECTION_TIMEOUT
decl_stmt|;
DECL|field|requestedChannelMax
specifier|private
name|int
name|requestedChannelMax
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_CHANNEL_MAX
decl_stmt|;
DECL|field|requestedFrameMax
specifier|private
name|int
name|requestedFrameMax
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_FRAME_MAX
decl_stmt|;
DECL|field|requestedHeartbeat
specifier|private
name|int
name|requestedHeartbeat
init|=
name|ConnectionFactory
operator|.
name|DEFAULT_HEARTBEAT
decl_stmt|;
DECL|field|sslProtocol
specifier|private
name|String
name|sslProtocol
decl_stmt|;
DECL|field|trustManager
specifier|private
name|TrustManager
name|trustManager
decl_stmt|;
DECL|field|clientProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|clientProperties
decl_stmt|;
DECL|field|connectionFactory
specifier|private
name|ConnectionFactory
name|connectionFactory
decl_stmt|;
DECL|field|automaticRecoveryEnabled
specifier|private
name|Boolean
name|automaticRecoveryEnabled
decl_stmt|;
DECL|field|networkRecoveryInterval
specifier|private
name|Integer
name|networkRecoveryInterval
decl_stmt|;
DECL|field|topologyRecoveryEnabled
specifier|private
name|Boolean
name|topologyRecoveryEnabled
decl_stmt|;
DECL|method|RabbitMQEndpoint ()
specifier|public
name|RabbitMQEndpoint
parameter_list|()
block|{     }
DECL|method|RabbitMQEndpoint (String endpointUri, RabbitMQComponent component)
specifier|public
name|RabbitMQEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RabbitMQComponent
name|component
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
block|}
DECL|method|RabbitMQEndpoint (String endpointUri, RabbitMQComponent component, ConnectionFactory connectionFactory)
specifier|public
name|RabbitMQEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|RabbitMQComponent
name|component
parameter_list|,
name|ConnectionFactory
name|connectionFactory
parameter_list|)
throws|throws
name|URISyntaxException
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|createRabbitExchange (Envelope envelope, AMQP.BasicProperties properties, byte[] body)
specifier|public
name|Exchange
name|createRabbitExchange
parameter_list|(
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
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
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
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|RabbitMQConsumer
name|consumer
init|=
operator|new
name|RabbitMQConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|connect (ExecutorService executor)
specifier|public
name|Connection
name|connect
parameter_list|(
name|ExecutorService
name|executor
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|getAddresses
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|getOrCreateConnectionFactory
argument_list|()
operator|.
name|newConnection
argument_list|(
name|executor
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getOrCreateConnectionFactory
argument_list|()
operator|.
name|newConnection
argument_list|(
name|executor
argument_list|,
name|getAddresses
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getOrCreateConnectionFactory ()
specifier|protected
name|ConnectionFactory
name|getOrCreateConnectionFactory
parameter_list|()
block|{
if|if
condition|(
name|connectionFactory
operator|==
literal|null
condition|)
block|{
name|ConnectionFactory
name|factory
init|=
operator|new
name|ConnectionFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setUsername
argument_list|(
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPassword
argument_list|(
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setVirtualHost
argument_list|(
name|getVhost
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setHost
argument_list|(
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
name|getPortNumber
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getClientProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setClientProperties
argument_list|(
name|getClientProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|factory
operator|.
name|setConnectionTimeout
argument_list|(
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRequestedChannelMax
argument_list|(
name|getRequestedChannelMax
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRequestedFrameMax
argument_list|(
name|getRequestedFrameMax
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setRequestedHeartbeat
argument_list|(
name|getRequestedHeartbeat
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getSslProtocol
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
name|getSslProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"true"
argument_list|)
condition|)
block|{
name|factory
operator|.
name|useSslProtocol
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getTrustManager
argument_list|()
operator|==
literal|null
condition|)
block|{
name|factory
operator|.
name|useSslProtocol
argument_list|(
name|getSslProtocol
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|.
name|useSslProtocol
argument_list|(
name|getSslProtocol
argument_list|()
argument_list|,
name|getTrustManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid sslProtocol "
operator|+
name|sslProtocol
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|KeyManagementException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid sslProtocol "
operator|+
name|sslProtocol
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|getAutomaticRecoveryEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setAutomaticRecoveryEnabled
argument_list|(
name|getAutomaticRecoveryEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getNetworkRecoveryInterval
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setNetworkRecoveryInterval
argument_list|(
name|getNetworkRecoveryInterval
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getTopologyRecoveryEnabled
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setTopologyRecoveryEnabled
argument_list|(
name|getTopologyRecoveryEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|connectionFactory
operator|=
name|factory
expr_stmt|;
block|}
return|return
name|connectionFactory
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RabbitMQProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createExecutor ()
specifier|protected
name|ExecutorService
name|createExecutor
parameter_list|()
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"RabbitMQConsumer"
argument_list|,
name|getThreadPoolSize
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|getThreadPoolSize
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getVhost ()
specifier|public
name|String
name|getVhost
parameter_list|()
block|{
return|return
name|vhost
return|;
block|}
DECL|method|setVhost (String vhost)
specifier|public
name|void
name|setVhost
parameter_list|(
name|String
name|vhost
parameter_list|)
block|{
name|this
operator|.
name|vhost
operator|=
name|vhost
expr_stmt|;
block|}
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getThreadPoolSize ()
specifier|public
name|int
name|getThreadPoolSize
parameter_list|()
block|{
return|return
name|threadPoolSize
return|;
block|}
DECL|method|setThreadPoolSize (int threadPoolSize)
specifier|public
name|void
name|setThreadPoolSize
parameter_list|(
name|int
name|threadPoolSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolSize
operator|=
name|threadPoolSize
expr_stmt|;
block|}
DECL|method|getPortNumber ()
specifier|public
name|int
name|getPortNumber
parameter_list|()
block|{
return|return
name|portNumber
return|;
block|}
DECL|method|setPortNumber (int portNumber)
specifier|public
name|void
name|setPortNumber
parameter_list|(
name|int
name|portNumber
parameter_list|)
block|{
name|this
operator|.
name|portNumber
operator|=
name|portNumber
expr_stmt|;
block|}
DECL|method|isAutoAck ()
specifier|public
name|boolean
name|isAutoAck
parameter_list|()
block|{
return|return
name|autoAck
return|;
block|}
DECL|method|setAutoAck (boolean autoAck)
specifier|public
name|void
name|setAutoAck
parameter_list|(
name|boolean
name|autoAck
parameter_list|)
block|{
name|this
operator|.
name|autoAck
operator|=
name|autoAck
expr_stmt|;
block|}
DECL|method|isAutoDelete ()
specifier|public
name|boolean
name|isAutoDelete
parameter_list|()
block|{
return|return
name|autoDelete
return|;
block|}
DECL|method|setAutoDelete (boolean autoDelete)
specifier|public
name|void
name|setAutoDelete
parameter_list|(
name|boolean
name|autoDelete
parameter_list|)
block|{
name|this
operator|.
name|autoDelete
operator|=
name|autoDelete
expr_stmt|;
block|}
DECL|method|isDurable ()
specifier|public
name|boolean
name|isDurable
parameter_list|()
block|{
return|return
name|durable
return|;
block|}
DECL|method|setDurable (boolean durable)
specifier|public
name|void
name|setDurable
parameter_list|(
name|boolean
name|durable
parameter_list|)
block|{
name|this
operator|.
name|durable
operator|=
name|durable
expr_stmt|;
block|}
DECL|method|getQueue ()
specifier|public
name|String
name|getQueue
parameter_list|()
block|{
return|return
name|queue
return|;
block|}
DECL|method|setQueue (String queue)
specifier|public
name|void
name|setQueue
parameter_list|(
name|String
name|queue
parameter_list|)
block|{
name|this
operator|.
name|queue
operator|=
name|queue
expr_stmt|;
block|}
DECL|method|getExchangeName ()
specifier|public
name|String
name|getExchangeName
parameter_list|()
block|{
return|return
name|exchangeName
return|;
block|}
DECL|method|setExchangeName (String exchangeName)
specifier|public
name|void
name|setExchangeName
parameter_list|(
name|String
name|exchangeName
parameter_list|)
block|{
name|this
operator|.
name|exchangeName
operator|=
name|exchangeName
expr_stmt|;
block|}
DECL|method|getExchangeType ()
specifier|public
name|String
name|getExchangeType
parameter_list|()
block|{
return|return
name|exchangeType
return|;
block|}
DECL|method|setExchangeType (String exchangeType)
specifier|public
name|void
name|setExchangeType
parameter_list|(
name|String
name|exchangeType
parameter_list|)
block|{
name|this
operator|.
name|exchangeType
operator|=
name|exchangeType
expr_stmt|;
block|}
DECL|method|getRoutingKey ()
specifier|public
name|String
name|getRoutingKey
parameter_list|()
block|{
return|return
name|routingKey
return|;
block|}
DECL|method|setRoutingKey (String routingKey)
specifier|public
name|void
name|setRoutingKey
parameter_list|(
name|String
name|routingKey
parameter_list|)
block|{
name|this
operator|.
name|routingKey
operator|=
name|routingKey
expr_stmt|;
block|}
DECL|method|setBridgeEndpoint (boolean bridgeEndpoint)
specifier|public
name|void
name|setBridgeEndpoint
parameter_list|(
name|boolean
name|bridgeEndpoint
parameter_list|)
block|{
name|this
operator|.
name|bridgeEndpoint
operator|=
name|bridgeEndpoint
expr_stmt|;
block|}
DECL|method|isBridgeEndpoint ()
specifier|public
name|boolean
name|isBridgeEndpoint
parameter_list|()
block|{
return|return
name|bridgeEndpoint
return|;
block|}
DECL|method|setAddresses (String addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|String
name|addresses
parameter_list|)
block|{
name|Address
index|[]
name|addressArray
init|=
name|Address
operator|.
name|parseAddresses
argument_list|(
name|addresses
argument_list|)
decl_stmt|;
if|if
condition|(
name|addressArray
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addressArray
expr_stmt|;
block|}
block|}
DECL|method|getAddresses ()
specifier|public
name|Address
index|[]
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|int
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (int connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|int
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getRequestedChannelMax ()
specifier|public
name|int
name|getRequestedChannelMax
parameter_list|()
block|{
return|return
name|requestedChannelMax
return|;
block|}
DECL|method|setRequestedChannelMax (int requestedChannelMax)
specifier|public
name|void
name|setRequestedChannelMax
parameter_list|(
name|int
name|requestedChannelMax
parameter_list|)
block|{
name|this
operator|.
name|requestedChannelMax
operator|=
name|requestedChannelMax
expr_stmt|;
block|}
DECL|method|getRequestedFrameMax ()
specifier|public
name|int
name|getRequestedFrameMax
parameter_list|()
block|{
return|return
name|requestedFrameMax
return|;
block|}
DECL|method|setRequestedFrameMax (int requestedFrameMax)
specifier|public
name|void
name|setRequestedFrameMax
parameter_list|(
name|int
name|requestedFrameMax
parameter_list|)
block|{
name|this
operator|.
name|requestedFrameMax
operator|=
name|requestedFrameMax
expr_stmt|;
block|}
DECL|method|getRequestedHeartbeat ()
specifier|public
name|int
name|getRequestedHeartbeat
parameter_list|()
block|{
return|return
name|requestedHeartbeat
return|;
block|}
DECL|method|setRequestedHeartbeat (int requestedHeartbeat)
specifier|public
name|void
name|setRequestedHeartbeat
parameter_list|(
name|int
name|requestedHeartbeat
parameter_list|)
block|{
name|this
operator|.
name|requestedHeartbeat
operator|=
name|requestedHeartbeat
expr_stmt|;
block|}
DECL|method|getSslProtocol ()
specifier|public
name|String
name|getSslProtocol
parameter_list|()
block|{
return|return
name|sslProtocol
return|;
block|}
DECL|method|setSslProtocol (String sslProtocol)
specifier|public
name|void
name|setSslProtocol
parameter_list|(
name|String
name|sslProtocol
parameter_list|)
block|{
name|this
operator|.
name|sslProtocol
operator|=
name|sslProtocol
expr_stmt|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|getTrustManager ()
specifier|public
name|TrustManager
name|getTrustManager
parameter_list|()
block|{
return|return
name|trustManager
return|;
block|}
DECL|method|setTrustManager (TrustManager trustManager)
specifier|public
name|void
name|setTrustManager
parameter_list|(
name|TrustManager
name|trustManager
parameter_list|)
block|{
name|this
operator|.
name|trustManager
operator|=
name|trustManager
expr_stmt|;
block|}
DECL|method|getClientProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getClientProperties
parameter_list|()
block|{
return|return
name|clientProperties
return|;
block|}
DECL|method|setClientProperties (Map<String, Object> clientProperties)
specifier|public
name|void
name|setClientProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|clientProperties
parameter_list|)
block|{
name|this
operator|.
name|clientProperties
operator|=
name|clientProperties
expr_stmt|;
block|}
DECL|method|getAutomaticRecoveryEnabled ()
specifier|public
name|Boolean
name|getAutomaticRecoveryEnabled
parameter_list|()
block|{
return|return
name|automaticRecoveryEnabled
return|;
block|}
DECL|method|setAutomaticRecoveryEnabled (Boolean automaticRecoveryEnabled)
specifier|public
name|void
name|setAutomaticRecoveryEnabled
parameter_list|(
name|Boolean
name|automaticRecoveryEnabled
parameter_list|)
block|{
name|this
operator|.
name|automaticRecoveryEnabled
operator|=
name|automaticRecoveryEnabled
expr_stmt|;
block|}
DECL|method|getNetworkRecoveryInterval ()
specifier|public
name|Integer
name|getNetworkRecoveryInterval
parameter_list|()
block|{
return|return
name|networkRecoveryInterval
return|;
block|}
DECL|method|setNetworkRecoveryInterval (Integer networkRecoveryInterval)
specifier|public
name|void
name|setNetworkRecoveryInterval
parameter_list|(
name|Integer
name|networkRecoveryInterval
parameter_list|)
block|{
name|this
operator|.
name|networkRecoveryInterval
operator|=
name|networkRecoveryInterval
expr_stmt|;
block|}
DECL|method|getTopologyRecoveryEnabled ()
specifier|public
name|Boolean
name|getTopologyRecoveryEnabled
parameter_list|()
block|{
return|return
name|topologyRecoveryEnabled
return|;
block|}
DECL|method|setTopologyRecoveryEnabled (Boolean topologyRecoveryEnabled)
specifier|public
name|void
name|setTopologyRecoveryEnabled
parameter_list|(
name|Boolean
name|topologyRecoveryEnabled
parameter_list|)
block|{
name|this
operator|.
name|topologyRecoveryEnabled
operator|=
name|topologyRecoveryEnabled
expr_stmt|;
block|}
block|}
end_class

end_unit

