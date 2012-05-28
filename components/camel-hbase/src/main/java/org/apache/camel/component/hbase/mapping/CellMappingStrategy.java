begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|model
operator|.
name|HBaseData
import|;
end_import

begin_comment
comment|/**  * A Cell resolver is responsible on identifying the cells, to which the Echange refers to.  * Is used for all types of operations (Put, Get etc).  * It is allowed that an exchange refers to more than once cells. This happens if headers for multiple cells are present in the {@Exchange}.  */
end_comment

begin_interface
DECL|interface|CellMappingStrategy
specifier|public
interface|interface
name|CellMappingStrategy
block|{
comment|/**      * Resolves the cell that the {@link Exchange} refers to.      *      * @param message      * @return      */
DECL|method|resolveModel (Message message)
name|HBaseData
name|resolveModel
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Applies the KeyValues of a get opration to the {@link Exchange}.      *      * @param message The message that will be applied the Get result.      * @param data The rows that will be applied to the message.      */
DECL|method|applyGetResults (Message message, HBaseData data)
name|void
name|applyGetResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
function_decl|;
comment|/**      * Applies the KeyValues of a scan operation to the {@link Exchange}.      *      * @param message      * @param rows      */
DECL|method|applyScanResults (Message message, HBaseData data)
name|void
name|applyScanResults
parameter_list|(
name|Message
name|message
parameter_list|,
name|HBaseData
name|data
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

