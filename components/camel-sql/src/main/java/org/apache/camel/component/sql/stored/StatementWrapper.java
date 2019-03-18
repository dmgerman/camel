begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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

begin_comment
comment|/**  * Wrapper that simplifies operations on  {@link java.sql.CallableStatement} in {@link SqlStoredProducer}.  * Wrappers are stateful objects and must not be reused.  */
end_comment

begin_interface
DECL|interface|StatementWrapper
specifier|public
interface|interface
name|StatementWrapper
block|{
DECL|method|call (WrapperExecuteCallback cb)
name|void
name|call
parameter_list|(
name|WrapperExecuteCallback
name|cb
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|executeBatch ()
name|int
index|[]
name|executeBatch
parameter_list|()
throws|throws
name|SQLException
function_decl|;
DECL|method|getUpdateCount ()
name|Integer
name|getUpdateCount
parameter_list|()
throws|throws
name|SQLException
function_decl|;
DECL|method|executeStatement ()
name|Object
name|executeStatement
parameter_list|()
throws|throws
name|SQLException
function_decl|;
DECL|method|populateStatement (Object value, Exchange exchange)
name|void
name|populateStatement
parameter_list|(
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SQLException
function_decl|;
DECL|method|addBatch (Object value, Exchange exchange)
name|void
name|addBatch
parameter_list|(
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

