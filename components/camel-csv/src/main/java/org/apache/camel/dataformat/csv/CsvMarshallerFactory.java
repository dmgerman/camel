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
name|CSVFormat
import|;
end_import

begin_comment
comment|/**  * A {@link CsvMarshaller} factory.  */
end_comment

begin_interface
DECL|interface|CsvMarshallerFactory
specifier|public
interface|interface
name|CsvMarshallerFactory
block|{
DECL|field|DEFAULT
name|CsvMarshallerFactory
name|DEFAULT
init|=
operator|new
name|CsvMarshallerFactory
argument_list|()
block|{
annotation|@
name|Override
specifier|public
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
return|return
name|CsvMarshaller
operator|.
name|create
argument_list|(
name|format
argument_list|,
name|dataFormat
argument_list|)
return|;
block|}
block|}
decl_stmt|;
comment|/**      * Creates and returns a new {@link CsvMarshaller}.      *      * @param format     the<b>CSV</b> format. Can NOT be<code>null</code>.      * @param dataFormat the<b>CSV</b> data format. Can NOT be<code>null</code>.      * @return a new {@link CsvMarshaller}. Never<code>null</code>.      */
DECL|method|create (CSVFormat format, CsvDataFormat dataFormat)
name|CsvMarshaller
name|create
parameter_list|(
name|CSVFormat
name|format
parameter_list|,
name|CsvDataFormat
name|dataFormat
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

