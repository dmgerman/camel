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
name|impl
operator|.
name|DefaultCamelContext
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
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_class
DECL|class|RabbitMQProducerTest
specifier|public
class|class
name|RabbitMQProducerTest
block|{
DECL|field|endpoint
specifier|private
name|RabbitMQEndpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|message
specifier|private
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|conn
specifier|private
name|Connection
name|conn
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Connection
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|IOException
throws|,
name|TimeoutException
block|{
name|Mockito
operator|.
name|when
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|connect
argument_list|(
name|any
argument_list|(
name|ExecutorService
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|conn
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|conn
operator|.
name|createChannel
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getMessageConverter
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|RabbitMQMessageConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesContentTypeHeader ()
specifier|public
name|void
name|testPropertiesUsesContentTypeHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"application/json"
argument_list|,
name|props
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesCorrelationHeader ()
specifier|public
name|void
name|testPropertiesUsesCorrelationHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CORRELATIONID
argument_list|,
literal|"124544"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"124544"
argument_list|,
name|props
operator|.
name|getCorrelationId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesUserIdHeader ()
specifier|public
name|void
name|testPropertiesUsesUserIdHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|USERID
argument_list|,
literal|"abcd"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abcd"
argument_list|,
name|props
operator|.
name|getUserId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesMessageIdHeader ()
specifier|public
name|void
name|testPropertiesUsesMessageIdHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|MESSAGE_ID
argument_list|,
literal|"abvasweaqQQ"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abvasweaqQQ"
argument_list|,
name|props
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesDeliveryModeHeader ()
specifier|public
name|void
name|testPropertiesUsesDeliveryModeHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_MODE
argument_list|,
literal|"444"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|444
argument_list|,
name|props
operator|.
name|getDeliveryMode
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesClusterIdHeader ()
specifier|public
name|void
name|testPropertiesUsesClusterIdHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CLUSTERID
argument_list|,
literal|"abtasg5r"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"abtasg5r"
argument_list|,
name|props
operator|.
name|getClusterId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesReplyToHeader ()
specifier|public
name|void
name|testPropertiesUsesReplyToHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|REPLY_TO
argument_list|,
literal|"bbbbdfgdfg"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"bbbbdfgdfg"
argument_list|,
name|props
operator|.
name|getReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesPriorityHeader ()
specifier|public
name|void
name|testPropertiesUsesPriorityHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|PRIORITY
argument_list|,
literal|"15"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|15
argument_list|,
name|props
operator|.
name|getPriority
argument_list|()
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesExpirationHeader ()
specifier|public
name|void
name|testPropertiesUsesExpirationHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXPIRATION
argument_list|,
literal|"thursday"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"thursday"
argument_list|,
name|props
operator|.
name|getExpiration
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesTypeHeader ()
specifier|public
name|void
name|testPropertiesUsesTypeHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TYPE
argument_list|,
literal|"sometype"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sometype"
argument_list|,
name|props
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesContentEncodingHeader ()
specifier|public
name|void
name|testPropertiesUsesContentEncodingHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"qwergghdfdfgdfgg"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"qwergghdfdfgdfgg"
argument_list|,
name|props
operator|.
name|getContentEncoding
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesAppIdHeader ()
specifier|public
name|void
name|testPropertiesAppIdHeader
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|APP_ID
argument_list|,
literal|"qweeqwe"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"qweeqwe"
argument_list|,
name|props
operator|.
name|getAppId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesTimestampHeaderAsLongValue ()
specifier|public
name|void
name|testPropertiesUsesTimestampHeaderAsLongValue
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|TIMESTAMP
argument_list|,
literal|"12345123"
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|12345123
argument_list|,
name|props
operator|.
name|getTimestamp
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesTimestampHeaderAsDateValue ()
specifier|public
name|void
name|testPropertiesUsesTimestampHeaderAsDateValue
parameter_list|()
throws|throws
name|IOException
block|{
name|Date
name|timestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
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
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|timestamp
argument_list|,
name|props
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesUsesCustomHeaders ()
specifier|public
name|void
name|testPropertiesUsesCustomHeaders
parameter_list|()
throws|throws
name|IOException
block|{
name|RabbitMQProducer
name|producer
init|=
operator|new
name|RabbitMQProducer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|customHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"stringHeader"
argument_list|,
literal|"A string"
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"bigDecimalHeader"
argument_list|,
operator|new
name|BigDecimal
argument_list|(
literal|"12.34"
argument_list|)
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"integerHeader"
argument_list|,
literal|42
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"doubleHeader"
argument_list|,
literal|42.24
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"booleanHeader"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"dateHeader"
argument_list|,
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"byteArrayHeader"
argument_list|,
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|customHeaders
operator|.
name|put
argument_list|(
literal|"invalidHeader"
argument_list|,
operator|new
name|Something
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeaders
argument_list|(
name|customHeaders
argument_list|)
expr_stmt|;
name|AMQP
operator|.
name|BasicProperties
name|props
init|=
name|producer
operator|.
name|buildProperties
argument_list|(
name|exchange
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A string"
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"stringHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|BigDecimal
argument_list|(
literal|"12.34"
argument_list|)
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"bigDecimalHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"integerHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42.24
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"doubleHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"booleanHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Date
argument_list|(
literal|0
argument_list|)
argument_list|,
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"dateHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
literal|"foo"
operator|.
name|getBytes
argument_list|()
argument_list|,
operator|(
name|byte
index|[]
operator|)
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"byteArrayHeader"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|props
operator|.
name|getHeaders
argument_list|()
operator|.
name|get
argument_list|(
literal|"invalidHeader"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|class|Something
specifier|private
specifier|static
class|class
name|Something
block|{     }
block|}
end_class

end_unit

