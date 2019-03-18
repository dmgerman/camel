begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv.converter
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
operator|.
name|converter
package|;
end_package

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
name|List
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
name|dataformat
operator|.
name|csv
operator|.
name|CsvRecordConverter
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
name|CSVRecord
import|;
end_import

begin_comment
comment|/**  * Test {@link CsvRecordConverter} implementation.  *<p>  * This implementation is explicitely created in a subpackage to check the  * visibility of {@link CsvRecordConverter}.  *</p>  */
end_comment

begin_class
DECL|class|MyCvsRecordConverter
specifier|public
class|class
name|MyCvsRecordConverter
implements|implements
name|CsvRecordConverter
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
block|{
DECL|field|record
specifier|private
specifier|final
name|String
index|[]
name|record
decl_stmt|;
DECL|method|MyCvsRecordConverter (String... record)
specifier|public
name|MyCvsRecordConverter
parameter_list|(
name|String
modifier|...
name|record
parameter_list|)
block|{
assert|assert
name|record
operator|!=
literal|null
operator|:
literal|"Unspecified record"
assert|;
name|this
operator|.
name|record
operator|=
name|record
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|convertRecord (CSVRecord record)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|convertRecord
parameter_list|(
name|CSVRecord
name|record
parameter_list|)
block|{
assert|assert
name|record
operator|!=
literal|null
operator|:
literal|"Unspecified record"
assert|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|this
operator|.
name|record
argument_list|)
return|;
block|}
block|}
end_class

end_unit

