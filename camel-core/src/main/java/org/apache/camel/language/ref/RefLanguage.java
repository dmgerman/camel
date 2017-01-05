begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.ref
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|ref
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
name|Exchange
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
name|IsSingleton
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
name|ExpressionBuilder
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
name|Language
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
name|ExpressionAdapter
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
name|ExpressionToPredicateAdapter
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
name|PredicateToExpressionAdapter
import|;
end_import

begin_comment
comment|/**  * A language for referred expressions or predicates.  */
end_comment

begin_class
DECL|class|RefLanguage
specifier|public
class|class
name|RefLanguage
implements|implements
name|Language
implements|,
name|IsSingleton
block|{
DECL|method|ref (Object value)
specifier|public
specifier|static
name|Expression
name|ref
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
name|String
name|ref
init|=
name|value
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
name|ExpressionBuilder
operator|.
name|refExpression
argument_list|(
name|ref
argument_list|)
return|;
block|}
DECL|method|createPredicate (String expression)
specifier|public
name|Predicate
name|createPredicate
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionToPredicateAdapter
operator|.
name|toPredicate
argument_list|(
name|createExpression
argument_list|(
name|expression
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createExpression (final String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
specifier|final
name|String
name|expression
parameter_list|)
block|{
specifier|final
name|Expression
name|exp
init|=
name|RefLanguage
operator|.
name|ref
argument_list|(
name|expression
argument_list|)
decl_stmt|;
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Expression
name|target
init|=
literal|null
decl_stmt|;
name|Object
name|lookup
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// must favor expression over predicate
if|if
condition|(
name|lookup
operator|!=
literal|null
operator|&&
name|lookup
operator|instanceof
name|Expression
condition|)
block|{
name|target
operator|=
operator|(
name|Expression
operator|)
name|lookup
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lookup
operator|!=
literal|null
operator|&&
name|lookup
operator|instanceof
name|Predicate
condition|)
block|{
name|target
operator|=
name|PredicateToExpressionAdapter
operator|.
name|toExpression
argument_list|(
operator|(
name|Predicate
operator|)
name|lookup
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|target
operator|!=
literal|null
condition|)
block|{
return|return
name|target
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find expression or predicate in registry with ref: "
operator|+
name|expression
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|exp
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

