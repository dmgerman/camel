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
name|security
operator|.
name|PrivilegedAction
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
name|Map
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
name|Consumer
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
name|Producer
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
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
name|DefaultEndpoint
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
name|conf
operator|.
name|Configuration
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
name|TableName
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
name|Connection
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
name|HBaseAdmin
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
name|security
operator|.
name|UserGroupInformation
import|;
end_import

begin_comment
comment|/**  * For reading/writing from/to an HBase store (Hadoop database).  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"hbase"
argument_list|,
name|title
operator|=
literal|"HBase"
argument_list|,
name|syntax
operator|=
literal|"hbase:tableName"
argument_list|,
name|label
operator|=
literal|"hadoop"
argument_list|)
DECL|class|HBaseEndpoint
specifier|public
class|class
name|HBaseEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|connection
specifier|private
specifier|final
name|Connection
name|connection
decl_stmt|;
DECL|field|admin
specifier|private
name|HBaseAdmin
name|admin
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The name of the table"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|tableName
specifier|private
specifier|final
name|String
name|tableName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"100"
argument_list|)
DECL|field|maxResults
specifier|private
name|int
name|maxResults
init|=
literal|100
decl_stmt|;
annotation|@
name|UriParam
DECL|field|filters
specifier|private
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|enums
operator|=
literal|"CamelHBasePut,CamelHBaseGet,CamelHBaseScan,CamelHBaseDelete"
argument_list|)
DECL|field|operation
specifier|private
name|String
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|remove
specifier|private
name|boolean
name|remove
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"header,body"
argument_list|)
DECL|field|mappingStrategyName
specifier|private
name|String
name|mappingStrategyName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|mappingStrategyClassName
specifier|private
name|String
name|mappingStrategyClassName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cellMappingStrategyFactory
specifier|private
name|CellMappingStrategyFactory
name|cellMappingStrategyFactory
init|=
operator|new
name|CellMappingStrategyFactory
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|removeHandler
specifier|private
name|HBaseRemoveHandler
name|removeHandler
init|=
operator|new
name|HBaseDeleteHandler
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
DECL|field|rowModel
specifier|private
name|HBaseRow
name|rowModel
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|maxMessagesPerPoll
specifier|private
name|int
name|maxMessagesPerPoll
decl_stmt|;
annotation|@
name|UriParam
DECL|field|userGroupInformation
specifier|private
name|UserGroupInformation
name|userGroupInformation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|prefix
operator|=
literal|"row."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|)
DECL|field|rowMapping
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|rowMapping
decl_stmt|;
comment|/**      * in the purpose of performance optimization      */
DECL|field|tableNameBytes
specifier|private
name|byte
index|[]
name|tableNameBytes
decl_stmt|;
DECL|method|HBaseEndpoint (String uri, HBaseComponent component, Connection connection, String tableName)
specifier|public
name|HBaseEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|HBaseComponent
name|component
parameter_list|,
name|Connection
name|connection
parameter_list|,
name|String
name|tableName
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
name|this
operator|.
name|connection
operator|=
name|connection
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|tableName
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Table name can not be null"
argument_list|)
throw|;
block|}
else|else
block|{
name|tableNameBytes
operator|=
name|tableName
operator|.
name|getBytes
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|HBaseProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|HBaseConsumer
name|consumer
init|=
operator|new
name|HBaseConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|maxMessagesPerPoll
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getAdmin ()
specifier|public
name|HBaseAdmin
name|getAdmin
parameter_list|()
block|{
return|return
name|admin
return|;
block|}
DECL|method|setAdmin (HBaseAdmin admin)
specifier|public
name|void
name|setAdmin
parameter_list|(
name|HBaseAdmin
name|admin
parameter_list|)
block|{
name|this
operator|.
name|admin
operator|=
name|admin
expr_stmt|;
block|}
DECL|method|getMaxResults ()
specifier|public
name|int
name|getMaxResults
parameter_list|()
block|{
return|return
name|maxResults
return|;
block|}
comment|/**      * The maximum number of rows to scan.      */
DECL|method|setMaxResults (int maxResults)
specifier|public
name|void
name|setMaxResults
parameter_list|(
name|int
name|maxResults
parameter_list|)
block|{
name|this
operator|.
name|maxResults
operator|=
name|maxResults
expr_stmt|;
block|}
DECL|method|getFilters ()
specifier|public
name|List
argument_list|<
name|Filter
argument_list|>
name|getFilters
parameter_list|()
block|{
return|return
name|filters
return|;
block|}
comment|/**      * A list of filters to use.      */
DECL|method|setFilters (List<Filter> filters)
specifier|public
name|void
name|setFilters
parameter_list|(
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
parameter_list|)
block|{
name|this
operator|.
name|filters
operator|=
name|filters
expr_stmt|;
block|}
DECL|method|getOperation ()
specifier|public
name|String
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
comment|/**      * The HBase operation to perform      */
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getCellMappingStrategyFactory ()
specifier|public
name|CellMappingStrategyFactory
name|getCellMappingStrategyFactory
parameter_list|()
block|{
return|return
name|cellMappingStrategyFactory
return|;
block|}
comment|/**      * To use a custom CellMappingStrategyFactory that is responsible for mapping cells.      */
DECL|method|setCellMappingStrategyFactory (CellMappingStrategyFactory cellMappingStrategyFactory)
specifier|public
name|void
name|setCellMappingStrategyFactory
parameter_list|(
name|CellMappingStrategyFactory
name|cellMappingStrategyFactory
parameter_list|)
block|{
name|this
operator|.
name|cellMappingStrategyFactory
operator|=
name|cellMappingStrategyFactory
expr_stmt|;
block|}
DECL|method|getMappingStrategyName ()
specifier|public
name|String
name|getMappingStrategyName
parameter_list|()
block|{
return|return
name|mappingStrategyName
return|;
block|}
comment|/**      * The strategy to use for mapping Camel messages to HBase columns. Supported values: header, or body.      */
DECL|method|setMappingStrategyName (String mappingStrategyName)
specifier|public
name|void
name|setMappingStrategyName
parameter_list|(
name|String
name|mappingStrategyName
parameter_list|)
block|{
name|this
operator|.
name|mappingStrategyName
operator|=
name|mappingStrategyName
expr_stmt|;
block|}
DECL|method|getMappingStrategyClassName ()
specifier|public
name|String
name|getMappingStrategyClassName
parameter_list|()
block|{
return|return
name|mappingStrategyClassName
return|;
block|}
comment|/**      * The class name of a custom mapping strategy implementation.      */
DECL|method|setMappingStrategyClassName (String mappingStrategyClassName)
specifier|public
name|void
name|setMappingStrategyClassName
parameter_list|(
name|String
name|mappingStrategyClassName
parameter_list|)
block|{
name|this
operator|.
name|mappingStrategyClassName
operator|=
name|mappingStrategyClassName
expr_stmt|;
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
comment|/**      * An instance of org.apache.camel.component.hbase.model.HBaseRow which describes how each row should be modeled      */
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
DECL|method|isRemove ()
specifier|public
name|boolean
name|isRemove
parameter_list|()
block|{
return|return
name|remove
return|;
block|}
comment|/**      * If the option is true, Camel HBase Consumer will remove the rows which it processes.      */
DECL|method|setRemove (boolean remove)
specifier|public
name|void
name|setRemove
parameter_list|(
name|boolean
name|remove
parameter_list|)
block|{
name|this
operator|.
name|remove
operator|=
name|remove
expr_stmt|;
block|}
DECL|method|getRemoveHandler ()
specifier|public
name|HBaseRemoveHandler
name|getRemoveHandler
parameter_list|()
block|{
return|return
name|removeHandler
return|;
block|}
comment|/**      * To use a custom HBaseRemoveHandler that is executed when a row is to be removed.      */
DECL|method|setRemoveHandler (HBaseRemoveHandler removeHandler)
specifier|public
name|void
name|setRemoveHandler
parameter_list|(
name|HBaseRemoveHandler
name|removeHandler
parameter_list|)
block|{
name|this
operator|.
name|removeHandler
operator|=
name|removeHandler
expr_stmt|;
block|}
DECL|method|getMaxMessagesPerPoll ()
specifier|public
name|int
name|getMaxMessagesPerPoll
parameter_list|()
block|{
return|return
name|maxMessagesPerPoll
return|;
block|}
comment|/**      * Gets the maximum number of messages as a limit to poll at each polling.      *<p/>      * Is default unlimited, but use 0 or negative number to disable it as unlimited.      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
specifier|public
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
block|{
name|this
operator|.
name|maxMessagesPerPoll
operator|=
name|maxMessagesPerPoll
expr_stmt|;
block|}
DECL|method|getUserGroupInformation ()
specifier|public
name|UserGroupInformation
name|getUserGroupInformation
parameter_list|()
block|{
return|return
name|userGroupInformation
return|;
block|}
comment|/**      * Defines privileges to communicate with HBase such as using kerberos.      */
DECL|method|setUserGroupInformation (UserGroupInformation userGroupInformation)
specifier|public
name|void
name|setUserGroupInformation
parameter_list|(
name|UserGroupInformation
name|userGroupInformation
parameter_list|)
block|{
name|this
operator|.
name|userGroupInformation
operator|=
name|userGroupInformation
expr_stmt|;
block|}
DECL|method|getRowMapping ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getRowMapping
parameter_list|()
block|{
return|return
name|rowMapping
return|;
block|}
comment|/**      * To map the key/values from the Map to a {@link HBaseRow}.      *<p/>      * The following keys is supported:      *<ul>      *<li>rowId - The id of the row. This has limited use as the row usually changes per Exchange.</li>      *<li>rowType - The type to covert row id to. Supported operations: CamelHBaseScan.</li>      *<li>family - The column family. Supports a number suffix for referring to more than one columns.</li>      *<li>qualifier - The column qualifier. Supports a number suffix for referring to more than one columns.</li>      *<li>value - The value. Supports a number suffix for referring to more than one columns</li>      *<li>valueType - The value type. Supports a number suffix for referring to more than one columns. Supported operations: CamelHBaseGet, and CamelHBaseScan.</li>      *</ul>      */
DECL|method|setRowMapping (Map<String, Object> rowMapping)
specifier|public
name|void
name|setRowMapping
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|rowMapping
parameter_list|)
block|{
name|this
operator|.
name|rowMapping
operator|=
name|rowMapping
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|rowModel
operator|==
literal|null
operator|&&
name|rowMapping
operator|!=
literal|null
condition|)
block|{
name|rowModel
operator|=
name|createRowModel
argument_list|(
name|rowMapping
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|/**      * Gets connection to the table (secured or not, depends on the object initialization)      * please remember to close the table after use      * @return table, remember to close!      */
DECL|method|getTable ()
specifier|public
name|Table
name|getTable
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|userGroupInformation
operator|!=
literal|null
condition|)
block|{
return|return
name|userGroupInformation
operator|.
name|doAs
argument_list|(
operator|new
name|PrivilegedAction
argument_list|<
name|Table
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Table
name|run
parameter_list|()
block|{
try|try
block|{
return|return
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableNameBytes
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableNameBytes
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**      * Creates an {@link HBaseRow} model from the specified endpoint parameters.      */
DECL|method|createRowModel (Map<String, Object> parameters)
specifier|private
name|HBaseRow
name|createRowModel
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|HBaseRow
name|rowModel
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_ROW_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|rowType
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_ROW_TYPE
operator|.
name|asOption
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|rowType
operator|!=
literal|null
operator|&&
operator|!
name|rowType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rowModel
operator|.
name|setRowType
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|rowType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|parameters
operator|.
name|get
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
operator|!=
literal|null
operator|&&
name|parameters
operator|.
name|get
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
operator|!=
literal|null
condition|;
name|i
operator|++
control|)
block|{
name|HBaseCell
name|cellModel
init|=
operator|new
name|HBaseCell
argument_list|()
decl_stmt|;
name|cellModel
operator|.
name|setFamily
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cellModel
operator|.
name|setQualifier
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|cellModel
operator|.
name|setValue
argument_list|(
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE_TYPE
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
condition|)
block|{
name|String
name|valueType
init|=
name|String
operator|.
name|valueOf
argument_list|(
name|parameters
operator|.
name|remove
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE_TYPE
operator|.
name|asOption
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|valueType
operator|!=
literal|null
operator|&&
operator|!
name|valueType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|rowModel
operator|.
name|setRowType
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|valueType
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|rowModel
operator|.
name|getCells
argument_list|()
operator|.
name|add
argument_list|(
name|cellModel
argument_list|)
expr_stmt|;
block|}
return|return
name|rowModel
return|;
block|}
block|}
end_class

end_unit

