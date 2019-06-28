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
name|ArrayList
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ExpressionFactory
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
name|Predicate
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
name|ExpressionClause
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_class
DECL|class|ChoiceReifier
specifier|public
class|class
name|ChoiceReifier
extends|extends
name|ProcessorReifier
argument_list|<
name|ChoiceDefinition
argument_list|>
block|{
DECL|method|ChoiceReifier (ProcessorDefinition<?> definition)
name|ChoiceReifier
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
name|ChoiceDefinition
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
name|List
argument_list|<
name|FilterProcessor
argument_list|>
name|filters
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|WhenDefinition
name|whenClause
range|:
name|definition
operator|.
name|getWhenClauses
argument_list|()
control|)
block|{
name|ExpressionDefinition
name|exp
init|=
name|whenClause
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|exp
operator|.
name|getExpressionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|exp
operator|.
name|getExpressionType
argument_list|()
expr_stmt|;
block|}
name|Predicate
name|pre
init|=
name|exp
operator|.
name|getPredicate
argument_list|()
decl_stmt|;
if|if
condition|(
name|pre
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|ExpressionClause
argument_list|<
name|?
argument_list|>
name|clause
init|=
operator|(
name|ExpressionClause
argument_list|<
name|?
argument_list|>
operator|)
name|pre
decl_stmt|;
if|if
condition|(
name|clause
operator|.
name|getExpressionType
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// if using the Java DSL then the expression may have been set using the
comment|// ExpressionClause which is a fancy builder to define expressions and predicates
comment|// using fluent builders in the DSL. However we need afterwards a callback to
comment|// reset the expression to the expression type the ExpressionClause did build for us
name|ExpressionFactory
name|model
init|=
name|clause
operator|.
name|getExpressionType
argument_list|()
decl_stmt|;
if|if
condition|(
name|model
operator|instanceof
name|ExpressionDefinition
condition|)
block|{
name|whenClause
operator|.
name|setExpression
argument_list|(
operator|(
name|ExpressionDefinition
operator|)
name|model
argument_list|)
expr_stmt|;
block|}
block|}
name|exp
operator|=
name|whenClause
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
comment|// also resolve properties and constant fields on embedded expressions in the when clauses
if|if
condition|(
name|exp
operator|!=
literal|null
condition|)
block|{
comment|// resolve properties before we create the processor
name|ProcessorDefinitionHelper
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|exp
argument_list|)
expr_stmt|;
comment|// resolve constant fields (eg Exchange.FILE_NAME)
name|ProcessorDefinitionHelper
operator|.
name|resolveKnownConstantFields
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|exp
argument_list|)
expr_stmt|;
block|}
name|FilterProcessor
name|filter
init|=
operator|(
name|FilterProcessor
operator|)
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|whenClause
argument_list|)
decl_stmt|;
name|filters
operator|.
name|add
argument_list|(
name|filter
argument_list|)
expr_stmt|;
block|}
name|Processor
name|otherwiseProcessor
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|definition
operator|.
name|getOtherwise
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|otherwiseProcessor
operator|=
name|createProcessor
argument_list|(
name|routeContext
argument_list|,
name|definition
operator|.
name|getOtherwise
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ChoiceProcessor
argument_list|(
name|filters
argument_list|,
name|otherwiseProcessor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

