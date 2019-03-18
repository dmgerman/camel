begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockSpan
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|mock
operator|.
name|MockTracer
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
name|opentracing
operator|.
name|SpanDecorator
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
name|assertEquals
import|;
end_import

begin_class
DECL|class|KafkaSpanDecoratorTest
specifier|public
class|class
name|KafkaSpanDecoratorTest
block|{
annotation|@
name|Test
DECL|method|testGetDestinationHeaderTopic ()
specifier|public
name|void
name|testGetDestinationHeaderTopic
parameter_list|()
block|{
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
name|Message
name|message
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|TOPIC
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|KafkaSpanDecorator
name|decorator
init|=
operator|new
name|KafkaSpanDecorator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|decorator
operator|.
name|getDestination
argument_list|(
name|exchange
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetDestinationNoHeaderTopic ()
specifier|public
name|void
name|testGetDestinationNoHeaderTopic
parameter_list|()
block|{
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
name|Message
name|message
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"kafka:localhost:9092?topic=test&groupId=testing&consumersCount=1"
argument_list|)
expr_stmt|;
name|KafkaSpanDecorator
name|decorator
init|=
operator|new
name|KafkaSpanDecorator
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|decorator
operator|.
name|getDestination
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPreOffsetAndPartitionAsStringHeader ()
specifier|public
name|void
name|testPreOffsetAndPartitionAsStringHeader
parameter_list|()
block|{
name|String
name|testKey
init|=
literal|"TestKey"
decl_stmt|;
name|String
name|testOffset
init|=
literal|"TestOffset"
decl_stmt|;
name|String
name|testPartition
init|=
literal|"TestPartition"
decl_stmt|;
name|String
name|testPartitionKey
init|=
literal|"TestPartitionKey"
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|Message
name|message
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
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
name|message
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KEY
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testKey
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|OFFSET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testOffset
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|PARTITION
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testPartition
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|PARTITION_KEY
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testPartitionKey
argument_list|)
expr_stmt|;
name|SpanDecorator
name|decorator
init|=
operator|new
name|KafkaSpanDecorator
argument_list|()
decl_stmt|;
name|MockTracer
name|tracer
init|=
operator|new
name|MockTracer
argument_list|()
decl_stmt|;
name|MockSpan
name|span
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"TestSpan"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|decorator
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testKey
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_KEY_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testOffset
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_OFFSET_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testPartition
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_PARTITION_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|testPartitionKey
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_PARTITION_KEY_TAG
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPrePartitionAsIntegerHeaderAndOffsetAsLongHeader ()
specifier|public
name|void
name|testPrePartitionAsIntegerHeaderAndOffsetAsLongHeader
parameter_list|()
block|{
name|Long
name|testOffset
init|=
literal|4875454L
decl_stmt|;
name|Integer
name|testPartition
init|=
literal|0
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|Message
name|message
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"test"
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
name|message
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|OFFSET
argument_list|,
name|Long
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testOffset
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|message
operator|.
name|getHeader
argument_list|(
name|KafkaSpanDecorator
operator|.
name|PARTITION
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|testPartition
argument_list|)
expr_stmt|;
name|SpanDecorator
name|decorator
init|=
operator|new
name|KafkaSpanDecorator
argument_list|()
decl_stmt|;
name|MockTracer
name|tracer
init|=
operator|new
name|MockTracer
argument_list|()
decl_stmt|;
name|MockSpan
name|span
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
literal|"TestSpan"
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|decorator
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|testOffset
argument_list|)
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_OFFSET_TAG
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|testPartition
argument_list|)
argument_list|,
name|span
operator|.
name|tags
argument_list|()
operator|.
name|get
argument_list|(
name|KafkaSpanDecorator
operator|.
name|KAFKA_PARTITION_TAG
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

