begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|CamelContext
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|CamelOpenMBeanTypes
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedChoiceMBean
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
name|WhenDefinition
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
name|ChoiceProcessor
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
name|FilterProcessor
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
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Choice"
argument_list|)
DECL|class|ManagedChoice
specifier|public
class|class
name|ManagedChoice
extends|extends
name|ManagedProcessor
implements|implements
name|ManagedChoiceMBean
block|{
DECL|field|processor
specifier|private
specifier|final
name|ChoiceProcessor
name|processor
decl_stmt|;
DECL|method|ManagedChoice (CamelContext context, ChoiceProcessor processor, ProcessorDefinition<?> definition)
specifier|public
name|ManagedChoice
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ChoiceProcessor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
name|this
operator|.
name|processor
operator|=
name|processor
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDefinition ()
specifier|public
name|ChoiceDefinition
name|getDefinition
parameter_list|()
block|{
return|return
operator|(
name|ChoiceDefinition
operator|)
name|super
operator|.
name|getDefinition
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|reset ()
specifier|public
specifier|synchronized
name|void
name|reset
parameter_list|()
block|{
name|processor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|super
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|choiceStatistics ()
specifier|public
name|TabularData
name|choiceStatistics
parameter_list|()
block|{
try|try
block|{
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|choiceTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|WhenDefinition
argument_list|>
name|whens
init|=
name|getDefinition
argument_list|()
operator|.
name|getWhenClauses
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
init|=
name|processor
operator|.
name|getFilters
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|filters
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|WhenDefinition
name|when
init|=
name|whens
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|FilterProcessor
name|filter
init|=
name|filters
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|choiceCompositeType
argument_list|()
decl_stmt|;
name|String
name|predicate
init|=
name|when
operator|.
name|getExpression
argument_list|()
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|String
name|language
init|=
name|when
operator|.
name|getExpression
argument_list|()
operator|.
name|getLanguage
argument_list|()
decl_stmt|;
name|Long
name|matches
init|=
name|filter
operator|.
name|getFilteredCount
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"predicate"
block|,
literal|"language"
block|,
literal|"matches"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|predicate
block|,
name|language
block|,
name|matches
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getDefinition
argument_list|()
operator|.
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|choiceCompositeType
argument_list|()
decl_stmt|;
name|String
name|predicate
init|=
literal|"otherwise"
decl_stmt|;
name|String
name|language
init|=
literal|""
decl_stmt|;
name|Long
name|matches
init|=
name|processor
operator|.
name|getNotFilteredCount
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"predicate"
block|,
literal|"language"
block|,
literal|"matches"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|predicate
block|,
name|language
block|,
name|matches
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

