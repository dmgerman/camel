begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

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

begin_comment
comment|/**  * Represents a strategy for testing endpoints with canned data.  *  * @version $Revision: 1.1 $  */
end_comment

begin_interface
DECL|interface|DataSet
specifier|public
interface|interface
name|DataSet
block|{
DECL|field|INDEX_HEADER
name|String
name|INDEX_HEADER
init|=
literal|"camelDataSetIndex"
decl_stmt|;
comment|/**      * Populates a message exchange when using the DataSet as a source of messages      *      * @param exchange      */
DECL|method|populateMessage (Exchange exchange, long messageIndex)
name|void
name|populateMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|long
name|messageIndex
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the size of the dataset      */
DECL|method|getSize ()
name|long
name|getSize
parameter_list|()
function_decl|;
comment|/**      * Asserts that the expected message has been received for the given index      */
DECL|method|assertMessageExpected (DataSetEndpoint dataSetEndpoint, Exchange expected, Exchange actual, long index)
name|void
name|assertMessageExpected
parameter_list|(
name|DataSetEndpoint
name|dataSetEndpoint
parameter_list|,
name|Exchange
name|expected
parameter_list|,
name|Exchange
name|actual
parameter_list|,
name|long
name|index
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

