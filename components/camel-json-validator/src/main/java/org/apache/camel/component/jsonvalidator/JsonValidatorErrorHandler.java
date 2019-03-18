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
name|util
operator|.
name|Set
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
name|com
operator|.
name|networknt
operator|.
name|schema
operator|.
name|ValidationMessage
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ValidationException
import|;
end_import

begin_interface
DECL|interface|JsonValidatorErrorHandler
specifier|public
interface|interface
name|JsonValidatorErrorHandler
block|{
comment|/**      * Process any errors which may have occurred during validation      *      * @param exchange the exchange      * @param schema   the schema      * @param errors   the validation errors      * @throws ValidationException is thrown in case of validation errors      */
DECL|method|handleErrors (Exchange exchange, JsonSchema schema, Set<ValidationMessage> errors)
name|void
name|handleErrors
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JsonSchema
name|schema
parameter_list|,
name|Set
argument_list|<
name|ValidationMessage
argument_list|>
name|errors
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
comment|/**      * Process a general error that happens during valdating      *      * @param exchange the exchange      * @param schema   the schema      * @param e        general error      * @throws ValidationException is thrown in case of validation errors      */
DECL|method|handleErrors (Exchange exchange, JsonSchema schema, Exception e)
name|void
name|handleErrors
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|JsonSchema
name|schema
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit

