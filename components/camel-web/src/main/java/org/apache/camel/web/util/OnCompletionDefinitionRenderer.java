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
name|WhenDefinition
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|OnCompletionDefinitionRenderer
specifier|public
class|class
name|OnCompletionDefinitionRenderer
block|{
DECL|method|render (StringBuilder buffer, ProcessorDefinition processor)
specifier|public
specifier|static
name|void
name|render
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|ProcessorDefinition
name|processor
parameter_list|)
block|{
comment|// if not a global onCompletion, add a period
name|boolean
name|notGlobal
init|=
name|buffer
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|")"
argument_list|)
decl_stmt|;
if|if
condition|(
name|notGlobal
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"."
argument_list|)
expr_stmt|;
block|}
name|OnCompletionDefinition
name|onComplete
init|=
operator|(
name|OnCompletionDefinition
operator|)
name|processor
decl_stmt|;
name|buffer
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
if|if
condition|(
name|onComplete
operator|.
name|getOnWhen
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|WhenDefinition
name|when
init|=
name|onComplete
operator|.
name|getOnWhen
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|".onWhen"
argument_list|)
expr_stmt|;
if|if
condition|(
name|when
operator|.
name|getExpression
argument_list|()
operator|.
name|getPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
expr_stmt|;
name|PredicateRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|when
operator|.
name|getExpression
argument_list|()
operator|.
name|getPredicate
argument_list|()
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"Unsupported Expression!"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|onComplete
operator|.
name|getOnCompleteOnly
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".onCompleteOnly()"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onComplete
operator|.
name|getOnFailureOnly
argument_list|()
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".onFailureOnly()"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|branches
init|=
name|onComplete
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
for|for
control|(
name|ProcessorDefinition
name|branch
range|:
name|branches
control|)
block|{
name|SendDefinitionRenderer
operator|.
name|render
argument_list|(
name|buffer
argument_list|,
name|branch
argument_list|)
expr_stmt|;
block|}
comment|// if not a global onCompletion, using end() at the end
if|if
condition|(
name|notGlobal
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|".end()"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

