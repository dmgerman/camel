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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|Endpoint
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
name|EndpointInject
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|RabbitMQConsumerIntTest
specifier|public
class|class
name|RabbitMQConsumerIntTest
extends|extends
name|AbstractRabbitMQIntTest
block|{
DECL|field|EXCHANGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE
init|=
literal|"ex1"
decl_stmt|;
DECL|field|HEADERS_EXCHANGE
specifier|private
specifier|static
specifier|final
name|String
name|HEADERS_EXCHANGE
init|=
literal|"ex8"
decl_stmt|;
DECL|field|QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|QUEUE
init|=
literal|"q1"
decl_stmt|;
DECL|field|MSG
specifier|private
specifier|static
specifier|final
name|String
name|MSG
init|=
literal|"hello world"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"rabbitmq:localhost:5672/"
operator|+
name|EXCHANGE
operator|+
literal|"?username=cameltest&password=cameltest"
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|to
specifier|private
name|MockEndpoint
name|to
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"rabbitmq:localhost:5672/"
operator|+
name|HEADERS_EXCHANGE
operator|+
literal|"?username=cameltest&password=cameltest&exchangeType=headers&queue="
operator|+
name|QUEUE
operator|+
literal|"&args=#args"
argument_list|)
DECL|field|headersExchangeWithQueue
specifier|private
name|Endpoint
name|headersExchangeWithQueue
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"rabbitmq:localhost:5672/"
operator|+
literal|"ex7"
operator|+
literal|"?username=cameltest&password=cameltest&exchangeType=headers&autoDelete=false&durable=true&queue=q7&arg.binding.fizz=buzz"
argument_list|)
DECL|field|headersExchangeWithQueueDefiniedInline
specifier|private
name|Endpoint
name|headersExchangeWithQueueDefiniedInline
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"args"
argument_list|)
DECL|field|bindingArgs
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|bindingArgs
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|headersExchangeWithQueue
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|headersExchangeWithQueueDefiniedInline
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|sentMessageIsReceived ()
specifier|public
name|void
name|sentMessageIsReceived
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|to
operator|.
name|expectedHeaderReceived
argument_list|(
name|RabbitMQConstants
operator|.
name|REPLY_TO
argument_list|,
literal|"myReply"
argument_list|)
expr_stmt|;
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
name|properties
operator|.
name|replyTo
argument_list|(
literal|"myReply"
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|connection
argument_list|()
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|EXCHANGE
argument_list|,
literal|""
argument_list|,
name|properties
operator|.
name|build
argument_list|()
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sentMessageIsDeliveryModeSet ()
specifier|public
name|void
name|sentMessageIsDeliveryModeSet
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|to
operator|.
name|expectedHeaderReceived
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_MODE
argument_list|,
literal|1
argument_list|)
expr_stmt|;
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
name|properties
operator|.
name|deliveryMode
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|connection
argument_list|()
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|EXCHANGE
argument_list|,
literal|""
argument_list|,
name|properties
operator|.
name|build
argument_list|()
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sentMessageWithTimestampIsReceived ()
specifier|public
name|void
name|sentMessageWithTimestampIsReceived
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
name|Date
name|timestamp
init|=
name|currentTimestampWithoutMillis
argument_list|()
decl_stmt|;
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|to
operator|.
name|expectedHeaderReceived
argument_list|(
name|RabbitMQConstants
operator|.
name|TIMESTAMP
argument_list|,
name|timestamp
argument_list|)
expr_stmt|;
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
name|properties
operator|.
name|timestamp
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|connection
argument_list|()
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|EXCHANGE
argument_list|,
literal|""
argument_list|,
name|properties
operator|.
name|build
argument_list|()
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
comment|/**      * Tests the proper rabbit binding arguments are in place when the headersExchangeWithQueue is created.      * Should only receive messages with the header [foo=bar]      */
annotation|@
name|Test
DECL|method|sentMessageIsReceivedWithHeadersRouting ()
specifier|public
name|void
name|sentMessageIsReceivedWithHeadersRouting
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
block|{
comment|//should only be one message that makes it through because only
comment|//one has the correct header set
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|connection
argument_list|()
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|HEADERS_EXCHANGE
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|HEADERS_EXCHANGE
argument_list|,
literal|""
argument_list|,
literal|null
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|HEADERS_EXCHANGE
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"foo"
argument_list|,
literal|"bra"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|sentMessageIsReceivedWithHeadersRoutingMultiValueMapBindings ()
specifier|public
name|void
name|sentMessageIsReceivedWithHeadersRoutingMultiValueMapBindings
parameter_list|()
throws|throws
name|Exception
block|{
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|Channel
name|channel
init|=
name|connection
argument_list|()
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
literal|"ex7"
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"fizz"
argument_list|,
literal|"buzz"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
literal|"ex7"
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"fizz"
argument_list|,
literal|"buzz"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
literal|"ex7"
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"fizz"
argument_list|,
literal|"buzz"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
literal|"ex7"
argument_list|,
literal|""
argument_list|,
name|propertiesWithHeader
argument_list|(
literal|"fizz"
argument_list|,
literal|"nope"
argument_list|)
argument_list|,
name|MSG
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|propertiesWithHeader (String headerName, String headerValue)
specifier|private
name|AMQP
operator|.
name|BasicProperties
name|propertiesWithHeader
parameter_list|(
name|String
name|headerName
parameter_list|,
name|String
name|headerValue
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
name|properties
operator|.
name|headers
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|headerName
argument_list|,
name|headerValue
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|properties
operator|.
name|build
argument_list|()
return|;
block|}
DECL|method|currentTimestampWithoutMillis ()
specifier|private
name|Date
name|currentTimestampWithoutMillis
parameter_list|()
block|{
name|Calendar
name|calendar
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|calendar
operator|.
name|set
argument_list|(
name|Calendar
operator|.
name|MILLISECOND
argument_list|,
literal|0
argument_list|)
expr_stmt|;
return|return
name|calendar
operator|.
name|getTime
argument_list|()
return|;
block|}
block|}
end_class

end_unit

