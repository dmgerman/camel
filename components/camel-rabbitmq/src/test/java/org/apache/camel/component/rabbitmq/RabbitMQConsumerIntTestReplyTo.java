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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|DefaultConsumer
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
name|junit
operator|.
name|Before
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

begin_comment
comment|/**  * Integration test to check if requested direct reply messages are received  */
end_comment

begin_class
DECL|class|RabbitMQConsumerIntTestReplyTo
specifier|public
class|class
name|RabbitMQConsumerIntTestReplyTo
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
literal|"ex_reply"
decl_stmt|;
DECL|field|ROUTING_KEY
specifier|private
specifier|static
specifier|final
name|String
name|ROUTING_KEY
init|=
literal|"testreply"
decl_stmt|;
DECL|field|REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST
init|=
literal|"Knock! Knock!"
decl_stmt|;
DECL|field|REPLY
specifier|private
specifier|static
specifier|final
name|String
name|REPLY
init|=
literal|"Hello world"
decl_stmt|;
DECL|field|QUEUE
specifier|private
specifier|static
specifier|final
name|String
name|QUEUE
init|=
literal|"amq.rabbitmq.reply-to"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"rabbitmq:localhost:5672/"
operator|+
name|EXCHANGE
operator|+
literal|"?routingKey="
operator|+
name|ROUTING_KEY
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
annotation|@
name|Before
DECL|method|setUpRabbitMQ ()
specifier|public
name|void
name|setUpRabbitMQ
parameter_list|()
throws|throws
name|Exception
block|{
name|connection
operator|=
name|connection
argument_list|()
expr_stmt|;
name|channel
operator|=
name|connection
operator|.
name|createChannel
argument_list|()
expr_stmt|;
comment|//        channel.queueDeclare("sammyq", false, false, true, null);
comment|//        channel.queueBind("sammyq", EXCHANGE, ROUTE);
block|}
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
name|context
argument_list|()
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|info
argument_list|(
literal|"Building routes..."
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|log
argument_list|(
name|body
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|setBody
argument_list|(
name|simple
argument_list|(
name|REPLY
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|replyMessageIsReceived ()
specifier|public
name|void
name|replyMessageIsReceived
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
throws|,
name|InterruptedException
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|received
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
name|prop
init|=
operator|new
name|AMQP
operator|.
name|BasicProperties
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|prop
operator|.
name|replyTo
argument_list|(
name|QUEUE
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicConsume
argument_list|(
name|QUEUE
argument_list|,
literal|true
argument_list|,
operator|new
name|ArrayPopulatingConsumer
argument_list|(
name|received
argument_list|)
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicPublish
argument_list|(
name|EXCHANGE
argument_list|,
name|ROUTING_KEY
argument_list|,
name|prop
operator|.
name|build
argument_list|()
argument_list|,
name|REQUEST
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|,
name|REPLY
argument_list|)
expr_stmt|;
block|}
DECL|method|assertThatBodiesReceivedIn (final List<String> received, final String... expected)
specifier|private
name|void
name|assertThatBodiesReceivedIn
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|received
parameter_list|,
specifier|final
name|String
modifier|...
name|expected
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertListSize
argument_list|(
name|received
argument_list|,
name|expected
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|body
range|:
name|expected
control|)
block|{
name|assertEquals
argument_list|(
name|body
argument_list|,
name|received
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|ArrayPopulatingConsumer
specifier|private
class|class
name|ArrayPopulatingConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|received
specifier|private
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|received
decl_stmt|;
DECL|method|ArrayPopulatingConsumer (final List<String> received)
name|ArrayPopulatingConsumer
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|received
parameter_list|)
block|{
name|super
argument_list|(
name|RabbitMQConsumerIntTestReplyTo
operator|.
name|this
operator|.
name|channel
argument_list|)
expr_stmt|;
name|this
operator|.
name|received
operator|=
name|received
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
name|received
operator|.
name|add
argument_list|(
operator|new
name|String
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

