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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
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
name|DelayDefinition
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
name|language
operator|.
name|ExpressionDefinition
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
name|Delayer
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
DECL|class|DelayReifier
class|class
name|DelayReifier
extends|extends
name|ExpressionReifier
argument_list|<
name|DelayDefinition
argument_list|>
block|{
DECL|method|DelayReifier (ProcessorDefinition<?> definition)
name|DelayReifier
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
name|DelayDefinition
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
name|Processor
name|childProcessor
init|=
name|this
operator|.
name|createChildProcessor
argument_list|(
name|routeContext
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|Expression
name|delay
init|=
name|createAbsoluteTimeDelayExpression
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
name|boolean
name|async
init|=
name|definition
operator|.
name|getAsyncDelayed
argument_list|()
operator|==
literal|null
operator|||
name|definition
operator|.
name|getAsyncDelayed
argument_list|()
decl_stmt|;
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
name|async
argument_list|)
decl_stmt|;
name|ScheduledExecutorService
name|threadPool
init|=
name|ProcessorDefinitionHelper
operator|.
name|getConfiguredScheduledExecutorService
argument_list|(
name|routeContext
argument_list|,
literal|"Delay"
argument_list|,
name|definition
argument_list|,
name|async
argument_list|)
decl_stmt|;
name|Delayer
name|answer
init|=
operator|new
name|Delayer
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|childProcessor
argument_list|,
name|delay
argument_list|,
name|threadPool
argument_list|,
name|shutdownThreadPool
argument_list|)
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getAsyncDelayed
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|answer
operator|.
name|setAsyncDelayed
argument_list|(
name|definition
operator|.
name|getAsyncDelayed
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getCallerRunsWhenRejected
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// should be default true
name|answer
operator|.
name|setCallerRunsWhenRejected
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|.
name|setCallerRunsWhenRejected
argument_list|(
name|definition
operator|.
name|getCallerRunsWhenRejected
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|createAbsoluteTimeDelayExpression (RouteContext routeContext)
specifier|private
name|Expression
name|createAbsoluteTimeDelayExpression
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|ExpressionDefinition
name|expr
init|=
name|definition
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expr
operator|!=
literal|null
condition|)
block|{
return|return
name|expr
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

