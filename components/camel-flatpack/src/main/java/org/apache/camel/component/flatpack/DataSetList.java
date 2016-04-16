begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
package|;
end_package

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|AbstractList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|DataSet
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|Record
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|flatpack
operator|.
name|ordering
operator|.
name|OrderBy
import|;
end_import

begin_comment
comment|/**  * The {@link DataSetList} wraps the {@link DataSet} as a Java {@link List} type so the data can easily be iterated.  * You can access the {@link DataSet} API from this {@link DataSetList} as it implements {@link DataSet}.  */
end_comment

begin_class
DECL|class|DataSetList
specifier|public
class|class
name|DataSetList
extends|extends
name|AbstractList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
implements|implements
name|DataSet
block|{
DECL|field|dataSet
specifier|private
specifier|final
name|DataSet
name|dataSet
decl_stmt|;
DECL|method|DataSetList (DataSet dataSet)
specifier|public
name|DataSetList
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
block|{
name|this
operator|.
name|dataSet
operator|=
name|dataSet
expr_stmt|;
block|}
DECL|method|get (int index)
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|get
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|dataSet
operator|.
name|absolute
argument_list|(
name|index
argument_list|)
expr_stmt|;
return|return
name|FlatpackConverter
operator|.
name|toMap
argument_list|(
name|dataSet
argument_list|)
return|;
block|}
DECL|method|size ()
specifier|public
name|int
name|size
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getRowCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|iterator ()
specifier|public
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|iterator
parameter_list|()
block|{
name|dataSet
operator|.
name|goTop
argument_list|()
expr_stmt|;
return|return
operator|new
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
block|{
specifier|private
name|boolean
name|hasNext
init|=
name|dataSet
operator|.
name|next
argument_list|()
decl_stmt|;
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|hasNext
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|next
parameter_list|()
block|{
comment|// because of a limitation in split() we need to create an object for the current position
comment|// otherwise strangeness occurs when the same object is used to represent each row
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
name|FlatpackConverter
operator|.
name|toMap
argument_list|(
name|dataSet
argument_list|)
decl_stmt|;
name|hasNext
operator|=
name|dataSet
operator|.
name|next
argument_list|()
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"remove() not supported"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
comment|// delegate methods
comment|// --------------------------------------------------------------
annotation|@
name|Override
DECL|method|goTop ()
specifier|public
name|void
name|goTop
parameter_list|()
block|{
name|dataSet
operator|.
name|goTop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|goBottom ()
specifier|public
name|void
name|goBottom
parameter_list|()
block|{
name|dataSet
operator|.
name|goBottom
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|previous ()
specifier|public
name|boolean
name|previous
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|previous
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getErrors ()
specifier|public
name|List
name|getErrors
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getErrors
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
name|dataSet
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getIndex ()
specifier|public
name|int
name|getIndex
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getIndex
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRowCount ()
specifier|public
name|int
name|getRowCount
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getRowCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getErrorCount ()
specifier|public
name|int
name|getErrorCount
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getErrorCount
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isAnError (int lineNo)
specifier|public
name|boolean
name|isAnError
parameter_list|(
name|int
name|lineNo
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|isAnError
argument_list|(
name|lineNo
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|orderRows (OrderBy ob)
specifier|public
name|void
name|orderRows
parameter_list|(
name|OrderBy
name|ob
parameter_list|)
block|{
name|dataSet
operator|.
name|orderRows
argument_list|(
name|ob
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setLowerCase ()
specifier|public
name|void
name|setLowerCase
parameter_list|()
block|{
name|dataSet
operator|.
name|setLowerCase
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUpperCase ()
specifier|public
name|void
name|setUpperCase
parameter_list|()
block|{
name|dataSet
operator|.
name|setUpperCase
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|absolute (int localPointer)
specifier|public
name|void
name|absolute
parameter_list|(
name|int
name|localPointer
parameter_list|)
block|{
name|dataSet
operator|.
name|absolute
argument_list|(
name|localPointer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setStrictNumericParse (boolean strictNumericParse)
specifier|public
name|void
name|setStrictNumericParse
parameter_list|(
name|boolean
name|strictNumericParse
parameter_list|)
block|{
name|dataSet
operator|.
name|setStrictNumericParse
argument_list|(
name|strictNumericParse
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setPZConvertProps (Properties props)
specifier|public
name|void
name|setPZConvertProps
parameter_list|(
name|Properties
name|props
parameter_list|)
block|{
name|dataSet
operator|.
name|setPZConvertProps
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setValue (String column, String value)
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|column
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|dataSet
operator|.
name|setValue
argument_list|(
name|column
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clearRows ()
specifier|public
name|void
name|clearRows
parameter_list|()
block|{
name|dataSet
operator|.
name|clearRows
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clearErrors ()
specifier|public
name|void
name|clearErrors
parameter_list|()
block|{
name|dataSet
operator|.
name|clearErrors
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|clearAll ()
specifier|public
name|void
name|clearAll
parameter_list|()
block|{
name|dataSet
operator|.
name|clearAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getString (String column)
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getString
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDouble (String column)
specifier|public
name|double
name|getDouble
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getDouble
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getBigDecimal (String column)
specifier|public
name|BigDecimal
name|getBigDecimal
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getBigDecimal
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getInt (String column)
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getInt
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getLong (String column)
specifier|public
name|long
name|getLong
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getLong
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDate (String column)
specifier|public
name|Date
name|getDate
parameter_list|(
name|String
name|column
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
name|dataSet
operator|.
name|getDate
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDate (String column, SimpleDateFormat sdf)
specifier|public
name|Date
name|getDate
parameter_list|(
name|String
name|column
parameter_list|,
name|SimpleDateFormat
name|sdf
parameter_list|)
throws|throws
name|ParseException
block|{
return|return
name|dataSet
operator|.
name|getDate
argument_list|(
name|column
argument_list|,
name|sdf
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getObject (String column, Class<?> classToConvertTo)
specifier|public
name|Object
name|getObject
parameter_list|(
name|String
name|column
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|classToConvertTo
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getObject
argument_list|(
name|column
argument_list|,
name|classToConvertTo
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getColumns ()
specifier|public
name|String
index|[]
name|getColumns
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getColumns
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getColumns (String recordID)
specifier|public
name|String
index|[]
name|getColumns
parameter_list|(
name|String
name|recordID
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|getColumns
argument_list|(
name|recordID
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRowNo ()
specifier|public
name|int
name|getRowNo
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getRowNo
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isRecordID (String recordID)
specifier|public
name|boolean
name|isRecordID
parameter_list|(
name|String
name|recordID
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|recordID
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|contains (String column)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|column
parameter_list|)
block|{
return|return
name|dataSet
operator|.
name|contains
argument_list|(
name|column
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|isRowEmpty ()
specifier|public
name|boolean
name|isRowEmpty
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|isRowEmpty
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRawData ()
specifier|public
name|String
name|getRawData
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getRawData
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|boolean
name|next
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|next
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRecord ()
specifier|public
name|Record
name|getRecord
parameter_list|()
block|{
return|return
name|dataSet
operator|.
name|getRecord
argument_list|()
return|;
block|}
block|}
end_class

end_unit

