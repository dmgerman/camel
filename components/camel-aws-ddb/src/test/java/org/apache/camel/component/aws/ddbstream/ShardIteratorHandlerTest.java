begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ddbstream
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
name|ddbstream
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|dynamodbv2
operator|.
name|AmazonDynamoDBStreams
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
name|dynamodbv2
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
name|dynamodbv2
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
name|dynamodbv2
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
name|dynamodbv2
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ListStreamsRequest
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ListStreamsResult
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
name|dynamodbv2
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
name|dynamodbv2
operator|.
name|model
operator|.
name|Stream
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
name|dynamodbv2
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
name|invocation
operator|.
name|InvocationOnMock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|stubbing
operator|.
name|Answer
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
name|*
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
DECL|class|ShardIteratorHandlerTest
specifier|public
class|class
name|ShardIteratorHandlerTest
block|{
DECL|field|undertest
specifier|private
name|ShardIteratorHandler
name|undertest
decl_stmt|;
annotation|@
name|Mock
DECL|field|amazonDynamoDBStreams
specifier|private
name|AmazonDynamoDBStreams
name|amazonDynamoDBStreams
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
name|DdbStreamComponent
name|component
init|=
operator|new
name|DdbStreamComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|DdbStreamEndpoint
name|endpoint
init|=
operator|new
name|DdbStreamEndpoint
argument_list|(
literal|null
argument_list|,
operator|new
name|DdbStreamConfiguration
argument_list|()
argument_list|,
name|component
argument_list|)
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
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setAmazonDynamoDbStreamsClient
argument_list|(
name|amazonDynamoDBStreams
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|start
argument_list|()
expr_stmt|;
name|undertest
operator|=
operator|new
name|ShardIteratorHandler
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|amazonDynamoDBStreams
operator|.
name|listStreams
argument_list|(
name|any
argument_list|(
name|ListStreamsRequest
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|ListStreamsResult
argument_list|()
operator|.
name|withStreams
argument_list|(
operator|new
name|Stream
argument_list|()
operator|.
name|withStreamArn
argument_list|(
literal|"arn:aws:dynamodb:region:12345:table/table_name/stream/timestamp"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|amazonDynamoDBStreams
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
name|withTableName
argument_list|(
literal|"table_name"
argument_list|)
operator|.
name|withShards
argument_list|(
name|ShardListTest
operator|.
name|createShardsWithSequenceNumbers
argument_list|(
literal|null
argument_list|,
literal|"a"
argument_list|,
literal|"1"
argument_list|,
literal|"5"
argument_list|,
literal|"b"
argument_list|,
literal|"8"
argument_list|,
literal|"15"
argument_list|,
literal|"c"
argument_list|,
literal|"16"
argument_list|,
literal|"16"
argument_list|,
literal|"d"
argument_list|,
literal|"20"
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|amazonDynamoDBStreams
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
name|thenAnswer
argument_list|(
operator|new
name|Answer
argument_list|<
name|GetShardIteratorResult
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|GetShardIteratorResult
name|answer
parameter_list|(
name|InvocationOnMock
name|invocation
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
operator|new
name|GetShardIteratorResult
argument_list|()
operator|.
name|withShardIterator
argument_list|(
literal|"shard_iterator_"
operator|+
operator|(
operator|(
name|GetShardIteratorRequest
operator|)
name|invocation
operator|.
name|getArguments
argument_list|()
index|[
literal|0
index|]
operator|)
operator|.
name|getShardId
argument_list|()
operator|+
literal|"_000"
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|latestOnlyUsesTheLastShard ()
specifier|public
name|void
name|latestOnlyUsesTheLastShard
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|LATEST
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"shard_iterator_d_000"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|cachesRecentShardId ()
specifier|public
name|void
name|cachesRecentShardId
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|LATEST
argument_list|)
expr_stmt|;
name|undertest
operator|.
name|updateShardIterator
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|amazonDynamoDBStreams
argument_list|,
name|times
argument_list|(
literal|0
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
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|trimHorizonStartsWithTheFirstShard ()
specifier|public
name|void
name|trimHorizonStartsWithTheFirstShard
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|TRIM_HORIZON
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"shard_iterator_a_000"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|trimHorizonWalksAllShards ()
specifier|public
name|void
name|trimHorizonWalksAllShards
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|TRIM_HORIZON
argument_list|)
expr_stmt|;
name|String
index|[]
name|shardIterators
init|=
operator|new
name|String
index|[
literal|4
index|]
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
name|shardIterators
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|shardIterators
index|[
name|i
index|]
operator|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|undertest
operator|.
name|updateShardIterator
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|,
name|times
argument_list|(
literal|4
argument_list|)
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|String
index|[]
name|shards
init|=
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|,
literal|"d"
block|}
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
name|shards
operator|.
name|length
condition|;
operator|++
name|i
control|)
block|{
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getAllValues
argument_list|()
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
name|shards
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|assertThat
argument_list|(
name|shardIterators
argument_list|,
name|is
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"shard_iterator_a_000"
block|,
literal|"shard_iterator_b_000"
block|,
literal|"shard_iterator_c_000"
block|,
literal|"shard_iterator_d_000"
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|atSeqNumber12StartsWithShardB ()
specifier|public
name|void
name|atSeqNumber12StartsWithShardB
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|AT_SEQUENCE_NUMBER
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setSequenceNumberProvider
argument_list|(
operator|new
name|StaticSequenceNumberProvider
argument_list|(
literal|"12"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"shard_iterator_b_000"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|afterSeqNumber16StartsWithShardD ()
specifier|public
name|void
name|afterSeqNumber16StartsWithShardD
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|AFTER_SEQUENCE_NUMBER
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setSequenceNumberProvider
argument_list|(
operator|new
name|StaticSequenceNumberProvider
argument_list|(
literal|"16"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"d"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"shard_iterator_d_000"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|resumingFromSomewhereActuallyUsesTheAfterSequenceNumber ()
specifier|public
name|void
name|resumingFromSomewhereActuallyUsesTheAfterSequenceNumber
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|LATEST
argument_list|)
expr_stmt|;
name|String
name|shardIterator
init|=
name|undertest
operator|.
name|getShardIterator
argument_list|(
literal|"12"
argument_list|)
decl_stmt|;
name|ArgumentCaptor
argument_list|<
name|GetShardIteratorRequest
argument_list|>
name|getIteratorCaptor
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
name|amazonDynamoDBStreams
argument_list|)
operator|.
name|getShardIterator
argument_list|(
name|getIteratorCaptor
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"b"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|shardIterator
argument_list|,
name|is
argument_list|(
literal|"shard_iterator_b_000"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getShardIteratorType
argument_list|()
argument_list|,
name|is
argument_list|(
name|ShardIteratorType
operator|.
name|AFTER_SEQUENCE_NUMBER
operator|.
name|name
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|getIteratorCaptor
operator|.
name|getValue
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"12"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

