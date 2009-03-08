begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|Processor
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
name|model
operator|.
name|OnExceptionDefinition
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * A builder of a<a href="http://camel.apache.org/error-handler.html">Error Handler</a>  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ErrorHandlerBuilder
specifier|public
interface|interface
name|ErrorHandlerBuilder
block|{
comment|/**      * Creates a copy of this builder      */
DECL|method|copy ()
name|ErrorHandlerBuilder
name|copy
parameter_list|()
function_decl|;
comment|/**      * Creates the error handler interceptor      */
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
name|Processor
name|createErrorHandler
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Adds error handler for the given exception type      * @param exception  the exception to handle      */
DECL|method|addErrorHandlers (OnExceptionDefinition exception)
name|void
name|addErrorHandlers
parameter_list|(
name|OnExceptionDefinition
name|exception
parameter_list|)
function_decl|;
comment|/**      * Adds the error handlers for the given list of exception types      * @param exceptions  the list of exceptions to handle      */
DECL|method|setErrorHandlers (List<OnExceptionDefinition> exceptions)
name|void
name|setErrorHandlers
parameter_list|(
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|exceptions
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

