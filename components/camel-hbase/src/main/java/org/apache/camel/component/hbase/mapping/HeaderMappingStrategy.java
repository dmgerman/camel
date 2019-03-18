begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.mapping
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
operator|.
name|mapping
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|component
operator|.
name|hbase
operator|.
name|HBaseAttribute
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

begin_comment
comment|/**  * A default {@link CellMappingStrategy} implementation.  * It distinguishes between multiple cell, by reading headers with index suffix.  *<p/>  * In case of multiple headers:  *<ul>  *<li>First header is expected to have no suffix</li>  *<li>Suffixes start from number 2</li>  *<li>Suffixes need to be sequential</li>  *</ul>  */
end_comment

begin_class
DECL|class|HeaderMappingStrategy
specifier|public
class|class
name|HeaderMappingStrategy
implements|implements
name|CellMappingStrategy
block|{
comment|/**      * Resolves the cell that the {@link Exchange} refers to.      */
DECL|method|resolveRow (Message message, int index)
specifier|private
name|HBaseRow
name|resolveRow
parameter_list|(
name|Message
name|message
parameter_list|,
name|int
name|index
parameter_list|)
block|{
name|HBaseRow
name|hRow
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
name|HBaseCell
name|hCell
init|=
operator|new
name|HBaseCell
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|Object
name|id
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_ROW_ID
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|rowClassName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_ROW_TYPE
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|rowClass
init|=
name|rowClassName
operator|==
literal|null
operator|||
name|rowClassName
operator|.
name|isEmpty
argument_list|()
condition|?
name|String
operator|.
name|class
else|:
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|rowClassName
argument_list|)
decl_stmt|;
name|String
name|columnFamily
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|columnName
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
name|Object
name|value
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|valueClassName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE_TYPE
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|valueClass
init|=
name|valueClassName
operator|==
literal|null
operator|||
name|valueClassName
operator|.
name|isEmpty
argument_list|()
condition|?
name|String
operator|.
name|class
else|:
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|valueClassName
argument_list|)
decl_stmt|;
comment|//Id can be accepted as null when using get, scan etc.
if|if
condition|(
name|id
operator|==
literal|null
operator|&&
name|columnFamily
operator|==
literal|null
operator|&&
name|columnName
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|hRow
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|hRow
operator|.
name|setRowType
argument_list|(
name|rowClass
argument_list|)
expr_stmt|;
if|if
condition|(
name|columnFamily
operator|!=
literal|null
operator|&&
name|columnName
operator|!=
literal|null
condition|)
block|{
name|hCell
operator|.
name|setQualifier
argument_list|(
name|columnName
argument_list|)
expr_stmt|;
name|hCell
operator|.
name|setFamily
argument_list|(
name|columnFamily
argument_list|)
expr_stmt|;
name|hCell
operator|.
name|setValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
comment|// String is the default value type
name|hCell
operator|.
name|setValueType
argument_list|(
operator|(
name|valueClass
operator|!=
literal|null
operator|)
condition|?
name|valueClass
else|:
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|hRow
operator|.
name|getCells
argument_list|()
operator|.
name|add
argument_list|(
name|hCell
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|hRow
return|;
block|}
comment|/**      * Resolves the cells that the {@link org.apache.camel.Exchange} refers to.      */
annotation|@
name|Override
DECL|method|resolveModel (Message message)
specifier|public
name|HBaseData
name|resolveModel
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|int
name|index
init|=
literal|1
decl_stmt|;
name|HBaseData
name|data
init|=
operator|new
name|HBaseData
argument_list|()
decl_stmt|;
comment|//We use a LinkedHashMap to preserve the order.
name|Map
argument_list|<
name|Object
argument_list|,
name|HBaseRow
argument_list|>
name|rows
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|HBaseRow
name|hRow
init|=
operator|new
name|HBaseRow
argument_list|()
decl_stmt|;
while|while
condition|(
name|hRow
operator|!=
literal|null
condition|)
block|{
name|hRow
operator|=
name|resolveRow
argument_list|(
name|message
argument_list|,
name|index
operator|++
argument_list|)
expr_stmt|;
if|if
condition|(
name|hRow
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|rows
operator|.
name|containsKey
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|rows
operator|.
name|get
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|getCells
argument_list|()
operator|.
name|addAll
argument_list|(
name|hRow
operator|.
name|getCells
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|rows
operator|.
name|put
argument_list|(
name|hRow
operator|.
name|getId
argument_list|()
argument_list|,
name|hRow
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|HBaseRow
argument_list|>
name|rowEntry
range|:
name|rows
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|data
operator|.
name|getRows
argument_list|()
operator|.
name|add
argument_list|(
name|rowEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|data
return|;
block|}
comment|/**      * Applies the cells to the {@link org.apache.camel.Exchange}.      */
DECL|method|applyGetResults (Message message, HBaseData data)
specifier|public
name|void
name|applyGetResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
block|{
name|message
operator|.
name|setHeaders
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
operator|||
name|data
operator|.
name|getRows
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
if|if
condition|(
name|hRow
operator|.
name|getId
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
name|message
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|(
name|index
operator|++
argument_list|)
argument_list|,
name|getValueForColumn
argument_list|(
name|cells
argument_list|,
name|cell
operator|.
name|getFamily
argument_list|()
argument_list|,
name|cell
operator|.
name|getQualifier
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Applies the cells to the {@link org.apache.camel.Exchange}.      */
DECL|method|applyScanResults (Message message, HBaseData data)
specifier|public
name|void
name|applyScanResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
block|{
name|message
operator|.
name|setHeaders
argument_list|(
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|index
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|data
operator|==
literal|null
operator|||
name|data
operator|.
name|getRows
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|message
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_ROW_ID
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|hRow
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|cell
operator|.
name|getFamily
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|cell
operator|.
name|getQualifier
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|HBaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|(
name|index
argument_list|)
argument_list|,
name|cell
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|index
operator|++
expr_stmt|;
block|}
block|}
comment|/**      * Searches a list of cells and returns the value, if family/column matches with the specified.      */
DECL|method|getValueForColumn (Set<HBaseCell> cells, String family, String qualifier)
specifier|private
name|Object
name|getValueForColumn
parameter_list|(
name|Set
argument_list|<
name|HBaseCell
argument_list|>
name|cells
parameter_list|,
name|String
name|family
parameter_list|,
name|String
name|qualifier
parameter_list|)
block|{
if|if
condition|(
name|cells
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|HBaseCell
name|cell
range|:
name|cells
control|)
block|{
if|if
condition|(
name|cell
operator|.
name|getQualifier
argument_list|()
operator|.
name|equals
argument_list|(
name|qualifier
argument_list|)
operator|&&
name|cell
operator|.
name|getFamily
argument_list|()
operator|.
name|equals
argument_list|(
name|family
argument_list|)
condition|)
block|{
return|return
name|cell
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

