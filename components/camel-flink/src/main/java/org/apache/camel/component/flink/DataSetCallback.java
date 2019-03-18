begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|DataSet
import|;
end_import

begin_comment
comment|/**  * Generic block of code with parameters which can be executed against DataSet and return results.  *  * @param<T> results type  */
end_comment

begin_interface
DECL|interface|DataSetCallback
specifier|public
interface|interface
name|DataSetCallback
parameter_list|<
name|T
parameter_list|>
block|{
DECL|method|onDataSet (DataSet ds, Object... payloads)
name|T
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

