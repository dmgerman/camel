begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jsonvalidator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jsonvalidator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|networknt
operator|.
name|schema
operator|.
name|JsonSchema
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
name|CamelContext
import|;
end_import

begin_comment
comment|/**  * Can be used to create custom schema for the JSON validator endpoint.  */
end_comment

begin_interface
DECL|interface|JsonSchemaLoader
specifier|public
interface|interface
name|JsonSchemaLoader
block|{
comment|/**      * Create a new Schema based on the schema input stream.      *      * @param camelContext camel context      * @param inputStream the resource input stream      * @return a Schema to be used when validating incoming requests      */
DECL|method|createSchema (CamelContext camelContext, InputStream inputStream)
name|JsonSchema
name|createSchema
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

