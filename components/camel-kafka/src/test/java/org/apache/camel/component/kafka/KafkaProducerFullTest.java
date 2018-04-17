begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
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
name|Arrays
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
name|Properties
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
name|CountDownLatch
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
name|TimeUnit
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
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerRecords
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|KafkaConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|RecordMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
DECL|class|KafkaProducerFullTest
specifier|public
class|class
name|KafkaProducerFullTest
extends|extends
name|BaseEmbeddedKafkaTest
block|{
DECL|field|TOPIC_STRINGS
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_STRINGS
init|=
literal|"test"
decl_stmt|;
DECL|field|TOPIC_INTERCEPTED
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_INTERCEPTED
init|=
literal|"test"
decl_stmt|;
DECL|field|TOPIC_STRINGS_IN_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_STRINGS_IN_HEADER
init|=
literal|"testHeader"
decl_stmt|;
DECL|field|TOPIC_BYTES
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_BYTES
init|=
literal|"testBytes"
decl_stmt|;
DECL|field|TOPIC_BYTES_IN_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_BYTES_IN_HEADER
init|=
literal|"testBytesHeader"
decl_stmt|;
DECL|field|GROUP_BYTES
specifier|private
specifier|static
specifier|final
name|String
name|GROUP_BYTES
init|=
literal|"groupStrings"
decl_stmt|;
DECL|field|stringsConsumerConn
specifier|private
specifier|static
name|KafkaConsumer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|stringsConsumerConn
decl_stmt|;
DECL|field|bytesConsumerConn
specifier|private
specifier|static
name|KafkaConsumer
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|bytesConsumerConn
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"kafka:"
operator|+
name|TOPIC_STRINGS
operator|+
literal|"?requestRequiredAcks=-1"
argument_list|)
DECL|field|toStrings
specifier|private
name|Endpoint
name|toStrings
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"kafka:"
operator|+
name|TOPIC_STRINGS
operator|+
literal|"?requestRequiredAcks=-1&partitionKey=1"
argument_list|)
DECL|field|toStrings2
specifier|private
name|Endpoint
name|toStrings2
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"kafka:"
operator|+
name|TOPIC_INTERCEPTED
operator|+
literal|"?requestRequiredAcks=-1"
operator|+
literal|"&interceptorClasses=org.apache.camel.component.kafka.MockProducerInterceptor"
argument_list|)
DECL|field|toStringsWithInterceptor
specifier|private
name|Endpoint
name|toStringsWithInterceptor
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:kafkaAck"
argument_list|)
DECL|field|mockEndpoint
specifier|private
name|MockEndpoint
name|mockEndpoint
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"kafka:"
operator|+
name|TOPIC_BYTES
operator|+
literal|"?requestRequiredAcks=-1"
operator|+
literal|"&serializerClass=org.apache.kafka.common.serialization.ByteArraySerializer&"
operator|+
literal|"keySerializerClass=org.apache.kafka.common.serialization.ByteArraySerializer"
argument_list|)
DECL|field|toBytes
specifier|private
name|Endpoint
name|toBytes
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startStrings"
argument_list|)
DECL|field|stringsTemplate
specifier|private
name|ProducerTemplate
name|stringsTemplate
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startStrings2"
argument_list|)
DECL|field|stringsTemplate2
specifier|private
name|ProducerTemplate
name|stringsTemplate2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startBytes"
argument_list|)
DECL|field|bytesTemplate
specifier|private
name|ProducerTemplate
name|bytesTemplate
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:startTraced"
argument_list|)
DECL|field|interceptedTemplate
specifier|private
name|ProducerTemplate
name|interceptedTemplate
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|before ()
specifier|public
specifier|static
name|void
name|before
parameter_list|()
block|{
name|Properties
name|stringsProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|,
literal|"localhost:"
operator|+
name|getKafkaPort
argument_list|()
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|GROUP_ID_CONFIG
argument_list|,
literal|"DemoConsumer"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|ENABLE_AUTO_COMMIT_CONFIG
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|AUTO_COMMIT_INTERVAL_MS_CONFIG
argument_list|,
literal|"1000"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|SESSION_TIMEOUT_MS_CONFIG
argument_list|,
literal|"30000"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|KEY_DESERIALIZER_CLASS_CONFIG
argument_list|,
literal|"org.apache.kafka.common.serialization.StringDeserializer"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|VALUE_DESERIALIZER_CLASS_CONFIG
argument_list|,
literal|"org.apache.kafka.common.serialization.StringDeserializer"
argument_list|)
expr_stmt|;
name|stringsProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|AUTO_OFFSET_RESET_CONFIG
argument_list|,
literal|"earliest"
argument_list|)
expr_stmt|;
name|stringsConsumerConn
operator|=
operator|new
name|KafkaConsumer
argument_list|<>
argument_list|(
name|stringsProps
argument_list|)
expr_stmt|;
name|Properties
name|bytesProps
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|bytesProps
operator|.
name|putAll
argument_list|(
name|stringsProps
argument_list|)
expr_stmt|;
name|bytesProps
operator|.
name|put
argument_list|(
literal|"group.id"
argument_list|,
name|GROUP_BYTES
argument_list|)
expr_stmt|;
name|bytesProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|KEY_DESERIALIZER_CLASS_CONFIG
argument_list|,
literal|"org.apache.kafka.common.serialization.ByteArrayDeserializer"
argument_list|)
expr_stmt|;
name|bytesProps
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|ConsumerConfig
operator|.
name|VALUE_DESERIALIZER_CLASS_CONFIG
argument_list|,
literal|"org.apache.kafka.common.serialization.ByteArrayDeserializer"
argument_list|)
expr_stmt|;
name|bytesConsumerConn
operator|=
operator|new
name|KafkaConsumer
argument_list|<>
argument_list|(
name|bytesProps
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|after ()
specifier|public
specifier|static
name|void
name|after
parameter_list|()
block|{
name|stringsConsumerConn
operator|.
name|close
argument_list|()
expr_stmt|;
name|bytesConsumerConn
operator|.
name|close
argument_list|()
expr_stmt|;
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
literal|"direct:startStrings"
argument_list|)
operator|.
name|to
argument_list|(
name|toStrings
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startStrings2"
argument_list|)
operator|.
name|to
argument_list|(
name|toStrings2
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startBytes"
argument_list|)
operator|.
name|to
argument_list|(
name|toBytes
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:startTraced"
argument_list|)
operator|.
name|to
argument_list|(
name|toStringsWithInterceptor
argument_list|)
operator|.
name|to
argument_list|(
name|mockEndpoint
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|producedStringMessageIsReceivedByKafka ()
specifier|public
name|void
name|producedStringMessageIsReceivedByKafka
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|int
name|messageInTopic
init|=
literal|10
decl_stmt|;
name|int
name|messageInOtherTopic
init|=
literal|5
decl_stmt|;
name|CountDownLatch
name|messagesLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|)
decl_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInTopic
argument_list|,
name|stringsTemplate
argument_list|,
literal|"IT test message"
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInOtherTopic
argument_list|,
name|stringsTemplate
argument_list|,
literal|"IT test message in other topic"
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|,
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|)
expr_stmt|;
name|createKafkaMessageConsumer
argument_list|(
name|stringsConsumerConn
argument_list|,
name|TOPIC_STRINGS
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|,
name|messagesLatch
argument_list|)
expr_stmt|;
name|boolean
name|allMessagesReceived
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not all messages were published to the kafka topics. Not received: "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|allMessagesReceived
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fifteen Exchanges are expected"
argument_list|,
name|exchangeList
operator|.
name|size
argument_list|()
argument_list|,
literal|15
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchangeList
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetaData1
init|=
call|(
name|List
argument_list|<
name|RecordMetadata
argument_list|>
call|)
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"One RecordMetadata is expected."
argument_list|,
name|recordMetaData1
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Offset is positive"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|offset
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Topic Name start with 'test'"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|topic
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|producedString2MessageIsReceivedByKafka ()
specifier|public
name|void
name|producedString2MessageIsReceivedByKafka
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|int
name|messageInTopic
init|=
literal|10
decl_stmt|;
name|int
name|messageInOtherTopic
init|=
literal|5
decl_stmt|;
name|CountDownLatch
name|messagesLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|)
decl_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInTopic
argument_list|,
name|stringsTemplate2
argument_list|,
literal|"IT test message"
argument_list|,
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInOtherTopic
argument_list|,
name|stringsTemplate2
argument_list|,
literal|"IT test message in other topic"
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|,
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|)
expr_stmt|;
name|createKafkaMessageConsumer
argument_list|(
name|stringsConsumerConn
argument_list|,
name|TOPIC_STRINGS
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|,
name|messagesLatch
argument_list|)
expr_stmt|;
name|boolean
name|allMessagesReceived
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not all messages were published to the kafka topics. Not received: "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|allMessagesReceived
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fifteen Exchanges are expected"
argument_list|,
name|exchangeList
operator|.
name|size
argument_list|()
argument_list|,
literal|15
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchangeList
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetaData1
init|=
call|(
name|List
argument_list|<
name|RecordMetadata
argument_list|>
call|)
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"One RecordMetadata is expected."
argument_list|,
name|recordMetaData1
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Offset is positive"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|offset
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Topic Name start with 'test'"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|topic
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|producedStringMessageIsIntercepted ()
specifier|public
name|void
name|producedStringMessageIsIntercepted
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|int
name|messageInTopic
init|=
literal|10
decl_stmt|;
name|int
name|messageInOtherTopic
init|=
literal|5
decl_stmt|;
name|CountDownLatch
name|messagesLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|)
decl_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInTopic
argument_list|,
name|interceptedTemplate
argument_list|,
literal|"IT test message"
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInOtherTopic
argument_list|,
name|interceptedTemplate
argument_list|,
literal|"IT test message in other topic"
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|,
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|)
expr_stmt|;
name|createKafkaMessageConsumer
argument_list|(
name|stringsConsumerConn
argument_list|,
name|TOPIC_INTERCEPTED
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|,
name|messagesLatch
argument_list|)
expr_stmt|;
name|boolean
name|allMessagesReceived
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not all messages were published to the kafka topics. Not received: "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|allMessagesReceived
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|,
name|MockProducerInterceptor
operator|.
name|recordsCaptured
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|producedStringCollectionMessageIsReceivedByKafka ()
specifier|public
name|void
name|producedStringCollectionMessageIsReceivedByKafka
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|int
name|messageInTopic
init|=
literal|10
decl_stmt|;
name|int
name|messageInOtherTopic
init|=
literal|5
decl_stmt|;
name|CountDownLatch
name|messagesLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|msgs
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|messageInTopic
condition|;
name|x
operator|++
control|)
block|{
name|msgs
operator|.
name|add
argument_list|(
literal|"Message "
operator|+
name|x
argument_list|)
expr_stmt|;
block|}
name|sendMessagesInRoute
argument_list|(
literal|1
argument_list|,
name|stringsTemplate
argument_list|,
name|msgs
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|msgs
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
name|messageInOtherTopic
condition|;
name|x
operator|++
control|)
block|{
name|msgs
operator|.
name|add
argument_list|(
literal|"Other Message "
operator|+
name|x
argument_list|)
expr_stmt|;
block|}
name|sendMessagesInRoute
argument_list|(
literal|1
argument_list|,
name|stringsTemplate
argument_list|,
name|msgs
argument_list|,
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
argument_list|,
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|)
expr_stmt|;
name|createKafkaMessageConsumer
argument_list|(
name|stringsConsumerConn
argument_list|,
name|TOPIC_STRINGS
argument_list|,
name|TOPIC_STRINGS_IN_HEADER
argument_list|,
name|messagesLatch
argument_list|)
expr_stmt|;
name|boolean
name|allMessagesReceived
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not all messages were published to the kafka topics. Not received: "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|allMessagesReceived
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Two Exchanges are expected"
argument_list|,
name|exchangeList
operator|.
name|size
argument_list|()
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|Exchange
name|e1
init|=
name|exchangeList
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetaData1
init|=
call|(
name|List
argument_list|<
name|RecordMetadata
argument_list|>
call|)
argument_list|(
name|e1
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Ten RecordMetadata is expected."
argument_list|,
name|recordMetaData1
operator|.
name|size
argument_list|()
argument_list|,
literal|10
argument_list|)
expr_stmt|;
for|for
control|(
name|RecordMetadata
name|recordMeta
range|:
name|recordMetaData1
control|)
block|{
name|assertTrue
argument_list|(
literal|"Offset is positive"
argument_list|,
name|recordMeta
operator|.
name|offset
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Topic Name start with 'test'"
argument_list|,
name|recordMeta
operator|.
name|topic
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Exchange
name|e2
init|=
name|exchangeList
operator|.
name|get
argument_list|(
literal|1
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetaData2
init|=
call|(
name|List
argument_list|<
name|RecordMetadata
argument_list|>
call|)
argument_list|(
name|e2
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Five RecordMetadata is expected."
argument_list|,
name|recordMetaData2
operator|.
name|size
argument_list|()
argument_list|,
literal|5
argument_list|)
expr_stmt|;
for|for
control|(
name|RecordMetadata
name|recordMeta
range|:
name|recordMetaData2
control|)
block|{
name|assertTrue
argument_list|(
literal|"Offset is positive"
argument_list|,
name|recordMeta
operator|.
name|offset
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Topic Name start with 'test'"
argument_list|,
name|recordMeta
operator|.
name|topic
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|producedBytesMessageIsReceivedByKafka ()
specifier|public
name|void
name|producedBytesMessageIsReceivedByKafka
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|int
name|messageInTopic
init|=
literal|10
decl_stmt|;
name|int
name|messageInOtherTopic
init|=
literal|5
decl_stmt|;
name|CountDownLatch
name|messagesLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|messageInTopic
operator|+
name|messageInOtherTopic
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inTopicHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|inTopicHeaders
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInTopic
argument_list|,
name|bytesTemplate
argument_list|,
literal|"IT test message"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|inTopicHeaders
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|otherTopicHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|otherTopicHeaders
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"1"
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|otherTopicHeaders
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
name|TOPIC_BYTES_IN_HEADER
argument_list|)
expr_stmt|;
name|sendMessagesInRoute
argument_list|(
name|messageInOtherTopic
argument_list|,
name|bytesTemplate
argument_list|,
literal|"IT test message in other topic"
operator|.
name|getBytes
argument_list|()
argument_list|,
name|otherTopicHeaders
argument_list|)
expr_stmt|;
name|createKafkaBytesMessageConsumer
argument_list|(
name|bytesConsumerConn
argument_list|,
name|TOPIC_BYTES
argument_list|,
name|TOPIC_BYTES_IN_HEADER
argument_list|,
name|messagesLatch
argument_list|)
expr_stmt|;
name|boolean
name|allMessagesReceived
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|200
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Not all messages were published to the kafka topics. Not received: "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|allMessagesReceived
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchangeList
init|=
name|mockEndpoint
operator|.
name|getExchanges
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Fifteen Exchanges are expected"
argument_list|,
name|exchangeList
operator|.
name|size
argument_list|()
argument_list|,
literal|15
argument_list|)
expr_stmt|;
for|for
control|(
name|Exchange
name|exchange
range|:
name|exchangeList
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|RecordMetadata
argument_list|>
name|recordMetaData1
init|=
call|(
name|List
argument_list|<
name|RecordMetadata
argument_list|>
call|)
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KAFKA_RECORDMETA
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"One RecordMetadata is expected."
argument_list|,
name|recordMetaData1
operator|.
name|size
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Offset is positive"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|offset
argument_list|()
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Topic Name start with 'test'"
argument_list|,
name|recordMetaData1
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|topic
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createKafkaMessageConsumer (KafkaConsumer<String, String> consumerConn, String topic, String topicInHeader, CountDownLatch messagesLatch)
specifier|private
name|void
name|createKafkaMessageConsumer
parameter_list|(
name|KafkaConsumer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|consumerConn
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|topicInHeader
parameter_list|,
name|CountDownLatch
name|messagesLatch
parameter_list|)
block|{
name|consumerConn
operator|.
name|subscribe
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|topic
argument_list|,
name|topicInHeader
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|run
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|run
condition|)
block|{
name|ConsumerRecords
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|records
init|=
name|consumerConn
operator|.
name|poll
argument_list|(
literal|100
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|records
operator|.
name|count
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|messagesLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|messagesLatch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|run
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createKafkaBytesMessageConsumer (KafkaConsumer<byte[], byte[]> consumerConn, String topic, String topicInHeader, CountDownLatch messagesLatch)
specifier|private
name|void
name|createKafkaBytesMessageConsumer
parameter_list|(
name|KafkaConsumer
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|consumerConn
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|topicInHeader
parameter_list|,
name|CountDownLatch
name|messagesLatch
parameter_list|)
block|{
name|consumerConn
operator|.
name|subscribe
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|topic
argument_list|,
name|topicInHeader
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|run
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|run
condition|)
block|{
name|ConsumerRecords
argument_list|<
name|byte
index|[]
argument_list|,
name|byte
index|[]
argument_list|>
name|records
init|=
name|consumerConn
operator|.
name|poll
argument_list|(
literal|100
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|records
operator|.
name|count
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|messagesLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
if|if
condition|(
name|messagesLatch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
condition|)
block|{
name|run
operator|=
literal|false
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|sendMessagesInRoute (int messages, ProducerTemplate template, Object bodyOther, String... headersWithValue)
specifier|private
name|void
name|sendMessagesInRoute
parameter_list|(
name|int
name|messages
parameter_list|,
name|ProducerTemplate
name|template
parameter_list|,
name|Object
name|bodyOther
parameter_list|,
name|String
modifier|...
name|headersWithValue
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|headersWithValue
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headersWithValue
operator|.
name|length
condition|;
name|i
operator|=
name|i
operator|+
literal|2
control|)
block|{
name|headerMap
operator|.
name|put
argument_list|(
name|headersWithValue
index|[
name|i
index|]
argument_list|,
name|headersWithValue
index|[
name|i
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
name|sendMessagesInRoute
argument_list|(
name|messages
argument_list|,
name|template
argument_list|,
name|bodyOther
argument_list|,
name|headerMap
argument_list|)
expr_stmt|;
block|}
DECL|method|sendMessagesInRoute (int messages, ProducerTemplate template, Object bodyOther, Map<String, Object> headerMap)
specifier|private
name|void
name|sendMessagesInRoute
parameter_list|(
name|int
name|messages
parameter_list|,
name|ProducerTemplate
name|template
parameter_list|,
name|Object
name|bodyOther
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerMap
parameter_list|)
block|{
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
name|messages
condition|;
name|k
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|bodyOther
argument_list|,
name|headerMap
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

