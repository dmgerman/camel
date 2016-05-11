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
name|Future
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
name|AsyncCallback
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
name|CamelException
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
name|DefaultMessage
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
name|Callback
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
name|ProducerConfig
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
name|ProducerRecord
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
name|apache
operator|.
name|kafka
operator|.
name|common
operator|.
name|errors
operator|.
name|ApiException
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
name|ArgumentCaptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Matchers
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
name|assertEquals
import|;
end_import

begin_class
DECL|class|KafkaProducerTest
specifier|public
class|class
name|KafkaProducerTest
block|{
DECL|field|producer
specifier|private
name|KafkaProducer
name|producer
decl_stmt|;
DECL|field|endpoint
specifier|private
name|KafkaEndpoint
name|endpoint
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
DECL|field|in
specifier|private
name|Message
name|in
init|=
operator|new
name|DefaultMessage
argument_list|()
decl_stmt|;
DECL|field|callback
specifier|private
name|AsyncCallback
name|callback
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|KafkaProducerTest ()
specifier|public
name|KafkaProducerTest
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:broker1:1234,broker2:4567?topic=sometopic"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBrokers
argument_list|(
literal|"broker1:1234,broker2:4567"
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|KafkaProducer
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|RecordMetadata
name|rm
init|=
operator|new
name|RecordMetadata
argument_list|(
literal|null
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|Future
name|future
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Future
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|future
operator|.
name|get
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|rm
argument_list|)
expr_stmt|;
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
name|KafkaProducer
name|kp
init|=
name|Mockito
operator|.
name|mock
argument_list|(
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
name|KafkaProducer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|kp
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|future
argument_list|)
expr_stmt|;
name|producer
operator|.
name|setKafkaProducer
argument_list|(
name|kp
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertyBuilder ()
specifier|public
name|void
name|testPropertyBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
name|producer
operator|.
name|getProps
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"broker1:1234,broker2:4567"
argument_list|,
name|props
operator|.
name|getProperty
argument_list|(
name|ProducerConfig
operator|.
name|BOOTSTRAP_SERVERS_CONFIG
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|processSendsMessage ()
specifier|public
name|void
name|processSendsMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|Matchers
operator|.
name|any
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelException
operator|.
name|class
argument_list|)
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|}
argument_list|)
DECL|method|processSendsMessageWithException ()
specifier|public
name|void
name|processSendsMessageWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
comment|// setup the exception here
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
name|KafkaProducer
name|kp
init|=
name|producer
operator|.
name|getKafkaProducer
argument_list|()
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|kp
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|any
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|ApiException
argument_list|()
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processAsyncSendsMessage ()
specifier|public
name|void
name|processAsyncSendsMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|Matchers
operator|.
name|any
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
argument_list|,
name|Matchers
operator|.
name|any
argument_list|(
name|Callback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processAsyncSendsMessageWithException ()
specifier|public
name|void
name|processAsyncSendsMessageWithException
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
comment|// setup the exception here
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
name|KafkaProducer
name|kp
init|=
name|producer
operator|.
name|getKafkaProducer
argument_list|()
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|kp
operator|.
name|send
argument_list|(
name|Mockito
operator|.
name|any
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
argument_list|,
name|Mockito
operator|.
name|any
argument_list|(
name|Callback
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenThrow
argument_list|(
operator|new
name|ApiException
argument_list|()
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|Matchers
operator|.
name|any
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
argument_list|,
name|Matchers
operator|.
name|any
argument_list|(
name|Callback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|exchange
argument_list|)
operator|.
name|setException
argument_list|(
name|Matchers
operator|.
name|isA
argument_list|(
name|ApiException
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|callback
argument_list|)
operator|.
name|done
argument_list|(
name|Matchers
operator|.
name|eq
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processSendsMessageWithTopicHeaderAndNoTopicInEndPoint ()
specifier|public
name|void
name|processSendsMessageWithTopicHeaderAndNoTopicInEndPoint
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
literal|"anotherTopic"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"anotherTopic"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processSendsMessageWithTopicHeaderAndEndPoint ()
specifier|public
name|void
name|processSendsMessageWithTopicHeaderAndEndPoint
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
literal|"anotherTopic"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"anotherTopic"
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelException
operator|.
name|class
argument_list|)
DECL|method|processRequiresTopicInEndpointOrInHeader ()
specifier|public
name|void
name|processRequiresTopicInEndpointOrInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|null
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processDoesNotRequirePartitionHeader ()
specifier|public
name|void
name|processDoesNotRequirePartitionHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"sometopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processSendsMesssageWithPartitionKeyHeader ()
specifier|public
name|void
name|processSendsMesssageWithPartitionKeyHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"someTopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"someTopic"
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processSendsMesssageWithMessageKeyHeader ()
specifier|public
name|void
name|processSendsMesssageWithMessageKeyHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"someTopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"someTopic"
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processSendMessageWithBridgeEndpoint ()
specifier|public
name|void
name|processSendMessageWithBridgeEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"someTopic"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBridgeEndpoint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
literal|"anotherTopic"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
name|in
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|"4"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"4"
argument_list|,
literal|"someTopic"
argument_list|,
literal|"someKey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
comment|// Message and Topic Name alone
DECL|method|processSendsMesssageWithMessageTopicName ()
specifier|public
name|void
name|processSendsMesssageWithMessageTopicName
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|setTopic
argument_list|(
literal|"someTopic"
argument_list|)
expr_stmt|;
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
name|in
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verifySendMessage
argument_list|(
literal|"someTopic"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|verifySendMessage (String partitionKey, String topic, String messageKey)
specifier|protected
name|void
name|verifySendMessage
parameter_list|(
name|String
name|partitionKey
parameter_list|,
name|String
name|topic
parameter_list|,
name|String
name|messageKey
parameter_list|)
block|{
name|ArgumentCaptor
argument_list|<
name|ProducerRecord
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|new
name|Integer
argument_list|(
name|partitionKey
argument_list|)
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|partition
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messageKey
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|topic
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|verifySendMessage (String topic, String messageKey)
specifier|protected
name|void
name|verifySendMessage
parameter_list|(
name|String
name|topic
parameter_list|,
name|String
name|messageKey
parameter_list|)
block|{
name|ArgumentCaptor
argument_list|<
name|ProducerRecord
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|messageKey
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|key
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|topic
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
DECL|method|verifySendMessage (String topic)
specifier|protected
name|void
name|verifySendMessage
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|ArgumentCaptor
argument_list|<
name|ProducerRecord
argument_list|>
name|captor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|ProducerRecord
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|verify
argument_list|(
name|producer
operator|.
name|getKafkaProducer
argument_list|()
argument_list|)
operator|.
name|send
argument_list|(
name|captor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|topic
argument_list|,
name|captor
operator|.
name|getValue
argument_list|()
operator|.
name|topic
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

