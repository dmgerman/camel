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
name|util
operator|.
name|Date
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
name|DescribeStreamRequest
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
name|DescribeStreamResult
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
name|GetRecordsRequest
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
name|GetRecordsResult
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
name|GetShardIteratorRequest
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
name|GetShardIteratorResult
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
name|Record
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
name|Shard
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
name|ShardIteratorType
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
name|StreamDescription
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
name|AsyncProcessor
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
name|CamelContext
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
name|DefaultCamelContext
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
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
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
name|assertThat
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
name|times
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
DECL|class|KinesisConsumerTest
specifier|public
class|class
name|KinesisConsumerTest
block|{
annotation|@
name|Mock
DECL|field|kinesisClient
specifier|private
name|AmazonKinesis
name|kinesisClient
decl_stmt|;
annotation|@
name|Mock
DECL|field|processor
specifier|private
name|AsyncProcessor
name|processor
decl_stmt|;
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|component
specifier|private
specifier|final
name|KinesisComponent
name|component
init|=
operator|new
name|KinesisComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
DECL|field|undertest
specifier|private
name|KinesisConsumer
name|undertest
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
name|KinesisEndpoint
name|endpoint
init|=
operator|new
name|KinesisEndpoint
argument_list|(
literal|null
argument_list|,
literal|"streamName"
argument_list|,
name|component
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setAmazonKinesisClient
argument_list|(
name|kinesisClient
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|LATEST
argument_list|)
expr_stmt|;
name|undertest
operator|=
operator|new
name|KinesisConsumer
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisClient
operator|.
name|getRecords
argument_list|(
name|any
argument_list|(
name|GetRecordsRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|GetRecordsResult
argument_list|()
operator|.
name|withNextShardIterator
argument_list|(
literal|"nextShardIterator"
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisClient
operator|.
name|describeStream
argument_list|(
name|any
argument_list|(
name|DescribeStreamRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|DescribeStreamResult
argument_list|()
operator|.
name|withStreamDescription
argument_list|(
operator|new
name|StreamDescription
argument_list|()
operator|.
name|withShards
argument_list|(
operator|new
name|Shard
argument_list|()
operator|.
name|withShardId
argument_list|(
literal|"shardId"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|kinesisClient
operator|.
name|getShardIterator
argument_list|(
name|any
argument_list|(
name|GetShardIteratorRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|GetShardIteratorResult
argument_list|()
operator|.
name|withShardIterator
argument_list|(
literal|"shardIterator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|itObtainsAShardIteratorOnFirstPoll ()
specifier|public
name|void
name|itObtainsAShardIteratorOnFirstPoll
parameter_list|()
throws|throws
name|Exception
block|{
name|undertest
operator|.
name|poll
argument_list|()
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|DescribeStreamRequest
argument_list|>
name|describeStreamReqCap
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|DescribeStreamRequest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getShardIteratorReqCap
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|GetShardIteratorRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|)
operator|.
name|describeStream
argument_list|(
name|describeStreamReqCap
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|describeStreamReqCap
operator|.
name|getValue
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"streamName"
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getShardIteratorReqCap
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getShardIteratorReqCap
operator|.
name|getValue
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"streamName"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getShardIteratorReqCap
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"shardId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getShardIteratorReqCap
operator|.
name|getValue
argument_list|()
operator|.
name|getShardIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"LATEST"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|itUsesTheShardIteratorOnPolls ()
specifier|public
name|void
name|itUsesTheShardIteratorOnPolls
parameter_list|()
throws|throws
name|Exception
block|{
name|undertest
operator|.
name|poll
argument_list|()
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|GetRecordsRequest
argument_list|>
name|getRecordsReqCap
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|GetRecordsRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|)
operator|.
name|getRecords
argument_list|(
name|getRecordsReqCap
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRecordsReqCap
operator|.
name|getValue
argument_list|()
operator|.
name|getShardIterator
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"shardIterator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|itUsesTheShardIteratorOnSubsiquentPolls ()
specifier|public
name|void
name|itUsesTheShardIteratorOnSubsiquentPolls
parameter_list|()
throws|throws
name|Exception
block|{
name|undertest
operator|.
name|poll
argument_list|()
expr_stmt|;
name|undertest
operator|.
name|poll
argument_list|()
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|GetRecordsRequest
argument_list|>
name|getRecordsReqCap
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|GetRecordsRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|describeStream
argument_list|(
name|any
argument_list|(
name|DescribeStreamRequest
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|,
name|times
argument_list|(
literal|1
argument_list|)
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|any
argument_list|(
name|GetShardIteratorRequest
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|kinesisClient
argument_list|,
name|times
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|getRecords
argument_list|(
name|getRecordsReqCap
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRecordsReqCap
operator|.
name|getAllValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getShardIterator
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"shardIterator"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getRecordsReqCap
operator|.
name|getAllValues
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getShardIterator
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"nextShardIterator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|recordsAreSentToTheProcessor ()
specifier|public
name|void
name|recordsAreSentToTheProcessor
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|kinesisClient
operator|.
name|getRecords
argument_list|(
name|any
argument_list|(
name|GetRecordsRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|GetRecordsResult
argument_list|()
operator|.
name|withNextShardIterator
argument_list|(
literal|"nextShardIterator"
argument_list|)
operator|.
name|withRecords
argument_list|(
operator|new
name|Record
argument_list|()
operator|.
name|withSequenceNumber
argument_list|(
literal|"1"
argument_list|)
argument_list|,
operator|new
name|Record
argument_list|()
operator|.
name|withSequenceNumber
argument_list|(
literal|"2"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|int
name|messageCount
init|=
name|undertest
operator|.
name|poll
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|messageCount
argument_list|,
name|is
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|Exchange
argument_list|>
name|exchangeCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|,
name|times
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
name|exchangeCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|any
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getAllValues
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Record
operator|.
name|class
argument_list|)
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getAllValues
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Record
operator|.
name|class
argument_list|)
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|exchangePropertiesAreSet ()
specifier|public
name|void
name|exchangePropertiesAreSet
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|kinesisClient
operator|.
name|getRecords
argument_list|(
name|any
argument_list|(
name|GetRecordsRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|GetRecordsResult
argument_list|()
operator|.
name|withNextShardIterator
argument_list|(
literal|"nextShardIterator"
argument_list|)
operator|.
name|withRecords
argument_list|(
operator|new
name|Record
argument_list|()
operator|.
name|withSequenceNumber
argument_list|(
literal|"1"
argument_list|)
operator|.
name|withApproximateArrivalTimestamp
argument_list|(
operator|new
name|Date
argument_list|(
literal|42
argument_list|)
argument_list|)
operator|.
name|withPartitionKey
argument_list|(
literal|"shardId"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|undertest
operator|.
name|poll
argument_list|()
expr_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|Exchange
argument_list|>
name|exchangeCaptor
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|process
argument_list|(
name|exchangeCaptor
operator|.
name|capture
argument_list|()
argument_list|,
name|any
argument_list|(
name|AsyncCallback
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|APPROX_ARRIVAL_TIME
argument_list|,
name|long
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
literal|42L
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|PARTITION_KEY
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"shardId"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|SEQUENCE_NUMBER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|exchangeCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KinesisConstants
operator|.
name|SHARD_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|is
argument_list|(
literal|"shardId"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

