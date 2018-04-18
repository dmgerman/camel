begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.splunk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|splunk
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|component
operator|.
name|splunk
operator|.
name|event
operator|.
name|SplunkEvent
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
name|splunk
operator|.
name|support
operator|.
name|SplunkDataReader
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
name|splunk
operator|.
name|support
operator|.
name|SplunkResultProcessor
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

begin_comment
comment|/**  * The Splunk consumer.  */
end_comment

begin_class
DECL|class|SplunkConsumer
specifier|public
class|class
name|SplunkConsumer
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
name|SplunkConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataReader
specifier|private
name|SplunkDataReader
name|dataReader
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SplunkEndpoint
name|endpoint
decl_stmt|;
DECL|method|SplunkConsumer (SplunkEndpoint endpoint, Processor processor, ConsumerType consumerType)
specifier|public
name|SplunkConsumer
parameter_list|(
name|SplunkEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ConsumerType
name|consumerType
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
if|if
condition|(
name|consumerType
operator|.
name|equals
argument_list|(
name|ConsumerType
operator|.
name|NORMAL
argument_list|)
operator|||
name|consumerType
operator|.
name|equals
argument_list|(
name|ConsumerType
operator|.
name|REALTIME
argument_list|)
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSearch
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing option 'search' with normal or realtime search"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|consumerType
operator|.
name|equals
argument_list|(
name|ConsumerType
operator|.
name|SAVEDSEARCH
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getSavedSearch
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Missing option 'savedSearch' with saved search"
argument_list|)
throw|;
block|}
name|dataReader
operator|=
operator|new
name|SplunkDataReader
argument_list|(
name|endpoint
argument_list|,
name|consumerType
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
try|try
block|{
if|if
condition|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isStreaming
argument_list|()
condition|)
block|{
name|dataReader
operator|.
name|read
argument_list|(
operator|new
name|SplunkResultProcessor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|SplunkEvent
name|splunkEvent
parameter_list|)
block|{
specifier|final
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|splunkEvent
argument_list|)
expr_stmt|;
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
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
literal|"Done processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Return 0: no exchanges returned by poll, as exchanges have been returned asynchronously
return|return
literal|0
return|;
block|}
else|else
block|{
name|List
argument_list|<
name|SplunkEvent
argument_list|>
name|events
init|=
name|dataReader
operator|.
name|read
argument_list|()
decl_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|exchanges
init|=
name|createExchanges
argument_list|(
name|events
argument_list|)
decl_stmt|;
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|exchanges
argument_list|)
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|endpoint
operator|.
name|reset
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|0
return|;
block|}
block|}
DECL|method|createExchanges (List<SplunkEvent> splunkEvents)
specifier|protected
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|createExchanges
parameter_list|(
name|List
argument_list|<
name|SplunkEvent
argument_list|>
name|splunkEvents
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Received {} messages in this poll"
argument_list|,
name|splunkEvents
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|SplunkEvent
name|splunkEvent
range|:
name|splunkEvents
control|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|splunkEvent
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
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
name|total
init|=
name|exchanges
operator|.
name|size
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|total
operator|&&
name|isBatchAllowed
argument_list|()
condition|;
name|index
operator|++
control|)
block|{
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
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
name|index
operator|==
name|total
operator|-
literal|1
argument_list|)
expr_stmt|;
try|try
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange [{}]..."
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|total
return|;
block|}
block|}
end_class

end_unit

