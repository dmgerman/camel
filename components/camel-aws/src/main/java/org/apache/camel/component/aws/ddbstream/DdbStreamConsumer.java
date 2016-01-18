begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|dynamodbv2
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
name|dynamodbv2
operator|.
name|model
operator|.
name|Record
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
DECL|class|DdbStreamConsumer
specifier|public
class|class
name|DdbStreamConsumer
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
name|DdbStreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|shardIteratorHandler
specifier|private
specifier|final
name|ShardIteratorHandler
name|shardIteratorHandler
decl_stmt|;
DECL|method|DdbStreamConsumer (DdbStreamEndpoint endpoint, Processor processor)
specifier|public
name|DdbStreamConsumer
parameter_list|(
name|DdbStreamEndpoint
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
name|shardIteratorHandler
operator|=
operator|new
name|ShardIteratorHandler
argument_list|(
name|endpoint
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
name|shardIteratorHandler
operator|.
name|getShardIterator
argument_list|()
argument_list|)
operator|.
name|withLimit
argument_list|(
name|getEndpoint
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
name|shardIteratorHandler
operator|.
name|updateShardIterator
argument_list|(
name|result
operator|.
name|getNextShardIterator
argument_list|()
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|DdbStreamEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|DdbStreamEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
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
name|BigIntComparisons
name|condition
decl_stmt|;
name|BigInteger
name|providedSeqNum
init|=
literal|null
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
name|condition
operator|=
name|BigIntComparisons
operator|.
name|Conditions
operator|.
name|LT
expr_stmt|;
name|providedSeqNum
operator|=
operator|new
name|BigInteger
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumberProvider
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
name|condition
operator|=
name|BigIntComparisons
operator|.
name|Conditions
operator|.
name|LTEQ
expr_stmt|;
name|providedSeqNum
operator|=
operator|new
name|BigInteger
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getSequenceNumberProvider
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
name|condition
operator|=
literal|null
expr_stmt|;
block|}
for|for
control|(
name|Record
name|record
range|:
name|records
control|)
block|{
name|BigInteger
name|recordSeqNum
init|=
operator|new
name|BigInteger
argument_list|(
name|record
operator|.
name|getDynamodb
argument_list|()
operator|.
name|getSequenceNumber
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|condition
operator|==
literal|null
operator|||
name|condition
operator|.
name|matches
argument_list|(
name|providedSeqNum
argument_list|,
name|recordSeqNum
argument_list|)
condition|)
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
block|}
return|return
name|exchanges
return|;
block|}
block|}
end_class

end_unit

