begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|util
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
name|model
operator|.
name|AggregateDefinition
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
name|CatchDefinition
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
name|ChoiceDefinition
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
name|ConvertBodyDefinition
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
name|ExpressionNode
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
name|FinallyDefinition
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
name|LoadBalanceDefinition
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
name|OnCompletionDefinition
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
name|model
operator|.
name|OutputDefinition
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
name|ResequenceDefinition
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
name|RollbackDefinition
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
name|RoutingSlipDefinition
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
name|SendDefinition
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
name|ThrottleDefinition
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ProcessorDefinitionRenderer
specifier|public
specifier|final
class|class
name|ProcessorDefinitionRenderer
block|{
DECL|method|ProcessorDefinitionRenderer ()
specifier|private
name|ProcessorDefinitionRenderer
parameter_list|()
block|{
comment|// Utility class, no public or protected default constructor
block|}
DECL|method|render (StringBuilder buffer, ProcessorDefinition<?> processor)
specifier|public
specifier|static
name|void
name|render
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|processor
parameter_list|)
block|{
if|if
condition|(
name|processor
operator|instanceof
name|AggregateDefinition
condition|)
block|{
name|AggregateDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|CatchDefinition
condition|)
block|{
name|CatchDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ChoiceDefinition
condition|)
block|{
name|ChoiceDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ConvertBodyDefinition
condition|)
block|{
name|ConvertBodyDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ExpressionNode
condition|)
block|{
name|ExpressionNodeRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|LoadBalanceDefinition
condition|)
block|{
name|LoadBalanceDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OnCompletionDefinition
condition|)
block|{
name|OnCompletionDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
name|OnExceptionDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|OutputDefinition
condition|)
block|{
name|OutputDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ResequenceDefinition
condition|)
block|{
name|ResequenceDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|RollbackDefinition
condition|)
block|{
name|RollbackDefinition
name|rollback
init|=
operator|(
name|RollbackDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"(\""
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|rollback
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\")"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|RoutingSlipDefinition
condition|)
block|{
name|RoutingSlipDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|SendDefinition
condition|)
block|{
name|SendDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|processor
operator|instanceof
name|ThrottleDefinition
condition|)
block|{
name|ThrottleDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
operator|.
name|append
argument_list|(
name|processor
operator|.
name|getShortName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"()"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|processor
operator|instanceof
name|OutputDefinition
condition|)
block|{
name|OutputDefinition
name|out
init|=
operator|(
name|OutputDefinition
operator|)
name|processor
decl_stmt|;
if|if
condition|(
name|out
operator|instanceof
name|FinallyDefinition
condition|)
block|{
return|return;
block|}
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
name|processor
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|nextProcessor
range|:
name|outputs
control|)
block|{
name|render
argument_list|(
name|buffer
argument_list|,
name|nextProcessor
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

