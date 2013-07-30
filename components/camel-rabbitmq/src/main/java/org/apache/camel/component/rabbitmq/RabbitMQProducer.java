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
specifier|final
name|Connection
name|conn
decl_stmt|;
DECL|field|channel
specifier|private
specifier|final
name|Channel
name|channel
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
name|this
operator|.
name|conn
operator|=
name|endpoint
operator|.
name|connect
argument_list|(
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|channel
operator|=
name|conn
operator|.
name|createChannel
argument_list|()
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
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|IOException
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
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
if|if
condition|(
name|exchangeName
operator|==
literal|null
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
if|if
condition|(
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
literal|"ExchangeName is not provided in header "
operator|+
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|)
throw|;
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
literal|""
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
operator|.
name|Builder
name|properties
init|=
name|buildProperties
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|exchangeName
argument_list|,
name|key
argument_list|,
name|properties
operator|.
name|build
argument_list|()
argument_list|,
name|messageBodyBytes
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
return|return
name|properties
return|;
block|}
block|}
end_class

end_unit

