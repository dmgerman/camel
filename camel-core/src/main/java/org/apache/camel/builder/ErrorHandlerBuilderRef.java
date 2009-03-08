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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents a proxy to an error handler builder which is resolved by named reference  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ErrorHandlerBuilderRef
specifier|public
class|class
name|ErrorHandlerBuilderRef
extends|extends
name|ErrorHandlerBuilderSupport
block|{
DECL|field|ref
specifier|private
specifier|final
name|String
name|ref
decl_stmt|;
DECL|field|handler
specifier|private
name|ErrorHandlerBuilder
name|handler
decl_stmt|;
DECL|method|ErrorHandlerBuilderRef (String ref)
specifier|public
name|ErrorHandlerBuilderRef
parameter_list|(
name|String
name|ref
parameter_list|)
block|{
name|this
operator|.
name|ref
operator|=
name|ref
expr_stmt|;
block|}
DECL|method|copy ()
specifier|public
name|ErrorHandlerBuilder
name|copy
parameter_list|()
block|{
return|return
operator|new
name|ErrorHandlerBuilderRef
argument_list|(
name|ref
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|addErrorHandlers (OnExceptionDefinition exception)
specifier|public
name|void
name|addErrorHandlers
parameter_list|(
name|OnExceptionDefinition
name|exception
parameter_list|)
block|{
if|if
condition|(
name|handler
operator|!=
literal|null
condition|)
block|{
name|handler
operator|.
name|addErrorHandlers
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|addErrorHandlers
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
DECL|method|createErrorHandler (RouteContext routeContext, Processor processor)
specifier|public
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
block|{
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
name|handler
operator|=
name|routeContext
operator|.
name|lookup
argument_list|(
name|ref
argument_list|,
name|ErrorHandlerBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|handler
argument_list|,
literal|"error handler '"
operator|+
name|ref
operator|+
literal|"'"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OnExceptionDefinition
argument_list|>
name|list
init|=
name|getExceptions
argument_list|()
decl_stmt|;
for|for
control|(
name|OnExceptionDefinition
name|exceptionType
range|:
name|list
control|)
block|{
name|handler
operator|.
name|addErrorHandlers
argument_list|(
name|exceptionType
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|handler
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

