begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ServicePoolAware
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
name|filters
operator|.
name|ModelAwareFilter
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
name|impl
operator|.
name|DefaultProducer
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
name|KeyValue
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
name|Delete
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
name|Get
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
name|HTableInterface
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
name|Put
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
name|util
operator|.
name|Bytes
import|;
end_import

begin_comment
comment|/**  * The HBase producer.  */
end_comment

begin_class
DECL|class|HBaseProducer
specifier|public
class|class
name|HBaseProducer
extends|extends
name|DefaultProducer
implements|implements
name|ServicePoolAware
block|{
DECL|field|endpoint
specifier|private
name|HBaseEndpoint
name|endpoint
decl_stmt|;
DECL|field|rowModel
specifier|private
name|HBaseRow
name|rowModel
decl_stmt|;
DECL|method|HBaseProducer (HBaseEndpoint endpoint)
specifier|public
name|HBaseProducer
parameter_list|(
name|HBaseEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
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
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|HTableInterface
name|table
init|=
name|endpoint
operator|.
name|getTable
argument_list|()
decl_stmt|;
try|try
block|{
name|updateHeaders
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|String
name|operation
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|)
decl_stmt|;
name|Integer
name|maxScanResult
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|HBASE_MAX_SCAN_RESULTS
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|fromRowId
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|FROM_ROW
argument_list|)
decl_stmt|;
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
name|HBaseData
name|data
init|=
name|mappingStrategy
operator|.
name|resolveModel
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Put
argument_list|>
name|putOperations
init|=
operator|new
name|LinkedList
argument_list|<
name|Put
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Delete
argument_list|>
name|deleteOperations
init|=
operator|new
name|LinkedList
argument_list|<
name|Delete
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|getOperationResult
init|=
operator|new
name|LinkedList
argument_list|<
name|HBaseRow
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|scanOperationResult
init|=
operator|new
name|LinkedList
argument_list|<
name|HBaseRow
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|HBaseRow
name|hRow
range|:
name|data
operator|.
name|getRows
argument_list|()
control|)
block|{
name|hRow
operator|.
name|apply
argument_list|(
name|rowModel
argument_list|)
expr_stmt|;
if|if
condition|(
name|HBaseConstants
operator|.
name|PUT
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|putOperations
operator|.
name|add
argument_list|(
name|createPut
argument_list|(
name|hRow
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HBaseConstants
operator|.
name|GET
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|HBaseRow
name|getResultRow
init|=
name|getCells
argument_list|(
name|table
argument_list|,
name|hRow
argument_list|)
decl_stmt|;
name|getOperationResult
operator|.
name|add
argument_list|(
name|getResultRow
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HBaseConstants
operator|.
name|DELETE
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|deleteOperations
operator|.
name|add
argument_list|(
name|createDeleteRow
argument_list|(
name|hRow
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|HBaseConstants
operator|.
name|SCAN
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|scanOperationResult
operator|=
name|scanCells
argument_list|(
name|table
argument_list|,
name|hRow
argument_list|,
name|fromRowId
argument_list|,
name|maxScanResult
argument_list|,
name|endpoint
operator|.
name|getFilters
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//Check if we have something to add.
if|if
condition|(
operator|!
name|putOperations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|table
operator|.
name|put
argument_list|(
name|putOperations
argument_list|)
expr_stmt|;
name|table
operator|.
name|flushCommits
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|deleteOperations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|table
operator|.
name|delete
argument_list|(
name|deleteOperations
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|getOperationResult
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|mappingStrategy
operator|.
name|applyGetResults
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
operator|new
name|HBaseData
argument_list|(
name|getOperationResult
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|scanOperationResult
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|mappingStrategy
operator|.
name|applyScanResults
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
operator|new
name|HBaseData
argument_list|(
name|scanOperationResult
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|table
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Creates an HBase {@link Put} on a specific row, using a collection of values (family/column/value pairs).      *      * @param hRow      * @throws Exception      */
DECL|method|createPut (HBaseRow hRow)
specifier|private
name|Put
name|createPut
parameter_list|(
name|HBaseRow
name|hRow
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
argument_list|,
literal|"HBase row"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|,
literal|"HBase row id"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
operator|.
name|getCells
argument_list|()
argument_list|,
literal|"HBase cells"
argument_list|)
expr_stmt|;
name|Put
name|put
init|=
operator|new
name|Put
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cells
init|=
name|hRow
operator|.
name|getCells
argument_list|()
decl_stmt|;
for|for
control|(
name|HBaseCell
name|cell
range|:
name|cells
control|)
block|{
name|String
name|family
init|=
name|cell
operator|.
name|getFamily
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|cell
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
name|Object
name|value
init|=
name|cell
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|family
argument_list|,
literal|"HBase column family"
argument_list|,
name|cell
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|column
argument_list|,
literal|"HBase column"
argument_list|,
name|cell
argument_list|)
expr_stmt|;
name|put
operator|.
name|add
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
argument_list|,
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|put
return|;
block|}
comment|/**      * Performs an HBase {@link Get} on a specific row, using a collection of values (family/column/value pairs).      * The result is<p>the most recent entry</p> for each column.      */
DECL|method|getCells (HTableInterface table, HBaseRow hRow)
specifier|private
name|HBaseRow
name|getCells
parameter_list|(
name|HTableInterface
name|table
parameter_list|,
name|HBaseRow
name|hRow
parameter_list|)
throws|throws
name|Exception
block|{
name|HBaseRow
name|resultRow
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|HBaseCell
argument_list|>
name|resultCells
init|=
operator|new
name|LinkedList
argument_list|<
name|HBaseCell
argument_list|>
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
argument_list|,
literal|"HBase row"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|,
literal|"HBase row id"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
operator|.
name|getCells
argument_list|()
argument_list|,
literal|"HBase cells"
argument_list|)
expr_stmt|;
name|resultRow
operator|.
name|setId
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Get
name|get
init|=
operator|new
name|Get
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cellModels
init|=
name|hRow
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
name|String
name|family
init|=
name|cellModel
operator|.
name|getFamily
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|cellModel
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|family
argument_list|,
literal|"HBase column family"
argument_list|,
name|cellModel
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|column
argument_list|,
literal|"HBase column"
argument_list|,
name|cellModel
argument_list|)
expr_stmt|;
name|get
operator|.
name|addColumn
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
expr_stmt|;
block|}
name|Result
name|result
init|=
name|table
operator|.
name|get
argument_list|(
name|get
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|resultRow
operator|.
name|setTimestamp
argument_list|(
name|result
operator|.
name|raw
argument_list|()
index|[
literal|0
index|]
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|HBaseCell
name|cellModel
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
name|cellModel
operator|.
name|getFamily
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|cellModel
operator|.
name|getQualifier
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
name|column
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|KeyValue
argument_list|>
name|kvs
init|=
name|result
operator|.
name|getColumn
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
decl_stmt|;
if|if
condition|(
name|kvs
operator|!=
literal|null
operator|&&
operator|!
name|kvs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|//Return the most recent entry.
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
name|cellModel
operator|.
name|getValueType
argument_list|()
argument_list|,
name|kvs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resultCell
operator|.
name|setTimestamp
argument_list|(
name|kvs
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|resultCells
operator|.
name|add
argument_list|(
name|resultCell
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
return|return
name|resultRow
return|;
block|}
comment|/**      * Creates an HBase {@link Delete} on a specific row, using a collection of values (family/column/value pairs).      */
DECL|method|createDeleteRow (HBaseRow hRow)
specifier|private
name|Delete
name|createDeleteRow
parameter_list|(
name|HBaseRow
name|hRow
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
argument_list|,
literal|"HBase row"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|,
literal|"HBase row id"
argument_list|)
expr_stmt|;
return|return
operator|new
name|Delete
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Performs an HBase {@link Get} on a specific row, using a collection of values (family/column/value pairs).      * The result is<p>the most recent entry</p> for each column.      */
DECL|method|scanCells (HTableInterface table, HBaseRow model, String start, Integer maxRowScan, List<Filter> filters)
specifier|private
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|scanCells
parameter_list|(
name|HTableInterface
name|table
parameter_list|,
name|HBaseRow
name|model
parameter_list|,
name|String
name|start
parameter_list|,
name|Integer
name|maxRowScan
parameter_list|,
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|HBaseRow
argument_list|>
name|rowSet
init|=
operator|new
name|LinkedList
argument_list|<
name|HBaseRow
argument_list|>
argument_list|()
decl_stmt|;
name|HBaseRow
name|startRow
init|=
operator|new
name|HBaseRow
argument_list|(
name|model
operator|.
name|getCells
argument_list|()
argument_list|)
decl_stmt|;
name|startRow
operator|.
name|setId
argument_list|(
name|start
argument_list|)
expr_stmt|;
name|Scan
name|scan
decl_stmt|;
if|if
condition|(
name|start
operator|!=
literal|null
condition|)
block|{
name|scan
operator|=
operator|new
name|Scan
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
name|start
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|scan
operator|=
operator|new
name|Scan
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|filters
operator|!=
literal|null
operator|&&
operator|!
name|filters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
operator|(
operator|(
name|ModelAwareFilter
argument_list|<
name|?
argument_list|>
operator|)
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|apply
argument_list|(
name|endpoint
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|scan
operator|.
name|setFilter
argument_list|(
operator|new
name|FilterList
argument_list|(
name|FilterList
operator|.
name|Operator
operator|.
name|MUST_PASS_ALL
argument_list|,
operator|(
operator|(
name|ModelAwareFilter
argument_list|<
name|?
argument_list|>
operator|)
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|)
operator|.
name|getFilteredList
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cellModels
init|=
name|model
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
name|String
name|family
init|=
name|cellModel
operator|.
name|getFamily
argument_list|()
decl_stmt|;
name|String
name|column
init|=
name|cellModel
operator|.
name|getQualifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|family
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|column
argument_list|)
condition|)
block|{
name|scan
operator|.
name|addColumn
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
expr_stmt|;
block|}
block|}
name|ResultScanner
name|resultScanner
init|=
name|table
operator|.
name|getScanner
argument_list|(
name|scan
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|Result
name|result
init|=
name|resultScanner
operator|.
name|next
argument_list|()
decl_stmt|;
while|while
condition|(
name|result
operator|!=
literal|null
operator|&&
name|count
operator|<
name|maxRowScan
condition|)
block|{
name|HBaseRow
name|resultRow
init|=
operator|new
name|HBaseRow
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
name|model
operator|.
name|getRowType
argument_list|()
argument_list|,
name|result
operator|.
name|getRow
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|resultRow
operator|.
name|setTimestamp
argument_list|(
name|result
operator|.
name|raw
argument_list|()
index|[
literal|0
index|]
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|cellModels
operator|=
name|model
operator|.
name|getCells
argument_list|()
expr_stmt|;
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
name|model
operator|.
name|getRowType
argument_list|()
argument_list|,
name|result
operator|.
name|getRow
argument_list|()
argument_list|)
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
if|if
condition|(
name|result
operator|.
name|getColumnLatest
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
operator|!=
literal|null
condition|)
block|{
name|resultCell
operator|.
name|setTimestamp
argument_list|(
name|result
operator|.
name|getColumnLatest
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
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|rowSet
operator|.
name|add
argument_list|(
name|resultRow
argument_list|)
expr_stmt|;
name|count
operator|++
expr_stmt|;
name|result
operator|=
name|resultScanner
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|rowSet
return|;
block|}
comment|/**      * This methods fill possible gaps in the {@link Exchange} headers, with values passed from the Endpoint.      */
DECL|method|updateHeaders (Exchange exchange)
specifier|private
name|void
name|updateHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|endpoint
operator|.
name|getMaxResults
argument_list|()
operator|!=
literal|0
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|HBASE_MAX_SCAN_RESULTS
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HBaseConstants
operator|.
name|HBASE_MAX_SCAN_RESULTS
argument_list|,
name|endpoint
operator|.
name|getMaxResults
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getMappingStrategyName
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|STRATEGY
argument_list|)
operator|==
literal|null
condition|)
block|{
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
name|endpoint
operator|.
name|getMappingStrategyName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getMappingStrategyName
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|STRATEGY_CLASS_NAME
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CellMappingStrategyFactory
operator|.
name|STRATEGY_CLASS_NAME
argument_list|,
name|endpoint
operator|.
name|getMappingStrategyClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|,
name|endpoint
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|==
literal|null
operator|&&
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|,
name|HBaseConstants
operator|.
name|PUT
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

