begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|WhenDefinition
import|;
end_import

begin_comment
comment|/**  * Helper class for ProcessorType and the other model classes.  */
end_comment

begin_class
DECL|class|ProcessorDefinitionHelper
specifier|public
specifier|final
class|class
name|ProcessorDefinitionHelper
block|{
DECL|method|ProcessorDefinitionHelper ()
specifier|private
name|ProcessorDefinitionHelper
parameter_list|()
block|{     }
comment|/**      * Looks for the given type in the list of outputs and recurring all the children as well.      * Will stop at first found and return it.      *      * @param outputs  list of outputs, can be null or empty.      * @param type     the type to look for      * @return         the first found type, or<tt>null</tt> if not found      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|findFirstTypeInOutputs (List<ProcessorDefinition> outputs, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|findFirstTypeInOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|outputs
operator|==
literal|null
operator|||
name|outputs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
for|for
control|(
name|ProcessorDefinition
name|out
range|:
name|outputs
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|out
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|out
argument_list|)
return|;
block|}
comment|// send is much common
if|if
condition|(
name|out
operator|instanceof
name|SendDefinition
condition|)
block|{
name|SendDefinition
name|send
init|=
operator|(
name|SendDefinition
operator|)
name|out
decl_stmt|;
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|send
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|T
name|child
init|=
name|findFirstTypeInOutputs
argument_list|(
name|children
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
comment|// special for choice
if|if
condition|(
name|out
operator|instanceof
name|ChoiceDefinition
condition|)
block|{
name|ChoiceDefinition
name|choice
init|=
operator|(
name|ChoiceDefinition
operator|)
name|out
decl_stmt|;
for|for
control|(
name|WhenDefinition
name|when
range|:
name|choice
operator|.
name|getWhenClauses
argument_list|()
control|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|when
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|T
name|child
init|=
name|findFirstTypeInOutputs
argument_list|(
name|children
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
comment|// otherwise is optional
if|if
condition|(
name|choice
operator|.
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|choice
operator|.
name|getOtherwise
argument_list|()
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|T
name|child
init|=
name|findFirstTypeInOutputs
argument_list|(
name|children
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
block|}
comment|// try children as well
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|children
init|=
name|out
operator|.
name|getOutputs
argument_list|()
decl_stmt|;
name|T
name|child
init|=
name|findFirstTypeInOutputs
argument_list|(
name|children
argument_list|,
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

