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
name|java
operator|.
name|math
operator|.
name|BigInteger
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
name|dynamodbv2
operator|.
name|model
operator|.
name|ShardIteratorType
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
DECL|class|ShardIteratorHandler
class|class
name|ShardIteratorHandler
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ShardIteratorHandler
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|DdbStreamEndpoint
name|endpoint
decl_stmt|;
DECL|field|shardList
specifier|private
specifier|final
name|ShardList
name|shardList
init|=
operator|new
name|ShardList
argument_list|()
decl_stmt|;
DECL|field|currentShardIterator
specifier|private
name|String
name|currentShardIterator
decl_stmt|;
DECL|field|currentShard
specifier|private
name|Shard
name|currentShard
decl_stmt|;
DECL|method|ShardIteratorHandler (DdbStreamEndpoint endpoint)
name|ShardIteratorHandler
parameter_list|(
name|DdbStreamEndpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|getShardIterator (String resumeFromSequenceNumber)
name|String
name|getShardIterator
parameter_list|(
name|String
name|resumeFromSequenceNumber
parameter_list|)
block|{
name|ShardIteratorType
name|iteratorType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
decl_stmt|;
name|String
name|sequenceNumber
init|=
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
decl_stmt|;
if|if
condition|(
name|resumeFromSequenceNumber
operator|!=
literal|null
condition|)
block|{
comment|// Reset things as we're in an error condition.
name|currentShard
operator|=
literal|null
expr_stmt|;
name|currentShardIterator
operator|=
literal|null
expr_stmt|;
name|iteratorType
operator|=
name|ShardIteratorType
operator|.
name|AFTER_SEQUENCE_NUMBER
expr_stmt|;
name|sequenceNumber
operator|=
name|resumeFromSequenceNumber
expr_stmt|;
block|}
comment|// either return a cached one or get a new one via a GetShardIterator request.
if|if
condition|(
name|currentShardIterator
operator|==
literal|null
condition|)
block|{
name|ListStreamsResult
name|streamsListResult
init|=
name|getClient
argument_list|()
operator|.
name|listStreams
argument_list|(
operator|new
name|ListStreamsRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getTableName
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|String
name|streamArn
init|=
name|streamsListResult
operator|.
name|getStreams
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getStreamArn
argument_list|()
decl_stmt|;
comment|// XXX assumes there is only one stream
name|DescribeStreamResult
name|streamDescriptionResult
init|=
name|getClient
argument_list|()
operator|.
name|describeStream
argument_list|(
operator|new
name|DescribeStreamRequest
argument_list|()
operator|.
name|withStreamArn
argument_list|(
name|streamArn
argument_list|)
argument_list|)
decl_stmt|;
name|shardList
operator|.
name|addAll
argument_list|(
name|streamDescriptionResult
operator|.
name|getStreamDescription
argument_list|()
operator|.
name|getShards
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Current shard is: {} (in {})"
argument_list|,
name|currentShard
argument_list|,
name|shardList
argument_list|)
expr_stmt|;
if|if
condition|(
name|currentShard
operator|==
literal|null
condition|)
block|{
name|currentShard
operator|=
name|resolveNewShard
argument_list|(
name|iteratorType
argument_list|,
name|resumeFromSequenceNumber
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentShard
operator|=
name|shardList
operator|.
name|nextAfter
argument_list|(
name|currentShard
argument_list|)
expr_stmt|;
block|}
name|shardList
operator|.
name|removeOlderThan
argument_list|(
name|currentShard
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Next shard is: {} (in {})"
argument_list|,
name|currentShard
argument_list|,
name|shardList
argument_list|)
expr_stmt|;
name|GetShardIteratorResult
name|result
init|=
name|getClient
argument_list|()
operator|.
name|getShardIterator
argument_list|(
name|buildGetShardIteratorRequest
argument_list|(
name|streamArn
argument_list|,
name|iteratorType
argument_list|,
name|sequenceNumber
argument_list|)
argument_list|)
decl_stmt|;
name|currentShardIterator
operator|=
name|result
operator|.
name|getShardIterator
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shard Iterator is: {}"
argument_list|,
name|currentShardIterator
argument_list|)
expr_stmt|;
return|return
name|currentShardIterator
return|;
block|}
DECL|method|buildGetShardIteratorRequest (final String streamArn, ShardIteratorType iteratorType, String sequenceNumber)
specifier|private
name|GetShardIteratorRequest
name|buildGetShardIteratorRequest
parameter_list|(
specifier|final
name|String
name|streamArn
parameter_list|,
name|ShardIteratorType
name|iteratorType
parameter_list|,
name|String
name|sequenceNumber
parameter_list|)
block|{
name|GetShardIteratorRequest
name|req
init|=
operator|new
name|GetShardIteratorRequest
argument_list|()
operator|.
name|withStreamArn
argument_list|(
name|streamArn
argument_list|)
operator|.
name|withShardId
argument_list|(
name|currentShard
operator|.
name|getShardId
argument_list|()
argument_list|)
operator|.
name|withShardIteratorType
argument_list|(
name|iteratorType
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|iteratorType
condition|)
block|{
case|case
name|AFTER_SEQUENCE_NUMBER
case|:
case|case
name|AT_SEQUENCE_NUMBER
case|:
comment|// if you request with a sequence number that is LESS than the
comment|// start of the shard, you get a HTTP 400 from AWS.
comment|// So only add the sequence number if the endpoints
comment|// sequence number is less than or equal to the starting
comment|// sequence for the shard.
comment|// Otherwise change the shart iterator type to trim_horizon
comment|// because we get a 400 when we use one of the
comment|// {at,after}_sequence_number iterator types and don't supply
comment|// a sequence number.
if|if
condition|(
name|BigIntComparisons
operator|.
name|Conditions
operator|.
name|LTEQ
operator|.
name|matches
argument_list|(
operator|new
name|BigInteger
argument_list|(
name|currentShard
operator|.
name|getSequenceNumberRange
argument_list|()
operator|.
name|getStartingSequenceNumber
argument_list|()
argument_list|)
argument_list|,
operator|new
name|BigInteger
argument_list|(
name|sequenceNumber
argument_list|)
argument_list|)
condition|)
block|{
name|req
operator|=
name|req
operator|.
name|withSequenceNumber
argument_list|(
name|sequenceNumber
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|req
operator|=
name|req
operator|.
name|withShardIteratorType
argument_list|(
name|ShardIteratorType
operator|.
name|TRIM_HORIZON
argument_list|)
expr_stmt|;
block|}
break|break;
default|default:
block|}
return|return
name|req
return|;
block|}
DECL|method|resolveNewShard (ShardIteratorType type, String resumeFrom)
specifier|private
name|Shard
name|resolveNewShard
parameter_list|(
name|ShardIteratorType
name|type
parameter_list|,
name|String
name|resumeFrom
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|AFTER_SEQUENCE_NUMBER
case|:
return|return
name|shardList
operator|.
name|afterSeq
argument_list|(
name|resumeFrom
operator|!=
literal|null
condition|?
name|resumeFrom
else|:
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
return|;
case|case
name|AT_SEQUENCE_NUMBER
case|:
return|return
name|shardList
operator|.
name|atSeq
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
return|;
case|case
name|TRIM_HORIZON
case|:
return|return
name|shardList
operator|.
name|first
argument_list|()
return|;
case|case
name|LATEST
case|:
default|default:
return|return
name|shardList
operator|.
name|last
argument_list|()
return|;
block|}
block|}
DECL|method|updateShardIterator (String nextShardIterator)
name|void
name|updateShardIterator
parameter_list|(
name|String
name|nextShardIterator
parameter_list|)
block|{
name|this
operator|.
name|currentShardIterator
operator|=
name|nextShardIterator
expr_stmt|;
block|}
DECL|method|getEndpoint ()
name|DdbStreamEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|getClient ()
specifier|private
name|AmazonDynamoDBStreams
name|getClient
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getClient
argument_list|()
return|;
block|}
block|}
end_class

end_unit

