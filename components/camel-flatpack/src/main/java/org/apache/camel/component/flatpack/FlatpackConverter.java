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
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|FlatpackConverter
specifier|public
specifier|final
class|class
name|FlatpackConverter
block|{
DECL|method|FlatpackConverter ()
specifier|private
name|FlatpackConverter
parameter_list|()
block|{
comment|// helper class
block|}
annotation|@
name|Converter
DECL|method|toMap (DataSet dataSet)
specifier|public
specifier|static
name|Map
name|toMap
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|putValues
argument_list|(
name|map
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|map
return|;
block|}
annotation|@
name|Converter
DECL|method|toList (DataSet dataSet)
specifier|public
specifier|static
name|List
name|toList
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|dataSet
operator|.
name|goTop
argument_list|()
expr_stmt|;
while|while
condition|(
name|dataSet
operator|.
name|next
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|putValues
argument_list|(
name|map
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Puts the values of the dataset into the map      */
DECL|method|putValues (Map<String, Object> map, DataSet dataSet)
specifier|public
specifier|static
name|void
name|putValues
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|DataSet
name|dataSet
parameter_list|)
block|{
name|boolean
name|header
init|=
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|HEADER_ID
argument_list|)
decl_stmt|;
name|boolean
name|trailer
init|=
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|TRAILER_ID
argument_list|)
decl_stmt|;
comment|// the columns can vary depending on header, body or trailer
name|String
index|[]
name|columns
decl_stmt|;
if|if
condition|(
name|header
condition|)
block|{
name|columns
operator|=
name|dataSet
operator|.
name|getColumns
argument_list|(
name|FlatpackComponent
operator|.
name|HEADER_ID
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|trailer
condition|)
block|{
name|columns
operator|=
name|dataSet
operator|.
name|getColumns
argument_list|(
name|FlatpackComponent
operator|.
name|TRAILER_ID
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|columns
operator|=
name|dataSet
operator|.
name|getColumns
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|column
range|:
name|columns
control|)
block|{
name|String
name|value
init|=
name|dataSet
operator|.
name|getString
argument_list|(
name|column
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|column
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

