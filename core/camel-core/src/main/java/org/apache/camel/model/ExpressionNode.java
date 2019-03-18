begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
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

begin_comment
comment|/**  * A base class for nodes which contain an expression and a number of outputs  */
end_comment

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlTransient
DECL|class|ExpressionNode
specifier|public
specifier|abstract
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
argument_list|<
name|?
argument_list|>
argument_list|>
name|outputs
init|=
operator|new
name|ArrayList
argument_list|<>
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
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
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
name|ExpressionNodeHelper
operator|.
name|toExpressionDefinition
argument_list|(
name|predicate
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
DECL|method|setExpression (ExpressionDefinition expression)
specifier|public
name|void
name|setExpression
parameter_list|(
name|ExpressionDefinition
name|expression
parameter_list|)
block|{
comment|// favour using the helper to set the expression as it can unwrap some unwanted builders when using Java DSL
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getOutputs ()
specifier|public
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
argument_list|>
name|getOutputs
parameter_list|()
block|{
return|return
name|outputs
return|;
block|}
DECL|method|setOutputs (List<ProcessorDefinition<?>> outputs)
specifier|public
name|void
name|setOutputs
parameter_list|(
name|List
argument_list|<
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
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
annotation|@
name|Override
DECL|method|configureChild (ProcessorDefinition<?> output)
specifier|public
name|void
name|configureChild
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|output
parameter_list|)
block|{
comment|// reuse the logic from pre create processor
name|preCreateProcessor
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|preCreateProcessor ()
specifier|public
name|void
name|preCreateProcessor
parameter_list|()
block|{
name|Expression
name|exp
init|=
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
operator|&&
name|getExpression
argument_list|()
operator|.
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exp
operator|=
name|getExpression
argument_list|()
operator|.
name|getExpressionValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|exp
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
name|exp
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
name|setExpression
argument_list|(
name|clause
operator|.
name|getExpressionType
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|getExpression
argument_list|()
operator|!=
literal|null
operator|&&
name|getExpression
argument_list|()
operator|.
name|getExpression
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// use toString from predicate or expression so we have some information to show in the route model
if|if
condition|(
name|getExpression
argument_list|()
operator|.
name|getPredicate
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExpression
argument_list|()
operator|.
name|setExpression
argument_list|(
name|getExpression
argument_list|()
operator|.
name|getPredicate
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getExpression
argument_list|()
operator|.
name|getExpressionValue
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExpression
argument_list|()
operator|.
name|setExpression
argument_list|(
name|getExpression
argument_list|()
operator|.
name|getExpressionValue
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

