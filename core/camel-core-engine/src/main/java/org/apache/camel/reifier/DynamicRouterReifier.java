begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
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
name|AsyncProcessor
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
name|ErrorHandlerFactory
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
name|Expression
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
name|DynamicRouterDefinition
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
name|ProcessorDefinition
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
name|processor
operator|.
name|DynamicRouter
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
name|reifier
operator|.
name|errorhandler
operator|.
name|ErrorHandlerReifier
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

begin_class
DECL|class|DynamicRouterReifier
specifier|public
class|class
name|DynamicRouterReifier
extends|extends
name|ExpressionReifier
argument_list|<
name|DynamicRouterDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|method|DynamicRouterReifier (ProcessorDefinition<?> definition)
specifier|public
name|DynamicRouterReifier
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|DynamicRouterDefinition
operator|.
name|class
operator|.
name|cast
argument_list|(
name|definition
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProcessor (RouteContext routeContext)
specifier|public
name|Processor
name|createProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|Expression
name|expression
init|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|String
name|delimiter
init|=
name|definition
operator|.
name|getUriDelimiter
argument_list|()
operator|!=
literal|null
condition|?
name|definition
operator|.
name|getUriDelimiter
argument_list|()
else|:
name|DynamicRouterDefinition
operator|.
name|DEFAULT_DELIMITER
decl_stmt|;
name|DynamicRouter
name|dynamicRouter
init|=
operator|new
name|DynamicRouter
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|expression
argument_list|,
name|delimiter
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getIgnoreInvalidEndpoints
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dynamicRouter
operator|.
name|setIgnoreInvalidEndpoints
argument_list|(
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|definition
operator|.
name|getIgnoreInvalidEndpoints
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCacheSize
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|dynamicRouter
operator|.
name|setCacheSize
argument_list|(
name|Integer
operator|.
name|parseInt
argument_list|(
name|definition
operator|.
name|getCacheSize
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// and wrap this in an error handler
name|ErrorHandlerFactory
name|builder
init|=
name|routeContext
operator|.
name|getErrorHandlerFactory
argument_list|()
decl_stmt|;
comment|// create error handler (create error handler directly to keep it light
comment|// weight,
comment|// instead of using ProcessorReifier.wrapInErrorHandler)
name|AsyncProcessor
name|errorHandler
init|=
operator|(
name|AsyncProcessor
operator|)
name|ErrorHandlerReifier
operator|.
name|reifier
argument_list|(
name|builder
argument_list|)
operator|.
name|createErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|dynamicRouter
operator|.
name|newRoutingSlipProcessorForErrorHandler
argument_list|()
argument_list|)
decl_stmt|;
name|dynamicRouter
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|dynamicRouter
return|;
block|}
block|}
end_class

end_unit

