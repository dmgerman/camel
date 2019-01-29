begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.json
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|json
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_comment
comment|/**  * Jsonables can be serialized in java script object notation (JSON).  *   * @since 2.0.0  */
end_comment

begin_interface
DECL|interface|Jsonable
specifier|public
interface|interface
name|Jsonable
block|{
comment|/**      * Serialize to a JSON formatted string.      *       * @return a string, formatted in JSON, that represents the Jsonable.      */
DECL|method|toJson ()
name|String
name|toJson
parameter_list|()
function_decl|;
comment|/**      * Serialize to a JSON formatted stream.      *       * @param writable where the resulting JSON text should be sent.      * @throws IOException when the writable encounters an I/O error.      */
DECL|method|toJson (Writer writable)
name|void
name|toJson
parameter_list|(
name|Writer
name|writable
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

