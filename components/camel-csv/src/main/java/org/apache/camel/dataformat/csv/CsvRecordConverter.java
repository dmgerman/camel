begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * This interface is used to define a converter that transform a {@link org.apache.commons.csv.CSVRecord} into another  * type.  *<p/>  * The {@link org.apache.camel.dataformat.csv.CsvRecordConverters} class defines common converters.  *  * @param<T> Conversion type  * @see org.apache.camel.dataformat.csv.CsvRecordConverters  */
end_comment

begin_interface
DECL|interface|CsvRecordConverter
specifier|public
interface|interface
name|CsvRecordConverter
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Converts the CSV record into another type.      *      * @param record CSV record to convert      * @return converted CSV record      */
DECL|method|convertRecord (CSVRecord record)
name|T
name|convertRecord
parameter_list|(
name|CSVRecord
name|record
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

