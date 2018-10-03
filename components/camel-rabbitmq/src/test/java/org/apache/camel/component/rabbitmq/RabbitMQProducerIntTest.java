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
name|HashMap
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
name|support
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
DECL|class|RabbitMQProducerIntTest
specifier|public
class|class
name|RabbitMQProducerIntTest
extends|extends
name|AbstractRabbitMQIntTest
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|RabbitMQProducerIntTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|EXCHANGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCHANGE
init|=
literal|"ex1"
decl_stmt|;
DECL|field|ROUTE
specifier|private
specifier|static
specifier|final
name|String
name|ROUTE
init|=
literal|"route1"
decl_stmt|;
DECL|field|CUSTOM_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|CUSTOM_HEADER
init|=
literal|"CustomHeader"
decl_stmt|;
DECL|field|BASIC_URI_FORMAT
specifier|private
specifier|static
specifier|final
name|String
name|BASIC_URI_FORMAT
init|=
literal|"rabbitmq:localhost:5672/%s?routingKey=%s&username=cameltest&password=cameltest&skipQueueDeclare=true"
decl_stmt|;
DECL|field|BASIC_URI
specifier|private
specifier|static
specifier|final
name|String
name|BASIC_URI
init|=
name|String
operator|.
name|format
argument_list|(
name|BASIC_URI_FORMAT
argument_list|,
name|EXCHANGE
argument_list|,
name|ROUTE
argument_list|)
decl_stmt|;
DECL|field|ALLOW_NULL_HEADERS
specifier|private
specifier|static
specifier|final
name|String
name|ALLOW_NULL_HEADERS
init|=
name|BASIC_URI
operator|+
literal|"&allowNullHeaders=true"
decl_stmt|;
DECL|field|PUBLISHER_ACKNOWLEDGES_URI
specifier|private
specifier|static
specifier|final
name|String
name|PUBLISHER_ACKNOWLEDGES_URI
init|=
name|BASIC_URI
operator|+
literal|"&mandatory=true&publisherAcknowledgements=true"
decl_stmt|;
DECL|field|PUBLISHER_ACKNOWLEDGES_BAD_ROUTE_URI
specifier|private
specifier|static
specifier|final
name|String
name|PUBLISHER_ACKNOWLEDGES_BAD_ROUTE_URI
init|=
name|String
operator|.
name|format
argument_list|(
name|BASIC_URI_FORMAT
argument_list|,
name|EXCHANGE
argument_list|,
literal|"route2"
argument_list|)
operator|+
literal|"&publisherAcknowledgements=true"
decl_stmt|;
DECL|field|GUARANTEED_DELIVERY_URI
specifier|private
specifier|static
specifier|final
name|String
name|GUARANTEED_DELIVERY_URI
init|=
name|BASIC_URI
operator|+
literal|"&mandatory=true&guaranteedDeliveries=true"
decl_stmt|;
DECL|field|GUARANTEED_DELIVERY_BAD_ROUTE_NOT_MANDATORY_URI
specifier|private
specifier|static
specifier|final
name|String
name|GUARANTEED_DELIVERY_BAD_ROUTE_NOT_MANDATORY_URI
init|=
name|String
operator|.
name|format
argument_list|(
name|BASIC_URI_FORMAT
argument_list|,
name|EXCHANGE
argument_list|,
literal|"route2"
argument_list|)
operator|+
literal|"&guaranteedDeliveries=true"
decl_stmt|;
DECL|field|GUARANTEED_DELIVERY_BAD_ROUTE_URI
specifier|private
specifier|static
specifier|final
name|String
name|GUARANTEED_DELIVERY_BAD_ROUTE_URI
init|=
name|String
operator|.
name|format
argument_list|(
name|BASIC_URI_FORMAT
argument_list|,
name|EXCHANGE
argument_list|,
literal|"route2"
argument_list|)
operator|+
literal|"&mandatory=true&guaranteedDeliveries=true"
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
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-allow-null-headers"
argument_list|)
DECL|field|templateAllowNullHeaders
specifier|protected
name|ProducerTemplate
name|templateAllowNullHeaders
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-with-confirms"
argument_list|)
DECL|field|templateWithConfirms
specifier|protected
name|ProducerTemplate
name|templateWithConfirms
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-with-confirms-bad-route"
argument_list|)
DECL|field|templateWithConfirmsAndBadRoute
specifier|protected
name|ProducerTemplate
name|templateWithConfirmsAndBadRoute
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-with-guaranteed-delivery"
argument_list|)
DECL|field|templateWithGuranteedDelivery
specifier|protected
name|ProducerTemplate
name|templateWithGuranteedDelivery
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-with-guaranteed-delivery-bad-route"
argument_list|)
DECL|field|templateWithGuranteedDeliveryAndBadRoute
specifier|protected
name|ProducerTemplate
name|templateWithGuranteedDeliveryAndBadRoute
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start-with-guaranteed-delivery-bad-route-but-not-mandatory"
argument_list|)
DECL|field|templateWithGuranteedDeliveryBadRouteButNotMandatory
specifier|protected
name|ProducerTemplate
name|templateWithGuranteedDeliveryBadRouteButNotMandatory
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
name|BASIC_URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-allow-null-headers"
argument_list|)
operator|.
name|to
argument_list|(
name|ALLOW_NULL_HEADERS
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-with-confirms"
argument_list|)
operator|.
name|to
argument_list|(
name|PUBLISHER_ACKNOWLEDGES_URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-with-confirms-bad-route"
argument_list|)
operator|.
name|to
argument_list|(
name|PUBLISHER_ACKNOWLEDGES_BAD_ROUTE_URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-with-guaranteed-delivery"
argument_list|)
operator|.
name|to
argument_list|(
name|GUARANTEED_DELIVERY_URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-with-guaranteed-delivery-bad-route"
argument_list|)
operator|.
name|to
argument_list|(
name|GUARANTEED_DELIVERY_BAD_ROUTE_URI
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start-with-guaranteed-delivery-bad-route-but-not-mandatory"
argument_list|)
operator|.
name|to
argument_list|(
name|GUARANTEED_DELIVERY_BAD_ROUTE_NOT_MANDATORY_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
name|ROUTE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDownRabbitMQ ()
specifier|public
name|void
name|tearDownRabbitMQ
parameter_list|()
throws|throws
name|Exception
block|{
name|channel
operator|.
name|abort
argument_list|()
expr_stmt|;
name|connection
operator|.
name|abort
argument_list|()
expr_stmt|;
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
throws|,
name|TimeoutException
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
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
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
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|,
literal|"new message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producedMessageWithNotNullHeaders ()
specifier|public
name|void
name|producedMessageWithNotNullHeaders
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
name|headers
operator|.
name|put
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
name|EXCHANGE
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|CUSTOM_HEADER
argument_list|,
name|CUSTOM_HEADER
operator|.
name|toLowerCase
argument_list|()
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
name|ArrayPopulatingConsumer
argument_list|(
name|received
argument_list|,
name|receivedHeaders
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"new message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertThatBodiesAndHeadersReceivedIn
argument_list|(
name|receivedHeaders
argument_list|,
name|headers
argument_list|,
name|received
argument_list|,
literal|"new message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producedMessageAllowNullHeaders ()
specifier|public
name|void
name|producedMessageAllowNullHeaders
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
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
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
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
name|headers
operator|.
name|put
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|CUSTOM_HEADER
argument_list|,
literal|null
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
name|ArrayPopulatingConsumer
argument_list|(
name|received
argument_list|,
name|receivedHeaders
argument_list|)
argument_list|)
expr_stmt|;
name|templateAllowNullHeaders
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"new message"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|assertThatBodiesAndHeadersReceivedIn
argument_list|(
name|receivedHeaders
argument_list|,
name|headers
argument_list|,
name|received
argument_list|,
literal|"new message"
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
DECL|method|assertThatBodiesAndHeadersReceivedIn (Map<String, Object> receivedHeaders, Map<String, Object> expectedHeaders, final List<String> received, final String... expected)
specifier|private
name|void
name|assertThatBodiesAndHeadersReceivedIn
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|expectedHeaders
parameter_list|,
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
name|headers
range|:
name|expectedHeaders
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|receivedValue
init|=
name|receivedHeaders
operator|.
name|get
argument_list|(
name|headers
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|expectedValue
init|=
name|headers
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Header key "
operator|+
name|headers
operator|.
name|getKey
argument_list|()
operator|+
literal|" not found"
argument_list|,
name|receivedHeaders
operator|.
name|containsKey
argument_list|(
name|headers
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|receivedValue
operator|==
literal|null
condition|?
literal|""
else|:
name|receivedValue
operator|.
name|toString
argument_list|()
argument_list|,
name|expectedValue
operator|==
literal|null
condition|?
literal|""
else|:
name|expectedValue
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|producedMessageIsReceivedWhenPublisherAcknowledgementsAreEnabled ()
specifier|public
name|void
name|producedMessageIsReceivedWhenPublisherAcknowledgementsAreEnabled
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
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
name|templateWithConfirms
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"publisher ack message"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
literal|"ex1"
argument_list|)
expr_stmt|;
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|,
literal|"publisher ack message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producedMessageIsReceivedWhenPublisherAcknowledgementsAreEnabledAndBadRoutingKeyIsUsed ()
specifier|public
name|void
name|producedMessageIsReceivedWhenPublisherAcknowledgementsAreEnabledAndBadRoutingKeyIsUsed
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
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
name|templateWithConfirmsAndBadRoute
operator|.
name|sendBody
argument_list|(
literal|"publisher ack message"
argument_list|)
expr_stmt|;
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSuccessfullyProduceMessageWhenGuaranteedDeliveryIsActivatedAndMessageIsMarkedAsMandatory ()
specifier|public
name|void
name|shouldSuccessfullyProduceMessageWhenGuaranteedDeliveryIsActivatedAndMessageIsMarkedAsMandatory
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
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
name|templateWithGuranteedDelivery
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"publisher ack message"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
literal|"ex1"
argument_list|)
expr_stmt|;
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|,
literal|"publisher ack message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|RuntimeCamelException
operator|.
name|class
argument_list|)
DECL|method|shouldFailIfMessageIsMarkedAsMandatoryAndGuaranteedDeliveryIsActiveButNoQueueIsBound ()
specifier|public
name|void
name|shouldFailIfMessageIsMarkedAsMandatoryAndGuaranteedDeliveryIsActiveButNoQueueIsBound
parameter_list|()
block|{
name|templateWithGuranteedDeliveryAndBadRoute
operator|.
name|sendBody
argument_list|(
literal|"publish with ack and return message"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSuccessfullyProduceMessageWhenGuaranteedDeliveryIsActivatedOnABadRouteButMessageIsNotMandatory ()
specifier|public
name|void
name|shouldSuccessfullyProduceMessageWhenGuaranteedDeliveryIsActivatedOnABadRouteButMessageIsNotMandatory
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
throws|,
name|TimeoutException
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
name|channel
operator|.
name|basicConsume
argument_list|(
literal|"sammyq"
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
name|templateWithGuranteedDeliveryBadRouteButNotMandatory
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"publisher ack message"
argument_list|,
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|,
literal|"ex1"
argument_list|)
expr_stmt|;
name|assertThatBodiesReceivedIn
argument_list|(
name|received
argument_list|)
expr_stmt|;
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
DECL|field|receivedHeaders
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
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
name|RabbitMQProducerIntTest
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
name|receivedHeaders
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|ArrayPopulatingConsumer (final List<String> received, Map<String, Object> receivedHeaders)
name|ArrayPopulatingConsumer
parameter_list|(
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|received
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|receivedHeaders
parameter_list|)
block|{
name|super
argument_list|(
name|RabbitMQProducerIntTest
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
name|this
operator|.
name|receivedHeaders
operator|=
name|receivedHeaders
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
name|LOGGER
operator|.
name|info
argument_list|(
literal|"AMQP.BasicProperties: {}"
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|receivedHeaders
operator|.
name|putAll
argument_list|(
name|properties
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
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

