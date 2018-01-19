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
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|Queue
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
name|Processor
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
name|ScheduledBatchPollingConsumer
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
name|util
operator|.
name|CastUtils
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
name|util
operator|.
name|ObjectHelper
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
DECL|class|KinesisConsumer
specifier|public
class|class
name|KinesisConsumer
extends|extends
name|ScheduledBatchPollingConsumer
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
name|KinesisConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|currentShardIterator
specifier|private
name|String
name|currentShardIterator
decl_stmt|;
DECL|field|isShardClosed
specifier|private
name|boolean
name|isShardClosed
decl_stmt|;
DECL|method|KinesisConsumer (KinesisEndpoint endpoint, Processor processor)
specifier|public
name|KinesisConsumer
parameter_list|(
name|KinesisEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|protected
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|GetRecordsRequest
name|req
init|=
operator|new
name|GetRecordsRequest
argument_list|()
operator|.
name|withShardIterator
argument_list|(
name|getShardItertor
argument_list|()
argument_list|)
operator|.
name|withLimit
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMaxResultsPerRequest
argument_list|()
argument_list|)
decl_stmt|;
name|GetRecordsResult
name|result
init|=
name|getClient
argument_list|()
operator|.
name|getRecords
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|createExchanges
argument_list|(
name|result
operator|.
name|getRecords
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|processedExchangeCount
init|=
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
decl_stmt|;
comment|// May cache the last successful sequence number, and pass it to the
comment|// getRecords request. That way, on the next poll, we start from where
comment|// we left off, however, I don't know what happens to subsequent
comment|// exchanges when an earlier echangee fails.
name|currentShardIterator
operator|=
name|result
operator|.
name|getNextShardIterator
argument_list|()
expr_stmt|;
if|if
condition|(
name|isShardClosed
condition|)
block|{
switch|switch
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardClosed
argument_list|()
condition|)
block|{
case|case
name|ignore
case|:
name|LOG
operator|.
name|warn
argument_list|(
literal|"The shard {} is in closed state"
argument_list|)
expr_stmt|;
break|break;
case|case
name|silent
case|:
break|break;
case|case
name|fail
case|:
name|LOG
operator|.
name|info
argument_list|(
literal|"Shard Iterator reaches CLOSE status:"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ReachedClosedStatusException
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|)
throw|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported shard closed strategy"
argument_list|)
throw|;
block|}
block|}
return|return
name|processedExchangeCount
return|;
block|}
annotation|@
name|Override
DECL|method|processBatch (Queue<Object> exchanges)
specifier|public
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|processedExchanges
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|exchanges
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|ObjectHelper
operator|.
name|cast
argument_list|(
name|Exchange
operator|.
name|class
argument_list|,
name|exchanges
operator|.
name|poll
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}] started."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}] done."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|processedExchanges
operator|++
expr_stmt|;
block|}
return|return
name|processedExchanges
return|;
block|}
DECL|method|getClient ()
specifier|private
name|AmazonKinesis
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
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|KinesisEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|KinesisEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getShardItertor ()
specifier|private
name|String
name|getShardItertor
parameter_list|()
block|{
comment|// either return a cached one or get a new one via a GetShardIterator
comment|// request.
if|if
condition|(
name|currentShardIterator
operator|==
literal|null
condition|)
block|{
name|String
name|shardId
decl_stmt|;
comment|// If ShardId supplied use it, else choose first one
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|shardId
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
expr_stmt|;
name|DescribeStreamRequest
name|req1
init|=
operator|new
name|DescribeStreamRequest
argument_list|()
operator|.
name|withStreamName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
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
name|Iterator
name|it
init|=
name|res1
operator|.
name|getStreamDescription
argument_list|()
operator|.
name|getShards
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Shard
name|shard
init|=
operator|(
name|Shard
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|shard
operator|.
name|getShardId
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getShardId
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|shard
operator|.
name|getSequenceNumberRange
argument_list|()
operator|.
name|getEndingSequenceNumber
argument_list|()
operator|==
literal|null
condition|)
block|{
name|isShardClosed
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|isShardClosed
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|DescribeStreamRequest
name|req1
init|=
operator|new
name|DescribeStreamRequest
argument_list|()
operator|.
name|withStreamName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
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
name|shardId
operator|=
name|res1
operator|.
name|getStreamDescription
argument_list|()
operator|.
name|getShards
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getShardId
argument_list|()
expr_stmt|;
if|if
condition|(
name|res1
operator|.
name|getStreamDescription
argument_list|()
operator|.
name|getShards
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getSequenceNumberRange
argument_list|()
operator|.
name|getEndingSequenceNumber
argument_list|()
operator|==
literal|null
condition|)
block|{
name|isShardClosed
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|isShardClosed
operator|=
literal|true
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"ShardId is: {}"
argument_list|,
name|shardId
argument_list|)
expr_stmt|;
name|GetShardIteratorRequest
name|req
init|=
operator|new
name|GetShardIteratorRequest
argument_list|()
operator|.
name|withStreamName
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getStreamName
argument_list|()
argument_list|)
operator|.
name|withShardId
argument_list|(
name|shardId
argument_list|)
operator|.
name|withShardIteratorType
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|hasSequenceNumber
argument_list|()
condition|)
block|{
name|req
operator|.
name|withStartingSequenceNumber
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
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
name|debug
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
DECL|method|createExchanges (List<Record> records)
specifier|private
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|List
argument_list|<
name|Record
argument_list|>
name|records
parameter_list|)
block|{
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Record
name|record
range|:
name|records
control|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|record
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|exchanges
return|;
block|}
DECL|method|hasSequenceNumber ()
specifier|private
name|boolean
name|hasSequenceNumber
parameter_list|()
block|{
return|return
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|(
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
operator|.
name|equals
argument_list|(
name|ShardIteratorType
operator|.
name|AFTER_SEQUENCE_NUMBER
argument_list|)
operator|||
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getIteratorType
argument_list|()
operator|.
name|equals
argument_list|(
name|ShardIteratorType
operator|.
name|AT_SEQUENCE_NUMBER
argument_list|)
operator|)
return|;
block|}
block|}
end_class

end_unit

