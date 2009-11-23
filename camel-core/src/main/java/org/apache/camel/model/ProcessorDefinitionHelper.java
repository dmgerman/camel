begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Helper class for ProcessorDefinition and the other model classes.  */
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
comment|/**      * Looks for the given type in the list of outputs and recurring all the children as well.      *      * @param outputs  list of outputs, can be null or empty.      * @param type     the type to look for      * @return         the found definitions, or<tt>null</tt> if not found      */
DECL|method|filterTypeInOutputs (List<ProcessorDefinition> outputs, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|Iterator
argument_list|<
name|T
argument_list|>
name|filterTypeInOutputs
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
name|List
argument_list|<
name|T
argument_list|>
name|found
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|outputs
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
return|return
name|found
operator|.
name|iterator
argument_list|()
return|;
block|}
comment|/**      * Looks for the given type in the list of outputs and recurring all the children as well.      * Will stop at first found and return it.      *      * @param outputs  list of outputs, can be null or empty.      * @param type     the type to look for      * @return         the first found type, or<tt>null</tt> if not found      */
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
name|List
argument_list|<
name|T
argument_list|>
name|found
init|=
operator|new
name|ArrayList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|doFindType
argument_list|(
name|outputs
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
if|if
condition|(
name|found
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|found
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
comment|/**      * Is the given child the first in the outputs from the parent?      *      * @param parentType the type the parent must be      * @param node the node      * @return<tt>true</tt> if first child,<tt>false</tt> otherwise      */
DECL|method|isFirstChildOfType (Class<?> parentType, ProcessorDefinition<?> node)
specifier|public
specifier|static
name|boolean
name|isFirstChildOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
operator|||
name|node
operator|.
name|getParent
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|equals
argument_list|(
name|parentType
argument_list|)
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|node
operator|.
name|getParent
argument_list|()
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|equals
argument_list|(
name|node
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doFindType (List<ProcessorDefinition> outputs, Class<T> type, List<T> found)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|void
name|doFindType
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
parameter_list|,
name|List
argument_list|<
name|T
argument_list|>
name|found
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
return|return;
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
name|found
operator|.
name|add
argument_list|(
operator|(
name|T
operator|)
name|out
argument_list|)
expr_stmt|;
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
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
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
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
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
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
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
name|doFindType
argument_list|(
name|children
argument_list|,
name|type
argument_list|,
name|found
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

