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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
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
name|Required
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

begin_comment
comment|/**  * A base class for nodes which contain an expression and a number of outputs  *  * @version  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|ExpressionNode
specifier|public
class|class
name|ExpressionNode
extends|extends
name|ProcessorDefinition
argument_list|<
name|ExpressionNode
argument_list|>
block|{
annotation|@
name|XmlElementRef
DECL|field|expression
specifier|private
name|ExpressionDefinition
name|expression
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|outputs
specifier|private
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<
name|ProcessorDefinition
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|ExpressionNode ()
specifier|public
name|ExpressionNode
parameter_list|()
block|{     }
DECL|method|ExpressionNode (ExpressionDefinition expression)
specifier|public
name|ExpressionNode
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|ExpressionNode (Expression expression)
specifier|public
name|ExpressionNode
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|!=
literal|null
condition|)
block|{
name|setExpression
argument_list|(
operator|new
name|ExpressionDefinition
argument_list|(
name|expression
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|ExpressionNode (Predicate predicate)
specifier|public
name|ExpressionNode
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|!=
literal|null
condition|)
block|{
name|setExpression
argument_list|(
operator|new
name|ExpressionDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getShortName ()
specifier|public
name|String
name|getShortName
parameter_list|()
block|{
return|return
literal|"exp"
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|ExpressionDefinition
name|getExpression
parameter_list|()
block|{
return|return
name|expression
return|;
block|}
annotation|@
name|Required
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|outputs
parameter_list|)
block|{
name|this
operator|.
name|outputs
operator|=
name|outputs
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isOutputSupported ()
specifier|public
name|boolean
name|isOutputSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
if|if
condition|(
name|getExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|""
return|;
block|}
return|return
name|getExpression
argument_list|()
operator|.
name|getLabel
argument_list|()
return|;
block|}
DECL|method|createFilterProcessor (RouteContext routeContext)
specifier|protected
name|FilterProcessor
name|createFilterProcessor
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
return|return
operator|new
name|FilterProcessor
argument_list|(
name|getExpression
argument_list|()
operator|.
name|createPredicate
argument_list|(
name|routeContext
argument_list|)
argument_list|,
name|childProcessor
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureChild (ProcessorDefinition output)
specifier|protected
name|void
name|configureChild
parameter_list|(
name|ProcessorDefinition
name|output
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|instanceof
name|ExpressionClause
condition|)
block|{
name|ExpressionClause
name|clause
init|=
operator|(
name|ExpressionClause
operator|)
name|expression
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
name|expression
operator|=
name|clause
operator|.
name|getExpressionType
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

