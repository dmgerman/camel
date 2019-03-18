begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
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
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
argument_list|<>
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
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
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
argument_list|<>
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
argument_list|<>
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
annotation|@
name|Converter
DECL|method|toDocument (DataSet dataSet)
specifier|public
specifier|static
name|Document
name|toDocument
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Document
name|doc
init|=
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|newDocument
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataSet
operator|.
name|getIndex
argument_list|()
operator|==
operator|-
literal|1
condition|)
block|{
name|Element
name|list
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"Dataset"
argument_list|)
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
name|list
operator|.
name|appendChild
argument_list|(
name|createDatasetRecord
argument_list|(
name|dataSet
argument_list|,
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|doc
operator|.
name|appendChild
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|doc
operator|.
name|appendChild
argument_list|(
name|createDatasetRecord
argument_list|(
name|dataSet
argument_list|,
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|doc
return|;
block|}
comment|/**      * Puts the values of the dataset into the map      */
DECL|method|putValues (Map<String, Object> map, DataSet dataSet)
specifier|private
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
name|String
index|[]
name|columns
init|=
name|getColumns
argument_list|(
name|dataSet
argument_list|)
decl_stmt|;
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
DECL|method|createDatasetRecord (DataSet dataSet, Document doc)
specifier|private
specifier|static
name|Element
name|createDatasetRecord
parameter_list|(
name|DataSet
name|dataSet
parameter_list|,
name|Document
name|doc
parameter_list|)
block|{
name|Element
name|record
decl_stmt|;
if|if
condition|(
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|HEADER_ID
argument_list|)
condition|)
block|{
name|record
operator|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"DatasetHeader"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|TRAILER_ID
argument_list|)
condition|)
block|{
name|record
operator|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"DatasetTrailer"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|record
operator|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"DatasetRecord"
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|columns
init|=
name|getColumns
argument_list|(
name|dataSet
argument_list|)
decl_stmt|;
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
name|Element
name|columnElement
init|=
name|doc
operator|.
name|createElement
argument_list|(
literal|"Column"
argument_list|)
decl_stmt|;
name|columnElement
operator|.
name|setAttribute
argument_list|(
literal|"name"
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|columnElement
operator|.
name|setTextContent
argument_list|(
name|value
argument_list|)
expr_stmt|;
name|record
operator|.
name|appendChild
argument_list|(
name|columnElement
argument_list|)
expr_stmt|;
block|}
return|return
name|record
return|;
block|}
DECL|method|getColumns (DataSet dataSet)
specifier|private
specifier|static
name|String
index|[]
name|getColumns
parameter_list|(
name|DataSet
name|dataSet
parameter_list|)
block|{
comment|// the columns can vary depending on header, body or trailer
if|if
condition|(
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|HEADER_ID
argument_list|)
condition|)
block|{
return|return
name|dataSet
operator|.
name|getColumns
argument_list|(
name|FlatpackComponent
operator|.
name|HEADER_ID
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|dataSet
operator|.
name|isRecordID
argument_list|(
name|FlatpackComponent
operator|.
name|TRAILER_ID
argument_list|)
condition|)
block|{
return|return
name|dataSet
operator|.
name|getColumns
argument_list|(
name|FlatpackComponent
operator|.
name|TRAILER_ID
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|dataSet
operator|.
name|getColumns
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

