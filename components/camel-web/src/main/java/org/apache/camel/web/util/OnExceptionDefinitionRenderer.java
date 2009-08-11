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
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|OnExceptionDefinitionRenderer
specifier|public
class|class
name|OnExceptionDefinitionRenderer
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
name|OnExceptionDefinition
name|onException
init|=
operator|(
name|OnExceptionDefinition
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
literal|"("
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Class
argument_list|>
name|exceptions
init|=
name|onException
operator|.
name|getExceptionClasses
argument_list|()
decl_stmt|;
for|for
control|(
name|Class
name|excep
range|:
name|exceptions
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|excep
operator|.
name|getSimpleName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|".class"
argument_list|)
expr_stmt|;
if|if
condition|(
name|excep
operator|!=
name|exceptions
operator|.
name|get
argument_list|(
name|exceptions
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
name|buffer
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
comment|// render handled() dsl
if|if
condition|(
name|onException
operator|.
name|getHandledPolicy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|String
name|handled
init|=
name|onException
operator|.
name|getHandledPolicy
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|".handled("
argument_list|)
operator|.
name|append
argument_list|(
name|handled
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|branches
init|=
name|onException
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
block|}
block|}
end_class

end_unit

