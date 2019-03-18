begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|Set
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
name|component
operator|.
name|hbase
operator|.
name|mapping
operator|.
name|CellMappingStrategy
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
name|hbase
operator|.
name|mapping
operator|.
name|CellMappingStrategyFactory
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
name|hbase
operator|.
name|model
operator|.
name|HBaseCell
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
name|hbase
operator|.
name|model
operator|.
name|HBaseData
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
name|hbase
operator|.
name|model
operator|.
name|HBaseRow
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
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|Cell
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|CellUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ResultScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Scan
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Table
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|FilterList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|PageFilter
import|;
end_import

begin_comment
comment|/**  * The HBase consumer.  */
end_comment

begin_class
DECL|class|HBaseConsumer
specifier|public
class|class
name|HBaseConsumer
extends|extends
name|ScheduledBatchPollingConsumer
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|HBaseEndpoint
name|endpoint
decl_stmt|;
DECL|field|rowModel
specifier|private
name|HBaseRow
name|rowModel
decl_stmt|;
DECL|method|HBaseConsumer (HBaseEndpoint endpoint, Processor processor)
specifier|public
name|HBaseConsumer
parameter_list|(
name|HBaseEndpoint
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
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|rowModel
operator|=
name|endpoint
operator|.
name|getRowModel
argument_list|()
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
init|(
name|Table
name|table
init|=
name|endpoint
operator|.
name|getTable
argument_list|()
init|)
block|{
name|shutdownRunningTask
operator|=
literal|null
expr_stmt|;
name|pendingExchanges
operator|=
literal|0
expr_stmt|;
name|Queue
argument_list|<
name|Exchange
argument_list|>
name|queue
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|Scan
name|scan
init|=
operator|new
name|Scan
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getFilters
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|filters
operator|.
name|addAll
argument_list|(
name|endpoint
operator|.
name|getFilters
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
condition|)
block|{
name|filters
operator|.
name|add
argument_list|(
operator|new
name|PageFilter
argument_list|(
name|maxMessagesPerPoll
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Filter
name|compoundFilter
init|=
operator|new
name|FilterList
argument_list|(
name|filters
argument_list|)
decl_stmt|;
name|scan
operator|.
name|setFilter
argument_list|(
name|compoundFilter
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|rowModel
operator|!=
literal|null
operator|&&
name|rowModel
operator|.
name|getCells
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cellModels
init|=
name|rowModel
operator|.
name|getCells
argument_list|()
decl_stmt|;
for|for
control|(
name|HBaseCell
name|cellModel
range|:
name|cellModels
control|)
block|{
name|scan
operator|.
name|addColumn
argument_list|(
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|cellModel
operator|.
name|getFamily
argument_list|()
argument_list|)
argument_list|,
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|cellModel
operator|.
name|getQualifier
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|ResultScanner
name|scanner
init|=
name|table
operator|.
name|getScanner
argument_list|(
name|scan
argument_list|)
decl_stmt|;
name|int
name|exchangeCount
init|=
literal|0
decl_stmt|;
comment|// The next three statements are used just to get a reference to the BodyCellMappingStrategy instance.
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|STRATEGY
argument_list|,
name|CellMappingStrategyFactory
operator|.
name|BODY
argument_list|)
expr_stmt|;
name|CellMappingStrategy
name|mappingStrategy
init|=
name|endpoint
operator|.
name|getCellMappingStrategyFactory
argument_list|()
operator|.
name|getStrategy
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Result
name|result
init|=
name|scanner
operator|.
name|next
argument_list|()
init|;
operator|(
name|exchangeCount
operator|<
name|maxMessagesPerPoll
operator|||
name|maxMessagesPerPoll
operator|<=
literal|0
operator|)
operator|&&
name|result
operator|!=
literal|null
condition|;
name|result
operator|=
name|scanner
operator|.
name|next
argument_list|()
control|)
block|{
name|HBaseData
name|data
init|=
operator|new
name|HBaseData
argument_list|()
decl_stmt|;
name|HBaseRow
name|resultRow
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
name|resultRow
operator|.
name|apply
argument_list|(
name|rowModel
argument_list|)
expr_stmt|;
name|byte
index|[]
name|row
init|=
name|result
operator|.
name|getRow
argument_list|()
decl_stmt|;
name|resultRow
operator|.
name|setId
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|rowModel
operator|.
name|getRowType
argument_list|()
argument_list|,
name|row
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Cell
argument_list|>
name|cells
init|=
name|result
operator|.
name|listCells
argument_list|()
decl_stmt|;
if|if
condition|(
name|cells
operator|!=
literal|null
condition|)
block|{
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cellModels
init|=
name|rowModel
operator|.
name|getCells
argument_list|()
decl_stmt|;
if|if
condition|(
name|cellModels
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
for|for
control|(
name|HBaseCell
name|modelCell
range|:
name|cellModels
control|)
block|{
name|HBaseCell
name|resultCell
init|=
operator|new
name|HBaseCell
argument_list|()
decl_stmt|;
name|String
name|family
init|=
name|modelCell
operator|.
name|getFamily
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|modelCell
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
name|resultCell
operator|.
name|setValue
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|modelCell
operator|.
name|getValueType
argument_list|()
argument_list|,
name|result
operator|.
name|getValue
argument_list|(
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|family
argument_list|)
argument_list|,
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|column
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|resultCell
operator|.
name|setFamily
argument_list|(
name|modelCell
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
name|resultCell
operator|.
name|setQualifier
argument_list|(
name|modelCell
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|resultRow
operator|.
name|getCells
argument_list|()
operator|.
name|add
argument_list|(
name|resultCell
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|// just need to put every key value into the result Cells
for|for
control|(
name|Cell
name|cell
range|:
name|cells
control|)
block|{
name|String
name|qualifier
init|=
operator|new
name|String
argument_list|(
name|CellUtil
operator|.
name|cloneQualifier
argument_list|(
name|cell
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|family
init|=
operator|new
name|String
argument_list|(
name|CellUtil
operator|.
name|cloneFamily
argument_list|(
name|cell
argument_list|)
argument_list|)
decl_stmt|;
name|HBaseCell
name|resultCell
init|=
operator|new
name|HBaseCell
argument_list|()
decl_stmt|;
name|resultCell
operator|.
name|setFamily
argument_list|(
name|family
argument_list|)
expr_stmt|;
name|resultCell
operator|.
name|setQualifier
argument_list|(
name|qualifier
argument_list|)
expr_stmt|;
name|resultCell
operator|.
name|setValue
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|CellUtil
operator|.
name|cloneValue
argument_list|(
name|cell
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|resultRow
operator|.
name|getCells
argument_list|()
operator|.
name|add
argument_list|(
name|resultCell
argument_list|)
expr_stmt|;
block|}
block|}
name|data
operator|.
name|getRows
argument_list|()
operator|.
name|add
argument_list|(
name|resultRow
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|endpoint
operator|.
name|createExchange
argument_list|()
expr_stmt|;
comment|// Probably overkill but kept it here for consistency.
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|STRATEGY
argument_list|,
name|CellMappingStrategyFactory
operator|.
name|BODY
argument_list|)
expr_stmt|;
name|mappingStrategy
operator|.
name|applyScanResults
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
comment|//Make sure that there is a header containing the marked row ids, so that they can be deleted.
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_MARKED_ROW_ID
operator|.
name|asHeader
argument_list|()
argument_list|,
name|result
operator|.
name|getRow
argument_list|()
argument_list|)
expr_stmt|;
name|queue
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchangeCount
operator|++
expr_stmt|;
block|}
block|}
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|queue
operator|.
name|isEmpty
argument_list|()
condition|?
literal|0
else|:
name|processBatch
argument_list|(
name|CastUtils
operator|.
name|cast
argument_list|(
name|queue
argument_list|)
argument_list|)
return|;
block|}
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
comment|// limit if needed
if|if
condition|(
name|maxMessagesPerPoll
operator|>
literal|0
operator|&&
name|total
operator|>
name|maxMessagesPerPoll
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Limiting to maximum messages to poll {} as there were {} messages in this poll."
argument_list|,
name|maxMessagesPerPoll
argument_list|,
name|total
argument_list|)
expr_stmt|;
name|total
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
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
name|log
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
comment|// if we failed then throw exception
throw|throw
name|exchange
operator|.
name|getException
argument_list|()
throw|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|isRemove
argument_list|()
condition|)
block|{
name|remove
argument_list|(
operator|(
name|byte
index|[]
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_MARKED_ROW_ID
operator|.
name|asHeader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|total
return|;
block|}
comment|/**      * Delegates to the {@link HBaseRemoveHandler}.      */
DECL|method|remove (byte[] row)
specifier|private
name|void
name|remove
parameter_list|(
name|byte
index|[]
name|row
parameter_list|)
throws|throws
name|IOException
block|{
try|try
init|(
name|Table
name|table
init|=
name|endpoint
operator|.
name|getTable
argument_list|()
init|)
block|{
name|endpoint
operator|.
name|getRemoveHandler
argument_list|()
operator|.
name|remove
argument_list|(
name|table
argument_list|,
name|row
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getRowModel ()
specifier|public
name|HBaseRow
name|getRowModel
parameter_list|()
block|{
return|return
name|rowModel
return|;
block|}
DECL|method|setRowModel (HBaseRow rowModel)
specifier|public
name|void
name|setRowModel
parameter_list|(
name|HBaseRow
name|rowModel
parameter_list|)
block|{
name|this
operator|.
name|rowModel
operator|=
name|rowModel
expr_stmt|;
block|}
block|}
end_class

end_unit

