begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|NoTypeConversionAvailableException
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
name|ExchangeHelper
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
name|IOHelper
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|csv
operator|.
name|CSVFormat
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|csv
operator|.
name|CSVPrinter
import|;
end_import

begin_comment
comment|/**  * This class marshal data into a CSV format.  */
end_comment

begin_class
DECL|class|CsvMarshaller
specifier|abstract
class|class
name|CsvMarshaller
block|{
DECL|field|format
specifier|private
specifier|final
name|CSVFormat
name|format
decl_stmt|;
DECL|method|CsvMarshaller (CSVFormat format)
specifier|private
name|CsvMarshaller
parameter_list|(
name|CSVFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
comment|/**      * Creates a new instance.      *      * @param format     CSV format      * @param dataFormat Camel CSV data format      * @return New instance      */
DECL|method|create (CSVFormat format, CsvDataFormat dataFormat)
specifier|public
specifier|static
name|CsvMarshaller
name|create
parameter_list|(
name|CSVFormat
name|format
parameter_list|,
name|CsvDataFormat
name|dataFormat
parameter_list|)
block|{
comment|// If we don't want the header record, clear it
if|if
condition|(
name|format
operator|.
name|getSkipHeaderRecord
argument_list|()
condition|)
block|{
name|format
operator|=
name|format
operator|.
name|withHeader
argument_list|(
operator|(
name|String
index|[]
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|fixedColumns
init|=
name|dataFormat
operator|.
name|getHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|fixedColumns
operator|!=
literal|null
operator|&&
name|fixedColumns
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
operator|new
name|FixedColumnsMarshaller
argument_list|(
name|format
argument_list|,
name|fixedColumns
argument_list|)
return|;
block|}
return|return
operator|new
name|DynamicColumnsMarshaller
argument_list|(
name|format
argument_list|)
return|;
block|}
comment|/**      * Marshals the given object into the given stream.      *      * @param exchange     Exchange (used for access to type conversion)      * @param object       Body to marshal      * @param outputStream Output stream of the CSV      * @throws NoTypeConversionAvailableException if the body cannot be converted      * @throws IOException                        if we cannot write into the given stream      */
DECL|method|marshal (Exchange exchange, Object object, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|object
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
throws|,
name|IOException
block|{
name|CSVPrinter
name|printer
init|=
operator|new
name|CSVPrinter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|,
name|format
argument_list|)
decl_stmt|;
try|try
block|{
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|object
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|child
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|printer
operator|.
name|printRecord
argument_list|(
name|getRecordValues
argument_list|(
name|exchange
argument_list|,
name|child
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|printer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getRecordValues (Exchange exchange, Object data)
specifier|private
name|Iterable
argument_list|<
name|?
argument_list|>
name|getRecordValues
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|data
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
comment|// each row must be a map or list based
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|tryConvertTo
argument_list|(
name|Map
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|data
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
block|{
return|return
name|getMapRecordValues
argument_list|(
name|map
argument_list|)
return|;
block|}
return|return
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|,
name|data
argument_list|)
return|;
block|}
comment|/**      * Gets the CSV record values of the given map.      *      * @param map Input map      * @return CSV record values of the given map      */
DECL|method|getMapRecordValues (Map<?, ?> map)
specifier|protected
specifier|abstract
name|Iterable
argument_list|<
name|?
argument_list|>
name|getMapRecordValues
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
function_decl|;
comment|//region Implementations
comment|/**      * This marshaller has fixed columns      */
DECL|class|FixedColumnsMarshaller
specifier|private
specifier|static
specifier|final
class|class
name|FixedColumnsMarshaller
extends|extends
name|CsvMarshaller
block|{
DECL|field|fixedColumns
specifier|private
specifier|final
name|String
index|[]
name|fixedColumns
decl_stmt|;
DECL|method|FixedColumnsMarshaller (CSVFormat format, String[] fixedColumns)
specifier|private
name|FixedColumnsMarshaller
parameter_list|(
name|CSVFormat
name|format
parameter_list|,
name|String
index|[]
name|fixedColumns
parameter_list|)
block|{
name|super
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|this
operator|.
name|fixedColumns
operator|=
name|Arrays
operator|.
name|copyOf
argument_list|(
name|fixedColumns
argument_list|,
name|fixedColumns
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMapRecordValues (Map<?, ?> map)
specifier|protected
name|Iterable
argument_list|<
name|?
argument_list|>
name|getMapRecordValues
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|fixedColumns
operator|.
name|length
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|fixedColumns
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
comment|/**      * This marshaller adapts the columns but always keep them in the same order      */
DECL|class|DynamicColumnsMarshaller
specifier|private
specifier|static
specifier|final
class|class
name|DynamicColumnsMarshaller
extends|extends
name|CsvMarshaller
block|{
DECL|method|DynamicColumnsMarshaller (CSVFormat format)
specifier|private
name|DynamicColumnsMarshaller
parameter_list|(
name|CSVFormat
name|format
parameter_list|)
block|{
name|super
argument_list|(
name|format
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getMapRecordValues (Map<?, ?> map)
specifier|protected
name|Iterable
argument_list|<
name|?
argument_list|>
name|getMapRecordValues
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|map
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
block|}
comment|//endregion
block|}
end_class

end_unit

