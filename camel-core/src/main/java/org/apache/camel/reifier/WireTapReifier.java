begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
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
name|ExchangePattern
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
name|builder
operator|.
name|ExpressionBuilder
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
name|model
operator|.
name|ProcessorDefinitionHelper
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
name|SetHeaderDefinition
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
name|WireTapDefinition
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
name|CamelInternalProcessor
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
name|SendDynamicProcessor
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
name|WireTapProcessor
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
name|support
operator|.
name|CamelContextHelper
import|;
end_import

begin_class
DECL|class|WireTapReifier
class|class
name|WireTapReifier
extends|extends
name|ToDynamicReifier
argument_list|<
name|WireTapDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
block|{
DECL|method|WireTapReifier (ProcessorDefinition<?> definition)
name|WireTapReifier
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
name|definition
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
comment|// executor service is mandatory for wire tap
name|boolean
name|shutdownThreadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|willCreateNewThreadPool
argument_list|(
name|routeContext
argument_list|,
name|definition
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"WireTap"
argument_list|,
name|definition
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// must use InOnly for WireTap
name|definition
operator|.
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
expr_stmt|;
comment|// create the send dynamic producer to send to the wire tapped endpoint
name|SendDynamicProcessor
name|dynamicTo
init|=
operator|(
name|SendDynamicProcessor
operator|)
name|super
operator|.
name|createProcessor
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
comment|// create error handler we need to use for processing the wire tapped
name|Processor
name|target
init|=
name|wrapInErrorHandler
argument_list|(
name|routeContext
argument_list|,
name|dynamicTo
argument_list|)
decl_stmt|;
comment|// and wrap in unit of work
name|CamelInternalProcessor
name|internal
init|=
operator|new
name|CamelInternalProcessor
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|internal
operator|.
name|addAdvice
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|UnitOfWorkProcessorAdvice
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
comment|// is true by default
name|boolean
name|isCopy
init|=
name|definition
operator|.
name|getCopy
argument_list|()
operator|==
literal|null
operator|||
name|definition
operator|.
name|getCopy
argument_list|()
decl_stmt|;
name|WireTapProcessor
name|answer
init|=
operator|new
name|WireTapProcessor
argument_list|(
name|dynamicTo
argument_list|,
name|internal
argument_list|,
name|definition
operator|.
name|getPattern
argument_list|()
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|,
name|definition
operator|.
name|isDynamic
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setCopy
argument_list|(
name|isCopy
argument_list|)
expr_stmt|;
if|if
condition|(
name|definition
operator|.
name|getNewExchangeProcessorRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setNewExchangeProcessor
argument_list|(
name|routeContext
operator|.
name|mandatoryLookup
argument_list|(
name|definition
operator|.
name|getNewExchangeProcessorRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getNewExchangeProcessor
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|addNewExchangeProcessor
argument_list|(
name|definition
operator|.
name|getNewExchangeProcessor
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getNewExchangeExpression
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setNewExchangeExpression
argument_list|(
name|definition
operator|.
name|getNewExchangeExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getHeaders
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|definition
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|SetHeaderDefinition
name|header
range|:
name|definition
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|Processor
name|processor
init|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|header
argument_list|)
decl_stmt|;
name|answer
operator|.
name|addNewExchangeProcessor
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|definition
operator|.
name|getOnPrepareRef
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|definition
operator|.
name|setOnPrepare
argument_list|(
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|definition
operator|.
name|getOnPrepareRef
argument_list|()
argument_list|,
name|Processor
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getOnPrepare
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setOnPrepare
argument_list|(
name|definition
operator|.
name|getOnPrepare
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createExpression (RouteContext routeContext)
specifier|protected
name|Expression
name|createExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
comment|// whether to use dynamic or static uri
if|if
condition|(
name|definition
operator|.
name|isDynamic
argument_list|()
condition|)
block|{
return|return
name|super
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|definition
operator|.
name|getUri
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

