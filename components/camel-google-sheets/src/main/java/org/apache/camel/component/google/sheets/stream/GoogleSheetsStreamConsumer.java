begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.google.sheets.stream
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|google
operator|.
name|sheets
operator|.
name|stream
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|Sheets
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|BatchGetValuesResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|api
operator|.
name|services
operator|.
name|sheets
operator|.
name|v4
operator|.
name|model
operator|.
name|Spreadsheet
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
name|support
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
comment|/**  * The GoogleSheets consumer.  */
end_comment

begin_class
DECL|class|GoogleSheetsStreamConsumer
specifier|public
class|class
name|GoogleSheetsStreamConsumer
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
name|GoogleSheetsStreamConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|GoogleSheetsStreamConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|GoogleSheetsStreamConsumer
parameter_list|(
name|Endpoint
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
DECL|method|getConfiguration ()
specifier|protected
name|GoogleSheetsStreamConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
return|;
block|}
DECL|method|getClient ()
specifier|protected
name|Sheets
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
name|GoogleSheetsStreamEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|GoogleSheetsStreamEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|answer
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getRange
argument_list|()
argument_list|)
condition|)
block|{
name|Sheets
operator|.
name|Spreadsheets
operator|.
name|Values
operator|.
name|BatchGet
name|request
init|=
name|getClient
argument_list|()
operator|.
name|spreadsheets
argument_list|()
operator|.
name|values
argument_list|()
operator|.
name|batchGet
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
decl_stmt|;
name|request
operator|.
name|setMajorDimension
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMajorDimension
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setValueRenderOption
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getValueRenderOption
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getRange
argument_list|()
operator|.
name|contains
argument_list|(
literal|","
argument_list|)
condition|)
block|{
name|request
operator|.
name|setRanges
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getRange
argument_list|()
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|map
argument_list|(
name|String
operator|::
name|trim
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|request
operator|.
name|setRanges
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getRange
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|BatchGetValuesResponse
name|response
init|=
name|request
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|response
operator|.
name|getValueRanges
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|response
operator|.
name|getValueRanges
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|limit
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getMaxResults
argument_list|()
argument_list|)
operator|.
name|map
argument_list|(
name|valueRange
lambda|->
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|valueRange
argument_list|)
argument_list|)
operator|.
name|forEach
argument_list|(
name|answer
operator|::
name|add
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Sheets
operator|.
name|Spreadsheets
operator|.
name|Get
name|request
init|=
name|getClient
argument_list|()
operator|.
name|spreadsheets
argument_list|()
operator|.
name|get
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getSpreadsheetId
argument_list|()
argument_list|)
decl_stmt|;
name|request
operator|.
name|setIncludeGridData
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|isIncludeGridData
argument_list|()
argument_list|)
expr_stmt|;
name|Spreadsheet
name|spreadsheet
init|=
name|request
operator|.
name|execute
argument_list|()
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|spreadsheet
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|answer
argument_list|)
argument_list|)
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
comment|// only loop if we are started (allowed to run)
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
comment|// add current index and total as properties
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
comment|// update pending number of exchanges
name|pendingExchanges
operator|=
name|total
operator|-
name|index
operator|-
literal|1
expr_stmt|;
name|getAsyncProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange done"
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|total
return|;
block|}
block|}
end_class

end_unit

