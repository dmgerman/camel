begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|HTableInterface
import|;
end_import

begin_interface
DECL|interface|HBaseRemoveHandler
specifier|public
interface|interface
name|HBaseRemoveHandler
block|{
comment|/**      * 'Removes' a row from the table.      * The removal is not necessarily physical remove.      * The implementation decides how a row can be considered as removed.      */
DECL|method|remove (HTableInterface table, byte[] row)
name|void
name|remove
parameter_list|(
name|HTableInterface
name|table
parameter_list|,
name|byte
index|[]
name|row
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

