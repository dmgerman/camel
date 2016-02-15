begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.kinesis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|kinesis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|ByteBuffer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|AmazonKinesis
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|model
operator|.
name|PutRecordRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kinesis
operator|.
name|model
operator|.
name|PutRecordResult
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
name|ExchangePattern
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Answers
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
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
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
name|mockito
operator|.
name|Matchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|KinesisProducerTest
specifier|public
class|class
name|KinesisProducerTest
block|{
DECL|field|SHARD_ID
specifier|private
specifier|static
specifier|final
name|String
name|SHARD_ID
init|=
literal|"SHARD145"
decl_stmt|;
DECL|field|SEQUENCE_NUMBER
specifier|private
specifier|static
specifier|final
name|String
name|SEQUENCE_NUMBER
init|=
literal|"SEQ123"
decl_stmt|;
DECL|field|STREAM_NAME
specifier|private
specifier|static
specifier|final
name|String
name|STREAM_NAME
init|=
literal|"streams"
decl_stmt|;
DECL|field|SAMPLE_RECORD_BODY
specifier|private
specifier|static
specifier|final
name|String
name|SAMPLE_RECORD_BODY
init|=
literal|"SAMPLE"
decl_stmt|;
DECL|field|SAMPLE_BUFFER
specifier|private
specifier|static
specifier|final
name|ByteBuffer
name|SAMPLE_BUFFER
init|=
name|ByteBuffer
operator|.
name|wrap
argument_list|(
name|SAMPLE_RECORD_BODY
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Mock
DECL|field|kinesisClient
specifier|private
name|AmazonKinesis
name|kinesisClient
decl_stmt|;
annotation|@
name|Mock
DECL|field|kinesisEndpoint
specifier|private
name|KinesisEndpoint
name|kinesisEndpoint
decl_stmt|;
annotation|@
name|Mock
DECL|field|outMessage
specifier|private
name|Message
name|outMessage
decl_stmt|;
annotation|@
name|Mock
DECL|field|inMessage
specifier|private
name|Message
name|inMessage
decl_stmt|;
annotation|@
name|Mock
DECL|field|putRecordResult
specifier|private
name|PutRecordResult
name|putRecordResult
decl_stmt|;
annotation|@
name|Mock
argument_list|(
name|answer
operator|=
name|Answers
operator|.
name|RETURNS_DEEP_STUBS
argument_list|)
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
DECL|field|kinesisProducer
specifier|private
name|KinesisProducer
name|kinesisProducer
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|kinesisEndpoint
operator|.
name|getClient
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|kinesisClient
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisEndpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"kinesis://etl"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisEndpoint
operator|.
name|getStreamName
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|STREAM_NAME
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|outMessage
argument_list|)
expr_stmt|;
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
name|inMessage
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|exchange
operator|.
name|getPattern
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|inMessage
operator|.
name|getBody
argument_list|(
name|ByteBuffer
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|SAMPLE_BUFFER
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|putRecordResult
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|SEQUENCE_NUMBER
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|putRecordResult
operator|.
name|getShardId
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|SHARD_ID
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisClient
operator|.
name|putRecord
argument_list|(
name|any
argument_list|(
name|PutRecordRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|putRecordResult
argument_list|)
expr_stmt|;
name|kinesisProducer
operator|=
operator|new
name|KinesisProducer
argument_list|(
name|kinesisEndpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldPutRecordInRightStreamWhenProcessingExchange ()
specifier|public
name|void
name|shouldPutRecordInRightStreamWhenProcessingExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|kinesisProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutRecordRequest
argument_list|>
name|capture
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutRecordRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|)
operator|.
name|putRecord
argument_list|(
name|capture
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|PutRecordRequest
name|request
init|=
name|capture
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ByteBuffer
name|byteBuffer
init|=
name|request
operator|.
name|getData
argument_list|()
decl_stmt|;
name|byte
index|[]
name|actualArray
init|=
name|byteBuffer
operator|.
name|array
argument_list|()
decl_stmt|;
name|byte
index|[]
name|sampleArray
init|=
name|SAMPLE_BUFFER
operator|.
name|array
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|sampleArray
argument_list|,
name|actualArray
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|STREAM_NAME
argument_list|,
name|request
operator|.
name|getStreamName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHaveProperHeadersWhenSending ()
specifier|public
name|void
name|shouldHaveProperHeadersWhenSending
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|partitionKey
init|=
literal|"partition"
decl_stmt|;
name|String
name|seqNoForOrdering
init|=
literal|"1851"
decl_stmt|;
name|when
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|seqNoForOrdering
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|PARTITION_KEY
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|partitionKey
argument_list|)
expr_stmt|;
name|kinesisProducer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|ArgumentCaptor
argument_list|<
name|PutRecordRequest
argument_list|>
name|capture
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|PutRecordRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|)
operator|.
name|putRecord
argument_list|(
name|capture
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|PutRecordRequest
name|request
init|=
name|capture
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|partitionKey
argument_list|,
name|request
operator|.
name|getPartitionKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|seqNoForOrdering
argument_list|,
name|request
operator|.
name|getSequenceNumberForOrdering
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|outMessage
argument_list|)
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|SEQUENCE_NUMBER
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|outMessage
argument_list|)
operator|.
name|setHeader
argument_list|(
name|KinesisConstants
operator|.
name|SHARD_ID
argument_list|,
name|SHARD_ID
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

