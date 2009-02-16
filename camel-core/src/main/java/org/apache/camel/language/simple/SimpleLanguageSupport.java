begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language.simple
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
operator|.
name|simple
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|builder
operator|.
name|PredicateBuilder
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
name|impl
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|language
operator|.
name|simple
operator|.
name|SimpleLangaugeOperator
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * Abstract base class for Simple languages.  */
end_comment

begin_class
DECL|class|SimpleLanguageSupport
specifier|public
specifier|abstract
class|class
name|SimpleLanguageSupport
implements|implements
name|Language
block|{
DECL|field|PATTERN
specifier|protected
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"^\\$\\{(.+)\\}\\s+(==|>|>=|<|<=|!=|contains|regex|in)\\s+(.+)$"
argument_list|)
decl_stmt|;
DECL|field|log
specifier|protected
specifier|final
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
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
name|PredicateBuilder
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
DECL|method|createExpression (String expression)
specifier|public
name|Expression
name|createExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|Matcher
name|matcher
init|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|matches
argument_list|()
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Expression is evaluated as operator expression: "
operator|+
name|expression
argument_list|)
expr_stmt|;
block|}
return|return
name|createOperatorExpression
argument_list|(
name|matcher
argument_list|,
name|expression
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|expression
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|)
operator|>=
literal|0
condition|)
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Expression is evaluated as complex expression: "
operator|+
name|expression
argument_list|)
expr_stmt|;
block|}
return|return
name|createComplexConcatExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Expression is evaluated as simple expression: "
operator|+
name|expression
argument_list|)
expr_stmt|;
block|}
return|return
name|createSimpleExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
block|}
DECL|method|createOperatorExpression (final Matcher matcher, final String expression)
specifier|private
name|Expression
name|createOperatorExpression
parameter_list|(
specifier|final
name|Matcher
name|matcher
parameter_list|,
specifier|final
name|String
name|expression
parameter_list|)
block|{
specifier|final
name|Expression
name|left
init|=
name|createSimpleExpression
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|SimpleLangaugeOperator
name|operator
init|=
name|asOperator
argument_list|(
name|matcher
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
comment|// the right hand side expression can either be a constant expression wiht ' '
comment|// or another simple expression using ${ } placeholders
name|String
name|text
init|=
name|matcher
operator|.
name|group
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|final
name|Expression
name|right
decl_stmt|;
specifier|final
name|Expression
name|rightConverted
decl_stmt|;
comment|// special null handling
if|if
condition|(
literal|"null"
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
name|right
operator|=
name|createConstantExpression
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|rightConverted
operator|=
name|right
expr_stmt|;
block|}
else|else
block|{
comment|// text can either be a constant enclosed by ' ' or another expression using ${ } placeholders
name|String
name|constant
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|text
argument_list|,
literal|"'"
argument_list|,
literal|"'"
argument_list|)
decl_stmt|;
if|if
condition|(
name|constant
operator|==
literal|null
condition|)
block|{
comment|// if no ' ' around then fallback to the text itself
name|constant
operator|=
name|text
expr_stmt|;
block|}
name|String
name|simple
init|=
name|ObjectHelper
operator|.
name|between
argument_list|(
name|text
argument_list|,
literal|"${"
argument_list|,
literal|"}"
argument_list|)
decl_stmt|;
name|right
operator|=
name|simple
operator|!=
literal|null
condition|?
name|createSimpleExpression
argument_list|(
name|simple
argument_list|)
else|:
name|createConstantExpression
argument_list|(
name|constant
argument_list|)
expr_stmt|;
comment|// to support numeric comparions using> and< operators we must convert the right hand side
comment|// to the same type as the left
name|rightConverted
operator|=
name|ExpressionBuilder
operator|.
name|convertTo
argument_list|(
name|right
argument_list|,
name|left
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|ExpressionAdapter
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|evaluate
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Predicate
name|predicate
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|operator
operator|==
name|EQ
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isEqualTo
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GT
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isGreaterThan
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|GTE
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isGreaterThanOrEqualTo
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LT
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isLessThan
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|LTE
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isLessThanOrEqualTo
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|NOT
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|isNotEqualTo
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|CONTAINS
condition|)
block|{
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|contains
argument_list|(
name|left
argument_list|,
name|rightConverted
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|REGEX
condition|)
block|{
comment|// reg ex should use String pattern, so we evalute the right hand side as a String
name|predicate
operator|=
name|PredicateBuilder
operator|.
name|regex
argument_list|(
name|left
argument_list|,
name|right
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|operator
operator|==
name|IN
condition|)
block|{
comment|// okay the in operator is a bit more complex as we need to build a list of values
comment|// from the right handside expression.
comment|// each element on the right handside must be separated by comma (default for create iterator)
name|Iterator
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|right
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|values
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// then reuse value builder to create the in predicate with the list of values
name|ValueBuilder
name|vb
init|=
operator|new
name|ValueBuilder
argument_list|(
name|left
argument_list|)
decl_stmt|;
name|predicate
operator|=
name|vb
operator|.
name|in
argument_list|(
name|values
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|predicate
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported operator: "
operator|+
name|operator
operator|+
literal|" for expression: "
operator|+
name|expression
argument_list|)
throw|;
block|}
return|return
name|predicate
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|left
operator|+
literal|" "
operator|+
name|operator
operator|+
literal|" "
operator|+
name|right
return|;
block|}
block|}
return|;
block|}
DECL|method|createComplexConcatExpression (String expression)
specifier|protected
name|Expression
name|createComplexConcatExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|List
argument_list|<
name|Expression
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Expression
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|pivot
init|=
literal|0
decl_stmt|;
name|int
name|size
init|=
name|expression
operator|.
name|length
argument_list|()
decl_stmt|;
while|while
condition|(
name|pivot
operator|<
name|size
condition|)
block|{
name|int
name|idx
init|=
name|expression
operator|.
name|indexOf
argument_list|(
literal|"${"
argument_list|,
name|pivot
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|<
literal|0
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|createConstantExpression
argument_list|(
name|expression
argument_list|,
name|pivot
argument_list|,
name|size
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
else|else
block|{
if|if
condition|(
name|pivot
operator|<
name|idx
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
name|createConstantExpression
argument_list|(
name|expression
argument_list|,
name|pivot
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|pivot
operator|=
name|idx
operator|+
literal|2
expr_stmt|;
name|int
name|endIdx
init|=
name|expression
operator|.
name|indexOf
argument_list|(
literal|"}"
argument_list|,
name|pivot
argument_list|)
decl_stmt|;
if|if
condition|(
name|endIdx
operator|<
literal|0
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Expecting } but found end of string for simple expression: "
operator|+
name|expression
argument_list|)
throw|;
block|}
name|String
name|simpleText
init|=
name|expression
operator|.
name|substring
argument_list|(
name|pivot
argument_list|,
name|endIdx
argument_list|)
decl_stmt|;
name|Expression
name|simpleExpression
init|=
name|createSimpleExpression
argument_list|(
name|simpleText
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
name|simpleExpression
argument_list|)
expr_stmt|;
name|pivot
operator|=
name|endIdx
operator|+
literal|1
expr_stmt|;
block|}
block|}
return|return
name|ExpressionBuilder
operator|.
name|concatExpression
argument_list|(
name|results
argument_list|,
name|expression
argument_list|)
return|;
block|}
DECL|method|createConstantExpression (String expression, int start, int end)
specifier|protected
name|Expression
name|createConstantExpression
parameter_list|(
name|String
name|expression
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|end
parameter_list|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|expression
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConstantExpression (String expression)
specifier|protected
name|Expression
name|createConstantExpression
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|constantExpression
argument_list|(
name|expression
argument_list|)
return|;
block|}
comment|/**      * Creates the simple expression based on the extracted content from the ${ } place holders      *      * @param expression  the content between ${ and }      * @return the expression      */
DECL|method|createSimpleExpression (String expression)
specifier|protected
specifier|abstract
name|Expression
name|createSimpleExpression
parameter_list|(
name|String
name|expression
parameter_list|)
function_decl|;
DECL|method|ifStartsWithReturnRemainder (String prefix, String text)
specifier|protected
name|String
name|ifStartsWithReturnRemainder
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|text
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|String
name|remainder
init|=
name|text
operator|.
name|substring
argument_list|(
name|prefix
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|remainder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
return|return
name|remainder
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

