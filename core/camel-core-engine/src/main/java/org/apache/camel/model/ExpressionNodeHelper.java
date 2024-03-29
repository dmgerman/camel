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
name|SimpleBuilder
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
name|ValueBuilder
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
name|model
operator|.
name|language
operator|.
name|SimpleExpression
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
name|XPathExpression
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
name|ExpressionResultTypeAware
import|;
end_import

begin_comment
comment|/**  * Helper for {@link ExpressionNode}  */
end_comment

begin_class
DECL|class|ExpressionNodeHelper
specifier|public
specifier|final
class|class
name|ExpressionNodeHelper
block|{
DECL|method|ExpressionNodeHelper ()
specifier|private
name|ExpressionNodeHelper
parameter_list|()
block|{     }
comment|/**      * Determines which {@link ExpressionDefinition} describes the given      * expression best possible.      *<p/>      * This implementation will use types such as {@link SimpleExpression},      * {@link XPathExpression} etc. if the given expression is detect as such a      * type.      *      * @param expression the expression      * @return a definition which describes the expression      */
DECL|method|toExpressionDefinition (Expression expression)
specifier|public
specifier|static
name|ExpressionDefinition
name|toExpressionDefinition
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|instanceof
name|SimpleBuilder
condition|)
block|{
name|SimpleBuilder
name|builder
init|=
operator|(
name|SimpleBuilder
operator|)
name|expression
decl_stmt|;
comment|// we keep the original expression by using the constructor that
comment|// accepts an expression
name|SimpleExpression
name|answer
init|=
operator|new
name|SimpleExpression
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExpression
argument_list|(
name|builder
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|builder
operator|.
name|getResultType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|instanceof
name|ExpressionResultTypeAware
operator|&&
name|expression
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.camel.language.xpath.XPathBuilder"
argument_list|)
condition|)
block|{
name|ExpressionResultTypeAware
name|aware
init|=
operator|(
name|ExpressionResultTypeAware
operator|)
name|expression
decl_stmt|;
comment|// we keep the original expression by using the constructor that
comment|// accepts an expression
name|XPathExpression
name|answer
init|=
operator|new
name|XPathExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExpression
argument_list|(
name|aware
operator|.
name|getExpressionText
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|answer
operator|.
name|getResultType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|instanceof
name|ValueBuilder
condition|)
block|{
comment|// ValueBuilder wraps the actual expression so unwrap
name|ValueBuilder
name|builder
init|=
operator|(
name|ValueBuilder
operator|)
name|expression
decl_stmt|;
name|expression
operator|=
name|builder
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|expression
operator|instanceof
name|ExpressionDefinition
condition|)
block|{
return|return
operator|(
name|ExpressionDefinition
operator|)
name|expression
return|;
block|}
return|return
operator|new
name|ExpressionDefinition
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Determines which {@link ExpressionDefinition} describes the given      * predicate best possible.      *<p/>      * This implementation will use types such as {@link SimpleExpression},      * {@link XPathExpression} etc. if the given predicate is detect as such a      * type.      *      * @param predicate the predicate      * @return a definition which describes the predicate      */
DECL|method|toExpressionDefinition (Predicate predicate)
specifier|public
specifier|static
name|ExpressionDefinition
name|toExpressionDefinition
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
if|if
condition|(
name|predicate
operator|instanceof
name|SimpleBuilder
condition|)
block|{
name|SimpleBuilder
name|builder
init|=
operator|(
name|SimpleBuilder
operator|)
name|predicate
decl_stmt|;
comment|// we keep the original expression by using the constructor that
comment|// accepts an expression
name|SimpleExpression
name|answer
init|=
operator|new
name|SimpleExpression
argument_list|(
name|builder
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExpression
argument_list|(
name|builder
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|predicate
operator|instanceof
name|ExpressionResultTypeAware
operator|&&
name|predicate
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.camel.language.xpath.XPathBuilder"
argument_list|)
condition|)
block|{
name|ExpressionResultTypeAware
name|aware
init|=
operator|(
name|ExpressionResultTypeAware
operator|)
name|predicate
decl_stmt|;
name|Expression
name|expression
init|=
operator|(
name|Expression
operator|)
name|predicate
decl_stmt|;
comment|// we keep the original expression by using the constructor that
comment|// accepts an expression
name|XPathExpression
name|answer
init|=
operator|new
name|XPathExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setExpression
argument_list|(
name|aware
operator|.
name|getExpressionText
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|answer
operator|.
name|getResultType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
elseif|else
if|if
condition|(
name|predicate
operator|instanceof
name|ValueBuilder
condition|)
block|{
comment|// ValueBuilder wraps the actual predicate so unwrap
name|ValueBuilder
name|builder
init|=
operator|(
name|ValueBuilder
operator|)
name|predicate
decl_stmt|;
name|Expression
name|expression
init|=
name|builder
operator|.
name|getExpression
argument_list|()
decl_stmt|;
if|if
condition|(
name|expression
operator|instanceof
name|Predicate
condition|)
block|{
name|predicate
operator|=
operator|(
name|Predicate
operator|)
name|expression
expr_stmt|;
block|}
block|}
if|if
condition|(
name|predicate
operator|instanceof
name|ExpressionDefinition
condition|)
block|{
return|return
operator|(
name|ExpressionDefinition
operator|)
name|predicate
return|;
block|}
return|return
operator|new
name|ExpressionDefinition
argument_list|(
name|predicate
argument_list|)
return|;
block|}
block|}
end_class

end_unit

