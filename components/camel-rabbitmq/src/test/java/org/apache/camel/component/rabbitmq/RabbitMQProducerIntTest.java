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
name|Produce
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
name|ProducerTemplate
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
DECL|class|RabbitMQProducerIntTest
specifier|public
class|class
name|RabbitMQProducerIntTest
extends|extends
name|CamelTestSupport
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
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
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
literal|"?routingKey=route1&username=cameltest&password=cameltest"
argument_list|)
DECL|field|to
specifier|private
name|Endpoint
name|to
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
literal|"direct:start"
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
DECL|method|producedMessageIsReceived ()
specifier|public
name|void
name|producedMessageIsReceived
parameter_list|()
throws|throws
name|InterruptedException
throws|,
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
name|setHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPort
argument_list|(
literal|5672
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setUsername
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setPassword
argument_list|(
literal|"cameltest"
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setVirtualHost
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|Connection
name|conn
init|=
name|factory
operator|.
name|newConnection
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Envelope
argument_list|>
name|received
init|=
operator|new
name|ArrayList
argument_list|<
name|Envelope
argument_list|>
argument_list|()
decl_stmt|;
name|Channel
name|channel
init|=
name|conn
operator|.
name|createChannel
argument_list|()
decl_stmt|;
name|channel
operator|.
name|queueDeclare
argument_list|(
literal|"sammyq"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|channel
operator|.
name|queueBind
argument_list|(
literal|"sammyq"
argument_list|,
name|EXCHANGE
argument_list|,
literal|"route1"
argument_list|)
expr_stmt|;
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
argument_list|,
literal|true
argument_list|,
operator|new
name|DefaultConsumer
argument_list|(
name|channel
argument_list|)
block|{
annotation|@
name|Override
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
name|envelope
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"new message"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
literal|"ex1"
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|received
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

