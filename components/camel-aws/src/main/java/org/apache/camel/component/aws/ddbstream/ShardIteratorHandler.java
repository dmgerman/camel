begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
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
DECL|method|getShardIterator ()
name|String
name|getShardIterator
parameter_list|()
block|{
comment|// either return a cached one or get a new one via a GetShardIterator request.
if|if
condition|(
name|currentShardIterator
operator|==
literal|null
condition|)
block|{
name|ListStreamsRequest
name|req0
init|=
operator|new
name|ListStreamsRequest
argument_list|()
operator|.
name|withTableName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getTableName
argument_list|()
argument_list|)
decl_stmt|;
name|ListStreamsResult
name|res0
init|=
name|getClient
argument_list|()
operator|.
name|listStreams
argument_list|(
name|req0
argument_list|)
decl_stmt|;
specifier|final
name|String
name|streamArn
init|=
name|res0
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
name|DescribeStreamRequest
name|req1
init|=
operator|new
name|DescribeStreamRequest
argument_list|()
operator|.
name|withStreamArn
argument_list|(
name|streamArn
argument_list|)
decl_stmt|;
name|DescribeStreamResult
name|res1
init|=
name|getClient
argument_list|()
operator|.
name|describeStream
argument_list|(
name|req1
argument_list|)
decl_stmt|;
name|shardList
operator|.
name|addAll
argument_list|(
name|res1
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
switch|switch
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getIteratorType
argument_list|()
condition|)
block|{
case|case
name|AFTER_SEQUENCE_NUMBER
case|:
name|currentShard
operator|=
name|shardList
operator|.
name|afterSeq
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|AT_SEQUENCE_NUMBER
case|:
name|currentShard
operator|=
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
expr_stmt|;
break|break;
case|case
name|TRIM_HORIZON
case|:
name|currentShard
operator|=
name|shardList
operator|.
name|first
argument_list|()
expr_stmt|;
break|break;
case|case
name|LATEST
case|:
default|default:
name|currentShard
operator|=
name|shardList
operator|.
name|last
argument_list|()
expr_stmt|;
break|break;
block|}
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
name|getEndpoint
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getIteratorType
argument_list|()
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
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
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
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
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
name|GetShardIteratorResult
name|result
init|=
name|getClient
argument_list|()
operator|.
name|getShardIterator
argument_list|(
name|req
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

