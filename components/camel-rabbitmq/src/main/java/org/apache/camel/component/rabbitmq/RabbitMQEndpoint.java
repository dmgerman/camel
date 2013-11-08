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
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|field|vhost
specifier|private
name|String
name|vhost
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
if|if
condition|(
name|getVhost
argument_list|()
operator|==
literal|null
condition|)
block|{
name|factory
operator|.
name|setVirtualHost
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|.
name|setVirtualHost
argument_list|(
name|getVhost
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
return|return
name|factory
operator|.
name|newConnection
argument_list|(
name|executor
argument_list|)
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
block|}
end_class

end_unit

