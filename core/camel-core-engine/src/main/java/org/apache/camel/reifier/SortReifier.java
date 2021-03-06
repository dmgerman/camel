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
name|Comparator
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
name|SortDefinition
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
name|SortProcessor
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
name|ObjectHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|ExpressionBuilder
operator|.
name|bodyExpression
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|isNotEmpty
import|;
end_import

begin_class
DECL|class|SortReifier
specifier|public
class|class
name|SortReifier
parameter_list|<
name|T
parameter_list|,
name|U
extends|extends
name|SortDefinition
parameter_list|<
name|T
parameter_list|>
parameter_list|>
extends|extends
name|ExpressionReifier
argument_list|<
name|U
argument_list|>
block|{
DECL|method|SortReifier (ProcessorDefinition<?> definition)
specifier|public
name|SortReifier
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
operator|(
name|U
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
comment|// lookup in registry
if|if
condition|(
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getComparatorRef
argument_list|()
argument_list|)
condition|)
block|{
name|definition
operator|.
name|setComparator
argument_list|(
name|routeContext
operator|.
name|lookup
argument_list|(
name|definition
operator|.
name|getComparatorRef
argument_list|()
argument_list|,
name|Comparator
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// if no comparator then default on to string representation
if|if
condition|(
name|definition
operator|.
name|getComparator
argument_list|()
operator|==
literal|null
condition|)
block|{
name|definition
operator|.
name|setComparator
argument_list|(
operator|new
name|Comparator
argument_list|<
name|T
argument_list|>
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|T
name|o1
parameter_list|,
name|T
name|o2
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|compare
argument_list|(
name|o1
argument_list|,
name|o2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|// if no expression provided then default to body expression
name|Expression
name|exp
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exp
operator|=
name|bodyExpression
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|exp
operator|=
name|definition
operator|.
name|getExpression
argument_list|()
operator|.
name|createExpression
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SortProcessor
argument_list|<
name|T
argument_list|>
argument_list|(
name|exp
argument_list|,
name|definition
operator|.
name|getComparator
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

