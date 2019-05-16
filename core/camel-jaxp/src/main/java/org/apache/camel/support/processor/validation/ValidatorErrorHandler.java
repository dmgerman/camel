begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support.processor.validation
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|processor
operator|.
name|validation
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|ErrorHandler
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

begin_comment
comment|/**  * Validator error handler.  */
end_comment

begin_interface
DECL|interface|ValidatorErrorHandler
specifier|public
interface|interface
name|ValidatorErrorHandler
extends|extends
name|ErrorHandler
block|{
comment|/**      * Resets any state within this error handler      */
DECL|method|reset ()
name|void
name|reset
parameter_list|()
function_decl|;
comment|/**      * Process any errors which may have occurred during validation      *      * @param exchange the exchange      * @param schema   the schema      * @param result   the result      * @throws ValidationException is thrown in case of validation errors      */
DECL|method|handleErrors (Exchange exchange, Schema schema, Result result)
name|void
name|handleErrors
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Schema
name|schema
parameter_list|,
name|Result
name|result
parameter_list|)
throws|throws
name|ValidationException
function_decl|;
block|}
end_interface

end_unit

